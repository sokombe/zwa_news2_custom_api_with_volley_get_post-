package com.example.zwanews.ui.actus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zwanews.Models.ApiModels.Articles;
import com.example.zwanews.Models.ApiModels.JsonResponse;
import com.example.zwanews.Models.GlobalVariables_and_public_functions;
import com.example.zwanews.Models.Users;
import com.example.zwanews.R;
import com.example.zwanews.RecyclerViewAdapters.RecycleAdapter;
import com.example.zwanews.RequestMAnager.OnFetchDataListener;
import com.example.zwanews.RequestMAnager.RequestManager;
import com.example.zwanews.databinding.FragmentActusBinding;
import com.example.zwanews.ui.Login_and_splash.Splash;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class ActusFragment extends Fragment  implements View.OnClickListener {

    // https://newsapi.org/v2/top-headlines?country=us&apiKey=34123614cde84413a1b0af3451082009
//https://newsapi.org/docs/endpoints/top-headlines

    // for ads banner
    AdView mAdView;

    // for ads reward
    private RewardedAd mRewardedAd;
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

        // we begin with fetching data
        RequestManager manager=new RequestManager(getActivity());
        manager.getArticles(listener,"fr","general",null);


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

    }



    //########################################################################################################
    private final OnFetchDataListener<JsonResponse> listener=new OnFetchDataListener<JsonResponse>() {

        @Override
        public void onFetchData(List<Articles> list, String message) {
            // show data in recyclerview
            ShowNews(list);
            // we dismiss the progress when data are showed
            progressDialog.dismiss();
        }
        @Override
        public void onError(String message) {

        }
    };
    //######################################################################################################################
    // the show data function for showing in the recyclerview
    private void ShowNews(List<Articles> list) {

        recyclerView=getView().findViewById(R.id.Recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        adapter=new RecycleAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

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

    @Override
    public void onClick(View v) {

        ImageView image=(ImageView) v;

        String category="";

        // change backgroundcolor
        switch (image.getId())
        {
            case R.id.Image_cat_tout:
                category="general";
                break;
            case R.id.Image_cat_Affaire:
                category="business";
                break;
            case R.id.Image_cat_Divers:
                category="entertainment";
                break;
            case R.id.Image_cat_Sante:
                category="health";
                break;
            case R.id.Image_cat_Science:
                category="science";
                break;
            case R.id.Image_cat_Sport:
                category="sports";

                break;
            default:
                category="technology";



        }

        progressDialog.setMessage("Chargerment de news catégorie "+ GlobalVariables_and_public_functions.getfrench(category));
        progressDialog.show();
        progressDialog.setCancelable(true);

        RequestManager manager=new RequestManager(getActivity());
        manager.getArticles(listener,"fr",category,null);
    }


//################################# End ToolBar #####################################################################################



    // #######################################  for ads banner ################################################
    private void loadBaner() {
        // initialize admob
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

//        mAdView =getView(). findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }

    // #######################################  for ads reward ################################################
    private  void loadvideosreward(){
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(getActivity(), getString(R.string.recompense_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                mRewardedAd = null;
            }
        });

        if (mRewardedAd != null) {
            Activity activityContext = getActivity();
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }
}