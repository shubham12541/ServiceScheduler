package com.entra.barbers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.entra.barbers.adapters.ShopListAdapter;
import com.entra.barbers.models.Shop;
import com.entra.barbers.utility.FirebaseUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    EditText searchList;
    ListView shopListView;

    ArrayList<Shop> shopList;
    FirebaseUtil firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebase = new FirebaseUtil();

        //TODO: Fetch all shops

        searchList = (EditText) findViewById(R.id.search_barbers);
        shopListView = (ListView) findViewById(R.id.barber_list);

        final ShopListAdapter adapter = new ShopListAdapter(MainActivity.this, shopList);

        shopListView.setAdapter(adapter);

        searchList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " + s.toString());
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        firebase.getShops(adapter);

        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(MainActivity.this, ShopActivity.class);
                in.putExtra("shop", shopList.get(position));
                startActivity(in);
            }
        });

    }
}
