package com.testing.api.utils;
import com.github.javafaker.Faker;

public final class Constants {
    public static final  String VALUE_CONTENT_TYPE       = "application/json";
    public static final  String CONTENT_TYPE             = "Content-Type";
    public static final  String CLIENTS_PATH             = "clients";
    public static final  String DEFAULT_CLIENT_FILE_PATH = "src/main/resources/data/defaultClient.json";
    public static final String BASE_URL                 = "https://63b6dfe11907f863aa04ff81.mockapi.io";
    public static final  String URL                      = "/api/v1/%s";
    public static final  String URL_WITH_PARAM           = "/api/v1/%s/%s";
    public static final String RESOURCES_PATH            = "resources";
    public static final Faker FAKER = new Faker();

    private Constants() {
    }
}
