package com.fappslab.myais.remote.network.retrofit

import com.fappslab.myais.remote.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

private const val TAG = "OkHttp"
private const val READ_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 10L
private const val CONNECT_TIMEOUT = 15L

internal class RetrofitClient(
    private val baseUrl: String,
    private val interceptors: List<Interceptor>
) {

    fun create(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient())
            .build()
    }

    private fun okhttpClient(): OkHttpClient {
        val trustManager = createTrustManager()
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)

        val builder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .sslSocketFactory(sslContext.socketFactory, trustManager)

        return interceptors.fold(builder) { acc, interceptor ->
            acc.addInterceptor(interceptor)
        }.build()
    }

    private val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
        if (BuildConfig.DEBUG) Timber.tag(TAG).d(message)
    }.apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor.Level.NONE
    }

    private fun createTrustManager(): X509TrustManager {
        val trustManagerFactory = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        if (trustManagers.isEmpty() || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers: ${trustManagers.contentToString()}")
        }
        return trustManagers[0] as X509TrustManager
    }
}
