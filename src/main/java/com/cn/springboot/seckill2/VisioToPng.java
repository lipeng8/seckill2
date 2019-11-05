package com.cn.springboot.seckill2;


import org.apache.poi.hdgf.HDGFDiagram;
import org.apache.poi.hdgf.streams.TrailerStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xdgf.geom.Dimension2dDouble;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.apache.poi.xdgf.usermodel.shape.ShapeRenderer;
import org.apache.poi.xdgf.util.VsdxToPng;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;


/**
 * @Author: lipeng
 * @CreateDate: 2019/8/7$ 15:21$
 * @Version: 1.0
 */
public class VisioToPng {

    public static void main(String[] args) throws Exception {
        InputStream is = null;
        try {
            is = new FileInputStream("E:/log/ins.vsdx");
            vsdxToPng(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
        }
    }

    public static void vsdToPng(InputStream is) throws Exception {
        POIFSFileSystem poiFile = new POIFSFileSystem(is);
        HDGFDiagram visio = new HDGFDiagram(poiFile);
        TrailerStream trailer = visio.getTrailerStream();

        System.out.println("success");
    }

    public static void vsdxToPng(InputStream is) throws Exception {
        OPCPackage pkg = OPCPackage.open(is);
        XmlVisioDocument document = new XmlVisioDocument(pkg);
        Collection<XDGFPage> page = document.getPages();
        Iterator<XDGFPage> it = page.iterator();
        int i = 1;
        while (it.hasNext()) {
            XDGFPage xdgf = it.next();
            Dimension2dDouble sz = xdgf.getPageSize();
            String filename = "E:/log/07-" + (xdgf.getName()) + ".png";
            int width = (int) (100 * sz.getWidth());
            int height = (int) (100 * sz.getHeight());
            BufferedImage img = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // default rendering options
            renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            renderHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics.setRenderingHints(renderHints);
            graphics.setColor(Color.black);
            graphics.setBackground(Color.white);
            graphics.clearRect(0, 0, width, height);
            // Visio's coordinate system is flipped, so flip the image vertically
            graphics.translate(0, img.getHeight());
            graphics.scale(100, -100);
            // toplevel shapes only
            ShapeRenderer renderer = new ShapeRenderer();
            renderer.setGraphics(graphics);
            xdgf.getContent().visitShapes(renderer);
            graphics.dispose();
            try (FileOutputStream out = new FileOutputStream(filename)) {
                ImageIO.write(img, "png", out);
            }
            System.out.println(filename);
        }
        System.out.println("success");
    }
    public static void vsdxToPng2(InputStream is) throws Exception {
        XmlVisioDocument document = new XmlVisioDocument(is);
        ShapeRenderer renderer = new ShapeRenderer();
        VsdxToPng.renderToPng(document,"E:/log/",2000/11.0,renderer);
    }
}
