package com.example.mandarinmarket.UI.Users;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.Model.FAQ_model;
import com.example.mandarinmarket.Model.Myadapter_faq;
import com.example.mandarinmarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {
    TextView FAQ_button_f;
    RecyclerView recyclerView ;
    ArrayList<FAQ_model> datalist;
    FirebaseDatabase db;
    DatabaseReference root;
    Myadapter_faq myadapter_faq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FAQ_button_f =(TextView) findViewById(R.id.FAQ_button_f);

        FAQ_button_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rs_list_FAQ);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance();
        root = db.getReference().child("Shops").child("FAQ");
        datalist=new ArrayList<>();
        myadapter_faq = new Myadapter_faq(this, datalist);
        recyclerView.setAdapter(myadapter_faq);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FAQ_model faq_model = dataSnapshot.getValue(FAQ_model.class);
                    datalist.add(faq_model);
                }
                myadapter_faq.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


}