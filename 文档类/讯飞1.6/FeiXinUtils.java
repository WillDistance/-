package com.thinkive.framework.util;

import com.iflytek.cloud.speech.*;
import com.thinkive.base.config.Configuration;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.weixin.base.service.WeiXinRedisClient;

import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * ��������Ѷ�����ϳ�
 * @author ����
 * @created 2016��7��8�� ����2:45:22
 * @since
 */
public class FeiXinUtils 
{
    private static Logger    logger    = Logger.getLogger(FeiXinUtils.class);
    private static Object  error_n =null;
    private static int progress_n =-1;
    private static boolean f = false;
    private static String url;
    static int i =1;
    static WeiXinRedisClient client =new WeiXinRedisClient();
    //private static String PATH = "";//�ļ�·��
  
    
	public static DataRow synthesizeToUri(String content, final String path)
	{
	    File file = new File(path+".wav");
	    
	        //PATH = path;
	    final DataRow data = new DataRow();
		String appid = Configuration.getString("system.appid", "577e2d72");
		String voiceName = Configuration.getString("system.voiceName", "xiaoyan");
		//      param.append( ","+SpeechConstant.LIB_NAME_32+"=myMscName" );
		SpeechUtility.createUtility("appid=" + appid);
		//1.����SpeechSynthesizer����  
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer();
		//2.�ϳɲ������ã������iFlytek MSC Reference Manual��SpeechSynthesizer ��  
		
            mTts.setParameter(SpeechConstant.VOICE_NAME, voiceName);//���÷�����  
            mTts.setParameter(SpeechConstant.SPEED, "50");//�������٣���Χ0~100  
            mTts.setParameter(SpeechConstant.PITCH, "50");//�����������Χ0~100  
            mTts.setParameter(SpeechConstant.VOLUME, "50");//������������Χ0~100  
            mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, path);
            // ���ò�������Ƶ������  
            mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
            
            
                
                //3.��ʼ�ϳ�  
                //���úϳ���Ƶ����λ�ã����Զ��屣��λ�ã���Ĭ�ϱ����ڡ�iflytek.pcm��  
            mTts.synthesizeToUri(content, path+".pcm",  new SynthesizeToUriListener()
            {
                //progressΪ�ϳɽ���0~100   
                public void onBufferProgress(int progress)
                {

                    progress_n = progress;
                    logger.info(progress_n);
                   /* client.set("yuying@"+path,progress+"");
                    client.expire("yuying@"+path, 60*30);//���ù���ʱ��30����
*/                  if(progress==100){
                                try {
                                     String str = ConvertAudio.convertAudioFiles(path+".pcm", path+".wav");
                                     logger.info("ConvertAudio.convertAudioFiles����");
                                     if(str.equals("OK"));{
                                        f = true;
                                     }
                                }catch (Exception e)  {
                                        f = false;
                                        logger.info(e);
                                }
                    }
                //�Ự�ϳ���ɻص��ӿ�  
                //uriΪ�ϳɱ����ַ��errorΪ������Ϣ��Ϊnullʱ��ʾ�ϳɻỰ�ɹ�  
                }
                
                public void onSynthesizeCompleted(String uri, SpeechError error)
                {
                    logger.error("�ϳ�·��:"+uri);
                    logger.error("�ϳ����:"+error);
                    error_n = error;
                    url = uri;
                   /* client.set("yuying_error@"+path,error.toString());
                    client.expire("yuying_error@"+path, 60*30);*/
                     
                }
            });
            int m=0;
           
            while(true){
                m++;
                try
                {
                    Thread.sleep(300);//�ӳټ��������ϳɷ���ֵ
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    return data;
                   
                };
                if(f&&file.exists()){
                    //�ϳɳɹ�
                    logger.info(f);
                    return data;
                }else if((m==1&&progress_n==100&&error_n!=null)||(m==10&&progress_n==-1&&error_n!=null)){
                      data.set("error","�ϳ�ʧ��");
                      return data;
                }             
            }
           
        }

            public  static  void main(String... args) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String separator = File.separator;
                String path = "D:";
                String xdpath = separator+"upload"+separator + dateFormat.format(new Date()) + separator + System.currentTimeMillis();
                String filePath = path+ xdpath;
                DataRow data =  FeiXinUtils.synthesizeToUri(" ���εĺ��֮������Ӧ�е������ĳ��������ɾ����޺��Ʈ��Ĺ�Ӱ�£�һ����Խ��Խ�鸡�����͵ĵ���Խ��Խ�ߣ����ݵĳ߶�Խ��ԽС���ػ������Խ��Խ�١�Խ��Խ�����ʱ�����ѧ������˽���������ܣ����Լ��߸����ϵķ��������޿������λ�á���������һ���˵Ķ���Ϸ�����ٷ����빲��\n" +
                        "\n" +
                        "���� ׯ����ʥ�Ĵ��ɫ���֤�飬��������ȥ�����������ƺͲ����ַ������ϣ�����˿��������������Ʒ�������á���һ���ա�����̬ȥ�ǻ����������������飬���룬̤�ſ������������ɣ����ſ����Ĳ�������������������һ����������ǵص�����������\n" +
                        "\n" +
                        "���� һֻ�ճ�¯������������˵��¶���������������������ڸ��³�ʪ�ĵط����ܸ�ҹ����ù���ͺܲ����ˡ���ժ��һֻ������ƻ���������Ƶı�����Ҫ���������ˮ�ɱ�����Ǹ��á������������������ţ������ժ����һ���£���۾����˲���ֱ�ӡ����������ĳ��̣����ں��Ƿ����ʣ�����û����ȷ�ģ����׵ģ����ĵ�ȥ�ǻ���\n" +
                        "\n" +
                        "���� ����Ҳһ������Ҫ���Ƶı��ܲ������������������ӵ����¡�Я���������Χ�ǵ������ˣ����ܳ���ʱ���Ķ���ô���ң����ǲ��úõ����Ĺ��������õ�廨԰�������ڻ���ǰ�����ڵ���֮��ù���á�����ƶ����񣬰����������ȱ��������ȱ�ٵ��ǰѼ�����ȷ��ת��Ϊ����ķ����������ģ�����������װ���ң���װ���㣬��Ĭ����װ�����Ƕ�δ���Ĵ�������һ·���еļ��䡣\n" +
                        "\n" +
                        "���� ���������������������ģ��������ε��������и�����˲�һ����Ҫ��飬�����˻��һ��Ҫ�������Ρ��齨�������ͥ�����˺�Ů�ˣ������ǵ����ĸ��壬���Ǽ������ɻ���ܻ���Լ���Ľ���塣ͬ��һ�������£���ͬ����һ���ң���������ҡ������ĺû����ܲ��ܳ��õ�ʹ�ã�����������ʱ�ܲ��ܵõ����Ƶ�ά������Ҫ����������Э��ȥ�����ġ�\n" +
                        "\n" +
                        "���� �õĻ������������ľ�Ӫ�����ġ�һ�ݺǻ�����һ�ݺ�г��һ�������ջ�һ�ݰ��ȣ�һ�ݸ����õ�һ�����ġ���Ӫ�������Ѥ�����ﻨ��ļң��������볾�������ж�ƣ�����������ŵ���һ�̣��������˵ķ�Χ����������ϵĳ�������ķ�ȥ��һ��ů�ĵĻ���һ��ϲ���Ĳ裬���ո�һ�յļ��У��ô���ĸ���������Ũ��\n" +
                        "\n" +
                        "���� ��ů����Ĳ���ů������ȥ����Ĳ��ǿյ�������һ�ݶ������������ܹ�˩סһ���ĵĲ�����ò����ģ�Ҳ���ǵ�λ����ۣ�������һ���ĵ��ں����������ã��ı�͸��Ĳݷ���Ҳ�ᱻ���ڵ��Ҹ���������������ĸ��壬�������Ϣ�ĵط�������Ҫ�ݻ���ȴһ��Ҫ���ʡ�\n" +
                        "\n" +
                        "���� ����������Ǹ��飬һ��û�кú�άϵ����Ļ�������һ���տǣ�����װ���ô���ݳ޻����������ڷ����в���һ����ÿ�죬ϸ��ĸ��ܰ��˵�һ�һЦ����ʱ�ĵ����Լ����飬ת���Լ���˼�롣���ĵĽ�������˵ı��뻶��������ͬʱ��Ҳ̹�ϵĶ�����˳����Լ����飬���Լ��Ŀ����֣���������ǡ���ķ�ʽ�����밮�ˡ�\n" +
                        "\n" +
                        "���� ���������ð��鳤�ò��ɻ�ȱ�ķ�ʽ֮һ����Щ���ǣ�˵�������Ľ������Ĵ򿪡���Щì�ܣ�˵���������ɸ�ͻữΪ�񲯡���Щ���ԣ�˵������������üͷ�ͻ��Ƶ����ᡣ����Ҳ���ˣ�����δ����֪��Ҳ�����������㣬��Щ������˵����ʧȥ����TA���õĻ��ᡣ���㲻����TA�������ʣ��㻹���Խ��͡�\n" +
                        "\n" +
                        "���� ���޹�ϵ���κ�һ�ֹ�ϵ����һ��������֮�飬����֮�飬������ֿ�ģ��������ǿ�������ȥ���������ĳ�����浱ͷ�����ǻ���������أ�����һ���Ƿ���С����޵����治���������ᣬһ�پ��٣�һ�����û�б˴�֮�֣�û��ʤ��֮˵��ȷ�е�˵���˴�֮����κ���̬����û��ʲô���������ʧ�����ǵ���ĳһ���ġ�\n" +
                        "\n" +
                        "���� �����������ˣ�û�����ɲ��໥�������飬�ͳ��θ�����Ϊ��ҵ��ƴ�Ŀ���ʱ������Ѭ���ǵ���϶������ű�������һ�����£���������ĳ������������˵��ģ�Ҳ�ð��������Լ����ġ����Լ�����������첹���˴����������µ��˿ڣ����Լ����˼��еı���Ҳ�ð�����TA��ϸ�������ڻ���\n" +
                        "\n" +
                        "���� ��õĻ����������ɲ�Ůò���ŵ����ԣ������޲���ò���Ų��������Ե������ˣ��˴��ں����˴���ϧ���˴����ã��˴˷��ף���Ϊ������ֿ�İ����ֲ赭���Ż��ڴ��ݼ����㣬�ֲ��������ܴ��������컵ķ羰��\n" +
                        "\n" +
                        "���� �ܹ��߽������ĵ��ã��������һ�����Ļ����վ����顣ֻ���Ǹ�������Ȼ�Ķ������ڲ�����������Ȼ��ɫ�ʡ���������������Ϊ�������ʱ������˧�������������Щ�ۿۣ�����ѩ����ʱʱΪ����������ĵ�ʱ����������������������ʧЩ����Ҫ��Թ��ǰ�����TA�������ˣ��ܹ��ı�Է������ش󲿷����Լ���ɵġ�\n" +
                        "\n" +
                        "���� ��Ϊһ���������ܶ������ص�Ϊ���˳���һƬ��գ��ùػ��ͺǻ�����������������Ů����������ܰ��ҡ��������������������ӿɰ����Ȼ�ǰ�й�֮���޲�����û���ĸ�Ů��Ը����Թ���������泩��Ů�˻�ѹ���ư���Э��������ĺ������Ȳ�������֮����ȴҲ����������������\n" +
                        "\n" +
                        "���� ��Ϊһ��Ů��������һ��������ĸ������Ϊ���˽�ȥ���֮�ǣ����������ƽ����������ܹ������Լ�����ҵ�������齽���������ӳ������أ���ȫ����ȥΪ�Ҵ�ƴ��û���ĸ�����Ը��Ѽ�ͥ����ս������ů���ʵ�С�ѻ������ִ˲��˵�Ϊ����ϵ��Χȹ����һ��һ���ջ��˵�ζ�٣���ů���˵����顣\n" +
                        "\n" +
                        "���� ����Ŀ���࣬���ĸ�ǿ��ѹ�����ⲻ�˻��������ϵ�ѹ����Ҫ�÷�й�����⡣����й�ķ�ʽ�кܶ��֣�������ƣ������������������˴��������ੲ��ݵı�Թ��ֻ���������ǵ��ˣ��Ż���Ű��ˣ��渵����Լ��ĸо����ʿ��빲����һ�룬Ҳ���Լ��Ķ�����˼�룬��ʱ��TA���޷�������Ļ���ķ�ʽ��������������ʱ��ǡ������һ�����������һ�ݰ��ݣ�һ�����º�һ���ӵĽ��͡�\n" +
                        "\n" +
                        "���� ϸ��İ�����ȥ�ڳ����а�����ɳ����ģ����޸��ڷ紵����е����˺ۣ���Ӫ������Ӧ��ƣ����������ġ��õİ����ǻ������󻬼����ڲ��ϵ�ע���£���������Զ�ĺ�г���á��������޵�ͬ���ɣ�ǧ���޵ù����ߡ�����Ȼ�Ҳ�����ǰ��������˵����������ֻ�Ǹ�һ���Ե����壬����֮�󣬾ͻ�ع���Ȼ��������������ǧǧ�������֮�У��ܹ�������Ĳ����ף�Ӧ����������ϧ�����֮���׵�Ե�֡�\n" +
                        "\n" +
                        "���� ��������һ��ͬ��һ�������£�����������û�иߵ͹��֮�֣�û�д�����ɵ֮�֡����վõ������ĭ�ϰ�������ںϣ��Ը��������������ļ�������������Ѫ�����������顣�����飬������������ǣ���ֵ�����������ϢϢ��ͨ������Σ����������ĸ���Ը������һ��Ĺ��ȣ���Ը����TA���ۣ�TA���죬TA��һ�е�һ�С���", filePath);
            }
	    }
		 
