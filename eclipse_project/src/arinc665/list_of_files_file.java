package arinc665;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;

import lspb.file;

/*
 * Class : list_of_files_file
 */
public class list_of_files_file {
	// (ARINC665-2 �3.2.3.2) (ARINC665-3 �3.3.2.3.2) (ARINC665-4 �3.3.2.3.2)
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    private static final String kFile_extension = "LUM";
    private String pName;
    public int aLength;									//                            (ARINC665-3 �3.3.2.3.2.1)	(ARINC665-4 �3.3.2.3.2.1)
    public char aVersion;								// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.2)	(ARINC665-4 �3.3.2.3.2.2)
    public final char kSpare = 0;						// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.3)	(ARINC665-4 �3.3.2.3.2.3)
    public int aPointer_to_Media_Information;			// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.4)	(ARINC665-4 �3.3.2.3.2.4)
    public int aPointer_to_File_List;					// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.5)	(ARINC665-4 �3.3.2.3.2.5)
    public int aPointer_to_UDD;							// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.6)	(ARINC665-4 �3.3.2.3.2.6)
    public int aPointer_to_FCV;							//                            (ARINC665-3 �3.3.2.3.2.7)	(ARINC665-4 �3.3.2.3.2.7)
    public char aMSPN_length;							// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.9)	(ARINC665-4 �3.3.2.3.2.9)
    public String aMSPN;								// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.10)	(ARINC665-4 �3.3.2.3.2.10)
    public final byte aMedia_sequence_number = 1;		// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.11)	(ARINC665-4 �3.3.2.3.2.11)
    public final byte aNumber_of_media_set_member = 1;	// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.12)	(ARINC665-4 �3.3.2.3.2.12)
    public char aNumber_of_media_set_files;				// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.13)	(ARINC665-4 �3.3.2.3.2.13)
    public char[] aFile_pointer;						// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.14)	(ARINC665-4 �3.3.2.3.2.14)
    public char[] aFN_length;							// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.15)	(ARINC665-4 �3.3.2.3.2.15)
    public String[] aFile_name;							// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.16)	(ARINC665-4 �3.3.2.3.2.16)
    public char[] aFP_length;							// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.17)	(ARINC665-4 �3.3.2.3.2.17)
    public String[] aFile_pathname;						// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.18)	(ARINC665-4 �3.3.2.3.2.18)
    public final char aFile_member_sequence_number = 1;	// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.19)	(ARINC665-4 �3.3.2.3.2.19)
    public int[] aFile_CRC;								// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.20)	(ARINC665-4 �3.3.2.3.2.20)
    public char aCheck_value_length;					//                            (ARINC665-3 �3.3.2.3.2.27)	(ARINC665-4 �3.3.2.3.2.27)
    public check_value_type aCheck_value_type;			//                            (ARINC665-3 �3.3.2.3.2.28)	(ARINC665-4 �3.3.2.3.2.28)
    public byte[] aCheck_value;							//                            (ARINC665-3 �3.3.2.3.2.29)	(ARINC665-4 �3.3.2.3.2.29)
    public String aCheck_value_string;					//                            (ARINC665-3 �3.3.2.3.2.29)	(ARINC665-4 �3.3.2.3.2.29)
    public int aCRC16;									// (ARINC665-2 �3.2.3.2.XX)    (ARINC665-3 �3.3.2.3.2.30)	(ARINC665-4 �3.3.2.3.2.30)
    public String aHexa_dump;
    /**************************************************************************
     ** Constructor : list_of_files_file                                     **
     **************************************************************************/
    public list_of_files_file(ARINC_norm_version a_norm_version,
    		                  String a_sub_directory,
    		                  String a_load_PN,
                              header_file a_header_file,
                              list_of_loads_file a_list_of_loads_file,
                              data_file a_data_file,
                              support_file a_support_file,
                              batch_file a_batch_file,
                              char a_load_integrity_check) {
        int i;
        int file_index;

        // Build the name of list of loads file using the basename and the extension
        pName = "FILES." + kFile_extension;
        // Set the length to 2 (size of List of Files File Length field)
        aLength = 2;
        // Set the Media File Format Version field
        aVersion = a_norm_version.getMediaFileFormatVersion();
        // Increase the length (size of Load File Format Version field)
        aLength++;
        // Set the Spare field
        //spare = 0;
        // Increase the length (size of Spare field)
        aLength++;
        // Initialize the Pointer to Media Information field
        aPointer_to_Media_Information = 0;
        // Increase the length (size of Pointer to Media Information field)
        aLength += 2;
        // Initialize the Pointer to File List field
        aPointer_to_File_List = 0;
        // Increase the length (size of Pointer to File List field)
        aLength += 2;
        // Initialize the Pointer to User Defined Data field
        aPointer_to_UDD = 0;
        // Increase the length (size of Pointer to User Defined Data field)
        aLength += 2;
        // Initialize the Pointer to FILES.LUM File Check Value field
        aPointer_to_FCV = 0;
        // Increase the length (size of Pointer to File Check Value field)
        aLength += 2;
        // Set the Pointer to Media Information field
        aPointer_to_Media_Information = aLength;
        // Set the Media Set PN Length field
        aMSPN_length = (char) a_load_PN.length();
        // Increase the length (size of Media Set PN Length field)
        aLength++;
        // Set the Media Set PN
        aMSPN = new String(a_load_PN);
        // Increase the length (size of Media Set PN field)
        aLength += (1 + a_load_PN.length()) / 2;
        // Set the Media Sequence Number field
        //aMedia_sequence_number = 1;
        // Set the Number Of Media Set Members field
        //aNumber_of_media_set_member = 1;
        // Increase the length (size of Media Sequence Number and Number Of Media Set Members fields)
        aLength++;
        // Set the Pointer to File List field
        aPointer_to_File_List = aLength;
        // Set the Number Of Media Set Files field (including support files if any)
        aNumber_of_media_set_files = (char) (1 + 1 + 1 + a_data_file.aName.length);
        if (a_support_file != null) {
        	aNumber_of_media_set_files += (char)a_support_file.aName.length;
        }
        else {
        }
        // Increase the length (size of Number Of Media Set Files fields)
        aLength++;
        aFile_pointer = new char[aNumber_of_media_set_files];
        aFN_length = new char[aNumber_of_media_set_files];
        aFile_name = new String[aNumber_of_media_set_files];
        aFP_length = new char[aNumber_of_media_set_files];
        aFile_pathname = new String[aNumber_of_media_set_files];
        aFile_CRC = new int[aNumber_of_media_set_files];

        // Header file
        // First file (index = 0)
        file_index = 0;
        // Set the File Name Length field
        aFN_length[0] = (char) a_header_file.aName.length();
        // Increase the length (size of File Name Length field)
        aLength++;
        // Set the File Name field
        aFile_name[0] = new String(a_header_file.aName);
        // Increase the length (size of File Name field)
        aLength += (1 + a_header_file.aName.length()) / 2;
        // Set the File Pathname Length field
        aFP_length[0] = (char) (a_sub_directory.length() + 2);
        // Increase the length (size of File Pathname Length field)
        aLength++;
        // Set the File Pathname field
        aFile_pathname[0] = new String("\\" + a_sub_directory + "\\");
        // Increase the length (size of File Pathname field)
        aLength += (1 + a_sub_directory.length() + 2) / 2;
        // Set the Member Sequence Number field
        //aMember_sequence_number = 1;
        // Increase the length (size of Member Sequence Number field)
        aLength++;
        // Set the File CRC field
        aFile_CRC[0] = a_header_file.aCRC16;
        // Increase the length (size of File CRC field)
        aLength++;
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	// Increase the length (size of File Check Value Length field)
        	aLength++;
        }
        else {
        }
        // Set the File Pointer field
        aFile_pointer[0] = (char) (1 +
                                   1 +
                                   ((1 + a_header_file.aName.length()) / 2) +
                                   1 +
                                   ((1 + a_sub_directory.length() + 2) / 2) +
                                   1 +
                                   1);
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	aFile_pointer[0]++;
        }
        else {
        }
        // Increase the length (size of File Pointer field)
        aLength++;

        // LOADS.LUM file
        // Second file (index = 1)
        file_index = 1;
        // Set the File Name Length field
        aFN_length[1] = (char) a_list_of_loads_file.aName.length();
        // Increase the length (size of File Name Length field)
        aLength++;
        // Set the File Name field
        aFile_name[1] = new String(a_list_of_loads_file.aName);
        // Increase the length (size of File Name field)
        aLength += (1 + a_list_of_loads_file.aName.length()) / 2;
        // Set the File Pathname Length field
        aFP_length[1] = 2;
        // Increase the length (size of File Pathname Length field)
        aLength++;
        // Set the File Pathname field
        aFile_pathname[1] = new String("\\.");
        // Increase the length (size of File Pathname field)
        aLength += (1 + 2) / 2;
        // Set the Member Sequence Number field
        //aMember_sequence_number = 1;
        // Increase the length (size of Member Sequence Number field)
        aLength++;
        // Set the File CRC field
        aFile_CRC[1] = a_list_of_loads_file.aCRC16;
        // Increase the length (size of File CRC field)
        aLength++;
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	// Increase the length (size of File Check Value Length field)
        	aLength++;
        }
        else {
        }
        // Set the File Pointer field
        aFile_pointer[1] = (char) (1 +
                                   1 +
                                   ((1 + a_list_of_loads_file.aName.length()) / 2) +
                                   1 +
                                   ((1 + 1) / 2) +
                                   1 +
                                   1);
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	aFile_pointer[1]++;
        }
        else {
        }
        // Increase the length (size of File Pointer field)
        aLength++;

        // Batch file (see ARINC665-2 �2.3.1)
        // Third file (index = 2)
        file_index = 2;
        // Set the File Name Length field
        aFN_length[2] = (char) a_batch_file.aName.length();
        // Increase the length (size of File Name Length field)
        aLength++;
        // Set the File Name field
        aFile_name[2] = new String(a_batch_file.aName);
        // Increase the length (size of File Name field)
        aLength += (1 + a_batch_file.aName.length()) / 2;
        // Set the File Pathname Length field
        aFP_length[2] = 2;
        // Increase the length (size of File Pathname Length field)
        aLength++;
        // Set the File Pathname field
        aFile_pathname[2] = new String("\\.");
        // Increase the length (size of File Pathname field)
        aLength += (1 + 2) / 2;
        // Set the Member Sequence Number field
        //aMember_sequence_number = 1;
        // Increase the length (size of Member Sequence Number field)
        aLength++;
        // Set the File CRC field
        aFile_CRC[2] = a_batch_file.aCRC16;
        // Increase the length (size of File CRC field)
        aLength++;
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	// Increase the length (size of File Check Value Length field)
        	aLength++;
        }
        else {
        }
        // Set the File Pointer field
        aFile_pointer[2] = (char) (1 +
                                   1 +
                                   ((1 + a_batch_file.aName.length()) / 2) +
                                   1 +
                                   ((1 + 1) / 2) +
                                   1 +
                                   1);
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	aFile_pointer[2]++;
        }
        else {
        }
        // Increase the length (size of File Pointer field)
        aLength++;

        // Data files
        for(i=0;i < a_data_file.aName.length;i++) {
            // Next file (index = 3, ...)
        	file_index++;
            // Set the File Name Length field
            aFN_length[i + 3] = (char) a_data_file.aName[i].length();
            // Increase the length (size of File Name Length field)
            aLength++;
            // Set the File Name field
            aFile_name[i + 3] = new String(a_data_file.aName[i]);
            // Increase the length (size of File Name field)
            aLength += (1 + a_data_file.aName[i].length()) / 2;
            // Set the File Pathname Length field
            aFP_length[i + 3] = (char) (a_sub_directory.length() + 2);
            // Increase the length (size of File Pathname Length field)
            aLength++;
            // Set the File Pathname field
            aFile_pathname[i + 3] = new String("\\" + a_sub_directory + "\\");
            // Increase the length (size of File Pathname field)
            aLength += (1 + a_sub_directory.length() + 2) / 2;
            // Set the Member Sequence Number field
            //aMember_sequence_number = 1;
            // Increase the length (size of Member Sequence Number field)
            aLength++;
            // Set the File CRC field
            aFile_CRC[i + 3] = a_data_file.aCRC16[i];
            // Increase the length (size of File CRC field)
            aLength++;
            if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
            	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
            	// Increase the length (size of File Check Value Length field)
            	aLength++;
            }
            else {
            }
            if (i == (a_data_file.aName.length - 1)) {
                // Set the File Pointer field
                aFile_pointer[i + 3] = 0;
            }
            else {
                // Set the File Pointer field
                aFile_pointer[i + 3] = (char) (1 +
                                               1 +
                                               ((1 + a_data_file.aName[i].length()) / 2) +
                                               1 +
                                               ((1 + a_sub_directory.length() + 2) / 2) +
                                               1 +
                                               1);
                if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
                	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
                	aFile_pointer[i + 3]++;
                }
                else {
                }
            }
            // Increase the length (size of File Pointer field)
            aLength++;
        }

        // Support files
        if (a_support_file != null) {
            for(i=0;i < a_support_file.aName.length;i++) {
                // Next file
            	file_index++;
                // Set the File Name Length field
                aFN_length[i + file_index] = (char) a_support_file.aName[i].length();
                // Increase the length (size of File Name Length field)
                aLength++;
                // Set the File Name field
                aFile_name[i + file_index] = new String(a_support_file.aName[i]);
                // Increase the length (size of File Name field)
                aLength += (1 + a_support_file.aName[i].length()) / 2;
                // Set the File Pathname Length field
                aFP_length[i + file_index] = (char) (a_sub_directory.length() + 2);
                // Increase the length (size of File Pathname Length field)
                aLength++;
                // Set the File Pathname field
                aFile_pathname[i + file_index] = new String("\\" + a_sub_directory + "\\");
                // Increase the length (size of File Pathname field)
                aLength += (1 + a_sub_directory.length() + 2) / 2;
                // Set the Member Sequence Number field
                //aMember_sequence_number = 1;
                // Increase the length (size of Member Sequence Number field)
                aLength++;
                // Set the File CRC field
                aFile_CRC[i + file_index] = a_support_file.aCRC16[i];
                // Increase the length (size of File CRC field)
                aLength++;
                if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
                	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
                	// Increase the length (size of File Check Value Length field)
                	aLength++;
                }
                else {
                }
                if (i == (a_support_file.aName.length - 1)) {
                    // Set the File Pointer field
                    aFile_pointer[i + file_index] = 0;
                }
                else {
                    // Set the File Pointer field
                    aFile_pointer[i + file_index] = (char) (1 +
                                                   1 +
                                                   ((1 + a_support_file.aName[i].length()) / 2) +
                                                   1 +
                                                   ((1 + a_sub_directory.length() + 2) / 2) +
                                                   1 +
                                                   1);
                    if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
                    	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
                    	aFile_pointer[i + file_index]++;
                    }
                    else {
                    }
                }
                // Increase the length (size of File Pointer field)
                aLength++;
            }
        }
        else {
        }
        
        aCheck_value_type = check_value_type.NOT_USED;
        aCheck_value_length = 0;
        aCheck_value = null;
        aCheck_value_string = null;
        // Add the FILES.LUM File Check Value fields if requested
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
            // Set the Pointer to FILES.LUM File Check Value field
            aPointer_to_FCV = aLength;
            // Increase the length (size of FILES.LUM File Check Value Length field)
            aLength++;
        	switch(a_load_integrity_check) {
	        	case 3 :
	            	aCheck_value_length = 4;
	            	aCheck_value_type = check_value_type.CRC32;
	            	aCheck_value = new byte[4];
	            	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 3;
	        	break;
        		case 4 :
                	aCheck_value_length = 16;
                	aCheck_value_type = check_value_type.MD5;
                	aCheck_value = new byte[16];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 9;
                    break;
        		case 5 :
                    aCheck_value_length = 20;
                	aCheck_value_type = check_value_type.SHA_1;
                	aCheck_value = new byte[20];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 11;
                    break;
            	case 6 :
                	aCheck_value_length = 32;
                	aCheck_value_type = check_value_type.SHA_256;
                	aCheck_value = new byte[32];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 17;
            	break;
            	case 7 :
                	aCheck_value_length = 64;
                	aCheck_value_type = check_value_type.SHA_512;
                	aCheck_value = new byte[64];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 33;
            	break;
            	case 8 :
                	aCheck_value_length = 8;
                	aCheck_value_type = check_value_type.CRC64;
                	aCheck_value = new byte[8];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 5;
            	break;
                default : ;
        	}
        }
        else {
        }
        // Increase the length (size of FILES.LUM file CRC field)
        aLength++;
        // Initialize the hexa dump of Header file
        aHexa_dump = new String();
    }
    /**************************************************************************
     ** Constructor : list_of_files_file                                     **
     **************************************************************************/
    public list_of_files_file(ARINC_norm_version a_norm_version,
    						  String a_sub_directory,
                              String a_load_PN,
                              header_file a_header_file,
                              list_of_loads_file a_list_of_loads_file,
                              data_file a_data_file,
                              support_file a_support_file,
                              char a_load_integrity_check) {
        int i;
        int file_index;

        // Build the name of list of loads file using the basename and the extension
        pName = "FILES." + kFile_extension;
        // Set the length to 2 (size of Header File Length field)
        aLength = 2;
        // Set the Media File Format Version field
        aVersion = a_norm_version.getMediaFileFormatVersion();
        // Increase the length (size of Load File Format Version field)
        aLength++;
        // Set the Spare field
        //spare = 0;
        // Increase the length (size of Spare field)
        aLength++;
        // Initialize the Pointer to Media Information field
        aPointer_to_Media_Information = 0;
        // Increase the length (size of Pointer to Media Information field)
        aLength += 2;
        // Initialize the Pointer to File List field
        aPointer_to_File_List = 0;
        // Increase the length (size of Pointer to File List field)
        aLength += 2;
        // Initialize the Pointer to User Defined Data field
        aPointer_to_UDD = 0;
        // Increase the length (size of Pointer to User Defined Data field)
        aLength += 2;
        // Initialize the Pointer to FILES.LUM File Check Value field
        aPointer_to_FCV = 0;
        // Increase the length (size of Pointer to File Check Value field)
        aLength += 2;
        // Set the Pointer to Media Information field
        aPointer_to_Media_Information = aLength;
        // Set the Media Set PN Length field
        aMSPN_length = (char) a_load_PN.length();
        // Increase the length (size of Media Set PN Length field)
        aLength++;
        // Set the Media Set PN
        aMSPN = new String(a_load_PN);
        // Increase the length (size of Media Set PN field)
        aLength += (1 + a_load_PN.length()) / 2;
        // Set the Media Sequence Number field
        //aMedia_sequence_number = 1;
        // Set the Number Of Media Set Members field
        //aNumber_of_media_set_member = 1;
        // Increase the length (size of Media Sequence Number and Number Of Media Set Members fields)
        aLength++;
        // Set the Pointer to File List field
        aPointer_to_File_List = aLength;
        // Set the Number Of Media Set Files field (including support files if any)
        aNumber_of_media_set_files = (char) (1 + 1 + a_data_file.aName.length);
        if (a_support_file != null) {
        	aNumber_of_media_set_files += (char)a_support_file.aName.length;
        }
        else {
        }
        // Increase the length (size of Number Of Media Set Files fields)
        aLength++;
        aFile_pointer = new char[aNumber_of_media_set_files];
        aFN_length = new char[aNumber_of_media_set_files];
        aFile_name = new String[aNumber_of_media_set_files];
        aFP_length = new char[aNumber_of_media_set_files];
        aFile_pathname = new String[aNumber_of_media_set_files];
        aFile_CRC = new int[aNumber_of_media_set_files];

        // Header file
        // First file (index = 0)
        file_index = 0;
        // Set the File Name Length field
        aFN_length[0] = (char) a_header_file.aName.length();
        // Increase the length (size of File Name Length field)
        aLength++;
        // Set the File Name field
        aFile_name[0] = new String(a_header_file.aName);
        // Increase the length (size of File Name field)
        aLength += (1 + a_header_file.aName.length()) / 2;
        // Set the File Pathname Length field
        aFP_length[0] = (char) (a_sub_directory.length() + 2);
        // Increase the length (size of File Pathname Length field)
        aLength++;
        // Set the File Pathname field
        aFile_pathname[0] = new String("\\" + a_sub_directory + "\\");
        // Increase the length (size of File Pathname field)
        aLength += (1 + a_sub_directory.length() + 2) / 2;
        // Set the Member Sequence Number field
        //aMember_sequence_number = 1;
        // Increase the length (size of Member Sequence Number field)
        aLength++;
        // Set the File CRC field
        aFile_CRC[0] = a_header_file.aCRC16;
        // Increase the length (size of File CRC field)
        aLength++;
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	// Increase the length (size of File Check Value Length field)
        	aLength++;
        }
        else {
        }
        // Set the File Pointer field
        aFile_pointer[0] = (char) (1 +
                                   1 +
                                   ((1 + a_header_file.aName.length()) / 2) +
                                   1 +
                                   ((1 + a_sub_directory.length() + 2) / 2) +
                                   1 +
                                   1);
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	aFile_pointer[0]++;
        }
        else {
        }
        // Increase the length (size of File Pointer field)
        aLength++;

        // LOADS.LUM file
        // Second file (index = 1)
        file_index = 1;
        // Set the File Name Length field
        aFN_length[1] = (char) a_list_of_loads_file.aName.length();
        // Increase the length (size of File Name Length field)
        aLength++;
        // Set the File Name field
        aFile_name[1] = new String(a_list_of_loads_file.aName);
        // Increase the length (size of File Name field)
        aLength += (1 + a_list_of_loads_file.aName.length()) / 2;
        // Set the File Pathname Length field
        aFP_length[1] = 2;
        // Increase the length (size of File Pathname Length field)
        aLength++;
        // Set the File Pathname field
        aFile_pathname[1] = new String("\\.");
        // Increase the length (size of File Pathname field)
        aLength += (1 + 2) / 2;
        // Set the Member Sequence Number field
        //aMember_sequence_number = 1;
        // Increase the length (size of Member Sequence Number field)
        aLength++;
        // Set the File CRC field
        aFile_CRC[1] = a_list_of_loads_file.aCRC16;
        // Increase the length (size of File CRC field)
        aLength++;
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	// Increase the length (size of File Check Value Length field)
        	aLength++;
        }
        else {
        }
        // Set the File Pointer field
        aFile_pointer[1] = (char) (1 +
                                   1 +
                                   ((1 + a_list_of_loads_file.aName.length()) / 2) +
                                   1 +
                                   ((1 + 1) / 2) +
                                   1 +
                                   1);
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	aFile_pointer[1]++;
        }
        else {
        }
        // Increase the length (size of File Pointer field)
        aLength++;

        // Data files
        for(i=0;i < a_data_file.aName.length;i++) {
            // Next file (index = 2, ...)
        	file_index++;
            // Set the File Name Length field
            aFN_length[i + 2] = (char) a_data_file.aName[i].length();
            // Increase the length (size of File Name Length field)
            aLength++;
            // Set the File Name field
            aFile_name[i + 2] = new String(a_data_file.aName[i]);
            // Increase the length (size of File Name field)
            aLength += (1 + a_data_file.aName[i].length()) / 2;
            // Set the File Pathname Length field
            aFP_length[i + 2] = (char) (a_sub_directory.length() + 2);
            // Increase the length (size of File Pathname Length field)
            aLength++;
            // Set the File Pathname field
            aFile_pathname[i + 2] = new String("\\" + a_sub_directory + "\\");
            // Increase the length (size of File Pathname field)
            aLength += (1 + a_sub_directory.length() + 2) / 2;
            // Set the Member Sequence Number field
            //aMember_sequence_number = 1;
            // Increase the length (size of Member Sequence Number field)
            aLength++;
            // Set the File CRC field
            aFile_CRC[i + 2] = a_data_file.aCRC16[i];
            // Increase the length (size of File CRC field)
            aLength++;
            if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
            	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
            	// Increase the length (size of File Check Value Length field)
            	aLength++;
            }
            else {
            }
            if (i == (a_data_file.aName.length - 1)) {
                // Set the File Pointer field
                aFile_pointer[i + 2] = 0;
            }
            else {
                // Set the File Pointer field
                aFile_pointer[i + 2] = (char) (1 +
                                               1 +
                                               ((1 + a_data_file.aName[i].length()) / 2) +
                                               1 +
                                               ((1 + a_sub_directory.length() + 2) / 2) +
                                               1 +
                                               1);
                if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
                	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
                	aFile_pointer[i + 2]++;
                }
                else {
                }
            }
            // Increase the length (size of File Pointer field)
            aLength++;
        }

        // Support files
        if (a_support_file != null) {
            for(i=0;i < a_support_file.aName.length;i++) {
                // Next file
            	file_index++;
                // Set the File Name Length field
                aFN_length[i + file_index] = (char) a_support_file.aName[i].length();
                // Increase the length (size of File Name Length field)
                aLength++;
                // Set the File Name field
                aFile_name[i + file_index] = new String(a_support_file.aName[i]);
                // Increase the length (size of File Name field)
                aLength += (1 + a_support_file.aName[i].length()) / 2;
                // Set the File Pathname Length field
                aFP_length[i + file_index] = (char) (a_sub_directory.length() + 2);
                // Increase the length (size of File Pathname Length field)
                aLength++;
                // Set the File Pathname field
                aFile_pathname[i + file_index] = new String("\\" + a_sub_directory + "\\");
                // Increase the length (size of File Pathname field)
                aLength += (1 + a_sub_directory.length() + 2) / 2;
                // Set the Member Sequence Number field
                //aMember_sequence_number = 1;
                // Increase the length (size of Member Sequence Number field)
                aLength++;
                // Set the File CRC field
                aFile_CRC[i + file_index] = a_support_file.aCRC16[i];
                // Increase the length (size of File CRC field)
                aLength++;
                if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
                	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
                	// Increase the length (size of File Check Value Length field)
                	aLength++;
                }
                else {
                }
                if (i == (a_support_file.aName.length - 1)) {
                    // Set the File Pointer field
                    aFile_pointer[i + file_index] = 0;
                }
                else {
                    // Set the File Pointer field
                    aFile_pointer[i + file_index] = (char) (1 +
                                                   1 +
                                                   ((1 + a_support_file.aName[i].length()) / 2) +
                                                   1 +
                                                   ((1 + a_sub_directory.length() + 2) / 2) +
                                                   1 +
                                                   1);
                    if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
                    	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
                    	aFile_pointer[i + file_index]++;
                    }
                    else {
                    }
                }
                // Increase the length (size of File Pointer field)
                aLength++;
            }
        }
        else {
        }
        
        aCheck_value_type = check_value_type.NOT_USED;
        aCheck_value_length = 0;
        aCheck_value = null;
        aCheck_value_string = null;
        // Add the FILES.LUM File Check Value fields if requested
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
            // Set the Pointer to FILES.LUM File Check Value field
            aPointer_to_FCV = aLength;
            // Increase the length (size of FILES.LUM File Check Value Length field)
            aLength++;
        	switch(a_load_integrity_check) {
	        	case 3 :
	            	aCheck_value_length = 4;
	            	aCheck_value_type = check_value_type.CRC32;
	            	aCheck_value = new byte[4];
	            	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 3;
	        	break;
        		case 4 :
                	aCheck_value_length = 16;
                	aCheck_value_type = check_value_type.MD5;
                	aCheck_value = new byte[16];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 9;
                    break;
        		case 5 :
                    aCheck_value_length = 20;
                	aCheck_value_type = check_value_type.SHA_1;
                	aCheck_value = new byte[20];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 11;
                    break;
            	case 6 :
                	aCheck_value_length = 32;
                	aCheck_value_type = check_value_type.SHA_256;
                	aCheck_value = new byte[32];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 17;
            	break;
            	case 7 :
                	aCheck_value_length = 64;
                	aCheck_value_type = check_value_type.SHA_512;
                	aCheck_value = new byte[64];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 33;
            	break;
            	case 8 :
                	aCheck_value_length = 8;
                	aCheck_value_type = check_value_type.CRC64;
                	aCheck_value = new byte[8];
                	aCheck_value_string = new String();
                    // Increase the length (size of FILES.LUM File Check Value Type and FILES.LUM File Check Value fields)
                    aLength += 5;
            	break;
                default : ;
        	}
        }
        else {
        }
        // Increase the length (size of FILES.LUM file CRC field)
        aLength++;
        // Initialize the hexa dump of Header file
        aHexa_dump = new String();
    }
    /**************************************************************************
     ** Public method : BuildListOfLoadsFile                                 **
     **************************************************************************/
    public void BuildListOfFilesFile(ARINC_norm_version a_norm_version) throws IOException {
        int i;
        int j;
        int size;
        int lCRC32;
        long lCRC64;
        FileOutputStream lOut_file = new FileOutputStream(pName);

        // Write File Length field
        size = file.WriteInt(lOut_file, aLength);
        // Write Media File Format Version field
        size += file.WriteChar(lOut_file, aVersion);
        // Write Spare field
        size += file.WriteChar(lOut_file, kSpare);
        // Write Pointers
        size += file.WriteInt(lOut_file, aPointer_to_Media_Information);
        size += file.WriteInt(lOut_file, aPointer_to_File_List);
        size += file.WriteInt(lOut_file, aPointer_to_UDD);
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	size += file.WriteInt(lOut_file, aPointer_to_FCV);
        }
        else {
        }
        // Write Media Set PN fields
        size += file.WriteChar(lOut_file, aMSPN_length);
        size += file.WriteString(lOut_file, aMSPN);
        size += file.WriteChar(lOut_file, (char)(((char) aMedia_sequence_number << 8) + (char) aNumber_of_media_set_member));
        // Write Number Of Media Set Files field
        size += file.WriteChar(lOut_file, aNumber_of_media_set_files);
        // Write all the fields for files
        for(i=0;i < aFile_pointer.length;i++) {
            size += file.WriteChar(lOut_file, aFile_pointer[i]);
            size += file.WriteChar(lOut_file, aFN_length[i]);
            size += file.WriteString(lOut_file, aFile_name[i]);
            size += file.WriteChar(lOut_file, aFP_length[i]);
            size += file.WriteString(lOut_file, aFile_pathname[i]);
            size += file.WriteChar(lOut_file, aFile_member_sequence_number);
            size += file.WriteChar(lOut_file, (char) aFile_CRC[i]);
            if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
            	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
            	size += file.WriteChar(lOut_file, (char)0);
            }
            else {
            }
        }
        lOut_file.close();
        // Write File Check Value fields if requested
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
        	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
        	RandomAccessFile lFile = new RandomAccessFile(pName, "rw");
        	byte[] lBytes = new byte[(int) lFile.length()];
        	lFile.read(lBytes);
            switch(aCheck_value_type) {
	         	case CRC32 :
						lCRC32 = integrity_check.CalculateCRC32(lBytes, lBytes.length, 0xFFFFFFFF);
						aCheck_value_string = "";
						for(i=0, j=aCheck_value.length - 1; i < aCheck_value.length;i++, j--) {
							aCheck_value[i] = (byte) ((lCRC32 >> (j * 8)) & 0xFF);
							aCheck_value_string += String.format("%02X", aCheck_value[i]);
						}
						System.out.printf("*** Information *** CRC32 of file %s : 0x%s\n", pName, aCheck_value_string);
	          		break;
            	case MD5 :
    				try {
    					aCheck_value_string = "";
    					aCheck_value = integrity_check.CalculateMD5(lBytes);
    		            size += file.WriteChar(lFile, aCheck_value_length);
                    	size += file.WriteChar(lFile, aCheck_value_type.getValue());
                    	for(i=1;i <= aCheck_value.length;i++) {
                        	size += file.WriteByte(lFile, aCheck_value[i - 1]);
                        	aCheck_value_string += String.format("%02X", aCheck_value[i - 1]);
                        }
     					System.out.printf("*** Information *** MD5 of file %s : %s\n", pName, aCheck_value_string);
    				} catch (NoSuchAlgorithmException e) {
    		            size += file.WriteChar(lFile, (char)0);
                		System.out.printf("*** Information *** MD5 algorihtm exception for %s file\n", pName);
    				}
                    break;
            	case SHA_1 :
    				try {
    					aCheck_value_string = "";
                    	aCheck_value = integrity_check.CalculateSHA1(lBytes);
                    	size += file.WriteChar(lFile, aCheck_value_length);
                    	size += file.WriteChar(lFile, aCheck_value_type.getValue());
                    	for(i=1;i <= aCheck_value.length;i++) {
                        	size += file.WriteByte(lFile, aCheck_value[i - 1]);
                        	aCheck_value_string += String.format("%02X", aCheck_value[i - 1]);
                        }
     					System.out.printf("*** Information *** SHA-1 of file %s : %s\n", pName, aCheck_value_string);
    				} catch (NoSuchAlgorithmException e) {
    		            size += file.WriteChar(lFile, (char)0);
                		System.out.printf("*** Information *** SHA-1 algorihtm exception for %s file\n", pName);
    				}
            		break;
            	case SHA_256 :
    				try {
    					aCheck_value_string = "";
                    	aCheck_value = integrity_check.CalculateSHA256(lBytes);
                    	size += file.WriteChar(lFile, aCheck_value_length);
                    	size += file.WriteChar(lFile, aCheck_value_type.getValue());
                    	for(i=1;i <= aCheck_value.length;i++) {
                        	size += file.WriteByte(lFile, aCheck_value[i - 1]);
                        	aCheck_value_string += String.format("%02X", aCheck_value[i - 1]);
                        }
     					System.out.printf("*** Information *** SHA-256 of file %s : %s\n", pName, aCheck_value_string);
    				} catch (NoSuchAlgorithmException e) {
    		            size += file.WriteChar(lFile, (char)0);
                		System.out.printf("*** Information *** SHA-256 algorihtm exception for %s file\n", pName);
    				}
            		break;
            	case SHA_512 :
    				try {
    					aCheck_value_string = "";
                    	aCheck_value = integrity_check.CalculateSHA512(lBytes);
                    	size += file.WriteChar(lFile, aCheck_value_length);
                    	size += file.WriteChar(lFile, aCheck_value_type.getValue());
                    	for(i=1;i <= aCheck_value.length;i++) {
                        	size += file.WriteByte(lFile, aCheck_value[i - 1]);
                        	aCheck_value_string += String.format("%02X", aCheck_value[i - 1]);
                        }
     					System.out.printf("*** Information *** SHA-512 of file %s : %s\n", pName, aCheck_value_string);
    				} catch (NoSuchAlgorithmException e) {
    		            size += file.WriteChar(lFile, (char)0);
                		System.out.printf("*** Information *** SHA-512 algorihtm exception for %s file\n", pName);
    				}
            		break;
	         	case CRC64 :
						lCRC64 = integrity_check.CalculateCRC64(lBytes, lBytes.length);
						aCheck_value_string = "";
						for(i=0, j=aCheck_value.length - 1; i < aCheck_value.length;i++, j--) {
							aCheck_value[i] = (byte) ((lCRC64 >> (j * 8)) & 0xFF);
							aCheck_value_string += String.format("%02X", aCheck_value[i]);
						}
						System.out.printf("*** Information *** CRC64 of file %s : 0x%s\n", pName, aCheck_value_string);
	          		break;
                default :
		            size += file.WriteChar(lFile, aCheck_value_length);
            }
            lFile.close();
        }
        else {
        }
        aCRC16 = integrity_check.AddCRC16AtEndOfFile(pName);
        size += 2;
        System.out.printf("*** Information *** CRC16 of file %s : 0x%04X\n", pName, aCRC16);
        // Build the hexa dump of Header file
        aHexa_dump = file.BuildHexaDump(pName, size);
    }
}
