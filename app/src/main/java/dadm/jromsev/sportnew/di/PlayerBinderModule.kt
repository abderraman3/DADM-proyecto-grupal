package dadm.jromsev.sportnew.di

import dadm.jromsev.sportnew.data.player.PlayerDataSource
import dadm.jromsev.sportnew.data.player.PlayerDataSourceImpl
import dadm.jromsev.sportnew.data.player.PlayerRepository
import dadm.jromsev.sportnew.data.player.PlayerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerBinderModule {

    @Binds
    abstract fun bindPlayerRepository(
        impl: PlayerRepositoryImpl
    ): PlayerRepository

    @Binds
    abstract fun bindPlayerDataSource(
        impl: PlayerDataSourceImpl
    ): PlayerDataSource
}