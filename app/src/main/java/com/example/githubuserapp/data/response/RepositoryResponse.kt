package com.example.githubuserapp.data.response

import com.google.gson.annotations.SerializedName

data class RepositoryResponseItem(

	@field:SerializedName("private")
	val jsonMemberPrivate: Boolean,

	@field:SerializedName("full_name")
	val fullName: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("node_id")
	val nodeId: String
)
