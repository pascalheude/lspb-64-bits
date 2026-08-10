package arinc665;

/*
 * Class : ARINC_norm_version
 */
public enum ARINC_norm_version {
	ARINC665_1(0),
	ARINC665_2(1),
	ARINC665_3(2),
	ARINC665_4(3);

    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    //                                                  ARINC665-1 ARINC665-2 ARINC665-3 ARINC665-4 (see ARINC665 �1.4.1)
    private final char kLoad_file_format_version[]  = {  '\u8002',  '\u8003',  '\u8004',  '\u8004'  };
    private final char kBatch_file_format_version[] = {  '\u0000',  '\u9003',  '\u9004',  '\u9004'  };
    private final char kMedia_file_format_version[] = {  '\u8002',  '\uA003',  '\uA004',  '\uA004'  };
	private int pValue;

    /**************************************************************************
     ** Constructor : ARINC_norm_version                                     **
     **************************************************************************/
	private ARINC_norm_version(int a_value) {
		this.pValue = a_value;
	}
    /**************************************************************************
     ** Public method : pValue                                               **
     **************************************************************************/
    public static ARINC_norm_version fromValue(int a_value) {
    	switch(a_value) {
			case 1 : return(ARINC665_1);
			case 2 : return(ARINC665_2);
			case 3 : return(ARINC665_3);
			case 4 : return(ARINC665_4);
    	}
    	return(ARINC665_2);
    }
    /**************************************************************************
     ** Public method : getLoadFileFormatVersion                             **
     **************************************************************************/
	public char getLoadFileFormatVersion() {
		return(kLoad_file_format_version[pValue]);
	}
    /**************************************************************************
     ** Public method : getBatchFileFormatVersion                            **
     **************************************************************************/
	public char getBatchFileFormatVersion() {
		return(kBatch_file_format_version[pValue]);
	}
    /**************************************************************************
     ** Public method : getMediaFileFormatVersion                            **
     **************************************************************************/
	public char getMediaFileFormatVersion() {
		return(kMedia_file_format_version[pValue]);
	}
}
