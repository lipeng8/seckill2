package com.cn.springboot.seckill2;

import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lipeng
 * @CreateDate: 2019/8/9$ 14:17$
 * @Version: 1.0
 */
public class PptxToPng {
    public static void main(String[] args) throws Exception {
//        InputStream is = null;
//        try {
//            is = new FileInputStream("E:/对比/读取/2.pptx");
//            pptxToPng(is);
//        } catch (IOException e) {
//        } finally {
//            if (is != null)
//                is.close();
//        }
//        clusterInfo.trim().equals(MASTER) ? MASTER_CONFIG_PATH : SLAVE_CONFIG_PATH;
//        System.out.println("master".equals("master")?"1":"2");
        String a="2456465sda";
        if( a.matches("\\d+")){
            System.out.println("***"+a);
        }
    }

    private static void pptxToPng(InputStream is) throws Exception {
        XMLSlideShow ppt = new XMLSlideShow(is);
        Dimension pgsize = ppt.getPageSize();
        System.out.println(pgsize.width + "--" + pgsize.height);
        List<BufferedImage> ios = new ArrayList<BufferedImage>();
        for (int i = 0; i < ppt.getSlides().size(); i++) {
            try {
                List<XSLFShape> list = ppt.getSlides().get(i).getShapes();
                //防止中文乱码
                for (XSLFShape shape : ppt.getSlides().get(i).getShapes()) {
                    System.out.println("shape：" + shape.getShapeName());
                    System.out.println("shape：" + shape.getSheet());
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape tsh = (XSLFTextShape) shape;
                        for (XSLFTextParagraph p : tsh) {
                            System.out.println("XSLFTextParagraph：" + p.getText());
                            for (XSLFTextRun r : p) {
                                r.setFontFamily("宋体");
                            }
                        }
                    }
//                    }
                    if (shape instanceof XSLFGroupShape) {
                        XSLFGroupShape group = (XSLFGroupShape) shape;
                        List<XSLFShape> listGroup = group.getShapes();
                        for (XSLFShape shapeGroup : listGroup) {
                            System.out.println("shapeGroup：" + shapeGroup.getShapeName());
                            System.out.println("shapeGroup：" + shapeGroup.getSheet());
                            if (shapeGroup instanceof XSLFTextShape) {
                                XSLFTextShape ts = (XSLFTextShape) shapeGroup;
                                for (XSLFTextParagraph p : ts) {
                                    System.out.println("shapeGroupXSLFTextParagraph：" + p.getText());
                                    for (XSLFTextRun r : p) {
                                        r.setFontFamily("宋体");
                                    }
                                }
                            }
                        }
                    }

                }
                int times = 1;
                BufferedImage img = new BufferedImage(pgsize.width * times, pgsize.height * times, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                //默认取原背景
//                graphics.setPaint(Color.white);
                graphics.scale(times, times);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width * times, pgsize.height * times));
                // render
                ppt.getSlides().get(i).draw(graphics);
                // save the output
                String filename = "E:/对比/输入/pptx/07-" + (i + 1) + ".png";
                System.out.println(filename);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(img, "png", out);
                ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                try {
                    byte[] content = new byte[1024];
                    int length = 0;
                    while ((length = in.read(content)) != -1) {
                        out2.write(content, 0, length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    out2.close();
                    in.close();
                    out.close();
                }
                fileOutputStream.write(out2.toByteArray());
//                ios.add(resizeImage(img, pgsize.width, pgsize.height));
                graphics.dispose();
            } catch (Exception e) {
                System.out.println("第" + i + "张ppt转换出错");
            }
        }
        for (int i = 0; i < ios.size(); i++) {
            String filename = "E:/对比/输入/pptx/07-" + (i + 1) + ".png";
            System.out.println(filename);
            try (FileOutputStream out = new FileOutputStream(filename)) {
                ImageIO.write(ios.get(i), "png", out);
            }
        }
        System.out.println("success");
    }



    /***
     * 功能 :调整图片大小
     * @param width   转换后图片宽度
     * @param height  转换后图片高度
     */
    public static BufferedImage resizeImage(BufferedImage inImg, int width, int height) throws IOException {
        Image in = inImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage outImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        outImg.getGraphics().drawImage(
                in.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
                0, null);
        return outImg;
    }
}
