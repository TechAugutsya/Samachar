package com.example.samachar.repository

import androidx.lifecycle.LiveData
import com.example.samachar.api.RetrofitInstance
import com.example.samachar.db.ArticleDatabase
import com.example.samachar.models.Article
import com.example.samachar.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getHeadLines(countryCode: String, pageNumber: Int): Response<NewsResponse> =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article): Long =
        db.getArticleDao().upsert(article)

    fun getFavoriteArticles(): LiveData<List<Article>> =
        db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article): Unit =
        db.getArticleDao().deleteArticle(article)
}
