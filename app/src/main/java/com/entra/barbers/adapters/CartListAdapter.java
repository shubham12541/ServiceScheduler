package com.entra.barbers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.entra.barbers.R;
import com.entra.barbers.models.Service;

import java.util.ArrayList;

/**
 * Created by I334104 on 9/5/2017.
 */

public class CartListAdapter extends BaseAdapter {

    ArrayList<Service> cartList;
    Context c;

    public CartListAdapter(Context c, ArrayList<Service> cartList){
        this.c = c;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int i) {
        return cartList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.cart_item, viewGroup, false);

        TextView name = (TextView) root.findViewById(R.id.cart_item_name);
        TextView amount = (TextView) root.findViewById(R.id.cart_item_price);
        TextView time = (TextView) root.findViewById(R.id.cart_item_time);

        name.setText(cartList.get(i).getName());
        amount.setText(cartList.get(i).getPrice() + "");
        time.setText(cartList.get(i).getTime_mins() + " mins");

        root.setClickable(false);

        return root;
    }
}
