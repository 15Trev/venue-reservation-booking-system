package com.techelevator.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;
import com.techelevator.reservation.Reservation;
import com.techelevator.space.Space;

public class Menu {

	private Scanner in = new Scanner(System.in);
	
	private int numberOfDays;
	private Map<Long, Space> spaceMap;
	
	public String getUserInput(String question) {
		System.out.print(question);
		return in.nextLine();
	}
	
	public int getChoiceFromMenu(String[] menuItems, String[] charOptions, int menuQuestion) {
		return getChoiceFromMenu(menuItems, charOptions, menuQuestion, null);
	}
	
	public int getChoiceFromMenu(List<String> menuList, String[] charOptions, int menuQuestion) {
		String[] menuItems = new String[menuList.size()];
		for(int i = 0; i < menuList.size(); i++) {
			menuItems[i] = menuList.get(i);
		}
		return getChoiceFromMenu(menuItems, charOptions, menuQuestion, null);
	}

	public int getChoiceFromMenu(String[] menuItems, String[] charOptions, int menuQuestion, String infoMessage) {
	
		int userChoice = 0;
		
		if(menuQuestion == 0) {
			System.out.println("What would you like to do?");
		} else if(menuQuestion == 1) {
			System.out.println("Which venue would you like to view?");
		} else if(menuQuestion == 2) {
			System.out.println("What would you like to do next?");
		}
		
		
		while (true) {
			for (int i = 0; i < menuItems.length; i++) {
				System.out.printf("\t %1s) %-20s%n", (i + 1), menuItems[i] );
			}
			for (int i = 0; i < charOptions.length; i++) {
				System.out.printf("\t %1s) %-20s%n", charOptions[i].charAt(0), charOptions[i] );
			}
			if (infoMessage != null) {
				System.out.println(infoMessage);
			}
			System.out.print("Select >>> ");
			String choice = in.nextLine();
			System.out.println();
			if(choice.isEmpty() ) {
				System.out.println("Error: Please make a selection");
				System.out.println();
				continue;
			}
			
			if(Character.isLetter(choice.charAt(0))) {
				char choiceAsChar = choice.charAt(0);
				if(menuQuestion == 0 && (choiceAsChar == 'Q' || choiceAsChar == 'q')) {
					userChoice = -1;
					menuQuestion = 0;
				} else if (choiceAsChar == 'R' || choiceAsChar == 'r') {
					userChoice = -2;
					menuQuestion = 0;
				} else if (menuQuestion == 0 && (choiceAsChar == 's' || choiceAsChar == 'S')) {
					userChoice = -3;
					menuQuestion = 0;
				} else if (menuQuestion == 0 && (choiceAsChar == 'd' || choiceAsChar == 'D')) {
					userChoice = -4;
					menuQuestion = 0;
				} else {
					showInvalidChoiceError();
					continue;
				}
				return userChoice;
			}
			
			try {
				userChoice = Integer.parseInt(choice) - 1;
			} catch (NumberFormatException e) {
				showInvalidChoiceError();
				continue;
			}
			
			if (userChoice < 0 || userChoice >= menuItems.length) {
				showInvalidChoiceError();
				continue;
			}
			
			break;	
		}
		return userChoice;
	}
	
	private void showInvalidChoiceError() {
		System.out.println("Invalid choice, please make another selection.");
		System.out.println();
	}
	
	public void displayVenueDetails(String venueDetails) {
		System.out.println(venueDetails);
	}
	
	private String convertNumberToMonth(int month) {
		String[] monthsAsStrings = {"", "Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.", "Jul.", "Aug.", "Sept.", "Oct.", "Nov.", "Dec."}; 
		return monthsAsStrings[month];
	}
	
	public void displaySpaces(String venueName, List<Space> spaces) {
		System.out.println(venueName + " Spaces");
		System.out.println();
		System.out.printf("%-5s %-35s %-7s %-7s %-15s %-25s", "", "Name", "Open", "Close", "Daily Rate", "Max. Occupancy");
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------------------------------------");
		int count = 1;
		for(Space s: spaces) {
			System.out.printf("%-5s %-35s %-7s %-7s $%-15s %-25d \n", "#" + count, s.getName(), convertNumberToMonth(s.getOpenFrom()), convertNumberToMonth(s.getOpenTo()), String.valueOf(s.getDailyRate()) + ".00", s.getMaxOccupancy());
			count ++;
			if(count == 6) { 
				break; 
			}
		}
		System.out.println();
	}
	
	public LocalDate scanForDate() {
		LocalDate date = null;
		while(true) {
			System.out.print("When do you need the space? ");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			try {
				date = LocalDate.parse(in.nextLine(), formatter);
				if(date.compareTo(LocalDate.now()) < 0) {
					System.out.println("You cannot reserve for a past date.");
					System.out.println();
					continue;
				}
				break;
			} catch(Exception e) {
				System.out.println("That is not a date. Please format as MM/DD/YYYY");
				System.out.println();
				continue;
			}
		}
		return date;
	}
	
	public int scanForDays() {
		int days = 0;
		while(true) {
			System.out.print("How many days will you need the space? ");
			try {
				days = Integer.parseInt(in.nextLine());
				if(days <= 0) {
					System.out.println("Please input a positive number!");
					System.out.println();
					continue;
				}
				this.numberOfDays = days;
				break;
			} catch (NumberFormatException e) {
				System.out.println("That is not a number.");
				continue;
			}
		}
		return days;
	}
	
	public int scanForAttendance() {
		int attendance = 0;
		while(true) {
			System.out.print("How many people will be in attendance? ");
			try {
				attendance = Integer.parseInt(in.nextLine());
				if(attendance <=0) {
					System.out.println("Please input a positive number!");
					System.out.println();
					continue;
				}
				break;
			} catch (NumberFormatException e) {
				System.out.println("That is not a number.");
				continue;
			}
		}
		return attendance;
	}
	
	public String printSpaces(List<Space> availableSpaces) {
		if(availableSpaces.isEmpty()) {
			System.out.println();
			System.out.println("Sorry, there are no spaces available based on your input criteria.");
			System.out.print("Would you like to try a different search? (Y)es or (N)o >>> ");
			while(true) {
				String response = in.nextLine();
				if(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n") ) {
					System.out.println();
					return response;
				} else {
					System.out.print("Please choose (Y)es or (N)o >>> ");
				}
			}
			
		} else {
			System.out.println();
			System.out.println();
			System.out.println("The following spaces are available based on your needs:");
			System.out.println();
			System.out.printf("%-8s %-37s %-15s %-15s %-15s %-15s \n", "Space #", "Name", "Daily Rate", "Max. Occupancy", "Accessible", "Total Cost");
			System.out.println("--------------------------------------------------------------------------------------------------------------------------");
			
			Map<Long, Space> spaceMap = new HashMap<Long, Space>();
			for(Space s: availableSpaces) {
				spaceMap.put(s.getSpaceId(), s);
				String accessible;
				if(s.isAccessible()) {
					accessible = "Yes";
				} else {
					accessible = "No";
				}
				
				System.out.printf("%-8d %-37s $%-15s %-15d %-15s $%-15s \n", s.getSpaceId(), s.getName(), String.valueOf(s.getDailyRate()) + ".00", s.getMaxOccupancy(), accessible, String.valueOf(s.getDailyRate() * numberOfDays) + ".00");
				
			}
			System.out.println();
			this.spaceMap = spaceMap;
			return "";
		}
	}
	
	public Long reserveSpace() {
		Long spaceId = null;
		while(true) {
			
			System.out.print("Which space would you like to reserve (enter 0 to cancel)? ");
			
			try {
				spaceId = Long.parseLong(in.nextLine());
			} catch (Exception e) {
				System.out.println("That was not a number");
				System.out.println();
				continue;
			}
			
			if(spaceId == 0) { break; }
			if(!spaceMap.containsKey(spaceId)) {
				System.out.println("Error, please type in the space # or 0.");
				System.out.println();
				continue;
			} else {
				break;
			}
			
		}
		System.out.println();
		return spaceId;
		
	}
	
	public String getReservationName() {
		System.out.print("Who is this reservation for? ");
		return in.nextLine();
	}
	
	public void printConfirmation(String confirmation) {
		System.out.println();
		System.out.println("Thanks for submitting your reservation! The details for your event are listed below:");
		System.out.println();
		System.out.print(confirmation);
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	public void displayReservations(List<Reservation> reservations) {
		if(reservations.isEmpty()) {
			System.out.println("Your reservation could not be found.");
		} else { 
			System.out.println("These are your reservations for ** " + reservations.get(0).getVenueName() + " **");
			System.out.println();
			System.out.printf("%-30s %-15s %-15s %-15s %-15s \n", "Space Name", "Attendees", "Start Date", "End Date", "Total Cost");
			System.out.println("------------------------------------------------------------------------------------");
		
			for(Reservation r: reservations) {
				Long duration = ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());
				System.out.printf("%-30s %-15s %-15s %-15s $%-15s \n", r.getSpaceName(), r.getNumberOfAttendees(), r.getStartDate(), r.getEndDate(), String.valueOf(r.getDailyRate()*duration) + ".00");
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public boolean scanForAccessibility() {
		System.out.print("Does the space require accessibility accomodations? (Y)es or (N)o? >>> ");
		while(true) {
			String response = in.nextLine();
			if(response.equalsIgnoreCase("y")) {
				return true;
			} else if(response.equalsIgnoreCase("n") ) {
				return false;
			} else {
				System.out.print("Please choose (Y)es or (N)o >>> ");
				continue;
			}
		}
	}
	public int scanForBudget() {
		int budget = 0;
		while(true) {
			System.out.print("What is your daily budget for the event? ");
			try {
				budget = Integer.parseInt(in.nextLine());
				if(budget <=0) {
					System.out.println("Please enter a postive whole number");
					budget = 0;
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("Please enter a postive whole number");
				budget = 0;
				continue;
			}
		}
		return budget;
	}
	
	public List<String> scanForCategories(List<String> categories) {
		List<String> categoryNames = new ArrayList<String>();
		int count = 1;
		
		while(true) {
			System.out.println("Which of the categories would you like to include?");
		
			for(String s: categories) {
				System.out.println("\t" + count + ") " + s);
				count++;
			}
			System.out.println("\tN) None");
			System.out.print("Please enter category numbers separated by commas >>> ");
			String userInput = in.nextLine();
			System.out.println();
			if( userInput.contains("N") || userInput.contains("n") ) {
				break;
			}
			userInput = userInput.replaceAll(" ", "");
			String[] eachCategory = userInput.split(",");
			
			try {
				for(int i = 0; i < eachCategory.length; i++) {
					int index = Integer.parseInt(eachCategory[i]);
					categoryNames.add(categories.get(index-1));
				}
				break;
			} catch (Exception e) {
				System.out.println("Please enter category numbers separated by commas");
				System.out.println();
				continue;
			}
		}
		
		return categoryNames;
	}
	
	public void displayAdvancedResults(Map<String, List<Space>> advancedResults, LocalDate date, int duration, boolean accessibility) {
		if(advancedResults.isEmpty()) {
			System.out.println("There are no spaces available from the specified request.");
		} else {
			System.out.println("The following venues and spaces are available based on your needs:");
			this.spaceMap = new HashMap<Long, Space>();
			for(Entry<String, List<Space>> e : advancedResults.entrySet()) {
				System.out.println();
				List<Space> spaceList = e.getValue();
				System.out.println(e.getKey());
				System.out.println();
			
				System.out.printf("%-15s %-35s %-15s %-15s %-15s %-15s \n", "Space #", "Space Name", "Daily Rate", "Max Occup.", "Accessible?", "Total Cost");
				System.out.println("----------------------------------------------------------------------------------------------------------------");
				int count = 1;
				for(Space s: spaceList) {
					String accessible = "";
					if(s.isAccessible()) {
						accessible = "Yes";
					} else {
						accessible = "No";
					}
					System.out.printf("%-15s %-35s $%-15s %-15s %-15s $%-15s \n", s.getSpaceId(), s.getName(), String.valueOf(s.getDailyRate()), s.getMaxOccupancy(), accessible, String.valueOf(s.getDailyRate()*duration));
					spaceMap.put(s.getSpaceId(), s);
					count ++;
					if(count == 6) { 
						break; 
					}
				}
				System.out.println();
			}
		}
		System.out.println();
	}

	public void displayUpcomingReservations(List<Reservation> reservations) {
		System.out.println("The following reservations are coming up in the next 30 days: ");
		System.out.println();
		System.out.printf("%-30s %-30s %-30s %-15s %-15s \n", "Venue Name", "Space Name", "Reserved For", "Start Date", "End Date");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------");
		for(Reservation r: reservations) {
			System.out.printf("%-30s %-30s %-30s %-15s %-15s \n", r.getVenueName(), r.getSpaceName(), r.getReservedFor(), r.getStartDate(), r.getEndDate());
		}
		System.out.println();
		System.out.println();
	}
	
	public void displayStart() {
		displayLogo();
	}
	
	private void displayLogo() {
				System.out.println("   __              _     _");            
				System.out.println("  /__\\_  _____ ___| |___(_) ___  _ __");
				System.out.println(" /_\\ \\ \\/ / __/ _ \\ / __| |/ _ \\| '__|" );
				System.out.println("//__  >  < (_|  __/ \\__ \\ | (_) | |  "); 
				System.out.println("\\__/ /_/\\_\\___\\___|_|___/_|\\___/|_|  ");
				System.out.println("   - VENUE RESERVATION SYSTEMS -       ");
				
				System.out.println();
				System.out.println();
	}
	
	public void displayQuit() {
		System.out.println("Thank you for using:");
		displayLogo();
	}
}
