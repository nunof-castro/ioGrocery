package com.example.iogrocery;

import android.app.Person;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.homeListAdapter;

import com.example.iogrocery.adapter.purchaseListAdapter;
import com.example.iogrocery.models.Product;
import com.example.iogrocery.models.Purchase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class PurchasesAdminFragment extends Fragment {
    JSONObject purchasesObj;
    JSONObject product;
    ArrayList<Purchase> purchasesRecord= new ArrayList<>();
    JSONArray products = new JSONArray();
    JSONObject productObj;
    RecyclerView recyclerView;
    ImageButton purchaseRecPageBack;
    String access_token;
    public PurchasesAdminFragment() {
        // Required empty public constructor
    }
    public static PurchasesAdminFragment newInstance(String param1, String param2) {
        PurchasesAdminFragment fragment = new PurchasesAdminFragment();

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_purchases_admin, container, false);
        getRecord(UrlApi.PURCHASES_RECORD);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        access_token = sharedPref.getString("access_token", default_value);
        recyclerView = view.findViewById(R.id.purchaseList);
        purchaseRecPageBack = view.findViewById(R.id.purchaseRecPageBack);
        purchaseRecPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(adminPanelFragment.newInstance("",""));
            }
        });
        return view;

    }
    private void getRecord(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("purchasesObj", "purchaseObj: "+response.length());
                            for (int i = 0; i < response.length(); i++) {
                                purchasesObj = response.getJSONObject(i);
                                String purchaseid = purchasesObj.getString("_id");
                                String purchaseCard = purchasesObj.getString("card");
                                String balance = (purchasesObj.getString("balance"));

                                  products =  purchasesObj.getJSONArray("products");
                                String purchaseDate = purchasesObj.getString("date");

                                Purchase purchase = new Purchase(purchaseid,purchaseCard,balance,purchaseDate,products);
                                Log.d("purchase", "onResponse: "+purchase);
                                purchasesRecord.add(purchase);

                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(new purchaseListAdapter(purchasesRecord));




                        } catch (JSONException e){
                            Log.d("VolleyDebug", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("VolleyDebug", error.getMessage());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+ access_token );
                Log.d("Authorization", "token entrou ");
                return headers;
            }
        };

        queue.add(request);
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "fragment_purchases_admin");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}