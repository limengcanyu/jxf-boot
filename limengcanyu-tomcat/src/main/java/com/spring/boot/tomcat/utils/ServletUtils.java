package com.spring.boot.tomcat.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ServletUtils {

    public static String getPostBody(HttpServletRequest servletRequest) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(servletRequest.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder body = new StringBuilder();
            while (bufferedReader.ready()) {
                body.append(bufferedReader.readLine().trim());
            }
            return body.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
