import java.util.ArrayList;
import java.util.Arrays;

public class ParkingLot {

    private static final int MAX_CAPACITY = 2;
    private final ArrayList<String> parkingList;

    public ParkingLot() {
        this.parkingList = new ArrayList<>();
    }

    public int vehicleParking(String[] vehicle) throws ParkingLotException {
        addVehicle(vehicle);
        return parkingList.size();
    }

    private void addVehicle(String[] vehicles) throws ParkingLotException {
        int capacity = 0;
        for (String vehicle : vehicles){
            if (capacity < MAX_CAPACITY)
                parkingList.add(vehicle);
            else
                throw new ParkingLotException("Capacity Exceeded", ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
            capacity++;
        }
    }

    public boolean vehicleUnparking(String vehicleNumber) {
       if(parkingList.contains(vehicleNumber)) {
            parkingList.remove(vehicleNumber);
            return true;
        }
        return false;
    }
}
