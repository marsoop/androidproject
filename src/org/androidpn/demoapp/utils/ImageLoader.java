package org.androidpn.demoapp.utils;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 图片加载类 单例模式
 * @author dourl
 *
 */
@SuppressLint({ "NewApi", "HandlerLeak" })
public class ImageLoader {

	private static ImageLoader mImageLoader;
	/**
	 * 图片缓存核心对象
	 */
	private LruCache<String,Bitmap> mLruCache;
	/**
	 * 线程池
	 */
	private ExecutorService mThreadPool;
	
	private static final int DEAFULT_THREAD_COUNT =1;
	
	/**
	 * 队列默认调度方式
	 */
	private Type mType = Type.LIFO;
    public enum Type{
		
		FIFO,LIFO;
	}
	
	/**
	 * 任务队列 有头有尾
	 */
	private LinkedList<Runnable> mTaskQueuer;
	/**
	 * 后台轮询线程
	 */
	private Thread mPoolThread;
	
	/**
	 *被thread绑定的headler
	 *
	 */
	private Handler mPoolThreadHandler;
	/**
	 * ui
	 */
	private Handler mUIHandler;
	
	
	//防止实例
	private ImageLoader( int threadCount,Type type){
		
		init(threadCount,type);
		
	}
	/**
	 * 初始化操作
	 * @param threadCount
	 * @param type
	 */
	private void init(int threadCount, Type type) {
		// TODO Auto-generated method stub
		//后台轮询
		mPoolThread = new Thread(){

			@Override
			public void run() {
			  Looper.prepare();
			  mPoolThreadHandler = new Handler(){
				  @Override
				public void handleMessage(Message msg) {
					// 线程池去取一个任务进行执行
					  mThreadPool.execute(getTask());
					
				}

				
			  };
			  Looper.loop();
			};		
		};
		
		
		mPoolThread.start();
		//获取应用的最大可用内存
		int maxMemory = (int)Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory/8;
		//测量每个bitmap 的大小
		mLruCache = new LruCache<String, Bitmap>(cacheMemory){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes()* value.getHeight();
			}
		};
		//创建线程池
		mThreadPool = Executors.newFixedThreadPool(threadCount);
	    mTaskQueuer = new LinkedList<Runnable>();
	    mType = type;
	
	}

	private static ImageLoader getInstance(){
		
		if(mImageLoader==null){
			synchronized (ImageLoader.class) {
				if(mImageLoader==null){
					mImageLoader = new ImageLoader(DEAFULT_THREAD_COUNT,Type.LIFO);
				}
			}
			
		}
		return mImageLoader;
		
	}
	/**
	 * 从任务队列中取出一个方法
	 * @return
	 */
	private Runnable getTask() {
		// TODO Auto-generated method stub
		if(mType ==Type.FIFO){
			return mTaskQueuer.removeFirst();
		}else if(mType ==Type.LIFO){
			return mTaskQueuer.removeLast();
		}
		return null;
		
	}
	/**
	 * 初始化muiHandler
	 * @param path
	 * @param imageView
	 * 根据path 设置imageview
	 */
	public void loadImage(final String path, final ImageView imageView){
		imageView.setTag(path);	
		if(mUIHandler == null){
			mUIHandler = new Handler(){
				public void handleMessage(Message msg) {
					//获取得到的图片为imageView回调设置图片
					ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView imageView =holder.imageView;
					String path = holder.path;
					
					if(imageView.getTag().toString().equals(path)){
						imageView.setImageBitmap(bm);
					}
				};
			};
		}
		//根据path在缓存中获取bitmap
		Bitmap bm = getBitMapFromLruCache(path);
		
		if(bm!=null){
			Message msg = Message.obtain();
			ImageBeanHolder holder = new ImageBeanHolder();
			holder.bitmap =bm;
			holder.path=path;
			holder.imageView= imageView;
			msg.obj = holder;
			mUIHandler.sendMessage(msg);
		}else{
			addTasks(new Runnable() {
				
				@Override
				public void run() {
					// 加载图片
					//1图片的压缩 获取图片需要显示的大小
				ImageSize imageSize =	getImageViewSize(imageView);
					//2 压缩图片
				Bitmap bm  = decodeSampleBitmapFromPath(path ,imageSize.width,imageSize.height);
				}

				

				
			});
		}
	}
	
	/**
	 * 根据图片需要显示的宽 高 为图片压缩
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap decodeSampleBitmapFromPath(String path,
			int width, int height) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		return null;
	}
	
	/**
	 * 根据imageview 获取适当的压缩的宽和高
	 * @param imageView
	 * @return
	 */
	private ImageSize getImageViewSize(ImageView imageView) {
		ImageSize imageSize = new ImageSize();
		LayoutParams lp=imageView.getLayoutParams();
		//获取屏幕的宽度
		DisplayMetrics displayMetrics= imageView.getContext().getResources().getDisplayMetrics();
		//获取iamgview 的实际宽度
		int width = imageView.getWidth();
		if(width <=0){
			//获取 imageview 在layout中声明的宽度
			width =lp.width;
		}
		if(width <=0){
			//检查最大值
			width =imageView.getMaxWidth();
		}
		if(width <=0){
			//检查最大值
			width =displayMetrics.widthPixels;
		}
		
		//获取iamgview 的实际宽度
				int height = imageView.getHeight();
				if(height <=0){
					//获取 imageview 在layout中声明的宽度
					height =lp.height;
				}
				if(height <=0){
					//检查最大值
					height =imageView.getMaxHeight();
				}
				if(height <=0){
					//检查最大值
					height =displayMetrics.heightPixels;
				}
				
				imageSize.height=height;
				imageSize.width = width;
						
		return imageSize;
		// TODO Auto-generated method stub
		
	}
	
	private void addTasks(Runnable runnable) {
		mTaskQueuer.add(runnable);
		mPoolThreadHandler.sendEmptyMessage(0x110);
		
	}
	private Bitmap getBitMapFromLruCache(String key) {
		// TODO Auto-generated method stub
		return mLruCache.get(key);
	}
	
	private class ImageSize{
		int width;
		int height;
	}
	
	private class ImageBeanHolder{
		
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}
	
}
