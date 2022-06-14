package com.example.news.Activities_fragments.Login_and_splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.Activities_fragments.MainActivity;
import com.example.news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText etLoginEmail;
    EditText etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        etLoginEmail = findViewById(R.id.EditEmail);
        etLoginPassword = findViewById(R.id.Password);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.Button_Login);

        progressDialog =new ProgressDialog(Login.this);


        btnLogin.setOnClickListener(view -> {
            loginUser();
        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(Login.this, Signin.class));
        });
    }



    //#####################################################################################################

    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("L'adresse mail ne doit pas être vide");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Le mot de passe ne doit pas être vide\"");
            etLoginPassword.requestFocus();
        }else{

            progressDialog.setMessage("Verification...");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful()){
                        Toast.makeText(Login.this, "Bienvenue", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(Login.this, "Erreur de connexion: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}