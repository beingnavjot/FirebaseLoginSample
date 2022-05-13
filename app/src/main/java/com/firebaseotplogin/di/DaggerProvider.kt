package com.firebaseotplogin.di

import com.firebaseotplogin.app.MyApplication
import com.firebaseotplogin.di.component.AppComponent
import com.firebaseotplogin.di.component.DaggerAppComponent

import com.firebaseotplogin.di.modules.ApplicationModule


class DaggerProvider {

    companion object {
        private var appComponent: AppComponent? = null

        fun initComponent(application: MyApplication) {
            appComponent = DaggerAppComponent.builder()
                .applicationModule(ApplicationModule(application))
                .build()
        }

        fun getAppComponent(): AppComponent? {
            return appComponent
        }

    }
}