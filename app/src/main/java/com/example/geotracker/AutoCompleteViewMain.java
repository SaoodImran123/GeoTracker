//SAOOD IMRAN, 100620420
//Geotracker app
//Nov, 2021
package com.example.geotracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

@SuppressLint("AppCompatCustomView")
public class AutoCompleteViewMain extends AutoCompleteTextView {
    public AutoCompleteViewMain(Context context) {
        super(context);
    }

    public AutoCompleteViewMain(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteViewMain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AutoCompleteViewMain(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    //this function will turn off the default filter (I change filter to an empty string)
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode){
        String filter="";
        super.performFiltering(filter,keyCode);
    }
    //this method replaces the string in the edittext to the one the user selects
    @Override
    protected void replaceText(final CharSequence text){super.replaceText(text);}

    public AutoCompleteViewMain(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, popupTheme);
    }
}
