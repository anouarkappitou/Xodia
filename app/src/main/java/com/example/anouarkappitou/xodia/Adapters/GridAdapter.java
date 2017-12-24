package com.example.anouarkappitou.xodia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anouarkappitou.xodia.R;
import com.example.anouarkappitou.xodia.data.GridItem;

import java.util.List;

/**
 * Created by anouarkappitou on 8/1/17.
 */

public class GridAdapter extends BaseAdapter
{
    private static int VIEW_RESOURCE = R.layout.main_grid_item;

    private List<GridItem> _list;
    private Context _context;

    public GridAdapter( Context context , List<GridItem> list )
    {
        _list = list;
        _context = context;
    }

    @Override
    public int getCount() {
        return _list.size();
    }

    @Override
    public Object getItem(int i) {
        return _list.get(i);
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
            LayoutInflater inflater = LayoutInflater.from( _context );
            ret_view = inflater.inflate( VIEW_RESOURCE , viewGroup , false );
        }

        ViewHolder holder = new ViewHolder( ret_view );

        GridItem item = _list.get( i );

        holder._image.setImageResource( item.get_image_resource() );
        holder._text.setText( item.get_text() );

        return ret_view;
    }

    private class ViewHolder
    {
        ImageView _image;
        TextView _text;

        public ViewHolder( View view )
        {
            _image = view.findViewById( R.id.main_grid_item_image );
            _text = view.findViewById( R.id.main_grid_item_text );
        }

        public ImageView get_imageview(){ return _image; }
        public TextView get_textview() { return _text; }
    }
}
