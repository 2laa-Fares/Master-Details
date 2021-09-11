package alaa.ewis.masterdetails.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Service Generator class (used to handle connection to server).
 */
object ServiceGenerator {
    private var okHttpClient: OkHttpClient? = null

    /**
     * connect to server.
     *
     * @return Retrofit object.
     */
    fun getClient(): Retrofit? {
        if (okHttpClient == null) initOkHttp()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient!!)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * used to init OkHttpClient if it's null.
     */
    private fun initOkHttp() {
        val requestTimeout = 60
        val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
            .connectTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        })

        okHttpClient = httpClient.build()
    }
}