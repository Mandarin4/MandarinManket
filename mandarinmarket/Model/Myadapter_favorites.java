package com.example.mandarinmarket.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.UI.Users.RegicterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.mandarinmarket.R.id;
import static com.example.mandarinmarket.R.layout;

public class Myadapter_favorites extends RecyclerView.Adapter<Myadapter_favorites.myviewholder>{
    ArrayList<Productes_model> datalist;
    Context context;
    Boolean bool = true;
    Boolean bool2 = true;
    String ownerId;
    DatabaseReference databasePropertyData = null;

    public Myadapter_favorites(Context context, ArrayList<Productes_model> datalist){
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(layout.item_favorites, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, final int position) {
        //final Favorites_model favorites_model =  datalist.get(position);
        final Productes_model productesmodel =  datalist.get(position);
        final Favorites_model favorites_model = PeremennyeActivity.datalistfavorit.get(position);
        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        Picasso.get().load(productesmodel.getSrcImage()).into(holder.t1);
        holder.t2.setText(productesmodel.getName());
        if(productesmodel.getDiscount_price()==0) {
            holder.t3.setText(productesmodel.getPrice().toString());
            holder.t8.setVisibility(View.GONE);
        }
        else {
            holder.t3.setText(productesmodel.getPrice().toString());
            holder.t3.setPaintFlags(holder.t3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.t3.setTextColor(Color.RED);
            holder.t8.setText(productesmodel.getDiscount_price().toString());
            holder.t8.setVisibility(View.VISIBLE);
        }

        //holder.t3.setText(productesmodel.getPrice().toString());
        holder.t4.setText(productesmodel.getSht());
        holder.t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeremennyeActivity.Products = productesmodel.getNameP();
                bool = true;
                final DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(Email2).child("Basket");
                RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshott : snapshot.getChildren()) {
                                if (snapshott.getKey().equals("IdProductBasket") && snapshott.getValue().equals(productesmodel.getIdProduct().toString())) {
                                    bool = false;
                                    Integer re = Integer.parseInt(String.valueOf(snapshot.child("Quantity").getValue(Integer.class)));
                                    RootRef.child("Users").child(Email2).child("Basket").child(snapshot.getKey()).child("Quantity").setValue(re +1);
                                    Toast.makeText(context, productesmodel.getName() +" добавлено в корзину!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(bool){
                            databasePropertyData =  FirebaseDatabase.getInstance().getReference("Users");
                            ownerId = databasePropertyData.push().getKey();
                            final HashMap<String, Object> UserDataMap = new HashMap<>();
                            UserDataMap.put("Shops", favorites_model.getShops());
                            UserDataMap.put("ShopsName", favorites_model.getShopsName());
                            UserDataMap.put("Id", ownerId);
                            UserDataMap.put("Categories", favorites_model.getCategories());
                            UserDataMap.put("Productes", PeremennyeActivity.Products);
                            UserDataMap.put("Quantity", 1);
                            UserDataMap.put("IdProductBasket", productesmodel.getIdProduct().toString());
                            RootRef.child("Users").child(Email2).child("Basket").child(ownerId).updateChildren(UserDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, productesmodel.getName() +" добавлено в корзину!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, productesmodel.getName() +" не добавлено в корзину!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                PeremennyeActivity.addClickEffect(holder.t5);
            }
        });
        holder.t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(holder.t6);
                final DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(Email2).child("Favorites");
                RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshott : snapshot.getChildren()) {
                                if (snapshott.getKey().toString().equals("IdProductFavorites") && snapshott.getValue().toString().equals(productesmodel.getIdProduct().toString())) {
                                    RootRef.child("Users").child(Email2).child("Favorites").child(snapshot.getKey()).removeValue();
                                    Toast.makeText(context, productesmodel.getName() + " удалено из избранного!", Toast.LENGTH_SHORT).show();
                                    if(datalist.size() == 1){
                                        datalist.remove(position);
                                        PeremennyeActivity.datalistfavorit.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, datalist.size());
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.t7.setText(favorites_model.getShopsName());

        holder.t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(holder.t9);
                PeremennyeActivity.productesmodel = productesmodel;
                PeremennyeActivity.Shops = favorites_model.getShops();
                PeremennyeActivity.Categoriya = favorites_model.getCategories();
                PeremennyeActivity.ShopsName = favorites_model.getShopsName();
                Intent DescriptionActivity = new Intent(Myadapter_favorites.this.context, com.example.mandarinmarket.Product.DescriptionActivity.class);
                holder.t9.getContext().startActivity(DescriptionActivity);

            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView t1, t6;
        TextView t2,t3,t4, t5,t7,t8;
        LinearLayout t9;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(id.item_image_favorites);
            t2 = itemView.findViewById(id.item_text_nameproduct_favorites);
            t3 = itemView.findViewById(id.price2_item_products_favorites);
            t4 = itemView.findViewById(id.price4_item_products_favorites);
            t5 = itemView.findViewById(id.item_text_bottom_favorites);
            t6 = itemView.findViewById(id.item_favorites_favorites);
            t7 = itemView.findViewById(id.nameShops_item_favorites);
            t8 = itemView.findViewById(id.discount_price_item_favorites);
            t9 = itemView.findViewById(id.liner_favor);
        }
    }
}
