package by.mylnikov.transport.api.model

import com.google.gson.annotations.SerializedName


class SuggestionsResponse(@SerializedName("total_found") val total: Int,
                          @SerializedName("suggests") val suggestions: List<TransportStop>,
                          @SerializedName("has_exact_match") val exactMatch: Boolean) {

    fun getBusSuggestions(): List<TransportStop> {
        return suggestions.filter { it.countryId == 149 && (it.transportType == "bus" || it.transportType == "") }
    }
}