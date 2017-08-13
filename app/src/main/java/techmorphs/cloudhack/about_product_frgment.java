package techmorphs.cloudhack;

/**
 * Created by Pasindu on 8/12/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class about_product_frgment extends Fragment {


    //private LexiNavigation LN = new LexiNavigation();
    Item LN= Cam.returnedItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        ImageView productImage = (ImageView) getView().findViewById(R.id.productimg);

        Context c = getActivity().getApplicationContext();
        Picasso.with(c).load(LN.img)
                .fit().into(productImage);
        return inflater.inflate(R.layout.about_product, container, false);



    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        getActivity().setTitle("About Product");
    }


    public void setText(String text){
        TextView productName = (TextView) getView().findViewById(R.id.product_name);
        productName.setText(LN.name);

        TextView productDesc = (TextView) getView().findViewById(R.id.discription);
        productDesc.setText(LN.descrp);
    }


}
