package io.jhaber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class AnalyzeCambridgeCrashData {

  private static final String HOSPITALIZATIONS = "Hospitalizations (estimated)";
  private static final String MOTORISTS = "Number of Motorists";
  private static final String CYCLISTS = "Number of Cyclists";
  private static final String PEDESTRIANS = "Number of Pedestrians";

  public static void main(String... args) throws Exception {
    int total = 0;
    int allThree = 0;
    int motoristAndPedestrian = 0;
    int motoristAndCyclist = 0;
    int pedestrianAndCyclist = 0;
    int onlyMotorist = 0;
    int onlyCyclist = 0;
    int onlyPedestrian = 0;
    int none = 0;

    try (
      InputStream fileStream = AnalyzeCambridgeCrashData.class.getClassLoader()
        .getResource("CPD_Crash_Log.csv")
        .openStream();
      Reader reader = new InputStreamReader(fileStream, StandardCharsets.UTF_8);
      CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader)
    ) {
      for (CSVRecord record : parser) {
        int hospitalizations = Integer.parseInt(record.get(HOSPITALIZATIONS));
        int motorists = Integer.parseInt(record.get(MOTORISTS));
        int cyclists = Integer.parseInt(record.get(CYCLISTS));
        int pedestrians = Integer.parseInt(record.get(PEDESTRIANS));

        if (hospitalizations > 0) {
          total++;
          if (motorists > 0 && cyclists > 0 && pedestrians > 0) {
            allThree++;
          } else if (motorists > 0 && pedestrians > 0) {
            motoristAndPedestrian++;
          } else if (motorists > 0 && cyclists > 0) {
            motoristAndCyclist++;
          } else if (pedestrians > 0 && cyclists > 0) {
            pedestrianAndCyclist++;
          } else if (motorists > 0) {
            onlyMotorist++;
          } else if (cyclists > 0) {
            onlyCyclist++;
          } else if (pedestrians > 0) {
            onlyPedestrian++;
          } else {
            none++;
          }
        }
      }
    }

    System.out.println(
      "Motorist, pedestrian, and cyclist: " + countAndPercent(allThree, total)
    );
    System.out.println(
      "Motorist and pedestrian: " + countAndPercent(motoristAndPedestrian, total)
    );
    System.out.println(
      "Motorist and cyclist: " + countAndPercent(motoristAndCyclist, total)
    );
    System.out.println(
      "Pedestrian and cyclist: " + countAndPercent(pedestrianAndCyclist, total)
    );
    System.out.println("Motorist: " + countAndPercent(onlyMotorist, total));
    System.out.println("Cyclist: " + countAndPercent(onlyCyclist, total));
    System.out.println("Pedestrian: " + countAndPercent(onlyPedestrian, total));
    System.out.println("None: " + countAndPercent(none, total));
  }

  private static String countAndPercent(int count, int total) {
    double percent = (count * 100.0d) / total;
    return String.format("%s/%s (%.2f%%)", count, total, percent);
  }
}
