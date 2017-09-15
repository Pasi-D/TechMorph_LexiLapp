package techmorphs.cloudhack;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Cam extends AppCompatActivity {
    public static Item returnedItem = null;
    public String keyword_server = "";
    public byte[] thePictureByteArray;
    private Camera mCamera;
    private CameraPreview mPreview;
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

                    for (String s : keyWords) {
                        //Log.d("ppx",s);
                        keyword_server += " " + s;
                    }

                    Log.d("pppp", "" + keyword_server);
                    new SendPostRequest().execute(keyword_server);

/*
                    if(returnedItem == null){
                        Toast.makeText(Cam.this, "Item Not found! Please rescan",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent = new Intent(Cam.this, abc.class);
                        startActivity(intent);


                        i.putExtra("returnItm", returnedItem);
                        startActivity(i);
*/
                    /*
                    Intent i = getIntent();
                    Item itm = (Item)i.getSerializableExtra("returnItm
                    ");
                     */
                } catch (IOException e) {
                    Log.e("gcp", "Cloud Vison API error: ", e);
                }
            }
        }
    };

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
/*
        DatabaseHelper db = new DatabaseHelper(this);
        db.insertItem("sprite","sprite","soft drink bottle plastic product","b","Low sugar level-- No added coloring");
        db.insertItem("null","bottled water","drinking water plastic bottle","a","Purefied water-- Natural");
*/
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
                        Log.d("hello1","1234564");
                        //Intent i = new Intent(Cam.this, abc.class);
                        //startActivity(i);
                        mCamera.takePicture(null, null, mPicture);
                        Log.d("hello1", "12");


                        /*
                        Handler mHandler = new Handler();   //this method is not good... need to switch activity after receving the json
                        mHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Intent intent = new Intent(Cam.this, NavNavDrawer.class);
                                startActivity(intent);
                            }

                        }, 5000L);
*/




                        //Intent intent = new Intent(Cam.this, NavNavDrawer.class);
                        //startActivity(intent);
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

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://35.197.26.101"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("keywords", arg0[0]);
                //postDataParams.put("email", "abc@gmail.com");
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //Log.d("helo");
            Toast.makeText(Cam.this, result,
                    Toast.LENGTH_LONG).show();
        }
    }

}
