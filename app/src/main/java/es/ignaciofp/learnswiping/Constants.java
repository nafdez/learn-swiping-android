package es.ignaciofp.learnswiping;

import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class Constants {

    public static final String PREF_KEY = "LEARN_SWIPING_PREFS";

//    public static final String BASE_URL = "http://10.0.2.2:9999/";
    public static final String BASE_URL = "http://192.168.1.137:9999/";
    public static final String PICTURE_ENDPOINT = "pics";

    // Esto es una guarrada, lo sé
    public static final JsonDeserializer<LocalDateTime> LOCAL_DATE_TIME_JSON_DESERIALIZER = (json, typeOfT, context) -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();

}
