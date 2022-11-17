package parkinglots;

import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parkingspots.*;
import parkingstrategies.ParkingStrategyFactory;
import parkingstrategies.RegularUserParkingStrategy;
import parkingstrategies.VipUserParkingStrategy;
import repositories.ParkingSpotService;
import tickets.Ticket;
import users.RegularUser;
import users.User;
import users.VIPUser;
import vehicles.*;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingLotManagerTest {
    @Mock
    private ParkingSpotService parkingSpotService;
    @Mock
    private ParkingStrategyFactory parkingStrategyFactory;
    @Mock
    private RegularUserParkingStrategy regularUserParkingStrategy;
    @Mock
    private VipUserParkingStrategy vipUserParkingStrategy;

    @InjectMocks
    private ParkingLotManager parkingLotManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parkingLotManager = new ParkingLotManager(parkingSpotService, parkingStrategyFactory);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithNotElectricMotorcycleWhenParkedOnASmallParkingSpotWithoutElectricChargerBasedOnUserStrategy() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle notElectricMotorcycle = new Motorcycle("", false);
        ParkingSpot smallParkingSpotWithoutElectricCharger = new SmallParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(regularUser)).thenReturn(regularUserParkingStrategy);
        when(regularUserParkingStrategy.getParkingSpot(notElectricMotorcycle, parkingSpotService)).thenReturn(Optional.of(smallParkingSpotWithoutElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, notElectricMotorcycle);

        assertEquals(smallParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(notElectricMotorcycle, ticket.getVehicle());

        verify(regularUserParkingStrategy, times(1)).getParkingSpot(notElectricMotorcycle, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithElectricMotorcycleWhenParkedOnASmallParkingSpotWithElectricChargerBasedOnUserStrategy() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle electricMotorcycle = new Motorcycle("", true);
        ParkingSpot smallParkingSpotWithElectricCharger = new SmallParkingSpot(true);

        when(parkingStrategyFactory.getParkingStrategy(regularUser)).thenReturn(regularUserParkingStrategy);
        when(regularUserParkingStrategy.getParkingSpot(electricMotorcycle, parkingSpotService)).thenReturn(Optional.of(smallParkingSpotWithElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, electricMotorcycle);

        assertEquals(smallParkingSpotWithElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(electricMotorcycle, ticket.getVehicle());

        verify(regularUserParkingStrategy, times(1)).getParkingSpot(electricMotorcycle, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithNotElectricCarWhenParkedOnAMediumSmallParkingSpotWithoutElectricChargerBasedOnUserStrategy() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle notElectricCar = new Car("", false);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(regularUser)).thenReturn(regularUserParkingStrategy);
        when(regularUserParkingStrategy.getParkingSpot(notElectricCar, parkingSpotService)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, notElectricCar);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(notElectricCar, ticket.getVehicle());

        verify(regularUserParkingStrategy, times(1)).getParkingSpot(notElectricCar, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithElectricCarWhenParkedOnAMediumSmallParkingSpotWithoutElectricChargerBasedOnUserStrategy() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle electricCar = new Car("", true);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(regularUser)).thenReturn(regularUserParkingStrategy);
        when(regularUserParkingStrategy.getParkingSpot(electricCar, parkingSpotService)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, electricCar);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(electricCar, ticket.getVehicle());

        verify(regularUserParkingStrategy, times(1)).getParkingSpot(electricCar, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithElectricCarWhenParkedOnAMediumParkingSpotWithElectricChargerBasedOnUserStrategy() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle electricCar = new Car("", true);
        ParkingSpot mediumParkingSpotWithElectricCharger = new MediumParkingSpot(true);

        when(parkingStrategyFactory.getParkingStrategy(regularUser)).thenReturn(regularUserParkingStrategy);
        when(regularUserParkingStrategy.getParkingSpot(electricCar, parkingSpotService)).thenReturn(Optional.of(mediumParkingSpotWithElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, electricCar);

        assertEquals(mediumParkingSpotWithElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(electricCar, ticket.getVehicle());

        verify(regularUserParkingStrategy, times(1)).getParkingSpot(electricCar, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser);
    }

    @Test
    void park_ShouldReturnTicketForVipUserWithElectricCarWhenParkedOnAMediumParkingSpotWithoutElectricChargerBasedOnUserStrategy() throws ParkingSpotNotFound {
        User vipUser = new VIPUser("");
        Vehicle electricCar = new Car("", true);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(vipUser)).thenReturn(vipUserParkingStrategy);
        when(vipUserParkingStrategy.getParkingSpot(electricCar, parkingSpotService)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));

        Ticket ticket = parkingLotManager.park(vipUser, electricCar);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(electricCar, ticket.getVehicle());

        verify(vipUserParkingStrategy, times(1)).getParkingSpot(electricCar, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser);
    }

    @Test
    void park_ShouldReturnTicketForVipUserWithElectricMotorcycleWhenParkedOnAMediumParkingSpotWithoutElectricChargerBasedOnUserStrategy() throws ParkingSpotNotFound {
        User vipUser = new VIPUser("");
        Vehicle electricMotorcycle = new Motorcycle("", true);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(vipUser)).thenReturn(vipUserParkingStrategy);
        when(vipUserParkingStrategy.getParkingSpot(electricMotorcycle, parkingSpotService)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(electricMotorcycle, ticket.getVehicle());

        verify(vipUserParkingStrategy, times(1)).getParkingSpot(electricMotorcycle, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser);
    }

    @ParameterizedTest
    @MethodSource("generateParkingSpotsWithoutElectricCharger")
    void park_ShouldReturnTicketForVipUserWithNotMotorcycleWhenParkedOnAParkingSpotBasedOnUserStrategy(ParkingSpot parkingSpot) throws ParkingSpotNotFound {
        // this test covers:    vip user strategy -> not electric motorcycle -> small parking spot without EC
        //                      vip user strategy -> not electric motorcycle -> medium parking spot without EC
        //                      vip user strategy -> not electric motorcycle -> large parking spot without EC

        User vipUser = new VIPUser("");
        Vehicle notElectricMotorcycle = new Motorcycle("", false);

        when(parkingStrategyFactory.getParkingStrategy(vipUser)).thenReturn(vipUserParkingStrategy);
        when(vipUserParkingStrategy.getParkingSpot(notElectricMotorcycle, parkingSpotService)).thenReturn(Optional.of(parkingSpot));
        Ticket ticket = parkingLotManager.park(vipUser, notElectricMotorcycle);

        assertEquals(parkingSpot, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(notElectricMotorcycle, ticket.getVehicle());

        verify(vipUserParkingStrategy, times(1)).getParkingSpot(notElectricMotorcycle, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser);
    }

    private static Stream<Arguments> generateParkingSpotsWithoutElectricCharger(){
        return Stream.of(
                Arguments.of(new SmallParkingSpot(false)),
                Arguments.of(new MediumParkingSpot(false)),
                Arguments.of(new LargeParkingSpot(false))
        );
    }

    @ParameterizedTest
    @MethodSource("generateEachTypeOfParkingSpotWithElectricCharger")
    void park_ShouldReturnTicketForVipUserWithElectricMotorcycleWhenParkedOnAParkingSpotBasedOnUserStrategy(ParkingSpot parkingSpot) throws ParkingSpotNotFound {
        // this test covers:  vip user strategy  ->      electric motorcycle        ->      small parking spot with EC
        //                    vip user strategy  ->      electric motorcycle        ->      medium parking spot with EC
        //                    vip user strategy  ->      electric motorcycle        ->      large parking spot with EC
        User vipUser = new VIPUser("");
        Vehicle electricMotorcycle = new Motorcycle("", true);

        when(parkingStrategyFactory.getParkingStrategy(vipUser)).thenReturn(vipUserParkingStrategy);
        when(vipUserParkingStrategy.getParkingSpot(electricMotorcycle, parkingSpotService)).thenReturn(Optional.of(parkingSpot));
        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);

        assertEquals(parkingSpot, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(electricMotorcycle, ticket.getVehicle());

        verify(vipUserParkingStrategy, times(1)).getParkingSpot(electricMotorcycle, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser);
    }

    private static Stream<Arguments> generateEachTypeOfParkingSpotWithElectricCharger(){
        return Stream.of(
                Arguments.of(new SmallParkingSpot(true)),
                Arguments.of(new MediumParkingSpot(true)),
                Arguments.of(new LargeParkingSpot(true))
        );
    }

    @Test
    void park_ShouldThrowParkingSpotNotFoundForRegularUserWithElectricCarWhenBasedOnUserStrategyThereIsNoParkingSpotFit(){
        User regularUser = new RegularUser("");
        Vehicle electricCar = new Car("", true);

        when(parkingStrategyFactory.getParkingStrategy(regularUser)).thenReturn(regularUserParkingStrategy);
        when(regularUserParkingStrategy.getParkingSpot(electricCar, parkingSpotService)).thenReturn(Optional.empty());

        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(regularUser, electricCar));

        verify(regularUserParkingStrategy, times(1)).getParkingSpot(electricCar, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser);
    }

    @Test
    void park_ShouldThrowParkingSpotNotFoundForVipUserWithNotElectricTruckWhenBasedOnUserStrategyThereIsNoParkingSpotFit(){
        User vipUser = new VIPUser("");
        Vehicle notElectricTruck = new Truck("", false);

        when(parkingStrategyFactory.getParkingStrategy(vipUser)).thenReturn(regularUserParkingStrategy);
        when(regularUserParkingStrategy.getParkingSpot(notElectricTruck, parkingSpotService)).thenReturn(Optional.empty());

        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(vipUser, notElectricTruck));

        verify(regularUserParkingStrategy, times(1)).getParkingSpot(notElectricTruck, parkingSpotService);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser);
    }

    @Test
    void findVehicleByPlateNumber_ShouldReturnEmptyOptionalWhenAVehicleWithThatPlateNumberIsNotParked(){
        Vehicle car = new Car("", true);
        when(parkingSpotService.findVehicleByPlateNumber(car.getPlateNumber())).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(car.getPlateNumber());
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingSpotService, times(1)).findVehicleByPlateNumber(car.getPlateNumber());
    }

    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void findVehicleByPlateNumber_ShouldReturnSmallParkingSpotWithElectricChargerWhenElectricMotorcycleIsParkedOnThatParkingSpot(boolean motorcycleIsElectric){
        Vehicle motorcycle = new Motorcycle("", motorcycleIsElectric);
        ParkingSpot smallParkingSpotWithElectricCharger = new SmallParkingSpot(true);
        when(parkingSpotService.findVehicleByPlateNumber(motorcycle.getPlateNumber())).thenReturn(Optional.of(smallParkingSpotWithElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(motorcycle.getPlateNumber());
        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(smallParkingSpotWithElectricCharger, returnedParkingSpot.get());
        verify(parkingSpotService, times(1)).findVehicleByPlateNumber(motorcycle.getPlateNumber());
    }

    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void findVehicleByPlateNumber_ShouldReturnMediumParkingSpotWithElectricChargerWhenCarIsParkedOnThatParkingSpot(boolean carIsElectric){
        Vehicle car = new Car("", carIsElectric);
        ParkingSpot mediumParkingSpotWithElectricCharger = new MediumParkingSpot(true);

        when(parkingSpotService.findVehicleByPlateNumber(car.getPlateNumber())).thenReturn(Optional.of(mediumParkingSpotWithElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(car.getPlateNumber());

        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(mediumParkingSpotWithElectricCharger, returnedParkingSpot.get());
        verify(parkingSpotService, times(1)).findVehicleByPlateNumber(car.getPlateNumber());
    }

    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void findVehicleByPlateNumber_ShouldReturnLargeParkingSpotWithElectricChargerWhenTruckIsParkedOnThatParkingSpot(boolean truckIsElectric){
        Vehicle truck = new Truck("", truckIsElectric);
        ParkingSpot largeParkingSpotWithElectricCharger = new LargeParkingSpot(true);

        when(parkingSpotService.findVehicleByPlateNumber(truck.getPlateNumber())).thenReturn(Optional.of(largeParkingSpotWithElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(truck.getPlateNumber());

        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(largeParkingSpotWithElectricCharger, returnedParkingSpot.get());
        verify(parkingSpotService, times(1)).findVehicleByPlateNumber(truck.getPlateNumber());
    }

    @Test
    void findVehicleByPlateNumber_ShouldReturnLargeParkingSpotWithoutElectricChargerForElectricCarWhenThatCarIsParkedOnThatParkingSpot(){
        Vehicle electricCar = new Car("", true);
        ParkingSpot largeParkingSpotWithoutElectricCharger = new LargeParkingSpot(false);

        when(parkingSpotService.findVehicleByPlateNumber(electricCar.getPlateNumber())).thenReturn(Optional.of(largeParkingSpotWithoutElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(electricCar.getPlateNumber());

        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(largeParkingSpotWithoutElectricCharger, returnedParkingSpot.get());
        verify(parkingSpotService, times(1)).findVehicleByPlateNumber(electricCar.getPlateNumber());
    }
}