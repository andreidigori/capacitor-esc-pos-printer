package com.getcapacitor.community.escposprinter.printers;

import android.util.Log;

import com.getcapacitor.community.escposprinter.printers.constants.PrinterErrorCode;
import com.getcapacitor.community.escposprinter.printers.exceptions.PrinterException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public abstract class BasePrinter {
    protected InputStream inputStream;
    protected OutputStream outputStream;

    public abstract void connect() throws PrinterException;

    public boolean isConnected() {
        return inputStream != null && outputStream != null;
    }

    public void disconnect() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = null;
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
    }

    public void send(byte[] data, int addWaitingTime) throws PrinterException {
        if(!this.isConnected()) {
            this.disconnect();

            throw new PrinterException(PrinterErrorCode.NOT_CONENCTED, "Printer not connected.");
        }
        try {
            this.outputStream.write(data);
            this.outputStream.flush();

            int waitingTime = addWaitingTime + data.length / 16;
            if(waitingTime > 0) {
                Thread.sleep(waitingTime);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

            throw new PrinterException(PrinterErrorCode.SEND, e.getMessage());
        }
    }

    public byte[] read() throws PrinterException {
        if(!this.isConnected()) {
            this.disconnect();

            throw new PrinterException(PrinterErrorCode.NOT_CONENCTED, "Printer not connected.");
        }
        try {
            // TODO: review again
            var availableLength = this.inputStream.available();
            if (availableLength > 0) {
                var buffer = new byte[availableLength];
                this.inputStream.read(buffer);
                return buffer;
            }
        } catch (IOException e) {
            e.printStackTrace();

            throw new PrinterException(PrinterErrorCode.READ, e.getMessage());
        }
        return new byte[0];
    }
}
