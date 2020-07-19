import com.bridgelabz.parkinglot.enums.VIEWER;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.service.ParkingLot;
import com.bridgelabz.parkinglot.model.Vehicle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotTest {

    private ParkingLot parkingLot;

    @Before
    public void setup() {
        parkingLot = new ParkingLot();
    }

    //UC1
    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() throws ParkingLotException {
        Vehicle vehicle1 = new Vehicle();
        parkingLot.parkVehicle(vehicle1);
        boolean isParked = parkingLot.isVehicleParked(vehicle1);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_IfPresent_ShouldThrowException() {
        try {
            Vehicle vehicle1 = new Vehicle();
            parkingLot.parkVehicle(vehicle1);
            parkingLot.parkVehicle(vehicle1);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.ALREADY_PRESENT);
        }
    }

    //UC2
    @Test
    public void givenVehicleNumber_WhenUnParked_ShouldUnParkAndReturnFalse() throws ParkingLotException {
        Vehicle vehicle1 = new Vehicle();
        parkingLot.parkVehicle(vehicle1);
        parkingLot.unParkVehicle(vehicle1);
        boolean isParked = parkingLot.isVehicleParked(vehicle1);
        Assert.assertFalse(isParked);
    }


    //UC3
    @Test
    public void givenVehiclesToPark_WhenCapacityExceeds_ShouldThrowException() {
        try {
            Vehicle vehicle1 = new Vehicle();
            Vehicle vehicle2 = new Vehicle();
            Vehicle vehicle3 = new Vehicle();
            parkingLot.parkVehicle(vehicle1);
            parkingLot.parkVehicle(vehicle2);
            parkingLot.parkVehicle(vehicle3);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
    }

    //UC4
    @Test
    public void givenVehiclesToPark_IfCapacityFull_ShouldInformAirportSecurity() throws ParkingLotException {
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        parkingLot.parkVehicle(vehicle1);
        parkingLot.parkVehicle(vehicle2);
        boolean isParkingFull = parkingLot.isParkingFull(VIEWER.AIRPORT_SECURITY);
        Assert.assertTrue(isParkingFull);
    }

    //UC5
    @Test
    public void givenCapacityIsFull_WhenUnParked_ShouldInformParkingLotOwner() throws ParkingLotException {
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        parkingLot.parkVehicle(vehicle1);
        parkingLot.parkVehicle(vehicle2);
        parkingLot.unParkVehicle(vehicle1);
        boolean isParkingFull = parkingLot.isParkingFull(VIEWER.OWNER);
        Assert.assertFalse(isParkingFull);
    }
}