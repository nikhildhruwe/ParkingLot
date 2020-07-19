import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.service.ParkingLot;
import com.bridgelabz.parkinglot.model.Vehicle;
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
    public void givenVehicle_WhenParked_ShouldReturnTrue() throws ParkingLotException {
        Vehicle vehicle1 = new Vehicle("111");
        parkingLot.parkVehicle(vehicle1);
        boolean isParked = parkingLot.isVehicleParked();
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_IfPresent_ShouldThrowException() {
        try {
            Vehicle vehicle1 = new Vehicle("111");
            Vehicle vehicle2 = new Vehicle("111");
            parkingLot.parkVehicle(vehicle1);
            parkingLot.parkVehicle(vehicle2);
        }catch (ParkingLotException e){
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.ALREADY_PRESENT);
        }
    }

    //UC2
    @Test
    public void givenVehicleNumber_WhenUnParked_ShouldUnParkAndReturnFalse() throws ParkingLotException {
        Vehicle vehicle1 = new Vehicle("111");
        parkingLot.unParkVehicle(vehicle1);
        boolean isParked = parkingLot.isVehicleParked();
        Assert.assertFalse(isParked);
    }
//
//    @Test
//    public void givenVehicleNumber_IfNotPresent_ShouldReturnFalse() throws ParkingLotException {
//        String[] vehicleNumber = {"111", "222"};
//        parkingLot.vehicleParking(vehicleNumber);
//        boolean isRemoved = parkingLot.vehicleUnparking("333");
//        Assert.assertFalse(isRemoved);
//    }
//
//    //UC3
//    @Test
//    public void givenVehiclesToPark_WhenCapacityExceeds_ShouldThrowException() {
//        try {
//            String[] vehicleNumber = {"111", "222", "333"};
//            parkingLot.vehicleParking(vehicleNumber);
//        } catch (ParkingLotException e) {
//            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
//        }
//    }
//
//    @Test
//    public void givenVehiclesToPark_IfCapacityFull_ShouldInformAirportSecurity() throws ParkingLotException {
//        String[] vehicleNumber = {"111", "222", "333"};
//        parkingLot.vehicleParking(vehicleNumber);
//        boolean parkingFull = parkingLot.isParkingFull();
//        Assert.assertTrue(parkingFull);
//    }
}