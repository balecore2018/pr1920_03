package com.example.network.domains.common;

public class Settings {
    public static final String URL = "https://bloop-me.ru/student";
    public static final String SWAGGER_URL = URL + "/swagger/index.html";

    public static final String DEMO_TOKEN = "";

    public static String url(String path) {
        if (path.startsWith("/")) {
            return URL + path;
        }
        return URL + "/" + path;
    }
}
