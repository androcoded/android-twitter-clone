package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginUsername,edtLoginPassword;
    private Button btnLogin,btnSignUpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtLoginUsername = findViewById(R.id.edtUserName);
        edtLoginPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUpActivity = findViewById(R.id.btnSignUpActivity);


        btnSignUpActivity.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(LoginActivity.this,TwitterUser.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUpActivity:
                Intent intent = new Intent(this,SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnLogin:
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                if (edtLoginUsername.getText().toString().equals("") || edtLoginPassword.getText().toString().equals("")){
                    Toast.makeText(this, "All field is required!", Toast.LENGTH_SHORT).show();

                }else{
                    progressDialog.show();
                    ParseUser.logInInBackground(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user!=null && e == null){
                                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(LoginActivity.this,TwitterUser.class);
                                startActivity(intent1);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
        }

    }
}
