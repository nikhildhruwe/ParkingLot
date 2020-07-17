import java.util.ArrayList;
import java.util.Arrays;

public class ParkingLot {

    private final ArrayList<String> vehicleList;

    public ParkingLot() {
        this.vehicleList = new ArrayList<>();
    }

    public int vehicleParking(String[] vehicle) {
        addVehicle(vehicle);
        return vehicleList.size();
    }

    private void addVehicle(String[] vehicles) {
        vehicleList.addAll(Arrays.asList(vehicles));
    }

    public boolean vehicleUnparking(String vehicleNumber) {
       if(vehicleList.contains(vehicleNumber)) {
            vehicleList.remove(vehicleNumber);
            return true;
        }
        return false;
    }
}
