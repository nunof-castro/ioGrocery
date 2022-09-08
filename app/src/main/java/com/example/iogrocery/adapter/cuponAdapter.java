package com.example.iogrocery.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iogrocery.CartFragment;
import com.example.iogrocery.R;
import com.example.iogrocery.models.Cart;
import com.example.iogrocery.models.Cupon;

import java.util.ArrayList;

public class cuponAdapter extends RecyclerView.Adapter<cuponAdapter.cuponHolder> implements View.OnClickListener {
    ArrayList<Cupon> cuponHolder;

    public cuponAdapter(ArrayList<Cupon> cuponHolder){this.cuponHolder=cuponHolder;}

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public cuponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cupons_card, parent, false);

        return new cuponHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cuponHolder holder, int position) {
        holder.id.setText(cuponHolder.get(position).getId());
        holder.amount.setText(String.valueOf(cuponHolder.get(position).getDiscount()));

        String idCupon = cuponHolder.get(position).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Bundle cuponBundle = new Bundle();
               cuponBundle.putString("idCupon", idCupon);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                CartFragment cartFragment = new CartFragment();
                cartFragment.setArguments(cuponBundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,cartFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cuponHolder.size();
    }

    class cuponHolder extends RecyclerView.ViewHolder{
        TextView id, amount, date;
        public cuponHolder(@NonNull View itemView){
            super(itemView);
            id=itemView.findViewById(R.id.cuponId);
            amount=itemView.findViewById(R.id.cuponAmount);
            date=itemView.findViewById(R.id.cuponExpiration);
        }
    }


}
