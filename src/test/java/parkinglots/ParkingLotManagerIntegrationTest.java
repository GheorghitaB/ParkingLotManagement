package parkinglots;

import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import parkingspots.*;
import parkingstrategies.ParkingStrategyFactory;
import repositories.ParkingSpotInMemoryService;
import repositories.ParkingSpotService;
import tickets.Ticket;
import users.RegularUser;
import users.User;
import users.VIPUser;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Truck;
import vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotManagerIntegrationTest {
    private ParkingLotManager parkingLotManager;
    private User regularUser;
    private User vipUser;
    private Vehicle nonElectricMotorcycle;
    private Vehicle electricMotorcycle;
    private Vehicle nonElectricCar;
    private Vehicle electricCar;
    private Vehicle nonElectricTruck;
    private Vehicle electricTruck;
    private List<ParkingSpot> parkingSpotList;
    private ParkingSpot smallParkingSpotWithoutElectricCharger;
    private ParkingSpot smallParkingSpotWithElectricCharger;
    private ParkingSpot mediumParkingSpotWithoutElectricCharger;
    private ParkingSpot mediumParkingSpotWithElectricCharger;
    private ParkingSpot largeParkingSpotWithoutElectricCharger;
    private ParkingSpot largeParkingSpotWithElectricCharger;


    @BeforeEach
    void setUp() {
        initParkingSpots();
        populateParkingSpotList();
        initUsers();
        initVehicles();
        ParkingSpotService parkingSpotService = new ParkingSpotInMemoryService(parkingSpotList);
        parkingLotManager = new ParkingLotManager(parkingSpotService, ParkingStrategyFactory.getParkingStrategyInstance());
    }

    private void initParkingSpots(){
        smallParkingSpotWithoutElectricCharger = new SmallParkingSpot(false);
        smallParkingSpotWithElectricCharger = new SmallParkingSpot(true);
        mediumParkingSpotWithoutElectricCharger = new MediumParkingSpot(false);
        mediumParkingSpotWithElectricCharger = new MediumParkingSpot(true);
        largeParkingSpotWithoutElectricCharger = new LargeParkingSpot(false);
        largeParkingSpotWithElectricCharger = new LargeParkingSpot(true);
    }

    private void populateParkingSpotList(){
        parkingSpotList = new ArrayList<>();
        parkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        parkingSpotList.add(smallParkingSpotWithElectricCharger);
        parkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        parkingSpotList.add(mediumParkingSpotWithElectricCharger);
        parkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingSpotList.add(largeParkingSpotWithElectricCharger);
    }

    private void initUsers(){
        regularUser = new RegularUser("RegularUser");
        vipUser = new VIPUser("VipUser");
    }

    private void initVehicles(){
        nonElectricMotorcycle = new Motorcycle("", false);
        electricMotorcycle = new Motorcycle("", true);
        nonElectricCar = new Car("", false);
        electricCar = new Car("", true);
        nonElectricTruck = new Truck("", false);
        electricTruck = new Truck("", true);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedANonElectricMotorcycleOnAGivenSmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(regularUser, nonElectricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, nonElectricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedAnElectricMotorcycleOnAGivenSmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(regularUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedANonElectricCarOnAGivenMediumParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(regularUser, nonElectricCar);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, nonElectricCar);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedAnElectricCarOnAGivenMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(regularUser, electricCar);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, electricCar);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedANonElectricTruckOnAGivenLargeParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(regularUser, nonElectricTruck);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, largeParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, nonElectricTruck);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedAnElectricTruckOnAGivenLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(regularUser, electricTruck);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, largeParkingSpotWithElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, electricTruck);
    }

    @Test
    void parkShouldThrowParkingSpotNotFoundExceptionWhenRegularUserWantsToParkANonElectricMotorcycleWhenThereIsNotAnEmptySmallParkingSpotWithoutElectricCharger(){
        parkingSpotList.remove(smallParkingSpotWithoutElectricCharger);
        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(regularUser, nonElectricMotorcycle));
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedANonElectricMotorcycleOnAGivenSmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(vipUser, nonElectricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, nonElectricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenSmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(smallParkingSpotWithElectricCharger, ticketGivenParkingSpot);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, largeParkingSpotWithElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenSmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenMediumParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenLargeParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());

        Ticket ticket = parkingLotManager.park(vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, largeParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldThrowParkingSpotNotFoundExceptionWhenVipUserWantsToParkANonElectricMotorcycleAndThereAreNotAnyFittingParkingSpots(){
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());
        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(vipUser, nonElectricMotorcycle));
    }

    @Test
    void findVehicleByPlateNumberShouldReturnSmallParkingSpotWithoutElectricChargerWhenRegularUserHasParkedANonElectricMotorcycleOnThatParkingSpot() throws ParkingSpotNotFound {
        parkingLotManager.park(regularUser, nonElectricMotorcycle);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(nonElectricMotorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        Vehicle vehicleParkedOnTheParkingSpot = parkingSpotOptional.get().getVehicle();

        assertEquals(parkingSpotOptional.get(), smallParkingSpotWithoutElectricCharger);
        assertEquals(vehicleParkedOnTheParkingSpot, nonElectricMotorcycle);
    }

    @Test
    void findVehicleByPlateNumberShouldReturnSmallParkingSpotWithElectricChargerWhenRegularUserHasParkedAnElectricMotorcycleOnThatParkingSpot() throws ParkingSpotNotFound {
        parkingLotManager.park(regularUser, electricMotorcycle);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricMotorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        Vehicle vehicleParkedOnTheParkingSpot = parkingSpotOptional.get().getVehicle();
        assertEquals(electricMotorcycle, vehicleParkedOnTheParkingSpot);
        assertEquals(parkingSpotOptional.get(), smallParkingSpotWithElectricCharger);
    }


    @Test
    void findVehicleByPlateNumberShouldReturnLargeParkingSpotWithElectricChargerWhenVipUserHasParkedAnElectricCarOnThatParkingSpot() throws ParkingSpotNotFound {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(smallParkingSpotWithElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());

        parkingLotManager.park(vipUser, electricCar);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricCar.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        Vehicle vehicleParkedOnTheParkingSpot = parkingSpotOptional.get().getVehicle();
        assertEquals(electricCar, vehicleParkedOnTheParkingSpot);
        assertEquals(parkingSpotOptional.get(), largeParkingSpotWithElectricCharger);
    }

    @Test
    void findVehicleByPlateNumberShouldReturnMediumParkingSpotWithoutElectricChargerWhenVipUserHasParkedAnElectricCarOnThatParkingSpot() throws ParkingSpotNotFound {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(smallParkingSpotWithElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance());

        parkingLotManager.park(vipUser, electricCar);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricCar.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        Vehicle vehicleParkedOnTheParkingSpot = parkingSpotOptional.get().getVehicle();
        assertEquals(electricCar, vehicleParkedOnTheParkingSpot);
        assertEquals(parkingSpotOptional.get(), mediumParkingSpotWithoutElectricCharger);
    }

    @ParameterizedTest
    @MethodSource("vehicleGenerator")
    void findVehicleByPlateNumberShoutReturnEmptyOptionalForAnyVehicleWhichIsNotParked(Vehicle vehicle){
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(vehicle.getPlateNumber());
        assertTrue(parkingSpotOptional.isEmpty());
    }

    private static Stream<Arguments> vehicleGenerator() {
        return Stream.of(
                Arguments.of(new Motorcycle("", false)),
                Arguments.of(new Motorcycle("", true)),
                Arguments.of(new Car("", false)),
                Arguments.of(new Car("", true)),
                Arguments.of(new Truck("", false)),
                Arguments.of(new Truck("", true)));
    }
}