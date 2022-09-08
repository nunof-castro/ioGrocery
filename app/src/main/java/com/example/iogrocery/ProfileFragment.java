package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.homeListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Button adminBtn;
    ImageView editBtn;
    ImageButton cuponsBtn,RedirectCaloriesPage,RedirectWaterPage;
    String role;
    TextView nameLabel,emailLabel,balanceTxt;
    ImageView profilePic;
    String userName,profileImg,user_card,userEmail, access_token;
    Float balance;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        adminBtn = view.findViewById(R.id.adminBtn);
        cuponsBtn = view.findViewById(R.id.cuponsBtn);
        RedirectCaloriesPage = view.findViewById(R.id.RedirectCaloriesPage);
        RedirectWaterPage = view.findViewById(R.id.RedirectWaterPage);
        editBtn=view.findViewById(R.id.editIcon);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        user_card = sharedPref.getString("cardId", default_value);
        access_token = sharedPref.getString("access_token", default_value);
        role = sharedPref.getString("role", default_value);
        getUserInfo(user_card);
        nameLabel = view.findViewById(R.id.nameLabelProf);
        emailLabel = view.findViewById(R.id.emailLabelProf);
        profilePic = view.findViewById(R.id.profilePicProf);
        balanceTxt = view.findViewById(R.id.balaceTxtProf);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(editProfileFragment.newInstance("",""));
            }
        });


        if (role.equals("admin")){
            adminBtn.setEnabled(true);
        }else{
            adminBtn.setEnabled(false);
            adminBtn.setVisibility(View.INVISIBLE);
        }
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(adminPanelFragment.newInstance("",""));
            }
        });

        cuponsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(cuponsFragment.newInstance("",""));
            }
        });
        RedirectCaloriesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(CaloriesFragment.newInstance("",""));
            }
        });
        RedirectWaterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(WaterFragment.newInstance("",""));
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {

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
                            nameLabel.setText(userName);

                            userEmail= response.getJSONObject("userContent").getString("email");
                            emailLabel.setText(userEmail);

                            balance= Float.parseFloat(response.getJSONObject("userCard").getString("amount"));
                            balanceTxt.setText(df.format(balance) + " €");
                            profileImg = response.getJSONObject("userContent").getString("img");
                            Picasso.get()
                                    .load(profileImg)
                                    .into(profilePic);
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
        transaction.replace(R.id.frameContainer, fragment, "product_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}