package name_battle;

import name_battle.attribute.AttributeBook;

public class Estimation {
	public String name;
	private String md5;
	private int power_level;
	
	public int estimate_name(String _name,AttributeBook attribute_list){
		name=_name;
		md5=getMD5(name.getBytes());		

		int strength=0;
		//generate attributes
		for(int counter=0;counter<attribute_list.attributes.size();counter++){
			String attribute_seed=md5.substring(counter*2, counter*2+2);
			int attribute_data=14+(Integer.decode("0x"+attribute_seed))%87;
			strength+=attribute_data;
			
		}
		
		power_level=strength/attribute_list.attributes.size();
		return power_level;
	}
	
	

	private static String getMD5(byte[] source)
	{
	String s = null;
	char hexDigits[] =
	{ // �������ֽ�ת���� 16 ���Ʊ�ʾ���ַ�
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
	'c', 'd', 'e', 'f'
	};
	try
	{
	java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	md.update(source);
	// MD5 �ļ�������һ�� 128 λ�ĳ���������
	byte tmp[] = md.digest();
	// ���ֽڱ�ʾ���� 16 ���ֽ�
	char str[] = new char[16 * 2]; // ÿ���ֽ��� 16 ���Ʊ�ʾ�Ļ���ʹ�������ַ���
	// ���Ա�ʾ�� 16 ������Ҫ 32 ���ַ�
	int k = 0; // ��ʾת������ж�Ӧ���ַ�λ��
	for (int i = 0; i < 16; i++)
	{ // �ӵ�һ���ֽڿ�ʼ���� MD5 ��ÿһ���ֽ�
	// ת���� 16 �����ַ���ת��
	byte byte0 = tmp[i]; // ȡ�� i ���ֽ�
	str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // ȡ�ֽ��и� 4 λ������ת��,
	// >>> Ϊ�߼����ƣ����޷������ƣ���������λһ������

	// ȡ�ֽ��е� 4 λ������ת��
	str[k++] = hexDigits[byte0 & 0xf];
	}
	s = new String(str); // ����Ľ��ת��Ϊ�ַ���

	}
	catch (Exception e)
	{
	e.printStackTrace();
	}
	return s;
	
}
	
}
