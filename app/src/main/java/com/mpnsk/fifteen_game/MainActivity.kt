package com.mpnsk.fifteen_game

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imgUri = Uri.parse("android.resource://com.mpnsk.fifteen_game/${R.drawable.default_image}")
        binding.uploadImgButton.setOnClickListener {
            galleryLauncher.launch(
                Intent(Intent.ACTION_PICK)
                    .setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            )
        }
        binding.startGameButton.setOnClickListener {
            val stringMatrixSize = binding.sizeInput.text.toString()
            GameActivity.matrixSize = if (stringMatrixSize.isNotEmpty()) stringMatrixSize.toInt() else 4
            startActivity(
                Intent(
                    this@MainActivity,
                    GameActivity::class.java
                )
            )
        }
    }
}