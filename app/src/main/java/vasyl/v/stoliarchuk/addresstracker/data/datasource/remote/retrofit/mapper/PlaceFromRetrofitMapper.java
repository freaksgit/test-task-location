package vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.mapper;

import io.reactivex.functions.Function;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.entity.RetrofitAddress;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.entity.RetrofitPlace;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Address;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Place;

public class PlaceFromRetrofitMapper implements Function<RetrofitPlace, Place> {

    private final Function<RetrofitAddress, Address> addressFromRetrofitMapper;

    public PlaceFromRetrofitMapper(Function<RetrofitAddress, Address> addressFromRetrofitMapper) {
        this.addressFromRetrofitMapper = addressFromRetrofitMapper;
    }

    @Override
    public Place apply(RetrofitPlace retrofitPlace) throws Exception {
        return new Place(
                retrofitPlace.getPlaceId(),
                retrofitPlace.getLicence(),
                retrofitPlace.getOsmType(),
                retrofitPlace.getOsmId(),
                retrofitPlace.getLat(),
                retrofitPlace.getLon(),
                retrofitPlace.getDisplayName(),
                retrofitPlace.getAddress() == null ? null : addressFromRetrofitMapper.apply(retrofitPlace.getAddress()),
                retrofitPlace.getBoundingbox());
    }
}
