package com.example.usb_host;

import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

// MOST IMPORTANT ACTIVITY
public class Third extends Activity{
	private final float ENDPOINT_TEXT_SIZE_f = 10.0f;
	private final float INTERFACE_TEXT_SIZE_f = 15.0f;
	private String DeviceName = null;
	private UsbDevice TheDevice=null;
	private UsbInterface UsbIntTable[] = null;
	private String UsbIntName[] = null;
	private int usbIntEndCount[] = null;
	private int usbAllEndCount = 0;
	private int interfaceCount = 0;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("tag", "WARN: POCZATEK ^^^^^^^^^^^^^^^^^");
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		int whichDevice = data.getInt("whichDevice");
		Toast.makeText(this, "Device nr " + whichDevice , Toast.LENGTH_LONG).show();
		
    	Log.d("tag", "warn: przed intent.getSerializable");
		
		HashMap<String, UsbDevice> deviceList;
		deviceList = (HashMap<String, UsbDevice>) intent.getSerializableExtra("deviceList");
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		
		int k=1;
		UsbDevice device = null;
		
		Log.d("tag", "warn: przed while; deviceList.size() = " + deviceList.size());
		// to sie jeszcze przydasie
		
    	// searching for wanted Device
		
			while (deviceIterator.hasNext()) {
			    device = deviceIterator.next();
			    if (k==whichDevice) {
			    	TheDevice=device;
			    	DeviceName = TheDevice.getDeviceName();
			    	Log.d("tag", "warn: tu go byc nie powinno");
			    	break;
			    }
			    k++;
			}
		if (TheDevice==null) {
			TextView e = new TextView(this);
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			@SuppressWarnings("deprecation")
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			p.setMargins(20, 20, 20, 0);
			e.setText("Error: Device = null");
			e.setLayoutParams(p);
			e.setGravity(Gravity.CENTER);
			layout.addView(e);
			this.setContentView(layout);
		}
		else {
			interfaceCount = TheDevice.getInterfaceCount();
			Log.d("tag", "warn: przed inicjacja; interfaceCount = " + interfaceCount);
			
			if (interfaceCount>0) {
				this.UsbIntTable = new UsbInterface[interfaceCount];
				this.UsbIntName = new String[interfaceCount];
				this.usbIntEndCount = new int[interfaceCount];
				for (k=0; k<interfaceCount; k++) {
					UsbIntTable[k] = TheDevice.getInterface(k);
					UsbIntName[k] = UsbIntTable[k].toString();
					usbIntEndCount[k] = UsbIntTable[k].getEndpointCount();
					usbAllEndCount += usbIntEndCount[k];
				}
			}
			
	    	Log.d("tag", "warn: przed petla z tablicami UsbInt***");
					
			
			///////////////////////////////////////////////// INTERFACE
			
	    	ScrollView scroll_view = new ScrollView(this);  	
	    	
	    	
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			@SuppressWarnings("deprecation")
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);	
			p.setMargins(10, 10, 10, 0);
			TextView NameTV = new TextView(this);
			NameTV.setText("Device Name:\n" + DeviceName + "\nnumber of interfaces: "
					+ interfaceCount + "\nnumber of all endpoints: " + usbAllEndCount + "\nClick to specific endopoint to open a connection on it.");
			NameTV.setTextSize(25.0f);
			NameTV.setLayoutParams(p);
			NameTV.setGravity(Gravity.CENTER);
			layout.addView(NameTV);
					
			TextView tv[] = new TextView[interfaceCount];
			for (k=0; k<interfaceCount; k++) {
				tv[k] = new TextView(this);
				tv[k].setText("\n\tName of interface:\n\t" + UsbIntName + "\n\tnumber of endpoints: " + usbIntEndCount[k]);
				tv[k].setTextSize(INTERFACE_TEXT_SIZE_f);
				tv[k].setGravity(Gravity.CENTER);
				tv[k].setLayoutParams(p);				
				layout.addView(tv[k]);
				
				Button btn[] = new Button[usbIntEndCount[k]];
				for (int i=0; i<usbIntEndCount[k]; i++) {
					btn[i] = new Button(this);
					btn[i].setText(UsbIntTable[i].toString());
					btn[i].setOnClickListener(new View.OnClickListener() {
									   public void onClick(View v)	{
										   		/*tu onCLLICK*/ finishAffinity();
										   		
										   		
									   }});
			        btn[i].setTextSize(ENDPOINT_TEXT_SIZE_f);
					btn[i].setLayoutParams(p);
			        layout.addView(btn[i]);
			        
				}
			}
			scroll_view.addView(layout);
			this.setContentView(scroll_view);
		}
	}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view) {this.onStop();}
	public void onClick2(View view) {super.finishAffinity();}
}
