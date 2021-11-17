package com.example.mandarinmarket.Product;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.Myadapter_Orders;
import com.example.mandarinmarket.Model.Orders_model;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    TextView orders_button,orders_text_price1,orders_text_status2,orders_text_delivery,orders_text_delivery2,orders_text_delivery3,orders_text_delivery4;
    RecyclerView recyclerView;
    ArrayList<Orders_model> datalist;
    FirebaseDatabase db;
    DatabaseReference root;
    DatabaseReference root2;
    Myadapter_Orders myadapter_Orders;
    String status="asdaf";
    LinearLayout liner_delivery2,liner_delivery3,liner_delivery4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = FirebaseDatabase.getInstance();

        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        orders_button =(TextView) findViewById(R.id.orders_button);
        orders_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orders_text_delivery =(TextView) findViewById(R.id.orders_text_delivery);
        orders_text_delivery2 =(TextView) findViewById(R.id.orders_text_delivery2);
        orders_text_delivery3 =(TextView) findViewById(R.id.orders_text_delivery3);
        orders_text_delivery4 =(TextView) findViewById(R.id.orders_text_delivery4);
        liner_delivery2 =(LinearLayout) findViewById(R.id.liner_delivery2);
        liner_delivery3 =(LinearLayout) findViewById(R.id.liner_delivery3);
        liner_delivery4 =(LinearLayout) findViewById(R.id.liner_delivery4);



        orders_text_price1 =(TextView) findViewById(R.id.orders_text_price1);
        orders_text_price1.setText(PeremennyeActivity.OrdersInt.toString());

        orders_text_status2 =(TextView) findViewById(R.id.orders_text_status2);
        root2 = db.getReference().child("Shops").child("Orders").child(PeremennyeActivity.OrdersInt.toString());
        root2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
               // for(DataSnapshot dataSnapshot : data.getChildren()) {
                    for(DataSnapshot dataSnapshot2 : data.getChildren()) {
                        if (dataSnapshot2.getKey().equals("Delivery")){
                            orders_text_delivery.setText(dataSnapshot2.getValue().toString());
                        }
                        if (dataSnapshot2.getKey().equals("Magnit")){
                            orders_text_delivery2.setText(dataSnapshot2.getValue().toString());
                            liner_delivery2.setVisibility(View.VISIBLE);
                        }//else liner_delivery2.setVisibility(View.GONE);
                        if (dataSnapshot2.getKey().equals("Perekryostok")){
                            orders_text_delivery3.setText(dataSnapshot2.getValue().toString());
                            liner_delivery3.setVisibility(View.VISIBLE);
                        }//else liner_delivery3.setVisibility(View.GONE);
                        if (dataSnapshot2.getKey().equals("Pyatyorochka")){
                            orders_text_delivery4.setText(dataSnapshot2.getValue().toString());
                            liner_delivery4.setVisibility(View.VISIBLE);
                        }//else liner_delivery4.setVisibility(View.GONE);

                        if (dataSnapshot2.getKey().equals("Status")){// &&  dataSnapshot.getKey().equals(PeremennyeActivity.OrdersInt.toString())) {
                            status= dataSnapshot2.getValue().toString();
                            orders_text_status2.setText(status);

                            if(status.equals("Ожидает обработки."))
                                orders_text_status2.setTextColor(Color.parseColor("#fad220"));
                            else if(status.equals("Выполняется."))
                                orders_text_status2.setTextColor(Color.BLUE);
                            else if(status.equals("Выполнено."))
                                orders_text_status2.setTextColor(Color.GREEN);
                            else orders_text_status2.setTextColor(Color.RED);
                        }

                   // }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        recyclerView = (RecyclerView) findViewById(R.id.rs_list_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        root = db.getReference().child("Shops").child("Orders").child(String.valueOf(PeremennyeActivity.OrdersInt)).child("Orders");
        datalist=new ArrayList<>();
        myadapter_Orders = new Myadapter_Orders(this, datalist);
        recyclerView.setAdapter(myadapter_Orders);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Orders_model orders_model = dataSnapshot.getValue(Orders_model.class);
                    datalist.add(orders_model);
                }
                myadapter_Orders.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}