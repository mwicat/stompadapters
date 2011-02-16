import static ch.lambdaj.Lambda.convertMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import pk.aamir.stompj.Message;
import ch.lambdaj.function.convert.DefaultStringConverter;


public class JsonStompDataAdapter extends BaseStompDataAdapter {
	protected Map<String, String> messageToItem(Message msg) throws JsonParseException, JsonMappingException, IOException {
		String source = msg.getContentAsString();
		Map<String,Object> map =
			new ObjectMapper().readValue(source, HashMap.class);
		Map<String, String> result = convertMap(map, new DefaultStringConverter());
		return result;
	}

}
