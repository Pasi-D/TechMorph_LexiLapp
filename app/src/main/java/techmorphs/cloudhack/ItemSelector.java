package techmorphs.cloudhack;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oshan Wickramaratne on 2017-08-12.
 */

public class ItemSelector {

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

    private int[] getAllProductsforKeywords(String[] keyWords) {
        int[] products = null;
        String sql = "SELECT ID FROM MyTable WHERE keywords CONTAINS ('" + keyWords[0] + "'";
        if (keyWords.length >= 0) {
            for (int i = 1; i < keyWords.length; i++) {
                sql += " OR '" + keyWords[i] + "'";
            }
        }
        sql += ");";

        //put code for getiing from database and processing to make a array here
        //cam.db = new DatabaseHelper(null);
        Cursor res=Cam.db.getResult(sql);
        res.moveToFirst();

        //Position after the last row means the end of the results

        products=new int[res.getCount()];
        int i=0;
        while (!res.isAfterLast()) {
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