package com.techelevator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.techelevator.reservation.Reservation;
import com.techelevator.space.Space;
import com.techelevator.view.Menu;

public class ExcelsiorCLI {
	
	private static Menu menu;
	private static ExcelsiorController controller = new ExcelsiorController();
	
	private final static String[] MAIN_MENU = { "List Venues" };
	private final static String[] MAIN_MENU_CHARS = { "Search for a Space", "Display Reservations", "Quit" };
	private final static int ESCAPE_Q = -1;
	private final static int ESCAPE_R = -2;
	private final static int ADVANCED_VENUE_SEARCH = -3;
	private final static int DISPLAY_RESERVATIONS = -4;
	
	private final static List<String> VENUES_LIST = controller.getVenues();
	private final static String[] VENUE_MENU_CHARS = { "Return to Previous Screen" };
	
	private final static String[] VENUE_DETAILS_MENU = { "View Spaces", "Search for Reservation" };
	private final static String[] RESERVE_SPACE_MENU = { "Reserve a Space" };
	
	private final static String[] RETURN_CHAR = { "Return to Previous Screen" };


	public void run() {
		menu.displayStart();
		boolean exit = false;
		
//Main Menu	
		while (!exit) {
			int menuQuestion = 0;
			int choice = menu.getChoiceFromMenu(MAIN_MENU, MAIN_MENU_CHARS, menuQuestion);
			
			if (choice == ESCAPE_Q) { 
				exit = true;
				menu.displayQuit();
				break; 
			}
			if (choice == DISPLAY_RESERVATIONS) {
				List<Reservation> displayList = controller.findUpcomingReservations();
				menu.displayUpcomingReservations(displayList);
				continue;
			}
			
			if (choice == ADVANCED_VENUE_SEARCH) {
				List<String> categoryList = controller.getCategoryList();
				LocalDate date = menu.scanForDate();
				int days = menu.scanForDays();
				int attendance = menu.scanForAttendance();
				boolean isAccessible = menu.scanForAccessibility();
				int budget = menu.scanForBudget();	
				List<String> categories = menu.scanForCategories(categoryList);
				Map<String, List<Space>> usableSpaces = controller.findUsableSpaces(date, days, attendance, isAccessible, budget, categories);
				menu.displayAdvancedResults(usableSpaces, date, days, isAccessible);
				
				if(usableSpaces.isEmpty()) {
					continue;
				}
				
				Long advancedSpaceId = menu.reserveSpace();
				
				if(advancedSpaceId == 0) {
					continue;
				}
				
				String advancedReservationName = menu.getReservationName();
				String advancedConfirmation = controller.makeReservation(advancedSpaceId, advancedReservationName, date, days, attendance);
				menu.printConfirmation(advancedConfirmation);
				continue;
			}
			
			menuQuestion = 1;
			boolean keepGoing = true;
//View Venues Loop					
			while(keepGoing) {
				int choice2 = menu.getChoiceFromMenu(VENUES_LIST, VENUE_MENU_CHARS, menuQuestion);
				
				if(choice2 == ESCAPE_R) {
					menuQuestion -= 1;
					break;
				} 
				
				String details = controller.getVenueDetailsPage(choice2);
				
// Venue Details		
				while(keepGoing) {				
				
					menu.displayVenueDetails(details);
					menuQuestion = 2;

					int choice3 = menu.getChoiceFromMenu(VENUE_DETAILS_MENU, RETURN_CHAR, menuQuestion);
					if (choice3 == ESCAPE_R) {
						menuQuestion -=1;
						break;
					}
					if (choice3 == 0) {

// List Venue Spaces							
						while(keepGoing) {
							menu.displaySpaces(controller.venueNameForSpaceDetails, controller.getSpaces(controller.venueIdForSpaceDetails));
							menuQuestion = 3;
							int choice4 = menu.getChoiceFromMenu(RESERVE_SPACE_MENU, RETURN_CHAR, menuQuestion);
							
							if(choice4 == ESCAPE_R) {
								menuQuestion -= 1;
								break;
							} 
							
							else if (choice4 == 0) {
								while(keepGoing) {
									LocalDate date = menu.scanForDate();
									int duration = menu.scanForDays();
									int attendance = menu.scanForAttendance();
									
									List<Space> availableSpaces = controller.findUsableSpaces(date, duration, attendance);
									String searchAgain = menu.printSpaces(availableSpaces);
									if(searchAgain.equalsIgnoreCase("y")) { 
										continue; 
									} else if(searchAgain.equalsIgnoreCase("n")) { 
										break; 
									}
																
// Reserve a Space
									Long spaceId = menu.reserveSpace();
									
									if(spaceId == 0) {
										break;
									}
									
									String reservationName = menu.getReservationName();
									String confirmation = controller.makeReservation(spaceId, reservationName, date, duration, attendance);
									menu.printConfirmation(confirmation);
									keepGoing = false;
									break;
								}
							}
						}
						
					} else if (choice3 == 1) {
						//reserved for searching for reservations
						String reservationName = menu.getReservationName();
						List<Reservation> reservations = controller.getReservations(reservationName);
						menu.displayReservations(reservations);
						// what is the name on reservation?
						// display reservations
						}
							
				} // end Venue Details
			
			} // end Venue Loop
			
		} // end Main Menu
		
	} // end run()
	
	private ExcelsiorCLI(Menu thisMenu) {
		menu = thisMenu;
	}

	public static void main(String[] args) {
		menu = new Menu();
		ExcelsiorCLI application = new ExcelsiorCLI(menu);
		application.run();
	}

}
