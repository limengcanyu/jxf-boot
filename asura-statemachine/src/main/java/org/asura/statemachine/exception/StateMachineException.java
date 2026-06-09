package org.asura.statemachine.exception;

public class StateMachineException extends RuntimeException {

    private final String errorCode;

    public StateMachineException(String message) {
        super(message);
        this.errorCode = "STATEMACHINE_ERROR";
    }

    public StateMachineException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public StateMachineException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "STATEMACHINE_ERROR";
    }

    public StateMachineException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}