import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleCompany;
import com.bridgelabz.parkinglot.enums.VehicleSize;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;
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
        Car firstCar = new Car();
        parkingLot.parkVehicle(firstCar, "abc");
        boolean isParked = parkingLot.isVehicleParked(firstCar);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_IfPresentInParkingLot_ShouldThrowException() {
        try {
            Car firstCar = new Car();
            parkingLot.parkVehicle(firstCar, "abc");
            parkingLot.parkVehicle(firstCar, "abc");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.ALREADY_PRESENT);
        }
    }

    //UC2
    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnFalse() {
        Car firstCar = new Car();
        try {
            parkingLot.parkVehicle(firstCar, "abc");
            parkingLot.unParkVehicle(firstCar);
            boolean isParked = parkingLot.isVehicleParked(firstCar);
            Assert.assertFalse(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }


    //UC3
    @Test
    public void givenVehiclesToPark_WhenCapacityExceeds_ShouldThrowException() {
        try {
            Car firstCar = new Car();
            Car secondCar = new Car();
            Car thirdCar = new Car();
            parkingLot.parkVehicle(firstCar, "abc");
            parkingLot.parkVehicle(secondCar, "abc");
            parkingLot.parkVehicle(thirdCar, "abc");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
    }

    //UC4
    @Test
    public void givenVehiclesToPark_IfCapacityFull_ShouldInformAirportSecurity() {
        parkingLot.addObserver(airportSecurity);
        Car firstCar = new Car();
        Car secondCar = new Car();
        try {
            parkingLot.parkVehicle(firstCar, "abc");
            parkingLot.parkVehicle(secondCar, "abc");
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
        Car firstCar = new Car();
        Car secondCar = new Car();
        try {
            parkingLot.parkVehicle(firstCar, "abc");
            parkingLot.parkVehicle(secondCar, "abc");
            parkingLot.unParkVehicle(firstCar);
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
        Car firstCar = new Car();
        Car secondCar = new Car();
        parkingLot.parkVehicle(firstCar, "abc");
        parkingLot.parkVehicle(secondCar, "abc");
        parkingLot.unParkVehicle(firstCar);
        parkingLot.parkVehicle(firstCar, "abc");
        int slotNumber = parkingLot.getVehicleSlotNumber(secondCar);
        Assert.assertEquals(1, slotNumber);
    }

    //UC7
    @Test
    public void givenVehicleToUnPark_WhenFound_ShouldUnParkAndReturnParkingStatus() {
        parkingLot.addObserver(parkingLotOwner);
        Car firstCar = new Car();
        parkingLot.parkVehicle(firstCar, "abc");
        int slotNumber = parkingLot.getVehicleSlotNumber(firstCar);
        parkingLot.unParkVehicle(slotNumber);
        boolean isVehicleParked = parkingLot.isVehicleParked(firstCar);
        Assert.assertFalse(isVehicleParked);
    }

    //UC8
    @Test
    public void givenVehicle_WhenParked_ShouldReturnParkingTimeOfVehicle() {
        parkingLot.addObserver(parkingLotOwner);
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Car firstCar = new Car();
        parkingLot.parkVehicle(firstCar, "abc");
        LocalDateTime parkTime = parkingLot.getParkTime(firstCar);
        Assert.assertEquals(currentTime, parkTime);
    }

    //UC9
    @Test
    public void givenVehiclesToPark_WhenThereIsMultipleLots_ShouldBeEvenlyDistributedInParkingLots() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        parkingLot.addObserver(parkingLotOwner);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        Car fourthCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        parkingLotSystem.parkVehicle(firstCar, "abc");
        parkingLotSystem.parkVehicle(secondCar, "abc");
        parkingLotSystem.parkVehicle(thirdCar, "abc");
        parkingLotSystem.unParkVehicle(secondCar);
        parkingLotSystem.parkVehicle(fourthCar, "abc");
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthCar);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthCar);
        Assert.assertEquals(2, vehicleLotLocation);
        Assert.assertEquals(1, vehicleSlotNumber);
    }

    @Test
    public void givenVehiclesToPark_IfVehicleAlreadyPresentInAnyParkingLot_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        parkingLot.addObserver(parkingLotOwner);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        try {
            parkingLotSystem.parkVehicle(firstCar, "abc");
            parkingLotSystem.parkVehicle(secondCar, "abc");
            parkingLotSystem.parkVehicle(thirdCar, "abc");
            parkingLotSystem.parkVehicle(thirdCar, "abc");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.ALREADY_PRESENT);
        }
    }

    @Test
    public void givenVehiclesToPark_IfNoSpaceInAnyParkingLot_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(2, 1);
        parkingLot.addObserver(parkingLotOwner);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        try {
            parkingLotSystem.parkVehicle(firstCar, "abc");
            parkingLotSystem.parkVehicle(secondCar, "abc");
            parkingLotSystem.parkVehicle(thirdCar, "abc");
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
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.HANDICAP, "grey");
        Car fourthCar = new Car(VehicleSize.SMALL, DriverType.HANDICAP, "red");
        parkingLotSystem.parkVehicle(firstCar, "abc");
        parkingLotSystem.parkVehicle(secondCar, "abc");
        parkingLotSystem.parkVehicle(thirdCar, "abc");
        parkingLotSystem.parkVehicle(fourthCar, "abc");
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthCar);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthCar);
        Assert.assertEquals(1, vehicleLotLocation);
        Assert.assertEquals(3, vehicleSlotNumber);
    }

    @Test
    public void givenVehicleToPark_IfNotPresent_ShouldThrowException() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
            parkingLot.addObserver(parkingLotOwner);
            Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
            Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
            Car thirdCar = new Car(VehicleSize.SMALL, DriverType.HANDICAP, "grey");
            parkingLotSystem.parkVehicle(firstCar, "abc");
            parkingLotSystem.parkVehicle(secondCar, "abc");
            parkingLotSystem.unParkVehicle(thirdCar);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
            System.out.println(e.getMessage());
        }
    }

    //UC11
    @Test
    public void givenLargeVehicle_WhenParked_ShouldParkInLotWithHighestNumberOfSlots() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "green");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.HANDICAP, "grey");
        Car fourthCar = new Car(VehicleSize.LARGE, DriverType.HANDICAP, "red");

        parkingLotSystem.parkVehicle(firstCar, "abc");
        parkingLotSystem.parkVehicle(secondCar, "abc");
        parkingLotSystem.parkVehicle(thirdCar, "abc");
        parkingLotSystem.parkVehicle(fourthCar, "abc");
        int vehicleLotLocation = parkingLotSystem.getVehicleLotNumber(fourthCar);
        int vehicleSlotNumber = parkingLotSystem.getVehicleSlotNumber(fourthCar);
        Assert.assertEquals(3, vehicleLotLocation);
        Assert.assertEquals(1, vehicleSlotNumber);
    }

    //UC12
    @Test
    public void givenVehiclesInParkingLot_IfColorIsWhite_ShouldReturnLocation() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "white");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "orange");
        Car fourthCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "white");

        parkingLotSystem.parkVehicle(firstCar, "abc");
        parkingLotSystem.parkVehicle(secondCar, "abc");
        parkingLotSystem.parkVehicle(thirdCar, "abc");
        parkingLotSystem.parkVehicle(fourthCar, "abc");

        List<String> locationList = parkingLotSystem.getVehicleByColor("white");
        List<String> expectedList = Arrays.asList("2-1", "1-2");
        Assert.assertEquals(expectedList, locationList);
    }

    @Test
    public void givenVehiclesInParkingLot_IfRequiredVehicleColorIsNotPresent_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "blue");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "white");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "orange");
        Car fourthCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, "white");
        try {
            parkingLotSystem.parkVehicle(firstCar, "abc");
            parkingLotSystem.parkVehicle(secondCar, "abc");
            parkingLotSystem.parkVehicle(thirdCar, "abc");
            parkingLotSystem.parkVehicle(fourthCar, "abc");

            parkingLotSystem.getVehicleByColor("red");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.INVALID_COLOR);
        }
    }

    //UC13
    @Test
    public void givenBlueToyotaCar_IfPresent_ShouldReturnLocation() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.TOYOTA, "blue", "a123");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.BMW, "white", "b234");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MARUTI, "blue", "c456");
        Car fourthCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.KIA, "grey", "d567");
        Car fifthCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.TOYOTA, "grey", "e899");

        parkingLotSystem.parkVehicle(firstCar, "firstAttendant");
        parkingLotSystem.parkVehicle(secondCar, "secondAttendant");
        parkingLotSystem.parkVehicle(thirdCar, "thirdAttendant");
        parkingLotSystem.parkVehicle(fourthCar, "firstAttendant");
        parkingLotSystem.parkVehicle(fifthCar, "secondAttendant");

        List<String> vehicleDetails = parkingLotSystem.getVehicleDetailsByCompanyAndColor(VehicleCompany.TOYOTA, "blue");
        List<String> expectedDetails = Arrays.asList("Lot: 1,Slot: 1,Attendant: firstAttendant,Number Plate: a123");
        Assert.assertEquals(expectedDetails, vehicleDetails);
    }

    @Test
    public void givenBlueToyotaCar_IfNotPresent_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MAHINDRA, "blue", "a123");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.BMW, "white", "b234");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MARUTI, "blue", "c456");
        try {
            parkingLotSystem.parkVehicle(firstCar, "firstAttendant");
            parkingLotSystem.parkVehicle(secondCar, "secondAttendant");
            parkingLotSystem.parkVehicle(thirdCar, "thirdAttendant");

            parkingLotSystem.getVehicleDetailsByCompanyAndColor(VehicleCompany.TOYOTA, "blue");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        }
    }

    //UC14
    @Test
    public void givenVehiclesToPark_IfGivenCompanyVehiclePresent_ShouldReturnParkingDetailsOfVehicle() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.TOYOTA, "blue", "a123");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.BMW, "white", "b234");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MARUTI, "blue", "c456");
        Car fourthCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.BMW, "grey", "d567");

        parkingLotSystem.parkVehicle(firstCar, "firstAttendant");
        parkingLotSystem.parkVehicle(secondCar, "secondAttendant");
        parkingLotSystem.parkVehicle(thirdCar, "thirdAttendant");
        parkingLotSystem.parkVehicle(fourthCar, "firstAttendant");

        List<String> vehicleParkingDetails = parkingLotSystem.getVehicleDetailsByCompany(VehicleCompany.BMW);
        List<String> expectedDetails = Arrays.asList("Lot: 2,Slot: 1,Number Plate: b234",
                "Lot: 1,Slot: 2,Number Plate: d567");
        Assert.assertEquals(expectedDetails, vehicleParkingDetails);
    }

    @Test
    public void givenVehicleBMW_IfNotPresent_ShouldThrowException() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MAHINDRA, "blue", "a123");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MARUTI, "blue", "c456");
        try {
            parkingLotSystem.parkVehicle(secondCar, "thirdAttendant");

            parkingLotSystem.getVehicleDetailsByCompany(VehicleCompany.BMW);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        }
    }

    //UC15
    @Test
    public void givenVehiclesToPark_IfParkedWithInLast30Minutes_ShouldReturnVehicleParkingDetails() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.LARGE, DriverType.NORMAL, VehicleCompany.TOYOTA, "blue", "a123");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.BMW, "white", "b234");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MARUTI, "blue", "c456");

        parkingLotSystem.parkVehicle(firstCar, "firstAttendant");
        parkingLotSystem.parkVehicle(secondCar, "secondAttendant");
        parkingLotSystem.parkVehicle(thirdCar, "thirdAttendant");

        List<String> vehicleDetailsWithInProvidedTime = parkingLotSystem.getVehicleDetailsWithInProvidedTime(30);
        List<String> expectedDetails = Arrays.asList("Lot: 1,Slot: 1,Number Plate: a123",
                "Lot: 2,Slot: 1,Number Plate: b234",
                "Lot: 3,Slot: 1,Number Plate: c456");
        Assert.assertEquals(expectedDetails, vehicleDetailsWithInProvidedTime);

    }

    @Test
    public void givenNoVehicles_IfCheckedVehiclesParkedWithInGivenTime_ShouldThrowException() {
        try {
            ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
            parkingLotSystem.getVehicleDetailsWithInProvidedTime(10);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        }
    }

    //UC16
    @Test
    public void givenLotNumber_IfSmallCarAndHandicapDriversPresent_ShouldReturnParkingDetails() {
        ParkingLotSystem parkingLotSystem = new ParkingLotSystem(3, 3);
        Car firstCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.TOYOTA, "blue", "a123");
        Car secondCar = new Car(VehicleSize.SMALL, DriverType.HANDICAP, VehicleCompany.MAHINDRA, "white", "b234");
        Car thirdCar = new Car(VehicleSize.SMALL, DriverType.NORMAL, VehicleCompany.MARUTI, "blue", "c456");
        Car fourthCar = new Car(VehicleSize.SMALL, DriverType.HANDICAP, VehicleCompany.KIA, "grey", "d567");

        parkingLotSystem.parkVehicle(firstCar, "firstAttendant");
        parkingLotSystem.parkVehicle(secondCar, "secondAttendant");
        parkingLotSystem.parkVehicle(thirdCar, "thirdAttendant");
        parkingLotSystem.parkVehicle(fourthCar, "firstAttendant");

        List<String> vehicleDetails = parkingLotSystem.getVehicleDetailsOfHandicapCarFromGivenLot(1);
        List<String> expectedDetails = Arrays.asList("Lot: 1,Slot: 2,Number Plate: b234",
                "Lot: 1,Slot: 3,Number Plate: d567");
        Assert.assertEquals(expectedDetails, vehicleDetails);
    }
}
