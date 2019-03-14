import java.io.*;
import java.util.*;

public class Main{

    // ����
    static int lineNum = 0;

    // �ַ���
    static int charNum = 0;

    // �������������ĸ�Ӣ����ĸ��ͷ�������ִ�Сд
    static int wordNum = 0;

    // ����õĵ��ʼ���
    static List<Map.Entry<String, Integer>> wordList = null;

    // �����ļ��ֽ�����
    static byte[] inputFileBytes = null;

    // �����ӡ��ǰ���ĸ���
    static final int SORT_PRINT_NUM = 10;


    /**
     * �������
     * @param args[0] �����ļ���
     */
    public static void main(String[] args) {
        // ��ʼ��
        String inputFileName = null;
        if (args.length > 0 && args != null){
            inputFileName = args[0];
        } else{
            System.out.println("δ�������");
            System.exit(1);
        }

        // ��ȡ�ļ�
        inputFileBytes = readFileToBytes(inputFileName);

        Lib core = new Lib(inputFileBytes);

        // Ԥ���������ַ�����������������������Ƶ��
        core.preproccess();
        core.collectWord();
        core.sortWordMap();

        // ��ȡ���
        charNum = core.getCharNum();
        wordNum = core.getWordNum();
        lineNum = core.getLineNum();
        wordList = core.getSortedList();

        // ���
        // printResult();
        writeResult();
    }


    /**
     * ��ȡ�ļ����ֽ�������
     *
     * @param fileName �ļ���
     *
     * @return bytes �ֽ�����
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
            System.out.println("�ļ�������");
            System.exit(1);
        }
        catch(Exception e){
            System.out.println("��ȡ�ļ�����");
            e.printStackTrace();
            System.exit(1);
        }

        return fileBytes;
    }

    /**
     * ��ӡ���������̨
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
     * ���������ļ���
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
            System.out.println("д���ļ�����");
            e.printStackTrace();
        }
    }

}