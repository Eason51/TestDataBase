package com.example.test;

import java.util.concurrent.ExecutionException;

public class Global {

    public static String accessToken = "";
    public static String env = "hkustlife-1gmkzslzdce7a450";

    public static void retrieveAccessToken(){
            new AccessToken().execute();
    }
}
