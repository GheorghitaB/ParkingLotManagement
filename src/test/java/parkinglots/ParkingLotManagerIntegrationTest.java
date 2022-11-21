package parkinglots;

import exceptions.ParkingSpotNotFound;
import exceptions.ParkingSpotTypeNotFoundException;
import exceptions.UserTypeNotFoundException;
import exceptions.VehicleTypeNotFoundException;
import services.parkings.lots.ParkingLotManager;
import models.parkings.spots.LargeParkingSpot;
import models.parkings.spots.MediumParkingSpot;
import models.parkings.spots.ParkingSpot;
import models.parkings.spots.SmallParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import services.parkings.strategies.ParkingStrategyFactory;
import services.parkings.spots.ParkingSpotInMemoryService;
import services.parkings.spots.ParkingSpotService;
import models.tickets.Ticket;
import models.users.RegularUser;
import models.users.User;
import models.users.VIPUser;
import models.vehicles.Car;
import models.vehicles.Motorcycle;
import models.vehicles.Truck;
import models.vehicles.Vehicle;
import services.taxes.ParkingPriceCalculator;
import services.taxes.implementations.*;

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
    private int parkingDurationTimeInMinutes;
    private ParkingPriceCalculator parkingPriceCalculator;


    @BeforeEach
    void setUp() {
        initParkingSpots();
        populateParkingSpotList();
        initUsers();
        initVehicles();
        parkingDurationTimeInMinutes = 60;
        ParkingSpotService parkingSpotService = new ParkingSpotInMemoryService(parkingSpotList);
        parkingPriceCalculator = new ParkingPriceCalculatorImpl(new UserTypePriceImpl(),
                new VehicleTypePriceImpl(), new ParkingSpotTypePriceImpl(), new DiscountCalculatorImpl());

        parkingLotManager = new ParkingLotManager(parkingSpotService, ParkingStrategyFactory.getParkingStrategyInstance(),
                parkingPriceCalculator);
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
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedANonElectricMotorcycleOnAGivenSmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, nonElectricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, nonElectricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedAnElectricMotorcycleOnAGivenSmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedANonElectricCarOnAGivenMediumParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, nonElectricCar);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, nonElectricCar);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedAnElectricCarOnAGivenMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, electricCar);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, electricCar);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedANonElectricTruckOnAGivenLargeParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, nonElectricTruck);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, largeParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, regularUser);
        assertEquals(ticketVehicle, nonElectricTruck);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutRegularUserWhichParkedAnElectricTruckOnAGivenLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, electricTruck);
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
        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, nonElectricMotorcycle));
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedANonElectricMotorcycleOnAGivenSmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, nonElectricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, nonElectricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenSmallParkingSpotWithElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(smallParkingSpotWithElectricCharger, ticketGivenParkingSpot);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenLargeParkingSpotWithElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, largeParkingSpotWithElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenSmallParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenMediumParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricMotorcycle);
        ParkingSpot ticketGivenParkingSpot = ticket.getParkingSpot();
        User ticketUser = ticket.getUser();
        Vehicle ticketVehicle = ticket.getVehicle();

        assertEquals(ticketGivenParkingSpot, mediumParkingSpotWithoutElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenLargeParkingSpotWithoutElectricCharger() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        Ticket ticket = parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricMotorcycle);
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
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);
        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, nonElectricMotorcycle));
    }

    @Test
    void findVehicleByPlateNumberShouldReturnSmallParkingSpotWithoutElectricChargerWhenRegularUserHasParkedANonElectricMotorcycleOnThatParkingSpot() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, nonElectricMotorcycle);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(nonElectricMotorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        Vehicle vehicleParkedOnTheParkingSpot = parkingSpotOptional.get().getVehicle();

        assertEquals(parkingSpotOptional.get(), smallParkingSpotWithoutElectricCharger);
        assertEquals(vehicleParkedOnTheParkingSpot, nonElectricMotorcycle);
    }

    @Test
    void findVehicleByPlateNumberShouldReturnSmallParkingSpotWithElectricChargerWhenRegularUserHasParkedAnElectricMotorcycleOnThatParkingSpot() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        parkingLotManager.park(parkingDurationTimeInMinutes, regularUser, electricMotorcycle);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricMotorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        Vehicle vehicleParkedOnTheParkingSpot = parkingSpotOptional.get().getVehicle();
        assertEquals(electricMotorcycle, vehicleParkedOnTheParkingSpot);
        assertEquals(parkingSpotOptional.get(), smallParkingSpotWithElectricCharger);
    }


    @Test
    void findVehicleByPlateNumberShouldReturnLargeParkingSpotWithElectricChargerWhenVipUserHasParkedAnElectricCarOnThatParkingSpot() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(smallParkingSpotWithElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricCar);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricCar.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        Vehicle vehicleParkedOnTheParkingSpot = parkingSpotOptional.get().getVehicle();
        assertEquals(electricCar, vehicleParkedOnTheParkingSpot);
        assertEquals(parkingSpotOptional.get(), largeParkingSpotWithElectricCharger);
    }

    @Test
    void findVehicleByPlateNumberShouldReturnMediumParkingSpotWithoutElectricChargerWhenVipUserHasParkedAnElectricCarOnThatParkingSpot() throws ParkingSpotNotFound, VehicleTypeNotFoundException, UserTypeNotFoundException, ParkingSpotTypeNotFoundException {
        List<ParkingSpot> newParkingSpotList = new ArrayList<>();
        newParkingSpotList.add(smallParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(smallParkingSpotWithElectricCharger);
        newParkingSpotList.add(mediumParkingSpotWithoutElectricCharger);
        newParkingSpotList.add(largeParkingSpotWithoutElectricCharger);
        parkingLotManager = new ParkingLotManager(new ParkingSpotInMemoryService(newParkingSpotList), ParkingStrategyFactory.getParkingStrategyInstance(), parkingPriceCalculator);

        parkingLotManager.park(parkingDurationTimeInMinutes, vipUser, electricCar);
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