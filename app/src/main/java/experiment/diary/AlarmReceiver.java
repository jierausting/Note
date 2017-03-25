package experiment.diary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by 志锋 on 2016/12/23.
 */
public class AlarmReceiver extends BroadcastReceiver {
    Vibrator vibrator;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "闹铃响了", Toast.LENGTH_LONG).show();
        //设置通知内容并在onReceive()这个函数执行时开启
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("提醒").setContentText("写日记去").setPriority(Notification.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_menu_send)
                .setAutoCancel(true);
        //Notification notification=new Notification(R.drawable.ic_menu_send,"写日记去",System.currentTimeMillis());
        //notification.defaults = Notification.DEFAULT_ALL;

        Intent mIntent = new Intent(context,StartActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mIntent,0);
        builder.setContentIntent(mPendingIntent);

        Notification notification = builder.build();
        manager.notify(0, notification);

         /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1

//        //再次开启LongRunningService这个服务，从而可以
//        Intent i = new Intent(context, LongRunningService.class);
//        context.startService(i);
    }
}
