package org.weishe.weichat.view;

import org.weishe.weichat.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableView extends LinearLayout {
	private ImageView imageView;
	private TextView textView;

	public TableView(Context context) {
		super(context);
		View mView = LayoutInflater.from(context).inflate(R.layout.tab_view,
				this, true);
		imageView = (ImageView) mView.findViewById(R.id.menu_button_image);
		textView = (TextView) mView.findViewById(R.id.menu_button_title);
	}

	/**
	 * 
	 * @param icon
	 * @param title
	 */
	public void setTitle(Drawable icon, String title) {
		imageView.setImageDrawable(icon);
		textView.setText(title);
	}

}
