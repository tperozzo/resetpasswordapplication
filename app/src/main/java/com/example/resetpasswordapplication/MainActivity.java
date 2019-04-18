package com.example.resetpasswordapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    AppCompatActivity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;

        findViewById(R.id.request_admin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceAdminReceiver.enableAdmin(ctx);
            }
        });

        findViewById(R.id.reset_password_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordPolicy.resetPassword(ctx, "1111");
            }
        });



    }
}
