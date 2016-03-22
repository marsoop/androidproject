package org.androidpn.demoapp.activity;

import org.androidpn.demoapp.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TagActivity extends BaseActivity implements OnClickListener {
    private View tagActionBarView;
    private TextView newTagTv,allTagTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    setContentView(R.layout.activity_taglist);
	    initView();
	}
	@SuppressLint("InlinedApi")
	private void initView() {
      if(tagActionBarView == null){
    	  tagActionBarView = LayoutInflater.from(context).inflate(R.layout.tag_select_actionbar, null);
    	  newTagTv = (TextView) tagActionBarView.findViewById(R.id.tag_add_newtv);
          allTagTv = (TextView) tagActionBarView.findViewById(R.id.tag_alltv);
      }
      getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_CUSTOM); 
      //ActionBar.DISPLAY_SHOW_CUSTOM 显示自定义的view
      getActionBar().setCustomView(tagActionBarView);
      getActionBar().setDisplayHomeAsUpEnabled(true);
      
      newTagTv.setText(R.string.tag_add_new);
      newTagTv.setOnClickListener(this);
      allTagTv.setText(R.string.tag_all);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
         switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return false;
		}
		

	}
	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.tag_add_newtv:
			startActivity(new Intent(this,AddressForSelectActivity.class));
			break;

		default:
			break;
		}
		
	}
	
	
	
	
	
	
	
	
}


