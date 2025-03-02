package api.equilibria_sharing.exceptions;

/**
 * UnauthorizedException - Is thrown, when a resource that requires authorization is accessed without auth token
 *
 * @author Manuel Fellner
 * @version 02.03.2025
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}