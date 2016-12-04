package p.minn.client;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Map;

import p.minn.controller.Controller;
import p.minn.packet.amf.AmfEncoder;
import p.minn.packet.amf.RtmpDecoder;
import p.minn.packet.amf.RtmpEncoder;
import p.minn.packet.amf.RtmpMessageFactory;
import p.minn.packet.amf.RtmpPacket;
import p.minn.packet.amf.RtmpWrapper;
import p.minn.packet.amf.TypeValue;
import p.minn.utils.DateUtil;
import p.minn.utils.RtmpProtocolUtil;
import p.minn.utils.Util;

/**
 * @author minn
 * @QQ:394286006
 * 
 */
public class Client extends BaseClient<RtmpPacket, RtmpWrapper> {

  private int messageStreamId;
  private long timeStamp;
  private int chunkStreamId;
  private int streamId = 1;

  public Client(String uuid, SocketChannel socket, Controller clientController, int clientId) {
    super(uuid, socket, clientId, clientController);
    decoder = new RtmpDecoder(this, socket);
    encoder = new RtmpEncoder(socket);
  }

  public void onEvent(RtmpWrapper wrapper, RtmpPacket packet) throws Exception {
    // TODO Auto-generated method stub
    RtmpProtocolUtil.traceWrappers(wrapper.getInfo(), "onEvent:" + wrapper.getName());
    String invokeID = wrapper.getName();
    System.out.println("invokeID:"+invokeID);
    if (invokeID.equals("flashCallJava")) {
      javaCallFlash(wrapper);
    }
    if (invokeID.equals("createStream")) {
      // onStreamPlayRequest(arguments,packet);
      onStreamCreateRequest(wrapper);
      // onStreamPublishRequest(arguments,packet);
      // Util.traceRtmpWrappers(arguments, "createStream");

    }
    if (invokeID.equals("play")) {
      // Util.traceWrappers(arguments, "play");
      onStreamPlayRequest(wrapper, packet);
    }
    if (invokeID.equals("publish")) {
      onStreamPublishRequest(wrapper, packet);
    }
  }

  public void javaCallFlash(RtmpWrapper wrapper) throws IOException {
    double responseId = wrapper.getId();
    
    Map<String, TypeValue> args =
        RtmpMessageFactory.infoMessage(this.clientId, "status", "Invoke.Callback.sucess", uuid,
            "显示时间");
        args.put("msg", new TypeValue(DateUtil.toDateTime(System.currentTimeMillis())));
        RtmpPacket packet = RtmpMessageFactory.resultMessage(responseId, 0, 3, 4, args);
        this.addPacket(packet);
   
  }


  public void onStreamPlayRequest(RtmpWrapper wrapper, RtmpPacket packet) throws IOException {
    messageStreamId = packet.messageStreamId;
    double responseId = wrapper.getId();
    timeStamp = packet.timeStamp;
    chunkStreamId = packet.chunkStreamId;
    Map<String, TypeValue> args =
        RtmpMessageFactory.infoMessage(this.clientId, "status", "NetStream.Play.Start", uuid,
            "play start");
    RtmpPacket npacket =
        RtmpMessageFactory.invokeMessage("onStatus", responseId, 0, 0x03, 0x04, 0x14, args);
    this.addPacket(npacket);
  }


  public void onStreamPublishRequest(RtmpWrapper wrapper, RtmpPacket packet) throws IOException {
    // Util.traceWrappers(argumentsX, "streampublish");
    double responseId = wrapper.getId();
    messageStreamId = packet.messageStreamId;
    timeStamp = packet.timeStamp;
    chunkStreamId = packet.chunkStreamId;
    Map<String, TypeValue> args =
        RtmpMessageFactory.infoMessage(this.clientId, "status", "NetStream.Publish.Start", uuid,
            "publish start");
    RtmpPacket packet1 = RtmpMessageFactory.statusMessage(responseId, 0, 0x03, 0x02, args);
    this.addPacket(packet1);
  }


  public void onStreamCreateRequest(RtmpWrapper wrapper) throws IOException {

    double responseId = wrapper.getId();
    // Util.traceWrappers(arguments, "Client.onStreamCreateRequest");
    Map<String, TypeValue> args =
        RtmpMessageFactory.infoMessage(this.clientId, "status", "NetStream.Publish.Start", uuid,
            "createstream request");
    RtmpPacket packet = RtmpMessageFactory.statusMessage(responseId, 0, 0x04, 0x01, args);
    this.getEncoder().add(packet);
    // this.addMethodPacket(packet);

  }

  public void onErrorPacketHandler(String msg) {
    // TODO Auto-generated method stub
    logger.info("onErrorPacketHandler:" + msg);
  }


  @Override
  public void onFlvEvent(RtmpPacket packet, RtmpWrapper wraper) throws Exception {
    // TODO Auto-generated method stub
    logger.info("onFlvEvent");
    // this.broadCast(packet);
  }

  @Override
  public void onAudioEvent(RtmpPacket packet, RtmpWrapper wrapper) throws Exception {
    // TODO Auto-generated method stub
    logger.info("onAudioEvent");
  }

  @Override
  public void existsMessage(double responseId) throws Exception {
    // TODO Auto-generated method stub
    Map<String, TypeValue> args =
        RtmpMessageFactory.infoMessage(this.clientId, "status", "NetConnection.Connect.Success",
            uuid, "用户已登录");
    RtmpPacket newpacket = RtmpMessageFactory.resultMessage(responseId, 0, 0x03, 0x02, args);
    this.getEncoder().add(newpacket);
  }

  public void onControllerEvent(RtmpPacket packet) throws Exception {
    // TODO Auto-generated method stub
    byte[] dst = new byte[2];
    System.arraycopy(packet.body, 0, dst, 0, 2);
    if (AmfEncoder.bytesToInt(dst) == 3) {
      RtmpPacket newpacket = RtmpMessageFactory.pingMessage(34, 0, 2, -1);
      this.getEncoder().add(newpacket);
    }
  }

}
