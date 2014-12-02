package com.mapmypark.mapmypark;

import java.io.IOException;
//import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import com.google.android.gms.maps.*;
//import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.Cluster;
//import com.google.maps.android.MarkerManager;
//import com.google.maps.android.MarkerManager.Collection;
import com.google.maps.android.clustering.ClusterManager;

import android.os.Bundle;

//@SuppressLint("NewApi")
public class MainActivity extends Activity implements ClusterManager.OnClusterClickListener<Park>, ClusterManager.OnClusterInfoWindowClickListener<Park>, ClusterManager.OnClusterItemClickListener<Park>, ClusterManager.OnClusterItemInfoWindowClickListener<Park> {
	
	private GoogleMap mMap;
	private ParksDB mDB;
	private List <Park> parkList = new LinkedList<Park>();
	//private List <Integer> idList = new LinkedList<Integer>();
	private LatLng ne = new LatLng(55.945498, -3.180704);
	//private LatLng sw = new LatLng(55.921594, -3.216367);
	//private LatLngBounds currentBounds = new LatLngBounds(sw,ne);
	// Declare a variable for the cluster manager.
    private ClusterManager<Park> mClusterManager;
	//private MarkerManager markMan;
	//private MarkerManager.Collection parks = markMan.newCollection("parks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //check map availability
        setUpMapIfNeeded();
        
        //setup marker manager
        //markMan = new MarkerManager(mMap);
        
        mDB = new ParksDB(this);
        
        try
        {
        	String file = "all_scotland_playparks.txt";
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
        /*
        //get the bounding lat and lng for the current screen
        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                currentBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                //selectMarkers();//fetchData(currentBounds);
            }
        });*/
        /*MultiListener mL = new MultiListener();
        mL.add(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                currentBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                selectMarkers();//fetchData(currentBounds);
            }
        });*/
        mMap.setMyLocationEnabled(true);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ne, 13));
        //mMap.setOnCameraChangeListener(mL);
        setUpClusterer();
    	
    }
    /*
    private void selectMarkers()
    {
    	
    	LatLng nE = currentBounds.northeast;
        LatLng sW = currentBounds.southwest;
        Double latN = nE.latitude;
        Double latS = sW.latitude;
        Double lngE = nE.longitude;
        Double lngW = sW.longitude;
        List <Park> parkList = new LinkedList<Park>();
        System.out.println("parkList size1: " + parkList.size());
        
        //get parks in view id's
        idList = null;
        idList = mDB.getParksInView(latN, latS, lngE, lngW); //this works :)
        
        //add parks in view to the parkList
        for (int i =0; i<idList.size(); i++)
        {
        	parkList.add(mDB.getPark(idList.get(i)));
        }
        System.out.println("parkList size2: " + parkList.size());
        mMap.clear();
        
        addMarkers();
        
        //place the park markers
        for (int i = 0; i < parkList.size(); i++)
        {
        	Park aPark = parkList.get(i);
        	//LatLng loc = new LatLng(aPark.getLat(), aPark.getLng());
        	//mMap.setMyLocationEnabled(true);
        	//mMap.addMarker(new MarkerOptions()
        		//.title(aPark.getTitle())
        		//.snippet(aPark.getSnippet())
        		//.position(loc));
        	MyItem anItem = new MyItem(aPark.getLat(), aPark.getLng());
        	mClusterManager.addItem(anItem);
        	//mClusterManager.getMarkerCollection();
        }
    	Collection<Marker> markers = parks.getMarkers();
    	for (Marker m : markers)
    	{
    		MyItem anItem = new MyItem(m.getPosition().latitude,m.getPosition().longitude);
        	mClusterManager.addItem(anItem);
    	}
    }*/
    
    //add parks to cluster manager
    private void addItems(){
    	//List <Park> parkList = new LinkedList<Park>();
    	//MarkerManager.Collection parks = markMan.newCollection("parks");
    	System.out.println("parkList size01: " + parkList.size());
		parkList = mDB.getAllParks();
		System.out.println("parkList size02: " + parkList.size());
    	/*for (int i =0; i<parkList.size(); i++){
    		Park aPark = parkList.get(i);
    		MarkerOptions mO = new MarkerOptions();
    		//LatLng loc = new LatLng(aPark.getLat(), aPark.getLng());
    		mO.position(new LatLng(aPark.getLat(), aPark.getLng()));
    		mO.snippet(aPark.getSnippet());
    		mO.title(aPark.getTitle());
    		parks.addMarker(mO);
    	}*/
		mClusterManager.addItems(parkList);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ne, 13));
        
        // Initialise the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<Park>(this, mMap);
        mClusterManager.setRenderer(new ParkClusterRenderer(this, mMap, mClusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        
        // Add cluster items (markers) to the cluster manager.
        addItems();
    }
	@Override
	public void onClusterItemInfoWindowClick(com.mapmypark.mapmypark.Park item) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onClusterItemClick(com.mapmypark.mapmypark.Park aPark) {
		// TODO Auto-generated method stub
		aPark.getTitle();
		return true;
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
}