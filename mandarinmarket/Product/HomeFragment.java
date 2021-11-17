package com.example.mandarinmarket.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.Myadapter_shops;
import com.example.mandarinmarket.Model.Shops_model;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.example.mandarinmarket.UI.Users.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class HomeFragment extends Fragment {
    private long backPressedtimer;
    private Toast backToast;
    private Button logoutBtn;
    private TextView nameUser;
    RecyclerView recyclerView ;
    ArrayList<Shops_model> datalist;
    FirebaseDatabase db;
    DatabaseReference root;
    Myadapter_shops myadaptershops;
    String r = "Null";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home,container,false);

        PeremennyeActivity.mAuth = FirebaseAuth.getInstance();
        nameUser =(TextView) v.findViewById(R.id.nameUserFormShops);

        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        nameUser.setText(PeremennyeActivity.username);
        recyclerView = (RecyclerView) v.findViewById(R.id.rs_list_shops2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("Shops").child("Shop");
        datalist=new ArrayList<>();
        myadaptershops = new Myadapter_shops(getContext(), datalist);
        recyclerView.setAdapter(myadaptershops);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Shops_model shopsmodel = dataSnapshot.getValue(Shops_model.class);
                    datalist.add(shopsmodel);
                }
                myadaptershops.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        logoutBtn = (Button) v.findViewById(R.id.buttonShops);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                PeremennyeActivity.mAuth.signOut();
                if( PeremennyeActivity.mAuthListener != null)  PeremennyeActivity.mAuth.removeAuthStateListener( PeremennyeActivity.mAuthListener);
                Intent logoutIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(logoutIntent);
            }
        });

        return v;
    }

}
