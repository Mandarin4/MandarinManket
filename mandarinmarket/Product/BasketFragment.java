package com.example.mandarinmarket.Product;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.Basket_model;
import com.example.mandarinmarket.Model.Myadapter_basket;
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

public class BasketFragment extends Fragment {
    private Button logoutBtn;
    TextView nameUser;
    static TextView total3;
    static TextView total6;
    static TextView fragment_basket_button;
    TextView basket_fragment_ismpty;
    RecyclerView recyclerView ;
    ArrayList<Productes_model> datalist;
    ArrayList<String> arraylistshop2;
    FirebaseDatabase db;
    DatabaseReference root, basketroot;
    Myadapter_basket myadapterbasket;
    static Integer totalprice = 0;
    static Integer totalquan = 0;

    public Integer lastItem;
    DatabaseReference RootRef;
    DatabaseReference RootRef2;
    String ownerId;
    //Boolean bool = false;
    DatabaseReference databasePropertyData = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_basket,container,false);
        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        logoutBtn = (Button) v.findViewById(R.id.buttonBasket);
        nameUser =(TextView) v.findViewById(R.id.nameUserFormBasket);
        total3 = (TextView) v.findViewById(R.id.total3);
        total6 = (TextView) v.findViewById(R.id.total6);
        fragment_basket_button = (TextView) v.findViewById(R.id.fragment_basket_button);
        basket_fragment_ismpty = (TextView) v.findViewById(R.id.basket_fragment_ismpty);

        recyclerView = (RecyclerView) v.findViewById(R.id.rs_list_Basket);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseDatabase.getInstance();
        datalist=new ArrayList<>();
        myadapterbasket = new Myadapter_basket(getContext(), datalist);
        recyclerView.setAdapter(myadapterbasket);
        nameUser.setText(PeremennyeActivity.username);
        final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        basketroot = db.getReference().child("Users").child(Email2).child("Basket");
        PeremennyeActivity.datalistbasket = new ArrayList<>();
        PeremennyeActivity.arrayListShop = new ArrayList<>();
        arraylistshop2 = new ArrayList<>();
        basketroot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                PeremennyeActivity.datalistbasket.clear();
                totalprice = 0;
                totalquan = 0;
                arraylistshop2.clear();
                PeremennyeActivity.arrayListShop.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final Basket_model basketmodel = dataSnapshot.getValue(Basket_model.class);
                  /*  if(arraylistshop2.isEmpty()) arraylistshop2.add(basketmodel.getShopsName());
                    else for (int i = 0; i<arraylistshop2.size(); i++){
                        if(!arraylistshop2.get(i).equals(basketmodel.getShopsName())){
                            arraylistshop2.add(basketmodel.getShopsName());
                        }
                    } PeremennyeActivity.arrayListShop = arraylistshop2;*/

                    PeremennyeActivity.datalistbasket.add(basketmodel);
                    totalquan += basketmodel.getQuantity();
                    total3.setText(totalquan.toString());
                    root = db.getReference().child("Shops").child("Product").child(basketmodel.getShops()).child(basketmodel.getCategories()).child(basketmodel.getProductes());
                    root.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Productes_model productesmodel = snapshot.getValue(Productes_model.class);
                            datalist.add(productesmodel);
                            if (productesmodel.getDiscount_price() == 0)
                                totalprice += productesmodel.getPrice() * basketmodel.getQuantity();
                            else totalprice += productesmodel.getDiscount_price() * basketmodel.getQuantity();
                            total6.setText(totalprice.toString());
                            myadapterbasket.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                if (PeremennyeActivity.datalistbasket.isEmpty()){
                    basket_fragment_ismpty.setVisibility(VISIBLE);
                    fragment_basket_button.setClickable(false);
                    datalist.clear();
                    PeremennyeActivity.datalistbasket.clear();
                   // myadapterbasket.notifyDataSetChanged();
                    totalquan=0;
                    total3.setText(totalquan.toString());
                    totalprice = 0;
                    total6.setText(totalprice.toString());
                }
                else basket_fragment_ismpty.setVisibility(INVISIBLE);
                fragment_basket_button.setClickable(true);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Toast.makeText(getActivity(),myadapterbasket.getItemCount() , LENGTH_SHORT).show();
        lastItem=0;

        DatabaseReference RootRef2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = RootRef2.child("Shops").child("Orders");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> arrayList = (ArrayList<Integer>) snapshot.getValue();
                if (!arrayList.isEmpty()){
                    lastItem = arrayList.size();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        fragment_basket_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PeremennyeActivity.datalistbasket.isEmpty()){

                    basketroot.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                final Basket_model basketmodel = dataSnapshot.getValue(Basket_model.class);
                                if (arraylistshop2.isEmpty())
                                    arraylistshop2.add(basketmodel.getShopsName());
                                else for (int i = 0; i < arraylistshop2.size(); i++) {
                                    if (!arraylistshop2.get(i).equals(basketmodel.getShopsName())) {
                                        arraylistshop2.add(basketmodel.getShopsName());
                                    }
                                }
                                PeremennyeActivity.arrayListShop = arraylistshop2;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    PeremennyeActivity.addClickEffect(fragment_basket_button);
                    PeremennyeActivity.OrdersInt = lastItem;
                    PeremennyeActivity.totalprice = totalprice;
                    PeremennyeActivity.totalquan = totalquan;


                    PeremennyeActivity.datalist = datalist;
                    PeremennyeActivity.databasePropertyData = databasePropertyData;
                    PeremennyeActivity.ownerId = ownerId;



                    Intent intent = new Intent(getActivity(), BasketActivity.class);
                    startActivity(intent);
                }


                /*for(int i=0;i<datalist.size();i++){
                    //Toast.makeText(getActivity(), "Ваш заказ №" + lastItem +" оформлен---***! ", LENGTH_SHORT).show();
                    databasePropertyData =  FirebaseDatabase.getInstance().getReference("Users");
                    ownerId = databasePropertyData.push().getKey();
                    final Basket_model basketmodel = PeremennyeActivity.datalistbasket.get(i);
                    final Productes_model productesmodel =  datalist.get(i);
                    RootRef = FirebaseDatabase.getInstance().getReference();
                    final HashMap<String, Object> UserDataMap = new HashMap<>();
                    UserDataMap.put("Shops", basketmodel.getShops());
                    UserDataMap.put("ShopsName", basketmodel.getShopsName());
                    UserDataMap.put("IdProductOrders", basketmodel.getIdProductBasket());
                    UserDataMap.put("Quantity", basketmodel.getQuantity());
                    UserDataMap.put("NameProduct", productesmodel.getName());

                    final HashMap<String, Object> UserDataMap2 = new HashMap<>();
                    UserDataMap2.put("TotalPrice", totalprice);
                    UserDataMap2.put("User", user.getEmail());
                    Date date = new Date();
                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    final HashMap<String, Object> UserDataMap3 = new HashMap<>();
                    UserDataMap3.put("Ordernumber",lastItem);
                    UserDataMap3.put("TotalPrice",totalprice);
                    UserDataMap3.put("Date", formatForDateNow.format(date));

                    RootRef.child("Shops").child("Orders").child(lastItem.toString()).child("Orders").child(ownerId).updateChildren(UserDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                RootRef.child("Users").child(Email2).child("Basket").removeValue();
                                datalist.clear();
                                PeremennyeActivity.datalistbasket.clear();
                                myadapterbasket.notifyDataSetChanged();
                                totalquan=0;
                                total3.setText(totalquan.toString());
                                totalprice = 0;
                                total6.setText(totalprice.toString());
                            }
                        }
                    });
                    RootRef.child("Users").child(Email2).child("Orders").child(lastItem.toString()).updateChildren(UserDataMap3);
                    Toast.makeText(getActivity(), "Ваш заказ №" + lastItem +" оформлен! ", LENGTH_SHORT).show();
                    RootRef.child("Shops").child("Orders").child(lastItem.toString()).updateChildren(UserDataMap2);
                }*/
               /* AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                BasketFragment basketFragment = new BasketFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,basketFragment).commit();*/
            }
        });

        return v;
    }

   /* public void zakaz(Context context, Integer totalprice2, String adress, HashMap hashMap, String delivery){
        for(int i=0;i<datalist.size();i++){
            //Toast.makeText(getActivity(), "Ваш заказ №" + lastItem +" оформлен---***! ", LENGTH_SHORT).show();
            databasePropertyData =  FirebaseDatabase.getInstance().getReference("Users");
            ownerId = databasePropertyData.push().getKey();
            final Basket_model basketmodel = PeremennyeActivity.datalistbasket.get(i);
           // final Basket_model basketmodel = datalist2.get(i);
            final Productes_model productesmodel =  datalist.get(i);
            RootRef = FirebaseDatabase.getInstance().getReference();
            final HashMap<String, Object> UserDataMap = new HashMap<>();
            UserDataMap.put("Shops", basketmodel.getShops());
            UserDataMap.put("ShopsName", basketmodel.getShopsName());
            UserDataMap.put("IdProductOrders", basketmodel.getIdProductBasket());
            UserDataMap.put("Quantity", basketmodel.getQuantity());
            UserDataMap.put("NameProduct", productesmodel.getName());

            final HashMap<String, Object> UserDataMap2 = new HashMap<>();
            UserDataMap2.put("TotalPrice", totalprice2);
            UserDataMap2.put("User", user.getEmail());
            UserDataMap2.put("Status", "Ожидает обработки.");
            UserDataMap2.put("AdressUser", adress.toString());
            UserDataMap2.put("Delivery", delivery);
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            final HashMap<String, Object> UserDataMap3 = new HashMap<>();
            UserDataMap3.put("Ordernumber",lastItem);
            UserDataMap3.put("TotalPrice",totalprice2);
            UserDataMap3.put("Date", formatForDateNow.format(date));
            UserDataMap3.put("Delivery", delivery);

            final HashMap<String, Object> UserDataMap4 = new HashMap<>();
            UserDataMap4.put("AdressUser", adress.toString());

            RootRef.child("Shops").child("Orders").child(lastItem.toString())
                    .child("Orders").child(ownerId).updateChildren(UserDataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                RootRef.child("Users").child(Email2).child("Basket").removeValue();
                                datalist.clear();
                               // datalist2.clear();
                                PeremennyeActivity.datalistbasket.clear();
                                myadapterbasket.notifyDataSetChanged();
                                totalquan=0;
                                total3.setText(totalquan.toString());
                                totalprice = 0;

                                total6.setText(totalprice.toString());
                            }
                        }
                    });
            RootRef.child("Users").child(Email2).child("Orders").child(lastItem.toString())
                    .updateChildren(UserDataMap3);

            RootRef.child("Users").child(Email2).updateChildren(UserDataMap4);
            Toast.makeText(context, "Ваш заказ №" + lastItem +" оформлен! ",
                    LENGTH_SHORT).show();

            RootRef.child("Shops").child("Orders").child(lastItem.toString())
                    .updateChildren(UserDataMap2);
            RootRef.child("Shops").child("Orders").child(lastItem.toString())
                    .updateChildren(hashMap);
            RootRef.child("Users").child(Email2).child("Orders").child(lastItem.toString())
                    .updateChildren(hashMap);
        }
    }*/
}
