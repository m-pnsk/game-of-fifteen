package com.mpnsk.fifteen_game

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowCompat
import com.mpnsk.fifteen_game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        var imgUri: Uri? = null
    }

    private lateinit var binding: ActivityMainBinding
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.data != null) {
            imgUri = it.data!!.data
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.uploadImgButton.setOnClickListener {
            galleryLauncher.launch(
                Intent(Intent.ACTION_PICK)
                    .setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            )
        }
        binding.startGameButton.setOnClickListener {
            val stringMatrixSize = binding.sizeInput.text.toString()
            GameActivity.matrixSize =
                if (stringMatrixSize.isNotEmpty()) stringMatrixSize.toInt() else 4
            startActivity(
                Intent(
                    this@MainActivity,
                    GameActivity::class.java
                )
            )
        }
    }

    private fun fullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}