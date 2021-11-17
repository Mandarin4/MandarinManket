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

public class Myadapter_MoreDetails extends RecyclerView.Adapter<Myadapter_MoreDetails.myviewholder>{
    ArrayList<MoreDetails_model> datalist;
    Context context;
    public Myadapter_MoreDetails(Context context, ArrayList<MoreDetails_model> datalist){
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_description, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        final MoreDetails_model moreDetails_model =  datalist.get(position);
        holder.t1.setText(moreDetails_model.getNameT());
        holder.t2.setText(moreDetails_model.getNameD());

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView t1,t2;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.item_text_decription);
            t2 = itemView.findViewById(R.id.item_text_decription2);
        }
    }
}
