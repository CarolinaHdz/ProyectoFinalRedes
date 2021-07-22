package huffmanarchivos;

import huffmanarchivos.HuffmanCode;
import java.util.Arrays;


/**
 *
 * @author Caro
 */
public class HuffmanArchivos {

    /**
     * @param args the command line arguments
     */
 
	public static void main(String[] args) {
		String string = "I like you just like you!";
 
		HuffmanCode huffmanCode = new HuffmanCode();
		byte [] bytes = huffmanCode.Hzip (string.getBytes ()); // compresión del archivo
 
		 String string2 = huffmanCode.unzip (bytes); // Descomprimir
		 System.out.println ("Aspecto comprimido:" + Arrays.toString (bytes));
		 System.out.println("Inicia el proceso de compresión");
		 huffmanCode.zipFile("D://inserts.txt", "D://fondo1.zip");
		 System.out.println ("Su archivo ha sido comprimido");
//		System.out.println("============================================");
//		 System.out.println ("Descomprimir");
//		huffmanCode.unzipFile("D://fondo12.zip", "D://fondoDescomprimido.jpg");
//		 System.out.println ("Descomprimir con éxito");
 
	}
}
