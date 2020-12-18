package club.zunhuier;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.widget.Toast;
import es.dmoral.toasty.Toasty;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "club.zunhuier.action.FOO";
    public static final String ACTION_BAZ = "club.zunhuier.action.BAZ";

    //定义启动Service的Action
    public static final String ACTION_MUSIC = "club.zunhuier.action.MUSIC";
    public static final String ACTION_STOP = "club.zunhuier.action.STOP";

    private MediaPlayer mediaPlayer;


    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "club.zunhuier.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "club.zunhuier.extra.PARAM2";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("=======当前为" + Thread.currentThread().getName() + "线程，正在调用onHandleIntent方法=============");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            } else if (ACTION_MUSIC.equals(action)){
                handleActionMusic();
            }
        }
    }

    /**
     * 播放音乐的方法，被onHandleIntent方法调用
     */
    private void handleActionMusic() {
        System.out.println("=======当前为" + Thread.currentThread().getName() + "线程，正在调用handleActionMusic方法=============");
        if (mediaPlayer == null){
            //动态注册广播接收器
            System.out.println("=======当前为" + Thread.currentThread().getName() + "线程，正在注册广播接收器=============");
            MusicBCE musicBCE = new MusicBCE();
            IntentFilter filter = new IntentFilter();
            filter.addAction(MyService.ACTION_STOP);
            getApplication().registerReceiver(musicBCE,filter);
            //根据音乐资源文件创建MediaPlayer对象 设置循环播放属性 开始播放
            mediaPlayer = MediaPlayer.create(this,R.raw.solstice);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        System.out.println("=======当前为" + Thread.currentThread().getName() + "线程，正在调用onDestroy方法=============");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        System.out.println("=======当前为" + Thread.currentThread().getName() + "线程，正在调用onCreate方法=============");
        super.onCreate();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MusicBCE extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean stop = intent.getBooleanExtra("stop",false);
            System.out.println("==========接收到了广播，stop值为：" + stop + "===============");
            if (stop) {
                mediaPlayer.pause();
                Toasty.success(context,"背景音乐已暂停", Toast.LENGTH_SHORT,true).show();
            }
            else {
                mediaPlayer.start();
                Toasty.success(context,"背景音乐已开启", Toast.LENGTH_SHORT,true).show();
            }
        }
    }
}