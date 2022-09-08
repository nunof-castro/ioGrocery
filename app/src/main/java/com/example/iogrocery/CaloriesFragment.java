package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.caloriesAdatpter;
import com.example.iogrocery.adapter.editProductAdapter;
import com.example.iogrocery.adapter.homeListAdapter;
import com.example.iogrocery.adapter.waterAdapter;
import com.example.iogrocery.models.Calories;
import com.example.iogrocery.models.Product;
import com.example.iogrocery.models.Water;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CaloriesFragment extends Fragment {
    RecyclerView caloriesRecycler;
    JSONObject caloriesRecObj;
    String user_card;
    public Double caloriesQtyInd;
    public JSONArray refillsCal;
    ImageButton editProdPageBack;
    ArrayList<Calories> caloriesHolder=new ArrayList<>();
    Double totalCalories,caloriesLimit;
    TextView caloriesTotal,limitCalories;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public CaloriesFragment() {
        // Required empty public constructor
    }

    public static CaloriesFragment newInstance(String param1, String param2) {
        CaloriesFragment fragment = new CaloriesFragment();
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
        View view = inflater.inflate(R.layout.fragment_calories, container, false);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        user_card = sharedPref.getString("cardId", default_value);
        getCalories(UrlApi.URL_USERS+user_card+"/calories");
        caloriesRecycler = view.findViewById(R.id.allProductsList);
        caloriesTotal = view.findViewById(R.id.calories_total);
        limitCalories = view.findViewById(R.id.calories_limit);

        //editProdPageBack = view.findViewById(R.id.editProdPageBack);
        /*editProdPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(adminPanelFragment.newInstance("",""));
            }
        });*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getCalories(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray refills= response.getJSONArray("refills");
                        Log.d("TAG", "getWaterInfo: "+refills.length());
                        totalCalories= response.getDouble("totalQuantity");
                        caloriesLimit= response.getDouble("caloriesLimit");

                        Log.d("totalCalories", "getCalories: "+totalCalories);

                        for(int i=0; i<refills.length();i++){
                            Double reffilQty = refills.getJSONObject(i).getDouble("quantity");
                            Log.d("TAG", "getWaterInfo: " + reffilQty);
                            Calories calories= new Calories(reffilQty);
                            caloriesHolder.add(calories);

                        }
                        caloriesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                        caloriesRecycler.setLayoutManager(gridLayoutManager);
                        caloriesRecycler.setAdapter(new caloriesAdatpter(caloriesHolder));
                        caloriesTotal.setText(df.format(totalCalories));
                        limitCalories.setText(df.format(caloriesLimit));
                        refillsCal=refills;



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.d("tagMAAA", String.valueOf(error)));
        queue.add(objectRequest);
    }




    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "EditProductFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}