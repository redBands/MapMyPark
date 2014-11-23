package com.mapmypark.mapmypark;

import java.util.*;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;

public class MultiListener implements OnCameraChangeListener {
	
	List<OnCameraChangeListener> mListeners = new ArrayList<OnCameraChangeListener>();

	@Override
	public void onCameraChange(CameraPosition aPosition) {
		// TODO Auto-generated method stub
		for (OnCameraChangeListener ccl : mListeners)
            ccl.onCameraChange(aPosition);

	}

}
