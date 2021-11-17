package com.example.mandarinmarket.UI.Users;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinmarket.PeremennyeActivity;
import com.example.mandarinmarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegicterActivity extends AppCompatActivity {


    Button register_btn, btn_back_register;
    EditText user_name, user_pass, user_phone, user_email;
    private ProgressDialog loadbar;
RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regicter);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PeremennyeActivity.mAuth = FirebaseAuth.getInstance();




        register_btn = findViewById(R.id.btn_register);
        user_name =  findViewById(R.id.btn_register_user_name_input);
        user_pass =  findViewById(R.id.btn_register_pass_input);
        user_phone =  findViewById(R.id.btn_register_phone_input);
        user_email = findViewById(R.id.btn_register_email_input);
        loadbar = new ProgressDialog(this);

        btn_back_register = findViewById(R.id.btn_back_register);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        btn_back_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent(RegicterActivity.this, MainActivity.class);
                startActivity(backintent);finish();
            }
        });
        relativeLayout = findViewById(R.id.relativ);
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean validatePassword(String pass){
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern digitCase = Pattern.compile("[0-9]");

        if (!lowerCase.matcher(pass).find()){
            Toast.makeText(this, "В пароле должны содеражаться латиницу нижнего регистра.", Toast.LENGTH_SHORT).show();
            return false;
        } else
            if (!upperCase.matcher(pass).find()){
            Toast.makeText(this, "В пароле должны содеражаться латиницу верхнего регистра.", Toast.LENGTH_SHORT).show();
            return false;
        }else
            if (!digitCase.matcher(pass).find()){
            Toast.makeText(this, "В пароле должны содеражаться цифры.", Toast.LENGTH_SHORT).show();
            return false;
        }else
            if (pass.length() < 6){
            Toast.makeText(this, "В пароле должно содержаться более 6 символов.", Toast.LENGTH_SHORT).show();
            return false;
        }else
            if (pass.length() > 20){
                Toast.makeText(this, "В пароле должно содержаться менее 20 символов.", Toast.LENGTH_SHORT).show();
                return false;
            }else

        return true;
    }


    static boolean validatePhone(String phone) {
        Pattern numphoneCase = Pattern.compile("[0-9]");
        Pattern numphoneCase2 = Pattern.compile("[0-9]+");
        if (phone.charAt(0) == '+' && phone.charAt(1) == '7' && phone.length() == 12 && numphoneCase2.matcher(phone).find() ) return false;
        else if (phone.charAt(0) == '8'  && phone.length() ==11 && numphoneCase.matcher(phone).find()) return false;
        else return true;
    }

    private void CreateAccount() {
        String username = user_name.getText().toString();
        String pass = user_pass.getText().toString().trim();
        String phone = user_phone.getText().toString().trim();
        String email = user_email.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Введите ваше имя.", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Введите телефон.", Toast.LENGTH_SHORT).show();
        }else if (validatePhone(phone)){
            Toast.makeText(this, "Телефон введен не корректно.", Toast.LENGTH_SHORT).show();
        }else if (pass.isEmpty()){
            Toast.makeText(this, "Пароль не может быть пустым.", Toast.LENGTH_SHORT).show();
        }else if (!validatePassword(pass)){
        }else if (!PeremennyeActivity.validateEmailAddress(email)){
            Toast.makeText(this, "Введите правильный email.", Toast.LENGTH_SHORT).show();
        }else {
                loadbar.setTitle("Создание аккаунта");
                loadbar.setMessage("Пожалуйста подождите...");
                loadbar.setCanceledOnTouchOutside(false);
                loadbar.show();

                ValidatePhone(username, pass, phone, email);
        }
    }

    public static String removeChar(String s, char c) {
        StringBuilder r = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) {
                r.append(cur);
            }
        }

        return r.toString();
    }

    private void ValidatePhone(final String username, final String pass, final String phone, final String email) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        final String Email2 = removeChar(email,'.');
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(Email2).exists())) {
                    final HashMap<String, Object> UserDataMap = new HashMap<>();
                    UserDataMap.put("phone", phone); UserDataMap.put("username", username);  UserDataMap.put("email", email);
                    PeremennyeActivity.mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            PeremennyeActivity.mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override  public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    RootRef.child("Users").child(Email2).updateChildren(UserDataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override  public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadbar.dismiss();
                                            Toast.makeText(RegicterActivity.this, "Регистрация прошла " +
                                                    "успешно. Проверьте Email для подтверждения почты!", Toast.LENGTH_SHORT).show();
                                            Intent loginIntent = new Intent(RegicterActivity.this, Sign_In_Activity.class);
                                            startActivity(loginIntent);
                                            finish();
                                        } else {
                                            loadbar.dismiss();
                                            Toast.makeText(RegicterActivity.this, "Ошибка.", Toast.LENGTH_SHORT).show();} }}); } }}); } }});
                } else {loadbar.dismiss();
                    Toast.makeText(RegicterActivity.this, "Почта " + email + " уже зарегистрирована!", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(RegicterActivity.this, Sign_In_Activity.class);
                    startActivity(loginIntent);finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Системная кнопка назад
    @Override
    public void onBackPressed(){
        Intent backintent = new Intent(RegicterActivity.this, MainActivity.class);
        startActivity(backintent);finish();
    }

}