package com.timmy.gogolook.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.timmy.gogolook.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


//感覺遙控行數比較好玩 XD
const val constantKeySpanCount: String = "GridLayoutSpanCount"

//但題目要求是要設定List或Grid,OK,fine。
const val constantKeyIsList: String = "IsListLayoutOrGridLayout"

@Module
@InstallIn(ApplicationComponent::class)
object DIRemoteModule {

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
        setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1) //這個值並不是說在App內經過每這段時間就會更新，而是取到這個值後，下次取值前要經過這麼多的時間(秒)才會更新(實測結果)
                .build()
        )
        //Set the default values locally //設定預設值
//       setDefaultsAsync(R.xml.remote_config_defaults)
        setDefaultsAsync(HashMap<String, Any>().apply { //使用統一的key感覺比較好改。
            this[constantKeySpanCount] = 2
            this[constantKeyIsList] = true
        })
    }
}



