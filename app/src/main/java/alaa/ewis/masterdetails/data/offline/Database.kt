package alaa.ewis.masterdetails.data.offline

import alaa.ewis.masterdetails.data.model.Passenger
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

//Initialisation for Realm
fun initRealm(context: Context) {
    Realm.init(context)
    val config = RealmConfiguration.Builder()
        .name("masterDetails.database")
        .allowQueriesOnUiThread(true)
        .schemaVersion(1)
        .deleteRealmIfMigrationNeeded()
        .build()
    Realm.setDefaultConfiguration(config)
}

// Get passenger list from Realm.
fun getDatabasePassengers(context: Context): List<Passenger> {
    Realm.init(context)
    val realm = Realm.getDefaultInstance()
    val passengers: List<Passenger> = realm.where(Passenger::class.java).findAll()
    return passengers
}

// Get passenger data by id from Realm.
fun getDatabasePassengerData(context: Context, passengerID: String): Passenger? {
    Realm.init(context)
    val realm = Realm.getDefaultInstance()
    val passengerData: Passenger? = realm.where(Passenger::class.java).equalTo("_id", passengerID).findFirst()
    return passengerData
}

// Save passenger list into Realm.
fun saveDatabasePassengers(context: Context, passengersList: List<Passenger>) {
    deletePassengers(context)
    Realm.init(context)
    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    for (passenger in passengersList) {
        //realm.createObject(Passenger::class.java, passenger)
        realm.copyToRealm(passenger)
    }
    realm.commitTransaction()
}

// Save passenger data into Realm.
fun postDatabasePassenger(context: Context, passenger: Passenger) {
    Realm.init(context)
    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    realm.copyToRealm(passenger)
    realm.commitTransaction()
}

// Delete passengers data from Realm.
fun deletePassengers(context: Context) {
    Realm.init(context)
    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    val passengers = realm.where(Passenger::class.java).findAll()
    passengers?.deleteAllFromRealm()
    realm.commitTransaction()
}