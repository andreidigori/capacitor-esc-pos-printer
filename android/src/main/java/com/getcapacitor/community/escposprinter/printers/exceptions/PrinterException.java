package com.getcapacitor.community.escposprinter.printers.exceptions;

public class PrinterException extends Exception {
    protected int errorCode;

    public PrinterException(int errorCode) {
        this(errorCode, "" + errorCode);
    }

    public PrinterException(int errorCode, String errorMessage) {
        super(errorMessage);

        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
