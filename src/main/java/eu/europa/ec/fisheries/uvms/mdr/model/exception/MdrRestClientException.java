package eu.europa.ec.fisheries.uvms.mdr.model.exception;

public class MdrRestClientException extends Exception{

    private static final long serialVersionUID = 1L;

    public MdrRestClientException(String message) {
        super(message);
    }

    public MdrRestClientException(String s, Throwable e) {
        super(s, e);
    }
}
