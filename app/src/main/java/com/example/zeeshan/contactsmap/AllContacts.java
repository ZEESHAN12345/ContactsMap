package com.example.zeeshan.contactsmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllContacts extends AppCompatActivity {

    public static ListView contactNames;

    private static String JSON_URL = "http://private-b08d8d-nikitest.apiary-mock.com/contacts";
    private static int len;

    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> emails = new ArrayList<String>();
    private ArrayList<String> phones = new ArrayList<String>();
    private ArrayList<String> officePhones = new ArrayList<String>();
    private ArrayList<String> latitudes = new ArrayList<String>();
    private ArrayList<String> longitudes = new ArrayList<String>();

    private static boolean pesentNameInPhone = false;

    private boolean _insertContact;

    public static ArrayList< ArrayList<String> > details= new ArrayList< ArrayList<String> >();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);

        contactNames = (ListView) findViewById(R.id.contact_names);

        final Context context = getApplicationContext();

        final ContentResolver cr = getContentResolver();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        Log.v("JSON_URL", JSON_URL);

        if ( isNetworkAvailable() ) {

        JsonArrayRequest request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //pDialog.hide();
                Log.v("Response", response.toString());

                try {
                    JSONObject result = response.getJSONObject(0);
                    JSONArray contacts = result.getJSONArray("contacts");
                    len = contacts.length();

                    for (int i=0; i<len; i++) {
                        JSONObject details = contacts.getJSONObject(i);

                        String name = details.getString("name");

                        String email;
                        if(details.has("email")) {
                            email = details.getString("email");
                        } else {
                            email = "Not Available";
                            //Log.v("entering", "email = "+email);
                        }

                        String phone;
                        if(details.has("phone")) {
                            phone = details.getString("phone");
                        } else {
                            phone = "Not Available";
                        }

                        String officePhone;
                        if(details.has("officePhone")) {
                            officePhone = details.getString("officePhone");
                        } else {
                            officePhone = "Not Available";
                        }

                        String latitude = details.getString("latitude");

                        String longitude = details.getString("longitude");

                        names.add(name);
                        emails.add(email);
                        phones.add(phone);
                        officePhones.add(officePhone);
                        latitudes.add(latitude);
                        longitudes.add(longitude);


                            Log.v("Entering2", phone + " ==> Yes");
                            _insertContact = insertContact(context, cr, name, email, phone, officePhone);
                            Log.v("checkInsert", String.valueOf(_insertContact));

                    }

                    details.add(names);
                    details.add(emails);
                    details.add(phones);
                    details.add(officePhones);
                    details.add(latitudes);
                    details.add(longitudes);

                    pDialog.hide();
                    if (pesentNameInPhone) {
                        Toast.makeText(getApplicationContext(), "Some Contacts were already present in your phone.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Contacts have been successfully inserted in your phone!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                contactNames.setAdapter( new CustomAdapter(context, names, details, len) );
                Log.v("Fucked1?", "Yes");

                ContactsMap obj = new ContactsMap();
                obj.ContactsMapConstructor(latitudes, longitudes, details);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(request);

        } else {
            pDialog.hide();
            Toast.makeText(context, "No available Internet connection!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }




    public static boolean insertContact( Context context, ContentResolver contactAdder, String name, String email, String phone, String officePhone) {

       if( contactExists( context ,name ) ) {
            //Toast.makeText(ctx, "Contact name: " + name + " already exists!", Toast.LENGTH_SHORT).show();
            pesentNameInPhone = true;
            return false;
        }

        else {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                            name).build());

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());

            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            phone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, officePhone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());

            try {
                contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                return false;
            }
            return true;
       }
    }


    private static boolean contactExists(Context context, String name) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID};
        String selection = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?";
        String[] selectionArguments = { name };
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArguments, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                return true;
            }
        }
        return false;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}





class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> names;
    ArrayList<ArrayList<String>> _details;
    int _len;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context_here, ArrayList<String> Names, ArrayList<ArrayList<String>> detail, int len) {
        context = context_here;
        names = Names;
        _details = detail;
        _len = len;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView nameTv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.row_list, null);

        holder.nameTv = (TextView) rowView.findViewById(R.id.name_tv);
        holder.nameTv.setText(names.get(position));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> details_final = new ArrayList<String>();

                for (int i = 0; i < _details.size(); i++) {
                    details_final.add(_details.get(i).get(position));
                    //Log.v("check = ", _details.get(i).get(position));
                }

                Intent i1 = new Intent(context, ContactDetails.class);
                i1.putExtra("details_final", details_final);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i1);

            }
        });
        return rowView;
    }
}