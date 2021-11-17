package com.example.mandarinmarket.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mandarinmarket.R;

import java.util.ArrayList;

public class Myadapter_ListView extends BaseAdapter {
    ArrayList<MoreDetails_model> list;
    Context context;

    public Myadapter_ListView(Context context, ArrayList<MoreDetails_model> list){
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* View view = convertView;
        MoreDetails_model moreDetails_model = list.get(position);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_description, parent);
        }*/

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_description,null);
        MoreDetails_model moreDetails_model = list.get(position);
        TextView t1 = convertView.findViewById(R.id.item_text_decription);
        TextView t2 = convertView.findViewById(R.id.item_text_decription2);
        t1.setText(moreDetails_model.getNameT());
        t2.setText(moreDetails_model.getNameD());



        return convertView;
    }

}
