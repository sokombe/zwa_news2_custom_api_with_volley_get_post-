package com.example.zwanews.RequestMAnager;

import android.content.Context;
import android.widget.Toast;


import com.example.zwanews.Models.ApiModels.JsonResponse;
import com.example.zwanews.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {

    Context context;

    public RequestManager(Context context) {
        this.context = context;
    }


    String BASE_URL=" https://newsapi.org/v2/";

    Retrofit retrofit=new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getArticles(OnFetchDataListener listener,String country ,String category, String query){

        CallNewsApi callNewsApi=retrofit.create(CallNewsApi.class);

        Call<JsonResponse> call=callNewsApi.getArticles(country,category,query,context.getString(com.example.zwanews.R.string.api_key));

        try{
            call.enqueue(new Callback<JsonResponse>() {
                @Override
                public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {

                    if(!response.isSuccessful()){
                        Toast.makeText(context,"Error",Toast.LENGTH_LONG);


                    }

                    listener.onFetchData(response.body().getArticles(),response.message());

                }

                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                        listener.onError("Request Failed!!!!!!!!!");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public  interface CallNewsApi{
        @GET("top-headlines") //
        Call<JsonResponse> getArticles(
                @Query("country") String country, // exactement comme écrit dans la documentation de l'api
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String api_key
        );

            // Si le lien est direct sans paramètres
            //@GET
            //Call<JsonMode> getAllNews(@Url String url);  // JsonMode ou notre modèle de retour

    }
}
