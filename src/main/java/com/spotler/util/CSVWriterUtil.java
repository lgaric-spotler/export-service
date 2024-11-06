package com.spotler.util;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class CSVWriterUtil {

    public CSVWriterUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated!");
    }

    /**
     * Write a list of objects to a CSV file
     * @param dataList List of objects to write
     * @param <T> Type of the objects
     * @return Writer containing the CSV data
     * @throws IOException If an I/O error occurs
     * @throws IllegalAccessException If an error occurs while accessing the fields of the objects
     */
    public static <T> Writer write(List<T> dataList) throws IOException, IllegalAccessException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }

        StringWriter writer = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        CSVWriter csvWriter = new CSVWriter(bufferedWriter);

        // Get the class of the first element
        Class<?> clazz = dataList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Write header
        String[] header = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            header[i] = fields[i].getName();
        }
        csvWriter.writeNext(header);

        // Write data
        for (T data : dataList) {
            String[] values = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(data);
                values[i] = value != null ? value.toString() : "";
            }
            csvWriter.writeNext(values);
        }

        bufferedWriter.close();
        csvWriter.close();
        writer.close();

        return writer;
    }
}
