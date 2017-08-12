package techmorphs.cloudhack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cam extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    public byte [] thePictureByteArray;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d("hello", ""+data.length);
        thePictureByteArray =new byte[data.length];
        for(int i=0;i<data.length;i++){
            thePictureByteArray[i]=127;
            Log.d("hello",""+thePictureByteArray[i]);
        }
        System.arraycopy(data,0,thePictureByteArray,0,data.length);
       // thePictureByteArray=data;
        Log.d("hello", "done"+thePictureByteArray[3000]);
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

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
                        for(int i=0;i<thePictureByteArray.length;i++){
                            Log.d("assignedFuck"," "+i+" "+thePictureByteArray[i]);
                        }
                    }
                }
        );
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
