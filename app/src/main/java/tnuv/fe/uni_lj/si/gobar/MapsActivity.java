package tnuv.fe.uni_lj.si.gobar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    LocationManager locationManager;
    LocationListener locationListener;
    private GoogleMap mMap;

    Location currentUserLocation;

    FloatingActionButton fab;

    public void centerMapOnLocation(Location location, String title) {
        if(location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initInstances();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                currentUserLocation = lastKnownLocation;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        for(int i=0; i<MainActivity.locations.size(); i++) {
            Marker currentMarker = mMap.addMarker(new MarkerOptions().position(MainActivity.locations.get(i)).title(MainActivity.places.get(i)));
        }

        Intent intent = getIntent();
        if(intent.getIntExtra("placeNumber", -1) == -1) {
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                  centerMapOnLocation(location, "Your Location");
                    currentUserLocation = location;
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                currentUserLocation = lastKnownLocation;
                centerMapOnLocation(lastKnownLocation, "Your Location");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        } else {
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(MainActivity.locations.get(intent.getIntExtra("placeNumber", 0)).latitude);
            placeLocation.setLongitude(MainActivity.locations.get(intent.getIntExtra("placeNumber", 0)).longitude);

            String address = MainActivity.places.get(intent.getIntExtra("placeNumber", 0));

            centerMapOnLocation(placeLocation, MainActivity.places.get(intent.getIntExtra("placeNumber", 0)));
            LatLng gobasLocation = new LatLng(placeLocation.getLatitude(), placeLocation.getLongitude());
            Marker currentMarker = mMap.addMarker(new MarkerOptions().position(gobasLocation).title(address));
            currentMarker.showInfoWindow();
        }

        //FAButton:
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng  = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
                saveLocation(latLng);
            }
        });

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

    @Override
    public void onMapLongClick(LatLng latLng) {
        saveLocation(latLng);
    }

    public void saveLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String address = "";
        String date = "";

        try {

            List<Address> listAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if(listAddresses != null && listAddresses.size() > 0) {
                if (listAddresses.get(0).getThoroughfare() != null) {
                    if (listAddresses.get(0).getSubThoroughfare() != null) {
                        address += listAddresses.get(0).getSubThoroughfare() + " ";
                    }
                    address += listAddresses.get(0).getThoroughfare();
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date += sdf.format(new Date());


        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        MainActivity.places.add(address);
        MainActivity.locations.add(latLng);

//        MainActivity.arrayAdapter.notifyDataSetChanged();

        MainActivity.mName.add("ime-test");
        MainActivity.mAdress.add(address);
        MainActivity.mDate.add(date);
        MainActivity.mVrsteGob.add("vrste-gob-test");

        MainActivity.locationsAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Lokacija je shranjena!", Toast.LENGTH_SHORT).show();
    }
}
