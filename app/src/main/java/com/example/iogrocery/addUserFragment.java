package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class addUserFragment extends Fragment implements View.OnClickListener {
    Button createUser;
    ImageButton goBack;
    JSONObject userInfo;
    EditText userName, userEmail, userRole, userAmount;
    String access_token;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public addUserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static addUserFragment newInstance(String param1, String param2) {
        addUserFragment fragment = new addUserFragment();
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
       View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        createUser=view.findViewById(R.id.createBtn);
        userName=view.findViewById(R.id.nameInput);
        userEmail=view.findViewById(R.id.emailInput);
        userAmount=view.findViewById(R.id.amountInput);
        userRole=view.findViewById(R.id.roleInput);
        goBack = view.findViewById(R.id.cardsListPageBack);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        access_token = sharedPref.getString("access_token", default_value);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(CardsListFragment.newInstance("",""));
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo=new JSONObject();
                try {
                    userInfo.put("amount", Integer.parseInt(userAmount.getText().toString()));
                    userInfo.put("person", userName.getText().toString());
                    userInfo.put("email", userEmail.getText().toString());
                    userInfo.put("role", userEmail.getText().toString());

                    createNewUser(UrlApi.URL_CARDS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


       return view;
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "product_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createNewUser(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, userInfo,
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