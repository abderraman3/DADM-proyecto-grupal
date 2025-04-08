package dadm.jromsev.sportnew.data.player

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

class ConnectivityChecker @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    fun isConnectionAvailable(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false
    }
}