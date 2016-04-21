package com.example.zeeshan.contactsmap;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ContactsMap extends FragmentActivity implements OnMapReadyCallback {

    private LinkedHashMap< String, ArrayList<String> > hashMapNames = new LinkedHashMap< String,ArrayList<String> >();
    private LinkedHashMap< String, ArrayList<String> > hashMapEmails = new LinkedHashMap< String,ArrayList<String> >();
    private LinkedHashMap< String, ArrayList<String> > hashMapPhones = new LinkedHashMap< String,ArrayList<String> >();
    private LinkedHashMap< String, ArrayList<String> > hashMapOfficePhones = new LinkedHashMap< String,ArrayList<String> >();
    private LinkedHashMap< String, ArrayList<String> > hashMapLatitudes = new LinkedHashMap< String,ArrayList<String> >();
    private LinkedHashMap< String, ArrayList<String> > hashMapLongitudes = new LinkedHashMap< String,ArrayList<String> >();

    private ArrayList< LinkedHashMap<String, ArrayList<String> > > send = new ArrayList< LinkedHashMap<String, ArrayList<String> > >();

    private GoogleMap mMap;
    LatLng pos;

    private static ArrayList<String> latitudes;
    private static ArrayList<String> longitudes;
    public static ArrayList< ArrayList<String> > details;

    public void ContactsMapConstructor(ArrayList<String> latits, ArrayList<String> longits, ArrayList< ArrayList<String> > _details  ) {
            latitudes = latits;
            longitudes = longits;
            details = _details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_map);

        Log.v("check_passed", details.get(1).get(0));
        //Log.v("check here", latitudes.toString() );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (int i=0; i<latitudes.size(); i++) {

            String check_latLang = latitudes.get(i) + longitudes.get(i);
            if (hashMapNames.containsKey(check_latLang)) {
                hashMapNames.get( check_latLang ).add(details.get(0).get(i));
                //Log.v("check_email_2", "email at " + i + "= " + details.get(1).get(i));
                hashMapEmails.get( check_latLang ).add(details.get(1).get(i));
                //Log.v("check_email_2", String.valueOf(hashMapEmails.get(check_latLang)));
                hashMapPhones.get( check_latLang ).add( details.get(2).get(i) );
                hashMapOfficePhones.get( check_latLang ).add( details.get(3).get(i) );
                hashMapLatitudes.get( check_latLang ).add( details.get(4).get(i) );
                hashMapLongitudes.get( check_latLang ).add( details.get(5).get(i) );

            } else {
                ArrayList<String> check0 = new ArrayList<String>();
                check0.add( details.get(0).get(i) );
                hashMapNames.put(check_latLang, check0);

                ArrayList<String> check1 = new ArrayList<String>();
                //Log.v("check_email_1", "email at " + i + "= " + details.get(1).get(i));
                check1.add(details.get(1).get(i));
                hashMapEmails.put(check_latLang, check1);
                //Log.v( "check_email_1", String.valueOf(hashMapEmails.get(check_latLang)) );

                ArrayList<String> check2 = new ArrayList<String>();
                check2.add( details.get(2).get(i) );
                hashMapPhones.put(check_latLang, check2);

                ArrayList<String> check3 = new ArrayList<String>();
                check3.add( details.get(3).get(i) );
                hashMapOfficePhones.put(check_latLang, check3);

                ArrayList<String> check4 = new ArrayList<String>();
                check4.add( details.get(4).get(i) );
                hashMapLatitudes.put(check_latLang, check4);

                ArrayList<String> check5 = new ArrayList<String>();
                check5.add( details.get(5).get(i) );
                hashMapLongitudes.put(check_latLang, check5);
            }

        }
        send.add(hashMapNames);
        send.add(hashMapEmails);
        send.add(hashMapPhones);
        send.add(hashMapPhones);
        send.add(hashMapOfficePhones);
        send.add(hashMapLatitudes);
        send.add(hashMapLongitudes);

        for ( int j=0; j<hashMapNames.size(); j++ ) {

            String lat_lang = (String) hashMapNames.keySet().toArray()[j];
            Log.v( "check lat_lang", lat_lang );

            String names = String.valueOf(hashMapNames.get(hashMapNames.keySet().toArray()[j]));
            Log.v( "check names", names );


            Double lat_final = Double.parseDouble( lat_lang.substring(0, 7) );
            Log.v( "latitude", String.valueOf(lat_final) );
            Double long_final = Double.parseDouble( lat_lang.substring(7) );
            Log.v( "longitude", String.valueOf(long_final) );
            pos = new LatLng(lat_final, long_final);
            mMap.addMarker(new MarkerOptions().position(pos).title(names));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                LatLng loc = marker.getPosition();
                double _lat = loc.latitude;
                double _long = loc.longitude;
                String _latlong = String.valueOf(_lat) + String.valueOf(_long);


                Log.v("_latlong", _latlong);
                String check_phone = String.valueOf(hashMapPhones.get(_latlong));
                Log.v("check_phone_1", check_phone);

                    ArrayList<String> _names = hashMapNames.get( _latlong );
                    ArrayList<String> _emails = hashMapEmails.get( _latlong );
                    ArrayList<String> _phones = hashMapPhones.get( _latlong );
                    ArrayList<String> _officePhones = hashMapOfficePhones.get( _latlong );
                    ArrayList<String> _latitudes = hashMapLatitudes.get( _latlong );
                    ArrayList<String> _longitudes = hashMapLongitudes.get( _latlong );

                Log.v("check_phone_2", _phones.toString());

                Intent i = new Intent(getApplicationContext(), MapLocation.class);
                i.putExtra("_latlong", _latlong);
                i.putExtra("_names", _names);
                i.putExtra("_emails", _emails);
                i.putExtra("_phones", _phones);
                i.putExtra("_officePhones", _officePhones);
                i.putExtra("_latitudes", _latitudes);
                i.putExtra("_longitudes", _longitudes);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);

            }
        });

    }

}