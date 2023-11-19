package CAMSv2;

import java.util.ArrayList;
/** 
 Representing a Student user registered in the system.
 @author Zhu Yu Hao
 @since 13-11-2023
 */
public class Student extends User{
    ArrayList<Camp> registeredCamps = new ArrayList<Camp>();
    Enquiries enquiries;
    public Student(String name, String emailID, UserGroup faculty, String password, Role role) {
        super(name, emailID, faculty, password, role);
        // setup the enquiries and registeredCamps
        enquiries = CampManager.setUpStudentEnquiries(name);
        registeredCamps = CampManager.setUpStudentRegisteredCamps(name);
    }
    // Student User Interface

    public Camp ifCampNameInAvailableListOfCamps(String campName) {
        Camp camp = CampManager.getCamp(campName);
        if (camp == null) {
            System.out.println("Camp not found!");
            return null;
        }
        else if (!CampManager.getCampListByFacultyAndVisibility(faculty).contains(camp)) {
            System.out.println("The chosen camp is not available to you.");
            return null;
        } 
        else {
            return camp;
        }
    }

    public ArrayList<Camp> getRegisteredCamps() {
        return registeredCamps;
    }

    public void viewRemainingCampSlots(String campName) {
        // get Camp from CampManager
        Camp camp = CampManager.getCamp(campName);
        // Display available slots
        System.out.println( camp.getCampName() + " Available Slots: " + (camp.getTotalSlots() - camp.getStudentList().size()));
        // Camp Manager should check that the camp is Visible and userGroup
    }



    // --- Enquiries -----------------------------------------------------
    public void createEnquiry(String description, Camp camp) {

        Question question = new Question(description, camp.getCampName(), EnquiryManager.getEnquiryCounter());
        enquiries.addQuestion(question);
        EnquiryManager.createEnquiry(question, camp, getName());
    }

    public Question getEnquiryById(int id) {
        for (Question enquiry : enquiries.getQuestions()) {
            if (enquiry.getQuestionId() == id) {
                return enquiry;
            }
        }
        return null;
    }

    /**
     * Don't need to change the Camp Question because they are the same reference.
     * @param question
     * @param description
     */
    public void editEnquiry(Question question, String description) {
        question.setQuestion(description);   
    }

    public void deleteEnquiry(int Id, Camp camp) {
        ArrayList<Question> questions = enquiries.getQuestions();
        Question thisQuestion = null;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getQuestionId() == Id) {
                // remove
                thisQuestion = questions.get(i);
                questions.remove(i);
                // debugging message
                System.out.println("Deleted the Enquiry!");
                break;
            }
        }
        if (thisQuestion == null) {
            System.out.println("No such Enquiry!");
            return;
        }
        else {
            camp.deleteEnquiry(thisQuestion, getName());            
        }



    }

    public void deleteAllEnquiries() {
        enquiries = new Enquiries(super.name);
    }

    public Enquiries getEnquiries() {
        return enquiries;
    }

    // --- Registration--------------------------------------
    public boolean canRegisterCamp(Camp camp) {
        if (camp.isStudentInBlackList(this)) {
            System.out.println("You have been blacklisted from this camp!");
            return false;            
        }
        else if(camp.isCampFull()) {
            System.out.println("Camp is full, select another camp!");
            return false;
        }
        else if (camp.isRegistrationOverDeadline()) {
            System.out.println("Over Registration deadline!");
            return false;            
        }
        return true;
        
    }

    public void registerCampRole(Role role, Camp camp) {
        // to be replaced with factory;
        if (role.equals(Role.CAMP_COMMITTEE_MEMBER)) {
            // could potentially look at initially starting with CAMP COMMITTEE MEMBER, then Downcasting all of them later.
            CampCommitteeMember campCommitteeMember = new CampCommitteeMember(emailID, password, faculty, name, role, camp);
            camp.addCampCommitteeMember(campCommitteeMember);
            camp.addStudent(this);
            // append into database of Camp Committee Member
            CampCommitteeDataBase.getInstance().getCampCommitteeMembersList().add(campCommitteeMember);
            CampCommitteeDataBase.getInstance().writeToCSV();
            System.out.println("REMINDER: Please Re-Login to access Camp Committee Member Privileges...");
        }
        else if (role.equals(Role.STUDENT)) {
            camp.addStudent(this);
        }
        camp.printStudentList();
        registeredCamps.add(camp);

    }

    public void withdrawFromCamp(Camp camp) {
        // if camp committee member should remove from camp
        camp.getStudentList().remove(this);
        camp.addToBlackList(this);
        System.out.println("Withdrawn from camp");
        
    }

}

