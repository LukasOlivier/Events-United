package be.howest.ti.mars.logic.exceptions;

public class RepositoryException extends RuntimeException {

    public static final long serialVersionUID = 10000;

    public RepositoryException(String msg) {
        super(msg);
    }

}
