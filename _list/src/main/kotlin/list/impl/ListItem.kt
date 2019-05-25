package list.impl

import com.squareup.moshi.Json

data class ListItem(
  @field:Json(name = "poster_path")
  val posterPath: String?,
  @field:Json(name = "id")
  val id: Long?,
  @field:Json(name = "name")
  val name: String?)
