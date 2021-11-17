package com.example.mandarinmarket.Product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.example.mandarinmarket.UI.Users.AboutCompany_Activity;
import com.example.mandarinmarket.UI.Users.FAQActivity;
import com.example.mandarinmarket.UI.Users.MainActivity;
import com.example.mandarinmarket.UI.Users.ProfileSettingsActivity;
import com.example.mandarinmarket.UI.Users.RegicterActivity;
import com.example.mandarinmarket.UI.Users.SupportActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import io.paperdb.Paper;

public class PersonFragment extends Fragment {
    private TextView nameUser,txt_user_adress, txt_user_name,txt_user_email, txt_user_phone, history_of_orders,fragment_person_exit,about_company_person;
    DatabaseReference rootRef , demoRef,demoRef2;
    String date;
    Button Support,FAQ_button_fragment_person;
    ImageView ic_settings;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person,container,false);
        PeremennyeActivity.mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();

        txt_user_name =(TextView) v.findViewById(R.id.txt_user_name);
        txt_user_email =(TextView) v.findViewById(R.id.txt_user_email);
        txt_user_phone =(TextView) v.findViewById(R.id.txt_user_phone);
        txt_user_adress =(TextView) v.findViewById(R.id.txt_user_adress);

        history_of_orders =(TextView) v.findViewById(R.id.history_of_orders);
        about_company_person =(TextView) v.findViewById(R.id.about_company_person);
        fragment_person_exit =(TextView) v.findViewById(R.id.fragment_person_exit);
        Support =(Button) v.findViewById(R.id.Support);
        FAQ_button_fragment_person =(Button) v.findViewById(R.id.FAQ_button_fragment_person);
        ic_settings =(ImageView) v.findViewById(R.id.ic_settings);
        ic_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfileSettings = new Intent(getActivity(), ProfileSettingsActivity.class);
                startActivity(ProfileSettings);
            }
        });

        txt_user_email.setText(user.getEmail().toString());


        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        demoRef = rootRef.child("Users");
        final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        demoRef2 = demoRef.child(Email2);

        demoRef2.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_user_name.setText(dataSnapshot.getValue(String.class));
                //date = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        demoRef2.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_user_phone.setText(dataSnapshot.getValue(String.class));
                //date = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        demoRef2.child("AdressUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_user_adress.setText(dataSnapshot.getValue(String.class));
               // adress = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        history_of_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(history_of_orders);
                Intent backintent = new Intent(getActivity(), History_of_ordersActivity.class);
                startActivity(backintent);//finish();
            }
        });
        about_company_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(about_company_person);
                Intent AboutCompany_Activity = new Intent(getActivity(), AboutCompany_Activity.class);
                startActivity(AboutCompany_Activity);//finish();
            }
        });
        Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(Support);
                Intent SupportActivity = new Intent(getActivity(), SupportActivity.class);
                startActivity(SupportActivity);//finish();
            }
        });
        FAQ_button_fragment_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeremennyeActivity.addClickEffect(FAQ_button_fragment_person);
                Intent FAQActivity = new Intent(getActivity(), FAQActivity.class);
                startActivity(FAQActivity);//finish();
            }
        });

      /*  fragment_person_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                PeremennyeActivity.mAuth.signOut();
                if( PeremennyeActivity.mAuthListener != null)  PeremennyeActivity.mAuth.removeAuthStateListener( PeremennyeActivity.mAuthListener);
                Intent logoutIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(logoutIntent);getActivity().finish();
            }
        });*/

fragment_person_exit.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
PeremennyeActivity.addClickEffect(fragment_person_exit);
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
builder//.setTitle("Диалог")
.setMessage("Вы действительно хотите выйти из учетной записи?")
.setPositiveButton("Да", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
//Toast.makeText(getActivity(),"Нажата кнопка 'OK'",Toast.LENGTH_SHORT).show();
Paper.book().destroy();
PeremennyeActivity.mAuth.signOut();
if( PeremennyeActivity.mAuthListener != null)  PeremennyeActivity.mAuth.removeAuthStateListener( PeremennyeActivity.mAuthListener);
Intent logoutIntent = new Intent(getActivity(), MainActivity.class);
startActivity(logoutIntent);getActivity().finish();
}
})
.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
//Toast.makeText(getActivity(),"Нажата кнопка 'Отмена'",Toast.LENGTH_SHORT).show();
}
});
builder.create().show();
}
});

        return v;
    }
}
