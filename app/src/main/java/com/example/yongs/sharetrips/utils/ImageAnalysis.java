package com.example.yongs.sharetrips.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.yongs.sharetrips.activity.MainActivity;
import com.example.yongs.sharetrips.activity.ModifyActivity;
import com.example.yongs.sharetrips.activity.ReportActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.LocationInfo;
import com.google.common.io.BaseEncoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImageAnalysis {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyAT8ducnkfgCi-oz8-MzDygnRdsGW6IUIE";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;

    private static final String TAG = ImageAnalysis.class.getSimpleName();

    private MainActivity mMainActivity;
    private static ReportActivity mReportActivity = new ReportActivity();

    private static class LabelDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<ReportActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        private LabelDetectionTask(ReportActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... objects) {
            try{
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);
            }catch(GoogleJsonResponseException e){
                Log.d(TAG,"failed to make API request because " + e.getContent());
            }catch(IOException e){
                Log.d(TAG, "failed to make API request because of other IOException " + e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details";
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.equals("nothing")) {
                ReportActivity.mLocation = result;
                ReportActivity.mThread.mHandler.sendEmptyMessage(0);
            }
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(final Context context, final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY){
            @Override
            protected void initializeVisionRequest(VisionRequest<?> request) throws IOException {
                super.initializeVisionRequest(request);

                String packageName = context.getPackageName();
                request.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                String sig = getSignature(context.getPackageManager(),packageName);

                request.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
            }
        };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);
        builder.setApplicationName("ClassificationPhoto");

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            Image base64EncodedImage = new Image();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("LANDMARK_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
        annotateRequest.setDisableGZipContent(true);

        Log.d(TAG,"created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    public void callCloudVision(final Bitmap bitmap, Context context){
        try{
            AsyncTask<Object, Void, String> labelDetectionTask = new LabelDetectionTask(mReportActivity, prepareAnnotationRequest(context,bitmap));
            labelDetectionTask.execute();
        } catch(IOException e){
            Log.d(TAG, "failed to make API request because of other IOException" + e.getMessage());
        }
    }

    private static String getSignature(@NonNull PackageManager pm, @NonNull String packageName){
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            if (packageInfo == null
                    || packageInfo.signatures == null
                    || packageInfo.signatures.length == 0
                    || packageInfo.signatures[0] == null) {
                return null;
            }
            return signatureDigest(packageInfo.signatures[0]);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private static String signatureDigest(Signature sig){
        byte[] signature = sig.toByteArray();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(signature);
            return BaseEncoding.base16().lowerCase().encode(digest);
        }catch(NoSuchAlgorithmException e){
            return null;
        }
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response){
        StringBuilder message = new StringBuilder();

        List<EntityAnnotation> landmarks = response.getResponses().get(0).getLandmarkAnnotations();

        if(landmarks != null){
            for(EntityAnnotation landmark : landmarks){
                message.append(String.format(Locale.US,"%s,%s,%s", landmark.getDescription(), landmark.getLocations().get(0).getLatLng().getLatitude().toString(), landmark.getLocations().get(0).getLatLng().getLongitude().toString()));
                message.append("\n");
                break;
            }
        }else{
            message.append("nothing");
        }

        Log.d(TAG,message.toString());
        return message.toString();
    }
}