# Troubleshooting

**Autor**: [Manuel Fellner](mfellner@student.tgm.ac.at)

**Version**: 15.03.20250

In diesem File werden ein paar Fehler un die jeweiligen Lösungen dazu dokumentiert.

## 1. Error - Could not resolve placeholder 'secretJwtKey' in value "${secretJwtKey}" oder Error creating bean with name 'jwtService'

Hier ist das Problem, dass die `secretJwtKey` Variable im `application.properties` File nicht gesetzt wurde.

**How to fix**:

Im `application.properties` muss die `secretJwtKey` Variable gesetzt werden:

```properties
[...]
secretJwtKey=fidjsfoidsjsfdsdsddsfjoisjoi3i92i2109i09i)=)§=)"!§UJINDSKALFNIDAOF!UJ§)"!=§UJ"!DIOSA
```

## 2. Unexpected Error (500) beim Login

Falls beim Login einfach ein `Unexpected Error - 500` auftritt, könnte dies ebenso auf die `secretJwtKey` Variable im `application.properties` file verweisen.

Diese muss SICHER gesetzt werden - Werte wie `secret` oder `key` reichen nicht aus und führen zu genau diesem Fehler.

Falls Debugging durchgeführt wird, könnte dies im AuthController bei der login Methode passieren.

Falls die Fehlernachricht die folgende ist:

```
io.jsonwebtoken.security.WeakKeyException: The specified key byte array is 32 bits which is not secure enough for any JWT HMAC-SHA algorithm.  The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a size >= 256 bits (the key size must be greater than or equal to the hash output size).  Consider using the Jwts.SIG.HS256.key() builder (or HS384.key() or HS512.key()) to create a key guaranteed to be secure enough for your preferred HMAC-SHA algorithm.  See https://tools.ietf.org/html/rfc7518#section-3.2 for more information.
```

hat sich der Verdacht bestätigt.

**How to fix**:
Im `application.properties` muss die `secretJwtKey` Variable SICHER (>= 256 bits) gesetzt werden:

```properties
[...]
secretJwtKey=fidjsfoidsjsfdsdsddsfjoisjoi3i92i2109i09i)=)§=)"!§UJINDSKALFNIDAOF!UJ§)"!=§UJ"!DIOSA
```

## 3. Errors regarding Datenbank

Falls der Verdacht auftritt, dass irgendein Fehler mit der Datenbank auftritt, könnte es sein, dass sich die Spring boot App nicht gescheit mit der DB verbunden hat.

**How To Fix**:

Sind die Zugangsdaten für die Datenbank, welche im `compose.yaml` File gesetzt werden, auch 1:1 so im `application.properties`  File vorhanden? Wenn nicht, dann übernehmen!