package se.somath.publisher.excpetion;

public class TooManyFilesFoundException extends RuntimeException {
    public TooManyFilesFoundException(String message) {
        super(message);
    }
}
