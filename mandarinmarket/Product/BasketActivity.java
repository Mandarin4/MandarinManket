package com.example.mandarinmarket.Product;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinmarket.Model.Basket_model;
import com.example.mandarinmarket.Model.Productes_model;
import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.example.mandarinmarket.UI.Users.RegicterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.mandarinmarket.PeremennyeActivity.Email2;

public class BasketActivity extends AppCompatActivity {
TextView  close_basket_button, total3_basket_act,total6_basket_act,activity_basket_button;
double totalprice2;
double totalprice;
double totalprice4;
LinearLayout liner_full, liner_full1,liner_full2,liner_full3;
EditText edit_shop_basket_act;
Boolean flag = false;


   // private TextView
static FirebaseUser user;
    DatabaseReference rootRef , demoRef,demoRef2;
    RadioButton RadioButton;
    RadioButton RadioButton2;
    String delivery;
    String delivery2;
    String stringdouble;
    String stringdouble2;

    DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user = PeremennyeActivity.mAuth.getCurrentUser();
       // Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        //String[] countries = { "Бразилия", "Аргентина", "Колумбия", "Чили", "Уругвай"};
        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("Users");
        final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        demoRef2 = demoRef.child(Email2);



        liner_full =(LinearLayout) findViewById(R.id.liner_full);
        liner_full1 =(LinearLayout) findViewById(R.id.liner_full1);
        liner_full2 =(LinearLayout) findViewById(R.id.liner_full2);
        liner_full3 =(LinearLayout) findViewById(R.id.liner_full3);
        edit_shop_basket_act =(EditText) findViewById(R.id.edit_shop_basket_act);

        liner_full.setVisibility(View.VISIBLE);
        for (int i=0; i < PeremennyeActivity.arrayListShop.size();i++){
            if( PeremennyeActivity.arrayListShop.get(i).equals("Магнит")) liner_full1.setVisibility(View.VISIBLE);
            if( PeremennyeActivity.arrayListShop.get(i).equals("Перекресток")) liner_full2.setVisibility(View.VISIBLE);
            if( PeremennyeActivity.arrayListShop.get(i).equals("Пятерочка")) liner_full3.setVisibility(View.VISIBLE);
            //else sho = false;
        }


        demoRef2.child("AdressUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_shop_basket_act.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        activity_basket_button =(TextView) findViewById(R.id.activity_basket_button);



        close_basket_button =(TextView) findViewById(R.id.close_basket_act_button);
        close_basket_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //textBasket_2 = (TextView) findViewById(R.id.textBasket_2);
        total3_basket_act = (TextView) findViewById(R.id.total3_basket_act);
        total3_basket_act.setText(PeremennyeActivity.totalquan.toString());

        total6_basket_act = (TextView) findViewById(R.id.total6_basket_act);
        total6_basket_act.setText(PeremennyeActivity.totalprice.toString());

        totalprice2 = PeremennyeActivity.totalprice;

        RadioButton = findViewById(R.id.radio_red);
        delivery2 = RadioButton.getText().toString();
        delivery = RadioButton.getText().toString();
        RadioButton2 = findViewById(R.id.radio_blue);
        totalprice4=0;
        totalprice4 = PeremennyeActivity.totalprice*0.1;
        stringdouble2= Double.toString(totalprice4);
        RadioButton2.setText("Доставка - " + stringdouble2 + "руб.");
        RadioGroup radioGroup = findViewById(R.id.radioGroup_basket_act);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radio_red:
                        RadioButton = findViewById(R.id.radio_red);
                        delivery = delivery2;
                        RadioButton.setText(delivery2);
                        totalprice2 = PeremennyeActivity.totalprice;
                        stringdouble= Double.toString(totalprice2);
                        total6_basket_act.setText( stringdouble);
                       // total6_basket_act.setText(PeremennyeActivity.totalprice.toString());
                        liner_full.setVisibility(View.VISIBLE);
                        liner_full1.setVisibility(View.GONE);
                        liner_full2.setVisibility(View.GONE);
                        liner_full3.setVisibility(View.GONE);
                        edit_shop_basket_act.setVisibility(View.GONE);
                        for (int i=0; i < PeremennyeActivity.arrayListShop.size();i++){
                            if( PeremennyeActivity.arrayListShop.get(i).equals("Магнит")) liner_full1.setVisibility(View.VISIBLE);
                            if( PeremennyeActivity.arrayListShop.get(i).equals("Перекресток")) liner_full2.setVisibility(View.VISIBLE);
                            if( PeremennyeActivity.arrayListShop.get(i).equals("Пятерочка")) liner_full3.setVisibility(View.VISIBLE);
                            //else sho = false;
                        }
                        flag = false;
                        break;
                    case R.id.radio_blue:
                        RadioButton = findViewById(R.id.radio_blue);
                        totalprice2 = PeremennyeActivity.totalprice + PeremennyeActivity.totalprice*0.1;
                        stringdouble= Double.toString(totalprice2);
                        total6_basket_act.setText( stringdouble);
                        delivery = "Доставка - " + stringdouble2 + "руб.";
                        liner_full.setVisibility(View.GONE);
                        edit_shop_basket_act.setVisibility(View.VISIBLE);
                        liner_full1.setVisibility(View.GONE);
                        liner_full2.setVisibility(View.GONE);
                        liner_full3.setVisibility(View.GONE);
                        flag = true;
                        break;

                    default:
                        break;
                }
            }
        });

        String[] adressMagnit = {"Ростов-на-Дону, Соборный переулок, 24"
                ,"Ростов-на-Дону, переулок Семашко, 48е","Ростов-на-Дону, Ворошиловский проспект, 54"
                ,"Ростов-на-Дону, Станиславского, 52","Ростов-на-Дону, Максима Горького, 121"
                ,"Ростов-на-Дону, Большая Садовая, 109а","Ростов-на-Дону, Ворошиловский проспект, 12"};
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adressMagnit);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        String[] adressPerek = {"Ростов-на-Дону, Буденновский пр-кт, д. 49"
                ,"Ростов-на-Дону, ул. Зорге, д. 33","Ростов-на-Дону, пр. Космонавтов, д. 2/2"
                ,"Ростов-на-Дону, пр. Коммунистический, д. 32","Ростов-на-Дону, ул. Красноармейская, д. 105"
                ,"Ростов-на-Дону, пр. Стачки, д. 25","Ростов-на-Дону, ул. Текучёва, д. 139 а"};
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adressPerek);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner2.setAdapter(adapter2);

        String[] adressPyater = {"Ростов-на-Дону, Текучёва, 246"
                ,"Ростов-на-Дону, Оганова, 10","Ростов-на-Дону, проспект Сельмаш, 98"
                ,"Ростов-на-Дону, Пушкинская, 231","Ростов-на-Дону, Октябрьская, 147"
                ,"Ростов-на-Дону, Пушкинская, 199","Ростов-на-Дону, Халтуринский пер, 165"};
        final Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adressPyater);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner3.setAdapter(adapter3);



        activity_basket_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, Object> WUserDataMap = new HashMap<>();
                if (!flag){
                    for (int i=0; i < PeremennyeActivity.arrayListShop.size();i++){
                        if( PeremennyeActivity.arrayListShop.get(i).equals("Магнит")) WUserDataMap.put("Magnit", spinner.getSelectedItem().toString());;
                        if( PeremennyeActivity.arrayListShop.get(i).equals("Перекресток")) WUserDataMap.put("Perekryostok", spinner2.getSelectedItem().toString());;
                        if( PeremennyeActivity.arrayListShop.get(i).equals("Пятерочка")) WUserDataMap.put("Pyatyorochka", spinner3.getSelectedItem().toString());
                    }


                    ofor(WUserDataMap);



                    //BasketFragment.zakaz(BasketActivity.this, totalprice2, edit_shop_basket_act.getText().toString(),UserDataMap,delivery);
                    //PeremennyeActivity.OrdersInt = BasketFragment.lastItem;
                    Intent intent = new Intent(BasketActivity.this, OrdersActivity.class);
                    startActivity(intent);finish();
                }else if (flag && edit_shop_basket_act.getText().length() != 0){
                    //BasketFragment.zakaz(BasketActivity.this, totalprice2, edit_shop_basket_act.getText().toString(),UserDataMap,delivery);
                   // PeremennyeActivity.OrdersInt = BasketFragment.lastItem;
                    ofor(WUserDataMap);
                    Intent intent = new Intent(BasketActivity.this, OrdersActivity.class);
                    startActivity(intent);finish();

                }
                else if (flag && edit_shop_basket_act.getText().length() == 0)Toast.makeText(BasketActivity.this, "Введите адрес доставки!" , LENGTH_SHORT).show();;
            }
        });

    }
    private void ofor( HashMap qwe){
        for(int i=0;i<PeremennyeActivity.datalist.size();i++){
            //Toast.makeText(getActivity(), "Ваш заказ №" + lastItem +" оформлен---***! ", LENGTH_SHORT).show();
            PeremennyeActivity.databasePropertyData =  FirebaseDatabase.getInstance().getReference("Users");
            PeremennyeActivity.ownerId = PeremennyeActivity.databasePropertyData.push().getKey();
            final Basket_model basketmodel = PeremennyeActivity.datalistbasket.get(i);
            // final Basket_model basketmodel = datalist2.get(i);
            final Productes_model productesmodel =  PeremennyeActivity.datalist.get(i);
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
            UserDataMap2.put("AdressUser", edit_shop_basket_act.getText().toString());
            UserDataMap2.put("Delivery", delivery);
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            final HashMap<String, Object> UserDataMap3 = new HashMap<>();
            UserDataMap3.put("Ordernumber",PeremennyeActivity.OrdersInt);
            UserDataMap3.put("TotalPrice",totalprice2);
            UserDataMap3.put("Date", formatForDateNow.format(date));
            UserDataMap3.put("Delivery", delivery);

            final HashMap<String, Object> UserDataMap4 = new HashMap<>();
            UserDataMap4.put("AdressUser", edit_shop_basket_act.getText().toString());

            RootRef.child("Shops").child("Orders").child(PeremennyeActivity.OrdersInt.toString())
                    .child("Orders").child(PeremennyeActivity.ownerId).updateChildren(UserDataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                RootRef.child("Users").child(Email2).child("Basket").removeValue();
                                PeremennyeActivity.datalist.clear();
                                // datalist2.clear();
                                PeremennyeActivity.datalistbasket.clear();
                                //BasketFragment.myadapterbasket.notifyDataSetChanged();
                                BasketFragment.totalquan=0;
                                BasketFragment.total3.setText(BasketFragment.totalquan.toString());
                                BasketFragment.totalprice = 0;
                              //  BasketFragment.fragment_basket_button.setClickable(false);
                                BasketFragment.total6.setText(BasketFragment.totalprice.toString());
                            }
                        }
                    });
            RootRef.child("Users").child(Email2).child("Orders").child(PeremennyeActivity.OrdersInt.toString())
                    .updateChildren(UserDataMap3);

            RootRef.child("Users").child(Email2).updateChildren(UserDataMap4);
            Toast.makeText(BasketActivity.this, "Ваш заказ №" + PeremennyeActivity.OrdersInt +" оформлен! ",
                    LENGTH_SHORT).show();

            RootRef.child("Shops").child("Orders").child(PeremennyeActivity.OrdersInt.toString())
                    .updateChildren(UserDataMap2);
            RootRef.child("Shops").child("Orders").child(PeremennyeActivity.OrdersInt.toString())
                    .updateChildren(qwe);
            RootRef.child("Users").child(Email2).child("Orders").child(PeremennyeActivity.OrdersInt.toString())
                    .updateChildren(qwe);
        }
    }

}