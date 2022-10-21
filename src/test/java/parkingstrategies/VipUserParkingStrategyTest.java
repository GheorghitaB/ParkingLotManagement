package parkingstrategies;

import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import parkinglots.ParkingLotRepository;
import parkingspots.*;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;

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
    void getParkingSpotShouldReturnParkingSpotNotFoundWhenThereIsNotAnyAvailableParkingSpotFittingForMotorcycle() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotOfType(SMALL)).thenThrow(ParkingSpotNotFound.class);
        assertThrows(ParkingSpotNotFound.class, ()->parkingLotRepository.getEmptyParkingSpotOfType(SMALL));
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsAMediumParkingSpot() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotOfType(SMALL)).thenReturn(new MediumParkingSpot(false));
        //when
        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);
        //then
        assertEquals(MEDIUM, parkingSpot.getParkingSpotType());
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsNotAMediumParkingSpotAndThereIsALargeParkingSpot()
            throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotOfType(SMALL)).thenReturn(new LargeParkingSpot(false));
        //when
        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);
        //then
        assertEquals(LARGE, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForCarWhenThereIsAnEmptyMediumParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotOfType(MEDIUM)).thenReturn(new MediumParkingSpot(false));

        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);

        assertEquals(MEDIUM, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForCarWhenThereIsNotAnEmptyMediumParkingSpotAndThereIsAnEmptyLargeParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotOfType(MEDIUM)).thenReturn(new LargeParkingSpot(false));

        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);

        assertEquals(LARGE, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForTruckWhenThereIsAnEmptyLargeParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotOfType(LARGE)).thenReturn(new LargeParkingSpot(false));

        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Truck("", false), parkingLotRepository);

        assertEquals(LARGE, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(LARGE);
    }


    @Test
    void getParkingSpotShouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() throws ParkingSpotNotFound {
        //given
        when(vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository))
                .thenReturn(new SmallParkingSpot(false));
        //when
        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("",false), parkingLotRepository);
        //then
        assertEquals(SMALL, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(new SmallParkingSpot(true));
        //when
        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(SMALL, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumWithElectricChargerForElectricMotorcycleWhenThereIsNotASmallParkingSpotWithElectricChargerAndThereIsAMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(new MediumParkingSpot(true));

        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);

        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(MEDIUM, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithElectricChargerForElectricCarWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(new MediumParkingSpot(true));
        //when
        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(MEDIUM, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithECForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithECAndThereIsAnEmptyLargeParkingSpotWithEC() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(new LargeParkingSpot(true));
        //when
        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(LARGE, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithElectricChargerForTruckWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(new LargeParkingSpot(true));
        //when
        ParkingSpot parkingSpot = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.hasElectricCharger());
        assertEquals(LARGE, parkingSpot.getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }
}