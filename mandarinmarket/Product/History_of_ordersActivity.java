package com.example.mandarinmarket.Product;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.HistoryOfOrders_model;
import com.example.mandarinmarket.Model.Myadapter_HistoryOfOrders;
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

public class History_of_ordersActivity extends AppCompatActivity {
    TextView History_of_orders_button;
    RecyclerView recyclerView;
    ArrayList<HistoryOfOrders_model> datalist;
    FirebaseDatabase db;

    DatabaseReference root;

    Myadapter_HistoryOfOrders myadapterHistory_ofOrders;
    String Email2, status ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_orders);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
        History_of_orders_button =(TextView) findViewById(R.id.History_of_orders_button);
        History_of_orders_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        recyclerView = (RecyclerView) findViewById(R.id.rs_list_History_of_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        //db2 = FirebaseDatabase.getInstance();
        root = db.getReference().child("Users").child(Email2).child("Orders");
        datalist=new ArrayList<>();
        myadapterHistory_ofOrders = new Myadapter_HistoryOfOrders(this, datalist);
        recyclerView.setAdapter(myadapterHistory_ofOrders);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final HistoryOfOrders_model history_ofOrders_model = dataSnapshot.getValue(HistoryOfOrders_model.class);
                    datalist.add(history_ofOrders_model);

                   /* root2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                if()
                                PeremennyeActivity.Status.add(history_ofOrders_model);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });*/
                }
                myadapterHistory_ofOrders.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}