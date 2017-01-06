package by.mylnikov.transport.model


class RouteRecord(val routeName: String, val number: String,
                  val timeDeparture: String, val timeArrival: String,
                  var regularity: String, val timeOnWay: String)