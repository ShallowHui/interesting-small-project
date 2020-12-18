package club.zunhuier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.suke.widget.SwitchButton;

public class HomeActivity extends AppCompatActivity {

    private TextView title;
    private TextView content;

    private Button introduction;
    private Button person;

    private BatteryBCE batteryBCE;

    private SwitchButton switchButton;
    private TextView musictext;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        //自定义Actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_title);
        //设置字体
        title = (TextView)findViewById(R.id.title);
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
        content = (TextView)findViewById(R.id.hobby);
        content.setText("我喜欢篮球，\n正如，\n我可以接受失败，\n但无法接受放弃。");
        final Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/方正卡通简体.ttf");
        content.setTypeface(typeface);
        //监听按钮
        introduction = (Button)findViewById(R.id.introduction);
        person = (Button)findViewById(R.id.person);
        introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HomeActivity.this,Introduction.class);
                startActivity(intent1);
            }
        });
        person = (Button)findViewById(R.id.person);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(HomeActivity.this,Star.class);
                startActivity(intent2);
            }
        });
        //注册接收系统低电量提示的广播接收器
        batteryBCE = new BatteryBCE();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        //动态注册
        registerReceiver(batteryBCE,filter);
        //监听音乐开关
        musictext = (TextView)findViewById(R.id.musictext);
        TextPaint mp = musictext.getPaint();
        mp.setFakeBoldText(true);
        switchButton = (SwitchButton)findViewById(R.id.switch_button);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                System.out.println("=======当前为" + Thread.currentThread().getName() + "线程，当前开关为：" + isChecked + "=============");
                Intent musicIntent = new Intent(MyService.ACTION_STOP);
                if (isChecked){
                    musicIntent.putExtra("stop",false);
                }else {
                    musicIntent.putExtra("stop",true);
                }
                sendBroadcast(musicIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        //注销广播接收器
        unregisterReceiver(batteryBCE);
        super.onDestroy();
    }
}