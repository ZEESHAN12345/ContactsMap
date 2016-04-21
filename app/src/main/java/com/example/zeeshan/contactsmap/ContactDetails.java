package com.example.zeeshan.contactsmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactDetails extends AppCompatActivity {

    TextView Name;
    TextView Details;

    String name, email, phone, officePhone, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        Name = (TextView) findViewById(R.id.employer_name);
        Details = (TextView) findViewById(R.id.employer_details);

        Intent i2 = getIntent();
        Bundle b2 =  i2.getExtras();
        ArrayList<String> details_final = (ArrayList<String>) b2.get("details_final");
        Log.v( "details_final", details_final.toString() );
        for(int i=0; i<details_final.size(); i++) {
            switch (i)
            {

                case 0:
                    name = details_final.get(i);
                    break;
                case 1:
                    email = details_final.get(i);
                    Log.v("details_final.get(1) = ", email);
                    break;
                case 2:
                    phone = details_final.get(i);
                    break;
                case 3:
                    officePhone = details_final.get(i);
                    break;
                case 4:
                    latitude = details_final.get(i);
                    break;
                case 5:
                    longitude = details_final.get(i);
                    break;

            }
        }

        Name.setText(name);

        String details = "Name = "+name+",\n" +
                "Email = "+email+",\n" +
                "Phone = "+phone+",\n" +
                "Office Phone = "+officePhone+",\n" +
                "Latitude = "+latitude+",\n" +
                "Longitude = "+longitude;

        Details.setText(details);

    }


}
