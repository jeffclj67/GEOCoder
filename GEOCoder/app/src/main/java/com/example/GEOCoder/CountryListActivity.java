package com.example.GEOCoder;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;


public class CountryListActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NOTE = 1;

    private DBManager dbManager;
    private ListView listView,listView2;
    private SimpleCursorAdapter adapter, adapter2;

    ArrayList<String> listItem;

    final String[] from = new String[] {DatabaseHelper._ID,
        DatabaseHelper.LAT, DatabaseHelper.LONG, DatabaseHelper.ADD};

    final int[] to = new int[] {R.id.id, R.id.title, R.id.desc, R.id.addr};



    @Override

    // opens the home screen of the app, display list views
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_emp_list);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listItem = new ArrayList<>();


        // setup listview for whole database and queried result based on user search
        listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));
        listView2 = findViewById(R.id.list_view);

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record,
                cursor, from, to, 0);

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // user search address in the search bar, and it will update the listview with result
        EditText inputSearch = findViewById(R.id.searchFilter);
        inputSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = inputSearch.getText().toString(); // the keyword user typed in

                // some toast messages for troubleshooting.
                Toast myToast2 = Toast.makeText(getApplicationContext(), "your input is: " + nameString, Toast.LENGTH_SHORT);
                myToast2.show();
                int count = dbManager.fetch().getCount();
                Toast myToast3 = Toast.makeText(getApplicationContext(), "count: " + count, Toast.LENGTH_SHORT);
                myToast3.show();
                int i2 = dbManager.searchAddr(nameString).getCount();
                Toast myToast4 = Toast.makeText(getApplicationContext(), "query: " + i2, Toast.LENGTH_SHORT);
                myToast4.show();

                // update the search result listview with cursor
                Cursor c2 = dbManager.searchAddr(nameString);
                adapter2 = new SimpleCursorAdapter(getApplicationContext(), R.layout.activity_view_record,
                        c2, from, to, 0);

                adapter2.notifyDataSetChanged();

                listView2.setAdapter(adapter2);


            }
        });


        // OnClickListener for List Items:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long Viewid) {
                TextView idTextView = view.findViewById(R.id.id);
                TextView titleTextView = view.findViewById(R.id.title);
                TextView descTextView = view.findViewById(R.id.desc);
                TextView addrTextView = view.findViewById(R.id.addr);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString();
                String addr = addrTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyCountryActivity.class);

                modify_intent.putExtra("title", title);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("addr", addr);
                startActivity(modify_intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.item_search);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.go_to_add){
            Intent add_mem = new Intent(this, AddCountryActivity.class);
            startActivity(add_mem);
        } else if(id == R.id.pop){
            Geocoder geocoder = new Geocoder(CountryListActivity.this);
            try{

                // the 15 coordinates pre-loaded in the app
                String[][] addArray = {
                        {"47.6062083", "122.33207"},
                        {"43.653225", "-79.383186"},
                        {"36.015991", "-114.739299"},
                        {"35.7091798", "139.7197213"},
                        {"43.4742089", "-80.5455296"},
                        {"45.4211435", "-75.6900574"},
                        {"45.5031824","-73.5698065"},
                        {"49.2608724", "-123.113952"},
                        {"51.1777781", "-115.5682504"},
                        {"51.0460954", "-114.065465"},
                        {"37.5385087", "-77.43428"},
                        {"43.04929", "-79.11958"},
                        {"52.4760892", "-71.8258668"},
                        {"43.8975558", "-78.8635324"},
                        {"42.8975558", "-79.8635324"}

                };
                for (int i = 0; i < 16; i++){

                    // find the addresses for pre-loaded coordinates
                    List<Address> address = geocoder.getFromLocation(Double.parseDouble(addArray[i][0]),Double.parseDouble(addArray[i][1]),1);
                    String a = address.get(0).getAddressLine(0);
                    dbManager.insert(addArray[i][0], addArray[i][1], a);
                }

            }catch (Exception e){
                Toast.makeText(this, "Unable to get street address", Toast.LENGTH_SHORT).show();
            }

            Intent main = new Intent(CountryListActivity.this,
                    CountryListActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }


}

