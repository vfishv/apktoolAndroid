package apktool.android.com;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    private static App app;

    public static final App getInstance(){
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public String getApkToolVersion(){
        return getResources().getString(R.string.apktool_version);
    }
}
