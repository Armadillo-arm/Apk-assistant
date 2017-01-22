package com.perfume.FileAdapter;

import android.view.*;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import com.perfume.App;
/*
屏幕分辨率转换
*/
public class AndroidUtilities {

	public static float density = 1;
	public static Point displaySize = new Point();

	static {
		density = App.thzz.getResources()
				.getDisplayMetrics().density;
		checkDisplaySize();
	}

	public static int dp(float value) {
		return (int) Math.ceil(density * value);
	}

	public static float dpf2(float value) {
		return density * value;
	}

	public static void checkDisplaySize() {
		try {
			WindowManager manager = (WindowManager) App.thzz
					.getSystemService(Context.WINDOW_SERVICE);
			if (manager != null) {
				Display display = manager.getDefaultDisplay();
				if (display != null) {
					if (android.os.Build.VERSION.SDK_INT < 13) {
						displaySize
								.set(display.getWidth(), display.getHeight());
					} else {
						display.getSize(displaySize);
					}
					
				}
			}
		} catch (Exception e) {
			
		}
	}

}
