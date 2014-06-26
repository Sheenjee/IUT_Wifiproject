package packet.projetwifi;

import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import android.net.wifi.*;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
	
	Button b1;
	Button[] Bconnec = new Button[50];
	LinearLayout llt;
	ScrollView llw;
	WifiManager wifi;
	TextView txt;
	Builder pop;
	int i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if(!wifi.isWifiEnabled()){
		if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
			wifi.setWifiEnabled(true);
			}
		}
		

		pop = new AlertDialog.Builder(this);
		llw = new ScrollView(this);
		
		llt = new LinearLayout(this);
		llt.setGravity(Gravity.TOP);
		llt.setOrientation(LinearLayout.VERTICAL);
		
		b1 = new Button(this);
		b1.setText("Scan for WiFis");
		b1.setId(100);
		b1.setOnClickListener(this);
		llt.addView(b1);
		
		txt = new TextView(this);
		txt.setText("Wifi Dispo:");

		
		llw.addView(llt);
		
		setContentView(llw);
		
		
		
	}
	
	public void onClick(View v) {
		
		if(v.getId() == 100){
		
		llt.removeAllViews();
		llw.removeAllViews();
		llt.addView(b1);
		llt.addView(txt);
		TextView mainText = new TextView(this);
		new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		wifi.startScan();
		i=0;
		List<ScanResult> results = wifi.getScanResults();
		for (ScanResult result : results){
			String info = String.format("%s\n - BSSID: %s\n - Power: %sdB\n - Auth: %s\n  - Freq: %sMHz", result.SSID,result.BSSID,result.level,result.capabilities,result.frequency);
			Bconnec[i] = new Button(this);
			Bconnec[i].setId(i);
			Bconnec[i].setText(info);
			Bconnec[i].setOnClickListener(this);
			llt.addView(Bconnec[i]);
			i++;
		}

		llt.addView(mainText);
		llw.addView(llt);
		setContentView(llw);
		
		}
		else{
			
			String sta = "" + Bconnec[v.getId()].getText();
			StringTokenizer st = new StringTokenizer(sta, "-");
			String Ssid = st.nextToken();
			
			pop.setTitle("" + Ssid);
			pop.setMessage("Mot de passe :");
			final EditText input = new EditText(this);
			pop.setView(input);
			pop.setPositiveButton("Connexion", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// connexion
					
				}
			});
			pop.show();	
		}
	}
	

}
