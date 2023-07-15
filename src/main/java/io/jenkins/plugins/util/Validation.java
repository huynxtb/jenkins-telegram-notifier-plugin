package io.jenkins.plugins.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean isUrl(String input){
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
