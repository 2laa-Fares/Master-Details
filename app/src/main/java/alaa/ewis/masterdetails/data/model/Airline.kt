package alaa.ewis.masterdetails.data.model

import io.realm.RealmObject
import java.math.BigInteger

// Passenger Airline details.
open class Airline(
    var id: String? = null,
    var name: String? = null,
    var country: String? = null,
    var logo: String? = null,
    var slogan: String? = null,
    var head_quaters: String? = null,
    var website: String? = null,
    var established: String? = null
):  RealmObject()