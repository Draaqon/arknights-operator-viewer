package com.example.arknightsoperatorviewer;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StringReader {

    private static final String TAG = "bruh";
    private Context context;

    public StringReader(Context context)
    {
        this.context = context;
    }

    public String readEntire(String file)
    {
        String entireFile = "";
        StringBuilder sb = new StringBuilder();

        AssetManager am = context.getAssets();

        try
        {
            InputStream is = am.open(file);
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            boolean newLine = false;

            while((line = bf.readLine()) != null)
            {
                //entireFile += line;
                sb.append(line);
                Log.d(TAG, line);
                if(line.equals(""))
                {
                    //wonky formatting but for some reason it looks good and works
                    sb.append(System.getProperty("line.separator"));
                    sb.append(line = bf.readLine());
                    Log.d("bruh2", line);
                    sb.append(System.getProperty("line.separator"));
                    sb.append(line = bf.readLine().trim());
                    sb.append(System.getProperty("line.separator"));
                }
                else
                {
                    sb.append(System.getProperty("line.separator"));
                }
                /*
                if(newLine == true)
                {
                    sb.append(System.getProperty("line.separator"));
                    newLine = false;
                }
                 */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
