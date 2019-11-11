package lspb;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import arinc665.ARINC665;
import arinc665.ARINC_norm_version;
import arinc665.integrity_check;
import arinc665.ARINC665Exception;

/*
 * Class : LSPB
 */
public class LSPB extends JFrame {
    /**************************************************************************
     ** Private class MyTreeCellRenderer                                     **
     **************************************************************************/
	private class MyTreeCellRenderer extends DefaultTreeCellRenderer
	{
		@Override public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode lDefaultMutableTreeNode = (DefaultMutableTreeNode)value;
			if (lDefaultMutableTreeNode.getUserObject().toString().contains("Error" )) {
				this.setForeground(Color.RED);
			}
			else {
			}
        return this;
		}
	}
    /**************************************************************************
     ** Private class StatusBar                                              **
     **************************************************************************/
	private class StatusBar extends JLabel {
		public StatusBar() {
			super();
		}
		public StatusBar(String a_message) {
			super(a_message);
			setBorder(new EtchedBorder());
		}
		public void setMessage(String a_message) {
			setText(a_message);
		}
	}
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
	private static boolean pF_with_CRC;
	private static byte[] pMACAddress;
	private JMenuItem pJMenuItemGenerate;
	private JCheckBoxMenuItem pJCheckBoxMenuItemARINC665_1;
	private JCheckBoxMenuItem pJCheckBoxMenuItemARINC665_2;
	private JCheckBoxMenuItem pJCheckBoxMenuItemARINC665_3;
	private JSplitPane pSplitPane;
	private JTabbedPane pJTabbedPane;
	private JTree pJTreeConfiguration;
	private DefaultMutableTreeNode pRootNodeConfiguration;
	private JScrollPane pRightJScrollPane;
	private JTextArea pJTextAreaConfiguration;
	private JTextArea pJTextAreaLoad;
	private JTree pJTreeLoad;
	private DefaultMutableTreeNode pRootNodeLoad;
	private StatusBar pStatus_bar;
	private static XMLConfigurationFileHanlder pXML_configuration_file;
	public static ARINC665 aARINC665;
	private static ARINC_norm_version aARINC_norm_version;
	private static final int kSIS_system_zone_size = 0x3B0;
	private static final String kTitle = "LSPB v3.5 beta";
	private final String kARINC_logo = "/images/arinc_logo.jpg";
	/**************************************************************************
	 ** Private method : DisplayUsage                                        **
	 **************************************************************************/
	private static void DisplayUsage() {
		System.out.println("Usage : java -jar LSPB.jar [<xml file>]");
		System.exit(1);
	}
	/**************************************************************************
	 ** Private method : ExitWithException                                   **
	 **************************************************************************/
	private static void ExitWithException(Exception e, String message) {
		System.err.println(message);
		e.printStackTrace(System.err);
		System.exit(2);
	}
	/**************************************************************************
	 ** Private method : BuildFileUserDefinedData                            **
	 **************************************************************************/
	private static void BuildFileUserDefinedData(byte[] a_UDD, String a_filename) {
		try {
			FileInputStream lFile = new FileInputStream(a_filename);
			lFile.read(a_UDD);
			lFile.close();
		} catch (FileNotFoundException e) {
			// Impossible case because the existance of the user defined data file has been tested during the analysis of the XML configuration file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**************************************************************************
	 ** Private method : BuildBCCUserDefinedData                             **
	 **************************************************************************/
	private static void BuildBCCUserDefinedData(byte[] a_UDD, String a_load_PN) {
		int i;
		int j;
		int lCRC16;
		byte[] lSIS_zone = new byte[kSIS_system_zone_size - 2];

		// Null character
		lSIS_zone[0] = 0;
		// Copy the HW functional designation
		j = 1;
		for(i=1;i <= pXML_configuration_file.aHW_functional_designation.length();i++) {
			lSIS_zone[j] = (byte) pXML_configuration_file.aHW_functional_designation.charAt(i - 1);
			j++;
		}
		// Add a null character
		lSIS_zone[j] = 0;
		j++;
		// Copy the load PN
		for(i=1;i <= a_load_PN.length();i++) {
			lSIS_zone[j] = (byte) a_load_PN.charAt(i - 1);
			j++;
		}
		j++;
		// Fill with null characters until the end
		for(;j < lSIS_zone.length;j++) {
			lSIS_zone[j] = 0;
		}
		// Calculate the CRC16 of SIS zone
		lCRC16 = integrity_check.CalculateCRC16(lSIS_zone, lSIS_zone.length);
		// Set the HW/SW compatibility index in UDD
		a_UDD[0] = 0;
		a_UDD[1] = (byte) pXML_configuration_file.aHW_SW_compatibility_index;
		// Copy the SIS zone into UDD
		for(i=0;i < lSIS_zone.length;i++) {
			a_UDD[i + 2] = lSIS_zone[i];
		}
		// Set the CRC16 in UDD
		a_UDD[kSIS_system_zone_size + 1] = (byte) (lCRC16 >> 8);
		a_UDD[kSIS_system_zone_size] = (byte) (lCRC16 & 0xFF);
	}
	/**************************************************************************
	 ** Private method : ReadXMLFile                                         **
	 **************************************************************************/
	private static void ReadXMLFile(String a_filename) {
		File lFile = new File(a_filename);
		if (lFile.canRead()) {
			try {
				SAXParserFactory lFactory = SAXParserFactory.newInstance();
				SAXParser lParser = lFactory.newSAXParser();
				pXML_configuration_file = new XMLConfigurationFileHanlder();
				lParser.parse(a_filename, pXML_configuration_file);
			}
			catch (Exception e) {
				ExitWithException(e, "*** Error *** Parsing the file " + a_filename);
			}
		}
		else {
			Exception e = new Exception();
			ExitWithException(e, "*** Error *** The file " + a_filename + " does not exists");
		}
	}
	/**************************************************************************
	 ** Private method : RemoveFile                                          **
	 **************************************************************************/
	private static void RemoveDataFile() {
		File lSub_directory = new File(pXML_configuration_file.aSub_directory);
		if (! lSub_directory.isDirectory()) {
			if (! lSub_directory.mkdirs()) {
				Exception e = new Exception();
				ExitWithException(e, "*** Error *** Impossible to create directories ".concat(pXML_configuration_file.aSub_directory));
			}
			else {
			}
		}
		else {
		}
		File lFile = new File(pXML_configuration_file.aSub_directory + "/*.LUP");
		if (lFile.exists()) {
			lFile.delete();
		}
		else {
		}
	}
	/**************************************************************************
	 ** Private method : Hash                                                **
	 **************************************************************************/
	private static String Hash(byte[] a_bytes) {
		return(String.format("%04X%08X", integrity_check.CalculateCRC16(a_bytes, a_bytes.length), integrity_check.CalculateCRC32(a_bytes, a_bytes.length, 0xFFFFFFFF)));
	}
	/**************************************************************************
	 ** Private method : BuildLoad                                           **
	 **************************************************************************/
	private static void BuildLoad(String a_CC, ARINC_norm_version a_norm_version) {
		boolean lF_with_UDD;
		byte[] lUDD = null;
		File lFile;

		if (pXML_configuration_file.aKey.contentEquals(Hash(pMACAddress)) == false) {
			System.out.printf("*** Warning *** Load CRC32 will not be generated (replaced by 0)\n");
			pF_with_CRC = false;
		}
		else {
			pF_with_CRC = true;
		}
			lF_with_UDD = false;
			if (pXML_configuration_file.aUser_data_file != null) {
				lFile = new File(pXML_configuration_file.aUser_data_file);
				if ((lFile.length() % 2) == 0) {
					lUDD = new byte[(int) lFile.length()];
				}
				else {
					System.out.println("*** Information *** Padding user define data with one byte");
					lUDD = new byte[(int) lFile.length() + 1];
				}
				BuildFileUserDefinedData(lUDD, pXML_configuration_file.aUser_data_file);
				lF_with_UDD = true;
			}
			else if (pXML_configuration_file.aUser_data_text != null) {
				if ((pXML_configuration_file.aUser_data_text.length() % 2) == 0) {
					lUDD = pXML_configuration_file.aUser_data_text.getBytes();
				}
				else {
					System.out.println("*** Information *** Padding user define data with one byte");
					lUDD = new byte[pXML_configuration_file.aUser_data_text.length() + 1];
					for(int i=0;i < pXML_configuration_file.aUser_data_text.length();i++) {
						lUDD[i] = (byte) pXML_configuration_file.aUser_data_text.charAt(i);
					}
					lUDD[pXML_configuration_file.aUser_data_text.length()] = 0;
				}
				lF_with_UDD = true;
			}
			else if (pXML_configuration_file.aF_HW_SW_compatibility_index_present) {
				lUDD = new byte[2 + kSIS_system_zone_size];
				BuildBCCUserDefinedData(lUDD, pXML_configuration_file.aMMM + a_CC + pXML_configuration_file.aLoad_PN);
				lF_with_UDD = true;
			}
			else {
				lUDD = new byte[0];
			}
			try {
				aARINC665 = new ARINC665(a_norm_version,
										 pXML_configuration_file.aMMM + a_CC + pXML_configuration_file.aLoad_PN,
										 pXML_configuration_file.aLoad_type_description,
										 pXML_configuration_file.aLoad_integrity_check,
										 pXML_configuration_file.aLoad_type_ID,
										 pXML_configuration_file.aInput_file,
										 pXML_configuration_file.aInput_file_integrity_check,
										 pXML_configuration_file.aPadding,
										 pXML_configuration_file.aSplit_size,
										 pXML_configuration_file.aSub_directory,
										 pXML_configuration_file.aTHWID,
										 pXML_configuration_file.aSupport_file,
										 pXML_configuration_file.aSupport_file_integrity_check,
										 pXML_configuration_file.aF_batch_required,
										 pXML_configuration_file.aComment,
										 lF_with_UDD,
										 lUDD,
										 pXML_configuration_file.aF_media_required,
										 pXML_configuration_file.aMMM + a_CC + pXML_configuration_file.aLoad_PN,
										 pF_with_CRC);
			}
			catch (ARINC665Exception e) {
				ExitWithException(e, e.getMessage());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	}
	/**************************************************************************
	 ** Private method : GetDataFileSubTree                                  **
	 **************************************************************************/
	private static DefaultMutableTreeNode GetDataFileSubTree() {
		int i;
		DefaultMutableTreeNode lSubNode;
		DefaultMutableTreeNode lSubSubNode;
		DefaultMutableTreeNode lNode = new DefaultMutableTreeNode("Data files : " + String.format("%d", LSPB.aARINC665.aSoftware_part.aData_file.aCRC16.length), true);
		for(i=0;i < LSPB.aARINC665.aSoftware_part.aData_file.aCRC16.length;i++) {
			lSubNode = new DefaultMutableTreeNode(String.format("%s", LSPB.aARINC665.aSoftware_part.aData_file.aName[i]), true);
			lNode.add(lSubNode);
			lSubSubNode = new DefaultMutableTreeNode(String.format("Length = 0x%04X bytes (%d)", LSPB.aARINC665.aSoftware_part.aData_file.aLength_in_bytes[i], LSPB.aARINC665.aSoftware_part.aData_file.aLength_in_bytes[i]));
			lSubNode.add(lSubSubNode);
			if (LSPB.aARINC665.aSoftware_part.aData_file.aPadding[i]) {
				lSubSubNode = new DefaultMutableTreeNode(String.format("Padded of 1 byte (ASCII %d)", pXML_configuration_file.aPadding));
				lSubNode.add(lSubSubNode);
			}
			else {
			}
			lSubSubNode = new DefaultMutableTreeNode(String.format("CRC16 = 0x%04X", LSPB.aARINC665.aSoftware_part.aData_file.aCRC16[i]));
			lSubNode.add(lSubSubNode);
			if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
				switch(LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_type) {
					case 4 :
						lSubSubNode = new DefaultMutableTreeNode(String.format("Integrity check : MD5 = %s", LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_string[i]));
						lSubNode.add(lSubSubNode);
						break;
					case 5 :
						lSubSubNode = new DefaultMutableTreeNode(String.format("Integrity check : SHA-1 = %s", LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_string[i]));
						lSubNode.add(lSubSubNode);
						break;
					default : ;
				}
			}
			else {
			}
		}
		return(lNode);
	}
	/**************************************************************************
	 ** Private method : GetHeaderFileSubTree                                **
	 **************************************************************************/
	private static DefaultMutableTreeNode GetHeaderFileSubTree() {
		int i;
		DefaultMutableTreeNode lSubNode;
		DefaultMutableTreeNode lSubSubNode;
		DefaultMutableTreeNode lSubSubSubNode;
		DefaultMutableTreeNode lNode = new DefaultMutableTreeNode(String.format("Header file : %s", LSPB.aARINC665.aSoftware_part.aHeader_file.aName), true);
		lSubNode = new DefaultMutableTreeNode(String.format("Length = 0x%04X words (%d)", LSPB.aARINC665.aSoftware_part.aHeader_file.aLength, LSPB.aARINC665.aSoftware_part.aHeader_file.aLength), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Version = 0x%02X%02X", LSPB.aARINC665.aSoftware_part.aHeader_file.aVersion >> 8, LSPB.aARINC665.aSoftware_part.aHeader_file.aVersion & 0xFF), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to load P/N length = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_LPN * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_LPN), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to number of THWID = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_THWID_list * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_THWID_list), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to number of data files = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_DF_list * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_DF_list), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to number of support files = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_SF_list * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_SF_list), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to user defined data = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_UDD * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_UDD), true);
		lNode.add(lSubNode);
		if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
			lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to load type description = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_LTD * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_LTD), true);
			lNode.add(lSubNode);
			lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to number of THWID with positions = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.kPointer_to_THWID_with_position_list * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.kPointer_to_THWID_with_position_list), true);
			lNode.add(lSubNode);
			lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to load check value = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_LCV * 2, LSPB.aARINC665.aSoftware_part.aHeader_file.aPointer_to_LCV), true);
			lNode.add(lSubNode);
		}
		else {
		}
		lSubNode = new DefaultMutableTreeNode(String.format("Load P/N length = %d bytes (0x%04X)", (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aLPN_length, (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aLPN_length), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Load P/N = %s", LSPB.aARINC665.aSoftware_part.aHeader_file.aLoad_PN), true);
		lNode.add(lSubNode);
		if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
			lSubNode = new DefaultMutableTreeNode(String.format("Load type description length = %d bytes (0x%04X)", (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aLoad_type.aDescription.length(), LSPB.aARINC665.aSoftware_part.aHeader_file.aLoad_type.aDescription.length()), true);
			lNode.add(lSubNode);
			lSubNode = new DefaultMutableTreeNode(String.format("Load type description = %s", LSPB.aARINC665.aSoftware_part.aHeader_file.aLoad_type.aDescription), true);
			lNode.add(lSubNode);
			lSubNode = new DefaultMutableTreeNode(String.format("Load type ID = %d", (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aLoad_type.aID), true);
			lNode.add(lSubNode);
		}
		else {
		}
		lSubNode = new DefaultMutableTreeNode(String.format("Number of target HW ID = %d", (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aNumber_of_THWID), true);
		lNode.add(lSubNode);
		for(i=0;i < (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aNumber_of_THWID;i++) {
			lSubSubNode = new DefaultMutableTreeNode(String.format("Target HW ID #%d = %s", i + 1, LSPB.aARINC665.aSoftware_part.aHeader_file.aTHWID_list[i]));
			lSubNode.add(lSubSubNode);
		}
		lSubNode = new DefaultMutableTreeNode(String.format("Number of data files = %d", (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aNumber_of_DF), true);
		lNode.add(lSubNode);
		for(i=0;i < (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aNumber_of_DF;i++) {
			lSubSubNode = new DefaultMutableTreeNode(String.format("Data file #%d", i + 1));
			lSubNode.add(lSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Pointer = %d words (0x%04X)", (int)LSPB.aARINC665.aSoftware_part.aData_file.aPointer[i], (int)LSPB.aARINC665.aSoftware_part.aData_file.aPointer[i]));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Name = %s (%d bytes)", LSPB.aARINC665.aSoftware_part.aData_file.aName[i], (int)LSPB.aARINC665.aSoftware_part.aData_file.aName_length));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("P/N = %s (%d bytes)", LSPB.aARINC665.aSoftware_part.aData_file.aPN, LSPB.aARINC665.aSoftware_part.aData_file.aPN.length()));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Length = %d bytes", LSPB.aARINC665.aSoftware_part.aData_file.aLength_in_bytes[i]));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("CRC16 = 0x%04X", LSPB.aARINC665.aSoftware_part.aData_file.aCRC16[i]));
			lSubSubNode.add(lSubSubSubNode);
			if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
				lSubSubSubNode = new DefaultMutableTreeNode(String.format("Check value length = %d words (0x%04X)", (int)LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_length, (int)LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_length));
				lSubSubNode.add(lSubSubSubNode);
				switch(LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_type) {
				case 4 :
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("Check value type = MD5"));
					lSubSubNode.add(lSubSubSubNode);
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("MD5 = %s", LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_string[i]));
					lSubSubNode.add(lSubSubSubNode);
					break;
				case 5 :
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("Check value type = SHA-1"));
					lSubSubNode.add(lSubSubSubNode);
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("SHA-1 = %s", LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_string[i]));
					lSubSubNode.add(lSubSubSubNode);
					break;
					default : ;
				}
			}
			else {
			}
		}
		lSubNode = new DefaultMutableTreeNode(String.format("Number of support files = %d", (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aNumber_of_SF), true);
		lNode.add(lSubNode);
		for(i=0;i < (int)LSPB.aARINC665.aSoftware_part.aHeader_file.aNumber_of_SF;i++) {
			lSubSubNode = new DefaultMutableTreeNode(String.format("Support file #%d", i + 1));
			lSubNode.add(lSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Pointer = %d words (0x%04X)", (int)LSPB.aARINC665.aSoftware_part.aSupport_file.aPointer[i], (int)LSPB.aARINC665.aSoftware_part.aSupport_file.aPointer[i]));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Name = %s (%d bytes)", LSPB.aARINC665.aSoftware_part.aSupport_file.aName[i], (int)LSPB.aARINC665.aSoftware_part.aSupport_file.aName_length[i]));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("P/N = %s (%d bytes)", LSPB.aARINC665.aSoftware_part.aSupport_file.aPN, LSPB.aARINC665.aSoftware_part.aSupport_file.aPN.length()));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Length = %d bytes", LSPB.aARINC665.aSoftware_part.aSupport_file.aLength[i]));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("CRC16 = 0x%04X", LSPB.aARINC665.aSoftware_part.aSupport_file.aCRC16[i]));
			lSubSubNode.add(lSubSubSubNode);
			if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
				lSubSubSubNode = new DefaultMutableTreeNode(String.format("Check value length = %d words (0x%04X)", (int)LSPB.aARINC665.aSoftware_part.aSupport_file.aCheck_value_length, (int)LSPB.aARINC665.aSoftware_part.aData_file.aCheck_value_length));
				lSubSubNode.add(lSubSubSubNode);
				switch(LSPB.aARINC665.aSoftware_part.aSupport_file.aCheck_value_type) {
				case 4 :
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("Check value type = MD5"));
					lSubSubNode.add(lSubSubSubNode);
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("MD5 = %s", LSPB.aARINC665.aSoftware_part.aSupport_file.aCheck_value_string[i]));
					lSubSubNode.add(lSubSubSubNode);
					break;
				case 5 :
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("Check value type = SHA-1"));
					lSubSubNode.add(lSubSubSubNode);
					lSubSubSubNode = new DefaultMutableTreeNode(String.format("SHA-1 = %s", LSPB.aARINC665.aSoftware_part.aSupport_file.aCheck_value_string[i]));
					lSubSubNode.add(lSubSubSubNode);
					break;
					default : ;
				}
			}
			else {
			}
		}
		if (LSPB.aARINC665.aSoftware_part.aHeader_file.aUser_defined_data != null) {
			lSubNode = new DefaultMutableTreeNode(String.format("User defined data length = %d bytes", LSPB.aARINC665.aSoftware_part.aHeader_file.aUser_defined_data.length * 2), true);
			lNode.add(lSubNode);
		}
		else {
		}
		lSubNode = new DefaultMutableTreeNode(String.format("CRC16 = 0x%04X", LSPB.aARINC665.aSoftware_part.aHeader_file.aCRC16), true);
		lNode.add(lSubNode);
		return(lNode);
	}
	/**************************************************************************
	 ** Private method : GetLoadsFileSubTree                                 **
	 **************************************************************************/
	private static DefaultMutableTreeNode GetLoadsFileSubTree() {
		int i;
		int j;
		DefaultMutableTreeNode lSubNode;
		DefaultMutableTreeNode lSubSubNode;
		DefaultMutableTreeNode lSubSubSubNode;
		DefaultMutableTreeNode lSubSubSubSubNode;
		DefaultMutableTreeNode lNode = new DefaultMutableTreeNode("LOADS.LUM file", true);
		lSubNode = new DefaultMutableTreeNode(String.format("Length = 0x%04X words (%d)", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aLength, LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aLength), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Version = 0x%02X%02X", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aVersion >> 8, LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aVersion & 0xFF), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to media set P/N length = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aPointer_to_MI * 2, LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aPointer_to_MI), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to number of media set files = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aPointer_to_load_list * 2, LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aPointer_to_load_list), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to user defined data = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aPointer_to_UDD * 2, LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aPointer_to_UDD), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Media set P/N length = %d bytes (0x%04X)", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aMSPN_length, (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aMSPN_length), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Media set P/N = %s", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aMSPN), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Media sequence number = %d", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aMedia_sequence_number), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Number of media set members = %d", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aNumber_of_media_set_member), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Number of loads = %d", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aNumber_of_load), true);
		lNode.add(lSubNode);
		for(i=0;i < (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aNumber_of_load;i++) {
			lSubSubNode = new DefaultMutableTreeNode(String.format("Load #%d", i + 1));
			lSubNode.add(lSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Pointer = %d words (0x%04X)", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aLoad_pointer, (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aLoad_pointer));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("P/N = %s (%d bytes)", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aLoad_PN, (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aLoad_PN.length()));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Header file name = %s (%d bytes)", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aHeader_file_name, LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aHeader_file_name.length()));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Member sequence number = %d", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aMember_sequence_number));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Number of THWID = %d", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aNumber_of_THWID));
			lSubSubNode.add(lSubSubSubNode);
			for(j=0;j < (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aNumber_of_THWID;j++) {
				lSubSubSubSubNode = new DefaultMutableTreeNode(String.format("Target HW ID #%d = %s", i + 1, LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aTHWID_list[i]));
				lSubSubSubNode.add(lSubSubSubSubNode);
			}
		}
		lSubNode = new DefaultMutableTreeNode(String.format("CRC16 = 0x%04X", LSPB.aARINC665.aSoftware_transport_media.aList_of_loads_file.aCRC16), true);
		lNode.add(lSubNode);
		return(lNode);
	}
	/**************************************************************************
	 ** Private method : GetFilesFileSubTree                                 **
	 **************************************************************************/
	private static DefaultMutableTreeNode GetFilesFileSubTree() {
		int i;
		DefaultMutableTreeNode lSubNode;
		DefaultMutableTreeNode lSubSubNode;
		DefaultMutableTreeNode lSubSubSubNode;
		DefaultMutableTreeNode lNode = new DefaultMutableTreeNode("FILES.LUM file", true);
		lSubNode = new DefaultMutableTreeNode(String.format("Length = 0x%04X words (%d)", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aLength, LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aLength), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Version = 0x%02X%02X", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aVersion >> 8, LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aVersion & 0xFF), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to media set P/N length = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_Media_Information * 2, LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_Media_Information), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to number of media set files = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_File_List * 2, LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_File_List), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to user defined data = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_UDD * 2, LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_UDD), true);
		if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
			lSubNode = new DefaultMutableTreeNode(String.format("Absolute Pointer to FILES.LUM check value length = %d bytes (0x%04X)", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_FCV * 2, LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aPointer_to_FCV), true);
		}
		else {
		}
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Media set P/N length = %d bytes (0x%04X)", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aMSPN_length, (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aMSPN_length), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Media set P/N = %s", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aMSPN), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Media sequence number = %d", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aMedia_sequence_number), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Number of media set members = %d", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aNumber_of_media_set_member), true);
		lNode.add(lSubNode);
		lSubNode = new DefaultMutableTreeNode(String.format("Number of media set files = %d", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aNumber_of_media_set_files), true);
		lNode.add(lSubNode);
		for(i=0;i < (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aNumber_of_media_set_files;i++) {
			lSubSubNode = new DefaultMutableTreeNode(String.format("Media set file #%d", i + 1));
			lSubNode.add(lSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Pointer = %d words (0x%04X)", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_pointer[i], (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_pointer[i]));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Name = %s (%d bytes)", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_name[i], (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_name[i].length()));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Pathname = %s (%d bytes)", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_pathname[i], LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_pathname[i].length()));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("Member sequence number = %d", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_member_sequence_number));
			lSubSubNode.add(lSubSubSubNode);
			lSubSubSubNode = new DefaultMutableTreeNode(String.format("CRC16 = 0x%04X", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aFile_CRC[i]));
			lSubSubNode.add(lSubSubSubNode);
			if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
				lSubSubSubNode = new DefaultMutableTreeNode("File check value length = 0 word");
				lSubSubNode.add(lSubSubSubNode);
			}
			else {
			}
		}
		if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
			lSubNode = new DefaultMutableTreeNode(String.format("FILES.LUM file check value length = %d words (0x%04X)", (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aCheck_value_length, (int)LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aCheck_value_length), true);
			lNode.add(lSubNode);
			if (aARINC665.aSoftware_transport_media.aList_of_files_file.aCheck_value_length != 0) {
				switch(aARINC665.aSoftware_transport_media.aList_of_files_file.aCheck_value_type) {
					case 4 :
						lSubSubNode = new DefaultMutableTreeNode("FILES.LUM file check value type = MD5");
						lSubNode.add(lSubSubNode);
						lSubSubNode = new DefaultMutableTreeNode(String.format("FILES.LUM file check value = %s", aARINC665.aSoftware_transport_media.aList_of_files_file.aCheck_value_string));
						lSubNode.add(lSubSubNode);
						break;
					case 5 :
						lSubSubNode = new DefaultMutableTreeNode("FILES.LUM file check value type = SHA-1");
						lSubNode.add(lSubSubNode);
						lSubSubNode = new DefaultMutableTreeNode(String.format("FILES.LUM file check value = %s", aARINC665.aSoftware_transport_media.aList_of_files_file.aCheck_value_string));
						lSubNode.add(lSubSubNode);
						break;
					default : ;
				}
			}
			else {
			}
		}
		else {
		}
		lSubNode = new DefaultMutableTreeNode(String.format("CRC16 = 0x%04X", LSPB.aARINC665.aSoftware_transport_media.aList_of_files_file.aCRC16), true);
		lNode.add(lSubNode);
		return(lNode);
	}
	/**************************************************************************
	 ** MAIN                                                                 
	 **************************************************************************/
	public static void main(String[] args) throws UnknownHostException, SocketException, ParseException {
		InetAddress lIPAddress = InetAddress.getLocalHost();
		NetworkInterface lNetwork = NetworkInterface.getByInetAddress(lIPAddress);
		pMACAddress = lNetwork.getHardwareAddress();
		SimpleDateFormat lSdf = new SimpleDateFormat("yyyy-MM-dd");
		Date lToday = new Date();
		Date l20200101 = lSdf.parse("2020-01-01");
		// IF today is after 01/01/2020 THEN
		if (lToday.after(l20200101)) {
			System.out.println("Date for using beta version is over");
			System.exit(1);
		}
		// ELSE
		else {
			// IF no argument THEN
			if (args.length == 0) {
				// Create new LSBP object
				LSPB lLSPB = new LSPB();
				// Make it visible
				lLSPB.setVisible(true);
			}
			// ELSE IF one argument THEN
			else if (args.length == 1) {
				String lCC;
				// Print version
				System.out.println(kTitle);
				// Print Date
				SimpleDateFormat lSdf_with_hour = new SimpleDateFormat("d MMM yyyy HH:mm:ss" );
				System.out.println(lSdf_with_hour.format(lToday));
				// Read XML files using filename as argument
				ReadXMLFile(args[0]);
				// Remove generated LUP files
				RemoveDataFile();
				// Calculate CRC of load P/N
				lCC = Integer.toHexString(integrity_check.CalculateCRC8(pXML_configuration_file.aMMM + pXML_configuration_file.aLoad_PN)).toUpperCase();
				// Build load files
				switch(pXML_configuration_file.aNorm_version) {
				case 1 :
					aARINC_norm_version = ARINC_norm_version.ARINC665_1;
					break;
				default :
				case 2 :
					aARINC_norm_version = ARINC_norm_version.ARINC665_2;
					break;
				case 3 :
					aARINC_norm_version = ARINC_norm_version.ARINC665_3;
					break;
				}
				BuildLoad(lCC, aARINC_norm_version);
			}
			// ELSE
			else {
				// Display usage
				System.out.println("*** Error *** Invalid number of arguments");
				DisplayUsage();
			}
			// END IF
		}
		// END IF
	}

	/**************************************************************************
	 ** Constructor                                                          **
	 **************************************************************************/
	public LSPB() {
		super("LSPB");
		final TreeSelectionListener treeSelection = new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent event) {
				DefaultMutableTreeNode lNode= (DefaultMutableTreeNode)pJTreeLoad.getLastSelectedPathComponent();
				DefaultMutableTreeNode lNextNode;
				if (lNode != null) {
					DefaultTreeModel lTreeModel = (DefaultTreeModel) pJTreeLoad.getModel();
					lNextNode = lNode;
					while((lNextNode != null) && (lNextNode.getParent() != lNode.getRoot())) {
						lNextNode = (DefaultMutableTreeNode) lNode.getParent();
					}
					if (lNextNode != null) {
						switch(lTreeModel.getIndexOfChild(lNextNode.getParent(), lNextNode)) {
						case 0 :
							while(lNode.getParent() != lNextNode) {
								lNode = (DefaultMutableTreeNode) lNode.getParent();
							}
							int i = lTreeModel.getIndexOfChild(lNextNode, lNode);
							if (i < aARINC665.aSoftware_part.aData_file.aName.length) {
								pJTextAreaConfiguration.setVisible(false);
								try {
									pJTextAreaLoad.setText(file.BuildHexaDump(aARINC665.aSoftware_part.aData_file.aSub_directory + "/" + aARINC665.aSoftware_part.aData_file.aName[i], aARINC665.aSoftware_part.aData_file.aLength_in_bytes[i]));
								} catch (IOException e) {
									pJTextAreaLoad.setText("File not found : " + aARINC665.aSoftware_part.aData_file.aSub_directory + "/" + aARINC665.aSoftware_part.aData_file.aName[i]);
								}
								pJTextAreaLoad.setEditable(false);
								pJTextAreaLoad.setVisible(true);
								pRightJScrollPane.setViewportView(pJTextAreaLoad);
							}
							else {
							}
							break;
						case 1 :
							pJTextAreaConfiguration.setVisible(false);
							pJTextAreaLoad.setText(aARINC665.aSoftware_part.aHeader_file.aHexa_dump);
							pJTextAreaLoad.setEditable(false);
							pJTextAreaLoad.setVisible(true);
							pRightJScrollPane.setViewportView(pJTextAreaLoad);
							break;
						case 2 :
							pJTextAreaConfiguration.setVisible(false);
							pJTextAreaLoad.setText(aARINC665.aSoftware_transport_media.aList_of_loads_file.aHexa_dump);
							pJTextAreaLoad.setEditable(false);
							pJTextAreaLoad.setVisible(true);
							pRightJScrollPane.setViewportView(pJTextAreaLoad);
							break;
						case 3 :
							pJTextAreaConfiguration.setVisible(false);
							pJTextAreaLoad.setText(aARINC665.aSoftware_transport_media.aList_of_files_file.aHexa_dump);
							pJTextAreaLoad.setEditable(false);
							pJTextAreaLoad.setVisible(true);
							pRightJScrollPane.setViewportView(pJTextAreaLoad);
							break;
						default : ;
						}
					}
					else {
					}
				}
				else {
				}
			}
		};
		ChangeListener changeTab = new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				switch(pJTabbedPane.getSelectedIndex()) {
					case 0 :  // Configuration
						pJTextAreaLoad.setVisible(false);
						pRightJScrollPane.setViewportView(pJTextAreaConfiguration);
						pJTextAreaConfiguration.setVisible(true);
						break;
					case 1 : // Load
						pJTextAreaConfiguration.setVisible(false);
						pRightJScrollPane.setViewportView(pJTextAreaLoad);
						pJTextAreaLoad.setVisible(true);
						break;
					default : ;
				}
			}
		};
		ActionListener actionOpen = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int lRead_bytes;
				String lFilename = new String();
				File lFile;
				final Display lDisplay = new Display();
				final Shell lShell = new Shell(lDisplay);
				FileDialog lDialog = new FileDialog(lShell, SWT.OPEN);
				lDialog.setFilterExtensions(new String[] { "*.xml", "*.*" });
				lDialog.setText("Open LSPB configuration file");
				lFilename = lDialog.open();
				lDisplay.dispose();
				if (lFilename != null) {
					lFile = new File(lFilename);
					if (lFile.canRead()) {
						lRead_bytes = (int) lFile.length();
						setTitle(kTitle + "-" + lFilename);
						ReadXMLFile(lFilename);
						if (pXML_configuration_file.aF_debug) {
							pStatus_bar.setMessage("ID : " + pXML_configuration_file.aKey + " / KEY : " + Hash(pXML_configuration_file.aDefaultMACAddress));
						}
						else {
						}
						switch(pXML_configuration_file.aNorm_version) {
						case 1 :
							aARINC_norm_version = ARINC_norm_version.ARINC665_1;
							pJCheckBoxMenuItemARINC665_1.setSelected(true);
							pJCheckBoxMenuItemARINC665_2.setSelected(false);
							pJCheckBoxMenuItemARINC665_3.setSelected(false);
							break;
						default :
						case 2 :
							aARINC_norm_version = ARINC_norm_version.ARINC665_2;
							pJCheckBoxMenuItemARINC665_1.setSelected(false);
							pJCheckBoxMenuItemARINC665_2.setSelected(true);
							pJCheckBoxMenuItemARINC665_3.setSelected(false);
							break;
						case 3 :
							aARINC_norm_version = ARINC_norm_version.ARINC665_3;
							pJCheckBoxMenuItemARINC665_1.setSelected(false);
							pJCheckBoxMenuItemARINC665_2.setSelected(false);
							pJCheckBoxMenuItemARINC665_3.setSelected(true);
							break;
						}
						pJTreeConfiguration = new JTree();
						pJTabbedPane.add("Configuration", pJTreeConfiguration);
						pJMenuItemGenerate.setEnabled(true);
						pRootNodeConfiguration = new DefaultMutableTreeNode("Configuration file : " + lFilename, true);
						DefaultMutableTreeNode lNode = new DefaultMutableTreeNode();
						lNode = new DefaultMutableTreeNode("ARINC665-" + String.format("%d", pXML_configuration_file.aNorm_version), true);
						pRootNodeConfiguration.add(lNode);
						lNode = new DefaultMutableTreeNode("ARINC code : " + pXML_configuration_file.aMMM, true);
						pRootNodeConfiguration.add(lNode);
						lNode = new DefaultMutableTreeNode(String.format("Target HW ID : %d", pXML_configuration_file.aTHWID.length), true);
						pRootNodeConfiguration.add(lNode);
						DefaultMutableTreeNode lSubNode;
						for(int i=0;i < pXML_configuration_file.aTHWID.length;i++) {
							lSubNode = new DefaultMutableTreeNode(pXML_configuration_file.aTHWID[i], true);
							lNode.add(lSubNode);
						}
						lNode = new DefaultMutableTreeNode("Load", true);
						pRootNodeConfiguration.add(lNode);
						lSubNode = new DefaultMutableTreeNode("Part Number : " + pXML_configuration_file.aLoad_PN, true);
						lNode.add(lSubNode);
						if (aARINC_norm_version == ARINC_norm_version.ARINC665_3) {
							if (pXML_configuration_file.aLoad_type_description != null) {
								lSubNode = new DefaultMutableTreeNode("Load type description : " + pXML_configuration_file.aLoad_type_description, true);
								lNode.add(lSubNode);
								lSubNode = new DefaultMutableTreeNode("Load type ID : " + String.format("%d", (int)pXML_configuration_file.aLoad_type_ID), true);
								lNode.add(lSubNode);
							}
							else {
							}
							switch(pXML_configuration_file.aLoad_integrity_check) {
								case 4 :
									lSubNode = new DefaultMutableTreeNode("Integrity check : MD5\n", true);
									lNode.add(lSubNode);
								break;
								case 5 :
									lSubNode = new DefaultMutableTreeNode("Integrity check : SHA-1\n", true);
									lNode.add(lSubNode);
								break;
								default : ;
							}
						}
						else {
						}
						lFile = new File(pXML_configuration_file.aInput_file);
						if (lFile.canRead()) {
							lSubNode = new DefaultMutableTreeNode("Input file : " + pXML_configuration_file.aInput_file, true);
						}
						else {
							lSubNode = new DefaultMutableTreeNode("*** Error *** Input file : " + pXML_configuration_file.aInput_file, true);
						}
						lNode.add(lSubNode);
						DefaultMutableTreeNode lSubSubNode;
						switch(pXML_configuration_file.aInput_file_integrity_check) {
							case 4 :
								lSubSubNode = new DefaultMutableTreeNode("Integrity check : MD5\n", true);
								lSubNode.add(lSubSubNode);
							break;
							case 5 :
								lSubSubNode = new DefaultMutableTreeNode("Integrity check : SHA-1\n", true);
								lSubNode.add(lSubSubNode);
							break;
							default : ;
						}
						lSubSubNode = new DefaultMutableTreeNode("Padding character : " + String.format("%d (ASCII code)", pXML_configuration_file.aPadding), true);
						lSubNode.add(lSubSubNode);
						lSubNode = new DefaultMutableTreeNode("Split size : " + String.format("%d bytes", pXML_configuration_file.aSplit_size), true);
						lNode.add(lSubNode);
						lSubNode = new DefaultMutableTreeNode("Sub directory : " + pXML_configuration_file.aSub_directory, true);
						lNode.add(lSubNode);
						if (pXML_configuration_file.aSupport_file != null) {
							lSubNode = new DefaultMutableTreeNode(String.format("Support files : %d", pXML_configuration_file.aSupport_file.length), true);
							lNode.add(lSubNode);
							if (pXML_configuration_file.aSupport_file_integrity_check == 4) {
								lSubSubNode = new DefaultMutableTreeNode("Integrity check : MD5\n", true);
								lSubNode.add(lSubSubNode);
							}
							else {
							}
							for(int i=0;i < pXML_configuration_file.aSupport_file.length;i++) {
								lSubSubNode = new DefaultMutableTreeNode(String.format("File #%d : %s", i+1, pXML_configuration_file.aSupport_file[i]), true);
								lSubNode.add(lSubSubNode);
							}
						}
						else {
						}
						if (pXML_configuration_file.aF_batch_required) {
							lSubNode = new DefaultMutableTreeNode("Comment for batch file : " + pXML_configuration_file.aComment, true);
							lNode.add(lSubNode);
						}
						else {
						}
						if (pXML_configuration_file.aUser_data_file != null) {
							lSubNode = new DefaultMutableTreeNode(String.format("User data file : %s", pXML_configuration_file.aUser_data_file), true);
							lNode.add(lSubNode);
						}
						else if (pXML_configuration_file.aUser_data_text != null) {
							lSubNode = new DefaultMutableTreeNode(String.format("User data text : %s", pXML_configuration_file.aUser_data_text), true);
							lNode.add(lSubNode);
						}
						else if (pXML_configuration_file.aF_HW_SW_compatibility_index_present) {
							lNode = new DefaultMutableTreeNode("HW/SW compatibility index : " + String.format("%d", (int) pXML_configuration_file.aHW_SW_compatibility_index), true);
							pRootNodeConfiguration.add(lNode);
							lNode = new DefaultMutableTreeNode("HW functional designation : " + pXML_configuration_file.aHW_functional_designation, true);
							pRootNodeConfiguration.add(lNode);
						}
						else {
						}
						if (pXML_configuration_file.aF_media_required) {
							lNode = new DefaultMutableTreeNode("Media", true);
							pRootNodeConfiguration.add(lNode);
							lSubNode = new DefaultMutableTreeNode("Part Number : " + pXML_configuration_file.aMedia_PN, true);
							lNode.add(lSubNode);
						}
						else {
						}
						DefaultTreeModel lTreeModel = new DefaultTreeModel(pRootNodeConfiguration);
						pJTreeConfiguration.setModel(lTreeModel);
						// Use the new renderer class for tree cells
						MyTreeCellRenderer lMyTreeCellRenderer = new MyTreeCellRenderer();
						pJTreeConfiguration.setCellRenderer(lMyTreeCellRenderer);
						// Modify the way to select elements of tree : uniq selection
						pJTreeConfiguration.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
						// Make visible the split pane
						pSplitPane.resetToPreferredSizes();
						try {
							FileInputStream lIn_file = new FileInputStream(lFilename);
							byte[] lBytes = new byte[lRead_bytes];
							lIn_file.read(lBytes);
							lIn_file.close();
							String lString = new String();
							for(byte b : lBytes) {
								lString += String.format("%c", b);
							}
							pJTextAreaConfiguration.setText(lString);
							pJTextAreaConfiguration.setEditable(false);
							pSplitPane.setRightComponent(pRightJScrollPane);
						} catch (FileNotFoundException e) {
						} catch (IOException e) {
						}
						pSplitPane.setVisible(true);
					}
					else {
					}
				}
				else {
				}
			}
		};
		ActionListener actionQuit = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		};
		ActionListener actionARINC665_1 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pJCheckBoxMenuItemARINC665_2.setSelected(false);
				pJCheckBoxMenuItemARINC665_3.setSelected(false);
				aARINC_norm_version = ARINC_norm_version.ARINC665_1;
			}
		};
		ActionListener actionARINC665_2 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pJCheckBoxMenuItemARINC665_1.setSelected(false);
				pJCheckBoxMenuItemARINC665_3.setSelected(false);
				aARINC_norm_version = ARINC_norm_version.ARINC665_2;
			}
		};
		ActionListener actionARINC665_3 = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pJCheckBoxMenuItemARINC665_1.setSelected(false);
				pJCheckBoxMenuItemARINC665_2.setSelected(false);
				aARINC_norm_version = ARINC_norm_version.ARINC665_3;
			}
		};
		ActionListener actionGenerate = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String lCC;
				DefaultMutableTreeNode lNode;
				RemoveDataFile();
				lCC = Integer.toHexString(integrity_check.CalculateCRC8(pXML_configuration_file.aMMM + pXML_configuration_file.aLoad_PN)).toUpperCase();
				BuildLoad(lCC, aARINC_norm_version);
				pJTreeLoad = new JTree();
				pJTreeLoad.addTreeSelectionListener(treeSelection);
				JScrollPane lJScrollPaneLoad = new JScrollPane();
				pJTabbedPane.add("Load", lJScrollPaneLoad);
				lJScrollPaneLoad.setViewportView(pJTreeLoad);
				pRootNodeLoad = new DefaultMutableTreeNode("Load : " + pXML_configuration_file.aMMM + lCC + pXML_configuration_file.aLoad_PN, true);
				pRootNodeLoad.add(GetDataFileSubTree());
				pRootNodeLoad.add(GetHeaderFileSubTree());
				if (pXML_configuration_file.aF_media_required) {
					pRootNodeLoad.add(GetLoadsFileSubTree());
					pRootNodeLoad.add(GetFilesFileSubTree());
				}
				else {
				}
				if (pF_with_CRC) {
					lNode = new DefaultMutableTreeNode(String.format("Load CRC32 : 0x%08X", LSPB.aARINC665.aSoftware_part.aCRC32), true);
				}
				else {
					lNode = new DefaultMutableTreeNode(String.format("Load CRC32 : 0"), true);
				}
				pRootNodeLoad.add(lNode);
				DefaultTreeModel lTreeModel = new DefaultTreeModel(pRootNodeLoad);
				pJTreeLoad.setModel(lTreeModel);
			}
		};
		ActionListener actionAbout = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				final Display lDisplay = new Display();
				final Shell lShell = new Shell(lDisplay);
				MessageBox lMessageBox = new MessageBox(lShell, SWT.ICON_INFORMATION | SWT.OK);
				lMessageBox.setMessage("Loadable Software Part Builder (64 bits)\n" +
						"© Assistance Informatique Toulouse 2019\n\n" +
						"Written in Java under Eclipse with SWT\n" +
						"Start with Eclipse 3.5 Galileo + SWT 3.6.1\n" +
						"Migration to Eclipse 3.6 Indigo + SWT 3.7.1\n" +
						"Migration to Eclipse 3.7 Juno + SWT 3.7.1\n" +
						"Migration to Eclipse 4.4 Luna + SWT 4.4\n" +
						"Migration to Eclipse 4.5.2 Mars + SWT 4.4\n" +
						"Migration to Eclipse 4.13.0 (2019-09 R) + SWT 4.13\n\n" +
						"ARINC665 load generator, see limitations\n" +
						"Input file is not modified, only splitted\n\n" +
						"Corrections, modifications can be requested to assistanceinformatiquetoulouse@gmail.com\n"
						);
				lMessageBox.setText(kTitle);
				lMessageBox.open();
				lDisplay.dispose();
			}
		};
		ActionListener actionLimitation = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				final Display lDisplay = new Display();
				final Shell lShell = new Shell(lDisplay);
				MessageBox lMessageBox = new MessageBox(lShell, SWT.ICON_INFORMATION | SWT.OK);
				lMessageBox.setMessage("For limitations, see §6 of user's manual\n");
				lMessageBox.setText(kTitle);
				lMessageBox.open();
				lDisplay.dispose();
			}
		};
		this.setTitle(kTitle);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.setIconImage(new ImageIcon(getClass().getResource(kARINC_logo)).getImage());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JMenuBar lJMenuBar = new JMenuBar();
		setJMenuBar(lJMenuBar);

		JMenu lJMenuFile = new JMenu("File");
		lJMenuFile.setMnemonic('F');
		lJMenuBar.add(lJMenuFile);

		JMenuItem lJMenuItemOpen = new JMenuItem("Open ...");
		lJMenuItemOpen.setMnemonic('O');
		lJMenuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		lJMenuFile.add(lJMenuItemOpen);
		lJMenuItemOpen.addActionListener(actionOpen);

		JMenuItem lJMenuItemQuit = new JMenuItem("Quit");
		lJMenuItemQuit.setMnemonic('Q');
		lJMenuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		lJMenuFile.add(lJMenuItemQuit);
		lJMenuItemQuit.addActionListener(actionQuit);

		JMenu lJMenuNorm = new JMenu("Report");
		lJMenuNorm.setMnemonic('R');
		lJMenuBar.add(lJMenuNorm);

		pJCheckBoxMenuItemARINC665_1 = new JCheckBoxMenuItem("ARINC665-1");
		pJCheckBoxMenuItemARINC665_1.setEnabled(false);
		pJCheckBoxMenuItemARINC665_1.setMnemonic('1');
		pJCheckBoxMenuItemARINC665_1.setSelected(false);
		pJCheckBoxMenuItemARINC665_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
		pJCheckBoxMenuItemARINC665_1.addActionListener(actionARINC665_1);
		lJMenuNorm.add(pJCheckBoxMenuItemARINC665_1);

		pJCheckBoxMenuItemARINC665_2 = new JCheckBoxMenuItem("ARINC665-2");
		pJCheckBoxMenuItemARINC665_2.setEnabled(true);
		pJCheckBoxMenuItemARINC665_2.setMnemonic('2');
		pJCheckBoxMenuItemARINC665_2.setSelected(true);
		pJCheckBoxMenuItemARINC665_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));
		pJCheckBoxMenuItemARINC665_2.addActionListener(actionARINC665_2);
		lJMenuNorm.add(pJCheckBoxMenuItemARINC665_2);
		aARINC_norm_version = ARINC_norm_version.ARINC665_2;

		pJCheckBoxMenuItemARINC665_3 = new JCheckBoxMenuItem("ARINC665-3");
		pJCheckBoxMenuItemARINC665_3.setEnabled(true);
		pJCheckBoxMenuItemARINC665_3.setMnemonic('3');
		pJCheckBoxMenuItemARINC665_3.setSelected(false);
		pJCheckBoxMenuItemARINC665_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));
		pJCheckBoxMenuItemARINC665_3.addActionListener(actionARINC665_3);
		lJMenuNorm.add(pJCheckBoxMenuItemARINC665_3);

		pJMenuItemGenerate = new JMenuItem("Generate");
		pJMenuItemGenerate.setMaximumSize(new Dimension(65, 50));
		pJMenuItemGenerate.setEnabled(false);
		pJMenuItemGenerate.setMnemonic('G');
		lJMenuBar.add(pJMenuItemGenerate);
		pJMenuItemGenerate.addActionListener(actionGenerate);

		JMenu lJMenuHelp = new JMenu("Help");
		lJMenuHelp.setMnemonic('H');
		lJMenuBar.add(lJMenuHelp);

		JMenuItem lJMenuItemAbout = new JMenuItem("About");
		lJMenuItemAbout.setMnemonic('A');
		lJMenuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		lJMenuHelp.add(lJMenuItemAbout);
		lJMenuItemAbout.addActionListener(actionAbout);

		JMenuItem lJMenuItemLimitation = new JMenuItem("Limitations");
		lJMenuItemLimitation.setMnemonic('L');
		lJMenuHelp.add(lJMenuItemLimitation);
		lJMenuItemLimitation.addActionListener(actionLimitation);

		// TODO Rendre la JSplitPane invisible jusqu'à l'ouverture du fichier xml
		pSplitPane = new JSplitPane();
		//pSplitPane.setVisible(false);

		pJTabbedPane = new JTabbedPane();
		pJTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		pJTabbedPane.addChangeListener(changeTab);
		pSplitPane.setLeftComponent(pJTabbedPane);

		String lString = new String("ID : ");
		for(byte b : pMACAddress) {
			lString = lString + String.format("%02X", b);
		}
		pStatus_bar = new StatusBar(lString);

		JPanel lLeftJPanel = new JPanel(new BorderLayout());
		lLeftJPanel.add(BorderLayout.SOUTH, pStatus_bar);
		lLeftJPanel.add(BorderLayout.CENTER, pSplitPane);
		setContentPane(lLeftJPanel);

		pJTextAreaConfiguration = new JTextArea();
		pJTextAreaLoad = new JTextArea();
		pJTextAreaLoad.setFont(new Font("Courier", Font.PLAIN, 12));
		pRightJScrollPane = new JScrollPane(pJTextAreaConfiguration);
		
		JPanel lRightJpanel = new JPanel(new BorderLayout());
		JLabel lJLabel = new JLabel(new ImageIcon(getClass().getResource(kARINC_logo)));
		lRightJpanel.add(BorderLayout.CENTER, lJLabel);
		pSplitPane.setRightComponent(lRightJpanel);
    }
}
