package comin.chaten.statens.caugfitten.servernendto;

import android.app.Application;
import android.support.multidex.MultiDex;


public class PortableCloudApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

}
