package CAMSv2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class StaffDataBase {
    private static ArrayList<Staff> staffList = new ArrayList<>();
    private static String filePath;


    public StaffDataBase(String filePath) {
        this.filePath = filePath;
    }



    public static void loadToCSV(){
        try (InputStream inputStream = new FileInputStream(filePath);
             Reader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            reader.mark(1);
            String line;

            if (reader.read() != 0xFEFF) {
                reader.reset(); // Reset if the first character is not the BOM
            }

            while ((line = reader.readLine()) != null) {
                if (line.equals("")) {return;}
                // String firstLine = reader.readLine(); // Check the first line
                // if (firstLine == null) {
                //     return; // Exit method if the file is empty
                // }
                String[] values = line.split(",");
                String name = values[0].trim();
                String emailID = values[1];
                String faculty = values[2].trim();
                String password = values[3].trim(); // Assume default password
                // if csv == student.csv ,
                //    String role = student;
                //else if cvs == staff.csv ,
                // String role= staff
                Staff staff = new Staff(name,emailID, faculty,password,Role.STAFF);
                staffList.add(staff);
            }
        } catch (IOException e) {e.printStackTrace();}

    }

    public void writeToCSV() {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filePath, false))) {
            for (Staff staff : staffList) {
                printWriter.println( staff.getName()+ "," +staff.getEmailID()  + "," +staff.getFaculty()  + ","+ staff.getPassword());
            }
        }
        catch (IOException e) {
            System.out.println("CSV file not found");
        }
    }

    public static ArrayList<Staff> getStaffList() {
        return staffList;
    }

}