package com.quipmate.feature;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quipmate.R;

public class CustomFriendAdapter extends BaseAdapter{

	private List<FriendInfo> data ;
	private Context context;
	
	
	public CustomFriendAdapter(List<FriendInfo> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@SuppressWarnings("unused")
	private void changeData(List<FriendInfo> data){
		this.data = data;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return data != null ? data.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return data != null ? data.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.friend_info, parent, false);

			holder = new ViewHolder();
			
			holder.mName = (TextView) view.findViewById(R.id.name);
			holder.mImage = (ImageView) view.findViewById(R.id.pimage);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		return view;
	}

	private static class ViewHolder{
		ImageView mImage;
		TextView mName;
	}
}
