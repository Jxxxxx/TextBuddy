import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * TextBuddy program allows user to manipulate text in file through basic functions such as
 * add, delete, display, clear, exit.
 * 
 * Assumptions:
 * File being specified will be a text file.
 * If the text file being specified exists, it will be in the same directory as the program.
 * User have to exit the program before all the changes made will be saved.
 * 
 * @author Joleeen
 *
 */

public class TextBuddy {
    
    private static final String MESSAGE_USAGE = "Usage: java textbuddy <file path>";
    private static final String MESSAGE_WELCOME = "Welcome to Textbuddy!";
    private static final String MESSAGE_PROMPT = "command: ";
    private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
    private static final String MESSAGE_EMPTY_FILE = "%1$s is empty";
    private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
    private static final String MESSAGE_CLEARED = "all content deleted from %1$s";
    private static final String MESSAGE_INVALID = "Error. Invalid command.";
    private static final String MESSAGE_EXIT = "Goodbye!";
    
    private static final int PARAM_SIZE_FOR_RUNNING = 1;
    private static final boolean NEW_LINE_ENABLED = true;
    private static final boolean NEW_LINE_DISABLED = false;
    private static final int FILE_CONTENT_EMPTY = 0;
    private static final int OFFSET_FOR_ZERO = 1;
    
    private static Scanner scanner = new Scanner(System.in);
    private static String fileName = null;
    private static ArrayList<String> contents = new ArrayList<String>();
    
    public static void main(String[] args) throws IOException {
        checkProperUsage(args.length);
        setFileName(args);
        prepareFile(fileName);
        showToUser(MESSAGE_WELCOME, NEW_LINE_ENABLED);
        askForInput();
    }
    
    private static void checkProperUsage(int length) {
        if (length != PARAM_SIZE_FOR_RUNNING) {
            showToUser(MESSAGE_USAGE, NEW_LINE_ENABLED);
            System.exit(1);
        }
    }
    
    private static void setFileName(String[] args) {
        fileName = args[0];
    }
    
    /**
     * Checks if the file specified exist
     * If it doesn't, create new file
     * If it does, read the existing content
     * @param fileName
     * @throws IOException
     */
    private static void prepareFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String existing = reader.readLine();
            while (existing != null) {
                contents.add(existing);
            }
            reader.close();
        }
    }
    
    private static void showToUser(String text, boolean hasNewLine) {
        if (hasNewLine == true) {
            System.out.println(text);
        } else {
            System.out.print(text);
        }
    }
    
    private static void askForInput() throws IOException {
        while (true) {
            showToUser(MESSAGE_PROMPT, NEW_LINE_DISABLED);
            executeCommand();
        }
    }
    
    private static void executeCommand() throws IOException {
        String command = scanner.next();
        
        switch (command) {
            case "add"  :
                add();
                break;
                
            case "display" :
                display();
                break;
                
            case "delete" :
                delete();
                break;
                
            case "clear" :
                clear();
                break;
                
            case "exit" :
                save(fileName);
                exit();
                break;
                
            default :
                showToUser(MESSAGE_INVALID, NEW_LINE_ENABLED);
                break;
        }
    }
    
    private static void add() {
        String input = scanner.nextLine().trim();
        contents.add(input);
        showToUser((String.format(MESSAGE_ADDED, fileName, input)), NEW_LINE_ENABLED);
    }
    
    private static void display() {
        if (contents.size() == FILE_CONTENT_EMPTY) {
            showToUser((String.format(MESSAGE_EMPTY_FILE, fileName)), NEW_LINE_ENABLED);
        } else {
            int index = 1;
            for (int i = 0; i < contents.size(); i++) {
                showToUser(index + ".", NEW_LINE_DISABLED);
                showToUser(contents.get(i), NEW_LINE_ENABLED);
                index++;
            }
        }
    }
    
    private static void delete() {
        int index = getDeleteIndex();
        boolean canDelete = checkBeforeDelete(index);
        deleting(index, canDelete);
    }

    private static int getDeleteIndex() {
		int index = 0;     
		
        try {
            index = scanner.nextInt();
        } catch (Exception e) {
            showToUser(MESSAGE_INVALID, NEW_LINE_ENABLED);
            scanner.next();
        }
		return index;
	}
    
    
    /**
     * Check that file is not empty, or that index specify is not out of range
     * @param index
     * @return
     */
    private static boolean checkBeforeDelete(int index) {
        if (contents.size() == FILE_CONTENT_EMPTY) {
            showToUser((String.format(MESSAGE_EMPTY_FILE, fileName)), NEW_LINE_ENABLED);
        }
        
        if (index <= 0) {
            return false;
        } else if (index > contents.size()) {
            return false;
        }
        
        return true;
    }
    
	private static void deleting(int index, boolean canDelete) {
		if (canDelete) {
        	index = index - OFFSET_FOR_ZERO;
            String deleted = contents.remove(index);
            showToUser((String.format(MESSAGE_DELETED, fileName, deleted)), NEW_LINE_ENABLED);
        } else {
            showToUser(MESSAGE_INVALID, NEW_LINE_ENABLED);
        }
	}
	
    private static void clear() {
        contents.clear();
        showToUser((String.format(MESSAGE_CLEARED, fileName)), NEW_LINE_ENABLED);
    }
    
    private static void save(String fileName) throws IOException {
        writeToFile(fileName);
    }

	/**
	 * Writes the existing contents in array list to file in order to save it
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	private static void writeToFile(String fileName) throws IOException {
		File file = new File(fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        
        for (int i = 0; i < contents.size(); i++) {
            writer.write(contents.get(i));
            writer.newLine();
        }
        
        writer.close();
	}
	
	private static void exit() {
    	showToUser(MESSAGE_EXIT, NEW_LINE_ENABLED);
    	System.exit(0);
    }
}
