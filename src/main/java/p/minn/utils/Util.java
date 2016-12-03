package p.minn.utils;

import java.util.List;

import p.minn.packet.amf.TypeValue;


/**
 * @author minn
 * @QQ:394286006
 * 
 */
public class Util extends BaseUtil
{
	
  public static void trace(String desc){
    System.out.println(desc);
}
  public static void traceWrappers(List<TypeValue> ws,String desc)
  {
      System.out.println("**********"+desc+"**************");
      for(int i=0;i<ws.size();i++)
      {
      System.out.println("num"+i+","+ws.get(i).toString());
      }
      System.out.println("**********end***********");
  }

  public static void main(String[] args){
    System.out.println((0|0x05&0x3F));
  }

}
