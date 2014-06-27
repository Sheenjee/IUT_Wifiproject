package packet.projetwifi;

import java.util.List;
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
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	
	Button b1; //Bouton Scan
	Button[] Bconnec = new Button[50]; //liste de bouton
	LinearLayout llt;
	ScrollView llw;
	WifiManager wifi;
	TextView txt;
	Builder pop; //Fenetre pop-up
	int i;
	Toast test; 
	String selec;
	EditText input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Activation du Wifi
		wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if(!wifi.isWifiEnabled()){
		if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING){
			wifi.setWifiEnabled(true);
			}
		}
		
		input = new EditText(this); //Champt de saisie
		input.setText("");
		test = new Toast(this); //Toast
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
		//Bouton Scan
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
			String info = String.format("%s:\nBSSID: %s\nPower: %sdB\nAuth: -%s-\nFreq: %sMHz", result.SSID,result.BSSID,result.level,result.capabilities,result.frequency);
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
		//Bouton réseaux Wifi
		else{
			//recuperation du SSID
			String stat = "" + Bconnec[v.getId()].getText();
			StringTokenizer st1 = new StringTokenizer(stat, ":");
			final String Ssid = st1.nextToken();
			//recuperation type Authentification
			StringTokenizer st2 = new StringTokenizer(stat, "-");
			String[] stats;
			stats = new String[10];
			i=0;
			while(st2.hasMoreTokens()){
				stats[i] = st2.nextToken();
				i++;
			}
			
			String Auth = stats[2]; //Authentification en case 2
			
			pop.setTitle("" + Ssid + " - " + Auth); //Titre dialog
			pop.setMessage("Pass (si nécessaire):");
			pop.setView(input);
			pop.setPositiveButton("Connexion", new DialogInterface.OnClickListener() {
				//Bouton Connexion
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Context con = getApplicationContext();
					String tos = "Connexion...";
					test.makeText(con, tos, test.LENGTH_SHORT).show();
					//Connexion à un réseaux ouvert
					WifiConfiguration conf = new WifiConfiguration();					
					conf.SSID = "\"" + Ssid + "\"";
					conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				
					WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
					wifiManager.addNetwork(conf);
					
					List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
					for( WifiConfiguration i : list ) {
					    if(i.SSID != null && i.SSID.equals("\"" + Ssid + "\"")) {
					         wifiManager.disconnect();
					         wifiManager.enableNetwork(i.networkId, true);
					         wifiManager.reconnect();               

					         break;
					    }           
					 }
					
				}
			});pop.show();	
		}
	}
}
