import net.bigpoint.assessment.gasstation.BigpointGasStation;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;


public class BuyGasThread implements Runnable {

	GasType type;
	double amountInLiters;
	double maxPricePerLiter;
	BigpointGasStation gasStation;
	
	public BuyGasThread(GasType type, double amountInLiters, double maxPricePerLiter, BigpointGasStation gasStation)
	{
		this.type = type;
		this.amountInLiters = amountInLiters;
		this.maxPricePerLiter = maxPricePerLiter;
		this.gasStation = gasStation;
	}
	
	@Override
	public void run() 
	{
		try
		{
			gasStation.buyGas(type, amountInLiters, maxPricePerLiter);	
		}
		catch(GasTooExpensiveException e)
		{
			System.out.println("\nThe gas rate is higher than your suggested rate\n");
		}
		catch(NotEnoughGasException e)
		{
			System.out.println("\nAvailable gas is less than your required gas\n");
		}
		
		System.out.printf("\nCancellations due to NoGas: %d", gasStation.getNumberOfCancellationsNoGas());
		System.out.printf("\nCancellations due to Expensive: %d", gasStation.getNumberOfCancellationsTooExpensive());
		System.out.printf("\nTotal sales: %d", gasStation.getNumberOfSales());
		System.out.printf("\nRevenue Generated: %f", gasStation.getRevenue());
		System.out.println("\n---------------------------------------------------------\n");
	}
	
	
	

}
