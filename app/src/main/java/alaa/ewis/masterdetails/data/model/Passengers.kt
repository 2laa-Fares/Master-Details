package alaa.ewis.masterdetails.data.model

// Passengers List Response.
class Passengers (
    val totalPassengers: Int,
    val totalPages: Int,
    val data: List<Passenger>
)