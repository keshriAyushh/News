package com.example.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.databinding.ActivityMainBinding.inflate
import com.example.news.databinding.ItemLayoutBinding.inflate

class NewsAdapter(
    private val context: Context,
    private val articles: List<Article>
    ) :

    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ArticleViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        holder.newsTitle.text = article.title
        holder.newsContent.text = article.description


        Glide.with(context).load(article.urlToImage).into(holder.newsImage)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class ArticleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var newsImage = itemView.findViewById<ImageView>(R.id.ivNewsImage)
        var newsTitle = itemView.findViewById<TextView>(R.id.tvNewsTitle)
        var newsContent = itemView.findViewById<TextView>(R.id.tvNewsContent)

    }
}