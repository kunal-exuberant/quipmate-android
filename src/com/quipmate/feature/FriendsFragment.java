package com.quipmate.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.quipmate.R;
import com.quipmate.constants.AppProperties;
import com.quipmate.utils.JSONTask;

public class FriendsFragment extends Fragment {

	private JSONArray result,friendsId;
	private JSONTask jsonTask;
	private JSONObject  friendsName, friendsImage;
	private ListView friendListView;
	private Session session;
	
	private String action = "friend_fetch";

	private List<FriendInfo> friendDataList;
	private FriendListAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		session = new Session(getActivity());
		List<NameValuePair> apiParams = new ArrayList<NameValuePair>();
		apiParams.add(new BasicNameValuePair(AppProperties.ACTION, action));
		apiParams.add(new BasicNameValuePair(AppProperties.PROFILE_ID, session
				.getValue(AppProperties.PROFILE_ID)));
		jsonTask = new JSONTask(AppProperties.URL, AppProperties.METHOD_GET,
				apiParams);
		try {
			result = jsonTask.execute().get();
			JSONObject data = result.getJSONObject(0);
			if (data != null) {
				if (!data.has(getString(R.string.error))) {
					friendsId = data.getJSONArray(AppProperties.ACTION);
					friendsName = data.getJSONObject(AppProperties.NAME);
					friendsImage = data.getJSONObject(AppProperties.PROFILE_IMAGE);
					
					friendDataList = new ArrayList<FriendInfo>();
					// ready data for adapter
					String id;
					String name = null;
					String imageURL = null;
					for(int i = 0; i<friendsId.length(); i++){
						 id = friendsId.getString(i);
						 if(id != null){
							 if(friendsName != null){
								 name = friendsName.getString(id);
							 }
							 
							 if(friendsImage != null){
								 imageURL = friendsImage.getString(id);
							 }
							 
							 friendDataList.add(new FriendInfo(name, imageURL));
						 }
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.friends_fragment, null);
		friendListView = (ListView) view.findViewById(R.id.friendListView);
		return view;

	}

	@Override
	public void onStart() {
		if(friendListView != null){
			adapter = new FriendListAdapter(friendDataList);
			friendListView.setAdapter(adapter);
		}
		super.onStart();
		
	}

	
}
