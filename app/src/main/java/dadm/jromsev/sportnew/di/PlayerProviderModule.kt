package dadm.jromsev.sportnew.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import dadm.jromsev.sportnew.database.AppDatabase
import dadm.jromsev.sportnew.database.PlayerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// Módulo de Dagger que proporciona las dependencias necesarias para la conexión a la red, la base de datos y el acceso a los datos de jugadores.
@Module
@InstallIn(SingletonComponent::class)
class PlayerProviderModule {
    // Proporciona el servicio de conectividad para verificar la conexión a Internet.
    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    // Proporciona una instancia de Retrofit configurada para hacer solicitudes HTTP a la API.
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.thesportsdb.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    // Proporciona la base de datos Room para almacenar los datos locales de los jugadores.
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "players_db"
        ).build()
    }

    // Proporciona el acceso a la base de datos a través del DAO de jugadores.
    @Provides
    fun providePlayerDao(database: AppDatabase): PlayerDao {
        return database.playerDao()
    }
}