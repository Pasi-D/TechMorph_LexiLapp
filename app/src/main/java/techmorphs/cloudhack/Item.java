package techmorphs.cloudhack;

import android.os.Parcelable;

/**
 * Created by Nimesha on 8/12/2017.
 */

public class Item implements Parcelable {
    Item(int id,String name,String img,String descrp){
        this.id=id;
        this.name=name;
        this.img=img;
        this.descrp=descrp;
    }
    public int id;
    public String logo=null;
    public String name=null;
    public String keywords=null;
    public String img=null;
    public String descrp=null;
}
