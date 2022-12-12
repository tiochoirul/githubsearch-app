package com.example.githubuserapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.FragmentFollowersBinding
import com.example.githubuserapp.ui.detail.viewmodel.FollowingViewModel
import com.google.android.material.snackbar.Snackbar

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private val followingViewModel by viewModels<FollowingViewModel>()

    private lateinit var rvList : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFollowersBinding.bind(view)

        rvList = binding.rvFollowersList
        rvList.setHasFixedSize(true)

        rvList.layoutManager = LinearLayoutManager(context)

        val userName = arguments?.getString(ARG_USER_NAME,"")

        followingViewModel.getFollowers(userName.toString())
        followingViewModel.following.observe(viewLifecycleOwner) { followingList ->
            setFollowerList(followingList)
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followingViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(
                    view.rootView,
                    snackbarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setFollowerList(followingList: ArrayList<ItemsItem>) {
        val listUserDataAdapter = ListUserDataAdapter(followingList)
        rvList.adapter = listUserDataAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        const val ARG_USER_NAME = "user_name"
    }
}