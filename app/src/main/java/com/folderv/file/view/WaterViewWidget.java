package com.folderv.file.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class WaterViewWidget extends View {
	Animation an;
	private AnimateDrawable2 mDrawable;
	Bitmap bm;
	

	public WaterViewWidget(Context context,int resId) {
		super(context);
		
		setFocusable(true);
        setFocusableInTouchMode(true);

        Drawable dr = context.getResources().getDrawable(resId);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        
        an = new TranslateAnimation(0, 100, 0, 200);
        an.setDuration(20);
        an.setRepeatCount(-1);
        an.initialize(10, 10, 10, 10);
        
		bm = BitmapFactory.decodeResource(getResources(), resId);
        mDrawable = new AnimateDrawable2(dr, an, bm);
        //an.startNow();
       
	}
	
	 @Override protected void onDraw(Canvas canvas) {
         canvas.drawColor(Color.BLACK);

         mDrawable.draw(canvas);
         invalidate();
     }
	 @Override
     public boolean onTouchEvent(MotionEvent event) {
         float x = event.getX();
         float y = event.getY();
         mDrawable.click(x, y);
		return true;
	 }

	public void start() {
		an.startNow();
		invalidate();
	}

	public void touchCenter()
	{
		if (bm != null)
			mDrawable.click(bm.getWidth() / 2, bm.getHeight() / 2);
	}
	
	//@Override
//	protected void onDraw2(Canvas canvas) {
//		
//
//		canvas.drawColor(Color.WHITE);
//
//		Paint paint = new Paint();
//
//		Bitmap bm = BitmapFactory.decodeResource(getResources(),
//				R.drawable.flower);
//
//		Matrix matrix = new Matrix();
//
//		Bitmap dst = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm
//				.getConfig());
//		f.filter(bm, dst);
//		canvas.drawBitmap(dst, matrix, paint);
//	}

//	public void next() {
//		float p = f.getPhase();
//		p++;
//		f.setPhase(p);
//	}

}
