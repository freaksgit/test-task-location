package vasyl.v.stoliarchuk.addresstracker.data.datasource.remote;

import io.reactivex.Maybe;
import vasyl.v.stoliarchuk.addresstracker.data.AddressDataSource;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.RestApi;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.mapper.PlaceFromRetrofitMapper;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Place;

public class RemoteAddressDataSource implements AddressDataSource {

    private final RestApi restApi;
    private final PlaceFromRetrofitMapper placeFromRetrofitMapper;

    public RemoteAddressDataSource(RestApi restApi, PlaceFromRetrofitMapper placeFromRetrofitMapper) {
        this.restApi = restApi;
        this.placeFromRetrofitMapper = placeFromRetrofitMapper;
    }

    @Override
    public Maybe<Place> getPlace(float lat, float lon) {
        return restApi.getPlace(lat, lon)
                .map(placeFromRetrofitMapper);
    }
}
