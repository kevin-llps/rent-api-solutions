package fr.esgi.rent.services;

import fr.esgi.rent.beans.RentalProperty;
import fr.esgi.rent.exception.RentalPropertyIOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static fr.esgi.rent.samples.RentalPropertySample.oneRentalProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RentalPropertiesFileWriterTest {

    public static final String TMP_TEST_CSV = "/tmp/test.csv";

    @AfterEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(TMP_TEST_CSV));
    }

    @Test
    void shouldAddRentalProperty() throws IOException {
        RentalProperty rentalProperty = oneRentalProperty();

        RentalPropertiesFileWriter rentalPropertiesFileWriter = new RentalPropertiesFileWriter(TMP_TEST_CSV);

        rentalPropertiesFileWriter.addRentalProperty(rentalProperty);

        try (Stream<String> lines = Files.lines(Paths.get(TMP_TEST_CSV))) {
            assertThat(lines.toList()).containsExactly("46890,Appartement spacieux avec vue sur l'ESGI,Paris,77 Rue des roses,FLAT,750.9,1200.9,37.48,2,1,3,1990,D,true,true,true,true");
        }
    }

    @Test
    void givenInvalidPath_shouldThrowRentalPropertyIOException() {
        RentalProperty rentalProperty = oneRentalProperty();

        RentalPropertiesFileWriter rentalPropertiesFileWriter = new RentalPropertiesFileWriter("/no-exist/test.csv");

        assertThatExceptionOfType(RentalPropertyIOException.class)
                .isThrownBy(() -> rentalPropertiesFileWriter.addRentalProperty(rentalProperty))
                .satisfies(e -> assertThat(e.getMessage()).isEqualTo("Erreur durant l'ajout d'une location dans le fichier CSV : /no-exist/test.csv"));
    }

}
