package com.example.network.domains.common;

public class Settings {
    public static final String URL = "https://bloop-me.ru/student";
    public static final String SWAGGER_URL = URL + "/swagger/index.html";

    public static final String DEMO_TOKEN = "5cffbfde-00f9-48a0-90ea-229fd5f04376";

    public static String url(String path) {
        if (path.startsWith("/")) {
            return URL + path;
        }
        return URL + "/" + path;
    }
}
