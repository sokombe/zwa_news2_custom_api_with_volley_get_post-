package com.example.zwanews.ui.AllCommentsOfNews;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.zwanews.Models.Comments;
import com.example.zwanews.R;

import java.util.ArrayList;

public class AllComments extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //set back button on toolbar and set title to toolbar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(com.example.zwanews.R.id.ListView_comments);

        ArrayList<Comments> list = new ArrayList<>();
        list = (ArrayList<Comments>) getIntent().getExtras().getSerializable("data");

        com.example.zwanews.ListviewCommentsAdapter.ListAdapter adapter = new com.example.zwanews.ListviewCommentsAdapter.ListAdapter(this, list);

        listView.setAdapter(adapter);

        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }
}