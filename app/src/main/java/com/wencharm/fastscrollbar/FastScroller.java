package com.wencharm.fastscrollbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Wencharm on 2/20/16.
 */
public class FastScroller extends View {
	public FastScroller(Context context) {
		super(context);
		init(context);
	}

	public FastScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	// Touch event
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	public static String HEART = "HEART";
	public static String[] b = {HEART, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
	private int choose = -1;
	private Paint paint = new Paint();

	private TextView mTextDialog;
	private Context context;

	private float singleHeight;
	private Typeface regularFont;
	private Typeface iconFont;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public void init(Context context) {
		this.context = context;
		regularFont = Typeface.DEFAULT;
		iconFont = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "icomoon.ttf");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (getHeight() < context.getResources().getDisplayMetrics().heightPixels / 2) return;
		int height = getHeight();
		int width = getWidth();
		singleHeight = (height * 1f) / b.length;
		singleHeight = (height * 1f - singleHeight / 2) / b.length;
		paint.setColor(context.getResources().getColor(R.color.colorPrimary));
		paint.setAntiAlias(true);
		paint.setTextSize(context.getResources().getDisplayMetrics().density * 10);
		for (int i = 0; i < b.length; i++) {
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			if (b[i].equals(HEART)) {
				paint.setTypeface(iconFont);
				canvas.drawText(context.getString(R.string.icon_heart), width / 2 - paint.measureText(b[1]) / 2 - context.getResources().getDisplayMetrics().density * 2, singleHeight, paint);
			} else {
				paint.setTypeface(regularFont);
				canvas.drawText(b[i], xPos, yPos, paint);
			}
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				choose = -1;
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				break;

			default:
				if (c >= 0 && c < b.length) {
					if (listener != null && oldChoose != c) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						if (b[c].equals(HEART)) {
							mTextDialog.setTypeface(iconFont);
							mTextDialog.setText(context.getString(R.string.icon_heart));
						} else {
							mTextDialog.setTypeface(regularFont);
							mTextDialog.setText(b[c]);
						}
						mTextDialog.setVisibility(View.VISIBLE);
						mTextDialog.setTranslationY(y < singleHeight * 2 ? 0 : y - singleHeight * 2);
					}
					choose = c;
				}
				break;
		}
		return true;
	}

	/**
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		void onTouchingLetterChanged(String s);
	}
}
