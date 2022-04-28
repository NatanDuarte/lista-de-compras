package com.natanduarte.listadecompras.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.natanduarte.listadecompras.R;
import com.natanduarte.listadecompras.model.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private final List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return this.products.size();
    }

    @Override
    public Product getItem(int position) {
        return this.products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.products.get(position).getId();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View newView = LayoutInflater
                .from(context)
                .inflate(R.layout.cart_item, viewGroup, false);

        TextView itemName = newView.findViewById(R.id.product_name);
        TextView itemAmount = newView.findViewById(R.id.product_amount);

        itemName.setText(products.get(position).getName());
        itemAmount.setText(String.format("%02d", products.get(position).getQuantity()));
        return newView;
    }

    public void remove(Product product) {
        this.products.remove(product);
    }

    public void clear() {
        this.products.clear();
    }
}
