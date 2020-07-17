import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.service.ParkingLot;
import com.bridgelabz.parkinglot.utility.AirportSecurity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotTest {

    private ParkingLot parkingLot;

    @Before
    public void setup(){
        parkingLot = new ParkingLot();
    }
    //UC1
    @Test
    public void givenVehicles_WhenParked_ShouldReturnVehicleCount() throws ParkingLotException {
        String[] vehicleNumber = {"111", "222"};
        int parked = parkingLot.vehicleParking(vehicleNumber);
        Assert.assertEquals(2, parked);
    }

    //UC2
    @Test
    public void givenVehicleNumber_IfPresent_ShouldRemoveFromListAndReturnTrue() throws ParkingLotException {
        String[] vehicleNumber = {"111", "222"};
        parkingLot.vehicleParking(vehicleNumber);
        boolean isRemoved = parkingLot.vehicleUnparking("111");
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void givenVehicleNumber_IfNotPresent_ShouldReturnFalse() throws ParkingLotException {
        String[] vehicleNumber = {"111", "222"};
        parkingLot.vehicleParking(vehicleNumber);
        boolean isRemoved = parkingLot.vehicleUnparking("333");
        Assert.assertFalse(isRemoved);
    }

    //UC3
    @Test
    public void givenVehiclesToPark_WhenCapacityExceeds_ShouldThrowException() {
        try {
            String[] vehicleNumber = {"111", "222", "333"};
            parkingLot.vehicleParking(vehicleNumber);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
    }

    @Test
    public void givenVehiclesToPark_IfCapacityFull_ShouldInformAirportSecurity() throws ParkingLotException {
        String[] vehicleNumber = {"111", "222", "333"};
        parkingLot.vehicleParking(vehicleNumber);
        boolean parkingFull = parkingLot.isParkingFull();
        Assert.assertTrue(parkingFull);
    }
}