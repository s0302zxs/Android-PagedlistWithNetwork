package com.giles.android.pagedlistwithnetwork.data.model

import com.google.gson.annotations.SerializedName

data class UserDetail(@field:SerializedName("avatar_url")
                      val avatarUrl: String?,
                      @field:SerializedName("name")
                      val name: String,
                      @field:SerializedName("bio")
                      val bio: String?,
                      @field:SerializedName("login")
                      val login: String,
                      @field:SerializedName("site_admin")
                      val siteAdmin: Boolean,
                      @field:SerializedName("location")
                      val location: String?,
                      @field:SerializedName("blog")
                      val blog: String?){
}