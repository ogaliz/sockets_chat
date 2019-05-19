package holamundo_sockets;
/**
 * Para poder correr esta app deberemos de cambiar la IP en la linea 73
 * */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;



public class Cliente {

	public static void main(String[] args) {
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel{
	
	public LaminaMarcoCliente(){
		
		// Campo de texto en el que aparece el usuario actual.
		nick = new JTextField(5);
		add(nick);
	
		JLabel texto = new JLabel("-CHAT-");
		add(texto);
		
		// Campo de texto en el que especificamos la dirección ip de la persona con la que queremos chatear.
		ip = new JTextField(8);
		add(ip);
		
		campochat = new JTextArea(12, 20);
		add(campochat);
		
		campo1=new JTextField(20);
		add(campo1);		
	
		miboton=new JButton("Enviar");
		
		EnviaTexto miEvento = new EnviaTexto();
		miboton.addActionListener(miEvento);
		
		add(miboton);			
	}
	
	private class EnviaTexto implements ActionListener{

		//Vamos a crear un Socket dentro de la accion ActionPerformed, para que al pulsar el boton, envie el contenido
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//En este bloque del try preparamos la aplicacion de cliente para que mande los datos
			try {
				//Utilizamos nuestra IP si queremos que se ejecute en local y el puerto, en este caso 9999
				Socket miSocket = new Socket("192.168.99.1", 9999);
				
				// Empaquetamos los datos que han escrito en el Cliente, creamo el Objeto
				PaqueteEnvio datos = new PaqueteEnvio();
				
				// Pasamos lo que hay dentro del cuadro de texto 
				datos.setNick(nick.getText());
				datos.setMensaje(campo1.getText());
				datos.setIp(ip.getText());
				
				/* Creamos un flujo de salida para enviar los datos por la red */
				ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());
				
				paquete_datos.writeObject(datos);
				
				miSocket.close();
				
				/**Creamos un flujo de salida de datos
				DataOutputStream flujoSalida = new DataOutputStream(miSocket.getOutputStream());
				
				flujoSalida.writeUTF(campo1.getText());
				
				flujoSalida.close();*/
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
			} catch (IOException e1) {
				
				System.out.println(e1.getMessage());
			}
				
		}		
				
	}

	private JTextField campo1, nick, ip;
	
	private JTextArea campochat; 
	
	private JButton miboton;
	
}

/** Esta clase encapsula las tres caracteristicas que enviamos al Servidor 
 * Todas las instancias de esta clase van a ser capaces de convertirse en bytes y enviarse por la red. */
class PaqueteEnvio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nick, ip, mensaje;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}



