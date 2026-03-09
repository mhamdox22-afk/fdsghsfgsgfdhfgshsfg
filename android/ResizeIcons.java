import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResizeIcons {
    public static void main(String[] args) {
        // 图标源文件
        String sourceIconPath = "C:\\Users\\Administrator\\Downloads\\Telegram Desktop\\Coinbase.png";
        
        // 目标目录
        String targetDir = "C:\\Users\\Administrator\\Desktop\\7\\H5-VUE\\android\\app\\src\\main\\res\\";
        
        // 各密度等级的尺寸
        String[][] densities = {
            {"mipmap-mdpi", "48"},
            {"mipmap-hdpi", "72"},
            {"mipmap-xhdpi", "96"},
            {"mipmap-xxhdpi", "144"},
            {"mipmap-xxxhdpi", "192"}
        };
        
        try {
            // 读取源图标
            BufferedImage sourceImage = ImageIO.read(new File(sourceIconPath));
            System.out.println("源图标尺寸: " + sourceImage.getWidth() + "x" + sourceImage.getHeight());
            
            // 为每个密度等级创建图标
            for (String[] density : densities) {
                String dir = density[0];
                int size = Integer.parseInt(density[1]);
                
                // 创建调整大小后的图像
                BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resizedImage.createGraphics();
                
                // 设置抗锯齿
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 绘制调整大小后的图像
                g2d.drawImage(sourceImage, 0, 0, size, size, null);
                g2d.dispose();
                
                // 保存到目标文件
                String outputPath = targetDir + dir + "\\ic_launcher_foreground.png";
                File outputFile = new File(outputPath);
                ImageIO.write(resizedImage, "png", outputFile);
                System.out.println("已创建: " + outputPath + " (" + size + "x" + size + ")");
                
                // 同时更新ic_launcher.png和ic_launcher_round.png
                outputPath = targetDir + dir + "\\ic_launcher.png";
                outputFile = new File(outputPath);
                ImageIO.write(resizedImage, "png", outputFile);
                System.out.println("已创建: " + outputPath + " (" + size + "x" + size + ")");
                
                outputPath = targetDir + dir + "\\ic_launcher_round.png";
                outputFile = new File(outputPath);
                ImageIO.write(resizedImage, "png", outputFile);
                System.out.println("已创建: " + outputPath + " (" + size + "x" + size + ")");
            }
            
            System.out.println("\n图标调整完成！");
            System.out.println("\nAndroid应用图标尺寸要求:");
            System.out.println("- mipmap-mdpi: 48x48px");
            System.out.println("- mipmap-hdpi: 72x72px");
            System.out.println("- mipmap-xhdpi: 96x96px");
            System.out.println("- mipmap-xxhdpi: 144x144px");
            System.out.println("- mipmap-xxxhdpi: 192x192px");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}