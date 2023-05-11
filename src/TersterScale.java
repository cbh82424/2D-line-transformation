import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TersterScale {
	public static void main (String[] args) {
		//Change window dimensions if your monitor resolution is too low
		int width = 1000;
		int height = 1000;
		String input = " ";
		Scanner s = new Scanner(System.in);
		
		int x1[] = {};
		int x2[] = {};
		int y1[] = {};
		int y2[] = {};
		//Format of file must be:
		// **
		// (number of lines
		// (x1 coords separated by a space)
		// (x2 coords separated by a space)
		// (y1 coords separated by a space)
		// (y2 coords separated by a space)
		// **
		File file = new File("src/coords");   
		Scanner scanner;
		int length;
		try {
			scanner = new Scanner(file);
			length = scanner.nextInt();
			x1 = new int[length];
			x2 = new int[length];
			y1 = new int[length];
			y2 = new int[length];
			for (int i = 0; i < length; i++) {
			    x1[i] = scanner.nextInt();
			}
			for (int i = 0; i < length; i++) {
			    x2[i] = scanner.nextInt();
			}
			for (int i = 0; i < length; i++) {
			    y1[i] = scanner.nextInt();
			}
			for (int i = 0; i < length; i++) {
			    y2[i] = scanner.nextInt();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Frame frame = new Frame(width, height, x1, x2, y1, y2);				
		frame.setUpGUI();		
		Graphics g = frame.getGraphics();
		
		while (!(input.equals("q"))) {
			System.out.println("Would you like to perform a transformation on the printed line(s)? \nType \"BT\" for Basic Translation, \"BS\" for Basic Scale, \"BR\" for Basic Rotate, \"S\" for Scale, or \"R\" for Rotate. Type \"q\" to exit.");
			input = s.nextLine();
			if (input.equalsIgnoreCase("BT")) {
				frame.resetT();
				System.out.println("You have chosen Basic Translation");
				System.out.println("Enter your X displacement: ");
				int xDis = s.nextInt();
				System.out.println("Enter your Y displacement: ");
				int yDis = s.nextInt();
				frame.basicTranslate(xDis, yDis);
				g.clearRect(0, 0, height, width);
				frame.repaint();
			} else if (input.equalsIgnoreCase("BS")) {
				frame.resetT();
				System.out.println("You have chosen Basic Scale");
				System.out.println("Enter your X Scale Factor: ");
				double xFac = s.nextDouble();
				System.out.println("Enter your Y Scale Factor: ");
				double yFac = s.nextDouble();
				frame.basicScale(xFac, yFac);
				g.clearRect(0, 0, height, width);
				frame.repaint();
			} else if (input.equalsIgnoreCase("BR")) {
				frame.resetT();
				System.out.println("You have chosen Basic Rotate");
				System.out.println("Enter your Angle of Rotation (Clockwise): ");
				double angle = s.nextDouble();
				frame.basicRotate(angle);
				g.clearRect(0, 0, height, width);
				frame.repaint();
			} else if (input.equalsIgnoreCase("S")) {
				frame.resetT();
				System.out.println("You have chosen Scale");
				System.out.println("Enter your X Scale Factor: ");
				double xFac = s.nextDouble();
				System.out.println("Enter your Y Scale Factor: ");
				double yFac = s.nextDouble();
				System.out.println("Enter your Center X Coordinate: ");
				int xCent = s.nextInt();
				System.out.println("Enter your Center Y Coordinate: ");
				int yCent = s.nextInt();
				frame.scale(xFac, yFac, xCent, yCent);
				g.clearRect(0, 0, height, width);
				frame.repaint();
			} else if (input.equalsIgnoreCase("R")) {
				frame.resetT();
				System.out.println("You have chosen Rotate");
				System.out.println("Enter your Angle of Rotation (Clockwise): ");
				double angle = s.nextDouble();
				System.out.println("Enter your Center X Coordinate: ");
				int xCent = s.nextInt();
				System.out.println("Enter your Center Y Coordinate: ");
				int yCent = s.nextInt();
				frame.rotate(angle, xCent, yCent);
				g.clearRect(0, 0, height, width);
				frame.repaint();
			} else if (input.equalsIgnoreCase("q")) {
				System.out.println("Exiting");
			} else {
				//Failsafe case if invalid input is detected
				System.out.println("Invalid input, please enter one of the options listed.");
			}
		}
	}
}
