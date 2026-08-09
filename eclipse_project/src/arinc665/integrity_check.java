package arinc665;

import java.io.*;
import java.security.*;

/*
 ** Class : integrity_check
*/
public class integrity_check {
	/**************************************************************************
	 ** Public method : CalculateCRC8                                        **
	 **************************************************************************/
	public static byte CalculateCRC8(String a_string) {
		byte[] lBytes = a_string.getBytes();
		byte lCRC8 = 0;

		for(byte b : lBytes) {
			lCRC8 ^= b;
		}
		return(lCRC8);
	}
	/**************************************************************************
	** Public method : CalculateCRC16                                       **
	**************************************************************************/
	public static int CalculateCRC16(byte[] a_bytes, int a_length) {
		int i;
		int lTable_index;
		int[] lTable = {
		                  0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5, 0x60C6, 0x70E7,
		                  0x8108, 0x9129, 0xA14A, 0xB16B, 0xC18C, 0xD1AD, 0xE1CE, 0xF1EF,
		                  0x1231, 0x0210, 0x3273, 0x2252, 0x52B5, 0x4294, 0x72F7, 0x62D6,
		                  0x9339, 0x8318, 0xB37B, 0xA35A, 0xD3BD, 0xC39C, 0xF3FF, 0xE3DE,
		                  0x2462, 0x3443, 0x0420, 0x1401, 0x64E6, 0x74C7, 0x44A4, 0x5485,
		                  0xA56A, 0xB54B, 0x8528, 0x9509, 0xE5EE, 0xF5CF, 0xC5AC, 0xD58D,
		                  0x3653, 0x2672, 0x1611, 0x0630, 0x76D7, 0x66F6, 0x5695, 0x46B4,
		                  0xB75B, 0xA77A, 0x9719, 0x8738, 0xF7DF, 0xE7FE, 0xD79D, 0xC7BC,
		                  0x48C4, 0x58E5, 0x6886, 0x78A7, 0x0840, 0x1861, 0x2802, 0x3823,
		                  0xC9CC, 0xD9ED, 0xE98E, 0xF9AF, 0x8948, 0x9969, 0xA90A, 0xB92B,
		                  0x5AF5, 0x4AD4, 0x7AB7, 0x6A96, 0x1A71, 0x0A50, 0x3A33, 0x2A12,
		                  0xDBFD, 0xCBDC, 0xFBBF, 0xEB9E, 0x9B79, 0x8B58, 0xBB3B, 0xAB1A,
		                  0x6CA6, 0x7C87, 0x4CE4, 0x5CC5, 0x2C22, 0x3C03, 0x0C60, 0x1C41,
		                  0xEDAE, 0xFD8F, 0xCDEC, 0xDDCD, 0xAD2A, 0xBD0B, 0x8D68, 0x9D49,
		                  0x7E97, 0x6EB6, 0x5ED5, 0x4EF4, 0x3E13, 0x2E32, 0x1E51, 0x0E70,
		                  0xFF9F, 0xEFBE, 0xDFDD, 0xCFFC, 0xBF1B, 0xAF3A, 0x9F59, 0x8F78,
		                  0x9188, 0x81A9, 0xB1CA, 0xA1EB, 0xD10C, 0xC12D, 0xF14E, 0xE16F,
		                  0x1080, 0x00A1, 0x30C2, 0x20E3, 0x5004, 0x4025, 0x7046, 0x6067,
		                  0x83B9, 0x9398, 0xA3FB, 0xB3DA, 0xC33D, 0xD31C, 0xE37F, 0xF35E,
		                  0x02B1, 0x1290, 0x22F3, 0x32D2, 0x4235, 0x5214, 0x6277, 0x7256,
		                  0xB5EA, 0xA5CB, 0x95A8, 0x8589, 0xF56E, 0xE54F, 0xD52C, 0xC50D,
		                  0x34E2, 0x24C3, 0x14A0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405,
		                  0xA7DB, 0xB7FA, 0x8799, 0x97B8, 0xE75F, 0xF77E, 0xC71D, 0xD73C,
		                  0x26D3, 0x36F2, 0x0691, 0x16B0, 0x6657, 0x7676, 0x4615, 0x5634,
		                  0xD94C, 0xC96D, 0xF90E, 0xE92F, 0x99C8, 0x89E9, 0xB98A, 0xA9AB,
		                  0x5844, 0x4865, 0x7806, 0x6827, 0x18C0, 0x08E1, 0x3882, 0x28A3,
		                  0xCB7D, 0xDB5C, 0xEB3F, 0xFB1E, 0x8BF9, 0x9BD8, 0xABBB, 0xBB9A,
		                  0x4A75, 0x5A54, 0x6A37, 0x7A16, 0x0AF1, 0x1AD0, 0x2AB3, 0x3A92,
		                  0xFD2E, 0xED0F, 0xDD6C, 0xCD4D, 0xBDAA, 0xAD8B, 0x9DE8, 0x8DC9,
		                  0x7C26, 0x6C07, 0x5C64, 0x4C45, 0x3CA2, 0x2C83, 0x1CE0, 0x0CC1,
		                  0xEF1F, 0xFF3E, 0xCF5D, 0xDF7C, 0xAF9B, 0xBFBA, 0x8FD9, 0x9FF8,
		                  0x6E17, 0x7E36, 0x4E55, 0x5E74, 0x2E93, 0x3EB2, 0x0ED1, 0x1EF0
		};
		int lCRC16 = 0xFFFF;

		for (i=0;i < a_length;i++) {
			lTable_index = (((lCRC16 >> 8) ^ a_bytes[i]) & 0xFF);
			lCRC16 = ((lCRC16 << 8) ^ lTable[lTable_index]) & 0xFFFF;
		}
		return(lCRC16);
	}
	/**************************************************************************
	 ** Public method : CalculateCRC32                                       **
	 **************************************************************************/
	public static int CalculateCRC32(byte[] a_bytes, int a_length, int a_remainder) {
		int[] lTable = {
		                 0x00000000, 0x04C11DB7, 0x09823B6E, 0x0D4326D9, 0x130476DC, 0x17C56B6B, 0x1A864DB2, 0x1E475005, 
		                 0x2608EDB8, 0x22C9F00F, 0x2F8AD6D6, 0x2B4BCB61, 0x350C9B64, 0x31CD86D3, 0x3C8EA00A, 0x384FBDBD, 
		                 0x4C11DB70, 0x48D0C6C7, 0x4593E01E, 0x4152FDA9, 0x5F15ADAC, 0x5BD4B01B, 0x569796C2, 0x52568B75, 
		                 0x6A1936C8, 0x6ED82B7F, 0x639B0DA6, 0x675A1011, 0x791D4014, 0x7DDC5DA3, 0x709F7B7A, 0x745E66CD, 
		                 0x9823B6E0, 0x9CE2AB57, 0x91A18D8E, 0x95609039, 0x8B27C03C, 0x8FE6DD8B, 0x82A5FB52, 0x8664E6E5, 
		                 0xBE2B5B58, 0xBAEA46EF, 0xB7A96036, 0xB3687D81, 0xAD2F2D84, 0xA9EE3033, 0xA4AD16EA, 0xA06C0B5D, 
		                 0xD4326D90, 0xD0F37027, 0xDDB056FE, 0xD9714B49, 0xC7361B4C, 0xC3F706FB, 0xCEB42022, 0xCA753D95, 
		                 0xF23A8028, 0xF6FB9D9F, 0xFBB8BB46, 0xFF79A6F1, 0xE13EF6F4, 0xE5FFEB43, 0xE8BCCD9A, 0xEC7DD02D, 
		                 0x34867077, 0x30476DC0, 0x3D044B19, 0x39C556AE, 0x278206AB, 0x23431B1C, 0x2E003DC5, 0x2AC12072, 
		                 0x128E9DCF, 0x164F8078, 0x1B0CA6A1, 0x1FCDBB16, 0x018AEB13, 0x054BF6A4, 0x0808D07D, 0x0CC9CDCA, 
		                 0x7897AB07, 0x7C56B6B0, 0x71159069, 0x75D48DDE, 0x6B93DDDB, 0x6F52C06C, 0x6211E6B5, 0x66D0FB02, 
		                 0x5E9F46BF, 0x5A5E5B08, 0x571D7DD1, 0x53DC6066, 0x4D9B3063, 0x495A2DD4, 0x44190B0D, 0x40D816BA, 
		                 0xACA5C697, 0xA864DB20, 0xA527FDF9, 0xA1E6E04E, 0xBFA1B04B, 0xBB60ADFC, 0xB6238B25, 0xB2E29692, 
		                 0x8AAD2B2F, 0x8E6C3698, 0x832F1041, 0x87EE0DF6, 0x99A95DF3, 0x9D684044, 0x902B669D, 0x94EA7B2A, 
		                 0xE0B41DE7, 0xE4750050, 0xE9362689, 0xEDF73B3E, 0xF3B06B3B, 0xF771768C, 0xFA325055, 0xFEF34DE2, 
		                 0xC6BCF05F, 0xC27DEDE8, 0xCF3ECB31, 0xCBFFD686, 0xD5B88683, 0xD1799B34, 0xDC3ABDED, 0xD8FBA05A, 
		                 0x690CE0EE, 0x6DCDFD59, 0x608EDB80, 0x644FC637, 0x7A089632, 0x7EC98B85, 0x738AAD5C, 0x774BB0EB, 
		                 0x4F040D56, 0x4BC510E1, 0x46863638, 0x42472B8F, 0x5C007B8A, 0x58C1663D, 0x558240E4, 0x51435D53, 
		                 0x251D3B9E, 0x21DC2629, 0x2C9F00F0, 0x285E1D47, 0x36194D42, 0x32D850F5, 0x3F9B762C, 0x3B5A6B9B, 
		                 0x0315D626, 0x07D4CB91, 0x0A97ED48, 0x0E56F0FF, 0x1011A0FA, 0x14D0BD4D, 0x19939B94, 0x1D528623, 
		                 0xF12F560E, 0xF5EE4BB9, 0xF8AD6D60, 0xFC6C70D7, 0xE22B20D2, 0xE6EA3D65, 0xEBA91BBC, 0xEF68060B, 
		                 0xD727BBB6, 0xD3E6A601, 0xDEA580D8, 0xDA649D6F, 0xC423CD6A, 0xC0E2D0DD, 0xCDA1F604, 0xC960EBB3, 
		                 0xBD3E8D7E, 0xB9FF90C9, 0xB4BCB610, 0xB07DABA7, 0xAE3AFBA2, 0xAAFBE615, 0xA7B8C0CC, 0xA379DD7B, 
		                 0x9B3660C6, 0x9FF77D71, 0x92B45BA8, 0x9675461F, 0x8832161A, 0x8CF30BAD, 0x81B02D74, 0x857130C3, 
		                 0x5D8A9099, 0x594B8D2E, 0x5408ABF7, 0x50C9B640, 0x4E8EE645, 0x4A4FFBF2, 0x470CDD2B, 0x43CDC09C, 
		                 0x7B827D21, 0x7F436096, 0x7200464F, 0x76C15BF8, 0x68860BFD, 0x6C47164A, 0x61043093, 0x65C52D24, 
		                 0x119B4BE9, 0x155A565E, 0x18197087, 0x1CD86D30, 0x029F3D35, 0x065E2082, 0x0B1D065B, 0x0FDC1BEC, 
		                 0x3793A651, 0x3352BBE6, 0x3E119D3F, 0x3AD08088, 0x2497D08D, 0x2056CD3A, 0x2D15EBE3, 0x29D4F654, 
		                 0xC5A92679, 0xC1683BCE, 0xCC2B1D17, 0xC8EA00A0, 0xD6AD50A5, 0xD26C4D12, 0xDF2F6BCB, 0xDBEE767C, 
		                 0xE3A1CBC1, 0xE760D676, 0xEA23F0AF, 0xEEE2ED18, 0xF0A5BD1D, 0xF464A0AA, 0xF9278673, 0xFDE69BC4, 
		                 0x89B8FD09, 0x8D79E0BE, 0x803AC667, 0x84FBDBD0, 0x9ABC8BD5, 0x9E7D9662, 0x933EB0BB, 0x97FFAD0C, 
		                 0xAFB010B1, 0xAB710D06, 0xA6322BDF, 0xA2F33668, 0xBCB4666D, 0xB8757BDA, 0xB5365D03, 0xB1F740B4
		};
		int lTable_index;
		int lCRC32 = a_remainder;

		for (byte b : a_bytes) {
			lTable_index = ((lCRC32 >> 24) ^ b) & 0xff;
			lCRC32 = (lCRC32 << 8) ^ lTable[lTable_index];
		}
		return(lCRC32);
		}
	/**************************************************************************
	 ** Public method : CalculateCRC32                                       **
	 **************************************************************************/
	public static int CalculateCRC32(String a_filename, int a_length, int a_remainder, boolean a_F_final) throws IOException {
		int lCRC32;
		FileInputStream lFile = new FileInputStream(a_filename);
		byte[] lBytes = new byte[a_length];

		lFile.read(lBytes);
		lFile.close();
		lCRC32 = CalculateCRC32(lBytes, a_length, a_remainder);
		if (a_F_final) {
			lCRC32 ^= 0xFFFFFFFF;
		}
		else {
		}
		return(lCRC32);
		}
    /**************************************************************************
     ** Public method : AddCRC16AtEndOfFile                                  **
     **************************************************************************/
    public static int AddCRC16AtEndOfFile(String a_filename) throws IOException {
        int lCRC16;

        // Open the file in random access
        RandomAccessFile lFile = new RandomAccessFile(a_filename, "rw");
        // Read the bytes in the file
        byte[] lBytes = new byte[(int) lFile.length()];
        lFile.read(lBytes);
        // Calculate the CRC16
        lCRC16 = CalculateCRC16(lBytes, lBytes.length);
        // Write at the end of the file the CRC16
        lFile.writeChar(lCRC16);
        // Close the file
        lFile.close();
        // Return the CRC16
        return(lCRC16);
    }
    /**************************************************************************
     ** Public method : AddCRC32AtEndOfFile                                  **
     **************************************************************************/
    public static void AddCRC32AtEndOfFile(String a_filename, int a_CRC32) throws IOException {
        // Open the file in random access
        RandomAccessFile lFile = new RandomAccessFile(a_filename, "rw");
        // Read the bytes in the file
        byte[] lBytes = new byte[(int) lFile.length()];
        lFile.read(lBytes);
        // Write at the end of the file the CRC32
        lFile.writeInt(a_CRC32);
        // Close the file
        lFile.close();
    }
	/**************************************************************************
	 ** Public method : CalculateCRC64                                       **
	 **************************************************************************/
	public static long CalculateCRC64(byte[] a_bytes, int a_length) {
		int lTable_index;
		long[] lTable = {
                0x0000000000000000L, 0x42F0E1EBA9EA3693L, 0x85E1C3D753D46D26L, 0xC711223CFA3E5BB5L,
                0x493366450E42ECDFL, 0x0BC387AEA7A8DA4CL, 0xCCD2A5925D9681F9L, 0x8E224479F47CB76AL,
                0x9266CC8A1C85D9BEL, 0xD0962D61B56FEF2DL, 0x17870F5D4F51B498L, 0x5577EEB6E6BB820BL,
                0xDB55AACF12C73561L, 0x99A54B24BB2D03F2L, 0x5EB4691841135847L, 0x1C4488F3E8F96ED4L,
                0x663D78FF90E185EFL, 0x24CD9914390BB37CL, 0xE3DCBB28C335E8C9L, 0xA12C5AC36ADFDE5AL,
                0x2F0E1EBA9EA36930L, 0x6DFEFF5137495FA3L, 0xAAEFDD6DCD770416L, 0xE81F3C86649D3285L,
                0xF45BB4758C645C51L, 0xB6AB559E258E6AC2L, 0x71BA77A2DFB03177L, 0x334A9649765A07E4L,
                0xBD68D2308226B08EL, 0xFF9833DB2BCC861DL, 0x388911E7D1F2DDA8L, 0x7A79F00C7818EB3BL,
                0xCC7AF1FF21C30BDEL, 0x8E8A101488293D4DL, 0x499B3228721766F8L, 0x0B6BD3C3DBFD506BL,
                0x854997BA2F81E701L, 0xC7B97651866BD192L, 0x00A8546D7C558A27L, 0x4258B586D5BFBCB4L,
                0x5E1C3D753D46D260L, 0x1CECDC9E94ACE4F3L, 0xDBFDFEA26E92BF46L, 0x990D1F49C77889D5L,
                0x172F5B3033043EBFL, 0x55DFBADB9AEE082CL, 0x92CE98E760D05399L, 0xD03E790CC93A650AL,
                0xAA478900B1228E31L, 0xE8B768EB18C8B8A2L, 0x2FA64AD7E2F6E317L, 0x6D56AB3C4B1CD584L,
                0xE374EF45BF6062EEL, 0xA1840EAE168A547DL, 0x66952C92ECB40FC8L, 0x2465CD79455E395BL,
                0x3821458AADA7578FL, 0x7AD1A461044D611CL, 0xBDC0865DFE733AA9L, 0xFF3067B657990C3AL,
                0x711223CFA3E5BB50L, 0x33E2C2240A0F8DC3L, 0xF4F3E018F031D676L, 0xB60301F359DBE0E5L,
                0xDA050215EA6C212FL, 0x98F5E3FE438617BCL, 0x5FE4C1C2B9B84C09L, 0x1D14202910527A9AL,
                0x93366450E42ECDF0L, 0xD1C685BB4DC4FB63L, 0x16D7A787B7FAA0D6L, 0x5427466C1E109645L,
                0x4863CE9FF6E9F891L, 0x0A932F745F03CE02L, 0xCD820D48A53D95B7L, 0x8F72ECA30CD7A324L,
                0x0150A8DAF8AB144EL, 0x43A04931514122DDL, 0x84B16B0DAB7F7968L, 0xC6418AE602954FFBL,
                0xBC387AEA7A8DA4C0L, 0xFEC89B01D3679253L, 0x39D9B93D2959C9E6L, 0x7B2958D680B3FF75L,
                0xF50B1CAF74CF481FL, 0xB7FBFD44DD257E8CL, 0x70EADF78271B2539L, 0x321A3E938EF113AAL,
                0x2E5EB66066087D7EL, 0x6CAE578BCFE24BEDL, 0xABBF75B735DC1058L, 0xE94F945C9C3626CBL,
                0x676DD025684A91A1L, 0x259D31CEC1A0A732L, 0xE28C13F23B9EFC87L, 0xA07CF2199274CA14L,
                0x167FF3EACBAF2AF1L, 0x548F120162451C62L, 0x939E303D987B47D7L, 0xD16ED1D631917144L,
                0x5F4C95AFC5EDC62EL, 0x1DBC74446C07F0BDL, 0xDAAD56789639AB08L, 0x985DB7933FD39D9BL,
                0x84193F60D72AF34FL, 0xC6E9DE8B7EC0C5DCL, 0x01F8FCB784FE9E69L, 0x43081D5C2D14A8FAL,
                0xCD2A5925D9681F90L, 0x8FDAB8CE70822903L, 0x48CB9AF28ABC72B6L, 0x0A3B7B1923564425L,
                0x70428B155B4EAF1EL, 0x32B26AFEF2A4998DL, 0xF5A348C2089AC238L, 0xB753A929A170F4ABL,
                0x3971ED50550C43C1L, 0x7B810CBBFCE67552L, 0xBC902E8706D82EE7L, 0xFE60CF6CAF321874L,
                0xE224479F47CB76A0L, 0xA0D4A674EE214033L, 0x67C58448141F1B86L, 0x253565A3BDF52D15L,
                0xAB1721DA49899A7FL, 0xE9E7C031E063ACECL, 0x2EF6E20D1A5DF759L, 0x6C0603E6B3B7C1CAL,
                0xF6FAE5C07D3274CDL, 0xB40A042BD4D8425EL, 0x731B26172EE619EBL, 0x31EBC7FC870C2F78L,
                0xBFC9838573709812L, 0xFD39626EDA9AAE81L, 0x3A28405220A4F534L, 0x78D8A1B9894EC3A7L,
                0x649C294A61B7AD73L, 0x266CC8A1C85D9BE0L, 0xE17DEA9D3263C055L, 0xA38D0B769B89F6C6L,
                0x2DAF4F0F6FF541ACL, 0x6F5FAEE4C61F773FL, 0xA84E8CD83C212C8AL, 0xEABE6D3395CB1A19L,
                0x90C79D3FEDD3F122L, 0xD2377CD44439C7B1L, 0x15265EE8BE079C04L, 0x57D6BF0317EDAA97L,
                0xD9F4FB7AE3911DFDL, 0x9B041A914A7B2B6EL, 0x5C1538ADB04570DBL, 0x1EE5D94619AF4648L,
                0x02A151B5F156289CL, 0x4051B05E58BC1E0FL, 0x87409262A28245BAL, 0xC5B073890B687329L,
                0x4B9237F0FF14C443L, 0x0962D61B56FEF2D0L, 0xCE73F427ACC0A965L, 0x8C8315CC052A9FF6L,
                0x3A80143F5CF17F13L, 0x7870F5D4F51B4980L, 0xBF61D7E80F251235L, 0xFD913603A6CF24A6L,
                0x73B3727A52B393CCL, 0x31439391FB59A55FL, 0xF652B1AD0167FEEAL, 0xB4A25046A88DC879L,
                0xA8E6D8B54074A6ADL, 0xEA16395EE99E903EL, 0x2D071B6213A0CB8BL, 0x6FF7FA89BA4AFD18L,
                0xE1D5BEF04E364A72L, 0xA3255F1BE7DC7CE1L, 0x64347D271DE22754L, 0x26C49CCCB40811C7L,
                0x5CBD6CC0CC10FAFCL, 0x1E4D8D2B65FACC6FL, 0xD95CAF179FC497DAL, 0x9BAC4EFC362EA149L,
                0x158E0A85C2521623L, 0x577EEB6E6BB820B0L, 0x906FC95291867B05L, 0xD29F28B9386C4D96L,
                0xCEDBA04AD0952342L, 0x8C2B41A1797F15D1L, 0x4B3A639D83414E64L, 0x09CA82762AAB78F7L,
                0x87E8C60FDED7CF9DL, 0xC51827E4773DF90EL, 0x020905D88D03A2BBL, 0x40F9E43324E99428L,
                0x2CFFE7D5975E55E2L, 0x6E0F063E3EB46371L, 0xA91E2402C48A38C4L, 0xEBEEC5E96D600E57L,
                0x65CC8190991CB93DL, 0x273C607B30F68FAEL, 0xE02D4247CAC8D41BL, 0xA2DDA3AC6322E288L,
                0xBE992B5F8BDB8C5CL, 0xFC69CAB42231BACFL, 0x3B78E888D80FE17AL, 0x7988096371E5D7E9L,
                0xF7AA4D1A85996083L, 0xB55AACF12C735610L, 0x724B8ECDD64D0DA5L, 0x30BB6F267FA73B36L,
                0x4AC29F2A07BFD00DL, 0x08327EC1AE55E69EL, 0xCF235CFD546BBD2BL, 0x8DD3BD16FD818BB8L,
                0x03F1F96F09FD3CD2L, 0x41011884A0170A41L, 0x86103AB85A2951F4L, 0xC4E0DB53F3C36767L,
                0xD8A453A01B3A09B3L, 0x9A54B24BB2D03F20L, 0x5D45907748EE6495L, 0x1FB5719CE1045206L,
                0x919735E51578E56CL, 0xD367D40EBC92D3FFL, 0x1476F63246AC884AL, 0x568617D9EF46BED9L,
                0xE085162AB69D5E3CL, 0xA275F7C11F7768AFL, 0x6564D5FDE549331AL, 0x279434164CA30589L,
                0xA9B6706FB8DFB2E3L, 0xEB46918411358470L, 0x2C57B3B8EB0BDFC5L, 0x6EA7525342E1E956L,
                0x72E3DAA0AA188782L, 0x30133B4B03F2B111L, 0xF7021977F9CCEAA4L, 0xB5F2F89C5026DC37L,
                0x3BD0BCE5A45A6B5DL, 0x79205D0E0DB05DCEL, 0xBE317F32F78E067BL, 0xFCC19ED95E6430E8L,
                0x86B86ED5267CDBD3L, 0xC4488F3E8F96ED40L, 0x0359AD0275A8B6F5L, 0x41A94CE9DC428066L,
                0xCF8B0890283E370CL, 0x8D7BE97B81D4019FL, 0x4A6ACB477BEA5A2AL, 0x089A2AACD2006CB9L,
                0x14DEA25F3AF9026DL, 0x562E43B4931334FEL, 0x913F6188692D6F4BL, 0xD3CF8063C0C759D8L,
                0x5DEDC41A34BBEEB2L, 0x1F1D25F19D51D821L, 0xD80C07CD676F8394L, 0x9AFCE626CE85B507L
	    };
		long lCRC64 = 0xFFFFFFFFFFFFFFFFL;
	    for (byte b : a_bytes) {
	        lTable_index = (int) (((lCRC64 >> (24+32)) ^ (long)b) & 0xFF);
	        lCRC64 = (lCRC64 << 8) ^ lTable[lTable_index];
	    }
		return(lCRC64 ^ 0xFFFFFFFFFFFFFFFFL);
	}
	/**************************************************************************
     ** Public method : CalculateMD5                                         **
     **************************************************************************/
    public static byte[] CalculateMD5(byte[] a_bytes) throws NoSuchAlgorithmException {
    	MessageDigest lMD;

    	lMD = MessageDigest.getInstance("MD5");
    	return(lMD.digest(a_bytes));
    }
    /**************************************************************************
     ** Public method : CalculateSHA1                                        **
     **************************************************************************/
    public static byte[] CalculateSHA1(byte[] a_bytes) throws NoSuchAlgorithmException {
    	MessageDigest lMD;

    	lMD = MessageDigest.getInstance("SHA-1");
    	return(lMD.digest(a_bytes));
    }
}
