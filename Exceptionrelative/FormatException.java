package Exceptionrelative;

public class FormatException extends Exception{
    private final String errorCode;

    public FormatException(String message,String errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void logException() {
        System.err.println("ErrorCode: " + errorCode + ", ErrorMessage: " + getMessage());

    }
}
