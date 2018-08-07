package com.lab110b3.vendingmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity{

    Button button0, button1, button2, button3, button4, button5, button6, button7, button8,
            button9, buttonHash, buttonAst, buttonClr, buttonEnt;
    TextView edt1;

    int intValue, intCheck;
    String HexStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonAst = (Button) findViewById(R.id.buttonAst);
        buttonHash = (Button) findViewById(R.id.buttonHash);
        buttonEnt = (Button) findViewById(R.id.buttonEnt);
        buttonClr = (Button) findViewById(R.id.buttonClr);
        edt1 = (TextView) findViewById(R.id.textView);

        buttonClr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edt1.clearComposingText();
                edt1.setText("");
            }
        });
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("0"));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("1"));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("2"));
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("3"));
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("4"));
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("5"));
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("6"));
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("7"));
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("8"));
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt1.setText(String.valueOf(edt1.getText()).concat("9"));
            }
        });
        buttonEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intValue = Integer.parseInt(String.valueOf(edt1.getText()));
                if (intValue > 0 && intValue <= 60){
                    edt1.setText(getString(R.string.accepted));
                    intCheck = 255 - intValue;
                    String hexValue = Integer.toHexString(intValue);
                    if (!(hexValue.length() == 2)) {
                        hexValue = "0"+hexValue;
                    }
                    HexStr = "00ff" + hexValue + Integer.toHexString(intCheck) + "55aa";
                    //edt1.setText(HexStr);

                    // Find al available drivers from attached devices
                    UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
                    if (availableDrivers.isEmpty()){
                        return;
                    }

                    UsbSerialDriver driver = availableDrivers.get(0);
                    UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
                    if (connection == null){
                        // Call UsbManager.requestPermission(driver.getDevice(),..)
                        return;
                    }

                    UsbSerialPort port = driver.getPorts().get(0);
                    try {
                        port.open(connection);
                        port.setParameters(9600, UsbSerialPort.DATABITS_8,
                                UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                        port.write(HexStrToByteArray(HexStr),1000);

                    } catch (IOException e){
                        //deal with error
                    } finally {
                        try {
                            port.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    edt1.setText("");
                } else {
                    edt1.setText(getString(R.string.invalid));
                }
            }
        });
    }
    public static byte[] HexStrToByteArray(String str){
        int len = str.length();
        byte[] data = new byte[len/2];
        for (int i = 0; i < len; i +=2){
            data[i/2] = (byte) ((Character.digit(str.charAt(i),16) << 4)
                    + Character.digit(str.charAt(i+1), 16));
        }
        return data;
    }
}
