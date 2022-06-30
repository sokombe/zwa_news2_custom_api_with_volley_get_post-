package com.example.zwanews.RequestMAnager.i;

import android.content.Context;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.zwanews.MainActivity;
import com.example.zwanews.Models.ApiModels.JsonResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ArticleRequestManager {

    Context context;

    public ArticleRequestManager(Context context) {
        this.context = context;
    }

    RequestQueue requestQueue;








}