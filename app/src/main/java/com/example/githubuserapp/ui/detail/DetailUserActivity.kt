package com.example.githubuserapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.data.response.DetailUserResponse
import com.example.githubuserapp.data.database.FavoriteUser
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.example.githubuserapp.ui.detail.viewmodel.DetailUserViewModel
import com.example.githubuserapp.ui.helper.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailUserViewModel

    private var userID: Int = 0
    private var userName: String = ""
    private var avatarURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = intent.getIntExtra(EXTRA_ID, 0)
        userName = intent.getStringExtra(EXTRA_USER).toString()
        avatarURL = intent.getStringExtra(EXTRA_AVATAR_URL).toString()

        detailViewModel = obtainViewModel(this@DetailUserActivity)

        detailViewModel.findUserDetail(userName)
        detailViewModel.user.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackbarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        supportActionBar?.title = userName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        var isFavorite: Boolean
        var countFavoriteUser: Int

        val item = menu.findItem(R.id.action_favorite)
        CoroutineScope(Dispatchers.IO).launch {
            countFavoriteUser = detailViewModel.checkUser(userID)
            withContext(Dispatchers.Main) {
                isFavorite = countFavoriteUser <= 0
                if (isFavorite) {
                    item.setIcon(R.drawable.ic_favorite_border_24)
                } else {
                    item.setIcon(R.drawable.ic_favorite_white_24)
                }
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val link = resources.getString(SHARE_LINK) + userName
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, link)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share to: "))
            }
            R.id.action_favorite -> {
                val favoriteUser = FavoriteUser(userID, userName, avatarURL)

                var isFavorite: Boolean
                var countFavoriteUser: Int
                CoroutineScope(Dispatchers.IO).launch {
                    countFavoriteUser = detailViewModel.checkUser(userID)
                    withContext(Dispatchers.Main) {
                        isFavorite = countFavoriteUser <= 0
                        if (isFavorite) {
                            item.setIcon(R.drawable.ic_favorite_white_24)
                            detailViewModel.addToFavorite(favoriteUser)
                        } else {
                            item.setIcon(R.drawable.ic_favorite_border_24)
                            detailViewModel.removeFromFavorite(favoriteUser)
                        }
                    }
                }

            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDetailUser(detailUser: DetailUserResponse) {
        binding.apply {
            Glide.with(this@DetailUserActivity)
                .load(detailUser.avatarUrl)
                .into(imgDetailPhoto)

            txtDetailName.text = detailUser.name
            txtDetailLocation.text = detailUser.location
            txtDetailCompany.text = detailUser.company
            txtDetailRepository.text = detailUser.publicRepos.toString()
            txtDetailFollowers.text = detailUser.followers.toString()
            txtDetailFollowing.text = detailUser.following.toString()

            txtBio.text = detailUser.bio as CharSequence?
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.userName = userName
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    companion object {
        const val EXTRA_ID = "extra_ID"
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"

        private const val SHARE_LINK = R.string.share_link

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.repositoryUP,
            R.string.followersUP,
            R.string.followingUP
        )

    }
}