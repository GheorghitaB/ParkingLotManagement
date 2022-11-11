package parkingstrategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import users.RegularUser;
import users.User;
import users.VIPUser;

import static org.junit.jupiter.api.Assertions.*;

class ParkingStrategyFactoryTest {

    private ParkingStrategyFactory parkingStrategyFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        parkingStrategyFactory = ParkingStrategyFactory.getParkingStrategyInstance();
    }

    @Test
    void getParkingStrategy_ShouldReturnRegularUserParkingStrategyWhenUserIsOfTypeRegular(){
        User regularUser = new RegularUser("");
        ParkingStrategy parkingStrategy = parkingStrategyFactory.getParkingStrategy(regularUser);
        assertTrue(parkingStrategy instanceof RegularUserParkingStrategy);
    }

    @Test
    void getParkingStrategy_ShouldReturnVipUserParkingStrategyWhenUserIsOfTypeVip(){
        User vipUser = new VIPUser("");
        ParkingStrategy parkingStrategy = parkingStrategyFactory.getParkingStrategy(vipUser);
        assertTrue(parkingStrategy instanceof VipUserParkingStrategy);
    }
}