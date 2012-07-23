package se.somath.publisher.excpetion;

public class PublishException extends RuntimeException {
    public PublishException(Throwable throwable) {
        super(throwable);
    }
}
