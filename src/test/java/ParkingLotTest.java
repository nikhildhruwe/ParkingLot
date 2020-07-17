import org.junit.Assert;
import org.junit.Test;

public class ParkingLotTest {

    @Test
    public void givenVehicles_WhenParked_ShouldReturnVehicleCount() {
        ParkingLot parkingLot = new ParkingLot();
        String[] vehicleNumber = { "111", "222"};
        int parked = parkingLot.vehicleParking(vehicleNumber);
        Assert.assertEquals(2,parked);
    }

    @Test
    public void givenVehicleNumber_IfPresent_ShouldRemoveFromList() {
        ParkingLot parkingLot = new ParkingLot();
        String[] vehicleNumber = { "111", "222"};
        parkingLot.vehicleParking(vehicleNumber);
        boolean isRemoved = parkingLot.vehicleUnparking("111");
        Assert.assertTrue(isRemoved);
    }
}