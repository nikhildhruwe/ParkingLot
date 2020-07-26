import com.bridgelabz.parkinglot.enums.DriverType;
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
        Car car1 = new Car();
        parkingLot.parkVehicle(car1);
        boolean isParked = parkingLot.isVehicleParked(car1);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_IfPresentInParkingLot_ShouldThrowException() {
        try {
            Car car1 = new Car();
            parkingLot.parkVehicle(car1);
            parkingLot.parkVehicle(car1);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.ALREADY_PRESENT);
        }
    }

    //UC2
    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnFalse() {
        Car car1 = new Car();
        try {
            parkingLot.parkVehicle(car1);
            parkingLot.unParkVehicle(car1);
            boolean isParked = parkingLot.isVehicleParked(car1);
            Assert.assertFalse(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }


    //UC3
    @Test
    public void givenVehiclesToPark_WhenCapacityExceeds_ShouldThrowException() {
        try {
            Car car1 = new Car();
            Car car2 = new Car();
            Car car3 = new Car();
            parkingLot.parkVehicle(car1);
            parkingLot.parkVehicle(car2);
            parkingLot.parkVehicle(car3);
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
    }

    //UC4
    @Test
    public void givenVehiclesToPark_IfCapacityFull_ShouldInformAirportSecurity() {
        parkingLot.addObserver(airportSecurity);
        Car car1 = new Car();
        Car car2 = new Car();
        try {
            parkingLot.parkVehicle(car1);
            parkingLot.parkVehicle(car2);
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
        Car car1 = new Car();
        Car car2 = new Car();
        try {
            parkingLot.parkVehicle(car1);
            parkingLot.parkVehicle(car2);
            parkingLot.unParkVehicle(car1);
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
        parkingLot.parkVehicle(firstCar);
        parkingLot.parkVehicle(secondCar);
        parkingLot.unParkVehicle(firstCar);
        parkingLot.parkVehicle(firstCar);
        int slotNumber = parkingLot.getVehicleSlotNumber(secondCar);
        Assert.assertEquals(1, slotNumber);
    }

    //UC7
    @Test
    public void givenVehicleToUnPark_WhenFound_ShouldUnParkAndReturnParkingStatus() {
        parkingLot.addObserver(parkingLotOwner);
        Car firstCar = new Car();
        parkingLot.parkVehicle(firstCar);
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
        parkingLot.parkVehicle(firstCar);
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
        parkingLotSystem.parkVehicle(firstCar);
        parkingLotSystem.parkVehicle(secondCar);
        parkingLotSystem.parkVehicle(thirdCar);
        parkingLotSystem.unParkVehicle(secondCar);
        parkingLotSystem.parkVehicle(fourthCar);
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
            parkingLotSystem.parkVehicle(firstCar);
            parkingLotSystem.parkVehicle(secondCar);
            parkingLotSystem.parkVehicle(thirdCar);
            parkingLotSystem.parkVehicle(thirdCar);
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
            parkingLotSystem.parkVehicle(firstCar);
            parkingLotSystem.parkVehicle(secondCar);
            parkingLotSystem.parkVehicle(thirdCar);
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
        parkingLotSystem.parkVehicle(firstCar);
        parkingLotSystem.parkVehicle(secondCar);
        parkingLotSystem.parkVehicle(thirdCar);
        parkingLotSystem.parkVehicle(fourthCar);
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
            parkingLotSystem.parkVehicle(firstCar);
            parkingLotSystem.parkVehicle(secondCar);
            parkingLotSystem.parkVehicle(thirdCar);
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

        parkingLotSystem.parkVehicle(firstCar);
        parkingLotSystem.parkVehicle(secondCar);
        parkingLotSystem.parkVehicle(thirdCar);
        parkingLotSystem.parkVehicle(fourthCar);
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

        parkingLotSystem.parkVehicle(firstCar);
        parkingLotSystem.parkVehicle(secondCar);
        parkingLotSystem.parkVehicle(thirdCar);
        parkingLotSystem.parkVehicle(fourthCar);

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
            parkingLotSystem.parkVehicle(firstCar);
            parkingLotSystem.parkVehicle(secondCar);
            parkingLotSystem.parkVehicle(thirdCar);
            parkingLotSystem.parkVehicle(fourthCar);

            parkingLotSystem.getVehicleByColor("red");
        } catch (ParkingLotException e) {
            Assert.assertEquals(e.type, ParkingLotException.ExceptionType.INVALID_COLOR);
        }
    }
}
