package CAMSv2;

import java.util.*;

public class Staff extends User {
    public Staff(name,emailID,faculty,password,role){
        //super(name,emailID, faculty,password,role)
    }

    public void createCamp(){
        CampManager util = new CampManager();
        util.createCamp(this.name);
    }

    public void editCamp() {
        String campName;
        int index;
        Scanner sc = new Scanner(System.in);
        CampManager campManager = new CampManager();
  
        this.viewCamp(); //this prints a table of camp choices

        System.out.println("Enter the index of the camp to delete");
        index = sc.nextInt();
        campName = getCampName(--index); //index is one larger than actual index
        
        // calls campManager
        campManager.editCamp(campName,this.name);
        
    }

    public void deleteCamp() {
        String campName;
        int index;
        Scanner sc = new Scanner(System.in);
        CampManager campManager = new CampManager();
  
        this.viewCamp();
        //take in integer of which camp they wnat to view, i--; and get its camp name from its index

        System.out.println("Enter the index of the camp to delete");
        index = sc.nextInt();
        campName = getCampName(--index);
        
        //delete calls campManager
        campManager.deleteCamp(campName);
    }

    public void changeVisibility(){
        String campName;
        int index;
        Scanner sc = new Scanner(System.in);
        CampManager campManager = new CampManager();
  
        this.viewCamp();
        //take in integer of which camp they wnat to view, i--; and get its camp name from its index

        System.out.println("Enter the index of the camp to change visibility");
        index = sc.nextInt();
        campName = getCampName(--index);
        
        //visibility calls campManager
        campManager.changeVisibility(campName);
        }

    public void viewCamp(){
        //any staff can view all camps
        //all this method does is print out a list of camps
        CampManager campManager = new CampManager();
        ArrayList<Camp> campList = campManager.getCampList();
        int numOfCamps = campList.size();
        Camp camp = new Camp();
        String campName;

        for(int i=0;i<numOfCamps;i++){
            camp = campList.get(i);
            campName = camp.getCampName();
            System.out.println(i+1 + ". "+ campName);
        }//end for

    }//end viewCamp()

    public void myList(){
        // CampManager util = new CampManager();
        //        util.StaffCampListGenerator(this.name);

    }

    public void viewEnquiries(){
        //ask which ccamp name they want to view
        //scann a string
        EnquiryManager enqManager = new EnquiryManager();
        enqManager.viewEnquiry(campName, this.name);
    }

    public void replyEnquiries(){
        EnquiryManager enqManager = new EnquiryManager();
        Scanner sc = new Scanner(System.in);
        String campName;
        System.out.println("which camp's enquiry would you want to reply to? ");
        campName = sc.nextLine();
        enqManager.replyEnquiry(campName, this.name);

    }

    public void viewSuggestion(){
         //ask which ccamp name they want to view
        SuggestionManager suggManager = new SuggestionManager();
        suggManager.viewSuggestion(campName, this.name);
    }

    public void approveAdvice(){
        //ask which ccamp name they want to approve;
        SuggestionManager suggManager = new SuggestionManager();
        suggManager.approveAdvice(campName, this.name);

    }

    public void generateReport(){
        CampManager campManager = new CampManager();
        campManager.generateReport(this.name);
    }


    public String getCampName(int indexOfCamp){
        String campName;
        ArrayList<Camp> campList = new ArrayList<Camp>();
        CampManager campManager = new CampManager();
        Scanner sc = new Scanner(System.in);

        indexOfCamp = sc.nextInt();
        indexOfCamp--;
        sc.close();

        //find name of camp
        campList = campManager.getCampList();
        campName = campList.get(indexOfCamp).getCampName();

        return campName;
    }








}
