package com.project.ems.extras;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
public class News {
    public List<ArrayList> getNews (){
        List news = new ArrayList<>();
        try {

            // Create a URL object with the endpoint you want to fetch data from
            URL url = new URL("https://hindustantimes-1-t3366110.deta.app/top-india-news");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method (GET by default)
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {

                response.append(line);
            }
            reader.close();

            // Print the response
            StringTokenizer tokenizer = new StringTokenizer(response.toString(),",");
            // System.out.println(response.toString());
            while (tokenizer.hasMoreTokens()) {
                // Get the next token and print it
                String token = tokenizer.nextToken();
                news.add(token);
            }


            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;

    }
}

