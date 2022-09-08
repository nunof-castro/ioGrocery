package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class chargeCardFragment extends Fragment implements View.OnClickListener {
    Button chargeBtn;
    JSONObject chargeObj;
    String amountValueInput;
    ImageButton chargeCardPageBack;
    String access_token;

    EditText amountInput;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public chargeCardFragment() {
        // Required empty public constructor
    }


    public static chargeCardFragment newInstance(String param1, String param2) {
        chargeCardFragment fragment = new chargeCardFragment();
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
        View view= inflater.inflate(R.layout.fragment_charge_card, container, false);
            Bundle bundle = this.getArguments();
            String idCard = bundle.getString("idCard");

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        access_token = sharedPref.getString("access_token", default_value);


            amountInput=view.findViewById(R.id.amountInput);
        chargeCardPageBack=view.findViewById(R.id.chargeCardPageBack);
        chargeCardPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
            chargeBtn=view.findViewById(R.id.chargeCard);


            chargeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    amountValueInput = amountInput.getText().toString();
                    chargeObj = new JSONObject();
                    try{
                        chargeObj.put("amount", Integer.parseInt(amountValueInput));
                        addAmount(UrlApi.URL_USERS+"/"+idCard);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            });
        return view;
    }


    private void addAmount(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, chargeObj,
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

        queue.add(putRequest);
    }

    @Override
    public void onClick(View view) {

    }
}