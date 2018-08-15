package vasyl.v.stoliarchuk.addresstracker;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import vasyl.v.stoliarchuk.addresstracker.di.component.DaggerAppComponent;

public class App extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent
                .builder()
                .application(this)
                .build();
    }
}
