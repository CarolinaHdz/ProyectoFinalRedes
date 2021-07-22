/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package separator_file2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Beatriz Leiva
 */
public class Separator_File2 {

   public static  void splitAndZipFile(File f) throws IOException {
        int partCounter=0;
        int counter=1;
        int sizeOfFiles = (1024 * 1024)*100;// 100MB
       //int sizeOfFiles = 10;
       byte[] buffer = new byte[sizeOfFiles];

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {//try-with-resources para asegurar que se cierran los buffers.

            String name = f.getName();
                int tmp;
                System.out.println(bis.read(buffer));
                while ((tmp = bis.read(buffer)) > 0) {
                    //Nombramos cada parte con un identificador al final, para que no se sobreescriba
                    System.out.println("entro");
                    File newFile = new File(f.getParent(), name+ "." + String.format("%03d", partCounter++));
                    //Grabamos el fichero temporal(que sera borrado posteriormente)
                    try (FileOutputStream out = new FileOutputStream(newFile)) {
                        System.out.println(tmp);
                        out.write(buffer, 0, tmp);//tmp is chunk size
                       
                    }

                    //Leemos el nuevo fichero
                   FileInputStream in = new FileInputStream(newFile);
//                    //Creamos el zip con el fichero
//                    System.out.println("crear zip");
//                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream("F:\\Universidad\\Redes I\\Tareas\\Tarea 2\\Separator_File\\"+counter+".zip"));

                    //Creamos la entrada dentro del zip
//                    out.putNextEntry(new ZipEntry("parte"+counter+".txt")); 

                    //Agregamos el contenido del fichero al zip
//                    byte[] b = new byte[1024];
//                    int count;
//
//                    while ((count = in.read(b)) > 0) {
//                        out.write(b, 0, count);
//                    }
//                    counter++;
//                    in.close();
//                    out.close();
//
//                    if(newFile.exists()){
//                       newFile.delete();
//                    }
            }
        }

        System.out.println("Termine!");
    }



public static void main (String[] args){

         try {
            splitAndZipFile(new File("F:\\Universidad\\Redes I\\Tareas\\Tarea 2\\Separator_File2\\Grossman_Algebra_Lineal_6ta_edicion.pdf"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

     }
}
  