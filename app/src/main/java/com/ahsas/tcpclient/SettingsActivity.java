package com.ahsas.tcpclient;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;

public class SettingsActivity extends AppCompatActivity {

    public static final String MY_PREF_NAME = "Settings_preferences";
    public static final String LOG = "Settings";
    private String mIPAddress = "127.0.0.1";
    private static int mPort = 3020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final TextView mIPLabel = (TextView) findViewById(R.id.IP_label_textView);
        final TextView mPingLabel = (TextView) findViewById(R.id.ping_label_textView);
        final EditText mIPFirstDigit = (EditText) findViewById(R.id.ip_first_dig_editText);
        final EditText mIPSecondDigit = (EditText) findViewById(R.id.ip_second_dig_editText);
        final EditText mIPThirdDigit = (EditText) findViewById(R.id.ip_third_dig_editText);
        final EditText mIPFourthDigit = (EditText) findViewById(R.id.ip_fourth_dig_editText);
        final EditText mPortEditText = (EditText) findViewById(R.id.port_editText);

        final Button mPingButton = (Button) findViewById(R.id.ping_btn);
        final Button mSetIPButton = (Button) findViewById(R.id.set_IP_btn);
        final Button mSetPortButton = (Button) findViewById(R.id.set_port_btn);

        mIPLabel.setText("Set IP: now is set to: " + mIPAddress + ":" + mPort);
        mPingLabel.setText("Press ping to check connection");



        mPingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PingAddress().execute(mIPAddress);
                mPingLabel.setText("Pinging...");
            }
        });

        mSetIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIPFirstDigit.getText().toString().equals("")){
                    if (!mIPSecondDigit.getText().toString().equals("")){
                        if (!mIPThirdDigit.getText().toString().equals("")){
                            if (!mIPFourthDigit.getText().toString().equals("")){
                                mIPAddress = mIPFirstDigit.getText().toString() + "." +
                                        mIPSecondDigit.getText().toString() + "." +
                                        mIPThirdDigit.getText().toString() + "." +
                                        mIPFourthDigit.getText().toString();
                                mIPLabel.setText("Set IP: now is set to: " + mIPAddress + ":" + mPort);
                                mPingLabel.setText("Press ping to check connection");
                                changePreferences();
                            }
                        }
                    }
                }
            }
        });

        mSetPortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPortEditText.getText().toString().equals("")){
                    mPort = Integer.valueOf(mPortEditText.getText().toString());
                    changePreferences();
                    mIPLabel.setText("Set IP: now is set to: " + mIPAddress + ":" + mPort);
                }
            }
        });
    }

    private  void changePreferences(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString("IP", mIPAddress);
        editor.putInt("port", mPort);
        editor.apply();
    }

    public SettingsActivity() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG, "OnStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG, "OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "OnDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG, "OnPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG, "OnRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG, "OnResume");
        SharedPreferences preferences = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE);
        String restoredIP = preferences.getString("IP", "127.0.0.1");
        int restoredPort = preferences.getInt("port", 3020);
        if (!restoredIP.equals("127.0.0.1")){
            mIPAddress = restoredIP;
        }
        if (restoredPort != 3020) {
            mPort = restoredPort;
        }
        TextView mIPLabel = (TextView) findViewById(R.id.IP_label_textView);
        mIPLabel.setText("Set IP: now is set to: " + mIPAddress + ":" + mPort);
    }


    public class PingAddress extends AsyncTask<String, Void, Void> {


        boolean reachable = false;

        public PingAddress() {
        }

        @Override
        protected Void doInBackground(String... ipAddress) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + mIPAddress);
                int     exitValue = ipProcess.waitFor();
                reachable = exitValue == 0;
            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            TextView mPingLabel = (TextView) findViewById(R.id.ping_label_textView);
            mPingLabel.setText("Pinging...");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView mPingLabel = (TextView) findViewById(R.id.ping_label_textView);
            if (reachable){
                mPingLabel.setText("Reachable.");
            } else {
                mPingLabel.setText("Not Reachable.");
            }
        }
    }
}
