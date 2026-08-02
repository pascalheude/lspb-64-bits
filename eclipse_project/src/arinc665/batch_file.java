package arinc665;

import java.io.FileOutputStream;
import java.io.IOException;

import lspb.file;

/**************************************************************************
 **                                                                      **
 ** Class : batch_file                                                   **
 **                                                                      **
 **************************************************************************/
public class batch_file {
	// (ARINC665-2 §2.3.1) (ARINC665-3 §2.3.1)
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    private static final String kFile_extension = "LUB";
    public String aName;
    public int aLength;											// (ARINC665-2 §2.3.1.1)     (ARINC665-3 §2.3.1.1)
    public char aVersion;										// (ARINC665-2 §2.3.1.2)     (ARINC665-3 §2.3.1.2)
    public final char kSpare = 0;								// (ARINC665-2 §2.3.1.3)     (ARINC665-3 §2.3.1.3)
    public int aPointer_to_BFPN_length;							// (ARINC665-2 §2.3.1.4)     (ARINC665-3 §2.3.1.4)
    public int aPointer_to_number_of_THWID_load_list_blocks;	// (ARINC665-2 §2.3.1.5)     (ARINC665-3 §2.3.1.5)
    public char aBFPN_length;									// (ARINC665-2 §2.3.1.7)     (ARINC665-3 §2.3.1.7)
    public String aBatch_file_PN;								// (ARINC665-2 §2.3.1.8)     (ARINC665-3 §2.3.1.8)
    public char aComment_length;								// (ARINC665-2 §2.3.1.9)     (ARINC665-3 §2.3.1.9)
    public String aComment;										// (ARINC665-2 §2.3.1.10)    (ARINC665-3 §2.3.1.10)
    public char aNumber_of_THWID_load_list_blocks;				// (ARINC665-2 §2.3.1.11)    (ARINC665-3 §2.3.1.11)
    public char[] aPointer_to_next_THWID_load_list_block;		// (ARINC665-2 §2.3.1.12)    (ARINC665-3 §2.3.1.12)
    public String[] aTHWID_list;								// (ARINC665-2 §2.3.1.13-14) (ARINC665-3 §2.3.1.13-14)
    public String[] aHeader_filename;							// (ARINC665-2 §2.3.1.16-17) (ARINC665-3 §2.3.1.16-17)
    public String[] aLoad_PN;									// (ARINC665-2 §2.3.1.18-19) (ARINC665-3 §2.3.1.18-19)
    public int aCRC16;											// (ARINC665-2 §2.3.1.20)    (ARINC665-3 §2.3.1.20)
    /**************************************************************************
     ** Constructor : batch_file                                             **
     **************************************************************************/
    public batch_file (ARINC_norm_version a_norm_version,
    		           String a_base_filename,
                       String a_load_PN,
                       String a_comment,
                       String[] a_THWID_list,
                       String a_header_filename) {
        int i;

        // Build the name of batch file using the basename and the extension
        aName = a_base_filename  + "." + kFile_extension;
        // Set the length to 2 (size of Header File Length field)
        aLength = 2;
        // Set the Batch File Format Version field
        aVersion = a_norm_version.getBatchFileFormatVersion();
        // Increase the length (size of Load File Format Version field)
        aLength++;
        // Set the Spare field
        //spare = 0;
        // Increase the length (size of Spare field)
        aLength++;
        // Initialize the Pointer to Batch File PN field
        aPointer_to_BFPN_length = 0;
        // Increase the length (size of Pointer to Batch File PN field)
        aLength += 2;
        // Initialize the Pointer to Number of THW ID Load-List Blocks field
        aPointer_to_number_of_THWID_load_list_blocks = 0;
        // Increase the length (size of Number of THW ID Load-List Blocks field)
        aLength += 2;
        // Set the Pointer to Batch File PN field
        aPointer_to_BFPN_length = aLength;
        // Set the Pointer to Batch File PN field
        aBFPN_length = (char) aLength;
        // Set the Batch File PN field
        aBFPN_length = (char) a_load_PN.length();
        // Increase the length (size of Load PN Length field)
        aLength++;
        // Set the Load PN field
        aBatch_file_PN = new String(a_load_PN);
        // Increase the length (size of Load PN field)
        aLength += (1 + a_load_PN.length()) / 2;
        // Set the Comment Length field
        aComment_length = (char) a_comment.length();
        // Increase the length (size of Comment Length field)
        aLength++;
        // Set the Comment field
        aComment = a_comment;
        // Increase the length (size of Comment field)
        aLength += (1 + a_comment.length()) / 2;
        // Set the Pointer to Number of THW ID Load-List Blocks field
        aPointer_to_number_of_THWID_load_list_blocks = aLength;
        // Set the Number of Target HW ID Load-List Blocks field
        aNumber_of_THWID_load_list_blocks = (char) a_THWID_list.length;
        // Increase the length (size of Comment field)
        aLength++;
        aPointer_to_next_THWID_load_list_block = new char[aNumber_of_THWID_load_list_blocks];
        aTHWID_list = a_THWID_list;
        aHeader_filename = new String[aNumber_of_THWID_load_list_blocks];
        aLoad_PN = new String[aNumber_of_THWID_load_list_blocks];
        for(i=0;i < aNumber_of_THWID_load_list_blocks;i++) {
            if (i != (aNumber_of_THWID_load_list_blocks - 1)) {
                aPointer_to_next_THWID_load_list_block[i] = (char) (1 +
                                                                    ((1 + a_THWID_list[i].length()) / 2) +
                                                                    1 +
                                                                    1 +
                                                                    ((1 + a_header_filename.length()) / 2) +
                                                                    1 +
                                                                    ((1 + a_load_PN.length() + 2) / 2));
            }
            else {
                aPointer_to_next_THWID_load_list_block[i] = 0;
            }
            aHeader_filename[i] = a_header_filename;
            aLoad_PN[i] = a_load_PN;
            // Increase the length (size of fields)
            aLength += (char) (1 +
                               ((1 + a_THWID_list[i].length()) / 2) +
                               1 +
                               1 +
                               ((1 + a_header_filename.length()) / 2) +
                               1 +
                               ((1 + a_load_PN.length() + 2) / 2));
        }
        // Increase the length (size of batch file CRC field)
        aLength++;        
    }
    /**************************************************************************
     ** Public method : BuildBatchFile                                       **
     **************************************************************************/
    public void BuildBatchFile() throws IOException {
        int i;
        int size;
        FileOutputStream lOut_file = new FileOutputStream(aName);

        size = file.WriteInt(lOut_file, aLength);
        size += file.WriteChar(lOut_file, aVersion);
        size += file.WriteChar(lOut_file, kSpare);
        size += file.WriteInt(lOut_file, aPointer_to_BFPN_length);
        size += file.WriteInt(lOut_file, aPointer_to_number_of_THWID_load_list_blocks);
        size += file.WriteChar(lOut_file, aBFPN_length);
        size += file.WriteString(lOut_file, aBatch_file_PN);
        size += file.WriteChar(lOut_file, aComment_length);
        if (aComment.length() != 0) {
            size += file.WriteString(lOut_file, aComment);
        }
        else {
            size += file.WriteChar(lOut_file, (char) 0);
        }
        size += file.WriteChar(lOut_file, aNumber_of_THWID_load_list_blocks);
        for(i=1;i <= aTHWID_list.length;i++) {
            size += file.WriteChar(lOut_file, (char) aPointer_to_next_THWID_load_list_block[i - 1]);
            size += file.WriteChar(lOut_file, (char) aTHWID_list[i - 1].length());
            size += file.WriteString(lOut_file, aTHWID_list[i - 1]);
            size += file.WriteChar(lOut_file, (char) 1);
            size += file.WriteChar(lOut_file, (char) aHeader_filename[i - 1].length());
            size += file.WriteString(lOut_file, aHeader_filename[i - 1]);
            size += file.WriteChar(lOut_file, (char) aLoad_PN[i - 1].length());
            size += file.WriteString(lOut_file, aLoad_PN[i - 1]);
        }
        lOut_file.close();
        aCRC16 = integrity_check.AddCRC16AtEndOfFile(aName);
        size += 2;
        System.out.printf("*** Information *** CRC16 of file %s : 0x%04X\n", aName, aCRC16);
    }
}
