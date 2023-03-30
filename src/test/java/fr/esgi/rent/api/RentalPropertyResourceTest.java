package fr.esgi.rent.api;

import fr.esgi.rent.beans.RentalProperty;
import fr.esgi.rent.services.RentalPropertiesFileParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static fr.esgi.rent.samples.RentalPropertySample.rentalProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalPropertyResourceTest {

    @InjectMocks
    private RentalPropertyResource rentalPropertyResource;

    @Mock
    private RentalPropertiesFileParser rentalPropertiesFileParser;

    @Test
    void shouldGetRentalProperties() {
        List<RentalProperty> expectedRentalProperties = rentalProperties();

        when(rentalPropertiesFileParser.parse("rentalProperties.csv")).thenReturn(expectedRentalProperties);

        List<RentalProperty> rentalProperties = rentalPropertyResource.getRentalProperties();

        assertThat(rentalProperties).containsExactlyInAnyOrderElementsOf(expectedRentalProperties);

        verifyNoMoreInteractions(rentalPropertiesFileParser);
    }

}
