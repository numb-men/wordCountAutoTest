import java.io.*;
import java.util.*;

public class Main{

    // 行数
    static int lineNum = 0;

    // 字符数
    static int charNum = 0;

    // 单词数：至少四个英文字母开头，不区分大小写
    static int wordNum = 0;

    // 排序好的单词集合
    static List<Map.Entry<String, Integer>> wordList = null;

    // 输入文件字节数组
    static byte[] inputFileBytes = null;

    // 排序打印出前几的个数
    static final int SORT_PRINT_NUM = 10;


    /**
     * 程序入口
     * @param args[0] 输入文件名
     */
    public static void main(String[] args) {
        // 初始化
        String inputFileName = null;
        if (args.length > 0 && args != null){
            inputFileName = args[0];
        } else{
            System.out.println("未输入参数");
            System.exit(1);
        }

        // 读取文件
        inputFileBytes = readFileToBytes(inputFileName);

        Lib core = new Lib(inputFileBytes);

        // 预处理，计算字符数、行数、单词数，排序频率
        core.preproccess();
        core.collectWord();
        core.sortWordMap();

        // 获取结果
        charNum = core.getCharNum();
        wordNum = core.getWordNum();
        lineNum = core.getLineNum();
        wordList = core.getSortedList();

        // 结果
        // printResult();
        writeResult();
    }


    /**
     * 读取文件到字节数组中
     *
     * @param fileName 文件名
     *
     * @return bytes 字节数组
     */
    static byte[] readFileToBytes(String fileName){
        byte[] fileBytes = null;

        try {
            File file = new File(fileName);
			FileInputStream reader = new FileInputStream(file);
			Long fileLength = file.length();
			// System.out.println("fileLength: " + fileLength);
			fileBytes = new byte[fileLength.intValue()];
			reader.read(fileBytes);
			reader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("文件不存在");
            System.exit(1);
        }
        catch(Exception e){
            System.out.println("读取文件出错");
            e.printStackTrace();
            System.exit(1);
        }

        return fileBytes;
    }

    /**
     * 打印结果到控制台
     */
    static void printResult(){
        System.out.println("characters: " + charNum);
        System.out.println("words: " + wordNum);
        System.out.println("lines: " + lineNum);
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            System.out.println("<" + entry.getKey() + ">: " + entry.getValue());
            if (++ i >= SORT_PRINT_NUM){
                break;
            }
        }
    }

    /**
     * 输出结果到文件中
     */
    static void writeResult(){
        String resultString = String.format(
            "characters: %d\nwords: %d\nlines: %d\n",
            charNum, wordNum, lineNum
        );
        int i = 0;
        for (Map.Entry<String, Integer> entry : wordList) {
            resultString += String.format("<%s>: %d", entry.getKey(), entry.getValue());
            if (++ i >= SORT_PRINT_NUM && i == wordList.size()-1){
                break;
            }
			resultString += '\n';
        }
        try{
            File outPutFile = new File("result.txt");
            if (! outPutFile.exists()){
                outPutFile.createNewFile();
            }
            FileWriter writter = new FileWriter(outPutFile.getName(), false);
            writter.write(resultString);
            writter.close();
        }catch(Exception e){
            System.out.println("写入文件出错");
            e.printStackTrace();
        }
    }

}