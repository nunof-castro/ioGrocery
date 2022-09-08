package com.example.iogrocery.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iogrocery.ProductFragment;
import com.example.iogrocery.R;
import com.example.iogrocery.models.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class homeListAdapter extends RecyclerView.Adapter<homeListAdapter.myViewHolder> implements View.OnClickListener {
    ArrayList<Product> dataholder;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public homeListAdapter(ArrayList<Product> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);

        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        boolean infinityChecker;
        Double priceUnit= dataholder.get(position).getPackPrice()/dataholder.get(position).getPackUnits();
        infinityChecker=priceUnit.isInfinite();

        holder.name.setText(dataholder.get(position).getName());
        if(infinityChecker){
            holder.price.setText(dataholder.get(position).getPackPrice() + " €");
        }else{
            holder.price.setText(df.format(dataholder.get(position).getPackPrice()/dataholder.get(position).getPackUnits()) + " €");
        }
        holder.available.setText("available: " + String.valueOf(dataholder.get(position).getAvailable()));
        Picasso.get().load(String.valueOf(dataholder.get(position).getImg())).into(holder.productImg);

        String idProduto = dataholder.get(position).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("idProduto", idProduto);
                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                ProductFragment productFragment=new ProductFragment();
                productFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,productFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    @Override
    public void onClick(View view) {

    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name, available, price;
        ImageView productImg;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            available=itemView.findViewById(R.id.productAvailability);
            price = itemView.findViewById(R.id.productPrice);
            productImg=itemView.findViewById(R.id.productImg);
        }
    }


}