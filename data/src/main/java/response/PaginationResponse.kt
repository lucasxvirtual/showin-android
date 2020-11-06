package response

data class PaginationResponse <T> (val count : Int?,
                                   val next : String?,
                                   val previous : String?,
                                   val results : List<T>)