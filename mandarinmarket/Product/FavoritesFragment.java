package com.example.mandarinmarket.Product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.Favorites_model;
import com.example.mandarinmarket.Model.Myadapter_favorites;
import com.example.mandarinmarket.Model.Productes_model;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.example.mandarinmarket.UI.Users.RegicterActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FavoritesFragment extends Fragment {
    //private Button buttonProductes;
    private TextView nameUser, favorites_fragment_ismpty;
    RecyclerView recyclerView ;
    //ArrayList<Favorites_model> datalist;
    ArrayList<Productes_model> datalist2;
    FirebaseDatabase db;
    DatabaseReference root;
    Myadapter_favorites myadapter_favorites;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

//<activity android:name=".Product.FavoritesFragment" /> <activity android:name=".UI.Admin.AdminCategoryActivity" />
//        <activity android:name=".UI.Admin.AdminAddNewProductActivity" />

        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        nameUser =(TextView) v.findViewById(R.id.nameUserFormFavorites);
        nameUser.setText(PeremennyeActivity.username);
        favorites_fragment_ismpty = (TextView) v.findViewById(R.id.favorites_fragment_ismpty);
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("Shops").child("Product");
        recyclerView = (RecyclerView) v.findViewById(R.id.rs_list_Favorites);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("Users").child(PeremennyeActivity.getEmail2()).child("Favorites");
        PeremennyeActivity.datalistfavorit=new ArrayList<>();
        datalist2=new ArrayList<>();
        myadapter_favorites = new Myadapter_favorites(getContext(),datalist2);
        recyclerView.setAdapter(myadapter_favorites);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PeremennyeActivity.datalistfavorit.clear();
                datalist2.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final Favorites_model favorites_model = dataSnapshot.getValue(Favorites_model.class);
                    PeremennyeActivity.datalistfavorit.add(favorites_model);
                    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                            //Toast.makeText(context, snapshot3.getKey(), Toast.LENGTH_SHORT).show();
                                            if (snapshot3.getKey().toString().equals("IdProduct") && snapshot3.getValue().toString().equals(favorites_model.getIdProductFavorites().toString())) {
                                                Productes_model productesmodel = snapshot2.getValue(Productes_model.class);
                                                //Toast.makeText(getActivity(), productesmodel.getNameP().toString(), Toast.LENGTH_SHORT).show();
                                                datalist2.add(productesmodel);
                                            }
                                            // }
                                        }
                                    }
                                }
                            }
                            myadapter_favorites.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if (PeremennyeActivity.datalistfavorit.isEmpty()){
                    favorites_fragment_ismpty.setVisibility(VISIBLE);
                }
                else favorites_fragment_ismpty.setVisibility(INVISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return v;
    }
}