package com.example.mandarinmarket.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.Product.CategoriesFragment;
import com.example.mandarinmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Myadapter_shops extends RecyclerView.Adapter<Myadapter_shops.myviewholder>{
    ArrayList<Shops_model> datalist;
    Context context;
    public Myadapter_shops(Context context, ArrayList<Shops_model> datalist){
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {
        final Shops_model shopsmodel =  datalist.get(position);
        Picasso.get().load(shopsmodel.getSrc()).into(holder.t1);
        holder.t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PeremennyeActivity.addClickEffect(holder.t1);
                PeremennyeActivity.Shops = shopsmodel.getName();
                PeremennyeActivity.ShopsName = shopsmodel.getNameShop();
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,categoriesFragment).addToBackStack( "tag" ).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView t1;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.item_home_image);

        }
    }
}
