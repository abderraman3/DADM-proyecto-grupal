package dadm.jromsev.sportnew.di

import android.net.ConnectivityManager
import dadm.jromsev.sportnew.data.event.SportEventDataSource
import dadm.jromsev.sportnew.data.event.SportEventDataSourceImpl
import dadm.jromsev.sportnew.data.network.ConnectivityChecker
import dadm.jromsev.sportnew.domain.model.repository.SportEventRepository
import dadm.jromsev.sportnew.domain.model.repository.SportEventRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// Módulo de Dagger que proporciona las dependencias necesarias para obtener los datos de eventos deportivos y verificar la conectividad.
@Module
@InstallIn(SingletonComponent::class)
object SportEventProviderModule {

    // Proporciona la fuente de datos de eventos deportivos utilizando Retrofit para realizar solicitudes a la API.
    @Provides
    @Singleton
    fun provideSportEventDataSource(retrofit: Retrofit): SportEventDataSource {
        return SportEventDataSourceImpl(retrofit)
    }

    // Proporciona el verificador de conectividad, que utiliza el servicio ConnectivityManager para comprobar la conexión a internet.
    @Provides
    @Singleton
    fun provideConnectivityChecker(connectivityManager: ConnectivityManager): ConnectivityChecker {
        return ConnectivityChecker(connectivityManager)
    }
}