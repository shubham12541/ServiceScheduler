package com.entra.barbers;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;
import com.entra.barbers.models.Order;
import com.entra.barbers.models.Service;
import com.entra.barbers.models.Shop;
import com.entra.barbers.utility.FirebaseUtil;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class ShopActivity extends AppCompatActivity {
    RangeBar timeRange;
    Shop shop;
    ExpandableLinearLayout maleLayout, femaleLayout, shopCartLayout;
    FlexboxLayout maleFlexbox, femaleFlexbox;
    TextView shopNext, shopName, shopAddress;
    LinearLayout maleServicesHeader, femaleServicesHeader;

    private final String TAG = "ShopActivity";

    ArrayList<Service> cart;

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
        shopCartLayout = (ExpandableLinearLayout) findViewById(R.id.shop_cart);
        maleFlexbox = (FlexboxLayout) findViewById(R.id.shop_male_flexbox);
        femaleFlexbox = (FlexboxLayout) findViewById(R.id.shop_female_flexbox);
        shopNext = (TextView) findViewById(R.id.shop_cart_next);
        maleServicesHeader = (LinearLayout) findViewById(R.id.male_services_header);
        femaleServicesHeader = (LinearLayout) findViewById(R.id.female_services_header);

        shopCartLayout.collapse();

        shopName.setText(shop.getName());
        shopAddress.setText(shop.getAddress());


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
//        maleChips.addChips(shop.getServices_m());

        ChipCloud femaleChips = new ChipCloud(this, femaleFlexbox, config);
        for(Service service: femaleServices){
            femaleChips.addChip(service.getName());
        }

//        femaleChips.addChips(shop.getServices_f());

        timeRange.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                int startTimeHours = leftPinIndex/2 + 8;
                int startTimeMinutes = (leftPinIndex%2)*30;

                int endTimeHours = rightPinIndex/2 + 8;
                int entTimeMinutes = (rightPinIndex%2)*30;

            }
        });

        timeRange.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String value) {
                int time = Integer.parseInt(value);

                int hours = time/2 + 8;
                int minutes = (time%2)*30;

                String result="";
                if(hours<10) result += "0" + hours;
                else if(hours > 12) result += (hours-12);
                else result += hours;

                if(minutes==0) result += ":00";
                else result += ":30";

                return result;
            }
        });

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
                    shopCartLayout.expand();
                } else if(b1 && !b){
                    cart.remove(maleServices.get(i));
                    if(cart.size()==0) shopCartLayout.collapse();
                }
            }
        };

        ChipListener femaleChipListener = new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b1 && b){
                    cart.add(femaleServices.get(i));
                    shopCartLayout.expand();
                } else if(b1 && !b){
                    cart.remove(femaleServices.get(i));
                    if(cart.size()==0) shopCartLayout.collapse();
                }
            }
        };

        maleChips.setListener(maleChipListener);
        femaleChips.setListener(femaleChipListener);

        shopNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(ShopActivity.this, "Order done", Toast.LENGTH_SHORT).show();
                placeOrder();
            }
        });

    }

    private void placeOrder(){
        FirebaseUtil firebase = new FirebaseUtil();
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

        Order order = new Order();
        order.setUserId(userId);
        order.setAmount(amount);
        order.setTime_mins(time);
        order.setServices(names);

        database.push().setValue(order);
    }
}
