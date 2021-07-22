/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package huffmanarchivos;

/**
 *
 * @author Caro
 */
public class Node implements Comparable<Node> {
	int weight ; // El peso se usa para almacenar el número de ocurrencias de caracteres en la codificación Huffman
	Byte data; // Se usa para almacenar los caracteres que aparecen
	Node left;
	Node right;
 
	public Node() {
		super();
	}
 
	public Node(Integer weight, Byte data) {
		super();
		this.weight = weight;
		this.data = data;
	}
 
	@Override
	public String toString() {
		return "Node [weight=" + weight + ", data=" + data + "]";
	}
 
/**
   * Se usa para ordenar nodos
 */
	@Override
	public int compareTo(Node node) {
		// TODO Auto-generated method stub
		return this.weight - node.weight;
	}
 
}
