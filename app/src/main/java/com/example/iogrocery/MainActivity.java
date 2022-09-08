package com.example.iogrocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_nav_view);
        bottomNavigation.setBackground(null);
        Menu menuNav=bottomNavigation.getMenu();
        MenuItem disableItem = menuNav.findItem(R.id.placeholder);
        disableItem.setEnabled(false);

        openFragment(login.newInstance("",""));

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new login()).commit();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp=null;
                switch (item.getItemId()){
                    case R.id.menu_item_home:  temp = new HomeFragment();
                        break;
                    case R.id.menu_item_water: temp = new WaterFragment();
                        break;
                    case R.id.menu_item_calories:  temp = new CaloriesFragment();
                        break;
                    case R.id.menu_item_profile:  temp = new ProfileFragment();

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, temp).commit();
                return true;
            }
        });


        Fragment openCart = new CartFragment();
        FloatingActionButton fab = findViewById(R.id.cartBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, openCart).commit();
            }
        });






    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showToast (String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected  void onResume() {
        super.onResume();

    }

}



