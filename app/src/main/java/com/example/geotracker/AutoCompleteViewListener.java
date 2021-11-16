//SAOOD IMRAN, 100620420
//Geotracker app
//Nov, 2021
package com.example.geotracker;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

public class AutoCompleteViewListener implements TextWatcher {
    Context context;
    //default constructor
    public AutoCompleteViewListener(Context context){
        this.context = context;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //this method displays the list of addresses in the database that are similar to the text entered into the edit-text
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        EditPage editPage = ((EditPage) context);
        editPage.address = editPage.getAddresses(s.toString());
        editPage.searchAdapter.notifyDataSetChanged();
        editPage.searchAdapter = new ArrayAdapter<>(editPage, android.R.layout.simple_dropdown_item_1line, editPage.address);
        editPage.addressSearch.setAdapter(editPage.searchAdapter);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
