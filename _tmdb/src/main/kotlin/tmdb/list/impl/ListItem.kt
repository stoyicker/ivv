package tmdb.list.impl

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ListItem(
    @field:Json(name = "poster_path")
    val posterPath: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?) {
  override fun equals(other: Any?) = other is ListItem && id == other.id

  override fun hashCode() = id?.hashCode() ?: 0
}
