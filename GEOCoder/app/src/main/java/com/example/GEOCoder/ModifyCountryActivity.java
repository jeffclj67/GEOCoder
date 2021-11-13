package com.example.GEOCoder;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ModifyCountryActivity extends Activity implements View.OnClickListener {


    //widgets

    private EditText titleText, titleText2;
    private Button updateBtn, deleteBtn;
    private EditText descText, addrText;

    private long _id;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Record");
        setContentView(R.layout.activity_modify_country);

        dbManager = new DBManager(this);
        dbManager.open();

        titleText = findViewById(R.id.subject_edittext);
        descText= findViewById(R.id.description_edittext);
        addrText=findViewById(R.id.address_edittext);

        updateBtn = findViewById(R.id.btn_update);
        deleteBtn = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String addr = intent.getStringExtra("addr");


        _id = Long.parseLong(id);

        titleText.setText(name);
        descText.setText(desc);
        addrText.setText(addr);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_update:

                // get the values of updated coordinates
                String title = titleText.getText().toString();
                String desc = descText.getText().toString();
                String addr = addrText.getText().toString();

                Geocoder geocoder = new Geocoder(ModifyCountryActivity.this);
                try {

                    // find the address of the updated coordinates and update the database
                    List<Address> address = geocoder.getFromLocation(Double.parseDouble(title),Double.parseDouble(desc),1);
                    String a = address.get(0).getAddressLine(0);
                    dbManager.update(_id, title, desc, a);
                } catch (IOException e){
                    Toast.makeText(getApplicationContext(), "Unable to get street address", Toast.LENGTH_SHORT).show();
                }

                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;


        }
    }

    public void returnHome(){
        Intent home_intent = new Intent(getApplicationContext(),
                CountryListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

}