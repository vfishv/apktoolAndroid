package apktool.android.com.util;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.security.NetworkSecurityPolicy;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.Vector;

public final class Utility
{
	private Utility()
	{
	}

	public static final String TAG = "Utility";
	public static boolean cancelLogin = false;
	private static NotificationManager mNotiManager;
	private static Notification mStatusNotify;
	private static PendingIntent contentIntent = null;	// 点击通知后的intent
	
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static boolean isDoPlayerSound(Context context)
	{
		boolean isDoPlayerSound = false;
		if( getAudioManagerMode(context)==Notification.DEFAULT_SOUND || getAudioManagerMode(context)==Notification.DEFAULT_ALL)
			isDoPlayerSound = true;
		return isDoPlayerSound;
	}
	
	/**
	 * 获取当前的系统情景模式设置
	 * @param context
	 * @return
	 */
	public static int getAudioManagerMode(Context context)
	{
		try
		{
			AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (am != null)
			{
				boolean vibrate = am.shouldVibrate(AudioManager.VIBRATE_TYPE_RINGER);
				int mode = am.getRingerMode();
				switch (mode)
				{
					case AudioManager.RINGER_MODE_NORMAL:// 正常模式
						if (vibrate)
						{
							return Notification.DEFAULT_ALL;
						}
						else
						{
							return Notification.DEFAULT_SOUND;
						}
					case AudioManager.RINGER_MODE_SILENT:// 静音模式
						return Notification.DEFAULT_LIGHTS;
					case AudioManager.RINGER_MODE_VIBRATE:// 振动模式
						return Notification.DEFAULT_VIBRATE;
				}
			}

			return Notification.DEFAULT_SOUND;
		}
		catch (Exception e)
		{
			return Notification.DEFAULT_LIGHTS;
		}
	}

	/**
	 * 获取LOG文件的名字，每次运行会生成一个 以时间戳为后缀名的TXT文件
	 */
	private static boolean isFirstGet = true;
	private static String logFileName = "yuecai_log_";

	public static String getLogFileName()
	{
		if (isFirstGet)
		{
			long milliseconds = System.currentTimeMillis();
			logFileName += Utility.getDate(milliseconds) + "_" + Utility.getTime(milliseconds, "_") + ".txt";
			isFirstGet = false;
		}
		return logFileName;
	}

	public static String getTime(long milliseconds, String symbol)
	{
		Date date = new Date(milliseconds);
		//  SimpleDateFormat formatter = new SimpleDateFormat("HH：mm");//H为占位符如多了就会添0补位
		//  return formatter.format(date);

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		StringBuffer sb = new StringBuffer();
		if (hour < 10)
		{
			sb.append(0);//添0补位
		}
		sb.append(hour);

		sb.append(symbol);
		if (minute < 10)
		{
			sb.append(0);//添0补位
		}
		sb.append(minute);

		sb.append(symbol);
		if (second < 10)
		{
			sb.append(0);//添0补位
		}
		sb.append(second);
		return sb.toString();
	}

	/**
	 * 计算执行时间
	 */
	private static long startTime = 0;
	public static void setStartTime()
	{
		startTime = System.currentTimeMillis();
	}
	public static void getRunTime(String methodName)
	{
		long endTime = System.currentTimeMillis();
		Log.e(methodName, "" + (endTime - startTime));
//		Utility.writeFileLog(methodName + (endTime - startTime) );
		startTime = endTime;
	}

	public static String getDefaultTimeZoneRawOffset()
	{
		//System.out.println(System.currentTimeMillis());
		String t ="TimeZone>>";
		t += TimeZone.getDefault().getDisplayName();
		t += "\t";
		t += TimeZone.getDefault().getRawOffset()/1000/60/60;
		t += "\t";
		t += TimeZone.getDefault().getDSTSavings();
		return t;
	}
	
	/**
	 * 通过milliseconds得到一个格式为"yyyy-mm-dd"的日期，比如2009-10-29 SimpleDateFormat方式模拟器耗时30毫秒，真机13毫秒
	 * Calendar方式模拟器耗时12毫秒，真机2毫秒
	 */
	public static String getDate(String milliseconds)
	{
		long mil;
		try
		{
			mil = Long.parseLong(milliseconds);
		}
		catch (Exception e)
		{
			return milliseconds;
		}
		return getDate(mil);
	}

	public static String getDate(long milliseconds)
	{
		Date date = new Date(milliseconds);
		//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		//		return formatter.format(date);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		StringBuffer sb = new StringBuffer();
		sb.append(c.get(Calendar.YEAR));
		sb.append("-");
		if (month < 10)
		{
			sb.append(0);//添0补位
		}
		sb.append(month);//月份从0开始
		sb.append("-");
		if (day < 10)
		{
			sb.append(0);//添0补位
		}
		sb.append(day);
		return sb.toString();
	}

	/**
	 * 通过milliseconds得到一个格式为"HH:mm"的时间，比如15：25 SimpleDateFormat方式耗时40毫秒，真机16毫秒
	 * Calendar方式模拟器耗时12毫秒，真机4毫秒
	 */
	public static String getTime(String milliseconds)
	{
		long mil;
		try
		{
			mil = Long.parseLong(milliseconds);
		}
		catch (Exception e)
		{
			return milliseconds;
		}

		return getTime(mil);
	}

	public static String getTime(long milliseconds)
	{
		Date date = new Date(milliseconds);
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		StringBuffer sb = new StringBuffer();
		if (hour < 10)
		{
			sb.append(0);//添0补位
		}
		sb.append(hour);
		sb.append(":");
		if (minute < 10)
		{
			sb.append(0);//添0补位
		}
		sb.append(minute);
		return sb.toString();
	}

	/**
	 * 得到时间，如果是今天的时间，就显示TIME,如果是过去的时间就显示DATE SimpleDateFormat方式耗时70毫秒，真机35毫秒
	 * Calendar方式模拟器耗时20毫秒，真机7毫秒
	 */
	public static String getDateORTime(String milliseconds)
	{
		String today = getDate(System.currentTimeMillis());
		if (today.equals(getDate(milliseconds)))	//时间就是今天的
		{
			return getTime(milliseconds);
		}
		else
		{
			return getDate(milliseconds);
		}
	}

	/**
	 * 根据给定的时间，判断是否是当前的，如果是当天的就不需要显示日期返回false，如果不是就需要显示日期返回true
	 */
	public static boolean isNeedDate(String milliseconds)
	{
		String today = getDate(System.currentTimeMillis());
		if (today.equals(getDate(milliseconds)))	//时间就是今天的
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static String getBasebandVersion()
	{
		Log.d("test-ams", Build.VERSION.RELEASE);
		return Build.VERSION.RELEASE;
	}

	public static String getUserAgent(Context context) {
		String userAgent = "";
		if (SupportVersion.JellyBeanMR1()) {
			try {
				userAgent = WebSettings.getDefaultUserAgent(context);
			} catch (Exception e) {
				userAgent = System.getProperty("http.agent");
			}
		} else {
			userAgent = System.getProperty("http.agent");
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0, length = userAgent.length(); i < length; i++) {
			char c = userAgent.charAt(i);
			if (c <= '\u001f' || c >= '\u007f') {
				sb.append(String.format("\\u%04x", (int) c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取当前版本信息对
	 * @param ctt 上下文对象
	 * @return  当前版本信息
	 */
	public static String getVerstionStr(Context ctt)
	{
		StringBuffer sb = new StringBuffer();
		PackageManager manager = ctt.getPackageManager();
		String pkgName = ctt.getPackageName();
		try {
		    PackageInfo info = manager.getPackageInfo(pkgName, 0);
		    //sb.append(info.packageName);
		    sb.append(info.versionName);// 版本名
		    //sb.append(info.versionCode);
		} catch (NameNotFoundException e) {
		     // TODO Auto-generated catch block
		     e.printStackTrace();
		}
		return sb.toString();
	}

	public static int getViewHeight(View view) {
		if (view == null) {
			return 0;
		}
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		//int width = view.getMeasuredWidth();
		return view.getMeasuredHeight();
	}

	public static int getViewWidth(View view) {
		if (view == null) {
			return 0;
		}
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		return view.getMeasuredWidth();
	}


	/**
	 * 复制文本到系统剪切板
	 */
	public static void copy2ClipboardManager(Context context, String text) {
		if (context == null) {
			return;
		}
		ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		if (SupportVersion.Honeycomb()) {
			ClipData mClip = ClipData.newPlainText("text", text);
			clipboard.setPrimaryClip(mClip);
		} else {
			clipboard.setText(text);
		}
		//Toast.makeText(context, "已经复制到剪切版", Toast.LENGTH_SHORT).show();
	}

	public static void doubleClick2Copy(final TextView textView) {
		if (textView != null) {
			textView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					copy2ClipboardManager(textView.getContext(), textView.getText().toString());
					return false;
				}
			});
		}
	}

	public static int getNavigationBarHeight(Context context) {
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			return resources.getDimensionPixelSize(resourceId);
		}
		return 0;
	}
	
	public static String getMetaOfApplication(Context ctt,String name)
	{
		ApplicationInfo appInfo = null;
		String msg = "";
		try
		{
			appInfo = ctt.getPackageManager().getApplicationInfo(ctt.getPackageName(), PackageManager.GET_META_DATA);
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		if (appInfo != null)
		{
			msg = String.valueOf(appInfo.metaData.getString(name));
			//System.out.println(name + ":" + msg);
		}
		return msg;
	}
	
	public static String getMobileModel()
	{
		Log.d("test-ams", Build.MODEL);
		return Build.MODEL;
	}

	/**
	 * 检查字符串中的字符是否都是数字
	 */
	public static boolean isAllNumber(String strNum)
	{
		if (strNum == null)
			return false;
		for (int i = 0; i < strNum.length(); i++)
		{
			char c = strNum.charAt(i);
			if (c < '0' || c > '9')
				return false;
		}
		return true;
	}

	/**
	 * 过滤字符，返回数字
	 * @param str
	 * @return
	 */
	public static String filterCharacter(String str)
	{
		StringBuffer numbers;
		if(str == null)
		{
			return null;
		}
		numbers = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
		{
			char ch = str.charAt(i);
			if (ch >= '0' && ch <= '9')
				numbers.append(ch);
		}
		return numbers.toString();
	}
	
	/**
	 * 带有返回值的页面跳转
	 * 
	 * @param from
	 * @param cls
	 * @param bundle
	 * @param requestCode
	 */
	public static void skipActivityForResult(Activity from, Class<?> cls, Bundle bundle, int requestCode)
	{
		Intent intent = new Intent();
		intent.setClass(from, cls);
		if (bundle != null)
		{
			intent.putExtras(bundle);
		}
		from.startActivityForResult(intent, requestCode);
	}

	//-----------------------------------------------------------

	/**
	 * 将给定的列表项目listText转换成一个Spinner可用的arrayAdapter 提供给 一级，二级 分类搜索使用,其他地方也可以使用
	 */
	public static ArrayAdapter<CharSequence> getArrayAdapter(Context context, String[] listText)
	{
		ArrayAdapter<CharSequence> mAddTypeSelectAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, listText);
		mAddTypeSelectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return mAddTypeSelectAdapter;
	}
	
	//socket这两个应该定义在核心层中
	public static String local_ip;// ip
	public static int local_port;// port

	/**
	 * <p>
	 * get IP string from an IP byte array
	 * </p>
	 * 
	 * @param ipBytes
	 *            byte array of IP, should be 4 bytes
	 * @return IP string, or empty string if can't convert
	 */
	public static String getIpString(byte[] ipBytes)
	{
		if (ipBytes == null || ipBytes.length != 4)
		{
			return "";
		}

		StringBuilder buf = new StringBuilder();
		buf.append(ipBytes[0] & 0xFF);
		buf.append('.');
		buf.append(ipBytes[1] & 0xFF);
		buf.append('.');
		buf.append(ipBytes[2] & 0xFF);
		buf.append('.');
		buf.append(ipBytes[3] & 0xFF);

		return buf.toString();
	}

	/**
	 * Get host from http's request string.
	 * 
	 * @param req
	 * @return
	 */
	public static String getHostFromRequestUrl(String req)
	{
		String[] tmp = req.split("/");
		for (String s : tmp)
		{
			if (!TextUtils.isEmpty(s))
			{
				String su = s.toUpperCase();
				if (!"HTTP:".equals(su) && !"HTTPS:".equals(su))
				{
					return s;
				}
			}
		}

		return null;
	}

	/**
	 * Get ip address from an address string For example: 202.202.211.211:8000, then return
	 * 202.202.211.211
	 * 
	 * @param address
	 *            address string, may contains a port
	 * @return ip address only
	 */
	public static String getIp(String address)
	{
		address = delDomain(address);
		String[] vals = address.split(":");
		return vals[0];
	}

	/**
	 * Get port from an address string For example: 202.202.211.211:8000, then return 8000
	 * 
	 * @param address
	 *            address string, may contains a port
	 * @return port number
	 */
	public static int getPort(String address)
	{
		address = delDomain(address);
		String[] vals = address.split(":");
		if (vals.length > 1)
		{
			try
			{
				return Integer.parseInt(vals[1]);
			}
			catch (NumberFormatException e)
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 去掉domain
	 * 
	 * @param address
	 * @return
	 */
	private static String delDomain(String address)
	{
		String domain = "socket://";
		if (address.startsWith(domain))
		{
			return address.substring(domain.length(), address.length());
		}
		else
		{
			return address;
		}
	}

	/**
	 * 根据传入的分割符,得到分割后的字符串
	 * 
	 * @param str
	 *            要被分割的字符
	 * @param split
	 *            按照什么分割字符来分割
	 * @return
	 */
	public static String[] splitString(String str, String split)
	{
		if (str != null && !str.equals("") && split != null && !split.equals(""))
		{
			String[] result = str.split(split);
			return result;
		}
		return null;
	}

	/**
	 * 将willDel从array数组中删除,并返回删除后的数组
	 * 
	 * @param willDel
	 * @param array
	 * @return
	 */
	public static String[] deleteStringFromArray(String willDel, String[] array)
	{
		if (array == null)
		{
			return null;
		}
		int index = 0;
		String[] result = new String[array.length - 1];
		for (int i = 0; i < array.length; i++)
		{
			if (array[i].equals(willDel))
			{
				continue;
			}
			result[index] = array[i];
			index++;
		}
		return result;
	}

	/**
	 * 此函数用来调用照相
	 * 
	 * @author sudongfeng
	 */
	public static void getCamera(Context packageContext)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		packageContext.startActivity(intent);
	}

	/**
	 * 判断是否存在 sdcard
	 * 
	 * @author sudongfeng
	 */
	public static boolean sdCardExit()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	private static File m_recordAudioFile;
	private static File m_recordAudioDir;
	private static MediaRecorder m_mediaRecorder;

	/**
	 * 开始录音
	 * 
	 * @author sudongfeng
	 */
	public static void startMediaRecorder()
	{
		try
		{
			if (!sdCardExit())
			{
				return;
			}
			m_recordAudioDir = new File(Environment.getExternalStorageDirectory(), "record");
			if (!m_recordAudioDir.exists())
				m_recordAudioDir.mkdir();
			String strTempFile = "VoiceMemo";
			m_recordAudioFile = File.createTempFile(strTempFile, ".amr", m_recordAudioDir);
			m_mediaRecorder = new MediaRecorder();
			m_mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			m_mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			m_mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

			m_mediaRecorder.setOutputFile(m_recordAudioFile.getAbsolutePath());
			m_mediaRecorder.prepare();
			m_mediaRecorder.start();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	/**
	 * 停止录音
	 * 
	 * @author sudongfeng
	 */
	public static void stopMediaRecorder()
	{
		if (m_recordAudioFile != null)
		{
			try
			{
				m_mediaRecorder.stop();
				m_mediaRecorder.release();
				m_mediaRecorder = null;
				m_recordAudioFile = null;
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
		}
	}

	//TODO 待优化
	public static void playSound(int resid) {
		try {
			MediaPlayer mp = MediaPlayer.create(getContext(), resid);
			mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int PLAYER_ONLINE = 0;
	public static final int PLAYER_NEWINFO = 1;
	private static MediaPlayer[] mediaPlayer = new MediaPlayer[2];

	/**
	 * 释放声音资源
	 */
	public static void clearSound()
	{
		for (MediaPlayer mp : mediaPlayer)
		{
			if (mp != null)
			{
				mp.stop();
				mp.release();
			}
		}
		mediaPlayer = null;
	}

	//判断电话号码是否为空
	public static boolean isEmptyContactPhonenumber(String phoneNumber)
	{
		if (phoneNumber != null && phoneNumber.length() > 0)
			return false;
		else
			return true;
	}

	//判断电话是否是手机号，现在不推送mobilenolist，无法判断是否为移动号码，只判断长度
	public static boolean isMobileNumber(String phonenumber)
	{
		if (phonenumber == null || phonenumber.length() < 11)
		{
			return false;
		}
		else
		{
			return true;
		}
//		if (cn.com.fetion.javacore.v11.common.Utility.checkMobile(phonenumber).equals("OK"))
//			return true;
//		else
//			return false;
	}

	//根据日期判断星期
	public static String getWeek(Date date)
	{
		Calendar cal = Calendar.getInstance(); // 创建一个日历对象。 
		cal.set(2008, 0, 12);//将日历翻到2008年5月12日,注意0表示一月 
		cal.setTime(date);
		int val = cal.get(Calendar.DAY_OF_WEEK) - 1;
		switch (val)
		{
			case 0:
				return "星期日";
			case 1:
				return "星期一";
			case 2:
				return "星期二";
			case 3:
				return "星期三";
			case 4:
				return "星期四";
			case 5:
				return "星期五";
			case 6:
				return "星期六";
			default:
				break;
		}
		return null;
	}
	
	public static String getWeek(int num)
	{
		switch (num)
		{
			case 0:
				return "星期日";
			case 1:
				return "星期一";
			case 2:
				return "星期二";
			case 3:
				return "星期三";
			case 4:
				return "星期四";
			case 5:
				return "星期五";
			case 6:
				return "星期六";
			default:
				break;
		}
		return null;
	}
	
	public static int getWeekNum(Date date)
	{
		Calendar cal = Calendar.getInstance(); // 创建一个日历对象。 
		cal.set(2008, 0, 12);//将日历翻到2008年5月12日,注意0表示一月 
		cal.setTime(date);
		int val = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return  val;
	}


	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input)
	{
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == ' ')
			{
				c[i] = '\u3000';
			}
			else if (c[i] < '\177')
			{
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input)
	{
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == '\u3000')
			{
				c[i] = ' ';
			}
			else if (c[i] > '\uFF00' && c[i] < '\uFF5F')
			{
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

	/**
	 * 使某个的menu置灰
	 * 
	 * @param menu
	 * @param menuId
	 *            某个服务的menuId
	 */
	public static void setMenuEnable(Menu menu, int menuId, boolean isEnable)
	{
		MenuItem m = menu.findItem(menuId);
		if (m != null)
		{
			m.setEnabled(isEnable);
		}
	}

	/**
	 * 使某个的menu隐藏
	 * 
	 * @param menu
	 * @param menuId
	 *            某个服务的menuId
	 */
	public static void setMenuVisible(Menu menu, int menuId, boolean visible)
	{
		MenuItem m = menu.findItem(menuId);
		if (m != null)
		{
			m.setVisible(visible);
		}
	}

	/**
	 *根据本地字符集排序，GBK,会有一部分文字排序不对，只有3800个汉字可以是正确顺序
	 */
	public static int compareChinaString(final String c1, final String c2)
	{
		//		result = Collator.getInstance(java.util.Locale.CHINA).compare(c1, c2);

		String s1 = null;
		String s2 = null;
		int result = 0;
		try
		{
			if (c1 == null)
			{
				s1 = new String("".getBytes("GBK"), "GBK");
			}
			else
			{
				s1 = new String(c1.getBytes("GBK"), "GBK");
			}
			if (c2 == null)
			{
				s2 = new String("".getBytes("GBK"), "GBK");
			}
			else
			{
				s2 = new String(c2.getBytes("GBK"), "GBK");
			}
			result = s1.compareTo(s2);//charAt(i)
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 全角转半角
	 * @param filterStr
	 * @return
	 */
	public static String getDBC(String filterStr)
	{
		return Utility.ToDBC(filterStr);
	}

	/**
	 * 用于把src里的内容拷贝到target中,因为直接转换会有异常
	 * 
	 * @param src
	 * @param target
	 */
	public static void copyObjects(Object[] src, Object[] target)
	{
		try
		{
			if (src == null)
			{
				return;
			}
			for (int i = 0; i < src.length; i++)
			{
				target[i] = src[i];
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝objs到集合list
	 * 
	 * @param <E>
	 * @param objs
	 * @param list
	 */
	public static <E> void copyObjectsToAbstractList(E[] objs, List<E> list)
	{
		if (objs == null)
		{
			return;
		}
		for (int i = 0; i < objs.length; i++)
		{
			list.add(objs[i]);
		}
	}

	/**
	 * 拷贝list集合到objs
	 * 
	 * @param <E>
	 * @param objs
	 * @param list
	 */
	public static <E> void copyAbstractListToObjects(List<E> list, E[] objs)
	{
		if (objs == null)
		{
			return;
		}
		if (objs.length != list.size())
		{
			return;
		}
		for (int i = 0, size = list.size(); i < size; i++)
		{
			objs[i] = list.get(i);
		}
	}

	//	/**
	//	 * 得到vectorData中的数据(是原数据)
	//	 * 
	//	 * @param <E>
	//	 */
	//	public static <E> Vector<E> getData(Vector<E> srcData)
	//	{
	//		Vector<E> result = new Vector<E>();
	//		synchronized (srcData)
	//		{
	//			for (E gc : srcData)
	//			{
	//				result.add(gc);
	//			}
	//			return result;
	//		}
	//	}

	/**
	 * 用src中的数据替换掉target中的数据
	 * 
	 * @param <E>
	 * @param src
	 * @param target
	 */
	public static <E> void replaceData(Vector<E> src, Vector<E> target)
	{
		synchronized (target)
		{
			target.clear();
			for (E gc : src)
			{
				target.add(gc);
			}
		}
	}

	/**
	 * 得到src的Object数组
	 * 
	 * @param <E>
	 * @param src
	 * @return
	 */
	public static <E> Object[] getArray(Vector<E> src)
	{
		Object[] result = new Object[src.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = src.elementAt(i);
		}
		return result;
	}
	
	/**
	 * 比较字符串
	 * 
	 * @param s1
	 * @param s2
	 */
	public static int compare(String s1, String s2)
	{
		String ss1 = DatabaseUtils.getHexCollationKey(s1);
		String ss2 = DatabaseUtils.getHexCollationKey(s2);
		return ss1.compareTo(ss2);
	}

	private static WeakReference<Context> mContext;

	public static void setContext(Context context)
	{
		mContext = new WeakReference<Context>(context);
	}

	public static Context getContext()
	{
		return mContext.get();
	}

	public static LayoutInflater getLayoutInflater()
	{
		return LayoutInflater.from(Utility.getContext());
	}
	public static WifiManager wifimanager;
	public static WifiManager.WifiLock wifilock;
	public static final int TYPE_NULL = -1;
	public static final int TYPE_CMWAP = 0;
	public static final int TYPE_CMNET = 1;
	public static final int TYPE_WIFI = 2;


	/**
	 * 获得网络连接管理
	 * 
	 * @return
	 */
	public static ConnectivityManager getConnectManager(Context context)
	{

		ConnectivityManager m_ConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		return m_ConnectivityManager;
	}
	
	/**
	 * 判断当前是否是CMWAP链接
	 * @param activeNetInfo
	 * @return
	 */
	public static boolean isCmwapConnected(NetworkInfo activeNetInfo)
	{
		boolean isCmwapConnected = false;
		if( activeNetInfo.getState() == NetworkInfo.State.CONNECTED )
		{
			if( activeNetInfo.getExtraInfo()!=null )
			{
				String str = activeNetInfo.getExtraInfo().toLowerCase();//&&isCurretApnIP(context)需要判断代理就加上
				if( str.indexOf("cmwap") >= 0 )
				{
					isCmwapConnected=true;
				}
			}
		}
		return isCmwapConnected;
	}
	
	/**
	 * 系统是否支持wml
	 * 
	 * @param ctt
	 * @return
	 */
	public static boolean isSupportWml(Context ctt)
	{
		String sdk = Build.VERSION.SDK;
		boolean largerThan8 = false;
		int sdkInt = 4;
		if (!TextUtils.isEmpty(sdk))
		{
			try
			{
				sdkInt = Integer.parseInt(sdk);
			}
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (sdkInt > 8)
			{
				largerThan8 = true;
			}
		}
		
		boolean existUC = false;
		PackageManager packageMgr = ctt.getPackageManager();
		List<PackageInfo> list = packageMgr.getInstalledPackages(0);
		for (int i = 0; i < list.size(); i++)
		{
			PackageInfo info = list.get(i);
			String temp = info.packageName;
			if (temp.equals("com.UCMobile"))
			{
				// 存在UC V8
				existUC = true;
			}
			else if (temp.equals("com.uc.browser"))
			{
				// 存在UC
				existUC = true;
			}
		}

		if (!existUC && largerThan8)
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean exitsUC(Context ctt)
	{
		boolean existUC = false;
		PackageManager packageMgr = ctt.getPackageManager();
		List<PackageInfo> list = packageMgr.getInstalledPackages(0);
		for (int i = 0; i < list.size(); i++)
		{
			PackageInfo info = list.get(i);
			String temp = info.packageName;
			if (temp.equals("com.UCMobile"))
			{
				// 存在UC V8
				existUC = true;
			}
			else if (temp.equals("com.uc.browser"))
			{
				// 存在UC
				existUC = true;
			}
		}

		return existUC;
	}

	/**
	 * 调用浏览器打开Url
	 * 
	 * @param ctt
	 * @param url
	 */
	public static void choiceBrowserToVisitUrl(Context ctt,String url)
	{
        boolean existChrome = false;
        String chrome = "";
		boolean existUC_HD = false, existUCV8 = false,existUC = false, existOpera = false, existQQ = false, existDolphin = false, existSkyfire = false, existSteel = false, existGoogle = false;
		String ucPathV8 = "",ucPath = "", operaPath = "", qqPath = "", dolphinPath = "", skyfirePath = "", steelPath = "", googlePath = "";
		PackageManager packageMgr = ctt.getPackageManager();
		List<PackageInfo> list = packageMgr.getInstalledPackages(0);//PackageManager.GET_UNINSTALLED_PACKAGES
		for (int i = 0; i < list.size(); i++)
		{
			PackageInfo info = list.get(i);
			String temp = info.packageName;

            if (temp.equals("com.android.chrome"))
            {
                chrome = temp;
                existChrome = true;
            }
			else if (temp.equals("com.uc.browser.hd"))
			{
				ucPathV8 = temp;
				existUC_HD = true;
			}
			else if (temp.equals("com.UCMobile"))
			{
				// 存在UC V8
				ucPathV8 = temp;
				existUCV8 = true;
			}
			else if (temp.equals("com.uc.browser"))
			{
				// 存在UC
				ucPath = temp;
				existUC = true;
			}
			else if (temp.equals("com.dolphin.browser.cn"))
			{
				// 存在海豚浏览器
				ucPathV8 = temp;
				existUCV8 = true;
			}
			/*else if (temp.equals("cn.miren.browser"))
			{
				// 存在迷人浏览器 不能打开指定网址
				ucPathV8 = temp;
				existUCV8 = true;
			}*/
			else if (temp.equals("com.tencent.mtt"))
			{
				// 存在QQ
				qqPath = temp;
				existQQ = true;
			}
			else if (temp.equals("com.opera.mini.android"))
			{
				// 存在Opera
				operaPath = temp;
				existOpera = true;
			}
			else if (temp.equals("mobi.mgeek.TunnyBrowser"))
			{
				dolphinPath = temp;
				existDolphin = true;
			}
			else if (temp.equals("com.skyfire.browser"))
			{
				skyfirePath = temp;
				existSkyfire = true;
			}
			else if (temp.equals("com.kolbysoft.steel"))
			{
				steelPath = temp;
				existSteel = true;
			}
			else if (temp.equals("com.android.browser"))
			{
				// 存在GoogleBroser
				googlePath = temp;
				existGoogle = true;
			}
		}
		/*if(existUC_HD)
		{
			gotoUrl(ctt,ucPathV8, url, packageMgr);
		}
		else */

        if (existChrome)
        {
            gotoUrl(ctt,chrome, url, packageMgr);
        }
        else
        if (existUCV8)
		{
			gotoUrl(ctt,ucPathV8, url, packageMgr);
		}
		else if (existUC)
		{
			gotoUrl(ctt,ucPath, url, packageMgr);
		}
		else if (existOpera)
		{
			gotoUrl(ctt,operaPath, url, packageMgr);
		}
		else if (existQQ)
		{
			gotoUrl(ctt,qqPath, url, packageMgr);
		}
		else if (existDolphin)
		{
			gotoUrl(ctt,dolphinPath, url, packageMgr);
		}
		else if (existSkyfire)
		{
			gotoUrl(ctt,skyfirePath, url, packageMgr);
		}
		else if (existSteel)
		{
			gotoUrl(ctt,steelPath, url, packageMgr);
		}
		else if (existGoogle)
		{
			gotoUrl(ctt,googlePath, url, packageMgr);
		}
		else
		{
			doDefault(ctt,url);
		}
	}
	
	private static void doDefault(Context ctt,String url)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		ctt.startActivity(intent);
	}

	private static void gotoUrl(Context ctt,String packageName, String url, PackageManager packageMgr)
	{
		try
		{
			Intent intent;
			intent = packageMgr.getLaunchIntentForPackage(packageName);
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse(url));
			ctt.startActivity(intent);
		}
		catch (Exception e)
		{
			// 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
			e.printStackTrace();
		}
	}

	/**
	 * 获得Sim卡运营商信息
	 * 
	 * @return
	 */
	private static String getSimOperator(TelephonyManager tm)
	{

		String SimOperator = tm.getSimOperator();
		return SimOperator;
	}

	private static String getMCC(TelephonyManager tm)
	{
		//		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String numeric = tm.getSimOperator();
		String mcc = numeric.substring(0, 3);
		return mcc;
	}

	private static String getMNC(TelephonyManager tm)
	{
		//		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String numeric = tm.getSimOperator();
		String mnc = numeric.substring(3, numeric.length());
		return mnc;
	}

	/**
	 * 跳转系统网络设置界面
	 * 
	 * @param context
	 */
	public static void startActivitySetting(Context context)
	{
		//调用其他应用的activity
		//		在代码中"com.android.settings"是要打开的程序包名，"com.android.settings.WirelessSettings"是要打开的Activity�?
		Intent intent = new Intent(Intent.ACTION_MAIN);
		ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
		intent.setComponent(componentName);
		context.startActivity(intent);
	}

	static Vector<String> myClass = new Vector<String>(64);

	public static void setMyClassName(String name)
	{
		myClass.add(name);
	}

	public static void removeMyClassName(String name)
	{
		myClass.remove(name);
	}


	/**
	 * 判断是否是汉字
	 * @param filterStr
	 * @return
	 */
	public static boolean isHanzi(String filterStr)
	{
		if(filterStr.length()>0&&Character.toString(filterStr.charAt(0)).matches("[\\u4E00-\\u9FA5]+"))
		{
			return true;
		}
		return false;
	}

	/**
	 * 预判断是否需要过滤
	 * @param filterStr
	 * @return
	 */
	public static boolean needFilter(String filterStr)
	{
		if(filterStr.length()>=1&&"".equals(filterStr.trim()))//只有空格
		{
			return false;
		}
		return true;
	}

	/**
	 * 检查手机号是否合法. 目前只检查是否为全数字的手机号
	 * 
	 * @param phoneNumber
	 *            要检查的手机号
	 * @return 是否合法
	 */
	public static boolean checkPhoneNumber(String phoneNumber) {
		boolean isOK = true;
		char c;
		int size = phoneNumber.length();
		for (int i = 0; i < size; i++) {
			c = phoneNumber.charAt(i);
			switch (c) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':

					break;

				default:
					isOK = false;
					break;
			}
			if (!isOK) {
				break;
			}
		}
		return isOK;
	}
	
	
	/**
	 * 获取手机固件版本号，返回1，2，3，4，5，6，7，8之类的整数，
	 * 对应版本号，os1.0 =1; os1.1=2; os1.5=3;os1.6=4;os2.0=5;os2.0.1=6;os2.1=7;os2.2=8;
	 * 其中 我们版本使用3-8通用，1or2不做适配。3or4在手机通讯录里使用相同方法获取通讯录联系人，5-8使用OS2.1的方法获取联系人。
	 * @return
	 */
	private static int level=-1;
	private static int getVersionLevel()
	{
		Log.e("Build.SDK", "Build.VERSION.SDK="+Build.VERSION.SDK);
		if( level == -1 )
		{
			level = 3;
			try
			{
				String strLevel = Build.VERSION.SDK;
				level = Integer.parseInt(strLevel);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return level;
	}
	/**
	 * 是否使用新的通讯录获取方法，目前os1.5/1.6使用等级为false，2.0/2.0.1/2.1/2.2使用等级为true
	 * @return
	 */
	public static boolean isUsedNewAddressBookMethod()
	{
		if( getVersionLevel() <=4 )
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * 复制文本到系统剪切板
	 */
/*	public static void copy2ClipboardManager(Context context,String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText( text );
        Toast.makeText( context, Utility.getContext().getString(R.string.copy_ok)+text, Toast.LENGTH_SHORT ).show();
    }*/
	
    private static boolean isReconnecting=false;
    public static void setIsReconnecting(boolean _isReconnecting)
    {
    	isReconnecting = _isReconnecting;
    }
    
	/**
	 * 替换原来代码中LogicSet.getLogicSet().getSysState() == SysConstants.ACT_TYPE_DATA_STATE_LOGGED
	 * @return
	 */
	public static boolean isACT_TYPE_DATA_STATE_LOGGED()
	{
		return true;
	}
	
	/**
	 * 替换原来代码中LogicSet.getLogicSet().getSysState() == SysConstants.ACT_TYPE_DATA_STATE_CLOSECONNT
	 * @return
	 */
	public static boolean isACT_TYPE_DATA_STATE_CLOSECONNT()
	{
//		if( SysGlobalID.sysState == Constants.SYSTEM_STATE_LOGIN_SUCCEEDED)
//		{
			return false;
//		}
//		else
//		{
//			return true;
//		}
	}
	
	
	
	public static void backToDesk(Activity activity){
		Intent MyIntent = new Intent(Intent.ACTION_MAIN);
		MyIntent.addCategory(Intent.CATEGORY_HOME);
		activity.startActivity(MyIntent);
//		activity.getParent().moveTaskToBack(true);
	}
	
	/**
	 * 跳到系统自带的编辑短信界面
	 * @param context
	 * @param telNumber
	 */
	public static void skipSms(Context context, String telNumber)
	{
		if (telNumber != null)
		{
			Intent messageIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + telNumber));
			context.startActivity(messageIntent);
		}
	}

	/**
	 * 跳到系统自带的浏览器
	 * @param context
	 * @param url
	 */
	public static void skipBrowser(Context context, String url)
	{
		if (url != null)
		{
			Intent messageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
			messageIntent.putExtra("url", "http://"+url);
			context.startActivity(messageIntent);
//			LogicSet.getLogicSet().jumpView(SysConstants.VIEW_ID_BROWSER, "http://"+url);
		}
	}
	
	/**
	 * 跳到系统自带的编辑EMAIL的界面
	 * @param context
	 * @param mail
	 */
	public static void skipMail(Context context, String mail)
	{
		if (mail != null)
		{
			Intent messageIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mail));
			context.startActivity(messageIntent);
		}
	}

	/**
	 * 调用系统电话
	 * @param context
	 * @param richText
	 */
	public static void skipTel(Context context, String richText)
	{
		Uri num = Uri.parse("tel:"+richText);
		context.startActivity(new Intent(Intent.ACTION_DIAL, num));
	}
	///////////////保存 用户 USER ID///////////////////////////////////////////////////////////
	//保存一下 升级提示的类型
	private static String USER_ID = "";//
	public static void setUSER_ID(String _user_id)
	{
		USER_ID = _user_id;
	}
	public static String getUSER_ID()
	{
		Log.e("Utility", "getUSER_ID="+USER_ID);
		return USER_ID;
	}
	
//	保存一下 服务器版本是否有变化，其中一个变化则需要提示用户升级？
	private static boolean isVersionChange = false;//服务器版本无变化
	private static void setVersionChange(boolean _isVersionChange)
	{
		isVersionChange = _isVersionChange;
	}
	public static boolean getVersionChange()
	{
		return isVersionChange;
	}
	
	/**
	 * 获取升级提示时的 新版下载URL
	 */
/*	public static String getNewVersionsUrl()
	{
		String config_clnt_install_uri 	= getConfigData(Constants.CONFIG_CLNT_INSTALL_URI);
		Log.e("getNewVersionsHighest", "config_clnt_install_uri="+config_clnt_install_uri);
		return config_clnt_install_uri;
	}
	*/
	/**
	 * 获取升级提示时的 新版特性描述
	 */
/*	public static String getNewVersionsDesc()
	{
		String config_clnt_desc 		= getConfigData(Constants.CONFIG_CLNT_DESC);
		Log.e("getNewVersionsHighest", "config_clnt_desc="+config_clnt_desc);
		return config_clnt_desc;
	}*/
	
	/**
	 * 是否可以下载，如果可以下载则自动下载，如果不可以下载 有文字提示，并要延迟退出
	 */
/*	public static boolean goDownload(Context context,String url)
	{
		boolean isGoDownload = false;
        try
		{
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED))
			{
				Uri downloadURL = Uri.parse(url);
				Intent url_intent = new Intent(Intent.ACTION_VIEW, downloadURL);
				context.startActivity(url_intent);
				isGoDownload = true;
			}
			else if (status.equals(Environment.MEDIA_SHARED)) 
			{
				Toast.makeText(context, context.getString(R.string.download_sdcard_in_use), Toast.LENGTH_SHORT).show();
			} 
			else 
			{
				Toast.makeText(context, context.getString(R.string.download_need_sdcard), Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(context, context.getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
		}
		return isGoDownload;
	}*/

	
	


	/**
	 * 隐藏软键盘
	 *
	 * @param context
	 * @param view
	 */
	public static void hideSoftInputFromWindow(Context context,View view)
	{
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public static void showSoftInput(Context context,View view)
	{
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	private static void insertData(Context ctt)
	{
		ContentResolver cr = ctt.getContentResolver();
		ContentValues values;

//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11057");
//		values.put(YueCaiContentProvider.COL_NUMBER, "04,06,20,21,26,33|2");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "33,21,06,04,26,20|2");
//		Uri uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11056");
//		values.put(YueCaiContentProvider.COL_NUMBER, "13,16,19,20,23,25|10");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "16,25,20,23,19,13|10");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11055");
//		values.put(YueCaiContentProvider.COL_NUMBER, "08,13,16,17,29,32|16");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "29,13,17,16,08,32|16");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11054");
//		values.put(YueCaiContentProvider.COL_NUMBER, "08,11,16,17,22,33|8");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "22,33,08,17,16,11|8");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11053");
//		values.put(YueCaiContentProvider.COL_NUMBER, "03,06,10,12,22,30|15");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "22,03,30,12,06,10|15");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11052");
//		values.put(YueCaiContentProvider.COL_NUMBER, "04,05,08,19,27,28|8");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "05,19,28,04,08,27|8");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11051");
//		values.put(YueCaiContentProvider.COL_NUMBER, "01,07,11,14,15,16|14");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "11,01,16,07,14,15|14");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11050");
//		values.put(YueCaiContentProvider.COL_NUMBER, "04,05,19,22,28,29|15");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "19,28,05,22,04,29|15");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11049");
//		values.put(YueCaiContentProvider.COL_NUMBER, "01,11,17,18,27,31|14");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "31,11,01,27,17,18|14");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11048");
//		values.put(YueCaiContentProvider.COL_NUMBER, "10,14,18,25,26,27|15");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "18,10,25,14,26,27|15");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11047");
//		values.put(YueCaiContentProvider.COL_NUMBER, "04,13,23,25,27,33|14");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "13,25,23,04,33,27|14");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11046");
//		values.put(YueCaiContentProvider.COL_NUMBER, "09,17,18,26,29,30|8");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "30,29,17,18,09,26|8");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11045");
//		values.put(YueCaiContentProvider.COL_NUMBER, "02,16,17,20,26,32|8");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "17,32,26,16,20,02|8");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11044");
//		values.put(YueCaiContentProvider.COL_NUMBER, "03,14,16,26,27,31|9");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "16,27,26,31,14,03|9");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11043");
//		values.put(YueCaiContentProvider.COL_NUMBER, "04,13,14,17,25,31|4");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "25,14,31,04,13,17|4");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11042");
//		values.put(YueCaiContentProvider.COL_NUMBER, "05,13,15,17,19,21|15");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "13,19,05,17,15,21|15");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11041");
//		values.put(YueCaiContentProvider.COL_NUMBER, "04,10,12,13,30,32|13");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "13,12,10,04,32,30|13");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11040");
//		values.put(YueCaiContentProvider.COL_NUMBER, "05,11,14,24,26,28|13");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "11,05,28,14,24,26|13");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11039");
//		values.put(YueCaiContentProvider.COL_NUMBER, "03,07,10,16,24,29|13");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "03,10,24,16,07,29|13");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11038");
//		values.put(YueCaiContentProvider.COL_NUMBER, "02,14,15,19,23,24|12");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "02,23,24,19,15,14|12");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11037");
//		values.put(YueCaiContentProvider.COL_NUMBER, "01,03,04,06,17,25|11");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "06,25,04,03,17,01|11");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11036");
//		values.put(YueCaiContentProvider.COL_NUMBER, "02,11,20,22,24,31|5");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "24,02,22,20,11,31|5");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11035");
//		values.put(YueCaiContentProvider.COL_NUMBER, "13,14,18,20,27,31|2");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "18,13,27,31,20,14|2");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11034");
//		values.put(YueCaiContentProvider.COL_NUMBER, "13,14,17,19,24,31|8");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "13,19,31,24,17,14|8");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11033");
//		values.put(YueCaiContentProvider.COL_NUMBER, "02,08,12,13,19,29|4");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "08,13,29,12,19,02|4");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11032");
//		values.put(YueCaiContentProvider.COL_NUMBER, "03,05,07,13,14,15|15");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "05,15,14,07,13,03|15");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11031");
//		values.put(YueCaiContentProvider.COL_NUMBER, "16,17,24,28,29,32|12");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "29,28,24,32,17,16|12");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11030");
//		values.put(YueCaiContentProvider.COL_NUMBER, "02,09,19,24,25,33|10");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "09,33,25,24,19,02|10");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11029");
//		values.put(YueCaiContentProvider.COL_NUMBER, "01,04,09,10,20,31|7");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "10,01,31,04,20,09|7");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);
//
//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_TYPE, LotteryType.SSQ.toString());
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "11028");
//		values.put(YueCaiContentProvider.COL_NUMBER, "01,04,05,14,16,17|1");
//		values.put(YueCaiContentProvider.COL_NUM_ORDER, "16,05,17,14,04,01|1");
//		uri = cr.insert(YueCaiContentProvider.OPEN_CONTENT_URI_NO_NOTIFICATION, values);

//		values = new ContentValues();
//		values.put(YueCaiContentProvider.COL_OPEN_TERM, "");
//		values.put(YueCaiContentProvider.COL_OPEN_TIME, "");
//		values.put(YueCaiContentProvider.COL_UPDATE_TIME,"");
//		values.put(YueCaiContentProvider.COL_NUM, "");
//		values.put(YueCaiContentProvider.COL_ORDER, "");
//		values.put(YueCaiContentProvider.COL_PERSON_NUM, "");
//		values.put(YueCaiContentProvider.COL_LOTTERY_MONEY, "");
//				
//		uri = cr.insert(YueCaiContentProvider.CONTACT_CONTENT_URI_NO_NOTIFICATION, values);
	}

	static BigInteger factorialBigInteger4Combination(int x, int min)
	{
		BigInteger res = BigInteger.ONE;
		if (x < 1)
			return res;
		if (x < min)
			return BigInteger.ZERO;
		if (min < 1)
		{
			return BigInteger.ZERO;
		}
		if (min == 1)
		{
			min = 2;
		}
		if (x == min)
		{
			return BigInteger.valueOf(x);
		}
		for (int i = min; i <= x; i++)
		{
			BigInteger num = new BigInteger(String.valueOf(i));
			res = res.multiply(num);//调用自乘方法
		}
		return res;
	}
	
	
	/**
	 * 获取双色球开奖奖等与中奖号码
	 * @param open 开奖号码  比如  "1,2,3,4,5,6|9" 
	 * @param lotStr 投注号码   比如  "1,2,3,4,5,6|9"
	 * @return  HashMap<String, Object> 其中中奖等级Key:level(int);用户中奖数字位置为：has (Boolean[],ture表示该数字中奖)
	 */
	public static HashMap<String, Object> getLotterySsqLevel(String open, String lotStr)
	{   
		
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		
		int  LotLevel = 0;//中奖等级
		String[] opens =open.split(",");
		String[] lotStrs=lotStr.split(",");
		int countRed =0;
		Boolean selectedBlue=false;
		
		//取得指定的红球和蓝球
		String openBlue = opens[opens.length-1].substring(opens[opens.length-1].length()-1); //开出的蓝球
		opens[opens.length-1] =opens[opens.length-1].substring(0, opens[opens.length-1].indexOf("|"));   //所有开出的红球 
		
		String lotBlue  =lotStrs[lotStrs.length-1].substring(lotStrs[lotStrs.length-1].length()-1);
		lotStrs[lotStrs.length-1]=lotStrs[lotStrs.length-1].substring(0, lotStrs[lotStrs.length-1].indexOf("|"));
		
		//中奖的数字
		boolean [] has= new boolean[opens.length+1];
		
		for(int i=0;i<has.length;i++){
			has[i]=false;
		}
		
		
		//判断选中了几个红球 
		for(int i=0;i<opens.length;i++){
			for(int j=0;j<lotStrs.length;j++){
				if(opens[i].equals(lotStrs[j])){
					has[j]=true;
					countRed++;
				}
			}
		}
		//判断蓝球是否中奖
		if(openBlue.equals(lotBlue)){
			selectedBlue =true;
			has[has.length-1]=true;
		}
		
		switch (countRed) {
		case 0:
		case 1:
		case 2:
			 if(selectedBlue)
				 LotLevel=6;
			break;
		case 3:
			if(selectedBlue)
				 LotLevel= 5;
			break;
		case 4:
			   if(selectedBlue){
				   LotLevel=4;
			   }else{
				   LotLevel=5;
			   }
			break;
		case 5:
			   if(selectedBlue){
				   LotLevel=3;
			   }else {
				   LotLevel=4;
			   }
			break;
		case 6:
			if(selectedBlue){
				LotLevel=1;
			}else {
				LotLevel=2;
			}
		}
		System.out.println(countRed);
		System.out.println(selectedBlue);
		System.out.println(LotLevel);
		
		retMap.put("level", LotLevel);
		retMap.put("has", has);
		//将为null的值复制为false，防止在以后的引用中出现空指针错误

		return retMap;
	} 
	
	/**
	 * 组合 n>=m
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
    public static BigInteger Combination(int n,int m)
    {
		//System.out.print("C" + m + "/" + n + "\t");
        if(m>n)
            return BigInteger.ZERO;
        else if (m == n || m < 1)
            return BigInteger.ONE;
        else
        {
        	if(m>n-m)
        	{
        		BigInteger n_mfactorial = factorialBigInteger(n-m);
                return factorialBigInteger4Combination(n, m+1).divide(n_mfactorial);
        	}
        	else
        	{
        		BigInteger mfactorial = factorialBigInteger(m);
                return factorialBigInteger4Combination(n, n-m+1).divide(mfactorial);
        	}
        }
    }
    
	/**
	 * 阶乘
	 * 
	 * @param x
	 * @return
	 */
	private static BigInteger factorialBigInteger(int x)
	{
		BigInteger res = BigInteger.ONE;
		if (x < 1)
			return res;
		for (int i = 2; i <= x; i++)
		{
			res = res.multiply(BigInteger.valueOf(i));// 调用自乘方法
		}
		return res;
	}
	
	/**
     * 计算福彩3D某个号码的和值总注数(包括直选和值，组选3和值，组选6和值)
     * 和值的计算方式是这样的：如选中的号码为3，那么计算出来的值为10,即系某个三位数的和等于3，其注为:003,300,030,012,021,210,120,201,102,111
     * 
     * @param num 被选中的某个号码
     * @param type 只能用0，1，2这三个数，0表示直选和值，1表示组选三和值，2表示组选六和值
     * @return 某个号码和值总数
     */
	public static int CountFuCai3DSumBet(int num, int type)
	{
		int betSum = 0;
		for (int i = 0; i <= 9; i++)
		{
			for (int j = 0; j <= 9; j++)
			{
				for (int k = 0; k <= 9; k++)
				{
					if ((i + j + k) == num)
					{
						// 直选和值
						if (type == 0)
						{
							betSum++;
						}
						// 组选3和值
						else if (type == 1)
						{
							if ((i == j || i == k || j == k) && !(i == j && i == k && j == k))
							{
								betSum++;
							}
						}
						// 组选6和值
						else if (type == 2)
						{
							if (i != j && i != k && j != k)
							{
								betSum++;
							}
						}
					}
				}
			}
		}
		// 组选3和值
		if (type == 1) {
			betSum /= 3;
		}
		// 组选6和值
		else if (type == 2) {
			betSum /= 6;
		}
		return betSum;
	}
		
	/**
	 * 计算排列六和值注数
	 * @param num
	 * @return
	 */
	public static int calcPl3Hezhi6(int num)
	{
		int betSum = 0;
		for (int i = 0; i <= 9; i++)
		{
			for (int j = 0; j <= i; j++)
			{
				for (int k = 0; k <= j; k++)
				{
					int sum =i+j+k;
					if (sum==num && !(i==j && j==k))
					{
						//System.out.println(i + "\t" + j + "\t" + k);
						betSum++;
					}
				}
			}
		}
		return betSum;
	}
	
	/**
	 * 计算时时彩某个号码的和值总注数(三星组选和值)
	 * @param num 和值
	 * @return 时时彩某个号码的和值总注数(三星组选和值)
	 */
	public static int CountSscSum3Bet(int num)
	{
		int betSum = 0;
		for (int i = 0; i <= 9; i++)
		{
			for (int j = 0; j <= i; j++) 
			{
				for (int k = 0; k <= j; k++)
				{
					if ((i + j + k) == num) 
					{
						betSum++;
					}
				}
			}
		}
		return betSum;
	}
	
	/**
	 * 计算时时彩某个号码的和值总注数(二星组选和值)
	 * @param num 和值
	 * @return 时时彩某个号码的和值总注数(二星组选和值)
	 */
	public static int CountSscSum2Bet(int num)
	{
		int betSum = 0;
		for (int i = 0; i <= 9; i++)
		{
			for (int j = 0; j <= i; j++) 
			{
				if ((i + j) == num) 
				{
					betSum++;
				}
			}
		}
		return betSum;
	}

	public static boolean isCachePath(Context ctt, String path)
	{
		if (!TextUtils.isEmpty(path) && ctt != null)
		{
			String pkg = ctt.getPackageName();
			File file = new File(path);
			String p1 = null;
			String p2 = null;
			if (file != null && file.exists() && file.isDirectory())
			{
				String absPath = file.getAbsolutePath();
				Log.i(TAG, "absPath:" + absPath);
				File cacheDir = null;
				File cacheExternal = null;
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					cacheExternal = ctt.getExternalCacheDir();
				}
				cacheDir = ctt.getCacheDir();
				if (cacheDir != null)
				{
					String cachePath = cacheDir.getAbsolutePath();
					Log.i(TAG, "CacheDir:" + cachePath);
					if (cachePath != null && cachePath.contains(pkg))
					{
						int pos = cachePath.indexOf(pkg);
						p1 = cachePath.substring(0, pos);
						Log.i(TAG, p1);
						//return true;
					}
				}

				if (cacheExternal != null)
				{
					String extCacheDir = cacheExternal.getAbsolutePath();
					Log.i(TAG, "ExternalCacheDir:" + extCacheDir);
					if (extCacheDir != null && extCacheDir.contains(pkg))
					{
						int pos = extCacheDir.indexOf(pkg);
						p2 = extCacheDir.substring(0, pos);
						Log.i(TAG, p2);
						//return true;
					}
				}
				
				if (p1 != null)
				{
					File f1 = new File(p1);
					if (f1 != null)
					{
						if (absPath.equals(f1.getAbsolutePath()))
						{
							return true;
						}
					}
				}
				if (p2 != null)
				{
					File f2 = new File(p2);
					if (f2 != null)
					{
						if (absPath.equals(f2.getAbsolutePath()))
						{
							return true;
						}
					}
				}
			}

		}
		return false;
	}
	
	public static ArrayList<String> getCharset()
	{
		SortedMap<String, Charset> charsetsMap = Charset.availableCharsets();
		Set<Entry<String, Charset>> entries = charsetsMap.entrySet();
		ArrayList<String> charsets = new ArrayList<String>();
		String defaultCharset = Charset.defaultCharset().name();
		//Log.e(TAG, defaultCharset);
		charsets.add(defaultCharset);
		for (Entry<String, Charset> entry : entries)
		{
			//String key = entry.getKey();
			Charset charset = entry.getValue();
			String name = charset.name();
			if (!charsets.contains(name))
			{
				charsets.add(name);
			}
			//Log.e(TAG, key + ":" + name);
		}
		
//		for (String charset : charsets)
//		{
//			Log.e(TAG, charset);
//		}
		return charsets;
	}

	public static boolean canAccessWifiState(Context context) {
		boolean hasPermission = false;
		if(!SupportVersion.M()) {
			return true;
		} else if(context == null) {
			Log.e(TAG, "context null!");
			return hasPermission;
		} else {
			if(SupportVersion.M() && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
				hasPermission = true;
			}
			return hasPermission;
		}
	}


	public static boolean isCleartextTrafficPermitted() {
		boolean isCleartextTrafficPermitted = false;
		if (SupportVersion.Marshmallow()) {
			isCleartextTrafficPermitted = NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
			Log.e(TAG, "M isCleartextTrafficPermitted: " + isCleartextTrafficPermitted);
		}
		if (SupportVersion.N()) {
			//isCleartextTrafficPermitted = NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
		}
		Log.e(TAG, "isCleartextTrafficPermitted: " + isCleartextTrafficPermitted);
		return isCleartextTrafficPermitted;
	}

    public static boolean isCleartextTrafficPermitted(String hostname) {
        boolean isCleartextTrafficPermitted = false;
        if (SupportVersion.N()) {
            isCleartextTrafficPermitted = NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted(hostname);
            Log.e(TAG, "N isCleartextTrafficPermitted: " + isCleartextTrafficPermitted);
            return isCleartextTrafficPermitted;
        }
        if (SupportVersion.Marshmallow()) {
            isCleartextTrafficPermitted = NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
            Log.e(TAG, "M isCleartextTrafficPermitted: " + isCleartextTrafficPermitted);
        }
        Log.e(TAG, "isCleartextTrafficPermitted: " + isCleartextTrafficPermitted);
        return isCleartextTrafficPermitted;
    }

	 /*********************图形缩放**************************/
	public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h)
	{
		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = ((float) dst_w) / src_w;
		float scale_h = ((float) dst_h) / src_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);
		return dstbmp;
	}

	/**
	 * 将图片缩放在一定范围
	 * @param bitmap
	 * @param maxW
	 * @param maxH
	 * @return
	 */
	public static Bitmap imageScaleLimited(Bitmap bitmap, int maxW, int maxH,boolean recycle) {
		if (bitmap == null) {
			return bitmap;
		}
		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = ((float) maxW) / src_w;
		float scale_h = ((float) maxH) / src_h;
		scale_h = Math.min(scale_w, scale_h);
		scale_w = scale_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);
		if (recycle && bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return dstbmp;
	}

	
	public static void date(String second)
	{
		Long time = Long.parseLong(second);
		Date date = new Date(time * 1000);
//		System.out.println(TimeZone.getDefault().getDisplayName());
		//System.out.println("Date:" + date);
	}
	
	/**
	 * 秒转时分秒
	 * @param second
	 * @return
	 */
	public static String cal(int second) {
		int h = 0;//小时
		int d = 0;//分
		int s = 0;//秒
		final int HOUR = 3600;//60*60
		int temp = second % HOUR;
		if (second > HOUR)//大于一小时
		{
			h = second / HOUR;
			if (temp != 0)
			{
				if (temp > 60)
				{
					d = temp / 60;
					if (temp % 60 != 0) 
					{
						s = temp % 60;
					}
				} 
				else 
				{
					s = temp;
				}
			}
		}
		else 
		{
			d = second / 60;
			if (second % 60 != 0) 
			{
				s = second % 60;
			}
		}

		return h + "时" + d + "分" + s + "秒";
	}
	
	public static String date2StringMDHM(String second)
	{
		Long time = Long.parseLong(second);
		Date date = new Date(time * 1000);
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(date);
	}
	
	public static String date2StringYMDHM(String second)
	{
		Long time = Long.parseLong(second);
		Date date = new Date(time * 1000);
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	public static String date2StringWithOutYear(String second)
	{
		Long time = Long.parseLong(second);
		Date date = new Date(time * 1000);
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String date2StringWithOutYearAndSecond(String second)
	{
		if(TextUtils.isEmpty(second))
		{
			return "";
		}
		Long time = Long.parseLong(second);
		Date date = new Date(time * 1000);
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(date);
	}
	
	/**
	 * 计算机标准时间 以  yyyy-MM-dd显示
	 * @param second
	 * @return
	 */
	public static String date2StringDate(String second)
	{
		if(second==null || second.length()<1)
		{
			return "";
		}
		Long time = Long.parseLong(second);
		Date date = new Date(time * 1000);
//		System.out.println(TimeZone.getDefault().getDisplayName());
		//System.out.println("Date:" + date);
		//return date.toLocaleString();
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String date2StringDateMMdd(String second)
	{
		if(second==null || second.length()<1)
		{
			return "";
		}
		Long time = Long.parseLong(second);
		Date date = new Date(time * 1000);
//		System.out.println(TimeZone.getDefault().getDisplayName());
		//System.out.println("Date:" + date);
		//return date.toLocaleString();
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("MM-dd");
		return sdf.format(date);
	}
	
	public static void getDefault(){
		TimeZone.getDefault();
	}
	
	private static Random rand = new Random(System.currentTimeMillis());

	public static String formatHtmlBox(String[] array)
	{
		String format = "<div style=\"border:1px solid #a0a0a0; background:#f2f2f2; padding:2px 5px; float:left; margin:3px;\">XX</div>";
		StringBuffer sb = new StringBuffer();
		if (array != null)
		{
			for (String tmp : array)
			{
				sb.append(format.replace("XX", tmp));
			}
			return sb.toString();
		}

		return null;
	}
	

	
	private String showUninstallAPKSignatures(String apkPath) {
        String PATH_PackageParser = "android.content.pm.PackageParser";
        try {
            // apk包的文件路径
            // 这是一个Package 解释器, 是隐藏的
            // 构造函数的参数只有一个, apk文件的路径
            // PackageParser packageParser = new PackageParser(apkPath);
            Class pkgParserCls = Class.forName(PATH_PackageParser);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser = pkgParserCt.newInstance(valueArgs);
//            MediaApplication.logD(DownloadApk.class, "pkgParser:" + pkgParser.toString());
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
            // File(apkPath), apkPath,
            // metrics, 0);
            typeArgs = new Class[4];
            typeArgs[0] = File.class;
            typeArgs[1] = String.class;
            typeArgs[2] = DisplayMetrics.class;
            typeArgs[3] = Integer.TYPE;
            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
                    typeArgs);
            valueArgs = new Object[4];
            valueArgs[0] = new File(apkPath);
            valueArgs[1] = apkPath;
            valueArgs[2] = metrics;
            valueArgs[3] = PackageManager.GET_SIGNATURES;
            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
            
            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = Integer.TYPE;
            Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates",
                    typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
//            MediaApplication.logD(DownloadApk.class, "size:"+info.length);
//            MediaApplication.logD(DownloadApk.class, info[0].toCharsString());
            return info[0].toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	private String getSign(Context context,Activity instance) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while(iter.hasNext()) {
             PackageInfo packageinfo = iter.next();
             String packageName = packageinfo.packageName;
             if (packageName.equals(instance.getPackageName())) {
//                MediaApplication.logD(DownloadApk.class, packageinfo.signatures[0].toCharsString());
                return packageinfo.signatures[0].toCharsString();
             }
     }
        return null;
    }
	
	static final Point screenSize = new Point();
	public static Point getScreenSize(Context ctt) {
		if (ctt == null) {
			return screenSize;
		}
		WindowManager wm = (WindowManager) ctt.getSystemService(Context.WINDOW_SERVICE);
		if (wm != null) {
			DisplayMetrics mDisplayMetrics = new DisplayMetrics();
			Display diplay = wm.getDefaultDisplay();
			if(diplay!=null)
			{
				if (SupportVersion.JellyBeanMR1())// Build.VERSION_CODES.JELLY_BEAN
				{
					diplay.getRealMetrics(mDisplayMetrics);
				}
				else
				{
					diplay.getMetrics(mDisplayMetrics);
				}
				int W = mDisplayMetrics.widthPixels;
				int H = mDisplayMetrics.heightPixels;
				if (W * H > 0 /*&& (W > screenSize.x || H > screenSize.y)*/)
				{
					screenSize.set(W, H);
					//Log.i(TAG, "screen size:" + screenSize.toString());
				}
			}
		}
//		if (MainActivity.DEBUG) {
//			Log.i(TAG, screenSize.toString());
//		}
		return screenSize;
	}

	// 判断SD卡是否存在
	public static boolean isExternalStorageMounted()
	{
		//TOOD 权限检查？
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	public static String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (SocketException ex)
		{
			Log.e(TAG, ex.toString());
		}
		return null;
	}
	
	
	/**
	 * 返回字符串str子串sign的数量
	 * @param str
	 * @param sign
	 * @return
	 */
	public static int getCount(final String str, final String sign)
	{
		// 查找某一字符串中str，特定子串sign的出现次数
		if (str == null || sign == null || sign.length() < 1)
			return 0;
		int i = str.length();
		String tmp = str.replaceAll(sign, "");// 反串中的字符sign替换成""
		return (i - tmp.length()) / sign.length();
	}
	
	public static int getActionBarHeight(Activity activity)
	{
		int actionBarHeight = 0;
		if(activity!=null)
		{
			TypedValue tv = new TypedValue();
			if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
			{
			    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,activity.getResources().getDisplayMetrics());
			}
		}
		return actionBarHeight;
	}
	
	/**
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isIntentAvailable(Context context, Intent intent) {
		if (context != null && intent != null) {
			final PackageManager packageManager = context.getPackageManager();
			List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list != null && list.size() > 0;
		}
		return false;
	}

	public final static String GOOGLE_PLAY_PACKAGENAME = "com.android.vending";
	
	public static boolean exitsApp(Context ctt,String packagename)
	{
		boolean exists = false;
		if(ctt==null || TextUtils.isEmpty(packagename))
		{
			return exists;
		}
		PackageManager packageMgr = ctt.getPackageManager();
		if(packageMgr!=null)
		{
			List<PackageInfo> list = packageMgr.getInstalledPackages(0);
			for (int i = 0; i < list.size(); i++)
			{
				PackageInfo info = list.get(i);
				String temp = info.packageName;
				if (temp.equals(packagename))
				{
					exists = true;
					break;
				}
			}
		}
		return exists;
	}

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	public static boolean isFileUriExits(String uriStr)
	{
		if(TextUtils.isEmpty(uriStr))
			return false;
		try {
			File file = new File(Uri.parse(uriStr).getPath());
			if(file!=null && file.exists())
			{
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static void fullScreen(boolean full, Activity activity)
	{
		Window window = activity.getWindow();
		if (full)
		{
			WindowManager.LayoutParams params = window.getAttributes();
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			//window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			
			/*
			View decorView = window.getDecorView();
			if (Build.VERSION.SDK_INT >= 14 && decorView!=null)
			{
				int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
				if (Build.VERSION.SDK_INT >= 16)
				{
					uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
				}
				decorView.setSystemUiVisibility(uiOptions);
			}
			*/
		}
		else
		{
			WindowManager.LayoutParams params = window.getAttributes();
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			//window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
	
	public static boolean setWallpaper(Activity activity,String packageName,String cls)
	{
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		Intent intent = new Intent();
		if (Build.VERSION.SDK_INT > 15) {
			intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			if(cls!=null)
			{
				ComponentName component = new ComponentName(packageName, cls);
				intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, component);
			}
		} else {
			intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
		}
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(isIntentAvailable(activity, intent))
		{
			activity.startActivityForResult(intent, 0);
			return true;
		}
		return false;
	}

	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 

	public static int px2dip(Context context, float pxValue){ 
	        final float scale = context.getResources().getDisplayMetrics().density; 
	        return (int)(pxValue / scale + 0.5f); 
	}

	public static void goStoreGeneral(Activity activity) {
		if (activity == null) {
			return;
		}
		final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
		try {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		} catch (Exception e){
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}

	public static void goToStore(Context ctt,String pkg) {
		if (ctt == null || TextUtils.isEmpty(pkg)) {
			return;
		}
		//https://developer.android.com/distribute/marketing-tools/linking-to-google-play#android-app
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + pkg));
		if (isIntentAvailable(ctt, intent)) {
			ctt.startActivity(intent);
		}
	}


	public static boolean go2GooglePlay(Context ctt, String packageName)
	{
		if(TextUtils.isEmpty(packageName))
		{
			return false;
		}
		String uriString = "market://details?id=" + packageName;
		if (packageName.startsWith("market:")) {
			uriString = packageName;
		}
		
		boolean sucess = false;
		PackageManager packageMgr = null;
		
		if(ctt!=null)
		{
			packageMgr = ctt.getPackageManager();
		}
		if (packageMgr != null && ctt != null)
		{
			if(exitsApp(ctt, GOOGLE_PLAY_PACKAGENAME))
			{
				try
				{
					Intent intent = packageMgr.getLaunchIntentForPackage(GOOGLE_PLAY_PACKAGENAME);
					if(intent!=null)
					{
						intent.setAction(Intent.ACTION_VIEW);
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.setData(Uri.parse(uriString));
						ctt.startActivity(intent);
						sucess = true;
					}
				}
				catch (Exception e)
				{
					// 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
					e.printStackTrace();
					sucess = false;
				}
			}
			if(!sucess)
			{
				Uri uri = Uri.parse(uriString);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				if(isIntentAvailable(ctt, it))
				{
					try
					{
						ctt.startActivity(it);
						sucess = true;
					}
					catch (Exception e)
					{
						// 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
						e.printStackTrace();
						sucess = false;
					}
				}
			}
			
		}
		return sucess;
	}

	public static void go2GooglePlay(Context ctt,String packageName,PackageManager packageMgr)
	{
		try
		{
			Intent intent = packageMgr.getLaunchIntentForPackage(GOOGLE_PLAY_PACKAGENAME);
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse("market://details?id="+packageName));
			if(isIntentAvailable(ctt, intent))
			{
				ctt.startActivity(intent);
			}
		}
		catch (Exception e)
		{
			// 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
			e.printStackTrace();
		}
	}

	public static boolean exitsAppAndActivity(Context ctt,String packagename,String activityName)
	{
		boolean exists = false;
		if (ctt == null || TextUtils.isEmpty(packagename) || TextUtils.isEmpty(activityName)) {
			return exists;
		}
		PackageManager packageMgr = ctt.getPackageManager();
		List<PackageInfo> list = packageMgr.getInstalledPackages(0);
		for (int i = 0; i < list.size(); i++)
		{
			PackageInfo info = list.get(i);
			String temp = info.packageName;
			if (temp.equals(packagename))
			{
				if(info!=null && info.activities!=null)
				{
					for(ActivityInfo ai:info.activities)
					{
						Log.e("wechat", ai.name);
						System.out.println(ai.name);
						if(ai.name!=null && ai.name.contains(activityName))
						{
							return true;
						}
					}
				}
				break;
			}
		}
		return exists;
	}
	
	/**
	 * 限定字符串字节数不多余某字节数
	 * 
	 * @param str
	 * @param bytes
	 * @return
	 */
	public static String limitString(String str, int bytes) {
		if (bytes == 0) {
			return "";
		}
		if (bytes < 1) {
			bytes = 1;
		}
		if (str == null || str.getBytes().length <= bytes) {
			return str;
		}

		if (str.length() > (bytes+1)) {
			str = str.substring(0, (bytes+1));
		}
		return limitString(str.substring(0, str.length() - 1), bytes);
	}
	
	public static byte[] compressImageAsByteArray(Bitmap image, int kb) {
		if (image == null) {
			return null;
		}
		if (kb < 0) {
			kb = 1;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		image.recycle();
		return baos.toByteArray();
	}

	public static Bitmap compressImage(Bitmap image,int kb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	public static byte[] bmpToByteArray(Bitmap bitmap, CompressFormat compressFormat, int quality) {
		try {
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(8 * 1024);
			if (bitmap.compress(compressFormat, quality, localByteArrayOutputStream)) {
				byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
				localByteArrayOutputStream.close();
				return arrayOfByte;
			}
		} catch (Throwable localThrowable) {
		}
		return null;
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void openUrl(Context context, String url) {
		if (url != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//	        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity"); 
	        intent.addCategory(Intent.CATEGORY_BROWSABLE);
	        if(isIntentAvailable(context, intent))
	        {
	        	context.startActivity(intent);
	        }
		}
	}

	public static short getLocale() {
		short code = 1;
		Locale defaultLocale = Locale.getDefault();
		if (Locale.SIMPLIFIED_CHINESE.equals(defaultLocale)) {
			code = 2;
		}
		if (Locale.TRADITIONAL_CHINESE.equals(defaultLocale)) {
			code = 3;
		}
		if (Locale.TAIWAN.equals(defaultLocale)) {
			code = 3;
		}
		if(Locale.ENGLISH.equals(defaultLocale))
		{
			//Log.e(TAG, Locale.ENGLISH.toString());
			code = 1;
		}
		if(Locale.US.equals(defaultLocale))
		{
			//Log.e(TAG, Locale.US.toString());
			code = 1;
		}
		return code;
	}




	public static boolean isLongValueEquals(Long l1, Long l2) {
		if (l1 == null && l2 == null) {
			return true;
		}
		if (l1 == null && l2 != null) {
			return false;
		}
		if (l1 != null && l2 == null) {
			return false;
		}
		return l1.longValue() == l2.longValue();
	}

	public static boolean isBooleanValueEquals(Boolean b1, Boolean b2) {
		if (b1 == null && b2 == null) {
			return true;
		}
		if (b1 == null && b2 != null) {
			return false;
		}
		if (b1 != null && b2 == null) {
			return false;
		}
		if (b1.booleanValue() && b2.booleanValue()) {
			return true;
		}
		if ((!b1.booleanValue()) && (!b2.booleanValue())) {
			return true;
		}
		return false;
	}

	public static float percent(long value, long total) throws Exception {
		if (total == 0) {
			throw new Exception();
		}
		return percent(value, total, 100);
	}

	private static float percent(long value, long total, long base) {
		return 1f * value / total * base;
	}


	/**
	 * make a color darker
	 * @param color
	 * @param factor 0.7f
	 * @return
	 */
	public static int manipulateColor(int color, float factor) {
		int a = Color.alpha(color);
		int r = Math.round(Color.red(color) * factor);
		int g = Math.round(Color.green(color) * factor);
		int b = Math.round(Color.blue(color) * factor);
		return Color.argb(a,
				Math.min(r,255),
				Math.min(g,255),
				Math.min(b,255));
	}


//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃ 　
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃   神兽保佑　　　　　　　　
//    ┃　　　┃   代码无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
	
}
