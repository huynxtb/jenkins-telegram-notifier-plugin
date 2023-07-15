package io.jenkins.plugins.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jenkins.plugins.exception.AppException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringHelper {

    public static String serializableObject(Object obj) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer();
        return ow.writeValueAsString(obj);
    }

    public static String toDateTimeNow(String timeZone) {
        try {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone(timeZone));
            return df.format(date);
        } catch (Exception e) {
            try {
                throw new AppException("Time zone invalid.");
            } catch (AppException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

