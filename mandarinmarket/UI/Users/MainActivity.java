package com.example.mandarinmarket.UI.Users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private long backPressedtimer;
    private Toast backToast;

    private Button btn_sign_in, btn_register;
    private ProgressDialog loadbar;
    DatabaseReference rootRef , demoRef,demoRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        PeremennyeActivity.mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        btn_register = (Button) findViewById(R.id.btn_register);
        loadbar = new ProgressDialog(this);

        Paper.init(this);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_in_intent = new Intent(MainActivity.this, Sign_In_Activity.class);
                startActivity(sign_in_intent);finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btn_register_intent = new Intent(MainActivity.this, RegicterActivity.class);
                startActivity(btn_register_intent);finish();
            }
        });

        String UserEmailKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserEmailKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey)){
                ValidateUser(UserEmailKey, UserPasswordKey);

                loadbar.setTitle("Вход в приложение");
                loadbar.setMessage("Пожалуйста подождите...");
                loadbar.setCanceledOnTouchOutside(false);
                loadbar.show();

            }
        }
    }

    private void ValidateUser(final String email, final String pass) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PeremennyeActivity.mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
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
                        }else Toast.makeText(MainActivity.this, "Не правильный логин или пароль!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // Системная кнопка назад
    @Override
    public void onBackPressed(){
        if (backPressedtimer + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти!", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedtimer = System.currentTimeMillis();
    }
}