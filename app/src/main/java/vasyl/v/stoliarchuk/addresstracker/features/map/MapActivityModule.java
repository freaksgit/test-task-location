package vasyl.v.stoliarchuk.addresstracker.features.map;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import vasyl.v.stoliarchuk.addresstracker.di.ActivityScope;

@Module
public abstract class MapActivityModule {

    @Binds
    abstract MapContract.View bindMapView(MapActivity mapActivity);

    @Provides
    @ActivityScope
    static MapContract.Presenter provideMapPresenter(MapContract.View mvpView) {
        return new MapPresenter(mvpView);
    }
}
