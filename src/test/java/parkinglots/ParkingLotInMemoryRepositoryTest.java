package parkinglots;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parkingspots.MediumParkingSpot;
import parkingspots.ParkingSpot;
import parkingspots.SmallParkingSpot;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static parkingspots.ParkingSpotType.MEDIUM;
import static parkingspots.ParkingSpotType.SMALL;

class ParkingLotInMemoryRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void findVehicleShouldReturnSmallParkingSpotWithoutElectricChargerWhenNonElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot smallParkingSpot = new SmallParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", false);
        smallParkingSpot.setVehicle(motorcycle);

        ParkingLotInMemoryRepository parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(Arrays.asList(smallParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber());

        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleShouldReturnSmallParkingSpotWithElectricChargerWhenElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot smallParkingSpot = new SmallParkingSpot(true);
        Vehicle motorcycle = new Motorcycle("", true);
        smallParkingSpot.setVehicle(motorcycle);

        ParkingLotInMemoryRepository parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(new ArrayList<>(Arrays.asList(smallParkingSpot)));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleShouldReturnMediumParkingSpotWithElectricChargerWhenElectricCarIsSetOnThatParkingSpot(){
        ParkingSpot mediumParkingSpot = new MediumParkingSpot(true);
        Vehicle car = new Car("", true);
        mediumParkingSpot.setVehicle(car);

        ParkingLotInMemoryRepository parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(new ArrayList<>(Arrays.asList(mediumParkingSpot)));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(car.getPlateNumber());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(car, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void findVehicleShouldReturnMediumParkingSpotWithoutElectricChargerForElectricMotorcycleWhenElectricMotorcycleIsSetOnThatParkingSpot(){
        ParkingSpot mediumParkingSpot = new MediumParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", true);
        mediumParkingSpot.setVehicle(motorcycle);

        ParkingLotInMemoryRepository parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(new ArrayList<>(Arrays.asList(mediumParkingSpot)));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(motorcycle, parkingSpotOptional.get().getVehicle());
    }

    @Test
    void getEmptyParkingSpotWithoutElectricChargerOfTypeSmallShouldReturnEmptyOptionalIfThereIsNoEmptySmallParkingSpotWithoutElectricCharger(){
        ParkingLotInMemoryRepository parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(new ArrayList<>());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isEmpty());
    }

    @Test
    void getEmptyParkingSpotWithoutElectricChargerOfTypeSmallShouldReturnOptionalOfSmallParkingSpotWithoutElectricChargerIfThereIsAtLeastOneEmptySmallParkingSpotWithoutElectricCharger(){
        ParkingLotInMemoryRepository parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(new ArrayList<>(Arrays.asList(new SmallParkingSpot(false))));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
    }

    @Test
    void getEmptyParkingSpotWithElectricChargerOfTypeSmallShouldReturnEmptyOptionalIfThereIsNoEmptySmallParkingSpotWithElectricCharger(){
        ParkingLotInMemoryRepository parkingLotInMemoryRepository = new ParkingLotInMemoryRepository(new ArrayList<>());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotInMemoryRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isEmpty());
    }


}