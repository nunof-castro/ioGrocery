package com.example.iogrocery.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iogrocery.CartFragment;
import com.example.iogrocery.ProductFragment;
import com.example.iogrocery.R;
import com.example.iogrocery.arrayCart;
import com.example.iogrocery.models.Cart;
import com.example.iogrocery.models.Product;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.myViewHolder>{
    ArrayList<Cart> dataholder;
    public int newQuantity;
    private CartFragment cartFragment;
    public Double semiPrice =0.00;
    arrayCart arrayCart;
    Double teste=0.00;
    Double teste2=0.00;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public cartAdapter(CartFragment cartFragment,ArrayList<Cart> dataholder) {
        this.cartFragment = cartFragment;
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.name.setText(dataholder.get(position).getName());
        holder.price.setText(String.valueOf(df.format(dataholder.get(position).getQuantity()*dataholder.get(position).getUnitPrice())));
        holder.quantity.setText(String.valueOf(dataholder.get(position).getQuantity()));
        Picasso.get().load(dataholder.get(position).getImg()).into(holder.productImg);
        newQuantity =  dataholder.get(position).getQuantity();



        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               newQuantity++;
               arrayCart.semiPrice=0.00;
               dataholder.get(position).getQuantity();
                if(newQuantity<=dataholder.get(position).getAvailable()){

                    holder.increment.setAlpha(1f);
                    holder.decrement.setAlpha(1f);
                    holder.increment.setEnabled(true);
                    holder.decrement.setEnabled(true);
                    holder.quantity.setText(String.valueOf(newQuantity));

                }
                if(newQuantity==dataholder.get(position).getAvailable()){
                    holder.increment.setAlpha(.5f);
                    holder.increment.setEnabled(false);
                }
                dataholder.get(position).setQuantity(newQuantity);
                for(int i =0; i<dataholder.size(); i++){
                    arrayCart.semiPrice+=(dataholder.get(i).getQuantity()*dataholder.get(i).getUnitPrice());
                }


                cartFragment.semiCartPrice.setText(df.format(arrayCart.semiPrice) + " €");
                holder.price.setText(df.format(dataholder.get(position).getUnitPrice()*newQuantity) + " €");

            }

        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newQuantity--;
                arrayCart.semiPrice=0.00;
                dataholder.get(position).getQuantity();
                if (newQuantity==0){
                    holder.decrement.setAlpha(.5f);
                    holder.decrement.setEnabled(false);
                    holder.quantity.setText(String.valueOf(newQuantity));

                }
                if(newQuantity>=1){
                    holder.decrement.setAlpha(1f);
                    holder.increment.setAlpha(1f);
                    holder.increment.setEnabled(true);
                    holder.decrement.setEnabled(true);
                    holder.quantity.setText(String.valueOf(newQuantity));

                }
                dataholder.get(position).setQuantity(newQuantity);
                for(int i =0; i<dataholder.size(); i++){
                    arrayCart.semiPrice+=(dataholder.get(i).getQuantity()*dataholder.get(i).getUnitPrice());
                }
                cartFragment.semiCartPrice.setText(df.format(arrayCart.semiPrice) + " €");
                holder.price.setText(df.format(dataholder.get(position).getUnitPrice()*newQuantity) + " €");
                arrayCart.totalPrice=arrayCart.semiPrice;
            }
        });
        arrayCart.totalPrice=arrayCart.semiPrice;
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }



    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name, quantity, price;
        ImageView productImg;
        ImageButton increment, decrement;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.productCartName);
            price= itemView.findViewById(R.id.productCartPrice);
            quantity=itemView.findViewById(R.id.cartQty);
            productImg=itemView.findViewById(R.id.productCartImg);
            increment=itemView.findViewById(R.id.addCartBtn);
            decrement=itemView.findViewById(R.id.removeCartBtn);

        }
    }


}
