package ugis.cmmn.imgproc;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadImageApplet extends Applet {

	private BufferedImage img;

	public void init() {
		try {
			URL url = new URL(getCodeBase(),
					"D:\\TestData\\03_SatImage\\Sentinel-2\\S2A_MSIL1C_20200414T020651_N0209_R103_T52SDF_20200414T042401.SAFE\\GRANULE\\L1C_T52SDF_A025125_20200414T020652\\IMG_DATA\\T52SDF_20200414T020651.tif");
			img = ImageIO.read(url);
		} catch (IOException e) {
		}
	}

	public void paint(Graphics g) {
		g.drawImage(img, 50, 50, null);
	}
}
