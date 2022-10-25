package be.cocoding.training.spring.rest;

public class UserDeletionException extends RuntimeException {

    public UserDeletionException() {
    }

    public UserDeletionException(String message) {
        super(message);
    }
}
