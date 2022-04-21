package com.example.giuaky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class DetailListViewAdapter extends ArrayAdapter<DayDetails> {

    private Context context;
    private ArrayList<DayDetails> detailsList;
    private int itemLayout;

    public DetailListViewAdapter(@NonNull Context context, int resource, ArrayList<DayDetails> detailsList) {
        super(context, resource, detailsList);
        this.context = context;
        this.detailsList = detailsList;
        this.itemLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            v=inflater.inflate(this.itemLayout, null);
        }

        DayDetails d = this.detailsList.get(position);
        if(d!=null){

            ImageView imgProduct;
            TextView txtName, txtPrice, txtGoodProduct, txtBadProduct, txtTotalPrice;

            imgProduct = v.findViewById(R.id.img_product);
            txtName = v.findViewById(R.id.txt_detail_product_name);
            txtPrice = v.findViewById(R.id.txt_detail_product_price);
            txtGoodProduct = v.findViewById(R.id.txt_detail_good_product);
            txtBadProduct = v.findViewById(R.id.txt_detail_bad_product);
            txtTotalPrice = v.findViewById(R.id.txt_detail_total_price);

            txtName.setText(d.getProduct().getName());
            txtPrice.setText(d.getProduct().getPrice()+"");
            txtGoodProduct.setText(d.getNumbertGood()+"");
            txtBadProduct.setText(d.getNumberBad()+"");
            txtTotalPrice.setText(d.getTotalPrice()+"");

            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadius(15)
                    .oval(false)
                    .build();
            Picasso.get()
                    .load(d.getProduct().getImage())
                    .transform(transformation)
                    .fit()
                    .into(imgProduct);

        }

        return v;
    }
}
