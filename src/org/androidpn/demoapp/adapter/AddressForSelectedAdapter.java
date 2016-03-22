package org.androidpn.demoapp.adapter;

import java.util.List;

import org.androidpn.demoapp.R;
import org.androidpn.demoapp.model.SortModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AddressForSelectedAdapter extends BaseAdapter implements
		SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private ListView listView;
	private Bitmap[] bitmaps;
	protected ImageLoader imageLoader;
	private ImageLoadingListener animateFirstListener;
	DisplayImageOptions options_head; // DisplayImageOptions是用于设置图片显示的类

	public AddressForSelectedAdapter(Context mContext, List<SortModel> list,
			ListView listView, ImageLoader imageLoader,
			ImageLoadingListener animateFirstListener) {
		this.mContext = mContext;
		this.list = list;
		this.listView = listView;
		this.bitmaps = new Bitmap[list.size()];
		this.imageLoader = imageLoader;
		this.animateFirstListener = animateFirstListener;
		// 头像
		options_head = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_header) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_header) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.default_header) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565)// 使用RGB_565会比使用ARGB_8888少消耗2倍的内存
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//
				.displayer(new RoundedBitmapDisplayer(5)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		// 初始化数据
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public Bitmap getBitmap(int position) {
		return bitmaps[position];
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.item_contactlist_selected, null);
			// 显示的名称
			viewHolder.tvName = (TextView) view.findViewById(R.id.sort_name);
			// ABC
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.sort);
			viewHolder.tvUserName = (TextView) view
					.findViewById(R.id.to_user_name);

			viewHolder.imageView = (ImageView) view.findViewById(R.id.image);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		viewHolder.tvName.setText(this.list.get(position).getName());
		viewHolder.tvUserName.setText(this.list.get(position).getToUserName());
		updateBackground(position, view);
		/**
		 * 显示图片 参数1：图片url 参数2：显示图片的控件 参数3：显示图片的设置 参数4：监听器
		 */
		imageLoader.displayImage("drawable://" + R.drawable.default_header,
				viewHolder.imageView, options_head, animateFirstListener);

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvUserName;
		TextView tvName;
		ImageView imageView;
		CheckBox checkBox;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@SuppressLint("NewApi")
	private void updateBackground(int position, View view) {
		int backgroundId;

		if (listView.isItemChecked(position)) {

			backgroundId = R.drawable.list_selected_holo_light;
		} else {
			backgroundId = R.drawable.conversation_item_background_read;
		}
		Drawable background = mContext.getResources().getDrawable(backgroundId);
		view.setBackground(background);

	}

}