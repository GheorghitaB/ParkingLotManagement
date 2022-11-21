package inits.parkingstrategies;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import models.parkings.spots.inits.VipUserStrategyInit;
import org.junit.jupiter.api.Test;
import models.parkings.spots.ParkingSpotType;
import models.vehicles.VehicleType;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VipUserStrategyInitIntegrationTest {
    private static final String VIP_USER_STRATEGY_INIT_TEST1_FILEPATH = "inits/parkingstrategies/vip-user-strategy/test1.init";
    private static final String VIP_USER_STRATEGY_INIT_TEST2_FILEPATH = "inits/parkingstrategies/vip-user-strategy/test2.init";
    private static final String VIP_USER_STRATEGY_INIT_TEST3_FILEPATH = "inits/parkingstrategies/vip-user-strategy/test3.init";
    private static final String VIP_USER_STRATEGY_INIT_TEST4_FILEPATH = "inits/parkingstrategies/vip-user-strategy/test4.init";
    private static final String VIP_USER_STRATEGY_INIT_TEST5_FILEPATH = "inits/parkingstrategies/vip-user-strategy/test5.init";
    private static final String VIP_USER_STRATEGY_INIT_TEST6_FILEPATH = "inits/parkingstrategies/vip-user-strategy/test6.init";

    @Test
    void getParkingSpotsFitsFromResource_ShouldReturnMapOfSizeThreeWhenThereAreThreeLinesWithDifferentVehicleTypes(){
        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(VIP_USER_STRATEGY_INIT_TEST1_FILEPATH);
        assertEquals(3, resultMap.size());
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldReturnMapWithListOfSmallAndMediumForMotorcycleEvenIfThereIsADuplicatedSmallParkingSpot(){
        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(VIP_USER_STRATEGY_INIT_TEST2_FILEPATH);
        assertEquals(2, resultMap.get(VehicleType.MOTORCYCLE).size());
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldReturnMapWithListOfSizeTwoForMotorcycleWhenThereAreTwoLinesWithTheSameVehicleTypeArgument(){
        Map<VehicleType, Set<ParkingSpotType>> resultMap = VipUserStrategyInit.getParkingSpotsFitsFromResource(VIP_USER_STRATEGY_INIT_TEST3_FILEPATH);
        assertEquals(2, resultMap.get(VehicleType.MOTORCYCLE).size());
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenTheVehicleTypeArgumentIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> VipUserStrategyInit.getParkingSpotsFitsFromResource(VIP_USER_STRATEGY_INIT_TEST4_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenTheParkingSpotTypeArgumentIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> VipUserStrategyInit.getParkingSpotsFitsFromResource(VIP_USER_STRATEGY_INIT_TEST5_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getParkingSpotsFitsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenTheNumberOfArgumentsIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> VipUserStrategyInit.getParkingSpotsFitsFromResource(VIP_USER_STRATEGY_INIT_TEST6_FILEPATH));
        assertEquals(-1, status);
    }

}