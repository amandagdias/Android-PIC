package com.example.projmic;

import java.util.ArrayList;
import java.util.Set;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {
	BluetoothAdapter adaptador;
	
	ArrayList<BluetoothDevice> lista_emparelhados;
	public static TextView lbl_temp;
	ImageView img_refresh;
	BluetoothDevice myDisp;
	Thread t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MapearComponentes();
		MapearEventos();
		
		
		ProcurarDispositivo();
		
		if (myDisp!=null){
			
			Conexão();	
		}	
		
	}

	private void Conexão() {
	
			new Conectar(myDisp, adaptador);
		
		
		
	}	
		
	

	private void ProcurarDispositivo() {

		Intent enableBtIntent;
		adaptador = BluetoothAdapter.getDefaultAdapter();
	    if (adaptador == null) {
		   // Aparelho não suporta Bluetooth
		}else{
		
			//ve se o bluetooth está ativo, se nao estivar ativa
		    if (!adaptador.isEnabled()) {
		    	enableBtIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);	
		    	//o codigo continua no onActivityResult
		    	startActivityForResult(enableBtIntent, 1);
		    }
		    else
		    {
		    	// ver se existe dispositivos ja emparelhados
		    	Set<BluetoothDevice> pairedDevices = adaptador.getBondedDevices();
		    	// Se existir dispositivos pareados eu procuro o dispositivo bluetooth
		    	myDisp = null;
		    	if (pairedDevices.size() > 0) {
		    	    // Procura o dispositivo 
		    		
		    	    for (BluetoothDevice device : pairedDevices) {
		    	    	//O nome do JY-MCU default é HC-06
		    	        if (device.getName().equals("HC-06"))
		    	    	{
		    	    		myDisp = device;
		    	    		break;
		    	    	}
		    	    }
		    	}
		    }
		    	
		}	    
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);	
		return true;
		
	}
	private void MapearEventos() {
		//Programação do click do botao
		img_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (myDisp!=null){
					
					new Conectar(1,myDisp, adaptador);	
				}
			
				
			}
		});
		
	}
	private void MapearComponentes() {
		// TODO Auto-generated method stub		
		img_refresh = (ImageView) findViewById(R.id.img_refresh);
		lbl_temp = (TextView) findViewById(R.id.lbl_temp);
		
	}
	@Override
	public void onBackPressed() {
		//Verifica se o usuario deseja sair do sistema
		new AlertDialog.Builder(MainActivity.this);  
		new AlertDialog.Builder(MainActivity.this)  
		.setMessage("Voce realmente deseja fechar o sistema?")  
		.setCancelable(false)  
		.setPositiveButton("Sim", new DialogInterface.OnClickListener() {  
		    public void onClick(DialogInterface dialog, int which)   
		    { 
		        dialog.cancel();  
		        finish();
		    }  
		})    
		.setNegativeButton("Não", new DialogInterface.OnClickListener() {  
		      public void onClick(DialogInterface dialog, int which)   
		      { 
		         dialog.cancel();  
		      }  
		}).show();
		
		 
		
	}

	protected void onActivityResult(
		int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		//Este método é o retorno do dialogo que pergunta se o usuario quer ativar ou nao o bluetooth
		if (requestCode == 1){
			if (RESULT_OK == resultCode){
				
				// ver se existe dispositivos ja emparelhados
		    	Set<BluetoothDevice> pairedDevices = adaptador.getBondedDevices();
		    	// Se existir dispositivos pareados eu procuro o dispositivo bluetooth
		    	myDisp = null;
		    	if (pairedDevices.size() > 0) {
		    	    // Procura o dispositivo 
		    		
		    	    for (BluetoothDevice device : pairedDevices) {
		    	        
		    	    	if (device.getName().equals("HC-06"))
		    	    	{
		    	    		myDisp = device;
		    	    		break;
		    	    	}
		    	    }
		    	} 	
			}else {
		        finish();
		    	}
		}
	}
	


	

}
