import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import parkingstrategies.RegularUserParkingStrategy;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    void shouldThrowParkingSpotNotFoundWhenThereIsNoEmptyParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotOfType(ParkingSpotType.SMALL)).thenThrow(ParkingSpotNotFound.class);
        assertThrows(ParkingSpotNotFound.class,
                ()-> regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository));
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(ParkingSpotType.SMALL);
    }

    @Test
    void shouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotOfType(ParkingSpotType.SMALL)).thenReturn(new SmallParkingSpot(false));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(ParkingSpotType.SMALL);
        assertEquals(ParkingSpotType.SMALL, parkingSpot.getParkingSpotType());
    }

    @Test
    void shouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType.SMALL)).thenReturn(new SmallParkingSpot(true));

        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);

        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(ParkingSpotType.SMALL, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType.SMALL);
    }

    @Test
    void shouldReturnMediumParkingSpotWhenThereIsAnEmptyMediumParkingSpot() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotOfType(ParkingSpotType.MEDIUM)).thenReturn(new MediumParkingSpot(false));
        //when
        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);
        //then
        assertEquals(ParkingSpotType.MEDIUM, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(ParkingSpotType.MEDIUM);
    }

    @Test
    void shouldReturnMediumParkingSpotWithElectricChargerWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType.MEDIUM)).thenReturn(new MediumParkingSpot(true));
        //when
        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(ParkingSpotType.MEDIUM, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType.MEDIUM);
    }

    @Test
    void shouldReturnLargeParkingSpotWhenThereIsAnEmptyLargeParkingSpot() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotOfType(ParkingSpotType.LARGE)).thenReturn(new LargeParkingSpot(false));
        //when
        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Truck("", false), parkingLotRepository);
        //then
        assertEquals(ParkingSpotType.LARGE, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(ParkingSpotType.LARGE);
    }

    @Test
    void shouldReturnLargeParkingSpotWithElectricChargerWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType.LARGE)).thenReturn(new LargeParkingSpot(true));
        //when
        ParkingSpot parkingSpot = regularUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(ParkingSpotType.LARGE, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(ParkingSpotType.LARGE);
    }

}