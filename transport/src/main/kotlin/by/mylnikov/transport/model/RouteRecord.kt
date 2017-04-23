package by.mylnikov.transport.model


class RouteRecord(val routeName: String, val number: String,
                  val timeDeparture: String, val timeArrival: String,
                  var regularity: String, val timeOnWay: String) : Comparable<RouteRecord> {
    override fun compareTo(other: RouteRecord): Int {
        val i = timeDeparture.compareTo(other.timeDeparture)
        if (i != 0) {
            return i
        } else {
            return timeArrival.compareTo(other.timeArrival)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as RouteRecord?

        return routeName == other.routeName &&
                number == other.number &&
                timeDeparture == other.timeDeparture &&
                timeArrival == other.timeArrival &&
                regularity == other.regularity &&
                timeOnWay == other.timeOnWay
    }

    override fun hashCode(): Int {
        var result = routeName.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + timeDeparture.hashCode()
        result = 31 * result + timeArrival.hashCode()
        result = 31 * result + regularity.hashCode()
        result = 31 * result + timeOnWay.hashCode()
        return result
    }

    override fun toString(): String {
        return "RouteRecord(routeName='$routeName', number='$number', timeDeparture='$timeDeparture', timeArrival='$timeArrival', regularity='$regularity', timeOnWay='$timeOnWay')"
    }


}