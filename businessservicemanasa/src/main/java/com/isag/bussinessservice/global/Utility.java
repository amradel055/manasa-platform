/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.bussinessservice.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author amr
 */
public class Utility {

    public static Boolean checkContainString(String code) {

        final Pattern standaloneNumber = Pattern.compile("[0-9]");
        Matcher matcher = standaloneNumber.matcher(code);
        int pos = 0;
        int count2 = 0;

        while (matcher.find(pos)) {
            pos = matcher.end();
            count2++;
        }

        Integer diff = code.length() - count2;
        if (diff == 0) {
            return true;
        }
        return false;
    }

    public static String uploadFileToDashBoard(String name, File file) throws MalformedURLException, ProtocolException, IOException {
          String boundary = "===" + System.currentTimeMillis() + "===";
        String crlf = "\r\n";
        
        URL url = new URL("https://educator.igma-it.com/dashboard/upload-thumb.php?");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream outputStream = connection.getOutputStream();
        
        outputStream.write(("--" + boundary + crlf).getBytes());
        outputStream.write(("Content-Disposition: form-data; thumb=" + file.toPath()+ "; name=\"" + file.getName() + "\"" + crlf).getBytes());
        outputStream.write(("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + crlf).getBytes());
        outputStream.write(("Content-Transfer-Encoding: binary" + crlf).getBytes());
        outputStream.write(crlf.getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();

        outputStream.write(crlf.getBytes());
        outputStream.write(("--" + boundary + "--" + crlf).getBytes());
        outputStream.close();

        int code = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
//        String encodedFile = new String(file);
//        URL url = new URL("https://educator.igma-it.com/dashboard/upload-thumb.php?name="+name+ "&tmp_name="+tempName); // Replace with your API endpoint URL
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        String formData = "thumb=" + encodedFile+ "&name="+name+ "&tmp_name="+tempName; // Replace with your form data
//        connection.setDoOutput(true);
//        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
//        writer.write(formData);
//        writer.flush();
//        int code = connection.getResponseCode();
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuilder response = new StringBuilder();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        return response.toString();
    }
}
