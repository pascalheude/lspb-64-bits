package arinc665;

/*
 * Class : software_transport_media
 */
public class software_transport_media {
	// (ARINC665-2 §3.0) (ARINC665-3 §3.0)
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    public list_of_loads_file aList_of_loads_file;
    public list_of_files_file aList_of_files_file;
    public list_of_batch_file aList_of_batch_file;

    /**************************************************************************
     ** Constructor : software_transport_media                               **
     **************************************************************************/
    public software_transport_media(ARINC_norm_version a_norm_version,
                                    String a_load_PN,
                                    char a_load_integrity_check,
                                    String a_sub_directory,
                                    String[] a_THWID_list,
                                    software_part a_software_part) throws Exception {
        // Initialize the list of loads file
        aList_of_loads_file = new list_of_loads_file(a_norm_version,
                                                     a_load_PN,
                                                     a_software_part.aHeader_file.aName,
                                                     a_THWID_list);
        // Build the list of loads file
        aList_of_loads_file.BuildListOfLoadsFile();
        // If batch file is requested
        if (a_software_part.aBatch_file != null) {
            // Initialize the list of batch file
            aList_of_batch_file = new list_of_batch_file(a_norm_version,
                                                         a_load_PN,
                                                         a_software_part.aBatch_file.aBatch_file_PN,
                                                         a_software_part.aBatch_file.aName);
            // Build the list of batch file
            aList_of_batch_file.BuildListOfBatchFile();
            // Initialize the list of files file
            aList_of_files_file = new list_of_files_file(a_norm_version,
                                                         a_sub_directory,
                                                         a_load_PN,
                                                         a_software_part.aHeader_file,
                                                         aList_of_loads_file,
                                                         a_software_part.aData_file,
                                                         a_software_part.aBatch_file,
                                                         a_load_integrity_check);
        }
        else {
            // Initialize the list of files file
            aList_of_files_file = new list_of_files_file(a_norm_version,
                                                         a_sub_directory,
                                                         a_load_PN,
                                                         a_software_part.aHeader_file,
                                                         aList_of_loads_file,
                                                         a_software_part.aData_file,
                                                         a_load_integrity_check);
        }
        // Build the list of files file
        aList_of_files_file.BuildListOfFilesFile(a_norm_version);
    }
}
