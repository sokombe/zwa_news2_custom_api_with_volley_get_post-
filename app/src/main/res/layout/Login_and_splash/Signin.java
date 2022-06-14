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
import com.example.news.Models.Users;
import com.example.news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signin extends AppCompatActivity {

    EditText etRegEmail;
    EditText etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;

    EditText editText_username;
    FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference UsersCollection = db.collection("Users");

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etRegEmail = findViewById(R.id.Emal_Signin);
        etRegPassword = findViewById(R.id.Password_sigin);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.Button_Signin);
        editText_username=findViewById(R.id.Edit_UserName);

        mAuth = FirebaseAuth.getInstance();

        progressDialog =new ProgressDialog(Signin.this);


        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(Signin.this, Login.class));
        });
    }

    private void createUser(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String username=editText_username.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("L'email ne doit pas être vide");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Le mot de passe ne doit pas être vide");
            etRegPassword.requestFocus();
        }
        else if (TextUtils.isEmpty(username)) {
            etRegPassword.setError("Veuillez fournir votre nom d'utilisateur");
            editText_username.requestFocus();
        }
        else{

            progressDialog.setMessage("Verification...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){

                        // we set username in gmail
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        mAuth.getCurrentUser().updateProfile(profileUpdates);

                        // Add new user in the db
                        String userUrl="";
                        Users user=new Users(username,userUrl);

                        UsersCollection.document(mAuth.getCurrentUser().getEmail()).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Signin.this, "Réussi, Bienvenue!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Signin.this, MainActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Signin.this, "Erreur d'enregistrement: \n"+e.toString() , Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }else{
                        Toast.makeText(Signin.this, "Erreur d'enregistrement: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}