package sztucznainteligencja;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.List;
 
public class NeuralInputCell
{
		private List<Double> Dendrites;
		private List<Double> Synapses;
		
		public NeuralInputCell()
		{
			Dendrites = new ArrayList<>(100);
			Synapses = new ArrayList<>(100);
		}
		
		
		public double finalizeData(double membranePotential)
		{
			return Dendrites.get(1);
		}
                
                public double pochodna(double wartosc){
                    //return pow(Math.E,-wartosc)/pow(1+pow(Math.E,-wartosc),2.0);
                    return finalizeData(getMembranePotential())*(1-finalizeData(getMembranePotential()));
                }
		
		public int getInputSize()
		{
			return Dendrites.size();
		}
		
		public void addInput()
		{
			Dendrites.add(0.0);
			Synapses.add(1.0);
		}
		
		public void addInput(int count)
		{
			for(int i = 1; i <= count; i++)
				this.addInput();
		}
		
		public double getInputData(int index)
		{
			return Dendrites.get(index);
		}
		
		public void setInputData(int index, double value)
		{
			Dendrites.set(index, value);
		}
		
		public double getInputWeight(int index)
		{
			return Synapses.get(index);
		}
		
		public void setInputWeight(int index, double weight)
		{
			Synapses.set(index, weight);
		}
		
		public double processCellNode(int index)
		{
			return (Dendrites.get(index)*Synapses.get(index));
		}
		
		public double getMembranePotential()
		{
			if(getInputSize() == 0)
				return -1;
			
			double sum = 0;
                        sum+=Synapses.get(0);
			for (int i = 1; i < getInputSize(); i++)
				sum+=processCellNode(i);
 
			return sum;
		}
		
		public double getOutput()
		{
			if(getInputSize() == 0)
				return -1;
				
			return finalizeData(getMembranePotential());
		}
}