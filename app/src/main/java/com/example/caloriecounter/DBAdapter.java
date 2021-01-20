package com.example.caloriecounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {
    // Variable
    private static final String databaseName = "stramdiet";
    private static  final int databaseVersion = 35;

    // DB Variable
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    // Class Adapter
    public DBAdapter(Context context){
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    // Database Helper
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super (context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                //Create table goal
                db.execSQL("CREATE TABLE IF NOT EXISTS goal (" +
                        "goal_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "goal_current_weight INT," +
                        "goal_target_weight INT," +
                        "goal_i_want_to VARCHAR, " +
                        "goal_weekly_goal VARCHAR, " +
                        "goal_date DATE, " +
                        "goal_energy_bmr INT, " +
                            "goal_proteins_bmr INT, " +
                            "goal_carbs_bmr INT, " +
                            "goal_fat_bmr INT, " +
                        "goal_energy_diet INT, " +
                            "goal_proteins_diet INT, " +
                            "goal_carbs_diet INT, " +
                            "goal_fat_diet INT, " +
                        "goal_energy_with_activity INT, " +
                            "goal_proteins_with_activity INT, " +
                            "goal_carbs_with_activity INT, " +
                            "goal_fat_with_activity INT, " +
                        "goal_energy_with_activity_and_diet INT, " +
                            "goal_proteins_with_activity_and_diet INT, " +
                            "goal_carbs_with_activity_and_diet INT, " +
                            "goal_fat_with_activity_and_diet INT, " +
                        "goal_notes VARCHAR);");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            try{
                //Create table
                db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                        "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "user_email VARCHAR, " +
                        "user_password VARCHAR," +
                        "user_salt VARCHAR," +
                        "user_alias VARCHAR," +
                        "user_dob DATE, " +
                        "user_gender INT, " +
                        "user_location VARCHAR, " +
                        "user_height INTEGER, " +
                        "user_activity_level INT," +
                        "user_mesurment VARCHAR, " +
                        "user_last_seen TIME, " +
                        "user_note VARCHAR);");


                db.execSQL("CREATE TABLE IF NOT EXISTS food_diary_cal_eaten (" +
                        "cal_eaten_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "cal_eaten_date DATE, " +
                        "cal_eaten_meal_no INT, " +
                        "cal_eaten_energy INT, " +
                        "cal_eaten_protein INT, " +
                        "cal_eaten_carbs INT, " +
                        "cal_eaten_fat);");

                db.execSQL("CREATE TABLE IF NOT EXISTS food_diary (" +
                        "fd_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "fd_date DATE," +
                        "fd_meal_number INT," +
                        "fd_food_id INT," +
                        "fd_serving_size DOUBLE," +
                        "fd_serving_measurement VARCHAR," +
                        "fd_energy_calculated DOUBLE," +
                        "fd_protein_calculated DOUBLE," +
                        "fd_carbohydrates_calculated DOUBLE, " +
                        "fd_fat_calculated DOUBLE, " +
                        "fd_fat_meal_id INT);");


                db.execSQL("CREATE TABLE IF NOT EXISTS categories (" +
                        "category_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "category_name VARCHAR," +
                        "category_parent_id INT," +
                        "category_icon VARCHAR," +
                        "category_note VARCHAR);");


                db.execSQL("CREATE TABLE IF NOT EXISTS food("
                        + " food_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "food_name VARCHAR,"
                        + "food_manufactor VARCHAR,"
                        + "food_serving_size DOUBLE,"
                        + "food_serving_measurement VARCHAR,"
                        + "food_serving_name_number DOUBLE,"
                        + "food_serving_name_word VARCHAR,"
                        + "food_energy DOUBLE,"
                        + "food_proteins DOUBLE,"
                        + "food_carbohydrates DOUBLE,"
                        + "food_fat DOUBLE,"
                        + "food_energy_calculated DOUBLE,"
                        + "food_proteins_calculated DOUBLE,"
                        + "food_carbohydrates_calculated DOUBLE,"
                        + "food_fat_calculated DOUBLE,"
                        + "food_user_id int,"
                        + "food_barcode VARCHAR,"
                        + "food_category_id int,"
                        + "food_thumb VARCHAR,"
                        + "food_image_a VARCHAR,"
                        + "food_image_b VARCHAR,"
                        + "food_image_c VARCHAR,"
                        + "food_note VARCHAR); ");

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS food_diary_cal_eaten");
            db.execSQL("DROP TABLE IF EXISTS food_diary");
            db.execSQL("DROP TABLE IF EXISTS categories");
            db.execSQL("DROP TABLE IF EXISTS food");
            onCreate(db);

            String TAG = "Tag";
            Log.w(TAG, "Upgrading database from version " + oldVersion + "to" + newVersion + ", which will destroy all the data");
        }
    }

    // Open Database
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // Close Database
    public void close() {
        DBHelper.close();
    }

    // Quote Smart
    public String quoteSmart(String value){
        // Is numeric?
        boolean isNumeric = false;
        try {
            double myDouble = Double.parseDouble(value);
            isNumeric = true;
        }
        catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        if(isNumeric == false){
            // Escapes special characters in a string for use in an SQL statement
            if (value != null && value.length() > 0) {
                value = value.replace("\\", "\\\\");
                value = value.replace("'", "\\'");
                value = value.replace("\0", "\\0");
                value = value.replace("\n", "\\n");
                value = value.replace("\r", "\\r");
                value = value.replace("\"", "\\\"");
                value = value.replace("\\x1a", "\\Z");
            }
        }

        value = "'" + value + "'";

        return value;
    }

    public double quoteSmart(double value) {
        return value;
    }

    public int quoteSmart(int value) {
        return value;
    }

    // Insert Database
    public void insert (String table, String fields, String values){
        db.execSQL(" INSERT INTO " + table + "(" + fields + ") VALUES (" + values + ")");
    }

    //Count
    public int count(String table)
    {
        try {
            Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + table + "", null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            mCount.close();
            return count;
        }
        catch (SQLException e) {
            return -1;
        }
    }

    /*SELECT----------------------------------------------*/
    public Cursor selectPrimaryKey(String table, String primaryKey, long rowId, String[] fields) throws SQLException {

        /*Select example :
            long row = 3;
            String fields[] = new String[] {
                    "food_id",
                    "food_name",
                    "food_manufactor_name"
            };
            Cursor c = db.select("food", "food_id", row, fields);
            displayRecordFromNotes(c);

          */

        Cursor mCursor = db.query(table, fields, primaryKey + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /*----------UPDATE----------------------------------------------*/
    public boolean update(String table, String primaryKey, long rowId, String fields, String value) {
        /*Update Example
        long id = 1;
        String value = "xxt@isitwork.com";
        String valueSQL = db.quoteSmart(value);
        db.update("users", "user_id", id, "user_email", valueSQL);
         */

        //Remove first and last value of value
        value = value.substring(1, value.length()-1);//remove ' after running quote smart

        ContentValues args = new ContentValues();
        args.put(fields, value);
        return db.update(table, args, primaryKey + "=" + rowId, null) > 0;
    }

    public boolean update(String table, String primaryKey, long rowId, String fields, double value) {
        ContentValues args = new ContentValues();
        args.put(fields, value);
        return db.update(table, args, primaryKey + "=" + rowId, null) > 0;
    }

    public boolean update(String table, String primaryKey, long rowId, String fields, int value) {
        ContentValues args = new ContentValues();
        args.put(fields, value);
        return db.update(table, args, primaryKey + "=" + rowId, null) > 0;
    }
}
