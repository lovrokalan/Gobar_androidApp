package tnuv.fe.uni_lj.si.gobar;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;

    static ArrayList<String> mName = new ArrayList<String>();
    static ArrayList<String> mDate = new ArrayList<String>();
    static ArrayList<String> mAdress = new ArrayList<String>();
    static ArrayList<String> mVrsteGob = new ArrayList<String>();

//    String mName[] = {"Jurčki", "Lisičke", "Mušnice", "Lesne"};
//    String mDate[] = {"12. 1. 2019", "12. 1. 2015", "1. 9. 2015", "11. 1. 2010"};
//    String mAdress[] = {"Jurčki", "Lisičke", "Mušnice", "Lesne"};
//    String mVrsteGob[] = {"Jurčki", "Lisičke", "Mušnice", "Lesne"};

    static MyAdapter locationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();

        ListView listView = findViewById(R.id.listView);

//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
//
//        listView.setAdapter(arrayAdapter);

        TextView starterNotification = findViewById(R.id.starter_notification);

        if (places.size() > 0) {
            starterNotification.setVisibility(View.INVISIBLE);
        }

        locationsAdapter = new MyAdapter(this, mName, mDate, mAdress, mVrsteGob);

        listView.setAdapter(locationsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeId", i);

                startActivity(intent);
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
