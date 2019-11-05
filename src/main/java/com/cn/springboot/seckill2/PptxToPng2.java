package com.cn.springboot.seckill2;

import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.sl.usermodel.ColorStyle;
import org.apache.poi.sl.usermodel.PaintStyle;
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
public class PptxToPng2 {
    public static void main(String[] args) throws Exception {
        InputStream is = null;
        try {
//            is = new FileInputStream("E:/对比/读取/信息技术部中心化架构培训材料201803.ppt");
            is = new FileInputStream("E:/对比/读取/2.pptx");
            pptxToPng(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
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
                turnFontColor(list);
                //防止中文乱码
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
                e.printStackTrace();
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

    public static void pptToPng(InputStream is) throws Exception {
        HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl(is));
        Dimension pgsize = ppt.getPageSize();
        List<BufferedImage> ios = new ArrayList<BufferedImage>();
        for (int i = 0; i < ppt.getSlides().size(); i++) {
            try {
                //防止中文乱码
                for (HSLFShape shape : ppt.getSlides().get(i).getShapes()) {
                    if (shape instanceof HSLFTextShape) {
                        HSLFTextShape tsh = (HSLFTextShape) shape;
                        for (HSLFTextParagraph p : tsh) {
                            for (HSLFTextRun r : p) {
                                r.setFontFamily("宋体");
                            }
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                //默认取原背景
//                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                ppt.getSlides().get(i).draw(graphics);
                ios.add(img);
                graphics.dispose();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("第" + i + "张ppt转换出错");
            }
        }
        for (int i = 0; i < ios.size(); i++) {
            String filename = "E:/对比/输入/ppt/07-" + (i + 1) + ".png";
            System.out.println(filename);
            try (FileOutputStream out = new FileOutputStream(filename)) {
                ImageIO.write(ios.get(i), "png", out);
            }
        }
        System.out.println("3success");
    }


    public static void turnFontColor(List<XSLFShape> list) throws Exception {
        for (XSLFShape shape : list) {
            Color backgroundcolor=shape.getSheet().getBackground().getFillColor();
            if (shape instanceof XSLFTextShape) {
                XSLFTextShape tsh = (XSLFTextShape) shape;
                for (XSLFTextParagraph p : tsh) {
                    for (XSLFTextRun r : p) {
                        //颜色过滤 更改
                        PaintStyle.SolidPaint solidPaint = (PaintStyle.SolidPaint) r.getFontColor();
                        ColorStyle style = solidPaint.getSolidColor();
                        Color color = style.getColor();
                        if (color.equals(Color.white)) {
                            r.setFontColor(Color.black);
                        } else if (color.equals(new Color(91, 155, 213))) {
                            r.setFontColor(Color.black);
                        }
                        r.setFontFamily("宋体");
                    }
                }
            }
            if (shape instanceof XSLFGroupShape) {
                XSLFGroupShape group = (XSLFGroupShape) shape;
                List<XSLFShape> listGroup = group.getShapes();
                if (listGroup.size() > 0) {
                    turnFontColor(group.getShapes());
                }
            }

        }
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
