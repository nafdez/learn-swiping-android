package es.ignaciofp.learnswiping.services.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import okhttp3.OkHttpClient;

public abstract class APIService {

    protected static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    protected static final JsonDeserializer<LocalDateTime> LOCAL_DATE_TIME_JSON_DESERIALIZER = (json, typeOfT, context) -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime();

    // Since LocalDateTime is going to be used in manu places, it's the default
    protected static final Gson BASIC_GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, LOCAL_DATE_TIME_JSON_DESERIALIZER).create();

    protected final String TEXT_PLAIN = "text/plain";
    protected final String APPLICATION_JSON = "application/json";
    protected final String IMAGE_PNG = "image/png";

    protected final String METHOD_GET = "GET";
    protected final String METHOD_POST = "POST";
    protected final String METHOD_PUT = "PUT";
    protected final String METHOD_DELETE = "DELETE";
}
