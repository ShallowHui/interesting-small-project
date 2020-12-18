package club.zunhuier;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Introduction extends AppCompatActivity {

    private TextView common;
    private Button back;
    private TabHost tabHost;
    private TextView basketballhistory;
    private TextView nba;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_common);
        //设置标题
        common = (TextView)findViewById(R.id.common);
        common.setText("篮球简介");
        TextPaint tp = common.getPaint();
        tp.setFakeBoldText(true);
        //设置选项卡
        tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("篮球历史").setContent(R.id.contenthost1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("NBA联盟").setContent(R.id.contenthost2));
        //显示文本，文本定义在string.xml中
        basketballhistory = (TextView)findViewById(R.id.testviewhost1);
        basketballhistory.setText(R.string.basketballhistory);
        nba = (TextView)findViewById(R.id.testviewhost2);
        nba.setText(R.string.nba);
        TextPaint bp = basketballhistory.getPaint();
        bp.setFakeBoldText(true);
        TextPaint np = nba.getPaint();
        np.setFakeBoldText(true);
        //监听返回主页按钮
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Introduction.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}