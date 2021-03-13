package com.balazs.einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText mEdit;
    TextView tcpResult;

    @SuppressLint("StaticFieldLeak")
    public class NetworkCallTCP extends AsyncTask<String, int[], String> {
        @Override
        protected String doInBackground(String... params) {
            String resultFromTCPRequest = "";
            if (params.length != 0) {
                Socket clientSocket = null;
                try {
                    clientSocket = new Socket("se2-isys.aau.at", 53212);
                    DataOutputStream outToServer = null;
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = null;
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes(params[0] + '\n');
                    resultFromTCPRequest = inFromServer.readLine();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultFromTCPRequest;
        }

        @Override
        protected void onPostExecute(String resultFromTCPRequest) {
            super.onPostExecute(resultFromTCPRequest);
            tcpResult = (TextView) findViewById(R.id.tcpResultText);
            tcpResult.setText(resultFromTCPRequest);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit = (EditText) findViewById(R.id.editTextNumberSigned2);
    }

    public void runTCP(View v) {
        new NetworkCallTCP().execute(mEdit.getText().toString());
    }
}