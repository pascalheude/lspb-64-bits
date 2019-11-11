package lspb;

import java.io.File;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/*
 * Class : XMLConfigurationFileHanlder
 */
class XMLConfigurationFileHanlder extends DefaultHandler {
    /**************************************************************************
     ** Types                                                                **
     **************************************************************************/
    private enum T_XMLTag {
    NONE,
    KEY,
    ARINC665_CONFIGURATION_FILE,
    BATCH,
    CODE,
    LOAD,
    PART_NUMBER,
    TYPE_DESCRIPTION,
    INPUT_FILE,
    SPLIT_SIZE,
    THW_ID_LIST,
    THW_ID,
    DIRECTORY,
    SUPPORT_FILE_LIST,
    SUPPORT_FILE,
    USER_DATA,
    USER_DATA_FILE,
    USER_DATA_TEXT,
    USER_DATA_BCC,
    HW_SW_COMPATIBILITY_INDEX,
    HW_FUNCTIONAL_DESIGNATION,
    MEDIA
    }
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    private T_XMLTag pXML_tag;
    private int pLevel;
    private int pCurrent_number_of_THW_ID;
    private int pCurrent_number_of_support_file;
	public int aNorm_version;
	public boolean aF_batch_required;
	public boolean aF_media_required;
	public byte[] aDefaultMACAddress;
    public String aKey;
    public boolean aF_debug;
	public String aComment;
	public String aMMM;
	public String aLoad_PN;
	public String aMedia_PN;
	public String aLoad_type_description;
	public char aLoad_type_ID;
	public char aLoad_integrity_check;
	public String aInput_file;
	public char aInput_file_integrity_check;
	public byte aPadding;
	public String aSub_directory;
	public int aSplit_size;
	public String[] aTHWID;
	public String[] aSupport_file;
	public char aSupport_file_integrity_check;
	public String aUser_data_file;
	public String aUser_data_text;
	public String aHW_functional_designation;
	public boolean aF_HW_SW_compatibility_index_present;
	public char aHW_SW_compatibility_index;
	/**************************************************************************
	 ** Private method : DisplayUsage                                        **
	 **************************************************************************/
	private static char GetIntegrityCheckValue(String a_string) {
        if (a_string.equals("MD5")) {
            return(4);
        }
        else if (a_string.equals("SHA-1")) {
            return(5);
        }
        else {
        	return(0);
        }
	}
   /**************************************************************************
     ** Public method : startElement                                         **
     **************************************************************************/
    public void startElement(String a_name_space,
                             String a_local_name, 
                             String a_qualified_name,
                             Attributes a_attr) throws SAXException {
    	pLevel++;
        if (a_qualified_name.equals("ARINC665_CONFIGURATION_FILE")) {
            pXML_tag = T_XMLTag.ARINC665_CONFIGURATION_FILE;
            try {
                int j = a_attr.getIndex("version");
                if (j != -1) {
                    Integer i = new Integer(a_attr.getValue("version"));
                    aNorm_version = i.intValue();
                }
                else {
                	aNorm_version = 2;
                }
            }
            catch(Exception e) {
            	aNorm_version = 0;
            }
            if ((aNorm_version < 1) || (aNorm_version > 3)) {
            	throw new SAXException("*** Error *** ARINC665 version shall be 1 or 2 or 3.");
            }
            else {
            }
            aF_HW_SW_compatibility_index_present = false;
            pCurrent_number_of_THW_ID = 0;
            pCurrent_number_of_support_file = 0;
        }
        else if (a_qualified_name.equals("KEY")) {
        	int j = a_attr.getIndex("debug");
        	if (j != -1) {
        		aF_debug = true;
        	}
        	else {
        	}
    		pXML_tag = T_XMLTag.KEY;
    	}
        else if (a_qualified_name.equals("BATCH")) {
            if (pLevel == 3) {
                pXML_tag = T_XMLTag.BATCH;
            }
        	else {
        	    throw new SAXException("*** Error *** Tag BATCH shall be inside tag LOAD");
            }
        }
        else if (a_qualified_name.equals("CODE")) {
            pXML_tag = T_XMLTag.CODE;
        }
        else if (a_qualified_name.equals("LOAD")) {
            pXML_tag = T_XMLTag.LOAD;
            try {
                int j = a_attr.getIndex("integrity_check");
                if (j != -1) {
                	aLoad_integrity_check = GetIntegrityCheckValue(a_attr.getValue("integrity_check"));
                }
                else {
                	aLoad_integrity_check = 0;
                }
            }
            catch(Exception e) {
            	aLoad_integrity_check = 0;
            }
        }
        else if (a_qualified_name.equals("PART_NUMBER")) {
        		pXML_tag = T_XMLTag.PART_NUMBER;
        }
        else if (a_qualified_name.equals("TYPE_DESCRIPTION")) {
        	if (pLevel == 3) {
                pXML_tag = T_XMLTag.TYPE_DESCRIPTION;
                try {
                    int j = a_attr.getIndex("id");
                    if (j != -1) {
                        Integer i = new Integer(a_attr.getValue("id"));
                        aLoad_type_ID = (char) i.intValue();
                    }
                    else {
                    	aLoad_type_ID = 0;
                    }
                }
                catch(Exception e) {
                	aLoad_type_ID = 0;
                }
        	}
        	else {
        		throw new SAXException("*** Error *** Tag TYPE_DESCRIPTION shall be inside tag LOAD");
        	}
        }
        else if (a_qualified_name.equals("INPUT_FILE")) {
            pXML_tag = T_XMLTag.INPUT_FILE;
            try {
                int j = a_attr.getIndex("integrity_check");
                if (j != -1) {
                	aInput_file_integrity_check = GetIntegrityCheckValue(a_attr.getValue("integrity_check"));
                }
                else {
                	aInput_file_integrity_check = 0;
                }
            }
            catch(Exception e) {
            	aInput_file_integrity_check = 0;
            }
            try {
                int j = a_attr.getIndex("padding");
                if (j != -1) {
                    Integer i = new Integer(a_attr.getValue("padding"));
                    aPadding = (byte) i.intValue();
                }
                else {
                	aPadding = 0;
                }
            }
            catch(Exception e) {
            	aPadding = 0;
            }
        }
        else if (a_qualified_name.equals("SPLIT_SIZE")) {
            pXML_tag = T_XMLTag.SPLIT_SIZE;
        }
        else if (a_qualified_name.equals("DIRECTORY")) {
            pXML_tag = T_XMLTag.DIRECTORY;
        }
        else if (a_qualified_name.equals("THW_ID_LIST"))  {
            pXML_tag = T_XMLTag.THW_ID_LIST;
            try {
                Integer i = new Integer(a_attr.getValue("number"));
                if (i <= 0) {
                    throw new SAXException("*** Error *** Number in tag THW_ID_LIST shall be greater than 0");
                }
                else {
                    aTHWID = new String[i.intValue()];
                }
            }
            catch (Exception e) {
                throw new SAXException(e);
            }
        }
        else if (a_qualified_name.equals("THW_ID")) {
        	if (pLevel == 3) {
        		pXML_tag = T_XMLTag.THW_ID;
        	}
        	else {
        		throw new SAXException("*** Error *** Tag THW_ID shall be inside tag THW_ID_LIST");
        	}
        }
        else if (a_qualified_name.equals("SUPPORT_FILE_LIST")) {
            pXML_tag = T_XMLTag.SUPPORT_FILE_LIST;
            try {
                int j = a_attr.getIndex("integrity_check");
                if (j != -1) {
                	aSupport_file_integrity_check = GetIntegrityCheckValue(a_attr.getValue("integrity_check"));
                }
                else {
                	aSupport_file_integrity_check = 0;
                }
            }
            catch(Exception e) {
            	aSupport_file_integrity_check = 0;
            }
            try {
                Integer i = new Integer(a_attr.getValue("number"));
                if (i < 0) {
                    throw new SAXException("*** Error *** Number in tag SUPPORT_FILE_LIST shall be greater than 0");
                }
                else if (i > 0) {
                	aSupport_file = new String[i.intValue()];
                }
                else {
                }
            }
            catch (Exception e) {
                throw new SAXException(e);
            }
        }
        else if (a_qualified_name.equals("SUPPORT_FILE")) {
        	if (pLevel == 4) {
        		pXML_tag = T_XMLTag.SUPPORT_FILE;
        	}
        	else {
        		throw new SAXException("*** Error *** Tag SUPPORT_FILE shall be inside tag SUPPORT_FILE_LIST");
        	}
        }
        else if (a_qualified_name.equals("USER_DATA")) {
        	if (pLevel == 3) {
                pXML_tag = T_XMLTag.USER_DATA;
        	}
        	else {
        	    throw new SAXException("*** Error *** Tag USER_DATA shall be inside tag LOAD");
        	}
        }
        else if (a_qualified_name.equals("USER_DATA_FILE")) {
            pXML_tag = T_XMLTag.USER_DATA_FILE;
        }
        else if (a_qualified_name.equals("USER_DATA_TEXT")) {
            pXML_tag = T_XMLTag.USER_DATA_TEXT;
        }
        else if (a_qualified_name.equals("USER_DATA_BCC")) {
        	if (pLevel == 4) {
                pXML_tag = T_XMLTag.USER_DATA_BCC;
        	}
        	else {
        		throw new SAXException("*** Error *** Tag USER_DATA_BCC shall be inside tag USER_DATA");
        	}
        }
        else if (a_qualified_name.equals("HW_SW_COMPATIBILITY_INDEX")) {
        	if (pLevel == 5) {
                pXML_tag = T_XMLTag.HW_SW_COMPATIBILITY_INDEX;
                aF_HW_SW_compatibility_index_present = true;
        	}
        	else {
        		throw new SAXException("*** Error *** Tag HW_SW_COMPATIBILITY_INDEX shall be inside tag USER_DATA_BCC");
        	}
        }
        else if (a_qualified_name.equals("HW_FUNCTIONAL_DESIGNATION")) {
        	if (pLevel == 5) {
                pXML_tag = T_XMLTag.HW_FUNCTIONAL_DESIGNATION;
        	}
        	else {
        		throw new SAXException("*** Error *** Tag HW_FUNCTIONAL_DESIGNATION shall be inside tag USER_DATA_BCC");
        	}
        }
        else if (a_qualified_name.equals("MEDIA")) {
        	pXML_tag = T_XMLTag.MEDIA;
        }
        else {
            throw new SAXException("*** Error *** Unknown tag " + a_qualified_name);
        }
    }
    /**************************************************************************
     ** Public method : endElement                                           **
     **************************************************************************/
    public void endElement(String a_name_space,
                           String a_local_name, 
                           String a_qualified_name) throws SAXException {
    	pXML_tag = T_XMLTag.NONE;
    	pLevel--;
    }
    /**************************************************************************
     ** Public method : startDocument                                        **
     **************************************************************************/
    public void startDocument() {
    	aDefaultMACAddress = new byte[6];
    	aKey = null;
    	aF_debug = false;
    	aNorm_version = 0;
    	aF_batch_required = false;
    	aF_media_required = false;
    	aComment = null;
    	aMMM = null;
    	aInput_file = null;
    	aSub_directory = null;
    	aLoad_PN = null;
    	aMedia_PN = null;
    	aLoad_type_description = null;
    	aLoad_type_ID = 0;
    	aLoad_integrity_check = 0;
    	aSplit_size = 32748;
    	aTHWID = null;
    	aSupport_file = null;
    	aUser_data_file = null;
    	aUser_data_text = null;
    	aHW_functional_designation = null;
    	aF_HW_SW_compatibility_index_present = false;
    	aHW_SW_compatibility_index = 0;
        pXML_tag = T_XMLTag.NONE;
        pLevel = 0;
        pCurrent_number_of_THW_ID = 0;
        pCurrent_number_of_support_file = 0;
    }
    /**************************************************************************
     ** Public method : endDocument                                          **
     **************************************************************************/
    public void endDocument() throws SAXException {
        int i;

        if (aMMM == null) {
            throw new SAXException("*** Error *** Empty CODE tag in xml file");
        }
        else {
        }
        if (aLoad_PN == null) {
            throw new SAXException("*** Error *** Empty PART_NUMBER tag in xml file");
        }
        else {
        }
        if (aInput_file == null) {
            throw new SAXException("*** Error *** Empty INPUT_FILE tag in xml file");
        }
        else {
        }
        if (aSub_directory == null) {
            throw new SAXException("*** Error *** Empty DIRECTORY tag in xml file");
        }
        else {
        }
        for(i=0;i < aTHWID.length;i++) {
            if (aTHWID[i] == null) {
                throw new SAXException("*** Error *** Empty THW_ID tag in xml file");
            }
            else {
            }
        }
        if (aF_HW_SW_compatibility_index_present &&
            (aHW_functional_designation == null)) {
            throw new SAXException("*** Error *** Empty HW_FUNCTIONAL_DESIGNATION tag in xml file");
        }
        else {
        }
        if (pCurrent_number_of_THW_ID != aTHWID.length) {
            throw new SAXException("*** Error *** Not enough THW ID");
        }
        else {
        }
        if (aSupport_file != null) {
            for(i=0;i < aSupport_file.length;i++) {
                if (aSupport_file[i] == null) {
                    throw new SAXException("*** Error *** Empty support file tag in xml file");
                }
                else {
                }
            }
            if (pCurrent_number_of_THW_ID != aSupport_file.length) {
                throw new SAXException("*** Error *** Not enough support file");
            }
            else {
            }
        }
        else {
        }
    }
    /**************************************************************************
     ** Public method : characters                                           **
     **************************************************************************/
    public void characters(char[] a_TAB_char,
                           int a_start_index, 
                           int a_length) throws SAXException {
        String lString = new String(a_TAB_char, a_start_index, a_length);
        File lFile;

        switch(pXML_tag)
        {
        	default :
            case NONE :
            	break;
            case ARINC665_CONFIGURATION_FILE :
            	System.out.printf("ARINC665-%d\n", aNorm_version);
                break;
            case KEY :
            	aKey = new String(lString);
            	if (aF_debug) {
                	int i;
            		for (i=0;(i < (2 * (aKey.length() / 2))) && (i < 12);i+=2) {
            			//lKey = "0x" + aKey.substring(i, i + 1);
            			Integer b = new Integer(0);
            			b = Integer.parseInt(aKey.substring(i, i + 2), 16);
            			aDefaultMACAddress[i / 2] = b.byteValue();
            		}
            	}
            	else {
            	}
                System.out.printf("Used key : %s\n", lString);
                break;
            case BATCH :
                if (lString.equalsIgnoreCase("no")) {
                    System.out.println("Batch files will not be generated");
                    aF_batch_required = false;
                    aComment = "";
                }
                else if (lString.equalsIgnoreCase("yes")) {
                        System.out.println("Batch files will be generated without comment");
                        aF_batch_required = true;
                        aComment = "";
                }
                else {
                    if (lString.length() != 0) {
                        System.out.printf("Batch files will be generated with comment : %s\n", lString);
                    	aF_batch_required = true;
                    	aComment = lString;
                    }
                    else {
                    }
                }
                break;
            case CODE :
                if (a_length != 3) {
                    throw new SAXException("*** Error *** Invalid company code");
                }
                else {
                }
                aMMM = lString;
                System.out.printf("Company name : %s\n", aMMM);
                break;
            case LOAD : // TBT
            	if (aLoad_integrity_check == 4) {
                    if (aNorm_version == 2) {
                    	System.out.printf("*** Information *** integrity check is useless with ARINC665 version 2\n");
                    }
                    else {
                		System.out.printf("Load integrity check : MD5\n");
                    }
            	}
            	else if (aLoad_integrity_check == 5) {
                    if (aNorm_version == 2) {
                    	System.out.printf("*** Information *** integrity check is useless with ARINC665 version 2\n");
                    }
                    else {
                		System.out.printf("Load integrity check : SHA-1\n");
                    }
            	}
            	else {
            	}
                break;
            case PART_NUMBER :
                aLoad_PN = lString;
                aMedia_PN = lString;
                System.out.printf("Load name : %s\n", aLoad_PN);
                break;
            case TYPE_DESCRIPTION :
            	aLoad_type_description = lString;
                System.out.printf("Load type description : %s\n", aLoad_type_description);
                System.out.printf("Load type ID : %d\n", (int) aLoad_type_ID);
                if (aNorm_version == 2) {
                	System.out.printf("Type description is useless with ARINC665 version 2\n"); // TBT
                }
                else {
                }
            	break;
            case INPUT_FILE :
            	if (aInput_file_integrity_check == 4) {
                    if (aNorm_version == 2) {
                    	System.out.printf("*** Information *** integrity check is useless with ARINC665 version 2\n");
                    }
                    else {
                		System.out.printf("Input file integrity check : MD5\n");
                    }
            	}
            	else if (aInput_file_integrity_check == 5) {
                    if (aNorm_version == 2) {
                    	System.out.printf("*** Information *** integrity check is useless with ARINC665 version 2\n");
                    }
                    else {
                		System.out.printf("Input file integrity check : SHA-1\n");
                    }
            	}
            	else {
            	}
                aInput_file = lString;
                System.out.printf("Input file name : %s\n", aInput_file);
                lFile = new File(aInput_file);
                if (lFile.canRead() == false) {
                    throw new SAXException("*** Error *** Can not read input file");
                }
                else {
                }
                System.out.printf("Padding character : %d (ASCII code)\n", aPadding);
                break;
            case DIRECTORY :
                aSub_directory = lString;
                System.out.printf("Directory : %s\n", aSub_directory);
                break;
            case SPLIT_SIZE :
                Integer i = new Integer(lString);
                aSplit_size = i.intValue();
                if ((i <= 0) ||
                    ((i & 1) == 1)) {
                    throw new SAXException("*** Error *** Invalid split size");
                }
                else {
                }
                System.out.printf("Split size : %d\n", aSplit_size);
                break;
            case THW_ID_LIST :
                System.out.printf("Number of THW ID : %d\n", aTHWID.length);
                break;
            case THW_ID :
                if (pCurrent_number_of_THW_ID < aTHWID.length) {
                    aTHWID[pCurrent_number_of_THW_ID] = new String(lString);
                    System.out.printf("THW ID #%d : %s\n", pCurrent_number_of_THW_ID + 1, aTHWID[pCurrent_number_of_THW_ID]);
                    pCurrent_number_of_THW_ID++;
                }
                else {
                    throw new SAXException("*** Error *** Too many THW ID");
                }
                break;
            case SUPPORT_FILE_LIST :
            	if (aSupport_file_integrity_check == 4) {
                    if (aNorm_version == 2) {
                    	System.out.printf("*** Warning *** integrity check is useless with ARINC665 version 2\n");
                    }
                    else {
                		System.out.printf("Support file integrity check : MD5\n");
                    }
            	}
            	else if (aSupport_file_integrity_check == 5) {
                    if (aNorm_version == 2) {
                    	System.out.printf("*** Warning *** integrity check is useless with ARINC665 version 2\n");
                    }
                    else {
                		System.out.printf("Support file integrity check : SHA-1\n");
                    }
            	}
            	else {
            	}
            	if (aSupport_file == null) {
            		System.out.printf("Number of support files : 0\n");
            	}
            	else {
            		System.out.printf("Number of support files : %d\n", aSupport_file.length);
            	}
                break;
            case SUPPORT_FILE :
                if (pCurrent_number_of_support_file < aSupport_file.length) {
                	aSupport_file[pCurrent_number_of_support_file] = new String(lString);
                    System.out.printf("Support file #%d : %s\n", pCurrent_number_of_support_file + 1, aSupport_file[pCurrent_number_of_support_file]);
                    lFile = new File(aSupport_file[pCurrent_number_of_support_file]);
                    if (lFile.canRead() == false) {
                        throw new SAXException("*** Error *** Can not read support file " + aSupport_file[pCurrent_number_of_support_file]);
                    }
                    else {
                    }
                    pCurrent_number_of_support_file++;
                }
                else {
                    throw new SAXException("*** Error *** Too many support files");
                }
                break;
            case USER_DATA_FILE :
            	aUser_data_file = lString;
            	System.out.printf("User data file : %s\n", aUser_data_file);
                lFile = new File(aUser_data_file);
                if (lFile.canRead() == false) {
                    throw new SAXException("*** Error *** Can not read user data file");
                }
                else {
                }
                break;
            case USER_DATA_TEXT :
            	aUser_data_text = lString;
            	System.out.printf("User data text : %s\n", aUser_data_text);
                break;
            case HW_SW_COMPATIBILITY_INDEX :
                Integer j = new Integer(lString);
                aHW_SW_compatibility_index = (char) j.intValue();
                System.out.printf("HW/SW compatibility index : %d\n", (int) aHW_SW_compatibility_index);
                if (aHW_SW_compatibility_index == 0) {
                    System.out.println("*** Information *** Compatibility index check disabled\n");
                }
                else if (aHW_SW_compatibility_index > 254) {
                    throw new SAXException("*** Error *** Invalid HW/SW compatiblity index");
                }
                else {
                }
                break;
            case HW_FUNCTIONAL_DESIGNATION :
                aHW_functional_designation = lString;
                System.out.printf("HW functional designation : %s\n", aHW_functional_designation);
                break;
            case MEDIA :
            	aF_media_required = true;
            	break;
        }
    }
}