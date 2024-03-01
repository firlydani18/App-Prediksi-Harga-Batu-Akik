/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linearRegression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Firly
 */
public class file {
    // Membaca file input dan output Observation ArrayList.
    public static observasi[] read(String filePath){
        File input = new File(filePath);
        BufferedReader reader = null;
        observasi[] obsArr = null;
        
        // membaca input file. 
        try {
            String text = null;

            // Menginstal BufferedReader, menentukan ukuran file dan mengatur array dipakai.
            reader = new BufferedReader(new FileReader(filePath));
            int size = -1;
            while ((text = reader.readLine()) != null) {
                size++; 
            }
            obsArr = new observasi[size];

            // Inisialisasi ulang BufferedReader dan membaca setiap baris menjadi objek Observasi dalam array.
            reader = new BufferedReader(new FileReader(filePath));
            String[] features = reader.readLine().split(";");
            int index = 0; 
            while ((text = reader.readLine()) != null) {
                String[] values = text.split(";");
                obsArr[index] = new observasi();
                for (int i = 0; i < features.length; i++){
                    obsArr[index].putFeature(features[i], Double.parseDouble(values[i]));
                }
                index++;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return obsArr;
    }
}
