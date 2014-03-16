package net.bigpoint.assessment.gasstation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

public class BigpointGasStation implements GasStation{

	/**
	 * 		List containing all gas pump objects at this station
	 */
	List<GasPump> gasPumps;
	
	/**
	 * 		Total amount of money generated from all sales
	 * 		
	 */
	double revenue;
	/**
	 * 		Number of successful sales from this pump,
	 * 		sales which the customer paid for
	 */
	int successfulSales;
	/**
	 * 		Number of unsuccessful sales due to lack
	 * 		of gas in pump.
	 */
	int canceledSales_noGas;
	/**
	 * 		Number of unsuccessful sales caused whenever
	 * 		customer bids lower than the fixed price of gas
	 */
	int canceledSales_expensive;
	
	/**
	 * 		Semaphore is used for synchronizing threads. 1 is passed as argument, it is 
	 * 		to ensure that only one thread at a time is calling the pumpGas(double) 
	 * 		method as per requirement. Semaphore releases a thread just after committing
	 * 		all changes by calling semaphore.release().
	 */
	private final Semaphore mySemaphore = new Semaphore(1);
	
	
	/**
	 * 		Constructor initializing List and variables
	 */
	public BigpointGasStation()
	{
		super();
		revenue = 0;
		successfulSales = 0;
		canceledSales_expensive = 0;
		canceledSales_noGas = 0;
		gasPumps = new LinkedList<GasPump>();
	}
	
	@Override
	public void addGasPump(GasPump pump) 
	{
		this.gasPumps.add(pump);
	}

	@Override
	public Collection<GasPump> getGasPumps() 
	{
		return gasPumps;
	}

	@Override
	public double buyGas(GasType type, double amountInLiters, double maxPricePerLiter) throws NotEnoughGasException,
			GasTooExpensiveException
	{
		int lowGasCheck = 0, expensiveGasCheck = 0, expensiveGas = 0, lowGas = 0, index=0;
		boolean check=false;
		
		//	This acquires one thread at a time
		try {
			mySemaphore.acquire();
		} catch (InterruptedException e) {}
		
		for(int i=0; i<gasPumps.size(); i++)
		{
			GasPump pump = gasPumps.get(i);
			if(type == pump.getGasType())	//also checks if any other thread is using this pump
			{			
				check = true;
				lowGasCheck++;
				if(pump.getRemainingAmount() >= amountInLiters)
				{
					expensiveGasCheck++;
					if(getPrice(type) <= maxPricePerLiter)
					{
						index=i;
						break;
					}
					else
						expensiveGas++;
				}
				else
					lowGas++;
					
			}				
		}
		
		if(check)
			if(lowGas == lowGasCheck)
			{
				canceledSales_noGas++;
				throw new NotEnoughGasException();
			}
			if(expensiveGas == expensiveGasCheck)
			{
				canceledSales_expensive++;
				throw new GasTooExpensiveException();
			}
			
		
		gasPumps.get(index).pumpGas(amountInLiters);
		double payableAmount = amountInLiters * maxPricePerLiter;
		revenue += payableAmount;
		successfulSales++;
		
		mySemaphore.release();
		
		return payableAmount;
	}

	@Override
	public double getRevenue() 
	{
		return revenue;
	}

	@Override
	public int getNumberOfSales() 
	{
		return successfulSales;
	}

	@Override
	public int getNumberOfCancellationsNoGas() 
	{
		return canceledSales_noGas;
	}
	
	@Override
	public int getNumberOfCancellationsTooExpensive() 
	{
		return canceledSales_expensive;
	}

	@Override
	public double getPrice(GasType type) 
	{
		return type.price;
	}

	@Override
	public void setPrice(GasType type, double price) 
	{
		type.price = price;
	}
}