package com.example.resetpasswordapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.resetpasswordapplication.PasswordPolicy.REQUEST_CONFIRM_CREDENTIAL;

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
                PasswordPolicy.resetPassword(ctx, "1234");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CONFIRM_CREDENTIAL){
            if(resultCode == RESULT_OK){
                PasswordPolicy.resetPassword(this,"1234");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
