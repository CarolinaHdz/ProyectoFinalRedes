/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package huffmanarchivos;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class HuffmanCode {
 
	 // Almacenar la tabla de codificación de Huffman
	static Map<Byte, String> map = new HashMap<>();
	 // Almacenar la variable intermedia codificada
	static StringBuilder sb = new StringBuilder();
 
	/**
	  * Convertir matriz de bytes a árbol Huffman
	 * 
	  * @param matriz de bytes de datos para convertir
	 * @return
	 */
	public Node createNodeTree(byte[] data) {
 
		Map<Byte, Integer> map = new HashMap<Byte, Integer>();
 
		 // cuenta el número de caracteres
		for (Byte b : data) {
			Integer count = map.get(b);
			if (count == null) {
				map.put(b, 1);
			} else {
				map.put(b, count + 1);
			}
		}
 
		 // Ordena los caracteres de la lista según la cantidad de veces
		List<Node> nodes = new ArrayList<Node>();
		Node node = null;
		for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
			node = new Node(entry.getValue(), entry.getKey());
			nodes.add(node);
		}
		 // Construye un árbol Huffman
		while (nodes.size() > 1) {
			Collections.sort(nodes);
			Node leftChild = nodes.get(0);
			Node rightChild = nodes.get(1);
 
			Node newNode = new Node((leftChild.weight + rightChild.weight), null);
			newNode.left = leftChild;
			newNode.right = rightChild;
			nodes.remove(leftChild);
			nodes.remove(rightChild);
			nodes.add(newNode);
 
		}
 
		return nodes.get(0);
 
	}
 
	/**
	  * Establecer la tabla de codificación de Huffman
	 * 
	  * @param nodo nodo
	  * @param código código 0 o 1
	  * @param cadena codificada sbBuilder
	 */
	public void setCodeTable(Node node, String code, StringBuilder sbBuilder) {
		StringBuilder stringBuilder = new StringBuilder(sbBuilder);
		stringBuilder.append(code);
		if (node != null) {
			if (node.data == null) {
				setCodeTable(node.left, "0", stringBuilder);
				setCodeTable(node.right, "1", stringBuilder);
			} else {
				map.put(node.data, stringBuilder.toString());
			}
		}
 
	}
 
	/**
	  * Obtenga la tabla de codificación de Huffman
	 * 
	 * @param node
	 * @return
	 */
	public Map<Byte, String> getCodeTable(Node node) {
		if (node == null) {
			return null;
		}
		setCodeTable(node.left, "0", sb);
		setCodeTable(node.right, "1", sb);
 
		return map;
 
	}
 
	/**
	  * Método de datos comprimidos
	 * 
	  * @param bytes matriz para ser comprimido
	 * @return
	 */
	public byte[] zip(byte[] bytes, Map<Byte, String> map) {
		StringBuilder sbuf = new StringBuilder();
		 // Convertir la matriz de bytes [] en una matriz de codificación
		for (byte b : bytes) {
			String string = map.get(b);
			sbuf.append(string);
		}
		 // convertido a la longitud del campo
                 int len =(sbuf.length() + 7) / 8; // Encuentra la longitud del caracter
 
		 byte [] huffmanbytes = new byte [len]; // utilizado para almacenar datos comprimidos
		for (int i = 0, index = 0; i < sbuf.length(); i += 8, index++) {
			String string = null;
			if (i + 8 > sbuf.length()) {
				string = sbuf.substring(i);
 
			} else {
				string = sbuf.substring(i, i + 8);
			}
			byte b = (byte) (Integer.parseInt(string, 2));
 
			huffmanbytes[index] = b;
 
		}
		return huffmanbytes;
 
	}
 
	/**
	  * Convertir datos de tipo byte en una cadena de datos binarios
	 * 
	  * @param b datos de bytes
	  * @param flag Necesita llenar el bit alto, el último bit no necesita llenar el bit alto, si es falso no necesita llenar el bit alto
	 * @return
	 */
	public String byteTobitString(byte b, boolean flag) {
		int temp = b;
		 // Número positivo lleno alto
		if (flag) {
                    temp |= 256; // bit a bit y 1 0000 0000 | 0000 0001 => 1 0000 0001
		}
		 String string = Integer.toBinaryString (temp); // El resultado es el complemento
 
		if (flag) {
			return string.substring(string.length() - 8);
 
		} else {
			return string;
		}
 
	}
 
	public String unzip(byte[] huffmanBytes) {
 
		return new String(unzip(map, huffmanBytes));
 
	}
 
	public byte[] unzip(Map<Byte, String> huffmanTable, byte[] huffmanBytes) {
		 // Loop para convertir huffmanBytes en cadena binaria
		StringBuilder sbs = new StringBuilder();
		for (int i = 0; i < huffmanBytes.length; i++) {
			boolean flag = (i == huffmanBytes.length - 1);
			String ss = byteTobitString(huffmanBytes[i], !flag);
			sbs.append(ss);
		}
 
		 // Convertir la tabla de codificación de Huffman
		Map<String, Byte> reverseMap = new HashMap<String, Byte>();
		for (Map.Entry<Byte, String> entry : huffmanTable.entrySet()) {
			reverseMap.put(entry.getValue(), entry.getKey());
		}
		List<Byte> list = new ArrayList<Byte>();
		 // Genera una cadena binaria basada en la tabla huffman invertida para generar el byte []
		int count;
		for (int i = 0; i < sbs.length();) {
			count = 1;
			boolean flag = true;
			while (flag) {
				String key = sbs.substring(i, i + count);
				Byte bs = reverseMap.get(key);
				if (bs == null) {
					count++;
				} else {
					list.add(bs);
					flag = false;
				}
			}
 
			i = i + count;
		}
		byte[] blist = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			blist[i] = list.get(i);
		}
 
		return blist;
 
	}
 
	/**
	  * Comprimir método de cadena
	 * 
	 * @param string
	 * @return
	 */
	public byte[] Hzip(byte[] string) {
 
		Node rootNode = createNodeTree(string);
		Map<Byte, String> map = getCodeTable(rootNode);
		byte[] bysr = zip(string, map);
		return bysr;
	}
 
	/**
	  	 * Archivo comprimido 
	 * 
	  * @param src ruta del archivo fuente
	  * @param dest ruta del archivo comprimido
	 */
	public void zipFile(String src, String dest) {
		 InputStream in = null; // Flujo de entrada de archivo
		 OutputStream os = null; // Flujo de salida del archivo
		 ObjectOutputStream oos = null; // Flujo de salida del objeto
 
		try {
			 // Secuencia de entrada de archivo para leer archivos
			in = new FileInputStream(src);
			 byte [] b = new byte [in.available ()]; // in.available () representa el tamaño de byte del archivo
			 in.read(b); // Lee los datos de la secuencia de entrada en la matriz b
 
			 byte [] bys = Hzip (b); // compresión
 
			 os = new FileOutputStream (dest); // Crear flujo de salida de archivo
 
			 oos = new ObjectOutputStream (os); // Crear flujo de salida de objeto
			 // Escribe los datos comprimidos en la secuencia del objeto
			oos.writeObject(bys);
			 // Salida de la tabla de codificación de Huffman a un archivo comprimido para una fácil descompresión posterior
			oos.writeObject(map);
 
		} catch (Exception e) {
 
			e.printStackTrace();
		} finally {
			try {
				in.close();
				os.close();
				oos.close();
			} catch (IOException e) {
 
				e.printStackTrace();
			}
		}
	}
 
	/**
	  * descomprimir archivos
	 * 
	  * @param src ruta de archivo comprimido
	  * @param dest La ruta de almacenamiento después de la descompresión
	 */
	public void unzipFile(String src, String dest) {
		 InputStream in = null; // Flujo de entrada de archivo
		 OutputStream os = null; // Flujo de salida del archivo
		 ObjectInputStream ois = null; // Flujo de salida del objeto
 
		try {
			 // La secuencia de entrada del archivo lee archivos comprimidos
			in = new FileInputStream(src);
			 // ajusta el flujo de entrada
			ois = new ObjectInputStream(in);
			 byte [] b = (byte []) ois.readObject (); // lee la matriz de archivos comprimidos
			Map <Byte, String> huffmanMap = (Map <Byte, String>) ois.readObject (); // Leer el código de Huffman
			System.out.println(huffmanMap.isEmpty());
			 byte [] res = unzip(huffmanMap, b); // Descomprimir el archivo
 
			 os = new FileOutputStream (dest); // Crear flujo de salida
 
			 os.write (res); // Escribe los datos descomprimidos
 
		} catch (Exception e) {
 
			e.printStackTrace();
		} finally {
			try {
				in.close();
				os.close();
				ois.close();
			} catch (IOException e) {
 
				e.printStackTrace();
			}
		}
	}
 
	 // pedido anticipado transversal
	public void preOrder(Node node) {
 
		if (node == null) {
			return;
		}
		System.out.println(node);
 
		preOrder(node.left);
		preOrder(node.right);
 
	}
}