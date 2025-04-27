package com.data_management;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileDataReader implements DataReader {
    private String baseDirectory;

    public FileDataReader(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
    /**
     * this function will find a directory or file of a specific name if it happens to be
     * a directory it will go through all the file inside of it and check if one is usable as data.
     * it will then call readFile to read and store the data in a data Storage object
     * @param dataStorage the storage that is passed down to readFile
     */

    public void readData(DataStorage dataStorage) throws IOException {
        Path mainPath = Paths.get(baseDirectory);
        if(!Files.exists(mainPath)||!Files.isDirectory(mainPath)) {
            System.err.println("Invalid base directory: " + baseDirectory);
        }
       try (Stream<Path> paths = Files.walk(Paths.get(baseDirectory))) {
           paths.filter(Files::isRegularFile)
                   .filter(path->path.getFileName().toString().endsWith(".txt"))
                   .forEach(path -> readFile(path,dataStorage));
       }
    }
    /** basing myself on the file that FileOutputStrategy will give out
     * @param dataStorage to fill it with the data from the file
     * @param path the path to that specific file
     * */
    private void readFile(Path path, DataStorage dataStorage ) {
        try(BufferedReader reader= Files.newBufferedReader(path) ) {
            String line =null;
            while ((line = reader.readLine()) != null) {

                try {
                    String[] splits = line.split(",\\s*");
                    if(splits.length>4){
                        System.err.println("Invalid record format: " + line+ " in "+path);
                        continue;
                    }
                    // substring because FileOutput put a suffix before the data in question
                    int patientId = Integer.parseInt(splits[0].substring(11).trim());
                    long timestamp = Long.parseLong(splits[1].substring(9).trim());
                    String label = splits[2].substring(6).trim();
                    double measurementValue = Double.parseDouble(splits[3].substring(5).trim());

                    dataStorage.addPatientData(patientId,measurementValue,label,timestamp);
                }catch (NumberFormatException e){
                    System.err.println("Invalid number format: " + line+" in "+path);
                }

            }
        } catch (IOException e) {
           System.err.println("Error reading file: " + path);
        }

    }
}
