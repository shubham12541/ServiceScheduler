package com.entra.barbers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.entra.barbers.adapters.CartListAdapter;
import com.entra.barbers.adapters.ShopListAdapter;
import com.entra.barbers.models.Booking;
import com.entra.barbers.models.Order;
import com.entra.barbers.models.Service;
import com.entra.barbers.models.Shop;
import com.entra.barbers.utility.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class CartSummaryActivity extends AppCompatActivity {
    TextView cartTotalTime, cartTotalAmount;
    ListView shopitems;
    Button cartPlaceOrder;
    CartListAdapter adapter;

    Shop shop;
    String startTime, endTime;

    private final static String TAG = "CartSummartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_summary);

        cartTotalAmount = (TextView) findViewById(R.id.cart_total_amount);
        cartTotalTime = (TextView) findViewById(R.id.cart_total_time);
        cartPlaceOrder = (Button) findViewById(R.id.cart_place_order);
        shopitems = (ListView) findViewById(R.id.shop_items);

        final ArrayList<Service> cartItems = (ArrayList<Service>) getIntent().getSerializableExtra("cart");
        shop = (Shop) getIntent().getSerializableExtra("shop");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");

        adapter = new CartListAdapter(CartSummaryActivity.this, cartItems);

        shopitems.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        cartPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder(cartItems);
            }
        });

        int totalAmount=0, totalTime=0;
        for(Service item: cartItems){
            totalAmount += item.getPrice();
            totalTime += item.getTime_mins();
        }

        cartTotalAmount.setText("Rs. " + totalAmount);
        cartTotalTime.setText("(" + totalTime + " mins)");

    }


    private void placeOrder(ArrayList<Service> cart){
        final FirebaseUtil firebase = new FirebaseUtil();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("/orders");
        String userId = firebase.getUser().getUid();

        int amount=0;
        int time=0;
        ArrayList<String> names = new ArrayList<>();
        for(Service service: cart){
            amount += service.getPrice();
            time += service.getTime_mins();
            names.add(service.getName());
        }

        final Booking booking = new Booking();
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setDate("05-09-2017");

        shop.getBookings().add(booking);

        final ArrayList<Booking> allBookings = new ArrayList<>();
        for(Booking temp: shop.getBookings()){
            if(!temp.getBookingId().equals("0000")){
                allBookings.add(temp);
            }
        }


        Order order = new Order();
        order.setUserId(userId);
        order.setAmount(amount);
        order.setTime_mins(time);
        order.setServices(names);
        order.setStartTime(startTime);
        order.setEndTime(endTime);

        database.push().setValue(order)
                .addOnCompleteListener(CartSummaryActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toasty.success(CartSummaryActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(CartSummaryActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(CartSummaryActivity.this, "Order Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        DatabaseReference database2 = (DatabaseReference) FirebaseDatabase.getInstance().getReference("/shops");
        Query query = database2.orderByChild("id").equalTo(shop.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot data: dataSnapshot.getChildren()){
                                data.getRef().child("bookings").setValue(allBookings);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void updateBookings(){

    }
}
