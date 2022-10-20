import exceptions.ParkingSpotNotFound;
import exceptions.UnknownVehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parkinglots.ParkingLotInMemoryRepository;
import parkinglots.ParkingLotRepository;
import parkingspots.LargeParkingSpot;
import parkingspots.MediumParkingSpot;
import parkingspots.ParkingSpot;
import parkingspots.SmallParkingSpot;
import parkingstrategies.RegularUserParkingStrategy;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class RegularUserParkingStrategyTest {
    private RegularUserParkingStrategy regularUserParkingStrategy;
    private ParkingLotRepository parkingLotRepository;
    List<ParkingSpot> parkingSpots;
    final String PLATE_NUMBER = "FAKE";

    @BeforeEach
    void initEach(){
        regularUserParkingStrategy = new RegularUserParkingStrategy();
        parkingSpots = new ArrayList<>();
        parkingLotRepository = new ParkingLotInMemoryRepository(parkingSpots);
    }

    @Test
    void getParkingSpotShouldThrowExceptionWhenNoParkingSpots(){
        assertThrows(ParkingSpotNotFound.class, ()-> regularUserParkingStrategy.getParkingSpot(new Car(PLATE_NUMBER, false),
                parkingLotRepository));
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpot() throws ParkingSpotNotFound {
        parkingSpots.add(new SmallParkingSpot(false));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Motorcycle(PLATE_NUMBER, false),
                parkingLotRepository);

        assertEquals(SmallParkingSpot.class, parkingSpot.getClass());
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpot() throws ParkingSpotNotFound {
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new LargeParkingSpot(false));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Car(PLATE_NUMBER, false),
                parkingLotRepository);

        assertEquals(MediumParkingSpot.class, parkingSpot.getClass());
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpot() throws ParkingSpotNotFound {
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new LargeParkingSpot(false));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Truck(PLATE_NUMBER, false),
                parkingLotRepository);

        assertEquals(LargeParkingSpot.class, parkingSpot.getClass());
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new SmallParkingSpot(true));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Motorcycle(PLATE_NUMBER, true),
                parkingLotRepository);

        assertEquals(SmallParkingSpot.class, parkingSpot.getClass());
        assertEquals(true, parkingSpot.hasElectricCharger());
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound{
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(true));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Car(PLATE_NUMBER, true),
                parkingLotRepository);

        assertEquals(MediumParkingSpot.class, parkingSpot.getClass());
        assertEquals(true, parkingSpot.hasElectricCharger());
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound{
        parkingSpots.add(new LargeParkingSpot(false));
        parkingSpots.add(new LargeParkingSpot(true));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Truck(PLATE_NUMBER, true),
                parkingLotRepository);

        assertEquals(LargeParkingSpot.class, parkingSpot.getClass());
        assertEquals(true, parkingSpot.hasElectricCharger());
    }

    @Test
    void getParkingSpotShouldThrowExceptionWhenSmallParkingSpotHasNoElectricCharger(){
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(true));
        parkingSpots.add(new LargeParkingSpot(true));

        assertThrows(ParkingSpotNotFound.class, ()-> regularUserParkingStrategy.getParkingSpot(new Motorcycle(PLATE_NUMBER, true),
                parkingLotRepository));
    }

    @Test
    void getParkingSpotShouldThrowExceptionWhenMediumParkingSpotHasNoElectricCharger(){
        parkingSpots.add(new SmallParkingSpot(true));
        parkingSpots.add(new SmallParkingSpot(false));
        parkingSpots.add(new MediumParkingSpot(false));
        parkingSpots.add(new LargeParkingSpot(true));

        assertThrows(ParkingSpotNotFound.class, ()-> regularUserParkingStrategy.getParkingSpot(new Car(PLATE_NUMBER, true),
                parkingLotRepository));
    }

    @Test
    void getParkingSpotShouldThrowExceptionWhenLargeParkingSpotHasNoElectricCharger(){
        parkingSpots.add(new SmallParkingSpot(true));
        parkingSpots.add(new SmallParkingSpot(true));
        parkingSpots.add(new MediumParkingSpot(true));
        parkingSpots.add(new LargeParkingSpot(false));

        assertThrows(ParkingSpotNotFound.class, ()-> regularUserParkingStrategy.getParkingSpot(new Truck(PLATE_NUMBER, true),
                parkingLotRepository));
    }

}
