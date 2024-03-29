package com.faralot.core.rest.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Factory: Mappt den JSON String auf ein Objekt
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class RestTypeAdapterFactory implements TypeAdapterFactory {

  public RestTypeAdapterFactory() {}

  @Override
  public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
    final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
    return new TypeAdapter<T>() {
      @Override
      public void write(JsonWriter out, T value) throws IOException {
        delegate.write(out, value);
      }

      @Override
      public T read(JsonReader in) throws IOException {
        JsonElement jsonElement = elementAdapter.read(in);
        if (jsonElement.isJsonObject()) {
          JsonObject jsonObject = jsonElement.getAsJsonObject();
          if (jsonObject.has("cod") && jsonObject.get("cod").getAsInt() == 404) {
            throw new IllegalArgumentException(jsonObject.get("message").getAsString());
          }
          if (jsonObject.has("data")) {
            jsonElement = jsonObject.get("data");
          }
        }
        return delegate.fromJsonTree(jsonElement);
      }
    }.nullSafe();
  }
}