package holamundo_sockets;

/**
 * Este server tiene que recibir los datos y a la vez permanecer a la escucha del puerto que le 
 * especificamos en el lado cliente.
 * Para hacer estas dos cosas a la vez, deberemos utilizar threads.
 * */
import javax.swing.*;

import java.awt.*;
// import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor  {

	public static void main(String[] args) {
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;

	public MarcoServidor(){
		
		setBounds(1200,300,280,350);
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		//Creamos un hilo
		Thread miHilo = new Thread(this);
		
		miHilo.start();
		
	}
	
	private	JTextArea areatexto;

	@Override
	public void run() {
		
		ServerSocket servidor = null;
		
		try {
			//1. Ponemos el servidor a la escucha
			servidor = new ServerSocket(9999);
			
			/**Creamos las variables para almacenar las variables que llegan desde el Cliente. */
			String nick, ip, mensaje;
			
			PaqueteEnvio paquete_recibido;
			
			while(true) {
			
				//2. que acepte las conexiones que le vengan del exterior.
				Socket miSocket = servidor.accept();
				
				ObjectInputStream paquete_datos = new ObjectInputStream(miSocket.getInputStream());
				
				/* Almacenamos lo recibido de paquete envio con nuestro Stream */
				paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();
				
				nick = paquete_recibido.getNick();
				ip = paquete_recibido.getIp();
				mensaje = paquete_recibido.getMensaje();
				
				/**3. Crear un flujo de entrada de datos, para poder recibir el objeto que manda el cliente.
				DataInputStream flujoEntrada = new DataInputStream(miSocket.getInputStream());
				
				//4. Leemos el flujo de entrada
				String mensaje = flujoEntrada.readUTF();
				
				areatexto.append("\n Recibido: "+ mensaje);
				
				flujoEntrada.close();*/
				
				/** Mostramos en el area de texto lo que hemos recibido de Cliente */
				areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);
				
				/* Creamos un Socket que hará de puente de comunicación entre 
				 * el cliente que recibe el comentario del remitente */
				Socket enviaDestinatario = new Socket(ip, 9090);
				
				ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
				
				/* Metemos dentro de paqueteReenvio el objeto paqueteRecibido */
				paqueteReenvio.writeObject(paquete_recibido);
				
				paqueteReenvio.close();
				enviaDestinatario.close();
				miSocket.close();
			
			}
			
			
		} catch (IOException | ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
	}
}