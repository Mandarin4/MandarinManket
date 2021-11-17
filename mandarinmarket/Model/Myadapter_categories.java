package com.example.mandarinmarket.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.Product.ProductesFragment;
import com.example.mandarinmarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Myadapter_categories extends RecyclerView.Adapter<Myadapter_categories.myviewholder>{
    ArrayList<Categories_model> datalist;
    Context context;
    public Myadapter_categories(Context context, ArrayList<Categories_model> datalist){
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {

        //final Animation animScale = AnimationUtils.loadAnimation(context, R.anim.scale);
        final Categories_model categories =  datalist.get(position);
        holder.t1.setText(categories.getName());
        holder.t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeremennyeActivity.addClickEffect(holder.t3);
                PeremennyeActivity.Categoriya = categories.getNameC();
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                ProductesFragment productesFragment = new ProductesFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,productesFragment)
                        .addToBackStack( "tag" ).commit();
            }
        });
        Picasso.get().load(categories.getSrcImage()).into(holder.t2);
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView t1;
        ImageView t2;
        LinearLayout t3;
        public  myviewholder(@NonNull View itemView){
            super(itemView);
            t1 = itemView.findViewById(R.id.itemtextcategories);
            t2 = itemView.findViewById(R.id.image_view_categories);
            t3 = itemView.findViewById(R.id.liner_categor);
        }
    }
}
