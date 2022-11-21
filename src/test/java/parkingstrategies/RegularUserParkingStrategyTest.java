package parkingstrategies;

import models.parkings.spots.LargeParkingSpot;
import models.parkings.spots.MediumParkingSpot;
import models.parkings.spots.ParkingSpot;
import models.parkings.spots.SmallParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import services.parkings.spots.ParkingSpotService;
import models.vehicles.Car;
import models.vehicles.Motorcycle;
import models.vehicles.Truck;
import services.parkings.strategies.RegularUserParkingStrategy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static models.parkings.spots.ParkingSpotType.*;

public class RegularUserParkingStrategyTest{
    @Mock
    private ParkingSpotService parkingSpotService;
    @InjectMocks
    private RegularUserParkingStrategy regularUserParkingStrategy;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
        regularUserParkingStrategy = new RegularUserParkingStrategy();
    }

    @Test
    void shouldReturnEmptyOptionalForMotorcycleWhenThereIsNoEmptySmallParkingSpot(){
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional =  regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingSpotService);
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() {
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingSpotService);

        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
    }
    @Test
    void shouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));

        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWhenThereIsAnEmptyMediumParkingSpot() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", false), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnMediumParkingSpotWithElectricChargerWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assert (parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeParkingSpotWhenThereIsAnEmptyLargeParkingSpot() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", false), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnLargeParkingSpotWithElectricChargerWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithElectricChargerAndThereIsAnEmptyMediumParkingSpotWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeMediumParkingSpotWithoutElectricChargerForElectricTruckWhenThereIsNotAnEmptyLargeParkingSpotWithElectricChargerAndThereIsAnEmptyLargeParkingSpotWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnEmptyOptionalForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsNotAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }
}