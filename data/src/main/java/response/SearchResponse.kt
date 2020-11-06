package response


data class SearchResponse(val users: ArrayList<UserResponse>, val shows: ArrayList<ShowResponse>, val categories: ArrayList<CategoryResponse>)