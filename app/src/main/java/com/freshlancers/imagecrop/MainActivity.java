package com.freshlancers.imagecrop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity {

    public static final String OPEN_CAMERA_OR_GALLERY_TO_CHOOSE_AN_IMAGE = "Open Camera or Gallery to choose an Image";
    public static final int TYPE = 1001;
    public static final int ASPECT_RATIO = 1;
    private static final String TAG = "MainActivity";
    @BindView(R.id.image)
    ImageView imageView;
    private Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.chooser_button)
    public void openChooser(View view) {
        EasyImage.openChooserWithGallery(MainActivity.this, OPEN_CAMERA_OR_GALLERY_TO_CHOOSE_AN_IMAGE, TYPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFile);
            }

        });

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                GlideApp.with(this)
                        .load(resultUri)
                        .into(imageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
                Log.e(TAG, "onActivityResult: ", result.getError());
            }
        }
    }

    private void onPhotosReturned(File imageFile) {
        CropImage.activity(Uri.fromFile(imageFile))
                .setAspectRatio(ASPECT_RATIO, ASPECT_RATIO)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setActivityMenuIconColor(Color.WHITE)
                .setAllowRotation(true)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setOutputCompressQuality(50)
                .setAutoZoomEnabled(true)
                .setActivityTitle("Crop Image")
                .start(this);

    }
}
