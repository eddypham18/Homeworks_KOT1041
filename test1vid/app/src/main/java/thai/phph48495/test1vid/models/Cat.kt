package thai.phph48495.test1vid.models

import com.google.gson.annotations.SerializedName

data class Cat (
    val id: String,
    val tags: List<String>,
    val name: String,
    val image: String
)