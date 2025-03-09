package api.equilibria_sharing.exceptions;

/**
 * ProtocolGenerationException - Is thrown, when a booking protocol could not be generated
 *
 * @author Manuel Fellner
 * @version 09.03.2025
 */
public class ProtocolGenerationException extends RuntimeException {
    public ProtocolGenerationException(String message) {
        super(message);
    }
}
