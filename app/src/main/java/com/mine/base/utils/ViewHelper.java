package com.mine.base.utils;

import android.widget.TextView;

/**
 * Created by jacky on 15/10/8.
 */
public class ViewHelper {

    public static void setText(TextView view, String str){
        if (view != null && str != null){
            view.setText(str);
        }else {
            view.setText("");
        }
    }

}
