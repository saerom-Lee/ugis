package ugis.cmmn.imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.RasterFactory;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriter;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

//GeoTiff ����(����) ���� Ŭ����
public class GTiffDataWriter {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// GeoTiff ����(8bit)�� �����Ѵ�.
	// @ filePath : ���� ���� ���
	// @ outCrs : ���念���� ��ǥ�� ����
	// @ coordLB : ���念���� ���ϴ� ������ǥ
	// @ coordRT : ���念���� ���ϴ� ������ǥ
	// @ width : ���念���� ���� ũ��
	// @ height : ���念���� ���� ũ��
	// @ bc : ���念���� ����
	// @ pixels : ���念���� ȭ�Ұ�
	// @
	// @ return : boolean ���� ���� ����
	public boolean geoTiffWriter(String filePath, CoordinateReferenceSystem outCrs, Coordinate coordLB,
			Coordinate coordRT, int width, int height, int bc, byte[] pixels) throws NoSuchAuthorityCodeException,
			FactoryException, IllegalArgumentException, IndexOutOfBoundsException, IOException {
		boolean ret = true;
		int lastIdx = 0;
		String saveFilePath = filePath;
		String fileName = "";
		String tfwFilePath = "";

		if (pixels == null)
			return false;

		// file name
		lastIdx = filePath.lastIndexOf("\\");
		if (lastIdx < 0)
			lastIdx = filePath.lastIndexOf("//");

		if (lastIdx < 0)
			fileName = "";
		else
			fileName = filePath.substring(lastIdx + 1);

		// tfw file path
		lastIdx = filePath.lastIndexOf(".");
		tfwFilePath = filePath.substring(0, lastIdx) + ".tfw";

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataWriter.geoTiffWriter : ");
			System.out.println(
					"[DEBUG]\t filePath : " + filePath + ", length = " + filePath.length() + ", lastIdx = " + lastIdx);
			System.out.println("[DEBUG]\t fileName : " + fileName);
			System.out.println("[DEBUG]\t tfwFilePath : " + tfwFilePath);
		}
//[DEBUG]

		try {
			WritableRaster raster = null;
			boolean isNeedTwf = false;

			// Write the Tiff File
			if (CRS.equalsIgnoreMetadata(outCrs, AbstractGridFormat.getDefaultCRS())) {
				BufferedImage bi = null;

				switch (bc) {
				case 1:
					bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
					break;
				case 3:
					bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
					break;
				case 4:
					bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
					break;
				}

				raster = bi.getRaster();
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						for (int k = 0; k < bc; k++) {
							raster.setSample(i, j, k, pixels[(i + j * width) * bc + k]);
						}
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Set Sample OK! ");
				}
//[DEBUG]

				File tif = new File(saveFilePath);
				FileImageOutputStream fios = new FileImageOutputStream(tif);
				TIFFImageWriterSpi tiffImageWriterSpi = new TIFFImageWriterSpi();
				TIFFImageWriter tiffImageWriter = new TIFFImageWriter(tiffImageWriterSpi);
				tiffImageWriter.setOutput(fios);
				tiffImageWriter.write(bi);
				tiffImageWriter.dispose();

				isNeedTwf = true;
			} else { // Write the GeoTiff File
				// Covert to a Raster
				raster = RasterFactory.createBandedRaster(java.awt.image.DataBuffer.TYPE_BYTE, width, height, bc, null);
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						for (int k = 0; k < bc; k++) {
							raster.setSample(i, j, k, (int) pixels[(i + j * width) * bc + k]);
						}
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Set Sample OK! ");
				}
//[DEBUG]

				// Create a GeoTools 2D grid referenced in the cooridnate system crs
				GridCoverageFactory gcf = new GridCoverageFactory();
				ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(coordLB.x, coordRT.x, coordLB.y,
						coordRT.y, outCrs);
				GridCoverage2D gc = gcf.create(fileName, raster, referencedEnvelope);

				// Write out to a GeoTiff file
				File geotiff = new File(saveFilePath);
				ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(geotiff);
				GeoTiffWriter writer = new GeoTiffWriter(imageOutStream);

				ParameterValue<Boolean> tfw = GeoTiffFormat.WRITE_TFW.createValue();
				tfw.setValue(true);
				writer.write(gc, new GeneralParameterValue[] { tfw });

				writer.dispose();
				gc.dispose(true);
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t write OK! ");
			}
//[DEBUG]			

			// tfw check!
			if (isNeedTwf) {
				File tfwFile = new File(tfwFilePath);
				if (!tfwFile.exists() || !tfwFile.isFile()) {
					try {
						BufferedWriter tfwWriter = new BufferedWriter(new FileWriter(tfwFile));
						String str = "";
						double gridOffsetX = 0, gridOffsetY = 0;
						double LTX = 0, LTY = 0;
						double resX = 0, resY = 0;

						// 0.51 : X resolution
						// 0
						// 0
						// -0.51 : Y resolution (negative)
						// 336405.255 : Left Top X
						// 445791.745 : Left Top Y

						LTX = coordLB.x;
						LTY = coordRT.y;
						resX = (coordRT.x - coordLB.x) / width;
						resY = (coordRT.y - coordLB.y) / height;

						str = String.format("%.2f\n", resX);
						tfwWriter.write(str);
						str = String.format("%.2f\n", gridOffsetX);
						tfwWriter.write(str);
						str = String.format("%.2f\n", gridOffsetY);
						tfwWriter.write(str);
						str = String.format("%.2f\n", -1.0 * resY);
						tfwWriter.write(str);
						str = String.format("%.3f\n", LTX);
						tfwWriter.write(str);
						str = String.format("%.3f\n", LTY);
						tfwWriter.write(str);

						tfwWriter.flush();
						tfwWriter.close();
					} catch (IOException e) {
						e.printStackTrace();

						ret = false;
					}
				}
			}
		} catch (IOException ex) {
			System.out.println("GTiffDataWriter.geoTiffWriter : (IOException) " + ex.toString());
			ex.printStackTrace();

			ret = false;
		} catch (IllegalArgumentException iae) {
			System.out.println("GTiffDataWriter.geoTiffWriter : (IllegalArgumentException) " + iae.toString());
			iae.printStackTrace();

			ret = false;
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GTiffDataWriter.geoTiffWriter : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();

			ret = false;
		}

		return ret;
	}

	// GeoTiff ����(16bit)�� �����Ѵ�.
	// @ filePath : ���� ���� ���
	// @ outCrs : ���念���� ��ǥ�� ����
	// @ coordLB : ���念���� ���ϴ� ������ǥ
	// @ coordRT : ���念���� ���ϴ� ������ǥ
	// @ width : ���念���� ���� ũ��
	// @ height : ���念���� ���� ũ��
	// @ pixels : ���念���� ȭ�Ұ�
	// @
	// @ return : boolean ���� ���� ����
	public boolean geoTiffWriter16Bit(String filePath, CoordinateReferenceSystem outCrs, Coordinate coordLB,
			Coordinate coordRT, int width, int height, int[] pixels) throws NoSuchAuthorityCodeException,
			FactoryException, IllegalArgumentException, IndexOutOfBoundsException, IOException {
		boolean ret = true;
		int lastIdx = 0;
		String saveFilePath = filePath;
		String fileName = "";
		String tfwFilePath = "";

		if (pixels == null)
			return false;

		// file name
		lastIdx = filePath.lastIndexOf("\\");
		if (lastIdx < 0)
			lastIdx = filePath.lastIndexOf("//");

		if (lastIdx < 0)
			fileName = "";
		else
			fileName = filePath.substring(lastIdx + 1);

		// tfw file path
		lastIdx = filePath.lastIndexOf(".");
		tfwFilePath = filePath.substring(0, lastIdx) + ".tfw";

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataWriter.geoTiffWriter16Bit : ");
			System.out.println(
					"[DEBUG]\t filePath : " + filePath + ", length = " + filePath.length() + ", lastIdx = " + lastIdx);
			System.out.println("[DEBUG]\t fileName : " + fileName);
			System.out.println("[DEBUG]\t tfwFilePath : " + tfwFilePath);
		}
//[DEBUG]

		try {
			WritableRaster raster = null;
			boolean isNeedTwf = false;

			// Write the Tiff File
			if (CRS.equalsIgnoreMetadata(outCrs, AbstractGridFormat.getDefaultCRS())) {
				BufferedImage bi = null;

				bi = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
				raster = bi.getRaster();

				// Pixel�� ����
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						raster.setSample(i, j, 0, pixels[i + j * width]);
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Set Sample OK! ");
				}
//[DEBUG]

				File tif = new File(saveFilePath);
				FileImageOutputStream fios = new FileImageOutputStream(tif);
				TIFFImageWriterSpi tiffImageWriterSpi = new TIFFImageWriterSpi();
				TIFFImageWriter tiffImageWriter = new TIFFImageWriter(tiffImageWriterSpi);
				tiffImageWriter.setOutput(fios);
				tiffImageWriter.write(bi);
				tiffImageWriter.dispose();

				isNeedTwf = true;
			} else { // Write the GeoTiff File
				// Covert to a Raster
				raster = RasterFactory.createBandedRaster(java.awt.image.DataBuffer.TYPE_USHORT, width, height, 1,
						null);
				// Pixel�� ����
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						raster.setSample(i, j, 0, pixels[i + j * width]);
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Set Sample OK! ");
				}
//[DEBUG]

				// Create a GeoTools 2D grid referenced in the cooridnate system crs
				GridCoverageFactory gcf = new GridCoverageFactory();
				ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(coordLB.x, coordRT.x, coordLB.y,
						coordRT.y, outCrs);
				GridCoverage2D gc = gcf.create(fileName, raster, referencedEnvelope);

				// Write out to a GeoTiff file
				File geotiff = new File(saveFilePath);
				ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(geotiff);
				GeoTiffWriter writer = new GeoTiffWriter(imageOutStream);

				ParameterValue<Boolean> tfw = GeoTiffFormat.WRITE_TFW.createValue();
				tfw.setValue(true);
				writer.write(gc, new GeneralParameterValue[] { tfw });

				writer.dispose();
				gc.dispose(true);
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t write OK! ");
			}
//[DEBUG]			

			// tfw check!
			if (isNeedTwf) {
				File tfwFile = new File(tfwFilePath);
				if (!tfwFile.exists() || !tfwFile.isFile()) {
					try {
						BufferedWriter tfwWriter = new BufferedWriter(new FileWriter(tfwFile));
						String str = "";
						double gridOffsetX = 0, gridOffsetY = 0;
						double LTX = 0, LTY = 0;
						double resX = 0, resY = 0;

						// 0.51 : X resolution
						// 0
						// 0
						// -0.51 : Y resolution (negative)
						// 336405.255 : Left Top X
						// 445791.745 : Left Top Y

						LTX = coordLB.x;
						LTY = coordRT.y;
						resX = (coordRT.x - coordLB.x) / width;
						resY = (coordRT.y - coordLB.y) / height;

						str = String.format("%.2f\n", resX);
						tfwWriter.write(str);
						str = String.format("%.2f\n", gridOffsetX);
						tfwWriter.write(str);
						str = String.format("%.2f\n", gridOffsetY);
						tfwWriter.write(str);
						str = String.format("%.2f\n", -1.0 * resY);
						tfwWriter.write(str);
						str = String.format("%.3f\n", LTX);
						tfwWriter.write(str);
						str = String.format("%.3f\n", LTY);
						tfwWriter.write(str);

						tfwWriter.flush();
						tfwWriter.close();
					} catch (IOException e) {
						e.printStackTrace();

						ret = false;
					}
				}
			}
		} catch (IOException ex) {
			System.out.println("GTiffDataWriter.geoTiffWriter16Bit : (IOException) " + ex.toString());
			ex.printStackTrace();

			ret = false;
		} catch (IllegalArgumentException iae) {
			System.out.println("GTiffDataWriter.geoTiffWriter16Bit : (IllegalArgumentException) " + iae.toString());
			iae.printStackTrace();

			ret = false;
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GTiffDataWriter.geoTiffWriter16Bit : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();

			ret = false;
		}

		return ret;
	}

	// GeoTiff ����(32bit float)�� �����Ѵ�.
	// @ filePath : ���� ���� ���
	// @ outCrs : ���念���� ��ǥ�� ����
	// @ coordLB : ���念���� ���ϴ� ������ǥ
	// @ coordRT : ���念���� ���ϴ� ������ǥ
	// @ width : ���念���� ���� ũ��
	// @ height : ���念���� ���� ũ��
	// @ pixels : ���念���� ȭ�Ұ�
	// @
	// @ return : boolean ���� ���� ����
	public boolean geoTiffWriter32BitFloat(String filePath, CoordinateReferenceSystem outCrs, Coordinate coordLB,
			Coordinate coordRT, int width, int height, double[] pixels) throws NoSuchAuthorityCodeException,
			FactoryException, IllegalArgumentException, IndexOutOfBoundsException, IOException {
		boolean ret = true;
		int lastIdx = 0;
		String saveFilePath = filePath;
		String fileName = "";

		if (pixels == null)
			return false;

		// file name
		lastIdx = filePath.lastIndexOf("\\");
		if (lastIdx < 0)
			lastIdx = filePath.lastIndexOf("//");

		if (lastIdx < 0)
			fileName = "";
		else
			fileName = filePath.substring(lastIdx + 1);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataWriter.geoTiffWriter32BitFloat : ");
			System.out.println(
					"[DEBUG]\t filePath : " + filePath + ", length = " + filePath.length() + ", lastIdx = " + lastIdx);
			System.out.println("[DEBUG]\t fileName : " + fileName);
		}
//[DEBUG]

		try {
			WritableRaster raster = null;

			// Write the Tiff File
			if (CRS.equalsIgnoreMetadata(outCrs, AbstractGridFormat.getDefaultCRS())) {
				System.out.println("GTiffDataWriter.geoTiffWriter32BitFloat : Exist no coordinate system.");

				ret = false;
			} else { // Write the GeoTiff File
				// Covert to a Raster
				raster = RasterFactory.createBandedRaster(java.awt.image.DataBuffer.TYPE_FLOAT, width, height, 1, null);
				// Pixel�� ����
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						raster.setSample(i, j, 0, pixels[i + j * width]);
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Set Sample OK! ");
				}
//[DEBUG]

				// Create a GeoTools 2D grid referenced in the cooridnate system crs
				GridCoverageFactory gcf = new GridCoverageFactory();
				ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(coordLB.x, coordRT.x, coordLB.y,
						coordRT.y, outCrs);
				GridCoverage2D gc = gcf.create(fileName, raster, referencedEnvelope);

				// Write out to a GeoTiff file
				File geotiff = new File(saveFilePath);
				ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(geotiff);
				GeoTiffWriter writer = new GeoTiffWriter(imageOutStream);

				ParameterValue<Boolean> tfw = GeoTiffFormat.WRITE_TFW.createValue();
				tfw.setValue(true);
				writer.write(gc, new GeneralParameterValue[] { tfw });

				writer.dispose();
				gc.dispose(true);
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t write OK! ");
			}
//[DEBUG]		
		} catch (IOException ex) {
			System.out.println("GTiffDataWriter.geoTiffWriter32BitFloat : (IOException) " + ex.toString());
			ex.printStackTrace();

			ret = false;
		} catch (IllegalArgumentException iae) {
			System.out
					.println("GTiffDataWriter.geoTiffWriter32BitFloat : (IllegalArgumentException) " + iae.toString());
			iae.printStackTrace();

			ret = false;
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GTiffDataWriter.geoTiffWriter32BitFloat : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();

			ret = false;
		}

		return ret;
	}

	// GeoTiff ����(16bit ���߹��)�� �����Ѵ�.
	// @ filePath : ���� ���� ���
	// @ outCrs : ���念���� ��ǥ�� ����
	// @ coordLB : ���念���� ���ϴ� ������ǥ
	// @ coordRT : ���念���� ���ϴ� ������ǥ
	// @ width : ���念���� ���� ũ��
	// @ height : ���念���� ���� ũ��
	// @ bc : ���念���� ����
	// @ pixels : ���念���� ȭ�Ұ�
	// @
	// @ return : boolean ���� ���� ����
	public boolean geoTiffWriter16BitMultiBand(String filePath, CoordinateReferenceSystem outCrs, Coordinate coordLB,
			Coordinate coordRT, int width, int height, int bc, int[] pixels) throws NoSuchAuthorityCodeException,
			FactoryException, IllegalArgumentException, IndexOutOfBoundsException, IOException {
		boolean ret = true;
		int lastIdx = 0;
		String saveFilePath = filePath;
		String fileName = "";
		String tfwFilePath = "";

		if (pixels == null)
			return false;

		// file name
		lastIdx = filePath.lastIndexOf("\\");
		if (lastIdx < 0)
			lastIdx = filePath.lastIndexOf("//");

		if (lastIdx < 0)
			fileName = "";
		else
			fileName = filePath.substring(lastIdx + 1);

		// tfw file path
		lastIdx = filePath.lastIndexOf(".");
		tfwFilePath = filePath.substring(0, lastIdx) + ".tfw";

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataWriter.geoTiffWriter16BitMultiBand : ");
			System.out.println(
					"[DEBUG]\t filePath : " + filePath + ", length = " + filePath.length() + ", lastIdx = " + lastIdx);
			System.out.println("[DEBUG]\t fileName : " + fileName);
			System.out.println("[DEBUG]\t tfwFilePath : " + tfwFilePath);
		}
//[DEBUG]

		try {
			WritableRaster raster = null;
			boolean isNeedTwf = false;

			// Write the Tiff File
			if (CRS.equalsIgnoreMetadata(outCrs, AbstractGridFormat.getDefaultCRS())) {
				BufferedImage bi = null;

				switch (bc) {
				case 1:
					bi = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
					break;
				case 3:
					bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					break;
				case 4:
					bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					break;
				}

				raster = bi.getRaster();
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						for (int k = 0; k < bc; k++) {
							raster.setSample(i, j, k, pixels[(i + j * width) * bc + k]);
						}
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Set Sample OK! ");
				}
//[DEBUG]

				File tif = new File(saveFilePath);
				FileImageOutputStream fios = new FileImageOutputStream(tif);
				TIFFImageWriterSpi tiffImageWriterSpi = new TIFFImageWriterSpi();
				TIFFImageWriter tiffImageWriter = new TIFFImageWriter(tiffImageWriterSpi);
				tiffImageWriter.setOutput(fios);
				tiffImageWriter.write(bi);
				tiffImageWriter.dispose();

				isNeedTwf = true;
			} else { // Write the GeoTiff File
				// Covert to a Raster
				switch (bc) {
				case 1:
					raster = RasterFactory.createBandedRaster(java.awt.image.DataBuffer.TYPE_USHORT, width, height, bc,
							null);
					break;
				case 3:
				case 4:
					raster = RasterFactory.createBandedRaster(java.awt.image.DataBuffer.TYPE_INT, width, height, bc,
							null);
					break;
				}

				// Pixel�� ����
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						for (int k = 0; k < bc; k++) {
							raster.setSample(i, j, k, pixels[(i + j * width) * bc + k]);
						}
					}
				}
//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Set Sample OK! ");
				}
//[DEBUG]

				// Create a GeoTools 2D grid referenced in the cooridnate system crs
				GridCoverageFactory gcf = new GridCoverageFactory();
				ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(coordLB.x, coordRT.x, coordLB.y,
						coordRT.y, outCrs);
				GridCoverage2D gc = gcf.create(fileName, raster, referencedEnvelope);

				// Write out to a GeoTiff file
				File geotiff = new File(saveFilePath);
				ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(geotiff);
				GeoTiffWriter writer = new GeoTiffWriter(imageOutStream);

				ParameterValue<Boolean> tfw = GeoTiffFormat.WRITE_TFW.createValue();
				tfw.setValue(true);
				writer.write(gc, new GeneralParameterValue[] { tfw });

				writer.dispose();
				gc.dispose(true);
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t write OK! ");
			}
//[DEBUG]			

			// tfw check!
			if (isNeedTwf) {
				File tfwFile = new File(tfwFilePath);
				if (!tfwFile.exists() || !tfwFile.isFile()) {
					try {
						BufferedWriter tfwWriter = new BufferedWriter(new FileWriter(tfwFile));
						String str = "";
						double gridOffsetX = 0, gridOffsetY = 0;
						double LTX = 0, LTY = 0;
						double resX = 0, resY = 0;

						// 0.51 : X resolution
						// 0
						// 0
						// -0.51 : Y resolution (negative)
						// 336405.255 : Left Top X
						// 445791.745 : Left Top Y

						LTX = coordLB.x;
						LTY = coordRT.y;
						resX = (coordRT.x - coordLB.x) / width;
						resY = (coordRT.y - coordLB.y) / height;

						str = String.format("%.2f\n", resX);
						tfwWriter.write(str);
						str = String.format("%.2f\n", gridOffsetX);
						tfwWriter.write(str);
						str = String.format("%.2f\n", gridOffsetY);
						tfwWriter.write(str);
						str = String.format("%.2f\n", -1.0 * resY);
						tfwWriter.write(str);
						str = String.format("%.3f\n", LTX);
						tfwWriter.write(str);
						str = String.format("%.3f\n", LTY);
						tfwWriter.write(str);

						tfwWriter.flush();
						tfwWriter.close();
					} catch (IOException e) {
						e.printStackTrace();

						ret = false;
					}
				}
			}
		} catch (IOException ex) {
			System.out.println("GTiffDataWriter.geoTiffWriter16BitMultiBand : (IOException) " + ex.toString());
			ex.printStackTrace();

			ret = false;
		} catch (IllegalArgumentException iae) {
			System.out.println(
					"GTiffDataWriter.geoTiffWriter16BitMultiBand : (IllegalArgumentException) " + iae.toString());
			iae.printStackTrace();

			ret = false;
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GTiffDataWriter.geoTiffWriter16BitMultiBand : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();

			ret = false;
		}

		return ret;
	}
}
