package com.example.mandarinmarket.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.example.mandarinmarket.UI.Users.RegicterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Myadapter_basket extends RecyclerView.Adapter<Myadapter_basket.myviewholder>{
    ArrayList<Productes_model> datalist;
    ArrayList<Basket_model> datalist2;

    Context context;
    public Myadapter_basket(Context context,ArrayList<Productes_model> datalist){
        this.datalist = datalist;
        this.datalist2 = datalist2;
        this.context = context;

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_basket, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, final int position) {
        final Basket_model basketmodel = PeremennyeActivity.datalistbasket.get(position);
        final Productes_model productesmodel =  datalist.get(position);
/*
        System.out.println("\n\n\n                      ((((*************************************************position - " + position);
        System.out.println("\n\n\n                       ))))*************************************************datalist2.size() - " + datalist2.size());
        System.out.println("\n\n\n                      /*************************************************" + basketmodel.getShopsName().toString());
        System.out.println("\n\n\n                      /*************************************************" + basketmodel.getProductes().toString());
        System.out.println("\n\n\n                      ////*************************************************" + basketmodel.getQuantity().toString());
        System.out.println("************************************************************************************");*/
        Picasso.get().load(productesmodel.getSrcImage()).into(holder.t1);
        holder.t2.setText(productesmodel.getName());
        if(productesmodel.getDiscount_price()==0) {
            holder.t3.setText(productesmodel.getPrice().toString());
            holder.t10.setVisibility(View.GONE);
        }
        else {
            holder.t3.setText(productesmodel.getPrice().toString());
            holder.t3.setPaintFlags(holder.t3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.t3.setTextColor(Color.RED);
            holder.t10.setText(productesmodel.getDiscount_price().toString());
            holder.t10.setVisibility(View.VISIBLE);
        }
        //holder.t3.setText(productesmodel.getPrice().toString());
        holder.t4.setText(productesmodel.getSht());
        holder.t8.setText(basketmodel.getQuantity().toString());
        holder.t9.setText(basketmodel.getShopsName());
        holder.t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = RootRef.child("Users").child(PeremennyeActivity.getEmail2()).child("Basket").orderByChild("IdProductBasket").equalTo(productesmodel.getIdProduct().toString());
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            if(datalist.size() == 1){
                                datalist.remove(position);
                                PeremennyeActivity.datalistbasket.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, datalist.size());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
        holder.t6.setOnClickListener(new View.OnClickListener() {
            //PeremennyeActivity.addClickEffect(holder.t6);
            DatabaseReference RootRef;
            final DatabaseReference[] databasePropertyData = new DatabaseReference[1];
            @Override
            public void onClick(View v) {
                RootRef = FirebaseDatabase.getInstance().getReference();
                databasePropertyData[0] =  FirebaseDatabase.getInstance().getReference("Users");
                final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
                //String ownerId = databasePropertyData[0].push().getKey();
                final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
                if(basketmodel.getQuantity() > 1) {
                    RootRef.child("Users").child(Email2).child("Basket").child(basketmodel.getId()).child("Quantity").setValue(basketmodel.getQuantity() - 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(context, productesmodel.getName() +" добавлено в корзину!", Toast.LENGTH_SHORT).show();
                            } else {
                            }
                        }
                    });
                }
            }
        });
        holder.t7.setOnClickListener(new View.OnClickListener() {
            //PeremennyeActivity.addClickEffect(holder.t7);


            DatabaseReference RootRef;
            //final DatabaseReference[] databasePropertyData = new DatabaseReference[1];
            @Override
            public void onClick(View v) {
                RootRef = FirebaseDatabase.getInstance().getReference();
               // databasePropertyData[0] =  FirebaseDatabase.getInstance().getReference("Users");
                final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
                //String ownerId = databasePropertyData[0].push().getKey();
                final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
                RootRef.child("Users").child(Email2).child("Basket").child(basketmodel.getId()).child("Quantity").setValue(basketmodel.getQuantity() + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(context, productesmodel.getName() +" добавлено в корзину!", Toast.LENGTH_SHORT).show();
                        } else {}
                    }
                });
            }
        });

        holder.t11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.productesmodel = productesmodel;
                PeremennyeActivity.Categoriya = basketmodel.getCategories();
                PeremennyeActivity.Shops = basketmodel.getShops();
                PeremennyeActivity.ShopsName = basketmodel.getShopsName();

                Intent DescriptionActivity = new Intent(Myadapter_basket.this.context, com.example.mandarinmarket.Product.DescriptionActivity.class);
                holder.t11.getContext().startActivity(DescriptionActivity);

            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView t1;
        TextView t2,t3,t4,t5,t6,t7,t8,t9,t10;
        LinearLayout t11;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.item_image_basket);
            t2 = itemView.findViewById(R.id.itemtextnamebasket);
            t3 = itemView.findViewById(R.id.price2_item_basket);
            t4 = itemView.findViewById(R.id.price4_item_basket);
            t5 = itemView.findViewById(R.id.itemtextbottombasket);
            t6 = itemView.findViewById(R.id.minus_item_basket);
            t7 = itemView.findViewById(R.id.plus_item_basket);
            t8 = itemView.findViewById(R.id.kol2_item_basket);
            t9 = itemView.findViewById(R.id.nameShops_item_basket);
            t10 = itemView.findViewById(R.id.discount_price_item_basket);
            t11 = itemView.findViewById(R.id.liner_basket);
        }
    }
}
