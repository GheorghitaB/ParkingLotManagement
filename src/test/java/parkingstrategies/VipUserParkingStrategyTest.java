package parkingstrategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repositories.ParkingSpotService;
import parkingspots.*;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static parkingspots.ParkingSpotType.*;

class VipUserParkingStrategyTest {
    @Mock
    private ParkingSpotService parkingSpotService;

    @InjectMocks
    private VipUserParkingStrategy vipUserParkingStrategy;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        vipUserParkingStrategy = new VipUserParkingStrategy();
    }

    @Test
    void getParkingSpotShouldReturnEmptyOptionalWhenThereIsNotAnyAvailableParkingSpotFittingForMotorcycle() {
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsAMediumParkingSpot() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsNotAMediumParkingSpotAndThereIsALargeParkingSpot() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForCarWhenThereIsAnEmptyMediumParkingSpot() {
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingSpotService);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForCarWhenThereIsNotAnEmptyMediumParkingSpotAndThereIsAnEmptyLargeParkingSpot() {
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingSpotService);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForTruckWhenThereIsAnEmptyLargeParkingSpot() {
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", false), parkingSpotService);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }


    @Test
    void getParkingSpotShouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("",false), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumWithElectricChargerForElectricMotorcycleWhenThereIsNotASmallParkingSpotWithElectricChargerAndThereIsAMediumParkingSpotWithElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(true)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithElectricChargerForElectricCarWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithECForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithECAndThereIsAnEmptyLargeParkingSpotWithEC() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithElectricChargerForTruckWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingSpotService);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyMediumParkingSpotWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyLargeParkingSpotWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereOnlyAreEmptyMediumParkingSpotsWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForTruckWhenThereOnlyAreLargeParkingSpotsWithoutElectricCharger() {
        when(parkingSpotService.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotService.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingSpotService);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotService, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(0)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotService, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingSpotService, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingSpotService, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }
}