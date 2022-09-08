package com.example.iogrocery;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends Fragment implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    private String userId;
    private String userName;
    private String email;
    private Uri img;
    String cardId;

    public login() {
        // Required empty public constructor
    }

    public static login newInstance(String param1, String param2) {
        login fragment = new login();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

         mGoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup frameContainer,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, frameContainer, false);
        v.findViewById(R.id.sign_in_button).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn(v);

                break;
        }
    }
    private void signIn(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           // Log.d("TAG", "onActivityResult: "+ task);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            userName = account.getDisplayName();
            userId = account.getId();
            email = account.getEmail();
            img = account.getPhotoUrl();
            Log.d("account", userName +"//"+ userId +"//"+ email +"//"+ img);
            // Signed in successfully, show authenticated UI.
            volleyPostUser( userId, userName, email,  img );
            Log.w("signInResult", "success");
            openFragment(HomeFragment.newInstance("",""));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("signInResult", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    //post user -> googleName: Nome da google, googleId: Id da google, googleEmail: email da google, googleImg: imagem google rota : /login
    public void volleyPostUser(String userId,String userName,String email, Uri img ){
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = UrlApi.LOGIN;
        JSONObject postData = new JSONObject();
        try {
            postData.put("userName", userName);
            postData.put("userId", userId);
            postData.put("userEmail", email);
            postData.put("userImg", img);
        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d( "volley","Oops: " + e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String access_token = response.getString("access_token");
                            String cardId = response.getString("card");
                            String role = response.getString("role");
                            Log.d("access_token", access_token);
                            Log.d("user_card", cardId);
                            Log.d("role", role);
                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("cardId",cardId );
                            editor.putString("access_token",access_token);
                            editor.putString("role", role );
                            editor.apply();
                            Log.d("SHARED_PREFERENCES", "cardId"+cardId+"accesstoken:"+access_token+"role: "+role);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d( "volley","Oops: " + error);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "product_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

}