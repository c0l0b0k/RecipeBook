package com.example.recipebook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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


class RecipesOneGategoria : ComponentActivity(){
    companion object {
        const val EXTRA_TEXT = "extra_text"
        const val API_URL = "https://www.themealdb.com/api/json/v1/1/filter.php?c="
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Получаем текст из Intent
            val receivedText = intent.getStringExtra(EXTRA_TEXT)

            // Создаем URL для запроса JSON с учетом категории
            val apiUrl = "$API_URL$receivedText"

            // Запускаем корутину для выполнения асинхронного запроса
            CoroutineScope(Dispatchers.IO).launch {
                // Выполняем запрос
                val jsonResult = makeApiCall(apiUrl)

                // Обработка полученных данных в основном потоке
                withContext(Dispatchers.Main) {
//                    val mealsResponse = Json { ignoreUnknownKeys = true }.decodeFromString<MealsResponse>(jsonResult)
                    val mealsResponse= JSONObject(jsonResult)
                    val mealsJeson = mealsResponse.getJSONArray("meals")
                    val meals=parseJsonArray(mealsJeson)
                    // Используем Compose для отображения данных
                    setContent {
                        LazyColumn {
                            items(meals) { meal ->
                                MealItem(meal)
                            }
                        }
                    }
                }
            }
        }
    }

    // Функция для выполнения HTTP-запроса
    private fun makeApiCall(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }
    private fun parseJsonArray(jsonArray: JSONArray): List<Meal> {
        val mealsList = mutableListOf<Meal>()

        for (i in 0 until jsonArray.length()) {
            val mealObject = jsonArray.getJSONObject(i)
            val strMeal = mealObject.getString("strMeal")
            val strMealThumb = mealObject.getString("strMealThumb")
            val idMeal = mealObject.getString("idMeal")

            val meal = Meal(strMeal,  strMealThumb, idMeal)
            mealsList.add(meal)
        }

        return mealsList
    }


}
@Composable
private fun MealItem(meal: Meal) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    val intent = Intent(context,mealRecipe::class.java).apply {
                        putExtra(mealRecipe.EXTRA_TEXT, meal.id)
                    }
                    context.startActivity(intent)
                }
                .padding(16.dp)
        ) {
            Text(text = meal.name)
            Spacer(modifier = Modifier.height(8.dp))
//             Используйте Coil для загрузки изображения по URL
            Image(
                painter = rememberAsyncImagePainter(model = meal.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
           

        }
    }
}