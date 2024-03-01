/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linearRegression;

import java.util.LinkedHashMap;

/**
 *
 * @author Firly
 */
public class Model {
    public LinkedHashMap<String, Double> parameters;
    public String dependent;
    public double rSquared;

    public Model(LinkedHashMap<String, Double> parameters, String dependent, double rSquared){ 
        this.parameters = parameters;
        this.dependent = dependent;
        this.rSquared = rSquared;
    }
    // Mengambil Observasi yang cocok dengan data input dan menghitung nilai untuk variabel dependen. 
    public double prediksi(observasi input){
        double yhat = parameters.get("Intercept");
        for (String feature: parameters.keySet())
            if (!feature.equals("Intercept")) { yhat += parameters.get(feature) * input.getFeature(feature); } 
        return yhat;
    }
    public String toString(){
        String output = "Multiple linear regression predicting " + dependent + " using " + (parameters.size() - 1) + " features.\n"
            + "R-Squared: " + rSquared + "\n\nFeature\t\t\t\tParameter\n-------------------------------------------------------------\n";

        for (String feature : parameters.keySet()){
            String formattedName = feature;
            while (formattedName.length() < 16) { formattedName = formattedName + " "; }
            output = output + formattedName + "\t\t" + parameters.get(feature) + "\n";
        }
        return output;
    }

}
