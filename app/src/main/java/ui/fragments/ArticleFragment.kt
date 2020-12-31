package berkaytuzel.ui.fragments

import berkaytuzel.ui.OnSwipeTouchListener
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import berkaytuzel.ui.NewsActivity
import berkaytuzel.ui.NewsViewModel
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.henrynews.R
import kotlinx.android.synthetic.main.fragment_article.*
import java.text.SimpleDateFormat


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    private var x = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val allArticles = viewModel.breakingNews.value!!.data!!.articles
        val article = args.article
        tvDesc.text = article.description
        tvSource.text = article.source?.name

        for(i in 0 until allArticles.size) {
            if(article.title == viewModel.breakingNews.value!!.data!!.articles[i].title ) {
                x = i
            }
        }
        if (x == 0){
            leftArrow.visibility = View.GONE
        } else {
            leftArrow.visibility = View.VISIBLE
        }
        tvSource.startAnimation(
            AnimationUtils.loadAnimation(
                this.context,
                R.anim.slide_in_left
            )
        )
        tvPublishedAt.text = (article.publishedAt?.let { fixTime(it) }?.let { checktimings(it) })
        tvTitle.text = article.title
        tvPublishedAt.startAnimation(
            AnimationUtils.loadAnimation(
                this.context,
                R.anim.slide_in_left
            )
        )
        tvTitle.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.slide_in_left))
        clock.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.slide_in_left))

        when {
            article.urlToImage.isNullOrEmpty() -> {
                Glide.with(this).load("https://cdn.discordapp.com/attachments/711259533602848882/791896839665614858/a76bbe2b-b31f-4bb8-877d-c174784f2c0b.JPG").into(
                    ivArticleImage
                )
            }
            article.urlToImage.contains("?crop") -> {
                Glide.with(this).load(article.urlToImage.split("?crop")[0]).into(ivArticleImage)
            }
            else -> {
                Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            }
        }
        val animation1 = AlphaAnimation(0.2f, 1.0f)
        animation1.duration = 500
        animation1.startOffset = 100
        animation1.fillAfter = true
        val animation2 = AlphaAnimation(0.01f, 1.0f)
        animation2.duration = 750
        animation2.startOffset = 500
        animation2.fillAfter = true
        ivArticleImage.startAnimation(animation1)
        tvDesc.startAnimation(animation2)
        extended_fab.shrink()
        extended_fab.setOnClickListener {
            viewModel.saveArticle(article)
            extended_fab.extend()
            Handler().postDelayed({
                extended_fab.shrink()
            }, 2000L)
        }

        context?.let {
            ivArticleImage.setOnTouchListener(object : OnSwipeTouchListener(it) {
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    rightThings()

                }

                override fun onSwipeRight() {
                    super.onSwipeRight()
                    leftThings()
                }

            })
        }
        rightArrow.setOnClickListener {
            rightThings()
        }
        leftArrow.setOnClickListener {
            leftThings()
        }

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
    private fun rightThings() {
        if (x != 19) {
            rightArrow.visibility = View.VISIBLE
            leftArrow.visibility = View.VISIBLE
            tvDesc.text = viewModel.breakingNews.value!!.data!!.articles[x + 1].description
            tvSource.text = viewModel.breakingNews.value!!.data!!.articles[x + 1].source?.name
            tvSource.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )
            tvPublishedAt.text =
                viewModel.breakingNews.value!!.data!!.articles[x + 1].publishedAt?.let {
                    checktimings(
                        fixTime(
                            it
                        )
                    )
                }
            tvPublishedAt.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )
            tvTitle.text = viewModel.breakingNews.value!!.data!!.articles[x + 1].title
            tvTitle.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )
            clock.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )
            when {
                viewModel.breakingNews.value!!.data!!.articles[x + 1].urlToImage.isNullOrEmpty() -> {
                    Glide.with(this)
                        .load("https://cdn.discordapp.com/attachments/711259533602848882/791896839665614858/a76bbe2b-b31f-4bb8-877d-c174784f2c0b.JPG")
                        .into(
                            ivArticleImage
                        )
                }
                viewModel.breakingNews.value!!.data!!.articles[x + 1].urlToImage!!.contains("?crop") -> {
                    Glide.with(this).load(
                        viewModel.breakingNews.value!!.data!!.articles[x + 1].urlToImage!!.split(
                            "?crop"
                        )[0]
                    ).into(ivArticleImage)
                }
                else -> {
                    Glide.with(this)
                        .load(viewModel.breakingNews.value!!.data!!.articles[x + 1].urlToImage)
                        .into(
                            ivArticleImage
                        )
                }
            }

            val animation1 = AlphaAnimation(0.2f, 1.0f)
            animation1.duration = 500
            animation1.startOffset = 100
            animation1.fillAfter = true
            val animation2 = AlphaAnimation(0.01f, 1.0f)
            animation2.duration = 750
            animation2.startOffset = 500
            animation2.fillAfter = true
            ivArticleImage.startAnimation(animation1)
            tvDesc.startAnimation(animation2)
            x++
        }
        if (x == 19){
            rightArrow.visibility = View.GONE
        }
    }
    private fun leftThings() {
        if (x != 0) {
            leftArrow.visibility = View.VISIBLE
            tvDesc.text = viewModel.breakingNews.value!!.data!!.articles[x - 1].description
            tvSource.text = viewModel.breakingNews.value!!.data!!.articles[x - 1].source?.name
            tvSource.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )
            tvPublishedAt.text =
                viewModel.breakingNews.value!!.data!!.articles[x - 1].publishedAt?.let {
                    checktimings(
                        fixTime(
                            it
                        )
                    )
                }
            tvPublishedAt.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )
            clock.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )

            tvTitle.text = viewModel.breakingNews.value!!.data!!.articles[x - 1].title
            tvTitle.startAnimation(
                AnimationUtils.loadAnimation(
                    this.context,
                    R.anim.slide_in_left
                )
            )

            when {
                viewModel.breakingNews.value!!.data!!.articles[x - 1].urlToImage.isNullOrEmpty() -> {
                    Glide.with(this)
                        .load("https://cdn.discordapp.com/attachments/711259533602848882/791896839665614858/a76bbe2b-b31f-4bb8-877d-c174784f2c0b.JPG")
                        .into(
                            ivArticleImage
                        )
                }
                viewModel.breakingNews.value!!.data!!.articles[x - 1].urlToImage!!.contains("?crop") -> {
                    Glide.with(this).load(
                        viewModel.breakingNews.value!!.data!!.articles[x - 1].urlToImage!!.split(
                            "?crop"
                        )[0]
                    ).into(ivArticleImage)
                }
                else -> {
                    Glide.with(this)
                        .load(viewModel.breakingNews.value!!.data!!.articles[x - 1].urlToImage)
                        .into(
                            ivArticleImage
                        )
                }
            }
            val animation1 = AlphaAnimation(0.2f, 1.0f)
            animation1.duration = 500
            animation1.startOffset = 100
            animation1.fillAfter = true
            val animation2 = AlphaAnimation(0.01f, 1.0f)
            animation2.duration = 750
            animation2.startOffset = 500
            animation2.fillAfter = true
            ivArticleImage.startAnimation(animation1)
            tvDesc.startAnimation(animation2)
            x--
        }
        if (x == 0){
            leftArrow.visibility = View.GONE
        }
    }
    }
