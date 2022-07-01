package com.example.zwanews.ui.Login_and_splash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zwanews.MainActivity;
import com.example.zwanews.Models.Users;
import com.example.zwanews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signin extends AppCompatActivity {

    EditText etRegEmail;
    EditText etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;

    EditText editText_username_prenom,editText_username_nom,phone;
    FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference UsersCollection = db.collection("Users");

    ProgressDialog progressDialog;

    // list of users
    List<Users> usersList=new ArrayList<>();

    // for sharedpreferences
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.zwanews.R.layout.activity_signin);

        etRegEmail = findViewById(R.id.Emal_Signin);
        etRegPassword = findViewById(R.id.Password_sigin);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.Button_Signin);
        editText_username_prenom=findViewById(R.id.Edit_UserName_prenom);
        editText_username_nom=findViewById(R.id.Edit_UserName_nom);
        phone=findViewById(R.id.phone);


        // init firebase
        mAuth = FirebaseAuth.getInstance();
        //init sharedpreferences
        sharedPreferences=getSharedPreferences("User", Context.MODE_PRIVATE);

        progressDialog =new ProgressDialog(Signin.this);


        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(Signin.this, Login.class));
        });
    }
 //-------------------------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        getallUsers();
    }

    //------------------------------------------------------------------------------------------------------

    private void createUser(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String username=editText_username_prenom.getText().toString();
        String nom=editText_username_nom.getText().toString();
        String myphone=phone.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("L'email ne doit pas être vide");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Le mot de passe ne doit pas être vide");
            etRegPassword.requestFocus();
        }
        else if (TextUtils.isEmpty(username)) {
            etRegPassword.setError("Veuillez fournir votre nom d'utilisateur");
            editText_username_prenom.requestFocus();
        }
        else if(TextUtils.isEmpty(nom)){
            editText_username_nom.setError("Veillez fournir un nom de famille");
            editText_username_nom.requestFocus();
        }
        else if(TextUtils.isEmpty(myphone)){
            phone.setError("Veillez fournir un nom de famille");
            phone.requestFocus();
        }
        else{


            // we prepare our share to edit
            SharedPreferences.Editor shareEdit=sharedPreferences.edit();

            progressDialog.setMessage("Verification...");
            progressDialog.show();

            boolean emailExists=false;

          for (int i=0;i<usersList.size();i++) {
              if(usersList.get(i).getEmail().equals(email)){
                  emailExists=true;
                  progressDialog.dismiss();
                  break;
              }
          }

             if(emailExists) {
                 Toast.makeText(this, "Email déjà eistant", Toast.LENGTH_SHORT).show();
            }
             else {
                 // we save the user email ------------------------
                 shareEdit.putString("email",email);
                 shareEdit.commit();
                 //  post request ---------------------------------
                 adduser(username,nom,email,password,myphone);
                 //------------------------------------------------
             }
        }
    }


    // -------------------------------------- we get all users first -------------------------------------
    private void getallUsers() {
        usersList.clear();
        // url to post our data
        // String url = "https://reqres.in/api/users";
        String url="http://192.168.1.100:8080/api/users/getallUsers";


        RequestQueue requestQueue = Volley.newRequestQueue(Signin.this);
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



    //----------------------------------- Post Data --------------------------------------

    private void adduser(String prenom, String nom,String email,String password,String myphone) {
        // url to post our data
        // String url = "https://reqres.in/api/users";
        String url="http://192.168.1.100:8080/api/users/addUser";
      progressDialog.setMessage("Envoie...");
      progressDialog.show();

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Signin.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                editText_username_nom.setVisibility(View.GONE);
                editText_username_prenom.setText("");
                etRegEmail.setText("");
                etRegPassword.setText("");
                phone.setText("");

                // on below line we are displaying a success toast message.
                Toast.makeText(Signin.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);

                    // below are the strings which we
                    // extract from our json object.
                    String name = respObj.getString("firstname");

                    // on below line we are setting this string s to our text view.
                   progressDialog.dismiss();
                    // we go to the main
                   startActivity(new Intent(Signin.this,MainActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(Signin.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                System.out.println( error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("firstname", prenom);
                params.put("secondname", nom);
                params.put("email", email);

                params.put("password", password);
                params.put("phone", myphone);


                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

}