package com.mapmypark.mapmypark;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class ParkClusterRenderer extends DefaultClusterRenderer<Park>{ //implements OnCameraChangeListener {
	
	//private GoogleMap mMap;
	//LatLngBounds bounds;

	public ParkClusterRenderer(Context context, GoogleMap map, ClusterManager<Park> pcm) {
		super(context, map, pcm);
		//mMap = map;
		// TODO Auto-generated constructor stub
	}
	/*
	public LatLngBounds getBounds(){
		return bounds;
	}*/
	
	@Override
	protected void onBeforeClusterItemRendered(Park park, MarkerOptions mOp){
		super.onBeforeClusterItemRendered(park, mOp);
		
		mOp.title(park.getTitle());
		mOp.snippet(park.getSnippet());
	}

	/*@Override
	public void onCameraChange(CameraPosition cPos) {
		// TODO Auto-generated method stub
		bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
		//System.out.println("bounds is: " + bounds.toString());
		//mMap.;
		//cluster();
	}*/
	
	

}
