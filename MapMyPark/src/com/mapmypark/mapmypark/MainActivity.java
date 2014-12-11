package com.mapmypark.mapmypark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import android.os.Bundle;

//@SuppressLint("NewApi")
public class MainActivity extends Activity implements ClusterManager.OnClusterClickListener<Park>, ClusterManager.OnClusterInfoWindowClickListener<Park>, ClusterManager.OnClusterItemClickListener<Park>, ClusterManager.OnClusterItemInfoWindowClickListener<Park> {
	
	private GoogleMap mMap;
	private ParksDB mDB;
	private LatLng ne = new LatLng(55.940680, -3.184002);
	private LatLng sw = new LatLng(55.890984, -3.325909);
	LatLngBounds bounds = new LatLngBounds(sw,ne);
	// Declare a variable for the cluster manager.
    private ClusterManager<Park> mClusterManager;
    // Declare a variable for the park cluster renderer
    private ParkClusterRenderer pcr;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //check map availability
        setUpMapIfNeeded();
        
        // initiate renderer
        //pcr = new ParkClusterRenderer(this, mMap, mClusterManager);
        
        mDB = new ParksDB(this);
        
        try
        {
        	String file = "england.csv";
        	mDB.importParks(file);
        }
        catch (IOException e)
        {
           System.out.println(e);
        }
        catch (Exception e)
        {
           System.out.println(e);
           System.out.println("Class Tester may still be incomplete.");
        }
        
        mMap.setMyLocationEnabled(true);
        
        //attempt to limit the number of markers
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ne, 13));
    	
        //bounds = new LatLngBounds(sw, ne);
        
        // Set up the Cluster Manager
        setUpClusterer();
    }
    
    //add parks to cluster manager
    private void addItems(){
        /*bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
    	Projection projection = mMap.getProjection();
    	System.out.println("bounds is: " + projection.toString());*/
    	//bounds = pcr.getBounds();
    	//bounds = new LatLngBounds(sw,ne);
		System.out.println("bounds1 is: " + bounds.toString());
    	//mClusterManager.addItems(mDB.getAllParks());
		mClusterManager.clearItems();
    	mClusterManager.addItems(mDB.getParksInRange2(bounds));
    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                                .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }
    
    private void setUpClusterer() {
    	// Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ne, 13));
        
        // Initialise the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Park>(this, mMap);
        pcr = new ParkClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(pcr);
        //mClusterManager.setRenderer(new ParkClusterRenderer(this, mMap, mClusterManager));
        
      // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        MultiListener ml = new MultiListener();
        ml.addListener(mClusterManager);
        ml.addListener(new OnCameraChangeListener(){

			@Override
			public void onCameraChange(CameraPosition arg0) {
				// TODO Auto-generated method stub
				bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
				System.out.println("ml bounds is: " + bounds.toString());
				addItems();
				mClusterManager.cluster();
			}
        	
        });
        mMap.setOnCameraChangeListener(ml);
        //mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        // Add cluster items (markers) to the cluster manager.
        //addItems();
    }
    
    
    
    
	@Override
	public void onClusterItemInfoWindowClick(com.mapmypark.mapmypark.Park item) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onClusterItemClick(com.mapmypark.mapmypark.Park aPark) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onClusterInfoWindowClick(
			Cluster<com.mapmypark.mapmypark.Park> cluster) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onClusterClick(
			Cluster<com.mapmypark.mapmypark.Park> cluster) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public class MultiListener implements OnCameraChangeListener
	{
	    List<OnCameraChangeListener> mListeners = new ArrayList<OnCameraChangeListener>();

	    @Override
		public void onCameraChange(CameraPosition camPos)
	    {
	        for (OnCameraChangeListener ccl : mListeners)
	            ccl.onCameraChange(camPos);
	    }

	    // Other methods to add, remove listeners
	    
	    public void addListener(OnCameraChangeListener occl){
	    	mListeners.add(occl);
	    }
	

		}
}