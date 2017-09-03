package com.entra.barbers.utility;

import android.util.Log;

import com.entra.barbers.adapters.ShopListAdapter;
import com.entra.barbers.models.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private static final String TAG = "FirebaseUTIL";

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

    public FirebaseUser getUser(){
        return mAuth.getCurrentUser();
    }



}
