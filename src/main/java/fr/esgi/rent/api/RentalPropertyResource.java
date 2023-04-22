package fr.esgi.rent.api;

import fr.esgi.rent.beans.RentalProperty;
import fr.esgi.rent.dto.request.RentalPropertyRequestDto;
import fr.esgi.rent.dto.response.RentalPropertyResponseDto;
import fr.esgi.rent.exception.NotFoundRentalPropertyException;
import fr.esgi.rent.mapper.RentalPropertyDtoMapper;
import fr.esgi.rent.services.RentalPropertiesFileParser;
import fr.esgi.rent.services.RentalPropertiesFileWriter;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("/rental-properties")
public class RentalPropertyResource {

    private final RentalPropertiesFileParser rentalPropertiesFileParser;
    private final RentalPropertyDtoMapper rentalPropertyDtoMapper;
    private final RentalPropertiesFileWriter rentalPropertiesFileWriter;

    @Inject
    public RentalPropertyResource(RentalPropertiesFileParser rentalPropertiesFileParser,
                                  RentalPropertyDtoMapper rentalPropertyDtoMapper,
                                  RentalPropertiesFileWriter rentalPropertiesFileWriter) {
        this.rentalPropertiesFileParser = rentalPropertiesFileParser;
        this.rentalPropertyDtoMapper = rentalPropertyDtoMapper;
        this.rentalPropertiesFileWriter = rentalPropertiesFileWriter;
    }

    @GET
    public List<RentalPropertyResponseDto> getRentalProperties() {
        List<RentalProperty> rentalProperties = rentalPropertiesFileParser.parse("rentalProperties.csv");

        return rentalPropertyDtoMapper.mapToDtoList(rentalProperties);
    }

    @GET
    @Path("/{id}")
    public RentalPropertyResponseDto getRentalProperty(@PathParam("id") @Positive int id) {
        return rentalPropertiesFileParser.parse("rentalProperties.csv")
                .stream()
                .filter(rentalProperty -> rentalProperty.referenceId() == id)
                .map(rentalPropertyDtoMapper::mapToDto)
                .findAny()
                .orElseThrow(() -> new NotFoundRentalPropertyException("RentalProperty not found with id " + id));
    }

    @POST
    public RentalPropertyResponseDto addRentalProperty(RentalPropertyRequestDto rentalPropertyRequestDto) {
        RentalProperty rentalProperty = rentalPropertyDtoMapper.mapToBean(rentalPropertyRequestDto);

        rentalPropertiesFileWriter.addRentalProperty(rentalProperty);

        return rentalPropertyDtoMapper.mapToDto(rentalProperty);
    }

}