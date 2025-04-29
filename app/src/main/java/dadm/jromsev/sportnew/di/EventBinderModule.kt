package dadm.jromsev.sportnew.di

import dadm.jromsev.sportnew.data.event.SportEventDataSource
import dadm.jromsev.sportnew.data.event.SportEventDataSourceImpl
import dadm.jromsev.sportnew.domain.model.repository.SportEventRepository
import dadm.jromsev.sportnew.domain.model.repository.SportEventRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EventBinderModule {

    @Binds
    abstract fun bindSportEventRepository(
        impl: SportEventRepositoryImpl
    ): SportEventRepository
}