package apktool.android.com.util;

import android.os.Build;

/**
 * Util class to check if the current device is running some of the awesome Android versions.
 *
 * Created by Quentin Zhang o(*￣︶￣*)o on 2018-7-16.
 *
 * http://androiddoc.qiniudn.com/reference/android/os/Build.VERSION_CODES.html
 * http://www.umindex.com/devices/android_os
 *
 */
public class SupportVersion {

    /*
http://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels

Android 12	                31	S
Android 11	                30	R
Android 10	                29	Q
Android 9	                28	P
Android 8.1	                27	O_MR1
Android 8.0	                26	O
Android 7.1.1
Android 7.1	                25	N_MR1
Android 7.0	                24	N
Android 6.0 	            23 	M 	API Changes
Android 5.1 	            22 	LOLLIPOP_MR1
Android 5.0 	            21 	LOLLIPOP
Android 4.4W 	            20 	KITKAT_WATCH 	KitKat for Wearables Only
Android 4.4 	            19 	KITKAT
Android 4.3 	            18 	JELLY_BEAN_MR2
Android 4.2, 4.2.2 	        17 	JELLY_BEAN_MR1
Android 4.1, 4.1.1 	        16 	JELLY_BEAN
Android 4.0.3, 4.0.4 	    15 	ICE_CREAM_SANDWICH_MR1
Android 4.0, 4.0.1, 4.0.2 	14 	ICE_CREAM_SANDWICH
Android 3.2 	            13 	HONEYCOMB_MR2
Android 3.1.x 	            12 	HONEYCOMB_MR1
Android 3.0.x 	            11 	HONEYCOMB
Android 2.3.4
Android 2.3.3 	            10 	GINGERBREAD_MR1
Android 2.3.2
Android 2.3.1
Android 2.3 	            9 	GINGERBREAD
Android 2.2.x 	            8 	FROYO
Android 2.1.x 	            7 	ECLAIR_MR1
Android 2.0.1 	            6 	ECLAIR_0_1
Android 2.0 	            5 	ECLAIR
Android 1.6 	            4 	DONUT
Android 1.5 	            3 	CUPCAKE
Android 1.1 	            2 	BASE_1_1
Android 1.0 	            1 	BASE
     */

    public static String versionName(int version)
    {
        String versionName = "Android ";
        switch (version) {
            case 0:
            case 1:
                versionName += "1.0";
                break;
            case 2:
                versionName += "1.1";
                break;
            case 3:
                versionName += "1.5";
                break;
            case 4:
                versionName += "1.6";
                break;
            case 5:
                versionName += "2.0";
                break;
            case 6:
                versionName += "2.0.1";
                break;
            case 7:
                versionName += "2.1.x";
                break;
            case 8:
                versionName += "2.2.x";
                break;
            case 9:
                versionName += "2.3";
                break;
            case 10:
                versionName += "2.3.3";
                break;
            case 11:
                versionName += "3.0.x";
                break;
            case 12:
                versionName += "3.1.x";
                break;
            case 13:
                versionName += "3.2";
                break;
            case 14:
                versionName += "4.0";
                break;
            case 15:
                versionName += "4.0.3";
                break;
            case 16:
                versionName += "4.1";
                break;
            case 17:
                versionName += "4.2";
                break;
            case 18:
                versionName += "4.3";
                break;
            case 19:
                versionName += "4.4";
                break;
            case 20:
                versionName += "4.4W";
                break;
            case 21:
                versionName += "5.0";
                break;
            case 22:
                versionName += "5.1";
                break;
            case 23:
                versionName += "6.0";
                break;
            case 24:
                versionName += "7.0";
                break;
            case 25:
                versionName += "7.1";
                break;
            case 26:
                versionName += "8.0";
                break;
            case 27:
                versionName += "8.1";
                break;
            case 28:
                versionName += "9";
                break;
            case 29:
                versionName += "10";
                break;
            case 30:
                versionName += "11";
                break;

        }
        return versionName;
    }

    /**
     * @return true when the caller API version is at least Cupcake 3
     */
    public static boolean Cupcake() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE;
    }

    /**
     * @return true when the caller API version is at least Donut 4
     */
    public static boolean Donut() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
    }

    /**
     * @return true when the caller API version is at least Eclair 5
     */
    public static boolean Eclair() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }

    /**
     * @return true when the caller API version is at least Froyo 8
     */
    public static boolean Froyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * @return true when the caller API version is at least GingerBread 9
     */
    public static boolean Gingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * @return true when the caller API version is at least Honeycomb 11 (Android 3.0)
     */
    public static boolean Honeycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * @return true when the caller API version is at least Honeycomb 3.2, 13
     */
    public static boolean HoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * @return true when the caller API version is at least ICS 14 (Android 4.0)
     */
    public static boolean IceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * @return true when the caller API version is at least ICS 15 (Android 4.0.3)
     */
    public static boolean IceCreamSandwichMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    /**
     * @return true when the caller API version is at least JellyBean 16
     */
    public static boolean JellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * @return true when the caller API version is at least JellyBean MR1 17
     */
    public static boolean JellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * @return true when the caller API version is at least JellyBean MR2 18
     */
    public static boolean JellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * @return true when the caller API version is at least Kitkat 19 (Android 4.4)
     */
    public static boolean Kitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


    /**
     * @return true when the caller API version is at least KITKAT_WATCH 20
     */
    public static boolean KitkatWatch() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH;
    }

    /**
     * @return true when the caller API version is at least Lollipop 21
     */
    public static boolean Lollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * @return true when the caller API version is at least Lollipop 22
     */
    public static boolean LollipopMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }



    /**
     * @return true when the caller API version is at least Marshmallow 23 (Android 6.0)
     *
     * http://androiddoc.qiniudn.com/sdk/api_diff/23/changes.html
     * http://androiddoc.qiniudn.com/preview/features/runtime-permissions.html
     */
    public static boolean M() {
        return Build.VERSION.SDK_INT >= 23;// Build.VERSION_CODES.M
    }

    public static boolean Marshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;// Build.VERSION_CODES.M
    }

    public static boolean N() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;// 24
    }

    public static boolean N_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;// 25
    }

    public static boolean equalsN_MR1(){
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1;// 25 7.1
    }

    public static boolean O() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;// 26
    }

    public static boolean O_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;// 27
    }

    public static boolean P() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;// 28
    }

    public static boolean Q() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;// 29
    }

    public static boolean R() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;// 30
    }

    public static boolean S() {
        return Build.VERSION.SDK_INT >= 31;
    }

}