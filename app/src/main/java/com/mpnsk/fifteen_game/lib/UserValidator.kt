package com.mpnsk.fifteen_game.lib

import android.content.Context

interface UserValidator {
    fun isExistsAndActiveSim(context: Context): Boolean
}