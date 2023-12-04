package be.howest.ti.mars.web.exceptions;

public class MalformedRequestException extends RuntimeException {
    public static final long serialVersionUID = 10000;

    public MalformedRequestException(String msg) {
        super(msg);
    }

}
