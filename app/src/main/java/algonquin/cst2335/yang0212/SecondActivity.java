package algonquin.cst2335.yang0212;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.yang0212.databinding.ActivityMainBinding;
import algonquin.cst2335.yang0212.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        binding.textView.setText("Welcome back " + emailAddress);


        binding.phoneButton.setOnClickListener(btn -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            String phoneNumber = binding.editTextPhone.getText().toString();
            startActivity(call.setData(Uri.parse("tel:" + phoneNumber)));
        });

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String number = prefs.getString("PhoneNumber", "");
        binding.editTextPhone.setText(number);


        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            binding.camera.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;


                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (IOException e)
                            { e.printStackTrace();
                            }
                        }


                    }
                });
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        binding.cameraButton.setOnClickListener(btn -> {
            cameraResult.launch(cameraIntent);

            File file = new File( getFilesDir(), "Picture.png");
            if(file.exists())
            {
                Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                binding.camera.setImageBitmap(theImage);
            }


        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", binding.editTextPhone.getText().toString());
        editor.apply();
    }
}