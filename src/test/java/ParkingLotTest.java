import org.junit.Assert;
import org.junit.Test;

public class ParkingLotTest {

    //UC1
    @Test
    public void givenVehicles_WhenParked_ShouldReturnVehicleCount() throws ParkingLotException {
        ParkingLot parkingLot = new ParkingLot();
        String[] vehicleNumber = { "111", "222"};
        int parked = parkingLot.vehicleParking(vehicleNumber);
        Assert.assertEquals(2,parked);
    }

    //UC2
    @Test
    public void givenVehicleNumber_IfPresent_ShouldRemoveFromListAndReturnTrue() throws ParkingLotException {
        ParkingLot parkingLot = new ParkingLot();
        String[] vehicleNumber = { "111", "222"};
        parkingLot.vehicleParking(vehicleNumber);
        boolean isRemoved = parkingLot.vehicleUnparking("111");
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void givenVehicleNumber_IfNotPresentInList_ShouldReturnFalse() throws ParkingLotException {
        ParkingLot parkingLot = new ParkingLot();
        String[] vehicleNumber = { "111", "222"};
        parkingLot.vehicleParking(vehicleNumber);
        boolean isRemoved = parkingLot.vehicleUnparking("333");
        Assert.assertFalse(isRemoved);
    }

    //UC3
    @Test
    public void givenVehicles_WhenCapacityExceeds_ShouldThrowException(){
        try {
            ParkingLot parkingLot = new ParkingLot();
            String[] vehicleNumber = {"111", "222", "333"};
            parkingLot.vehicleParking(vehicleNumber);
        }catch (ParkingLotException e){
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
    }
}