package com.junyangwang.monkeyrescue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {
	
	public static final String KEY_ROWID = "id";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_SCORE = "score";
	
	private static final String DATABASE_NAME = "MonkeyRescue";
	private static final String DATABASE_TABLE_NAME = "MonkeyRescueTable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
						KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						KEY_LEVEL + " INT, " +
						KEY_SCORE + " INT);"		
			);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
			onCreate(db);
		}
		
	}
	
	public Database(Context c){
		ourContext = c;
	}
	
	public Database open(){
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		//ourHelper.onUpgrade(ourDatabase, 1, 1);
		if (!isTableExists(DATABASE_TABLE_NAME)){
			ourHelper.onCreate(ourDatabase);
		}
		return this;
	}
	
	public void close(){
		ourHelper.close();
	}
	
	public boolean isTableExists(String tableName) {
	    Cursor c = ourDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
	    if(c!=null) {
	        if(c.getCount()>0) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void createEntry(int level, int score){
		
		int max = this.getHighestLevel();
		
		if (level > max){
			ContentValues cv = new ContentValues();
			cv.put(KEY_LEVEL, level);
			cv.put(KEY_SCORE, score);
			
			ourDatabase.insert(DATABASE_TABLE_NAME, null, cv);
		}
	}
	
	public int getHighestLevel(){
		String[] columns = new String[]{KEY_ROWID, KEY_LEVEL, KEY_SCORE};
		Cursor c = ourDatabase.query(DATABASE_TABLE_NAME, columns, null, null, null, null, KEY_LEVEL + " DESC");
		
		int max = -1;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if (c.getInt(1) > max){
				max = c.getInt(1);
			}
		}
		
		return max;
	}
	
	public void deleteAllLevels(){
		String[] columns = new String[]{KEY_ROWID, KEY_LEVEL, KEY_SCORE};
		Cursor c = ourDatabase.query(DATABASE_TABLE_NAME, columns, null, null, null, null, KEY_LEVEL + " DESC");
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			ourDatabase.delete(DATABASE_TABLE_NAME, KEY_ROWID + "=" + c.getInt(0), null);
		}
	}
	
}
