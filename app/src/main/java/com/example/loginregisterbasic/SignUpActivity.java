package com.example.loginregisterbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class SignUpActivity extends AppCompatActivity {
    private EditText username,password,repassword;
    private Button signup,signin;
    private Sqlite_DB_handler sqlite_db_handler;

    private boolean validate_username(EditText username)
    {
        String name = username.getText().toString();
        boolean name_valid=true;
        System.out.println("name length: "+name.length());
        if (name.equals(""))
        {
            name_valid=false;
            username.requestFocus();
            username.setError("Field cannot be empty");
        }
        else if (!name.matches("[a-zA-Z ]+"))
        {
            name_valid=false;
            username.requestFocus();
            username.setError("Enter only Alphabetical characters");
        }
        if (!name_valid)
        {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validate_password(EditText password)
    {
        String pass = password.getText().toString();
        boolean password_valid=true;
        System.out.println("password length: "+pass.length());
        String errorMessage="";
        if (pass.length()<8)
        {
            password_valid=false;
            password.requestFocus();
            errorMessage+="-> Minimum length of password is 8"+"\n";
        }
        if (!pass.matches("(?=.*[a-z])(?=.*[A-Z]).+$"))
        {
            System.out.println("upper and lower case error password: "+pass);
            password_valid=false;
            password.requestFocus();
            errorMessage+="-> Password should contain uppercase and lowercase letters"+"\n";
        }
        if (!pass.matches("(?=.*\\d).+$"))
        {
            password_valid=false;
            password.requestFocus();
            errorMessage+="-> Password should contain atleast 1 number"+"\n";
        }
        if (!pass.matches("(?=.*[-+_!@#$%^&*,. ?]).+$"))
        {
            password_valid=false;
            password.requestFocus();
            errorMessage+="-> Password should contain atleast 1 special character";
        }

        if (!password_valid)
        {
            password.setError(errorMessage);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);

        sqlite_db_handler = new Sqlite_DB_handler(SignUpActivity.this);

        username.addTextChangedListener(new TextWatcher() {
            boolean username_valid;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                username.requestFocus();
//                username.setHint("Only Alphabets [a-z A-Z]");
                username_valid=false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (!validate_username(username))
               {
                   username_valid=false;
               }
               else
               {
                   username_valid=true;
               }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username_valid) {
                    username.setBackgroundResource(R.drawable.success);
                }
                else {
                    username.setBackgroundResource(0);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            boolean password_valid;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password.requestFocus();
                password_valid=false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!validate_password(password))
                {
                    password_valid=false;
                }
                else
                {
                    password_valid=true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password_valid) {
                    password.setBackgroundResource(R.drawable.success);
                }
                else {
                    password.setBackgroundResource(0);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean validation_success=true;
                if (!validate_username(username))
                {
                    validation_success=false;
                }
                else if (!validate_password(password))
                {
                    validation_success=false;
                }
                else if (!repassword.getText().toString().equals(password.getText().toString()))
                {
                    repassword.setError("Passwords do not match");
                    validation_success=false;
                }
                if (validation_success)
                {
                    Toast.makeText(getApplicationContext(),"Validation Successful",Toast.LENGTH_LONG).show();
                    try {
//                        wait(3000);
                        sqlite_db_handler.addUsers(username.getText().toString(),password.getText().toString());
                        Toast.makeText(getApplicationContext(),"New User "+username.getText().toString()+" Added Successfully",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"User "+username.getText().toString()+" not added, try again later",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}