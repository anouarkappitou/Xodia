package com.example.anouarkappitou.xodia.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anouarkappitou.xodia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anouarkappitou on 8/1/17.
 */

public class DeviceListDialog extends AlertDialog {

    public interface onDeviceSelected
    {
        void onSelect( int position );
    }

    public static class Device
    {
        int _icon;
        String _name;
        String _mac;

        public Device( String name , String address , int icon )
        {
            _icon = icon;
            _mac = address;
            _name = name;
        }


        public int get_icon() {
            return _icon;
        }

        public void set_icon(int _icon) {
            this._icon = _icon;
        }

        public String get_name() {
            return _name;
        }

        public void set_name(String _name) {
            this._name = _name;
        }

        public String get_mac() {
            return _mac;
        }

        public void set_mac(String _mac) {
            this._mac = _mac;
        }
    }

    private onDeviceSelected _device_listener;
    private List<Device> _devices;

    public DeviceListDialog(Context context) {
        super(context);
        _devices = new ArrayList<>();
    }

    public void add_device( Device device )
    {
        _devices.add( device );
    }

    public void set_device_listener( onDeviceSelected device_listener )
    {
        _device_listener = device_listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from( getContext() );

        View view = inflater.inflate( R.layout.device_list_dialog , null );

        init_dialog( view );

    }

    private void init_dialog( View view )
    {
        setContentView( view );
        setCancelable( false );
        setTitle( "device list" );

        ListView listView = view.findViewById( R.id.device_list );

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return _devices.size();
            }

            @Override
            public Object getItem(int i) {
                return _devices.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                View ret_view = view;

                if( ret_view == null )
                {
                    ret_view = LayoutInflater.from( getContext() ).inflate( R.layout.device_list_item , viewGroup , false );
                }

                ImageView image = ret_view.findViewById( R.id.device_image );
                TextView tv = ret_view.findViewById( R.id.device_name );
                TextView mac = ret_view.findViewById( R.id.device_address );

                Device device = _devices.get( i );

                tv.setText( device.get_name() );
                image.setImageResource( device.get_icon() );
                mac.setText( device.get_mac() );

                return ret_view;
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( _device_listener != null )
                {
                    _device_listener.onSelect( i );
                }
            }
        });
    }
}
