/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linearRegression;

/**
 *
 * @author Firly
 */
public class MultipleLinearRegression {
   
    public static void main(String[] args){
        observasi [] inputVectors = file.read("Data/ruby.csv");
        //  memprediksi fitur price menggunakan model gradient descent. 
        double alpha = 0.0001;
        System.out.println("\n\nmodel yang digunakan gradient descent, alpha = " + alpha + " ...");
        System.out.println("*************************************************************************\n");
        Model gradientFit = Fit.gradientDescent(inputVectors, "price", 0.0001);
        System.out.println(gradientFit.toString());
        // memprediksi fitur price menggunakan model normal equation method. 
        System.out.println("\n\nmodel yang digunakan the normal equation method ...");
        System.out.println("*************************************************************************\n");
        Model normalFit = Fit.normalEquation(inputVectors, "price");
        System.out.println(normalFit.toString());
        // Mendemonstrasikan metode Model.prediksi(), serta model yang cocok, dengan memprediksi beberapa baris arbitrer dari dataset.
        System.out.println("\n\nmemprediksi beberapa model ...\n");
        double testValue = inputVectors[1000].getFeature("price");
        double predictionA = gradientFit.prediksi(inputVectors[1000]);
        double predictionB = normalFit.prediksi(inputVectors[1000]);
        System.out.println("Actual value: " + testValue + "\nprediksi linear regrssion menggunakan gradient descent: " + predictionA);
        System.out.println("prediksi linear regrssion menggunakan normal equation method: " + predictionB);
       
        // mengetahui nilai mse dan rmse 
        double mse1 = Math.pow( predictionA - testValue, 2);
        System.out.println("Nilai MSE prediksi menggunakan gradient descent: " + mse1);
        double rmse1 = Math.sqrt(mse1);
        System.out.println("Nilai RMSE prediksi menggunakan gradient descent : " + rmse1);
    
        double mse2 = Math.pow( predictionB - testValue, 2);
        System.out.println("Nilai MSE prediksi menggunakan normal equation method: " + mse2);
        double rmse2 = Math.sqrt(mse2);
        System.out.println("Nilai RMSE prediksi menggunakan normal equation method:: " + rmse2);
            
    }

}