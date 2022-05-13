package com.firebaseotplogin.di.modules




import com.firebaseotplogin.app.MyApplication
import com.firebaseotplogin.utils.Util

import com.google.gson.Gson
import com.google.gson.GsonBuilder


import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApplication) {

    // region Global Use
    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .create()
    }

 /*   @Singleton
    @Provides
    fun provideErrorProvider(): ErrorProvider {
        return ErrorProviderImpl(application.applicationContext)
    }*/

    @Singleton
    @Provides
    fun provideUtil(): Util = Util(application.applicationContext)

/*    @Singleton
    @Provides
    fun PrefUtils(): PrefUtils = PrefUtils(application.applicationContext)*/




}