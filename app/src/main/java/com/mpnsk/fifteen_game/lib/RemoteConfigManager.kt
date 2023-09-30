package com.mpnsk.fifteen_game.lib

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

interface RemoteConfigManager {
    fun configure() : FirebaseRemoteConfig
    fun getRemoteConfig() : FirebaseRemoteConfig
}