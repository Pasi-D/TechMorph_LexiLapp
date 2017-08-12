package techmorphs.cloudhack;

import java.io.Serializable;

/**
 * Created by Pasindu on 8/13/2017.
 */

public class Item implements Serializable {
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