package com.example.mandarinmarket.UI.Users;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.Prevalent.Prevalent;
import com.example.mandarinmarket.Product.HomeActivity;
import com.example.mandarinmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Sign_In_Activity extends AppCompatActivity {
    private Button sign_in_btn, btn_back_sign_in_in;
    private EditText user_name, user_pass, user_email;
    private TextView btn_email_inp;
    private ProgressDialog loadbar;
    private String parentDbName = "Users";
    private CheckBox checkBoxRememberMe;
    private TextView  btn_forget_pass;
    DatabaseReference rootRef , demoRef,demoRef2;
    RelativeLayout relativeLayout;
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in_);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        relativeLayout = findViewById(R.id.relativ);
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        PeremennyeActivity.mAuth = FirebaseAuth.getInstance();

        sign_in_btn = findViewById(R.id.btn_sign_in_in);
        user_pass =  findViewById(R.id.btn_pass_input);
        user_email =  findViewById(R.id.btn_email_input);
        loadbar = new ProgressDialog(this);
        btn_forget_pass = findViewById(R.id.forget_pass);
        btn_back_sign_in_in = findViewById(R.id.btn_back_sign_in_in);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkbox_sign_in);
        Paper.init(this);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        btn_back_sign_in_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent(Sign_In_Activity.this, MainActivity.class);
                startActivity(backintent);finish();
            }
        });
        btn_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btn_forget_pass_intent = new Intent(Sign_In_Activity.this, Pass_Reset_Activity.class);
                startActivity(btn_forget_pass_intent);finish();
            }
        });
    }

    private void loginUser() {
        String pass = user_pass.getText().toString();
        String email = user_email.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Введите Email.", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Введите пароль.", Toast.LENGTH_SHORT).show();
        }else {
            loadbar.setTitle("Вход в приложение");
            loadbar.setMessage("Пожалуйста подождите...");
            loadbar.setCanceledOnTouchOutside(false);
            loadbar.show();

            ValidateUser(pass, email);
        }
    }

    private void ValidateUser(final String pass, final String email) {
        PeremennyeActivity.mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (PeremennyeActivity.mAuth.getCurrentUser().isEmailVerified()){
                        loadbar.dismiss();
                        Toast.makeText(Sign_In_Activity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                        if(checkBoxRememberMe.isChecked()){
                            Paper.book().write(Prevalent.UserPhoneKey, email);
                            Paper.book().write(Prevalent.UserPasswordKey, pass);
                        }
                        Intent homeIntent = new Intent(Sign_In_Activity.this, HomeActivity.class);
                        //database reference pointing to root of database
                        final FirebaseUser user = PeremennyeActivity.mAuth.getCurrentUser();
                        rootRef = FirebaseDatabase.getInstance().getReference();
                        //database reference pointing to demo node
                        demoRef = rootRef.child("Users");
                        final String Email2 = RegicterActivity.removeChar(user.getEmail().toString(),'.');
                        demoRef2 = demoRef.child(Email2);

                        demoRef2.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                PeremennyeActivity.username = dataSnapshot.getValue(String.class);
                                //date = dataSnapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        PeremennyeActivity.user = PeremennyeActivity.mAuth.getCurrentUser();
                        startActivity(homeIntent);
                        finish();
                    }else {
                        Toast.makeText(Sign_In_Activity.this, "Пожалуйста подтвердите свою почту!!!", Toast.LENGTH_SHORT).show();
                        PeremennyeActivity.mAuth.getCurrentUser().sendEmailVerification();
                        PeremennyeActivity.mAuth.getInstance().signOut();
                        Intent homeIntent = new Intent(Sign_In_Activity.this, MainActivity.class);
                        startActivity(homeIntent);
                    }
                }else{ loadbar.dismiss();
                    Toast.makeText(Sign_In_Activity.this, "Не правильно указан логин или пароль!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Системная кнопка назад
    @Override
    public void onBackPressed(){
        Intent backintent = new Intent(Sign_In_Activity.this, MainActivity.class);
        startActivity(backintent);finish();
    }
}