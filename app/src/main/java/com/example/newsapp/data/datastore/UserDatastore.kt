package com.example.newsapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_cache")

@Singleton
class UserDatastore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val UID_KEY = stringPreferencesKey("user_uid")  // ← created once
    }

    suspend fun saveUid(uid: String) {
        context.dataStore.edit { it[UID_KEY] = uid }
    }

    suspend fun getUid(): String? {
        return context.dataStore.data.map { it[UID_KEY] }.first()
    }

}