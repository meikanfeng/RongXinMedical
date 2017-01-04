package com.huagu.RX.rongxinmedical.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class MyWindowsManage {

	public static int getWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public static int getHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 整个屏幕区域
	 * 
	 * @param activity
	 * @return
	 */
	private Dimension getAreaOne(Activity activity) {
		Dimension dimen = new Dimension();
		Display disp = activity.getWindowManager().getDefaultDisplay();
		Point outP = new Point();
		disp.getSize(outP);
		dimen.mWidth = outP.x;
		dimen.mHeight = outP.y;
		return dimen;
	}
	/**
	 * 应用的区域
	 * 
	 * @param activity
	 * @return
	 */
	private Dimension getAreaTwo(Activity activity) {
		Dimension dimen = new Dimension();
		Rect outRect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
		System.out.println("top:" + outRect.top + " ; left: " + outRect.left);
		dimen.mWidth = outRect.width();
		dimen.mHeight = outRect.height();
		return dimen;
	}

	/**
	 * 绘制的区域
	 * 
	 * @param activity
	 * @return
	 */
	private Dimension getAreaThree(Activity activity) {
		Dimension dimen = new Dimension();
		// 用户绘制区域
		Rect outRect = new Rect();
		activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
		dimen.mWidth = outRect.width();
		dimen.mHeight = outRect.height();
		// end
		return dimen;
	}

	private class Dimension {
		public int mWidth;
		public int mHeight;

		public Dimension() {
		}
	}

}
