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

import com.example.mandarinmarket.Model.Myadapter_productes;
import com.example.mandarinmarket.Model.Productes_model;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductesFragment extends Fragment {
    private Button buttonProductes;
    private TextView nameUser,textProductesShops2 ;
    RecyclerView recyclerView ;
    ArrayList<Productes_model> datalist;
    FirebaseDatabase db;
    DatabaseReference root;
    DatabaseReference root2;
    Myadapter_productes myadapterproductes;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_productes,container,false);
        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        buttonProductes = (Button) v.findViewById(R.id.buttonProductes);
        textProductesShops2 =(TextView) v.findViewById(R.id.textProductesShops2);
        nameUser =(TextView) v.findViewById(R.id.nameUserFormProductes);
        nameUser.setText(PeremennyeActivity.username);

        recyclerView = (RecyclerView) v.findViewById(R.id.rs_list_Productes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseDatabase.getInstance();

        root2 = db.getReference().child("Shops").child("Shop").child(PeremennyeActivity.Shops).child("nameShop");
        root2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textProductesShops2.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        root = db.getReference().child("Shops").child("Product").child(PeremennyeActivity.Shops).child(PeremennyeActivity.Categoriya);
        datalist=new ArrayList<>();
        myadapterproductes = new Myadapter_productes(getContext(), datalist);
        recyclerView.setAdapter(myadapterproductes);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Productes_model productesmodel = dataSnapshot.getValue(Productes_model.class);
                    datalist.add(productesmodel);
                }
                myadapterproductes.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        buttonProductes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoriesFragment).commit();
            }
        });

        return v;
    }
}
