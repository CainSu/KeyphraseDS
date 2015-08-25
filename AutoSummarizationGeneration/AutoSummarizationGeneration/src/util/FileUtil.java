package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * @author Deathy Young 2013-3-25
 */
public class FileUtil {
	private static FileReader fr;
	private static FileOutputStream fos;
	private static FileInputStream fis;

	public static boolean mkdir(String dirPath) {
		File dirFile = new File(dirPath);
		if (!dirFile.isDirectory()) {
			return dirFile.mkdir();
		}
		return true;
	}

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param content
	 */
	public static boolean addToFile(String content, String targetFilePath) {
		File targetFile = new File(targetFilePath);
		return addToFile(content, targetFile);
	}
	
	public static boolean isExist(String filePath) {
	    String paths[] = filePath.split("\\\\");
	    String dir = paths[0];
	    for (int i = 0; i < paths.length - 2; i++) {
	        try {
	            dir = dir + "/" + paths[i + 1];
	            File dirFile = new File(dir);
	            if (!dirFile.exists()) {
	                dirFile.mkdir();
	                System.out.println("����Ŀ¼Ϊ��" + dir);
	            }
	        } catch (Exception err) {
	            System.err.println("ELS - Chart : �ļ��д��������쳣");
	        }
	    }
	    File fp = new File(filePath);
	    if(!fp.exists()){
	        return true; // �ļ������ڣ�ִ�����ع���
	    }else{
	        return false; // �ļ����ڲ�������
	    }
	}

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param content
	 */
	public static boolean addToFileln(String content, String targetFilePath) {
		File targetFile = new File(targetFilePath);
		return addToFileln(content, targetFile);
	}

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param contents
	 */
	public static boolean addToFile(String[] contents, String targetFilePath) {
		File targetFile = new File(targetFilePath);
		return addToFile(contents, targetFile);
	}

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param contents
	 */
	public static boolean addToFileln(String[] contents, String targetFilePath) {
		File targetFile = new File(targetFilePath);
		return addToFileln(contents, targetFile);
	}

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param content
	 * @param targetFile
	 *            Ŀ���ļ�
	 */
	public static boolean addToFile(String content, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// ��content�ַ�����Ϣд���ļ�
			fw.write(content);
			// �ر���
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param content
	 * @param targetFile
	 *            Ŀ���ļ�
	 */
	public static boolean addToFileln(String content, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// ��content�ַ�����Ϣд���ļ�
			fw.write(content);
			fw.write("\n");
			// �ر���
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param contents
	 * @param targetFile
	 *            Ŀ���ļ�
	 */
	public static boolean addToFile(String[] contents, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// ��content�ַ�����Ϣд���ļ�
			for (int i = 0; i < contents.length; i++) {
				fw.write(contents[i]);
			}
			// �ر���
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ����ַ�����ָ�����ļ�
	 * 
	 * @param contents
	 * @param targetFile
	 *            Ŀ���ļ�
	 */
	public static boolean addToFileln(String[] contents, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// ��content�ַ�����Ϣд���ļ�
			for (int i = 0; i < contents.length; i++) {
				fw.write(contents[i]);
				fw.write("\n");
			}
			// �ر���
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Title: getString
	 * 
	 * @see ���ļ����ַ�����ʽ��ȡ����
	 * @param file
	 *            �ı��ļ�
	 * @return �ı��ַ���
	 */
	public static String getString(File file) {
		return getString(file, 0, Integer.MAX_VALUE);
	}

	/**
	 * Title: getString
	 * 
	 * @see ���ļ����ַ�����ʽ��ȡ����
	 * @param filePath
	 *            �ı��ļ�·��
	 * @return �ı��ַ���
	 */
	public static String getString(String filePath) {
		return getString(filePath, 0, Integer.MAX_VALUE);
	}

	/**
	 * ���ļ����ַ�����ʽ��ȡ����
	 * 
	 * @param filePath
	 *            �ı��ļ�·��
	 * @param beginIndex
	 *            ��ʼλ���±꣨�±��0�ƣ�
	 * @return String �ı��ַ���
	 */
	public static String getString(String filePath, int beginIndex) {
		return getString(filePath, beginIndex, Integer.MAX_VALUE);
	}

	/**
	 * ���ļ����ַ�����ʽ��ȡ����
	 * 
	 * @param filePath
	 *            �ı��ļ�·��
	 * @param beginIndex
	 *            ��ʼλ���±꣨�±��0�ƣ�
	 * @param endIndex
	 *            ����λ���±꣬�����������±꣨�±��0�ƣ�
	 * @return String �ı��ַ���
	 */
	public static String getString(String filePath, int begin, int end) {
		File textFile = new File(filePath);
		return getString(textFile, begin, end);
	}

	/**
	 * ���ļ����ַ�����ʽ��ȡ����
	 * 
	 * @param filePath
	 *            �ı��ļ�·��
	 * @param beginIndex
	 *            ��ʼλ���±꣨�±��0�ƣ�
	 * @param endIndex
	 *            ����λ���±꣬�����������±꣨�±��0�ƣ�
	 * @return String �ı��ַ���
	 */
	public static String getString(File textFile, int begin, int end) {
		StringBuffer sb = new StringBuffer();
		try {
			fr = new FileReader(textFile);
			fr.skip(begin);
			int charSize = 1024;
			// ����һ���ַ����飬�û��洢�ļ��е��ַ�
			char[] cs = new char[charSize];
			// ��ʵ��ȡ�����ļ��ַ���
			int length = -1;
			int i = 0;
			while ((length = fr.read(cs)) > 0) {
				// ѭ����������е��ַ�
				for (int j = 0; j < length && i < end - begin; i++,j++) {
					sb.append(cs[j]);
				}
			}
			return new String(sb);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ȷ��ָ��·���ļ�������Ϊ�µĿ��ļ�
	 * 
	 * @param filePath
	 *            ָ��·���ļ�
	 * @return boolean �Ƿ񴴽��ɹ�
	 */
	public static boolean createNewFileForce(String filePath) {
		return createNewFileForce(filePath, 0);
	}

	/**
	 * ȷ��ָ��·���ļ�������Ϊ�µĿ��ļ�
	 * 
	 * @param filePath
	 *            ָ���ļ�·��
	 * @param fileLength
	 *            ָ���ļ���С����λKB
	 * @return �Ƿ񴴽��ɹ�
	 */
	public static boolean createNewFileForce(String filePath, int fileLength) {
		if (!createDirForce(filePath)) {
			return false;
		}
		File newFile = new File(filePath);
		if (newFile.exists()) {
			newFile.delete();
		}
		try {
			FileWriter fw = new FileWriter(newFile, true);
			fos = new FileOutputStream(newFile);

			for (int i = 0; i < fileLength / 4096; i++) {
				byte[] buffer = new byte[4096 * 1024]; // 1��4M�������ڴ濪�Ĵ�һЩ���ֲ����ر��
				fos.write(buffer);
			}
			fos.write(new byte[fileLength % 4096 * 1024]);

			// �ر���
			fw.close();
			return newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ɾ���ļ���
	 * 
	 * @param folderPath
	 *            �ļ�����������·��
	 */
	public static boolean delFolder(String folderPath) {
		boolean flag = true;
		try {
			flag = delAllFile(folderPath); // ɾ����������������
			if (flag) {
				String filePath = folderPath;
				filePath = filePath.toString();
				File myFilePath = new File(filePath);
				flag = myFilePath.delete(); // ɾ�����ļ���
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * ɾ��ָ���ļ����������ļ�
	 * 
	 * @param path
	 *            �ļ�����������·��
	 * @return boolean
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(path + "/" + tempList[i]);// ��ɾ�����ļ���
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * �ļ���С���
	 * 
	 * @param f
	 *            �������ļ�,Ĭ�ϴ�С2048KB
	 * @return ����ֵtrue��ʾ��֤�ļ���Сͨ��������ֵfalse��ʾ��֤�ļ���С��ͨ��,����FileΪnull
	 */
	public static boolean checkMaxSize(File f) {
		return checkMaxSize(f, 2048);
	}

	/**
	 * �ļ���С���
	 * 
	 * @param f
	 *            �������ļ�
	 * @param maxSize
	 *            ����ֵ��λ(KB)
	 * @return ����ֵtrue��ʾ��֤�ļ���Сͨ��������ֵfalse��ʾ��֤�ļ���С��ͨ��,����FileΪnull
	 */
	public static boolean checkMaxSize(File f, int maxSize) {
		boolean re = false;
		if (f == null) {
			return re;
		}
		try {
			fis = new FileInputStream(f);
			// ǧ�ֽڴ�С
			long fileSize = fis.available() / 1024;
			if (fileSize >= maxSize) {
				re = false;
			} else {
				re = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	/**
	 * Ĭ��ͼƬ�ļ����ͼ��
	 * 
	 * @param contentType
	 *            �ļ�����
	 * @return �Ƿ��������ļ�����
	 */
	public static boolean checkContentType(String contentType) {
		Set<String> types = new HashSet<String>();
		types.add("image/jpeg");
		types.add("image/png");
		types.add("image/gif");
		return checkContentType(contentType, types);
	}

	/**
	 * �ļ����ͼ��
	 * 
	 * @param contentType
	 *            �ļ�����
	 * @param types
	 *            ������ļ����ͼ���
	 * @return �Ƿ��������ļ�����
	 */
	public static boolean checkContentType(String contentType, Set<String> types) {
		if (types == null || types.size() == 0) {
			return checkContentType(contentType);
		}
		return types.contains(contentType);
	}

	/**
	 * ǿ��Ϊ�ļ��л��ļ������ļ���
	 * 
	 * @param path
	 *            �ļ��л��ļ�·��
	 * @return �Ƿ�ɹ�����
	 */
	public static boolean createDirForce(String path) {
		if (File.separator.equals("\\")) {
			path = path.replace('/', File.separatorChar);
		} else {
			path = path.replace('\\', File.separatorChar);
		}
		int last = path.lastIndexOf(File.separatorChar);
		if (last == -1) {
			return true;
		}
		path = path.substring(0, last);
		File file = new File(path);
		boolean flag = createDirForce(path);
		if (path.length() != 0 && flag && !file.isDirectory()) {
			flag = file.mkdir();
		}
		return flag;
	}

	/**
	 * �鿴ָ��·�����Ƿ���ڸ��ļ�
	 * 
	 * @param path
	 *            �ļ�·��
	 * @return
	 */
	public static boolean exists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * �����ļ��м������ļ��к��ļ�
	 * 
	 * @param src
	 *            Դ�ļ���·��
	 * @param des
	 *            Ŀ���ļ���·��
	 * @return ���ƽ��
	 */
	public static boolean copyDirResources(String src, String des) {
		createDirForce(src);
		createDirForce(des);
		// ��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼
		File[] file = (new File(src)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// �����ļ�
				try {
					copyFile(file[i],
							new File(des + File.separator + file[i].getName()));
				} catch (IOException e) {
					System.out.println("�����ļ�ʧ������·���Ƿ���ȷ!");
					e.printStackTrace();
					return false;
				}
			}
			if (file[i].isDirectory()) {
				// ����Ŀ¼
				String sourceDir = src + file[i].getName() + File.separator;
				String targetDir = des + file[i].getName() + File.separator;
				try {
					copyDirectiory(sourceDir, targetDir);
				} catch (IOException e) {
					System.out.println("�����ļ���ʧ������·���Ƿ���ȷ!");
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �����ļ�
	 * 
	 * @param sourceFile
	 *            Դ�ļ�
	 * @param targetFile
	 *            Ŀ���ļ�
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// �½��ļ����������������л���
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// �½��ļ���������������л���
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// ��������
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// ˢ�´˻���������
		outBuff.flush();

		// �ر���
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	/**
	 * �����ļ���
	 * 
	 * @param sourceDir
	 *            Դ�ļ���
	 * @param targetDir
	 *            Ŀ���ļ���
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// Ŀ��Ŀ¼
		createDirForce(targetDir);
		// ��ȡԴ�ļ��е�ǰ�µ��ļ���Ŀ¼
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// Դ�ļ�
				File sourceFile = file[i];
				// Ŀ���ļ�
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// ׼�����Ƶ�Դ�ļ���
				String dir1 = sourceDir + file[i].getName() + File.separator;
				// ׼�����Ƶ�Ŀ���ļ���
				String dir2 = targetDir + file[i].getName() + File.separator;
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * ����ָ������
	 * 
	 * @param br
	 * @param linesNum
	 *            ����
	 * @return void
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public static void skipLine(BufferedReader br, int linesNum)
			throws IOException {
		while (linesNum-- > 0) {
			br.readLine();
		}
	}

	/**
	 * ��ȡ��һ���ı�
	 * 
	 * @param in
	 * @return String
	 */
	public static String getLine(File in) {
		return getLine(in, 0);
	}

	/**
	 * ��ȡ��lineNum���ı�
	 * 
	 * @param br
	 * @param lineNum
	 * @throws IOException
	 * @return String
	 */
	public static String getLine(BufferedReader br, int lineNum)
			throws IOException {
		skipLine(br, lineNum);
		return br.readLine();
	}

	/**
	 * ��ȡ��lineNum���ı�
	 * 
	 * @param in
	 * @param lineNum
	 * @throws IOException
	 * @return String
	 */
	public static String getLine(File in, int lineNum) {
		FileReader reader = null;
		String str = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(in);
			br = new BufferedReader(reader);
			str = getLine(br, lineNum);
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * ��ȡȫ���ı�
	 * 
	 * @param br
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(BufferedReader br) throws IOException {
		List<String> strList = new LinkedList<String>();
		String temp;
		while ((temp = br.readLine()) != null) {
			if(temp.isEmpty() || temp.trim().equals(""))
				continue;
			strList.add(temp);
		}
		String[] strs = strList.toArray(new String[] {});
		return strs;
	}

	/**
	 * ��ȡȫ���ı�
	 * 
	 * @param in
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(File in) {
		FileReader reader = null;
		String[] strs = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(in);
			br = new BufferedReader(reader);
			strs = getLines(br);
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * ��ȡȫ���ı�
	 * 
	 * @param filePath
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(String filePath) {
		return getLines(new File(filePath));
	}

	/**
	 * ��ȡǰN���ı�
	 * 
	 * @param br
	 * @param topN
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(BufferedReader br, int topN)
			throws IOException {
		String[] strs = new String[topN];
		for (int i = 0; i < topN; i++) {
			strs[i] = br.readLine();
		}
		return strs;
	}

	/**
	 * ��ȡǰN���ı�
	 * 
	 * @param in
	 * @param topN
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(File in, int topN) {
		FileReader reader = null;
		String[] str = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(in);
			br = new BufferedReader(reader);
			str = getLines(br, topN);
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * ��ȡǰN���ı�
	 * 
	 * @param filePath
	 * @param topN
	 * @return String[]
	 */
	public static String[] getLines(String filePath, int topN) {
		return getLines(new File(filePath), topN);
	}

	/**
	 * ��ȡ��beginLine�е�endLine�е��ı���������endLine�У�
	 * 
	 * @param br
	 * @param beginLine
	 * @param endLine
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(BufferedReader br, int beginLine,
			int endLine) throws IOException {
		String[] strs = new String[endLine - beginLine];
		skipLine(br, beginLine - 1);
		for (int i = 0; i < endLine - beginLine; i++) {
			strs[i] = br.readLine();
		}
		return strs;
	}

	/**
	 * ��ȡ��beginLine�е�endLine�е��ı���������endLine�У�
	 * 
	 * @param in
	 * @param beginLine
	 * @param endLine
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(File in, int beginLine, int endLine) {
		FileReader reader = null;
		String[] str = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(in);
			br = new BufferedReader(reader);
			str = getLines(br, beginLine, endLine);
			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * ��ȡ��beginLine�е�endLine�е��ı���������endLine�У�
	 * 
	 * @param filePath
	 * @param beginLine
	 * @param endLine
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(String filePath, int beginLine, int endLine) {
		return getLines(new File(filePath), beginLine, endLine);
	}

	/**
	 * <P>
	 * TODO
	 * </P>
	 * 
	 * @param path
	 * @return long
	 */
	public static int getLinesNum(String path) {
		return getLinesNum(new File(path));
	}

	/**
	 * <P>
	 * TODO
	 * </P>
	 * 
	 * @param f
	 * @return long
	 */
	public static int getLinesNum(File f) {
		FileReader reader;
		try {
			reader = new FileReader(f);
			return getLinesNum(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * <P>
	 * TODO
	 * </P>
	 * 
	 * @param reader
	 * @return long
	 */
	public static int getLinesNum(FileReader reader) {
		BufferedReader br = new BufferedReader(reader);
		return getLinesNum(br);
	}

	/**
	 * <P>
	 * TODO
	 * </P>
	 * 
	 * @param br
	 * @return long
	 */
	public static int getLinesNum(BufferedReader br) {
		int num = 0;
		try {
			while ((br.readLine()) != null) {
				num++;
			}
		} catch (IOException e) {
			num = -1;
			e.printStackTrace();
		}
		return num;
	}
}