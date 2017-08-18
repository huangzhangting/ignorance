package nom.ignorance.test.test.scancode;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
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

    private static final int LINE_LIMIT = 50;
    private List<String> codeList = new ArrayList<>();

    XWPFDocument doc = new XWPFDocument();


    @Test
    public void test_scan() throws Exception{
        String path = "D:\\weidai\\project\\weidai\\wpai-common";
        String docName = "wpai_common.docx";
        String docPath = "document/"+docName;

        File file = new File(path);

        if(file.isDirectory()){
            handleDirectory(file);

            //处理结尾的代码
            handleCode(true);

            log.info("代码行数：{}", codeLineCount);
            log.info("大括弧占一行数：{}", onlyBraceCount);

            FileOutputStream out = new FileOutputStream(docPath);
            doc.write(out);
            out.close();
            doc.close();

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

        try {
            readJava(file);
        } catch (Exception e) {
            e.printStackTrace();
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
            codeList.add(line);
            calculateCodeLineCount(line);

            //处理代码
            handleCode(false);
        }

    }

    private void calculateCodeLineCount(String line){
        String str = line.trim();
        if(str.equals("{") || str.equals("}")){
            onlyBraceCount++;
//            return;
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

    /**
     * 开始处理代码，生成word
     * @param isEnd
     */
    private void handleCode(boolean isEnd){
        if(codeList.size() >= LINE_LIMIT){
//            System.out.println("开始处理代码："+codeList.size());
            handleCodeParagraph();
        }else{
            if(isEnd && !codeList.isEmpty()){
                System.out.println("开始处理结束代码："+codeList.size());
                handleCodeParagraph();
            }
        }
    }

    private void handleCodeParagraph(){
        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
//        p.setSpacingBetween(0.01);

        for(String code : codeList) {
            XWPFRun r = p.createRun();
            r.setText(code);
            r.addBreak();
            r.setFontSize(9); // 单位磅，对应小五
//            r.setFontFamily();
        }

        codeList.clear();
    }

}
