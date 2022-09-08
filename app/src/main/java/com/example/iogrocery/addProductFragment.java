package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class addProductFragment extends Fragment implements View.OnClickListener {
    EditText addName, addBought,addUnits,addPrice, addQty, addImg, addEnergy, addLipidos, addHidratos, addAcucares, addFibras,addProteina, addSal;
    Button addBtn;
    JSONObject productInfo;
    String access_token;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public addProductFragment() {
        // Required empty public constructor
    }



    public static addProductFragment newInstance(String param1, String param2) {
        addProductFragment fragment = new addProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        addName=view.findViewById(R.id.addName);
        addBought=view.findViewById(R.id.addBought);
        addUnits=view.findViewById(R.id.addUnits);
        addPrice=view.findViewById(R.id.addPrice);
        addQty=view.findViewById(R.id.addQty);
        addImg=view.findViewById(R.id.addImg);
        addEnergy=view.findViewById(R.id.addEnergy);
        addLipidos=view.findViewById(R.id.addLipidos);
        addHidratos=view.findViewById(R.id.addHidratos);
        addAcucares=view.findViewById(R.id.addAcucares);
        addFibras=view.findViewById(R.id.addFibras);
        addProteina=view.findViewById(R.id.addProteina);
        addSal=view.findViewById(R.id.addSal);
        addBtn= view.findViewById(R.id.addBtn);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        access_token = sharedPref.getString("access_token", default_value);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productInfo=new JSONObject();
                try {
                    productInfo.put("price", Double.parseDouble(addPrice.getText().toString()));
                    productInfo.put("units", Integer.parseInt(addUnits.getText().toString()));
                    productInfo.put("name", addName.getText().toString());
                    productInfo.put("bought_by", addBought.getText().toString());
                    productInfo.put("quantity", Integer.parseInt(addQty.getText().toString()));
                    productInfo.put("img",addImg.getText().toString());
                    productInfo.put("code", 0);
                    productInfo.put("energia", Double.parseDouble(addEnergy.getText().toString()));
                    productInfo.put("lipidos", Double.parseDouble(addLipidos.getText().toString()));
                    productInfo.put("hidratos", Double.parseDouble(addHidratos.getText().toString()));
                    productInfo.put("acucares", Double.parseDouble(addAcucares.getText().toString()));
                    productInfo.put("fibra", Double.parseDouble(addFibras.getText().toString()));
                    productInfo.put("proteina", Double.parseDouble(addProteina.getText().toString()));
                    productInfo.put("sal", Double.parseDouble(addSal.getText().toString()));

                    createNewProduct(UrlApi.URL_PRODUCTS);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    private void createNewProduct(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, productInfo,
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

    @Override
    public void onClick(View view) {

    }
}