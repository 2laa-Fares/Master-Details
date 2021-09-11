package alaa.ewis.masterdetails.data.model

// Body used in add/edit passenger request.
data class PassengerRequest(val name: String, val trips: Int, val airline: Int)