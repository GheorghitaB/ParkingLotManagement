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
    void getParkingSpotShouldReturnMediumParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsAMediumParkingSpot() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);
        //then
        assertEquals(MEDIUM, parkingSpot.get().getParkingSpotType());
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForMotorcycleWhenThereIsNotASmallParkingSpotAndThereIsNotAMediumParkingSpotAndThereIsALargeParkingSpot()
            throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", false), parkingLotRepository);
        //then
        assertEquals(LARGE, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotForCarWhenThereIsAnEmptyMediumParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));

        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);

        assertEquals(MEDIUM, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForCarWhenThereIsNotAnEmptyMediumParkingSpotAndThereIsAnEmptyLargeParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", false), parkingLotRepository);

        assertEquals(LARGE, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotForTruckWhenThereIsAnEmptyLargeParkingSpot() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));

        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Truck("", false), parkingLotRepository);

        assertEquals(LARGE, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }


    @Test
    void getParkingSpotShouldReturnSmallParkingSpotForMotorcycleWhenThereIsAnEmptySmallParkingSpot() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        //when
        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("",false), parkingLotRepository);
        //then
        assertEquals(SMALL, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithElectricChargerForElectricMotorcycleWhenThereIsAnEmptySmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.get().hasElectricCharger());
        assertEquals(SMALL, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumWithElectricChargerForElectricMotorcycleWhenThereIsNotASmallParkingSpotWithElectricChargerAndThereIsAMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(new MediumParkingSpot(true)));

        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);

        assertEquals(true, parkingSpot.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithElectricChargerForElectricCarWhenThereIsAnEmptyMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithECForElectricCarWhenThereIsNotAnEmptyMediumParkingSpotWithECAndThereIsAnEmptyLargeParkingSpotWithEC() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithElectricChargerForTruckWhenThereIsAnEmptyLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        //given
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(true)));
        //when
        Optional<ParkingSpot> parkingSpot = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        //then
        assertEquals(true, parkingSpot.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpot.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnSmallParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsAnEmptySmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(new SmallParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertEquals(SMALL, parkingSpotOptional.get().getParkingSpotType());
        assertEquals(false, parkingSpotOptional.get().hasElectricCharger());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyMediumParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertEquals(false, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForElectricMotorcycleWhenThereOnlyIsEmptyLargeParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Motorcycle("", true), parkingLotRepository);
        assertEquals(false, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }

    @Test
    void getParkingSpotShouldReturnMediumParkingSpotWithoutElectricChargerForElectricCarWhenThereOnlyAreEmptyMediumParkingSpotsWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(new MediumParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Car("", true), parkingLotRepository);
        assertEquals(false, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(MEDIUM, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
    }

    @Test
    void getParkingSpotShouldReturnLargeParkingSpotWithoutElectricChargerForTruckWhenThereOnlyAreLargeParkingSpotsWithoutElectricCharger() throws ParkingSpotNotFound {
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(LARGE)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(LARGE)).thenReturn(Optional.of(new LargeParkingSpot(false)));
        Optional<ParkingSpot> parkingSpotOptional = vipUserParkingStrategy.getParkingSpot(new Truck("", true), parkingLotRepository);
        assertEquals(false, parkingSpotOptional.get().hasElectricCharger());
        assertEquals(LARGE, parkingSpotOptional.get().getParkingSpotType());
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(LARGE);
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(LARGE);
    }
}