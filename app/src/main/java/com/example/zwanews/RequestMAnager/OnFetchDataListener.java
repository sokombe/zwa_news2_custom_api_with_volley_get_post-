package com.example.zwanews.RequestMAnager;

import com.example.zwanews.Models.ApiModels.Articles;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {
    void  onFetchData(List<Articles> list, String message);
    void  onError(String message);
}




