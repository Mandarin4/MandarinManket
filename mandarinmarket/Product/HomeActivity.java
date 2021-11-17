package com.example.mandarinmarket.Product;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.example.mandarinmarket.UI.Users.RegicterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private long backPressedtimer;
    private Toast backToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        PeremennyeActivity.Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigatebotton);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectdListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PromotionsFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectdListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.homeFragment:
                    selectedFragment = new PromotionsFragment();
                    break;
                case R.id.categoriesFragment:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.favoritesFragment:
                    selectedFragment = new FavoritesFragment();
                    break;
                case R.id.basketFragment:
                    selectedFragment = new BasketFragment();
                    break;
                case R.id.personFragment:
                    selectedFragment = new PersonFragment();
                    break;
            }

            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };


    /*// Системная кнопка назад
    @Override
    public void onBackPressed(){
        if (backPressedtimer + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти!", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedtimer = System.currentTimeMillis();
    }*/

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 0) {

            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            // Otherwise, ask user if he wants to leave :)
            if (backPressedtimer + 2000 > System.currentTimeMillis()){
                finish();
                moveTaskToBack(true);
                return;
            }else{
                backToast = Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти!", Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedtimer = System.currentTimeMillis();


          /*  new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            // MainActivity.super.onBackPressed();
                            finish();
                            moveTaskToBack(true);
                        }
                    }).create().show();*/
        }
    }
}