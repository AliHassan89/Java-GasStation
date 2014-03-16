
import java.util.LinkedList;
import java.util.List;

import net.bigpoint.assessment.gasstation.BigpointGasStation;
import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;

public class Main {
	
	public static void main (String [] args)
	{
		BigpointGasStation myGasStation = new BigpointGasStation();
		
		GasPump myGasPump_Regular = new GasPump(GasType.REGULAR, 10);
		GasPump myGasPump_Regular2 = new GasPump(GasType.REGULAR, 30);
		GasPump myGasPump_Diesel = new GasPump(GasType.DIESEL, 20);
		GasPump myGasPump_Super = new GasPump(GasType.SUPER, 30);
		
		myGasStation.addGasPump(myGasPump_Regular);
		myGasStation.addGasPump(myGasPump_Regular2);
		myGasStation.addGasPump(myGasPump_Diesel);
		myGasStation.addGasPump(myGasPump_Super);
		
		int pumps = myGasStation.getGasPumps().size();
		
		myGasStation.setPrice(GasType.SUPER, 500);
		myGasStation.setPrice(GasType.DIESEL, 300);
		myGasStation.setPrice(GasType.REGULAR, 100);
		
		double price = myGasStation.getPrice(GasType.SUPER);
		
		List<BuyGasThread> threads = new LinkedList<BuyGasThread>();
		
		threads.add(new BuyGasThread(GasType.REGULAR, 5, 110, myGasStation));
		threads.add(new BuyGasThread(GasType.REGULAR, 5, 110, myGasStation));
		threads.add(new BuyGasThread(GasType.DIESEL, 5, 310, myGasStation));
		threads.add(new BuyGasThread(GasType.REGULAR, 5, 110, myGasStation));
		threads.add(new BuyGasThread(GasType.SUPER, 5, 510, myGasStation));
		threads.add(new BuyGasThread(GasType.SUPER, 5, 10, myGasStation));
		
		for(int i=0;i<threads.size();i++)
			(new Thread(threads.get(i))).start();
		
		
		System.out.printf("Gas pumps in this station are: %d", pumps);
		System.out.printf("\nPrice of SUPER is: %f", price);
	}
}

