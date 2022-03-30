package com.example.square1taskapp.di


import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.example.square1taskapp.data.local.CitiesDatabase
import com.example.square1taskapp.util.Constants.BASE_URL
import com.example.square1taskapp.data.remote.CitiesApi
import com.example.square1taskapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun providesCitiesApi(): CitiesApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CitiesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CitiesDatabase {

        return Room.databaseBuilder(context, CitiesDatabase::class.java, Constants.CITIES_DATABASE).build()
    }

    /*@OptIn(ExperimentalPagingApi::class)
    @Singleton
    fun providesCitiesRepository(api: CitiesApi, database: CitiesDatabase): Repository{
        return Repository(api, database)
    }*/

}