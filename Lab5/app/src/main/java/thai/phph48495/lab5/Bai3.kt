package thai.phph48495.lab5

import androidx.arch.core.internal.SafeIterableMap.SupportRemove
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Bai3(nav: NavController){
    val categories = listOf("Fiction", "Mystery", "Science","Fantasy","Horror","Romance","Thriller","Westerns","Biographies","Autobiographies")
    val suggestions = listOf("Biography", "Cookbook", "Poetry", "Self-help", "Thriller")
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    Column (modifier = Modifier.padding(16.dp)) {
        Text("Choose a category", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        AssistChip(
            onClick = {  },
            label = { Text("Need help?") },
            modifier = Modifier.padding(end = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CategoryChips(categories, selectedCategories, onCategorySelected = {
            category -> selectedCategories = if(selectedCategories.contains(category)){
                selectedCategories - category } else {
                selectedCategories + category
                }
        })

        Spacer(modifier = Modifier.height(16.dp))

        SuggestionChips(suggestions, selectedCategories, onSuggestionSelected = {
            suggestion -> selectedCategories = selectedCategories + suggestion
        })

        if(selectedCategories.isNotEmpty()){
            Spacer(modifier = Modifier.height(16.dp))
            SelectedCategoriesChips(selectedCategories, onCategoryRemoved = {
                category -> selectedCategories = selectedCategories - category
            })

            Spacer(modifier = Modifier.height(4.dp))

            AssistChip(
                onClick = {selectedCategories = setOf()},
                label = { Text("Clear all", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black)
            )
        }
    }

}


@Composable
fun CategoryChips(
    categories: List<String>,
    selectedCategory: Set<String>,
    onCategorySelected: (String) -> Unit
){
    Text(text = "Choose category", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Row (
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ){
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategory.contains(category),
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}


@Composable
fun SuggestionChips(
    suggestions: List<String>,
    selectedCategory: Set<String>,
    onSuggestionSelected: (String) -> Unit
){
    Text(text = "Suggestions", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    Row (
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ){
        suggestions.forEach { suggestion ->
            val isSelected = selectedCategory.contains(suggestion)
            val chipColors = if(isSelected){
                SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Color.LightGray
                )
            } else {
                SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Color.Transparent
                )
            }

            SuggestionChip(
                onClick = { onSuggestionSelected(suggestion) },
                colors = chipColors,
                label = { Text(suggestion) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun SelectedCategoriesChips(
    selectedCategories: Set<String>,
    onCategoryRemoved: (String) -> Unit
){
    Text("Selected categories", style = MaterialTheme.typography.titleMedium)
    Row (
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        selectedCategories.forEach {selectedCategory ->
            InputChip(
                selected = true,
                onClick = {},
                label = { Text(selectedCategory) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Deselect",
                        modifier = Modifier.clickable {
                            onCategoryRemoved(selectedCategory)
                        }.size(18.dp)
                    )
                },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}