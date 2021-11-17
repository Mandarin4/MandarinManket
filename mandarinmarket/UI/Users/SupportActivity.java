package com.example.mandarinmarket.UI.Users;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinmarket.R;

public class SupportActivity extends AppCompatActivity {
    TextView vk_logo, whatsapp_logo, telegram_messenger, odnoklassniki_logo,About_Company_button,logo_mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        vk_logo =(TextView) findViewById(R.id.vk_logo);
        whatsapp_logo =(TextView) findViewById(R.id.whatsapp_logo);
        telegram_messenger =(TextView) findViewById(R.id.telegram_messenger);
        odnoklassniki_logo =(TextView) findViewById(R.id.odnoklassniki_logo);
        logo_mail =(TextView) findViewById(R.id.logo_mail);
        About_Company_button =(TextView) findViewById(R.id.About_Company_button);
        About_Company_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        vk_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/im?sel=203814443"));
                startActivity(browserIntent);
            }
        });
        whatsapp_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/79515249832"));
                startActivity(browserIntent);
            }
        });
        telegram_messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mandarinOfficial"));
                startActivity(browserIntent);
            }
        });
        logo_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mailto:mandarinov1935@mail.ru"));
                startActivity(browserIntent);
            }
        });
    }
}