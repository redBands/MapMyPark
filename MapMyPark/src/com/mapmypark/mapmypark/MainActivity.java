package com.mapmypark.mapmypark;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.MarkerManager;
//import com.google.maps.android.clustering.ClusterManager;

import android.os.Bundle;

//@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private GoogleMap mMap;
	private ParksDB mDB;
	//private List <Park> parkList = new LinkedList<Park>();
	private List <Integer> idList = new LinkedList<Integer>();
	private LatLng ne = new LatLng(55.945498, -3.180704);
	private LatLng sw = new LatLng(55.921594, -3.216367);
	private LatLngBounds currentBounds = new LatLngBounds(sw,ne);
	// Declare a variable for the cluster manager.
    //private ClusterManager<MyItem> mClusterManager;
	private MarkerManager markMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //check map availability
        setUpMapIfNeeded();
        
        //setup marker manager
        markMan = new MarkerManager(mMap);
        
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
        
        //get the bounding lat and lng for the current screen
        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                currentBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                selectMarkers();//fetchData(currentBounds);
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ne, 13));
    	
    }
    
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
        /*
        //place the park markers
        for (int i =0; i<parkList.size(); i++)
        {
        	Park aPark = parkList.get(i);
        	LatLng loc = new LatLng(aPark.getLat(), aPark.getLng());
        	mMap.setMyLocationEnabled(true);
        	mMap.addMarker(new MarkerOptions()
        		.title(aPark.getTitle())
        		.snippet(aPark.getSnippet())
        		.position(loc));
        	//MyItem anItem = new MyItem(aPark.getLat(), aPark.getLng());
        	//mClusterManager.addItem(anItem);
        }*/
    }
    
    //add markers to marker manager
    private void addMarkers(){
    	List <Park> parkList = new LinkedList<Park>();
    	MarkerManager.Collection parks = markMan.newCollection("parks");
		//parkList = mDB.getAllParks();
    	for (int i =0; i<parkList.size(); i++){
    		Park aPark = parkList.get(i);
    		MarkerOptions mO = new MarkerOptions();
    		LatLng loc = new LatLng(aPark.getLat(), aPark.getLng());
    		mO.position(loc);
    		mO.snippet(aPark.getSnippet());
    		mO.title(aPark.getTitle());
    		parks.addMarker(mO);
    	}
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
    
    /*private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ne, 13));

        // Initialise the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        selectMarkers();
    }*/
}