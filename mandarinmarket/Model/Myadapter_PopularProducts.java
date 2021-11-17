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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class Myadapter_PopularProducts extends RecyclerView.Adapter<Myadapter_PopularProducts.myviewholder>{
    //public static Object setdatalistshop;
    ArrayList<PopularProducts_model> datalist;
   // ArrayList<Shops_model> datalist2;
    Context context;
    FirebaseDatabase db;
    DatabaseReference root;
    Boolean bool = true;
    Boolean bool2 = true;
    String ownerId;
    DatabaseReference databasePropertyData = null;
    DatabaseReference root3;



    public static ArrayList<Shops_model> datalistshop;

    //String testString;

    public Myadapter_PopularProducts(Context context, ArrayList<PopularProducts_model> datalist){
        this.datalist = datalist;
        //this.datalist2 = datalist2;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_popular_productes, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, final int position) {
        final PopularProducts_model popularProducts_model =  datalist.get(position);
        //final Shops_model shops_model =  PeremennyeActivity.datalistshop.get(position);
        final Productes_model[] productes_model = new Productes_model[1];

        db = FirebaseDatabase.getInstance();

        root3 = db.getReference().child("Shops").child("Shop").child(popularProducts_model.getShop());
        root3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                //for(DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {
                Shops_model shops_model = snapshot2.getValue(Shops_model.class);
                holder.t1.setText(shops_model.getNameShop().toString());
                PeremennyeActivity.datalistshop.add(shops_model);
                //System.out.println("\n\n\n//////"+shops_model.toString());
                //System.out.println("\n\n\n//////"+PeremennyeActivity.datalistshop.size());
                //  datalist3.add(shops_model);
                //Toast.makeText(getActivity(), shops_model.getNameShop().toString(), Toast.LENGTH_SHORT);

                // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = RootRef.child("Shops").child("Product").child(popularProducts_model.getShop().toString())
                .child(popularProducts_model.getCategories().toString()).orderByChild("IdProduct").equalTo(popularProducts_model.getIdPopularProduct());
        //Query applesQuery = RootRef.child("Shops").child("Product").queryOrdered(byChild: "uid").queryEqual(toValue: "yourUserId");
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot Snapshot2 : Snapshot.getChildren()) {
                        if(Snapshot2.getKey().equals("IdProduct") ){
                            productes_model[0] =  Snapshot.getValue(Productes_model.class);
                            ///System.out.println("\n\n\n//////"+PeremennyeActivity.datalistshop.size());
                           /* if (PeremennyeActivity.datalistshop.size() >0){
                                holder.t1.setText(PeremennyeActivity.datalistshop.get(position).getNameShop().toString());
                            }*/

                          //  holder.t1.setText(PeremennyeActivity.datalistshop.size());
                          //  System.out.println("//////"+PeremennyeActivity.datalistshop.size());
                            //Toast.makeText(context, Snapshot2.toString(), Toast.LENGTH_SHORT).show();
                            Picasso.get().load(productes_model[0].getSrcImage()).into(holder.t2);
                            if(productes_model[0].getDiscount_price()==0) {
                                holder.t3.setText(productes_model[0].getPrice().toString());
                                holder.t4.setVisibility(View.GONE);
                            }
                            else {
                                holder.t3.setText(productes_model[0].getPrice().toString());
                                holder.t3.setPaintFlags(holder.t3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.t3.setTextColor(Color.RED);
                                holder.t4.setText(productes_model[0].getDiscount_price().toString());
                                holder.t4.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    //Toast.makeText(context, appleSnapshot.toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


        holder.t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeremennyeActivity.addClickEffect(holder.t5);
                //PeremennyeActivity.Products = productesmodel.getNameP();
                bool = true;
                final DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(PeremennyeActivity.getEmail2()).child("Basket");
                RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshott : snapshot.getChildren()) {
                                if (snapshott.getKey().equals("IdProductBasket") && snapshott.getValue().equals(productes_model[0].getIdProduct().toString())) {
                                    bool = false;
                                    Integer re = Integer.parseInt(String.valueOf(snapshot.child("Quantity").getValue(Integer.class)));
                                    RootRef.child("Users").child(PeremennyeActivity.getEmail2()).child("Basket").child(snapshot.getKey()).child("Quantity").setValue(re +1);
                                    Toast.makeText(context, productes_model[0].getName() +" добавлено в корзину!", Toast.LENGTH_SHORT).show();
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
                            UserDataMap.put("Shops", popularProducts_model.getShop());
                            UserDataMap.put("ShopsName", PeremennyeActivity.datalistshop.get(position).getNameShop());
                            UserDataMap.put("Id", ownerId);
                            UserDataMap.put("Categories", popularProducts_model.getCategories());
                            UserDataMap.put("Productes", productes_model[0].getNameP());
                            UserDataMap.put("Quantity", 1);
                            UserDataMap.put("IdProductBasket", productes_model[0].getIdProduct().toString());
                            RootRef.child("Users").child(PeremennyeActivity.getEmail2()).child("Basket").child(ownerId).updateChildren(UserDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, productes_model[0].getName() +" добавлено в корзину!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, productes_model[0].getName() +" не добавлено в корзину!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });


        /*root = db.getReference().child("Shops").child("Product");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Shops");
        ref.child("Product").orderByChild("asd").equalTo("asd").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Toast.makeText(context, userSnapshot.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });*/



        /*
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        Query PopularQuery = RootRef.child("Shops").child("Product").orderByChild("asd").equalTo("asd");
                //.equalTo(popularProducts_model.getIdPopularProduct().toString());

        PopularQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot: snapshot.getChildren()) {
                    Toast.makeText(context, datasnapshot.toString(), Toast.LENGTH_SHORT).show();
                    holder.t1.setText(datasnapshot.getValue().toString());
                }
                //Toast.makeText(context, snapshot.toString(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, popularProducts_model.getIdPopularProduct().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, " *-* " , Toast.LENGTH_SHORT).show();

                /*for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    Toast.makeText(context, appleSnapshot.getKey() +" *-* " + appleSnapshot.getValue(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                            Toast.makeText(context, , Toast.LENGTH_SHORT).show();
                            /*for (DataSnapshot snapshot4 : snapshot3.getChildren()) {
                                if (snapshot4.getKey().equals("IdProductBasket") ) {
                                    Toast.makeText(context, snapshot4.getKey() +" *-* " + snapshot4.getValue(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        /*if (snapshott.getKey().equals("IdProductBasket") && snapshott.getValue().equals(productesmodel.getIdProduct().toString())) {
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
        });*/

        /*holder.t1.setText(categories.getName());
        holder.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeremennyeActivity.Categoriya = categories.getNameC();
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                ProductesFragment productesFragment = new ProductesFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,productesFragment).addToBackStack( "tag" ).commit();
            }
        });*/
        holder.t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(holder.t6);
                PeremennyeActivity.productesmodel = productes_model[0];
                PeremennyeActivity.Shops = popularProducts_model.getShop();
                PeremennyeActivity.Categoriya = popularProducts_model.getCategories();
                PeremennyeActivity.ShopsName = PeremennyeActivity.datalistshop.get(position).getNameShop();

                Intent DescriptionActivity = new Intent(Myadapter_PopularProducts.this.context, com.example.mandarinmarket.Product.DescriptionActivity.class);
                holder.t6.getContext().startActivity(DescriptionActivity);

            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView t1,t3,t4;
        ImageView t2,t5;
        LinearLayout t6;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.item_populaar_shop);
            t2 = itemView.findViewById(R.id.item_populaar_image);
            t3 = itemView.findViewById(R.id.item_populaar_price);
            t4 = itemView.findViewById(R.id.item_populaar_price2);
            t5 = itemView.findViewById(R.id.item_populaar_botton);
            t6 = itemView.findViewById(R.id.liner_popul);
        }
    }
}
