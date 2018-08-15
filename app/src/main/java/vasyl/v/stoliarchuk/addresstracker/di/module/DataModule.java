package vasyl.v.stoliarchuk.addresstracker.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import vasyl.v.stoliarchuk.addresstracker.data.AddressDataSource;
import vasyl.v.stoliarchuk.addresstracker.data.AddressRepository;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.RemoteAddressDataSource;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.RestApi;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.mapper.AddressFromRetrofitMapper;
import vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.mapper.PlaceFromRetrofitMapper;
import vasyl.v.stoliarchuk.addresstracker.di.DiName;

@Module
public class DataModule {

    @Singleton
    @Provides
    @Named(DiName.REPOSITORY)
    AddressDataSource provideAddressRepository(@Named(DiName.REMOTE) AddressDataSource remoteAddressDataSource) {
        return new AddressRepository(remoteAddressDataSource);
    }

    @Singleton
    @Provides
    @Named(DiName.REMOTE)
    AddressDataSource provideRemoteAddressDataSource(RestApi restApi, PlaceFromRetrofitMapper placeFromRetrofitMapper) {
        return new RemoteAddressDataSource(restApi, placeFromRetrofitMapper);
    }

    @Singleton
    @Provides
    PlaceFromRetrofitMapper providePlaceFromRetrofitMapper(AddressFromRetrofitMapper addressFromRetrofitMapper) {
        return new PlaceFromRetrofitMapper(addressFromRetrofitMapper);
    }

    @Singleton
    @Provides
    AddressFromRetrofitMapper provideAddressFromRetrofitMapper() {
        return new AddressFromRetrofitMapper();
    }
}
