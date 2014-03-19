package com.quipmate.feature;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quipmate.R;
import com.quipmate.loadingBitmaps.ImageDownloader;

public class FriendListAdapter extends BaseAdapter{

	private List<FriendInfo> friendDataList;
	private ImageDownloader imageDownLoader;
	
	public FriendListAdapter(List<FriendInfo> friendDataList) {
		super();
		this.friendDataList = friendDataList;
		imageDownLoader = new ImageDownloader();
	}

	@Override
	public int getCount() {
		return friendDataList != null ? friendDataList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return friendDataList != null ? friendDataList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		FriendViewHolder friendViewHolder;
		if(view == null){
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.friend_info, parent,
					false);
			
			friendViewHolder = new FriendViewHolder();
			friendViewHolder.pimage = (ImageView) view.findViewById(R.id.pimage);
			friendViewHolder.name = (TextView) view.findViewById(R.id.name);
			
			view.setTag(friendViewHolder);
		} else {
			friendViewHolder = (FriendViewHolder) view.getTag();
		}
		
		final FriendInfo friend = friendDataList.get(position);
		if(friend != null){
			friendViewHolder.name.setText(friend.getName());
			imageDownLoader.download(friend.getImageURL(), friendViewHolder.pimage);
		}
		return view;
	}

	static class FriendViewHolder{
		ImageView pimage;
		TextView name;
	}
}
