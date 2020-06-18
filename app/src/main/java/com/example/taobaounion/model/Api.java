package com.example.taobaounion.model;

import com.example.taobaounion.model.bean.Categories;
import com.example.taobaounion.model.bean.ChoicenessCategories;
import com.example.taobaounion.model.bean.ChoicenessContent;
import com.example.taobaounion.model.bean.HomePagerContent;
import com.example.taobaounion.model.bean.OnSellContent;
import com.example.taobaounion.model.bean.SearchRecommend;
import com.example.taobaounion.model.bean.SearchResult;
import com.example.taobaounion.model.bean.Ticket;
import com.example.taobaounion.model.bean.TicketParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET("discovery/{materialId}/{page}")
    Call<HomePagerContent> getHomePagerContent(@Path("materialId") long materialId, @Path("page") int page);

    @POST("tpwd")
    Call<Ticket> getTicketResult(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<ChoicenessCategories> getChoicenessCategories();

    @GET("recommend/{categoryId}")
    Call<ChoicenessContent> getChoicenessContent(@Path("categoryId") int categoryId);

    @GET("onSell/{page}")
    Call<OnSellContent> getOnSellContent(@Path("page") int page);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page,@Query("keyword") String keyword );
}
