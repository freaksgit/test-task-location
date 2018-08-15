package vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.entity.RetrofitPlace;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Place;

import static vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.RestApi.RestConfig.GET_PLACE;

public interface RestApi {
    @GET(GET_PLACE)
    Maybe<RetrofitPlace> getPlace(@Query(value = "lat") float lat, @Query(value = "lon") float lon);

    interface RestConfig {
        String BASE_URL = "https://nominatim.openstreetmap.org";
        String GET_PLACE = "/reverse.php";
    }
}
