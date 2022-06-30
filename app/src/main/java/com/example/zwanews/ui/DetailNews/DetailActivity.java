package com.example.zwanews.ui.DetailNews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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


import com.example.zwanews.Helper.Helper;
import com.example.zwanews.Models.ApiModels.Articles;
import com.example.zwanews.Models.Comments;
import com.example.zwanews.R;
import com.example.zwanews.ui.AllCommentsOfNews.AllComments;
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

    Helper helper=new Helper(this);
    int commentsSize=0;
    List<Comments> getallcomment=new ArrayList<>();

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
//          commentsSize=helper.getCommentSize(article.getPublishedAt());
//        getallcomment=helper.getAllComments(article.getPublishedAt().toString());

        textView_title.setText(article.getTitle());
        textView_content.setText(article.getContent());
        textView_desc.setText(article.getContent());
        textView_author.setText(article.getAuthor());
        textView_time.setText(article.getCreatedAt());
        textView_nombre_comment.setText(String.valueOf(commentsSize)+" Commentaire(s)");
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

        button.setOnClickListener(v -> {

            if(!editText.getText().toString().isEmpty()){

                Comments comment=new Comments(article.getCreatedAt().toString(),editText.getText().toString(),getCurrentDateAndTime(),mauth.getCurrentUser().getEmail());
//
//              usercomments.document(mauth.getCurrentUser().getEmail()+"|"+getCurrentDateAndTime()).set(comment)

                    usercomments.document().set(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
    
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(DetailActivity.this, "Commentaire ajouté avec succès!", Toast.LENGTH_SHORT).show();

                        editText.setText("");
                        getallcomment.clear();

                        usercomments
                                .whereEqualTo("fk_new",article.getCreatedAt())
                                .get()

                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots ){
                                    Comments comment =documentSnapshot.toObject(Comments.class);
                                    comment.setIdcomment(documentSnapshot.getId());

                                    getallcomment.add(comment);
                                }

                                commentsSize=getallcomment.size();
                                textView_nombre_comment.setText(String.valueOf(commentsSize)+" Commentaire(s)");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                            }});


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }});

        // show comments users ######################################################################################
        textView_nombre_comment.setOnClickListener(v -> {
            if(!getallcomment.isEmpty()) {

                startActivity(new Intent(DetailActivity.this, AllComments.class).putExtra("data", (Serializable) getallcomment));
            }

                else {
               Toast.makeText(this,"Aucun commentaire pour cet article.",Toast.LENGTH_LONG).show(); ;
            }
        });

        //
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
        getallcomment.clear();

        usercomments
                .whereEqualTo("fk_new",article.getCreatedAt())
                .get()
                .addOnSuccessListener(this,new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) { // multiple snapshot with querydocumentsnapshot

                        System.out.println("//###########################################################");

                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots ){
                            Comments comment =documentSnapshot.toObject(Comments.class);
                            comment.setIdcomment(documentSnapshot.getId());
                            System.out.println(comment.getDate_comment());
                            getallcomment.add(comment);


                        }
                        commentsSize=getallcomment.size();
                        System.out.println("//###########################################################");
                        System.out.println(commentsSize);

                        textView_nombre_comment.setText(String.valueOf(commentsSize)+" Commentaire(s)");


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });



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
}





























