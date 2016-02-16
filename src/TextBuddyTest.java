import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Joleeen
 *
 */

public class TextBuddyTest {
	
	@BeforeClass
	public static void initalise() throws IOException {
		final String fileName = "mytextfile.txt";
		TextBuddy.setFileName(fileName);
		TextBuddy.prepareFile(fileName);
	}
	
	@Before
	public void testAdd() throws IOException {
		TextBuddy.add("Marshmallow");
		TextBuddy.add("Lollipop");
		TextBuddy.add("Kit Kat");
		TextBuddy.add("Jelly Bean");
		TextBuddy.add("Ice-cream Sandwich");
		TextBuddy.add("Honeycomb");
		TextBuddy.add("Gingerbread");
		TextBuddy.add("Froyo");
		TextBuddy.add("Eclair");
		TextBuddy.add("Donut");
		TextBuddy.add("Cupcake");
		
		assertEquals(11, TextBuddy.getLineCount());
	}
	
	@Test
	public void testSearch() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("Jelly Bean");
		assertEquals(result, TextBuddy.searching("bean"));
	}
	
	@Test
	public void testSort() {
		ArrayList<String> expected = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		
		expected.add("Cupcake");
		expected.add("Donut");
		expected.add("Eclair");
		expected.add("Froyo");
		expected.add("Gingerbread");
		expected.add("Honeycomb");
		expected.add("Ice-cream Sandwich");
		expected.add("Jelly Bean");
		expected.add("Kit Kat");
		expected.add("Lollipop");
		expected.add("Marshmallow");
		
		TextBuddy.sort();
		results = TextBuddy.getContents();
		assertEquals(expected, results);
	}
	
	@Test
	public void testDelete() {
		//index doesn't change because array shifts
		TextBuddy.delete(1);
		TextBuddy.delete(1);
		assertEquals(9, TextBuddy.getLineCount());
	}
	
	@Test
	public void testSave() throws IOException {
		TextBuddy.save();
		assertEquals(11, TextBuddy.getLineCount());
	}
	
	@After
	public void testClear() throws IOException {
		TextBuddy.clear();
		TextBuddy.save(); //needed to clear existing file for repeated testing
		assertEquals(0, TextBuddy.getLineCount());
	}
}

