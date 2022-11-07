package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.ParkingSpotRepository;
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
    private ParkingSpotRepository parkingSpotRepository;
    @InjectMocks
    private RegularUserParkingStrategy regularUserParkingStrategy;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
        regularUserParkingStrategy = new RegularUserParkingStrategy(parkingSpotRepository);
    }

    @Test
    void shouldThrowParkingSpotNotFoundExceptionForMotorcycleWhenThereIsNoEmptySmallParkingSpot(){
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenThrow(ParkingSpotNotFound.class);
        assertThrows(ParkingSpotNotFound.class, ()-> regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false)));
    }

    @Test
    void shouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() {
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false));

        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
    }
    @Test
    void shouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));

        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true));

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWhenThereIsAnEmptyMediumParkingSpot() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", false));
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnMediumParkingSpotWithElectricChargerWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true));
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assert (parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeParkingSpotWhenThereIsAnEmptyLargeParkingSpot() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", false));
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnLargeParkingSpotWithElectricChargerWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true));
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true));
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithElectricChargerAndThereIsAnEmptyMediumParkingSpotWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true));
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeMediumParkingSpotWithoutElectricChargerForElectricTruckWhenThereIsNotAnEmptyLargeParkingSpotWithElectricChargerAndThereIsAnEmptyLargeParkingSpotWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true));
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnEmptyOptionalForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsNotAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true));
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }
}