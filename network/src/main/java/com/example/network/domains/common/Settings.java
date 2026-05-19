package com.example.network.domains.common;

public class Settings {
    public static final String URL = "https://bloop-me.ru/student";
    public static final String SWAGGER_URL = URL + "/swagger/index.html";

    public static final String DEMO_TOKEN = "35ee4906-6b9e-4ec4-96db-bd51f8492d13";

    public static String url(String path) {
        if (path.startsWith("/")) {
            return URL + path;
        }
        return URL + "/" + path;
    }
}
