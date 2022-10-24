package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static parkingspots.ParkingSpotType.*;

public class RegularUserParkingStrategyTest{
    @Mock
    private ParkingLotRepository parkingLotRepository;
    @InjectMocks
    private RegularUserParkingStrategy regularUserParkingStrategy;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
        regularUserParkingStrategy = new RegularUserParkingStrategy();
    }

    @Test
    void shouldThrowParkingSpotNotFoundExceptionForMotorcycleWhenThereIsNoEmptySmallParkingSpot(){
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenThrow(ParkingSpotNotFound.class);
        assertThrows(ParkingSpotNotFound.class,
                ()-> regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository));
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
    }
    @Test
    void shouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));

        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWhenThereIsAnEmptyMediumParkingSpot() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnMediumParkingSpotWithElectricChargerWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assert (parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeParkingSpotWhenThereIsAnEmptyLargeParkingSpot() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", false), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnLargeParkingSpotWithElectricChargerWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithElectricChargerAndThereIsAnEmptyMediumParkingSpotWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeMediumParkingSpotWithoutElectricChargerForElectricTruckWhenThereIsNotAnEmptyLargeParkingSpotWithElectricChargerAndThereIsAnEmptyLargeParkingSpotWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnEmptyOptionalForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsNotAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }
}