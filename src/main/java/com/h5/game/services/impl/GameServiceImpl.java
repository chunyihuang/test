package com.h5.game.services.impl;


import com.h5.game.dao.base.PageResults;
import com.h5.game.model.bean.Comment;
import com.h5.game.model.bean.Game;
import com.h5.game.model.bean.GameType;
import com.h5.game.model.bean.User;
import com.h5.game.dao.interfaces.CommentDao;
import com.h5.game.dao.interfaces.GameDao;
import com.h5.game.dao.interfaces.GameTypeDao;
import com.h5.game.services.interfaces.GameService;
import com.h5.game.model.vo.CommentVo;
import com.h5.game.model.vo.GameVo;
import com.h5.game.model.vo.QueryCommentVo;
import com.h5.game.model.vo.QueryGameVo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Created by 黄春怡 on 2017/4/10.
 */
@Service
public class GameServiceImpl implements GameService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private GameTypeDao gameTypeDao;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private CommentDao commentDao;


    @Transactional
    public Map uploadGame(GameVo gameVo,String rootDir, User user){

        Map result = new HashedMap();
        Game game = new Game();
        Long timestamp = new Date().getTime();//当前上传的文件系统生成的唯一目录
        String zipDir = rootDir+"zip";
        String unzipDir = rootDir+"unzip";
       //解压游戏包
        if (!gameVo.getZip().isEmpty()) {
            File newUploadFile = new File(zipDir, String.valueOf(timestamp));
            if (newUploadFile.mkdirs()) {// 创建目标目录
                try {
                    gameVo.getZip().transferTo(newUploadFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    result.put("status", false);
                    result.put("reason", "游戏源文件上传失败!");
                    return result;
                }
            }
            //利用当前时间戳生成唯一的文件解压目录
           //解压后的目录
            String unzipDirectory = unzipDir + "\\" + String.valueOf(timestamp);
                //解压缩
                try {
                    if(runbat("E:\\winrar\\unzip.bat",newUploadFile.getPath(),unzipDir)){
                        String indexPath = getPath(unzipDirectory,gameVo.getIndexName()).replaceAll("\\\\","/").replaceFirst("D:/idea/game/web/target/web-1.0-SNAPSHOT","");
                        if(null != indexPath){
                            System.out.println("indexPath-----"+indexPath);
                            game.setOnlineUrl(indexPath);//-->设置在线试玩入口地址说
                        }
                        String fileSize = caculateFileSize(new File(unzipDir));
                        game.setSize(fileSize);//游戏大小
                        game.setGameName(gameVo.getGameName());//游戏名称
                        game.setSummary(gameVo.getSummary());//游戏简介
                        game.setUser(user);//上传者
                        game.setVersion(gameVo.getVersion());//游戏版本
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    result.put("status",false);
                    result.put("reason","文件解压缩失败!");
                    return result;
                }
                //游戏类型
                if(null != gameVo.getGameTypeId()){
                    GameType gameType = (GameType) gameTypeDao.getById(gameVo.getGameTypeId());
                    if(null != gameType){
                        game.setGameType(gameType);
                    }
                }
                //为每个游戏的解压文件创建一个res目录，存放apk icon 封面 预览图
                String res = createDir(unzipDirectory,"res").getPath()+"\\";
                String iconPath = createDir(res,"icon").getPath()+"\\";
                String screenPath = createDir(res,"screen").getPath()+"\\";
                String previewPicPath = createDir(res,"preview").getPath()+"\\";
                if (null != gameVo.getIcon() && !gameVo.getIcon().isEmpty()) {
                    try {
                        File icon = new File(iconPath,gameVo.getIcon().getOriginalFilename());
                        gameVo.getIcon().transferTo(icon);
                        game.setIcon(icon.getPath().replaceAll("\\\\","/").replaceFirst("D:/idea/game/web/target/web-1.0-SNAPSHOT",""));//游戏图标地址
                    }catch (Exception e){
                        e.printStackTrace();
                        result.put("status",false);
                        result.put("reason","图标上传失败!");
                        return result;
                    }
                }else {
                    fileCopy(rootDir+"/res-default/icon/android/icon.png",iconPath+"icon.png");
                    game.setIcon((iconPath.replaceAll("\\\\","/")+"/"+"icon.png").replaceFirst("D:/idea/game/web/target/web-1.0-SNAPSHOT",""));//游戏图标地址
                }

                if (null != gameVo.getScreen() && !gameVo.getScreen().isEmpty()) {
                    try {
                        File screen = new File(screenPath,gameVo.getScreen().getOriginalFilename());
                        gameVo.getScreen().transferTo(screen);
                        game.setScreen(screen.getPath().replaceAll("\\\\","/").replaceFirst("D:/idea/game/web/target/web-1.0-SNAPSHOT",""));//游戏封面地址
                    }catch (Exception e){
                        e.printStackTrace();
                        result.put("status",false);
                        result.put("reason","游戏封面上传失败!");
                        return result;
                    }
                }else {
                    fileCopy(rootDir+"/res-default/screen/android/screen.png","screen.png");
                    game.setScreen((screenPath.replaceAll("\\\\","/")+"/"+"screen.png").replaceFirst("D:/idea/game/web/target/web-1.0-SNAPSHOT",""));//游戏封面地址
                }
                //游戏预览图
                if (null != gameVo.getPreviewPics() && gameVo.getPreviewPics().length > 0 ) {
                    for(CommonsMultipartFile cmf:gameVo.getPreviewPics()){
                        File previewPic = new File(previewPicPath,cmf.getOriginalFilename());
                        try {
                            cmf.transferTo(previewPic);
                        }catch (Exception e){
                            e.printStackTrace();
                            result.put("status",false);
                            result.put("reason","游戏预览图上传失败!");
                            return result;
                        }
                    }

                }else {
                    fileCopy(rootDir+"/res-default/previewPics/android/preview.png",previewPicPath);
                }
                game.setPreviewPics(previewPicPath.replaceAll("\\\\","/").replaceFirst("D:/idea/game/web/target/web-1.0-SNAPSHOT",""));//游戏预览图


            gameDao.save(game);
        }else {
            result.put("status",false);
            result.put("reason","请上传游戏!");
            return result;
        }
        result.put("status",true);
        result.put("reason","上传成功!");
        return result;
    }

    public Game getGameById(Integer id) {
       return (Game)gameDao.getById(id);
    }

    public PageResults<Comment> listComments(QueryCommentVo commentVo,Integer page, Integer rows) {
        return commentDao.listComments(commentVo,page,rows);
    }

    public PageResults listGames(QueryGameVo queryGameVo, Integer page, Integer rows) {
        return gameDao.listGames(queryGameVo,page,rows);
    }

    public Boolean removeGame(Integer id) {
        Game game = (Game)gameDao.getById(id);
        if(null != game){
            gameDao.delete(game);
            return true;
        }else {
            return false;
        }
    }

    public Map updateGame(Game game) {
         Map<String,Object>result = new HashedMap();
         if(null != game && null != game.getId()){
             gameDao.saveOrUpdate(game);
             result.put("status",true);
             result.put("reason","");
             return result;

         }else {
            result.put("status",false);
            result.put("reason","请选择要修改的对象");
            return result;
         }
    }

    public boolean removeComment(Comment comment) {
        if(null != comment)
            commentDao.delete(comment);
        else return false;
        return true;
    }

    @Transactional
    public Map publishComment(CommentVo commentVo,User user) {

        Map<String,Object> result = new HashedMap();
        Comment comment = null;
        if(null != commentVo.getGameId()) {
            Game game = (Game) gameDao.getById(commentVo.getGameId());
            if (null != game) {
                comment = new Comment();
                comment.setGame(game);
                comment.setUser(user);
                comment.setCommentInfo(commentVo.getCommentInfo());
                comment.setLiked(commentVo.getLiked());
                comment.setDownload(commentVo.getDownload());
                //comment.setTag_id(commentVo.getTags());
                commentDao.saveOrUpdate(comment);
                result.put("status", true);
                result.put("reason", "评论成功!");
                return result;
            }

        }
        result.put("status",false);
        result.put("reason","参数错误");
        return result;
    }


    public Integer countComments(QueryCommentVo commentVo) {

        return commentDao.countComments(commentVo);
    }

    /**
     * 获取游戏指定文件路径
     * @return
     */
    private String getPath(String sourcePath,String fileName){

        File file = new File(sourcePath);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    list.add(file2);
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                    } else {
                        if(file2.getName().contains(fileName)){
                            return  file2.getPath();
                        }
                    }
                }
            }
        } else {
            return "没有该文件夹";
        }
        return "没有呀";
    }

    /**
     * 获取文件夹的大小
     * @param f
     * @return
     */
     private String caculateFileSize(File f){
        DecimalFormat df = new DecimalFormat("#.00");

        String fileSizeString = "";
        if (f.length() < 1024) {
            fileSizeString = df.format((double) f.length()) + "B";
        } else if (f.length() < 1048576) {
            fileSizeString = df.format((double) f.length() / 1024) + "K";
        } else if (f.length() < 1073741824) {
            fileSizeString = df.format((double) f.length() / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) f.length() / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 解压zip文件
     * @param zipPath
     * @param unzipPath
     * @return
     * @throws Exception
     */
    private boolean zipToFile(String zipPath, String unzipPath) throws Exception {

        if (null == zipPath || null == unzipPath) {
            return false;
        }
        ZipFile zfile = new ZipFile(zipPath);//连接待解压文件
        Enumeration zList = zfile.entries();//得到zip包里的所有元素
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                continue;
            }
            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                //以ZipEntry为参数得到一个InputStream，并写到OutputStream中
                outputStream = new BufferedOutputStream(new FileOutputStream(getRealFileName(unzipPath, ze.getName())));
                inputStream = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = inputStream.read(buf, 0, 1024)) != -1) {
                    outputStream.write(buf, 0, readLen);
                }
                inputStream.close();
                outputStream.close();

            } catch (Exception e) {
                throw new IOException("解压失败：" + e.toString());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ex) {
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                inputStream = null;
                outputStream = null;

            }

        }
        zfile.close();
        return true;
    }


    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * @param zippath 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    private static File getRealFileName(String zippath, String absFileName){

        String[] dirs = absFileName.split("/" , absFileName.length());
        File ret = new File(zippath);// 创建文件对象
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                ret = new File(ret, dirs[i]);
            }
        }
        if (!ret.exists()) {// 检测文件是否存在
            ret.mkdirs();// 创建此抽象路径名指定的目录
        }
        ret = new File(ret, dirs[dirs.length - 1]);// 根据 ret 抽象路径名和 child 路径名字符串创建一个新 File 实例
        return ret;
    }


    public boolean apk(String apkName,String srcPath) throws IOException,InterruptedException{

        System.out.println("开始打包apk");
        if(runbat("E:\\apk\\step1.bat",apkName,apkName.toUpperCase())) {
            if (runbat("E:\\apk\\step2.bat", apkName, srcPath)) {
                if (runbat("E:\\apk\\step3.bat", apkName)) {
                    if (runbat("E:\\apk\\step4.bat", apkName)) {
                        if (runbat("E:\\apk\\step5.bat", apkName, apkName.toUpperCase())) {
                            if (runbat("E:\\apk\\step6.bat", apkName, apkName.toUpperCase())) {
                                return true;
                            }
                        }
                    }
                }

            }
        }
        return true;
    }

    private static void copy(String src, String des) {  
        File file1=new File(src);
        File[] fs = file1.listFiles();
        File file2 = new File(des);
        if(!file2.exists()){  
            file2.mkdirs();  
        }  
        for (File f : fs) {  
            if(f.isFile()){  
                fileCopy(f.getPath(),des+"\\"+f.getName()); //调用文件拷贝的方法  
            }else if(f.isDirectory()){  
                copy(f.getPath(),des+"\\"+f.getName());  
            }  
        }  
          
    }  

    /**
     * 文件拷贝的方法
     */
    private static void fileCopy(String src, String des) {

        File srcFile = new File(src);
        File targetFile = new File(des);
        try{
            InputStream in = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(targetFile);
            byte[] bytes = new byte[1024];
            int len = -1;
            while((len=in.read(bytes))!=-1)
             {
                 out.write(bytes,0,len);
             }
             in.close();
             out.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 创建文件夹
     * @param path
     * @param name
     * @return
     */
    private static File createDir(String path,String name){
        File file = new File(path,name);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }


    /**
     * 更改apk配置文件
     * @param xmlPath
     */
    public  void insertNodeToXml(String xmlPath){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlPath);
            Element root = document.getRootElement();//得到根节点
            List<Element> elements = root.elements();//根节点下的所有节点
            int index = 0;
            for (Element element:elements){
                index++;
                if(element.getName().equals("platform") && "android".equals(element.attributeValue("name"))){
                    System.out.println("找到该节点");
                    Element ldpi = DocumentHelper.createElement("icon");
                    ldpi.addAttribute("density",  "ldpi");
                    ldpi.addAttribute("src", "res/icon/android/icon.png");
                    element.add(ldpi);
                    Element mdpi = DocumentHelper.createElement("icon");
                    mdpi.addAttribute("density",  "mdpi");
                    mdpi.addAttribute("src",  "res/icon/android/icon.png");
                    element.add(mdpi);
                    Element hdpi = DocumentHelper.createElement("icon");
                    hdpi.addAttribute("density",  "hdpi");
                    hdpi.addAttribute("src",  "res/icon/android/icon.png");
                    element.add(hdpi);
                    Element xhdpi = DocumentHelper.createElement("icon");
                    xhdpi.addAttribute("density",  "xhdpi");
                    xhdpi.addAttribute("src",  "res/icon/android/icon.png");
                    element.add(xhdpi);
                    Element xxhdpi = DocumentHelper.createElement("icon");
                    xxhdpi.addAttribute("density",  "xxhdpi");
                    xxhdpi.addAttribute("src",  "res/icon/android/icon.png");
                    element.add(xxhdpi);
                    break;

                }

            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            try {
                XMLWriter writer  = new XMLWriter(new FileWriter("xmlPath"),format);
                writer.write(document);
                writer.flush();
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }


        }catch (DocumentException de){
            de.printStackTrace();
        }
    }

    /**
     * 运行bat文件
     * @param batPath
     * @param params
     */
    public   boolean runbat(String batPath,String ...params){


        StringBuilder infoMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        String line = null;
        String cmd = "cmd /c start /b " + batPath + " ";
        if(null != params && params.length > 0){
            for(String parma:params){
                cmd += parma + " ";
            }
        }
        try {
            System.out.println("开始执行"+batPath);
            Process ps = Runtime.getRuntime().exec(cmd);
            System.out.println("结束执行"+batPath);
            BufferedReader info = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
            while((line=info.readLine())!=null) {
                System.out.println("info"+line);
                infoMsg.append(line).append("\n");
            }
            while((line=error.readLine())!=null) {
                System.out.println("errorMsg"+line);
                errorMsg.append(line).append("\n");
            }

            int i = ps.exitValue();  //接收执行完毕的返回值
            if (i == 0) {
               logger.info("执行完成.");
               return true;
            } else {
                logger.info("执行失败.");

            }
            ps.destroy();
            ps = null;
        }catch (IOException e){
            logger.error("执行"+batPath+"失败");
            e.printStackTrace();
        }
        return false;
    }


}
