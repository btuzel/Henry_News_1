package berkaytuzel.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.henrynews.R

class SplashScreenActivity : AppCompatActivity() {
    private val splashduration = 2000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreenActivity, NewsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            finish()
        }, splashduration)
    }
}