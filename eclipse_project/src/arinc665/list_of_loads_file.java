package arinc665;

import java.io.FileOutputStream;
import java.io.IOException;

import lspb.file;

/**************************************************************************
 **                                                                      **
 ** Class : list_of_loads_file                                           **
 **                                                                      **
 **************************************************************************/
public class list_of_loads_file {
	// (ARINC665-2 §3.2.3.1) (ARINC665-3 §3.2.3.1)
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    private static final String kFile_extension = "LUM";
    public String aName;
    public int aLength;									// (ARINC665-2 §3.2.3.1.1)     (ARINC665-3 §3.2.3.1.1)
    public char aVersion;								// (ARINC665-2 §3.2.3.1.2)     (ARINC665-3 §3.2.3.1.2)
    public final char kSpare = 0;						// (ARINC665-2 §3.2.3.1.3)     (ARINC665-3 §3.2.3.1.3)
    public int aPointer_to_MI;							// (ARINC665-2 §3.2.3.1.4)     (ARINC665-3 §3.2.3.1.4)
    public int aPointer_to_load_list;					// (ARINC665-2 §3.2.3.1.5)     (ARINC665-3 §3.2.3.1.5)
    public int aPointer_to_UDD;							// (ARINC665-2 §3.2.3.1.6)     (ARINC665-3 §3.2.3.1.6)
    public char aMSPN_length;							// (ARINC665-2 §3.2.3.1.8)     (ARINC665-3 §3.2.3.1.8)
    public String aMSPN;								// (ARINC665-2 §3.2.3.1.9)     (ARINC665-3 §3.2.3.1.9)
    public final byte aMedia_sequence_number = 1;		// (ARINC665-2 §3.2.3.1.10)    (ARINC665-3 §3.2.3.1.10)
    public final byte aNumber_of_media_set_member = 1;	// (ARINC665-2 §3.2.3.1.11)    (ARINC665-3 §3.2.3.1.11)
    public final char aNumber_of_load = 1;				// (ARINC665-2 §3.2.3.1.12)    (ARINC665-3 §3.2.3.1.12)
    public final char aLoad_pointer = 0;				// (ARINC665-2 §3.2.3.1.13)    (ARINC665-3 §3.2.3.1.13)
    public char aLPN_length;							// (ARINC665-2 §3.2.3.1.14)    (ARINC665-3 §3.2.3.1.14)
    public String aLoad_PN;								// (ARINC665-2 §3.2.3.1.15)    (ARINC665-3 §3.2.3.1.15)
    public char aHFN_length;							// (ARINC665-2 §3.2.3.1.16)    (ARINC665-3 §3.2.3.1.16)
    public String aHeader_file_name;					// (ARINC665-2 §3.2.3.1.17)    (ARINC665-3 §3.2.3.1.17)
    public final char aMember_sequence_number = 1;		// (ARINC665-2 §3.2.3.1.18)    (ARINC665-3 §3.2.3.1.18)
    public char aNumber_of_THWID;						// (ARINC665-2 §3.2.3.1.19)    (ARINC665-3 §3.2.3.1.19)
    public String[] aTHWID_list;						// (ARINC665-2 §3.2.3.1.20-21) (ARINC665-3 §3.2.3.1.20-21)
    public int aCRC16;									// (ARINC665-2 §3.2.3.1.25)    (ARINC665-3 §3.2.3.1.25)
    public String aHexa_dump;
    /**************************************************************************
     ** Constructor : list_of_loads_file                                     **
     **************************************************************************/
    public list_of_loads_file(ARINC_norm_version a_norm_version,
                              String a_load_PN,
                              String a_header_filename,
                              String[] a_THWID_list) {
        
        int i;

        // Build the name of list of loads file using the basename and the extension
        aName = "LOADS." + kFile_extension;
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
        aPointer_to_MI = 0;
        // Increase the length (size of Pointer to Media Information field)
        aLength += 2;
        // Initialize the Pointer to Load List field
        aPointer_to_load_list = 0;
        // Increase the length (size of Pointer to Load List field)
        aLength += 2;
        // Initialize the Pointer to User Defined Data field
        aPointer_to_UDD = 0;
        // Increase the length (size of Pointer to User Defined Data field)
        aLength += 2;
        // Set the Pointer to Media Information field
        aPointer_to_MI = aLength;
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
        // Set the Pointer to Load List field
        aPointer_to_load_list = aLength;
        // Set the Number Of Loads field
        //aNumber_of_load = 1;
        // Increase the length (size of Number Of Loads fields)
        aLength++;
        // Set the Load Pointer field
        //aLoad_pointer = 0;
        // Increase the length (size of Load Pointer field)
        aLength++;
        // Set the Load PN Length field
        aLPN_length = (char) a_load_PN.length();
        // Increase the length (size of Load PN Length field)
        aLength++;
        // Set the Load PN field
        aLoad_PN = new String(a_load_PN);
        // Increase the length (size of Load PN field)
        aLength += (1 + a_load_PN.length()) / 2;
        // Set the Header File Name Length field
        aHFN_length = (char) a_header_filename.length();
        // Increase the length (size of Header File Name Length field)
        aLength++;
        // Set the Header File Name field
        aHeader_file_name = new String(a_header_filename);
        // Increase the length (size of Header File Name field)
        aLength += (1 + a_header_filename.length()) / 2;
        // Set the Member Sequence Number field
        //aMember_sequence_number = 1;
        // Increase the length (size of Member Sequence Number field)
        aLength++;
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
        // Increase the length (size of LOADS.LUM file CRC field)
        aLength++;
        // Initialize the hexa dump of Header file
        aHexa_dump = new String();
    }
    /**************************************************************************
     ** Public method : BuildListOfLoadsFile                                 **
     **************************************************************************/
    public void BuildListOfLoadsFile() throws IOException {
        int i;
        int size;
        FileOutputStream lOut_file = new FileOutputStream(aName);

        size = file.WriteInt(lOut_file, aLength);
        size += file.WriteChar(lOut_file, aVersion);
        size += file.WriteChar(lOut_file, kSpare);
        size += file.WriteInt(lOut_file, aPointer_to_MI);
        size += file.WriteInt(lOut_file, aPointer_to_load_list);
        size += file.WriteInt(lOut_file, aPointer_to_UDD);
        size += file.WriteChar(lOut_file, aMSPN_length);
        size += file.WriteString(lOut_file, aMSPN);
        size += file.WriteChar(lOut_file, (char)(((char) aMedia_sequence_number << 8) + (char) aNumber_of_media_set_member));
        size += file.WriteChar(lOut_file, aNumber_of_load);
        size += file.WriteChar(lOut_file, aLoad_pointer);
        size += file.WriteChar(lOut_file, aLPN_length);
        size += file.WriteString(lOut_file, aLoad_PN);
        size += file.WriteChar(lOut_file, aHFN_length);
        size += file.WriteString(lOut_file, aHeader_file_name);
        size += file.WriteChar(lOut_file, aMember_sequence_number);
        size += file.WriteChar(lOut_file, aNumber_of_THWID);
        for(i=1;i <= aTHWID_list.length;i++) {
            size += file.WriteChar(lOut_file, (char) aTHWID_list[i - 1].length());
            size += file.WriteString(lOut_file, aTHWID_list[i - 1]);
        }
        lOut_file.close();
        aCRC16 = integrity_check.AddCRC16AtEndOfFile(aName);
        size += 2;
        System.out.printf("*** Information *** CRC16 of file %s : 0x%04X\n", aName, aCRC16);
        aHexa_dump = file.BuildHexaDump(aName, size);
    }
}
