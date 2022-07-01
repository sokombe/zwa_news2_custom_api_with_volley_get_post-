package com.example.zwanews.ListviewCommentsAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.zwanews.Models.Comments;
import com.example.zwanews.Models.GlobalVariables_and_public_functions;
import com.example.zwanews.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Comments> {
    public ListAdapter(Context context, ArrayList<Comments> comments) {
        super(context, com.example.zwanews.R.layout.news_comments_items, comments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Comments comments = getItem(position);

        if(convertView == null){
            convertView = View.inflate(getContext(), R.layout.news_comments_items, null);
        }

        //initialisations ###################################################
        ImageView imageView=convertView.findViewById(R.id.imageView_profile_picture);
        TextView textView_name=convertView.findViewById(R.id.text_detail_owner_name);
        TextView textView_comment=convertView.findViewById(R.id.text_detail_content);
        TextView textView_date=convertView.findViewById(R.id.text_detail_time);

        //set data image###########################################################
        if(!GlobalVariables_and_public_functions.UsersProfiles.get(comments.getId_user()+"url").toString().equals("")){
            Picasso.get().load(GlobalVariables_and_public_functions.UsersProfiles.get(comments.getId_user()+"url").toString()).placeholder(R.drawable.loading).into(imageView);
        }else {
            imageView.setImageResource(R.drawable.user);
        }
        //set data others ###########################################################
        textView_name.setText(GlobalVariables_and_public_functions.UsersProfiles.get(comments.getId_user()+"name").toString());
        textView_comment.setText(comments.getContent());
        textView_date.setText(comments.getContent());

        return convertView;
    }
}
