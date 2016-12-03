package p.minn.server;

import p.minn.controller.Controller;
import p.minn.packet.amf.RtmpPacket;
import p.minn.packet.amf.RtmpWrapper;

/**
 * @author minn
 * @QQ:394286006
 * 
 */
public class Server extends BaseServer<RtmpPacket,RtmpWrapper> {

	private static Server server;
	
	public Server(){
		  super();
		  controller=new Controller();
		  controller.start();
	}
	
	public static Server getInstance(){
		if(server==null)
			server=new Server();
		server.start();
		return server;
	}
}
