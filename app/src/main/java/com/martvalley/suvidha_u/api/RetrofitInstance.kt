package com.martvalley.suvidha_u.api

import com.martvalley.suvidha_u.MainApplication
import com.martvalley.suvidha_u.utils.Constants
import com.martvalley.suvidha_u.utils.SharedPref
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {

        val apiService: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient())
                .build()
                .create(ApiService::class.java)
        }


        private fun okClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }
}

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Bearer " + SharedPref(MainApplication.appContext).getValueString(Constants.AUTH_KEY)
            )
        val newRequest = currentRequest.build()
        return chain.proceed(newRequest)
    }
}