package com.ahsas.tcpclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREF_NAME = "Settings_preferences";
    boolean toggler = false;
    private static String mIPaddress = "127.0.0.1";
    private static int mPort = 3020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mCommandText = (EditText) findViewById(R.id.symbo_input_edit_text);
        Button mSendCommand = (Button) findViewById(R.id.send_button);
        Button mLedOnOff = (Button) findViewById(R.id.led_on_off_btn);
        Button mServerShutDown = (Button) findViewById(R.id.server_shut_down_btn);
        Button mSettings = (Button) findViewById(R.id.settings_btn);

        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);

            }
        });

        mSendCommand.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!mCommandText.getText().toString().equals("")){
                    new SendDataToServer().execute(mCommandText.getText().toString());
                    mCommandText.setText("");
                }
            }
        });

        mLedOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggler){
                    new SendDataToServer().execute("s");
                    toggler = false;
                } else {
                    new SendDataToServer().execute("a");
                    toggler = true;
                }
            }
        });

        mServerShutDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendDataToServer().execute("c");
            }
        });

    }

    public class SendDataToServer extends AsyncTask<String, Void, Void>{

        //TODO use shared data
        String dstAddress = mIPaddress;
        int dstPort = mPort;
        String response = "";

        public SendDataToServer() {
        }

        @Override
        protected Void doInBackground(String... strings) {
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                out.print((String) strings[0]);

                out.flush();
                out.close();
                socket.close();

            } catch (UnknownHostException e) {

                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();

            } catch (IOException e) {

                e.printStackTrace();

                response = "IOException: " + e.toString();
            } finally {

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE);
        String restoredIP = preferences.getString("IP", "127.0.0.1");
        int restoredPort = preferences.getInt("port", 3020);
        if (!restoredIP.equals(mIPaddress)){
            mIPaddress = restoredIP;
        }
        if (restoredPort != mPort) {
            mPort = restoredPort;
        }
    }
}
