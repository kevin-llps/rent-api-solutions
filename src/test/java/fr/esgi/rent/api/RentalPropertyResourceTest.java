package fr.esgi.rent.api;

import fr.esgi.rent.beans.RentalProperty;
import fr.esgi.rent.dto.RentalPropertyDto;
import fr.esgi.rent.mapper.RentalPropertyDtoMapper;
import fr.esgi.rent.services.RentalPropertiesFileParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static fr.esgi.rent.samples.RentalPropertyDtoSample.rentalPropertyDtoList;
import static fr.esgi.rent.samples.RentalPropertySample.rentalProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalPropertyResourceTest {

    @InjectMocks
    private RentalPropertyResource rentalPropertyResource;

    @Mock
    private RentalPropertiesFileParser rentalPropertiesFileParser;

    @Mock
    private RentalPropertyDtoMapper rentalPropertyDtoMapper;

    @Test
    void shouldGetRentalProperties() {
        List<RentalPropertyDto> expectedRentalPropertyDtoList = rentalPropertyDtoList();
        List<RentalProperty> rentalProperties = rentalProperties();

        when(rentalPropertiesFileParser.parse("rentalProperties.csv")).thenReturn(rentalProperties);
        when(rentalPropertyDtoMapper.mapToDtoList(rentalProperties)).thenReturn(expectedRentalPropertyDtoList);

        List<RentalPropertyDto> rentalPropertyDtoList = rentalPropertyResource.getRentalProperties();

        assertThat(rentalPropertyDtoList).containsExactlyInAnyOrderElementsOf(expectedRentalPropertyDtoList);

        verify(rentalPropertiesFileParser).parse("rentalProperties.csv");
        verify(rentalPropertyDtoMapper).mapToDtoList(rentalProperties);

        verifyNoMoreInteractions(rentalPropertiesFileParser);
    }

}
