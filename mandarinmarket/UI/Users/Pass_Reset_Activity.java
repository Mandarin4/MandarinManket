package com.example.mandarinmarket.UI.Users;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pass_Reset_Activity extends AppCompatActivity {

    private Button  btn_back_reset_pass_b, reset_pass;

    private EditText reset_pass_email;

    private FirebaseAuth mAuth;
    RelativeLayout relativeLayout;
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset_);

        relativeLayout = findViewById(R.id.relativ);
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        reset_pass_email =  findViewById(R.id.btn_reset_pass_email_input);
        reset_pass =  findViewById(R.id.btn_reset_pass);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        btn_back_reset_pass_b = findViewById(R.id.btn_back_reset_pass);
        btn_back_reset_pass_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btn_back_reset_pass_intent = new Intent(Pass_Reset_Activity.this, Sign_In_Activity.class);
                startActivity(btn_back_reset_pass_intent);finish();
            }
        });

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = reset_pass_email.getText().toString();
                if (TextUtils.isEmpty(userEmail)){
                    Toast.makeText(Pass_Reset_Activity.this, "Пожалуйста укажите ваш EMAIL", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Pass_Reset_Activity.this, "Пожалуйста проверьте свой Email аккаунт...", Toast.LENGTH_LONG).show();
                                //Toast.makeText(Pass_Reset_Activity.this, "2 " + userEmail, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Pass_Reset_Activity.this, Sign_In_Activity.class));finish();
                            }else{
                                String message = task.getException().getMessage();
                                Toast.makeText(Pass_Reset_Activity.this, " 3 " + userEmail, Toast.LENGTH_LONG).show();
                              //  Toast.makeText(Pass_Reset_Activity.this, "Error Occured: " + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }



}

