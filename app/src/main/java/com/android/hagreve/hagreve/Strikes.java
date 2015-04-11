package com.android.hagreve.hagreve;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Strikes {


    //Construtor
    public Strikes(){}

    public static void loadStrikes(){
        if (MainActivity.strikes.size() == 0) {
            AsyncTask<Void, Void, String[]> execute = new AsyncTask<Void, Void, String[]>() {
                @Override
                protected String[] doInBackground(Void... params) {
                    try {
                        //Log.d("PDM", "NEW REQUEST");

                        URL url = new URL("http://hagreve.com/api/v2/allstrikes");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();

                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line).append('\n');
                        }

                        JSONArray strikesArray = new JSONArray(response.toString());
                        String[] strikesResult = new String[strikesArray.length()];
                        for (int i = 0; i < strikesArray.length(); ++i) {
                            strikesResult[i] = strikesArray.getJSONObject(i).getJSONObject("company").getString("name");
                        }

                        return strikesResult;

                    } catch (Exception e) {
                        //Log.e("PDM", e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String[] res) {
                    MainActivity.strikes.clear();
                    for (String s : res) {
                        MainActivity.strikes.add(s);
                    }
                    MainActivity.adapter.notifyDataSetChanged();
                }
            }.execute((Void) null);
        }
    }
}
