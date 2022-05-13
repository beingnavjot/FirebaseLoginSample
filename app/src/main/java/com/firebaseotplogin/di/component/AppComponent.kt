package com.firebaseotplogin.di.component


import com.firebaseotplogin.app.MyApplication
import com.firebaseotplogin.di.modules.ApplicationModule
import com.firebaseotplogin.di.modules.ViewModules.ViewModelFactoryModule
import com.firebaseotplogin.di.modules.ViewModules.ViewModelModule
import com.firebaseotplogin.loginregister.view.OtpActivity
import com.firebaseotplogin.loginregister.view.SignInActivity

import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ApplicationModule::class,
      //  NetworkModule::class, ViewModelModule::class, ViewModelFactoryModule::class
    ]
)

interface AppComponent {

    fun inject(myApplication: MyApplication)
    fun inject(signInActivity: SignInActivity)
    fun inject(otpActivity: OtpActivity)


}