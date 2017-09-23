package mg.yyh.com.myfor3dfragmentsimple;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
/**
 * 类功能描述：</br>
 * 仿立体翻转效果
 * 博客地址：http://blog.csdn.net/androidstarjack
 * 公众号：终端研发部
 * @author yuyahao
 * @version 1.0 </p> 修改时间：</br> 修改备注：</br>
 */

public class MainActivity extends FragmentActivity {
	private FrameLayout framelayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		framelayout = (FrameLayout) findViewById(R.id.fragment_content);
		ReplaceFragmentMethod();
	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod() {
		FragmentTransaction tration = getSupportFragmentManager()
				.beginTransaction();
		tration.replace(R.id.fragment_content, new Fragment_First());
		tration.commit();
	}

	/**
	 * 设置一个新的三维旋转的容器视图。只翻一般，然后设置新的现实内容
	 * 
	 * @param zheng
	 *            一个判断机制 如果为true 则向右翻转，如果false则向左翻转
	 * @param fragment
	 *            传入的片段
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 */
	public void applyRotation(final boolean zheng, final Fragment fragment,
			final float start, final float end) {
		// Find the center of the container
		final float centerX = framelayout.getWidth() / 2.0f;
		final float centerY = framelayout.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Util_Rotate3DAnimation rotation = new Util_Rotate3DAnimation(
				start, end, centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(zheng, fragment));// 添加监听执行现实内容的切换
		framelayout.startAnimation(rotation);// 执行上半场翻转动画
	}

	/**
	 * 执行完上半部分旋转之后，设置要显示的新的View然后继续执行下半部分旋转
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final boolean mPosition;
		private final Fragment mfragment;

		private DisplayNextView(boolean zheng, Fragment fragment) {
			mPosition = zheng;
			mfragment = fragment;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			framelayout.post(new SwapViews(mPosition, mfragment));// 添加新的View
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * 添加要显示的新的View，并执行下半部分的旋转操作
	 */
	private final class SwapViews implements Runnable {
		private final boolean mPosition;
		private final Fragment mfragment;

		public SwapViews(boolean position, Fragment fragment) {
			mPosition = position;
			mfragment = fragment;
		}

		public void run() {
			final float centerX = framelayout.getWidth() / 2.0f;
			final float centerY = framelayout.getHeight() / 2.0f;
			Util_Rotate3DAnimation rotation;
			FragmentTransaction tration = getSupportFragmentManager()
					.beginTransaction();
			tration.replace(R.id.fragment_content, mfragment);
			if (mPosition) {
				rotation = new Util_Rotate3DAnimation(-90, 0, centerX, centerY,
						310.0f, false);
			} else {
				rotation = new Util_Rotate3DAnimation(90, 0, centerX, centerY,
						310.0f, false);
			}
			tration.commit();
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			framelayout.startAnimation(rotation);
		}
	}

}
