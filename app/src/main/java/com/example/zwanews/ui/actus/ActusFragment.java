package com.example.zwanews.ui.actus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zwanews.MainActivity;
import com.example.zwanews.Models.ApiModels.Articles;
import com.example.zwanews.Models.ApiModels.JsonResponse;
import com.example.zwanews.Models.GlobalVariables_and_public_functions;
import com.example.zwanews.Models.Users;
import com.example.zwanews.R;
import com.example.zwanews.RecyclerViewAdapters.RecycleAdapter;
import com.example.zwanews.RequestMAnager.i.OnFetchArticlesListener;
import com.example.zwanews.RequestMAnager.i.ArticleRequestManager;
import com.example.zwanews.databinding.FragmentActusBinding;
/*
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
 */
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActusFragment extends Fragment  implements View.OnClickListener {

    // https://newsapi.org/v2/top-headlines?country=us&apiKey=34123614cde84413a1b0af3451082009
   //https://newsapi.org/docs/endpoints/top-headlines

    //-------------------------  get all articles --------------------------
    List<Articles> articles=new ArrayList<>();
    //------------------------------------------------------------------

    //private RewardedAd mRewardedAd;
    private final String TAG = "MainActivity";

    RecyclerView recyclerView;
    RecycleAdapter adapter;

    AlertDialog alertDialog;

    ProgressDialog progressDialog;

    ViewFlipper viewFlipper;

    ImageView img1,img2,img3,img4,img5,img6,img7;

    Toolbar toolbar;

    FirebaseAuth auth;



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Allusers = db.collection("Users");


    //####################################################################################""



    private FragmentActusBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_actus, container, false);

        //set toolbar
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // for ads banner
//        loadBaner();
//        loadvideosreward();

        // ##################################### initialisation
        viewFlipper=view.findViewById(R.id.flipper);

        img1=view.findViewById(R.id.Image_cat_tout);
        img1.setOnClickListener(this);


        img2=view.findViewById(R.id.Image_cat_Affaire);
        img2.setOnClickListener(this);

        img3=view.findViewById(R.id.Image_cat_Divers);
        img3.setOnClickListener(this);

        img4=view.findViewById(R.id.Image_cat_Sante);
        img4.setOnClickListener(this);

        img5=view.findViewById(R.id.Image_cat_Science);
        img5.setOnClickListener(this);

        img6=view.findViewById(R.id.Image_cat_Sport);
        img6.setOnClickListener(this);

        img7=view.findViewById(R.id.Image_cat_Techno);
        img7.setOnClickListener(this);

        //################################# Firebase init

        auth=FirebaseAuth.getInstance();



        //################################# flipper images ##################

        int images[]={R.drawable.busness,R.drawable.image_4,R.drawable.info,R.drawable.technology, R.drawable.image_3,R.drawable.health,R.drawable.general,R.drawable.sport,R.drawable.putin,R.drawable.movie};


        for (int image:images){
            setViewFlipper(image);
        }

        //####################################### END #################################

        progressDialog =new ProgressDialog(getActivity());

        // we prepare progress;
        progressDialog.setMessage("loading...");
        progressDialog.show();
        progressDialog.setCancelable(true);

        //--------------------------------- we get data on int ----------------------------
        getAlldata();
        //------------------------------------ end ----------------------------------------------

        // we begin with fetching data
       // ArticleRequestManager manager=new ArticleRequestManager(getActivity());
        //manager.getArticles(listener,"fr","general",null);

        // Inflate the layout for getActivity() fragment
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
      /*
        Allusers
                .get()
                .addOnSuccessListener(getActivity(),new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) { // multiple snapshot with querydocumentsnapshot
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots ){

                            Users user =documentSnapshot.toObject(Users.class);
                            user.setUserEmail(documentSnapshot.getId());

                            //we add user info to the  global map
                            GlobalVariables_and_public_functions.UsersProfiles.put(user.getUserEmail()+"name",user.getdisplayname());
                            GlobalVariables_and_public_functions.UsersProfiles.put(user.getUserEmail()+"url",user.getUrl());
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
*/
    }

    //########################################################################################################

    @Override
    public void onClick(View v) {

        ImageView image=(ImageView) v;

        String category="";

        // change backgroundcolor
        switch (image.getId())
        {
            case R.id.Image_cat_tout:
                category="1";
                break;
            case R.id.Image_cat_Affaire:
                category="2";
                break;
            case R.id.Image_cat_Divers:
                category="3";
                break;
            case R.id.Image_cat_Sante:
                category="4";
                break;
            case R.id.Image_cat_Science:
                category="5";
                break;
            case R.id.Image_cat_Sport:
                category="6";
                break;
            default:
                category="7";

        }

        /// we begin fetching
        progressDialog.setMessage("Chargerment de news catégorie "+ GlobalVariables_and_public_functions.getcatname(category));
        progressDialog.show();
        progressDialog.setCancelable(true);

        if(category.equals("1")){
            getAlldata();
        }
        else {
            getAlldataByCategoryId(category);
        }

       // SharedPreferences sharedPreferences= getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
       // String email=sharedPreferences.getString("email","");
       // Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();

    }

    // the show data function for showing in the recyclerview
    private void ShowNews(List<Articles> list) {

        recyclerView=getView().findViewById(R.id.Recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        adapter=new RecycleAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        // we dismiss the progress when data are showed
        progressDialog.dismiss();

    }
    //###################### for viewFlipper #########################################"
    public  void setViewFlipper(int image){

        ImageView imageView=new ImageView(getActivity());

        //set images in flipper
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(7500);
        viewFlipper.setAutoStart(true);

        // animation
        viewFlipper.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getActivity(), android.R.anim.slide_out_right);


    }

    //----------------------------------- get all articles ------------------------------
    private  void getAlldata(){
        articles.clear();
     // if the api return an array [], we have to create a   JsonArrayRequest instance  and expeting JSONArray
        // if json {} we have to create a JsonObjectRequest instance and expeting a JsonObjet
        // we can expect a String with RequestString

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://192.168.1.100:8080/api/articles/getallArticles", null, new Response.Listener<JSONArray>() {
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
    //----------------------------------- get all articles by category id ------------------------------
    private  void getAlldataByCategoryId(String id_category){
        articles.clear();
        // if the api return an array [], we have to create a   JsonArrayRequest instance  and expeting JSONArray
        // if json {} we have to create a JsonObjectRequest instance and expeting a JsonObjet
        // we can expect a String with RequestString

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://192.168.1.100:8080/api/articles/"+id_category+"/getAllArticleByCategory", null, new Response.Listener<JSONArray>() {
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

                JSONObject article=jsonArray.getJSONObject(i);
                articles.add(
                        new Articles(
                                article.getString("id"),
                                article.getString("title"),
                                article.getString("author"),
                                article.getString("content"),
                                article.getString("source"),
                                article.getString("link"),
                                article.getString("langue"),
                                article.getString("category_id"),
                                article.getString("createdAt")
                        )
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ShowNews(articles);
    }
    //----------------------------------------  end ----------------------------------------


}