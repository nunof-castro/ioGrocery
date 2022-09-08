package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.homeListAdapter;
import com.example.iogrocery.models.Cart;
import com.example.iogrocery.models.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductFragment extends Fragment implements View.OnClickListener {
    TextView name, priceUnit, buyer, available, orderNum, totalPrice;
    ImageView img;
    ImageButton increment, decrement, backBtn, addToFavorites;
    Button addCart;
    String user_card, access_token;
    JSONObject productPage;
    JSONObject calories;
    TextView energyValue, lipidsValue, hydratesValue, sugarValue,fiberValue,proteinValue,saltValue;

    int orderNumber = 1;
    public int productQty;
    public Double productUnitPrice;
    public String imageHolder, nameProduct;
    String idProduto;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        Bundle bundle = this.getArguments();
        idProduto = bundle.getString("idProduto");
        Log.d("TAG", "onCreateView: " + UrlApi.URL_PRODUCTS + "/" + idProduto);
        getProductInfo(UrlApi.URL_PRODUCTS + "/" + idProduto);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        user_card = sharedPref.getString("cardId", default_value);
        access_token = sharedPref.getString("access_token", default_value);


        increment = view.findViewById(R.id.addBtn);
        decrement = view.findViewById(R.id.removeBtn);
        orderNum = view.findViewById(R.id.orderNumber);
        totalPrice = view.findViewById(R.id.productTotalPrice);
        addCart = view.findViewById(R.id.addCartBtn);
        backBtn = view.findViewById(R.id.productPageBack);
        addToFavorites=view.findViewById(R.id.addToFavorites);
        energyValue=view.findViewById(R.id.energyValue);
        lipidsValue=view.findViewById(R.id.lipidsValue);
        hydratesValue=view.findViewById(R.id.hydratesValue);
        sugarValue=view.findViewById(R.id.sugarsValue);
        fiberValue=view.findViewById(R.id.fiberValue);
        proteinValue=view.findViewById(R.id.proteinValue);
        saltValue=view.findViewById(R.id.saltValue);

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderNumber++;
                if (orderNumber <= productQty) {
                    Log.d("TAG", "onClick: " + orderNumber);
                    increment.setAlpha(1f);
                    decrement.setAlpha(1f);
                    increment.setEnabled(true);
                    decrement.setEnabled(true);
                    if (orderNumber < 10) {
                        orderNum.setText("0" + String.valueOf(orderNumber));
                    } else {
                        orderNum.setText(String.valueOf(orderNumber));
                    }
                }
                if (orderNumber == productQty) {
                    increment.setAlpha(.5f);
                    increment.setEnabled(false);
                }
                addCart.setAlpha(1f);
                addCart.setEnabled(true);
                totalPrice.setText(df.format(productUnitPrice * orderNumber));
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderNumber--;

                if (orderNumber == 0) {
                    decrement.setAlpha(.5f);
                    decrement.setEnabled(false);
                    orderNum.setText("0" + String.valueOf(orderNumber));
                    addCart.setEnabled(false);
                    addCart.setAlpha(0.5f);
                }
                if (orderNumber >= 1) {
                    decrement.setAlpha(1f);
                    increment.setAlpha(1f);
                    increment.setEnabled(true);
                    decrement.setEnabled(true);

                    if (orderNumber < 10) {
                        orderNum.setText("0" + String.valueOf(orderNumber));
                    } else {
                        orderNum.setText(String.valueOf(orderNumber));
                    }
                }
                totalPrice.setText(df.format(productUnitPrice * orderNumber));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart obj = new Cart(idProduto, nameProduct, imageHolder, Double.parseDouble(df.format(productUnitPrice * orderNumber)), productUnitPrice, productQty, orderNumber);
                arrayCart.Products.add(obj);
                Log.d("TAG", "onClick: " + name.getText().toString());
            }
        });
        addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volleyPostFavorite(UrlApi.URL_FAVORITES+user_card+"/favorites");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "product_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getProductInfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                       JSONObject productPage = response.getJSONObject("productPage");
                        String productImg = productPage.getString("img");
                        String productName = productPage.getString("name");
                        String[] splitName = productName.split("-");
                        Double packPrice = Double.parseDouble(productPage.getString("price"));
                        int packUnits = Integer.parseInt(productPage.getString("units"));
                        String productBuyer = productPage.getString("bought_by");
                        int productAvailable = Integer.parseInt(productPage.getString("quantity"));

                        JSONObject calories = response.getJSONObject("calories");
                        Double energy = Double.parseDouble(calories.getString("energia"));
                        Double lipids = Double.parseDouble(calories.getString("lipidos"));
                        Double hydrates = Double.parseDouble(calories.getString("hidratos"));
                        Double sugars = Double.parseDouble(calories.getString("acucares"));
                        Double fiber = Double.parseDouble(calories.getString("fibra"));
                        Double protein = Double.parseDouble(calories.getString("proteina"));
                        Double salt = Double.parseDouble(calories.getString("sal"));

                        energyValue.setText(String.valueOf(energy));
                        lipidsValue.setText(String.valueOf(lipids));
                        hydratesValue.setText(String.valueOf(hydrates));
                        sugarValue.setText(String.valueOf(sugars));
                        fiberValue.setText(String.valueOf(fiber));
                        proteinValue.setText(String.valueOf(protein));
                        saltValue.setText(String.valueOf(salt));

                        nameProduct = productName;
                        imageHolder = productImg;
                        img = getActivity().findViewById(R.id.productPageImage);
                        Picasso.get().load(productImg).into(img);
                        name = getActivity().findViewById(R.id.productPageName);
                        name.setText(splitName[0]);
                        priceUnit = getActivity().findViewById(R.id.priceUnit);
                        priceUnit.setText(df.format(packPrice / packUnits) + " €");
                        buyer = getActivity().findViewById(R.id.productBuyer);
                        buyer.setText(productBuyer);
                        available = getActivity().findViewById(R.id.productPageAvailable);

                        if (productAvailable >= 10) {
                            available.setText(String.valueOf(productAvailable));
                        } else {
                            available.setText("0" + String.valueOf(productAvailable));
                        }

                        productQty = productAvailable;
                        productUnitPrice = Double.parseDouble(df.format(packPrice / packUnits));
                        totalPrice.setText(df.format(productUnitPrice * orderNumber) + " €");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.d("tagMAAA", String.valueOf(error)));
        queue.add(objectRequest);
    }








    public void volleyPostFavorite(String url) {
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        JSONObject postData = new JSONObject();
        try {
            postData.put("card",user_card);
            postData.put("product", idProduto);

        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d( "volley","Oops: " + e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("addToFavorites", "onResponse: "+response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "Oops: " + error);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {

    }

}
