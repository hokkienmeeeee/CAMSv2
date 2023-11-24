package CAMSv2;

/**
 * The {@code CampCommitteeMemberController} class represents Controller for Camp Committee Member, managing their actions and interactions within the system.
 *  @author Zhu Yu Hao
 *  @since 13-11-2023
 */

public class CampCommitteeMemberController extends StudentController {
    private CampCommitteeMember ccm;
    private CampCommitteeMemberView ccmView;

    /**
     * Constructor for CampCommitteeMemberController.
     *
     * @param ccm   The CampCommitteeMember object.
     * @param view  The CampCommitteeMemberView object.
     */

    public CampCommitteeMemberController(CampCommitteeMember ccm, CampCommitteeMemberView view) {
        super(ccm, view);
        this.ccm = ccm;
        this.ccmView = view;
    }

    /**
     * Overrides the handling of the student menu choices.
     *
     * @param choice The selected menu option.
     * @return True if the operation is successful and the loop should continue, false otherwise.
     */
    @Override
    protected boolean handleStudentMenu(int choice) {
        switch (choice) {
            case 1:
                return enterChangePassword();
            case 2:
                return enterDisplayListOfCampsAvailable();
            case 3:
                return enterDisplayRegisteredCampAndRole(ccm.getRegisteredCamps(), ccm.getCamp());
            case 4:
                return enterDisplayProfile();
            case 5:
                return enterEnquiriesMenu();
            case 6:
                return enterCampSpecificOptions();
            case 111:
                return false; // Exit the loop
            default:
                return true; // Invalid choice, continue loop
        }
    }

    /**
     * Enters specific camp-related options based on the committee member's role.
     */
    @Override
    protected boolean enterDisplayProfile() {
        ccmView.displayHeader("PROFILE");
        ccm.displayProfile();
        return true;
    }

    @Override
    public boolean enterCampSpecificOptions() {
        super.camp = super.handleCampSelection();
        if (camp == null) {return true;}
        
        boolean running = true;
        while (running) {
            boolean studentRegistered = camp.isStudentRegistered(user.getName());
            System.out.println("student registered: " + studentRegistered);
            boolean isCCM = (camp.equals(ccm.getCamp()));
            // view depends on whether camp is CCM of, and then if it is no CCM camp, check for registration
            ccmView.displayCampSpecificOptions(studentRegistered, isCCM);
            view.displayReturnToPreviousPage();
            int choice = sc.nextInt();
            sc.nextLine();

            if (!isCCM) {
                running = handleCampSpecificOptions(choice, studentRegistered, new GoToMainMenu());                
            }
            else {
                // no registration/withdrawal check since user is CCM and thus cannot leave the camp
                running = handleCampCommitteeMemberMenu(choice);
            }

        }
        return true;
    }

    /**
     * Handles camp suggestions, allowing committee members to submit, view, edit, or delete suggestions.
     */
    protected void handleCampSuggestions() {
        boolean running = true;
        while (running) {
            ccmView.displayHeader("CAMP SUGGESTIONS");
            ccmView.displaySuggestionsMenu();
            ccmView.displayReturnToPreviousPage();
            int choice = sc.nextInt();
            sc.nextLine();
            running = handleCampSuggestionsSwitch(choice);
        }
    }

    /**
     * Handles the switch for camp suggestions, directing functionality based on user choice.
     * @param choice The selected option.
     * @return True if the operation is successful and the loop should continue, false otherwise.
     */
    protected boolean handleCampSuggestionsSwitch(int choice) {
        switch (choice) {
            case 1:
                handleSubmitSuggestion();
                return true;
            case 2:
                handleViewSuggestion();
                return true;
            case 3:
                handleEditSuggestion();
                return true;
            case 4:
                handleDeleteSuggestion();
                return true;
            case 111:
                return false;
            default:
                ccmView.displaySelectValidOption();
                return true;
        }
    }

    /**
     * Submits a suggestion to the camp.
     */
    protected void handleSubmitSuggestion() {
        ccmView.displaySubmitSuggestion();
        String suggestion = sc.nextLine();
        ccm.submitSuggestion(suggestion);
        ccm.addPointsByOne();
    }

    /**
     * Handles viewing a suggestion given by the committee member.
     */
    protected void handleViewSuggestion() {
        Suggestion suggestion = camp.getSuggestionBySuggester(ccm.getName());
        if (suggestion == null) {return;}
        ccmView.displayAllAdvices(suggestion);
    }

    /**
     * Handles acquiring a suggestion for editing or deletion.
     * @return The acquired suggestion.
     */
    protected Advice acquireSuggestion() {
        ccmView.displayGetSuggestionIndex();
        int index = sc.nextInt() - 1;
        sc.nextLine();

        Suggestion suggestion = camp.getSuggestionBySuggester(ccm.getName());
        if (suggestion == null) {return null;}
        // get the advice
        return suggestion.getAdviceList().get(index);
    }

    /**
     * Handles editing a suggestion given by the committee member.
     */
    protected void handleEditSuggestion() {
        Advice advice = acquireSuggestion();
        ccmView.displaySubmitSuggestion();
        String newSuggestion = sc.nextLine();
        advice.setNewAdvice(newSuggestion);
        System.out.println("Successfully set new suggestion!");
    }

    /**
     * Handles deleting a suggestion given by the committee member.
     */
    protected void handleDeleteSuggestion() {
        handleViewSuggestion();
        Advice advice = acquireSuggestion();
        Suggestion suggestion = camp.getSuggestionBySuggester(ccm.getName());
        suggestion.getAdviceList().remove(advice);
        System.out.println("Successfully removed Suggestion");        
    }

    /**
     * Handles the switch for camp enquiries, directing functionality based on user choice.
     * @param choice The selected option.
     * @return True if the operation is successful and the loop should continue, false otherwise.
     */
    protected boolean handleCampEnquiriesSwitch(int choice) {
        switch (choice) {
            case 1:
                camp.printEnquiriesList();
                return true;
            case 2:
                handleReplyToEnquiry();
                return true;
            case 111:
                return false;
            default:
                ccmView.displaySelectValidOption();
                return true;
        }
    }

    /**
     * Handles replying to a camp enquiry.
     */
    protected void handleReplyToEnquiry() {
        camp.printEnquiriesList();
        ccmView.displayGetEnquiryId();
        int id = sc.nextInt();
        sc.nextLine();

        Question question = camp.getEnquiryFromCamp(id);
        if (question == null) {
            System.out.println("Please provide a valid EnquiryId!");
            return;
        }
        ccmView.displayGetReply();
        String reply = sc.nextLine();
        question.getReplies().add(new Reply(ccm.getName(), reply));
        camp.printEnquiriesList();

        // increment student points
        ccm.addPointsByOne();
        System.out.println("Successfully sent reply!");
    }

    /**
     * Handles camp enquiries, allowing committee members to view enquiries and reply to them.
     */
    protected void handleCampEnquiries() {

        boolean running = true;
        while (running) {
            ccmView.displayHeader("CAMP ENQUIRIES");
            ccmView.displayCampEnquiriesMenu();
            ccmView.displayReturnToPreviousPage();
            int choice = sc.nextInt();
            sc.nextLine();
            running = handleCampEnquiriesSwitch(choice);
        }
    }

    /**
     * Handles generating a camp attendance report.
     */
    protected void handleCampAttendanceReport() {

    }

    /**
     * Handles the menu options specific to CampCommitteeMember.
     * @param choice The selected menu option.
     * @return True if the operation is successful and the loop should continue, false otherwise.
     */
    protected boolean handleCampCommitteeMemberMenu(int choice) {
        // Implement additional menu options specific to CampCommitteeMember
        switch (choice) {
            case 1:
                return super.handleSubmitEnquiryToCamp();
            case 2:
                return handleViewRemainingTimeSlots();
            case 3:
                ccmView.displayCampDetails(camp);
                break;
            case 4:
                handleCampSuggestions();
                break;  
            case 5:
                handleCampEnquiries();
                break;
            case 6:
                handleCampAttendanceReport();
                break;

            case 111:
                return false;
        }
        return true;
    }

    protected void handleDisplayCampDetails() {
        view.displayHeader("Camp Details");
        ccmView.displayCampDetails(camp);
    }
}