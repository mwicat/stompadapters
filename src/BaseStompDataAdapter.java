import java.util.*;
import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


import ch.lambdaj.collection.LambdaMap;
import ch.lambdaj.function.convert.DefaultStringConverter;

import com.lightstreamer.interfaces.data.*;

import pk.aamir.stompj.Connection;
import pk.aamir.stompj.Message;
import pk.aamir.stompj.MessageHandler;
import pk.aamir.stompj.StompJException;

import static ch.lambdaj.Lambda.*;


public abstract class BaseStompDataAdapter implements SmartDataProvider {

	private ItemEventListener listener;
	
	private static final String USER = "";
	private static final String PASSWORD = "";
	
	private static final String HOST = "localhost";
	private static final int PORT = 61613;
	
	private Connection con;


	public void init(Map params, File configDir) throws DataProviderException {
		con = new Connection(HOST, PORT, USER, PASSWORD);
		try {
			con.connect();
		} catch (StompJException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataProviderException(e.toString());
		}
	}

	public boolean isSnapshotAvailable(String itemName) throws SubscriptionException {
		return false;
	}

	public void setListener(ItemEventListener listener) {
		this.listener = listener;
	}
	
	protected abstract Map<String, String> messageToItem(Message msg) throws JsonParseException, JsonMappingException, IOException;


	private void updateListener(Message msg, Object itemHandle) {
		try {
			listener.smartUpdate(itemHandle, messageToItem(msg), false);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
	}

	public void subscribe(String itemName, final Object itemHandle, boolean needsIterator)
	throws SubscriptionException, FailureException {
		String chan = itemName;
		System.out.println("subscribing to channel " + chan);
		con.subscribe(chan, true);
		con.addMessageHandler(chan, new MessageHandler() {
			public void onMessage(Message msg) {
				System.out.println("got message from stomp broker");
				updateListener(msg, itemHandle);
			}
		});
	}

	public void subscribe(String itemName, boolean needsIterator)
	throws SubscriptionException, FailureException {
	}         	

	public void unsubscribe(String itemName) throws SubscriptionException,
	FailureException {
		con.unsubscribe(itemName);
	}

}