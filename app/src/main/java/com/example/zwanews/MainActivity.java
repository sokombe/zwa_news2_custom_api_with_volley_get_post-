package com.example.zwanews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.example.zwanews.ui.Login_and_splash.Splash;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zwanews.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import android.content.DialogInterface;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;



public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    //to handle fragment
    private ActivityMainBinding binding;

    FirebaseAuth auth;

    ImageView profile_image;
    TextView profile_name, profile_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set firebase auth
        auth = FirebaseAuth.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // for toolbar
        setSupportActionBar(binding.appBarMain.toolbar);

        //for drawer
        DrawerLayout drawer = binding.drawerLayout;
        //for nav entierly
        NavigationView navigationView = binding.navView;

        //set naviationview color
        navigationView.setItemIconTintList(null);

        //init profile image  and name and email
        View headerView = navigationView.getHeaderView(0);
        // header views
        profile_image = (ImageView) headerView.findViewById(R.id.imageView_profile_picture);
        profile_name = (TextView) headerView.findViewById(R.id.textView_name);
        profile_email = (TextView) headerView.findViewById(R.id.textView_email);

        // Passing each menu ID as a set of Ids because each

        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_actus, R.id.nav_user_profile, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();

        //to control navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //set the profile picture
        if (auth.getCurrentUser() != null) {
            profile_name.setText(auth.getCurrentUser().getDisplayName());
            profile_email.setText(auth.getCurrentUser().getEmail());
        }
        if (auth.getCurrentUser() != null && auth.getCurrentUser().getPhotoUrl() != null) {
            Picasso.get().load(auth.getCurrentUser().getPhotoUrl()).placeholder(R.drawable.user).into(profile_image);
        } else {
            Picasso.get().load(R.drawable.user).into(profile_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_popup_toolbar, menu);
        return true;
    }

    @Override
    public  boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){
            case R.id.action_logout:


                new AlertDialog.Builder(this)
                        .setTitle("Déconnection")
                        .setMessage("Voulez-vous vraiment vous déconnecter ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                           auth.signOut();
                        startActivity(new Intent(MainActivity.this, Splash.class));
                      finish();
                            }
                        })

                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;

            case R.id.action_mode_close_button:

                new AlertDialog.Builder(this)
                        .setTitle("Fermer l'application")
                        .setMessage("Voulez-vous vraiment quitter l'application ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                            finish(); // close activity   and direct to the background
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        .show();

                break;
        }

        //// for back button of detail activity
//        if(item.getItemId()==android.R.id.home){ // the leadin button in the app bar
//             finish();
//        }

        return  super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}