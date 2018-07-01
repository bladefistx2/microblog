package com.pendo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.pendo.microblog.Const;

public class EsClient {
	
	private static volatile EsClient instance;
	private static Object mutex = new Object();
	private Settings settings;
	private Client client;

	private EsClient() {
	}

	@SuppressWarnings("resource")
	public static Client getClient() {
		EsClient result = instance;
		if (result == null) {
			synchronized (mutex) {
				result = instance;
				if (result == null) {
					result = new EsClient();
					result.settings = Settings.builder().build();
					try {
						result.client = new PreBuiltTransportClient(result.settings)
						    	.addTransportAddress(new TransportAddress(InetAddress.getByName(Const.ES_HOST),Const.ES_PORT));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					instance = result;
				}
			}
		}
		return result.client;
	}
	

}
