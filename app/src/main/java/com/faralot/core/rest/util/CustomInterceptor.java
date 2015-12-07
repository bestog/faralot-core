package com.faralot.core.rest.util;

import android.util.Log;

import com.faralot.core.App;
import com.faralot.core.rest.RestClient;
import com.faralot.core.rest.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import io.paperdb.Paper;

/**
 * Interceptor: Kann Anfrage und Antwort manipulieren
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class CustomInterceptor implements Interceptor {

  public CustomInterceptor() {
  }

  @Override
  public final Response intercept(Interceptor.Chain chain) throws IOException {
    /**
     * Api-Token wird fuer den Request als Header angehangen
     */
    Request request = chain.request()
        .newBuilder()
            // API-Token hinzufuegen
        .header(RestClient.API_TOKEN, Paper.book().read(User.TOKEN, "none"))
            // APP-Namen hinzufuegen
        .header(RestClient.API_APP, App.API_APP)
        .build();
    Response response = chain.proceed(request);
    String bodyString = response.body().string();
    /**
     * Debug Ausgabe
     */

    long t1 = System.nanoTime();
    Log.d("CustomInterceptor",
        String.format("---> SEND %s%n#Parameter#%n%s%n#Header#%n%s%n", request.url(), request
            .toString(), request.headers()));
    long t2 = System.nanoTime();
    Log.d("CustomInterceptor",
        String.format("<--- RECEIVE %s (%.1fms)%n#Response# %s%n#Header#%n%s", response
            .request()
            .url(), (double) (t2 - t1) / 1.0e6d, bodyString, response.headers()));

    JsonObject jsonObject = new JsonParser().parse(bodyString).getAsJsonObject();
    if (jsonObject.has("error")) {
      if (jsonObject.get("error").getAsBoolean()) {
        throw new RestErrorException(jsonObject.get("message").getAsString());
      }
    }
    /**
     * Response zurueckgeben
     */
    return response.newBuilder().body(ResponseBody.create(response.body()
        .contentType(), bodyString))
        .build();
  }
}
