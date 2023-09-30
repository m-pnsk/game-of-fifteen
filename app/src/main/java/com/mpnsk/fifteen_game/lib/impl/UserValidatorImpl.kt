package com.mpnsk.fifteen_game.lib.impl

import android.content.Context
import android.telephony.TelephonyManager
import com.mpnsk.fifteen_game.lib.UserValidator

class UserValidatorImpl : UserValidator {
    override fun isExistsAndActiveSim(context: Context): Boolean {
        return try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val isStateReady = telephonyManager.simState == TelephonyManager.SIM_STATE_READY
            isStateReady
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}