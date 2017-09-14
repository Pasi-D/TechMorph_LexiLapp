package techmorphs.cloudhack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class abc extends AppCompatActivity {

    //    Item LN= Cam.returnedItem;
    TextView productName = (TextView) findViewById(R.id.name1);
    TextView productDesc = (TextView) findViewById(R.id.des1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc);

        productName.setText("pojh");


        productDesc.setText("buhghbijn");

    }
}
