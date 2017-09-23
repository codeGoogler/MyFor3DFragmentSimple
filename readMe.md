 高仿3D翻转切换效果

 效果图：

![高仿3D旋转效果.gif](http://upload-images.jianshu.io/upload_images/4614633-4be5ccaaafcd77c5.gif?imageMogr2/auto-orient/strip)


 ### 前言
 作为Android程序员，或者是想要去模仿一些酷炫的效果，或者是为了实现视觉的变态需求，或者是压抑不住内心的创造欲想要炫技，我们不可避免地需要做各种动画。


 ### 实现逻辑

 - 自定义Animation实现自己逻辑
 - 控制Matrix的旋转动画


#### 控件动画介绍

其实控件动画也是布局动画的一种，可以看做是自定义的动画实现，布局动画在XML中定义OPhone已经实现的几个动画效果（AlphaAnimation、TranslateAnimation、ScaleAnimation、RotateAnimation）而控件动画就是在代码中继承android.view.animation.Animation类来实现自定义效果。

#### 控件动画实现

通过重写Animation的 applyTransformation (float interpolatedTime, Transformation t)函数来实现自定义动画效果，另外一般也会实现 initialize (int width, int height, int parentWidth, int parentHeight)函数，这是一个回调函数告诉Animation目标View的大小参数，在这里可以初始化一些相关的参数，例如设置动画持续时间、设置Interpolator、设置动画的参考点等。

```
@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegrees;
		float degrees = fromDegrees
				+ ((mToDegrees - fromDegrees) * interpolatedTime);

		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();

		camera.save();
		if (mReverse) {
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
```

如何设置一个新的三维旋转的容器视图
```
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

```
利用fragment界面切换如何调用
```
button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).applyRotation(false,
						new Fragment_First(), 0, -90);
			}
		});
```
![](http://upload-images.jianshu.io/upload_images/4614633-f525a376741b6cb0.gif?imageMogr2/auto-orient/strip)


> 项目地址：
>
> [https://github.com/androidstarjack/MyBeautyWaveCusotomserButton](https://github.com/androidstarjack/MyBeautyWaveCusotomserButton/)
>


### 更多文章

[ 2017上半年技术文章集合—184篇文章分类汇总](http://blog.csdn.net/androidstarjack/article/details/77923753)

[ NDK项目实战—高仿360手机助手之卸载监听](http://blog.csdn.net/androidstarjack/article/details/77984865)


[高级UI特效仿直播点赞效果—一个优美炫酷的点赞动画](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=100000969&idx=1&sn=626d821d16346764fdce33e65f372031&chksm=6b4768575c30e14163ae8fb9f0406db0b3295ce47c4bc27b1df7a3abee1fa0bb71ef27b4e959#rd)

[一个实现录音和播放的小案例](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=100000959&idx=1&sn=a5acb0f44fbadeaa9351df067438922c&chksm=6b4768215c30e1371a3c750f2b826f38b3a263c937272ae208717f73f92ed3e8fd8b6a674686#rd)

#### 相信自己，没有做不到的，只有想不到的

![加入大牛圈](http://img.blog.csdn.net/20170910215455020?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYW5kcm9pZHN0YXJqYWNr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

 如果你觉得此文对您有所帮助，欢迎入群 QQ交流群 ：644196190
微信公众号：终端研发部

![技术+职场](https://user-gold-cdn.xitu.io/2017/8/1/d354d51a5c58fb8a5ba576f2d9ea7a8e)


