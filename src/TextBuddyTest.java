
import org.junit.*;
import static org.junit.Assert.*;

import java.io.IOException;




/**
 * @author Joleeen
 *
 */

public class TextBuddyTest {
	private static final String INPUT_FILE = "mytextfile.txt";
	
	@Test
	public void testClear() throws IOException {
	    TextBuddy.executeCommand("clear");
		assertEquals(0, TextBuddy.getLineCount());
		//not counted because file not specified yet right
	}
	


	
	

}

