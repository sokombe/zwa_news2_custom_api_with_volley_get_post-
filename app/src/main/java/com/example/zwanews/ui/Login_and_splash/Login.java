package com.example.zwanews.ui.Login_and_splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zwanews.MainActivity;
import com.example.zwanews.Models.ApiModels.Articles;
import com.example.zwanews.Models.Users;
import com.example.zwanews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText etLoginEmail;
    EditText etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;


    // list of users
    List<Users> usersList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.zwanews.R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        etLoginEmail = findViewById(R.id.EditEmail);
        etLoginPassword = findViewById(R.id.Password);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.Button_Login);

        progressDialog =new ProgressDialog(Login.this);


        btnLogin.setOnClickListener(view -> {

            // we test our api




            loginUser();
        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(Login.this, Signin.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getallUsers();

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

                 boolean isRegistred=false;
                progressDialog.setMessage("Verification...");
                progressDialog.show();

           for(int i=0;i<usersList.size();i++){
               if( usersList.get(i).getEmail().equals(email)&&usersList.get(i).getPassword().equals(password )){
                   isRegistred=true;
                   break;
               }
           }

           if(isRegistred==true){
               progressDialog.dismiss();
            startActivity(new Intent(Login.this,MainActivity.class));
            finish();

           }
           else {
               Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
               progressDialog.dismiss();
           }
           }
        }



    // -------------------------------------- we get all users first -------------------------------------
    private void getallUsers() {
        // url to post our data
        // String url = "https://reqres.in/api/users";
        String url="http://192.168.1.100:8080/api/users/getallUsers";


        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    if(response!=null){
                        // use for loop
                        parseArray( response);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }
// ----------------------------------------- end ---------------------------------------------------------------


    private void  parseArray(JSONArray jsonArray){
        for(int i=0;i<jsonArray.length();i++){
            // initialize json Object
            try {

                JSONObject article=jsonArray.getJSONObject(i);
                usersList.add(
                        new Users(
                                article.getString("id"),
                                article.getString("firstname"),
                                article.getString("secondname"),
                                article.getString("email"),
                                article.getString("password"),
                                article.getString("phone")
                        )
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    //----------------------------------------  end ----------------------------------------
}