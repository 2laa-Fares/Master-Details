package alaa.ewis.masterdetails.data.offline

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import java.lang.IllegalStateException


/**
 * Database Generator class (used to handle connection to realm database).
 */
object DatabaseGenerator {
    private val localRealms = ThreadLocal<Realm>()

    /**
     * Opens a reference-counted local Realm instance.
     *
     * @return the open Realm instance
     */
    fun openLocalInstance(ctx: Context): Realm? {
        checkDefaultConfiguration(ctx)
        val realm = Realm.getDefaultInstance()
        if (localRealms.get() == null) {
            localRealms.set(realm)
        }
        return realm
    }

    /**
     * Closes local Realm instance, decrementing the reference count.
     *
     * @throws IllegalStateException if there is no open Realm.
     */
    fun closeLocalInstance(ctx: Context) {
        checkDefaultConfiguration(ctx)
        val realm = localRealms.get()
            ?: throw IllegalStateException(
                "Cannot close a Realm that is not open."
            )
        realm.close()
        // noinspection ConstantConditions
        if (Realm.getLocalInstanceCount(Realm.getDefaultConfiguration()!!) <= 0) {
            localRealms.set(null)
        }
    }

    private fun checkDefaultConfiguration(ctx: Context) {
        if (Realm.getDefaultConfiguration() == null) {
            Realm.init(ctx)
            val config = RealmConfiguration.Builder()
                .name("masterDetails.database")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
            Realm.setDefaultConfiguration(config)
        }
    }
}