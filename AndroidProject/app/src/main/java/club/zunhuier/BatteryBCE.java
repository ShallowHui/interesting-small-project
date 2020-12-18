package club.zunhuier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import es.dmoral.toasty.Toasty;

public class BatteryBCE extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            //Toast.makeText(context, "当前电量为" + current, Toast.LENGTH_SHORT).show();
            //使用开源控件
            Toasty.warning(context, "当前电量不足！", Toast.LENGTH_SHORT, true).show();
        }
    }
}