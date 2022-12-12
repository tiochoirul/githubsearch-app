package com.example.githubuserapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapp.R
import com.example.githubuserapp.data.response.RepositoryResponseItem
import com.example.githubuserapp.databinding.FragmentReposBinding
import com.example.githubuserapp.ui.detail.viewmodel.ReposViewModel
import com.google.android.material.snackbar.Snackbar


class ReposFragment : Fragment() {

    private lateinit var binding: FragmentReposBinding
    private val reposViewModel by viewModels<ReposViewModel>()

    private lateinit var rvRepos: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReposBinding.bind(view)

        rvRepos = binding.rvRepositoryList
        rvRepos.setHasFixedSize(true)

        rvRepos.layoutManager = LinearLayoutManager(activity)

        val userName = arguments?.getString(ARG_USER_NAME, "")

        reposViewModel.getRepository(userName.toString())
        reposViewModel.repository.observe(viewLifecycleOwner) { reposList ->
            setReposList(reposList)
        }

        reposViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        reposViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackbarText ->
                Snackbar.make(
                    view.rootView,
                    snackbarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setReposList(repos: ArrayList<RepositoryResponseItem>) {
        val listReposAdapter = ListReposAdapter(repos)
        rvRepos.adapter = listReposAdapter
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