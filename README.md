# Filmbase Application 🎞️

Projekt semestralny przygotowany na kurs "Zaawansowane Techniki Programowania".

## Zespół projektowy
- **Krzysztof Radzięta** - Backend
- **Daniel Warzecha** - Backend
- **Maksymilian Patoń** - Frontend
- **Jakub Goch** - Frontend

---

## Opis projektu

### Główne funkcjonalności:
1. **Rejestracja i logowanie użytkowników**
   - Bezstanowe uwierzytelnianie oparte na tokenach JWT z wykorzystaniem refresh tokenów.

2. **Przeglądanie filmów i interakcja**
   - Użytkownicy mogą przeglądać filmy, wystawiać im oceny oraz dodawać komentarze.
   - Tylko zalogowani użytkownicy mają możliwość interakcji z filmami.

3. **Dokumentacja API**
   - Dokumentacja Swagger UI dostępna pod adresem: [http://localhost:8080/api/swagger-ui/index.html#/](http://localhost:8080/api/swagger-ui/index.html#/).

-  **Założenie projektowe**
   - Filmy są dodawane bezpośrednio przez administratora przy użyciu zapytań SQL.
   - W katalogu src\main\resources znjduje się skrypt SQL insert_sample_films.sql prostego utworzenia kilku filmów w bazie.
---

## Instrukcja uruchomienia

### Wymagania wstępne
- Zainstalowany Docker oraz Docker Compose.
- JDK 17, jeśli konieczne jest ponowne zbudowanie pliku `.jar`.

---

### Kroki uruchomienia

1. **Przygotowanie środowiska**
   - W pliku `example.docker-compose` uzupełnij poniższe zmienne środowiskowe:
      - `SPRING_DATASOURCE_USERNAME`: Nazwa użytkownika bazy danych PostgreSQL.
      - `SPRING_DATASOURCE_PASSWORD`: Hasło użytkownika bazy danych PostgreSQL.
      - `POSTGRES_USER`: Taka sama wartość jak `SPRING_DATASOURCE_USERNAME`.
      - `POSTGRES_PASSWORD`: Taka sama wartość jak `SPRING_DATASOURCE_PASSWORD`.
   - W katalogu src/main/resources utwórz i uzupełnij plik application.properties.
     Przykładowy plik wzór znajduję się w tej samej lokalizacji - example.application.properties.
     ! Pamiętaj - wartości zmiennych w application.properties muszą odpowiadać tym z example.docker-compose.

2. **Zbudowanie aplikacji**
   - Upewnij się, że jesteś w katalogu głównym projektu.
   - Wykonaj następujące komendy:
      - Oczyszczenie projektu:
        ```bash
        ./mvnw clean
        ```
      - Zbudowanie aplikacji:
        ```bash
        ./mvnw package -DskipTests
        ```
     *(Dla systemu Windows użyj `mvnw.cmd` zamiast `./mvnw`)*
   - UWAGA - flaga -DskipTests jest wymagana ze względu na użycie w testach bazy inmemory, która do inicjalizacji wymaga struktury w finalnej bazie.
   - W katalogu `target` zostanie wygenerowany plik `.jar`, np. `filmbase-1.0.0.jar`.

3. **Zbudowanie obrazu Dockera**  
   W katalogu głównym projektu zbuduj obraz aplikacji:
   (jeśli korzystasz z własnego pliku docker.compose.yml - podmień nazwę na właściwy w README)
   ```bash
   docker-compose -f example.docker-compose.yml build
   ```

4. **Uruchomienie aplikacji**  
   Wystartuj aplikację przy użyciu Docker Compose:
   (jeśli korzystasz z własnego pliku docker.compose.yml - podmień nazwę na właściwy w README)
   ```bash
   docker-compose -f example.docker-compose.yml up
   ```

5. **Dostęp do aplikacji**
   - Swagger UI: [http://localhost:8080/api/swagger-ui/index.html#/](http://localhost:8080/api/swagger-ui/index.html#/).
   - API dostępne pod adresem `http://localhost:8080`.

6. **Kolekcja Postman**
   - W głównym katalogu projektu znajduje się plik `filmbase.postman_collection`.
   - Zaimportuj ten plik do Postmana, aby przetestować endpointy API.

---

## Struktura aplikacji

### Backend:
- **Uwierzytelnianie**: Bezstanowe JWT z obsługą refresh tokenów.
- **Funkcje dotyczące filmów**: Ocena i komentowanie filmów.
- **Role użytkowników**:
   - Zwykli użytkownicy mogą oceniać i komentować filmy.
   - Administrator zarządza filmami bezpośrednio przez SQL.

### Frontend:
- Stworzony do komunikacji z API backendowym w celu zapewnienia płynnej obsługi użytkowników.

### Baza danych:
- **PostgreSQL** używany jako magazyn danych.
- Serwis `db` w pliku `example.docker-compose` inicjalizuje bazę danych z podanymi poświadczeniami i schematem.

---

## Uwagi dla programistów
- Jeśli potrzebujesz zmodyfikować backend, upewnij się, że po wprowadzeniu zmian plik `.jar` zostanie przebudowany:
  ```bash
  mvn clean package
  ```
  Następnie zaktualizuj obraz Dockera.

- Plik konfiguracyjny aplikacji Spring znajduje się w `src/main/resources/application.properties`.

---
