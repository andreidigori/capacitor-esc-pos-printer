package com.getcapacitor.community.escposprinter.printers;

// https://developer.android.com/develop/connectivity/bluetooth

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import com.getcapacitor.community.escposprinter.printers.constants.PrinterErrorCode;
import com.getcapacitor.community.escposprinter.printers.exceptions.PrinterException;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class BluetoothPrinter extends BasePrinter {
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @SuppressLint("MissingPermission")
    public static UUID getSerialPortProfileOrFirstUuid(BluetoothDevice device) {
        var uuids = device.getUuids();
        if (uuids != null && uuids.length > 0) {
            if (Arrays.asList(uuids).contains(new ParcelUuid(SPP_UUID))) {
                return SPP_UUID;
            }
            return uuids[0].getUuid();
        }
        return SPP_UUID;
    }

    private BluetoothAdapter bluetoothAdapter;
    private String address;
    private BluetoothSocket socket;

    public BluetoothPrinter(BluetoothAdapter bluetoothAdapter, String address) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.address = address;
    }

    @Override
    public boolean isConnected() {
        return socket != null && socket.isConnected() && super.isConnected();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void connect() throws PrinterException {
        var bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        var uuid = getSerialPortProfileOrFirstUuid(bluetoothDevice);
        try {
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothAdapter.cancelDiscovery();
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();

            disconnect();

            throw new PrinterException(PrinterErrorCode.CONNECT, "Unable to connect to bluetooth device.");
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }
}
