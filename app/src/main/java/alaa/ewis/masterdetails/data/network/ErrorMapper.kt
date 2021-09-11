package alaa.ewis.masterdetails.data.network

import alaa.ewis.masterdetails.R

class ErrorMapper{

    fun getErrorString(errorId: Int): String {
        return errorsMap.getValue(errorId)
    }

    private val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(403, getErrorString(R.string.forbidden)),
            Pair(404, getErrorString(R.string.bad_url_error)),
            Pair(500, getErrorString(R.string.server_error)),
            Pair(503, getErrorString(R.string.service_unavailable)),
            Pair(504, getErrorString(R.string.gateway_timeout)),
        ).withDefault { getErrorString(R.string.network_error) }
}
