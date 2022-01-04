package com.example.textrecognition.view.list_recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecognition.R
import com.example.textrecognition.TextInfoApplication
import com.example.textrecognition.TextInfoApplication.Companion.dataStore
import kotlinx.android.synthetic.main.activity_main.*

class ListRecipeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_recipe)

        recyclerView = findViewById(R.id.recycler_recipe)

        fillRecipeList()
    }

    private fun fillRecipeList() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TextInfoApplication.database?.textInfoDao()?.getAllText()
            ?.let { ListRecipeAdapter(it) }
    }
}