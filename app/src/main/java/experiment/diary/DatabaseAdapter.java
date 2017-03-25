package experiment.diary;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

class DatabaseAdapter {

    private static final String APP_DIARY_DB = "APP_DIARY_DB_NAME";
    private static final String APP_DIARY_DB_USER_TABLE = "APP_DIARY_DB_USER_TABLE_NAME";
    private static final String APP_DIARY_DB_DIARIES_TABLE = "APP_DIARY_DB_DIARIES_TABLE_NAME";
    private static final int DB_VERSION = 1;

    private class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, APP_DIARY_DB, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                // TODO: update username, password and head in version 2
                String CREATE_USER_TABLE = "CREATE TABLE if not exists " + APP_DIARY_DB_USER_TABLE
                        + " (_id INTEGER PRIMARY KEY, username TEXT, password TEXT, head BLOB)";
                db.execSQL(CREATE_USER_TABLE);
            } catch (Exception error) {
                Log.e(TAG, "onCreate: failed at create USER table", new Throwable("create USER table failed"));
                error.printStackTrace();
            }

            try {
                String CREATE_DIARIES_TABLE = "CREATE TABLE if not exists " + APP_DIARY_DB_DIARIES_TABLE
                        + " (_id INTEGER PRIMARY KEY, month INTEGER, day INTEGER, title TEXT, picture BLOB, content TEXT)";
                db.execSQL(CREATE_DIARIES_TABLE);
            } catch (Exception error) {
                Log.e(TAG, "onCreate: failed at create DIARIES table", new Throwable("create DIARIES table failed"));
                error.printStackTrace();
            }

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private DatabaseHelper dh;

    // constructor
    DatabaseAdapter(Context context) {
        dh = new DatabaseHelper(context);
    }

    /** DatabaseAdapter API index:
     *      TABLE USER:
     *          boolean insertUser(String username, String password, byte[] head)
     *          boolean updateUser(String username, String password, byte[] head)
     *          boolean deleteUser(String username, String password)
     *
     *          boolean isUserExist()
     *          // TODO: getPasswordByUsername(String username)
     *
     *      TABLE DIARIES:
     *          boolean insertDiary(int month, int day, String title, String content, byte[] picture)
     *          boolean updateDiary(int month, int day, String title, String content, byte[] picture)
     *          boolean deleteDiary(int month, int day)
     *
     *          ArrayList<ArrayList<Diary>> queryDiaryAll()
     *          ArrayList<Diary> queryDiaryByMonth(int month)
     *          Diary queryDiaryByMonthAndDay(int month, int day)
     *
     *
     * */

    // TODO: insert head with suitable type
    boolean insertUser(String username, String password) {
        SQLiteDatabase db = dh.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues cv = new ContentValues();
            cv.put("username", username);
            cv.put("password", password);
            // -1 means insert failed
            if (db.insert(APP_DIARY_DB_USER_TABLE, null, cv) != -1) {
                db.close();
                return true;
            }
            db.close();
        }
        // if code executed here then error in insert and handled it by SQLiteDatabase
        Log.e(TAG, "insertUser: cannot open db", new Throwable("cannot open db"));
        return false;
    }
    boolean updateUser(String username, String password) {
        SQLiteDatabase db = dh.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues cv = new ContentValues();
            cv.put("username", username);
            cv.put("password", password);

            String where = "username = ? AND password = ?";
            String whereArgs[] = {username, password};

            // db.update return the rows affected, so if succeeded return value greater then 0
            if (db.update(APP_DIARY_DB_USER_TABLE, cv, where, whereArgs) > 0) {
                db.close();
                return true;
            }
            db.close();
        }
        // if code executed here then error in insert and handled it by SQLiteDatabase
        Log.e(TAG, "updateUser: cannot open db", new Throwable("cannot open db"));
        return false;
    }
    boolean deleteUser(String username, String password) {
        SQLiteDatabase db = dh.getWritableDatabase();
        if (db.isOpen()) {

            String where = "username = ? AND password = ?";
            String whereArg[] = {username, password};

            // db.delete return the rows affected, so if succeeded return value greater then 0
            if (db.delete(APP_DIARY_DB_USER_TABLE, where, whereArg) > 0) {
                db.close();
                return true;
            }
            db.close();
        }
        Log.e(TAG, "deleteUser: cannot open db", new Throwable("cannot open db"));
        return false;
    }

    boolean isUserExist() {
        SQLiteDatabase db = dh.getReadableDatabase();
        boolean result;
        if (db.isOpen()) {
            String columns[] = {"username"};
            Cursor cursor = db.query(APP_DIARY_DB_USER_TABLE, columns, null, null, null, null, null);
            result = cursor.getCount() != 0;
            cursor.close();
            db.close();
            return result;
        } else {
            Log.e(TAG, "queryAll: cannot open db", new Throwable("cannot open db"));
        }
        return false;
    }

    boolean isRightUserAndPassword(String username, String password) {
        SQLiteDatabase db = dh.getReadableDatabase();
        boolean result;
        if (db.isOpen()) {
            String columns[] = {"username", "password"};
            String where = "username = ? AND password = ?";
            String whereArgs[] = {username, password};
            Cursor cursor = db.query(APP_DIARY_DB_USER_TABLE, columns, where, whereArgs, null, null, null);
            result = cursor.getCount() != 0;
            cursor.close();
            db.close();
            return result;
        } else {
            Log.e(TAG, "queryAll: cannot open db", new Throwable("cannot open db"));
        }
        return false;
    }


    // TODO: insert picture with suitable type
    boolean insertDiary(int month, int day, String title, String content, byte[] picture) {
        SQLiteDatabase db = dh.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues cv = new ContentValues();
            cv.put("month", month);
            cv.put("day", day);
            cv.put("title", title);
            cv.put("content", content);

            cv.put("picture", picture);

            // -1 means insert failed
            if (db.insert(APP_DIARY_DB_DIARIES_TABLE, null, cv) != -1) {
                db.close();
                return true;
            }
            db.close();
        }
        // if code executed here then error in insert and handled it by SQLiteDatabase
        Log.e(TAG, "insertDiary: cannot open db", new Throwable("cannot open db"));
        return false;
    }
    // TODO: update picture with suitable type
    boolean updateDiary(int month, int day, String title, String content, byte[] picture) {
        SQLiteDatabase db = dh.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues cv = new ContentValues();
            cv.put("month", month);
            cv.put("day", day);
            cv.put("title", title);
            cv.put("content", content);

            cv.put("picture", picture);

            String where = "month = ? AND day = ?";
            String whereArgs[] = {String.valueOf(month), String.valueOf(day)};

            // db.update return the rows affected, so if succeeded return value greater then 0
            if (db.update(APP_DIARY_DB_DIARIES_TABLE, cv, where, whereArgs) > 0) {
                db.close();
                return true;
            }
            db.close();
        }
        // if code executed here then error in insert and handled it by SQLiteDatabase
        Log.e(TAG, "updateDiary: cannot open db", new Throwable("cannot open db"));
        return false;
    }
    boolean deleteDiary(int month, int day) {
        SQLiteDatabase db = dh.getWritableDatabase();
        if (db.isOpen()) {

            String where = "month = ? AND day = ?";
            String whereArg[] = {String.valueOf(month), String.valueOf(day)};

            // db.delete return the rows affected, so if succeeded return value greater then 0
            if (db.delete(APP_DIARY_DB_DIARIES_TABLE, where, whereArg) > 0) {
                db.close();
                return true;
            }
            db.close();
        }
        Log.e(TAG, "deleteDiary: cannot open db", new Throwable("cannot open db"));
        return false;
    }

    ArrayList<ArrayList<Diary>> queryDiaryAll() {
        ArrayList<ArrayList<Diary>> result = new ArrayList<>();
        for (int i = 0; i <= 12; ++i) {
            result.add(new ArrayList<Diary>());
        }
        SQLiteDatabase db = dh.getReadableDatabase();

        if (db.isOpen()) {
            // TODO: picture
            String columns[] = {"month", "day", "title", "content", "picture"};
            Cursor cursor = db.query(APP_DIARY_DB_DIARIES_TABLE, columns, null, null, null, null, null);

            // convert the cursor into Diary and add it into result
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    int month = cursor.getInt(cursor.getColumnIndex("month"));
                    int day = cursor.getInt(cursor.getColumnIndex("day"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));

                    byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                    // TODO: picture
                    result.get(month).add(new Diary(month, day, title, content, picture));

                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();
            return result;
        } else {
            Log.e(TAG, "queryAll: cannot open db", new Throwable("cannot open db"));
        }
        return null;
    }
    ArrayList<Diary> queryDiaryByMonth(int queryMonth) {
        ArrayList<Diary> result = new ArrayList<>();
        SQLiteDatabase db = dh.getReadableDatabase();

        if (db.isOpen()) {
            // TODO: picture
            String columns[] = {"month", "day", "title", "content", "picture"};
            String where = "month = ?";
            String whereArgs[] = {String.valueOf(queryMonth)};

            Cursor cursor = db.query(APP_DIARY_DB_DIARIES_TABLE, columns, where, whereArgs, null, null, null);

            // convert the cursor into Diary and add it into result
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    int month = cursor.getInt(cursor.getColumnIndex("month"));
                    int day = cursor.getInt(cursor.getColumnIndex("day"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));


                    byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                    // TODO: picture
                    result.add(new Diary(month, day, title, content, picture));

                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();
            return result;
        } else {
            Log.e(TAG, "queryAll: cannot open db", new Throwable("cannot open db"));
        }
        return null;
    }
    Diary queryDiaryByMonthAndByDay(int queryMonth, int queryDay) {
        Diary result = null;
        SQLiteDatabase db = dh.getReadableDatabase();
        if (db.isOpen()) {
            // TODO: picture
            String columns[] = {"month", "day", "title", "content", "picture"};
            String where = "month = ? AND day = ?";
            String whereArgs[] = {String.valueOf(queryMonth), String.valueOf(queryDay)};

            Cursor cursor = db.query(APP_DIARY_DB_DIARIES_TABLE, columns, where, whereArgs, null, null, null);

            // convert the cursor into Diary and add it into result
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    int month = cursor.getInt(cursor.getColumnIndex("month"));
                    int day = cursor.getInt(cursor.getColumnIndex("day"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));


                    byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                    // TODO: picture
                    result = new Diary(month, day, title, content, picture);

                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();
            return result;
        } else {
            Log.e(TAG, "queryAll: cannot open db", new Throwable("cannot open db"));
        }
        return null;
    }

}
