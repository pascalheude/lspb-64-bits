package arinc665;

/*
 * Class : ARINC665
 */
public class ARINC665 {
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    public software_part aSoftware_part;
    public software_transport_media aSoftware_transport_media;
    	
    /**************************************************************************
     ** Constructor : ARINC665                                               **
     **************************************************************************/
	public ARINC665(ARINC_norm_version a_norm_version,
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
					boolean a_F_batch_file,
					String a_comment,
					boolean a_F_with_UDD,
					byte[] a_UDD,
					boolean a_F_media_required,
					String a_media_PN,
					boolean a_F_with_CRC) throws Exception {

        // Initialize and build the loadable software parts
        aSoftware_part = new software_part(a_norm_version,
                                           a_load_PN,
                                           a_load_type_description,
                                           a_load_integrity_check,
                                           a_load_type_ID,
                                           an_input_file,
                                           an_input_file_integrity_check,
                                           a_padding_char,
                                           a_file_size,
                                           a_sub_directory,
                                           a_THWID_list,
                                           a_support_file_list,
                                           a_support_file_integrity_check,
                                           a_F_with_UDD,
                                           a_UDD,
                                           a_F_batch_file,
                                           a_comment,
                                           a_F_with_CRC);
        // Initialize and build the loadable software transport media if requested
        if (a_F_media_required) {
        aSoftware_transport_media = new software_transport_media(a_norm_version,
                                                                 a_media_PN,
                                                                 a_load_integrity_check,
                                                                 a_sub_directory,
                                                                 a_THWID_list,
                                                                 aSoftware_part);
        }
        else {
        }
     }
}
