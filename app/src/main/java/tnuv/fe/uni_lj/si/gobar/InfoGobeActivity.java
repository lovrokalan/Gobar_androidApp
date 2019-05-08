package tnuv.fe.uni_lj.si.gobar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

public class InfoGobeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    ListView listView;
    String mTitle[] = {"Jurčki", "Lisičke", "Mušnice", "Lesne"};
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
                if(position==0){
                    Toast.makeText(InfoGobeActivity.this, "Opis Jurckov", Toast.LENGTH_SHORT).show();
                    ShowPopup(listView);
                }
                if(position==1){
                    Toast.makeText(InfoGobeActivity.this, "Opis Lisick", Toast.LENGTH_SHORT).show();
                }
                if(position==2){
                    Toast.makeText(InfoGobeActivity.this, "Opis Musnic", Toast.LENGTH_SHORT).show();
                }
                if(position==3){
                    Toast.makeText(InfoGobeActivity.this, "Opis Lesnih Gob", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void ShowPopup(View v) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.popup1);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btnFollow = (Button) myDialog.findViewById(R.id.btndodaj);
        txtclose.setOnClickListener(new View.OnClickListener() {
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
