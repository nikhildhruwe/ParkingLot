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
}
