package com.example.newsapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_cache")

class UserDatastore(private val context: Context) {
    private val UID_KEY = stringPreferencesKey("user_uid")

    suspend fun saveUid(uid: String) {
        context.dataStore.edit { it[UID_KEY] = uid }
    }

    suspend fun getUid(): String? {
        return context.dataStore.data.map { it[UID_KEY] }.first()
    }

    suspend fun clearUid() {
        context.dataStore.edit { it.remove(UID_KEY) }
    }
}