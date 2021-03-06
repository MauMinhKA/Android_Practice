/*
The MIT License (MIT)

Copyright (c) <2013> <Soft-Studio K.K. info@soft-studio.jp>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package jp.softstudio.DriversLicenseReader;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

public class DriversLicenseReader {
	private static final int DF1EF1length = 880;
	private static final int DF1EF2length = 82;
	private static final int DF1EF3length = 264;
	private static final int DF1EF4length = 640;
	private static final int DF1EF5length = 663;
	private static final int DF1EF6length = 256;
	private static final int DF1EF7length = 578;
	private static final int DF2EF1length = 2000;
	
	
	private static String name = "";
	private static String kana = "";
	private static String tusyo = "";
	private static String toitsu = "";
	private static String birth = "";
	private static String address = "";
	private static String kofuday = "";
	private static String syoukai = "";
	private static String kubun = "";
	private static String yukoday = "";
	private static String joken1 = "";
	private static String joken2 = "";
	private static String joken3 = "";
	private static String joken4 = "";
	private static String koanname = "";
	private static String menkyonumber = "";

	private static String nisyogenday = "";	//	??????????????????(???????????????)(??????(???6)YYMMDD)(???9)
	private static String hokaday = ""; //??????????????????(???)(??????(???6)YYMMDD)(???9)
	private static String nisyuday = "";//	??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String ogataday =""; //??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String futuday = "";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String daitokuday = ""; //??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String daijiniday ="";// ??????????????????(?????????)(??????(???6)YYMMDD)(???9)
	private static String futujiniday="";//??????????????????(?????????)(??????(???6)YYMMDD)(???9)
	private static String kotokuday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String gentukiday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String keninday="";//	??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String daijiday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String fujiday="";//??????????????????(??????)(??????(???6)YYMMDD)(???9)
	private static String daitokuji="";//??????????????????(?????????)(??????(???6)YYMMDD)(???9)
	private static String keninniday="";//??????????????????(?????????)(??????(???6)YYMMDD)(???9)
	private static String chuday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9,???12)
	private static String chuniday="";//??????????????????(??????)(??????(???6)YYMMDD)(???9,???12)
	
	private static String honseki="";
	
	private static byte[] Gaiji1;
	private static byte[] Gaiji2;
	private static byte[] picture;

	private static byte kisaijikoutuiki;//????????????????????? ??????
	
	private static byte[] pin1byte=null;
	private static byte[] pin2byte=null;
	private static boolean bUsePin1=false;
	private static boolean bUsePin2=false;

	
	public enum Reason {
		SUCCESS,
		NOT_CARD,
		PIN1_LOCK,
		PIN2_LOCK,
		PIN1_NG,
		PIN2_NG,
		OTHER
	};
	
	private String TAG = "DriversLicenseReaderClass";
	private String logStr ="";
	
	
	public DriversLicenseReader(byte[] data1,byte[] data2,boolean pin1,boolean pin2) {
		pin1byte=null;
		bUsePin1=false;
		if (pin1==true) {
			pin1byte= new byte[4];
			pin1byte = Arrays.copyOf(data1,4);
			bUsePin1=true;
		}
		
		pin2byte=null;
		bUsePin2=false;
		if (pin2==true) {
			pin2byte= new byte[4];
			pin2byte = Arrays.copyOf(data2,4);
			bUsePin2=true;
		}
		
	}
	public DriversLicenseReader(byte[] data1,byte[] data2) {
		
		pin1byte=null;
		pin1byte= new byte[4];
		pin1byte = Arrays.copyOf(data1,4);
		bUsePin1=true;
		
		pin2byte=null;
		pin2byte= new byte[4];
		pin2byte = Arrays.copyOf(data2,4);
		bUsePin2=true;
	}
	
	public DriversLicenseReader()
	{
		
	}
	
	
	public Reason tag_check(Tag tag) {
		Reason result = Reason.SUCCESS;
        byte commnadMF01[] = {0x00,(byte)0xA4,0x00,0x00};
        byte commnadVREF1[] = {0x00,0x20,0x00,(byte)0x81};
        byte commnadVREF2[] = {0x00,0x20,0x00,(byte)0x82};
        byte commnadVRPIN1[] = {0x00,0x20,0x00,(byte)0x81,0x00,0x00,0x00,0x00,0x00};
        byte commnadVRPIN2[] = {0x00,0x20,0x00,(byte)0x82,0x00,0x00,0x00,0x00,0x00};
        byte commnadDF01[] = {0x00,(byte)0xA4,0x04,0x0C,0x10,(byte)0xA0,0x00,0x00,0x02,0x31,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        byte commnadDF02[] = {0x00,(byte)0xA4,0x04,0x0C,0x10,(byte)0xA0,0x00,0x00,0x02,0x31,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        byte commnadEF01[] = {0x00,(byte)0xA4,0x02,0x0C,0x02,0x00,0x01};
        byte commnadEF02[] = {0x00,(byte)0xA4,0x02,0x0C,0x02,0x00,0x02};
        byte commnadEF03[] = {0x00,(byte)0xA4,0x02,0x0C,0x02,0x00,0x03};
        byte commnadEF04[] = {0x00,(byte)0xA4,0x02,0x0C,0x02,0x00,0x04};
        byte commnadEF05[] = {0x00,(byte)0xA4,0x02,0x0C,0x02,0x00,0x05};
        byte commnadEF06[] = {0x00,(byte)0xA4,0x02,0x0C,0x02,0x00,0x06};
        byte commnadREAD01[] = {0x00,(byte)0xB0,0x00,0x00,0x40};


        byte retbyte[] = new byte[2048];
		
		IsoDep isodep;
		isodep=null;
		picture=null;

		name = "";
		kana = "";
		tusyo = "";
		birth = "";
		address = "";
		kofuday = "";
		syoukai = "";
		kubun = "";
		yukoday = "";
		joken1 = "";
		joken2 = "";
		joken3 = "";
		joken4 = "";
		koanname = "";
		menkyonumber = "";

		nisyogenday = "";	//	??????????????????(???????????????)(??????(???6)YYMMDD)(???9)
		hokaday = ""; //??????????????????(???)(??????(???6)YYMMDD)(???9)
		nisyuday = "";//	??????????????????(??????)(??????(???6)YYMMDD)(???9)
		ogataday =""; //??????????????????(??????)(??????(???6)YYMMDD)(???9)
		futuday = "";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
		daitokuday = ""; //??????????????????(??????)(??????(???6)YYMMDD)(???9)
		daijiniday ="";// ??????????????????(?????????)(??????(???6)YYMMDD)(???9)
		futujiniday="";//??????????????????(?????????)(??????(???6)YYMMDD)(???9)
		kotokuday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
		gentukiday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
		keninday="";//	??????????????????(??????)(??????(???6)YYMMDD)(???9)
		daijiday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9)
		fujiday="";//??????????????????(??????)(??????(???6)YYMMDD)(???9)
		daitokuji="";//??????????????????(?????????)(??????(???6)YYMMDD)(???9)
		keninniday="";//??????????????????(?????????)(??????(???6)YYMMDD)(???9)
		chuday="";// ??????????????????(??????)(??????(???6)YYMMDD)(???9,???12)
		chuniday="";//??????????????????(??????)(??????(???6)YYMMDD)(???9,???12)
		
		honseki="";
		
		
		for (int i=0;i<3;i++) {
			try {
				isodep = IsoDep.get(tag);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				isodep=null;
			}
		}
		if(isodep == null) {
			result = Reason.OTHER;
			return result;
		}
        if(!ConnectCard(isodep)) {
			result = Reason.NOT_CARD;
			return result;
        }
        
        //MF??????
        if (!AccessCard(isodep,commnadMF01,retbyte)) {	
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        //????????????????????????????????????
        if (!AccessCard(isodep,commnadVREF1,retbyte)) {	
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        if(retbyte[0] != (byte)0x63){
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        if (retbyte[1] != (byte)0xC1 && retbyte[1] != (byte)0xC2 && retbyte[1] != (byte)0xc3)  {
            CloseCard(isodep);
			result = Reason.PIN1_LOCK;
			return result;
        }
        
        //????????????????????????????????????
        if (!AccessCard(isodep,commnadVREF2,retbyte)) {	
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        if(retbyte[0] != (byte)0x63){
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        if (retbyte[1] != (byte)0xC1 && retbyte[1] != (byte)0xC2 && retbyte[1] != (byte)0xc3)  {
            CloseCard(isodep);
			result = Reason.PIN2_LOCK;
			return result;
        }
        
        if((bUsePin1)&&(pin1byte==null)) {
            CloseCard(isodep);
			result = Reason.PIN1_NG;
			return result;
        }
        if((bUsePin2)&&(pin2byte==null)) {
            CloseCard(isodep);
			result = Reason.PIN2_NG;
			return result;
        }
        
        
        if ((pin1byte!=null) && (bUsePin1)){	//Piin1??????
        	//PIN1??????
        	commnadVRPIN1[4] = 4;
        	commnadVRPIN1[5] = pin1byte[0];
        	commnadVRPIN1[6] = pin1byte[1];
        	commnadVRPIN1[7] = pin1byte[2];
        	commnadVRPIN1[8] = pin1byte[3];
/*	?????????????????????		*/
        	if (!AccessCard(isodep,commnadVRPIN1,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] == (byte)0x63){
        		CloseCard(isodep);
        		result = Reason.PIN1_NG;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
/**/
        }
        if ((pin2byte!=null) && (bUsePin2)){	//Piin2??????
        	//PIN2??????
        	commnadVRPIN2[4] = 4;
        	commnadVRPIN2[5] = pin2byte[0];
        	commnadVRPIN2[6] = pin2byte[1];
        	commnadVRPIN2[7] = pin2byte[2];
        	commnadVRPIN2[8] = pin2byte[3];
/*	?????????????????????		*/
        	if (!AccessCard(isodep,commnadVRPIN2,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] == (byte)0x63){
        		CloseCard(isodep);
        		result = Reason.PIN2_NG;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
/**/
        }
    
        //////////////
        ///????????????DF01
        //////////////

        
        //DF01??????
        if (!AccessCard(isodep,commnadDF01,retbyte)) {	
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
            CloseCard(isodep);
			result = Reason.NOT_CARD;
			return result;
        }
        
        if ((pin1byte!=null) && (bUsePin1)){	//Pin1??????

        	//EF01??????
        	if (!AccessCard(isodep,commnadEF01,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}	

        	//READ
        	if (!ReadData(isodep,commnadREAD01,retbyte,DF1EF1length)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	parse_tag(retbyte);
        

        	//EF03??????
        	if (!AccessCard(isodep,commnadEF03,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	//READ
        	if (!ReadData(isodep,commnadREAD01,retbyte,DF1EF3length)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	parse_tag(retbyte);

        	//EF04??????
        	if (!AccessCard(isodep,commnadEF04,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	//READ
        	if (!ReadData(isodep,commnadREAD01,retbyte,DF1EF4length)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	parse_tag(retbyte);
        }

        if ((pin2byte!=null) && (bUsePin2)){	//Piin2??????
            
        	//EF02??????
        	if (!AccessCard(isodep,commnadEF02,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	//READ
        	if (!ReadData(isodep,commnadREAD01,retbyte,DF1EF2length)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	parse_tag(retbyte);
        }

        
        //////////////
        ///????????????DF01
        //////////////
        //////////////
        ///????????????DF02
        //////////////
        
        if ((pin2byte!=null) && (bUsePin2)){

        	//DF02??????
        	if (!AccessCard(isodep,commnadDF02,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	//EF01??????
        	if (!AccessCard(isodep,commnadEF01,retbyte)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	if(retbyte[0] != (byte)0x90 || retbyte[1] != (byte)0x00){
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	//READ
        	if (!ReadData(isodep,commnadREAD01,retbyte,DF2EF1length)) {	
        		CloseCard(isodep);
        		result = Reason.NOT_CARD;
        		return result;
        	}
        	parse_tag_picture(retbyte);
        
        }
        
        
        CloseCard(isodep);
		
		isodep=null;
        
		return result;
	}
	
	//?????????	
	private boolean ConnectCard(IsoDep isodep) {
		boolean result = false;

		if(!isodep.isConnected()) {
            try {
				isodep.connect();
				result=true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v(TAG,e.toString());
			}
        }
		return result;
	}
	
	
	private boolean AccessCard(IsoDep isodep,byte[] command, byte[] outbyte) {
		boolean result = false;
		 byte[] ret;
		try {
			ret = isodep.transceive(command);
        	Logout(ret);
        	for (int i=0;i<ret.length;i++) {
        		outbyte[i]=ret[i];
        	}
        	result=true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG,e.toString());
		}
		
		return result;
	}
	
	private boolean ReadData(IsoDep isodep,byte[] command, byte[] outbyte,int Length) {
		boolean result = false;
		int readLength=0;
		byte[] ret;
		try {
			do {
				command[2] = (byte)(readLength >> 8 & 0xff);;
				command[3] = (byte)(readLength & 0xff);;
				ret = isodep.transceive(command);
	        	Logout(ret);
	        	for (int i=0;i<ret.length;i++) {
	        		outbyte[i+readLength]=ret[i];
	        	}
	        	if (ret.length<=2) {
	        		break;
	        	}
	        	result=true;
	        	readLength=readLength+ret.length-2;
			} while(readLength<Length);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG,e.toString());
		}
		
		return result;
	}
	
	//????????????
	private void CloseCard(IsoDep isodep) {
		try {
			isodep.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG,e.toString());
		}
	}
	
	private void Logout(byte[] logbyte) {
		boolean debugLag=true;
		if (debugLag == true) {
			logStr="";
			for(int i=0;i<logbyte.length;i++) {
				logStr+="0x"+Integer.toHexString(logbyte[i]&0xFF)+",";
			}
			Log.v(TAG,logStr); 
		}
	}
	
	private void parse_tag_picture(byte[] data) {
		int index=0;
		int length=0;
		if ((data[index]==0x5F) && (data[index+1]==0x40)) {
			index=index+2;
			length=(data[index+1]<<8 & 0xFF00)+(data[index]&0xFF);
			index=index+2;
			picture = new byte[length];
			int count=0;
			do {
				if ((data[index]==(byte)0xFF) && (data[index+1]==(byte)0x4F)) {
					break;
				}
				index++;
//				length--;
				count++;
			} while(count<length);
			try {
				picture = Arrays.copyOfRange(data,index,index+length);
			} catch (Exception e) {
			      e.printStackTrace();
		    }
		}
	}

	
	private void parse_tag(byte[] data) {
		boolean bLoop = true;
		int index=0;
		int length=0;
		int errpos=0;
		try {
			
		do {
			switch (data[index]) {
			case 0x11:
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				break;
			case 0x12:	//??????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				name = convertByteToJisString(data,index,length); 
				break;
			case 0x13:	//??????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				kana = convertByteToJisString(data,index,length); 
				break;
			case 0x14:	//?????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				tusyo = convertByteToJisString(data,index,length); 
				break;
			case 0x15:	//????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				toitsu = convertByteToJisString(data,index,length); 
				break;
			case 0x16:	//????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				birth = convertByteToAsciiString(data,index,length); 
				break;
			case 0x17:	//??????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				address = convertByteToJisString(data,index,length); 
				break;
			case 0x18:	//???????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				kofuday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x19:	//????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				syoukai = convertByteToAsciiString(data,index,length); 
				break;
			case 0x1A:	//??????????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				kubun = convertByteToJisString(data,index,length); 
				break;
			case 0x1B:	//?????????????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				yukoday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x1C:	//???????????????1
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				joken1 = convertByteToJisString(data,index,length); 
				break;
			case 0x1D:	//???????????????2
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				joken2 = convertByteToJisString(data,index,length); 
				break;
			case 0x1E:	//???????????????3
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				joken3 = convertByteToJisString(data,index,length); 
				break;
			case 0x1F:	//???????????????4
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				joken4 = convertByteToJisString(data,index,length); 
				break;
			case 0x20:	//??????????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				koanname = convertByteToJisString(data,index,length); 
				break;
			case 0x21:	//??????????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				menkyonumber = convertByteToAsciiString(data,index,length); 
				break;
			case 0x22:	//??????????????????(???????????????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				nisyogenday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x23:	//??????????????????(???)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				hokaday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x24:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				nisyuday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x25:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				ogataday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x26:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				futuday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x27:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				daitokuday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x28:	//??????????????????(?????????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				daijiniday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x29:	//??????????????????(?????????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				futujiniday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x2A:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				kotokuday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x2B:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				gentukiday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x2C:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				keninday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x2D:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				daijiday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x2E:	//??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				fujiday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x2F:	//??????????????????(?????????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				daitokuji = convertByteToAsciiString(data,index,length); 
				break;
			case 0x30:	//??????????????????(?????????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				keninniday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x31:	// ??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				chuday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x32:	// ??????????????????(??????)
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				chuniday = convertByteToAsciiString(data,index,length); 
				break;
			case 0x41:	//??????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				honseki = convertByteToJisString(data,index,length); 
				break;
			case 0x48:	//?????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) {
					Gaiji1 = new byte[1];
					break;
				}
				Gaiji1 = new byte[length];
				Gaiji1 = Arrays.copyOfRange(data,index,index+length);
				break;
			case 0x49:	//?????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) {
					Gaiji2 = new byte[1];
					break;
				}
				Gaiji2 = new byte[length];
				Gaiji2 = Arrays.copyOfRange(data,index,index+length);
				break;
			case 0x50:	//???????????????
				errpos=data[index];
				index++;
				length=data[index++];
				if (length==0) break;
				kisaijikoutuiki=data[index];
				break;
			default:
				bLoop=false;
				break;
			}

			
			index = index+length;
			
			
		} while(bLoop);
		
		} catch (Exception e) {
		      e.printStackTrace();
		      
	    }
	}
	private String convertByteToAsciiString(byte[] data,int index, int length) {
		String result ="";
		
		try {
			result = new String(data,index,length,"ISO2022JP");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private String convertByteToJisString(byte[] data,int index, int length) {
		String result ="";
		try {
			byte[] kanjiin = {0x1B,0x24,0x42};

			byte[] temp =  Arrays.copyOfRange(data,index,index+length);
			byte[] marge = new byte[kanjiin.length+temp.length];
			System.arraycopy(kanjiin, 0, marge,0, kanjiin.length);
			System.arraycopy(temp, 0, marge,kanjiin.length, temp.length);
			try {
				result = new String(marge,"ISO2022JP");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
	      e.printStackTrace();
	    }
		
		return result;
	}
	
	
	public String getName()
	{
		return name;
	}
	public String getKana()
	{
		return kana;
	}
	public String getTusyo()
	{
		return tusyo;
	}
	public String getToitsu()
	{
		return toitsu;
	}
	public String getBirthDay()
	{
		return birth;
	}
	public String getAddress()
	{
		return address;
	}
	public String getKofuDay()
	{
		return kofuday;
	}
	public String getSyoukaiNumber()
	{
		return syoukai;
	}
	public String getKubun()
	{
		return kubun;
	}
	public String getYukouDay()
	{
		return yukoday;
	}
	public String getJyoken1()
	{
		return joken1;
	}
	public String getJyoken2()
	{
		return joken2;
	}
	public String getJyoken3()
	{
		return joken3;
	}
	public String getJyoken4()
	{
		return joken4;
	}
	public String getMenkyonumber()
	{
		return menkyonumber;
	}
	public String getHonseki()
	{
		return honseki;
	}
	public String getKoanname()
	{
		return koanname;
	}
	public byte[] getPicture()
	{
		return picture;
	}
	public String getNisyogenDay()
	{
		return nisyogenday;
	}
	public String getHokadayDay()
	{
		return hokaday;
	}
	public String getNisyuDay()
	{
		return nisyuday;
	}
	public String getOgataDay()
	{
		return ogataday;
	}
	public String getFutuDay()
	{
		return futuday;
	}
	public String getDaitokuDay()
	{
		return daitokuday;
	}
	public String getDaijiniDay()
	{
		return daijiniday;
	}
	public String getFutujiniDay()
	{
		return futujiniday;
	}
	public String getKotokuDay()
	{
		return kotokuday;
	}
	public String getGentukiDay()
	{
		return gentukiday;
	}
	public String getKeninDay()
	{
		return keninday;
	}
	public String getDaijiDay()
	{
		return daijiday;
	}
	public String getFujiDay()
	{
		return fujiday;
	}
	public String getDaitokujiDay()
	{
		return daitokuji;
	}
	public String getKeninniDay()
	{
		return keninniday;
	}
	public String getChuDay()
	{
		return chuday;
	}
	public String getChuniDay()
	{
		return chuniday;
	}
	
	
	
	
	
	
	public void setPin1(byte[] data) 
	{
		pin1byte=null;
		pin1byte= new byte[4];
		pin1byte = Arrays.copyOf(data,4);
		bUsePin1=true;
	}
	
	public void setPin2(byte[] data) 
	{
		pin2byte=null;
		pin2byte= new byte[4];
		pin2byte = Arrays.copyOf(data,4);
		bUsePin2=true;
	}
	
}
