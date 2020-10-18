package apktool.android.com.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import apktool.android.com.R;
import apktool.android.com.util.SupportVersion;
import apktool.android.com.util.Utility;

//import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
//import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

public class AboutDialogFragment extends AppCompatDialogFragment
{
	private static final String TAG = "AboutDialogFragment";

	private static final String PREF_FILE = "YearClass";
	private static final String PREF_NAME = "yearclass";

	private TextView aboutName;
	private TextView tv;
	private TextView versionTv;
	private ImageView iv_icon;
	private LinearLayout water;

//	private FingerprintIdentify mFingerprintIdentify;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Activity activity = getActivity();
		LayoutInflater inflator = LayoutInflater.from(activity);
		final View view = inflator.inflate(R.layout.alert_dialog_about_info, null);
		tv = view.findViewById(R.id.tv_message);
		aboutName = view.findViewById(R.id.aboutName);
		aboutName.setText(getString(R.string.app_name));
		versionTv = view.findViewById(R.id.versionTv);
		versionTv.setText("Version:" + Utility.getVerstionStr(activity));
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		SpannableString spannable = new SpannableString(getString(R.string.app_site));// \nhttp://www.zhangqingtian.cn
		Linkify.addLinks(spannable, Linkify.WEB_URLS);
		tv.setText(spannable);

		water = (LinearLayout) view.findViewById(R.id.water);

        iv_icon = view.findViewById(R.id.iv_icon);

		tv = (TextView) view.findViewById(R.id.tv_message2);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		tv.setText(spannable);


		//TextView auther = (TextView) view.findViewById(R.id.auther);
		aboutName.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				TextView tv = view.findViewById(R.id.test);
				tv.append(getCodec());
				tv.setVisibility(View.VISIBLE);
				return false;
			}
		});

		Point pt = Utility.getScreenSize(activity);

		tv = (TextView) view.findViewById(R.id.test);
		tv.setVisibility(View.GONE);
		String info = null;
		if (info == null) {
			info = "";
		}


		String debugStr = pt.x + "x" + pt.y + "\n" + Utility.dip2px(activity,1) + "x" + "\n" + info.replaceAll("#", "\n") + "\n";// + "\n" + FileUtil.getSdCardPathTest(activity)

		tv.setText(debugStr/* + Utility.getUserAgent(activity)*/ + "\n");

		/*
		mFingerprintIdentify = new FingerprintIdentify(getContext().getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
			@Override
			public void onCatchException(Throwable exception) {
				exception.printStackTrace();
				Log.e(TAG, "FingerprintIdentify onCatchException");
			}
		});

		if (mFingerprintIdentify.isFingerprintEnable()) {
			//
			final int MAX_AVAILABLE_TIMES = 3;
			mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
				@Override
				public void onSucceed() {
					//
					Log.e(TAG, "FingerprintIdentifyListener onSucceed: ");
				}

				@Override
				public void onNotMatch(int availableTimes) {
					//
					Log.e(TAG, "FingerprintIdentifyListener onNotMatch: ");
				}

				@Override
				public void onFailed(boolean isDeviceLocked) {
					//
					Log.e(TAG, "FingerprintIdentifyListener onFailed: ");
				}

				@Override
				public void onStartFailedByDeviceLocked() {
					//
					Log.e(TAG, "FingerprintIdentifyListener onStartFailedByDeviceLocked: ");
				}
			});
		}
		*/



		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.about)
		.setIcon(R.drawable.ic_action_about)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		}).setView(view).create();
	}



	@Override
	public void onStart()
	{
		super.onStart();
		Dialog dialog = getDialog();
	}
	

	private String getCodec(){
		StringBuffer sb = new StringBuffer();
		if(SupportVersion.Lollipop()){
			MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
			//MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
			MediaCodecInfo[] codecInfos = mediaCodecList.getCodecInfos();
			if(codecInfos!=null){
				for (MediaCodecInfo codecInfo : codecInfos) {
					sb.append(codecInfo.getName()).append(':');
					if(codecInfo.isEncoder()){
						sb.append("encoder,");
					}
					String[] supportedTypes = codecInfo.getSupportedTypes();
					for (String string : supportedTypes) {
						sb.append(" " + string);
					}
					sb.append('\n');
				}
			}
		}
		return sb.toString();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		/*
		if(mFingerprintIdentify!=null){
			mFingerprintIdentify.cancelIdentify();
		}
		*/
	}
}
