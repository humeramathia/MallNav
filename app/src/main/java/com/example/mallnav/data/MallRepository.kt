package com.example.mallnav.data

import com.example.mallnav.data.model.Mall
import com.example.mallnav.data.model.Store

interface MallRepository {
    fun getMalls(): List<Mall>
    fun getMall(id: String): Mall?
    fun getStores(mallId: String): List<Store>
    fun getStore(id: String): Store?
    fun getLandmarks(mallId: String): List<Store>
    fun getCategories(): List<String>
}
