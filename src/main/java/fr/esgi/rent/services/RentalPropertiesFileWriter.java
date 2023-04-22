package fr.esgi.rent.services;

import fr.esgi.rent.beans.RentalProperty;
import fr.esgi.rent.exception.RentalPropertyIOException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static fr.esgi.rent.csv.CsvProperties.HEADERS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;

@ApplicationScoped
public class RentalPropertiesFileWriter {

    private final String rentalPropertiesPathFile;

    public RentalPropertiesFileWriter() throws URISyntaxException {
        this.rentalPropertiesPathFile = getClass().getClassLoader().getResource("rentalProperties.csv").toURI().toString();
    }

    @Inject
    public RentalPropertiesFileWriter(@ConfigProperty(name = "rentalPropertyPathFile") String rentalPropertiesPathFile) {
        this.rentalPropertiesPathFile = rentalPropertiesPathFile;
    }

    public void addRentalProperty(RentalProperty rentalProperty) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();

        Path path = Paths.get(rentalPropertiesPathFile);

        String[] dataColumns = rentalProperty.getDataColumns();

        try (BufferedWriter writer = Files.newBufferedWriter(path, UTF_8, CREATE, WRITE, APPEND);
             CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {

            printer.printRecords(new String[][]{dataColumns});

            printer.flush();
        } catch (IOException e) {
            throw new RentalPropertyIOException("Erreur durant l'ajout d'une location dans le fichier CSV : " + rentalPropertiesPathFile, e);
        }
    }

}
