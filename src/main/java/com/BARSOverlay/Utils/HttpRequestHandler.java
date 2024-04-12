package com.BARSOverlay.Utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequestHandler {



    @SideOnly(Side.CLIENT)
    public static String get(URL url, Map<String, String> headers) throws IOException
    {
        HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
        httpurlconnection.setRequestMethod("GET");
        for(String i: headers.keySet()){
            httpurlconnection.setRequestProperty(i, headers.get(i));
        }
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
        StringBuilder stringbuilder = new StringBuilder();
        String s;

        while ((s = bufferedreader.readLine()) != null)
        {
            stringbuilder.append(s);
            stringbuilder.append('\r');
        }

        bufferedreader.close();
        return stringbuilder.toString();
    }

}
