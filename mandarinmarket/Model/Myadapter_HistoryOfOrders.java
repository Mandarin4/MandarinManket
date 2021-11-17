package com.example.mandarinmarket.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.Product.OrdersActivity;
import com.example.mandarinmarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Myadapter_HistoryOfOrders extends RecyclerView.Adapter<Myadapter_HistoryOfOrders.myviewholder>{
    ArrayList<HistoryOfOrders_model> datalist;
    Context context;
    String status="asdaf";
    DatabaseReference root2;
    FirebaseDatabase db2;
    public Myadapter_HistoryOfOrders(Context context, ArrayList<HistoryOfOrders_model> datalist){
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_history_of_orders, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {

        final HistoryOfOrders_model history_ofOrders_model =  datalist.get(position);
        db2 = FirebaseDatabase.getInstance();
        root2 = db2.getReference().child("Shops").child("Orders");
        //PeremennyeActivity.Status = query.toString();
        //.orderByChild("Status").equalTo("Ожидает обработки.");
        root2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                for(DataSnapshot dataSnapshot : data.getChildren()) {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        if (dataSnapshot2.getKey().equals("Status") &&  dataSnapshot.getKey().equals(history_ofOrders_model.getOrdernumber().toString())) {
                            //Toast.makeText(context, dataSnapshot.getKey().toString(), Toast.LENGTH_LONG).show();
                            status= dataSnapshot2.getValue().toString();
                            holder.t5.setText(status);
                           // Toast.makeText(context, status, Toast.LENGTH_LONG).show();
                            //Toast.makeText(this, "В пароле должны содеражаться латиницу верхнего регистра.", Toast.LENGTH_SHORT).show();

                            if(status.equals("Ожидает обработки."))
                                holder.t5.setTextColor(Color.parseColor("#fad220"));
                            else if(status.equals("Выполняется."))
                                holder.t5.setTextColor(Color.BLUE);
                            else if(status.equals("Выполнено."))
                                holder.t5.setTextColor(Color.GREEN);
                            else holder.t5.setTextColor(Color.RED);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Toast.makeText(context, status, Toast.LENGTH_LONG).show();

        holder.t1.setText(history_ofOrders_model.getOrdernumber().toString());
        holder.t2.setText(history_ofOrders_model.getTotalPrice().toString());
        holder.t3.setText(history_ofOrders_model.getDate());
        //holder.t5.setText(status);

        holder.t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(holder.t4);
                PeremennyeActivity.OrdersInt = history_ofOrders_model.getOrdernumber();
                Intent intent = new Intent(context, OrdersActivity.class);
                context.startActivity(intent);//finish();

            }
        });

       /* if(history_ofOrders_model.getStatus().equals("Ожидает обработки."))
            holder.t5.setTextColor(Color.YELLOW);
        else if(history_ofOrders_model.getStatus().equals("Выполняется."))
            holder.t5.setTextColor(Color.BLUE);
        else if(history_ofOrders_model.getStatus().equals("Выполнено."))
            holder.t5.setTextColor(Color.GREEN);
        else holder.t5.setTextColor(Color.RED);*/

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        LinearLayout t4;
        TextView t1,t2,t3, t5;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.History_of_orders_text_price1);
            t2 = itemView.findViewById(R.id.History_of_orders_text_price2);
            t3 = itemView.findViewById(R.id.History_of_orders_text_price3);
            t4 = itemView.findViewById(R.id.history_of_orders_liner);
            t5 = itemView.findViewById(R.id.History_of_orders_text_status2);

        }
    }
}
