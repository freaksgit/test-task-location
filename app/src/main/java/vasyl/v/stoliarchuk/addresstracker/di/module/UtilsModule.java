package vasyl.v.stoliarchuk.addresstracker.di.module;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import vasyl.v.stoliarchuk.addresstracker.di.DiName;
import vasyl.v.stoliarchuk.addresstracker.util.DeviceUtils;

@Module
public class UtilsModule {

    @Singleton
    @Provides
    DeviceUtils provideDeviceUtils(@Named(DiName.CONTEXT_APP) Context context) {
        return new DeviceUtils(context);
    }
}
