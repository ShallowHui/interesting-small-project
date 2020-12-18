package club.zunhuier;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class Star extends AppCompatActivity {

    private TextView common;
    private Button back;

    private ViewPager viewPager;
    private TextView name;
    private static final int[] images = {R.drawable.jorden,R.drawable.kobe,R.drawable.jms}; //图片资源数组

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_common);
        //设置标题
        common = (TextView)findViewById(R.id.common);
        common.setText("篮球巨星");
        TextPaint tp = common.getPaint();
        tp.setFakeBoldText(true);
        //监听返回主页按钮
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Star.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        //设置ViewPager容器，即可滑动的视图
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        MyAdapter myAdapter = new MyAdapter(images);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(myAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setPageMargin(66);
        //滑动视图时，修改巨星名字
        name = (TextView)findViewById(R.id.name);
        final Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/DroidSerif-BoldItalic.ttf");
        name.setTypeface(typeface);
        final TextPaint np = name.getPaint();
        np.setFakeBoldText(true);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        name.setText("Michael Jeffrey Jordan");
                        break;
                    case 1:
                        name.setText("Kobe Bean Bryant");
                        break;
                    case 2:
                        name.setText("LeBron Raymone James");
                        break;
                    default:
                        break;
                }
                name.setTypeface(typeface);
                np.setFakeBoldText(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}