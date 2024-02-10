package com.pavelshelkovenko.shelkovenko.di


import com.pavelshelkovenko.shelkovenko.data.FilmMapper
import com.pavelshelkovenko.shelkovenko.data.local.AppDatabase
import com.pavelshelkovenko.shelkovenko.data.local.CacheDao
import com.pavelshelkovenko.shelkovenko.data.local.FavoriteFilmDao
import com.pavelshelkovenko.shelkovenko.data.remote.ApiService
import com.pavelshelkovenko.shelkovenko.data.repository.FavoriteFilmsRepositoryImpl
import com.pavelshelkovenko.shelkovenko.data.repository.FilmsRepositoryImpl
import com.pavelshelkovenko.shelkovenko.domain.FavoriteFilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.FilmsRepository
import com.pavelshelkovenko.shelkovenko.domain.GetPopularFilmsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PopularFilmsModule {

    @Provides
    @ViewModelScoped
    fun provideCacheDao(db: AppDatabase): CacheDao {
        return db.cacheDao()
    }

    @Provides
    @ViewModelScoped
    fun provideFavoriteFilmDao(db: AppDatabase): FavoriteFilmDao {
        return db.favoriteFilmDao()
    }

    @Provides
    @ViewModelScoped
    fun provideGetPopularFilmsUseCase(
        filmsRepository: FilmsRepository,
        favoriteFilmsRepository: FavoriteFilmsRepository
    ): GetPopularFilmsUseCase {
        return GetPopularFilmsUseCase(
            filmsRepository = filmsRepository,
            favoriteFilmsRepository = favoriteFilmsRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideFavoriteFilmsRepository(
        mapper: FilmMapper,
        favoriteFilmDao: FavoriteFilmDao
    ): FavoriteFilmsRepository {
        return FavoriteFilmsRepositoryImpl(
            mapper = mapper,
            favoriteFilmDao = favoriteFilmDao
        )
    }

    @Provides
    @ViewModelScoped
    fun provideFilmsRepository(
        cacheDao: CacheDao,
        mapper: FilmMapper,
        apiService: ApiService
    ): FilmsRepository {
        return FilmsRepositoryImpl(
            apiService = apiService,
            cacheDao = cacheDao,
            mapper = mapper
        )
    }

}