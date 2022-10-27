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
import tickets.Ticket;
import users.RegularUser;
import users.User;
import users.VIPUser;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;
import vehicles.Vehicle;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;
import static parkingspots.ParkingSpotType.*;

class ParkingLotManagerTest {
    @Mock
    private ParkingLotRepository parkingLotRepository;
    @Mock
    private ParkingStrategyFactory parkingStrategyFactory;

    @InjectMocks
    private ParkingLotManager parkingLotManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parkingLotManager = new ParkingLotManager(parkingLotRepository, parkingStrategyFactory);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithNonElectricMotorcycleWhenParkedOnASmallParkingSpotWithoutElectricChargerBasedOnUserStrategyAndGivenByParkingLotRepository() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle nonElectricMotorcycle = new Motorcycle("", false);
        ParkingSpot smallParkingSpotWithoutElectricCharger = new SmallParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(regularUser, parkingLotRepository)).thenReturn(new RegularUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(SMALL)).thenReturn(Optional.of(smallParkingSpotWithoutElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, nonElectricMotorcycle);

        assertEquals(smallParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(nonElectricMotorcycle, ticket.getVehicle());

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser, parkingLotRepository);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithElectricMotorcycleWhenParkedOnASmallParkingSpotWithElectricChargerBasedOnUserStrategyAndGivenByParkingLotRepository() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle electricMotorcycle = new Motorcycle("", true);
        ParkingSpot smallParkingSpotWithElectricCharger = new SmallParkingSpot(true);

        when(parkingStrategyFactory.getParkingStrategy(regularUser, parkingLotRepository)).thenReturn(new RegularUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(SMALL)).thenReturn(Optional.of(smallParkingSpotWithElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, electricMotorcycle);

        assertEquals(smallParkingSpotWithElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(electricMotorcycle, ticket.getVehicle());

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(SMALL);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser, parkingLotRepository);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithNonElectricCarWhenParkedOnAMediumSmallParkingSpotWithoutElectricChargerBasedOnUserStrategyAndGivenByParkingLotRepository() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle nonElectricCar = new Car("", false);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(regularUser, parkingLotRepository)).thenReturn(new RegularUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, nonElectricCar);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(nonElectricCar, ticket.getVehicle());

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser, parkingLotRepository);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithElectricCarWhenParkedOnAMediumSmallParkingSpotWithoutElectricChargerBasedOnUserStrategyAndGivenByParkingLotRepository() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle electricCar = new Car("", true);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(regularUser, parkingLotRepository)).thenReturn(new RegularUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, electricCar);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(electricCar, ticket.getVehicle());

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser, parkingLotRepository);
    }

    @Test
    void park_ShouldReturnTicketForRegularUserWithElectricCarWhenParkedOnAMediumParkingSpotWithElectricChargerBasedOnUserStrategyAndGivenByParkingLotRepository() throws ParkingSpotNotFound {
        User regularUser = new RegularUser("");
        Vehicle electricCar = new Car("", true);
        ParkingSpot mediumParkingSpotWithElectricCharger = new MediumParkingSpot(true);

        when(parkingStrategyFactory.getParkingStrategy(regularUser, parkingLotRepository)).thenReturn(new RegularUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(mediumParkingSpotWithElectricCharger));
        Ticket ticket = parkingLotManager.park(regularUser, electricCar);

        assertEquals(mediumParkingSpotWithElectricCharger, ticket.getParkingSpot());
        assertEquals(regularUser, ticket.getUser());
        assertEquals(electricCar, ticket.getVehicle());

        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser, parkingLotRepository);
    }
    // cazuri ramase pentru regular user: electric truck     -> large parking spot with electric charger
    //                                    electric truck     -> large parking spot without electric charger
    //                                    not electric truck -> large parking spot without electric charger



    // cazuri pentru vip user:            not electric motorcycle       ->         small parking spot without EC
    //                                    electric motorcycle           ->         small parking spot with EC
    //                                    electric motorcycle           ->         medium parking spot with EC
    //                                    electric motorcycle           ->         large parking spot with EC
    //                                    not electric motorcycle       ->         medium parking spot without EC
    //                                    not electric motorcycle       ->         large parking spot without EC
    //                                    few other cases
    //                                    car & truck cases

    @Test
    void park_ShouldReturnTicketForVipUserWithElectricCarWhenParkedOnAMediumParkingSpotWithoutElectricChargerBasedOnUserStrategyAndGivenByParkingLotRepository() throws ParkingSpotNotFound {
        User vipUser = new VIPUser("");
        Vehicle electricCar = new Car("", true);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(vipUser, parkingLotRepository)).thenReturn(new VipUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(any())).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));

        Ticket ticket = parkingLotManager.park(vipUser, electricCar);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(electricCar, ticket.getVehicle());
        verify(parkingLotRepository, times(0)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(2)).getEmptyParkingSpotWithElectricChargerOfType(any());
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser, parkingLotRepository);
    }

    @Test
    void park_ShouldReturnTicketForVipUserWithElectricMotorcycleWhenParkedOnAMediumParkingSpotWithoutElectricChargerBasedOnUserStrategyAndGivenByParkingLotRepository() throws ParkingSpotNotFound {
        User vipUser = new VIPUser("");
        Vehicle electricMotorcycle = new Motorcycle("", true);
        ParkingSpot mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);

        when(parkingStrategyFactory.getParkingStrategy(vipUser, parkingLotRepository)).thenReturn(new VipUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(any())).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(not(eq(MEDIUM)))).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.of(mediumParkingSpotWithoutElectricCharger));

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);

        assertEquals(mediumParkingSpotWithoutElectricCharger, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(electricMotorcycle, ticket.getVehicle());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(SMALL);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(3)).getEmptyParkingSpotWithElectricChargerOfType(any());
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser, parkingLotRepository);
    }

    @ParameterizedTest
    @MethodSource("generateParkingSpotsWithoutElectricCharger")
    void park_ShouldReturnTicketForVipUserWithNotMotorcycleWhenParkedOnAParkingSpotBasedOnUserStrategyAndGivenByParkingLotRepository(ParkingSpot parkingSpot) throws ParkingSpotNotFound {
        // this test covers:    vip user strategy -> not electric motorcycle -> small parking spot without EC
        //                      vip user strategy -> not electric motorcycle -> medium parking spot without EC
        //                      vip user strategy -> not electric motorcycle -> large parking spot without EC

        User vipUser = new VIPUser("");
        Vehicle notElectricMotorcycle = new Motorcycle("", false);

        when(parkingStrategyFactory.getParkingStrategy(vipUser, parkingLotRepository)).thenReturn(new VipUserParkingStrategy(parkingLotRepository));

        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpot.getParkingSpotType()))
                .thenReturn(Optional.of(parkingSpot));

        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(not(eq(parkingSpot.getParkingSpotType()))))
                .thenReturn(Optional.empty());

        Ticket ticket = parkingLotManager.park(vipUser, notElectricMotorcycle);

        assertEquals(parkingSpot, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(notElectricMotorcycle, ticket.getVehicle());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(parkingSpot.getParkingSpotType());
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser, parkingLotRepository);
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
    void park_ShouldReturnTicketForVipUserWithElectricMotorcycleWhenParkedOnAParkingSpotBasedOnUserStrategyAndGivenByParkingLotRepository(ParkingSpot parkingSpot) throws ParkingSpotNotFound {
        // this test covers:  vip user strategy  ->      electric motorcycle        ->      small parking spot with EC
        //                    vip user strategy  ->      electric motorcycle        ->      medium parking spot with EC
        //                    vip user strategy  ->      electric motorcycle        ->      large parking spot with EC
        User vipUser = new VIPUser("");
        Vehicle electricMotorcycle = new Motorcycle("", true);

        when(parkingStrategyFactory.getParkingStrategy(vipUser, parkingLotRepository)).thenReturn(new VipUserParkingStrategy(parkingLotRepository));

        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(parkingSpot.getParkingSpotType()))
                .thenReturn(Optional.of(parkingSpot));

        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(not(eq(parkingSpot.getParkingSpotType()))))
                .thenReturn(Optional.empty());

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);

        assertEquals(parkingSpot, ticket.getParkingSpot());
        assertEquals(vipUser, ticket.getUser());
        assertEquals(electricMotorcycle, ticket.getVehicle());
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(parkingSpot.getParkingSpotType());
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(vipUser, parkingLotRepository);
    }

    private static Stream<Arguments> generateEachTypeOfParkingSpotWithElectricCharger(){
        return Stream.of(
                Arguments.of(new SmallParkingSpot(true)),
                Arguments.of(new MediumParkingSpot(true)),
                Arguments.of(new LargeParkingSpot(true))
        );
    }

    @Test
    void park_ShouldThrowParkingSpotNotFoundForRegularUserWithElectricCarWhenBasedOnUserStrategyAndParkingLotRepositoryThereIsNoParkingSpotFit(){
        User regularUser = new RegularUser("");
        Vehicle electricCar = new Car("", true);

        when(parkingStrategyFactory.getParkingStrategy(regularUser, parkingLotRepository)).thenReturn(new RegularUserParkingStrategy(parkingLotRepository));
        when(parkingLotRepository.getEmptyParkingSpotWithElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());
        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM)).thenReturn(Optional.empty());

        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(regularUser, electricCar));
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithElectricChargerOfType(MEDIUM);
        verify(parkingLotRepository, times(1)).getEmptyParkingSpotWithoutElectricChargerOfType(MEDIUM);
        verify(parkingStrategyFactory, times(1)).getParkingStrategy(regularUser, parkingLotRepository);
    }

    @Test
    void findVehicleByPlateNumber_ShouldReturnEmptyOptionalWhenAVehicleWithThatPlateNumberIsNotParked(){
        Vehicle car = new Car("", true);
        when(parkingLotRepository.findVehicleByPlateNumber(car.getPlateNumber())).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(car.getPlateNumber());
        assertTrue(parkingSpotOptional.isEmpty());
        verify(parkingLotRepository, times(1)).findVehicleByPlateNumber(car.getPlateNumber());
    }

    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void findVehicleByPlateNumber_ShouldReturnSmallParkingSpotWithElectricChargerWhenElectricMotorcycleIsParkedOnThatParkingSpot(boolean motorcycleIsElectric){
        Vehicle motorcycle = new Motorcycle("", motorcycleIsElectric);
        ParkingSpot smallParkingSpotWithElectricCharger = new SmallParkingSpot(true);
        when(parkingLotRepository.findVehicleByPlateNumber(motorcycle.getPlateNumber())).thenReturn(Optional.of(smallParkingSpotWithElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(motorcycle.getPlateNumber());
        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(smallParkingSpotWithElectricCharger, returnedParkingSpot.get());
        verify(parkingLotRepository, times(1)).findVehicleByPlateNumber(motorcycle.getPlateNumber());
    }

    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void findVehicleByPlateNumber_ShouldReturnMediumParkingSpotWithElectricChargerWhenCarIsParkedOnThatParkingSpot(boolean carIsElectric){
        Vehicle car = new Car("", carIsElectric);
        ParkingSpot mediumParkingSpotWithElectricCharger = new MediumParkingSpot(true);

        when(parkingLotRepository.findVehicleByPlateNumber(car.getPlateNumber())).thenReturn(Optional.of(mediumParkingSpotWithElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(car.getPlateNumber());

        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(mediumParkingSpotWithElectricCharger, returnedParkingSpot.get());
        verify(parkingLotRepository, times(1)).findVehicleByPlateNumber(car.getPlateNumber());
    }

    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void findVehicleByPlateNumber_ShouldReturnLargeParkingSpotWithElectricChargerWhenTruckIsParkedOnThatParkingSpot(boolean truckIsElectric){
        Vehicle truck = new Truck("", truckIsElectric);
        ParkingSpot largeParkingSpotWithElectricCharger = new LargeParkingSpot(true);

        when(parkingLotRepository.findVehicleByPlateNumber(truck.getPlateNumber())).thenReturn(Optional.of(largeParkingSpotWithElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(truck.getPlateNumber());

        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(largeParkingSpotWithElectricCharger, returnedParkingSpot.get());
        verify(parkingLotRepository, times(1)).findVehicleByPlateNumber(truck.getPlateNumber());
    }

    @Test
    void findVehicleByPlateNumber_ShouldReturnLargeParkingSpotWithoutElectricChargerForElectricCarWhenThatCarIsParkedOnThatParkingSpot(){
        Vehicle electricCar = new Car("", true);
        ParkingSpot largeParkingSpotWithoutElectricCharger = new LargeParkingSpot(false);

        when(parkingLotRepository.findVehicleByPlateNumber(electricCar.getPlateNumber())).thenReturn(Optional.of(largeParkingSpotWithoutElectricCharger));
        Optional<ParkingSpot> returnedParkingSpot = parkingLotManager.findVehicleByPlateNumber(electricCar.getPlateNumber());

        assertTrue(returnedParkingSpot.isPresent());
        assertEquals(largeParkingSpotWithoutElectricCharger, returnedParkingSpot.get());
        verify(parkingLotRepository, times(1)).findVehicleByPlateNumber(electricCar.getPlateNumber());
    }
}