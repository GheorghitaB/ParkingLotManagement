package parkinglots;

import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parkingspots.*;
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
        ParkingLotRepository parkingLotRepository = new ParkingLotInMemoryRepository(parkingSpotList);
        parkingLotManager = new ParkingLotManager(parkingLotRepository);
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

        assertEquals(ticketGivenParkingSpot, smallParkingSpotWithElectricCharger);
        assertEquals(ticketUser, vipUser);
        assertEquals(ticketVehicle, electricMotorcycle);
    }

    @Test
    void parkShouldReturnTicketWithDataAboutVipUserWhichParkedAnElectricMotorcycleOnAGivenMediumParkingSpotWithElectricCharger() throws ParkingSpotNotFound {
        parkingSpotList.remove(smallParkingSpotWithElectricCharger);

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
        parkingSpotList.remove(smallParkingSpotWithElectricCharger);
        parkingSpotList.remove(mediumParkingSpotWithElectricCharger);

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
        parkingSpotList.remove(smallParkingSpotWithElectricCharger);
        parkingSpotList.remove(mediumParkingSpotWithElectricCharger);
        parkingSpotList.remove(largeParkingSpotWithElectricCharger);

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
        parkingSpotList.remove(smallParkingSpotWithElectricCharger);
        parkingSpotList.remove(mediumParkingSpotWithElectricCharger);
        parkingSpotList.remove(largeParkingSpotWithElectricCharger);
        parkingSpotList.remove(smallParkingSpotWithoutElectricCharger);

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
        parkingSpotList.remove(smallParkingSpotWithElectricCharger);
        parkingSpotList.remove(mediumParkingSpotWithElectricCharger);
        parkingSpotList.remove(largeParkingSpotWithElectricCharger);
        parkingSpotList.remove(smallParkingSpotWithoutElectricCharger);
        parkingSpotList.remove(mediumParkingSpotWithoutElectricCharger);

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
        parkingSpotList.clear();
        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(vipUser, nonElectricMotorcycle));
    }

    @Test
    void findVehicleShouldReturnSmallParkingSpotWithoutElectricChargerWhenRegularUserHasParkedANonElectricMotorcycleOnThatParkingSpot() throws ParkingSpotNotFound {
        parkingLotManager.park(regularUser, nonElectricMotorcycle);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(nonElectricMotorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(parkingSpotOptional.get(), smallParkingSpotWithoutElectricCharger);
    }

    @Test
    void findVehicleShouldReturnSmallParkingSpotWithElectricChargerWhenRegularUserHasParkedAnElectricMotorcycleOnThatParkingSpot() throws ParkingSpotNotFound {
        parkingLotManager.park(regularUser, electricMotorcycle);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricMotorcycle.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(parkingSpotOptional.get(), smallParkingSpotWithElectricCharger);
    }

    @Test
    void findVehicleShouldReturnLargeParkingSpotWithElectricChargerWhenVipUserHasParkedAnElectricCarOnThatParkingSpot() throws ParkingSpotNotFound {
        parkingSpotList.remove(mediumParkingSpotWithElectricCharger);

        parkingLotManager.park(vipUser, electricCar);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricCar.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(parkingSpotOptional.get(), largeParkingSpotWithElectricCharger);
    }

    @Test
    void findVehicleShouldReturnMediumParkingSpotWithoutElectricChargerWhenVipUserHasParkedAnElectricCarOnThatParkingSpot() throws ParkingSpotNotFound {
        parkingSpotList.remove(mediumParkingSpotWithElectricCharger);
        parkingSpotList.remove(largeParkingSpotWithElectricCharger);

        parkingLotManager.park(vipUser, electricCar);
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricCar.getPlateNumber());

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(parkingSpotOptional.get(), mediumParkingSpotWithoutElectricCharger);
    }

    @Test
    void findVehicleShouldReturnEmptyOptionalForElectricMotorcycleWhenItIsNotParked(){
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicleByPlateNumber(electricMotorcycle.getPlateNumber());
        assertTrue(parkingSpotOptional.isEmpty());
    }
}













