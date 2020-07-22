import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.service.ParkingLot;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.AirportSecurity;
import com.bridgelabz.parkinglot.observer.ParkingLotOwner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ParkingLotTest {

    private ParkingLot parkingLot;
    ParkingLotOwner parkingLotOwner;
    AirportSecurity airportSecurity;

    @Before
    public void setup() {
        parkingLot = new ParkingLot(2);
        parkingLotOwner = new ParkingLotOwner();
        airportSecurity = new AirportSecurity();
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
    public void givenVehicle_IfPresentInParkingLot_ShouldThrowException() {
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
    public void givenVehicle_WhenUnParked_ShouldReturnFalse() {
        Vehicle vehicle1 = new Vehicle();
        try {
            parkingLot.parkVehicle(vehicle1);
            parkingLot.unParkVehicle(vehicle1);
            boolean isParked = parkingLot.isVehicleParked(vehicle1);
            Assert.assertFalse(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
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
    public void givenVehiclesToPark_IfCapacityFull_ShouldInformAirportSecurity() {
        parkingLot.addObserver(airportSecurity);
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        try {
            parkingLot.parkVehicle(vehicle1);
            parkingLot.parkVehicle(vehicle2);
            boolean isParkingFull = airportSecurity.getParkingCapacity();
            Assert.assertTrue(isParkingFull);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //UC5
    @Test
    public void givenCapacityIsFull_WhenUnParked_ShouldInformParkingLotOwner() {
        parkingLot.addObserver(parkingLotOwner);
        parkingLot.addObserver(airportSecurity);
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        try {
            parkingLot.parkVehicle(vehicle1);
            parkingLot.parkVehicle(vehicle2);
            parkingLot.unParkVehicle(vehicle1);
            boolean isParkingFull = parkingLotOwner.getParkingCapacity();
            Assert.assertFalse(isParkingFull);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //UC6
    @Test
    public void givenVehicleToAttendant_WhenParkedAccordingToOwner_ShouldReturnSlotNumber() {
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        Vehicle secondVehicle = new Vehicle();
        parkingLot.parkVehicle(firstVehicle);
        parkingLot.parkVehicle(secondVehicle);
        parkingLot.unParkVehicle(firstVehicle);
        parkingLot.parkVehicle(firstVehicle);
        int slotNumber = parkingLot.getSlotNumber(secondVehicle);
        Assert.assertEquals(1, slotNumber);
    }

    //UC7
    @Test
    public void givenVehicleToUnPark_WhenFound_ShouldUnParkAndReturnParkingStatus() {
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        parkingLot.parkVehicle(firstVehicle);
        int slotNumber = parkingLot.getSlotNumber(firstVehicle);
        parkingLot.unParkVehicle(slotNumber);
        boolean isVehicleParked = parkingLot.isVehicleParked(firstVehicle);
        Assert.assertFalse(isVehicleParked);
    }

    //UC8
    @Test
    public void givenVehicle_WhenParked_ShouldInformOwner() {
        parkingLot.addObserver(parkingLotOwner);
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Vehicle firstVehicle = new Vehicle();
        parkingLot.parkVehicle(firstVehicle);
        LocalDateTime parkTime = parkingLot.getParkTime(firstVehicle);
        Assert.assertEquals(currentTime, parkTime);
    }
}
