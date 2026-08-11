package arinc665;

/*
 * Class : check_value
 */
public enum check_value_type {
	// Table 5.1
	NOT_USED((char)0),
	CRC8((char)1),	// Deprecated
	CRC16((char)2),	// Deprecated
	CRC32((char)3),	// TODO
	MD5((char)4),
	SHA_1((char)5),
	SHA_256((char)6),	// TODO
	SHA_512((char)7),	// TODO
	CRC64((char)8);

    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
	private char pValue;

	/**************************************************************************
     ** Constructor : check_value                                            **
     **************************************************************************/
	private check_value_type(char a_value) {
		this.pValue = a_value;
	}

	/**************************************************************************
     ** Public method : getValue                                             **
     **************************************************************************/
	public char getValue() {
		return(pValue);
	}
}
