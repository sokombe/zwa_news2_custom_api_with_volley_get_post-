package com.example.zwanews.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zwanews.Models.ApiModels.Articles;
import com.example.zwanews.R;
import com.example.zwanews.ui.DetailNews.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.CustomViewHolder> {

    private Context context;
    private List<Articles> news;

    public RecycleAdapter(Context context, List<Articles> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.news_list_items,parent,false));

     }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder myView, int position) {

        myView.text_title.setText(news.get(position).getTitle());
        myView.text_source.setText(news.get(position).getSource());

        if(news.get(position).getLink()!=null){
            Picasso.get().load(news.get(position).getLink()).placeholder(com.example.zwanews.R.drawable.loading).into(myView.img_headline)    ;
        }

        myView.cardView.setOnClickListener(v -> {
           context.startActivity(new Intent(context, DetailActivity.class).putExtra("data",news.get(position)));
        });

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_title,text_source;
        ImageView img_headline;
        CardView cardView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            text_title=itemView.findViewById(R.id.text_title);
            text_source=itemView.findViewById(R.id.text_source);
            img_headline=itemView.findViewById(R.id.imge_headline);
            cardView=itemView.findViewById(R.id.main_container);


        }
    }
}





