package club.zunhuier;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * ViewPager的适配器，显示视图
 */
public class MyAdapter extends PagerAdapter {

    private int[] images;

    public MyAdapter(int[] images){
        this.images = images;
    }

    @Override
    public int getCount() {
        return 3; //只显示3个视图
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(this.images[position]);//ImageView设置图片
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView); // 添加到ViewPager容器
        return imageView;// 返回填充的View对象
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}