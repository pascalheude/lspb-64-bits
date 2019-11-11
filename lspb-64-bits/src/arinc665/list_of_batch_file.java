package arinc665;

import java.io.FileOutputStream;
import java.io.IOException;

import lspb.file;

/**************************************************************************
 **                                                                      **
 ** Class : list_of_batch_file                                           **
 **                                                                      **
 **************************************************************************/
public class list_of_batch_file {
	// (ARINC665-2 §3.2.3.3) (ARINC665-3 §3.2.3.3)
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    private static final String kFile_extension = "LUM";
    public String pName;
    public int aLength;									// (ARINC665-2 §3.2.3.3.1)  (ARINC665-3 §3.2.3.3.1)
    public char aVersion;								// (ARINC665-2 §3.2.3.3.2)  (ARINC665-3 §3.2.3.3.2)
    public final char kSpare = 0;						// (ARINC665-2 §3.2.3.3.3)  (ARINC665-3 §3.2.3.3.3)
    public int aPointer_to_Media_Information;			// (ARINC665-2 §3.2.3.3.4)  (ARINC665-3 §3.2.3.3.4)
    public int aPointer_to_Batch_List;					// (ARINC665-2 §3.2.3.3.5)  (ARINC665-3 §3.2.3.3.5)
    public int aPointer_to_UDD;							// (ARINC665-2 §3.2.3.3.6)  (ARINC665-3 §3.2.3.3.6)
    public char aMSPN_length;							// (ARINC665-2 §3.2.3.3.8)  (ARINC665-3 §3.2.3.3.8)
    public String aMSPN;								// (ARINC665-2 §3.2.3.3.9)  (ARINC665-3 §3.2.3.3.9)
    public final byte aMedia_sequence_number = 1;		// (ARINC665-2 §3.2.3.3.10) (ARINC665-3 §3.2.3.3.10)
    public final byte aNumber_of_media_set_member = 1;	// (ARINC665-2 §3.2.3.3.11) (ARINC665-3 §3.2.3.3.11)
    public final char aNumber_of_batches = 1;			// (ARINC665-2 §3.2.3.3.12) (ARINC665-3 §3.2.3.3.12)
    public char[] aBatch_pointer;						// (ARINC665-2 §3.2.3.3.13) (ARINC665-3 §3.2.3.3.13)
    public char[] aBatch_PN_length;						// (ARINC665-2 §3.2.3.3.14) (ARINC665-3 §3.2.3.3.14)
    public String[] aBatch_PN;							// (ARINC665-2 §3.2.3.3.15) (ARINC665-3 §3.2.3.3.15)
    public char[] aBatch_FN_length;						// (ARINC665-2 §3.2.3.3.16) (ARINC665-3 §3.2.3.3.16)
    public String[] aBatch_FN;							// (ARINC665-2 §3.2.3.3.17) (ARINC665-3 §3.2.3.3.17)
    public final char kMember_sequence_number = 1;		// (ARINC665-2 §3.2.3.3.18) (ARINC665-3 §3.2.3.3.18)
    public int aCRC16;									// (ARINC665-2 §3.2.3.3.22) (ARINC665-3 §3.2.3.3.22)
    /**************************************************************************
     ** Constructor : list_of_batch_file                                     **
     **************************************************************************/
	public list_of_batch_file(ARINC_norm_version a_norm_version,
                              String a_load_PN,
                              String a_batch_PN,
                              String a_batch_filename) {
        // Build the name of list of loads file using the basename and the extension
        pName = "BATCHES." + kFile_extension;
        // Set the length to 2 (size of BATCHES.LUM File Length field)
        aLength = 2;
        // Set the Media File Format Version field
        aVersion = a_norm_version.getMediaFileFormatVersion();
        // Increase the length (size of Media File Format Version field)
        aLength++;
        // Set the Spare field
        //spare = 0;
        // Increase the length (size of Spare field)
        aLength++;
        // Initialize the Pointer to Media Information field
        aPointer_to_Media_Information = 0;
        // Increase the length (size of Pointer to Media Information field)
        aLength += 2;
        // Initialize the Pointer to Batch List field
        aPointer_to_Batch_List = 0;
        // Increase the length (size of Pointer to Batch List field)
        aLength += 2;
        // Initialize the Pointer to User Defined Data field
        aPointer_to_UDD = 0;
        // Increase the length (size of Pointer to User Defined Data field)
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
        // Set the Number Of Media Set Members field
        //aMedia_sequence_number = 1;
        // Set the Number Of Media Set Members field
        //aNumber_of_media_set_member = 1;
        // Increase the length (size of Media Sequence Number and Number Of Media Set Members fields)
        aLength++;
        // Set the Pointer to Batch List field
        aPointer_to_Batch_List = aLength;
        aBatch_pointer = new char[aNumber_of_batches];
        aBatch_PN_length = new char[aNumber_of_batches];
        aBatch_PN = new String[aNumber_of_batches];
        aBatch_FN_length = new char[aNumber_of_batches];
        aBatch_FN = new String[aNumber_of_batches];

        // Set the Number of Batches field
        //aNumber_of_batches = 1;
        // Increase the length (size of Number of Batches field)
        aLength++;
        // Batch file
        // Set the Batch Pointer field
        aBatch_pointer[0] = 0;
        // Increase the length (size of Batch Pointer field)
        aLength++;
        // Set the Batch PN Length field
        aBatch_PN_length[0] = (char) a_batch_PN.length();
        // Increase the length (size of Batch PN Length field)
        aLength++;
        // Set the Batch PN field
        aBatch_PN[0] = new String(a_batch_PN);
        // Increase the length (size of Batch PN field)
        aLength += (1 + a_batch_PN.length()) / 2;
        // Set the Batch File Name Length field
        aBatch_FN_length[0] = (char) a_batch_filename.length();
        // Increase the length (size of Batch File Name Length field)
        aLength++;
        // Set the Batch File Name field
        aBatch_FN[0] = new String(a_batch_filename);
        // Increase the length (size of Batch File Name field)
        aLength += (1 + a_batch_filename.length()) / 2;
        // Increase the length (size of Member Sequence Number field)
        aLength++;
        // Increase the length (size of BATCHES.LUM file CRC field)
        aLength++;
	}
    /**************************************************************************
     ** Public method : BuildListOfBatchFile                                 **
     **************************************************************************/
	public void BuildListOfBatchFile() throws IOException {
		int i;
        int size;
        FileOutputStream lOut_file = new FileOutputStream(pName);

        size = file.WriteInt(lOut_file, aLength);
        size += file.WriteChar(lOut_file, aVersion);
        size += file.WriteChar(lOut_file, kSpare);
        size += file.WriteInt(lOut_file, aPointer_to_Media_Information);
        size += file.WriteInt(lOut_file, aPointer_to_Batch_List);
        size += file.WriteInt(lOut_file, aPointer_to_UDD);
        size += file.WriteChar(lOut_file, aMSPN_length);
        size += file.WriteString(lOut_file, aMSPN);
        size += file.WriteChar(lOut_file, (char)(((char) aMedia_sequence_number << 8) + (char) aNumber_of_media_set_member));
        size += file.WriteChar(lOut_file, aNumber_of_batches);
        for(i=0;i < aNumber_of_batches;i++) {
            size += file.WriteChar(lOut_file, aBatch_pointer[i]);
            size += file.WriteChar(lOut_file, aBatch_PN_length[i]);
            size += file.WriteString(lOut_file, aBatch_PN[i]);
            size += file.WriteChar(lOut_file, aBatch_FN_length[i]);
            size += file.WriteString(lOut_file, aBatch_FN[i]);
            size += file.WriteChar(lOut_file, kMember_sequence_number);
        }
        lOut_file.close();
        aCRC16 = integrity_check.AddCRC16AtEndOfFile(pName);
        System.out.printf("*** Information *** CRC16 of file %s : 0x%04X\n", pName, aCRC16);
	}
}
