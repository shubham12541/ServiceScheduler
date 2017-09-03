package com.entra.barbers.utility;

import com.entra.barbers.adapters.ShopListAdapter;
import com.entra.barbers.models.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 02/09/17.
 */

public class FirebaseUtil {

    FirebaseDatabase database;
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    public FirebaseUtil(){
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public DatabaseReference getDatabaseRef(){
        mRef = database.getReference();
        return mRef;
    }

    public FirebaseAuth getAuth(){
        return mAuth;
    }

    public List<Shop> getShops(final ShopListAdapter adapter){
        final ArrayList<Shop> shops = new ArrayList<>();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shop shop = dataSnapshot.getValue(Shop.class);
                shops.add(shop);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                shops.remove(dataSnapshot.getValue(Shop.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef = database.getReference("/shops");
        return shops;
    }

}
