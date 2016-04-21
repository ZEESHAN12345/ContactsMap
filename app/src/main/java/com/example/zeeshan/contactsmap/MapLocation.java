package com.example.zeeshan.contactsmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MapLocation extends AppCompatActivity {

    private TextView Locations;
    private TextView Heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        Locations = (TextView)findViewById(R.id.locations);
        Heading = (TextView)findViewById(R.id.heading);

        Intent i = getIntent();
        Bundle b =  i.getExtras();
        String _latlong_ = (String) b.get("_latlong");

        ArrayList<String> _names_ = (ArrayList<String>) b.get("_names");
        ArrayList<String> _emails_ = (ArrayList<String>) b.get("_emails");
        ArrayList<String> _phones_ = (ArrayList<String>) b.get("_phones");
        ArrayList<String> _officePhones_ = (ArrayList<String>) b.get("_officePhones");

        String latitude = _latlong_.substring(0, 7);
        String longitude = _latlong_.substring(7);

        String heading = "Employer(s) at" + "\n" + "latitude = " + latitude + " &" + "\n" + "longitude = " + longitude + " are: ";
        Heading.setText(heading);

        String locations = "";
        for (int j=0; j<_names_.size(); j++) {
            locations = locations + "\n";
            locations = locations + "Name = "+_names_.get(j)+"\n" +
                    "Email = "+_emails_.get(j)+"\n" +
                    "Phone = "+_phones_.get(j)+"\n" +
                    "Office Phone = "+_officePhones_.get(j)+"\n";
        }
        Locations.setText(locations);

    }
}
