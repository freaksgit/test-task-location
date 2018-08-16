package vasyl.v.stoliarchuk.addresstracker.di.component;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import vasyl.v.stoliarchuk.addresstracker.App;
import vasyl.v.stoliarchuk.addresstracker.di.module.AndroidModule;
import vasyl.v.stoliarchuk.addresstracker.di.module.DataModule;
import vasyl.v.stoliarchuk.addresstracker.di.module.NetworkModule;
import vasyl.v.stoliarchuk.addresstracker.di.module.UtilsModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AndroidModule.class,
        DataModule.class,
        NetworkModule.class,
        UtilsModule.class})

public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(App application);

        AppComponent build();

    }

    void inject(App app);

}
