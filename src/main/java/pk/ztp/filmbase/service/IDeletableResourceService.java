package pk.ztp.filmbase.service;

import org.springframework.security.access.AccessDeniedException;
import pk.ztp.filmbase.exception.ResourceNotFound;
import pk.ztp.filmbase.model.User;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IDeletableResourceService<T> {
    default void deleteResource(long resourceId, User user, Function<Long, Optional<T>> findResourceById,
                                Function<T, Long> getResourceOwnerId, Consumer<T> deleteResource) {
        T resource = findResourceById.apply(resourceId).orElseThrow(
                () -> new ResourceNotFound("Resource with id " + resourceId + " not found")
        );
        if (!Objects.equals(getResourceOwnerId.apply(resource), user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this resource.");
        }
        deleteResource.accept(resource);
    }
}
