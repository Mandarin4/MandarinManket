package com.example.mandarinmarket;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.mandarinmarket.Model.Basket_model;
import com.example.mandarinmarket.Model.Favorites_model;
import com.example.mandarinmarket.Model.Productes_model;
import com.example.mandarinmarket.Model.Shops_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class PeremennyeActivity {
    public static FirebaseAuth mAuth;
    public static FirebaseUser user;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static String Shops,ShopsName, Categoriya,Products, username;
    public static ArrayList<Basket_model> datalistbasket;
    public static ArrayList<String> Shops2,Categoriya2;
  //  public static ArrayList<Basket_model> datalistbasket;
    public static String Status;
    public static ArrayList<Favorites_model> datalistfavorit;
    public static ArrayList<Shops_model> datalistshop;
    public static ArrayList<Shops_model> datalistshop2;
    public static String Email2 ;
    public static Integer OrdersInt ;
    public static Productes_model productesmodel;
    //переменные для BasketActiviti
    public static Integer totalquan, totalprice;
    public static ArrayList<String> arrayListShop;
    public static ArrayList<Productes_model> datalist;
    public static DatabaseReference databasePropertyData ;
    public static String ownerId;





    public void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        //user = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (user != null) {

                } else {

                }
                // ...

            }
        };


    }
    public static boolean validateEmailAddress(String email){
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }else {
            return false;
        }
    }
    public static void addClickEffect(View view)
    {
        Drawable drawableNormal = view.getBackground();
        Drawable drawablePressed = view.getBackground().getConstantState().newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.parseColor("#969696"), PorterDuff.Mode.SRC_ATOP);
        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[] {android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[] {}, drawableNormal);
        view.setBackground(listDrawable);
    }


    public static String getEmail2() {
        return Email2;
    }

    public static String getShops() {
        return Shops;
    }


}
