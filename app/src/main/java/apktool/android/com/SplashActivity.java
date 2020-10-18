package apktool.android.com;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import apktool.android.com.util.PermissionsChecker;
import apktool.android.com.util.SupportVersion;


public class SplashActivity extends AppCompatActivity {

    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private static final String TAG = SplashActivity.class.getSimpleName();

    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORATE = 1;//读写
    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private boolean isRequireCheck; // 是否需要系统权限检测, 防止和系统提示框重叠

    private static final int DELAY_MILLIS = 500;
    private static final String EXTRA_PERMISSIONS = "extra_permission"; // 权限参数

    private static final Handler handler= new Handler();

    public static final String EXTRA_GO_WHALE = "isGoWhale";
    private boolean isGoWhale = false;

    /**
     * 去主页
     * @param activity
     * @param requestCode
     * @param permissions
     */
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra(EXTRA_GO_WHALE, true);
        //intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    /**
     * 只展示一下
     * @param activity
     */
    public static void startActivity(Activity activity) {
        if(activity instanceof SplashActivity){
            return;
        }
        if (activity != null) {
            Intent intent = new Intent(activity, SplashActivity.class);
            intent.putExtra(EXTRA_GO_WHALE, false);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView splashName = findViewById(R.id.splashName);
        if (splashName != null) {
            splashName.setText(getString(R.string.app_name) + "\n\n" + getString(R.string.app_site));
        }


        /*
        int retCode = com.google.android.gms.common.GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        boolean isGooglePlayServicesAvailable = com.google.android.gms.common.ConnectionResult.SUCCESS == retCode;
        com.google.firebase.perf.FirebasePerformance.getInstance().setPerformanceCollectionEnabled(isGooglePlayServicesAvailable);
        if (!isGooglePlayServicesAvailable && Config.DEBUG) {
            if (com.google.android.gms.common.ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED == retCode) {
                //Need to update
            } else if (com.google.android.gms.common.ConnectionResult.SERVICE_DISABLED == retCode) {
                // is disabled
            } else {

            }
            com.google.android.gms.common.GoogleApiAvailability.getInstance().getErrorDialog(this, retCode, 0).show();
        }
        */

        Intent intent = getIntent();
        if (intent != null) {
            isGoWhale = intent.getBooleanExtra(EXTRA_GO_WHALE, true);
        }

//        Utility.getScreenSize(this);
//        com.jakewharton.threetenabp.AndroidThreeTen.init(getApplication());
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .build();
//        ImageLoader.getInstance().init(config);

//        Intent intent = getIntent();
//        if (intent != null) {
//
//        }

        /*
        if (SupportVersion.Lollipop()) {
//            Intent intent = new Intent();
//            intent.setClass(this, MyJobService.class);
//            startService(intent);
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

            if (jobScheduler != null) {
                JobInfo.Builder jb = new JobInfo.Builder(1, new ComponentName(getPackageName(), MyJobService.class.getName()));

                boolean isPersisted = BaseFileUtil.checkReceiveBootCompleted(this);
                //Log.e(TAG, "checkReceiveBootCompleted: " + isPersisted);
                if(isPersisted){
                    jb.setPersisted(isPersisted);
                }
                //.setRequiresCharging(false) //是否在充电时执行
                //.setRequiresDeviceIdle(false) //是否在空闲时执行
                jb.setPeriodic(2000)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();
                JobInfo jobInfo = jb.build();
                try {
                    jobScheduler.schedule(jobInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //jobScheduler.cancelAll();
            }
        }
        */

        mPermissionsChecker = new PermissionsChecker(this);
        isRequireCheck = true;

    }

    @Override
    protected void onResume() {
        // 缺少权限时, 进入权限配置页面
        /**/
        boolean hasPermission = false;
        if(SupportVersion.M())
        {
            /*
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            }
            else
            {
                requestPermission();
            }
            */
            if (isRequireCheck) {
                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                    requestPermissions(PERMISSIONS); // 请求权限
                } else {
                    hasPermission = true;
                    allPermissionsGranted(); // 全部权限都已获取
                    super.onResume();
                    return;
                }
            } else {
                isRequireCheck = true;
            }
        }
        else
        {
            hasPermission = true;
        }
        if (hasPermission)
        {
            if (isGoWhale && DELAY_MILLIS > 0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goWhale();
                    }
                }, DELAY_MILLIS);
            } else {
                goWhale();
            }
        }


        super.onResume();
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        if (DELAY_MILLIS > 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goWhale();
                }
            }, DELAY_MILLIS);
        } else {
            goWhale();
        }
    }

    private void goWhale()
    {
        if (isGoWhale) {
            Intent intent = new Intent(this, DecoderActivity.class);
            startActivity(intent);
        }
        finish();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    protected void requestPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORATE);

        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
            //showMissingPermissionDialog();
            goWhale();// TODO 为了绿色应用公约
        }

        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORATE:
                //TODO
                Log.e(TAG, "WRITE_EXTERNAL_STORATE ");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    goWhale();

                } else {
                    // Permission Denied
                    //showMissingPermissionDialog();
                    goWhale();// TODO 为了绿色应用公约
                    //Toast.makeText(this, "WRITE_EXTERNAL_STORATE Permission is Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

        builder.setMessage(R.string.permission_help_text);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });

        builder.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.show();
    }
}
