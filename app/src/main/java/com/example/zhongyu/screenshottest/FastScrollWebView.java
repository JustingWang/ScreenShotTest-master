package com.example.zhongyu.screenshottest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;



/**
 * https://github.com/Mixiaoxiao/FastScroll-Everywhere FastScrollWebView
 * 
 * @author Mixiaoxiao 2016-08-31
 */
public class FastScrollWebView extends WebView implements FastScrollDelegate. FastScrollable {

	private FastScrollDelegate mFastScrollDelegate;
	private Context context;
	// override all other constructor to avoid crash
	// setting custom action bar
	private ActionMode mActionMode;
	private ActionMode.Callback mSelectActionModeCallback;
	private GestureDetector mDetector;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FastScrollWebView(Context context) {
		super(context);
		createFastScrollDelegate(context);
	}

	public FastScrollWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		createFastScrollDelegate(context);
	}

	public FastScrollWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		createFastScrollDelegate(context);
		WebSettings webviewSettings = getSettings();
		webviewSettings.setJavaScriptEnabled(true);
		// add JavaScript interface for copy
//		addJavascriptInterface(new CustomWebViewInterface(context), "JSInterface");
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public FastScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		createFastScrollDelegate(context);
	}

	// ===========================================================
	// createFastScrollDelegate
	// ===========================================================

	private void createFastScrollDelegate(Context context) {
		mFastScrollDelegate = new FastScrollDelegate.Builder(this).build();
	}

	// ===========================================================
	// Delegate
	// ===========================================================

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mFastScrollDelegate.onInterceptTouchEvent(ev)) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mDetector != null)
			mDetector.onTouchEvent(event);
		if (mFastScrollDelegate.onTouchEvent(event)) {
			return true;
		}

		return super.onTouchEvent(event);
	}


	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mFastScrollDelegate.onAttachedToWindow();
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (mFastScrollDelegate != null) {
			mFastScrollDelegate.onVisibilityChanged(changedView, visibility);
		}
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mFastScrollDelegate.onWindowVisibilityChanged(visibility);
	}

	@Override
	protected boolean awakenScrollBars() {
		return mFastScrollDelegate.awakenScrollBars();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		mFastScrollDelegate.dispatchDrawOver(canvas);
	}

	// ===========================================================
	// FastScrollable IMPL, ViewInternalSuperMethods
	// ===========================================================

	@Override
	public void superOnTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
	}

	@Override
	public int superComputeVerticalScrollExtent() {
		return super.computeVerticalScrollExtent();
	}

	@Override
	public int superComputeVerticalScrollOffset() {
		return super.computeVerticalScrollOffset();
	}

	@Override
	public int superComputeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}

	@Override
	public View getFastScrollableView() {
		return this;
	}

	/**
	 * @deprecated use {@link #getFastScrollDelegate()} instead
	 */
	public FastScrollDelegate getDelegate() {
		return getFastScrollDelegate();
	}

	@Override
	public FastScrollDelegate getFastScrollDelegate() {
		return mFastScrollDelegate;
	}

	@Override
	public void setNewFastScrollDelegate(FastScrollDelegate newDelegate) {
		if (newDelegate == null) {
			throw new IllegalArgumentException("setNewFastScrollDelegate must NOT be NULL.");
		}
		mFastScrollDelegate.onDetachedFromWindow();
		mFastScrollDelegate = newDelegate;
		newDelegate.onAttachedToWindow();
	}





	@Override
	public ActionMode startActionMode(ActionMode.Callback callback) {
		ViewParent parent = getParent();
		if (parent == null) {
			return null;
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			String name = callback.getClass().toString();
			if (name.contains("SelectActionModeCallback")) {
				mSelectActionModeCallback = callback;
				mDetector = new GestureDetector(context, new FastScrollWebView.CustomGestureListener());
			}
		}
		FastScrollWebView.CustomActionModeCallback mActionModeCallback = new FastScrollWebView.CustomActionModeCallback();
		return parent.startActionModeForChild(this, mActionModeCallback);
	}

	private class CustomActionModeCallback implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mActionMode = mode;
			MenuInflater inflater = mode.getMenuInflater();
//			inflater.inflate(R.menu.copy, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			Log.e("actionmod", mode.getTitle() + "sss");


			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

//			switch (item.getItemId()) {
//				case R.id.copy:
//					getSelectedData();
//
//					return true;
//				case R.id.share:
//
//					return true;
//				default:
//
//					if (mode != null) {
//						mode.finish();
//					}
				return false;
//			}

		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				clearFocus();
			} else {
				if (mSelectActionModeCallback != null) {
					mSelectActionModeCallback.onDestroyActionMode(mode);
				}
				// 2016 edit by sight
				// 这里 置空 会报 ActionModeImpl.invalidate(ActionBarImpl.java:1012) 空异常
				//mActionMode = null;
			}
		}
	}

	private void getSelectedData() {

		String js = "(function getSelectedText() {" + "var txt;" + "if (window.getSelection) {" + "txt = window.getSelection().toString();" + "} else if (window.document.getSelection) {"
				+ "txt = window.document.getSelection().toString();" + "} else if (window.document.selection) {" + "txt = window.document.selection.createRange().text;" + "}"
				+ "JSInterface.getText(txt);" + "})()";
		// calling the js function
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			evaluateJavascript("javascript:" + js, null);
		} else {
			loadUrl("javascript:" + js);
		}
	}

	private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
            return mActionMode != null;
        }

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);

//			Log.e("SSAASAD", e.getAction() + "");
		}

	}





}
