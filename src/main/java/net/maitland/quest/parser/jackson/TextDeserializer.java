package net.maitland.quest.parser.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.maitland.quest.model.Text;

import java.io.IOException;

/**
 * Created by David on 27/12/2016.
 */
public class TextDeserializer extends JsonDeserializer<Text> {
    @Override
    public Text deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Object cv = jsonParser.getCurrentValue();
        Object eo = jsonParser.getEmbeddedObject();
        String s = jsonParser.getText();
        char[] c = jsonParser.getTextCharacters();

        return new Text("some text");
    }
}
