package parkinglots;

import exceptions.ParkingSpotNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import parkingspots.ParkingSpot;
import parkingspots.SmallParkingSpot;
import tickets.Ticket;
import users.RegularUser;
import users.User;
import vehicles.Car;
import vehicles.Motorcycle;
import vehicles.Vehicle;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingLotManagerTest {
    @Mock
    private ParkingLotRepository parkingLotRepository;
    @InjectMocks
    private ParkingLotManager parkingLotManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parkingLotManager = new ParkingLotManager(parkingLotRepository);
    }

    @Test
    void parkShouldReturnTicketWithConsistentDataAboutUserAndVehicleAndParkingSpot() throws ParkingSpotNotFound {
        User user = new RegularUser("John");
        Vehicle motorcycle = new Motorcycle("", false);
        ParkingSpot smallParkingSpot = new SmallParkingSpot(false);

        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(smallParkingSpot.getParkingSpotType())).thenReturn(Optional.of(smallParkingSpot));
        Ticket ticket = parkingLotManager.park(user, motorcycle);

        assertEquals(user.getName(), ticket.getUser().getName());
        assertEquals(user.getUserType(), ticket.getUser().getUserType());
        assertEquals(smallParkingSpot.getParkingSpotType(), ticket.getParkingSpot().getParkingSpotType());
        assertFalse(ticket.getParkingSpot().hasElectricCharger());
        assertFalse(ticket.getVehicle().isElectric());
        assertEquals(smallParkingSpot.getId(), ticket.getParkingSpot().getId());
    }

    @Test
    void parkShouldThrowParkingSpotNotFoundExceptionForRegularUserWithNonElectricMotorcycleWhenThereIsNotASmallParkingSpotWithoutElectricChargerAvailable(){
        User user = new RegularUser("John");
        Vehicle motorcycle = new Motorcycle("", false);
        ParkingSpot smallParkingSpot = new SmallParkingSpot(false);

        when(parkingLotRepository.getEmptyParkingSpotWithoutElectricChargerOfType(smallParkingSpot.getParkingSpotType())).thenReturn(Optional.empty());

        assertThrows(ParkingSpotNotFound.class, () -> parkingLotManager.park(user, motorcycle));
    }

    @Test
    void findVehicleShouldReturnEmptyOptionalWhenTheSpecifiedVehicleIsNotParked(){
        Vehicle car = new Car("", false);

        when(parkingLotRepository.findVehicle(car)).thenReturn(Optional.empty());
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicle(car);

        assertTrue(parkingSpotOptional.isEmpty());
    }

    @Test
    void findVehicleShouldReturnSmallParkingSpotWithoutElectricChargerForNonElectricMotorcycleWhenItIsParkedOnSmallParkingSpotWithoutElectricCharger() {
        ParkingSpot smallParkingSpot = new SmallParkingSpot(false);
        Vehicle motorcycle = new Motorcycle("", false);

        when(parkingLotRepository.findVehicle(motorcycle)).thenReturn(Optional.of(smallParkingSpot));
        Optional<ParkingSpot> parkingSpotOptional = parkingLotManager.findVehicle(motorcycle);

        assertTrue(parkingSpotOptional.isPresent());
        assertEquals(smallParkingSpot.getParkingSpotType(), parkingSpotOptional.get().getParkingSpotType());
        assertFalse(parkingSpotOptional.get().hasElectricCharger());
        verify(parkingLotRepository, times(1)).findVehicle(motorcycle);
    }
}