package api.equilibria_sharing.exceptions;

/**
 * BadRequestException - Is thrown, when a request was sent to the backend and could not be processed
 *
 * @author Manuel Fellner
 * @version 02.03.2025
 */

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}