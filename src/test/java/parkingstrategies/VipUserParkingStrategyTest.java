package parkingstrategies;

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

class VipUserParkingStrategyTest {
    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private VipUserParkingStrategy vipUserParkingStrategy;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        vipUserParkingStrategy = new VipUserParkingStrategy();
    }

    @Test
    void getParkingSpotShouldReturnEmptyOptionalWhenThereIsNotAnyAvailableParkingSpotFittingForMotorcycle() {
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsAMediumParkingSpot() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingSpotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsNotAMediumParkingSpotAndThereIsALargeParkingSpot() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingSpotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForCarWhenThereIsAnEmptyMediumParkingSpot() {
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingSpotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForCarWhenThereIsNotAnEmptyMediumParkingSpotAndThereIsAnEmptyLargeParkingSpot() {
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingSpotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForTruckWhenThereIsAnEmptyLargeParkingSpot() {
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", false), parkingSpotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }


    @Test
    void getParkingSpotShouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("",false), parkingSpotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumWithElectricChargerForElectricMotorcycleWhenThereIsNotASmallParkingSpotWithElectricChargerAndThereIsAMediumParkingSpotWithElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(true)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithElectricChargerForElectricCarWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithECForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithECAndThereIsAnEmptyLargeParkingSpotWithEC() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithElectricChargerForTruckWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() {
        //given
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingSpotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyMediumParkingSpotWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyLargeParkingSpotWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingSpotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereOnlyAreEmptyMediumParkingSpotsWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingSpotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForTruckWhenThereOnlyAreLargeParkingSpotsWithoutElectricCharger() {
        when(parkingSpotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingSpotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingSpotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingSpotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingSpotRepository, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingSpotRepository, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingSpotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }
}