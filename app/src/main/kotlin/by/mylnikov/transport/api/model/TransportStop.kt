package by.mylnikov.transport.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class TransportStop(@SerializedName("title_ru") val stopName: String,
                    @SerializedName("point_key") val stopId: String,
                    @SerializedName("full_title_ru") val stopFullName: String,
                    @SerializedName("ttype") val transportType: String,
                    @SerializedName("country_id") val countryId: Int) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(stopName)
        out.writeString(stopId)
        out.writeString(stopFullName)
        out.writeString(transportType)
        out.writeInt(countryId)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<TransportStop> = object : Parcelable.Creator<TransportStop> {
            override fun createFromParcel(parcel: Parcel): TransportStop {
                return TransportStop(parcel)
            }

            override fun newArray(size: Int): Array<TransportStop?> {
                return arrayOfNulls(size)
            }
        }
    }

    private constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(),
            parcel.readString(), parcel.readString(), parcel.readInt()) {
    }

}