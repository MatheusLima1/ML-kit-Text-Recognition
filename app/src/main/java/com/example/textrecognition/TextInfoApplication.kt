package com.example.textrecognition

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.textrecognition.db.database.AppDataBase
import com.facebook.stetho.Stetho
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import java.util.*

class TextInfoApplication: Application() {
    companion object {
        val UUID_USER = stringPreferencesKey("uuid_user")
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        var database: AppDataBase? = null
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDataBase::class.java, "my-db").allowMainThreadQueries().build()

        //Stetho
        if (BuildConfig.DEBUG) {
            val initializerBuilder = Stetho.newInitializerBuilder(this)
            initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
            val initializer = initializerBuilder.build()
            Stetho.initialize(initializer)
        }
    }

    fun verifyUserId() {
        scope.launch {
            if (getUserUuid().isEmpty()) {
                withContext(Dispatchers.IO) {
                    storeUuidUser()
                }
            }
        }
    }

    suspend fun storeUuidUser() {
        dataStore.edit { settings ->
            settings[UUID_USER] = UUID.randomUUID().toString()
        }
    }

    fun getUserUuid(): String {
        return dataStore.data.map { preferences ->
            preferences[UUID_USER] ?: ""
        }.toString()
    }

}