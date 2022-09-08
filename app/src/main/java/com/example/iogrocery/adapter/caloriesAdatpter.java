package com.example.iogrocery.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iogrocery.R;
import com.example.iogrocery.models.Calories;
import com.example.iogrocery.models.Purchase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class caloriesAdatpter extends RecyclerView.Adapter<caloriesAdatpter.myViewHolder> implements View.OnClickListener {
    ArrayList<Calories> caloriesRef;


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public caloriesAdatpter(ArrayList<Calories> caloriesRef) {
        this.caloriesRef = caloriesRef;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_calories_card, parent, false);

        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.caloriesQty.setText(caloriesRef.get(position).getQuantity().toString()+" kcal");



    }

    @Override
    public int getItemCount() {
        return caloriesRef.size();
    }

    @Override
    public void onClick(View view) {

    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView caloriesQty;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            caloriesQty=itemView.findViewById(R.id.caloriesInd);
        }
    }


}