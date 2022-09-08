package com.example.iogrocery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adminPanelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adminPanelFragment extends Fragment implements View.OnClickListener {
    Button seePuchases;
    Button editProduct;
    Button carregarBtn, addProduct;
    ImageButton adminPageBack;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public adminPanelFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static adminPanelFragment newInstance(String param1, String param2) {
        adminPanelFragment fragment = new adminPanelFragment();
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
        View view =inflater.inflate(R.layout.fragment_admin_panel, container, false);
        carregarBtn=view.findViewById(R.id.carregarCard);
        adminPageBack = view.findViewById(R.id.adminPageBack);
        addProduct=view.findViewById(R.id.addAdminProduct);


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(addProductFragment.newInstance("",""));
            }
        });

        adminPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        carregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(CardsListFragment.newInstance("",""));
            }
        });

        seePuchases = view.findViewById(R.id.seePuchases);
        seePuchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(PurchasesAdminFragment.newInstance("",""));

            }
        });

        editProduct = view.findViewById(R.id.editProduct);
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(EditProductFragment.newInstance("",""));
            }
        });
        return view;
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment, "admin_panel_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {

    }
}