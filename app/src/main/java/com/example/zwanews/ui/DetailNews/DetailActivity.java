package com.example.zwanews.ui.DetailNews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.zwanews.Helper.Helper;
import com.example.zwanews.MainActivity;
import com.example.zwanews.Models.ApiModels.Articles;
import com.example.zwanews.Models.Comments;
import com.example.zwanews.R;
import com.example.zwanews.ui.AllCommentsOfNews.AllComments;
import com.example.zwanews.ui.Login_and_splash.Signin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    Articles article;
    TextView textView_title,textView_desc,textView_author,textView_time,
            textView_nombre_comment,
            textView_content,textView_share,textView_url;

    ImageView imageView_detail;
    Button button;
    EditText editText;

   // Helper helper=new Helper(this);
    int commentsSize=0;
    List<Comments> allcomments=new ArrayList<>();

    Button buttonCancel;
    AlertDialog alertDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usercomments = db.collection("Comments");
    CollectionReference Allusers = db.collection("Users");

    Map<String, Object> data=new HashMap<>();

    FirebaseAuth mauth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //set back button on toolbar and set title to toolbar as article title
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(article.getTitle());


        textView_author=findViewById(R.id.text_detail_author);
        textView_title=findViewById(R.id.text_detail_title);
        textView_desc=findViewById(R.id.text_detail_descrip);
        textView_time=findViewById(R.id.text_detail_time);
        textView_content=findViewById(R.id.text_detail_content);
        textView_url=findViewById(R.id.text_detail_Url);


        imageView_detail=findViewById(com.example.zwanews.R.id.Image_view_detail_news);
        textView_nombre_comment=findViewById(R.id.text_detail_nombre_comment);
        editText=findViewById(R.id.Edit_comments);
        button=findViewById(R.id.comment_button);
        textView_share=findViewById(R.id.button_share);


        mauth=FirebaseAuth.getInstance();

        textView_share.setOnClickListener(v -> {
             share();
        });

        article= (Articles) getIntent().getSerializableExtra("data");
        // commentsSize=helper.getCommentSize(article.getPublishedAt());
        //allcomments=helper.getAllComments(article.getPublishedAt().toString());



        //--------------------------------------------------------------------------------
        textView_title.setText(article.getTitle());
        textView_content.setText(article.getContent());
        textView_desc.setText(article.getContent());
        textView_author.setText(article.getAuthor());
        textView_time.setText(article.getCreatedAt());
       // textView_nombre_comment.setText(String.valueOf(commentsSize)+" Commentaire(s)");
        textView_url.setText(article.getLink());


        Picasso.get()
                .load(article.getLink())
                .noFade()
                .resize(300,300)
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.loading)
                .into(imageView_detail);


        ///// COMMENT BUTTON ----------------------------------------------------------------------

        button.setOnClickListener(v -> {
            if(!editText.getText().toString().isEmpty()){
             addcomment(editText.getText().toString());

            }
        });


        // show comments users ######################################################################################

        textView_nombre_comment.setOnClickListener(v -> {
            if(!allcomments.isEmpty()) {

             //   startActivity(new Intent(DetailActivity.this, AllComments.class).putExtra("data", (Serializable) allcomments));
            }

                else {
               Toast.makeText(this,"Aucun commentaire pour cet article.",Toast.LENGTH_LONG).show(); ;
            }
        });


        //----------------------------------------------------------------------------------------------------
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getTitle()+"\n"+article.getContent()+"\n"+article.getLink());
        sendIntent.setType("text/plain");
        if(sendIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(sendIntent);
        }
    }

    //######################################################################################################################
    public String getCurrentDateAndTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


//######################################################################################################################

    @Override
    protected void onStart() {
        super.onStart();
        editText.setText("");
        allcomments.clear();

        getAllcommentByArticleId(article.getId());
        // we get all comments by id
        //####################################################################################

    }



    public void   LauchUrl(View view){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(article.getLink()));
        startActivity(i);
    }

    @Override
    public void onClick(View v) {

    }


    //##################################### for menu option#################################################################################
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.share_in_detail,menu);

        //set color for my menu option icons
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
        }


        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_in_detail:
             share();
                break;
            case R.id.copy_in_clopboard:
                copyinClipboard(article.getTitle()+"\n"+article.getContent()+"\n"+article.getLink());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//############################################ End of menu option ##########################################################################

private  void copyinClipboard(String text) {
    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("text", text);
    clipboard.setPrimaryClip(clip);
    Toast.makeText(this, "Copié dans le Clipbord", Toast.LENGTH_SHORT).show();
}


    //----------------------------------- get all articles by category id ------------------------------
    private  void getAllcommentByArticleId(String id_article){
        allcomments.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(DetailActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://"+getString(R.string.LOCALHOST)+":8080/api/comments/"+id_article+"/getallCommentById", null, new Response.Listener<JSONArray>() {
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

    //// parseArray ----------------------------------------------------
    private void  parseArray(JSONArray jsonArray){

        for(int i=0;i<jsonArray.length();i++){
            // initialize json Object
            try {

                JSONObject comment=jsonArray.getJSONObject(i);
                allcomments.add(
                        new Comments(
                                comment.getString("content"),
                                comment.getString("id_article"),
                                comment.getString("id_user")
                        )
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

       // commentsSize=allcomments.size();
        textView_nombre_comment.setText(String.valueOf(allcomments.size())+" Commentaire(s)");
        Toast.makeText(this, String.valueOf(commentsSize), Toast.LENGTH_SHORT).show();

    }
    //----------------------------------------  end ----------------------------------------



    private void addcomment(String content) {

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("User", Context.MODE_PRIVATE);
         String email=sharedPreferences.getString("email","");

        // url to post our data
        // String url = "https://reqres.in/api/users";
        String url="http://"+getString(R.string.LOCALHOST)+":8080/api/comments/addComment";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(DetailActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                editText.setText("");
                getAllcommentByArticleId(article.getId());
                Toast.makeText(DetailActivity.this, "Commentaire ajouté avec succès", Toast.LENGTH_SHORT).show();

                try {

                    JSONObject respObj = new JSONObject(response);

                    String name = respObj.getString("content");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(DetailActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("content", content);
                params.put("id_article", article.getId());
                params.put("id_user", email);


                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

}





























