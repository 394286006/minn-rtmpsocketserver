package p.minn.start;


import p.minn.server.Server;
import p.minn.utils.RtmpConstants;

/**
 * @author minn
 * @QQ:394286006
 * 
 */
public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	  RtmpConstants.SOCKET_BLOCK=false;
		Server.getInstance();
	}

}
