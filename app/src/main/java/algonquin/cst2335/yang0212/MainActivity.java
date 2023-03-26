package algonquin.cst2335.yang0212;

import static java.net.URLEncoder.encode;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.yang0212.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    protected String cityName;
    protected RequestQueue queue = null;
    protected Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView(binding.getRoot());

        binding.forecastButton.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        // go through the string that is passed in and change all spaces to "+" instead
                        + URLEncoder.encode(cityName,"UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) ->{
                        try {

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");
                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");


                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);

                            if(file.exists()) {
                                image = BitmapFactory.decodeFile(pathname);

                            } else {
                                String imageUrl = "https://openweathermap.org/img/w/" + iconName + ".png";

                                ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(iconName+".png", Activity.MODE_PRIVATE));
                                        } catch (Exception e) {
                                        }

                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                });
                                queue.add(imgReq);


                            }
                            binding.icon.setImageBitmap(image);

                            runOnUiThread( (  )  -> {

                                binding.temp.setText("The current temperature is " + current);
                                binding.temp.setVisibility(View.VISIBLE);
                                binding.maxTemp.setText("The max temperature is " + max);
                                binding.maxTemp.setVisibility(View.VISIBLE);
                                binding.minTemp.setText("The min temperature is " + min);
                                binding.minTemp.setVisibility(View.VISIBLE);
                                binding.humidity.setText("The min temperature is " + humidity);
                                binding.humidity.setVisibility(View.VISIBLE);
                                //scaling the width and height
                                binding.icon.getLayoutParams().height=1000;
                                binding.icon.getLayoutParams().width=1000;
                                binding.icon.setVisibility(View.VISIBLE);
                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);


                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, (error) -> {});
            queue.add(request);
        });






    }



}