package com.example.mandarinmarket.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.R;

import java.util.ArrayList;

public class Myadapter_Orders extends RecyclerView.Adapter<Myadapter_Orders.myviewholder>{
    ArrayList<Orders_model> datalist;
    Context context;

    public Myadapter_Orders(Context context, ArrayList<Orders_model> datalist){
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_orders, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        final Orders_model orders_model =  datalist.get(position);

        holder.t1.setText(orders_model.getShopsName());
        holder.t2.setText(orders_model.getNameProduct());
        holder.t3.setText(orders_model.getQuantity().toString());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView t1,t2,t3;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.item_orders_Shops);
            t2 = itemView.findViewById(R.id.item_orders_Prod);
            t3 = itemView.findViewById(R.id.item_orders_Quan);
        }
    }
}
