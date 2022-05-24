package com.example.a12_ndatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.a12_ndatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val dataStore: DataStore<Preferences> by preferencesDataStore("userinfo")
    private val userName = stringPreferencesKey("name")
    //private val userAge = intPreferencesKey("age")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            //Save Data
            button.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    storeUserInfo(editText.text.toString())
                }
            }

            //Get Data
            lifecycle.coroutineScope.launchWhenCreated {
                showName().collect {
                    textView.text = it
                }
            }
        }
    }

    private suspend fun storeUserInfo(name: String) {
        dataStore.edit {
            it[userName] = name
        }
    }

    private fun showName() = dataStore.data.map {
        it[userName] ?:""
    }
}


