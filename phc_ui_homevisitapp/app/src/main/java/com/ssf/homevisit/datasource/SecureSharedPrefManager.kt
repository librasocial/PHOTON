package com.ssf.homevisit.datasource

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssf.homevisit.models.AuthenticationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SecureSharedPrefManager @Inject constructor(@ApplicationContext val context: Context) {
    private lateinit var encryptedFile: EncryptedFile
    private val TAG = "SecureSharedPrefManager"
    private lateinit var sharedPreferences: SharedPreferences

    init {
        secureSharedPrefInit(context)
        Log.i(TAG, "init called")
    }


    fun saveAuthToken(authenticationResult: AuthenticationResult) {
        val gson = Gson()
        val autString = gson.toJson(authenticationResult)
        sharedPreferences.edit().putString("KEY_AUTH", autString).commit()
        Log.i(TAG, "Saved Auth")
    }

    fun getAuthToken(): AuthenticationResult? {
        val authByteArray = sharedPreferences.getString("KEY_AUTH", "")
        val gson = Gson()
        val type = object : TypeToken<AuthenticationResult?>() {}.type
        return gson.fromJson(authByteArray, type)
    }

    private fun secureSharedPrefInit(context: Context) {
        val sharedPrefsFile: String = "AUTH"
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = context.getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            // Edit the user's shared preferences...
            apply()
        }
    }

    fun clearAuthToken() {
        sharedPreferences.edit().clear().commit()
    }
}