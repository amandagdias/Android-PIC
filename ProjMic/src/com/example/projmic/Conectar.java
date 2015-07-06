package com.example.projmic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class Conectar extends Thread{
	private BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    BluetoothAdapter mBluetoothAdapter;
    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Conectar(BluetoothDevice device, BluetoothAdapter mBluetoothAdapter) {
    	this.mBluetoothAdapter = mBluetoothAdapter;
        //Inicia o socket e o dispositivo (enviado como parâmetro)    	
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmSocket = tmp;
        //Incia a thread que atualizará a temperatura de 1 em 1 minuto
        this.start();
    }
    //Esta função só é utilizada quando existe o click do botão
    public Conectar (int btn,BluetoothDevice device, BluetoothAdapter mBluetoothAdapter)
    {
    	this.mBluetoothAdapter = mBluetoothAdapter;
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
    	
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmSocket = tmp;
        
        PegarTemp();

    	
    }
    
    private void PegarTemp() {
    	 //Cancela a procura poq isso deixa o app lento
    	mBluetoothAdapter.cancelDiscovery();
    	Log.i("conexao","iniciando conexao");
        try {
            
        	// Pega um bluetooth socket para conectar com o dispositivo            
            //Para disponibilizar um serviço de modo a aguardar por conexões dos clientes
        	Method m = null;
			try {
				m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	try {
				mmSocket = (BluetoothSocket) m.invoke(mmDevice, 1);
			} catch (IllegalAccessException e) {
				
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				
				e.printStackTrace();
			}
        	// Conecta o dispositivo com o socket           
        	mmSocket.connect();
            Log.i("Oi", "conectado!");    
	        //Tranferencia de dados do pic para o app
	         new TransDados(mmSocket, mHandler);           
        } catch (IOException connectException) {
            //Se não conseguiu fazer a conexão fecha o socket 
            try {
                mmSocket.close();
                Log.i("erro","Falha ao iniciar conexão: "+connectException  );
            } catch (IOException closeException) { }
           
        }
		
	}
//Responsável por mostrar as mensagens 
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	String Temp;
      
        	Log.i("msg","" + msg.arg1 + " "+msg.arg2);
        	float temp;
        	temp = (float)((msg.arg1 * 256 + msg.arg2)*5)/10;
        	Temp = String.valueOf(temp);
        	MainActivity.lbl_temp.setText(Temp + " ºC");
        
        }
    };
    @SuppressLint("NewApi")
	public void run() {
    	while(true)
    	{    		
    		 //Cancela a procura poq isso deixa o app lento
	    	mBluetoothAdapter.cancelDiscovery();
	    	Log.i("conexao","iniciando conexao");
	        try {
	            
	        	// Pega um bluetooth socket para conectar com o dispositivo            
	            //Para disponibilizar um serviço de modo a aguardar por conexões dos clientes
	        	Method m = null;
				try {
					m = mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	try {
					mmSocket = (BluetoothSocket) m.invoke(mmDevice, 1);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	//Conecta o dispositivo com o socket
	        	mmSocket.connect();
	            Log.i("Oi", "conectado!");    
		        //Tranferencia de dados do pic para o app
		         new TransDados(mmSocket, mHandler);
		         //"Dorme" por 1 minuto
		         try {			        	
						Thread.sleep(60000);
						Log.i("espera", "esperando");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.i("erro", "espera");
					}
	            
	        } catch (IOException connectException) {
	            //Não foi possível realizar a conexão: Fecha o socket
	            try {
	                mmSocket.close();
	                Log.i("erro","Falha ao iniciar conexão: "+connectException  );
	            } catch (IOException closeException) { }
	           
	        }
	       
    	}
	       
	        
    }
 

	/** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}
