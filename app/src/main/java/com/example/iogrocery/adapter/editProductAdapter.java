package com.example.iogrocery.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iogrocery.EditProductFragment;
import com.example.iogrocery.ProductFragment;
import com.example.iogrocery.R;
import com.example.iogrocery.models.Product;
import com.example.iogrocery.models.Purchase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class editProductAdapter extends RecyclerView.Adapter<editProductAdapter.myViewHolder> implements View.OnClickListener {
    ArrayList<Product> allProducts;
    Button setQtBtn;
    EditText newQuantity;


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public editProductAdapter(ArrayList<Product> allProducts) {
        this.allProducts = allProducts;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_edit_product_card, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.name_product.setText("Name" + allProducts.get(position).getName());
        holder.quantity_product.setText("Quantity:" + allProducts.get(position).getAvailable());
        String name_product = allProducts.get(position).getName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle passProdName = new Bundle();
                passProdName.putString("name_product", name_product);
                Log.d("name_product", "onClick: "+name_product);
                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                EditProductFragment editProductFragment=new EditProductFragment();
                editProductFragment.setArguments(passProdName);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,editProductFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    @Override
    public void onClick(View view) {

    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView name_product, quantity_product;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name_product = itemView.findViewById(R.id.name_product);
            quantity_product = itemView.findViewById(R.id.quantity_product);






        }
    }


}