package com.mpnsk.fifteen_game.lib.impl

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mpnsk.fifteen_game.R
import com.mpnsk.fifteen_game.lib.RemoteConfigManager

class FirebaseRemoteConfigManagerImpl : RemoteConfigManager {
    private val remoteConfig = Firebase.remoteConfig

    override fun configure() : FirebaseRemoteConfig {
        remoteConfig.setDefaultsAsync(R.xml.default_remote_condig)
        remoteConfig.activate()
        val configSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = 60
            minimumFetchIntervalInSeconds = 30
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        return remoteConfig
    }

    override fun getRemoteConfig(): FirebaseRemoteConfig {
        return remoteConfig
    }
}