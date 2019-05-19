package com.niyang.adviewpager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.niyang.adpviewpager.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity implements OnPageChangeListener {

	private ViewPager viewPager;
	private int[] imgIds;
	private List<ImageView> imsList;
	private LinearLayout ll_point;
	private String[] contents;
	private TextView tv_desc;
	private int lastEnablePoint = 0;
	private Timer timer=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化UI
		initUI();
		// 初始化数据
		initData();
		// 初始化viewpage适配器
		initAdapter();
		// 初始化自动,图片自动轮播
		initauto();
	}

	private void initauto() {
		timer = new Timer();
		timer.schedule(new MyTask(), 2000, 2000);
	}
	class MyTask extends TimerTask{

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
				}
			});
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (timer!=null) {
			timer.cancel();
		}
	}

	private void initAdapter() {
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(this);

		ll_point.getChildAt(0).setEnabled(true);

		viewPager.setCurrentItem(500000);
	}

	private void initData() {
		imgIds = new int[] { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e };

		contents = new String[] { "图片1", "图片2", "图片3", "图片4", "图片5" };
		ImageView imageView;
		imsList = new ArrayList<ImageView>();

		View point;
		LayoutParams layoutParams;
		for (int i = 0; i < imgIds.length; i++) {
			imageView = new ImageView(this);
			imageView.setBackgroundResource(imgIds[i]);
			imsList.add(imageView);

			// 加小白点,指示器
			point = new View(this);
			point.setBackgroundResource(R.drawable.selector_point);

			layoutParams = new LinearLayout.LayoutParams(5, 5);
			if (i != 0)
				layoutParams.leftMargin = 10;

			point.setEnabled(false);
			ll_point.addView(point, layoutParams);
			// ll_point.addView(point, 5, 5);
		}
	}

	private void initUI() {
		ll_point = (LinearLayout) findViewById(R.id.ll_point);

		viewPager = (ViewPager) findViewById(R.id.vp);

		tv_desc = (TextView) findViewById(R.id.tv_desc);

	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		// 是否可以复用(固定写法)
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// 返回要显示的内容
		// 创建条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			int newPosition = position % imsList.size();

			ImageView imageView = imsList.get(newPosition);
			container.addView(imageView);

			return imageView;
		}

		// 销毁条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View) object);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// 滚动状态变化时调用
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// 滚动时调用
	}

	@Override
	public void onPageSelected(int position) {

		int newPosition = position % imsList.size();
		// 选择时调用
		tv_desc.setText(contents[newPosition]);
		ll_point.getChildAt(lastEnablePoint).setEnabled(false);
		ll_point.getChildAt(newPosition).setEnabled(true);

		lastEnablePoint = newPosition;

	}

}
