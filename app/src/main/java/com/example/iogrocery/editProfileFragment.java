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
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class editProfileFragment extends Fragment implements View.OnClickListener {

    ImageView editPageBack;
    EditText heightInput, weightInput, calories;
    Button editBtn;
    JSONObject editProfile;
    String user_card, access_token;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public editProfileFragment() {
        // Required empty public constructor
    }



    public static editProfileFragment newInstance(String param1, String param2) {
        editProfileFragment fragment = new editProfileFragment();
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
        View view =inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editPageBack=view.findViewById(R.id.editPageBack);
        heightInput=view.findViewById(R.id.heightInput);
        weightInput=view.findViewById(R.id.weightInput);
        editBtn=view.findViewById(R.id.editBtn);
        calories=view.findViewById(R.id.calories);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        user_card = sharedPref.getString("cardId", default_value);
        access_token = sharedPref.getString("access_token", default_value);


        editPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(ProfileFragment.newInstance("",""));
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile=new JSONObject();
                try{
                    editProfile.put("height", Double.parseDouble(heightInput.getText().toString()));
                    editProfile.put("weight", Double.parseDouble(weightInput.getText().toString()));
                    editProfile.put("caloriesLimit", Integer.parseInt(calories.getText().toString()));
                    changeProifile(UrlApi.URL_USERS+"/"+user_card);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void changeProifile(String url){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PATCH, url, editProfile,
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

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "home_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}