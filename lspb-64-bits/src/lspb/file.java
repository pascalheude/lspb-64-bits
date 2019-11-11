package lspb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;

/*
 * Class : file
 */
public class file {
    /**************************************************************************
     ** Public method : BuildHexaDump                                        **
     **************************************************************************/
	public static String BuildHexaDump(String a_filename, int a_size) throws IOException {
		int i;
        byte[] lBytes;
        String lString;
        String lHexa_dump;
        FileInputStream lFile;
        i = 0;
        lString = new String();
        lFile = new FileInputStream(a_filename);
        lBytes = new byte[a_size];
        lFile.read(lBytes);
        lFile.close();
        lHexa_dump = "00000000  ";
        for(byte b : lBytes) {
        	if ((i != 0) && ((i % 16) == 0)) {
        		lHexa_dump += String.format("  %s\n%08X  ", lString, i);
        		lString = "";
        	}
        	else {
        	}
        	lHexa_dump += String.format("%02X ", b);
        	if ((b < 32) || (b > 127)) {
            	lString += ".";
        	}
        	else {
            	lString += String.format("%c", b);
        	}
        	i++;
        }
        while((i % 16) != 0) {
        	lHexa_dump += String.format("   ");
        	i++;
        }
        lHexa_dump += String.format("  %s", lString);
		return(lHexa_dump);
	}
    /**************************************************************************
     ** Public method : BuildHexaDump                                        **
     **************************************************************************/
	public static String BuildHexaDump(byte[] a_bytes, int a_size) throws IOException {
		int i;
        String lString;
        String lHexa_dump;
        i = 0;
        lString = new String();
        lHexa_dump = "00000000  ";
        for(byte b : a_bytes) {
        	if ((i != 0) && ((i % 16) == 0)) {
        		lHexa_dump += String.format("  %s\n%08X  ", lString, i);
        		lString = "";
        	}
        	else {
        	}
        	lHexa_dump += String.format("%02X ", b);
        	if ((b < 32) || (b > 127)) {
            	lString += ".";
        	}
        	else {
            	lString += String.format("%c", b);
        	}
        	i++;
        }
        while((i % 16) != 0) {
        	lHexa_dump += String.format("   ");
        	i++;
        }
        lHexa_dump += String.format("  %s", lString);
		return(lHexa_dump);
	}
    /**************************************************************************
     ** Public method : WriteByte                                            **
     **************************************************************************/
    public static int WriteByte(FileOutputStream a_file, byte a_byte) throws IOException {
        a_file.write(a_byte);
        return(1);
    }
    /**************************************************************************
     ** Public method : WriteByte                                            **
     **************************************************************************/
    public static int WriteByte(RandomAccessFile a_file, byte a_byte) throws IOException {
        a_file.write(a_byte);
        return(1);
    }
    /**************************************************************************
     ** Public method : WriteChar                                            **
     **************************************************************************/
    public static int WriteChar(FileOutputStream a_file, char a_char) throws IOException {
        a_file.write(a_char >> 8);
        a_file.write(a_char & 0xFF);
        return(2);
    }
    /**************************************************************************
     ** Public method : WriteChar                                            **
     **************************************************************************/
    public static int WriteChar(RandomAccessFile a_file, char a_char) throws IOException {
        a_file.write(a_char >> 8);
        a_file.write(a_char & 0xFF);
        return(2);
    }
    /**************************************************************************
     ** Public method : WriteInt                                             **
     **************************************************************************/
    public static int WriteInt(FileOutputStream a_file, int a_int) throws IOException {
        a_file.write(a_int >> 24);
        a_file.write((a_int >> 16) & 0xFF);
        a_file.write((a_int >> 8) & 0xFF);
        a_file.write(a_int & 0xFF);
        return(4);
    }
    /**************************************************************************
     ** Public method : WriteString                                          **
     **************************************************************************/
    public static int WriteString(FileOutputStream a_file, String a_string) throws IOException {
        a_file.write(a_string.getBytes());
        if ((a_string.length() % 2) != 0) {
            a_file.write(0);
            return(a_string.length() + 1);
        }
        else {
            return(a_string.length());
        }
    }
}
