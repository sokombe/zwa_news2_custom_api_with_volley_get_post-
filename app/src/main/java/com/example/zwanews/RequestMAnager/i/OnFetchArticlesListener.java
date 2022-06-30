package com.example.zwanews.RequestMAnager.i;

import com.example.zwanews.Models.ApiModels.Articles;

import java.util.List;

public interface OnFetchArticlesListener<NewsApiResponse> {
    void  onFetchData(List<Articles> list, String message);
    void  onError(String message);
}
