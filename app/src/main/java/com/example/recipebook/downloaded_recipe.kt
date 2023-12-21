package com.example.recipebook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipebook.ui.theme.RecipeBookTheme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipebook.MainViewModel
import com.example.recipebook.data.NameEntity


class downloaded_recipe : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeListItem()

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeListItem(mainViewModel: MainViewModel = viewModel(factory = MainViewModel.factory)) {
    val itemsList = mainViewModel.itemsList.collectAsState(initial = emptyList())



        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(itemsList.value) { item ->
                ListItem(
                    item, {
                        mainViewModel.nameEntity = it
                        mainViewModel.newText.value = it.name
                        mainViewModel.instructions.value = it.instructions
                    },
                    {
                        mainViewModel.deleteItem(it)
                    }
                )
            }
        }

}

@Composable
fun ListItem(
    item: NameEntity,
    onClick: (NameEntity) -> Unit,
    onClickDelete: (NameEntity) -> Unit
) {
    var isExpanded by remember{
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                onClick(item)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(
                item.name,

            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onClickDelete(item)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Delete")
            }
            Text(
                modifier = Modifier.clickable{
                      isExpanded=!isExpanded
                },
                maxLines =if(isExpanded) Int.MAX_VALUE else 5,
                text=item.instructions
            )

        }
    }
}