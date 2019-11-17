import java.awt.*;
import javax.swing.*;

public class localPanel extends JPanel {
	
	private static final long serialVersionUID = -1414981052119619803L;
	private JFrame frame;
	private ImageIcon picture = new ImageIcon();
	private JLabel display = new JLabel(picture);
	private Picture currentImage = null;
	
	localPanel() {
		initPanel();
	}
	
	localPanel(JComponent component) {
		this.add(component);
		initPanel();
	}
	
	localPanel(Picture newPic) {
		currentImage = newPic;
		currentImage.setLocalPanel(this);
		initPanel();
	}
	
	protected Picture getImage() {
		return currentImage;
	}
	
	protected void setImage(Picture newPicture) {
		currentImage = newPicture;
		currentImage.setLocalPanel(this);
		update();
	}
	
	protected JFrame getFrame() {
		return frame;
	}
	
	protected void setFrame(JFrame newFrame) {
		frame = newFrame;
	}
	
	protected ImageIcon getDisplay() {
		return picture;
	}
	
	public void update() {
		
		if (currentImage != null) {
			
			picture.setImage(currentImage.getImage());
			
			this.repaint();
			this.setVisible(true);
		}
	}
	
	private void initPanel() {
		
		this.setLayout(new BorderLayout());
		
		update();
		
		this.add(display);
		
		this.setVisible(true);
	}
}
