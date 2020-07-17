import org.junit.Assert;
import org.junit.Test;

public class ParkingLotTest {

    @Test
    public void givenVehicles_WhenParked_ShouldReturnVehicleCount() {
        ParkingLot parkingLot = new ParkingLot();
        String[] vehicleNumber = { "AAA", "BBB"};
        int parked = parkingLot.vehicleParking(vehicleNumber);
        Assert.assertEquals(2,parked);
    }
}