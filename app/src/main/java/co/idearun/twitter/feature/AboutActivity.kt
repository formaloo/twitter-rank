package co.idearun.twitter.feature

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import co.idearun.twitter.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        title = getString(R.string.how_this_work)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<ImageView>(R.id.back_btn).setOnClickListener {
            onBackPressed()
        }

        findViewById<AppCompatButton>(R.id.send_feedback_btn).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://formaloo.net/beta-feedback")
            startActivity(intent)
    }

}}