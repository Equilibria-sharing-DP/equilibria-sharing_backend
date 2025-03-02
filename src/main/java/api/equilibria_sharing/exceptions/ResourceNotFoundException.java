package api.equilibria_sharing.exceptions;

/**
 * ResourceNotFoundException - Is thrown, when a resource is not found
 *
 * @author Manuel Fellner
 * @version 02.03.2025
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}