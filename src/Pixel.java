import java.awt.Color;

public class Pixel {
	
	private Picture currentPic;
	private int x;
	private int y;
	
	Pixel(int newX, int newY, Picture newPic) {
		currentPic = newPic;
		x = newX;
		y = newY;
	}
	
	public Color getColor() {
		 return new Color(getRed(), getGreen(), getBlue());
	}
	
	public int getRGB() {
		return ((getAlpha() << 24) + (getRed() << 16) + (getGreen() << 8) + getBlue());
	}
	
	public int getAlpha() {
		return (currentPic.getBufferedPixel(x, y) >> 24) & 0xff;
	}
	
	public int getRed() {
		return (currentPic.getBufferedPixel(x, y) >> 16) & 0xff;
	}
	
	public int getGreen() {
		return (currentPic.getBufferedPixel(x, y) >> 8) & 0xff;
	}
	
	public int getBlue() {
		return currentPic.getBufferedPixel(x, y) & 0xff;
	}
	
	public void setColor(Color newPixelColor) {
		updateBufferedPixelColor(getAlpha(), newPixelColor.getRed(), newPixelColor.getGreen(), newPixelColor.getBlue());
	}
	
	private void updateBufferedPixelColor(int alpha, int red, int green, int blue) {
		
		int value = (alpha << 24) + (red << 16) + (green << 8) + blue;
		
		currentPic.setBufferedPixel(x, y, value);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	 public double colorDistance(Color testColor) {
		 
	   double redDistance = this.getRed() - testColor.getRed();
	   double greenDistance = this.getGreen() - testColor.getGreen();
	   double blueDistance = this.getBlue() - testColor.getBlue();
	   double distance = Math.sqrt((redDistance * redDistance) + (greenDistance * greenDistance) + (blueDistance * blueDistance));
	   return distance;
	 }
	
	public double colorDistance(Color color1,Color color2) {
		
		double redDistance = color1.getRed() - color2.getRed();
		double greenDistance = color1.getGreen() - color2.getGreen();
		double blueDistance = color1.getBlue() - color2.getBlue();
		double distance = Math.sqrt((redDistance * redDistance) + (greenDistance * greenDistance) + (blueDistance * blueDistance));
		return distance;
	}
	
	public Color subColor(Color subColor) {
		
		return subValues(subColor.getRed(), subColor.getGreen(), subColor.getBlue());
	}
	
	public Color addColor(Color addColor) {
		
		return addValues(addColor.getRed(), addColor.getGreen(), addColor.getBlue());
	}
	
	public Color subValues(int red, int green, int blue) {
		
		int newRed = subRed(red);
		int newGreen = subGreen(green);
		int newBlue = subBlue(blue);
		
		return (new Color(newRed, newGreen, newBlue));
	}
	
	public Color subForFade(int red, int green, int blue, Color fadeToColor) {
		
		int newRed;
		int newGreen;
		int newBlue;
		
		if ((this.getRed() - red) < fadeToColor.getRed()) {
			newRed = fadeToColor.getRed();
		} else {
			newRed = correctValue(this.getRed() - red);
		}
		
		if ((this.getGreen() - green) < fadeToColor.getGreen()) {
			newGreen = fadeToColor.getGreen();
		} else {
			newGreen = correctValue(this.getGreen() - green);
		}
		
		if ((this.getBlue() - blue) < fadeToColor.getBlue()) {
			newBlue = fadeToColor.getBlue();
		} else {
			newBlue = correctValue(this.getBlue() - blue);
		}
		
		return (new Color(newRed, newGreen, newBlue));
	}
	
	public Color addValues(int red, int green, int blue) {
		
		int newRed = addRed(red);
		int newGreen = addGreen(green);
		int newBlue = addBlue(blue);
		
		return (new Color(newRed, newGreen, newBlue));
	}
	
	public int subRed(int red) {
		return correctValue(this.getRed() - red);
	}
	
	public int subGreen(int green) {
		return correctValue(this.getGreen() - green);
	}
	
	public int subBlue(int blue) {
		return correctValue(this.getBlue() - blue);
	}
	
	public int addRed(int red) {
		return correctValue(this.getRed() + red);
	}
	
	public int addGreen(int green) {
		return correctValue(this.getGreen() + green);
	}
	
	public int addBlue(int blue) {
		return correctValue(this.getBlue() + blue);
	}
	
	public static int correctValue(int value) {
		
		if (value > 255) {
			value = 255;
		} else if (value < 0) {
			value = 0;
		}
		
		return value;
	}
}
