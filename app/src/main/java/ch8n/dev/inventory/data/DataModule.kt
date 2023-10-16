package ch8n.dev.inventory.data

import android.content.Context
import ch8n.dev.inventory.data.database.firestore.RemoteDatabase
import ch8n.dev.inventory.data.database.roomdb.AppDatabase

object DataModule {

    fun provideRemoteDatabase(): RemoteDatabase {
        return RemoteDatabase()
    }

    fun provideLocalDatabase(context: Context): AppDatabase {
        return AppDatabase.build(context)
    }

    object Injector {

        private var _appContext: Context? = null
        fun provideAppContext(appContext: Context) {
            _appContext = appContext
        }

        val appContext get() = requireNotNull(_appContext)
        val localDatabase by lazy { provideLocalDatabase(appContext) }
        val remoteDatabase by lazy { provideRemoteDatabase() }

    }
}