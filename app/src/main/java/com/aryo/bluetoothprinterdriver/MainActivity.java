package com.aryo.bluetoothprinterdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    BluetoothDevice bluetooth_printer_device;
    UUID bluetooth_printer_uuid;
    BluetoothSocket bluetooth_printer_socket;

    public String BT_PRINTER_MAC_ADDRESS = "66:22:2E:59:63:2F";
    public int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    public void pair_bluetooth(View view) {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            for (BluetoothDevice device : pairedDevices) { // save the printer's BluetoothDevice object in variable
                if (device.getAddress().compareTo(BT_PRINTER_MAC_ADDRESS) == 0) bluetooth_printer_device = device;
                break;
            }

            bluetooth_printer_uuid = bluetooth_printer_device.getUuids()[0].getUuid(); // getUuids() returns a ParcelUuid array.

            try {
                bluetooth_printer_socket = bluetooth_printer_device.createRfcommSocketToServiceRecord(bluetooth_printer_uuid);
                bluetooth_printer_socket.connect();
                OutputStream tmp = bluetooth_printer_socket.getOutputStream();
                tmp.write("It fucking works!\n\n".getBytes());
                bluetooth_printer_socket.close();
            } catch (IOException e) {
                Log.d("Bluetooth", "Socket's listen() method failed", e);
            }

        }
    }
}
