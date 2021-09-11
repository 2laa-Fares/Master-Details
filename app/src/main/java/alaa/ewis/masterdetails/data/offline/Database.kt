package alaa.ewis.masterdetails.data.offline

import alaa.ewis.masterdetails.data.model.Airline
import alaa.ewis.masterdetails.data.model.Passenger
import alaa.ewis.masterdetails.data.model.PassengerRequest
import alaa.ewis.masterdetails.data.offline.DatabaseGenerator.openLocalInstance
import alaa.ewis.masterdetails.data.offline.DatabaseGenerator.closeLocalInstance
import android.content.Context
import io.realm.Realm

// Get passenger list from Realm.
fun getDatabasePassengers(ctx: Context): List<Passenger> {
    val realm: Realm = openLocalInstance(ctx)!!
    val passengers: List<Passenger> = realm.where(Passenger::class.java).findAll()
    return passengers
}

// Get passenger data by id from Realm.
fun getDatabasePassengerData(ctx: Context, passengerID: String): Passenger? {
    val realm: Realm = openLocalInstance(ctx)!!
    val passengerData: Passenger? = realm.where(Passenger::class.java).equalTo("_id", passengerID).findFirst()
    return passengerData
}

// Save passenger list into Realm.
fun saveDatabasePassengers(ctx: Context, passengersList: List<Passenger>) {
    val realm: Realm = openLocalInstance(ctx)!!
    deletePassengers(realm)
    realm.beginTransaction()
    for (passenger in passengersList) {
        //realm.createObject(Passenger::class.java, passenger)
        realm.copyToRealm(passenger)
    }
    realm.commitTransaction()
    closeLocalInstance(ctx)
}

// Save passenger data into Realm.
fun postDatabasePassenger(ctx: Context, passenger: Passenger) {
    val realm: Realm = openLocalInstance(ctx)!!
    realm.beginTransaction()
    realm.copyToRealm(passenger)
    realm.commitTransaction()
    closeLocalInstance(ctx)
}

// Edit passenger data into Realm.
fun putDatabasePassenger(ctx: Context, passengerReq: PassengerRequest, passengerID: String) {
    val realm: Realm = openLocalInstance(ctx)!!
    val airline: Airline? =  getAirlineData(realm, passengerReq.airline.toString())
    realm.use { r ->
        r.executeTransaction { realm: Realm ->
            val passenger: Passenger? =
                realm.where(Passenger::class.java).equalTo("_id", passengerID).findFirst()
            if (passenger != null) {
                passenger.name = passengerReq.name
                passenger.trips = passengerReq.trips
                if(airline != null && passenger.airline!!.size > 0){
                    passenger.airline!![0] = airline
                }
            }
        }
    }
    closeLocalInstance(ctx)
}

// Delete passengers data from Realm.
fun deletePassengers(realm: Realm) {
    realm.beginTransaction()
    val passengers = realm.where(Passenger::class.java).findAll()
    passengers?.deleteAllFromRealm()
    realm.commitTransaction()
}

// Get airline list from Realm.
fun getDatabaseAirlineList(ctx: Context): List<Airline> {
    val realm: Realm = openLocalInstance(ctx)!!
    val airlinesList: List<Airline> = realm.where(Airline::class.java).distinct("id").findAll()
    return airlinesList
}

// Save airline list into Realm.
fun saveDatabaseAirlineList(ctx: Context, airlineList: List<Airline>) {
    val realm: Realm = openLocalInstance(ctx)!!
    deleteAirline(realm)
    realm.beginTransaction()
    for (airline in airlineList) {
        realm.copyToRealm(airline)
    }
    realm.commitTransaction()
    closeLocalInstance(ctx)
}

// Delete airlines data from Realm.
fun deleteAirline(realm: Realm) {
    realm.beginTransaction()
    val airlineList = realm.where(Airline::class.java).findAll()
    airlineList?.deleteAllFromRealm()
    realm.commitTransaction()
}

// Get airline data.
fun getAirlineData(realm: Realm, airlineId: String): Airline? {
    val airline: Airline? = realm.where(Airline::class.java).equalTo("id", airlineId).findFirst()
    return airline
}