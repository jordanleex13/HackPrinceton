//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Emotion-Android
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.jordanleex13.hackprinceton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.jordanleex13.hackprinceton.Helpers.ImageHelper;
import com.jordanleex13.hackprinceton.Helpers.RunnableParseJson;
import com.jordanleex13.hackprinceton.MachineLearning.EmotionalWant;
import com.jordanleex13.hackprinceton.MachineLearning.Neural_Network;
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.FaceRectangle;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class RecognizeActivity extends ActionBarActivity implements View.OnClickListener{

    public static final String TAG = RecognizeActivity.class.getSimpleName();

    private static final int REQUEST_TAKE_PHOTO = 0;

    // The URI of photo taken with camera
    private Uri mUriPhotoTaken;

    // The button to select an image
    private Button mButtonTakePic;
    private Button mButtonLaunchMaps;

    // The URI of the image selected to detect.
    private Uri mImageUri;

    // The image selected to detect.
    private Bitmap mBitmap;

    // The text to show status and result.
    private TextView mTextEmotion;
    private TextView mTextRecommendation;

    private EmotionServiceClient client;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    // Array of (max 4) queries
    private String[] searchQueries = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize);

        if (client == null) {
            client = new EmotionServiceRestClient(getString(R.string.subscription_key));
        }

        mButtonTakePic = (Button) findViewById(R.id.buttonTakePic);
        mButtonTakePic.setOnClickListener(this);
        mButtonLaunchMaps = (Button) findViewById(R.id.buttonLaunchMaps);
        mButtonLaunchMaps.setOnClickListener(this);
        mButtonLaunchMaps.setEnabled(false);
        mButtonLaunchMaps.setBackgroundColor(getResources().getColor(R.color.light_gray));

        mTextEmotion = (TextView) findViewById(R.id.textViewResult);
        mTextRecommendation = (TextView) findViewById(R.id.activity_recognize_recommendation);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recognize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doRecognize() {
        mButtonTakePic.setEnabled(false);

        // Do emotion detection using auto-detected faces.
        try {
            new doRequest(false).execute();
        } catch (Exception e) {
            mTextEmotion.append("Error encountered. Exception is: " + e.toString());
        }

//        String faceSubscriptionKey = getString(R.string.faceSubscription_key);
//        if (faceSubscriptionKey.equalsIgnoreCase("Please_add_the_face_subscription_key_here")) {
//            mTextEmotion.append("\n\nThere is no face subscription key in res/values/strings.xml. Skip the sample for detecting emotions using face rectangles\n");
//        } else {
//            // Do emotion detection using face rectangles provided by Face API.
//            try {
//                new doRequest(true).execute();
//            } catch (Exception e) {
//                mTextEmotion.append("Error encountered. Exception is: " + e.toString());
//            }
//        }
    }

    // Called when the "Select Image" button is clicked.
    public void takePic(View view) {
        mTextEmotion.setText("");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File file = File.createTempFile("IMG_", ".jpg", storageDir);
                mUriPhotoTaken = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            } catch (IOException e) {
                //setInfo(e.getMessage());
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void launchMaps() {
        Log.d(TAG, "Starting background thread");

        for (int i = 0; i < searchQueries.length; i++) {
            new Thread(new RunnableParseJson(searchQueries[0])).start();
        }

        Intent intent = new Intent(this, ActivityMaps.class);
        startActivity(intent);
    }
    // Called when image selection is done.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("RecognizeActivity", "onActivityResult");
        switch (requestCode) {

            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri;
                    if (data == null || data.getData() == null) {
                        imageUri = mUriPhotoTaken;
                    } else {
                        imageUri = data.getData();
                    }

                    if (imageUri != null) {
                        // If image is selected successfully, set the image URI and bitmap.
                        mImageUri = imageUri;

                        mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                                mImageUri, getContentResolver());
                        if (mBitmap != null) {
                            // Show the image on screen.
                            ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
                            imageView.setImageBitmap(mBitmap);

                            // Add detection log.
                            Log.d("RecognizeActivity", "Image: " + mImageUri + " resized to " + mBitmap.getWidth()
                                    + "x" + mBitmap.getHeight());

                            doRecognize();
                        }
                    }

                }
            default:
                break;
        }
    }


    private List<RecognizeResult> processWithAutoFaceDetection() throws EmotionServiceException, IOException {
        Log.d("emotion", "Start emotion detection with auto-face detection");

        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        long startTime = System.currentTimeMillis();
        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE STARTS HERE
        // -----------------------------------------------------------------------

        List<RecognizeResult> result = null;
        //
        // Detect emotion by auto-detecting faces in the image.
        //
        result = this.client.recognizeImage(inputStream);

        String json = gson.toJson(result);
        Log.d("result", json);

        Log.d("emotion", String.format("Detection done. Elapsed time: %d ms", (System.currentTimeMillis() - startTime)));
        // -----------------------------------------------------------------------
        // KEY SAMPLE CODE ENDS HERE
        // -----------------------------------------------------------------------
        return result;
    }

    private List<RecognizeResult> processWithFaceRectangles() throws EmotionServiceException, ClientException, IOException {
        Log.d("emotion", "Do emotion detection with known face rectangles");
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        long timeMark = System.currentTimeMillis();
        Log.d("emotion", "Start face detection using Face API");
        FaceRectangle[] faceRectangles = null;
        String faceSubscriptionKey = getString(R.string.faceSubscription_key);
        FaceServiceRestClient faceClient = new FaceServiceRestClient(faceSubscriptionKey);
        Face faces[] = faceClient.detect(inputStream, false, false, null);
        Log.d("emotion", String.format("Face detection is done. Elapsed time: %d ms", (System.currentTimeMillis() - timeMark)));

        if (faces != null) {
            faceRectangles = new FaceRectangle[faces.length];

            for (int i = 0; i < faceRectangles.length; i++) {
                // Face API and Emotion API have different FaceRectangle definition. Do the conversion.
                com.microsoft.projectoxford.face.contract.FaceRectangle rect = faces[i].faceRectangle;
                faceRectangles[i] = new FaceRectangle(rect.left, rect.top, rect.width, rect.height);
            }
        }

        List<RecognizeResult> result = null;
        if (faceRectangles != null) {
            inputStream.reset();

            timeMark = System.currentTimeMillis();
            Log.d("emotion", "Start emotion detection using Emotion API");
            // -----------------------------------------------------------------------
            // KEY SAMPLE CODE STARTS HERE
            // -----------------------------------------------------------------------
            result = this.client.recognizeImage(inputStream, faceRectangles);

            String json = gson.toJson(result);
            Log.d("result", json);
            // -----------------------------------------------------------------------
            // KEY SAMPLE CODE ENDS HERE
            // -----------------------------------------------------------------------
            Log.d("emotion", String.format("Emotion detection is done. Elapsed time: %d ms", (System.currentTimeMillis() - timeMark)));
        }
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Recognize Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jordanleex13.hackprinceton/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Recognize Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.jordanleex13.hackprinceton/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonTakePic:
                mTextRecommendation.setText("");
                mTextEmotion.setText("");
                takePic(v);
                break;
            case R.id.buttonLaunchMaps:
                launchMaps();
                break;
        }
    }

    private class doRequest extends AsyncTask<String, String, List<RecognizeResult>> {
        // Store error message
        private Exception e = null;
        private boolean useFaceRectangles = false;

        public doRequest(boolean useFaceRectangles) {
            this.useFaceRectangles = useFaceRectangles;
        }

        @Override
        protected List<RecognizeResult> doInBackground(String... args) {
            if (this.useFaceRectangles == false) {
                try {
                    return processWithAutoFaceDetection();
                } catch (Exception e) {
                    this.e = e;    // Store error
                }
            } else {
                try {
                    return processWithFaceRectangles();
                } catch (Exception e) {
                    this.e = e;    // Store error
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);
            // Display based on error existence

            if (this.useFaceRectangles == false) {
                //mTextEmotion.append("\n\nRecognizing emotions with auto-detected face rectangles...\n");
            } else {
                //mTextEmotion.append("\n\nRecognizing emotions with existing face rectangles from Face API...\n");
            }
            if (e != null) {
                mTextEmotion.setText("Error: " + e.getMessage());
                this.e = null;
            } else {
                if (result.size() == 0) {
                    mTextEmotion.append("No emotion detected :(");
                } else {
                    Integer count = 0;
                    // Covert bitmap to a mutable bitmap by copying it
                    Bitmap bitmapCopy = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas faceCanvas = new Canvas(bitmapCopy);
                    faceCanvas.drawBitmap(mBitmap, 0, 0, null);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(5);
                    paint.setColor(Color.RED);

                    double[] scoreArr = new double[8];
                    for (RecognizeResult r : result) {
//                        mTextEmotion.append(String.format("\nFace #%1$d \n", count));
//                        mTextEmotion.append(String.format("\t anger: %1$.5f\n", r.scores.anger));
//                        mTextEmotion.append(String.format("\t contempt: %1$.5f\n", r.scores.contempt));
//                        mTextEmotion.append(String.format("\t disgust: %1$.5f\n", r.scores.disgust));
//                        mTextEmotion.append(String.format("\t fear: %1$.5f\n", r.scores.fear));
//                        mTextEmotion.append(String.format("\t happiness: %1$.5f\n", r.scores.happiness));
//                        mTextEmotion.append(String.format("\t neutral: %1$.5f\n", r.scores.neutral));
//                        mTextEmotion.append(String.format("\t sadness: %1$.5f\n", r.scores.sadness));
//                        mTextEmotion.append(String.format("\t surprise: %1$.5f\n", r.scores.surprise));
//                        mTextEmotion.append(String.format("\t face rectangle: %d, %d, %d, %d", r.faceRectangle.left, r.faceRectangle.top, r.faceRectangle.width, r.faceRectangle.height));

                        scoreArr[0] = r.scores.anger;
                        scoreArr[1] = r.scores.contempt;
                        scoreArr[2] = r.scores.disgust;
                        scoreArr[3] = r.scores.fear;
                        scoreArr[4] = r.scores.happiness;
                        scoreArr[5] = r.scores.neutral;
                        scoreArr[6] = r.scores.sadness;
                        scoreArr[7] = r.scores.surprise;

                        faceCanvas.drawRect(r.faceRectangle.left,
                                r.faceRectangle.top,
                                r.faceRectangle.left + r.faceRectangle.width,
                                r.faceRectangle.top + r.faceRectangle.height,
                                paint);
                        count++;
                    }


                    double[][] inputMood = new double[1][8];
                    for (RecognizeResult r : result){
                        inputMood = new double[][]{{
                                r.scores.anger,
                                r.scores.contempt,
                                r.scores.disgust,
                                r.scores.fear,
                                r.scores.happiness,
                                r.scores.neutral,
                                r.scores.sadness,
                                r.scores.surprise
                        }};
                    }


                    // get mood with machine learning
                    double[] mood = Neural_Network.getMood(inputMood);

                    // First: sports
                    // Second: calm
                    // Third: excited
                    // Fourth: popular
                    for (int i = 0; i < mood.length;i++) {
                        Log.e(TAG, String.valueOf(mood[i]));
                    }

                    // current want decided by emotions
                    EmotionalWant ew = new EmotionalWant(mood[0],mood[1],mood[2],mood[3]);

                    String emotion = getEmotion(scoreArr);

                    // list of queries to search.
                    searchQueries = ew.getSearchTerms();

                    mTextEmotion.setText("You are " + emotion + ". Eventify recommends");

                    String recommendation = "";
                    for (int i = 0; i < searchQueries.length;i++) {
                        System.out.println("Search Query " + i + " : " + searchQueries[i]);
                        if (i == 0) {
                            recommendation = searchQueries[i];
                        } else {
                            recommendation = recommendation +  "\t" + searchQueries[i];
                        }

                    }

                    mTextRecommendation.setText(recommendation);
                    ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
                    imageView.setImageDrawable(new BitmapDrawable(getResources(), mBitmap));
                }
//                mTextEmotion.setSelection(0);

            }

            mButtonTakePic.setEnabled(true);
            mButtonLaunchMaps.setEnabled(true);
            mButtonLaunchMaps.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }



    public String getEmotion(double[] arr) {

        double max = arr[0];
        int indexOfMax = 0;
        for (int i = 0; i < arr.length; i++ ) {
            if (arr[i] > max) {
                max = arr[i];
                indexOfMax = i;
            }
        }

        switch(indexOfMax) {
            case 0: return "angry";
            case 1: return "contemptuous";
            case 2: return "disgusted";
            case 3: return "afraid";
            case 4: return "happy";
            case 5: return "neutral";
            case 6: return "sad";
            case 7: return "surprised";
            default:
                return "neutral";
        }
    }

}

