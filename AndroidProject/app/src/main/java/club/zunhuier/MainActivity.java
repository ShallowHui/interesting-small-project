package club.zunhuier;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //设置字体
        textView1 = (TextView)findViewById(R.id.content1);
        textView2 = (TextView)findViewById(R.id.content2);
        final Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/DroidSerif-BoldItalic.ttf");
        textView1.setTypeface(typeface);
        //隐藏状态栏和标题栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //启动背景音乐播放服务
        intent = new Intent(MainActivity.this,MyService.class);
        intent.setAction(MyService.ACTION_MUSIC);
        startService(intent);
        //定义handler
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x111){
                    textView2.setText("Basketball");
                    textView2.setTypeface(typeface);
                }
            }
        };
        //几秒钟后跳到主界面
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(0x111); //通知主线程更新UI
                    Thread.sleep(3000);
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}