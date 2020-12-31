package com.example.henrynews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.henrynews.R
import models.Article
import java.text.SimpleDateFormat


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            when {
                article.urlToImage.isNullOrEmpty() -> {
                    Glide.with(this).load("https://cdn.discordapp.com/attachments/711259533602848882/791896839665614858/a76bbe2b-b31f-4bb8-877d-c174784f2c0b.JPG").into(
                        findViewById(R.id.ivArticleImage)
                    )
                }
                article.urlToImage.contains("?crop") -> {
                    Glide.with(this).load(article.urlToImage.split("?crop")[0]).into(findViewById(R.id.ivArticleImage))
                }
                else -> {
                    Glide.with(this).load(article.urlToImage).into(findViewById(R.id.ivArticleImage))
                }
            }

            findViewById<TextView>(R.id.tvTitle).text = article.title
            findViewById<TextView>(R.id.tvSource).text = article.source?.name
            findViewById<TextView>(R.id.tvPublishedAt).text = article.publishedAt?.let { checktimings(fixTime(it)) }
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
    private fun fixTime(s: String) : String {
        val year =  s.replace("Z", "").split("T")[0].split("-")[0]
        val month =  s.replace("Z", "").split("T")[0].split("-")[1].split("-")[0]
        val day =  s.replace("Z", "").split("T")[0].subSequence(
            s.replace("Z", "").split("T")[0].length - 2, s.replace(
                "Z",
                ""
            ).split("T")[0].length
        )
        val date = "$day/$month/$year"
        val time = s.replace("Z", "").split("T")[1].subSequence(0, 5)
        return "$date $time"
    }

    private fun checktimings(time: String): String {
        val currentms = System.currentTimeMillis()
        var x = ""
        val timeInMilliseconds = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(time).time
        val diff = currentms - timeInMilliseconds
        x = when {
            diff <= 60000 -> {
                "JUST NOW"
            }
            diff < 3600000 -> {
                ((currentms - timeInMilliseconds) / 60).toString() + " MINUTES AGO"
            }
            diff < 7200000 -> {
                "1 HOUR AGO"
            }
            diff < 24*3600000 -> {
                ((currentms - timeInMilliseconds) / 3600000).toString() + " HOURS AGO"
            }
            diff < 48*3600000 -> {
                "1 DAY AGO"
            }
            else -> {
                ((currentms - timeInMilliseconds) / (3600000*24)).toString() + " DAYS AGO"
            }
        }
        return x
    }
}













