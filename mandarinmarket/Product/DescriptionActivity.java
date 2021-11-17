package com.example.mandarinmarket.Product;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.MoreDetails_model;
import com.example.mandarinmarket.Model.Myadapter_ListView;
import com.example.mandarinmarket.Model.Myadapter_MoreDetails;
import com.example.mandarinmarket.Model.Productes_model;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class DescriptionActivity extends AppCompatActivity {
    TextView description_name_shops,description_name_product, description_butoon_add, description_button, description_description, price2_item_products_description, discount_price_item_products_description,price4_item_products_description;
    ImageView description_prod_favorites, description_image_product;

    RecyclerView recyclerView;
    ArrayList<MoreDetails_model> datalist;
    FirebaseDatabase db;
    DatabaseReference root;
    DatabaseReference root2;
    Myadapter_MoreDetails myadapter_moreDetails;

    ListView listView;

    Productes_model productesmodel;

    Boolean bool = true;
    Boolean bool2 = true;
    DatabaseReference databasePropertyData = null;
    String ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        description_button =(TextView) findViewById(R.id.description_button);
        description_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        description_name_shops =(TextView) findViewById(R.id.description_name_shops);
        description_name_shops.setText(PeremennyeActivity.ShopsName.toString());

        description_name_product =(TextView) findViewById(R.id.description_name_product);
        description_butoon_add =(TextView) findViewById(R.id.description_butoon_add);
        description_description =(TextView) findViewById(R.id.description_description);
        price2_item_products_description =(TextView) findViewById(R.id.price2_item_products_description);
        discount_price_item_products_description =(TextView) findViewById(R.id.discount_price_item_products_description);
        price4_item_products_description =(TextView) findViewById(R.id.price4_item_products_description);
        description_prod_favorites =(ImageView) findViewById(R.id.description_prod_favorites);
        description_image_product =(ImageView) findViewById(R.id.description_image_product);

        productesmodel = PeremennyeActivity.productesmodel;

        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        description_name_product.setText(productesmodel.getName());
        final DatabaseReference RootRef3 = FirebaseDatabase.getInstance().getReference().child("Users").child(PeremennyeActivity.getEmail2()).child("Favorites");
        RootRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshott : snapshot.getChildren()) {
                        if (snapshott.getKey().toString().equals("IdProductFavorites") && snapshott.getValue().toString().equals(productesmodel.getIdProduct().toString())) {
                            description_prod_favorites.setImageResource(R.drawable.ic_favorite2);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        description_prod_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(description_prod_favorites);
                bool2 = true;
                final DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(PeremennyeActivity.getEmail2()).child("Favorites");
                RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshott : snapshot.getChildren()) {
                                if (snapshott.getKey().toString().equals("IdProductFavorites") && snapshott.getValue().toString().equals(productesmodel.getIdProduct().toString())) {
                                    bool2 = false;
                                    RootRef.child("Users").child(PeremennyeActivity.getEmail2()).child("Favorites").child(snapshot.getKey()).removeValue();
                                    Toast.makeText(DescriptionActivity.this, productesmodel.getName() + " удалено из избранного!", LENGTH_SHORT).show();
                                    description_prod_favorites.setImageResource(R.drawable.ic_favorite_border);
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
                        if (bool2){
                            databasePropertyData =  FirebaseDatabase.getInstance().getReference("Users").child(PeremennyeActivity.getEmail2()).child("Favorites");
                            ownerId = databasePropertyData.push().getKey();
                            final HashMap<String, Object> UserDataMap = new HashMap<>();
                            UserDataMap.put("IdProductFavorites", productesmodel.getIdProduct());
                            UserDataMap.put("ShopsName", PeremennyeActivity.ShopsName);
                            UserDataMap.put("Shops", PeremennyeActivity.Shops);
                            UserDataMap.put("Categories", PeremennyeActivity.Categoriya);
                            RootRef.child("Users").child(PeremennyeActivity.getEmail2()).child("Favorites").child(ownerId).updateChildren(UserDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DescriptionActivity.this, productesmodel.getName() +" добавлено в избранное!", LENGTH_SHORT).show();
                                        description_prod_favorites.setImageResource(R.drawable.ic_favorite2);
                                    } else {
                                        Toast.makeText(DescriptionActivity.this, productesmodel.getName() +" не добавлено в избранное!", LENGTH_SHORT).show();
                                        description_prod_favorites.setImageResource(R.drawable.ic_favorite_border);
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
        Picasso.get().load(productesmodel.getSrcImage()).into(description_image_product);
        if(productesmodel.getDiscount_price()==0) {
            price2_item_products_description.setText(productesmodel.getPrice().toString());
            discount_price_item_products_description.setVisibility(View.GONE);
        }
        else {
            price2_item_products_description.setText(productesmodel.getPrice().toString());
            price2_item_products_description.setPaintFlags(price2_item_products_description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            price2_item_products_description.setTextColor(Color.RED);
            discount_price_item_products_description.setText(productesmodel.getDiscount_price().toString());
            discount_price_item_products_description.setVisibility(View.VISIBLE);
        }
        price4_item_products_description.setText(productesmodel.getSht());
        description_description.setText(productesmodel.getDescription());

        description_butoon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PeremennyeActivity.Products = productesmodel.getNameP();
                PeremennyeActivity.addClickEffect(description_butoon_add);
                bool = true;
                final DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(PeremennyeActivity.getEmail2()).child("Basket");
                RootRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshott : snapshot.getChildren()) {
                                if (snapshott.getKey().equals("IdProductBasket") && snapshott.getValue().equals(productesmodel.getIdProduct().toString())) {
                                    bool = false;
                                    Integer re = Integer.parseInt(String.valueOf(snapshot.child("Quantity").getValue(Integer.class)));
                                    RootRef.child("Users").child(PeremennyeActivity.getEmail2()).child("Basket").child(snapshot.getKey()).child("Quantity").setValue(re +1);
                                    Toast.makeText(DescriptionActivity.this, productesmodel.getName() +" добавлено в корзину!", LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //PeremennyeActivity.addClickEffect(description_butoon_add);
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(bool){
                            databasePropertyData =  FirebaseDatabase.getInstance().getReference("Users");
                            ownerId = databasePropertyData.push().getKey();
                            final HashMap<String, Object> UserDataMap = new HashMap<>();
                            UserDataMap.put("Shops", PeremennyeActivity.Shops);
                            UserDataMap.put("ShopsName", PeremennyeActivity.ShopsName);
                            UserDataMap.put("Id", ownerId);
                            UserDataMap.put("Categories", PeremennyeActivity.Categoriya);
                            UserDataMap.put("Productes", PeremennyeActivity.Products);
                            UserDataMap.put("Quantity", 1);
                            UserDataMap.put("IdProductBasket", productesmodel.getIdProduct().toString());
                            RootRef.child("Users").child(PeremennyeActivity.getEmail2()).child("Basket").child(ownerId).updateChildren(UserDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DescriptionActivity.this, productesmodel.getName() +" добавлено в корзину!", LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DescriptionActivity.this, productesmodel.getName() +" не добавлено в корзину!", LENGTH_SHORT).show();
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


       listView = (ListView) findViewById(R.id.description_list_view);
        datalist= new ArrayList();
        db = FirebaseDatabase.getInstance();

        root = db.getReference().child("Shops").child("Product").child(PeremennyeActivity.Shops).child(PeremennyeActivity.Categoriya).child(productesmodel.getNameP()).child("moredetails");
       /*MoreDetails_model mor1 = new MoreDetails_model("11","11");
       MoreDetails_model mor2 = new MoreDetails_model("222","2222");
       MoreDetails_model mor3 = new MoreDetails_model("33","333");
       MoreDetails_model mor4 = new MoreDetails_model("44","44444");*/
        final Myadapter_ListView myadapter_listView = new Myadapter_ListView(this, datalist);
        listView.setAdapter(myadapter_listView);
        //listView.setScrollContainer(false);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoreDetails_model moreDetails_model = dataSnapshot.getValue(MoreDetails_model.class);
                    datalist.add(moreDetails_model);
                    //Toast.makeText(DescriptionActivity.this, moreDetails_model.getNameD().toString(), LENGTH_SHORT).show();

                }
                myadapter_listView.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(listView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




        /*datalist.add(mor1);
       datalist.add(mor2);
       datalist.add(mor3);
       datalist.add(mor4);*/
        //myadapter_listView.notifyDataSetChanged();
        //final Myadapter_ListView myadapter_listView = new Myadapter_ListView(this, datalist);

       // Toast.makeText(DescriptionActivity.this, myadapter_listView.getCount(), LENGTH_SHORT).show();

        /* root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoreDetails_model moreDetails_model = dataSnapshot.getValue(MoreDetails_model.class);
                    datalist.add(moreDetails_model);
                    Toast.makeText(DescriptionActivity.this, moreDetails_model.getNameD().toString(), LENGTH_SHORT).show();

                }
                myadapter_listView.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

//more details
        //Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');

   /*     recyclerView = (RecyclerView) findViewById(R.id.description_recycler_view);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("Shops").child("Product").child(PeremennyeActivity.Shops).child(PeremennyeActivity.Categoriya).child(productesmodel.getNameP()).child("moredetails");
        datalist=new ArrayList<>();
        myadapter_moreDetails = new Myadapter_MoreDetails(this, datalist);
        recyclerView.setAdapter(myadapter_moreDetails);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoreDetails_model moreDetails_model = dataSnapshot.getValue(MoreDetails_model.class);
                    datalist.add(moreDetails_model);
                }
                myadapter_moreDetails.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) { return; }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight +totalHeight/4 + (listView.getDividerHeight() * (adapter.getCount()-1));
        listView.setLayoutParams(par); listView.requestLayout();
    }
}