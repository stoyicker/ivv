package list.impl

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RefreshResponse constructor(
    @field:Json(name = "page")
    val page: Int?,
    @field:Json(name = "results")
    val results: List<ListItem>,
    @field:Json(name = "total_results")
    val totalResults: Int?,
    @field:Json(name = "total_pages")
    val totalPages: Int?)
