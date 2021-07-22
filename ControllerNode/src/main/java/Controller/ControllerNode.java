package Controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ControllerNode extends Thread{
    @Override
    public void run() {
        //Puerto por el cuál se va a enviar el mensaje
        final int PUERTO = 5000;
        //recibe los bytes del archivo
        byte[] buffer = new byte[500000];
        try {
            String userName = "";
            System.out.println("Iniciado el servidor UDP");
            //Creacion del socket
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);
            //Siempre atendera peticiones
            while (true) {
                //Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                //Recibo el datagrama
                socketUDP.receive(peticion);
                System.out.println("Recibo la informacion del cliente");

                //Convierto lo recibido y mostrar el mensaje
                buffer = peticion.getData();
                System.out.println("Recibido el archivo");
                
                //Se pide el nombre y el formato del archivo
                String ruta = "";
                String imageName = JOptionPane.showInputDialog("Ingrese el nombre del archivo");
                ruta += imageName+"‪.";
                String formato = JOptionPane.showInputDialog("Ingrese el formato del archivo");
                ruta += formato;
                //Convierto los bytes en un archivo
                convertBytesInFile(buffer,ruta);
                //Obtengo el puerto y la direccion de origen
                //Sino se quiere responder, no es necesario
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();

                String mensaje = "Imagen recibida!";
                buffer = mensaje.getBytes();

                //creo el datagrama
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, puertoCliente);

                //Envio la información
                System.out.println("Envío la confirmación al cliente");
                socketUDP.send(respuesta);

            }
        } catch (SocketException ex) {
            Logger.getLogger(ControllerNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControllerNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Este método convierte el array de bytes recibido en un archivo con el nombre y ruta especificados
     * @param array es el array con los bytes del archivo enviado
     * @param ruta el nombre del archivo con el formato
    **/
    public void convertBytesInFile(byte[] array, String ruta) throws FileNotFoundException, IOException{
        //se crea un fileOuputStream y se le pasa el nombre del arcivo con el frormato
        //Por defecto se carga en la carpeta que contiene el proyexto
        FileOutputStream fileOuputStream = new FileOutputStream(ruta);
        //se escriben los bytes del archivo recibido en el fileOuputStream
	fileOuputStream.write(array);
        //se cierra el fileOuputStream
	fileOuputStream.close();
    }
}
