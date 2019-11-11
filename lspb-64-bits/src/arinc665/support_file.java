package arinc665;

import java.io.*;
import java.security.NoSuchAlgorithmException;

/*
 * Class : support_file
 */
public class support_file {
    /**************************************************************************
     ** Attributes                                                           **
     **************************************************************************/
    public char[] aPointer;								// (ARINC665-2 §2.2.3.1.XX)    (ARINC665-3 §2.2.3.1.45)
    public String[] aPath;								// (ARINC665-2 §2.2.3.1.XX-XX) (ARINC665-3 §2.2.3.1.46-47)
    public String[] aName;
    public char aName_length[];
    public String aPN;									// (ARINC665-2 §2.2.3.1.XX-XX) (ARINC665-3 §2.2.3.1.48-49)
    public int[] aLength;								// (ARINC665-2 §2.2.3.1.XX)    (ARINC665-3 §2.2.3.1.50)
    public int[] aCRC16;								// (ARINC665-2 §2.2.3.1.XX)    (ARINC665-3 §2.2.3.1.51)
    public char aCheck_value_length;					//                             (ARINC665-3 §2.2.3.1.52)
    public char aCheck_value_type;						//                             (ARINC665-3 §2.2.3.1.53)
    public byte[][] aCheck_value;						//                             (ARINC665-3 §2.2.3.1.54)
    public String[] aCheck_value_string;				//                             (ARINC665-3 §2.2.3.1.54)
    /**************************************************************************
     ** Constructor : support_file                                           **
     **************************************************************************/
	public support_file(ARINC_norm_version a_norm_version,
			            String[] a_support_file_list,
                        String a_aPN,
                        char an_integrity_check) throws IOException {
		int i;
		int j;
        byte[] lBytes;
        File lFile;
        RandomAccessFile lRAFile;

        aPointer = new char[a_support_file_list.length];
        aPath = new String[a_support_file_list.length];
        aName = new String[a_support_file_list.length];
        aName_length = new char[a_support_file_list.length];
        aLength = new int[a_support_file_list.length];
        aPN = a_aPN;
        aCRC16 = new int[a_support_file_list.length];
    	aCheck_value_length = 0;
    	aCheck_value_type = 0;
    	aCheck_value = null;
    	aCheck_value_string = null;
        if (a_norm_version == ARINC_norm_version.ARINC665_3) {
            switch(an_integrity_check) {
            	case 4 :
                	aCheck_value_length = 20;
                	aCheck_value_type = 4;
                	aCheck_value = new byte[a_support_file_list.length][16];
                	aCheck_value_string = new String[a_support_file_list.length];
            	break;
            	case 5 :
                	aCheck_value_length = 24;
                	aCheck_value_type = 5;
                	aCheck_value = new byte[a_support_file_list.length][20];
                	aCheck_value_string = new String[a_support_file_list.length];
            	break;
            	default : ;
            }
        }
        else {
        }
        for(i=1;i <= a_support_file_list.length;i++) {
        	lFile = new File(a_support_file_list[i - 1]);
            aPath[i - 1] = new String(lFile.getParent());
            aName[i - 1] = new String(lFile.getName());
            aName_length[i - 1] = (char) aName[i - 1].length();
            lRAFile = new RandomAccessFile(a_support_file_list[i - 1], "r");
            lBytes = new byte[(int) lRAFile.length()];
            lRAFile.read(lBytes);
            lRAFile.close();
            aLength[i - 1] = lBytes.length;
            aCRC16[i - 1] = integrity_check.CalculateCRC16(lBytes, lBytes.length);
            System.out.printf("*** Information *** CRC16 of support file #%d %s : 0x%04X\n", i, aName[i - 1], aCRC16[i - 1]);
            if (a_norm_version == ARINC_norm_version.ARINC665_3) {
                switch(aCheck_value_type) {
                	case 4 :
                       try {
        					aCheck_value[i - 1] = integrity_check.CalculateMD5(lBytes);
         					aCheck_value_string[i - 1] = "";
         					for(j=0; j < aCheck_value[i - 1].length;j++) {
         						aCheck_value_string[i - 1] += String.format("%02X", aCheck_value[i - 1][j]);
         					}
        					System.out.printf("*** Information *** MD5 of file %s : %s\n", a_support_file_list[i - 1], aCheck_value_string[i - 1]);
                        } catch (NoSuchAlgorithmException e) {
                       		aCheck_value_length = 0;
                       		System.out.printf("*** Information *** MD5 algorihtm exception for data file\n");
                        }
                        break;
                	case 5 :
                       try {
        					aCheck_value[i - 1] = integrity_check.CalculateSHA1(lBytes);
         					aCheck_value_string[i - 1] = "";
         					for(j=0; j < aCheck_value[i - 1].length;j++) {
         						aCheck_value_string[i - 1] += String.format("%02X", aCheck_value[i - 1][j]);
         					}
        					System.out.printf("*** Information *** SHA-1 of file %s : %s\n", a_support_file_list[i - 1], aCheck_value_string[i - 1]);
        				} catch (NoSuchAlgorithmException e) {
        					aCheck_value_length = 0;
        					System.out.printf("*** Information *** SHA-1 algorihtm exception for data file\n");
        				}
                       break;
                	default : ;
                }
            }
            else {
            }
            if (i == a_support_file_list.length) {
                aPointer[i - 1] = 0;
            }
            else {
                aPointer[i - 1] = (char) (1 + 1 + ((1 + aName_length[i - 1]) / 2) + 1 + ((1 + aPN.length()) / 2) + 2 + 1);
                if (a_norm_version == ARINC_norm_version.ARINC665_3) {
                	switch(aCheck_value_type) {
                		case 4 :
                		case 5 :
                			aPointer[i - 1] += (aCheck_value_length / 2);
                			break;
                		default : ;
                	}
                }
                else {
                }
            }
        }
	}
}
