package vasyl.v.stoliarchuk.addresstracker.di.module;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import vasyl.v.stoliarchuk.addresstracker.App;
import vasyl.v.stoliarchuk.addresstracker.di.ActivityScope;
import vasyl.v.stoliarchuk.addresstracker.di.DiName;
import vasyl.v.stoliarchuk.addresstracker.features.map.MapActivity;
import vasyl.v.stoliarchuk.addresstracker.features.map.MapActivityModule;

@Module
public abstract class AndroidModule {

    @Named(DiName.CONTEXT_APP)
    @Singleton
    @Binds
    abstract Context provideAppContext(App app);

    @ActivityScope
    @ContributesAndroidInjector(modules = MapActivityModule.class)
    abstract MapActivity mapActivityInjector();
}
