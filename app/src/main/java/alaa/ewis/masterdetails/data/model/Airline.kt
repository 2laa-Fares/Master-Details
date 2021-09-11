package alaa.ewis.masterdetails.data.model

import io.realm.RealmObject

// Passenger Airline details.
open class Airline(
    var id: Int = 0,
    var name: String? = null,
    var country: String? = null,
    var logo: String? = null,
    var slogan: String? = null,
    var head_quaters: String? = null,
    var website: String? = null,
    var established: String? = null
):  RealmObject()