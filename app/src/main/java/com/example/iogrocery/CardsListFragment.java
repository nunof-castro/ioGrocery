package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.cardsAdminListAdapter;
import com.example.iogrocery.adapter.homeListAdapter;
import com.example.iogrocery.models.Card;
import com.example.iogrocery.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardsListFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Card> cardholder;
    JSONObject cardObj;
    Button createUser;
    ImageButton cardsListPageBack;
    String access_token;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CardsListFragment() {
        // Required empty public constructor
    }

    public static CardsListFragment newInstance(String param1, String param2) {
        CardsListFragment fragment = new CardsListFragment();
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
        View view = inflater.inflate(R.layout.fragment_cards_list, container, false);
        recyclerView = view.findViewById(R.id.cardsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cardholder= new ArrayList<>();
        cardsListPageBack = view.findViewById(R.id.cardsListPageBack);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        access_token = sharedPref.getString("access_token", default_value);


        cardsListPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        getCards(UrlApi.URL_CARDS);

        createUser=view.findViewById(R.id.createCardBtn);

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(addUserFragment.newInstance("",""));
            }
        });

        return view;
    }


    private void getCards(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                cardObj = response.getJSONObject(i);
                                String cardId = cardObj.getString("_id");
                                String userName = cardObj.getString("person");
                                Double cardAmount = Double.parseDouble(cardObj.getString("amount"));

                                Card card = new Card(cardId,userName,cardAmount);
                                cardholder.add(card);
                            }


                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(new cardsAdminListAdapter(cardholder,getActivity()));

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
        transaction.replace(R.id.frameContainer, fragment, "home_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}