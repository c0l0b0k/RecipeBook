package com.example.recipebook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel


class mealRecipe : ComponentActivity() {

    companion object {
        const val EXTRA_TEXT = "extra_text"
        const val API_URL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i="
    }
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedText = intent.getStringExtra(EXTRA_TEXT)

            // Создаем URL для запроса JSON с учетом категории
            val apiUrl = "$API_URL$receivedText"

            // Запускаем корутину для выполнения асинхронного запроса
            CoroutineScope(Dispatchers.IO).launch {
                // Выполняем запрос
                val jsonResult = makeApiCall(apiUrl)
                val mealsResponse= JSONObject(jsonResult)
                val jsonRecipe = mealsResponse.getJSONArray("meals")
                val recipe=jsonRecipe.getJSONObject(0)

                withContext(Dispatchers.Main) {
//

                    val recipe =parseJson(recipe)
                    // Используем Compose для отображения данных
                    setContent {
                        RecipeCard(recipe)
                        }
                    }
                }
            }
        }

    private fun makeApiCall(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }
    private fun parseJson(recipeJeson: JSONObject): Recipe{



            val strMeal = recipeJeson.getString("strMeal")
            val strMealThumb = recipeJeson.getString("strMealThumb")
            val instuctions = recipeJeson.getString("strInstructions")
            val id = recipeJeson.getString("idMeal")
            val recipe = Recipe(strMeal,  strMealThumb, instuctions,id)

        return recipe
    }
}
@Composable
fun RecipeCard(meal: Recipe,mainViewModel: MainViewModel = viewModel(factory = MainViewModel.factory)) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    // Обработка нажатия
                }
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = meal.name)
            Spacer(modifier = Modifier.height(8.dp))
            // Используйте Coil для загрузки изображения по URL
            Image(
                painter = rememberAsyncImagePainter(model = meal.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = meal.instructions)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    mainViewModel.id.value=meal.id
                    mainViewModel.newText.value=meal.name
                    mainViewModel.instructions.value=meal.instructions
                    mainViewModel.insertItem()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Download recipe")
            }
            // Остальные данные о блюде, такие как ингредиенты, измерения и т. д.
        }
    }
}


