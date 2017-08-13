package techmorphs.cloudhack;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import java.util.HashMap;
        import java.util.Map;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "app.db";
    public static final String TABLE_NAME = "item";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "logo";
    public static final String COL_3 = "name";
    public static final String COL_4 = "keywords";
    public static final String COL_5 = "img";
    public static final String COL_6 = "descrp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,logo TEXT,name TEXT,keywords TEXT,img TEXT,descrp TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertItem(String logo,String name,String keywords,String img,String descrp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,logo);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,keywords);
        contentValues.put(COL_5,img);
        contentValues.put(COL_6,descrp);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getResult(String sql) {
        String dbString="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(sql,null);
        return res;
        /*
        res.moveToFirst();

        //Position after the last row means the end of the results
        while (!res.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (res.getString(res.getColumnIndex("name")) != null) {
                dbString += res.getString(res.getColumnIndex("name"));
                dbString += "\n";
            }
            res.moveToNext();
        }
        db.close();
        return dbString;
*/
    }



    public boolean updateData(String id,String logo,String name,String keywords) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,logo);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,keywords);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    public int selectBestMatchingItem(String[] keyWords) {
        Map<Integer, Integer> map = new HashMap();
        String productId = null;
        /*String sql = "SELECT ID FROM MyTable WHERE keywords CONTAINS ('" + keyWords[0] + "'";
        for (int i = 1; i < keyWords.length; i++) {
            sql += " OR '" + keyWords[i] + "'";
        }
        sql += ");"; */
        int[] matchingProducts = getAllProductsforKeywords(keyWords);
        for (int i : matchingProducts) {
            map.put(i, 0);
        }
        for (String word : keyWords) {
            String[] wordArray = {word};
            fillMap(getAllProductsforKeywords(wordArray), map);

        }
        Map.Entry maxE = null;
        for (Map.Entry e : map.entrySet()) {
            if (maxE.getValue() == null || (int) e.getValue() > (int) maxE.getValue()) {
                maxE = e;
            }
        }

        return (int) maxE.getKey();
    }

    public static Item selectBestMatchingItem(int id){
        Item it[] = new Item[2];
        it[0]= new Item(1,"Sprite","https://firebasestorage.googleapis.com/v0/b/lexilapp-c54e1.appspot.com/o/18410.jpg?alt=media&token=174e2391-a5f2-4b7d-91bc-09d747a380d4","Low sugar level-- No added coloring-- No fruit contains");
        it[1]= new Item(2,"bottled water","https://firebasestorage.googleapis.com/v0/b/lexilapp-c54e1.appspot.com/o/WaterBottle.JPG?alt=media&token=4a72911b-6315-403c-864a-1fed155db17b","Purefied water-- Natural");
        return it[id-1];
    }

    private int[] getAllProductsforKeywords(String[] keyWords) {
        int[] products = null;
        String sql = "SELECT ID FROM item WHERE keywords CONTAINS ('" + keyWords[0] + "'";
        if (keyWords.length >= 0) {
            for (int i = 1; i < keyWords.length; i++) {
                sql += " OR '" + keyWords[i] + "'";
            }
        }
        sql += ");";

        //put code for getiing from database and processing to make a array here
        //cam.db = new DatabaseHelper(null)
        Cursor res= getResult(sql);
        res.moveToFirst();

        //Position after the last row means the end of the results

        products=new int[res.getCount()];
        int i=0;        while (!res.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (res.getString(res.getColumnIndex("ID")) != null) {
                products[i] = Integer.parseInt(res.getString(res.getColumnIndex("ID")));
                i++;
            }
            res.moveToNext();
        }

        return products;
    }

    private void fillMap(int[] products, Map map) {
        for (int i : products) {
            map.put(i, ((int) map.get(i)) + 1);
        }
    }



}