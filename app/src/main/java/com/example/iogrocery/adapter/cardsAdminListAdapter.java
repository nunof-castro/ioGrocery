package com.example.iogrocery.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.ProductFragment;
import com.example.iogrocery.R;
import com.example.iogrocery.UrlApi;
import com.example.iogrocery.chargeCardFragment;
import com.example.iogrocery.models.Card;
import com.example.iogrocery.models.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cardsAdminListAdapter extends RecyclerView.Adapter<cardsAdminListAdapter.userViewHolder> implements View.OnClickListener {

    ArrayList<Card> cardholder;
    Context getActivity;

    public cardsAdminListAdapter(ArrayList<Card> cardholder, Context getActivity) {
        this.cardholder = cardholder;
        this.getActivity = getActivity;
    }

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card, parent, false);

        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {
        String idCard=cardholder.get(position).getId();


        holder.name.setText(cardholder.get(position).getName());
        holder.cardId.setText(cardholder.get(position).getId());
        holder.amount.setText(String.valueOf(cardholder.get(position).getAmount()) + " $");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("idCard", idCard);
                AppCompatActivity activity =(AppCompatActivity) view.getContext();
                chargeCardFragment chargeCardFragment=new chargeCardFragment();
                chargeCardFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,chargeCardFragment).addToBackStack(null).commit();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(UrlApi.URL_CARDS+"/"+cardholder.get(position).getId());
            }
        });

    }

    private void deleteUser(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("VolleyDebug", error.getMessage());
                    }
                }){
        };

        queue.add(request);
    }



    @Override
    public int getItemCount() {
       return cardholder.size();
    }

    class userViewHolder extends RecyclerView.ViewHolder{
       TextView cardId,name, amount, delete;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            cardId=itemView.findViewById(R.id.cardId);
            name=itemView.findViewById(R.id.cardName);
            amount=itemView.findViewById(R.id.cardBalance);
            delete=itemView.findViewById(R.id.deleteUser);
        }
    }
}
