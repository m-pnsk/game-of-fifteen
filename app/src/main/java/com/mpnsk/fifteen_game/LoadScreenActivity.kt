package com.mpnsk.fifteen_game

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mpnsk.fifteen_game.databinding.ActivityLoadScreenBinding
import com.mpnsk.fifteen_game.lib.RemoteConfigManager
import com.mpnsk.fifteen_game.lib.RemoteConfigVariables
import com.mpnsk.fifteen_game.lib.UserValidator
import com.mpnsk.fifteen_game.lib.impl.FirebaseRemoteConfigManagerImpl
import com.mpnsk.fifteen_game.lib.impl.UserValidatorImpl


class LoadScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadScreenBinding
    private lateinit var userValidator: UserValidator
    private lateinit var remoteConfigManager: RemoteConfigManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        remoteConfigManager = FirebaseRemoteConfigManagerImpl()
        val remoteConfig = remoteConfigManager.configure()

        binding.progressBar.visibility = View.VISIBLE
        binding.loadingMsg.text =
            remoteConfig.getString(RemoteConfigVariables.LOADING_PHRASE_KEY)

        binding.privacyPolicyButton.setOnClickListener {
            var link = remoteConfig
                .getString(RemoteConfigVariables.PRIVACY_POLICY_LINK_KEY)
            if (!link.startsWith("http")) {
                link = "http://${link}"
            }
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(link))
            )
        }

        Thread {
            Thread.sleep(3000) // for demonstrating
            loadNextActivity()
        }.start()
    }

    private fun isValidUser(): Boolean {
        userValidator = UserValidatorImpl()
        return userValidator.isExistsAndActiveSim(this)
    }

    private fun loadNextActivity() {
        if (isValidUser()) {
            val remoteConfig = remoteConfigManager.getRemoteConfig()
            remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
                if (task.isSuccessful && isValidResult(remoteConfig)) {
                    startActivity(Intent(this, WebViewActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun isValidResult(remoteConfig: FirebaseRemoteConfig): Boolean {
        val notNullableConfigVars = mutableListOf(
            remoteConfig.getString(RemoteConfigVariables.MAIN_LINK_KEY),
            remoteConfig.getString(RemoteConfigVariables.PRIVACY_POLICY_LINK_KEY)
        )
        if ("" in notNullableConfigVars) {
            Log.w("RemoteConfigVariable", "Some of NotNull remote config variable is empty")
            return false
        }
        return true
    }
}