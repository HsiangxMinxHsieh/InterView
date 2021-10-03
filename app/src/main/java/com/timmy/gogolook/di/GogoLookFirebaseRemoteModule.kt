package com.timmy.gogolook.di

import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.timmy.gogolook.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


// 感覺遙控行數比較好玩 XD
const val constantKeySpanCount: String = "GridLayoutSpanCount"

// 題目要求是要設定List或Grid,OK,fine。
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
        // Set the default values locally
        setDefaultsAsync(R.xml.remote_config_defaults)
        fetchAndActivate().addOnCompleteListener { task ->
//            Timber.e("成功與否是=>${task.isSuccessful}")
            if (task.isSuccessful) {
//                Timber.e("成功接到值！=>${task.isSuccessful}")
                liveIsListLayout.postValue(getBoolean(constantKeyIsList))
            }
        }
    }

    val liveIsListLayout by lazy { MutableLiveData<Boolean>() }


    fun getIsListLayout() = isListLayout

    fun getIsGridSpanCount() = spanCount
}

private var isListLayout: Boolean = true
private var spanCount: Int = 0
//
//    @Singleton
//    @Provides
//    fun provideRemoteBody(repository: FirebaseRemoteConfig): FirebaseRemoteConfig {
//        val firebaseRemoteConfigRepository = provideRemote()
////        firebaseRemoteConfigRepository.init()
//        return firebaseRemoteConfigRepository
//    }

//    private val instance = FirebaseRemoteConfig.getInstance()
//    fun init() {
//        instance.apply {
//            Timber.e("執行記錄點=>001")
//            setConfigSettingsAsync(
//                FirebaseRemoteConfigSettings.Builder()
//                    .setMinimumFetchIntervalInSeconds(1) //這個值並不是說在App內經過每這段時間就會更新，而是取到這個值後，下次取值前要經過這麼多的時間(秒)才會更新(實測結果)
//                    .build()
//            )
//            Timber.e("執行記錄點=>002")
//            setDefaultsAsync(R.xml.remote_config_defaults)
//
//            fetchAndActivate().addOnCompleteListener { task ->
//                Timber.e("成功與否是=>${task.isSuccessful}")
//                if (task.isSuccessful) {
//                    isListLayout = getBoolean(constantKeyIsList)
//                    Timber.e("取到的 isListLayout是=>$isListLayout")
//                    spanCount = getLong(constantKeySpanCount).toInt()
//                }
//            }
//        }
//    }
//



