package nom.ignorance.test.test.scancode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;

/**
 * Created by huangzhangting on 2017/8/17.
 */
@Slf4j
public class ScanCodeTest {
    private int codeLineCount = 0;

    @Test
    public void test_1() throws Exception{
        String path = "D:\\weidai\\project\\weidai\\wpai-common";
        File file = new File(path);

        if(file.isDirectory()){
            handleDirectory(file);
            log.info("代码行数：{}", codeLineCount);
        }else{
            log.info(file.getName() + " 不是文件夹");
        }
    }

    private void handleDirectory(File directory){
        File[] files = directory.listFiles();
        if(files==null || files.length==0){
            log.info(directory.getName()+" 是空文件夹");
            return;
        }
        handleFileArray(files);
    }

    private void handleFileArray(File[] files){
//        System.out.println(files.length);
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
        if(!name.endsWith(".java")){
            return;
        }
        System.out.println(name);

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
        while ((line=reader.readLine()) != null){
            System.out.println(line);
            codeLineCount++;
        }
    }



}
