package com.example.zeeshan.contactsmap;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        TabSpec tabAllContacts = tabHost.newTabSpec("All-Contacts");
        tabAllContacts.setIndicator("All-Contacts");
        Intent allContacts = new Intent(this, AllContacts.class);
        tabAllContacts.setContent(allContacts);

        TabSpec tabContactsMap = tabHost.newTabSpec("Contacts-Map");
        tabContactsMap.setIndicator("Contacts-Map");
        Intent contactsMap = new Intent(this, ContactsMap.class);
        tabContactsMap.setContent(contactsMap);

        tabHost.addTab(tabAllContacts);
        tabHost.addTab(tabContactsMap);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
