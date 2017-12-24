package com.example.anouarkappitou.xodia.dialogs;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by anouarkappitou on 8/1/17.
 */

// TODO: later impl
public class ProgressDialog extends AlertDialog {

    public interface onTimeout
    {
        void timeout();
    }

    protected ProgressDialog(Context context) {
        super(context);
    }

}
