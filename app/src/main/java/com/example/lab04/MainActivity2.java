package com.example.lab04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {
    EditText edLinkAnh;
    Button btnTai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        edLinkAnh = findViewById(R.id.edLinkAnh);
        btnTai = findViewById(R.id.btnTai);
        btnTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckNetworkConnect()){
                    // gọi hàm download file
                    String link = edLinkAnh.getText().toString().trim();

                    // tạo đối tượng và thực thi luôn lệnh execute không cần khai báo biến
                    new DownloadImg().execute(link);

                }else{
                    // chưa kết nối internet
                    Toast.makeText(MainActivity2.this, "Bạn chưa kết nối internet", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onCreate: Bạn chưa kết nối internet");
                }
            }
        });
    }
    boolean CheckNetworkConnect(){
        ConnectivityManager cnn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // kiểm tra đối với wifi
        NetworkInfo networkInfo = cnn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifi = networkInfo.isConnected();

        // kiểm tra đối với 3G
        networkInfo = cnn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobile = networkInfo.isConnected();

        if(isWifi)
            Log.d("TAG", "CheckNetworkConnect: Đang kết nối Wifi");

        if(isMobile)
            Log.d("TAG", "CheckNetworkConnect: Đang kết nối Mobile Data (3G,4G,5G)");

        return isMobile || isWifi;
    }
    class DownloadImg extends AsyncTask<String, Void, Bitmap>{
        InputStream inputStream;
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("zzzzzz", "onPreExecute: bat dau download ");
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d("zzzzzzzzz", "doInBackground: dang download " + strings[0]);
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                httpsURLConnection.setReadTimeout(10000);
                httpsURLConnection.connect();

                int status = httpsURLConnection.getResponseCode();
                if(status == HttpsURLConnection.HTTP_OK){
                    inputStream = httpsURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);

                }
                httpsURLConnection.disconnect();

                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d("zzzzzz", "onPostExecute: Download xong roi ");
            ImageView imageView = findViewById(R.id.ivHinh);
            imageView.setImageBitmap(bitmap);
        }
    }
}