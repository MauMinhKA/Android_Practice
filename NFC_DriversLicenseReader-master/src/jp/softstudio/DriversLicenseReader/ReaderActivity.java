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

import jj2000.JJ2000Frontend;
import jp.softstudio.DriversLicenseReader.DriversLicenseReader.Reason;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReaderActivity extends Activity  implements Runnable {

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private static final String TAG = "ReaderActivity";

    private DriversLicenseReader mDriversLicenseReader;
    
    private ProgressDialog progressDialog;
    private Reason tagcheck;
    private Context mContext;
    private Bitmap decodedImage;

    
    private boolean bAction;
    private Tag tag;
    
	public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//?????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  	//?????????????????????

        setContentView(R.layout.reader);
        
        progressDialog=null;
        mContext=this;
        
        Intent intent = getIntent();
        if(intent == null) {
            finish();
            return;
        }
        if(!intent.hasExtra("pass1") || !intent.hasExtra("pass2")) {
        	Toast.makeText(this, "????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String pin1 = intent.getExtras().getString("pass1");
        String pin2 = intent.getExtras().getString("pass2");
        boolean cb1 = intent.getExtras().getBoolean("pass1use");
        boolean cb2 = intent.getExtras().getBoolean("pass2use");
        
        
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };
        mTechLists = new String[][] { new String[] { NfcB.class.getName() },new String[] { IsoDep.class.getName() }};

        
        byte pin1byte[] = pin1.getBytes();
        byte pin2byte[] = pin2.getBytes();

        mDriversLicenseReader = new DriversLicenseReader(pin1byte,pin2byte,cb1,cb2);
        
        bAction=false;
    }

	   @Override 
	   public void onResume()
	   {
		   super.onResume();
	        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
	   }

	   @Override
	   public void onDestroy() {
		   super.onDestroy();
	        mAdapter=null;

	   }

	   
	   @Override
	    public void onNewIntent(Intent intent) {
	        Log.i(TAG, "Discovered tag with intent: " + intent);

	        if(intent == null) {
	            return;
	        }
	        if (bAction==true) {
	        	return;
	        }
	        if (progressDialog!=null) {
	        	return;
	        }

	        
	        bAction=true;
        	TextView tv1 = (TextView) findViewById(R.id.textView1);
        	tv1.setText("???????????????");
	        
        	
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("????????????????????????????????????????????????????????????????????????");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Thread thread = new Thread(this);
            thread.start();
            


	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        if ( this.isFinishing() ) {
	            mAdapter.disableForegroundDispatch(this);
	        }
	    }	  

	    public void run() {
	        // ?????????????????????????????????????????????
		        tagcheck=mDriversLicenseReader.tag_check(tag);
		        bAction=false;
		        if(tagcheck != Reason.SUCCESS)
		        {

		        } else {
		        	decodedImage=null;
	        	    // try to decode JPEG2000
		        	byte b[] = mDriversLicenseReader.getPicture();
		        	if (b!=null) {
		        		decodedImage = JJ2000Frontend.decode(mDriversLicenseReader.getPicture());
		        	}
		        }
		        
	        handler.sendEmptyMessage(0);
	    }

	    
	    @SuppressLint("HandlerLeak")
		private Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	            // ?????????????????????????????????????????????
		        if(tagcheck != Reason.SUCCESS)
		        {
			        if((tagcheck == Reason.PIN1_NG) || (tagcheck == Reason.PIN2_NG)) {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.pin_error);
			        	builder.setPositiveButton("??????", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        } else if((tagcheck == Reason.PIN1_LOCK) || (tagcheck == Reason.PIN2_LOCK)) {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.pin_lock);
			        	builder.setPositiveButton("??????", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        } else if(tagcheck == Reason.NOT_CARD) {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.not_card_error);
			        	builder.setPositiveButton("??????", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        } else {
			        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			        	builder.setMessage(R.string.not_card_error);
			        	builder.setPositiveButton("??????", null);
			        	builder.setIcon(android.R.drawable.ic_dialog_alert);
			        	builder.show();
			        }
		        	
		        } else {
	        	    if (decodedImage != null) {
	            	    ImageView imageView = (ImageView)findViewById(R.id.imageView1);
	            	    imageView.setImageBitmap(decodedImage);
	        	    }
		        	TextView text = (TextView) findViewById(R.id.textView2);
		        	String work;
		        	work = "???????????????"+mDriversLicenseReader.getName()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getKana()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getTusyo()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getToitsu()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getAddress()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getHonseki()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getKubun()+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getBirthDay())+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getMenkyonumber()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getJyoken1()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getJyoken2()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getJyoken3()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getJyoken4()+"\n"+
		        		   "???????????????"+mDriversLicenseReader.getKoanname()+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getNisyogenDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getHokadayDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getNisyuDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getOgataDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getFutuDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getDaitokuDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getDaijiniDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getFutujiniDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getKotokuDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getGentukiDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getKeninDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getDaijiDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getFujiDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getDaitokujiDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getKeninniDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getChuDay())+"\n"+
		        		   "???????????????"+ConvertNego(mDriversLicenseReader.getChuniDay())
		        			;
		        	text.setText(work);
		        }
	        	TextView tv1 = (TextView) findViewById(R.id.textView1);
	        	tv1.setText("??????????????????");

		        // ????????????????????????????????????
	            progressDialog.dismiss();
	            progressDialog=null;
	        }
	    };
	    
	    private String ConvertNego(String topone) 
	    {
	    	String ret="??????";
	    	String one=topone.substring(0, 1);
	    	String after=topone.substring(1);
	    	String year=topone.substring(1, 3);
	    	String month=topone.substring(3, 5);
	    	String day=topone.substring(5, 7);

	    	if (one.compareTo("1")==0) {
	    		ret="??????";
	    	}
	    	if (one.compareTo("2")==0) {
	    		ret="??????";
	    	}
	    	if (one.compareTo("3")==0) {
	    		ret="??????";
	    	}
	    	if (one.compareTo("4")==0) {
	    		ret="??????";
	    	}
	    	ret = ret + year+"???"+month+"???"+day+"???";
	    	
	    	return ret;
	    }

	    
	 
	    
}