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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
        shopList = new ArrayList<>();

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

        getShops(adapter);

        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(MainActivity.this, ShopActivity.class);
                in.putExtra("shop", shopList.get(position));
                startActivity(in);
            }
        });

    }


    public void getShops(final ShopListAdapter adapter){

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: "  + dataSnapshot.getValue());
                Shop shop = dataSnapshot.getValue(Shop.class);
                shopList.add(shop);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                shopList.remove(dataSnapshot.getValue(Shop.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("/shops");
        mRef.addChildEventListener(childEventListener);

    }
}
