# Lokales Deployment der Applikation

**Autor**: [Manuel Fellner](mfellner@student.tgm.ac.at)

**Version**: 15.03.2025

Um das Projekt lokal zu starten, benötigt man die folgenden Komponenten:

- Java 17
- Maven
- Docker & Docker Compose (Spring Boot übernimmt das Deployment schon automatisch! Docker muss nur installiert sein and you're ready to go!)

Und, wenn man es besonders leicht haben möchte:
- IntelliJ als IDE (das übernimmt das meiste für dich, inkl. dependencies und datenbank)

## 1. Application.properties File

Das `application.properties` File: Dieses File enthält viele wichtige Variablen, welche in der Applikation verwendet werden.
Im `/src/main/resources` Folder liegt bereits ein vorgefertigtes `application.properties.example` File. Dieses muss lediglich auf `application.properties` umbenannt werden. Wichtig ist hier, dass
die darin enthaltenen MariaDB Zugangsdaten mit den Daten im `compose.yaml` File im Project Root übereinstimmen!

Am Ende könnte das `application.properties` File zum Beispiel so aussehen:

```properties
spring.application.name=equilibria-sharing

# MariaDB connection properties, example data
spring.datasource.url=jdbc:mariadb://localhost:3306/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret

# JDBC driver for MariaDB
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# Hibernate properties (optional, for JPA usage)
spring.jpa.hibernate.ddl-auto=update

spring.jpa.database-platform=org.hibernate.dialect.MariaDBDialect
server.servlet.session.timeout=30m

# Example registration code
employeeRegistrationCode=SPECIAL123

# Example JWT secret key
secretJwtKey=fidjsfoidsjsfdsdsddsfjoisjoi3i92i2109i09i)=)§=)"!§UJINDSKALFNIDAOF!UJ§)"!=§UJ"!DIOSA
```

**ACHTUNG!** Hier ist es wichtig, dass die `secretJwtKey` Variable lang und sicher ist! Die JWT JWA Specification setzt hier einen Minimumwert von >= 256 Bits. Werte wie `secret` oder `key` führen zu einem nicht funktionierenden Programm! 

Und das `compose.yaml` File folgendermaßen (aktuelle Version nur MariaDB):

```yaml
services:
  mariadb:
    image: 'mariadb:latest'
    environment:
      - 'MARIADB_DATABASE=mydatabase'
      - 'MARIADB_PASSWORD=secret'
      - 'MARIADB_ROOT_PASSWORD=verysecret'
      - 'MARIADB_USER=myuser'
    ports:
      - '3306:3306'
```

## 2. Start des Projekts

Als nächstes sollte man das Projekt mit dem Ausführen der `src/main/java/api/equilibria_sharing/EquilibriaSharingApplication.java` Datei starten können. Die Datenbank sollte sich automatisch per Docker aufsetzen und eine Verbindung aufbauen.


Alternativ kann das Projekt auch mit Maven mittels `maven spring-boot:run` gestartet werden.

## 3. Probleme beim aufsetzen?

- Ins [Troubleshooting File](Troubleshooting.md) schauen
- Github Issue erstellen
