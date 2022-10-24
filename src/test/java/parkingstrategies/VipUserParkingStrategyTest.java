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

class VipUserParkingStrategyTest {

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private VipUserParkingStrategy vipUserParkingStrategy;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        vipUserParkingStrategy = new VipUserParkingStrategy();
    }

    @Test
    void getParkingSpotShouldReturnParkingSpotNotFoundWhenThereIsNotAnyAvailableParkingSpotFittingForMotorcycle() {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenThrow(ParkingSpotNotFound.class);
        assertThrows(ParkingSpotNotFound.class, () -> parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL));
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsAMediumParkingSpot() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsNotAMediumParkingSpotAndThereIsALargeParkingSpot() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForCarWhenThereIsAnEmptyMediumParkingSpot() {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForCarWhenThereIsNotAnEmptyMediumParkingSpotAndThereIsAnEmptyLargeParkingSpot() {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForTruckWhenThereIsAnEmptyLargeParkingSpot() {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", false), parkingLotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }


    @Test
    void getParkingSpotShouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("",false), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumWithElectricChargerForElectricMotorcycleWhenThereIsNotASmallParkingSpotWithElectricChargerAndThereIsAMediumParkingSpotWithElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(true)));

        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);

        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithElectricChargerForElectricCarWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithECForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithECAndThereIsAnEmptyLargeParkingSpotWithEC() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithElectricChargerForTruckWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        //then
        assertTrue(parkingSpotOptional.isPresent());
        assertTrue(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsAnEmptySmallParkingSpotWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyMediumParkingSpotWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyLargeParkingSpotWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereOnlyAreEmptyMediumParkingSpotsWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForTruckWhenThereOnlyAreLargeParkingSpotsWithoutElectricCharger() {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        assertTrue(parkingSpotOptional.isPresent());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }
}