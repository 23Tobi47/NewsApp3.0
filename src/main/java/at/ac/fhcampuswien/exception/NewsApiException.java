package at.ac.fhcampuswien.exception;

public class NewsApiException extends Exception {

    public NewsApiException(String errorMessage) {
        super(errorMessage);
    }
}