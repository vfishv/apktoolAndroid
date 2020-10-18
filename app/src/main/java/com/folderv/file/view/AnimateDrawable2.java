/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.folderv.file.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

public class AnimateDrawable2 extends ProxyDrawable {
    
	Bitmap bm, dst;
	WaterHandle handle;
    private Animation mAnimation;
    private Transformation mTransformation = new Transformation();

    public AnimateDrawable2(Drawable target) {
        super(target);
    }
    
    public AnimateDrawable2(Drawable target, Animation animation, Bitmap oldmap) {
        super(target);
		
		bm = oldmap;
		dst = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm
				.getConfig());
        mAnimation = animation;
        handle = new WaterHandle(oldmap, dst);
        //handle.disturb(50, 50);
    }
    
    public Animation getAnimation() {
        return mAnimation;
    }
    
    public void setAnimation(Animation anim) {
        mAnimation = anim;
    }

    public boolean hasStarted() {
        return mAnimation != null && mAnimation.hasStarted();
    }
    
    public boolean hasEnded() {
        return mAnimation == null || mAnimation.hasEnded();
    }
    
    @Override
    public void draw(Canvas canvas) {
        Drawable dr = getProxy();
        if (dr != null) {
            Animation anim = mAnimation;
            if (anim != null) {
                anim.getTransformation(
                                    AnimationUtils.currentAnimationTimeMillis(),
                                    mTransformation);
                
                //f.filter(bm, dst);
                handle.run1();
                
                int[] out = handle.getOut();
                
                Paint paint = new Paint();
                //canvas.drawBitmap(dst, 0, 0, paint);//FIXME why need so many parameters
                canvas.drawBitmap(out, 0, bm.getWidth(), 0, 0, 
                		bm.getWidth(), bm.getHeight(), false, paint);
            }
        }
    }

	public void click(float x, float y) {
		handle.disturb((int)x, (int)y);
	}
}