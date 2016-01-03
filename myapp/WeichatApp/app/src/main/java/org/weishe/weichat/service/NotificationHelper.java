package org.weishe.weichat.service;

import org.weishe.weichat.R;
import org.weishe.weichat.bean.Constants;
import org.weishe.weichat.bean.Todo;
import org.weishe.weichat.util.BaseTools;
import org.weishe.weichat.util.BeepManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.widget.RemoteViews;

public class NotificationHelper {

	private Context mContext;
	private static NotificationHelper mNotificationHelper;
	private NotificationManager mNotificationManager;

	private NotificationHelper(Context context) {
		mContext = context;
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static NotificationHelper getInstance(Context context) {

		if (mNotificationHelper == null) {
			mNotificationHelper = new NotificationHelper(context);
		}
		return mNotificationHelper;

	}

	/**
	 * 显示代办通知，好友申请，加群申请
	 */
	public void showTodoNotify(Todo todo, int userId, String token) {
		NotificationCompat.Builder mBuilder = new Builder(mContext);
		RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(),
				R.layout.todo_notification_item);
		mRemoteViews.setImageViewResource(R.id.user_photo,
				R.drawable.channel_qq);
		// API3.0 以上的时候显示按钮，否则消失
		mRemoteViews.setTextViewText(R.id.subject, todo.getTodoSubject());
		mRemoteViews.setTextViewText(R.id.request_msg, todo.getRequestMsg());

		// 点击的事件处理
		Intent buttonIntent = new Intent(Constants.INTENT_ACTION_HANDING_TODO);
		buttonIntent.putExtra(Constants.INTENT_EXTRA_HANDING_TODO_ID,
				todo.getTodoId());

		PendingIntent intent_paly = PendingIntent.getBroadcast(mContext, 2,
				buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(R.id.notification, intent_paly);

		// textStyle.bigText("好顶顶顶顶顶顶顶顶顶顶顶");
		mBuilder.setContent(mRemoteViews)
				.setContentIntent(
						getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
				.setWhen(System.currentTimeMillis())
				// 通知产生的时间，会在通知信息里显示
				.setTicker("好友请求").setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				.setOngoing(false).setSmallIcon(R.drawable.icon);

		Notification notify = mBuilder.build();
		notify.bigContentView = mRemoteViews;
		notify.flags = Notification.FLAG_AUTO_CANCEL;
		// 会报错，还在找解决思路
		// notify.contentView = mRemoteViews;
		// notify.contentIntent = PendingIntent.getActivity(this, 0, new
		// Intent(), 0);
		mNotificationManager.notify(todo.getTodoId(), notify);

	}

	public PendingIntent getDefalutIntent(int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1,
				new Intent(), flags);
		return pendingIntent;
	}
}
