package alaa.ewis.masterdetails.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

// Passenger details.
open class Passenger (
    @PrimaryKey
    var _id: String? = null,
    var name: String? = null,
    var trips: Int = 0,
    var airline: RealmList<Airline>? = null
): RealmObject()