package com.mapmypark.mapmypark;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class ParkClusterRenderer extends DefaultClusterRenderer<Park> {

	public ParkClusterRenderer(Context context, GoogleMap map, ClusterManager<Park> pcm) {
		super(context, map, pcm);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onBeforeClusterItemRendered(Park park, MarkerOptions mOp){
		super.onBeforeClusterItemRendered(park, mOp);
		
		mOp.title(park.getTitle());
		mOp.snippet(park.getSnippet());
	}
	
	

}
