package arinc665;

import java.io.*;

import lspb.file;

/*
 * Class : header_file
 */
public class header_file {
	// (ARINC665-2 §2.2.3.1) (ARINC665-3 §2.2.3.1)
    /**************************************************************************
     ** Public class load_type                                               **
     **************************************************************************/
	public class load_type {
		public String aDescription;
		public char aID;
		public load_type(String a_load_type_description, char a_load_type_ID) {
			aDescription = a_load_type_description;
			aID = a_load_type_ID;
			
		}
	}
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    private static final String kFile_extension = "LUH";
    public String aSub_directory;
    public String aName;
    public int aLength;									// (ARINC665-2 §2.2.3.1.1)     (ARINC665-3 §2.2.3.1.1)
    public char aVersion;								// (ARINC665-2 §2.2.3.1.2)     (ARINC665-3 §2.2.3.1.2)
    public final char kSpare = 0;						// (ARINC665-2 §2.2.3.1.3)
    public char aPartFlags;								//                             (ARINC665-3 §2.2.3.1.3)
    public int aPointer_to_LPN;							//                             (ARINC665-3 §2.2.3.1.4)
    public int aPointer_to_THWID_list;					// (ARINC665-2 §2.2.3.1.4)     (ARINC665-3 §2.2.3.1.5)
    public int aPointer_to_DF_list;						// (ARINC665-2 §2.2.3.1.5)     (ARINC665-3 §2.2.3.1.6)
    public int aPointer_to_SF_list;						// (ARINC665-2 §2.2.3.1.6)     (ARINC665-3 §2.2.3.1.7)
    public int aPointer_to_UDD;							// (ARINC665-2 §2.2.3.1.7)     (ARINC665-3 §2.2.3.1.8)
    public int aPointer_to_LTD;							//                             (ARINC665-3 §2.2.3.1.9)
    public final int kPointer_to_THWID_with_position_list = 0;	//                     (ARINC665-3 §2.2.3.1.10)
    public int aPointer_to_LCV;							//                             (ARINC665-3 §2.2.3.1.11)
    public char aLPN_length;							// (ARINC665-2 §2.2.3.1.9)     (ARINC665-3 §2.2.3.1.13)
    public String aLoad_PN;								// (ARINC665-2 §2.2.3.1.10)    (ARINC665-3 §2.2.3.1.14)
    public load_type aLoad_type;						//                             (ARINC665-3 §2.2.3.1.16-18)
    public char aNumber_of_THWID;						// (ARINC665-2 §2.2.3.1.11)    (ARINC665-3 §2.2.3.1.20)
    public String[] aTHWID_list;						// (ARINC665-2 §2.2.3.1.12-13) (ARINC665-3 §2.2.3.1.21-22)
    public char aNumber_of_DF;							// (ARINC665-2 §2.2.3.1.14)    (ARINC665-3 §2.2.3.1.31)
    public data_file aData_file;						// (ARINC665-2 §2.2.3.1.15-21) (ARINC665-3 §2.2.3.1.32-42)
    public char aNumber_of_SF;							// (ARINC665-2 §2.2.3.1.23)    (ARINC665-3 §2.2.3.1.44)
    public support_file aSupport_file;					// (ARINC665-2 §2.2.3.1.24-30) (ARINC665-3 §2.2.3.1.45-54)
    public char[] aUser_defined_data;					// (ARINC665-2 §2.2.3.1.33)    (ARINC665-3 §2.2.3.1.57)
    public char aCheck_value_length;					//                             (ARINC665-3 §2.2.3.1.59)
    public char aCheck_value_type;						//                             (ARINC665-3 §2.2.3.1.60)
    public byte[] aCheck_value;							//                             (ARINC665-3 §2.2.3.1.61)
    public int aCRC16;									// (ARINC665-2 §2.2.3.1.34)    (ARINC665-3 §2.2.3.1.62)
    public String aHexa_dump;
    /**************************************************************************
     ** Constructor : header_file                                            **
     **************************************************************************/
    public header_file(ARINC_norm_version a_norm_version,
    		           String a_sub_directory,
                       String a_base_filename,
                       String a_load_PN,
                       String a_load_type_description,
                       char a_load_type_ID,
                       String[] a_THWID_list,
                       data_file a_data_file,
                       support_file a_support_file,
                       boolean a_F_with_UDD,
                       byte[] a_UDD,
                       char a_load_integrity_check) {
        int i;

        // Copy the directory
        aSub_directory = a_sub_directory;
        // Build the name of header file using the basename and the extension
        aName = a_base_filename  + "." + kFile_extension;
        // Set the length to 2 (size of Header File Length field)
        aLength = 2;
        // Set the Load File Format Version field
        aVersion = a_norm_version.getLoadFileFormatVersion();
        // Increase the length (size of Load File Format Version field)
        aLength++;
        if ((a_norm_version == ARINC_norm_version.ARINC665_1) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_2)) {
            // Set the Spare field
            // kSpare = 0;
            // Increase the length (size of Spare field)
            aLength++;
        }
        else {
            // Set the Part Flags field (ARINC665-3 §2.2.3.1.3)
            aPartFlags = 0;
            // Increase the length (size of Part Flags field)
            aLength++;
        }
        // Initialize the Pointer to Load Part Number field
        aPointer_to_LPN = 0;
        // Increase the length (size of Pointer to Load Part Number field)
        aLength += 2;
        // Initialize the Pointer to Target HW ID list field
        aPointer_to_THWID_list = 0;
        // Increase the length (size of Pointer to Target HW ID list field)
        aLength += 2;
        // Initialize the Pointer to Data File list field
        aPointer_to_DF_list = 0;
        // Increase the length (size of pointer to Data File list field)
        aLength += 2;
        // Initialize the Pointer to Support File list field
        aPointer_to_SF_list = 0;
        // Increase the length (size of Pointer to Support File list field)
        aLength += 2;
        // Initialize the Pointer to User Define Data field
        aPointer_to_UDD = 0;
        // Increase the length (size of Pointer to User Define Data field)
        aLength += 2;
        if (a_norm_version == ARINC_norm_version.ARINC665_3) {
        	// Initialize the Pointer to Load Type Description field (ARINC665-3 §2.2.3.1.9)
        	aPointer_to_LTD = 0;
            // Increase the length (size of Pointer to Load Type Description field)
            aLength += 2;
        	// Initialize the Pointer to THWID with positions field (ARINC665-3 §2.2.3.1.10)
        	// kPointer_to_THWID_with_position_list = 0;
            // Increase the length (size of Pointer to THWID with positions field)
            aLength += 2;
        	// Initialize the Pointer to Load Check Value field (ARINC665-3 §2.2.3.1.11)
        	aPointer_to_LCV = 0;
            // Increase the length (size of Pointer to Load Check Value field)
            aLength += 2;
        }
        else {
        }
        // Set the Pointer to Load Part Number field
        aPointer_to_LPN = aLength;
        // Set the Load PN Length field
        aLPN_length = (char) a_load_PN.length();
        // Increase the length (size of Load PN Length field)
        aLength++;
        // Set the Load PN field
        aLoad_PN = new String(a_load_PN);
        // Increase the length (size of Load PN field)
        aLength += (1 + a_load_PN.length()) / 2;
        // Set the Load Type Description fields if necessary
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) &&
        	(a_load_type_description != null)) {
        	// Set the Pointer to Load Type Description field (ARINC665-3 §2.2.3.1.9)
        	aPointer_to_LTD = aLength;
            // Increase the length (size of Load Type Description Length field)
        	aLength++;
        	// Set the Load Type
        	aLoad_type = new load_type(a_load_type_description, a_load_type_ID);
            // Increase the length (size of Load Type Description and Load Type ID fields)
        	aLength += 1 + ((1 + aLoad_type.aDescription.length()) / 2);
        }
        else {
        	// Initialize the Pointer to Load Type Description field (ARINC665-3 §2.2.3.1.9)
        	aPointer_to_LTD = 0;
        }
        // Set the Pointer to Load Part Number field
        aPointer_to_THWID_list = aLength;
        // Set the Number of Target HW IDs field
        aNumber_of_THWID = (char) a_THWID_list.length;
        // Increase the length (size of Number of Target HW IDs field)
        aLength++;
        // Set the target HW IDs list : Target HW ID Length and Target HW ID fields
        aTHWID_list = a_THWID_list;
        // Increase the length (size of Target HW ID Length and Target HW ID fields)
        for(i=1;i <= a_THWID_list.length;i++) {
            aLength += 1 + ((1 + a_THWID_list[i - 1].length()) / 2);
        }
        // Set the Pointer to Data File list field
        aPointer_to_DF_list = aLength;
        // Set the Number of Data Files field
        aNumber_of_DF = (char) a_data_file.aName.length;
        // Increase the length (size of Number of Data Files field)
        aLength++;
        // Set the Data File list
        aData_file = a_data_file;
        // Increase the length (size of Data File Pointer,
        //                              Data File Name Length,
        //                              Data File Name,
        //                              Data File PN Length,
        //                              Data File PN,
        //                              Data File Length,
        //                              Data File CRC,
        //                              Data File Length in Bytes if requested,
        //                              Data File Check Value Length if requested,
        //                              Data File Check Value type if requested,
        //                              Data File Check Value if requested fields)
        for(i=1;i <= aData_file.aName.length;i++) {
            aLength += 1 +
                       1 +
                       ((1 + aData_file.aName[i - 1].length()) / 2) +
                       1 +
                       ((1 + aData_file.aPN.length()) / 2) +
                       2 +
                       1;
            if (a_norm_version == ARINC_norm_version.ARINC665_3) {
            	switch(aData_file.aCheck_value_type) {
            		case 4 :
            		case 5 :
            			aLength += 4 + (aData_file.aCheck_value_length / 2);
            			break;
            		default : 
            			aLength += 4 + 1;
            	}
            }
            else {
            }
        }
        // Add the support files if requested
        if (a_support_file == null) {
            // Set the Number of Supplier Files field
            aNumber_of_SF = 0;
            // Increase the length (size of Number of Supplier Files field)
            aLength++;
        }
        else {
            // Set the Pointer to Support File list field
            aPointer_to_SF_list = aLength;
            // Set the Number of Supplier Files field
            aNumber_of_SF = (char) a_support_file.aName.length;
            // Increase the length (size of Number of Supplier Files field)
            aLength++;
            // Set the Data File list
            aSupport_file = a_support_file;
            // Increase the length (size of Support File Pointer,
            //                              Support File Name Length,
            //                              Support File Name,
            //                              Support File PN Length,
            //                              Support File PN (same as Load PN),
            //                              Support File Length and
            //                              Support File CRC, 
            //                              Support File Check Value Length if requested,
            //                              Support File Check Value type if requested,
            //                              Support File Check Value if requested fields)
            for(i=1;i <= aSupport_file.aName.length;i++) {
                aLength += 1 +
                           1 +
                           ((1 + aSupport_file.aName[i - 1].length()) / 2) +
                           1 +
                           ((1 + aLoad_PN.length()) / 2) +
                           2 +
                           1;
                if (a_norm_version == ARINC_norm_version.ARINC665_3) {
                	switch(aSupport_file.aCheck_value_type) {
                		case 4 :
                		case 5 :
                			aLength += (aSupport_file.aCheck_value_length / 2);
                			break;
                		default : ; 
                	}
                }
                else {
                }
            }
        }
        // Add the user defined data if requested
        if (a_F_with_UDD) {
            // Set the Pointer to User Defined Data field
            aPointer_to_UDD = aLength;
            // Set the User Defined Data field
            aUser_defined_data = new char[a_UDD.length / 2];
            for(i=0;i < a_UDD.length;i++) {
                if ((i % 2) == 0) {
                    aUser_defined_data[i / 2] = (char) ((char) a_UDD[i] << 8);
                }
                else {
                    aUser_defined_data[i / 2] |= (char) a_UDD[i] & 0xFF;
                }
            }
            // Increase the length (size of User Defined Data field)
            aLength += a_UDD.length / 2;
        }
        else {
        	aUser_defined_data = null;
        }
        // Set the Pointer to Load Check Value field
        aPointer_to_LCV = aLength;
        // Add the Load Check Value fields if requested
        if (a_norm_version == ARINC_norm_version.ARINC665_3) {
            // Increase the length (size of Load Check Value Length field)
            aLength++;
        	switch(a_load_integrity_check) {
                // TODO Implémenter le load check value
        		/*case 4 :
                	aCheck_value_length = 20;
                	aCheck_value_type = 4;
                	aCheck_value = new byte[16];
                    // Increase the length (size of Load Check Value Type and Load Check Value fields)
                    aLength += 1 + 8;
                    break;
        		case 5 :
                    aCheck_value_length = 24;
                	aCheck_value_type = 5;
                	aCheck_value = new byte[20];
                    // Increase the length (size of Load Check Value Type and Load Check Value fields)
                    aLength += 1 + 10;
                    break;*/
                default :
                	aCheck_value_length = 0;
                	aCheck_value_type = 0;
                	aCheck_value = null;
        	}
        }
        else {
        }
        // Increase the length (size of Header File CRC and Load CRC fields)
        aLength += 1 + 2;
        // Initialize the hexa dump of Header file
        aHexa_dump = new String();
    }
    /**************************************************************************
     ** Public method : BuildHeaderFile                                      **
     **************************************************************************/
    public int BuildHeaderFile(ARINC_norm_version a_norm_version) throws IOException {
        int i;
        int j;
        int size;
        FileOutputStream lOut_file = new FileOutputStream(aSub_directory + "/" + aName);

        // Write Header File Length field
        size = file.WriteInt(lOut_file, aLength);
        // Write Load File Format Version field
        size += file.WriteChar(lOut_file, aVersion);
        // Write Spare field or Part Flags field
        if ((a_norm_version == ARINC_norm_version.ARINC665_1) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_2)) {
            size += file.WriteChar(lOut_file, kSpare);
        }
        else {
        	size += file.WriteChar(lOut_file, aPartFlags);
        }
        // Write Pointers
        size += file.WriteInt(lOut_file, aPointer_to_LPN);
        size += file.WriteInt(lOut_file, aPointer_to_THWID_list);
        size += file.WriteInt(lOut_file, aPointer_to_DF_list);
        size += file.WriteInt(lOut_file, aPointer_to_SF_list);
        size += file.WriteInt(lOut_file, aPointer_to_UDD);
        if (a_norm_version == ARINC_norm_version.ARINC665_3) {
        	size += file.WriteInt(lOut_file, aPointer_to_LTD);
        	size += file.WriteInt(lOut_file, kPointer_to_THWID_with_position_list);
        	size += file.WriteInt(lOut_file, aPointer_to_LCV);
        }
        else {
        }
        // Write Load PN fields
        size += file.WriteChar(lOut_file, aLPN_length);
        size += file.WriteString(lOut_file, aLoad_PN);
        // Write Load Type Description if requested
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) &&
            (aPointer_to_LTD != 0)) {
        	size += file.WriteChar(lOut_file, (char) aLoad_type.aDescription.length());
        	size += file.WriteString(lOut_file, aLoad_type.aDescription);
        	size += file.WriteChar(lOut_file, (char) aLoad_type.aID);
        }
        else {
        }
        // Write all the fields for target HW ID
        size += file.WriteChar(lOut_file, aNumber_of_THWID);
        for(i=1;i <= aTHWID_list.length;i++) {
            size += file.WriteChar(lOut_file, (char) aTHWID_list[i - 1].length());
            size += file.WriteString(lOut_file, aTHWID_list[i - 1]);
        }
        // Write all the fields for target HW ID with position
    	// TODO Implémenter la partie Number of Target HW ID with Positions
        // Write all the fields for data files
        size += file.WriteChar(lOut_file, aNumber_of_DF);
        for(i=1;i <= aData_file.aName.length;i++) {
            size += file.WriteChar(lOut_file, aData_file.aPointer[i - 1]);
            size += file.WriteChar(lOut_file, aData_file.aName_length);
            size += file.WriteString(lOut_file, aData_file.aName[i - 1]);
            size += file.WriteChar(lOut_file, (char) aData_file.aPN.length());
            size += file.WriteString(lOut_file, aData_file.aPN);
            size += file.WriteInt(lOut_file, aData_file.aLength_in_bytes[i - 1] / 2);
            size += file.WriteChar(lOut_file, (char) aData_file.aCRC16[i - 1]);
            if (a_norm_version == ARINC_norm_version.ARINC665_3) {
            	size += file.WriteInt(lOut_file, 0);
            	size += file.WriteInt(lOut_file, aData_file.aLength_in_bytes[i - 1]);
            	size += file.WriteChar(lOut_file, aData_file.aCheck_value_length);
            	if (aData_file.aCheck_value_type != 0) {
            		size += file.WriteChar(lOut_file, aData_file.aCheck_value_type);
                	for(j=1;j <= aData_file.aCheck_value[i - 1].length;j++) {
                    	size += file.WriteByte(lOut_file, aData_file.aCheck_value[i - 1][j - 1]);
                	}
            	}
            	else {
            	}
            }
            else {
            }
        }
        // Write the Number of Support Files field
        size += file.WriteChar(lOut_file, aNumber_of_SF);
        // Write all the fields for support files if requested
        if (aSupport_file != null) {
            for(i=1;i <= aSupport_file.aName.length;i++) {
                size += file.WriteChar(lOut_file, aSupport_file.aPointer[i - 1]);
                size += file.WriteChar(lOut_file, aSupport_file.aName_length[i - 1]);
                size += file.WriteString(lOut_file, aSupport_file.aName[i - 1]);
                size += file.WriteChar(lOut_file, (char) aSupport_file.aPN.length());
                size += file.WriteString(lOut_file, aSupport_file.aPN);
                size += file.WriteInt(lOut_file, aSupport_file.aLength[i - 1]);
                size += file.WriteChar(lOut_file, (char) aSupport_file.aCRC16[i - 1]);
                if (a_norm_version == ARINC_norm_version.ARINC665_3) {
                	size += file.WriteChar(lOut_file, aSupport_file.aCheck_value_length);
                	if (aSupport_file.aCheck_value_type != 0) {
                		size += file.WriteChar(lOut_file, aSupport_file.aCheck_value_type);
                    	for(j=1;j <= aSupport_file.aCheck_value[i - 1].length;j++) {
                        	size += file.WriteByte(lOut_file, aSupport_file.aCheck_value[i - 1][j - 1]);
                    	}
                	}
                	else {
                	}
                }
                else {
                }
            }
        }
        else {
        }
        // Write the user defined data if requested
        if (aUser_defined_data != null) {
            for(i=1;i <= aUser_defined_data.length;i++) {
                size += file.WriteChar(lOut_file, aUser_defined_data[i - 1]);
            }
        }
        else {
        }
        // Write Load Check Value fields if requested
        if (a_norm_version == ARINC_norm_version.ARINC665_3) {
            size += file.WriteChar(lOut_file, (char) 0);
            // TODO Implémenter le load check value
            /*size += file.WriteChar(lOut_file, aCheck_value_length);
            switch(aCheck_value_type) {
            	case 4 :
                	size += file.WriteChar(lOut_file, aCheck_value_type);
                	aCheck_value = ;
                	for(i=1;i <= aCheck_value.length;i++) {
                    	size += file.WriteByte(lOut_file, aCheck_value[i - 1]);
                    }
                    break;
                default : ;
            }*/
        }
        else {
        }
        lOut_file.close();
        // Add the CRC16 at the end of the header file (the CRC32 will be added later, when all files will be build)
        aCRC16 = integrity_check.AddCRC16AtEndOfFile(aSub_directory + "/" + aName);
        size += 2;
        System.out.printf("*** Information *** CRC16 of file %s : 0x%04X\n", aName, aCRC16);
        // Build the hexa dump of Header file
        aHexa_dump = file.BuildHexaDump(aSub_directory + "/" + aName, size);
        aHexa_dump += "\nLoad CRC32 not displayed";
        return(size);
    }
}
