import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

public class Picture {
	
	private localPanel parentPanel; 
	private BufferedImage picture;
	private Graphics2D graphics;
	private Color textColor;
	private Font textFont;
	private String fontName;
	private int fontStyle;
	private int fontSize;
	private String fileName;
	private String extension;
	
	Picture(String newFileName) {
		
		if (newFileName.indexOf(".") != -1) {
			if (loadImage(newFileName)) {
				fileName = newFileName;
				extension = fileName.substring(fileName.indexOf("."));
				initPictureProperties();
			}
		}
	}
	
	Picture(int width, int height) {
		
		picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		fileName = "None";
		extension = "None";
		setAllPixelsToAColor(Color.white);
		initPictureProperties();
	}
	
	Picture(int width, int height, Color fillColor) {
		
		this(width, height);
		setAllPixelsToAColor(fillColor);
	}
	
	Picture(Pixel[][] newPixelArray) {
		
		picture = new BufferedImage(newPixelArray[0].length, newPixelArray.length, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < picture.getWidth(); x++) {
			for (int y = 0; y < picture.getHeight(); y++) {
				picture.setRGB(x, y, newPixelArray[y][x].getRGB());
			}
		}
		
		fileName = "None";
		extension = "None";
		initPictureProperties();
	}
	
	private void initPictureProperties() {
		parentPanel = new localPanel();
		initGraphics();
		fontSize = 16;
		fontStyle = Font.BOLD;
		textColor = Color.white;
		textFont = new Font("Helvetica", fontStyle, fontSize);
		getGraphics().setFont(textFont);
	}
	
	protected Image getImage() {
		return picture;
	}

	protected int getBufferedPixel(int x, int y) {
		return picture.getRGB(x, y);
	}
	
	protected void setBufferedPixel(int x, int y, int RGB) {
		picture.setRGB(x, y, RGB);
	}
	
	public Pixel getPixel(int x, int y) {
		return new Pixel(x, y, this);
	}
	
	public int getHeight() {
		return picture.getHeight();
	}
	
	public int getWidth() {
		return picture.getWidth();
	}
	
	protected Graphics2D getGraphics() {
		return graphics;
	}
	
	protected void initGraphics() {
		graphics = picture.createGraphics();
	}
	
	public Pixel[][] getPixels2D() {
		
		Pixel[][] pixelArray = new Pixel[picture.getHeight()][picture.getWidth()];
		
		for (int x = 0; x < pixelArray[0].length; x++) {
			for (int y = 0; y < pixelArray.length; y++) {
				pixelArray[y][x] = getPixel(x, y);
			}
		}
		
		return pixelArray;
	}
	
	public void setAllPixelsToAColor(Color newFillColor) {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				getPixel(x, y).setColor(newFillColor);
			}
		}
	}
	
	public Font getFont() {
		return textFont;
	}
	
	public String getFontName() {
		return fontName;
	}
	
	public int getFontStyle() {
		return fontStyle;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public Color getFontColor() {
		return textColor;
	}
	
	protected void setFont(Font newFont) {
		textFont = newFont;
		applyFont();
	}
	
	protected void setFont(String newFontName) {
		fontName = newFontName;
		applyFont();
	}
	
	protected void setFontStyle(int newFontStyle) {
		fontStyle = newFontStyle;
		applyFont();
	}
	
	protected void setFontSize(int newFontSize) {
		fontSize = newFontSize;
		applyFont();
	}
	
	protected void setFontColor(Color newTextColor) {
		textColor = newTextColor;
		applyFont();
	}
	
	protected void applyFont() {
		textFont = new Font(getFontName(), getFontStyle(), getFontSize());
		getGraphics().setFont(textFont);
		getGraphics().setPaint(textColor);
	}
	
	protected void drawString(String text, int xPos, int yPos) {
		
		Graphics2D textGraphics = getGraphics();
		textGraphics.setPaint(textColor);
		textGraphics.setFont(textFont);
		textGraphics.drawString(text, xPos, yPos + fontSize);
	}
	
	protected void setLocalPanel(localPanel newPanel) {
		parentPanel = newPanel;
	}
	
	public void show() {
		parentPanel.setVisible(true);
	}
	
	public void hide() {
		parentPanel.setVisible(false);
	}
	
	private boolean loadImage(String newFileName) {
		
		try {
			
			File image = new File(newFileName);
			if (!image.canRead()) {
				
				image = new File(getPicturePath(newFileName));
				
				if (!image.canRead()) {
					throw new IOException(newFileName +
                            " could not be opened. Check that you specified the path");
				}
				
				picture = ImageIO.read(image);
				return true;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	private String getPicturePath(String newFileName) {
		
		String directory;
		
		try {
	        Class currClass = Class.forName("Picture");
	        URL classURL = currClass.getResource("Picture.class");
	        URL fileURL = new URL(classURL,"../images/");
	        directory = fileURL.getPath();
	        directory = URLDecoder.decode(directory, "UTF-8");
	        File dirFile = new File(directory);
	        if (dirFile.exists()) {
	          return directory + newFileName;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
