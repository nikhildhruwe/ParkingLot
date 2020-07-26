import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleCompany;
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
import java.util.Arrays;
import java.util.List;

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
        parkingLot.parkVehicle(vehicle1, "abc");
        boolean isParked = parkingLot.isVehicleParked(vehicle1);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_IfPresentInParkingLot_ShouldThrowException() {
        try {
            Vehicle vehicle1 = new Vehicle();
            parkingLot.parkVehicle(vehicle1, "abc");
            parkingLot.parkVehicle(vehicle1, "abc");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.ALREADY_PRESENT);
        }
    }

    //UC2
    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnFalse() {
        Vehicle vehicle1 = new Vehicle();
        try {
            parkingLot.parkVehicle(vehicle1, "abc");
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
            parkingLot.parkVehicle(vehicle1, "abc");
            parkingLot.parkVehicle(vehicle2, "abc");
            parkingLot.parkVehicle(vehicle3, "abc");
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
            parkingLot.parkVehicle(vehicle1, "abc");
            parkingLot.parkVehicle(vehicle2, "abc");
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
            parkingLot.parkVehicle(vehicle1, "abc");
            parkingLot.parkVehicle(vehicle2, "abc");
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
        parkingLot.parkVehicle(firstVehicle, "abc");
        parkingLot.parkVehicle(secondVehicle, "abc");
        parkingLot.unParkVehicle(firstVehicle);
        parkingLot.parkVehicle(firstVehicle, "abc");
        int slotNumber = parkingLot.getVehicleSlotNumber(secondVehicle);
        Assert.assertEquals(1, slotNumber);
    }

    //UC7
    @Test
    public void givenVehicleToUnPark_WhenFound_ShouldUnParkAndReturnParkingStatus() {
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        parkingLot.parkVehicle(firstVehicle, "abc");
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
        parkingLot.parkVehicle(firstVehicle, "abc");
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
        parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.unParkVehicle(secondVehicle);
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, "abc");
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthVehicle);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthVehicle);
        Assert.assertEquals(2, vehicleLotLocation);
        Assert.assertEquals(1, vehicleSlotNumber);
    }

    @Test
    public void givenVehiclesToPark_IfVehicleAlreadyPresentInAnyParkingLot_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        parkingLot.addObserver(parkingLotOwner);
        Vehicle firstVehicle = new Vehicle();
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "abc");
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
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "abc");
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
        parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(thirdVehicle, DriverType.HANDICAP, "abc");
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.HANDICAP, "abc");
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthVehicle);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthVehicle);
        Assert.assertEquals(1, vehicleLotLocation);
        Assert.assertEquals(3, vehicleSlotNumber);
    }

    @Test
    public void givenVehicleToPark_IfNotPresent_ShouldThrowException() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
            parkingLot.addObserver(parkingLotOwner);
            Vehicle firstVehicle = new Vehicle();
            Vehicle secondVehicle = new Vehicle();
            Vehicle thirdVehicle = new Vehicle();
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.unParkVehicle(thirdVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
            System.out.println(e.getMessage());
        }
    }

    //UC11
    @Test
    public void givenLargeVehicle_WhenParked_ShouldParkInLotWithHighestNumberOfSlots() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Vehicle firstVehicle = new Vehicle(VehicleSize.SMALL, "blue");
        Vehicle secondVehicle = new Vehicle(VehicleSize.SMALL, "green");
        Vehicle thirdVehicle = new Vehicle(VehicleSize.SMALL, "grey");
        Vehicle fourthVehicle = new Vehicle(VehicleSize.LARGE, "red");

        parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(thirdVehicle, DriverType.HANDICAP, "abc");
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.HANDICAP, "abc");
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthVehicle);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthVehicle);
        Assert.assertEquals(3, vehicleLotLocation);
        Assert.assertEquals(1, vehicleSlotNumber);
    }

    //UC12
    @Test
    public void givenVehiclesInParkingLot_IfColorIsWhite_ShouldReturnLocation() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Vehicle firstVehicle = new Vehicle(VehicleSize.SMALL, "blue");
        Vehicle secondVehicle = new Vehicle(VehicleSize.SMALL, "white");
        Vehicle thirdVehicle = new Vehicle(VehicleSize.SMALL, "orange");
        Vehicle fourthVehicle = new Vehicle(VehicleSize.SMALL, "white");

        parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "abc");
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, "abc");

        List<String> locationList = parkingLotSystem.getVehicleByColor("white");
        List<String> expectedList = Arrays.asList("2-1", "1-2");
        Assert.assertEquals(expectedList, locationList);
    }

    @Test
    public void givenVehiclesInParkingLot_IfRequiredVehicleColorIsNotPresent_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Vehicle firstVehicle = new Vehicle(VehicleSize.SMALL, "blue");
        Vehicle secondVehicle = new Vehicle(VehicleSize.SMALL, "white");
        Vehicle thirdVehicle = new Vehicle(VehicleSize.SMALL, "orange");
        Vehicle fourthVehicle = new Vehicle(VehicleSize.SMALL, "white");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "abc");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, "abc");

            parkingLotSystem.getVehicleByColor("red");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.INVALID_COLOR);
        }
    }

    //UC13
    @Test
    public void givenBlueToyotaCar_IfPresent_ShouldReturnLocation() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Vehicle firstVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.TOYOTA, "blue", "a123");
        Vehicle secondVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.BMW, "white", "b234");
        Vehicle thirdVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.MARUTI, "blue", "c456");
        Vehicle fourthVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.KIA, "grey", "d567");
        Vehicle fifthVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.TOYOTA, "grey", "e899");

        parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "firstAttendant");
        parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "secondAttendant");
        parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "thirdAttendant");
        parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, "firstAttendant");
        parkingLotSystem.parkVehicle(fifthVehicle, DriverType.NORMAL, "secondAttendant");

        List<String> vehicleDetails = parkingLotSystem.getVehicleDetailsByCompanyAndColor(VehicleCompany.TOYOTA, "blue");
        List<String> expectedDetails = Arrays.asList("Lot: 1,Slot: 1,Attendant: firstAttendant,Number Plate: a123");
        Assert.assertEquals(expectedDetails, vehicleDetails);
    }

    @Test
    public void givenBlueToyotaCar_IfNotPresent_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Vehicle firstVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.MAHINDRA, "blue", "a123");
        Vehicle secondVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.BMW, "white", "b234");
        Vehicle thirdVehicle = new Vehicle(VehicleSize.SMALL, VehicleCompany.MARUTI, "blue", "c456");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, "firstAttendant");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, "secondAttendant");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, "thirdAttendant");

            parkingLotSystem.getVehicleDetailsByCompanyAndColor(VehicleCompany.TOYOTA, "blue");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        }
    }
}
