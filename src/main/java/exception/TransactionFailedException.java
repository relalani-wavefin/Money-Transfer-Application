package exception;

public class TransactionFailedException extends Exception {
    static final long serialVersionUID = -3387516993334229949L;

    public TransactionFailedException(String message) {
        super(message);
    }
}
