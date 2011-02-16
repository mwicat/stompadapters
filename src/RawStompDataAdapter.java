import static ch.lambdaj.Lambda.convertMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import pk.aamir.stompj.Message;
import ch.lambdaj.function.convert.DefaultStringConverter;


public class RawStompDataAdapter extends BaseStompDataAdapter {
	
	public static final String FIELD_MESSAGE = "msg";

	protected Map<String, String> messageToItem(Message msg) throws JsonParseException, JsonMappingException, IOException {
		String value = msg.getContentAsString();
		Map<String, String> map = new HashMap<String, String>();
		map.put(FIELD_MESSAGE, value);
		return map;
	}


}
