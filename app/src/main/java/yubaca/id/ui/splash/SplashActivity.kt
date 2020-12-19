package yubaca.id.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import yubaca.id.MainActivity
import yubaca.id.R
import yubaca.id.util.Constant

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ openMainActivity() }, Constant.LENGTH_SPLASH_SCREEN)

    }

    private fun openMainActivity() {

        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

}