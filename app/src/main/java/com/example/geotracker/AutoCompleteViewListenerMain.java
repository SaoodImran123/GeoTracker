//SAOOD IMRAN, 100620420
//Geotracker app
//Nov, 2021
package com.example.geotracker;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

public class AutoCompleteViewListenerMain implements TextWatcher {
    Context context;
    //default constructor
    public AutoCompleteViewListenerMain(Context context){
        this.context = context;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //this method displays the list of addresses in the database that are similar to the text entered into the edit-text
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        MainActivity mainact = ((MainActivity) context);
        mainact.address = mainact.getAddresses(s.toString());
        mainact.searchAdapter.notifyDataSetChanged();
        mainact.searchAdapter = new ArrayAdapter<>(mainact, android.R.layout.simple_dropdown_item_1line, mainact.address);
        mainact.addressSearch.setAdapter(mainact.searchAdapter);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
