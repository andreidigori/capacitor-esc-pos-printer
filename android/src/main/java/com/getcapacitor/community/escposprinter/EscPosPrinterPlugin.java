package com.getcapacitor.community.escposprinter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResult;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import com.getcapacitor.community.escposprinter.printers.BasePrinter;
import com.getcapacitor.community.escposprinter.printers.BluetoothPrinter;
import com.getcapacitor.community.escposprinter.printers.exceptions.PrinterException;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("unused")
@CapacitorPlugin(
        name = "EscPosPrinter",
        permissions = {
                /*@Permission(
                        alias = "bluetooth-legacy",
                        strings = {
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN
                        }
                ),*/
                @Permission(
                        alias = "bluetooth",
                        strings = {
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN
                        }
                )
        }
)
public class EscPosPrinterPlugin extends Plugin {
    private BluetoothAdapter bluetoothAdapter;
    private HashMap<String, BasePrinter> printersMap = new HashMap<>();

    @SuppressWarnings("unused")
    @PluginMethod
    public void requestBluetoothEnable(PluginCall call) {
        if (!assertBluetoothAdapter(call)) {
            return;
        }
        if (bluetoothAdapter.isEnabled()) {
            var data = new JSObject();
            data.put("value", true);
            call.resolve(data);
        } else {
            if (!assertBluetoothPermission(call)) {
                return;
            }
            var enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(call, enableBluetoothIntent, "requestBluetoothEnableResultCallback");
        }
    }

    @ActivityCallback
    private void requestBluetoothEnableResultCallback(PluginCall call, ActivityResult result) {
        if (call == null) {
            return;
        }
        var resultCode = result.getResultCode();
        var data = new JSObject();
        data.put("value", resultCode == Activity.RESULT_OK);
        call.resolve(data);
    }

    @SuppressWarnings("unused")
    @SuppressLint("MissingPermission")
    @PluginMethod
    public void getBluetoothPrinterDevices(PluginCall call) {
        if (!assertBluetoothAdapter(call) || !assertBluetoothEnabled(call) || !assertBluetoothPermission(call)) {
            return;
        }

        var devicesArray = new JSArray();
        var bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices != null) {
            for (var device : bondedDevices) {
                var bluetoothClass = device.getBluetoothClass();
                var majorClassType = bluetoothClass.getMajorDeviceClass();
                var deviceType = bluetoothClass.getDeviceClass();

                // From https://inthehand.github.io/html/T_InTheHand_Net_Bluetooth_DeviceClass.htm
                // 1664 - Imaging printer
                var isPrinterDevice = majorClassType == BluetoothClass.Device.Major.IMAGING && (deviceType == 1664 || deviceType == BluetoothClass.Device.Major.IMAGING);
                if (!isPrinterDevice) {
                    continue;
                }

                var uuidsArray = new JSArray();
                var servicesUuids = device.getUuids();
                if (servicesUuids != null) {
                    for (var uuid : servicesUuids) {
                        uuidsArray.put(uuid.toString());
                    }
                }

                var deviceObject = new JSObject();
                devicesArray.put(deviceObject);

                deviceObject.put("address", device.getAddress());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    deviceObject.put("alias", device.getAlias());
                }
                deviceObject.put("name", device.getName());
                deviceObject.put("bondState", device.getBondState());
                deviceObject.put("type", device.getType());
                deviceObject.put("uuids", uuidsArray);
            }
        }

        var data = new JSObject();
        data.put("devices", devicesArray);
        call.resolve(data);
    }

    @SuppressWarnings("unused")
    @PluginMethod
    public void createPrinter(PluginCall call) {
        var hashKey = UUID.randomUUID().toString();
        var address = call.getString("address");
        var connectionType = call.getString("connectionType", "bluetooth");

        BasePrinter printer;

        switch (connectionType) {
            case "bluetooth": {
                if (!assertBluetoothAdapter(call)) {
                    return;
                }

                // var useLowEnergy = call.getBoolean("le", true) && getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
                // var secure = call.getBoolean("secure", true);

                printer = new BluetoothPrinter(bluetoothAdapter, address);
                break;
            }
            default: {
                call.reject("Connection type not known.");
                return;
            }
        }

        printersMap.put(hashKey, printer);

        var data = new JSObject();
        data.put("value", hashKey);
        call.resolve(data);
    }

    @SuppressWarnings("unused")
    @PluginMethod
    public void disposePrinter(PluginCall call) {
        var hashKey = call.getString("hashKey");

        var hasPrinter = printersMap.containsKey(hashKey);
        if (hasPrinter) {
            printersMap.remove(hashKey);
        }

        var data = new JSObject();
        data.put("value", hasPrinter);
        call.resolve(data);
    }

    @SuppressWarnings("unused")
    @PluginMethod
    public void isPrinterConnected(PluginCall call) {
        var printer = getGuardedPrinterByHash(call);
        if (printer == null) {
            return;
        }

        var data = new JSObject();
        data.put("value", printer.isConnected());
        call.resolve(data);
    }

    @SuppressWarnings("unused")
    @PluginMethod
    public void connectPrinter(PluginCall call) {
        var printer = getGuardedPrinterByHash(call);
        if (printer == null) {
            return;
        }

        try {
            if (printer instanceof BluetoothPrinter && !assertBluetoothEnabled(call) || !assertBluetoothPermission(call)) {
                return;
            }

            printer.connect();

            call.resolve();
        } catch (PrinterException e) {
            rejectWithPrinterException(call, e);
        }
    }

    @SuppressWarnings("unused")
    @PluginMethod
    public void disconnectPrinter(PluginCall call) {
        var printer = getGuardedPrinterByHash(call);
        if (printer == null) {
            return;
        }

        printer.disconnect();

        call.resolve();
    }

    @SuppressWarnings("unused")
    @PluginMethod
    public void sendToPrinter(PluginCall call) {
        var printer = getGuardedPrinterByHash(call);
        if (printer == null) {
            return;
        }

        var waitingTime = call.getInt("waitingTime", 0);
        var data = call.getArray("data");

        byte[] bytesArray = new byte[data.length()];
        for (var i = 0; i < bytesArray.length; i++) {
            bytesArray[i] = (byte)data.optInt(i);
        }

        try {
            printer.send(bytesArray, waitingTime);

            call.resolve();
        } catch (PrinterException e) {
            rejectWithPrinterException(call, e);
        }
    }

    @SuppressWarnings("unused")
    @PluginMethod
    public void readFromPrinter(PluginCall call) {
        var printer = getGuardedPrinterByHash(call);
        if (printer == null) {
            return;
        }

        // TODO: read like https://github.com/AdoptOpenJDK/openjdk-jdk11/blob/master/src/java.base/share/classes/java/io/InputStream.java

        try {
            var bytes = printer.read();
            var bytesArray = new JSArray();
            for (var i = 0; i < bytes.length; i++) {
                try {
                    bytesArray.put(i, bytes[i]);
                } catch (JSONException e) {

                }
            }

            var data = new JSObject();
            data.put("value", bytesArray);
            call.resolve(data);
        } catch (PrinterException e) {
            rejectWithPrinterException(call, e);
        }
    }

    private boolean assertBluetoothAdapter(PluginCall call) {
        if (bluetoothAdapter == null) {
            var bluetoothAvailable = getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
            if (bluetoothAvailable) {
                var bluetoothManager = getContext().getSystemService(BluetoothManager.class);
                bluetoothAdapter = bluetoothManager.getAdapter();
            }
            if (bluetoothAdapter == null) {
                call.reject("Bluetooth is not available.");
                return false;
            }
        }
        return true;
    }

    private boolean assertBluetoothEnabled(PluginCall call) {
        if (!bluetoothAdapter.isEnabled()) {
            call.reject("Bluetooth is not enabled.");
            return false;
        }
        return true;
    }

    private boolean assertBluetoothPermission(PluginCall call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && getPermissionState("bluetooth") != PermissionState.GRANTED) {
            requestPermissionForAlias("bluetooth", call, "bluetoothPermissionCallback");
            return false;
        }
        return true;
    }

    @PermissionCallback
    private void bluetoothPermissionCallback(PluginCall call) {
        if (call == null) {
            return;
        }
        if (getPermissionState("bluetooth") == PermissionState.GRANTED) {
            var methodName = call.getMethodName();
            try {
                var method = this.getClass().getMethod(methodName, PluginCall.class);
                method.invoke(this, call);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

            }
        } else {
            call.reject("Bluetooth not granted.");
        }
    }

    private BasePrinter getGuardedPrinterByHash(PluginCall call) {
        var hashKey = call.getString("hashKey");
        var printer = printersMap.get(hashKey);
        if (printer == null) {
            call.reject("Printer with " + hashKey + " hash not found.");
            return null;
        }
        return printer;
    }

    private void rejectWithPrinterException(PluginCall call, PrinterException e) {
        var data = new JSObject();
        data.put("code", e.getErrorCode());
        call.reject(e.getMessage(), data);
    }
}
