package vasyl.v.stoliarchuk.addresstracker.data;

import io.reactivex.Maybe;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Place;

public interface AddressDataSource {
    Maybe<Place> getPlace(double lat, double lon);
}
