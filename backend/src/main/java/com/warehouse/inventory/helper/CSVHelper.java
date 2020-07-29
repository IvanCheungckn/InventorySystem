package com.warehouse.inventory.helper;

import com.warehouse.inventory.model.ProductCSV;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] PRODUCT_HEADERS = { "Product Code", "Product Name", "Product Weight", "Location Name", "Quantity" };

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<ProductCSV> csvToProducts(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<ProductCSV> products = new ArrayList<ProductCSV>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                ProductCSV productCSV = new ProductCSV(
                        csvRecord.get(0),
                        csvRecord.get("Product Name").trim(),
                        csvRecord.get("Location Name"),
                        Integer.parseInt(csvRecord.get("Product Weight")),
                        Long.parseLong(csvRecord.get("Quantity"))
                );

                products.add(productCSV);
            }
            return products;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
