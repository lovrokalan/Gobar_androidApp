package tnuv.fe.uni_lj.si.gobar;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    static ArrayList<String> places;
    static ArrayList<LatLng> locations;
    static ArrayAdapter arrayAdapter;

    static ArrayList<String> mName;
    static ArrayList<String> mDate;
    static ArrayList<String> mAdress;
    static ArrayList<String> mOpisLokacije;
    static ArrayList<String> mVrsteGob;

    static MyAdapter locationsAdapter;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();

        loadData();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.gobar_logo);

        myDialog = new Dialog(this);

        ListView listView = findViewById(R.id.listView);

        TextView starterNotification = findViewById(R.id.starter_notification);

        if (places.size() > 0) {
            starterNotification.setVisibility(View.INVISIBLE);
        }

        locationsAdapter = new MyAdapter(this, mName, mDate, mAdress, mVrsteGob);

        listView.setAdapter(locationsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                intent.putExtra("placeId", i);
//
//                startActivity(intent);
                ShowPopup(i);
            }
        });

        Button buttonOpenMaps = findViewById(R.id.buttonOpenMaps);
        buttonOpenMaps.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeId", -1);

                startActivity(intent);
            }
        });
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        String json = sharedPreferences.getString("places", null);
        places = gson.fromJson(json, type);

        if (places == null) {
            places = new ArrayList<String>();
        }

        json = sharedPreferences.getString("mName", null);
        mName = gson.fromJson(json, type);

        if (mName == null) {
            mName = new ArrayList<String>();
        }

        json = sharedPreferences.getString("mAdress", null);
        mAdress = gson.fromJson(json, type);

        if (mAdress == null) {
            mAdress = new ArrayList<String>();
        }

        json = sharedPreferences.getString("mDate", null);
        mDate = gson.fromJson(json, type);

        if (mDate == null) {
            mDate = new ArrayList<String>();
        }

        json = sharedPreferences.getString("mVrsteGob", null);
        mVrsteGob = gson.fromJson(json, type);

        if (mVrsteGob == null) {
            mVrsteGob = new ArrayList<String>();
        }

        json = sharedPreferences.getString("mOpisLokacije", null);
        mOpisLokacije = gson.fromJson(json, type);

        if (mOpisLokacije == null) {
            mOpisLokacije = new ArrayList<String>();
        }

        type = new TypeToken<ArrayList<LatLng>>() {}.getType();
        json = sharedPreferences.getString("locations", null);
        locations = gson.fromJson(json, type);

        if (locations == null) {
            locations = new ArrayList<LatLng>();
        }
    }

    public void ShowPopup(final int locationNumber) {
        TextView textViewDate;
        ImageView closeBtn;
        TextView textViewNaslov;
        TextView textViewIme;
        TextView textViewVrste;
        TextView textViewOpis;

        myDialog.setContentView(R.layout.location_details_popup);

        closeBtn =(ImageView) myDialog.findViewById(R.id.imageViewClose);

        textViewDate = (TextView) myDialog.findViewById(R.id.textViewDate);
        textViewDate.setText(mDate.get(locationNumber));

        textViewNaslov = (TextView) myDialog.findViewById(R.id.textViewNaslov);
        textViewNaslov.setText(mAdress.get(locationNumber));

        textViewIme = (TextView) myDialog.findViewById(R.id.textViewIme);
        textViewIme.setText(mName.get(locationNumber));

        textViewVrste = (TextView) myDialog.findViewById(R.id.textViewVrste);
        textViewVrste.setText(mVrsteGob.get(locationNumber));

        textViewOpis = (TextView) myDialog.findViewById(R.id.textViewOpis);
        textViewOpis.setText(mOpisLokacije.get(locationNumber));

        Button btnPrikaziMaps = (Button) myDialog.findViewById(R.id.btnPrikaziMaps);
        btnPrikaziMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeId", locationNumber);

                startActivity(intent);
            }
        });

        Button btnNavigiraj = (Button) myDialog.findViewById(R.id.btnNavigiraj);
        btnNavigiraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ locations.get(locationNumber).latitude +","+ locations.get(locationNumber).longitude + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        });

        Button btnDelete = (Button) myDialog.findViewById(R.id.btnDeleteLocation);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                places.remove(locationNumber);
                locations.remove(locationNumber);
                mAdress.remove(locationNumber);
                mDate.remove(locationNumber);
                mName.remove(locationNumber);
                mOpisLokacije.remove(locationNumber);
                mVrsteGob.remove(locationNumber);

                locationsAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                String json = gson.toJson(MainActivity.places);
                editor.putString("places", json);

                json = gson.toJson(MainActivity.locations);
                editor.putString("locations", json);

                json = gson.toJson(MainActivity.mName);
                editor.putString("mName", json);

                json = gson.toJson(MainActivity.mDate);
                editor.putString("mDate", json);

                json = gson.toJson(MainActivity.mAdress);
                editor.putString("mAdress", json);

                json = gson.toJson(MainActivity.mOpisLokacije);
                editor.putString("mOpisLokacije", json);

                json = gson.toJson(MainActivity.mVrsteGob);
                editor.putString("mVrsteGob", json);

                editor.apply();

                myDialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView starterNotification = findViewById(R.id.starter_notification);
        if (places.size() > 0) {
            starterNotification.setVisibility(View.INVISIBLE);
        }
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rName;
        ArrayList<String> rDate;
        ArrayList<String> rAddress;
        ArrayList<String> rVrsteGob;

        MyAdapter(Context c, ArrayList<String> name, ArrayList<String> date, ArrayList<String> address, ArrayList<String> vrsteGob) {
            super(c, R.layout.location_list_item, R.id.textViewLocationName, name);
            this.context=c;
            this.rName=name;
            this.rDate=date;
            this.rAddress=address;
            this.rVrsteGob=vrsteGob;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.location_list_item, parent, false);
            TextView locationName = row.findViewById(R.id.textViewLocationName);
            TextView locationDate = row.findViewById(R.id.textViewLocationDate);
            TextView locationAddress = row.findViewById(R.id.textViewAddress);
            TextView locationVrsteGob = row.findViewById(R.id.textViewVrsteGob);

            locationName.setText(rName.get(position));
            locationDate.setText(rDate.get(position));
            locationAddress.setText(rAddress.get(position));
            locationVrsteGob.setText(rVrsteGob.get(position));

            return row;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initInstances() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout.addDrawerListener(mToggle);

        NavigationView navigation = (NavigationView) findViewById(R.id.navigation_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_1:
                        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(i);
                        break;

                    case R.id.navigation_item_2:
                        i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        break;

                    case R.id.navigation_item_3:
                        i = new Intent(getApplicationContext(), InfoGobeActivity.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
    }
}
