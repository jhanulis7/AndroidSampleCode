package com.my.samplecode.kakaologin

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        // https://developers.kakao.com/console/app/779354 나의 샘플앱의 앱키, local.properties 에 정의함.(외부노출 git ignore)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}