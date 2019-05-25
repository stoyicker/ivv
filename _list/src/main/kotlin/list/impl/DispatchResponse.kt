package list.impl

import com.squareup.moshi.Json

internal class RefreshResponse private constructor(
    @field:Json(name = "page")
  val page: Long?,
    @field:Json(name = "results")
  val results: List<ListItem>,
    @field:Json(name = "total_results")
  val totalResults: Long?,
    @field:Json(name = "total_pages")
  val totalPages: Long?)
