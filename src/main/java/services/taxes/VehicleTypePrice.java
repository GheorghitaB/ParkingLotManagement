package services.taxes;

import exceptions.VehicleTypeNotFoundException;
import models.vehicles.VehicleType;

public interface VehicleTypePrice {
    double getVehicleTypePrice(VehicleType vehicleType) throws VehicleTypeNotFoundException;
}
