package com.example.recipebook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipebook.ui.theme.RecipeBookTheme


import androidx.compose.ui.unit.dp




class Categories : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val listOfItems = listOf(
                "Beef",
                "Breakfast",
                "Chicken",
                "Dessert",
                "Goat",
                "Lamb",
                "Miscellaneous",
                "Pasta",
                "Pork",
                "Seafood",
                "Side",
                "Starter",
                "Vegan",
                "Vegetarian"
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                items(listOfItems) { item ->
                    RecipeListItem(item)
                }
            }

        }
    }
}


@Composable
private fun RecipeListItem(categoria: String) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth().padding(10.dp).height(40.dp).clickable {
            val intent = Intent(context, RecipesOneGategoria::class.java).apply {
                putExtra(RecipesOneGategoria.EXTRA_TEXT, categoria)
            }
            context.startActivity(intent)
        },
        shape = RoundedCornerShape(15.dp),
    ) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()) {
            Text(text = categoria)
        }
    }
}