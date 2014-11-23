package com.mapmypark.mapmypark;

public class Park {
	
	private int id;
	private int ref;
	private double lat;
	private double lng;
    private String title;
    private String snippet;
 
    public Park(){}
 
    public Park(int aRef, double aLat, double aLng, String aTitle, String aSnippet) {
        super();
        ref = aRef;
        lat = aLat;
        lng = aLng;
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
    
    public void setLat(double aLat)
    {
    	lat = aLat;
    }
    
    public double getLat()
    {
    	return lat;
    }
    
    public void setLng(double aLng)
    {
    	lng = aLng;
    }
    
    public double getLng()
    {
    	return lng;
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
        return "Park [ref=" + ref + ", lat=" + lat + ", lng=" + lng + "," +
        		"title=" + title + ", snippet= " + snippet + "]";
    }

}
