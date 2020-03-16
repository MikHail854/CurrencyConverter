package ru.mikhail.converter.CurrentCurrencyValue;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ru.mikhail.converter.Converter.MainConverter;
import ru.mikhail.converter.R;


public class MainActivity extends AppCompatActivity {

    TextView textView;

    public static ArrayList<Collection> listCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://www.cbr.ru/scripts/XML_daily.asp");
    }


    public class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";
        ArrayList<Collection> collectionArrayList = new ArrayList<>();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "xml document: " + s);
            ProductXmlParser parser = new ProductXmlParser();
            if (s != null && parser.parse(s)) {
                for (Collection collection : parser.getCollections()) {
                    Log.d("XML", collection.toString());
                    textView = (TextView) findViewById(R.id.itog);
                    textView.append(collection.toString());
                }
                listCollection = new ArrayList<>();
                listCollection = parser.getCollections();
                collectionArrayList = parser.getCollections();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String content = "";
            try {
                content = downloadXML(params[0]);
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
            }
            return content;
        }

        private String downloadXML(String urlPath) throws IOException {
            StringBuilder xmlResult = new StringBuilder();
            BufferedReader reader = null;
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    xmlResult.append(line);
                }
                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: Security Exception. Needs permission?" + e.getMessage());
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            return null;
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, MainConverter.class);
        startActivity(intent);
    }

}

