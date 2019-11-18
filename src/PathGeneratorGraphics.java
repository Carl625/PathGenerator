import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.*;

public class PathGeneratorGraphics implements ActionListener {
	
	// graphics
	public JFrame frame;
	public JPanel contentPane;
	public JPanel functionData;
	
	// function generation/modification panel
	private JPanel functionGenPanel;
	
	private JLabel inputLabel;
	private JLabel tRangeLabel;
	private JLabel fRangeLabel;
	
	private JTextField functionInput;
	private JTextField pathRange;
	private JTextField functionRange;
	
	private JButton deleteFunc;
	private JButton genFunc;
	private JButton export;
	
	// function info panel
	private JPanel functionInfoPanel;
	private JScrollPane functionList;
	private JTable functionProperties;
	
	private static String[] functionPropColumns = new String[] {
			"Function",
			"Translation",
			"Rotation",
			"T Range",
			"Defined Range",
	};
	
	// field display
	private JPanel fieldDisplayPanel;
	private ArrayList<ParametricFunction2D> displayedFunctions;
	
	// Path storage
	private ArrayList<Function> functions;
    private ArrayList<Vector2D> translations;
    private ArrayList<Double> rotations;
    private ArrayList<double[]> definedFunctionRanges;
    private ArrayList<double[]> tRanges;
    private ArrayList<Double> maxSpeed;
	
	PathGeneratorGraphics() {
		
		initData();
		initFrame();
		initGenPanel();
		initInfoPanel();
		initDisplayPanel();
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private void initData() {
		
		functions = new ArrayList<Function>();
		translations = new ArrayList<Vector2D>();
		rotations = new ArrayList<Double>();
		definedFunctionRanges = new ArrayList<double[]>();
		tRanges = new ArrayList<double[]>();
		maxSpeed = new ArrayList<Double>();
	}
	
	private void initFrame() {
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Path Generator Java Graphics");
		
		contentPane = new JPanel();
		frame.setContentPane(contentPane);
		
		functionData = new JPanel();
		functionData.setLayout(new BoxLayout(functionData, BoxLayout.Y_AXIS));
		
		contentPane.add(functionData);
	}
	
	private void initGenPanel() {
		
		functionGenPanel = new JPanel();
		functionGenPanel.setLayout(new BoxLayout(functionGenPanel, BoxLayout.Y_AXIS));
		
		//Labels
		inputLabel = new JLabel("Function String:");
		tRangeLabel = new JLabel("T Range:");
		fRangeLabel = new JLabel("Function Range:");
		
		// Text Fields
		functionInput = new JTextField();
		functionInput.setActionCommand("Function Input");
		functionInput.addActionListener(this);
		pathRange = new JTextField();
		pathRange.setActionCommand("Path Range");
		pathRange.addActionListener(this);
		functionRange = new JTextField();
		functionRange.setActionCommand("Function Range");
		functionRange.addActionListener(this);
		
		//buttons
		deleteFunc = new JButton("Delete Function");
		deleteFunc.setActionCommand("Delete Function");
		deleteFunc.addActionListener(this);
		genFunc = new JButton("Generate Function");
		genFunc.setActionCommand("Generate Function");
		genFunc.addActionListener(this);
		export = new JButton("Export Path");
		export.setActionCommand("Export Path");
		export.addActionListener(this);
		
		functionGenPanel.add(inputLabel);
		functionGenPanel.add(functionInput);
		functionGenPanel.add(tRangeLabel);
		functionGenPanel.add(pathRange);
		functionGenPanel.add(fRangeLabel);
		functionGenPanel.add(functionRange);
		functionGenPanel.add(deleteFunc);
		functionGenPanel.add(genFunc);
		functionGenPanel.add(export);
		
		functionData.add(functionGenPanel);
	}
	
	private void initInfoPanel() {
		
		functionInfoPanel = new JPanel();
		
		String[][] functionDisplay = new String[functions.size()][];
		
		for (int f = 0; f < functionDisplay.length; f++) {
			
			functionDisplay[f] = generateTableRow(f);
		}
		
		stateModel functionModel = new stateModel();
		functionModel.setDataVector(functionDisplay, functionPropColumns);
		functionProperties = new JTable(functionModel);
		functionProperties.getTableHeader().setReorderingAllowed(true);
		functionProperties.setShowHorizontalLines(false);
		functionProperties.setShowVerticalLines(true);
		functionProperties.setGridColor(Color.black);
		functionProperties.setCellSelectionEnabled(false);
		functionProperties.setIntercellSpacing(new Dimension(1, 0));
		functionProperties.setDefaultRenderer(Object.class, new StateRenderer());
		
		functionList = new JScrollPane(functionProperties);
		functionList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Functions"));
		
		functionInfoPanel.add(functionList);
		
		functionData.add(functionInfoPanel);
	}
	
	private void initDisplayPanel() {
		
		fieldDisplayPanel = new JPanel();
		
		
		
		contentPane.add(fieldDisplayPanel);
	}
	
	private String[] generateTableRow(int functionIndex) {
		
		String[] row = new String[4];
		
		row[0] = functions.get(functionIndex).toString();
		row[1] = translations.get(functionIndex).toString();
		row[2] = String.valueOf(Math.toDegrees(rotations.get(functionIndex)));
		row[3] = Arrays.toString(tRanges.get(functionIndex));
		row[4] = Arrays.toString(definedFunctionRanges.get(functionIndex));
		
		return row;
	}

	class stateModel extends DefaultTableModel {
			
		    public boolean isCellEditable(int row, int col) {
		    	
		    	return false;
		    }
		}
	
	class StateRenderer extends DefaultTableCellRenderer {
		  public JLabel getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			  
			  JLabel field = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
			  
			  field.setBackground((row % 2 == 0) ? new Color(249, 249, 249) : new Color(210, 245, 250));
			  
			  return field;
		  }
	}
	
	public void actionPerformed(ActionEvent event) {
		
		String identifier = event.getActionCommand();
		
		switch (identifier) {
		}
	}
	
	public static void main(String[] args) {
		
		PathGeneratorGraphics g = new PathGeneratorGraphics();
		
	}
}
