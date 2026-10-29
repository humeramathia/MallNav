package com.example.mallnav.data

import com.example.mallnav.R
import com.example.mallnav.data.model.Mall
import com.example.mallnav.data.model.Store

object MockMallRepository : MallRepository {
    private val malls = listOf(
        Mall("canal-walk", "Canal Walk", "Cape Town", R.color.hero_olive, R.color.olive_container, R.drawable.ic_mall, 12),
        Mall("demo-mall", "Demo Mall", "Cape Town", R.color.hero_blush, R.color.blush_container, R.drawable.ic_mall, 8),
        Mall("va-waterfront", "V&A Waterfront", "Cape Town", R.color.hero_terracotta, R.color.terracotta_container, R.drawable.ic_mall, 10),
        Mall("sandton-city", "Sandton City", "Johannesburg", R.color.hero_sage, R.color.sage_container, R.drawable.ic_mall, 9),
    )

    private val stores = listOf(
        store("nike", "canal-walk", "Nike", "Fashion", "Level 2", "214", "Sportswear, footwear and accessories."),
        store("clicks", "canal-walk", "Clicks", "Beauty", "Level 1", "108", "Beauty, pharmacy and everyday essentials.", true),
        store("woolworths", "canal-walk", "Woolworths", "Food", "Level 1", "101", "Food, clothing and homeware.", true),
        store("mr-price", "canal-walk", "Mr Price", "Fashion", "Level 1", "122", "Affordable fashion and home.", true),
        store("cotton-on", "canal-walk", "Cotton On", "Fashion", "Level 1", "130", "Casual clothing and accessories."),
        store("starbucks", "canal-walk", "Starbucks", "Food", "Level 1", "140", "Coffee, tea and light bites.", true),
        store("checkers", "canal-walk", "Checkers", "Food", "Level 1", "150", "Groceries and household goods."),
        store("game-world", "canal-walk", "Game World", "Electronics", "Level 2", "220", "Games, gadgets and entertainment."),
        store("sport-zone", "canal-walk", "Sport Zone", "Sports", "Level 2", "232", "Sporting gear and activewear."),
        store("urban-threads", "canal-walk", "Urban Threads", "Fashion", "Level 2", "241", "Streetwear and denim."),
        store("info-desk", "canal-walk", "Information Desk", "Services", "Level 1", "A1", "Ask for help finding a store.", true),
        store("entrance-a", "canal-walk", "Entrance A", "Services", "Level 1", "E1", "Main parking entrance.", true),
        store("bathrooms-l1", "canal-walk", "Bathrooms", "Services", "Level 1", "F1", "Public restrooms near Woolworths.", true),
        store("food-court", "canal-walk", "Food Court", "Food", "Level 2", "200", "Casual dining and takeaway."),
        store("demo-nike", "demo-mall", "Nike", "Fashion", "Level 2", "12", "Sportswear, footwear and accessories."),
        store("demo-clicks", "demo-mall", "Clicks", "Beauty", "Level 1", "04", "Beauty and pharmacy.", true),
        store("va-nike", "va-waterfront", "Nike", "Fashion", "Level 1", "18", "Sportswear on the waterfront."),
        store("va-starbucks", "va-waterfront", "Starbucks", "Food", "Level 1", "06", "Harbour-side coffee.", true),
        store("sandton-woolworths", "sandton-city", "Woolworths", "Food", "Level 1", "09", "Flagship food and fashion.", true),
        store("sandton-game", "sandton-city", "Game World", "Electronics", "Level 2", "21", "Electronics and entertainment."),
    )

    override fun getMalls(): List<Mall> = malls

    override fun getMall(id: String): Mall? = malls.find { it.id == id }

    override fun getStores(mallId: String): List<Store> = stores.filter { it.mallId == mallId }

    override fun getStore(id: String): Store? = stores.find { it.id == id }

    override fun getLandmarks(mallId: String): List<Store> =
        getStores(mallId).filter { it.isLandmark }

    override fun getCategories(): List<String> =
        listOf("All", "Fashion", "Food", "Beauty", "Electronics", "Sports", "Services")

    private fun store(
        id: String,
        mallId: String,
        name: String,
        category: String,
        floor: String,
        unit: String,
        description: String,
        landmark: Boolean = false,
    ) = Store(id, mallId, name, category, floor, unit, description, landmark)
}
