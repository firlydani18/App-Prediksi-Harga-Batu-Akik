/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linearRegression;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author Firly
 */
public class observasi {
     private LinkedHashMap<String, Double> features = new LinkedHashMap<String, Double>();
    
    // mengakses dataset menggunakan features(fungsi).
    public void putFeature(String feature, double value){ features.put(feature, value); } 
    public double getFeature(String feature) { return features.get(feature); }
    public int size(){ return features.size(); }
    public Set<String> getFeatures() { return features.keySet(); } 
    
    public String toString(){
        String output = "";
        for (String feature : getFeatures()){ output = output + feature + ": " + getFeature(feature) + "\n"; }
        return output;
    }
}
