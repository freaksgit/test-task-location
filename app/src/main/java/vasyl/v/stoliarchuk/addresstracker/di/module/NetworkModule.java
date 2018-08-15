package vasyl.v.stoliarchuk.addresstracker.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vasyl.v.stoliarchuk.addresstracker.BuildConfig;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.RestApi;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.interceptor.RequestInterceptor;

@Module
public class NetworkModule {

    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, RequestInterceptor requestInterceptor) {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }
        httpClientBuilder.addInterceptor(requestInterceptor);
        return httpClientBuilder.build();
    }


    @Provides
    RequestInterceptor provideRequestInterceptor() {
        return new RequestInterceptor();
    }

    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(RestApi.RestConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    @Singleton
    @Provides
    RestApi provideDevdroidRestApi(Retrofit retrofit) {
        return retrofit.create(RestApi.class);
    }


}
