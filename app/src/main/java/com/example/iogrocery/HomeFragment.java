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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
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

import com.example.iogrocery.models.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment{
    private static final DecimalFormat df = new DecimalFormat("0.00");


    private homeListAdapter homeListAdapter;
    RecyclerView recyclerView;
    ArrayList<Product> dataholder;
    ArrayList<Product> favoritesArr;
    EditText searchInput;
    JSONObject productsObj = new JSONObject();
    JSONObject favoritesObj;
    //name
    TextView userNameTxt;
    String userName;
    //balance
    TextView balanceTxt;
    Float balance;
    ImageView profilePic;
    String profileImg;
    String access_token;
    String user_card;
    ImageButton showFavorites;
    int favClick=0;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchInput=view.findViewById(R.id.searchProductInput);
        searchInput.getText().clear();
        recyclerView = view.findViewById(R.id.productsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholder= new ArrayList<>();
        getProducts(UrlApi.URL_PRODUCTS);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
         access_token = sharedPref.getString("access_token", default_value);
        user_card = sharedPref.getString("cardId", default_value);
        getUserInfo(user_card);
        userNameTxt = view.findViewById(R.id.nameLabel);
        balanceTxt = view.findViewById(R.id.textView9);
        profilePic = view.findViewById(R.id.profileImg);
        showFavorites = view.findViewById(R.id.favoritesBtn);
        showFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favClick++;
                dataholder.clear();
                Log.d("TAG", "onClick: ");
                if(favClick%2==0){
                    showFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite);
                    getProducts(UrlApi.URL_PRODUCTS);
                    Log.d("getProd", "getProd: "+dataholder.size());
                }else{
                    showFavorites.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    getProducts(UrlApi.URL_FAVORITES+user_card+"/favorites");
                    Log.d("getProd", "getProd: "+dataholder.size());
                }
                Log.d("favclicks", "onClick: "+favClick);
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()==""){
                    getProducts(UrlApi.URL_PRODUCTS);
                }
                if(s.toString()!=""){
                    filter(s.toString());
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void filter(String text) {
        ArrayList<Product> dataFilter = new ArrayList<>();

        for (Product item : dataholder) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                dataFilter.add(item);
            }
        }
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(new homeListAdapter(dataFilter));
    }

    private void getUserInfo(String cardId){
        String url = UrlApi.URL_USERS+cardId;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            userName= response.getJSONObject("userCard").getString("person");
                            userNameTxt.setText(userName);
                            balance= Float.parseFloat(response.getJSONObject("userCard").getString("amount"));
                            balanceTxt.setText(df.format(balance) + " €");
                            profileImg = response.getJSONObject("userContent").getString("img");
                            Picasso.get()
                                    .load(profileImg)
                                    .into(profilePic);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(new homeListAdapter(dataholder));

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
                headers.put("Authorization", "Bearer "+access_token );
                Log.d("Authorization", "token entrou ");
                return headers;
            }
        };

        queue.add(request);
    }

    private void getProducts(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                productsObj=response.getJSONObject(i);
                                NumberFormat f = NumberFormat.getInstance();
                                String productId = productsObj.getString("_id");
                                Double packPrice = Double.parseDouble(productsObj.getString("price"));
                                int packUnits = Integer.parseInt(productsObj.getString("units"));
                                String productName  = productsObj.getString("name");
                                String buyer  = productsObj.getString("bought_by");
                                int productAvailable = Integer.parseInt(productsObj.getString("quantity"));
                                String productImg = productsObj.getString("img");
                                String[] splitName = productName.split("-");
                                Product product = new Product(productId,packPrice,packUnits, splitName[0],buyer,productAvailable,productImg);
                                if(productAvailable>0){
                                    dataholder.add(product);
                                }
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(new homeListAdapter(dataholder));

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
                headers.put("Authorization", "Bearer "+access_token );
                Log.d("Authorization", "token entrou ");
                return headers;
            }
        };

        queue.add(request);
    }


    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "product_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

