package vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.mapper;


import io.reactivex.functions.Function;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.entity.RetrofitAddress;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Address;

public class AddressFromRetrofitMapper implements Function<RetrofitAddress, Address> {
    @Override
    public Address apply(RetrofitAddress retrofitAddress) throws Exception {
        return new Address(
                retrofitAddress.getHouseNumber(),
                retrofitAddress.getRoad(),
                retrofitAddress.getNeighbourhood(),
                retrofitAddress.getSuburb(),
                retrofitAddress.getCounty(),
                retrofitAddress.getCity(),
                retrofitAddress.getPostcode(),
                retrofitAddress.getCountry(),
                retrofitAddress.getCountryCode());
    }
}
