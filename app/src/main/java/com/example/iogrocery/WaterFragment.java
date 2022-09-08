package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.homeListAdapter;
import com.example.iogrocery.adapter.waterAdapter;
import com.example.iogrocery.models.Product;
import com.example.iogrocery.models.Water;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WaterFragment extends Fragment implements View.OnClickListener {
    EditText waterQtyInput;
    Button addWaterBtn;
    int waterClicks = 0;
    JSONObject waterConsumed;
    public Double waterQtyP, waterObjectiveP;
    public JSONArray refillsP;
    String user_card, access_token;
    TextView objWater, remWater;
    ArrayList<Water> waterHolder;
    RecyclerView waterRecycler;
    private static final DecimalFormat df = new DecimalFormat("0.0");

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public WaterFragment() {
        // Required empty public constructor
    }


    public static WaterFragment newInstance(String param1, String param2) {
        WaterFragment fragment = new WaterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water, container, false);
        waterQtyInput=view.findViewById(R.id.waterQtyInput);
        waterQtyInput.setVisibility(View.INVISIBLE);
        addWaterBtn=view.findViewById(R.id.addWaterBtn);
        objWater=view.findViewById(R.id.objectiveWater);
        remWater=view.findViewById(R.id.remainingWater);
        waterRecycler=view.findViewById(R.id.waterRecycler);
        waterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        waterHolder= new ArrayList<>();
        waterClicks=0;
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        user_card = sharedPref.getString("cardId", default_value);
        access_token = sharedPref.getString("access_token", default_value);

        getWaterInfo(UrlApi.URL_USERS+user_card+"/water");


        addWaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterClicks++;
                Log.d("TAG", "verTeste: " + waterQtyInput.getText().toString().length());
                if (waterClicks % 2 != 0 ){
                    Log.d("TAG", "onClick: " + waterClicks);
                    waterQtyInput.setVisibility(View.VISIBLE);
                }else{
                   waterConsumed=new JSONObject();
                   try {
                       if(waterQtyInput.getText().toString().length()!=0){
                           waterConsumed.put("quantity", waterQtyInput.getText().toString());
                           createNewReffil(UrlApi.URL_USERS+user_card+"/water");
                           waterQtyInput.getText().clear();
                           waterQtyInput.setVisibility(View.INVISIBLE);
                       }else{
                           waterClicks--;
                           Toast.makeText(getActivity(), "Introduza um valor válido",
                                   Toast.LENGTH_LONG).show();
                           Log.d("TAG", "onClick: " + waterClicks);
                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                }
            }
        });


        return view;
    }

    private void createNewReffil(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, waterConsumed,
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

    private void getWaterInfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray refills= response.getJSONArray("refills");
                        Double waterQty = response.getDouble("totalQuantity");
                        Double waterObjective = response.getDouble("waterObjective");

                        Log.d("TAG", "getWaterInfo: "+refills.length());

                        for(int i=0; i<refills.length();i++){
                           /* refills = new JSONArray();*/
                            Double reffilQty = refills.getJSONObject(i).getDouble("quantity");
                            Log.d("TAG", "getWaterInfo: " + reffilQty);
                            Water water= new Water(reffilQty);
                            waterHolder.add(water);

                        }




                        waterRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                        waterRecycler.setLayoutManager(gridLayoutManager);
                        waterRecycler.setAdapter(new waterAdapter(waterHolder));

                        refillsP=refills;
                        waterQtyP=waterQty;
                        waterObjectiveP=waterObjective;
                        objWater.setText(String.valueOf(waterObjectiveP + " liters"));
                        remWater.setText(String.valueOf("Consumed: "+ df.format(waterQtyP)));
                        Log.d("TAG", "getWaterInfo: " + waterObjective);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> Log.d("tagMAAA", String.valueOf(error)));
        queue.add(objectRequest);
    }



    @Override
    public void onClick(View view) {

    }
}