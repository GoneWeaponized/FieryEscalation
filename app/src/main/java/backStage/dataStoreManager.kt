package backStage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    companion object {
        val SERVER_IP_KEY = stringPreferencesKey("server_ip")
        val YOU_YOU_ID_KEY = stringPreferencesKey("UUID")
        val YOUR_NAME = stringPreferencesKey("mein_name")
        val THEME_KEY = stringPreferencesKey("theme")
    }
    suspend fun saveIp(ip: String) {
        context.dataStore.edit { preferences ->
            preferences[SERVER_IP_KEY] = ip
        }
    }
    suspend fun saveIdMe(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[YOU_YOU_ID_KEY] = uid
        }
    }
    suspend fun saveNameMe(zovut: String) {
        context.dataStore.edit { preferences ->
            preferences[YOUR_NAME] = zovut
        }
    }
    suspend fun saveTheme(bTheme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = bTheme
        }
    }
    val getIp: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SERVER_IP_KEY] ?: "127.0.0.1" // Default value
    }
    val getMyId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[YOU_YOU_ID_KEY] ?: ""
    }
    val getMyZovut: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[YOUR_NAME] ?: "Default"
    }
    val getThemeRn: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "https://tiles.openfreemap.org/styles/fiord"
    }
}