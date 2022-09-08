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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.cardsAdminListAdapter;
import com.example.iogrocery.adapter.cuponAdapter;
import com.example.iogrocery.models.Cupon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class cuponsFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<Cupon> cuponHolder;
    JSONObject cuponObj;
    String user_card;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public cuponsFragment() {
        // Required empty public constructor
    }



    public static cuponsFragment newInstance(String param1, String param2) {
        cuponsFragment fragment = new cuponsFragment();
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
        View view = inflater.inflate(R.layout.fragment_cupons, container, false);

        recyclerView=view.findViewById(R.id.cuponsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cuponHolder= new ArrayList<>();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        user_card = sharedPref.getString("cardId", default_value);
        getCupons(UrlApi.URL_USERS+user_card+"/cupons");
        return view;
    }

    private void getCupons(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                cuponObj = response.getJSONObject(i);
                                String cuponId = cuponObj.getString("_id");
                                int cuponAmount = cuponObj.getInt("discount");


                                Cupon cupon = new Cupon(cuponId,cuponAmount);
                                Log.d("TAG", "onResponse: " + cupon);
                                cuponHolder.add(cupon);

                            }


                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(new cuponAdapter(cuponHolder));

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
                });

        queue.add(request);
    }

    @Override
    public void onClick(View view) {

    }
}