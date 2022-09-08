package com.example.iogrocery.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iogrocery.ProductFragment;
import com.example.iogrocery.PurchasesAdminFragment;
import com.example.iogrocery.R;
import com.example.iogrocery.models.Product;
import com.example.iogrocery.models.Purchase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class purchaseListAdapter extends RecyclerView.Adapter<purchaseListAdapter.myViewHolder> implements View.OnClickListener {
    ArrayList<Purchase> purchaseList= new ArrayList<>();


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public purchaseListAdapter(ArrayList<Purchase> purchaseList) {
        this.purchaseList = purchaseList;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_card, parent, false);

        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.purchaseId.setText(purchaseList.get(position).getPurchaseid());
        holder.purchaseCard.setText("Card Id: "+ purchaseList.get(position).getPurchaseCard());
        holder.balance.setText("Balance: "+purchaseList.get(position).getBalance());
        holder.purcahseDate.setText("Date: "+purchaseList.get(position).getPurchaseDate());
        String arrayHolder;
        Log.d("arrayHolder", "onBindViewHolder: "+ purchaseList.get(position).getProducts());


    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    @Override
    public void onClick(View view) {

    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView purchaseId, purchaseCard, balance,productId,purcahseDate;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            purchaseId=itemView.findViewById(R.id.purchaseId);
            purchaseCard=itemView.findViewById(R.id.purchaseCard);
            balance = itemView.findViewById(R.id.balance);

            purcahseDate=itemView.findViewById(R.id.purcahseDate);
        }
    }


}