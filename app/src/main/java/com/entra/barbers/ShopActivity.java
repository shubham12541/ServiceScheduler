package com.entra.barbers;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;
import com.entra.barbers.models.Shop;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class ShopActivity extends AppCompatActivity {
    RangeBar timeRange;
    Shop shop;
    ExpandableLinearLayout maleLayout, femaleLayout, shopCartLayout;
    FlexboxLayout maleFlexbox, femaleFlexbox;

    ArrayList<String> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shop = (Shop) getIntent().getSerializableExtra("shop");
        cart = new ArrayList<>();

        timeRange = (RangeBar) findViewById(R.id.time_rangebar);
        maleLayout = (ExpandableLinearLayout) findViewById(R.id.shop_male_expandable);
        femaleLayout = (ExpandableLinearLayout) findViewById(R.id.shop_female_expandable);
        shopCartLayout = (ExpandableLinearLayout) findViewById(R.id.shop_cart);
        maleFlexbox = (FlexboxLayout) findViewById(R.id.shop_male_flexbox);
        femaleFlexbox = (FlexboxLayout) findViewById(R.id.shop_female_flexbox);

        String[] maleServices = shop.getServices_m();
        String[] femaleServices = shop.getServices_f();

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#ddaa00"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#efefef"))
                .uncheckedTextColor(Color.parseColor("#666666"))
                .useInsetPadding(true);

        ChipCloud maleChips = new ChipCloud(this, maleFlexbox, config);
        maleChips.addChips(shop.getServices_m());

        ChipCloud femaleChips = new ChipCloud(this, femaleFlexbox, config);
        femaleChips.addChips(shop.getServices_f());

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

        maleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleLayout.toggle();
            }
        });

        femaleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleLayout.toggle();
            }
        });

        maleChips.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b1 && b){
                    cart.add(shop.getServices_m()[i]);
                } else if(b1 && !b){
                    cart.remove(shop.getServices_m()[i]);
                }
            }
        });

        femaleChips.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int i, boolean b, boolean b1) {
                if(b1 && b){
                    cart.add(shop.getServices_m()[i]);
                } else if(b1 && !b){
                    cart.remove(shop.getServices_m()[i]);
                }
            }
        });

    }
}
