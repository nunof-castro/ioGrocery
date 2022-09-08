package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.cartAdapter;
import com.example.iogrocery.models.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.CollationElementIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CartFragment extends Fragment implements View.OnClickListener {

    JSONObject productCheckOut;
    JSONObject allCheckout;
    ArrayList<Cart> dataholder = arrayCart.Products;
    RecyclerView recyclerView;
    String access_token;

    TextView cartDiscount;
    public TextView finalPrice, semiCartPrice;
    ImageButton backBtn, increment, decrement;
    Button checkoutBtn;
    public Double semiPrice = 0.0, productPrice;
    public String idCupon="";
    String user_card;
    JSONArray products = new JSONArray();
    private static final DecimalFormat df = new DecimalFormat("0.00");


    public CartFragment() {

    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        finalPrice=view.findViewById(R.id.cartFinalPrice);
        cartDiscount=view.findViewById(R.id.cartDiscount);
        semiCartPrice=view.findViewById(R.id.cartOrderTotal);
        semiCartPrice.setText(String.valueOf(df.format(arrayCart.semiPrice) + " €"));



        Bundle cuponBundle = this.getArguments();
        if(cuponBundle != null){
            idCupon = (String) cuponBundle.get("idCupon");
            df.setRoundingMode(RoundingMode.UP);
            cartDiscount.setText(String.valueOf(df.format(arrayCart.semiPrice*0.25) + " €"));
            df.setRoundingMode(RoundingMode.DOWN);
            finalPrice.setText(String.valueOf(df.format(arrayCart.semiPrice-(arrayCart.semiPrice*0.25)) + " €"));

        }else{
            cartDiscount.setText("Use Cupon");
            finalPrice.setText(String.valueOf(arrayCart.totalPrice) + " €");

            cartDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFragment(cuponsFragment.newInstance("",""));
                }
            });
        }


        Log.d("TAG", "onCreateView: " + idCupon);




        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new cartAdapter(this,dataholder));
        increment=view.findViewById(R.id.addCartBtn);
        decrement=view.findViewById(R.id.removeCartBtn);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        user_card = sharedPref.getString("cardId", default_value);
        access_token = sharedPref.getString("access_token", default_value);



        backBtn=view.findViewById(R.id.cartPageBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(HomeFragment.newInstance("",""));

            }
        });

        checkoutBtn=view.findViewById(R.id.checkoutBtn);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    allCheckout=new JSONObject();
                    try {
                        for(int i = 0; i<dataholder.size(); i++){
                            productCheckOut = new JSONObject();
                            products = new JSONArray();
                            productCheckOut.put("id",  dataholder.get(i).getId());
                            productCheckOut.put("quantity", dataholder.get(i).getQuantity());
                            products.put(productCheckOut);
                        }

                        allCheckout.put("products",products);
                        allCheckout.put("cupon", idCupon);
                        Log.d("TAG", "onClick: "+idCupon);
                        Log.d("TAG", "onClick: " + allCheckout);
                        postCheckout(UrlApi.URL_USERS+user_card);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "home_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {

    }

    private void postCheckout(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, allCheckout,
                response -> {
                    // response
                    Log.d("Response", String.valueOf(response));
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Authorization", "Bearer "+ access_token );
                return headerMap;
            }

        };

        queue.add(postRequest);
    }

}