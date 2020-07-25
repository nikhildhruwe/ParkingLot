import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleSize;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.AirportSecurity;
import com.bridgelabz.parkinglot.observer.ParkingLotOwner;
import com.bridgelabz.parkinglot.service.ParkingLot;
import com.bridgelabz.parkinglot.service.ParkingLotSystem;
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
        parkingLot = new ParkingLot(3);
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
        int slotNumber = parkingLot.getVehicleSlotNumber(secondVehicle);
        Assert.assertEquals(1, slotNumber);
    }

    //UC7
    @Test
    public void givenVehicleToUnPark_WhenFound_ShouldUnParkAndReturnParkingStatus() {
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        parkingLot.parkVehicle(firstVehicle);
        int slotNumber = parkingLot.getVehicleSlotNumber(firstVehicle);
        parkingLot.unParkVehicle(slotNumber);
        boolean isVehicleParked = parkingLot.isVehicleParked(firstVehicle);
        Assert.assertFalse(isVehicleParked);
    }

    //UC8
    @Test
    public void givenVehicle_WhenParked_ShouldReturnParkingTimeOfVehicle() {
        parkingLot.addObserver(parkingLotOwner);
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Vehicle firstVehicle = new Vehicle();
        parkingLot.parkVehicle(firstVehicle);
        LocalDateTime parkTime = parkingLot.getParkTime(firstVehicle);
        Assert.assertEquals(currentTime, parkTime);
    }

    //UC9
    @Test
    public void givenVehiclesToPark_WhenThereIsMultipleLots_ShouldBeEvenlyDistributedInParkingLots() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        Vehicle fourthVehicle = new Vehicle();
        parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL);
        parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL);
        parkingLotSystem.unParkVehicle(secondVehicle);
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL);
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthVehicle);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthVehicle);
        Assert.assertEquals(2,vehicleLotLocation);
        Assert.assertEquals(1,vehicleSlotNumber);
    }

    @Test
    public void givenVehiclesToPark_IfVehicleAlreadyPresentInAnyParkingLot_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL);
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.ALREADY_PRESENT);
        }
    }

    @Test
    public void givenVehiclesToPark_IfNoSpaceInAnyParkingLot_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 1);
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
            System.out.println(e.getMessage());
        }
    }

    //UC10
    @Test
    public void givenDriverType_IfHandicap_ShouldALotNearestParkingSlot() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        Vehicle fourthVehicle = new Vehicle();
        parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL);
        parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL);
        parkingLotSystem.parkVehicle(thirdVehicle, DriverType.HANDICAP);
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.HANDICAP);
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthVehicle);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthVehicle);
        Assert.assertEquals(1,vehicleLotLocation);
        Assert.assertEquals(3,vehicleSlotNumber);
    }

    @Test
    public void givenVehicleToPark_IfNotPresent_ShouldThrowException() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
            parkingLot.addObserver(parkingLotOwner);
            Vehicle firstVehicle = new Vehicle();
            Vehicle secondVehicle = new Vehicle();
            Vehicle thirdVehicle = new Vehicle();
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL);
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL);
            parkingLotSystem.unParkVehicle(thirdVehicle);
        }catch (ParkingLotException e){
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
            System.out.println(e.getMessage());
        }
    }

    //UC11
    @Test
    public void givenLargeVehicle_WhenParked_ShouldParkInLotWithHighestNumberOfSlots() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Vehicle firstVehicle = new Vehicle(VehicleSize.SMALL);
        Vehicle secondVehicle = new Vehicle(VehicleSize.SMALL);
        Vehicle thirdVehicle = new Vehicle(VehicleSize.SMALL);
        Vehicle fourthVehicle = new Vehicle(VehicleSize.LARGE);

        parkingLotSystem.parkVehicle(firstVehicle,DriverType.NORMAL);
        parkingLotSystem.parkVehicle(secondVehicle,DriverType.NORMAL);
        parkingLotSystem.parkVehicle(thirdVehicle,DriverType.HANDICAP);
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.HANDICAP);
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthVehicle);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthVehicle);
        Assert.assertEquals(3,vehicleLotLocation);
        Assert.assertEquals(1,vehicleSlotNumber);
    }
}
