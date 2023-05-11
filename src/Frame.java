import java.awt.Graphics;

import javax.swing.JFrame;

public class Frame extends JFrame {

	private int width;
	private int height;
	int lines;
	int x1Coords[];
	int x2Coords[];
	int y1Coords[];
	int y2Coords[];
	boolean useBres;
	int translationCase = 0;
	double t[][];
	
	
	/**
	 * Constructs a frame to display the generated lines. Coordinates are provided from arrays
	 * for testing purposes. Coordinates are randomly generated in the tester class. 
	 * @param w : Width of the Frame
	 * @param h : Height of the Frame
	 * @param x1 : x1 Coordinates for lines
	 * @param x2 : x2 Coordinates for lines
	 * @param y1 : y1 Coordinates for lines
	 * @param y2 : y2 Coordinates for lines
	 * @param useB : Selects which algorithm to use to draw lines
	 */
	public Frame(int w, int h, int[] x1, int[] x2, int[] y1, int[] y2, boolean useB) {
		width = w;
		height = h;
		x1Coords = x1;
		x2Coords = x2;
		y1Coords = y1;
		y2Coords = y2;
		useBres = useB;

	}
	
	public Frame(int w, int h, int[] x1, int[] x2, int[] y1, int[] y2) {
		width = w;
		height = h;
		x1Coords = x1;
		x2Coords = x2;
		y1Coords = y1;
		y2Coords = y2;
		useBres = true;
		t = new double[3][3];
		t[0][0] = 1;
		t[1][1] = 1;
		t[2][2] = 1;
	}
	
	//Resets the transformation Matrix
	public void resetT() {
		t[0][0] = 1;
		t[0][1] = 0;
		t[0][2] = 0;
		t[1][0] = 0;
		t[1][1] = 1;
		t[1][2] = 0;
		t[2][0] = 0;
		t[2][1] = 0;
		t[2][2] = 1;
	}
	
	
	//Matrix Multiplication algorithm for the case 1x3 * 3x3
	public double[] multiplyMatrices(double[] a, double[][] b) {
		double[] result = new double[3];
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++)
                    result[j] += (a[k]) * (b[k][j]);
            }
		return result;
	}
	
	//Matrix Multiplication algorithm for the case 3x3 * 3x3
	public double[][] multiplyMatrices3x3(double[][] a, double[][] b) {
		double[][] result = new double[3][3];
		 for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                for (int k = 0; k < 3; k++)
	                    result[i][j] += a[i][k] * b[k][j];
	            }
	        }
		return result;
	}
	
	//Translates the drawn lines by the provided x and y offsets
	public void basicTranslate(int x, int y) {
		this.t[2][0] = x;
		this.t[2][1] = y;
	}
	
	//Scales the drawn lines by the provided x and y factors from the origin
	public void basicScale(double x, double y) {
		this.t[0][0] = x;
		this.t[1][1] = y;
	}
	
	//Rotates the drawn lines by the provided angle about the origin
	public void basicRotate(double angle) {
		this.t[0][0] = Math.cos(Math.toRadians(-angle));
		this.t[1][0] = Math.sin(Math.toRadians(-angle));
		this.t[0][1] = -(Math.sin(Math.toRadians(-angle)));
		this.t[1][1] = Math.cos(Math.toRadians(-angle));
	}
	
	//Scales the drawn lines by the provided x and y factors from the provided center
	public void scale (double x, double y, double cx, double cy) {
		double[][] downTrans = {{1, 0, 0}, {0, 1, 0}, {-cx, -cy, 1}};
		basicScale(x,y);
		this.t = multiplyMatrices3x3(downTrans, this.t);
		double[][] upTrans = {{1, 0, 0}, {0, 1, 0}, {cx, cy, 1}};
		this.t = multiplyMatrices3x3(this.t, upTrans);
	}
	
	//Rotates the drawn lines by the provided angle about the provided center
	public void rotate(double angle, int cx, int cy) {
		double[][] downTrans = {{1, 0, 0}, {0, 1, 0}, {-cx, -cy, 1}};
		basicRotate(angle);
		this.t = multiplyMatrices3x3(downTrans, this.t);
		double[][] upTrans = {{1, 0, 0}, {0, 1, 0}, {cx, cy, 1}};
		this.t = multiplyMatrices3x3(this.t, upTrans);
	}
	
	/** Checks whether the user selected to the simple algorithm
	 * or Bresenham's then updates the frame with the corresponding 
	 * algorithm. 
	 * */
	public void paint(Graphics g) {
		if (useBres) {
			bres(g);
		} else {
			simple(g);
		}
	}
	
	
	/**
	 * Sets up the JFrame to display the generated lines.
	 */
	public void setUpGUI() {
		setSize(width, height);
		setTitle("Frame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Implementation of the Simple line drawing algorithm that uses the provided
	 * randomly generated coordinates from when the frame was constructed to draw the number
	 * of lines specified by the user. 
	 */
	public void simple(Graphics g) {
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;

		for (int i = 0; i < x1Coords.length; i++) {

			x1 = x1Coords[i];
			x2 = x2Coords[i];
			y1 = y1Coords[i];
			y2 = y2Coords[i];
			boolean swap = false;
						
			double dx = Math.abs(x2 - x1);
	        double dy = Math.abs(y2 - y1);
	        
	        
	        //To avoid dotted lines when dy>dx, the x and y values are swapped before calculation then swapped back at the end
	        if (dy > dx) {
	        	swap = true;
	        	int temp = x1;
	        	x1 = y1;
	        	y1 = temp;
	        	temp = x2;
	        	x2 = y2;
	        	y2 = temp;
	        	double tempDouble = dx;
	        	dx = dy;
	        	dy = tempDouble;
	        }
	        
	        
	        //Special case for vertical lines
	        if (dx == 0) {
	        	for (int j = 0; j <= (dy-1); j++) {
					int y = y1 + j;
					g.drawRect(x1,y,1,1);
				}
	        } else {
	        double m = dy/dx;
	        if (x1 > x2 && y1<= y2) {
	        	int temp = x1;
	        	x1 = x2;
	        	x2 = temp;
	        	temp = y1;
	        	y1 = y2;
	        	y2 = temp;
	        }
	        //Main loop for normal cases, increments dx times and increments y by the slope*j
			for (int j = 0; j <= (dx-1); j++) {
				int x = x1 + j;
				double y;
				if (y2 < y1) {
					y = y1 - m*j;
				} else if (y2 > y1) {
					y = m*j + y1;
				} else {
					y = y1;
				}
				if (swap) {
					g.drawRect((int)(y + 0.5),x,1,1);
				} else {
				g.drawRect(x,(int)(y + 0.5),1,1);
				}
				
			}
		}
		}
	}
	
	
	/**
	 * Implementation of the Bresenham Line Drawing Algorithm that uses the provided
	 * randomly generated coordinates from when the frame was constructed to draw the 
	 * number of lines specified by the user. 
	 */
	public void bres(Graphics g)
	{
	// pk is initial decision making parameter
	// Note:x1&y1,x2&y2, dx&dy values are interchanged
	// and passed in plotPixel function so
	// it can handle both cases when m>1 & m<1
		int decide;
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;
		
		for (int i = 0; i < x1Coords.length; i++) {
			double[] point1 = {x1Coords[i], y1Coords[i], 1};
			double[] point2 = {x2Coords[i], y2Coords[i], 1};
			point1 = multiplyMatrices(point1, this.t);
			point2 = multiplyMatrices(point2, this.t);
			
			x1 = (int)(point1[0] + 0.5);
			x2 = (int)(point2[0] + 0.5);
			y1 = (int)(point1[1] + 0.5);
			y2 = (int)(point2[1] + 0.5);
			
			x1Coords[i] = x1;
			x2Coords[i] = x2;
			y1Coords[i] = y1;
			y2Coords[i] = y2;
		
		int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        if (dx > dy) {
        	decide = 0;
        } else {
        	decide = 1;
        	int temp = x1;
        	x1 = y1;
        	y1 = temp;
        	temp = x2;
        	x2 = y2;
        	y2 = temp;
        	temp = dx;
        	dx = dy;
        	dy = temp;
        }
		int pk = 2 * dy - dx;
		
		//Main loop
		for (int j = 0; j <= dx-1; j++) {
	        // checking either to decrement or increment the
	        // value if we have to plot from (0,100) to (100,0)
	        if (x1 < x2) {
	        	x1++;
	        } else {
	        	x1--;
	        }
	        if (pk < 0) {
	            // decision value will decide to plot
	            // either  x1 or y1 in x's position
	            if (decide == 0) {
	                g.drawRect(x1, y1,1,1);
	                pk = pk + 2 * dy;
	            }
	            else {
	                //(y1,x1) is passed in xt
	                g.drawRect(y1,x1,1,1);
	                pk = pk + 2 * dy;
	            }
	        }
	        else {
	        	if (y1 < y2) {
		        	y1++;
		        } else {
		        	y1--;
		        }
	            if (decide == 0) {
	 
	            	g.drawRect(x1, y1,1,1);
	            }
	            else {
	            	g.drawRect(y1,x1,1,1);
	            }
	            pk = pk + 2 * dy - 2 * dx;
	        }
	    }
		}


}
}
