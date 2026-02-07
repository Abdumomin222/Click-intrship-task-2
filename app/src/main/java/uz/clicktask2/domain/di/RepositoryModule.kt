package uz.clicktask2.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.clicktask2.domain.repository.ProductRepository
import uz.clicktask2.domain.repository.impl.ProductRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindRepository(impl: ProductRepositoryImpl): ProductRepository
}