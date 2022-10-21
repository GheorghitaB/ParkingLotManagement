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
    void shouldThrowParkingSpotNotFoundForMotorcycleWhenThereIsNoEmptySmallParkingSpot(){
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenThrow(ParkingSpotNotFound.class);
        assertThrows(ParkingSpotNotFound.class,
                ()-> regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository));
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));

        Optional<ParkingSpot> parkingSpot = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        assertEquals(SMALL, parkingSpot.get().getParkingSpotType());
    }
    @Test
    void shouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));

        Optional<ParkingSpot> parkingSpot = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);

        assertEquals(true, parkingSpot.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWhenThereIsAnEmptyMediumParkingSpot() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);
        //then
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnMediumParkingSpotWithElectricChargerWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeParkingSpotWhenThereIsAnEmptyLargeParkingSpot() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", false), parkingLotRepository);
        //then
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnLargeParkingSpotWithElectricChargerWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void shouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsAnEmptySmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpot = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertEquals(false, parkingSpot.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithElectricChargerAndThereIsAnEmptyMediumParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        assertEquals(false, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void shouldReturnLargeMediumParkingSpotWithoutElectricChargerForElectricTruckWhenThereIsNotAnEmptyLargeParkingSpotWithElectricChargerAndThereIsAnEmptyLargeParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = regularUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        assertEquals(false, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void shouldThrowParkingSpotNotFoundExceptionForElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithElectricChargerAndThereIsNotAnEmptySmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        assertThrows(ParkingSpotNotFound.class, () -> regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository));
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }
}