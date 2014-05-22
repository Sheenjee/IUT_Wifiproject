package packet.projetwifi;

import java.util.List;
import android.net.wifi.*;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
	
	Button b1;
	LinearLayout llt;
	ScrollView llw;
	WifiManager wifi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if(!wifi.isWifiEnabled()){
		if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
			wifi.setWifiEnabled(true);
			}
		}
	
		
		llw = new ScrollView(this);
		
		llt = new LinearLayout(this);
		llt.setGravity(Gravity.TOP);
		llt.setOrientation(LinearLayout.VERTICAL);
		
		b1 = new Button(this);
		b1.setText("Scan for WiFis");
		b1.setId(1);
		b1.setOnClickListener(this);
		llt.addView(b1);
		
		llw.addView(llt);
		
		setContentView(llw);
		
	}
	
	public void onClick(View v) {
		
		llt.removeAllViews();
		llw.removeAllViews();
		llt.addView(b1);
		StringBuilder sb = new StringBuilder();
		TextView mainText = new TextView(this);
		new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		wifi.startScan();
		List<ScanResult> results = wifi.getScanResults();
		for (ScanResult result : results){
			String info = String.format("SSID: %s\nBSSID: %s\nPower: %sdB\nAuth: %s\nFreq: %sMHz\n==========================\n", result.SSID,result.BSSID,result.level,result.capabilities,result.frequency);
			sb.append(info);
		}
		mainText.setText("Available WiFis: \n\n"+sb);
		llt.addView(mainText);
		llw.addView(llt);
		setContentView(llw);
	}

}