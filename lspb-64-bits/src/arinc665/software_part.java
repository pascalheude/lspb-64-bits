package arinc665;

/*
 * Class : software_part
 */
public class software_part {
    // (ARINC665-2 §2.0) (ARINC665-3 §2.0)
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    public header_file aHeader_file;
    public support_file aSupport_file;
    public data_file aData_file;
    public batch_file aBatch_file;
    public int aCRC32;

    /**************************************************************************
     ** Constructor : software_part                                          **
     **************************************************************************/
    public software_part(ARINC_norm_version a_norm_version,
                         String a_load_PN,
                         String a_load_type_description,
                         char a_load_integrity_check,
                         char a_load_type_ID,
                         String an_input_file,
                         char an_input_file_integrity_check,
                         byte a_padding_char,
                         int a_file_size,
                         String a_sub_directory,
                         String[] a_THWID_list,
                         String[] a_support_file_list,
                         char a_support_file_integrity_check,
                         boolean a_F_with_UDD,
                         byte[] a_UDD,
                         boolean a_F_batch_file,
                         String a_comment,
                         boolean a_F_with_CRC) throws Exception {
        int i;
        // Initialize and build the support file
        if (a_support_file_list != null) {
            aSupport_file = new support_file(a_norm_version,
            		                         a_support_file_list,
                                             a_load_PN,
                                             a_support_file_integrity_check);
        }
        else {
        	aSupport_file = null;
        }
        // Initialize the data file
        aData_file = new data_file(a_norm_version,
        		                   an_input_file,
                                   a_sub_directory,
                                   a_load_PN,
                                   a_load_PN,
                                   a_file_size,
                                   an_input_file_integrity_check);
        // Build the data file
        aData_file.BuildDataFile(a_norm_version,
        		                 a_padding_char);
        // Initialize the header file
        aHeader_file = new header_file(a_norm_version,
        		                       a_sub_directory,
                                       a_load_PN,
                                       a_load_PN,
                                       a_load_type_description,
                                       a_load_type_ID,
                                       a_THWID_list,
                                       aData_file,
                                       aSupport_file,
                                       a_F_with_UDD,
                                       a_UDD,
                                       a_load_integrity_check);
        // Build the header file
        aHeader_file.BuildHeaderFile(a_norm_version);
        // CRC32 : data files, support files and then header file (see ARINC665-2 §2.2.3.1.36 or ARINC665-3 §2.2.3.1.63)
        // Initialize the CRC32
        aCRC32 = 0xFFFFFFFF;
        // Calculate the CRC32 of each data files
        for(i=0;i < aData_file.aName.length;i++) {
            aCRC32 = integrity_check.CalculateCRC32(aData_file.aSub_directory + "/" + aData_file.aName[i], aData_file.aLength_in_bytes[i], aCRC32, false);
        }
        // Calculate the CRC32 of each support files
        if (aSupport_file != null) {
        	for(i=0;i < aSupport_file.aName.length;i++) {
                aCRC32 = integrity_check.CalculateCRC32(aSupport_file.aPath[i] + "/" + aSupport_file.aName[i], aSupport_file.aLength[i] * 2, aCRC32, false);
        	}
        }
        else {
        }
        // Calculate the CRC32 of the header file
        aCRC32 = integrity_check.CalculateCRC32(aHeader_file.aSub_directory + "/" + aHeader_file.aName, aHeader_file.aLength * 2, aCRC32, true);
        // Add the calculated CRC32 at the end of header file
        if (a_F_with_CRC) {
            integrity_check.AddCRC32AtEndOfFile(aHeader_file.aSub_directory + "/" + aHeader_file.aName, aCRC32);
            System.out.printf("*** Information *** Load CRC : 0x%08X\n", aCRC32);
        }
        else {
            integrity_check.AddCRC32AtEndOfFile(aHeader_file.aSub_directory + "/" + aHeader_file.aName, 0);
            System.out.println("*** Information *** Load CRC forced to 0");
        }
        // Add the batch files if requested
        if (a_F_batch_file) {
            // Initialize the batch file
            aBatch_file = new batch_file(a_norm_version,
            		                     a_load_PN,
            							 a_load_PN,
                                         a_comment,
                                         a_THWID_list,
                                         aHeader_file.aName);
            // Build the batch file
            aBatch_file.BuildBatchFile();
        }
        else {
        	aBatch_file = null;
        }
    }
}