//SAOOD IMRAN, 100620420
//Geotracker app
//Nov, 2021
package com.example.geotracker;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText etPlace;
    Button btSubmit;
    TextView locaddress;
    Button nxtPage;
    AutoCompleteViewMain addressSearch;
    ArrayAdapter<String> searchAdapter;
    ArrayList<String> address = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creating autocompleteview for main activity to show entries already in database
        addressSearch = (AutoCompleteViewMain) findViewById(R.id.address_Search_main);
        addressSearch.addTextChangedListener(new AutoCompleteViewListenerMain(this));
        searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,address);
        addressSearch.setAdapter(searchAdapter);

        //This big section gets 50 random addresses within a 5km radius of the x0 and y0 I provided (the walmart near my house)
        databaseHelper = new DatabaseHelper(this);
        btSubmit = findViewById(R.id.bt_search);
        nxtPage = findViewById(R.id.button_EditPage);
        locaddress = findViewById(R.id.loc_address);
        Random r = new Random();
        double u;
        double v;
        //walmart location near house
        double x0 =43.94294377425153;
        double y0 =-78.84371110225409;
        //radius of circle, 5km/deg->km conversion
        double radius = 5000/ (111.32 * 1000 * Math.cos(x0 *(Math.PI/180)));
        double w;
        double t;
        double x;
        double y;
        double x2;
        double xlat, xlon;
        int addAmount = 0;
        long size = databaseHelper.getSize();
        //checks size of database at launch, if already greater than 50 - skip the adding part
        if (size < 50){
            addAmount = 50 - (int)size;
        }
        //generates the addresses
        for (int i = 0; i < addAmount; i++){
            u = r.nextDouble();
            v = r.nextDouble();
            w = radius * Math.sqrt(u);
            t = 2 * Math.PI * v;
            x = w * Math.cos(t);
            y = w * Math.sin(t);
            x2 = x / Math.cos(Math.toRadians(y0));
            xlat = x2+x0;
            xlon = y+y0;
            //converts the received doubles into strings
            String lat = String.valueOf(xlat);
            String lon = String.valueOf(xlon);
            String add = translateCoords(lat,lon);
            //adds the addresses into the database
            if (!databaseHelper.ifExists(add) && !(add=="Cannot locate address")){
                databaseHelper.addData(add,lat,lon);
            }
            //if the entry exists or cannot be found; de-increment i by 1 so that we can get 50 unique entries
            else {
                i-=1;
            }

        }
        //onclick listener for search button, checks if the address is in the database (or a close address to it) and returns its data
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressSearch.getText().toString();
                Cursor data = databaseHelper.fetch("SELECT * FROM geomapping WHERE address LIKE '%" + address + "%'");
                //default text to display if no database entries found
                String storedLat = "Not in database";
                String storedLon = "Not in database";
                String storedAdd = "Not in database";
                //if data is found, update the default values
                if(data != null && data.moveToFirst()){
                    storedAdd = data.getString(1);
                    storedLat = data.getString(2);
                    storedLon = data.getString(3);
                    data.close();
                }
                //sets the data to locaddress to display it
                locaddress.setText(storedAdd + "\nLatitude: " + storedLat + "\nLongitude: " + storedLon);
            }
        });
        //an onclick to take user to the edit page
        nxtPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditPage.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    //This function will take in a lat and lon and return an address. It is used to generate random addresses.
    private String translateCoords(String lat, String lon){
        Geocoder geocoder;
        List<Address> addresses;
        double latitude = Double.valueOf(lat);
        double longitude = Double.valueOf(lon);
        String address = "Cannot locate address";
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            address = addresses.get(0).getAddressLine(0);
            return address;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return address;

    }
    //This function will get the list of addresses currently in the database so I can help autofill text.
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