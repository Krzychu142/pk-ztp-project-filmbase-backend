package pk.ztp.filmbase.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class KeyUtils {
    private final Environment env;
    private static final Logger log = LoggerFactory.getLogger(KeyUtils.class);

    @Value("${access-token.private}")
    private String accessTokenPrivateKeyPath;

    @Value("${access-token.public}")
    private String accessTokenPublicKeyPath;

    @Value("${refresh-token.private}")
    private String refreshTokenPrivateKeyPath;

    @Value("${refresh-token.public}")
    private String refreshTokenPublicKeyPath;

    private KeyPair _accessTokenKeyPair;
    private KeyPair _refreshTokenKeyPair;

    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(_accessTokenKeyPair)) {
            _accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
        }
        return _accessTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
      KeyPair keyPair;
      File publicKeyFile = new File(publicKeyPath);
      File privateKeyFile = new File(privateKeyPath);

      if (publicKeyFile.exists() && privateKeyFile.exists()) {
          try {
              KeyFactory keyFactory = KeyFactory.getInstance("RSA");

              Path publicPath = publicKeyFile.toPath();
              Path privatePath = privateKeyFile.toPath();

              byte[] publicKeyBytes = Files.readAllBytes(publicPath);
              EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
              PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

              byte[] privateKeyBytes = Files.readAllBytes(privatePath);
              PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
              PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

              keyPair = new KeyPair(publicKey, privateKey);
              return keyPair;
          } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
              throw new RuntimeException(e);
          }
      } else {
          if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            throw new RuntimeException("Public or Private Key file does not exist");
          }
      }
      File directory = new File("keys");
      if (!directory.exists()) {
          boolean created = directory.mkdirs();
          if(!created) {
              log.error("Failed to create keys directory");
          }
      }
      try {
          KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
          keyPairGenerator.initialize(2048);
          keyPair = keyPairGenerator.generateKeyPair();
          try (FileOutputStream fos =  new FileOutputStream((publicKeyFile))) {
              X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
              fos.write(keySpec.getEncoded());
          }
          try (FileOutputStream fos =  new FileOutputStream((privateKeyFile))) {
              PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
              fos.write(keySpec.getEncoded());
          }
      } catch (NoSuchAlgorithmException | IOException e) {
          throw new RuntimeException(e);
      }
      return keyPair;
    }

    private KeyPair getRefreshTokenKeyPair() {
        if (Objects.isNull(_refreshTokenKeyPair)) {
            _refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath);
        }
        return _refreshTokenKeyPair;
    }

    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    }

    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    }

    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    }
}
