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

@Module
@InstallIn(SingletonComponent::class)
object SportEventProviderModule {

    // Fornisce SportEventDataSource usando Retrofit
    @Provides
    @Singleton
    fun provideSportEventDataSource(retrofit: Retrofit): SportEventDataSource {
        return SportEventDataSourceImpl(retrofit)
    }

    // Fornisce ConnectivityChecker
    @Provides
    @Singleton
    fun provideConnectivityChecker(connectivityManager: ConnectivityManager): ConnectivityChecker {
        return ConnectivityChecker(connectivityManager)
    }
}