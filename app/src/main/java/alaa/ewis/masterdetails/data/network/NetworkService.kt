package alaa.ewis.masterdetails.data.network

import alaa.ewis.masterdetails.data.model.*
import retrofit2.Call
import retrofit2.http.*

// Rest API base url.
const val BASE_URL = "https://api.instantwebtools.net/v1/"

interface NetworkService {
    // Used to get passenger data.
    @GET("passenger")
    fun getPassengersList(@Query("page") page: Int, @Query("size") size: Int): Call<Passengers>

    // Read single passenger data by passenger ID.
    @GET("passenger/{id}")
    fun getPassenger(@Path("id") id: String): Call<Passenger>

    // Read All Airlines.
    @GET("airlines")
    fun getAirlines(): Call<List<Airline>>

    // Add new passenger.
    @POST("passenger")
    fun postPassenger(@Body passenger: PassengerRequest): Call<Passenger>


    // Edit passenger data.
    @PUT("passenger/{id}")
    fun putPassenger(@Path("id") id: String, @Body passenger: PassengerRequest): Call<PassengerEditResponse>
}