package com.entra.barbers;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;
import com.entra.barbers.models.Booking;
import com.entra.barbers.models.Order;
import com.entra.barbers.models.Service;
import com.entra.barbers.models.Shop;
import com.entra.barbers.utility.FirebaseUtil;
import com.entra.barbers.utility.SchedulingUtil;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class ShopActivity extends AppCompatActivity {
    RangeBar timeRange;
    Shop shop;
    ExpandableLinearLayout maleLayout, femaleLayout;
    RelativeLayout shopCartLayout;
    FlexboxLayout maleFlexbox, femaleFlexbox;
    TextView  shopName, shopAddress, shopPhone;
    ImageView shopNext;
    LinearLayout maleServicesHeader, femaleServicesHeader;

    TextView shopRangeStartTime, shopRangeEndTime;

    LinearLayout shopAvailbleSlot, shopNotAvailableSlot;
    TextView shopStartTimeText, shopEndTimeText;

    TextView cartCount, cartAmount;

    private final String TAG = "ShopActivity";

    ArrayList<Service> cart;
    SchedulingUtil schedulingUtil;

    String startTime="0800", endTime="2000";
    String alottedStartTime="0800", alottedEndTime="0800";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shop = new Shop();
        shop = (Shop) getIntent().getSerializableExtra("shop");
        cart = new ArrayList<>();

        Log.d(TAG, "onCreate: Shop: " + shop.getSevices_m().size());

        timeRange = (RangeBar) findViewById(R.id.time_rangebar);
        shopName = (TextView) findViewById(R.id.shop_name);
        shopAddress = (TextView) findViewById(R.id.shop_address);
        maleLayout = (ExpandableLinearLayout) findViewById(R.id.shop_male_expandable);
        femaleLayout = (ExpandableLinearLayout) findViewById(R.id.shop_female_expandable);
        shopCartLayout = (RelativeLayout) findViewById(R.id.shop_cart);
        maleFlexbox = (FlexboxLayout) findViewById(R.id.shop_male_flexbox);
        femaleFlexbox = (FlexboxLayout) findViewById(R.id.shop_female_flexbox);
        shopNext = (ImageView) findViewById(R.id.shop_cart_next);
        maleServicesHeader = (LinearLayout) findViewById(R.id.male_services_header);
        femaleServicesHeader = (LinearLayout) findViewById(R.id.female_services_header);
        cartCount = (TextView) findViewById(R.id.shop_cart_count);
        shopAvailbleSlot = (LinearLayout) findViewById(R.id.shop_availalbe_layout);
        shopNotAvailableSlot = (LinearLayout) findViewById(R.id.shop_not_available_layout);
        shopStartTimeText = (TextView) findViewById(R.id.shop_slot_start_time);
        shopEndTimeText = (TextView) findViewById(R.id.shop_slot_end_time);
        shopPhone = (TextView) findViewById(R.id.shop_phone);
        cartAmount = (TextView) findViewById(R.id.shop_cart_price);
        shopRangeStartTime = (TextView) findViewById(R.id.shop_range_start_time);
        shopRangeEndTime = (TextView) findViewById(R.id.shop_range_end_time);

        shopCartLayout.setVisibility(View.GONE);

        shopName.setText(shop.getName());
        shopAddress.setText(shop.getAddress());
        shopPhone.setText(shop.getPhone());

        schedulingUtil = new SchedulingUtil();

        final ArrayList<Service> maleServices = shop.getSevices_m();
        final ArrayList<Service> femaleServices = shop.getServices_f();

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#efefef"))
                .uncheckedTextColor(Color.parseColor("#666666"))
                .useInsetPadding(true);

        ChipCloud maleChips = new ChipCloud(this, maleFlexbox, config);
        for(Service service: maleServices){
            maleChips.addChip(service.getName());
        }

        ChipCloud femaleChips = new ChipCloud(this, femaleFlexbox, config);
        for(Service service: femaleServices){
            femaleChips.addChip(service.getName());
        }

        timeRange.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                int startTimeHours = leftPinIndex/2 + 8;
                int startTimeMinutes = (leftPinIndex%2)*30;

                int endTimeHours = rightPinIndex/2 + 8;
                int endTimeMinutes = (rightPinIndex%2)*30;

                startTime = String.valueOf(startTimeHours < 10 ? "0" + startTimeHours : startTimeHours) + String.valueOf(startTimeMinutes < 10 ? "0" + startTimeMinutes : startTimeMinutes) + "";
                endTime = String.valueOf(endTimeHours < 10 ? "0" + endTimeHours : endTimeHours) + String.valueOf(endTimeMinutes < 10 ? "0" + endTimeMinutes : endTimeMinutes) + "";

                shopRangeStartTime.setText(convert24to12(startTime));
                shopRangeEndTime.setText(convert24to12(endTime));

                setSlots();
            }
        });

        Booking tempBook = new Booking();
        tempBook.setStartTime("0000");
        tempBook.setEndTime("0800");
        tempBook.setBookingId("0000");

        shop.getBookings().add(tempBook);

        Booking tempBook2 = new Booking();
        tempBook2.setStartTime("2000");
        tempBook2.setEndTime("2359");
        tempBook2.setBookingId("0000");

        shop.getBookings().add(tempBook2);

        setSlots();

//        timeRange.setFormatter(new IRangeBarFormatter() {
//            @Override
//            public String format(String value) {
//                int time = Integer.parseInt(value);
//
//                int hours = time/2 + 8;
//                int minutes = (time%2)*30;
//
//                String result="";
//                if(hours<10) result += "0" + hours;
//                else if(hours > 12) result += (hours-12);
//                else result += hours;
//
//                if(minutes==0) result += ":00";
//                else result += ":30";
////                Log.d(TAG, "format: " + result);
//                return value;
////                return result;
//            }
//        });

        maleServicesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleLayout.toggle();
            }
        });

        femaleServicesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleLayout.toggle();
            }
        });


        ChipListener maleChipListener = new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b1 && b){
                    cart.add(maleServices.get(i));
                    shopCartLayout.setVisibility(View.VISIBLE);
//                    cartCount.setText("Items: " + cart.size()+"");
                    setSlots();
                    setCartLayout();
                } else if(b1 && !b){
                    cart.remove(maleServices.get(i));
//                    cartCount.setText("Items: " + cart.size()+"");
                    if(cart.size()==0) shopCartLayout.setVisibility(View.GONE);
                    setSlots();
                    setCartLayout();
                }
            }
        };

        ChipListener femaleChipListener = new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b1 && b){
                    cart.add(femaleServices.get(i));
                    shopCartLayout.setVisibility(View.VISIBLE);
//                    cartCount.setText("Items: " + cart.size()+"");
                    setSlots();
                    setCartLayout();
                } else if(b1 && !b){
                    cart.remove(femaleServices.get(i));
                    if(cart.size()==0) shopCartLayout.setVisibility(View.GONE);
//                    cartCount.setText("Items: " + cart.size()+"");
                    setSlots();
                    setCartLayout();
                }
            }
        };

        maleChips.setListener(maleChipListener);
        femaleChips.setListener(femaleChipListener);

        shopCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasty.success(ShopActivity.this, "Order done", Toast.LENGTH_SHORT).show();
//                placeOrder();
                Intent in = new Intent(ShopActivity.this, CartSummaryActivity.class);
                in.putExtra("cart", cart);
                in.putExtra("shop", shop);
                in.putExtra("startTime", alottedStartTime);
                in.putExtra("endTime", alottedEndTime);
                startActivity(in);
            }
        });

    }

    private void setSlots(){

        int tempTime=0;
        for(Service serv: cart){
            tempTime += serv.getTime_mins();
        }
        Log.d(TAG, "onRangeChangeListener: START_TIME: " + startTime + " " + "ENDTIME: " + endTime + " TOTALTIME: " + tempTime);

        Pair<String, String> allottedTime = schedulingUtil.getTimeAlloted(shop.getBookings(), startTime, tempTime);
        setSlotAvailableLayout(allottedTime);
        alottedStartTime = allottedTime.first;
        alottedEndTime = allottedTime.second;
    }


    private void setSlotAvailableLayout(Pair<String, String> times){
        if(times==null){
            shopAvailbleSlot.setVisibility(View.GONE);
            shopNotAvailableSlot.setVisibility(View.VISIBLE);
        } else{
            shopAvailbleSlot.setVisibility(View.VISIBLE);
            shopNotAvailableSlot.setVisibility(View.GONE);

            shopStartTimeText.setText(convert24to12(times.first));
            shopEndTimeText.setText(convert24to12(times.second));
        }
    }

    private void setCartLayout(){
        int amount =0;
        int time_mins =0;

        for(Service temp : cart){
            amount += temp.getPrice();
            time_mins += temp.getTime_mins();
        }
        int hours=time_mins/60;
        int minutes = time_mins%60;

        cartAmount.setText("Rs. "+amount+"");
        cartCount.setText("Cart(" + cart.size() + ")" + " - " + String.valueOf(hours>0 ? hours + "h " : "") + String.valueOf(minutes>0 ? minutes + "m" : "") );


    }

    private String convert24to12(String time24){
        DateFormat f1 = new SimpleDateFormat("HHmm");
        Date d = null;
        try {
            d = f1.parse(time24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("h:mma");
        return f2.format(d).toUpperCase();
    }

}
