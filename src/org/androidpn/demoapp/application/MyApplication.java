package org.androidpn.demoapp.application;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.androidpn.client.ServiceManager;
import org.androidpn.demoapp.R;
import org.androidpn.demoapp.manager.MessageManager;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApplication extends Application {
	// 全局的请求队列
	public static RequestQueue requestQueue;


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		requestQueue = Volley.newRequestQueue(getApplicationContext());

		// 初始化ImageLoader
		initImageLoader(getApplicationContext());
		// 静态标表情的装载
		//initFaceMap();

	}
	
	/*public Map<String, Integer> getFaceMap()
	{
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

	private void initFaceMap() {
		mFaceMap.put("[呲牙]", R.drawable._000);
		mFaceMap.put("[调皮]", R.drawable._001);
		mFaceMap.put("[流汗]", R.drawable._002);
		mFaceMap.put("[偷笑]", R.drawable._003);
		mFaceMap.put("[再见]", R.drawable._004);
		mFaceMap.put("[敲打]", R.drawable._005);
		mFaceMap.put("[擦汗]", R.drawable._006);
		mFaceMap.put("[猪头]", R.drawable._007);
		mFaceMap.put("[玫瑰]", R.drawable._008);
		mFaceMap.put("[流泪]", R.drawable._009);
		mFaceMap.put("[大哭]", R.drawable._010);
		mFaceMap.put("[嘘]", R.drawable._011);
		mFaceMap.put("[酷]", R.drawable._012);
		mFaceMap.put("[抓狂]", R.drawable._013);
		mFaceMap.put("[委屈]", R.drawable._014);
		mFaceMap.put("[便便]", R.drawable._015);
		mFaceMap.put("[炸弹]", R.drawable._016);
		mFaceMap.put("[菜刀]", R.drawable._017);
		mFaceMap.put("[可爱]", R.drawable._018);
		mFaceMap.put("[色]", R.drawable._019);
		mFaceMap.put("[害羞]", R.drawable._020);

		mFaceMap.put("[得意]", R.drawable._021);
		mFaceMap.put("[吐]", R.drawable._022);
		mFaceMap.put("[微笑]", R.drawable._023);
		mFaceMap.put("[发怒]", R.drawable._024);
		mFaceMap.put("[尴尬]", R.drawable._025);
		mFaceMap.put("[惊恐]", R.drawable._026);
		mFaceMap.put("[冷汗]", R.drawable._027);
		mFaceMap.put("[爱心]", R.drawable._028);
		mFaceMap.put("[示爱]", R.drawable._029);
		mFaceMap.put("[白眼]", R.drawable._030);
		mFaceMap.put("[傲慢]", R.drawable._031);
		mFaceMap.put("[难过]", R.drawable._032);
		mFaceMap.put("[惊讶]", R.drawable._033);
		mFaceMap.put("[疑问]", R.drawable._034);
		mFaceMap.put("[睡]", R.drawable._035);
		mFaceMap.put("[亲亲]", R.drawable._036);
		mFaceMap.put("[憨笑]", R.drawable._037);
		mFaceMap.put("[爱情]", R.drawable._038);
		mFaceMap.put("[衰]", R.drawable._039);
		mFaceMap.put("[撇嘴]", R.drawable._040);
		mFaceMap.put("[阴险]", R.drawable._041);

		mFaceMap.put("[奋斗]", R.drawable._042);
		mFaceMap.put("[发呆]", R.drawable._043);
		mFaceMap.put("[右哼哼]", R.drawable._044);
		mFaceMap.put("[拥抱]", R.drawable._045);
		mFaceMap.put("[坏笑]", R.drawable._046);
		mFaceMap.put("[飞吻]", R.drawable._047);
		mFaceMap.put("[鄙视]", R.drawable._048);
		mFaceMap.put("[晕]", R.drawable._049);
		mFaceMap.put("[大兵]", R.drawable._050);
		mFaceMap.put("[可怜]", R.drawable._051);
		mFaceMap.put("[强]", R.drawable._052);
		mFaceMap.put("[弱]", R.drawable._053);
		mFaceMap.put("[握手]", R.drawable._054);
		mFaceMap.put("[胜利]", R.drawable._055);
		mFaceMap.put("[抱拳]", R.drawable._056);
		mFaceMap.put("[凋谢]", R.drawable._057);
		mFaceMap.put("[饭]", R.drawable._058);
		mFaceMap.put("[蛋糕]", R.drawable._059);
		mFaceMap.put("[西瓜]", R.drawable._060);
		mFaceMap.put("[啤酒]", R.drawable._061);
		mFaceMap.put("[飘虫]", R.drawable._062);

		mFaceMap.put("[勾引]", R.drawable._063);
		mFaceMap.put("[OK]", R.drawable._064);
		mFaceMap.put("[爱你]", R.drawable._065);
		mFaceMap.put("[咖啡]", R.drawable._066);
		mFaceMap.put("[钱]", R.drawable._067);
		mFaceMap.put("[月亮]", R.drawable._068);
		mFaceMap.put("[美女]", R.drawable._069);
		mFaceMap.put("[刀]", R.drawable._070);
		mFaceMap.put("[发抖]", R.drawable._071);
		mFaceMap.put("[差劲]", R.drawable._072);
		mFaceMap.put("[拳头]", R.drawable._073);
		mFaceMap.put("[心碎]", R.drawable._074);
		mFaceMap.put("[太阳]", R.drawable._075);
		mFaceMap.put("[礼物]", R.drawable._076);
		mFaceMap.put("[足球]", R.drawable._077);
		mFaceMap.put("[骷髅]", R.drawable._078);
		mFaceMap.put("[挥手]", R.drawable._079);
		mFaceMap.put("[闪电]", R.drawable._080);
		mFaceMap.put("[饥饿]", R.drawable._081);
		mFaceMap.put("[困]", R.drawable._082);
		mFaceMap.put("[咒骂]", R.drawable._083);

		mFaceMap.put("[折磨]", R.drawable._084);
		mFaceMap.put("[抠鼻]", R.drawable._085);
		mFaceMap.put("[鼓掌]", R.drawable._086);
		mFaceMap.put("[糗大了]", R.drawable._087);
		mFaceMap.put("[左哼哼]", R.drawable._088);
		mFaceMap.put("[哈欠]", R.drawable._089);
		mFaceMap.put("[快哭了]", R.drawable._090);
		mFaceMap.put("[吓]", R.drawable._091);
		mFaceMap.put("[篮球]", R.drawable._092);
		mFaceMap.put("[乒乓球]", R.drawable._093);
		mFaceMap.put("[NO]", R.drawable._094);
		mFaceMap.put("[跳跳]", R.drawable._095);
		mFaceMap.put("[怄火]", R.drawable._096);
		mFaceMap.put("[转圈]", R.drawable._097);
		mFaceMap.put("[磕头]", R.drawable._098);
		mFaceMap.put("[回头]", R.drawable._099);
		mFaceMap.put("[跳绳]", R.drawable._100);
		mFaceMap.put("[激动]", R.drawable._101);
		mFaceMap.put("[街舞]", R.drawable._102);
		mFaceMap.put("[献吻]", R.drawable._103);
		mFaceMap.put("[左太极]", R.drawable._104);
		mFaceMap.put("[右太极]", R.drawable._105);
		mFaceMap.put("[闭嘴]", R.drawable._106);

	}
*/
	public static RequestQueue getHttpRequestQueue() {
		return requestQueue;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"apnImageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
