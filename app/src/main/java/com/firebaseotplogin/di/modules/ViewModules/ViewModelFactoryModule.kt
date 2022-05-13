package com.firebaseotplogin.di.modules.ViewModules

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firebaseotplogin.di.modules.ViewModules.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton


@Module
    class ViewModelFactoryModule {

        @Provides
        fun viewModelFactory(providerMap: Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory {
            return ViewModelFactory(providerMap)
        }
    }



