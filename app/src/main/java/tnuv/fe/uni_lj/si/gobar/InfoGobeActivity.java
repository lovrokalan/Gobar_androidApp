package tnuv.fe.uni_lj.si.gobar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class InfoGobeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    ListView listView;
    String mTitle[] = {"Jurčki", "Lisičke", "Mušnice", "Lesne"};
    String latinNameArray[] = {"lalatin", "lalatin", "lalatin", "lalatin"};
    String mDescription[] = {"opis1", "opis2", "opis3", "opis4"};
    int images[]= {R.drawable.jurcki, R.drawable.lisicke, R.drawable.musnice, R.drawable.lesne};
    MyAdapter adapter;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_gobe);
        initInstances();

//        EditText theFilter = (EditText) findViewById(R.id.searchFilter);
        listView = findViewById(R.id.listView);

        myDialog = new Dialog(this);
        adapter = new MyAdapter(this, mTitle, mDescription, images);

        listView.setAdapter(adapter);

//        theFilter.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                (InfoGobeActivity.this).adapter.getFilter().filter(s);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopup(listView, position);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();

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
    }

    public void ShowPopup(View v, final int position) {
        TextView name;
        TextView latinName;
        TextView opis;

        myDialog.setContentView(R.layout.popup1);
        ImageView imageclose = (ImageView) myDialog.findViewById(R.id.imageViewClose);
        imageclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        name = (TextView) myDialog.findViewById(R.id.nameText);
        name.setText(mTitle[position]);

        latinName = (TextView) myDialog.findViewById(R.id.latinNameText);
        latinName.setText(latinNameArray[position]);

        opis = (TextView) myDialog.findViewById(R.id.textOpis);
        opis.setText(mDescription[position]);

        ImageView image = myDialog.findViewById(R.id.imageView);
        image.setImageResource(images[position]);

        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter(Context c, String title[], String description[], int imgs[]) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context=c;
            this.rTitle=title;
            this.rDescription=description;
            this.rImgs=imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);

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
