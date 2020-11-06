package response

import com.google.gson.annotations.SerializedName

data class ConstantsResponse(
    @SerializedName("age_rating") val ageRating : List<AgeRatingResponse>,
    val timezones : List<String>?,
    val config : ConfigResponse,
    val packs : List<PackResponse>,
    val brands : List<BrandResponse>,
    val categories : List<CategoryResponse>,
    val genders : List<GenderResponse>,
    val show_durations : List<ShowDurationResponse>
)