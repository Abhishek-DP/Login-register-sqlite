package com.example.loginregisterbasic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
    private EditText username,password;
    private Button signup,signin;
    private Sqlite_DB_handler sqlite_db_handler;
    SharedPreferences sharedPreferences;

    public static final String signinCredentials = "login_credentials";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);


        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);

        sqlite_db_handler = new Sqlite_DB_handler(SignInActivity.this);

        // Acts as session
        sharedPreferences = getSharedPreferences(signinCredentials, Context.MODE_PRIVATE);

        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Validation Successful",Toast.LENGTH_LONG).show();
                try {
//                        wait(3000);
                    Boolean checkUsernamePassword=sqlite_db_handler.checkUsernamePassword(username.getText().toString(),password.getText().toString());

                    if (checkUsernamePassword)
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("usernameKey", username.getText().toString());
                        editor.putString("passwordKey",password.getText().toString());
                        editor.apply();

                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignInActivity.this, loginSuccess.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Username/Password incorrect",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Something went wrong, try again later\n"+e,Toast.LENGTH_LONG).show();
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });


    }
}
