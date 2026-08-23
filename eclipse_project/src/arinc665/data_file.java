package arinc665;

import java.io.*;
import java.security.NoSuchAlgorithmException;

/**************************************************************************
 **                                                                      **
 ** Class : data_file                                                    **
 **                                                                      **
 **************************************************************************/
public class data_file {
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    private static final String kFile_extension = "LUP";
    private String pFile_to_split;
    private int pFile_size;
    public String aSub_directory;
    public char[] aPointer;								// (ARINC665-2 �2.2.3.1.15)    (ARINC665-3 �2.2.3.1.32)		(ARINC665-4 �2.2.3.1.32)
    public char aName_length;							// (ARINC665-2 �2.2.3.1.16)    (ARINC665-3 �2.2.3.1.33)		(ARINC665-4 �2.2.3.1.33)
    // do not need to be a table [] because the length of each data filename is always the same
    public String[] aName;								// (ARINC665-2 �2.2.3.1.17)    (ARINC665-3 �2.2.3.1.34)		(ARINC665-4 �2.2.3.1.34)
    public String aPN;									// (ARINC665-2 �2.2.3.1.18-19) (ARINC665-3 �2.2.3.1.35-36)	(ARINC665-4 �2.2.3.1.35-36)
    public int[] aLength_in_bytes;						// (ARINC665-2 �2.2.3.1.20)    (ARINC665-3 �2.2.3.1.37,39)	(ARINC665-4 �2.2.3.1.37,39)
    public int[] aCRC16;								// (ARINC665-2 �2.2.3.1.21)    (ARINC665-3 �2.2.3.1.38)		(ARINC665-4 �2.2.3.1.38)
    public char aCheck_value_length;					//                            (ARINC665-3 �2.2.3.1.40)	(ARINC665-4 �2.2.3.1.40)
    public check_value_type aCheck_value_type;			//                            (ARINC665-3 �2.2.3.1.41)	(ARINC665-4 �2.2.3.1.41)
    public byte[][] aCheck_value;						//                            (ARINC665-3 �2.2.3.1.42)	(ARINC665-4 �2.2.3.1.42)
    public String[] aCheck_value_string;				//                            (ARINC665-3 �2.2.3.1.42)	(ARINC665-4 �2.2.3.1.42)
    public boolean[] aPadding;
    /**************************************************************************
     ** Constructor : data_file                                              **
     **************************************************************************/
    public data_file(ARINC_norm_version a_norm_version,
    		         String an_input_file,
                     String a_sub_directory,
                     String a_base_filename,
                     String a_aPN,
                     int a_file_size,
                     char an_integrity_check) throws ARINC665Exception {
        File lIn_file = new File(an_input_file);
        int i;
        int lNumber_of_file;

        // Copy the directory
        pFile_size = a_file_size;
        pFile_to_split = an_input_file;
        // Copy the directory
        aSub_directory = a_sub_directory;
        // Calculate the number of data file
        lNumber_of_file = (int) (lIn_file.length() / a_file_size);
        if ((lIn_file.length() % a_file_size) != 0) {
            lNumber_of_file++;
        }
        else {
        }
        // Error when number of data files is greater than 9999
        if (lNumber_of_file <= 9999) {
            System.out.printf("*** Information *** Number of LUP file : %d\n", lNumber_of_file);
        }
        else {
            throw new ARINC665Exception("*** Error *** too many LUP files");
        }
        aPointer = new char[lNumber_of_file];
        aName = new String[lNumber_of_file];
        aPN = a_aPN;
        aCRC16 = new int[lNumber_of_file];
        aLength_in_bytes = new int[lNumber_of_file];
        aPadding = new boolean[lNumber_of_file];
    	aCheck_value_length = 0;
    	aCheck_value_type = check_value_type.NOT_USED;
    	aCheck_value = null;
    	aCheck_value_string = null;
        if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
    		(a_norm_version == ARINC_norm_version.ARINC665_4)) {
            switch(an_integrity_check) {
            	case 4 :
                	aCheck_value_length = 16;
                	aCheck_value_type = check_value_type.MD5;
                	aCheck_value = new byte[lNumber_of_file][16];
                	aCheck_value_string = new String[lNumber_of_file];
            	break;
            	case 5 :
                	aCheck_value_length = 20;
                	aCheck_value_type = check_value_type.SHA_1;
                	aCheck_value = new byte[lNumber_of_file][20];
                	aCheck_value_string = new String[lNumber_of_file];
            	break;
            	case 8 :
                	aCheck_value_length = 8;
                	aCheck_value_type = check_value_type.CRC64;
                	aCheck_value = new byte[lNumber_of_file][8];
                	aCheck_value_string = new String[lNumber_of_file];
            	break;
            	default : ;
            }
        }
        else {
        }
        // Build the name of data file
        for(i=1;i <= lNumber_of_file;i++) {
            aName[i - 1] = new String(a_base_filename + 
                                      String.format("_%04d.", i) + 
                                      kFile_extension);
            aName_length = (char) aName[0].length();
            if (i == lNumber_of_file) {
                aPointer[i - 1] = 0;
            }
            else {
                aPointer[i - 1] = (char) (1 + 1 + ((1 + aName_length) / 2) + 1 + ((1 + aPN.length()) / 2) + 2 + 1);
                if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
                	(a_norm_version == ARINC_norm_version.ARINC665_4)) {
                	aPointer[i - 1] += 4 +
                			           1;
                	switch(aCheck_value_type) {
                		case MD5 :
                		case SHA_1 :
                		case CRC64 :
                			aPointer[i - 1] += (aCheck_value_length / 2) - 1;
                			break;
                		default : ;
                	}
                }
                else {
                }
            }
        }
    }
    /**************************************************************************
     ** Public method : BuildDataFile                                        **
     **************************************************************************/
     public void BuildDataFile(ARINC_norm_version a_norm_version,
    		                   byte a_padding_char) throws IOException {
         int i;
         int j;
         int k;
         int lRead_byte;
         long lCRC64;
         byte[] lRead_bytes = new byte[pFile_size];
         byte[] lBytes;
         FileInputStream lIn_file = new FileInputStream(pFile_to_split);

         for(i=1;i <= aName.length;i++) {
             lRead_byte = lIn_file.read(lRead_bytes);
             if ((lRead_byte % 2) != 0) {
            	 aPadding[i - 1] = true;
                 System.out.printf("*** Information *** Padding file %s with one byte\n", aSub_directory + "/" + aName[i - 1]);
                 lBytes = new byte[lRead_byte + 1];
                 for(j=0;j < lRead_byte;j++) {
                     lBytes[j] = lRead_bytes[j];
                 }
                 // Chez LTS (fichier texte contenant du format S au format UNIX), le padding ne consiste pas � ajouter un caract�re � la fin du fichier
                 // mais � ins�rer un caract�re #13 en avant derni�re position sachant que le dernier caract�re sera #10
                 lBytes[lRead_byte] = a_padding_char;
                 lRead_byte++;
             }
             else {
            	 aPadding[i - 1] = false;
                 lBytes = lRead_bytes;
             }
             aLength_in_bytes[i - 1] = lRead_byte;
             aCRC16[i - 1] = integrity_check.CalculateCRC16(lBytes, lBytes.length);
             System.out.printf("*** Information *** CRC16 of file %s : 0x%04X\n", aSub_directory + "/" + aName[i - 1], aCRC16[i - 1]);
             if ((a_norm_version == ARINC_norm_version.ARINC665_3) ||
             	 (a_norm_version == ARINC_norm_version.ARINC665_4)) {
                 switch(aCheck_value_type) {
                 	case MD5 :
                        try {
         					aCheck_value[i - 1] = integrity_check.CalculateMD5(lBytes);
         					aCheck_value_string[i - 1] = "";
         					for(j=0; j < aCheck_value[i - 1].length;j++) {
         						aCheck_value_string[i - 1] += String.format("%02X", aCheck_value[i - 1][j]);
         					}
         					System.out.printf("*** Information *** MD5 of file %s : %s\n", aSub_directory + "/" + aName[i - 1], aCheck_value_string[i - 1]);
                         } catch (NoSuchAlgorithmException e) {
                        		aCheck_value_length = 0;
                        		System.out.printf("*** Information *** MD5 algorihtm exception for data file\n");
                         }
                         break;
                 	case SHA_1 :
                        try {
         					aCheck_value[i - 1] = integrity_check.CalculateSHA1(lBytes);
         					aCheck_value_string[i - 1] = "";
         					for(j=0; j < aCheck_value[i - 1].length;j++) {
         						aCheck_value_string[i - 1] += String.format("%02X", aCheck_value[i - 1][j]);
         					}
         					System.out.printf("*** Information *** SHA-1 of file %s : %s\n", aSub_directory + "/" + aName[i - 1], aCheck_value_string[i - 1]);
         				} catch (NoSuchAlgorithmException e) {
         					aCheck_value_length = 0;
         					System.out.printf("*** Information *** SHA-1 algorihtm exception for data file\n");
         				}
                        break;
                 	case CRC64 :
     					lCRC64 = integrity_check.CalculateCRC64(lBytes, lBytes.length);
      					aCheck_value_string[i - 1] = "";
      					for(j=0, k=aCheck_value[i - 1].length - 1; j < aCheck_value[i - 1].length;j++, k--) {
      						aCheck_value[i - 1][j] = (byte) ((lCRC64 >> (k * 8)) & 0xFF);
      						aCheck_value_string[i - 1] += String.format("%02X", aCheck_value[i - 1][j]);
      					}
     					System.out.printf("*** Information *** CRC64 of file %s : 0x%s\n", aSub_directory + "/" + aName[i - 1], aCheck_value_string[i - 1]);
                 		break;
                 	default : ;
                 }
             }
             else {
             }
             FileOutputStream lOut_file = new FileOutputStream(aSub_directory + "/" + aName[i - 1]);
             lOut_file.write(lBytes, 0, lRead_byte);
             lOut_file.close();
         }
         lIn_file.close();
     }
}
