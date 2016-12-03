package p.minn.controller;

import java.nio.channels.SocketChannel;





import p.minn.client.BaseClient;
import p.minn.client.Client;
import p.minn.controller.BaseController;
import p.minn.handshake.RtmpHandShake;
import p.minn.packet.amf.RtmpPacket;
import p.minn.packet.amf.RtmpWrapper;
import p.minn.utils.BaseConstants;

/**
 * @author minn
 * @QQ:394286006
 * 
 */
public class Controller extends BaseController<RtmpPacket,RtmpWrapper> {
 
	public Controller() {
		super();
		hsclient=new RtmpHandShake(this);
		hsclient.start();
	}

	public void broadcast(int group, RtmpPacket packet,int clientid) throws Exception {
		for (Object client : groups.get(group).values()) {
			if(clientid!=((Client) client).clientId)
			((Client) client).addPacket(packet);
		}

	}


	public void loginClien(int id, BaseClient<RtmpPacket,RtmpWrapper> obj) {
		  synchronized (newClients) {
			    this.newClients.put(id, obj);
		}
		  logger.info("****************user login************");
		  logger.info("******current login user count:" + currentLoginClients() + "*****");
	}
	
	

	public void rejectlogin(Client client) {
	  logger.info("login one group number:" + BaseConstants.ONE_GROUP_NUMBER + "!");
	}

	public Client createClient(String uuid,SocketChannel socket) {
		int id = getClientId();
		Client client = new Client(uuid,socket, this, id);
		return client;
	}

}
