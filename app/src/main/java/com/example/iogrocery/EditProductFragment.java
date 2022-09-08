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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iogrocery.adapter.editProductAdapter;
import com.example.iogrocery.adapter.homeListAdapter;
import com.example.iogrocery.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProductFragment extends Fragment {
    RecyclerView recyclerView;
    JSONObject productsObj;
    ArrayList<Product> productHolder = new ArrayList<>();
    TextView productName;
    Button setNewQuantity;
    EditText newQuantity;
    ImageButton editProdPageBack;
    String access_token;
    public EditProductFragment() {
        // Required empty public constructor
    }

    public static EditProductFragment newInstance(String param1, String param2) {
        EditProductFragment fragment = new EditProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        getProducts(UrlApi.URL_PRODUCTS);
        recyclerView = view.findViewById(R.id.allProductsList);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String default_value= "";
        access_token = sharedPref.getString("access_token", default_value);

        editProdPageBack = view.findViewById(R.id.editProdPageBack);
        editProdPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(adminPanelFragment.newInstance("",""));
            }
        });
        Bundle passProdName = this.getArguments();
        if (passProdName != null) {
            String name_product = passProdName.getString("name_product");
            String id_product = passProdName.getString("id_product");
            Log.d("id_product", "onCreateView: "+id_product);
            productName = view.findViewById(R.id.name_product);
            productName.setText(name_product);
            newQuantity=view.findViewById(R.id.newQuantity);

            setNewQuantity = view.findViewById(R.id.setNewQuantity);
            setNewQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double newQt =Double.parseDouble(newQuantity.getText().toString());
                    Log.d("newQt", "onClick: "+newQt);
                    putProduct(UrlApi.EDIT_PRODUCT+id_product,newQt );
                    openFragment(adminPanelFragment.newInstance("",""));


                }

            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getProducts(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                productsObj = response.getJSONObject(i);

                                NumberFormat f = NumberFormat.getInstance();

                                String productId = productsObj.getString("_id");
                                Double packPrice = Double.parseDouble(productsObj.getString("price"));
                                int packUnits = Integer.parseInt(productsObj.getString("units"));
                                String productName = productsObj.getString("name");
                                String buyer = productsObj.getString("bought_by");
                                int productAvailable = Integer.parseInt(productsObj.getString("quantity"));
                                String productImg = productsObj.getString("img");
                                String[] splitName = productName.split("-");
                                Product product = new Product(productId, packPrice, packUnits, splitName[0], buyer, productAvailable, productImg);
                                productHolder.add(product);
                            }


                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(new editProductAdapter(productHolder));

                        } catch (JSONException e) {
                            Log.d("VolleyDebug", e.getMessage());
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
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


    public void putProduct(String url, Double quantity){
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        JSONObject postData = new JSONObject();
        try {
            postData.put("quantity", quantity);

        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d( "volley","Oops: " + e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url, postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String response1 = response.toString();
                        Log.d("put succesful", "onResponse: "+response1);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d( "volley","Oops: " + error);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "EditProductFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}