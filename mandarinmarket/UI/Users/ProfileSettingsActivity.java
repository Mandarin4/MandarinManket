package com.example.mandarinmarket.UI.Users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.mandarinmarket.PeremennyeActivity.mAuth;

public class ProfileSettingsActivity extends AppCompatActivity {
    TextView ProfileSettings_button,delete_akaunt,txt_adress_ProfileSettings, txt_user_name_ProfileSettings, txt_user_email_ProfileSettings, txt_user_phone_ProfileSettings;
    Button ProfileSettings_button_pass;
    ImageView ic_create1, ic_create2, ic_create3, ic_create4;
    DatabaseReference rootRef , demoRef,demoRef2;
    String username, phone, adress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txt_user_email_ProfileSettings =(TextView) findViewById(R.id.txt_user_email_ProfileSettings);
        txt_user_name_ProfileSettings =(TextView) findViewById(R.id.txt_user_name_ProfileSettings);
        txt_user_phone_ProfileSettings =(TextView) findViewById(R.id.txt_user_phone_ProfileSettings);
        txt_adress_ProfileSettings =(TextView) findViewById(R.id.txt_adress_ProfileSettings);

        delete_akaunt =(TextView) findViewById(R.id.delete_akaunt);

        ic_create1 =(ImageView) findViewById(R.id.ic_create1);
        ic_create2 =(ImageView) findViewById(R.id.ic_create2);
        ic_create3 =(ImageView) findViewById(R.id.ic_create3);
        ic_create4 =(ImageView) findViewById(R.id.ic_create4);



        final FirebaseUser user = mAuth.getCurrentUser();
        ProfileSettings_button =(TextView) findViewById(R.id.ProfileSettings_button);
        ProfileSettings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_user_email_ProfileSettings.setText(user.getEmail().toString());


        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("Users");
        final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
        demoRef2 = demoRef.child(Email2);

        demoRef2.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_user_name_ProfileSettings.setText(dataSnapshot.getValue(String.class));
                //date = dataSnapshot.getValue(String.class);
                username = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        demoRef2.child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_user_phone_ProfileSettings.setText(dataSnapshot.getValue(String.class));
                //date = dataSnapshot.getValue(String.class);
                phone = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        demoRef2.child("AdressUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_adress_ProfileSettings.setText(dataSnapshot.getValue(String.class));
                adress = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ProfileSettings_button_pass =(Button) findViewById(R.id.ProfileSettings_button_pass);
        ProfileSettings_button_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ProfileSettingsActivity.this, "Пожалуйста проверьте свой Email аккаунт...", Toast.LENGTH_LONG).show();
                        //Toast.makeText(Pass_Reset_Activity.this, "2 " + userEmail, Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(Pass_Reset_Activity.this, Sign_In_Activity.class));finish();
                    }else{
                        String message = task.getException().getMessage();
                        Toast.makeText(ProfileSettingsActivity.this,message, Toast.LENGTH_LONG).show();
                        //  Toast.makeText(Pass_Reset_Activity.this, "Error Occured: " + message, Toast.LENGTH_LONG).show();
                    }
                    }
                });
            }
        });

        ic_create1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfileSettingsActivity.this,"OK", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettingsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_username, null);

                final EditText etUsername = alertLayout.findViewById(R.id.tiet_username);
                etUsername.setText(username);
                builder
                        .setView(alertLayout)
                        //.setTitle("Диалог")
                        //.setMessage("Вы действительно хотите выйти из учетной записи?")
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String user = etUsername.getText().toString();
                                demoRef2.child("username").setValue(user);
                                txt_user_name_ProfileSettings.setText(user.toString());
                                PeremennyeActivity.username = user;
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });
        ic_create4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfileSettingsActivity.this,"OK", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettingsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_adress, null);

                final EditText etAdress = alertLayout.findViewById(R.id.tiet_adress);
                etAdress.setText(adress);
                builder
                        .setView(alertLayout)
                        //.setTitle("Диалог")
                        //.setMessage("Вы действительно хотите выйти из учетной записи?")
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String user = etAdress.getText().toString();
                                demoRef2.child("AdressUser").setValue(user);
                                txt_adress_ProfileSettings.setText(user.toString());
                               // PeremennyeActivity.username = user;
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });
        ic_create2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfileSettingsActivity.this,"OK", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettingsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_email, null);

                final EditText etEmail = alertLayout.findViewById(R.id.tiet_email);
                etEmail.setText(user.getEmail());
                builder
                        .setView(alertLayout)
                        //.setTitle("Диалог")
                        //.setMessage("Вы действительно хотите выйти из учетной записи?")
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String email = etEmail.getText().toString();
                                if (!PeremennyeActivity.validateEmailAddress(email)) {
                                    Toast.makeText(ProfileSettingsActivity.this, "Введите правильный email.", Toast.LENGTH_SHORT).show();
                                }else{
                                    //demoRef2.child("phone").setValue(phone);
                                    user.updateEmail(email);
                                    txt_user_email_ProfileSettings.setText(email.toString());
                                    Toast.makeText(ProfileSettingsActivity.this, "Ваш email изменен.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });
        ic_create3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfileSettingsActivity.this,"OK", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettingsActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_phone, null);

                final EditText etPhone = alertLayout.findViewById(R.id.tiet_phone);
                etPhone.setText(phone);
                builder
                        .setView(alertLayout)
                        //.setTitle("Диалог")
                        //.setMessage("Вы действительно хотите выйти из учетной записи?")
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String phone = etPhone.getText().toString();
                                if (TextUtils.isEmpty(phone)){
                                    Toast.makeText(ProfileSettingsActivity.this, "Введите телефон.", Toast.LENGTH_SHORT).show();
                                }else if (RegicterActivity.validatePhone(phone)){
                                    Toast.makeText(ProfileSettingsActivity.this, "Телефон введен не корректно.", Toast.LENGTH_SHORT).show();
                                }else {
                                    demoRef2.child("phone").setValue(phone);
                                    txt_user_phone_ProfileSettings.setText(phone.toString());
                                }
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });

        delete_akaunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileSettingsActivity.this);
                builder.setTitle("Удалить учетную запись?")
                        .setMessage("При удалении учетной записи все Ваши данные будут удалены без возможности востановления.")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                user.delete();
                                demoRef2.removeValue();

                                //Toast.makeText(getActivity(),"Нажата кнопка 'OK'",Toast.LENGTH_SHORT).show();
                                Paper.book().destroy();
                                PeremennyeActivity.mAuth.signOut();
                                if( PeremennyeActivity.mAuthListener != null)  PeremennyeActivity.mAuth.removeAuthStateListener( PeremennyeActivity.mAuthListener);
                                Intent logoutIntent = new Intent(ProfileSettingsActivity.this, MainActivity.class);
                                startActivity(logoutIntent);
                                Toast.makeText(ProfileSettingsActivity.this,"Ваш аккаунт удален!",Toast.LENGTH_SHORT).show();
                                //HomeActivity..finish();
                                ProfileSettingsActivity.this.finish();
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
    }
}