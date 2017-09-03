package com.entra.barbers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.entra.barbers.R;
import com.entra.barbers.models.Shop;

import java.util.ArrayList;

/**
 * Created by shubham on 03/09/17.
 */

public class ShopListAdapter extends BaseAdapter implements Filterable{
    private Context c;
    ArrayList<Shop> shops;
    ShopFilter shopFilter;

    public ShopListAdapter(Context c, ArrayList<Shop> shops){
        this.c = c;
        this.shops = shops;
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public Object getItem(int position) {
        return shops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.barber_list_item, parent, false);
        }

        ImageView image;
        TextView name, address, likes, reviews;

        image = (ImageView) view.findViewById(R.id.shop_list_pic);
        name = (TextView) view.findViewById(R.id.shop_item_name);
        address = (TextView) view.findViewById(R.id.shop_item_address);
        likes = (TextView) view.findViewById(R.id.shop_item_likes);
        reviews = (TextView) view.findViewById(R.id.shop_item_reviews);

        name.setText(shops.get(position).getName());
        address.setText(shops.get(position).getAddress());
        likes.setText(shops.get(position).getLikes() + "");
        reviews.setText(shops.get(position).getReviews().length + "");

        return view;
    }

    @Override
    public Filter getFilter() {
        if(shopFilter==null){
            shopFilter = new ShopFilter();
        }
        return shopFilter;
    }


    private class ShopFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if(constraint==null || constraint.length()==0){
                filterResults.values = shops;
                filterResults.count = shops.size();
            } else{
                ArrayList<Shop> filteredShops = new ArrayList<>();

                for(Shop shop: shops){
                    if(shop.getName().toUpperCase().contains(constraint.toString().toUpperCase())){
                        filteredShops.add(shop);
                    }
                }

                filterResults.values = filteredShops;
                filterResults.count = filteredShops.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count==0){
                notifyDataSetInvalidated();
            } else{
                shops = (ArrayList<Shop>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}


