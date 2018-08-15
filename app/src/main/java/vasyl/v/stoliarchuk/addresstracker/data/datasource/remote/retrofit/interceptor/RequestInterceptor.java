package vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {
    private static final String QUERY_PARAM_FORMAT_NAME = "format";
    private static final String QUERY_PARAM_FORMAT_VALUE = "json";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder().addQueryParameter(QUERY_PARAM_FORMAT_NAME, QUERY_PARAM_FORMAT_VALUE).build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
