package com.production.wunner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Login extends AppCompatActivity {
    Button btn_login;
    EditText edit_name, edit_pass;
    String user,pass;
    private static final int MY_LOCATION_REQUEST_CODE = 1977;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences =getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
        //check user log in before
        if( preferences.getString("user_name",null)!=null&& preferences.getString("pass",null) !=null)
        {
            ChangeActivity();
        }
        Mapping();
        setupUI(findViewById(R.id.relative_login));
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= edit_name.getText().toString();
                pass =edit_pass.getText().toString();
                if(user==null|| pass==null)
                {
                    Toast.makeText(getApplicationContext(),"Please complete info",Toast.LENGTH_SHORT).show();
                }
                if(Validation(user,pass))
                {
                    SharedPreferences preferences =getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_name",user);
                    editor.putString("pass",pass);
                    editor.commit();
                    showPermissionRequest();

                }
            }
        });

    }

    private void ChangeActivity() {

        //send data user to request and fetch data
        // Storage User and pass
        SharedPreferences preferences =getApplicationContext().getSharedPreferences("Stage",MODE_PRIVATE);
        int num_stage=preferences.getInt("Num_Stage",0);
        switch (num_stage)
        {
        case 1:
        {
            Intent intent =new Intent(Login.this,UserInfo.class);
            Login.this.startActivity(intent);
            Login.this.finish();
            break;
        }
        case 2:
        {
            Intent intent =new Intent(Login.this,MainActivity.class);
            Login.this.startActivity(intent);
            Login.this.finish();
            break;
        }
        case 3:
        {
            Intent intent =new Intent(Login.this,Misson.class);
            Login.this.startActivity(intent);
            Login.this.finish();
            break;
        }
        default:
        {
            Intent intent =new Intent(Login.this,UserInfo.class);
            Login.this.startActivity(intent);
            Login.this.finish();
            break;
        }
        }


    }

    private boolean Validation(String user, String pass) {
        return true;
    }


    private void Mapping() {
        btn_login =(Button)findViewById(R.id.btn_login);
        edit_name= (EditText) findViewById(R.id.edit_name);
        edit_pass=(EditText) findViewById(R.id.edit_pass);
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Login.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    private void showPermissionRequest() {
        List<String> permissions =new ArrayList<>();
        int fine_location = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        int corase_location = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(fine_location!=PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(corase_location!=PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(permissions.isEmpty())
        {
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,permissions.toArray(new String[permissions.size()]),MY_LOCATION_REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case  MY_LOCATION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    ChangeActivity();
                } else {
                    Toast.makeText( this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences =getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("user_name");
                    editor.remove("pass");
                    editor.commit();
                    finish();
                }
                break;

        }
    }
}