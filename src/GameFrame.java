import javax.swing.*;

public class GameFrame extends JFrame {
	
	private static final long serialVersionUID = 2608782785310969717L;
	private localPanel mainPanel;
	
	GameFrame() {
		setUpComponents();
	}
	
	GameFrame(localPanel newPanel) {
		setMain(newPanel);
		setUpComponents();
	}
	
	protected localPanel getPanel() {
		return mainPanel;
	}
	
	protected void setMain(localPanel newPanel) {
		mainPanel = newPanel;
		mainPanel.setFrame(this);
		this.add(mainPanel);
	}
	
	private void setUpComponents() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	public void addComponent(JComponent component, String location) {
		mainPanel.add(component, location);
		mainPanel.update();
		this.pack();
		this.setVisible(true);
	}
	
	public void setImage(Picture newPicture) {
		mainPanel.setImage(newPicture);
		mainPanel.update();
		this.pack();
	}
}