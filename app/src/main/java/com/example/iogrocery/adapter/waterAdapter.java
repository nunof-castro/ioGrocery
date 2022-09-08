package com.example.iogrocery.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iogrocery.CartFragment;
import com.example.iogrocery.R;
import com.example.iogrocery.WaterFragment;
import com.example.iogrocery.models.Cart;
import com.example.iogrocery.models.Water;

import org.json.JSONArray;

import java.util.ArrayList;

public class waterAdapter extends RecyclerView.Adapter<waterAdapter.waterHolder> implements View.OnClickListener {
    ArrayList<Water> waterHolder;


    public waterAdapter(ArrayList<Water> waterHolder) {
        this.waterHolder = waterHolder;
    }

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public waterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.water_card, parent, false);

        return new waterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull waterHolder holder, int position) {
       holder.waterNum.setText(String.valueOf(waterHolder.get(position).getQuantity() + " liters"));

    }

    @Override
    public int getItemCount() {
        return waterHolder.size();
    }

    class waterHolder extends RecyclerView.ViewHolder{
        TextView waterNum;
        public waterHolder(@NonNull View itemView){
            super(itemView);
            waterNum=itemView.findViewById(R.id.waterNum);
        }
    }
}
