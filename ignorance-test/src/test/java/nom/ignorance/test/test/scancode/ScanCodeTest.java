package nom.ignorance.test.test.scancode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzhangting on 2017/8/17.
 */
@Slf4j
public class ScanCodeTest {
    private int codeLineCount = 0;
    private int onlyBraceCount = 0;

    private static final String JAVA_FILE = ".java";

    private static List<String> ignoreDirectoryList = new ArrayList<String>(){{
        add("test");
        add("target");

    }};

    private static List<String> ignoreFileList = new ArrayList<String>(){{
        add("OSSUtil");
        add("SequenceNumberUtil.java");

    }};


    @Test
    public void test_scan() throws Exception{
        String path = "D:\\weidai\\project\\weidai\\wpai-common";
        File file = new File(path);

        if(file.isDirectory()){
            handleDirectory(file);
            log.info("代码行数：{}", codeLineCount);
            log.info("大括弧占一行数：{}", onlyBraceCount);
        }else{
            log.info(file.getName() + " 不是文件夹");
        }
    }

    private void handleDirectory(File directory){
        String name = directory.getName();
        if(ignoreDirectoryList.contains(name)){
            log.info("忽略文件夹：{}", name);
            return;
        }
        File[] files = directory.listFiles();
        if(files==null || files.length==0){
            log.info(directory.getName()+" 是空文件夹");
            return;
        }
        for(File f : files){
            handleFile(f);
        }
    }

    private void handleFile(File file){
        if(file.isDirectory()){
            handleDirectory(file);
        }else{
            handleJavaFile(file);
        }
    }

    private void handleJavaFile(File file){
        String name = file.getName();
        if(!file.isFile()){
            log.info(name+" 不是文件");
            return;
        }
        if(!name.endsWith(JAVA_FILE)){
            return;
        }
        int idx = name.indexOf(JAVA_FILE);
//        System.out.println(name+" ---- "+name.substring(0, idx));
        if(ignoreFileList.contains(name.substring(0, idx)) || ignoreFileList.contains(name)){
            log.info("忽略文件：{}", name);
            return;
        }

        //fixme 测试
        if(name.startsWith("MD5Util")){
            try {
                readJava(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void readJava(File file) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line;
        boolean isDocNote = false; // 文档注释
        boolean isMultiNote = false; // 多行注释
        while ((line=reader.readLine()) != null){
            if(isDocNote){
                if(isMultiNoteEnd(line)){
                    isDocNote = false;
                }
                continue;
            }
            if(isMultiNote){
                if(isMultiNoteEnd(line)){
                    isMultiNote = false;
                }
                continue;
            }
            isDocNote = isDocNoteStart(line);
            if(!isDocNote){
                isMultiNote = isMultiNoteStart(line);
            }
            if(isDocNote || isMultiNote || isLineNote(line) || isBr(line)){
                continue;
            }
            calculateCodeLineCount(line);

            System.out.println(line);

        }
    }

    private void calculateCodeLineCount(String line){
        String str = line.trim();
        if(str.equals("{") || str.equals("}")){
            onlyBraceCount++;
            return;
        }
        codeLineCount++;
    }

    private boolean isDocNoteStart(String line){
        return line.trim().startsWith("/**");
    }
    private boolean isMultiNoteEnd(String line){
        return line.trim().endsWith("*/");
    }
    private boolean isMultiNoteStart(String line){
        return line.trim().startsWith("/*");
    }
    private boolean isLineNote(String line){
        return line.trim().startsWith("//");
    }
    private boolean isBr(String line){
        return line.trim().equals("");
    }

}
