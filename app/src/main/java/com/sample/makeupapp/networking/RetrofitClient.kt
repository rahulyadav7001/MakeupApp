import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.makeupapp.networking.ProductServiceApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private const val TIME_OUT: Long = 120
    private const val BASE_URL: String = "https://makeup-api.herokuapp.com/api/v1/"

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()

    // retrofit instance for communication with API/server
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }


    val productServiceApi: ProductServiceApiInterface by lazy {
        retrofit.create(ProductServiceApiInterface::class.java)
    }


}
