package com.mapmypark.mapmypark;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ParksDB extends SQLiteOpenHelper {
	
	/** Database name */
    private static String DBNAME = "parkfindersqlite";
 
    /** Version number of the database */
    private static int VERSION = 1;
 
    /** Field 1 of the table parks, which is the primary key */
    public static final String FIELD_ROW_ID = "_id";
 
    /** Field 2 of the table parks, which is the primary key */
    public static final String FIELD_REF = "ref";
 
    /** Field 3 of the table parks, stores the latitude */
    public static final String FIELD_LAT = "lat";
 
    /** Field 4 of the table parks, stores the longitude*/
    public static final String FIELD_LNG = "lng";
 
    /** Field 5 of the table parks, stores the title of the park*/
    public static final String FIELD_TITLE = "ttl";
    
    /** Field 6 of the table parks, stores the snippet of the park*/
    public static final String FIELD_SNIPPET = "snp";
    
    /** A constant, stores the the table name */
    private static final String DATABASE_TABLE = "parks";
 
    /** An instance variable for SQLiteDatabase */
    //private SQLiteDatabase mDB;
    
    private static final String[] COLUMNS = {FIELD_ROW_ID,FIELD_REF,FIELD_LAT,
    	FIELD_LNG,FIELD_TITLE,FIELD_SNIPPET};
    
    // Declare variables for input streams...
   	private InputStream is;
   	private BufferedReader br;

   	// The csv fields currently in use are:
   	// ref, lat, lng, title, snp,
   	private int numberOfFields = 5;
   	
   	//a context
   	private final Context myContext;
   	
   	//String to hold database file path
   	private static String DB_FILE;
 
    /** Constructor */
    public ParksDB(Context context) {
        super(context, DBNAME, null, VERSION);
        //this.mDB = getWritableDatabase();
        myContext = context;
        File outFile =myContext.getDatabasePath(DBNAME);
        DB_FILE = outFile.getPath() ;
    }
    
    /** This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called
     * provided the database does not exist
     * */
     @Override
     public void onCreate(SQLiteDatabase db) {
         String sql =     "create table " + DATABASE_TABLE + " ( " +
                          FIELD_ROW_ID + " integer primary key autoincrement, " +
                          FIELD_REF + " integer , " +
                          FIELD_LAT + " double , " +
                          FIELD_LNG + " double , " +
                          FIELD_TITLE + " text , " +
                          FIELD_SNIPPET + " text " +
                          " ) ";
  
         db.execSQL(sql);
     }
     
     public void addPark(Park aPark)
     {
    	 //for logging
    	 Log.d("addPark ", aPark.toString());
    	 
    	 SQLiteDatabase mDB = this.getWritableDatabase();
    	 
    	 //1. create ContentValues to add key "column"/value
    	 ContentValues values = new ContentValues();
    	 values.put(FIELD_REF, aPark.getRef()); //get OSM ref
    	 values.put(FIELD_LAT, aPark.getLat()); //get lat
    	 values.put(FIELD_LNG, aPark.getLng()); //get lng
    	 values.put(FIELD_TITLE, aPark.getTitle()); //get title
    	 values.put(FIELD_SNIPPET, aPark.getSnippet()); //get snippet
    	 
    	 //2. insert
    	 mDB.insert(DATABASE_TABLE,
    			 null, //nullColumnHack
    			 values); //key/value -> keys = column names/ values = column values
    	 
         // 4. close
         mDB.close();
    	 
     }
     
     public Park getPark(int id)
     {

    	 SQLiteDatabase mDB = this.getReadableDatabase();
    	 
    	 //1. BUILD QUERY
    	 Cursor cursor =
    			 mDB.query(DATABASE_TABLE, // a. THE TABLE
    					 COLUMNS, // b. COLUMN NAMES
    					 " _id = ?", // c. SELECTIONS
    					 new String[] { String.valueOf(id) }, // d. selections args
    			         null, // e. group by
    			         null, // f. having
    			         null, // g. order by
    			         null); // h. limit
    	 //2. If we got results get the first
    	 if (cursor != null)
    		 cursor.moveToFirst();
    	 
    	 //3. build park object
    	 Park aPark = new Park();
    	 aPark.setId(Integer.parseInt(cursor.getString(0)));
    	 aPark.setRef(cursor.getInt(1));
    	 aPark.setLat(cursor.getDouble(2));
    	 aPark.setLng(cursor.getDouble(3));
    	 aPark.setTitle(cursor.getString(4));
    	 aPark.setSnippet(cursor.getString(5));
    	 
    	 //log
    	 Log.d("getPark("+id+")", aPark.toString());
    	 
    	 //4. return park
    	 return aPark;
     }
     
     // Get All Parks
     public List<Park> getAllParks() {
         List<Park> parks = new LinkedList<Park>();
  
         // 1. build the query
         String query = "SELECT  * FROM " + DATABASE_TABLE;
  
         // 2. get reference to writable DB
         SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(query, null);
  
         // 3. go over each row, build park and add it to list
         Park park = null;
         if (cursor.moveToFirst()) {
             do {
                 park = new Park();
                 park.setId(Integer.parseInt(cursor.getString(0)));
                 park.setRef(cursor.getInt(1));
                 park.setLat(cursor.getDouble(2));
                 park.setLng(cursor.getDouble(3));
                 park.setTitle(cursor.getString(4));
                 park.setSnippet(cursor.getString(5));
  
                 // Add park to parks
                 parks.add(park);
             } while (cursor.moveToNext());
         }
  
         //Log.d("getAllParks()", parks.toString());
         Log.d("number of parks in database: ", Integer.toString(parks.size()));
  
         // return parks
         return parks;
     }
     
     
     /** Inserts a new location to the table locations */
     /*public long insert(ContentValues contentValues){
         long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
         return rowID;
     }
     
     /** Deletes all locations from the table 
     public int del(){
         int cnt = mDB.delete(DATABASE_TABLE, null , null);
         return cnt;
     }*/
     
     /** Returns all the locations from the table
     public Cursor getAllLocations(){
         return mDB.query(DATABASE_TABLE, new String[] { FIELD_ROW_ID,  FIELD_LAT , FIELD_LNG, FIELD_TITLE, FIELD_SNIPPET } , null, null, null, null, null);
     } */
     
     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     }
     
     public void importParks (String source) throws IOException	{
    	 
    	 if (checkDataBase() == false){
    	 SQLiteDatabase mDB = this.getWritableDatabase();
    	 
    	  // Open IO streams.
	      openStreams(source);
	      // Line to be read from the source file.
	      String inString;
	      // Array to hold tokens extracted from input line.
	      String[] fieldContents =  new String[numberOfFields];
	      
	      inString = br.readLine();
	      // Read the source line by line.
	      while (inString != null)
	      {
	         //alternative using String.split
	    	  fieldContents = inString.split(",",numberOfFields);

	         //add array data to parks database
	         
	         //1. create ContentValues to add key "column"/value
	    	 ContentValues values = new ContentValues();
	    	 values.put(FIELD_REF, fieldContents[0]); //get ref
	    	 values.put(FIELD_LAT, fieldContents[1]); //get lat
	    	 values.put(FIELD_LNG, fieldContents[2]); //get lng
	    	 values.put(FIELD_TITLE, fieldContents[3]); //get title
	    	 values.put(FIELD_SNIPPET, fieldContents[4]); //get snippet
	         
	    	//2. insert
	    	 mDB.insert(DATABASE_TABLE,
	    			 null, //nullColumnHack
	    			 values); //key/value -> keys = column names/ values = column values
	    	 
	         //3. close
	         //mDB.close();
	         
	         // Proceed to the next line of input.
	         inString = br.readLine();
	      }
	      // Close IO streams.
	      closeStreams();
	   }}
     
     public void openStreams(String input) throws
	   IOException
	   {
    	 //initiate the buffered reader from the csv file
    	 is = myContext.getAssets().open(input);
    	 br = new BufferedReader(new InputStreamReader(is));
	   }


	   public void closeStreams() throws IOException
	   {
	      // Close input streams...
	      is.close();
	      br.close();
	   }
	   
	   /**
	     * Check if the database already exist to avoid re-copying the file each time you open the application.
	     * @return true if it exists, false if it doesn't
	     */
	    private boolean checkDataBase(){
	 
	    	SQLiteDatabase checkDB = null;
	 
	    	try{
	    		String myPath = DB_FILE;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    	}catch(SQLiteException e){
	 
	    		//database does't exist yet.
	 
	    	}
	 
	    	if(checkDB != null){
	 
	    		checkDB.close();
	 
	    	}
	 
	    	return checkDB != null ? true : false;
	    }
	    
	    // Get park  within a defined Lat Lng range
	    
	    public List<Integer> getParksInView(Double nLat, Double sLat, Double eLng, Double wLng) {
	         List<Integer> iDs = new LinkedList<Integer>();
	         // get reference to writable DB
	         SQLiteDatabase db = this.getWritableDatabase();
	         
	         // Build the query
	         Cursor cursor =
	    			 db.query(DATABASE_TABLE, // a. THE TABLE
	    					 COLUMNS, // b. COLUMN NAMES
	    					 FIELD_LAT + " BETWEEN " + "?" + " and " + "?" + " and "
	    							 + FIELD_LNG + " BETWEEN " + "?" + " and " + "?", // c. SELECTIONS
	    					 new String[] { sLat.toString(),nLat.toString(),
	    					 wLng.toString(), eLng.toString()}, // d. selections args
	    			         null, // e. group by
	    			         null, // f. having
	    			         null, // g. order by
	    			         null); // h. limit
	  
	         // go over each row, build park and add it to list
	         if (cursor.moveToFirst()){	 //working
	             do {
	            	 // Add park id to iDs
	            	 iDs.add(Integer.parseInt(cursor.getString(0)));
	             } while (cursor.moveToNext());
	         }
	    	 return iDs;
	    }

}
