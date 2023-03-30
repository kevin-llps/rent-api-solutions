package fr.esgi.rent.api;

import fr.esgi.rent.beans.RentalProperty;
import fr.esgi.rent.services.RentalPropertiesFileParser;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/rental-properties")
public class RentalPropertyResource {

    private final RentalPropertiesFileParser rentalPropertiesFileParser;

    @Inject
    public RentalPropertyResource(RentalPropertiesFileParser rentalPropertiesFileParser) {
        this.rentalPropertiesFileParser = rentalPropertiesFileParser;
    }

    @GET
    public List<RentalProperty> getRentalProperties() {
        return rentalPropertiesFileParser.parse("rentalProperties.csv");
    }
}