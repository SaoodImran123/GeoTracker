//SAOOD IMRAN, 100620420
//Geotracker app
//Nov, 2021
package com.example.geotracker;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.navigation.ui.AppBarConfiguration;


import com.example.geotracker.databinding.ActivityEditPageBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityEditPageBinding binding;
    DatabaseHelper databaseHelper;
    EditText lond;
    EditText latd;
    Button btnsrch;
    Button btndiscover;
    Button btndelete;
    Button btnUpdate;
    AutoCompleteView addressSearch;
    ArrayAdapter<String> searchAdapter;
    ArrayList<String> address = new ArrayList<>();

    //default on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        //making same autofill feature in main for the edit page
        addressSearch = (AutoCompleteView) findViewById(R.id.address_Search);
        addressSearch.addTextChangedListener(new AutoCompleteViewListener(this));
        searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,address);
        addressSearch.setAdapter(searchAdapter);

        //binding latitude and longitude displays
        lond = findViewById(R.id.lonDisplay);
        latd = findViewById(R.id.latDisplay);

        //this onclick allows users to search the DATABASE for long-lats that match the query entered exactly
        btnsrch = findViewById(R.id.buttonSearch);
        btnsrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressSearch.getText().toString();
                //if entered addresses matches a database element exactly, display it
                Cursor data = databaseHelper.fetch("SELECT * FROM geomapping WHERE address= '" + address + "'");
                String storedLat = "Not in database";
                String storedLon = "Not in database";
                if(data != null && data.moveToFirst()){
                    storedLat = data.getString(2);
                    storedLon = data.getString(3);
                    data.close();
                }
                lond.setText(storedLon);
                latd.setText(storedLat);
            }
        });
        //this button allows user to enter an address not in the database, and display it. If no such address exists, displays that info
        btndiscover = findViewById(R.id.discoverLatLon);
        btndiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongLat ldata = getLocFinder(addressSearch.getText().toString());
                lond.setText(ldata.longitude);
                latd.setText(ldata.latitude);
            }
        });
        //this button updates the entry matching the address entered. If no such address exists, it adds a new entry.
        btnUpdate = findViewById(R.id.update_Button);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor data = databaseHelper.fetch("SELECT * FROM geomapping WHERE address ='" + addressSearch.getText().toString() + "'");
                int ID = data.getCount();
                String lat = latd.getText().toString();
                String lon = lond.getText().toString();
                //if there exists an entry matching it, update that entry
                if (ID == 1){
                    if(data.moveToFirst()){
                        String id = data.getString(0);
                        String address = data.getString(1);
                        databaseHelper.update(id, address, lat, lon);
                        Toast toast = Toast.makeText(getApplicationContext(), "Updated database entry!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                //otherwise, add a new entry
                else{
                    String address = addressSearch.getText().toString();
                    databaseHelper.addData(address, lat, lon);
                    Toast toast = Toast.makeText(getApplicationContext(), "New entry has been created!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        //this button deletes the entry matching the entered address.
        btndelete = findViewById(R.id.delete_Button);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor data = databaseHelper.fetch("SELECT * FROM geomapping WHERE address ='" + addressSearch.getText().toString() + "'");
                int ID = data.getCount();
                //if that address exists in database, delete its entry
                if (ID == 1){
                    if(data.moveToFirst()){
                        String id = data.getString(0);
                        databaseHelper.delete(id);
                        Toast toast = Toast.makeText(getApplicationContext(), "Entry removed.", Toast.LENGTH_SHORT);
                        toast.show();
                        addressSearch.setText("");
                        latd.setText("");
                        lond.setText("");
                    }
                }
                //otherwise, show an error toast
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "No database entry matches that address.", Toast.LENGTH_SHORT);
                    toast.show();

                }

            }
        });










    }
    private class LongLat{
        private String latitude;
        private String longitude;

        public LongLat(String lon, String lat){
            this.latitude=lat;
            this.longitude=lon;
        }
        public String getLatitude(){
            return latitude;
        }
        public String getLongitude(){
            return longitude;
        }

    }

    private EditPage.LongLat getLocFinder(String location){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String answer = null;
        try {
            List addressList = geocoder.getFromLocationName(location,1);
            if (addressList != null && addressList.size() >0){
                Address address = (Address) addressList.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(address.getLatitude());
                String latdata= String.valueOf(address.getLatitude());
                String longdata= String.valueOf(address.getLongitude());
                return new EditPage.LongLat(longdata, latdata);
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new EditPage.LongLat("No latitude data", "No longitude data");

    }

    public ArrayList<String> getAddresses(String address){
        Cursor addresses = databaseHelper.fetch("SELECT address FROM geomapping WHERE address LIKE '%" + address + "%'");
        ArrayList<String> addList = new ArrayList<String>();
        if (addresses.moveToFirst()){
            do{
                String add = addresses.getString(0);
                addList.add(add);
            } while(addresses.moveToNext());
        }
        return addList;
    }
}