package techmorphs.cloudhack;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

public class Cam extends AppCompatActivity {
    public static DatabaseHelper db;
    private Camera mCamera;
    private CameraPreview mPreview;
    public byte [] thePictureByteArray;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] imageBytes, Camera camera) {
        if (imageBytes != null) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                // Process the image using Cloud Vision
                Map<String, Float> annotations = CloudVisionUtils.annotateImage(imageBytes);
                Log.d("gcp", "cloud vision annotations:" + annotations);

                String[] keyWords = annotations.keySet().toArray(new String[annotations.keySet().size()]);
                //if keyWords

                //int n=DatabaseHelper.selectBestMatchingItem(keyWords);
                Log.d("pppp",""+keyWords[0]);
                Item returnedItem=null;
                for (String s :keyWords){
                    if (s.equals("soft drink")||s.equals("green")){
                        Log.d("FINAL","sprite");
                        returnedItem=DatabaseHelper.selectBestMatchingItem(1);
                        break;
                    }
                    else if (s.equals("bottled water")||s.equals("mineral water")){
                        Log.d("FINAL","bottleOfWater");
                        returnedItem=DatabaseHelper.selectBestMatchingItem(2);
                        break;
                    }

                }

                if(returnedItem == null){
                    Toast.makeText(Cam.this, "Item Not found! Please rescan",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(this, Y.class);
                    i.putExtra("returnItm", returnedItem);

                    /*
                    Intent i = getIntent();
                    Item itm = (Item)i.getSerializableExtra("returnItm");
                     */
                }


            } catch (IOException e) {
                Log.e("gcp", "Cloud Vison API error: ", e);
            }
        }
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        DatabaseHelper db = new DatabaseHelper(this);
        db.insertItem("sprite","sprite","soft drink bottle plastic product","b","Low sugar level-- No added coloring");
        db.insertItem("null","bottled water","drinking water plastic bottle","a","Purefied water-- Natural");

        //String x=db.databaseToString();
        //Log.d("mariyan",x);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        // Add a listener to the Capture button
        Button captureButton = (Button) findViewById(R.id.camera_button_rel);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                        //Log.d("hello1",""+thePictureByteArray.length);
                        /*
                        for(int i=0;i<thePictureByteArray.length;i++){
                            Log.d("assignedFuck"," "+i+" "+thePictureByteArray[i]);
                        }
                        */
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }


    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}
