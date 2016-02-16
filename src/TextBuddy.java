import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * TextBuddy program allows user to manipulate text in file through basic functions such as
 * add, display, search, sort, delete, clear, exit.
 *
 * Assumptions:
 * File being specified will be a text file.
 * If the text file being specified exists, it will be in the same directory as the program.
 * Commands accepted are in lower case.
 * User have to exit the program before all the changes made will be saved.
 *
 *
 * @author Joleeen
 *
 */

public class TextBuddy {
    
    private static final String MESSAGE_USAGE = "Usage: java TextBuddy <file path>";
    private static final String MESSAGE_WELCOME = "Welcome to Textbuddy!";
    private static final String MESSAGE_PROMPT = "command: ";
    private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
    private static final String MESSAGE_EMPTY_FILE = "%1$s is empty";
    private static final String MESSAGE_SEARCH_FAIL = "nothing contains %1$s";
    private static final String MESSAGE_SORTED = "all content in %1$s is sorted";
    private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
    private static final String MESSAGE_CLEARED = "all content deleted from %1$s";
    private static final String MESSAGE_INVALID = "error. invalid command.";
    private static final String MESSAGE_EXIT = "Goodbye!";
    
    private static final int PARAM_SIZE_FOR_RUNNING = 1;
    private static final boolean NEW_LINE_ENABLED = true;
    private static final boolean NEW_LINE_DISABLED = false;
    private static final int CONTENT_EMPTY = 0;
    private static final int OFFSET_FOR_ZERO = 1;
    
    private static Scanner scanner = new Scanner(System.in);
    private static String fileName = null;
    private static ArrayList<String> contents = new ArrayList<String>();
    
    public static void main(String[] args) throws IOException {
        checkProperUsage(args.length);
        setFileName(args[0].toString());
        prepareFile(fileName);
        showToUser(MESSAGE_WELCOME, NEW_LINE_ENABLED);
        loopingForInput();
    }
    
    private static void checkProperUsage(int length) {
        if (length != PARAM_SIZE_FOR_RUNNING) {
            showToUser(MESSAGE_USAGE, NEW_LINE_ENABLED);
            System.exit(1);
        }
    }
   
    public static void setFileName(String args0) {
        fileName = args0;
    }
    
    /**
     * Checks if the file specified exist
     * If it doesn't, create new file
     * If it does, read the existing content until end of file
     * @param fileName
     * @throws IOException
     */
    public static void prepareFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String existing = reader.readLine();
            while (existing != null) {
                contents.add(existing);
                existing = reader.readLine();
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
    
    private static void loopingForInput() throws IOException {
        while (true) {
            String command = askForInput();
            executeCommand(command);
        }
    }
    
    private static String askForInput() throws IOException {
        showToUser(MESSAGE_PROMPT, NEW_LINE_DISABLED);
        return scanner.next();
    }
    
    public static void executeCommand(String command) throws IOException {
        switch (command) {
            case "add" :
                add(getInputAfterCommand());
                break;
                
            case "display" :
                display();
                break;
                
            case "search" :
                search(getInputAfterCommand());
                break;
                
            case "sort" :
                sort();
                break;
                
            case "delete" :
                delete(getDeleteIndex());
                break;
                
            case "clear" :
                clear();
                break;
                
            case "exit" :
                save();
                exit();
                break;
                
            default :
                showToUser(MESSAGE_INVALID, NEW_LINE_ENABLED);
                break;
        }
    }
    
    private static String getInputAfterCommand() {
    	return scanner.nextLine().trim();
    }
    
    public static void add(String input) {
        contents.add(input);
        showToUser((String.format(MESSAGE_ADDED, fileName, input)), NEW_LINE_ENABLED);
    }

    public static void display() {
        if (contents.size() == CONTENT_EMPTY) {
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
    
    /**
     * Search through existing contents that contains the search term
     */
    public static void search(String searchTerm) {       
        ArrayList<String> results = new ArrayList<String>();
        results = searching(searchTerm);
        displaySearched(results, searchTerm);
    }
    
    public static ArrayList<String> searching(String searchTerm) {
        ArrayList<String> results = new ArrayList<String>();
        
        for (int i = 0; i < contents.size(); i++ ) {
            if (contents.get(i).toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(contents.get(i));
            }
        }
        
        return results;
    }
    
    private static void displaySearched(ArrayList<String> results, String searchTerm) {
        if (results.size() == CONTENT_EMPTY ) {
            showToUser((String.format(MESSAGE_SEARCH_FAIL, searchTerm)), NEW_LINE_ENABLED);
        } else {
            int index = 1;
            for (int i = 0; i < results.size(); i++) {
                showToUser(index + ".", NEW_LINE_DISABLED);
                showToUser(results.get(i), NEW_LINE_ENABLED);
                index++;
            }
        }
    }
    
    /**
     * Sorts the existing content in array alphabetically
     */
    public static void sort() {
        Collections.sort(contents, String.CASE_INSENSITIVE_ORDER);
        showToUser((String.format(MESSAGE_SORTED, fileName)), NEW_LINE_ENABLED);
    }
    
    /**
     * Deletes a specific line
     */
    public static void delete(int index) {
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
        if (contents.size() == CONTENT_EMPTY) {
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
    
    /**
     * Clears all existing contents
     */
    public static void clear() {
        contents.clear();
        showToUser((String.format(MESSAGE_CLEARED, fileName)), NEW_LINE_ENABLED);
    }
    
    public static void save() throws IOException {
        writeToFile(fileName);
    }
    
    /**
     * Writes the existing contents in array list to file in order to save it
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
 
    
    /**
     * Testing methods below
     */    
    public static ArrayList<String> getContents() {
    	return contents;
    }
    
    public static int getLineCount() {
        return contents.size();
    }
}
