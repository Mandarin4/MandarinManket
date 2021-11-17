package com.example.mandarinmarket.Product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.Categories_model;
import com.example.mandarinmarket.Model.Myadapter_categories;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    private Button buttonCategories;
    private TextView nameUser, textcategoriesShops2;
    RecyclerView recyclerView ;
    ArrayList<Categories_model> datalist;
    FirebaseDatabase db;
    DatabaseReference root;
    DatabaseReference root2;
    Myadapter_categories Myadapter_categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_categories,container,false);

        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        buttonCategories = (Button) v.findViewById(R.id.buttonCategories);
        nameUser =(TextView) v.findViewById(R.id.nameUserFormCategories);
        textcategoriesShops2 =(TextView) v.findViewById(R.id.textcategoriesShops2);
        //buttonCategories =(Button) v.findViewById(R.id.buttonCategories);
        nameUser.setText(PeremennyeActivity.username);
        db = FirebaseDatabase.getInstance();
        root2 = db.getReference().child("Shops").child("Shop").child(PeremennyeActivity.Shops).child("nameShop");
        root2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textcategoriesShops2.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.rs_list_categories2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        root = db.getReference().child("Shops").child("Categories").child(PeremennyeActivity.Shops);
        datalist=new ArrayList<>();
        Myadapter_categories = new Myadapter_categories(getContext(), datalist);
        recyclerView.setAdapter(Myadapter_categories);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Categories_model categ = dataSnapshot.getValue(Categories_model.class);
                    datalist.add(categ);
                }
                Myadapter_categories.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        buttonCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                HomeFragment homeFragment = new HomeFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
            }
        });

       /* // Системная кнопка назад
        @Override
        public void onBackPressed(){
            AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
            HomeFragment homeFragment = new HomeFragment();
            appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();
        }*/
        return v;
    }

}
