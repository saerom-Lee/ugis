package ugis.exception;

public class RestApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
