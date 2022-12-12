package com.example.githubuserapp.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.R
import com.example.githubuserapp.data.database.FavoriteUser
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.databinding.ActivityFavoriteListBinding
import com.example.githubuserapp.ui.detail.DetailUserActivity
import com.example.githubuserapp.ui.helper.ViewModelFactory
import com.example.githubuserapp.ui.main.ListUserAdapter

class FavoriteListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteListBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var rvFavorite: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel = obtainViewModel(this@FavoriteListActivity)

        rvFavorite = binding.rvFavoriteList
        rvFavorite.setHasFixedSize(true)

        rvFavorite.layoutManager = if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 3)
        } else {
            LinearLayoutManager(this)
        }

        favoriteViewModel.getFavoriteUser().observe(this) { favoriteList ->
            setFavoriteUserList(favoriteList)
        }

        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setFavoriteUserList(userList: List<FavoriteUser>) {
        val listUser = ArrayList<ItemsItem>()
        for (user in userList) {
            val mapUser = ItemsItem(user.avatarUrl, "", "","",user.id, user.login,"","")
            listUser.add(mapUser)
        }

        val listUserAdapter = ListUserAdapter(listUser)
        rvFavorite.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int, userName: String, avatar: String) {
                val moveWithObjectIntent = Intent(this@FavoriteListActivity, DetailUserActivity::class.java)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_ID, id)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_USER, userName)
                moveWithObjectIntent.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, avatar)
                startActivity(moveWithObjectIntent)
            }
        })
    }
}