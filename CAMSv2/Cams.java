package CAMSv2;

import java.util.Scanner;

public class Cams {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ALL PATHS ARE NOW STORED WITHIN THE DATABASE ITSELF -Yuhao


        StudentDataBase studentDataBase = StudentDataBase.getInstance();
        StaffDataBase staffDataBase = StaffDataBase.getInstance();
        CampCommitteeDataBase campCommitteeDataBase = CampCommitteeDataBase.getInstance();

        boolean running = true;

        while (running) {
            // Load CSV data at the start of each loop iteration
            studentDataBase.loadToCSV();
            staffDataBase.loadToCSV();
            campCommitteeDataBase.loadToCSV();

            int choice;
            int loginOption;

            System.out.println("Welcome to CAMS Menu:\n1. Log in\n2. Exit");
            choice = scanner.nextInt();
            if (choice == 2) {
                break;
            }

            System.out.print("Enter your Email: ");
            scanner.nextLine(); // Consume the newline character
            String emailID = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            System.out.println("Select your option: ");
            System.out.println("1. Staff");
            System.out.println("2. Student");
            System.out.println("3. Camp committee member");
            loginOption = scanner.nextInt();

            switch (choice) {
                case 1:
                    User user = Authenticator.check(emailID, password, loginOption);
                    if (user == null) {
                        System.out.println("Wrong credentials");
                        break;
                    }

                    System.out.println("Correct credentials");
                    if (loginOption == 1) {
                        Staff staff = (Staff) user;
                        staff.staffInterface();
                    } else if (loginOption == 2) {
                        Student student = (Student) user;
                        StudentController studentController = new StudentController(student, new StudentView());
                        studentController.startProgram();
                    } else {
                        CampCommitteeMember campCommitteeMember = (CampCommitteeMember) user;
                        CampCommitteeMemberController campCommitteeMemberController = new CampCommitteeMemberController(campCommitteeMember, new CampCommitteeMemberView());
                        campCommitteeMemberController.startProgram();
                    }
                    break;

                default:
                    running = false;
                    break;
            }

            // Write changes to the CSV files if needed
            studentDataBase.writeToCSV();
            staffDataBase.writeToCSV();
            campCommitteeDataBase.writeToCSV();
        }

    }
}
