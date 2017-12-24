package com.example.anouarkappitou.xodia.Activities;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anouarkappitou.xodia.Application;
import com.example.anouarkappitou.xodia.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Arrows extends AppCompatActivity {

    TextView _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrows);



        Application application = (Application) getApplication();
        final BluetoothSocket socket = application.get_socket();
        OutputStream output = null;

        try {
            output = socket.getOutputStream();
        } catch (IOException e) {
        }

        Button on,off;

        on = (Button) findViewById( R.id.on );
        off = (Button) findViewById( R.id.off );

        final OutputStream finalOutput = output;


        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if( finalOutput == null ) return;
                    PrintWriter writer = new PrintWriter( finalOutput );
                    writer.println('1');
                    writer.flush();
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if( finalOutput == null ) return;
                    PrintWriter writer = new PrintWriter( finalOutput );
                    writer.println('2');
                    writer.flush();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.d("Arrows" , "loop");
                    new AsyncReader(socket).execute();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });

    }


    private String listenforData(BluetoothSocket socket) {

        StringBuffer incoming = new StringBuffer();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            InputStream instream = socket.getInputStream();
            int bytesRead = -1;

            while (true) {
                bytesRead = instream.read(buffer);
                if (bytesRead != -1) {
                    String result = "";
                    while ((bytesRead == bufferSize) && (buffer[bufferSize - 1] != 0)) {
                        result = result + new String(buffer, 0, bytesRead - 1);
                        bytesRead = instream.read(buffer);
                    }
                    result = result + new String(buffer, 0, bytesRead - 1);
                    incoming.append(result);
                }
                socket.close();
            }
        } catch (IOException e) {
        } finally {
        }

        return null;
    }

    class AsyncReader extends AsyncTask<Void, Object, String> {

        private BluetoothSocket _socket;

        public AsyncReader(BluetoothSocket socket) {
            _socket = socket;
        }

        @Override
        protected String doInBackground(Void... objects) {
            return listenforData(_socket);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if( s != null )
            {
                _view.append( s );
            }
        }
    }
}
