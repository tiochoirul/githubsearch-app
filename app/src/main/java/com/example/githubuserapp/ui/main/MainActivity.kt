package com.example.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.ui.detail.DetailUserActivity
import com.example.githubuserapp.ui.favorite.FavoriteListActivity
import com.example.githubuserapp.ui.setting.PreferenceViewModelFactory
import com.example.githubuserapp.ui.setting.SettingPreferences
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var rvUser: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUser = binding.rvUserList
        rvUser.setHasFixedSize(true)

        rvUser.layoutManager = if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 3)
        } else {
            LinearLayoutManager(this)
        }

        supportActionBar?.hide()

        setThemeMode()

        mainViewModel.getUsers()
        mainViewModel.listUser.observe(this) { userList ->
            getUserList(userList)
        }

        binding.edtSearch.setOnKeyListener { _, key, event ->
            if (event.action == KeyEvent.ACTION_DOWN && key == KeyEvent.KEYCODE_ENTER) {
                searchUser()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.btnListFavorite.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteListActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.isLoading.observe(this) {
            it.getContentIfNotHandled()?.let { isLoading ->
                showLoading(isLoading)
            }
        }

        mainViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackbarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun searchUser() {
        val query = binding.edtSearch.text.toString()
        showLoading(true)
        mainViewModel.findUser(query)
        mainViewModel.listUser.observe(this) { userList ->
            setUserList(userList)
        }
    }

    private fun setUserList(userList: List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (user in userList) {
            listUser.add(user)
        }

        val listUserAdapter = ListUserAdapter(listUser)
        rvUser.adapter = listUserAdapter


        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int, userName: String, avatar: String) {
                val moveWithObjectIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_ID, id)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_USER, userName)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, avatar)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    private fun getUserList(userList: List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (user in userList) {
            listUser.add(user)
        }

        val listUserAdapter = ListUserAdapter(listUser)
        rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int, userName: String, avatar: String) {
                val moveWithObjectIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_ID, id)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_USER, userName)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, avatar)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    private fun setThemeMode() {
        val switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(
            this,
            PreferenceViewModelFactory(pref)
        )[MainViewModel::class.java]

        mainViewModel.getThemeSetting().observe(this
        ) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}