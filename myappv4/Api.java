package com.example.rafae.myappv4;

/**
 * Created by rafae on 04/04/2018.
 */

public class Api {

    private static final String ROOT_URL            = "http://6db6f3e3.ngrok.io/prolserhum/Api/Api.php?apicall=";
    public static final String URL_CREATE_ANIMAL    = ROOT_URL + "createAnimal";
    public static final String URL_READ_ANIMAIS     = ROOT_URL + "getAnimais";
    public static final String URL_UPDATE_ANIMAL    = ROOT_URL + "updateAnimal";
    public static final String URL_DELETE_ANIMAL    = ROOT_URL + "deleteAnimal&id=";
    public static final String URL_REGISTER         = ROOT_URL + "signup";
    public static final String URL_LOGIN            = ROOT_URL + "login";
    public static final String URL_UPDATE_USER      = ROOT_URL + "updateUser";
    public static final String URL_READ_OBS         = ROOT_URL + "getObs";
    public static final String URL_CREATE_OBS       = ROOT_URL + "createObs";
    public static final String URL_DELETE_OBS       = ROOT_URL + "deleteObs&id=";
    public static final String URL_SAVE_IMG         = ROOT_URL + "saveImage";


}
