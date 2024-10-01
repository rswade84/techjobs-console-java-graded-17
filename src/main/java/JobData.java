import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all the values of the given field
     */

    // This method returns a list of all values of the given field...
    public static ArrayList<String> findAll(String field) {

        ArrayList<String> values = new ArrayList<>();

        loadData();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of teh field to search for
     * @return List of all jobs matching the criteria
     */

    // This method returns list of jobs that match the search ...
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        String lowerCaseValue = value.toLowerCase();

        // Iterate through allJobs, return value (HashMap), convert to lowercase.
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(column).toLowerCase();

            // If 'a value' contains the value of the search term, add it to row...
            if (aValue.contains(lowerCaseValue)) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return List of all jobs with at least one field containing the value
     */

    // findByValue() holds the value of the user's search term...
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // Load the data, if it hasn't already been done...
        loadData();

        // Task 2: Create method findByValue()...
        // TODO - implement this method

        // We create an ArrayList of HashMaps called jobs...
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        String lowercaseSearchTerm = value.toLowerCase();

        for (HashMap<String, String> row : allJobs) {
            // .Entry represents the key-value pair for each HashMap...
            for (HashMap.Entry<String, String> job : row.entrySet()) {
                String jobValue = job.getValue().toLowerCase();

                if (jobValue.contains(lowercaseSearchTerm)) {
                    if (!jobs.contains(row)) {
                        jobs.add(row);
                    }
                    break;
                }
            }
        }

        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            int numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
