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
	 * 添加字符串到指定的文件
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
	                System.out.println("创建目录为：" + dir);
	            }
	        } catch (Exception err) {
	            System.err.println("ELS - Chart : 文件夹创建发生异常");
	        }
	    }
	    File fp = new File(filePath);
	    if(!fp.exists()){
	        return true; // 文件不存在，执行下载功能
	    }else{
	        return false; // 文件存在不做处理
	    }
	}

	/**
	 * 添加字符串到指定的文件
	 * 
	 * @param content
	 */
	public static boolean addToFileln(String content, String targetFilePath) {
		File targetFile = new File(targetFilePath);
		return addToFileln(content, targetFile);
	}

	/**
	 * 添加字符串到指定的文件
	 * 
	 * @param contents
	 */
	public static boolean addToFile(String[] contents, String targetFilePath) {
		File targetFile = new File(targetFilePath);
		return addToFile(contents, targetFile);
	}

	/**
	 * 添加字符串到指定的文件
	 * 
	 * @param contents
	 */
	public static boolean addToFileln(String[] contents, String targetFilePath) {
		File targetFile = new File(targetFilePath);
		return addToFileln(contents, targetFile);
	}

	/**
	 * 添加字符串到指定的文件
	 * 
	 * @param content
	 * @param targetFile
	 *            目标文件
	 */
	public static boolean addToFile(String content, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// 将content字符串信息写入文件
			fw.write(content);
			// 关闭流
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
	 * 添加字符串到指定的文件
	 * 
	 * @param content
	 * @param targetFile
	 *            目标文件
	 */
	public static boolean addToFileln(String content, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// 将content字符串信息写入文件
			fw.write(content);
			fw.write("\n");
			// 关闭流
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
	 * 添加字符串到指定的文件
	 * 
	 * @param contents
	 * @param targetFile
	 *            目标文件
	 */
	public static boolean addToFile(String[] contents, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// 将content字符串信息写入文件
			for (int i = 0; i < contents.length; i++) {
				fw.write(contents[i]);
			}
			// 关闭流
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
	 * 添加字符串到指定的文件
	 * 
	 * @param contents
	 * @param targetFile
	 *            目标文件
	 */
	public static boolean addToFileln(String[] contents, File targetFile) {
		try {
			FileWriter fw = new FileWriter(targetFile, true);
			// 将content字符串信息写入文件
			for (int i = 0; i < contents.length; i++) {
				fw.write(contents[i]);
				fw.write("\n");
			}
			// 关闭流
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
	 * @see 从文件以字符串形式独取出来
	 * @param file
	 *            文本文件
	 * @return 文本字符串
	 */
	public static String getString(File file) {
		return getString(file, 0, Integer.MAX_VALUE);
	}

	/**
	 * Title: getString
	 * 
	 * @see 从文件以字符串形式独取出来
	 * @param filePath
	 *            文本文件路径
	 * @return 文本字符串
	 */
	public static String getString(String filePath) {
		return getString(filePath, 0, Integer.MAX_VALUE);
	}

	/**
	 * 从文件以字符串形式独取出来
	 * 
	 * @param filePath
	 *            文本文件路径
	 * @param beginIndex
	 *            起始位置下标（下标从0计）
	 * @return String 文本字符串
	 */
	public static String getString(String filePath, int beginIndex) {
		return getString(filePath, beginIndex, Integer.MAX_VALUE);
	}

	/**
	 * 从文件以字符串形式独取出来
	 * 
	 * @param filePath
	 *            文本文件路径
	 * @param beginIndex
	 *            起始位置下标（下标从0计）
	 * @param endIndex
	 *            结束位置下标，但不包含该下标（下标从0计）
	 * @return String 文本字符串
	 */
	public static String getString(String filePath, int begin, int end) {
		File textFile = new File(filePath);
		return getString(textFile, begin, end);
	}

	/**
	 * 从文件以字符串形式独取出来
	 * 
	 * @param filePath
	 *            文本文件路径
	 * @param beginIndex
	 *            起始位置下标（下标从0计）
	 * @param endIndex
	 *            结束位置下标，但不包含该下标（下标从0计）
	 * @return String 文本字符串
	 */
	public static String getString(File textFile, int begin, int end) {
		StringBuffer sb = new StringBuffer();
		try {
			fr = new FileReader(textFile);
			fr.skip(begin);
			int charSize = 1024;
			// 生命一个字符数组，用户存储文件中的字符
			char[] cs = new char[charSize];
			// 真实获取到的文件字符数
			int length = -1;
			int i = 0;
			while ((length = fr.read(cs)) > 0) {
				// 循环输出数组中的字符
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
	 * 确保指定路径文件存在且为新的空文件
	 * 
	 * @param filePath
	 *            指定路径文件
	 * @return boolean 是否创建成功
	 */
	public static boolean createNewFileForce(String filePath) {
		return createNewFileForce(filePath, 0);
	}

	/**
	 * 确保指定路径文件存在且为新的空文件
	 * 
	 * @param filePath
	 *            指定文件路径
	 * @param fileLength
	 *            指定文件大小，单位KB
	 * @return 是否创建成功
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
				byte[] buffer = new byte[4096 * 1024]; // 1次4M，这样内存开的大一些，又不是特别大。
				fos.write(buffer);
			}
			fos.write(new byte[fileLength % 4096 * 1024]);

			// 关闭流
			fw.close();
			return newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 */
	public static boolean delFolder(String folderPath) {
		boolean flag = true;
		try {
			flag = delAllFile(folderPath); // 删除完里面所有内容
			if (flag) {
				String filePath = folderPath;
				filePath = filePath.toString();
				File myFilePath = new File(filePath);
				flag = myFilePath.delete(); // 删除空文件夹
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
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
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 文件大小检查
	 * 
	 * @param f
	 *            被检查的文件,默认大小2048KB
	 * @return 返回值true表示验证文件大小通过，返回值false表示验证文件大小不通过,或者File为null
	 */
	public static boolean checkMaxSize(File f) {
		return checkMaxSize(f, 2048);
	}

	/**
	 * 文件大小检查
	 * 
	 * @param f
	 *            被检查的文件
	 * @param maxSize
	 *            参数值单位(KB)
	 * @return 返回值true表示验证文件大小通过，返回值false表示验证文件大小不通过,或者File为null
	 */
	public static boolean checkMaxSize(File f, int maxSize) {
		boolean re = false;
		if (f == null) {
			return re;
		}
		try {
			fis = new FileInputStream(f);
			// 千字节大小
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
	 * 默认图片文件类型检查
	 * 
	 * @param contentType
	 *            文件类型
	 * @return 是否是允许文件类型
	 */
	public static boolean checkContentType(String contentType) {
		Set<String> types = new HashSet<String>();
		types.add("image/jpeg");
		types.add("image/png");
		types.add("image/gif");
		return checkContentType(contentType, types);
	}

	/**
	 * 文件类型检查
	 * 
	 * @param contentType
	 *            文件类型
	 * @param types
	 *            允许的文件类型集合
	 * @return 是否是允许文件类型
	 */
	public static boolean checkContentType(String contentType, Set<String> types) {
		if (types == null || types.size() == 0) {
			return checkContentType(contentType);
		}
		return types.contains(contentType);
	}

	/**
	 * 强制为文件夹或文件创建文件夹
	 * 
	 * @param path
	 *            文件夹或文件路径
	 * @return 是否成功创建
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
	 * 查看指定路径下是否存在该文件
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	public static boolean exists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 复制文件夹及其子文件夹和文件
	 * 
	 * @param src
	 *            源文件夹路径
	 * @param des
	 *            目的文件夹路径
	 * @return 复制结果
	 */
	public static boolean copyDirResources(String src, String des) {
		createDirForce(src);
		createDirForce(des);
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(src)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 复制文件
				try {
					copyFile(file[i],
							new File(des + File.separator + file[i].getName()));
				} catch (IOException e) {
					System.out.println("复制文件失败请检查路径是否正确!");
					e.printStackTrace();
					return false;
				}
			}
			if (file[i].isDirectory()) {
				// 复制目录
				String sourceDir = src + file[i].getName() + File.separator;
				String targetDir = des + file[i].getName() + File.separator;
				try {
					copyDirectiory(sourceDir, targetDir);
				} catch (IOException e) {
					System.out.println("复制文件夹失败请检查路径是否正确!");
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	/**
	 * 复制文件夹
	 * 
	 * @param sourceDir
	 *            源文件夹
	 * @param targetDir
	 *            目标文件夹
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 目标目录
		createDirForce(targetDir);
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + file[i].getName() + File.separator;
				// 准备复制的目标文件夹
				String dir2 = targetDir + file[i].getName() + File.separator;
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * 跳过指定行数
	 * 
	 * @param br
	 * @param linesNum
	 *            行数
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
	 * 读取第一行文本
	 * 
	 * @param in
	 * @return String
	 */
	public static String getLine(File in) {
		return getLine(in, 0);
	}

	/**
	 * 读取第lineNum行文本
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
	 * 读取第lineNum行文本
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
	 * 读取全部文本
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
	 * 读取全部文本
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
	 * 读取全部文本
	 * 
	 * @param filePath
	 * @throws IOException
	 * @return String[]
	 */
	public static String[] getLines(String filePath) {
		return getLines(new File(filePath));
	}

	/**
	 * 读取前N行文本
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
	 * 读取前N行文本
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
	 * 读取前N行文本
	 * 
	 * @param filePath
	 * @param topN
	 * @return String[]
	 */
	public static String[] getLines(String filePath, int topN) {
		return getLines(new File(filePath), topN);
	}

	/**
	 * 读取从beginLine行到endLine行的文本（不包含endLine行）
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
	 * 读取从beginLine行到endLine行的文本（不包含endLine行）
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
	 * 读取从beginLine行到endLine行的文本（不包含endLine行）
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