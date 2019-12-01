package com.production.wunner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserInfo extends AppCompatActivity {
    Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = findViewById(R.id.tb_info);
        setSupportActionBar(toolbar);
        updateStage(1);
        btn_start =findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UserInfo.this,MainActivity.class);
                UserInfo.this.startActivity(intent);
                UserInfo.this.finish();
            }
        });

    }

    private void updateStage(int i) {

        SharedPreferences.Editor editor =getSharedPreferences("Stage",MODE_PRIVATE).edit();
        editor.putInt("Num_Stage",i);
        editor.apply();
    }
}
