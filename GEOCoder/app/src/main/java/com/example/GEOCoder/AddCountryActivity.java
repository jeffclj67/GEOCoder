package com.example.GEOCoder;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class AddCountryActivity extends Activity implements View.OnClickListener {


    // Widgets
    private Button addTodoBtn, goBackBtn;
    private EditText subjectEditText, subjectEditText2;
    private EditText descEditText, addrEditText;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");
        setContentView(R.layout.activity_add_country);

        // instantiation of widgets
        subjectEditText = findViewById(R.id.subject_edittext);
        descEditText = findViewById(R.id.description_edittext);
        addrEditText = findViewById(R.id.address_edittext);
        addTodoBtn = findViewById(R.id.add_record);
        goBackBtn = findViewById(R.id.go_back);

        dbManager = new DBManager(this);
        dbManager.open();

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(view.getContext(), CountryListActivity.class);
                startActivity(intent2);
                Toast myToast = Toast.makeText(getApplicationContext(), "successful to home page", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get coordinate values from user input
                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();

                Geocoder geocoder = new Geocoder(AddCountryActivity.this);
                try {

                    // use geocoder to convert coordinates into address
                    List<Address> address = geocoder.getFromLocation(Double.parseDouble(name),Double.parseDouble(desc),1);
                    String a = address.get(0).getAddressLine(0);
                    if (TextUtils.isEmpty(name)){
                        subjectEditText.setError("Please enter a value");
                        subjectEditText.requestFocus();
                        return;
                    }else{

                        // update user input coordinates and converted address into database
                        dbManager.insert(name, desc, a);
                        Intent main = new Intent(AddCountryActivity.this,
                                CountryListActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(main);
                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Unable to get street address", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onClick(View view) {

    }

}

