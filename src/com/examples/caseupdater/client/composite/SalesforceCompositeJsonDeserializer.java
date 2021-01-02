package com.examples.caseupdater.client.composite;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class SalesforceCompositeJsonDeserializer extends JsonDeserializer<Body> {

  @Override
  public Body deserialize(final JsonParser jsonParser,
                          final DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    Body body = new Body();
    if ( JsonToken.START_ARRAY.equals(jsonParser.getCurrentToken())) {
      ObjectMapper mapper = new ObjectMapper();
      //mapper.readValue(deserializationContext);
    }
    return new Body();
  }
}
