package vasyl.v.stoliarchuk.addresstracker.di.component;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import vasyl.v.stoliarchuk.addresstracker.App;
import vasyl.v.stoliarchuk.addresstracker.di.module.AndroidModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AndroidModule.class})

public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(App application);

        AppComponent build();

    }

    void inject(App app);

}
