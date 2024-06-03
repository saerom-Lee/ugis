package ugis.cmmn.imgproc;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.media.jai.RasterFactory;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import kr.g119.imgproc.data.AbsoluteRadiatingCorrectionKompsatInput;
import lombok.Data;
import ugis.cmmn.imgproc.GImageEnhancement.LinearSigma;
import ugis.cmmn.imgproc.data.AbsoluteRadiatingCorrectionKompsatInputImpl;
import ugis.cmmn.imgproc.data.AbsoluteRadiatingCorrectionLandsatInput;
import ugis.cmmn.imgproc.data.GFileData;
import ugis.cmmn.imgproc.data.GHistogramMatchingData;
import ugis.cmmn.imgproc.data.GMosaicAlgorithmData;
import ugis.cmmn.imgproc.data.GMosaicData;
import ugis.cmmn.imgproc.data.GMosaicImgData;
import ugis.cmmn.imgproc.data.GMosaicResultData;
import ugis.cmmn.imgproc.data.GOpenFileData;
import ugis.cmmn.imgproc.mosaic.GAutoMosaic;
import ugis.cmmn.imgproc.mosaic.GCAutoMosaicControl;
import ugis.cmmn.imgproc.mosaic.GSeamline;

//����ó�� Ŭ����
@Data
public class GImageProcessor {

	double minX;
	double minY;
	double maxX;
	double maxY;
	CoordinateReferenceSystem crs;

	// ���� ��� ����
	public enum ProcessCode {
		SUCCESS("Success"), ERROR_FAIL_READ("Error : Failed to read the input file."),
		ERROR_FAIL_PROCESS("Error : Failed to process."), ERROR_FAIL_WRITE("Error : Failed to save the output file."),
		ERROR_NO_SUPPORTED_3BAND("Error : Three band is not supported."), ERROR("ERROR");

		private String name;

		ProcessCode(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// �̹��� ���� ����
	public enum ImageFormat {
		IMG_PNG("png"), IMG_JPEG("jpg"), IMG_TIF("tif"),;

		private String name;

		ImageFormat(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	////////////////////////////////////////////////////////////////////////////////
	// ����� �̹��� ����

	// �ش� ���� ���� Byteȭ �ȼ� ������ ���� ����� �̹����� �����Ѵ�.
	// @ inFilePath : �Է� ���� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ����� ������� ���
	// @ imgFormat : ����� ������� ���� (PNG, JPG)
	// @ thumbnailWidth : ����� ������� ���� ũ��
	// @ resampleMethod : ������迭 ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode createThumbnailImage(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath, ImageFormat imgFormat, int thumbnailWidth,
			GTiffDataReader.ResamplingMethod resampleMethod) {
		// return createThumbnailImage_Old(inFilePath, inMaxBit16, outFilePath,
		// imgFormat, thumbnailWidth, resampleMethod);
		return createThumbnailImage_New(inFilePath, inMaxBit16, outFilePath, imgFormat, thumbnailWidth, resampleMethod);
	}

	////////////////////////////////////////////////////////////////////////////////
	// [�ű�]����� �̹��� ����

	// [�ű�]�ش� ���� ���� Byteȭ �ȼ� ������ ���� ����� �̹����� �����Ѵ�.
	// @ inFilePath : �Է� ���� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ����� ������� ���
	// @ imgFormat : ����� ������� ���� (PNG, JPG)
	// @ thumbnailWidth : ����� ������� ���� ũ��
	// @ resampleMethod : ������迭 ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode createThumbnailImage_New(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath, ImageFormat imgFormat, int thumbnailWidth,
			GTiffDataReader.ResamplingMethod resampleMethod) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GImageEnhancement imgEnhance = new GImageEnhancement();
		GTiffDataReader gdReader = null;
		Envelope2D envelope = null;
		GridEnvelope2D range = new GridEnvelope2D();
		int width = thumbnailWidth;
		int height = thumbnailWidth;
		int bc = 1;
		int tmpBC = 1;
		Raster image = null;
		BufferedImage thumbImg = null;
		int lastIdx = 0;
		String outputName = "";
		File output = null;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.createThumbnailImage : ");
		}
//[DEBUG]

		// Read the input file
		try {

			// --------------------------------------------------------//
			// RGBN 4��� ���� ó�� �߰�
			// --------------------------------------------------------//
			// gdReader = new GTiffDataReader(inFilePath, inMaxBit16); //RGB 3��� ���Ϸ� ����
			// --------------------------------------------------------//
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16, false); // RGBA 4������ ���
			// --------------------------------------------------------//

		} catch (Exception ex) {
			System.out.println("GImageProcessor.createThumbnailImage : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		try {
			int dataBufferType = gdReader.getDataType();
			WritableRaster tmpRaster = null;
			int[] gridSize = gdReader.getGridSize();

			width = thumbnailWidth;
			height = (int) ((double) gridSize[1] * ((double) thumbnailWidth / (double) gridSize[0]));

			range.setBounds(0, 0, width, height);
			envelope = gdReader.getEnvelope();
			bc = gdReader.getBandCount();

			tmpBC = bc;
			if (bc == 4)
				tmpBC = 3;

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]GImageProcessor.createThumbnailImage : ");
				System.out.println("[DEBUG]\t Org Size : " + gridSize[0] + ", " + gridSize[1]);
				System.out.println("[DEBUG]\t Calc Size : " + width + ", " + height);
				System.out.println("[DEBUG]\t envelope : " + envelope.getSpan(0) + ", " + envelope.getSpan(1));
				System.out.println("[DEBUG]\t range : " + range.getWidth() + ", " + range.getHeight());
				System.out.println("[DEBUG]\t bc : " + bc + ", tmpBC : " + tmpBC);
			}
//[DEBUG]

			tmpRaster = RasterFactory.createBandedRaster(java.awt.image.DataBuffer.TYPE_BYTE, width, height, tmpBC,
					null);

			switch (dataBufferType) {
			case DataBuffer.TYPE_SHORT:
			case DataBuffer.TYPE_USHORT:
			case DataBuffer.TYPE_INT: {
				int[] gridIntPixels = gdReader.getResamplePixelsInt(range, envelope, resampleMethod, false);
				int[][] LUT = new int[3][];
				int[] stretchingMin = new int[3];
				int[] stretchingMax = new int[3];
				double[] vd = new double[3];
				int pixelValue = 0;
				int i = 0, j = 0, k = 0;
				int maxPixel = 65535;

				stretchingMin[1] = stretchingMin[2] = stretchingMin[0] = 0;
				stretchingMax[1] = stretchingMax[2] = stretchingMax[0] = 0;

				for (j = 0; j < height; j++) {
					for (i = 0; i < width; i++) {
						for (k = 0; k < bc; k++) {
							pixelValue = gridIntPixels[(i + j * width) * bc + k];

							if (pixelValue == 0)
								continue;

							switch (bc) {
							case 1: {
								if (i == 0 && j == 0) {
									stretchingMin[0] = pixelValue;
									stretchingMax[0] = pixelValue;
								} else {
									stretchingMin[0] = Math.min(stretchingMin[0], pixelValue);
									stretchingMax[0] = Math.max(stretchingMax[0], pixelValue);
								}

								stretchingMin[1] = stretchingMin[2] = stretchingMin[0];
								stretchingMax[1] = stretchingMax[2] = stretchingMax[0];
							}
								break;
							case 3:
							case 4: {
								if (k < 3) { // 0, 1, 2
									if (i == 0 && j == 0) {
										stretchingMin[k] = pixelValue;
										stretchingMax[k] = pixelValue;
									} else {
										stretchingMin[k] = Math.min(stretchingMin[k], pixelValue);
										stretchingMax[k] = Math.max(stretchingMax[k], pixelValue);
									}
								}
							}
								break;
							}
						}
					}
				}

				// Range per band
				for (i = 0; i < 3; i++) {
					if (bc == 1) {
						vd[i] = (double) (stretchingMax[0] - stretchingMin[0]);
					} else {
						vd[i] = (double) (stretchingMax[i] - stretchingMin[i]);
					}
				}

				switch (dataBufferType) {
				case DataBuffer.TYPE_SHORT: // 16bit (Lookup Table)
				case DataBuffer.TYPE_USHORT: {
					LUT[0] = new int[maxPixel + 1];
					LUT[1] = new int[maxPixel + 1];
					LUT[2] = new int[maxPixel + 1];

				}
					break;
				case DataBuffer.TYPE_INT: // 32bit (Lookup Table)
				{
					switch (bc) {
					case 1: {
						if (stretchingMax[0] > 0)
							maxPixel = stretchingMax[0];
						else
							maxPixel = 65535;
					}
						break;
					case 3:
					case 4: {
						int maxValue = 0;

						maxValue = Math.max(maxValue, stretchingMax[0]);
						maxValue = Math.max(maxValue, stretchingMax[1]);
						maxValue = Math.max(maxValue, stretchingMax[2]);

						if (maxValue > 0)
							maxPixel = maxValue;
						else
							maxPixel = 65535;
					}
						break;
					}

					LUT[0] = new int[maxPixel + 1];
					LUT[1] = new int[maxPixel + 1];
					LUT[2] = new int[maxPixel + 1];
				}
					break;
				}

				// --------------------------------------------------------------------//
				// Recalculate the Min/Max (Sigma 3)
				// --------------------------------------------------------------------//
				{
					int[] histgR = new int[maxPixel + 1];
					int[] histgG = new int[maxPixel + 1];
					int[] histgB = new int[maxPixel + 1];

					int[] pixel = new int[3];

					// Initialize
					for (i = 0; i < maxPixel + 1; i++) {
						histgR[i] = histgG[i] = histgB[i] = 0;
					}

					// Histogram
					for (j = 0; j < height; j++) {
						for (i = 0; i < width; i++) {
							switch (bc) {
							case 1: {
								pixel[0] = gridIntPixels[(i + j * width) * bc + 0];

								histgR[pixel[0]] += 1;
								histgG[pixel[0]] += 1;
								histgB[pixel[0]] += 1;
							}
								break;
							case 3:
							case 4: {
								pixel[0] = gridIntPixels[(i + j * width) * bc + 0];
								pixel[1] = gridIntPixels[(i + j * width) * bc + 1];
								pixel[2] = gridIntPixels[(i + j * width) * bc + 2];

								histgR[pixel[0]] += 1;
								histgG[pixel[1]] += 1;
								histgB[pixel[2]] += 1;
							}
								break;
							}
						}
					}

					// Min/Max By Histogram
					int[] stretchingMin2 = new int[3];
					int[] stretchingMax2 = new int[3];
					double[] stretchinSigma = new double[3];
					GImageEnhancement.LinearSigma sigmaType = GImageEnhancement.LinearSigma.LINEAR_3SIGMA;
					boolean isApplyedMinMax = false;

					imgEnhance.calcAutoHistogramMinMaxByLinearSigmaX(maxPixel, sigmaType, isApplyedMinMax, histgR,
							histgG, histgB, stretchingMin2, stretchingMax2, stretchinSigma);

					// Range per band (2)
					for (i = 0; i < 3; i++) {
						if (bc == 1) {
							vd[i] = (double) (stretchingMax2[0] - stretchingMin2[0]);
						} else {
							vd[i] = (double) (stretchingMax2[i] - stretchingMin2[i]);
						}
					}

					// Look Up Table
					for (i = 0; i < 3; i++) {
						for (j = 0; j < maxPixel + 1; j++) {
							if (bc == 1) {
								if (j < stretchingMin2[0])
									LUT[i][j] = 0;
								else if (j > stretchingMax2[0])
									LUT[i][j] = 255;
								else
									LUT[i][j] = (int) ((double) ((j - stretchingMin2[0]) * 256.f) / vd[i]);
							} else {
								if (j < stretchingMin2[i])
									LUT[i][j] = 0;
								else if (j > stretchingMax2[i])
									LUT[i][j] = 255;
								else
									LUT[i][j] = (int) ((double) ((j - stretchingMin2[i]) * 256.f) / vd[i]);
							}
						}
					}
				}
				// --------------------------------------------------------------------//

				try {
					for (j = 0; j < height; j++) {
						for (i = 0; i < width; i++) {
							for (k = 0; k < bc; k++) {
								pixelValue = gridIntPixels[(i + j * width) * bc + k];

								switch (bc) {
								case 1:
								case 3:
									tmpRaster.setSample(i, j, k, LUT[k][pixelValue]);
									break;
								case 4: {
									if (k < 3) { // 0, 1, 2
										tmpRaster.setSample(i, j, k, LUT[k][pixelValue]);
									}
								}
									break;
								}
							}
						}
					}
				} catch (ArrayIndexOutOfBoundsException ex) {
					System.out.println(
							"GImageProcessor.createThumbnailImage : (ArrayIndexOutOfBoundsException) " + ex.toString());
					ex.printStackTrace();
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]GImageProcessor.createThumbnailImage : ");
						System.out.println("[DEBUG]\t LUT[0].length : " + LUT[0].length);
						System.out.println("[DEBUG]\t LUT[1].length : " + LUT[1].length);
						System.out.println("[DEBUG]\t LUT[2].length : " + LUT[2].length);
						System.out.println("[DEBUG]\t i : " + i + ", j : " + j + ", k : " + k);
						System.out.println("[DEBUG]\t pixelValue : " + pixelValue);
					}
//[DEBUG]
				}
			}
				break;
			case DataBuffer.TYPE_BYTE: // 8bit
			default: {
				byte[] gridBytePixels = gdReader.getResamplePixelsByte(range, envelope, resampleMethod);
				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						for (int k = 0; k < bc; k++) {

							switch (bc) {
							case 1:
							case 3:
								tmpRaster.setSample(i, j, k, (int) gridBytePixels[(i + j * width) * bc + k]);
								break;
							case 4: {
								if (k < 3) { // 0, 1, 2
									tmpRaster.setSample(i, j, k, (int) gridBytePixels[(i + j * width) * bc + k]);
								}
							}
								break;
							}
						}
					}
				}
			}
				break;
			}

			// ������ ������ �̹����� ���� �̹����� ��� ������ ����,���� ���̿� �°� �����͸�
			// ����ش�.
			image = (Raster) tmpRaster;
			thumbImg = null;

			switch (tmpBC) {
			case 1:
				thumbImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
				break;
			case 3:
				thumbImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				break;
			}

			thumbImg.setData(image);

			if (outFilePath.isEmpty()) {
				lastIdx = inFilePath.lastIndexOf(".");
				outputName = inFilePath.substring(0, lastIdx);
				output = new File(outputName + "." + imgFormat.toString());
			} else {
				lastIdx = outFilePath.lastIndexOf(".");
				outputName = outFilePath.substring(0, lastIdx);
				output = new File(outputName + "." + imgFormat.toString());
			}

			ImageIO.write(thumbImg, imgFormat.toString(), output);
			thumbImg.flush();

		} catch (UnsupportedOperationException ex) {
			// "Unknown Format"
			System.out
					.println("GImageProcessor.createThumbnailImage : (UnsupportedOperationException) " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_READ;
		} catch (IOException ex) {
			System.out.println("GImageProcessor.createThumbnailImage : (IOException) " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////
	// [������]����� �̹��� ����

	// [������]�ش� ���� ���� Byteȭ �ȼ� ������ ���� ����� �̹����� �����Ѵ�.
	// @ inFilePath : �Է� ���� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ����� ������� ���
	// @ imgFormat : ����� ������� ���� (PNG, JPG)
	// @ thumbnailWidth : ����� ������� ���� ũ��
	// @ resampleMethod : ������迭 ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode createThumbnailImage_Old(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath, ImageFormat imgFormat, int thumbnailWidth,
			GTiffDataReader.ResamplingMethod resampleMethod) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GTiffDataReader gdReader = null;
		Envelope2D envelope = null;
		GridEnvelope2D range = new GridEnvelope2D();
		int width = thumbnailWidth;
		int height = thumbnailWidth;
		int bc = 1;
		int tmpBC = 1;
		Raster image = null;
		BufferedImage thumbImg = null;
		int lastIdx = 0;
		String outputName = "";
		File output = null;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.createThumbnailImage : ");
		}
//[DEBUG]

		// Read the input file
		try {

			// --------------------------------------------------------//
			// RGBN 4��� ���� ó�� �߰�
			// --------------------------------------------------------//
			// gdReader = new GTiffDataReader(inFilePath, inMaxBit16); //RGB 3��� ���Ϸ� ����
			// --------------------------------------------------------//
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16, false); // RGBA 4������ ���
			// --------------------------------------------------------//

		} catch (Exception ex) {
			System.out.println("GImageProcessor.createThumbnailImage : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		try {
			WritableRaster tmpRaster = null;
			int[] gridSize = gdReader.getGridSize();
			byte[] gridPixels = null;

			width = thumbnailWidth;
			height = (int) ((double) gridSize[1] * ((double) thumbnailWidth / (double) gridSize[0]));

			range.setBounds(0, 0, width, height);
			envelope = gdReader.getEnvelope();
			bc = gdReader.getBandCount();

			tmpBC = bc;
			if (bc == 4)
				tmpBC = 3;

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]GImageProcessor.createThumbnailImage : ");
				System.out.println("[DEBUG]\t Org Size : " + gridSize[0] + ", " + gridSize[1]);
				System.out.println("[DEBUG]\t Calc Size : " + width + ", " + height);
				System.out.println("[DEBUG]\t envelope : " + envelope.getSpan(0) + ", " + envelope.getSpan(1));
				System.out.println("[DEBUG]\t range : " + range.getWidth() + ", " + range.getHeight());
				System.out.println("[DEBUG]\t bc : " + bc + ", tmpBC : " + tmpBC);
			}
//[DEBUG]

			gridPixels = gdReader.getResamplePixelsByte(range, envelope, resampleMethod);
			tmpRaster = RasterFactory.createBandedRaster(java.awt.image.DataBuffer.TYPE_BYTE, width, height, tmpBC,
					null);
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					for (int k = 0; k < bc; k++) {

						switch (bc) {
						case 1:
						case 3:
							tmpRaster.setSample(i, j, k, (int) gridPixels[(i + j * width) * bc + k]);
							break;
						case 4: {
							if (k < 3) { // 0, 1, 2
								tmpRaster.setSample(i, j, k, (int) gridPixels[(i + j * width) * bc + k]);
							}
						}
							break;
						}
					}
				}
			}

			// ������ ������ �̹����� ���� �̹����� ��� ������ ����,���� ���̿� �°� �����͸�
			// ����ش�.
			image = (Raster) tmpRaster;
			thumbImg = null;

			switch (tmpBC) {
			case 1:
				thumbImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
				break;
			case 3:
				thumbImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				break;
			}

			thumbImg.setData(image);

			if (outFilePath.isEmpty()) {
				lastIdx = inFilePath.lastIndexOf(".");
				outputName = inFilePath.substring(0, lastIdx);
				output = new File(outputName + "." + imgFormat.toString());
			} else {
				lastIdx = outFilePath.lastIndexOf(".");
				outputName = outFilePath.substring(0, lastIdx);
				output = new File(outputName + "." + imgFormat.toString());
			}

			ImageIO.write(thumbImg, imgFormat.toString(), output);
			thumbImg.flush();

		} catch (UnsupportedOperationException ex) {
			// "Unknown Format"
			System.out
					.println("GImageProcessor.createThumbnailImage : (UnsupportedOperationException) " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_READ;
		} catch (IOException ex) {
			System.out.println("GImageProcessor.createThumbnailImage : (IOException) " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

//Remark
	/*
	 * //�ش� ���� ���� ����� �̹����� �����Ѵ�. // @ geotiffFilePath : �Է� GeoTiff
	 * ���� ��� // @ outFilePath : ������ ����� ������� ��� // @ imgFormat : �����
	 * ������� ���� (PNG, JPG) // @ thumbnailWidth : ����� ������� ���� ũ�� // @
	 * // @ return : ProcessCode ���� ��� ���� public ProcessCode
	 * createThumbnailImage(String geotiffFilePath, String outFilePath, ImageFormat
	 * imgFormat, int thumbnailWidth) { ProcessCode ret = ProcessCode.SUCCESS; File
	 * rasterFile = null; AbstractGridFormat format = null;
	 * AbstractGridCoverage2DReader reader = null; ParameterValue<GridGeometry2D> gg
	 * = null; GeneralEnvelope envelope = null; int width = thumbnailWidth; int
	 * height = thumbnailWidth; double stepWidth = 0; double stepHeight = 0;
	 * GeneralParameterValue[] params = new GeneralParameterValue[1]; Dimension dim
	 * = new Dimension(); Rectangle rasterArea = null; GridEnvelope2D range = null;
	 * GridCoverage2D coverage = null; int bc = 1; SampleDimensionType sdType =
	 * SampleDimensionType.SIGNED_8BITS; int dataBufferType = 0; Raster image =
	 * null; BufferedImage thumbImg = null; int lastIdx = 0; String outputName = "";
	 * File output = null;
	 * 
	 * //[DEBUG] if(_IS_DEBUG) {
	 * System.out.println("[DEBUG]GImageProcessor.createThumbnailImage : "); }
	 * //[DEBUG]
	 * 
	 * //Read the input file try{ rasterFile = new File(geotiffFilePath);
	 * 
	 * //File is ... if(!rasterFile.exists() || !rasterFile.isFile()) {
	 * System.out.println("GImageProcessor.createThumbnailImage : Exist no file - "
	 * + geotiffFilePath.toString());
	 * 
	 * ret = ProcessCode.ERROR_FAIL_READ; return ret; } }catch(Exception ex){
	 * System.out.println("GImageProcessor.createThumbnailImage : " +
	 * ex.toString()); ret = ProcessCode.ERROR_FAIL_READ;
	 * 
	 * return ret; }
	 * 
	 * try { //Find the grid format format =
	 * GridFormatFinder.findFormat(rasterFile);
	 * 
	 * // reader = format.getReader(rasterFile); gg =
	 * AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue(); envelope =
	 * reader.getOriginalEnvelope();
	 * 
	 * //����� �̹��� ���� ���� ���� //���� ���̿� �°� ���� ���� stepWidth =
	 * (double)reader.getOriginalGridRange().getSpan(0); stepHeight =
	 * (double)reader.getOriginalGridRange().getSpan(1);
	 * 
	 * if(reader.getOriginalGridRange().getSpan(0) > width) { while(true) {
	 * stepWidth /= 2.0f; stepHeight /= 2.0f; if(stepWidth < (double)width * 1.25)
	 * break; } } width = (int)stepWidth; height = (int)stepHeight;
	 * 
	 * dim.setSize(width,height); rasterArea =
	 * (GridEnvelope2D)reader.getOriginalGridRange();
	 * 
	 * //���� ������ dimension�� �ٽ� �������ش�. rasterArea.setSize(dim); range = new
	 * GridEnvelope2D(rasterArea); gg.setValue(new GridGeometry2D(range, envelope));
	 * 
	 * //[DEBUG] if(_IS_DEBUG) { System.out.println("[DEBUG]\t Org Size : " +
	 * reader.getOriginalGridRange().getSpan(0) + ", " +
	 * reader.getOriginalGridRange().getSpan(1));
	 * System.out.println("[DEBUG]\t Calc Size : " + width + ", " + height);
	 * System.out.println("[DEBUG]\t envelope : " + envelope.getSpan(0) + ", " +
	 * envelope.getSpan(1)); System.out.println("[DEBUG]\t dim : " + dim.getWidth()
	 * + ", " + dim.getHeight()); System.out.println("[DEBUG]\t rasterArea : " +
	 * rasterArea.getWidth() + ", " + rasterArea.getHeight());
	 * System.out.println("[DEBUG]\t range : " + range.getWidth() + ", " +
	 * range.getHeight()); } //[DEBUG]
	 * 
	 * params[0]=gg; coverage = reader.read(params); bc =
	 * coverage.getNumSampleDimensions(); sdType =
	 * coverage.getSampleDimension(0).getSampleDimensionType(); dataBufferType =
	 * TypeMap.getDataBufferType(sdType);
	 * 
	 * //������ ������ �̹����� ���� �̹����� ��� ������ ����,���� ���̿� �°� �����͸�
	 * ����ش�. image = coverage.getRenderedImage().getData(); thumbImg = null;
	 * 
	 * switch(bc) { case 1: { switch(dataBufferType){ case DataBuffer.TYPE_SHORT:
	 * case DataBuffer.TYPE_USHORT: thumbImg = new BufferedImage(width, height,
	 * BufferedImage.TYPE_USHORT_GRAY); break; case DataBuffer.TYPE_BYTE: case
	 * DataBuffer.TYPE_INT: thumbImg = new BufferedImage(width, height,
	 * BufferedImage.TYPE_BYTE_GRAY); break; case DataBuffer.TYPE_FLOAT: case
	 * DataBuffer.TYPE_DOUBLE: case DataBuffer.TYPE_UNDEFINED: default: thumbImg =
	 * new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); break; } }
	 * break; case 3: { switch(dataBufferType){ case DataBuffer.TYPE_BYTE: thumbImg
	 * = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR); break; case
	 * DataBuffer.TYPE_SHORT: case DataBuffer.TYPE_USHORT: thumbImg = new
	 * BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB); break; case
	 * DataBuffer.TYPE_INT: case DataBuffer.TYPE_FLOAT: case DataBuffer.TYPE_DOUBLE:
	 * thumbImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	 * break; case DataBuffer.TYPE_UNDEFINED: default: thumbImg = new
	 * BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR); break; } } break;
	 * }
	 * 
	 * thumbImg.setData(image);
	 * 
	 * if(outFilePath.isEmpty()) { lastIdx = geotiffFilePath.lastIndexOf(".");
	 * outputName = geotiffFilePath.substring(0, lastIdx); output = new
	 * File(outputName+"."+imgFormat.toString()); } else { lastIdx =
	 * outFilePath.lastIndexOf("."); outputName = outFilePath.substring(0, lastIdx);
	 * output = new File(outputName+"."+imgFormat.toString()); }
	 * 
	 * ImageIO.write(thumbImg, imgFormat.toString(), output);
	 * 
	 * reader.dispose(); thumbImg.flush();
	 * 
	 * } catch(UnsupportedOperationException ex) { //"Unknown Format" System.out.
	 * println("GImageProcessor.createThumbnailImage : (UnsupportedOperationException) "
	 * + ex.toString()); ex.printStackTrace();
	 * 
	 * ret = ProcessCode.ERROR_FAIL_READ; } catch (IOException ex) {
	 * System.out.println("GImageProcessor.createThumbnailImage : (IOException) " +
	 * ex.toString()); ex.printStackTrace();
	 * 
	 * ret = ProcessCode.ERROR_FAIL_WRITE; }
	 * 
	 * return ret; }
	 */

	////////////////////////////////////////////////////////////////////////////////
	// �ùķ�����(���󿵻� ����) : �Կ������� ���� ���� Resampling(Nearest Neighbor,
	//////////////////////////////////////////////////////////////////////////////// Bilinear,
	//////////////////////////////////////////////////////////////////////////////// Cubic
	//////////////////////////////////////////////////////////////////////////////// Convolution)
	// : 8 Bit Or 16 Bit 1 Band input -> 16 Bit 1 Band Output

	// �ش� ���� ���� ���󿵻��� �����Ѵ�.
	// @ inFilePath : �Է¿��� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ������� ���
	// @ outRes : ������� �ػ� (m)
	// @ outGridWidth : ������� ���� ũ��
	// @ outGridHeight : ������� ���� ũ��
	// @ shootAngle : �Կ����� (Degree)
	// @ resampleMethod : ������迭 ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procVirtualImageSimulation(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath, double outRes, int outGridWidth, int outGridHeight, double shootAngle,
			GTiffDataReader.ResamplingMethod resampleMethod) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GTiffDataReader gdReader = null;
		Envelope2D envelope = null;
		int dataBufferType = DataBuffer.TYPE_BYTE;

		GTiffDataWriter gdWriter = null;
		Coordinate coordCenter = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		Coordinate coord = new Coordinate();
		CoordinateReferenceSystem outCrs = null;
		double resX = outRes;
		double resY = outRes;
		double width = 0, height = 0;
		double pixelValue = 0;
		int[] outPixels = null;
		int i = 0, j = 0;
		double cosShoot = Math.cos(shootAngle * Math.PI / 180.0);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procVirtualImageSimulation : ");
		}
//[DEBUG]

		// Read the input file
		try {
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procVirtualImageSimulation : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Pprocessing
		try {
			gdWriter = new GTiffDataWriter();

			if (gdReader.getBandCount() != 1) {
				System.out.println("GImageProcessor.procVirtualImageSimulation : The 3 band file is not supported. - "
						+ inFilePath.toString());

				ret = ProcessCode.ERROR_NO_SUPPORTED_3BAND;

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
				return ret;
			} else {
				outPixels = new int[outGridWidth * outGridHeight];

				outCrs = gdReader.getCrs();
				envelope = gdReader.getEnvelope();
				coordCenter = new Coordinate(envelope.getCenterX(), envelope.getCenterY());

				// ��ǥ�谡 ���浵�� ��� UTM-K��ǥ�踦 �̿��Ͽ� �ػ� �缳��
				if (CRS.equalsIgnoreMetadata(outCrs, DefaultGeographicCRS.WGS84)) {
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t CRS.equalsIgnoreMetadata - Coordinate System is WGS84");
					}
//[DEBUG]

					CoordinateReferenceSystem utmkCrs = CRS.decode("EPSG:5179");
					MathTransform tranformWGS84ToUTMK = CRS.findMathTransform(outCrs, utmkCrs, false);
					MathTransform tranformUTMKToWGS84 = CRS.findMathTransform(utmkCrs, outCrs, false);
					GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

					// WGS84 -> UTMK
					Geometry srcGeometry = geometryFactory.createPoint(coordCenter);
					Geometry trgGeometry = JTS.transform(srcGeometry, tranformWGS84ToUTMK);

					// UTMK -> WGS84
					coord.x = trgGeometry.getCentroid().getX() + resX;
					coord.y = trgGeometry.getCentroid().getY() + resY;
					srcGeometry = geometryFactory.createPoint(coord);
					trgGeometry = JTS.transform(srcGeometry, tranformUTMKToWGS84);

					resX = Math.abs(trgGeometry.getCentroid().getX() - coordCenter.x);
					resY = Math.abs(trgGeometry.getCentroid().getY() - coordCenter.y);
				}

				width = outGridWidth * resX;
				height = outGridHeight * resY;

				coordLB = new Coordinate(coordCenter.x - width / 2.0, coordCenter.y - height / 2.0);
				coordRT = new Coordinate(coordCenter.x + width / 2.0, coordCenter.y + height / 2.0);

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Org Center : " + coordCenter.x + ", " + coordCenter.y);
					System.out.println("[DEBUG]\t Output inRes : " + outRes);
					System.out.println("[DEBUG]\t Output calcRes : " + resX + ", " + resY);
					System.out.println("[DEBUG]\t Output Size : " + outGridWidth + ", " + outGridHeight);
					System.out.println("[DEBUG]\t Output coordLB : " + coordLB.x + ", " + coordLB.y);
					System.out.println("[DEBUG]\t Output coordRT : " + coordRT.x + ", " + coordRT.y);
					// System.out.println("[DEBUG]\t Output CRS : " + outCrs.toString());
				}
//[DEBUG]	

				if (resampleMethod != GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR) {
					gdReader.loadPixelsInt();
//[DEBUG]				
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t Load all pixels integer ");
					}
//[DEBUG]	
				}

				for (j = 0; j < outGridHeight; j++) {
					for (i = 0; i < outGridWidth; i++) {
						coord.x = coordLB.x + (double) i * resX;
						coord.y = coordRT.y - (double) j * resY;

						pixelValue = (double) gdReader.getResamplePixel(coord, GTiffDataReader.RGBBand.RED_BAND,
								resampleMethod);
						pixelValue *= cosShoot;

						outPixels[i + j * outGridWidth] = (int) pixelValue;
					}
				}

//[DEBUG]				
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Resampling OK! ");
				}
//[DEBUG]					
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procVirtualImageSimulation : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file
		try {

			dataBufferType = gdReader.getDataType();
			switch (dataBufferType) {
			case DataBuffer.TYPE_SHORT:
			case DataBuffer.TYPE_USHORT: {
				if (!gdWriter.geoTiffWriter16Bit(outFilePath, outCrs, coordLB, coordRT, outGridWidth, outGridHeight,
						outPixels)) {
					ret = ProcessCode.ERROR_FAIL_WRITE;
				}
			}
				break;
			default: {
				byte[] tmpPixels = new byte[outGridWidth * outGridHeight];

				for (j = 0; j < outGridHeight; j++) {
					for (i = 0; i < outGridWidth; i++) {
						tmpPixels[i + j * outGridWidth] = (byte) (0xff & outPixels[i + j * outGridWidth]);
					}
				}

				if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, outGridWidth, outGridHeight, 1,
						tmpPixels)) {
					ret = ProcessCode.ERROR_FAIL_WRITE;
				}
			}
				break;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procVirtualImageSimulation : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////
	// ����⺸�� : 0�� ������ ȭ���� �ּҰ�(Dark Object)
	// : 1 Band Only

	// �ش� ���� ���� ����⺸���� �����Ѵ�.
	// @ inFilePath : �Է¿��� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ������� ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procRelativeAtmosphericCorrection(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GTiffDataReader gdReader = null;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		int[] pixels = null;
		int width = 0;
		int height = 0;
		int bc = 0;
		int dataBufferType = DataBuffer.TYPE_BYTE;
		int pixelShift = 0;
		boolean isFist = true;
		int i = 0, j = 0;

		// Read the input file
		try {
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procRelativeAtmosphericCorrection : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Pprocessing
		try {
			gdWriter = new GTiffDataWriter();
			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdReader.getCrs();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			bc = gdReader.getBandCount();
			dataBufferType = gdReader.getDataType();

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println(
						"[DEBUG]GImageProcessor.procRelativeAtmosphericCorrection : output CRS - " + outCrs.toString());
				System.out.println(
						"[DEBUG]GImageProcessor.procRelativeAtmosphericCorrection : output CRS - " + outCrs.toWKT());
			}
//[DEBUG]

			if (bc != 1) {
				System.out.println(
						"GImageProcessor.procRelativeAtmosphericCorrection : The 3 band file is not supported. - "
								+ inFilePath.toString());

				ret = ProcessCode.ERROR_NO_SUPPORTED_3BAND;

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
				return ret;
			} else {
				pixels = gdReader.getAllPixelsInt();

				// Find the dark object
				for (j = 0; j < height; j++) {
					for (i = 0; i < width; i++) {
						if (pixels[i + j * width] != 0) {
							if (isFist) {
								pixelShift = pixels[i + j * width];
								isFist = false;
							} else {
								pixelShift = Math.min(pixelShift, pixels[i + j * width]);
							}
						}
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t : pixelShift - " + pixelShift);
				}
//[DEBUG]

				// Shift the all pixel value about the 16bit value
				if (0 < pixelShift) {
					for (j = 0; j < height; j++) {
						for (i = 0; i < width; i++) {
							// Shift the pixel value
							if (pixels[i + j * width] <= pixelShift)
								pixels[i + j * width] = 0;
							else
								pixels[i + j * width] -= pixelShift;
						}
					}
				}

			}

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procRelativeAtmosphericCorrection : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file
		try {
			dataBufferType = gdReader.getDataType();
			switch (dataBufferType) {
			case DataBuffer.TYPE_SHORT:
			case DataBuffer.TYPE_USHORT: {
				if (!gdWriter.geoTiffWriter16Bit(outFilePath, outCrs, coordLB, coordRT, width, height, pixels)) {
					ret = ProcessCode.ERROR_FAIL_WRITE;
				}
			}
				break;
			default: {
				byte[] tmpPixels = new byte[width * height];

				for (j = 0; j < height; j++) {
					for (i = 0; i < width; i++) {
						tmpPixels[i + j * width] = (byte) (0xff & pixels[i + j * width]);
					}
				}

				if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, 1, tmpPixels)) {
					ret = ProcessCode.ERROR_FAIL_WRITE;
				}
			}
				break;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procRelativeAtmosphericCorrection : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////
	// �����纸�� : 1 Band Only
	// Landsat : Gain A, Offset B => A*pixel(Byte) + B = (Float)
	// : �Է� - ��Ÿ�������� Gain�� Offset�� => ����(TOA) ����
	// : ��� - TOA Reflectance(Atsensor reflectance) & ���翡����(Radiance)

	// �ش� Landsat ���� ���� �����纸���� �����Ѵ�.
	// @ inFilePath : �Է¿��� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ input : Landsat ������ �����纸���� ���� �Ķ����
	// @ outTOAReflectanceFilePath : ������ TOA Reflectance ������� ���
	// @ outRadianceFilePath : ������ ���翡����(Radiance) ������� ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procAbsoluteRadiatingCorrectionForLandsat(String inFilePath,
			GTiffDataReader.BIT16ToBIT8 inMaxBit16, AbsoluteRadiatingCorrectionLandsatInput input,
			String outTOAReflectanceFilePath, String outRadianceFilePath) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GTiffDataReader gdReader = null;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		double[] pixelsReflectance = null;
		double[] pixelsRadiance = null;
		int width = 0;
		int height = 0;
		int bc = 0;
		int i = 0, j = 0, pixelValue = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procAbsoluteRadiatingCorrectionForLandsat");
		}
//[DEBUG]		

		// Read the input file
		try {
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procAbsoluteRadiatingCorrectionForLandsat : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Pprocessing
		try {
			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdReader.getCrs();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			bc = gdReader.getBandCount();

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t : output CRS - " + outCrs.toString());
				System.out.println("[DEBUG]\t : output CRS - " + outCrs.toWKT());
			}
//[DEBUG]

			if (bc != 1) {
				System.out.println(
						"GImageProcessor.procAbsoluteRadiatingCorrectionForLandsat : The 3 band file is not supported. - "
								+ inFilePath.toString());

				ret = ProcessCode.ERROR_NO_SUPPORTED_3BAND;

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
				return ret;
			} else {
				pixelsReflectance = new double[width * height];
				pixelsRadiance = new double[width * height];

				// Calculate the reflectance and radiance
				for (j = 0; j < height; j++) {
					for (i = 0; i < width; i++) {
						pixelValue = gdReader.getValueByPixelInt(i, j, GTiffDataReader.RGBBand.RED_BAND);
						pixelsReflectance[i + j * width] = input.getReflectanceMultiple() * (double) pixelValue
								+ input.getReflectanceAddtion();
						pixelsRadiance[i + j * width] = input.getRadianceMultiple() * (double) pixelValue
								+ input.getRadianceAddtion();
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Calculate the correction OK!");
				}
//[DEBUG]	    		

			}

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procAbsoluteRadiatingCorrectionForLandsat : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file
		try {

			gdWriter = new GTiffDataWriter();

			// TOA Reflectance �������
			if (!outTOAReflectanceFilePath.isEmpty()) {
				if (pixelsReflectance != null) {
					if (!gdWriter.geoTiffWriter32BitFloat(outTOAReflectanceFilePath, outCrs, coordLB, coordRT, width,
							height, pixelsReflectance)) {
						ret = ProcessCode.ERROR_FAIL_WRITE;
					}
				}
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t Write the reflectance file OK!");
			}
//[DEBUG]	

			// ���翡����(Radiance) �������
			if (!outRadianceFilePath.isEmpty()) {
				if (pixelsRadiance != null) {
					if (!gdWriter.geoTiffWriter32BitFloat(outRadianceFilePath, outCrs, coordLB, coordRT, width, height,
							pixelsRadiance)) {
						ret = ProcessCode.ERROR_FAIL_WRITE;
					}
				}
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t Write the radiance file OK!");
			}
//[DEBUG]	

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procAbsoluteRadiatingCorrectionForLandsat : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	// �ش� Kompsat ���� ���� �����纸���� �����Ѵ�.
	// @ inFilePath : �Է¿��� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ input : Kompsat ������ �����纸���� ���� �Ķ����
	// @ outRadianceFilePath : ������ ���翡����(Radiance) ������� ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procAbsoluteRadiatingCorrectionForKompsat(String inFilePath,
			GTiffDataReader.BIT16ToBIT8 inMaxBit16, AbsoluteRadiatingCorrectionKompsatInputImpl input_k,
			String outRadianceFilePath) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GTiffDataReader gdReader = null;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		double[] pixelsRadiance = null;
		int width = 0;
		int height = 0;
		int bc = 0;
		int i = 0, j = 0, pixelValue = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procAbsoluteRadiatingCorrectionForKompsat");
		}
//[DEBUG]		

		// Read the input file
		try {
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procAbsoluteRadiatingCorrectionForKompsat : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Pprocessing
		try {
			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdReader.getCrs();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			bc = gdReader.getBandCount();

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t : output CRS - " + outCrs.toString());
				System.out.println("[DEBUG]\t : output CRS - " + outCrs.toWKT());
			}
//[DEBUG]

			if (bc != 1) {
				System.out.println(
						"GImageProcessor.procAbsoluteRadiatingCorrectionForKompsat : The 3 band file is not supported. - "
								+ inFilePath.toString());

				ret = ProcessCode.ERROR_NO_SUPPORTED_3BAND;

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
				return ret;
			} else {
				pixelsRadiance = new double[width * height];

				// Calculate the radiance
				for (j = 0; j < height; j++) {
					for (i = 0; i < width; i++) {
						pixelValue = gdReader.getValueByPixelInt(i, j, GTiffDataReader.RGBBand.RED_BAND);
						pixelsRadiance[i + j * width] = input_k.getRadianceMultiple() * (double) pixelValue
								+ input_k.getRadianceAddtion();
					}
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t Calculate the correction OK!");
				}
//[DEBUG]	    		

			}

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procAbsoluteRadiatingCorrectionForKompsat : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file
		try {

			gdWriter = new GTiffDataWriter();

			// ���翡����(Radiance) �������
			if (!outRadianceFilePath.isEmpty()) {
				if (pixelsRadiance != null) {
					if (!gdWriter.geoTiffWriter32BitFloat(outRadianceFilePath, outCrs, coordLB, coordRT, width, height,
							pixelsRadiance)) {
						ret = ProcessCode.ERROR_FAIL_WRITE;
					}
				}
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t Write the radiance file OK!");
			}
//[DEBUG]	

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procAbsoluteRadiatingCorrectionForKompsat : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	// �ش� Sentinel2 ���� ���� �����纸���� �����Ѵ�.
	// @todo : ??????

	////////////////////////////////////////////////////////////////////////////////
	// ����纸�� : Histogram Matching (���� ���� 1, ��󿵻� 1)
	// : 1 Band & 3 Band

	// ���ؿ��� ���� ��󿵻��� ��ø������ ��ȯ�Ѵ�.
	// @ srcFilePath : ���ؿ��� ���
	// @ trgFilePath : ��󿵻� ���
	// @
	// @ return : double ���� ��ø���� (%)
	public double procCalcOverlapRate(String srcFilePath, String trgFilePath) {
		double overlapRate = 0;
		GTiffDataReader gdSrcReader = null;
		GTiffDataReader gdTrgReader = null;
		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procCalcOverlapRate : ");
		}
//[DEBUG]		

		// Read the input file
		try {
			gdSrcReader = new GTiffDataReader(srcFilePath, maxBit16);
			gdTrgReader = new GTiffDataReader(trgFilePath, maxBit16);

			if (gdSrcReader.IsOpened() && gdTrgReader.IsOpened()) {
				if (gdSrcReader.IsAbsoluteCoordinateSystem() && gdTrgReader.IsAbsoluteCoordinateSystem()) {
					GridEnvelope2D srcGridEnvelope = gdSrcReader.getGridEnvelope();
					Envelope2D srcEnvelope = gdSrcReader.getEnvelope();
					Envelope2D trgEnvelope = gdTrgReader.getEnvelope();
					Rectangle2D overlap = srcEnvelope.createIntersection(trgEnvelope.getBounds2D());
					double resX = 0;
					double resY = 0;
					int ovGridWidth = 0;
					int ovGridHeight = 0;

					if (srcEnvelope.getWidth() == 0 || srcEnvelope.getHeight() == 0 || srcGridEnvelope.getWidth() == 0
							|| srcGridEnvelope.getHeight() == 0) {
						overlapRate = 0;

						if (gdSrcReader != null)
							gdSrcReader.destory();
						gdSrcReader = null;
						if (gdTrgReader != null)
							gdTrgReader.destory();
						gdTrgReader = null;
						return overlapRate;
					}

					resX = srcEnvelope.getWidth() / srcGridEnvelope.getWidth();
					resY = srcEnvelope.getHeight() / srcGridEnvelope.getHeight();
					ovGridWidth = (int) (overlap.getWidth() / resX + 0.5);
					ovGridHeight = (int) (overlap.getHeight() / resY + 0.5);
					overlapRate = (double) (ovGridWidth * ovGridHeight)
							/ (srcGridEnvelope.getWidth() * srcGridEnvelope.getHeight()) * 100;

//[DEBUG]		
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t resX : " + resX + ", resY : " + resY);
						System.out.println("[DEBUG]\t scrGridWidth : " + srcGridEnvelope.getWidth()
								+ ", srcGridHeight : " + srcGridEnvelope.getHeight());
						System.out.println(
								"[DEBUG]\t ovWidth : " + overlap.getWidth() + ", ovdHeight : " + overlap.getHeight());
						System.out
								.println("[DEBUG]\t ovGridWidth : " + ovGridWidth + ", ovGridHeight : " + ovGridHeight);
					}
//[DEBUG]
				}
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procCalcOverlapRate : " + ex.toString());
			ex.printStackTrace();

			overlapRate = 0;
		}

		if (gdSrcReader != null)
			gdSrcReader.destory();
		gdSrcReader = null;
		if (gdTrgReader != null)
			gdTrgReader.destory();
		gdTrgReader = null;
		return overlapRate;
	}

	// ���ؿ��� ���� ��󿵻��� ��ø������ ��ȯ�Ѵ�.
	// @ srcFilePath : ���ؿ��� ���
	// @ trgFilePath : ��󿵻� ���
	// @
	// @ return : Rectangle2D ���� ��ø����
	public Rectangle2D procCalcOverlapArea(String srcFilePath, String trgFilePath) {
		Rectangle2D overlap = new Rectangle2D.Double(0, 0, 0, 0);
		GTiffDataReader gdSrcReader = null;
		GTiffDataReader gdTrgReader = null;
		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procCalcOverlapArea : ");
		}
//[DEBUG]		

		// Read the input file
		try {
			gdSrcReader = new GTiffDataReader(srcFilePath, maxBit16);
			gdTrgReader = new GTiffDataReader(trgFilePath, maxBit16);

			if (gdSrcReader.IsOpened() && gdTrgReader.IsOpened()) {
				if (gdSrcReader.IsAbsoluteCoordinateSystem() && gdTrgReader.IsAbsoluteCoordinateSystem()) {
					Envelope2D srcEnvelope = gdSrcReader.getEnvelope();
					Envelope2D trgEnvelope = gdTrgReader.getEnvelope();

					overlap = srcEnvelope.createIntersection((Rectangle2D) trgEnvelope.getBounds2D());

//[DEBUG]
					if (_IS_DEBUG) {
						GridEnvelope2D srcGridEnvelope = gdSrcReader.getGridEnvelope();
						double resX = srcEnvelope.getWidth() / srcGridEnvelope.getWidth();
						double resY = srcEnvelope.getHeight() / srcGridEnvelope.getHeight();
						int ovGridWidth = (int) (overlap.getWidth() / resX + 0.5);
						int ovGridHeight = (int) (overlap.getHeight() / resY + 0.5);

						System.out.println("[DEBUG]\t ovX : " + overlap.getX() + ", ovY : " + overlap.getX());
						System.out.println(
								"[DEBUG]\t ovWidth : " + overlap.getWidth() + ", ovHeight : " + overlap.getHeight());
						System.out
								.println("[DEBUG]\t ovGridWidth : " + ovGridWidth + ", ovGridHeight : " + ovGridHeight);
					}
//[DEBUG]
				}
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procCalcOverlapRate : " + ex.toString());
			ex.printStackTrace();

			overlap = new Rectangle2D.Double(0, 0, 0, 0);
		}

		if (gdSrcReader != null)
			gdSrcReader.destory();
		gdSrcReader = null;
		if (gdTrgReader != null)
			gdTrgReader.destory();
		gdTrgReader = null;
		return overlap;
	}

	// ���ؿ��� ���� ��󿵻��� ����纸���� �����Ѵ�.
	// @ srcFilePath : ���ؿ��� ���
	// @ srcMaxBit16 : ���ؿ����� 16��Ʈ �ִ� ��Ʈ ����
	// @ trgFilePath : ��󿵻� ���
	// @ trgMaxBit16 : ��󿵻��� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ����纸�� ������� ���
	// @ isApplyOverlapArea : ���ؿ����� ��ø���� Histogram ���� ���� (true :
	// ���ؿ����� ��ø���� Histogram ����, false : ���ؿ����� ��ü���� Histogram ����)
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procRelativeRadiatingCorrection(String srcFilePath, GTiffDataReader.BIT16ToBIT8 srcMaxBit16,
			String trgFilePath, GTiffDataReader.BIT16ToBIT8 trgMaxBit16, String outFilePath,
			boolean isApplyOverlapArea) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GImageEnhancement.HistorgramMatchingMethod histogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT;
		GImageEnhancement imgEnhance = new GImageEnhancement();
		GTiffDataReader gdSrcReader = null;
		GTiffDataReader gdTrgReader = null;
		Rectangle2D overlap = procCalcOverlapArea(srcFilePath, trgFilePath); // ���뿵��

		GTiffDataWriter gdWriter = null;
		Envelope2D trgEnvelope = null;
		GridEnvelope2D srcGridEnvelope = null;
		GridEnvelope2D trgGridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		int srcBC = 0;
		int trgWidth = 0;
		int trgHeight = 0;
		int trgBC = 0;
		GHistogramMatchingData[] histMatchInfos = new GHistogramMatchingData[3];
		GHistogramMatchingData histMatchInfo = null;
		byte[] srcPixels = null;
		byte[] trgPixels = null;
		byte[] srcOverlapPixels = null;
		byte[] trgOverlapPixels = null;
		byte[] masterPixels = null;
		byte[] slavePixels = null;
		GridEnvelope2D masterGridEnvelope = null;
		GridEnvelope2D slaveGridEnvelope = null;
		int masterWidth = 0;
		int masterHeight = 0;
		int slaveWidth = 0;
		int slaveHeight = 0;
		boolean isOverlap = false;
		int i = 0, j = 0, k = 0;
		double dblMatched_DN = 0; // ȭ�Ұ� ���� ó�� (10.19)
		int Matched_DN = 0;
		int nIndex = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procRelativeRadiatingCorrection : ");
		}
//[DEBUG]		

		// Read the input file
		try {
			gdSrcReader = new GTiffDataReader(srcFilePath, srcMaxBit16);
			gdTrgReader = new GTiffDataReader(trgFilePath, trgMaxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procRelativeRadiatingCorrection : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdSrcReader != null)
				gdSrcReader.destory();
			gdSrcReader = null;
			if (gdTrgReader != null)
				gdTrgReader.destory();
			gdTrgReader = null;
			return ret;
		}

		if (!gdSrcReader.IsOpened() || !gdTrgReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdSrcReader != null)
				gdSrcReader.destory();
			gdSrcReader = null;
			if (gdTrgReader != null)
				gdTrgReader.destory();
			gdTrgReader = null;
			return ret;
		}

		// Processing
		try {
			gdWriter = new GTiffDataWriter();

			srcGridEnvelope = gdSrcReader.getGridEnvelope();
			srcBC = gdSrcReader.getBandCount();

			trgEnvelope = gdTrgReader.getEnvelope();
			trgGridEnvelope = gdTrgReader.getGridEnvelope();
			trgWidth = (int) trgGridEnvelope.getWidth();
			trgHeight = (int) trgGridEnvelope.getHeight();
			trgBC = gdTrgReader.getBandCount();

			coordLB = new Coordinate(trgEnvelope.getMinX(), trgEnvelope.getMinY());
			coordRT = new Coordinate(trgEnvelope.getMaxX(), trgEnvelope.getMaxY());
			outCrs = gdTrgReader.getCrs();

			trgPixels = gdTrgReader.getAllPixelsByte();

			if (overlap.getWidth() == 0 || overlap.getHeight() == 0) {
				isOverlap = false;
			} else {
				if (isApplyOverlapArea)
					isOverlap = true;
				else
					isOverlap = false;
			}

			if (isOverlap) {
				Coordinate ovLB = new Coordinate(overlap.getMinX(), overlap.getMinY());
				Coordinate ovRT = new Coordinate(overlap.getMaxX(), overlap.getMaxY());
				Rectangle srcGridRec = gdSrcReader.convertEnvelopeToGridEnvelope(ovLB, ovRT);
				Rectangle trgGridRec = gdTrgReader.convertEnvelopeToGridEnvelope(ovLB, ovRT);

				masterWidth = (int) srcGridRec.getWidth();
				masterHeight = (int) srcGridRec.getHeight();
				masterPixels = new byte[masterWidth * masterHeight];
				masterGridEnvelope = new GridEnvelope2D();
				masterGridEnvelope.setBounds(0, 0, masterWidth, masterHeight);

				slaveWidth = (int) trgGridRec.getWidth();
				slaveHeight = (int) trgGridRec.getHeight();
				slavePixels = new byte[slaveWidth * slaveHeight];
				slaveGridEnvelope = new GridEnvelope2D();
				slaveGridEnvelope.setBounds(0, 0, slaveWidth, slaveHeight);

				srcOverlapPixels = gdSrcReader.getAllPixelsByteOfGridEnvelope(srcGridRec);
				trgOverlapPixels = gdTrgReader.getAllPixelsByteOfGridEnvelope(trgGridRec);
			} else {
				masterGridEnvelope = srcGridEnvelope.clone();
				masterWidth = (int) masterGridEnvelope.getWidth();
				masterHeight = (int) masterGridEnvelope.getHeight();
				masterPixels = new byte[masterWidth * masterHeight];

				slaveGridEnvelope = trgGridEnvelope.clone();
				slaveWidth = (int) slaveGridEnvelope.getWidth();
				slaveHeight = (int) slaveGridEnvelope.getHeight();
				slavePixels = new byte[slaveWidth * slaveHeight];

				srcPixels = gdSrcReader.getAllPixelsByte();
			}

			// ---------------------------------------------------------------------------------//
			// ��庰 ������׷� ��Ī ���� ���
			// ---------------------------------------------------------------------------------//
			for (k = 0; k < trgBC; k++) {
				// Master Pixel
				for (j = 0; j < masterHeight; j++) {
					for (i = 0; i < masterWidth; i++) {
						if (isOverlap) {
							switch (srcBC) {
							case 1:
								masterPixels[i + j * masterWidth] = srcOverlapPixels[i + j * masterWidth];
								break;
							case 3:
								masterPixels[i + j * masterWidth] = srcOverlapPixels[(i + j * masterWidth) * srcBC + k];
								break;
							}
						} else {
							switch (srcBC) {
							case 1:
								masterPixels[i + j * masterWidth] = srcPixels[i + j * masterWidth];
								break;
							case 3:
								masterPixels[i + j * masterWidth] = srcPixels[(i + j * masterWidth) * srcBC + k];
								break;
							}
						}
					}
				}

				// Slave Pixel
				for (j = 0; j < slaveHeight; j++) {
					for (i = 0; i < slaveWidth; i++) {
						if (isOverlap) {
							slavePixels[i + j * slaveWidth] = trgOverlapPixels[(i + j * slaveWidth) * trgBC + k];
						} else {
							slavePixels[i + j * slaveWidth] = trgPixels[(i + j * slaveWidth) * trgBC + k];
						}
					}
				}

				histMatchInfos[k] = imgEnhance.calcHistogramMatchingInfo(histogramMatchingMethod, masterPixels,
						masterGridEnvelope, slavePixels, slaveGridEnvelope, isOverlap);

//[DEBUG]	
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t histMatchInfos[" + k + "] : ");
					if (histogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATCH_MEAN_STD_DEV) {
						System.out.println("[DEBUG]\t\t Match Mean & Std. Dev. : ");
						System.out.println("[DEBUG]\t\t\t _dblMeanMaster : " + histMatchInfos[k]._dblMeanMaster);
						System.out.println("[DEBUG]\t\t\t _dblMeanSlave : " + histMatchInfos[k]._dblMeanSlave);
						System.out.println("[DEBUG]\t\t\t _dblVariance : " + histMatchInfos[k]._dblVariance);
						System.out.println("[DEBUG]\t\t\t _dblCovariance : " + histMatchInfos[k]._dblCovariance);
					} else if (histogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATH_CUMULATIVE_FREQUENCY) {
						System.out.println("[DEBUG]\t\t Match Cumulative Frequency : ");
						System.out.println("[DEBUG]\t\t\t _nMinMaster : " + histMatchInfos[k]._nMinMaster);
						System.out.println("[DEBUG]\t\t\t _nMinSlave : " + histMatchInfos[k]._nMinSlave);
						System.out.println("[DEBUG]\t\t\t _nMaxMaster : " + histMatchInfos[k]._nMaxMaster);
						System.out.println("[DEBUG]\t\t\t _nMaxSlave : " + histMatchInfos[k]._nMaxSlave);
						System.out.println("[DEBUG]\t\t\t _dblMeanMaster : " + histMatchInfos[k]._dblMeanMaster);
						System.out.println("[DEBUG]\t\t\t _dblMeanSlave : " + histMatchInfos[k]._dblMeanSlave);
						if (histMatchInfos[k]._nLUTModified != null) {
							for (int l = 0; l < histMatchInfos[k]._nLUTModified.length; l++) {
								System.out.println("[DEBUG]\t\t\t _nLUTModified[" + l + "] : "
										+ histMatchInfos[k]._nLUTModified[l]);
							}
						}
					} else if (histogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT) {
						System.out.println("[DEBUG]\t\t Hue Adjustment & Match Cumulative Frequency : ");
						System.out.println("[DEBUG]\t\t\t _nMinMaster : " + histMatchInfos[k]._nMinMaster);
						System.out.println("[DEBUG]\t\t\t _nMinSlave : " + histMatchInfos[k]._nMinSlave);
						System.out.println("[DEBUG]\t\t\t _nMaxMaster : " + histMatchInfos[k]._nMaxMaster);
						System.out.println("[DEBUG]\t\t\t _nMaxSlave : " + histMatchInfos[k]._nMaxSlave);
						System.out.println("[DEBUG]\t\t\t _dblMeanMaster : " + histMatchInfos[k]._dblMeanMaster);
						System.out.println("[DEBUG]\t\t\t _dblMeanSlave : " + histMatchInfos[k]._dblMeanSlave);
					}
				}
//[DEBUG]

			}
			// ---------------------------------------------------------------------------------//

			// ---------------------------------------------------------------------------------//
			// ��庰 ������׷� ��Ī ���� ����
			// ---------------------------------------------------------------------------------//
			for (k = 0; k < trgBC; k++) {
				for (j = 0; j < trgHeight; j++) {
					for (i = 0; i < trgWidth; i++) {

						nIndex = (i + j * slaveWidth) * trgBC + k;
						histMatchInfo = histMatchInfos[k];

						if (histogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATCH_MEAN_STD_DEV) {

							if ((0xff & trgPixels[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & trgPixels[nIndex]) - histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								trgPixels[nIndex] = (byte) Matched_DN;
							}

						} else if (histogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATH_CUMULATIVE_FREQUENCY) {

							if ((0xff & trgPixels[nIndex]) != 0) {
								Matched_DN = (0xff & trgPixels[nIndex]);
								trgPixels[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							}

						} else if (histogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT) {

							if ((0xff & trgPixels[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & trgPixels[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								trgPixels[nIndex] = (byte) Matched_DN;
							}

						}
					}
				}
				// ---------------------------------------------------------------------------------//
			}

//[DEBUG]	
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t ovLB : " + overlap.getMinX() + ", " + overlap.getMinY());
				System.out.println("[DEBUG]\t ovRT : " + overlap.getMaxX() + ", " + overlap.getMaxY());
				System.out.println("[DEBUG]\t target pixels size : " + trgPixels.length);
				System.out.println(
						"[DEBUG]\t trgBC : " + trgBC + ", trgWidth = " + trgWidth + ", trgHeight = " + trgHeight);
			}
//[DEBUG]
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procRelativeRadiatingCorrection : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdSrcReader != null)
				gdSrcReader.destory();
			gdSrcReader = null;
			if (gdTrgReader != null)
				gdTrgReader.destory();
			gdTrgReader = null;
			return ret;
		}

		// Write the output file
		try {
			if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, trgWidth, trgHeight, trgBC, trgPixels)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procRelativeRadiatingCorrection : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdSrcReader != null)
			gdSrcReader.destory();
		gdSrcReader = null;
		if (gdTrgReader != null)
			gdTrgReader.destory();
		gdTrgReader = null;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////
	// �÷� �ռ�: 1 Band Only

	// �ش� ��庰 ���� ���� �÷� �ռ��� �����Ѵ�.
	// @ inRedFilePath : Red ��� �Է¿��� ���
	// @ inRedMaxBit16 : Red ��� �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ inGreenFilePath : Green ��� �Է¿��� ���
	// @ inGreenMaxBit16 : Green ��� �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ inBlueFilePath : Blue ��� �Է¿��� ���
	// @ inBlueMaxBit16 : Blue ��� �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ �÷��ռ� ������� ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procColorComposite(String inRedFilePath, GTiffDataReader.BIT16ToBIT8 inRedMaxBit16,
			String inGreenFilePath, GTiffDataReader.BIT16ToBIT8 inGreenMaxBit16, String inBlueFilePath,
			GTiffDataReader.BIT16ToBIT8 inBlueMaxBit16, String outFilePath) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GTiffDataReader gdRedReader = null;
		GTiffDataReader gdGreenReader = null;
		GTiffDataReader gdBlueReader = null;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		int width = 0;
		int height = 0;
		int bc = 3;
		byte[] pixels = null;
		int i = 0, j = 0;

		// Read the input file
		try {
			gdRedReader = new GTiffDataReader(inRedFilePath, inRedMaxBit16);
			gdGreenReader = new GTiffDataReader(inGreenFilePath, inGreenMaxBit16);
			gdBlueReader = new GTiffDataReader(inBlueFilePath, inBlueMaxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procColorComposite : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdRedReader != null)
				gdRedReader.destory();
			gdRedReader = null;
			if (gdGreenReader != null)
				gdGreenReader.destory();
			gdGreenReader = null;
			if (gdBlueReader != null)
				gdBlueReader.destory();
			gdBlueReader = null;
			return ret;
		}

		if (!gdRedReader.IsOpened() || !gdGreenReader.IsOpened() || !gdBlueReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdRedReader != null)
				gdRedReader.destory();
			gdRedReader = null;
			if (gdGreenReader != null)
				gdGreenReader.destory();
			gdGreenReader = null;
			if (gdBlueReader != null)
				gdBlueReader.destory();
			gdBlueReader = null;
			return ret;
		}

		// Processing
		try {
			gdWriter = new GTiffDataWriter();
			envelope = gdRedReader.getEnvelope();
			gridEnvelope = gdRedReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdRedReader.getCrs();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			pixels = new byte[width * height * bc];

			if (gdRedReader.getBandCount() != 1 || gdGreenReader.getBandCount() != 1
					|| gdBlueReader.getBandCount() != 1) {
				System.out.println("GImageProcessor.procColorComposite : The 3 band file is not supported. - "
						+ inRedFilePath.toString() + ", " + inGreenFilePath.toString() + ", "
						+ inBlueFilePath.toString());

				ret = ProcessCode.ERROR_NO_SUPPORTED_3BAND;

				if (gdRedReader != null)
					gdRedReader.destory();
				gdRedReader = null;
				if (gdGreenReader != null)
					gdGreenReader.destory();
				gdGreenReader = null;
				if (gdBlueReader != null)
					gdBlueReader.destory();
				gdBlueReader = null;
				return ret;
			} else {
				GImageEnhancement imgEnhance = new GImageEnhancement();
				int[][] histgR = new int[3][65536];
				int[][] histgG = new int[3][65536];
				int[][] histgB = new int[3][65536];
				int[][] stretchingMin = new int[3][3];
				int[][] stretchingMax = new int[3][3];
				int[][] stretchingMin2 = new int[3][3];
				int[][] stretchingMax2 = new int[3][3];
				double[][] stretchinSigma = new double[3][3];
				int[] stretchingMinResult = new int[3];
				int[] stretchingMaxResult = new int[3];
				int[] redIntPixels = gdRedReader.getAllPixelsInt();
				int[] greenIntPixels = gdGreenReader.getAllPixelsInt();
				int[] blueIntPixels = gdBlueReader.getAllPixelsInt();
				int[] redGridSize = gdRedReader.getGridSize();
				int[] greenGridSize = gdGreenReader.getGridSize();
				int[] blueGridSize = gdBlueReader.getGridSize();
				GImageEnhancement.LinearSigma sigmaType = GImageEnhancement.LinearSigma.LINEAR_3SIGMA;
				boolean isApplyedMinMax = false;
				double SigmaBase = 1;

				if (sigmaType == LinearSigma.LINEAR_1SIGMA)
					SigmaBase = 1;
				else if (sigmaType == LinearSigma.LINEAR_2SIGMA)
					SigmaBase = 2;
				else if (sigmaType == LinearSigma.LINEAR_3SIGMA)
					SigmaBase = 3;

				gdRedReader.getHistogram16(histgR[0], histgG[0], histgB[0]);
				gdGreenReader.getHistogram16(histgR[1], histgG[1], histgB[1]);
				gdBlueReader.getHistogram16(histgR[2], histgG[2], histgB[2]);

				// 1. �� ����� ������׷� ���� �� �ִ�, �ּҰ� ����
				// - �� ��� ������ ȭ�Ұ� �� ���� ���� �� �� ū �� ���� (0 ����)
				// - �� ��庰�� 1sigma�� �ش��ϴ� �� ����
				// - �� ��庰�� �ּҰ�+1sigma, �ִ밪-1sigma �� ��� �� �� �� ��� ������ �ִ밪, �ּҰ� ����

				imgEnhance.calcAutoHistogramMinMax16(histgR[0], histgG[0], histgB[0], stretchingMin[0],
						stretchingMax[0]);
				imgEnhance.calcAutoHistogramMinMax16(histgR[1], histgG[1], histgB[1], stretchingMin[1],
						stretchingMax[1]);
				imgEnhance.calcAutoHistogramMinMax16(histgR[2], histgG[2], histgB[2], stretchingMin[2],
						stretchingMax[2]);

				histgR[0][0] = histgG[0][0] = histgB[0][0] = 0;
				histgR[1][0] = histgG[1][0] = histgB[1][0] = 0;
				histgR[2][0] = histgG[2][0] = histgB[2][0] = 0;

				histgR[0][65535] = histgG[0][65535] = histgB[0][65535] = 0;
				histgR[1][65535] = histgG[1][65535] = histgB[1][65535] = 0;
				histgR[2][65535] = histgG[2][65535] = histgB[2][65535] = 0;

				imgEnhance.calcAutoHistogramMinMaxByLinearSigma16(sigmaType, isApplyedMinMax, histgR[0], histgG[0],
						histgB[0], stretchingMin2[0], stretchingMax2[0], stretchinSigma[0]);
				imgEnhance.calcAutoHistogramMinMaxByLinearSigma16(sigmaType, isApplyedMinMax, histgR[1], histgG[1],
						histgB[1], stretchingMin2[1], stretchingMax2[1], stretchinSigma[1]);
				imgEnhance.calcAutoHistogramMinMaxByLinearSigma16(sigmaType, isApplyedMinMax, histgR[2], histgG[2],
						histgB[2], stretchingMin2[2], stretchingMax2[2], stretchinSigma[2]);

				for (i = 0; i < 3; i++) {
					// - �� ��庰�� ���+1sigma, ���-1sigma �� ��� �� �� �� ��� ������ �ִ밪, �ּҰ� ����
					stretchingMin[0][i] = stretchingMin2[0][i] + (int) (stretchinSigma[0][i]);
					stretchingMin[1][i] = stretchingMin2[1][i] + (int) (stretchinSigma[1][i]);
					stretchingMin[2][i] = stretchingMin2[2][i] + (int) (stretchinSigma[2][i]);

					stretchingMax[0][i] = stretchingMax2[0][i] - (int) (stretchinSigma[0][i]);
					stretchingMax[1][i] = stretchingMax2[1][i] - (int) (stretchinSigma[1][i]);
					stretchingMax[2][i] = stretchingMax2[2][i] - (int) (stretchinSigma[2][i]);
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]GImageProcessor.procColorComposite : ");
					System.out.println("[DEBUG]\t Min : " + stretchingMin[0][0] + ", " + stretchingMin[1][0] + ", "
							+ stretchingMin[2][0]);
					System.out.println("[DEBUG]\t Max : " + stretchingMax[0][0] + ", " + stretchingMax[1][0] + ", "
							+ stretchingMax[2][0]);
				}
//[DEBUG]	

				// 2. ��ü���� �ִ�, �ּҰ� ����
				// - �� ��庰 �ּҰ� �� ���� ���� ���� �ּҰ�(MIN)���� ����
				// - �� ��庰 �ִ밪 �� ���� ū ���� �ִ밪(MAX)���� ����
				for (i = 0; i < 3; i++) {
					//////////////////////////////////////////////////////////////////////////////////
					// [Remork] - ��ü �ּ�, �ִ�
					//////////////////////////////////////////////////////////////////////////////////
					// stretchingMinResult[i] = Math.min(stretchingMin[0][i], stretchingMin[1][i]);
					// stretchingMinResult[i] = Math.min(stretchingMinResult[i],
					////////////////////////////////////////////////////////////////////////////////// stretchingMin[2][i]);
					//
					// stretchingMaxResult[i] = Math.max(stretchingMax[0][i], stretchingMax[1][i]);
					// stretchingMaxResult[i] = Math.max(stretchingMaxResult[i],
					////////////////////////////////////////////////////////////////////////////////// stretchingMax[2][i]);
					//////////////////////////////////////////////////////////////////////////////////
					// [Modify] ��庰 �ּ�, �ִ�
					stretchingMinResult[i] = stretchingMin[i][0];
					stretchingMaxResult[i] = stretchingMax[i][0];
					//////////////////////////////////////////////////////////////////////////////////
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]GImageProcessor.procColorComposite : ");
					System.out.println("[DEBUG]\t Result Min : " + stretchingMinResult[0] + ", "
							+ stretchingMinResult[1] + ", " + stretchingMinResult[2]);
					System.out.println("[DEBUG]\t Result Max : " + stretchingMaxResult[0] + ", "
							+ stretchingMaxResult[1] + ", " + stretchingMaxResult[2]);
				}
//[DEBUG]	

				// 3. 8bit ���� ��ȯ
				// - 2������ ������ ��ü ������ MIN�� MAX�� �̿��Ͽ� 8bit ���� ��ȯ
				// - MIN = 1 (�������󿡼� MIN ���� ���� ȭ�Ұ��� ��� 0���� �Ҵ�)
				// - MAX = 254 (���� ���󿡼� MAX ���� ū ȭ�Ұ��� ��� 255�� �Ҵ�)
				// - �߰� ������ MIN~MAX ������ �������� �й��Ͽ� ȭ�Ұ� �Ҵ�
				int[][] LUT = new int[3][65536];
				int[][] redLUT = new int[3][65536];
				int[][] greenLUT = new int[3][65536];
				int[][] blueLUT = new int[3][65536];
				double[] vd = new double[3];

				// Range per band
				for (i = 0; i < 3; i++) {
					vd[i] = (double) (stretchingMaxResult[i] - stretchingMinResult[i]);
				}

				// Look Up Table
				for (i = 0; i < 3; i++) {
					for (j = 0; j < 65536; j++) {
						if (j < stretchingMinResult[i])
							LUT[i][j] = 0;
						else if (j > stretchingMaxResult[i])
							LUT[i][j] = 255;
						else
							LUT[i][j] = (int) (((double) (j - stretchingMinResult[i]) * 253.0f) / vd[i] + 1.f);

						if (LUT[i][j] <= 0)
							LUT[i][j] = 0;
						else if (LUT[i][j] >= 255)
							LUT[i][j] = 255;
					}
				}

				for (j = 0; j < 65536; j++) {
					redLUT[0][j] = redLUT[1][j] = redLUT[2][j] = LUT[0][j];
					greenLUT[0][j] = greenLUT[1][j] = greenLUT[2][j] = LUT[1][j];
					blueLUT[0][j] = blueLUT[1][j] = blueLUT[2][j] = LUT[2][j];
				}

				imgEnhance.applyLUT16(redIntPixels, gdRedReader.getBandCount(), redGridSize[0], redGridSize[1], redLUT);
				imgEnhance.applyLUT16(greenIntPixels, gdGreenReader.getBandCount(), redGridSize[0], redGridSize[1],
						greenLUT);
				imgEnhance.applyLUT16(blueIntPixels, gdBlueReader.getBandCount(), redGridSize[0], redGridSize[1],
						blueLUT);

				for (j = 0; j < height; j++) {
					for (i = 0; i < width; i++) {
						// Initialize
						pixels[(i + j * width) * bc + 0] = 0;

						// Color Composite
						if (i < redGridSize[0] && j < redGridSize[1]) {
							int redPixel = redIntPixels[i + j * redGridSize[0]];
							pixels[(i + j * width) * bc + 0] = (byte) (0xff & redPixel);
							if(pixels[(i + j * width) * bc + 0] == 0) {
								pixels[(i + j * width) * bc + 0] = 1;
							}
						}
						if (i < greenGridSize[0] && j < greenGridSize[1]) {
							int greenPixel = greenIntPixels[i + j * greenGridSize[0]];
							pixels[(i + j * width) * bc + 1] = (byte) (0xff & greenPixel);
							if(pixels[(i + j * width) * bc + 1] == 0) {
								pixels[(i + j * width) * bc + 1] = 1;
							}
						}
						if (i < blueGridSize[0] && j < blueGridSize[1]) {
							int bluePixel = blueIntPixels[i + j * blueGridSize[0]];
							pixels[(i + j * width) * bc + 2] = (byte) (0xff & bluePixel);
							if(pixels[(i + j * width) * bc + 2] == 0) {
								pixels[(i + j * width) * bc + 2] = 1;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procColorComposite : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdRedReader != null)
				gdRedReader.destory();
			gdRedReader = null;
			if (gdGreenReader != null)
				gdGreenReader.destory();
			gdGreenReader = null;
			if (gdBlueReader != null)
				gdBlueReader.destory();
			gdBlueReader = null;
			return ret;
		}

		// Write the output file
		try {
			if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procColorComposite : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdRedReader != null)
			gdRedReader.destory();
		gdRedReader = null;
		if (gdGreenReader != null)
			gdGreenReader.destory();
		gdGreenReader = null;
		if (gdBlueReader != null)
			gdBlueReader.destory();
		gdBlueReader = null;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////
	// 4��� �ռ�: 1 Band Only

	// �ش� ��庰 ���� ���� �÷� �ռ��� �����Ѵ�.
	// @ inRedFilePath : Red ��� �Է¿��� ���
	// @ inGreenFilePath : Green ��� �Է¿��� ���
	// @ inBlueFilePath : Blue ��� �Է¿��� ���
	// @ inNirFilePath : Nir ��� �Է¿��� ���
	// @ outFilePath : ������ �÷��ռ� ������� ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procMerge4Band(String inRedFilePath, String inGreenFilePath, String inBlueFilePath,
			String inNirFilePath, String outFilePath) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GTiffDataReader gdRedReader = null;
		GTiffDataReader gdGreenReader = null;
		GTiffDataReader gdBlueReader = null;
		GTiffDataReader gdNirReader = null;
		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		int dataBufferType = DataBuffer.TYPE_BYTE;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		int width = 0;
		int height = 0;
		int bc = 4;
		byte[] pixels = null;
		int[] pixelsInt = null;
		int i = 0, j = 0;

		// Read the input file
		try {
			gdRedReader = new GTiffDataReader(inRedFilePath, maxBit16);
			gdGreenReader = new GTiffDataReader(inGreenFilePath, maxBit16);
			gdBlueReader = new GTiffDataReader(inBlueFilePath, maxBit16);
			gdNirReader = new GTiffDataReader(inNirFilePath, maxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procMerge4Band : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdRedReader != null)
				gdRedReader.destory();
			gdRedReader = null;
			if (gdGreenReader != null)
				gdGreenReader.destory();
			gdGreenReader = null;
			if (gdBlueReader != null)
				gdBlueReader.destory();
			gdBlueReader = null;
			if (gdNirReader != null)
				gdNirReader.destory();
			gdNirReader = null;
			return ret;
		}

		if (!gdRedReader.IsOpened() || !gdGreenReader.IsOpened() || !gdBlueReader.IsOpened()
				|| !gdNirReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdRedReader != null)
				gdRedReader.destory();
			gdRedReader = null;
			if (gdGreenReader != null)
				gdGreenReader.destory();
			gdGreenReader = null;
			if (gdBlueReader != null)
				gdBlueReader.destory();
			gdBlueReader = null;
			if (gdNirReader != null)
				gdNirReader.destory();
			gdNirReader = null;
			return ret;
		}

		// Processing
		try {
			gdWriter = new GTiffDataWriter();
			envelope = gdRedReader.getEnvelope();
			gridEnvelope = gdRedReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdRedReader.getCrs();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			dataBufferType = gdRedReader.getDataType();

			if (gdRedReader.getBandCount() != 1 || gdGreenReader.getBandCount() != 1 || gdBlueReader.getBandCount() != 1
					|| gdNirReader.getBandCount() != 1) {
				System.out.println("GImageProcessor.procMerge4Band : The 3 band file is not supported. - "
						+ inRedFilePath.toString() + ", " + inGreenFilePath.toString() + ", "
						+ inBlueFilePath.toString() + ", " + inNirFilePath.toString());

				ret = ProcessCode.ERROR_NO_SUPPORTED_3BAND;

				if (gdRedReader != null)
					gdRedReader.destory();
				gdRedReader = null;
				if (gdGreenReader != null)
					gdGreenReader.destory();
				gdGreenReader = null;
				if (gdBlueReader != null)
					gdBlueReader.destory();
				gdBlueReader = null;
				if (gdNirReader != null)
					gdNirReader.destory();
				gdNirReader = null;
				return ret;
			} else {
				int[] redGridSize = gdRedReader.getGridSize();
				int[] greenGridSize = gdGreenReader.getGridSize();
				int[] blueGridSize = gdBlueReader.getGridSize();
				int[] nirGridSize = gdNirReader.getGridSize();

				switch (dataBufferType) {
				case DataBuffer.TYPE_SHORT:
				case DataBuffer.TYPE_USHORT: {
					pixelsInt = new int[width * height * bc];
					int[] redPixelsInt = gdRedReader.getAllPixelsInt();
					int[] greenPixelsInt = gdGreenReader.getAllPixelsInt();
					int[] bluePixelsInt = gdBlueReader.getAllPixelsInt();
					int[] nirPixelsInt = gdNirReader.getAllPixelsInt();

					for (j = 0; j < height; j++) {
						for (i = 0; i < width; i++) {
							// Initialize
							pixelsInt[(i + j * width) * bc + 0] = 0;
							pixelsInt[(i + j * width) * bc + 1] = 0;
							pixelsInt[(i + j * width) * bc + 2] = 0;
							pixelsInt[(i + j * width) * bc + 3] = 0;

							// Color Composite
							if (i < redGridSize[0] && j < redGridSize[1])
								pixelsInt[(i + j * width) * bc + 0] = redPixelsInt[i + j * redGridSize[0]];
							if (i < greenGridSize[0] && j < greenGridSize[1])
								pixelsInt[(i + j * width) * bc + 1] = greenPixelsInt[i + j * greenGridSize[0]];
							if (i < blueGridSize[0] && j < blueGridSize[1])
								pixelsInt[(i + j * width) * bc + 2] = bluePixelsInt[i + j * blueGridSize[0]];
							if (i < nirGridSize[0] && j < nirGridSize[1])
								pixelsInt[(i + j * width) * bc + 3] = nirPixelsInt[i + j * nirGridSize[0]];
						}
					}
				}
					break;
				default: {
					pixels = new byte[width * height * bc];
					byte[] redPixels = gdRedReader.getAllPixelsByte();
					byte[] greenPixels = gdGreenReader.getAllPixelsByte();
					byte[] bluePixels = gdBlueReader.getAllPixelsByte();
					byte[] nirPixels = gdNirReader.getAllPixelsByte();

					for (j = 0; j < height; j++) {
						for (i = 0; i < width; i++) {
							// Initialize
							pixels[(i + j * width) * bc + 0] = 0;
							pixels[(i + j * width) * bc + 1] = 0;
							pixels[(i + j * width) * bc + 2] = 0;
							pixels[(i + j * width) * bc + 3] = 0;

							// Color Composite
							if (i < redGridSize[0] && j < redGridSize[1])
								pixels[(i + j * width) * bc + 0] = redPixels[i + j * redGridSize[0]];
							if (i < greenGridSize[0] && j < greenGridSize[1])
								pixels[(i + j * width) * bc + 1] = greenPixels[i + j * greenGridSize[0]];
							if (i < blueGridSize[0] && j < blueGridSize[1])
								pixels[(i + j * width) * bc + 2] = bluePixels[i + j * blueGridSize[0]];
							if (i < nirGridSize[0] && j < nirGridSize[1])
								pixels[(i + j * width) * bc + 3] = nirPixels[i + j * nirGridSize[0]];
						}
					}
				}
					break;
				}
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procMerge4Band : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdRedReader != null)
				gdRedReader.destory();
			gdRedReader = null;
			if (gdGreenReader != null)
				gdGreenReader.destory();
			gdGreenReader = null;
			if (gdBlueReader != null)
				gdBlueReader.destory();
			gdBlueReader = null;
			return ret;
		}

		// Write the output file
		try {
			switch (dataBufferType) {
			case DataBuffer.TYPE_SHORT:
			case DataBuffer.TYPE_USHORT: {
				if (!gdWriter.geoTiffWriter16BitMultiBand(outFilePath, outCrs, coordLB, coordRT, width, height, bc,
						pixelsInt)) {
					ret = ProcessCode.ERROR_FAIL_WRITE;
				}
			}
				break;
			default: {
				if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels)) {
					ret = ProcessCode.ERROR_FAIL_WRITE;
				}
			}
				break;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procMerge4Band : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdRedReader != null)
			gdRedReader.destory();
		gdRedReader = null;
		if (gdGreenReader != null)
			gdGreenReader.destory();
		gdGreenReader = null;
		if (gdBlueReader != null)
			gdBlueReader.destory();
		gdBlueReader = null;
		if (gdNirReader != null)
			gdNirReader.destory();
		gdNirReader = null;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////
	// ������׷� ���� : Linear Stretching / Histogram Auto Equalization / Histogram
	//////////////////////////////////////////////////////////////////////////////// Auto
	//////////////////////////////////////////////////////////////////////////////// Linear
	//////////////////////////////////////////////////////////////////////////////// Stretching
	// : 1 Band & 3 Band

	// �ش� ���� ���� ������׷��� Linear Stretching ������� �����Ѵ�.
	// @ inFilePath : �Է¿��� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ������� ���
	// @ stretchingMin : ��庰 �ּ� ȭ�Ұ� [1] or [3]
	// @ stretchingMax : ��庰 �ִ� ȭ�Ұ� [1] or [3]
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procHistogramLinearStretching(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath, int[] stretchingMin, int[] stretchingMax) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GImageEnhancement imgEnhance = new GImageEnhancement();
		GTiffDataReader gdReader = null;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		byte[] pixels = null;
		int width = 0;
		int height = 0;
		int bc = 0;

		// Read the input file
		try {
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramLinearStretching : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file after processing
		try {
			gdWriter = new GTiffDataWriter();
			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdReader.getCrs();
			pixels = gdReader.getAllPixelsByte();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			bc = gdReader.getBandCount();

			// Histogram Linear Stretching
			imgEnhance.calcLinearStretching(pixels, bc, width, height, stretchingMin, stretchingMax);
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramLinearStretching : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file
		try {
			if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramLinearStretching : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	// �ش� ���� ���� ������׷��� Histogram Equalization ������� �ڵ� �����Ѵ�.
	// @ inFilePath : �Է¿��� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ������� ���
	// @ isApplyBlueBand : Blue ��� LUT �ϰ� ���� ���� (true : Blue ��� LUT �ϰ� ����,
	// false : ��庰 LUT ����)
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procHistogramAutoEqualization(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath, boolean isApplyBlueBand) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GImageEnhancement imgEnhance = new GImageEnhancement();
		GTiffDataReader gdReader = null;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		int width = 0;
		int height = 0;
		int bc = 0;
		byte[] pixels = null;
		int[] histgR = new int[256];
		int[] histgG = new int[256];
		int[] histgB = new int[256];

		// Read the input file
		try {
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16);
//[DEBUG]
			if (_IS_DEBUG) {
				int dataBufferType = gdReader.getDataType();
				switch (dataBufferType) {
				case DataBuffer.TYPE_BYTE:
					System.out.println("[DEBUG]procHistogramAutoEqualization - DataBuffer.TYPE_BYTE : dataBufferType = "
							+ dataBufferType);
					break;
				case DataBuffer.TYPE_SHORT:
					System.out
							.println("[DEBUG]procHistogramAutoEqualization - DataBuffer.TYPE_SHORT : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_USHORT:
					System.out
							.println("[DEBUG]procHistogramAutoEqualization - DataBuffer.TYPE_USHORT : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_INT:
					System.out.println("[DEBUG]procHistogramAutoEqualization - DataBuffer.TYPE_INT : dataBufferType = "
							+ dataBufferType);
					break;
				case DataBuffer.TYPE_FLOAT:
					System.out
							.println("[DEBUG]procHistogramAutoEqualization - DataBuffer.TYPE_FLOAT : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_DOUBLE:
					System.out
							.println("[DEBUG]procHistogramAutoEqualization - DataBuffer.TYPE_DOUBLE : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_UNDEFINED:
				default:
					System.out.println(
							"[DEBUG]procHistogramAutoEqualization - DataBuffer.TYPE_UNDEFINED : dataBufferType = "
									+ dataBufferType);
					break;
				}
			}
//[DEBUG]

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramAutoEqualization : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file after processing
		try {
			gdWriter = new GTiffDataWriter();
			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdReader.getCrs();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			bc = gdReader.getBandCount();
			pixels = gdReader.getAllPixelsByte();

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]GImageProcessor.procHistogramAutoEqualization : ");
				System.out.println("[DEBUG]\t pixels size : " + pixels.length);
				System.out.println("[DEBUG]\t bc : " + bc + ", width = " + width + ", height = " + height);
			}
//[DEBUG]		

			// Histogram Auto Equalization
			gdReader.getHistogram(histgR, histgG, histgB);
			imgEnhance.calcHistogramAutoEqualization(pixels, bc, width, height, histgR, histgG, histgB,
					isApplyBlueBand);

			if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramAutoEqualization : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file
		try {
			if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramAutoEqualization : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	// �ش� ���� ���� ������׷��� Histogram Linear Stretching(1 Sigma) ������� �ڵ�
	// �����Ѵ�.
	// @ inFilePath : �Է¿��� ���
	// @ inMaxBit16 : �Է¿����� 16��Ʈ �ִ� ��Ʈ ����
	// @ outFilePath : ������ ������� ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procHistogramAutoLinearStretching(String inFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
			String outFilePath) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GImageEnhancement imgEnhance = new GImageEnhancement();
		GTiffDataReader gdReader = null;
		GImageEnhancement.LinearSigma sigmaBase = GImageEnhancement.LinearSigma.LINEAR_1SIGMA;

		GTiffDataWriter gdWriter = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		Coordinate coordLB = null;
		Coordinate coordRT = null;
		CoordinateReferenceSystem outCrs = null;
		int width = 0;
		int height = 0;
		int bc = 0;
		byte[] pixels = null;
		int[] histgR = new int[256];
		int[] histgG = new int[256];
		int[] histgB = new int[256];
		int[] stretchingMin = new int[3];
		int[] stretchingMax = new int[3];

		// Read the input file
		try {
			gdReader = new GTiffDataReader(inFilePath, inMaxBit16);
//[DEBUG]
			if (_IS_DEBUG) {
				int dataBufferType = gdReader.getDataType();
				switch (dataBufferType) {
				case DataBuffer.TYPE_BYTE:
					System.out.println(
							"[DEBUG]procHistogramAutoLinearStretching - DataBuffer.TYPE_BYTE : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_SHORT:
					System.out.println(
							"[DEBUG]procHistogramAutoLinearStretching - DataBuffer.TYPE_SHORT : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_USHORT:
					System.out.println(
							"[DEBUG]procHistogramAutoLinearStretching - DataBuffer.TYPE_USHORT : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_INT:
					System.out.println(
							"[DEBUG]procHistogramAutoLinearStretching - DataBuffer.TYPE_INT : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_FLOAT:
					System.out.println(
							"[DEBUG]procHistogramAutoLinearStretching - DataBuffer.TYPE_FLOAT : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_DOUBLE:
					System.out.println(
							"[DEBUG]procHistogramAutoLinearStretching - DataBuffer.TYPE_DOUBLE : dataBufferType = "
									+ dataBufferType);
					break;
				case DataBuffer.TYPE_UNDEFINED:
				default:
					System.out.println(
							"[DEBUG]procHistogramAutoLinearStretching - DataBuffer.TYPE_UNDEFINED : dataBufferType = "
									+ dataBufferType);
					break;
				}
			}
//[DEBUG]

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramAutoLinearStretching : " + ex.toString());
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		if (!gdReader.IsOpened()) {
			ret = ProcessCode.ERROR_FAIL_READ;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file after processing
		try {
			gdWriter = new GTiffDataWriter();
			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();
			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
			outCrs = gdReader.getCrs();
			width = (int) gridEnvelope.getWidth();
			height = (int) gridEnvelope.getHeight();
			bc = gdReader.getBandCount();
			pixels = gdReader.getAllPixelsByte();

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]GImageProcessor.procHistogramAutoLinearStretching : ");
				System.out.println("[DEBUG]\t pixels size : " + pixels.length);
				System.out.println("[DEBUG]\t bc : " + bc + ", width = " + width + ", height = " + height);
			}
//[DEBUG]		

			// Histogram Auto Linear Stretching (1 Sigma)
			gdReader.getHistogram(histgR, histgG, histgB);
			imgEnhance.calcAutoHistogramMinMaxByLinearSigma(sigmaBase, histgR, histgG, histgB, stretchingMin,
					stretchingMax);
			imgEnhance.calcLinearStretching(pixels, bc, width, height, stretchingMin, stretchingMax);

			if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramAutoLinearStretching : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
			return ret;
		}

		// Write the output file
		try {
			if (!gdWriter.geoTiffWriter(outFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			}
		} catch (Exception ex) {
			System.out.println("GImageProcessor.procHistogramAutoLinearStretching : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////
	// ������ũ : (���� ���� 1, ��󿵻� n)

	// [Seamline Method]
	// index 0 : Minimum Difference Sum " Minimum Gray Difference Sum"
	// index 1 : Middle Line " Middle Line Fitting"
	// index 2 : Use Existing SeamPoint " Use Existing Seamline"
	// (*) index 3 : Feature Selection Using Canny Edge Operator " Feature Selection
	// Using Edge"
	// Search Width : 100

	// [Histogram Matching Method[
	// index 0 : None "None"
	// index 1 : Match Mean & Std. Dev. "Match Mean & Std. Dev."
	// index 2 : Match Cumulative Frequency "Match Cumulative Frequency"
	// (*) index 3 : Hue Adjustment Through Moving the Histogram "Hue Adjustment"
	// Feather Width : "10"

	// �ش� ���� ��Ͽ� ���� �ڵ� ������ũ�� �����Ѵ�.
	// @ inFilePaths : �Է¿��� ���
	// @ outFilePath : ������ ������� ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procAutoMosaic(ArrayList<GFileData> inFilePaths, String outFilePath) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GCAutoMosaicControl autoMosaicCtrl = new GCAutoMosaicControl();
		GMosaicData mosaicData = new GMosaicData();
		GMosaicAlgorithmData mosaicAlgorithmData = new GMosaicAlgorithmData();
		GMosaicResultData[][] pMosaicRegistData = null;
		Coordinate nPathRow = new Coordinate();
		ArrayList<GOpenFileData> addFileDataArray = null;
		GOpenFileData pOpenFileData = null;
		GFileData oFileData = null;
		GTiffDataReader gdReader = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		String strWkt = "";
		int i = 0;
		int nPath = 0, nRow = 0;
		int bc = 1;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procAutoMosaic : ");
		}
//[DEBUG]	

		if (inFilePaths.size() < 2) {
			System.out.println("GImageProcessor.procAutoMosaic : Need over two input files - " + inFilePaths.size());
			ret = ProcessCode.ERROR_FAIL_READ;

			return ret;
		}

		addFileDataArray = new ArrayList<GOpenFileData>();
		for (i = 0; i < inFilePaths.size(); i++) {
			pOpenFileData = new GOpenFileData();

			oFileData = inFilePaths.get(i);
			pOpenFileData._oFileData.Copy(oFileData);

			// --------------------------------------------------//
			// Read the input file
			// --------------------------------------------------//
			try {
				gdReader = new GTiffDataReader(pOpenFileData._oFileData._strFilePath,
						pOpenFileData._oFileData._maxBit16);
			} catch (Exception ex) {
				System.out.println("GImageProcessor.procAutoMosaic : " + ex.toString());
				ex.printStackTrace();
				System.out.println("\t File Name : " + pOpenFileData._oFileData._strFilePath);
				ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
				return ret;
			}

			if (!gdReader.IsOpened()) {
				ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
				return ret;
			}
			// --------------------------------------------------//

			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();

			// --------------------------------------------------//
			// ��� ��
			pOpenFileData._lBandNum = gdReader.getBandCount();

			// �̹��� ���뿵�� ����
			// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
			pOpenFileData._mbr2d.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
			pOpenFileData._mbr2d.setRect(envelope.getBounds2D());

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t Input File : " + pOpenFileData._oFileData._strFilePath);
				System.out.println("[DEBUG]\t Envelope(Pos) : x = " + envelope.getBounds2D().getX() + ", y = "
						+ envelope.getBounds2D().getY());
				System.out.println("[DEBUG]\t Envelope(Size) : w = " + envelope.getBounds2D().getWidth() + ", h = "
						+ envelope.getBounds2D().getHeight());
				System.out.println("[DEBUG]\t GridEnvelope2D(Pos) : x = " + gridEnvelope.getBounds2D().getX() + ", y = "
						+ gridEnvelope.getBounds2D().getY());
				System.out.println("[DEBUG]\t GridEnvelope2D(Size) : w = " + gridEnvelope.getBounds2D().getWidth()
						+ ", h = " + gridEnvelope.getBounds2D().getHeight());
				System.out.println("[DEBUG]\t Min(LB) : x = " + pOpenFileData._mbr2d.getMinX() + ", y = "
						+ pOpenFileData._mbr2d.getMinY());
				System.out.println("[DEBUG]\t Max(RT) : x = " + pOpenFileData._mbr2d.getMaxX() + ", y = "
						+ pOpenFileData._mbr2d.getMaxY());
			}
//[DEBUG]	

			if (CRS.equalsIgnoreMetadata(gdReader.getCrs(), AbstractGridFormat.getDefaultCRS())) {
				pOpenFileData._Crs = AbstractGridFormat.getDefaultCRS();
			} else {
				try {
					// ��ǥ�� ����
					strWkt = gdReader.getCrs().toWKT();
					pOpenFileData._Crs = CRS.parseWKT(strWkt);
				} catch (FactoryException fe) {
					System.out.println("GImageProcessor.procAutoMosaic : " + fe.toString());
					System.out.println("\t File Name : " + pOpenFileData._oFileData._strFilePath);
					fe.printStackTrace();
					ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

					if (gdReader != null)
						gdReader.destory();
					gdReader = null;
					return ret;
				}
			}

			// �߽� ���� ��ǥ
			pOpenFileData._dblUtmCenter.x = pOpenFileData._mbr2d.getCenterX();
			pOpenFileData._dblUtmCenter.y = pOpenFileData._mbr2d.getCenterY();

			// �ػ�
			pOpenFileData._dblPixelScales[0] = envelope.getWidth() / gridEnvelope.getWidth();
			pOpenFileData._dblPixelScales[1] = envelope.getHeight() / gridEnvelope.getHeight();
			// --------------------------------------------------//

			if (i == 0) {
				bc = pOpenFileData._lBandNum;
			}

			addFileDataArray.add(pOpenFileData);

			if (gdReader != null)
				gdReader.destory();
			gdReader = null;
		}

		try {

			// Step 1. �Է¿��� ��� ����
			autoMosaicCtrl.setAddFileData(addFileDataArray);

			// Step 2. Path, Row�� ������ũ ��� ���� ��� ����
			pMosaicRegistData = autoMosaicCtrl.procPreprocessing(nPathRow);

			// Step 2-1. Path, Row�� ���� �̿� ���� ��ø ���� Ȯ��
			if (!checkOverlappedArea(inFilePaths, pMosaicRegistData, nPathRow)) {
				System.out.println("GImageProcessor.procAutoMosaic : Exist non-overlap files");
				ret = ProcessCode.ERROR_FAIL_PROCESS;

				return ret;
			}

			// Step 3. Path, Row�� ������ũ ��� ���� ��� ����
			autoMosaicCtrl.setMosaicRegistData(pMosaicRegistData);

			// ----------------------------------------------------------------------------------------------//
			// Step 4. ������ũ ���� ����
			// ----------------------------------------------------------------------------------------------//
			mosaicData._nNumOfPath = (int) nPathRow.x;
			mosaicData._nNumOfRow = (int) nPathRow.y;
			mosaicData._nNumOfFile = inFilePaths.size();

			// �ڵ� ������ũ ���ο��� �»��
			mosaicData._nMainMasterPath = 0;
			mosaicData._nMainMasterRow = 0;
			mosaicData._nNumOfMaster = 0;
			mosaicData._dblPixelScales[0] = 0;
			mosaicData._dblPixelScales[1] = 0;

			// ������ũ ��� ����
			switch (bc) {
			case 1:
				mosaicData._nGrayBand = 2; // R : 2, G : 1, B : 0, BandOdering : -1
				break;
			case 3:
				mosaicData._nGrayBand = -1; // R : 2, G : 1, B : 0, BandOdering : -1
				break;
			}

			if (mosaicData._nGrayBand == -1) {
				mosaicData._nBandOderingArray.add(new Integer(2)); // Red
				mosaicData._nBandOderingArray.add(new Integer(1)); // Green
				mosaicData._nBandOderingArray.add(new Integer(0)); // Blue
			}
			// ----------------------------------------------------------------------------------------------//

			// ----------------------------------------------------------------------------------------------//
			// Step 6. ������ũ �˰��� ����
			// ----------------------------------------------------------------------------------------------//
			// Seamline ����
			mosaicAlgorithmData._nSeamLineMethod = GSeamline.SeamlineMethod.FEATURE_SELECTION_USING_EDGE;
			// Seamline ������ ���� ������ ũ��
			mosaicAlgorithmData._lSearchWidth = 100;
			// ������׷� ��Ī ����
			mosaicAlgorithmData._nHistogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT;
			// ������ũ ���μ��� ����
			mosaicAlgorithmData._nMosaicProcDirection = GAutoMosaic.MosaicProcMethod.VERTICAL;

			// Seam Point ���ϸ�
			mosaicAlgorithmData._strSPointFileName = "";
			// Seame Point ���ϸ�
			mosaicAlgorithmData._strSPointDataFileName = "";
			// Seam Point ����
			mosaicAlgorithmData._strSeamPoint = "";
			// Seam Point ���� ����
			mosaicAlgorithmData._bSaveSeamPoint = false;

			// ������׷� ��Ī�� ���� Feather Width ���� ����
			mosaicAlgorithmData._bFeatherWidth = false;
			// ������׷� ��Ī�� ���� Feather Width ũ��
			mosaicAlgorithmData._lFeatherWidth = 10;
			// ----------------------------------------------------------------------------------------------//

			// ----------------------------------------------------------------------------------------------//
			// Step 6. �ڵ� ������ũ ���� ����
			// ----------------------------------------------------------------------------------------------//
			// ���� ȭ�� ��� ���� ���
			pMosaicRegistData = autoMosaicCtrl.calcImgStatisticsData(mosaicData, (int) nPathRow.x, (int) nPathRow.y);

			// �ڵ� ������ũ ���� ����
			autoMosaicCtrl.setMosaicData(mosaicData, mosaicAlgorithmData, pMosaicRegistData, outFilePath);
			// ----------------------------------------------------------------------------------------------//

			// Step 7. ������ũ ����
			autoMosaicCtrl.procMosaic();

			// Step 8. ������ũ ��� ���� �� ����
			autoMosaicCtrl.saveImageFile();
			autoMosaicCtrl.destroy();

			ret = ProcessCode.SUCCESS;

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procAutoMosaic : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_PROCESS;
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
		return ret;
	}

	// �ش� ���� ��Ͽ� ���� �ܼ� ���� ������ũ�� �����Ѵ�.
	// @ inFilePaths : �Է¿��� ���
	// @ outFilePath : ������ ������� ���
	// @ resampleMethod : ������迭 ���
	// @
	// @ return : ProcessCode ���� ��� ����
	public ProcessCode procSimpleMergedMosaic(ArrayList<GFileData> inFilePaths, String outFilePath,
			GTiffDataReader.ResamplingMethod resampleMethod) {
		ProcessCode ret = ProcessCode.SUCCESS;
		GMosaicImgData resultImgData = new GMosaicImgData(); // ��� ���� ����
		ArrayList<Integer> arrIndexOrdering = null;
		GMosaicImgData[] mosaicImgData = null;
		GMosaicImgData pImgData = null;
		Rectangle2D unionRect = null;
		GFileData oFileData = null;
		ArrayList<GTiffDataReader> arrGdReader = null;
		GTiffDataReader gdReader = null;
		Envelope2D envelope = null;
		GridEnvelope2D gridEnvelope = null;
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		Geometry srcGeometry = null;
		Geometry trgGeometry = null;
		Coordinate coordLB = new Coordinate();
		Coordinate coordRT = new Coordinate();
		GridEnvelope2D range = new GridEnvelope2D();
		String strWkt = "";
		int bc = 1;
		int x = 0, y = 0, width = 0, height = 0;
		int i = 0, j = 0, k = 0, m = 0, size = 0;
		int refImgIdx = 0;
		int resultIdx = 0, curIdx = 0, idx = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageProcessor.procSimpleMergedMosaic : ");
		}
//[DEBUG]	

		if (inFilePaths.size() < 2) {
			System.out.println(
					"GImageProcessor.procSimpleMergedMosaic : Need over two input files - " + inFilePaths.size());
			ret = ProcessCode.ERROR_FAIL_READ;

			return ret;
		}

		size = inFilePaths.size();
		mosaicImgData = new GMosaicImgData[size];

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Step 0. Search the reference image index.");
		}
//[DEBUG]	

		// ----------------------------------------------------------------------------------------------//
		// Step 0. ���� ���� �ε��� �˻�
		// ----------------------------------------------------------------------------------------------//
		for (i = 0; i < size; i++) {
			oFileData = inFilePaths.get(i);
			if (oFileData._isReferenced) {
				refImgIdx = i;
				break;
			}
		}
		// ----------------------------------------------------------------------------------------------//

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t\t Reference Image Index : " + refImgIdx);
		}
//[DEBUG]

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Step 1. Calculate the mosaic boundary");
		}
//[DEBUG]	

		// ----------------------------------------------------------------------------------------------//
		// Step 1. �� ���� ���� ����(���뿵�� ����, �ػ�)
		// -> ���ؿ��� �Ǵ� ù��° ����
		// ----------------------------------------------------------------------------------------------//
		arrGdReader = new ArrayList<GTiffDataReader>();
		for (i = 0; i < size; i++) {
			oFileData = inFilePaths.get(i);
			mosaicImgData[i] = new GMosaicImgData();
			pImgData = mosaicImgData[i];

			pImgData._oFileData.Copy(oFileData);

			// --------------------------------------------------//
			// Read the input file
			// --------------------------------------------------//
			try {
				gdReader = new GTiffDataReader(pImgData._oFileData._strFilePath, pImgData._oFileData._maxBit16);
				arrGdReader.add(gdReader);
			} catch (Exception ex) {
				System.out.println("GImageProcessor.procSimpleMergedMosaic : " + ex.toString());
				ex.printStackTrace();
				System.out.println("\t File Name : " + pImgData._oFileData._strFilePath);
				ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

				clearListGTiffDataReader(arrGdReader);
				return ret;
			}

			if (!gdReader.IsOpened()) {
				ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

				clearListGTiffDataReader(arrGdReader);
				return ret;
			}
			// --------------------------------------------------//

			envelope = gdReader.getEnvelope();
			gridEnvelope = gdReader.getGridEnvelope();

			// --------------------------------------------------//
			// ��� ��
			pImgData._lBandNum = gdReader.getBandCount();

			// �̹��� ���뿵�� ����
			// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
			pImgData._mbr2d.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
			pImgData._mbr2d.setRect(envelope.getBounds2D());

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]###################################################");
				System.out.println("[DEBUG]\t\t Input File : " + pImgData._oFileData._strFilePath);
				System.out.println("[DEBUG]\t\t Envelope(Pos) : x = " + envelope.getBounds2D().getX() + ", y = "
						+ envelope.getBounds2D().getY());
				System.out.println("[DEBUG]\t\t Envelope(Size) : w = " + envelope.getBounds2D().getWidth() + ", h = "
						+ envelope.getBounds2D().getHeight());
				System.out.println("[DEBUG]\t\t GridEnvelope2D(Pos) : x = " + gridEnvelope.getBounds2D().getX()
						+ ", y = " + gridEnvelope.getBounds2D().getY());
				System.out.println("[DEBUG]\t\t GridEnvelope2D(Size) : w = " + gridEnvelope.getBounds2D().getWidth()
						+ ", h = " + gridEnvelope.getBounds2D().getHeight());
				System.out.println("[DEBUG]\t\t Min(LB) : x = " + pImgData._mbr2d.getMinX() + ", y = "
						+ pImgData._mbr2d.getMinY());
				System.out.println("[DEBUG]\t\t Max(RT) : x = " + pImgData._mbr2d.getMaxX() + ", y = "
						+ pImgData._mbr2d.getMaxY());
				System.out.println("[DEBUG]###################################################");
			}
//[DEBUG]	

			// �ػ�
			pImgData._dblPixelScales[0] = envelope.getWidth() / gridEnvelope.getWidth();
			pImgData._dblPixelScales[1] = envelope.getHeight() / gridEnvelope.getHeight();
			// --------------------------------------------------//

			if (CRS.equalsIgnoreMetadata(gdReader.getCrs(), AbstractGridFormat.getDefaultCRS())) {
				pImgData._Crs = AbstractGridFormat.getDefaultCRS();
				if (i == refImgIdx) {
					resultImgData._Crs = AbstractGridFormat.getDefaultCRS();
				}
			} else {
				try {
					// ��ǥ�� ����
					strWkt = gdReader.getCrs().toWKT();
					pImgData._Crs = CRS.parseWKT(strWkt);

					if (i == refImgIdx) {
						resultImgData._Crs = CRS.parseWKT(strWkt);
					}
				} catch (FactoryException fe) {
					System.out.println("GImageProcessor.procSimpleMergedMosaic : " + fe.toString());
					System.out.println("\t File Name : " + pImgData._oFileData._strFilePath);
					fe.printStackTrace();
					ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

					clearListGTiffDataReader(arrGdReader);
					return ret;
				}
			}

			if (i == refImgIdx) {
				resultImgData._dblPixelScales[0] = pImgData._dblPixelScales[0];
				resultImgData._dblPixelScales[1] = pImgData._dblPixelScales[1];
				resultImgData._lBandNum = pImgData._lBandNum;
			}
		}
		// ----------------------------------------------------------------------------------------------//

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Step 2. Get the mosaic image pixels.");
		}
//[DEBUG]	

		// ----------------------------------------------------------------------------------------------//
		// Step 2. �� ���� ��� ���� ����(ȭ�ҿ���) �� ȭ�� ����
		// ----------------------------------------------------------------------------------------------//
		// ��� ���� ��������
		for (i = 0; i < size; i++) {
			pImgData = mosaicImgData[i];

			if (i == 0) {
				resultImgData._mbr2d.setCoordinateReferenceSystem(resultImgData._Crs);
				resultImgData._mbr2d.setRect(pImgData._mbr2d.getBounds2D());
			} else {
				unionRect = resultImgData._mbr2d.createUnion(pImgData._mbr2d.getBounds2D());
				resultImgData._mbr2d.setRect(unionRect);
			}
		}
		width = (int) (resultImgData._mbr2d.getWidth() / resultImgData._dblPixelScales[0]);
		height = (int) (resultImgData._mbr2d.getHeight() / resultImgData._dblPixelScales[1]);
		resultImgData._imgBox2d.setBounds(0, 0, width, height);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]###################################################");
			System.out.println("[DEBUG]\t\t Result Image Info. : " + outFilePath);
			System.out.println("[DEBUG]\t\t Envelope(Pos) : x = " + resultImgData._mbr2d.getBounds2D().getX() + ", y = "
					+ resultImgData._mbr2d.getBounds2D().getY());
			System.out.println("[DEBUG]\t\t Envelope(Size) : w = " + resultImgData._mbr2d.getBounds2D().getWidth()
					+ ", h = " + resultImgData._mbr2d.getBounds2D().getHeight());
			System.out.println("[DEBUG]\t\t GridEnvelope2D(Pos) : x = " + resultImgData._imgBox2d.getBounds2D().getX()
					+ ", y = " + resultImgData._imgBox2d.getBounds2D().getY());
			System.out.println(
					"[DEBUG]\t\t GridEnvelope2D(Size) : w = " + resultImgData._imgBox2d.getBounds2D().getWidth()
							+ ", h = " + resultImgData._imgBox2d.getBounds2D().getHeight());
			System.out.println("[DEBUG]\t\t Min(LB) : x = " + resultImgData._mbr2d.getMinX() + ", y = "
					+ resultImgData._mbr2d.getMinY());
			System.out.println("[DEBUG]\t\t Max(RT) : x = " + resultImgData._mbr2d.getMaxX() + ", y = "
					+ resultImgData._mbr2d.getMaxY());
			System.out.println("[DEBUG]###################################################");
		}
//[DEBUG]			

		// �� ���� ��� ���� ����(ȭ�ҿ���) �� ������ ��üȭ�� ����
		for (i = 0; i < size; i++) {
			pImgData = mosaicImgData[i];

			try {
				MathTransform tranformCurToResult = CRS.findMathTransform(pImgData._Crs, resultImgData._Crs, false);

				coordLB.x = pImgData._mbr2d.getMinX();
				coordLB.y = pImgData._mbr2d.getMinY();
				coordRT.x = pImgData._mbr2d.getMaxX();
				coordRT.y = pImgData._mbr2d.getMaxY();

				// Cur -> Result
				srcGeometry = geometryFactory.createPoint(coordLB);
				trgGeometry = JTS.transform(srcGeometry, tranformCurToResult);
				coordLB = trgGeometry.getCoordinate();
				srcGeometry = geometryFactory.createPoint(coordRT);
				trgGeometry = JTS.transform(srcGeometry, tranformCurToResult);
				coordRT = trgGeometry.getCoordinate();

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]###################################################");
					System.out.println("[DEBUG]\t\t Envelope(Pos) : x = " + pImgData._mbr2d.getBounds2D().getX()
							+ ", y = " + pImgData._mbr2d.getBounds2D().getY());
					System.out.println("[DEBUG]\t\t Envelope(Size) : w = " + pImgData._mbr2d.getBounds2D().getWidth()
							+ ", h = " + pImgData._mbr2d.getBounds2D().getHeight());
					System.out.println("[DEBUG]\t\t Cov. coordLB : x = " + coordLB.x + ", y = " + coordLB.y);
					System.out.println("[DEBUG]\t\t Cov. coordRT : x = " + coordRT.x + ", y = " + coordRT.y);
					System.out.println("[DEBUG]###################################################");
				}
//[DEBUG]

				// ������ũ ��� ȭ�ҿ��������� LT ���� ȭ�� ��ġ�� ����
				x = (int) (Math.abs(resultImgData._mbr2d.getMinX() - coordLB.x) / resultImgData._dblPixelScales[0]);
				y = (int) (Math.abs(resultImgData._mbr2d.getMaxY() - coordRT.y) / resultImgData._dblPixelScales[1]);
				width = (int) (Math.abs(coordRT.x - coordLB.x) / resultImgData._dblPixelScales[0]);
				height = (int) (Math.abs(coordRT.y - coordLB.y) / resultImgData._dblPixelScales[1]);
				pImgData._imgBox2d.setBounds(x, y, width, height);

				pImgData._dblPixelScales[0] = resultImgData._dblPixelScales[0];
				pImgData._dblPixelScales[1] = resultImgData._dblPixelScales[1];

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]###################################################");
					System.out.println("[DEBUG]\t\t Mosaic File : " + pImgData._oFileData._strFilePath);
					System.out.println("[DEBUG]\t\t Envelope(Pos) : x = " + pImgData._mbr2d.getBounds2D().getX()
							+ ", y = " + pImgData._mbr2d.getBounds2D().getY());
					System.out.println("[DEBUG]\t\t Envelope(Size) : w = " + pImgData._mbr2d.getBounds2D().getWidth()
							+ ", h = " + pImgData._mbr2d.getBounds2D().getHeight());
					System.out
							.println("[DEBUG]\t\t GridEnvelope2D(Pos) : x = " + pImgData._imgBox2d.getBounds2D().getX()
									+ ", y = " + pImgData._imgBox2d.getBounds2D().getY());
					System.out.println(
							"[DEBUG]\t\t GridEnvelope2D(Size) : w = " + pImgData._imgBox2d.getBounds2D().getWidth()
									+ ", h = " + pImgData._imgBox2d.getBounds2D().getHeight());
					System.out.println("[DEBUG]\t\t ImgBox(LB) : x = " + pImgData._imgBox2d.getMinX() + ", y = "
							+ pImgData._imgBox2d.getMinY());
					System.out.println("[DEBUG]\t\t ImgBox(RT) : x = " + pImgData._imgBox2d.getMaxX() + ", y = "
							+ pImgData._imgBox2d.getMaxY());
					System.out.println("[DEBUG]###################################################");
				}
//[DEBUG]

				gdReader = arrGdReader.get(i);

				range.setBounds(0, 0, (int) pImgData._imgBox2d.getWidth(), (int) pImgData._imgBox2d.getHeight());
				pImgData._pImage = gdReader.getResamplePixelsByte(range, pImgData._mbr2d, resampleMethod);

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
			} catch (Exception ex) {
				System.out.println("GImageProcessor.procSimpleMergedMosaic : " + ex.toString());
				ex.printStackTrace();

				ret = ProcessCode.ERROR_FAIL_PROCESS;
				clearListGTiffDataReader(arrGdReader);
				return ret;
			}
		}
		// ----------------------------------------------------------------------------------------------//

		clearListGTiffDataReader(arrGdReader);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Step 3. Merge the mosaic image.");
		}
//[DEBUG]	

		// ----------------------------------------------------------------------------------------------//
		// Step 3. ������ũ ���� ����
		// ----------------------------------------------------------------------------------------------//
		width = (int) resultImgData._imgBox2d.getWidth();
		height = (int) resultImgData._imgBox2d.getHeight();
		bc = resultImgData._lBandNum;
		resultImgData._pImage = new byte[width * height * bc];

		arrIndexOrdering = new ArrayList<Integer>();
		arrIndexOrdering.add(new Integer(refImgIdx));
		for (i = 0; i < size; i++) {
			if (i != refImgIdx) {
				arrIndexOrdering.add(new Integer(i));
			}
		}

		try {

			for (i = 0; i < size; i++) {
				idx = arrIndexOrdering.get(i).intValue();
				pImgData = mosaicImgData[idx];

				for (k = 0; k < pImgData._imgBox2d.height; k++) {
					for (j = 0; j < pImgData._imgBox2d.width; j++) {
						resultIdx = ((pImgData._imgBox2d.y + k) * resultImgData._imgBox2d.width
								+ (pImgData._imgBox2d.x + j)) * resultImgData._lBandNum;
						curIdx = (k * pImgData._imgBox2d.width + j) * pImgData._lBandNum;

						switch (bc) {
						case 1: {
							if (resultImgData._pImage[resultIdx] == 0) {
								switch (pImgData._lBandNum) {
								case 1: {
									if (pImgData._pImage[curIdx] != 0) {
										resultImgData._pImage[resultIdx] = pImgData._pImage[curIdx];
									}
								}
									break;
								case 3: {
									if (pImgData._pImage[curIdx + 0] != 0 && pImgData._pImage[curIdx + 1] != 0
											&& pImgData._pImage[curIdx + 2] != 0) {
										resultImgData._pImage[resultIdx + 0] = pImgData._pImage[curIdx + 0];
									}
								}
									break;
								}
							}
						}
							break;
						case 3: {
							if (resultImgData._pImage[resultIdx + 0] == 0 && resultImgData._pImage[resultIdx + 1] == 0
									&& resultImgData._pImage[resultIdx + 2] == 0) {
								switch (pImgData._lBandNum) {
								case 1: {
									if (pImgData._pImage[curIdx] != 0) {
										resultImgData._pImage[resultIdx + 0] = pImgData._pImage[curIdx];
										resultImgData._pImage[resultIdx + 1] = pImgData._pImage[curIdx];
										resultImgData._pImage[resultIdx + 2] = pImgData._pImage[curIdx];
									}
								}
									break;
								case 3: {
									if (pImgData._pImage[curIdx + 0] != 0 && pImgData._pImage[curIdx + 1] != 0
											&& pImgData._pImage[curIdx + 2] != 0) {
										resultImgData._pImage[resultIdx + 0] = pImgData._pImage[curIdx + 0];
										resultImgData._pImage[resultIdx + 1] = pImgData._pImage[curIdx + 1];
										resultImgData._pImage[resultIdx + 2] = pImgData._pImage[curIdx + 2];
									}
								}
									break;
								}
							}
						}
							break;
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out
					.println("GImageProcessor.procSimpleMergedMosaic : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();

			arrIndexOrdering.clear();
			arrIndexOrdering = null;

			ret = ProcessCode.ERROR_FAIL_PROCESS;
			return ret;
		}

		arrIndexOrdering.clear();
		arrIndexOrdering = null;
		// ----------------------------------------------------------------------------------------------//

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Step 4. Write the mosaic result.");
		}
//[DEBUG]	

		// ----------------------------------------------------------------------------------------------//
		// Step 4. ������ũ ��� ���� ����
		// ----------------------------------------------------------------------------------------------//
		try {

			GTiffDataWriter gdWriter = new GTiffDataWriter();

			coordLB.x = resultImgData._mbr2d.getMinX();
			coordLB.y = resultImgData._mbr2d.getMinY();
			coordRT.x = resultImgData._mbr2d.getMaxX();
			coordRT.y = resultImgData._mbr2d.getMaxY();
			width = (int) resultImgData._imgBox2d.getWidth();
			height = (int) resultImgData._imgBox2d.getHeight();
			bc = resultImgData._lBandNum;

			this.minX = coordLB.x;
			this.minY = coordLB.y;
			this.maxX = coordRT.x;
			this.maxY = coordRT.y;
			this.crs = resultImgData._Crs;

			if (!gdWriter.geoTiffWriter(outFilePath, resultImgData._Crs, coordLB, coordRT, width, height, bc,
					resultImgData._pImage)) {
				ret = ProcessCode.ERROR_FAIL_WRITE;
			} else {
				ret = ProcessCode.SUCCESS;
			}

		} catch (Exception ex) {
			System.out.println("GImageProcessor.procSimpleMergedMosaic : " + ex.toString());
			ex.printStackTrace();

			ret = ProcessCode.ERROR_FAIL_WRITE;
		}
		// ----------------------------------------------------------------------------------------------//

		return ret;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// GTiffDataReader ����Ʈ�� �����Ѵ�.
	// @ arrGdReader : GTiffDataReader ����Ʈ
	private void clearListGTiffDataReader(ArrayList<GTiffDataReader> arrGdReader) {
		if (arrGdReader.size() > 0) {
			int i = 0;
			GTiffDataReader gdReader = null;
			int size = arrGdReader.size();

			for (i = 0; i < size; i++) {
				gdReader = arrGdReader.get(i);

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
			}
		}

		arrGdReader.clear();
	}

	// Path, Row�� ������ũ ��� ���� ��Ͽ� ���� ��ø ���θ� ��ȯ�Ѵ�.
	// @ inFilePaths : �Է¿��� ���
	// @ pMosaicRegistData : Path, Row�� ������ũ ��� ���� ���
	// @ nPathRow : Path, Row ����
	// @
	// @ return : boolean ��ø ����
	private boolean checkOverlappedArea(ArrayList<GFileData> inFilePaths, GMosaicResultData[][] pMosaicRegistData,
			Coordinate nPathRow) {

		int nRow = 0, nPath = 0;

		try {
			for (nRow = 0; nRow < (int) nPathRow.y; nRow++) {
				for (nPath = 0; nPath < (int) nPathRow.x; nPath++) {
					if (pMosaicRegistData[nPath][nRow]._oFileData._strFilePath.isEmpty())
						continue;

					if (nPath >= 1 && !pMosaicRegistData[nPath - 1][nRow]._oFileData._strFilePath.isEmpty()) {
						if ((pMosaicRegistData[nPath][nRow]._imgData._mbr2d
								.getMinX() >= pMosaicRegistData[nPath - 1][nRow]._imgData._mbr2d.getMaxX())
								|| (pMosaicRegistData[nPath][nRow]._imgData._mbr2d
										.getMinX() <= pMosaicRegistData[nPath - 1][nRow]._imgData._mbr2d.getMinX())) {
							checkErr("GImageProcessor.checkOverlappedArea", inFilePaths, pMosaicRegistData, nPath - 1,
									nRow, nPath, nRow);
							return false;
						}
					}

					if (nPath + 1 < (int) nPathRow.x
							&& !pMosaicRegistData[nPath + 1][nRow]._oFileData._strFilePath.isEmpty()) {
						if ((pMosaicRegistData[nPath][nRow]._imgData._mbr2d
								.getMaxX() <= pMosaicRegistData[nPath + 1][nRow]._imgData._mbr2d.getMinX())
								|| (pMosaicRegistData[nPath][nRow]._imgData._mbr2d
										.getMinX() >= pMosaicRegistData[nPath + 1][nRow]._imgData._mbr2d.getMinX())) {
							checkErr("GImageProcessor.checkOverlappedArea", inFilePaths, pMosaicRegistData, nPath, nRow,
									nPath + 1, nRow);
							return false;
						}
					}

					if (nRow >= 1 && !pMosaicRegistData[nPath][nRow - 1]._oFileData._strFilePath.isEmpty()) {
						if ((pMosaicRegistData[nPath][nRow]._imgData._mbr2d
								.getMaxY() <= pMosaicRegistData[nPath][nRow - 1]._imgData._mbr2d.getMinY())
								|| (pMosaicRegistData[nPath][nRow]._imgData._mbr2d
										.getMaxY() >= pMosaicRegistData[nPath][nRow - 1]._imgData._mbr2d.getMaxY())) {
							checkErr("GImageProcessor.checkOverlappedArea", inFilePaths, pMosaicRegistData, nPath,
									nRow - 1, nPath, nRow);
							return false;
						}
					}

					if (nRow + 1 < (int) nPathRow.y
							&& !pMosaicRegistData[nPath][nRow + 1]._oFileData._strFilePath.isEmpty()) {
						if ((pMosaicRegistData[nPath][nRow]._imgData._mbr2d
								.getMinY() >= pMosaicRegistData[nPath][nRow + 1]._imgData._mbr2d.getMaxY())
								|| (pMosaicRegistData[nPath][nRow]._imgData._mbr2d
										.getMaxY() <= pMosaicRegistData[nPath][nRow + 1]._imgData._mbr2d.getMaxY())) {
							checkErr("GImageProcessor.checkOverlappedArea", inFilePaths, pMosaicRegistData, nPath, nRow,
									nPath, nRow + 1);
							return false;
						}
					}
				}
			}
		} catch (NullPointerException npe) {
			System.out.println("GImageProcessor.checkOverlappedArea : (NullPointerException)" + npe.toString());
			npe.printStackTrace();
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GImageProcessor.checkOverlappedArea : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GImageProcessor.checkOverlappedArea : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

		return true;
	}

	// ��ø ���� ������ ����Ѵ�.
	// @ strProcess : ���μ��� �̸�
	// @ inFilePaths : �Է¿��� ���
	// @ pMosaicRegistData : Path, Row�� ������ũ ��� ���� ���
	// @ nCPath : 1st ������ Path
	// @ nCRow : 1st ������ Row
	// @ nPath : 2nd ������ Path
	// @ nRow : 2nd ������ Row
	private void checkErr(String strProcess, ArrayList<GFileData> inFilePaths, GMosaicResultData[][] pMosaicRegistData,
			int nCPath, int nCRow, int nPath, int nRow) {
		int nPreFileNumber = 0, nFileNumber = 0;

		nPreFileNumber = getInputDataIndex(inFilePaths, pMosaicRegistData[nCPath][nCRow]._oFileData._strFilePath);
		nFileNumber = getInputDataIndex(inFilePaths, pMosaicRegistData[nPath][nRow]._oFileData._strFilePath);

		System.out.println(
				strProcess + " : WARNING - (" + nPreFileNumber + ") and (" + nFileNumber + ") is not overlapped!!!");
		System.out.println("\t Prevois File : " + pMosaicRegistData[nCPath][nCRow]._oFileData._strFilePath);
		System.out.println("\t Current File : " + pMosaicRegistData[nPath][nRow]._oFileData._strFilePath);
	}

	// ��ø ���� ������ ����Ѵ�.
	// @ inFilePaths : �Է¿��� ���
	// @ strFileName : �˻� ���� ���
	// @
	// @ return : int �˻��� �Է� ���� �ε���
	private int getInputDataIndex(ArrayList<GFileData> inFilePaths, String strFileName) {
		int nSize = 0;
		nSize = inFilePaths.size();

		for (int i = 0; i < nSize; i++) {
			if (strFileName.equals(inFilePaths.get(i)._strFilePath))
				return i;
		}

		return 0;
	}

	public ProcessCode autoMosaic(Map<String, GridCoverage2D> tifMap, String outFilePath, int dpi) {

		double allFileSize = 0;
		Iterator iter = tifMap.keySet().iterator();
		while (iter.hasNext()) {
			String path = (String) iter.next();
			File file = new File(path);
			String fnm = file.getName();
			if (fnm.endsWith(".tif") || fnm.endsWith(".tfw")) {
				double bytes = file.length();
				double kilobyte = bytes / 1024;
				double megabyte = kilobyte / 1024;
				double gigabyte = megabyte / 1024;
				allFileSize += gigabyte;
			}
		}

		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		if (allFileSize < 1) { // 2GB 미만 -> 해상도 조정 없이 모자이크 진행
			ArrayList<GFileData> inFilePaths = new ArrayList<GFileData>();
			GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;

			Iterator iterNew = tifMap.keySet().iterator();
			while (iterNew.hasNext()) {
				String path = (String) iterNew.next();
				File file = new File(path);

				GFileData gFileData = new GFileData();
				gFileData._strFilePath = file.getAbsolutePath();
				gFileData._maxBit16 = inMaxBit16;
				inFilePaths.add(gFileData);
			}

			procCode = procSimpleMergedMosaic(inFilePaths, outFilePath, resampleMethod);

			// create thumbnail
			String thumbnailPath = outFilePath.replace(".tif", ".png");
			createThumbnailImage(outFilePath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbnailPath,
					GImageProcessor.ImageFormat.IMG_PNG, 1000, GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);
			return procCode;
		} else { // 2GB 이상 -> 해상도 조정
			double resizeDpi = (1 * dpi) / allFileSize;
			double x = resizeDpi / dpi;

			GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_TIF;
			GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR;

			String tmpDir = new File(outFilePath).getParent() + "/" + "resize";
			File tmpDirFile = new File(tmpDir);
			if (!tmpDirFile.exists()) {
				tmpDirFile.mkdirs();
			}

			ArrayList<GFileData> inFilePaths = new ArrayList<GFileData>();
			GridCoverageFactory gcf = new GridCoverageFactory();
			Iterator iterNew = tifMap.keySet().iterator();
			while (iterNew.hasNext()) {
				String path = (String) iterNew.next();
				File file = new File(path);
				String fnm = file.getName();
				if (fnm.endsWith(".tif")) {
					try {
						BufferedImage image = ImageIO.read(file);
						int width = image.getWidth();
						int rewidth = (int) (width * x);
						String tmpOutFilePath = tmpDir + "/" + fnm.replace(".tif", "_tmp.tif");
						createThumbnailImage_New(file.getAbsolutePath(), inMaxBit16, tmpOutFilePath, outImgFormat,
								rewidth, resampleMethod);

						String tifPath = tmpDir + "/" + fnm;
						GridCoverage2D orgCoverage = tifMap.get(path);

						Envelope env = orgCoverage.getEnvelope();
						image = ImageIO.read(new File(tmpOutFilePath));
						GridCoverage2D coverage = gcf.create("coverage", image, env);

						GTiffDataCropper.writeGeoTiff(coverage, tifPath);

						String tfwPath = file.getAbsolutePath().replace(".tif", ".tfw");
						String retfwPath = tifPath.replace(".tif", ".tfw");

						File tfw = new File(tfwPath);
						File retfw = new File(retfwPath);
						Files.copy(tfw.toPath(), retfw.toPath(), StandardCopyOption.REPLACE_EXISTING);

						GFileData gFileData = new GFileData();
						gFileData._strFilePath = tifPath;
						gFileData._maxBit16 = inMaxBit16;
						inFilePaths.add(gFileData);

						new File(tmpOutFilePath).delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			procCode = procSimpleMergedMosaic(inFilePaths, outFilePath, resampleMethod);

			// create thumbnail
			String thumbnailPath = outFilePath.replace(".tif", ".png");
			createThumbnailImage(outFilePath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbnailPath,
					GImageProcessor.ImageFormat.IMG_PNG, 1000, GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

			try {
				FileUtils.deleteDirectory(tmpDirFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return procCode;
		}
	}

}
