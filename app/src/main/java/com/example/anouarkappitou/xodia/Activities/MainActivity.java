package com.example.anouarkappitou.xodia.Activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.anouarkappitou.xodia.Application;
import com.example.anouarkappitou.xodia.Adapters.GridAdapter;
import com.example.anouarkappitou.xodia.R;
import com.example.anouarkappitou.xodia.data.GridItem;
import com.example.anouarkappitou.xodia.dialogs.DeviceListDialog;
import com.example.bluetoothlib.Bluetooth;
import com.example.bluetoothlib.BluetoothClient;
import com.example.bluetoothlib.IConnection;
import com.example.bluetoothlib.IFoundCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private static final Class activities[] = { Arrows.class };

    private ProgressDialog _progress_dialog;
    private Bluetooth _bluetooth;
    private DeviceListDialog _device_dialog;
    private BluetoothClient _client;
    private List<BluetoothDevice> _devices;
    private BluetoothDevice _last_try_connect_device;
    private GridView _grid;
    private Handler _handler;
    private Runnable _runnable;
    private Snackbar _device_lookup_try,_connection_try;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _devices = new ArrayList<>();
        // GridView

        _grid = (GridView) findViewById( R.id.main_grid );

        List<GridItem> items = new ArrayList<>();

        items.add( new GridItem( "Arrows" , R.drawable.direction ));
        items.add( new GridItem( "Accelerometer" , R.drawable.compass ));
        items.add( new GridItem("Web" , R.drawable.web ));
        items.add( new GridItem("Joystik" , R.drawable.joystik ) );

        _grid.setAdapter( new GridAdapter( this , items ) );

        // TODO: refactor we would use diffrent protocoles for communication so we need manager to do it for us


        _bluetooth = new Bluetooth( this );
        _bluetooth.enable();

        _client = _bluetooth.client_create();

        _client.set_connection_callback(new IConnection() {
            @Override
            public void onConnect(BluetoothSocket bluetoothSocket) {

                // stop handler
                close_progress_dialog();
                Application application = (Application) getApplication();
                application.set_socket( bluetoothSocket );

                Snackbar.make( _grid , "Connected" , Snackbar.LENGTH_LONG ).show();
            }
        });

        _bluetooth.set_found_callback(new IFoundCallback() {
            @Override
            public void onDevice(BluetoothDevice bluetoothDevice) {

                close_progress_dialog();


                // TODO : stop handler

                show_device_list();
                add_device_to_list(  bluetoothDevice );

            }
        });

        _grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if( i < activities.length )
                {
                    Intent intent = new Intent( MainActivity.this , activities[i] );
                    startActivity( intent );
                }
            }
        });


        _bluetooth.start_discovery();
        show_device_progress_dialog();
    }

    public void show_device_progress_dialog()
    {
        _progress_dialog = new ProgressDialog(this);
        _progress_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progress_dialog.setCancelable(false);
        _progress_dialog.setTitle("Please Wait..");
        _progress_dialog.setMessage("looking for devices ...");
        show_progress(new com.example.anouarkappitou.xodia.dialogs.ProgressDialog.onTimeout() {
            @Override
            public void timeout() {

                _bluetooth.cancel_discovery();
                _device_lookup_try = Snackbar.make( _grid , "Not device was found" , Snackbar.LENGTH_INDEFINITE ).setAction("Rescan", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _bluetooth.start_discovery();
                        show_device_progress_dialog();
                    }
                });

                _device_lookup_try.show();
            }
        });
    }

    private void show_progress(final com.example.anouarkappitou.xodia.dialogs.ProgressDialog.onTimeout timeout )
    {
        Log.d(TAG , "showing progress dialog" );
        _progress_dialog.show();

        _handler = new Handler();
        _runnable = new Runnable() {
            @Override
            public void run() {
                _progress_dialog.dismiss();
                timeout.timeout();
            }
        };
        _handler.postDelayed( _runnable , 5000 );
    }

    public void show_connection_progress_dialog()
    {

        _bluetooth.cancel_discovery();

        // NOTE : her we are sure that we call this method after constructing the device list progress dialog
        // to make sure _progress_dialog is not null else application will crach

       _progress_dialog.setMessage("connecting to device...");
        show_progress(new com.example.anouarkappitou.xodia.dialogs.ProgressDialog.onTimeout() {
            @Override
            public void timeout() {
                _connection_try = Snackbar.make( _grid , "Couldn't connect try again" , Snackbar.LENGTH_INDEFINITE ).setAction("Reconnect", new View.OnClickListener() {
                    @Override
                    public void onClick( View view ) {
                        _client.device_connect( _last_try_connect_device );
                    }
                });

                _connection_try.show();

            }
        });
    }

    public void show_device_list()
    {
        if( _device_dialog != null )
        {
            if( !_device_dialog.isShowing() )
            {
                _device_dialog.show();
            }
            return;
        }

        _device_dialog = new DeviceListDialog( this );
        _device_dialog.set_device_listener(new DeviceListDialog.onDeviceSelected() {
            @Override
            public void onSelect(int position) {
                // TODO: add impl
                close_device_list_dialog();
                show_connection_progress_dialog();

                _last_try_connect_device = _devices.get( position );
                _client.device_connect( _devices.get( position ) );

            }
        });

        _device_dialog.show();
    }

    public void close_device_list_dialog()
    {
        _device_dialog.dismiss();
    }

    public void add_device_to_list( BluetoothDevice device )
    {
        _devices.add( device );

        if( _device_dialog != null )
        {
            _device_dialog.add_device( new DeviceListDialog.Device(device.getName(), device.getAddress() , R.drawable.phone_device ) );
        }
    }

    public void close_progress_dialog()
    {
        _handler.removeCallbacks( _runnable );
        _progress_dialog.dismiss();
    }
}
