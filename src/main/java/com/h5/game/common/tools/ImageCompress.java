package com.h5.game.common.tools;

import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;


/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class ImageCompress {
	public static final String FORMAT_NAME = "jpg";
	private static Logger logger = Logger.getLogger(ImageCompress.class);

    private static float quality = 0.5F;
	/*@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		System.out.println("开始：" + new Date().toLocaleString());
		ImgCompress imgCom = new ImgCompress("D:\\test.jpg");
		byte[] bs = imgCom.resizeFixToBytes(400, 400);
		File f = new File("D:\\1.jpg");
		FileOutputStream fs = new FileOutputStream(f);
		fs.write(bs);
		fs.flush();
		fs.close();
		//System.out.println(imgCom.resizeFixToBase64(400, 400));
		System.out.println("结束：" + new Date().toLocaleString());
	}*/
	
	public static String Base64Code(File file){
		FileInputStream is = null;
		try{
			is = new FileInputStream(file);
			byte[] data = new byte[is.available()];
			is.read(data);
			is.close();
			return Base64Code(data);
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}finally{
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static String Base64Code(byte[] b) {
		if(b == null)
			return null;
		BASE64Encoder encoder = new BASE64Encoder();
		String codeBase64 = "";
		StringBuilder pictureBuffer = new StringBuilder();
		pictureBuffer.append(encoder.encode(b));
		codeBase64 = pictureBuffer.toString();
		return codeBase64;
	}
	
	public static byte[] resizeFixToBytes(InputStream in,int w,int h){
		try{
			BufferedImage srcImg =  ImageIO.read(in);
			return convert(srcImg,w,h);
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	}

	/*public static byte[] resizeFixToBytes(InputStream in,int w,int h,String formatName){
		try{
			BufferedImage srcImg =  ImageIO.read(in);
			return convert(srcImg,w,h,formatName);
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	}*/
	
	public static byte[] resizeFixToBytes(byte[] b,int w,int h){
		try{
			InputStream buffin = new ByteArrayInputStream(b); 
			BufferedImage img = ImageIO.read(buffin); 
			return convert(img,w,h);
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	}

	public static byte[] resizeFixToBytes(byte[] b,int w,int h,String formatName){
		try{
			InputStream buffin = new ByteArrayInputStream(b);
			BufferedImage img = ImageIO.read(buffin);
			return convert(img,w,h,formatName);
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	}

    public static byte[] resizeFixToBytes(byte[] b,String formatName){
        try{
            InputStream buffin = new ByteArrayInputStream(b);
            BufferedImage img = ImageIO.read(buffin);
            return convert(img,img.getWidth(),img.getHeight(),formatName);
        }catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
	
	public static byte[] resizeFixToBytes(InputStream in,int w,int h,String formtName){
		try{
			BufferedImage srcImg =  ImageIO.read(in);
			return convert(srcImg,w,h,formtName);
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private static byte[] convert(BufferedImage srcImg,int w,int h,String formatName){
		try{
			BufferedImage buffer = null;
			int width = srcImg.getWidth();
			int height = srcImg.getHeight();
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			buffer = resize(srcImg,w,h);
			if(buffer != null){
                ImageIO.write(buffer,formatName,byteOut);
			}
			return byteOut.toByteArray();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return null;
	}

    public static byte[] compressImage(byte[] bytes,String formatName) throws IOException {
        InputStream buffin = new ByteArrayInputStream(bytes);
        return convert(buffin, quality ,formatName);
    }

    public static byte[] compressImage(byte[] bytes,float quality, String formatName) throws IOException {
        InputStream buffin = new ByteArrayInputStream(bytes);
        return convert(buffin,quality,formatName);
    }

    private static byte[] convert(InputStream inputStream,float quality ,String formatName) throws IOException {
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(formatName).next();
        ImageWriteParam imgWriteParams = new JPEGImageWriteParam(null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality(quality);
        imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ColorModel colorModel = bufferedImage.getColorModel();
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        imageWriter.reset();
        // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
        // OutputStream构造
        imageWriter.setOutput(ImageIO.createImageOutputStream(byteOut));
        // 调用write方法，就可以向输入流写图片
        imageWriter.write(null, new IIOImage(bufferedImage, null, null),
                imgWriteParams);
        byteOut.flush();
        return byteOut.toByteArray();
    }
	private static byte[] convert(BufferedImage srcImg,int w,int h){
		return convert(srcImg,w,h,FORMAT_NAME);
	}
	/**
	 * 对图片进行压缩
	 * @param filePath 图片的路径
	 * @param w 压缩后的宽度
	 * @param h	压缩后的高度
	 * @return	压缩后的图片的byte[]数组
	 * @throws IOException
	 */
	public static byte[] resizeFixToBytes(String filePath,int w, int h) {
		try{
			BufferedImage srcImg = ImageIO.read(new File(filePath));
			return convert(srcImg,w,h);
		}catch(Exception e){
			logger.error(e.getMessage());
			return null;
		}
		
	}
	
	/**
	 * 对图片进行压缩
	 * @param filePath 图片的路径
	 * @param w	压缩后图片的宽度
	 * @param h	压缩后图片的高度
	 * @return	返回编码成Base64的字符串
	 */
	public static String resizeFixToBase64(String filePath,int w, int h) {
		
		return Base64Code(resizeFixToBytes(filePath,w,h));
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * 
	 * @param w
	 *            int 新宽度
	 * @param h
	 *            int 新高度
	 */
	private static BufferedImage resize(Image img ,int w, int h) throws IOException {
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		boolean change = image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
		if(change)
			return image;
		else
			return null;
	}
}
