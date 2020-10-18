package brut.androlib;

import android.app.Application;

public class ApplicationHolder {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    public static void setApplication(Application application) {
        ApplicationHolder.application = application;
    }
}
