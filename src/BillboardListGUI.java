/**
 * 
 * @author - Kieran Lea
 *
 */

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JList;
import javax.swing.JFileChooser;

import alec.Billboard;
import alec.BillboardList;
import alec.CharacterHelper;
import alec.EnumSearchType;
import alec.IllegalFileFormatException;

import javax.swing.JComboBox;


@SuppressWarnings("serial")
public class BillboardListGUI extends JFrame implements ActionListener, TableModelListener { 
	private JMenu menuFile;
	private JMenuItem menuItemNewFile;
	private JMenuItem menuItemSaveAs;
	private JMenuItem menuItemExit;
	private JMenuBar menuBar;
	private JMenu menuHelp;
	private JMenuItem menuItemAbout;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemOpenFile;
	private JPanel contentPane;
	private JPanel insertPane;
	private JPanel searchPane;
	private JTable table;
	private JScrollPane tableScrollPane;
	private JPanel panelTitle;
	private JPanel panelInsertContents;
	private JLabel labelTitle;
	private JTextField textFieldTitle;
	private JPanel panelLocation;
	private JLabel labelLocation;
	private JTextField textFieldLocation;
	private JPanel panelInsertContents2;
	private JPanel panelMessage;
	private JLabel labelMessage;
	private JTextField textFieldMessage;
	private JPanel panelInnerInsert;
	private JButton buttonInsertRow;
	private final String[] columnNames = {"Title", "Location", "Message"};
	private String[][] data = {{}};
	private DefaultTableModel model = new DefaultTableModel(data, columnNames);
	private JPanel searchPanel;
	private JTextField searchTextField;
	private JPanel listPanel;
	private JPanel listMainPanel;
	private JPanel insideListPanel;
	private JList<String> list;
	private BillboardList billboard = new BillboardList();
	private JButton btnSearch;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JButton btnClear;
	private JComboBox<String> comboBox;
	private JPanel searchButtonPanel;
	private EnumSearchType enumType = EnumSearchType.TITLE;
	private List<Billboard> results;
	private JPanel panel_5;
	private JButton btnDeleteRow;
	private boolean updCheck = true;
	private File fileLoc = null;
	
	/**
	 * Create the frame.
	 */
	public BillboardListGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 925, 605);
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		menuItemNewFile = new JMenuItem("New File");
		menuFile.add(menuItemNewFile);
		menuItemNewFile.addActionListener(this);
		
		menuItemOpenFile = new JMenuItem("Open");
		menuFile.add(menuItemOpenFile);
		menuItemOpenFile.addActionListener(this);
		
		menuItemSave = new JMenuItem("Save");
		menuFile.add(menuItemSave);
		menuItemSave.addActionListener(this);
		
		menuItemSaveAs = new JMenuItem("Save As");
		menuFile.add(menuItemSaveAs);
		menuItemSaveAs.addActionListener(this);
		
		menuItemExit = new JMenuItem("Exit");
		menuFile.add(menuItemExit);
		menuItemExit.addActionListener(this);
		
		menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);
		
		menuItemAbout = new JMenuItem("About");
		menuHelp.add(menuItemAbout);
		menuItemAbout.addActionListener(this);

		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
	
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//Tab Icons
		ImageIcon insertIcon = new ImageIcon(BillboardListGUI.class.getResource("/Resources/insert_table.png"));
		ImageIcon searchIcon = new ImageIcon(BillboardListGUI.class.getResource("/Resources/edit_find.png"));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		insertPane = new JPanel();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Insert", insertIcon, insertPane, "Insert data into the table");
		insertPane.setLayout(new BorderLayout(0, 0));
		
		panelTitle = new JPanel();
		insertPane.add(panelTitle, BorderLayout.NORTH);
		
		labelTitle = new JLabel("Title:         ");
		panelTitle.add(labelTitle);
		
		textFieldTitle = new JTextField();
		textFieldTitle.setToolTipText("Enter the billboard title");
		panelTitle.add(textFieldTitle);
		textFieldTitle.setColumns(10);
		
		panelInsertContents = new JPanel();
		insertPane.add(panelInsertContents, BorderLayout.CENTER);
		panelInsertContents.setLayout(new BorderLayout(0, 0));
		
		panelLocation = new JPanel();
		panelInsertContents.add(panelLocation, BorderLayout.NORTH);
		
		labelLocation = new JLabel("Location: ");
		panelLocation.add(labelLocation);
		
		textFieldLocation = new JTextField();
		textFieldLocation.setToolTipText("Enter the billboard's location");
		panelLocation.add(textFieldLocation);
		textFieldLocation.setColumns(10);
		
		panelInsertContents2 = new JPanel();
		panelInsertContents.add(panelInsertContents2, BorderLayout.CENTER);
		panelInsertContents2.setLayout(new BorderLayout(0, 0));
		
		panelMessage = new JPanel();
		panelInsertContents2.add(panelMessage, BorderLayout.NORTH);
		
		labelMessage = new JLabel("Message:");
		panelMessage.add(labelMessage);
		
		textFieldMessage = new JTextField();
		textFieldMessage.setToolTipText("Enter the billboard message");
		panelMessage.add(textFieldMessage);
		textFieldMessage.setColumns(10);
		
		panelInnerInsert = new JPanel();
		panelInsertContents2.add(panelInnerInsert, BorderLayout.CENTER);
		panelInnerInsert.setLayout(new BorderLayout(0, 0));
		
		buttonInsertRow = new JButton("Insert");
		buttonInsertRow.addActionListener(this);
		panelInnerInsert.add(buttonInsertRow, BorderLayout.NORTH);
		
		panel_5 = new JPanel();
		panelInnerInsert.add(panel_5, BorderLayout.SOUTH);
		
		btnDeleteRow = new JButton("Delete Selected Rows");
		panel_5.add(btnDeleteRow);
		btnDeleteRow.addActionListener(this);
		
		contentPane.add(tabbedPane);
		searchPane = new JPanel();
		tabbedPane.addTab("Search", searchIcon, searchPane, "Search for data in the table");
		searchPane.setLayout(new BorderLayout(0, 0));
		
		searchPanel = new JPanel();
		searchPane.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BorderLayout(0, 0));
		
		searchTextField = new JTextField();
		searchTextField.setToolTipText("Enter title, message, or location.");
		searchPanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		comboBox = new JComboBox<String>();
		searchPanel.add(comboBox, BorderLayout.NORTH);
		comboBox.addActionListener(this);
		comboBox.addItem("Title");
		comboBox.addItem("Location");
		comboBox.addItem("Message");
		comboBox.addItem("All");
		
		listPanel = new JPanel();
		searchPane.add(listPanel, BorderLayout.CENTER);
		listPanel.setLayout(new BorderLayout(0, 0));
		
		listMainPanel = new JPanel();
		listPanel.add(listMainPanel, BorderLayout.NORTH);
		listMainPanel.setLayout(new BorderLayout(0, 0));
		
		insideListPanel = new JPanel();
		listPanel.add(insideListPanel, BorderLayout.CENTER);
		insideListPanel.setLayout(new BorderLayout(0, 0));
		
		list = new JList<String>(listModel);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.setBorder(new TitledBorder(null, "Search Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		list.setToolTipText("Search results");
		insideListPanel.add(list, BorderLayout.CENTER);
		
		btnClear = new JButton("Clear");
		insideListPanel.add(btnClear, BorderLayout.SOUTH);
		
		searchButtonPanel = new JPanel();
		insideListPanel.add(searchButtonPanel, BorderLayout.NORTH);
		
		btnSearch = new JButton("Search");
		searchButtonPanel.add(btnSearch);
		btnSearch.setIcon(new ImageIcon(BillboardListGUI.class.getResource("/Resources/find.png")));
		btnSearch.addActionListener(this);
		btnClear.addActionListener(this);
		
		table =  new JTable(model);
		model.addTableModelListener(this);
		tableScrollPane = new JScrollPane();
		contentPane.add(tableScrollPane);
		tableScrollPane.setToolTipText("You can modify values directly from the table.\r\n");
		tableScrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		tableScrollPane.setViewportView(table);
		table.setBackground(Color.WHITE);
		model.removeRow(0);
		this.setTitle("Billboard List Viewer");
	}

	//button actions
public void actionPerformed(ActionEvent e){
	if (e.getSource() == menuItemAbout){
		JOptionPane.showInternalMessageDialog(contentPane, "CS2043 Project \nBillboard Table/XML list\nAuthors:\nKieran Lea\nAlec Sobeck");
	}
	
	if (e.getSource() == menuItemExit){
		System.exit(0);
	}
	
	if (e.getSource() == menuItemNewFile){
		updCheck = false;
		model.setDataVector(null, columnNames);
		billboard.clear();
		this.setTitle("Billboard List Viewer");
		fileLoc = null;
		updCheck = true;
	}
	
	if (e.getSource() == buttonInsertRow){		
		updCheck = false;
		String title = textFieldTitle.getText();
		String message = textFieldMessage.getText();
		String location = textFieldLocation.getText();
		if (CharacterHelper.isValid(title) && CharacterHelper.isValid(message) && CharacterHelper.isValid(location)){
			Billboard bb = new Billboard(title, message, location);
			billboard.add(bb);
			this.increaseSize(title, location, message);
		} else{ 
			 JOptionPane.showInternalMessageDialog(contentPane, "Invalid Character entered.");
		}
		updCheck = true;
	}
	
	if (e.getSource() == menuItemSave){
		updCheck = false;
		try{
			if (fileLoc == null){
				JFileChooser fc = new JFileChooser();
	
		        int returnVal = fc.showSaveDialog(BillboardListGUI.this);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            fileLoc = fc.getSelectedFile();   
				try {
					try {
						billboard.save(fileLoc);
					} catch (IllegalFileFormatException e2) {
						JOptionPane.showInternalMessageDialog(contentPane, "File extension must be .xml");
						this.setTitle("Billboard List Viewer");
					} finally {
						updCheck = true;
					}
				} catch (FileNotFoundException e2) {
					 JOptionPane.showInternalMessageDialog(contentPane, "File not found.");
					 this.setTitle("Billboard List Viewer");
				} catch (NullPointerException np){
					this.setTitle("Billboard List Viewer");
				} finally {
					updCheck = true;
				}
			}
			} else {
				try {
					billboard.resave();
				} catch (FileNotFoundException e1) {
					 JOptionPane.showInternalMessageDialog(contentPane, "File not found.");
						this.setTitle("Billboard List Viewer");
				} catch (IllegalFileFormatException e1) {
					this.setTitle("Billboard List Viewer");
				} finally{
					updCheck = true;
				}
			}
		} finally {
		updCheck = true;
		}
		}
	
    //Open
    if (e.getSource() == menuItemOpenFile) {
    	updCheck = false;
    	try{
			JFileChooser fc = new JFileChooser();
	        int returnVal = fc.showOpenDialog(BillboardListGUI.this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            fileLoc = fc.getSelectedFile();
		        billboard.clear();
	    		model.setDataVector(null, columnNames);
	            this.setTitle(fileLoc.toString());
	            try {
					try {
						billboard.load(fileLoc);
					} catch (IllegalFileFormatException e1) {
						JOptionPane.showInternalMessageDialog(contentPane, "File must be an .xml file.");
						this.setTitle("Billboard List Viewer");
					} finally {
						
					}
					model.setDataVector(billboard.asStringArray(), columnNames);
				} catch (FileNotFoundException e1) {
					this.setTitle("Billboard List Viewer");
					 JOptionPane.showInternalMessageDialog(contentPane, "File not found.");
				} finally {
					
				}
	         }
    	} finally {
            updCheck = true;
    	}
    } 
    if (e.getSource() == menuItemSaveAs) {
    	updCheck = false;
		JFileChooser fc = new JFileChooser();
		try{
	        int returnVal = fc.showSaveDialog(BillboardListGUI.this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            fileLoc = fc.getSelectedFile();
	            this.setTitle(fileLoc.toString());
	            try {
					try {
						billboard.save(fileLoc);
					} catch (IllegalFileFormatException e1) {
						JOptionPane.showInternalMessageDialog(contentPane, "File extension must be .xml");
						this.setTitle("Billboard List Viewer");
					}
	
				} catch (FileNotFoundException e1) {
					 JOptionPane.showInternalMessageDialog(contentPane, "File not found.");
						this.setTitle("Billboard List Viewer");
				} 
	         }
		} finally {
        updCheck = true;
		}
    }
    
    if (e.getSource() == btnSearch){
    	String searchKey = searchTextField.getText();
    	listModel.removeAllElements();
    	
    	//SEARCH IN ALL
    	if (enumType == EnumSearchType.ALL){

    		//TITLE
    		results = (billboard.search(EnumSearchType.TITLE, searchKey));
        	if (results.toString() == "[]"){
        	}else{
        		for (Billboard billboard : results)
        		listModel.addElement(billboard.toString());
        	}
        	
        	//LOCATION
        	results = (billboard.search(EnumSearchType.LOCATION, searchKey));
           	if (results.toString() == "[]"){
           	}else{
           		for (Billboard billboard : results)
            		listModel.addElement(billboard.toString());
           	}
            	
        	//MESSAGE
        	results = (billboard.search(EnumSearchType.MESSAGE, searchKey));
           	if (results.toString() == "[]"){
           		return;
           	}else{
           		for (Billboard billboard : results)
            		listModel.addElement(billboard.toString());
           	}
    	}
    	
    	//Individual Searches
    	else{
        	results = billboard.search(enumType, searchKey);
        	if (results.toString() == "[]"){
        		return;
        	}
        	for (Billboard billboard : results)
        	listModel.addElement(billboard.toString());
    	}

    }
    
    if (e.getSource() == btnClear){
    	listModel.removeAllElements();
    }
    
    //Sets the current search type
    if (e.getSource() == comboBox){
    	int index = comboBox.getSelectedIndex();
    	if (comboBox.getItemAt(index) == "All"){
    		enumType = EnumSearchType.ALL; 
    	}
    	else if (comboBox.getItemAt(index) == "Title"){
    		enumType = EnumSearchType.TITLE;
    	}
    	else if (comboBox.getItemAt(index) == "Location"){
    		enumType = EnumSearchType.LOCATION;
    	}
    	else if (comboBox.getItemAt(index) == "Message"){
    		enumType = EnumSearchType.MESSAGE;
    	}
    }
    
    if (e.getSource() == btnDeleteRow){
    	updCheck = false;
    		int[] numRows = table.getSelectedRows();
    		for (int i = 0; i < numRows.length; i++){
    				model.removeRow(numRows[i] - i);
    				billboard.remove(numRows[i] - i);
    		}
    	updCheck = true;
    }
}

	public void increaseSize(String title, String location, String message){
		String[] tempData = {title, location, message};
		model.addRow(tempData);
	}

	@Override
	public void tableChanged(TableModelEvent e) {		
		if ((e.getType() == TableModelEvent.UPDATE) && (updCheck == true)){
			int rowChange = e.getLastRow();
			int colChange = e.getColumn();
		if (CharacterHelper.isValid((String)model.getValueAt(rowChange, colChange))){
			billboard.set(rowChange, colChange, (String)model.getValueAt(rowChange, colChange));
		}else{
			model.setValueAt("Invalid Character", rowChange, colChange);
			billboard.set(rowChange, colChange, "Invalid Character");
		}
		}
	}
}
