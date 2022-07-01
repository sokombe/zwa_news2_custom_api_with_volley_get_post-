package com.example.zwanews.ui.userProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.zwanews.MainActivity;
import com.example.zwanews.Models.Users;
import com.example.zwanews.R;
import com.example.zwanews.databinding.FragmentUserProfileBinding;
import com.example.zwanews.ui.Login_and_splash.Signin;
import com.example.zwanews.ui.Login_and_splash.Splash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText Edit_nav_user_profile_name,nav_user_profile_phone,Edit_nav_user_profile_passw;

    ImageView nav_user_profile_image;
    Button button_user_profile_save;

    Uri iamgeUri;

    StorageReference storageReference;

    ProgressDialog progressDialog;


    private FragmentUserProfileBinding binding;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference UsersCollection = db.collection("Users");

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // init storage
        storageReference = FirebaseStorage.getInstance().getReference("UserImagesProfiles");
        // set progress
        progressDialog=new ProgressDialog(getActivity());
        // init views
        nav_user_profile_phone=root.findViewById(R.id.nav_user_profile_phone);

        Edit_nav_user_profile_passw=root.findViewById(R.id.nav_user_profile_password);

        button_user_profile_save=root.findViewById(R.id.button_user_profile_save);

        nav_user_profile_image=root.findViewById(R.id.nav_user_profile_image);

        Edit_nav_user_profile_name=root.findViewById(R.id.Edit_nav_user_profile_name);


        // init users profile
    if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
    Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).placeholder(R.drawable.user).into(nav_user_profile_image);
        }
        // action



    nav_user_profile_image.setOnClickListener(v -> {
        openfilechooser();
    });



    button_user_profile_save.setOnClickListener(v -> {
        if(iamgeUri!=null){
            uploadfile();
        }
        if(!Edit_nav_user_profile_name.getText().toString().isEmpty()){

            progressDialog.setMessage("En cours de changement....");
            progressDialog.show();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(Edit_nav_user_profile_name.getText().toString()).build();
            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Votre nom est mis à jour avec succès!!!", Toast.LENGTH_SHORT).show();
                    Edit_nav_user_profile_name.setText("");
                    gottomain();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Echec de mis à jour...", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if(!Edit_nav_user_profile_passw.getText().toString().isEmpty()){

            FirebaseAuth.getInstance().getCurrentUser().updatePassword(Edit_nav_user_profile_passw.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(getActivity(), "Mot de passe mis à jour avec succès!!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(!nav_user_profile_phone.getText().toString().isEmpty()){

            FirebaseAuth.getInstance().getCurrentUser().updatePassword(Edit_nav_user_profile_passw.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "Mot de passe mis à jour avec succès!!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

        return root;
    }


    //###############################################################################################################################

    // for pick from gallery or camera
    private void openfilechooser() {
        // create intent to open file chooser
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //control + o
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.getData() != null) {
            iamgeUri = data.getData();
            Picasso.get().load(iamgeUri).into(nav_user_profile_image);

//            imageView_upload.setImageURI(iamgeUri);
        }
    }
    //upload file to firebase storage and database
    private void uploadfile() {
        // get file name
     if (iamgeUri == null) {
            Toast.makeText(getActivity(), "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // get file name
            progressDialog.setMessage("En cours de changement....");
            progressDialog.show();

            // get file extension
            String extension = iamgeUri.getLastPathSegment().substring(iamgeUri.getLastPathSegment().lastIndexOf(".") + 1);
            // get file path
            StorageReference path = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "." + extension);

            // upload file to firebase storage

           path.putFile(iamgeUri).addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {

                        // get file url
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                // we set username in gmail
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);

                             //   addToDatabase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), uri.toString());

                            }
                        });

                        // show toast
                        Toast.makeText(getActivity(), "Image mise à jour avec succès!!!", Toast.LENGTH_SHORT).show();


                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

/*
    private void addToDatabase(String name, String url) {


        // Add new user in the db
        Users user=new Users(name,url);

        UsersCollection.document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        progressDialog.dismiss();

                 gottomain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Erreur d'enregistrement: \n"+e.toString() , Toast.LENGTH_SHORT).show();
                    }
                });
    }
*/
    private void gottomain() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


