package parkinglots;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parkingspots.MediumParkingSpot;
import parkingspots.ParkingSpot;
import parkingspots.SmallParkingSpot;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static parkingspots.ParkingSpotType.MEDIUM;
import static parkingspots.ParkingSpotType.SMALL;

class ParkingLotInMemoryRepositoryTest {
    @Mock
    private List<ParkingSpot> parkingSpots;
    @InjectMocks
    private ParkingLotInMemoryRepository parkingLotInMemoryRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(parkingSpots);
    }

    @Test
    void findVehicleShouldReturnSmallParkingSpotWithoutElectricChargerWhenNonElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot smallParkingSpot = new SmallParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", false);
        smallParkingSpot.setVehicle(motorcycle);

        when(parkingSpots.stream()).thenReturn(Stream.of(smallParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicle(motorcycle);
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleShouldReturnSmallParkingSpotWithElectricChargerWhenElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot smallParkingSpot = new SmallParkingSpot(true);
        Vehicle motorcycle = new Motorcycle("", true);
        smallParkingSpot.setVehicle(motorcycle);

        when(parkingSpots.stream()).thenReturn(Stream.of(smallParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicle(motorcycle);
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleShouldReturnMediumParkingSpotWithElectricChargerWhenElectricCarIsSetOnThatParkingSpot(){
        ParkingSpot mediumParkingSpot = new MediumParkingSpot(true);
        Vehicle car = new Car("", true);
        mediumParkingSpot.setVehicle(car);

        when(parkingSpots.stream()).thenReturn(Stream.of(mediumParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicle(car);
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(car, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleShouldReturnMediumParkingSpotWithoutElectricChargerForElectricMotorcycleWhenElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot mediumParkingSpot = new MediumParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", true);
        mediumParkingSpot.setVehicle(motorcycle);

        when(parkingSpots.stream()).thenReturn(Stream.of(mediumParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicle(motorcycle);
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void getEmptyParkingSpotWithoutElectricChargerOfTypeSmallShouldReturnEmptyOptionalIfThereIsNoEmptySmallParkingSpotWithoutElectricCharger(){
        when(parkingSpots.stream()).thenReturn(Stream.empty());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isEmpty());
    }

    @Test
    void getEmptyParkingSpotWithoutElectricChargerOfTypeSmallShouldReturnOptionalOfSmallParkingSpotWithoutElectricChargerIfThereIsAtLeastOneEmptySmallParkingSpotWithoutElectricCharger(){
        when(parkingSpots.stream()).thenReturn(Stream.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
    }

    @Test
    void getEmptyParkingSpotWithElectricChargerOfTypeSmallShouldReturnEmptyOptionalIfThereIsNoEmptySmallParkingSpotWithElectricCharger(){
        when(parkingSpots.stream()).thenReturn(Stream.empty());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isEmpty());
    }
}