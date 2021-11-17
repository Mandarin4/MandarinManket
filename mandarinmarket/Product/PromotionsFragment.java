package com.example.mandarinmarket.Product;

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

import com.example.mandarinmarket.Model.Myadapter_PopularProducts;
import com.example.mandarinmarket.Model.Myadapter_promotions;
import com.example.mandarinmarket.Model.PopularProducts_model;
import com.example.mandarinmarket.Model.Productes_model;
import com.example.mandarinmarket.Model.Shops_model;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PromotionsFragment extends Fragment {
    private boolean isFirstBackPressed = false;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed = -1;
    private int clicks = 0;

    private long backPressedtimer;
    private Toast backToast;
    private Button logoutBtn;
    private TextView nameUser;
    RecyclerView recyclerView;
    // ArrayList<Promotions_model> datalist;
    ArrayList<Productes_model> datalist;
    public static ArrayList<Shops_model> datalist3;
    FirebaseDatabase db;
    DatabaseReference root;
    DatabaseReference root2;
    Myadapter_promotions myadapterpromotions;
    //Myadapter_promotions myadapterpromotions;
    RecyclerView recyclerView2;
    ArrayList<PopularProducts_model> datalist2;
    //ArrayList<Shops_model> datalist3;
    //DatabaseReference root2;

    DatabaseReference root4;
    Myadapter_PopularProducts myadapter_popularProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_promotions, container, false);

        PeremennyeActivity.mAuth = FirebaseAuth.getInstance();
        nameUser = (TextView) v.findViewById(R.id.nameUserFormPromotions);
        logoutBtn = (Button) v.findViewById(R.id.buttonPromotions);
        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        nameUser.setText(PeremennyeActivity.username);

        db = FirebaseDatabase.getInstance();

        recyclerView2 = (RecyclerView) v.findViewById(R.id.rs_list_product_popular);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);
        root2 = db.getReference().child("Shops").child("PopularProducts");
        datalist2 = new ArrayList<>();
        datalist3 = new ArrayList<>();
        PeremennyeActivity.datalistshop = new ArrayList<>();
        PeremennyeActivity.datalistshop2 = new ArrayList<>();
        myadapter_popularProducts = new Myadapter_PopularProducts(getContext(), datalist2);
        recyclerView2.setAdapter(myadapter_popularProducts);
        root2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PopularProducts_model popularProducts_model = dataSnapshot.getValue(PopularProducts_model.class);
                    datalist2.add(popularProducts_model);
                    Toast.makeText(getActivity(), popularProducts_model.getShop().toString(), Toast.LENGTH_SHORT);
                }
                myadapter_popularProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.rs_list_Promotions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        root = db.getReference().child("Shops").child("Product");
        root4 = db.getReference();
        datalist = new ArrayList<>();
        PeremennyeActivity.Shops2 = new ArrayList<>();
        PeremennyeActivity.Categoriya2 = new ArrayList<>();
        myadapterpromotions = new Myadapter_promotions(getContext(), datalist);
        recyclerView.setAdapter(myadapterpromotions);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // System.out.println("\n\n\n//////" + snapshot.toString());
                for (final DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (final DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                       // Toast.makeText(getActivity(), "***************", Toast.LENGTH_SHORT);
                        Query applesQuery = root4.child("Shops").child("Product").child(dataSnapshot.getKey().toString()).child(dataSnapshot2.getKey().toString()).orderByChild("discount_price").startAt(1);
                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot rdataSnapshot) {
                                for (DataSnapshot appleSnapshot : rdataSnapshot.getChildren()) {
                                    Productes_model productes_model = appleSnapshot.getValue(Productes_model.class);
                                    datalist.add(productes_model);
                                    PeremennyeActivity.Shops2.add(dataSnapshot.getKey().toString());
                                    PeremennyeActivity.Categoriya2.add(dataSnapshot2.getKey().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                    }
                }
                myadapterpromotions.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return v;
    }
}