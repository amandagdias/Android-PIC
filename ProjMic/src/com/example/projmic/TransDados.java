package com.example.projmic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;



public class TransDados extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler mHandler;
    public TransDados(BluetoothSocket socket, Handler mHandler) {
    	this.mHandler = mHandler;
    	// Inicializa o socket bem como o InputStream e OutputStream
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
		 
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        //Envia o byte 1 para o pic
        String manda = "" + (char)1;
        write(manda.getBytes());
        //Inicia a thread que irá receber os valores do sensor
        this.start();
       
    }
 
    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream      
        int cont, bytes;
        int[] LH;
    
        	LH= new int[2];
            cont= 0;
         
            // vou escutar o inputStream 2x (poderia ser um while(true)
            while (cont<2) {
            	 try {
            		 //Pega o valor do buffer (precisa disso pra "atualizar o buffer"
    				bytes = mmInStream.read(buffer);		          
    				// Envia os bytes obtidos para a classe conectar				
    				LH[cont] = buffer[0];
    				cont++;     
            	 }
    			catch (IOException e) {
    			
    				e.printStackTrace();
    			}
            }
            mHandler.obtainMessage(1,LH[0],LH[1],null).sendToTarget();  
            //Cancela a conexao
            cancel();       
        
        
    }
    
 
    //Manda um byte via serial
    public void write(byte[] bytes) {
        try {
        	        	
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }
 
    //Cancela a conexão
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}