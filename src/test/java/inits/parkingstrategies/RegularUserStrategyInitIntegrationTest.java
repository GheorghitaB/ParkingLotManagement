package inits.parkingstrategies;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import models.parkings.spots.inits.RegularUserStrategyInit;
import org.junit.jupiter.api.Test;
import models.parkings.spots.ParkingSpotType;
import models.vehicles.VehicleType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegularUserStrategyInitIntegrationTest {
    private static final String REGULAR_USER_STRATEGY_TEST1_FILEPATH = "inits/parkingstrategies/regular-user-strategy/test1.init";
    private static final String REGULAR_USER_STRATEGY_TEST2_FILEPATH = "inits/parkingstrategies/regular-user-strategy/test2.init";
    private static final String REGULAR_USER_STRATEGY_TEST3_FILEPATH = "inits/parkingstrategies/regular-user-strategy/test3.init";
    private static final String REGULAR_USER_STRATEGY_TEST4_FILEPATH = "inits/parkingstrategies/regular-user-strategy/test4.init";
    private static final String REGULAR_USER_STRATEGY_TEST5_FILEPATH = "inits/parkingstrategies/regular-user-strategy/test5.init";
    private static final String REGULAR_USER_STRATEGY_TEST6_FILEPATH = "inits/parkingstrategies/regular-user-strategy/test6.init";

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnMapWithSmallForMotorcycleAndMediumForCarAndLargeForTruckWhenTheseInfoIsReadFromTheFile(){
        Map<VehicleType, ParkingSpotType> resultMap = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(REGULAR_USER_STRATEGY_TEST1_FILEPATH);

        assertEquals(3, resultMap.size());
        assertTrue(resultMap.containsKey(VehicleType.MOTORCYCLE));
        assertTrue(resultMap.containsKey(VehicleType.CAR));
        assertTrue(resultMap.containsKey(VehicleType.TRUCK));
        assertEquals(ParkingSpotType.SMALL, resultMap.get(VehicleType.MOTORCYCLE));
        assertEquals(ParkingSpotType.MEDIUM, resultMap.get(VehicleType.CAR));
        assertEquals(ParkingSpotType.LARGE, resultMap.get(VehicleType.TRUCK));
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenTheNumberOfArgumentsIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(REGULAR_USER_STRATEGY_TEST2_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenVehicleTypeArgumentIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(REGULAR_USER_STRATEGY_TEST3_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldCloseApplicationWithStatusMinusOneWhenParkingSpotTypeArgumentIsNotValid() throws Exception {
        int status = SystemLambda.catchSystemExit( () -> RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(REGULAR_USER_STRATEGY_TEST4_FILEPATH));
        assertEquals(-1, status);
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnMapOfSizeOneWhenThereAreTwoLinesWithTheSameVehicleTypeThereforeOneLineIsIgnored(){
        Map<VehicleType, ParkingSpotType> resultMap = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(REGULAR_USER_STRATEGY_TEST5_FILEPATH);
        assertEquals(1, resultMap.size());
    }

    @Test
    void getMapOfFittingParkingSpotsFromResource_ShouldReturnEmptyMapWhenTheFileContainsOnlyCommentedLines(){
        Map<VehicleType, ParkingSpotType> resultMap = RegularUserStrategyInit.getMapOfFittingParkingSpotsFromResource(REGULAR_USER_STRATEGY_TEST6_FILEPATH);
        assertTrue(resultMap.isEmpty());
    }
}