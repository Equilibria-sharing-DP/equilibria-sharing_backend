package api.equilibria_sharing.exceptions;

/**
 * ConflictException - Is thrown when a conflict (e.g. duplicated files, ...) happens
 *
 * @author Manuel Fellner
 * @version 02.03.2025
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
