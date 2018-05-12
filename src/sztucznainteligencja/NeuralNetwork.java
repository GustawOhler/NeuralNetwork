package sztucznainteligencja;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import java.util.Random;

public class NeuralNetwork {

    private NeuralInputCell[] neuronyWejsc;
    private NeuralCell[] neuronyUkr;
    private NeuralCell[] neuronyWyjsc;
    private double wspol_q = 0.1;
    private double momentum = 0.85;
    private int neurWejsc;
    private int neurUkr;
    private int neurWyjsc;
    double[][] poprzbledyukr;
    double[][] poprzbledywyj;

    public NeuralNetwork(int wejscie, int ukryte, int wyjscie) {
        neurWejsc = wejscie;
        neurUkr = ukryte;
        neurWyjsc = wyjscie;
        neuronyWejsc = new NeuralInputCell[neurWejsc];
        neuronyUkr = new NeuralCell[neurUkr];
        neuronyWyjsc = new NeuralCell[neurWyjsc];
        poprzbledyukr = new double[neurUkr][neurWejsc + 1];
        poprzbledywyj = new double[neurWyjsc][neurUkr + 1];
        Random generator = new Random();
        for (int i = 0; i < neurWejsc; i++) {
            neuronyWejsc[i] = new NeuralInputCell();
            neuronyWejsc[i].addInput(2);
            neuronyWejsc[i].setInputWeight(0, generator.nextDouble());
            neuronyWejsc[i].setInputWeight(1, generator.nextDouble());
            //neuronyWejsc[i].setInputData(0, 1);
        }
        for (int i = 0; i < neurUkr; i++) {
            neuronyUkr[i] = new NeuralCell();
            neuronyUkr[i].addInput(neurWejsc + 1);
            for (int j = 0; j < neurWejsc + 1; j++) {
                neuronyUkr[i].setInputWeight(j, generator.nextDouble());
            }
            neuronyUkr[i].setInputData(0, 1);
            //neuronyUkr[i].setInputData(0, 1);
        }
        for (int i = 0; i < neurWyjsc; i++) {
            neuronyWyjsc[i] = new NeuralCell();
            neuronyWyjsc[i].addInput(neurUkr + 1);
            for (int j = 0; j < neurUkr + 1; j++) {
                neuronyWyjsc[i].setInputWeight(j, generator.nextDouble());
            }
            neuronyWyjsc[i].setInputData(0, 1);
            //neuronyWyjsc[i].setInputData(0, 1);
        }
    }

    public double uczSie(double[] tabwej, double[] tabwyj) {
        for (int i = 0; i < neurWejsc; i++) {
            neuronyWejsc[i].setInputData(1, tabwej[i]);
            //neuronyWejsc[i].setInputData(0, 1);
        }
        for (int i = 0; i < neurUkr; i++) {
            for (int j = 1; j < neurWejsc + 1; j++) {
                neuronyUkr[i].setInputData(j, neuronyWejsc[j - 1].getOutput());
            }
        }
        for (int i = 0; i < neurWyjsc; i++) {
            for (int j = 1; j < neurUkr + 1; j++) {
                neuronyWyjsc[i].setInputData(j, neuronyUkr[j - 1].getOutput());
            }

        }
        double bladpocz = 0.0;
        for (int i = 0; i < neurWyjsc; i++) {
            bladpocz += 0.5 * pow(tabwyj[i] - neuronyWyjsc[i].getOutput(), 2.0);
        }
        double[][] bledyukr = new double[neurUkr][neurWejsc + 1];
        double[][] bledywyj = new double[neurWyjsc][neurUkr + 1];
        double[] wspolukr = new double[neurUkr];
        double[] wspolwyj = new double[neurWyjsc];
        for (int i = 0; i < neurWyjsc; i++) {
            wspolwyj[i] = (tabwyj[i] - neuronyWyjsc[i].getOutput()) * neuronyWyjsc[i].pochodna(neuronyWyjsc[i].getMembranePotential());
            for (int j = 0; j < neurUkr + 1; j++) {
                bledywyj[i][j] = wspol_q * wspolwyj[i] * neuronyWyjsc[i].getInputData(j) +momentum * poprzbledywyj[i][j];
            }
        }
        poprzbledywyj = bledywyj;
        for (int i = 0; i < neurUkr; i++) {
            for (int j = 0; j < neurWyjsc; j++) {
                wspolukr[i] += neuronyUkr[i].pochodna(neuronyUkr[i].getMembranePotential()) * wspolwyj[j] * neuronyWyjsc[j].getInputWeight(1 + i);
            }
            for (int j = 0; j < neurWejsc + 1; j++) {
                bledyukr[i][j] = wspol_q * wspolukr[i] * neuronyUkr[i].getInputData(j) + momentum *poprzbledyukr[i][j];
            }
        }
        poprzbledyukr = bledyukr;
        for (int i = 0; i < neurUkr; i++) {
            for (int j = 0; j < neurWejsc + 1; j++) {
                neuronyUkr[i].setInputWeight(j, neuronyUkr[i].getInputWeight(j) + bledyukr[i][j]);
            }
        }
        for (int i = 0; i < neurWyjsc; i++) {
            for (int j = 0; j < neurUkr + 1; j++) {
                neuronyWyjsc[i].setInputWeight(j, neuronyWyjsc[i].getInputWeight(j) + bledywyj[i][j]);
            }
        }
        return bladpocz;
    }

    public int dajWynik(double[] tablica) {
        for (int i = 0; i < neurWejsc; i++) {
            neuronyWejsc[i].setInputData(1, tablica[i]);
        }
        for (int i = 0; i < neurUkr; i++) {
            for (int j = 1; j < neurWejsc + 1; j++) {
                neuronyUkr[i].setInputData(j, neuronyWejsc[j - 1].getOutput());
            }
        }
        for (int i = 0; i < neurWyjsc; i++) {
            for (int j = 1; j < neurUkr + 1; j++) {
                neuronyWyjsc[i].setInputData(j, neuronyUkr[j - 1].getOutput());
            }
        }
        double suma = 0;
        for (int i = 0; i < neurWyjsc; i++) {
            if (Double.compare(neuronyWyjsc[i].getOutput(),0.50) > 0)
                suma += i+1;
        }
        //System.out.println((int)suma);
        return ((int)suma);
    }

    public void zapiszSiec() {
        try (PrintWriter out = new PrintWriter("zapis_sieci.txt")) {
            for (int i = 0; i < neurUkr; i++) {
                for (int j = 0; j < neuronyUkr[i].getInputSize(); j++) {
                    out.print(Double.toString(neuronyUkr[i].getInputWeight(j)) + ",");
                }
            }
            out.print("\n");
            for (int i = 0; i < neurWyjsc; i++) {
                for (int j = 0; j < neuronyWyjsc[i].getInputSize(); j++) {
                    out.print(Double.toString(neuronyWyjsc[i].getInputWeight(j)) + ",");
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void wczytajSiec() {
        try (BufferedReader br = new BufferedReader(new FileReader("zapis_sieci.txt"))) {
            String line = br.readLine();
            String[] lineVector = line.split(",");
            for (int i = 0; i < neurUkr; i++) {
                for (int j = 0; j < neuronyUkr[i].getInputSize(); j++) {
                    neuronyUkr[i].setInputWeight(j, Double.parseDouble(lineVector[i * neuronyUkr[i].getInputSize() + j]));
                }
            }
            line = br.readLine();
            lineVector = line.split(",");
            for (int i = 0; i < neurWyjsc; i++) {
                for (int j = 0; j < neuronyWyjsc[i].getInputSize(); j++) {
                    neuronyWyjsc[i].setInputWeight(j, Double.parseDouble(lineVector[i * neuronyWyjsc[i].getInputSize() + j]));
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
