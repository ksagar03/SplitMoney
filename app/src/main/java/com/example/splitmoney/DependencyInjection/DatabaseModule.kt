package com.example.splitmoney.DependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.splitmoney.database.AppDatabase
import com.example.splitmoney.database.GroupDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "split-money.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun ProvideGroupDao(database: AppDatabase): GroupDao = database.groupDao()

    @Provides
    fun provideExpenseDao(database: AppDatabase) = database.expenseDao()
}



