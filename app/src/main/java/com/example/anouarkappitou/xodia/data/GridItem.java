package com.example.anouarkappitou.xodia.data;

/**
 * Created by anouarkappitou on 8/1/17.
 */

public class GridItem {

    private int _resource;
    private String _text;

    public GridItem( String text )
    {
        _resource = 0;
        _text = text;
    }

    public GridItem( String text , int resource )
    {
        _resource = resource;
        _text = text;
    }

    public String get_text(){ return _text; }
    public int get_image_resource(){ return _resource; }
}
