package sztucznainteligencja;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SztucznaInteligencja {

    public static void main(String[] args) {
        NeuralNetwork siec = new NeuralNetwork(85, 12, 9);
        Scanner odczyt = new Scanner(System.in);
        String[] lineVector;
        //String komenda = odczyt.next();
        String komenda = "wcz";
        if (komenda.equals("ucz")) {
            List<String> lista = new ArrayList();
            try (BufferedReader br = new BufferedReader(new FileReader("poker-hand-training-true.data"))) {
                String line = br.readLine();
                while (line != null) {
                    lista.add(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            double[][] ogltabwej = new double[lista.size()][85];
            double[][] ogltabwyj = new double[lista.size()][9];
            for (int a = 0; a < lista.size(); a++) {
                lineVector = lista.get(a).split(",");
                for (int i = 0; i < 5; i++) {
                    ogltabwej[a][i * 17 + Integer.parseInt(lineVector[i * 2]) - 1] = 1.0;
                    ogltabwej[a][i * 17 + 4 + Integer.parseInt(lineVector[i * 2 + 1]) - 1] = 1.0;
                }
                /*for (int i = 0; i < 9; i++) {
                ogltabwyj[a][i] = 0.0;
            }*/
                if (Integer.parseInt(lineVector[10]) != 0) {
                    ogltabwyj[a][Integer.parseInt(lineVector[10]) - 1] = 1.0;
                }
            }
            double poprzedni_blad = 0.0;
            double blad_calk = 0.0;
            double blad_5_wstecz = 1.0;
            int ostatnia = 0;
            int j = 0;
            /*try (PrintWriter out = new PrintWriter("blad.txt")) {
                out.println("Sredni blad kwadratowy dla " + lista.size() + " danych");*/
            //&& (Double.compare(abs(blad_5_wstecz - blad_calk), 0.0000001) > 0 || j < 2)
            while (j < 5000 && (Double.compare(blad_calk, 0.02) > 0 || j < 2)) {
                if (j % 6 == 1) {
                    poprzedni_blad = blad_calk;
                    ostatnia = j;
                }
                if (j - ostatnia == 5) {
                    blad_5_wstecz = poprzedni_blad;
                }
                blad_calk = 0.0;
                for (int g = 0; g < lista.size(); g++) {
                    double[] tabwej = ogltabwej[g];
                    double[] tabwyj = ogltabwyj[g];
                    blad_calk += siec.uczSie(tabwej, tabwyj);
                }
                blad_calk /= (double) lista.size();
                /*int dawka = 20;
                for (int g = 0; g < lista.size();) {
                    for (int i = g; i < g + dawka && i < lista.size(); i++) {
                        double[] tabwej = ogltabwej[i];
                        double[] tabwyj = ogltabwyj[i];
                        /*for (int h = 2; h < 9; h++) {
                            if (Double.compare(tabwyj[h], 1.0) == 0) {
                                siec.uczSie(tabwej, tabwyj);
                                break;
                            }

                        }*/
 /*blad_calk += siec.uczSie(tabwej, tabwyj);
                    }
                    for (int i = g + dawka - 1; i >= g; i--) {
                        if (i >= lista.size()) {
                            i = lista.size() - 1;
                        }
                        double[] tabwej = ogltabwej[i];
                        double[] tabwyj = ogltabwyj[i];
                        /*for (int h = 2; h < 9; h++) {
                            if (Double.compare(tabwyj[h], 1.0) == 0) {
                                siec.uczSie(tabwej, tabwyj);
                                break;
                            }

                        }*/
 /*siec.uczSie(tabwej, tabwyj);
                    }
                    if (lista.size() - 1 - g > dawka) {
                        g += dawka;
                    } else if (lista.size() - 1 - g != 0){
                        g += lista.size() - 1 - g;
                    }
                    else
                        g +=1;
                }
                blad_calk /= (double) lista.size();*/
                j++;
                if (j % 5 == 0) {
                    System.out.println("blad" + j + ": \t" + blad_calk);
                }
            }
            /*catch (IOException e){
                System.out.println(e);
                System.exit(1);
            }*/
            siec.zapiszSiec();
            System.out.println("juz:");
        } else if (komenda.equals("wcz")) {
            siec.wczytajSiec();
        }

        while (true) {
            double[] tabwej1 = new double[85];
            lineVector = odczyt.next().split(",");
            for (int i = 0; i < 5; i++) {
                tabwej1[i * 17 + Integer.parseInt(lineVector[i * 2]) - 1] = 1.0;
                tabwej1[i * 17 + 4 + Integer.parseInt(lineVector[i * 2 + 1]) - 1] = 1.0;
            }
            System.out.println(siec.dajWynik(tabwej1));
        }
        /*try (BufferedReader br = new BufferedReader(new FileReader("poker-hand-training-true.data"))) {
            String line2 = br.readLine();
            String[] lineVector2;
            int licznik = 0;
            int mianownik = 0;
            while (line2 != null) {
                lineVector2 = line2.split(",");
                double[] tabwej1 = new double[85];
                for (int i = 0; i < 5; i++) {
                    tabwej1[i * 17 + Integer.parseInt(lineVector2[i * 2]) - 1] = 1.0;
                    tabwej1[i * 17 + 4 + Integer.parseInt(lineVector2[i * 2 + 1]) - 1] = 1.0;
                }
                if (Integer.parseInt(lineVector2[10]) == siec.dajWynik(tabwej1))
                    licznik++;
                mianownik++;
                line2 = br.readLine();
            }
            System.out.println((double)licznik/(double)mianownik);
        } catch (IOException e) {
            System.out.println(e);
        }*/
    }
}
