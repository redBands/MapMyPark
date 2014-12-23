package com.mapmypark.mapmypark;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Park implements ClusterItem {
	
	private int id;
	private int ref;
	private LatLng parkPosition;
	private String title;
    private String snippet;
 
    public Park(){}
 
    public Park(LatLng position, int aRef, String aTitle, String aSnippet) {
        super();
        ref = aRef;
        parkPosition = position;
        title = aTitle;
        snippet = aSnippet;
    }
 
    public void setId(int anId)
    {
    	id = anId;
    }
    
    public int getID()
    {
    	return id;
    }
    
    public void setRef(int aRef)
    {
    	ref = aRef;
    }
    
    public int getRef()
    {
    	return ref;
    }
    
    public void setPosition(double aLat, double aLng)
    {
    	parkPosition = new LatLng(aLat, aLng);
    }
    
    public void setTitle(String aTitle)
    {
    	title = aTitle;
    }
    
    public String getTitle()
    {
    	return title;
    }
    
    public void setSnippet(String aSnippet)
    {
    	snippet = aSnippet;
    }
    
    public String getSnippet()
    {
    	return snippet;
    }
 
    @Override
    public String toString() {
        return "Park [ref=" + ref + ", lat=" + parkPosition.latitude +
        		", lng=" + parkPosition.longitude + "," +
        		"title=" + title + ", snippet= " + snippet + "]";
    }

	@Override
	public LatLng getPosition() {
		// TODO Auto-generated method stub
		return parkPosition;
	}

}
