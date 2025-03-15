### API Dokumentation - BookingController & AuthController

**Autor**: [Manuel Fellner](mfellner@student.tgm.ac.at)

**Version**: 15.03.2025

## Allgemeine Datenstruktur

Die Allgemeine Datenstruktur lässt sich anhand des folgenden UMl-Diagrams entnehmen:

![](equilibria-uml.drawio.png)

## Authentifizierung in der Anwendung

Die Anwendung verwendet eine Token-basierte Authentifizierung mittels JWT (JSON Web Tokens). Mitarbeiter müssen sich anmelden, um geschützte Endpunkte zu nutzen. Bei erfolgreicher Anmeldung wird ein JWT-Token zurückgegeben, der bei nachfolgenden Anfragen im `Authorization`-Header mitgesendet werden muss.

**Token-Format:**
```
Authorization: Bearer <jwt-token>
```

### AuthController

**Basis-URL:** `/auth`

#### 1. Mitarbeiter-Registrierung

- **Methode:** `POST`
- **URL:** `/api/v1/auth/register`
- **Beispiel-URL**: `http://localhost:8080/api/v1/auth/register`
- **Beschreibung:**
  Registriert einen neuen Mitarbeiter. Erfordert einen `uniqueCode`, der aus der Datei `application.properties` geladen wird (Attribut: `employeeRegistrationCode`).
- **Request Body:**
```json
{
  "username": "neuerMitarbeiter",
  "password": "starkesPasswort",
  "uniqueCode": "SPECIAL123"
}
```
- **Response:**
    - **Status:** `200 OK`
    - **Body:** Meldung über die erfolgreiche Registrierung.


#### 2. Mitarbeiter-Login

- **Methode:** `POST`
- **URL:** `/api/v1/auth/login`
- **Beispiel-URL**: `http://localhost:8080/api/v1/auth/login`
- **Beschreibung:**
  Authentifiziert einen Mitarbeiter mit Benutzername und Passwort. Gibt bei Erfolg einen JWT-Token zurück.
- **Request Body:**
```json
{
  "username": "mitarbeiter1",
  "password": "passwort123"
}
```
- **Response:**
    - **Status:** `200 OK`
    - **Body:** Der generierte JWT-Token. Dieser ist im Anschluss für den Zugriff auf geschützte API Endpunkte notwendig!

---

### Ablauf der Authentifizierung

1. Der Mitarbeiter sendet einen `POST`-Request an `/auth/login` mit gültigen Zugangsdaten.
2. Die Anwendung prüft die Zugangsdaten:
    - Bei Erfolg wird ein JWT-Token generiert und zurückgegeben.
    - Jeder Login wird im System protokolliert.
3. Der Mitarbeiter fügt den Token bei allen weiteren Anfragen in den `Authorization`-Header ein.
4. Bei jedem Zugriff auf einen geschützten Endpunkt:
    - Der JWT-Token wird validiert.
    - Der Zugriff wird protokolliert (Endpoint, Methode, Zeitstempel).

---

### API Dokumentation - BookingController

Der **BookingController** stellt Endpunkte zur Verwaltung von Buchungen (Bookings) bereit. Hierüber können Buchungen erstellt, abgerufen, aktualisiert und gelöscht werden.

**Basis-URL:** `/api/v1/bookings`

## Endpunkte

### 1. Buchung erstellen

- **Methode:** `POST`
- **URL:** `/api/v1/bookings`
- **Beispiel-URL**: `http://localhost:8080/api/v1/bookings`
- **Beschreibung:**  
  Erstellt eine neue Buchung anhand der übermittelten Formulardaten vom Kunden. Dieser Endpunkt ist ohne Authentifizierung zugänglich.
- **Request Body:**
```json
{
  "accommodationId": 123,
  "mainTraveler": {
    "firstName": "Max",
    "lastName": "Mustermann",
    "gender": "M",
    "birthDate": "1990-01-01",
    "street": "Musterstraße",
    "city": "Musterstadt",
    "country": "Deutschland",
    "houseNumber": "1",
    "postalCode": "12345",
    "addressAdditional": "Etage 2",
    "travelDocumentType": "Passport",
    "documentNr": "A1234567",
    "issueDate": "2020-01-01",
    "expiryDate": "2030-01-01",
    "issuingAuthority": "Musteramt"
  },
  "checkIn": "2025-05-01",
  "expectedCheckOut": "2025-05-10",
  "additionalGuests": [
    {
      "firstName": "Erika",
      "lastName": "Mustermann",
      "birthDate": "1992-02-02"
    }
  ]
}
```
- **Response:**
    - **Erfolgreich:**
        - **Status:** `201 Created`
        - **Body:** JSON-Darstellung des erstellten Buchungsobjekts.
    - **Fehler:**
        - `404 Not Found`, falls die angegebene Unterkunft nicht existiert.



### 2. Buchung nach ID abrufen

- **Methode:** `GET`
- **URL:** `/api/v1/bookings/{id}`
- **Beispiel-URL**: `http://localhost:8080/api/v1/bookings/1`
- **Beschreibung:**
  Ruft eine spezifische Buchung anhand der ID ab. Authentifizierung erforderlich.
- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```

- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`
        - **Body**: JSON der jeweiligen Buchung
    - **Fehler**:
        - **Status**: `404 Not Found`, falls die Buchung mit der angegebenen ID nicht gefunden wurde
        - **Status**: `401 Unauthorized`, falls die Anfrage ohne Angabe des Tokens im Header durchgeführt wurde


### 3. Alle Buchungen abrufen

- **Methode:** `GET`
- **URL:** `/api/v1/bookings`
- **Beispiel-URL**: `http://localhost:8080/api/v1/bookings`
- **Beschreibung:**
  Gibt eine Liste aller Buchungen zurück. Authentifizierung erforderlich.

- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```

- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`
        - **Body**: JSON mit allen Buchungen
    - **Fehler**:
        - **Status**: `401 Unauthorized`, falls die Anfrage ohne Angabe des Tokens im Header durchgeführt wurde

### 4. Alle Buchungen löschen

- **Methode:** `DELETE`
- **URL:** `/api/v1/bookings`
- **Beispiel-URL**: `http://localhost:8080/api/v1/bookings`
- **Beschreibung:**
  Löscht sämtliche Buchungen in der Datenbank. Authentifizierung erforderlich.
- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```

- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`
    - **Fehler**:
        - **Status**: `401 Unauthorized`, falls die Anfrage ohne Angabe des Tokens im Header durchgeführt wurde


### 5. Buchung nach ID löschen

- **Methode:** `DELETE`
- **URL:** `/api/v1/bookings/{id}`
- **Beispiel-URL**: `http://localhost:8080/api/v1/bookings/1`
- **Beschreibung:**
  Löscht eine spezifische Buchung anhand der ID. Authentifizierung erforderlich.
- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```

- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`
    - **Fehler**:
        - **Status**: `404 Not Found`, falls die die zu löschene Buchung nicht gefunden werden konnte
        - **Status**: `401 Unauthorized`, falls die Anfrage ohne Angabe des Tokens im Header durchgeführt wurde

### 6. Buchung aktualisieren

- **Methode:** `PUT`
- **URL:** `/api/v1/bookings/{id}`
- **Beispiel-URL**: `http://localhost:8080/api/v1/bookings/1`
- **Beschreibung:**
  Aktualisiert eine bestehende Buchung mit allen Daten. Authentifizierung erforderlich.

- **Request-Body**:

```json
{
  "accommodationId": 123,
  "mainTraveler": {
    "firstName": "Max",
    "lastName": "Mustermann",
    "gender": "M",
    "birthDate": "1990-01-01",
    "street": "Musterstraße",
    "city": "Musterstadt",
    "country": "Deutschland",
    "houseNumber": "1",
    "postalCode": "12345",
    "addressAdditional": "Etage 2",
    "travelDocumentType": "Passport",
    "documentNr": "A1234567",
    "issueDate": "2020-01-01",
    "expiryDate": "2030-01-01",
    "issuingAuthority": "Musteramt"
  },
  "checkIn": "2025-05-01",
  "expectedCheckOut": "2025-05-10",
  "additionalGuests": [
    {
      "firstName": "Erika",
      "lastName": "Mustermann",
      "birthDate": "1992-02-02"
    }
  ]
}
```
- **Response:**
    - **Erfolgreich:**
        - **Status:** `201 Created`
        - **Body:** JSON-Darstellung des aktualisierten Buchungsobjekts.
    - **Fehler:**
        - `404 Not Found`, falls die angegebene Unterkunft nicht existiert.


### API Dokumentation - AccommodationController

- **Basis-URL**: `/api/v1/accommodations`
- Der AccommodationController stellt Endpunkte zur Verwaltung von Unterkünften bereit. Mitarbeiter müssen authentifiziert sein, um diese Endpunkte nutzen zu können.

#### 1. Unterkunft erstellen

- **Methode**: `POST`

- **URL**: `/api/v1/accommodations`
- **Beispiel-URL**: `http://localhost:8080/api/v1/accommodations`
- **Beschreibung**: Erstellt eine neue Unterkunft mit den übergebenen Daten. Hierfür ist die Authentifizierung erforderlich.

- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```

- **Request Body**:

```json
{
"name": "Hotel Beispiel",
"type": "Hotel",
"description": "Ein schönes Hotel im Stadtzentrum.",
"maxGuests": 100,
"pricePerNight": 120.50,
"pictureUrls": ["https://example.com/image1.jpg"],
"street": "Hauptstraße",
"houseNumber": "10",
"postalCode": "1010",
"city": "Wien",
"country": "Österreich",
"addressAdditional": "Etage 3"
}
```

- **Response**:

    - **Erfolgreich**:
        - **Status**: `201 Created`
        - **Body**: JSON-Darstellung der erstellten Unterkunft
    - **Fehler**:
        - **Status**: `409 Conflict`, falls eine Unterkunft mit demselben Namen bereits existiert

#### 2. Alle Unterkünfte abrufen

- **Methode**: `GET`

- **URL**: `/api/v1/accommodations`
- **Beispiel-URL**: `http://localhost:8080/api/v1/accommodations`

- **Beschreibung**: Gibt eine Liste aller Unterkünfte zurück. Alle Benutzer können diese Schnittstelle aufrufen.

- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`
        - **Body**: JSON Liste aller Unterkünfte

#### 3. Unterkunft nach ID abrufen

- **Methode**: `GET`

- **URL**: `/api/v1/accommodations/{id}`
- **Beispiel-URL**: `http://localhost:8080/api/v1/accommodations/1`
- **Beschreibung**: Ruft eine spezifische Unterkunft anhand der ID ab. Alle Benutzer können diese Aktion durchführen.

- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`
        - **Body**: JSON-Darstellung der Unterkunft

    - **Fehler**:
        - **Status**: `404 Not Found`, falls die Unterkunft nicht existiert

#### 4. Alle Unterkünfte löschen

- **Methode**: `DELETE`

- **URL**: `/api/v1/accommodations`
- **Beispiel-URL**: `http://localhost:8080/api/v1/accommodations`
- **Beschreibung**: Löscht alle Unterkünfte aus der Datenbank. Authentifizierung ist notwendig.
- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```
- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`

#### 5. Unterkunft nach ID löschen

- **Methode**: `DELETE`

- **URL**: `/api/v1/accommodations/{id}`
- **Beispiel-URL**: `http://localhost:8080/api/v1/accommodations/1`
- **Beschreibung**: Löscht eine spezifische Unterkunft anhand der ID. Authentifizierung ist hierfür erforderlich.
- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```
- **Response**:
    - **Erfolgreich**:
        - **Status**: `200 OK`

    - **Fehler**:
    - **Status**: `404 Not Found`, falls die Unterkunft nicht existiert

#### 6. Unterkunft aktualisieren

- **Methode**: `PUT`

- **URL**: `/api/v1/accommodations/{id}`
- **Beispiel-URL**: `http://localhost:8080/api/v1/accommodation/1`
- **Beschreibung**: Aktualisiert eine bestehende Unterkunft mit den übergebenen Daten. Authentifizierug ist hierfür notwendig.
- **Request Header**:
  ```
    Authorization: Bearer <AUTH-TOKEN-RECEIVED-AT-LOGIN>
  ```
- **Zum Beispiel**:

  ```
    Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZmVsbG5lcjJAc3R1ZGVudC50Z20uYWMuYXQiLCJpYXQiOjE3NDIwMzIxODcsImV4cCI6MTc0MjA2ODE4N30.IQQ2Mfg_XSanR2PleTpsb3VxnPGrLaJIO2Tnr0wQLls
  ```
- **Request Body**:

```json
{
"name": "Hotel Beispiel Aktualisiert",
"type": "Hotel",
"description": "Ein renoviertes Hotel im Stadtzentrum.",
"maxGuests": 120,
"pricePerNight": 140.75,
"pictureUrls": ["https://example.com/image2.jpg"],
"street": "Neue Hauptstraße",
"houseNumber": "15",
"postalCode": "1020",
"city": "Wien",
"country": "Österreich",
"addressAdditional": "Etage 4"
}
```

- **Response**:
    - *+Erfolgreich**:

        - **Status**: `200 OK`

        - **Body**: JSON-Darstellung der aktualisierten Unterkunft

    - **Fehler**:
        - **Status**: `404 Not Found`, falls die Unterkunft nicht existiert

