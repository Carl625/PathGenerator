import java.awt.Color;

public class FieldDisplay {
	
	private static Color ignoreDrawColor = new Color(230, 230, 230);
	private Picture currentFrame;
	private Pixel[][] pixels;
	
	// image methods
	
	public Picture replaceColor(Picture canvas, Color replaceColor, Color newColor) {
		return new Picture(replaceColor(canvas.getPixels2D(), replaceColor, newColor));
	}
	
	public Pixel[][] replaceColor(Pixel[][] canvas, Color replaceColor, Color newColor) {
		
		Picture colorsExchangedImage = new Picture(canvas[0].length, canvas.length);
		Pixel[][] CEIPixels = colorsExchangedImage.getPixels2D();
		
		for (int x = 0; x < CEIPixels[0].length; x++) {
			for (int y = 0; y < CEIPixels.length; y++) {
				if (canvas[y][x].getColor().getRGB() == replaceColor.getRGB()) {
					CEIPixels[y][x].setColor(newColor);
				} else {
					CEIPixels[y][x].setColor(canvas[y][x].getColor());
				}
			}
		}
		
		return CEIPixels;
	}
	
public Picture copyImage(Picture image) {
		
		Picture newImage = new Picture(image.getWidth(), image.getHeight());
		
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				newImage.setBufferedPixel(x, y, image.getPixel(x, y).getColor().getRGB());
			}
		}
		
		return newImage;
	}
	
	public Picture flipImage(Picture image, boolean horz, boolean vert) {
		
		Picture flippedImage = new Picture(image.getWidth(), image.getHeight());
		
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int newX = x;
				int newY = y;
				
				if (horz) {
					if (image.getWidth() % 2 != 0) {
						newX -= (image.getWidth() / 2);
						newX *= -1;
						newX += (image.getWidth() / 2);
					} else {
						newX = ((image.getWidth() - 1) - newX); 
					}
				}
				
				if (vert) {
					if (image.getHeight() % 2 != 0) {
						newY -= (image.getHeight() / 2);
						newY *= -1;
						newY += (image.getHeight() / 2);
					} else {
						newY = ((image.getHeight() - 1) - newY); 
					}
				}
				
				flippedImage.setBufferedPixel(newX, newY, image.getBufferedPixel(x, y));
			}
		}
		
		return flippedImage;
	}
	
	public Pixel[][] flipImage(Pixel[][] image, boolean horz, boolean vert) {
		
		Picture flippedImage = new Picture(image[0].length, image.length);
		
		for (int x = 0; x < image[0].length; x++) {
			for (int y = 0; y < image.length; y++) {
				
				int newX = x;
				int newY = y;
				
				if (horz) {
					if (image[0].length % 2 != 0) {
						newX -= (image[0].length / 2);
						newX *= -1;
						newX += (image[0].length / 2);
					} else {
						newX = ((image[0].length - 1) - newX); 
					}
				}
				
				if (vert) {
					if (image.length % 2 != 0) {
						newY -= (image.length / 2);
						newY *= -1;
						newY += (image.length / 2);
					} else {
						newY = ((image.length - 1) - newY); 
					}
				}
				
				flippedImage.setBufferedPixel(newX, newY, image[y][x].getColor().getRGB());
			}
		}
		
		return flippedImage.getPixels2D();
	}
	
	protected Pixel[][] scaleImage(Pixel[][] image, double scale) {
		Picture scaledImage = new Picture((int)(image[0].length * scale), (int)(image.length * scale));
		Pixel[][] scaledImagePixels = scaledImage.getPixels2D();
		
		for (int x = 0; x < scaledImagePixels[0].length; x++) {
			for (int y = 0; y < scaledImagePixels.length; y++) {
				scaledImagePixels[y][x] = image[(int)(y / scale)][(int)(x / scale)];
			}
		}
		
		return scaledImagePixels;
	}
	
	protected void drawImage(Picture image, int xPos, int yPos) {
		drawImage(image.getPixels2D(), xPos, yPos);
	}
	
	protected void drawImage(Pixel[][] image, int xPos, int yPos) {
		
		for (int x = 0; x < image[0].length; x++) {
			for (int y = 0; y < image.length; y++) {
				if (((y + yPos) < currentFrame.getHeight() && (x + xPos) < currentFrame.getWidth()) && ((y + yPos) >= 0 && (x + xPos) >= 0)) {
					if (image[y][x].getColor().getRGB() != ignoreDrawColor.getRGB()) {
						pixels[y + yPos][x + xPos].setColor(image[y][x].getColor());
					}
				}
			}
		}
	}
	
	protected static void drawImage(Picture image, int xPos, int yPos, Picture canvas) {
		
		drawImage(image.getPixels2D(), xPos, yPos, canvas);
	}
	
	protected static void drawImage(Pixel[][] image, int xPos, int yPos, Picture canvas) {
		
		Pixel[][] canvasPixels = canvas.getPixels2D();
		
		for (int x = 0; x < image[0].length; x++) {
			for (int y = 0; y < image.length; y++) {
				if (((y + yPos) < canvas.getHeight() && (x + xPos) < canvas.getWidth()) && ((y + yPos) >= 0 && (x + xPos) >= 0)) {
					if (image[y][x].getColor().getRGB() != ignoreDrawColor.getRGB()) {
						canvasPixels[y + yPos][x + xPos].setColor(image[y][x].getColor());
					}
				}
			}
		}
	}
}
