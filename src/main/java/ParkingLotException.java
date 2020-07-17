public class ParkingLotException extends Exception {
    public enum ExceptionType{
        CAPACITY_EXCEEDED
    }
    public ExceptionType type;

    public ParkingLotException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
