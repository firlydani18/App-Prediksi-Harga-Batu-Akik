/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linearRegression;

import java.util.LinkedHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
/**
 *
 * @author Firly
 */
public class Fit {
   // Menyesuaikan model menggunakan penurunan gradien desert dengan kecepatan pembelajaran yang ditentukan oleh parameter Alfa.
   // Fungsi biaya convex sehingga minimum global akan selalu tercapai.
    public static Model gradientDescent(observasi[] inputVectors, String dependent, double alpha){
        // Menstandarisasi data input agar memiliki mean 0 dan standar deviasi 1. 
        Standardisation stanData = standardise(inputVectors);

        // Mengonversi larik objek Observasi ke matriks (array 2-d) dengan kolom pertama tambahan 1 untuk intersep. 
        double[][] train = new double[inputVectors[0].size() + 1][inputVectors.length];
        for (int i = 0; i < inputVectors.length; i++){
            train[0][i] = 1; // Intercept (y Konstanta)
            train[train.length - 1][i] = inputVectors[i].getFeature(dependent);
            int j = 1;
            for (String feature : inputVectors[i].getFeatures()){
                if (!feature.equals(dependent)){
                    train[j][i] = inputVectors[i].getFeature(feature);
                    j++;
                }
            }
        }
        // Satu parameter theta per variabel independen dan satu untuk intersep. 
        double[] thetas = new double[inputVectors[0].size()];
        double[] temps = new double[thetas.length]; // Parallel array for temp values. 
        double delta; // Perubahan Absolute dalam parameter; digunakan untuk mengukur konvergensi       
        do{ 
            delta = 0;
            // Untuk setiap parameter theta:
            for (int i = 0; i < thetas.length; i++){
                // Memperbarui nilai temp untuk parameter.
                temps[i] = thetas[i] - (alpha * ((double) 1/train.length)*evaluasiCost(thetas, train, i));
                // Menghitung perubahan mutlak pada parameter. 
                delta += Math.abs(thetas[i] - temps[i]);
            }
            // Memperbarui setiap theta ke nilai tempsnya.
            for (int i = 0; i < thetas.length; i++)
                thetas[i] = temps[i];
        }while(delta > 1E-7); // Ambang batas di mana kita menyimpulkan bahwa konvergensi yang telah terjadi.
        // Standarisasi data dan parameter yang dipasang.
        deStandardise(stanData, inputVectors, thetas); 
        
        // Membuat hashmap dari nama setiap fitur dan parameter terkaitnya. 
        LinkedHashMap<String, Double> parameters = new LinkedHashMap<String, Double>();
        parameters.put("Intercept", thetas[0]);
        int j = 1;
        for (String feature : inputVectors[1].getFeatures()){
            if (!feature.equals(dependent)){ // Menghiraukan nilai dependent variable 
                parameters.put(feature, thetas[j]);
                j++;
            }
        }
        // Membangun objek Model. 
        Model outputModel = new Model(parameters, dependent, 0);
        // Menghitung r-kuadrat dan menetapkannya pada model. 
        outputModel.rSquared = hitungRSquared(inputVectors, outputModel);
        return outputModel;
    }

    // Mengevaluasi fungsi biaya (squared error) untuk tingkat parameter tertentu.
     // Digunakan dalam langkah pembaruan gradient descent.
    private static double evaluasiCost(double[] thetas, double[][] data, int featureIndex){
        double result = 0;
        
        // Untuk setiap baris dalam dataset:
        for (int i = 0; i < data[0].length; i++){
            double error = 0;
            
            // Menghitung nilai yang diprediksi oleh parameter.
            for (int j = 0; j < data.length - 1; j++)
                error += data[j][i]*thetas[j];
            
            // Kurangi nilai sebenarnya untuk mendapatkan kesalahan dan skala dengan nilai di baris itu untuk fitur yang sedang diperbarui. 
            error -= data[data.length - 1][i];
            error *= data[featureIndex][i];
            
            // Menambahkan hasil ke jumlah. 
            result += error;
        }
        
        return result; 
    }
    
    // Menyesuaikan model dalam bentuk tertutup menggunakan normal equation method.
    public static Model normalEquation(observasi[] inputVectors, String dependent){
        // Membangun matriks desain fitur dari array objek Observasi, serta transposnya.
        double[][] design = new double[inputVectors.length][inputVectors[0].size()]; 
        double[][] designT = new double[inputVectors[0].size()][inputVectors.length]; // Transpose
        for (int i = 0; i < inputVectors.length; i++){
            design[i][0] = 1; // Intercept
            designT[0][i] = 1; 
            int j = 1;
            for (String feature : inputVectors[i].getFeatures()){
                if (!feature.equals(dependent)){
                    design[i][j] = inputVectors[i].getFeature(feature);
                    designT[j][i] = inputVectors[i].getFeature(feature);
                    j++;
                }
            }
        }
        RealMatrix X = new Array2DRowRealMatrix(design);
        RealMatrix XPrime = new Array2DRowRealMatrix(designT);

        // Membangun vektor nilai y. 
        double[] yArray = new double[inputVectors.length];
        for (int i = 0; i < inputVectors.length; i++)
            yArray[i] = inputVectors[i].getFeature(dependent); 
        RealMatrix y = new Array2DRowRealMatrix(yArray);
        // Memecahkan vektor parameter: theta = (X'X)-1 X'y
        RealMatrix theta = new LUDecomposition(XPrime.multiply(X)).getSolver().getInverse().multiply(XPrime).multiply(y);
        // Membuat hashmap dari nama setiap fitur dan parameter pas terkaitnya.. 
        LinkedHashMap<String, Double> parameters = new LinkedHashMap<String, Double>();
        double[] thetas = theta.getColumn(0);
        parameters.put("Intercept", thetas[0]);
        int j = 1;
        for (String feature : inputVectors[1].getFeatures()){
            if (!feature.equals(dependent)){ // menghiraukan the dependent variable 
                parameters.put(feature, thetas[j]);
                j++;
            }
        }
        // Membangun objek Model. 
        Model outputModel = new Model(parameters, dependent, 0);
        // Menghitung rSquared dan menetapkannya pada model. 
        outputModel.rSquared = hitungRSquared(inputVectors, outputModel);
        return outputModel;
    }

    // Berisi array objek standardised observation, array dari fitur, dan array deviasi standar fitur.
     // Menyimpan nilai yang digunakan untuk menstandardisasi data sehingga data dan parameter yang dipasang dapat didestandardisasi.
    private static class Standardisation {
        public observasi[] observations;
        public double[] xbars;
        public double[] sigmas;

        public Standardisation(observasi[] observations, double[] xbars, double[] sigmas){
            this.observations = observations;
            this.xbars = xbars;
            this.sigmas = sigmas;
        }
    }

    // Menstandarisasi data sedemikian rupa sehingga setiap fitur memiliki mean 0 dan standar deviasi 1.
     // Mempercepat konvergensi gradient descent.
    private static Standardisation standardise(observasi[] inputVectors){
        //  Array yang berisi mean dan standar deviasi dari setiap fitur
        double[] xbars = new double[inputVectors[0].size()];
        double[] sigmas = new double[inputVectors[0].size()];

        // Iterasi pada data input dan menambahkan nilai setiap fitur ke mean. 
        for (int i = 0; i < inputVectors.length; i++){
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()){
                xbars[j] += inputVectors[i].getFeature(feature);
                j++;
            }
        }
        
        //  Membagi setiap nilai dengan jumlah pengamatan untuk menghasilkan mean
        for (int i = 0; i < xbars.length; i++)
            xbars[i] = xbars[i] / inputVectors.length;

        // Mengulangi data masukan dan menambahkan selisih kuadrat antara nilai tersebut dan rata-rata ke standard deviation 
        for (int i = 0; i < inputVectors.length; i++){
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()){
                sigmas[j] += Math.pow(inputVectors[i].getFeature(feature) - xbars[j], 2);
                j++;
            }
        }


       // Akar kuadrat dan bagi setiap nilai dengan jumlah pengamatan untuk menghasilkan standard deviation.
        for (int i = 0; i < sigmas.length; i++)
            sigmas[i] = Math.sqrt(sigmas[i] / inputVectors.length);

       // Iterasi pada data input dan standarisasi setiap nilai berdasarkan rata-rata fitur dan standar deviasi.
        for (int i = 0; i < inputVectors.length; i++){
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()){
                inputVectors[i].putFeature(feature, (inputVectors[i].getFeature(feature) - xbars[j]) / sigmas[j]);
                j++;
            }
        }

        Standardisation output = new Standardisation(inputVectors, xbars, sigmas);

        return output;
    }
    
    // Mengambil objek Standardisasi dan mendestandardisasi data dan parameter yang dipasang berdasarkan sarana dan deviasi standar.
    private static void deStandardise(Standardisation standard, observasi[] inputVectors, double[] thetas){
        // Destandardisasi intersep dan parameter.
        for (int i = 1; i < thetas.length; i++){
            thetas[0] -= thetas[i] * (standard.xbars[i-1] / standard.sigmas[i-1]);
            thetas[i] = (thetas[i] * standard.sigmas[standard.sigmas.length - 1]) / standard.sigmas[i-1];
        }
        thetas[0] *= standard.sigmas[standard.sigmas.length - 1];
        thetas[0] += standard.xbars[standard.xbars.length - 1];

        // Destandardisasi data input.
        for (int i = 0; i < inputVectors.length; i++){
            int j = 0;
            for (String feature : inputVectors[i].getFeatures()){
                inputVectors[i].putFeature(feature, ((inputVectors[i].getFeature(feature) * standard.sigmas[j]) + standard.xbars[j]));
                j++;
            }
        }
    }

    // Menghitung R-squared dari model yang dipasang berdasarkan model dan input yang digunakan untuk menyesuaikannya.
    private static double hitungRSquared(observasi[] inputVectors, Model model){
        // Menghitung nilai rata-rata y.
        double ybar = 0;
        for (int i = 0; i < inputVectors.length; i++){
            ybar += inputVectors[i].getFeature(model.dependent);
        }
        ybar /= inputVectors.length;
        
        // Menghitung sisa (residua) dan jumlah total kuadrat (total sum of squares)untuk model
        double rss = 0;
        double tss = 0;
        for (int i = 0; i < inputVectors.length; i++){
            rss += Math.pow((inputVectors[i].getFeature(model.dependent) - model.prediksi(inputVectors[i])), 2);
            tss += Math.pow((inputVectors[i].getFeature(model.dependent) - ybar), 2);
        }
        
       // Mengembalikan nilai R-squared.
        return (1 - rss/tss);
    }
}
