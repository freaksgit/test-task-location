package vasyl.v.stoliarchuk.addresstracker.data;

import io.reactivex.Maybe;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Place;

public class AddressRepository implements AddressDataSource {

    private final AddressDataSource remoteAddressDataSource;

    public AddressRepository(AddressDataSource remoteAddressDataSource) {
        this.remoteAddressDataSource = remoteAddressDataSource;
    }

    @Override
    public Maybe<Place> getPlace(float lat, float lon) {
        return remoteAddressDataSource.getPlace(lat, lon);
    }
}
