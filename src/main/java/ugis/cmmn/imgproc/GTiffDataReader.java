package ugis.cmmn.imgproc;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffMetadata2CRSAdapter;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

//GeoTiff ����(���� �� �б�) ���� Ŭ����
public class GTiffDataReader {

	public enum RGBBand {
		RED_BAND("0"), GREEN_BAND("1"), BLUE_BAND("2"), ALPHA_BAND("3");

		private String name;

		RGBBand(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public enum BIT16ToBIT8 {
		MAX_BIT11("11"), // �Ƹ���-3, �Ƹ���-3A, �������� ����
		MAX_BIT16("16"); // Landsat-8 (default)

		private String name;

		BIT16ToBIT8(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// Resampling ���
	public enum ResamplingMethod {
		NEAREST_NEIGHBOR("Nearest Neighbor"), BILINEAR("Bilinear"), CUBIC_CONVOLUTION("Cubic Convolution");

		private String name;

		ResamplingMethod(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private GridCoverage2D _grid2d = null;
	private int[] _iPixels = null;
	private boolean _IsAbsoluteCoordinateSystem = false;
	private int _maxBitFor16bit = 2048; // 11bit �ִ밪
	private int _div16BitTo8Bit = 8; // 16bit -> 8bit ��ȯ��

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ������1 : GeoTiff ������ �����Ѵ�.
	// @ filePath : ���� ���
	// @ maxBit16 : ������ 16��Ʈ �ִ� ��Ʈ ����
	public GTiffDataReader(String filePath, BIT16ToBIT8 maxBit16) throws Exception, IOException {
		boolean isThreeBandLimit = true;
		GTiffDataReaderBandLimit(filePath, maxBit16, isThreeBandLimit);

		/////////////////////////////////////////////////////////////////////////////////////////////////
		// -> GTiffDataReaderBandLimit �Լ��� ����
		/////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * boolean isOnlyGeotiff = false; CoordinateReferenceSystem crs = null; boolean
		 * isTfwFile = true; File geoTiffFile = new File(filePath);
		 * 
		 * _maxBitFor16bit = (int)Math.pow(2, Integer.parseInt(maxBit16.toString()));
		 * _div16BitTo8Bit = (int)Math.pow(2, Integer.parseInt(maxBit16.toString()) -
		 * 8);
		 * 
		 * if(!geoTiffFile.exists() || !geoTiffFile.isFile()){
		 * System.out.println("GTiffDataReader : Exist no file - " +
		 * filePath.toString()); return; }
		 * 
		 * //---------------------------------------------------------------------------
		 * -----------------// if(!isOnlyGeotiff) { try { crs = getCRSInfo(filePath);
		 * 
		 * //tfw file path int lastIdx = filePath.lastIndexOf("."); String tfwFilePath =
		 * filePath.substring(0, lastIdx) + ".tfw"; File tfwFile = new
		 * File(tfwFilePath);
		 * 
		 * if(!tfwFile.exists() || !tfwFile.isFile()){ isTfwFile = false; } }
		 * catch(Exception ex) {
		 * System.out.println("GTiffDataReader : (Exception) - getCRSInfo - " +
		 * ex.toString()); ex.printStackTrace(); } }
		 * //---------------------------------------------------------------------------
		 * -----------------//
		 * 
		 * try{ ParameterValue<OverviewPolicy> policy =
		 * AbstractGridFormat.OVERVIEW_POLICY.createValue();
		 * policy.setValue(OverviewPolicy.IGNORE);
		 * 
		 * //this will basically read 4 tiles worth of data at once from the disk
		 * ParameterValue<String> gridSize =
		 * AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();
		 * 
		 * //Setting read type: use JAI ImageRead(true) or ImageReaders read
		 * methods(false) ParameterValue<Boolean> useJaiRead =
		 * AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
		 * useJaiRead.setValue(true);
		 * 
		 * //---------------------------------------------------------------------------
		 * -----------------// if(isOnlyGeotiff) { Hints hints = new
		 * Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
		 * //---------------------------------------------------------------
		 * //GridCoverage2DReader reader = new GeoTiffReader(geoTiffFile);
		 * //---------------------------------------------------------------
		 * GeoTiffReader reader = new GeoTiffReader(geoTiffFile, hints);
		 * //---------------------------------------------------------------
		 * 
		 * this._grid2d = reader.read(new GeneralParameterValue[]{ policy, gridSize,
		 * useJaiRead });
		 * 
		 * crs = _grid2d.getCoordinateReferenceSystem();
		 * if(CRS.equalsIgnoreMetadata(crs, AbstractGridFormat.getDefaultCRS())) {
		 * this._IsAbsoluteCoordinateSystem = false; } else {
		 * this._IsAbsoluteCoordinateSystem = true; } } else { if(crs != null ||
		 * isTfwFile) { GridCoverage2DReader reader = new GeoTiffReader(geoTiffFile);
		 * this._grid2d = reader.read(new GeneralParameterValue[]{ policy, gridSize,
		 * useJaiRead });
		 * 
		 * this._IsAbsoluteCoordinateSystem = true; } else { //[DEBUG] if(_IS_DEBUG) {
		 * System.out.println("[DEBUG]GTiffDataReader : None CRS"); } //[DEBUG]
		 * 
		 * //Default Coordinate System CoordinateReferenceSystem tempCrs =
		 * AbstractGridFormat.getDefaultCRS(); this._grid2d = openTiffFile(filePath,
		 * tempCrs); } }
		 * //---------------------------------------------------------------------------
		 * -----------------//
		 * 
		 * if(this._grid2d != null){ if(this._grid2d.getNumSampleDimensions() > 3) {
		 * this._grid2d.dispose(true); this._grid2d = null;
		 * this._IsAbsoluteCoordinateSystem = false;
		 * 
		 * System.out.println("GTiffDataReader : No support the 4 band file - " +
		 * filePath.toString()); return; } }
		 * 
		 * } catch(NullPointerException npe) {
		 * System.out.println("GTiffDataReader : (NullPointerException) " +
		 * npe.toString()); npe.printStackTrace();
		 * 
		 * this._grid2d = null; this._IsAbsoluteCoordinateSystem = false; }
		 * catch(ArithmeticException ae) {
		 * System.out.println("GTiffDataReader : (ArithmeticException) " +
		 * ae.toString()); ae.printStackTrace();
		 * 
		 * this._grid2d = null; this._IsAbsoluteCoordinateSystem = false; }
		 * catch(DataSourceException dse) {
		 * System.out.println("GTiffDataReader : (DataSourceException) " +
		 * dse.toString()); dse.printStackTrace();
		 * 
		 * this._grid2d = null; this._IsAbsoluteCoordinateSystem = false; }
		 * catch(Exception ex) { System.out.println("GTiffDataReader : (Exception) " +
		 * ex.toString()); ex.printStackTrace();
		 * 
		 * this._grid2d = null; this._IsAbsoluteCoordinateSystem = false; }
		 */
		/////////////////////////////////////////////////////////////////////////////////////////////////

	}

	// ������2 : GeoTiff ������ �����Ѵ�.
	// @ filePath : ���� ���
	// @ maxBit16 : ������ 16��Ʈ �ִ� ��Ʈ ����
	// @ isThreeBandLimit : ������ RGB 3��� ���Ϸ� ���� ����
	public GTiffDataReader(String filePath, BIT16ToBIT8 maxBit16, boolean isThreeBandLimit)
			throws Exception, IOException {
		GTiffDataReaderBandLimit(filePath, maxBit16, isThreeBandLimit);
	}

	// ���� ���� ������ ���� GeoTiff ������ �����Ѵ�.
	// @ filePath : ���� ���
	// @ maxBit16 : ������ 16��Ʈ �ִ� ��Ʈ ����
	// @ isThreeBandLimit : ������ RGB 3��� ���Ϸ� ���� ����
	private void GTiffDataReaderBandLimit(String filePath, BIT16ToBIT8 maxBit16, boolean isThreeBandLimit)
			throws Exception, IOException {
		boolean isOnlyGeotiff = false;
		CoordinateReferenceSystem crs = null;
		boolean isTfwFile = true;
		File geoTiffFile = new File(filePath);

		_maxBitFor16bit = (int) Math.pow(2, Integer.parseInt(maxBit16.toString()));
		_div16BitTo8Bit = (int) Math.pow(2, Integer.parseInt(maxBit16.toString()) - 8);

		if (!geoTiffFile.exists() || !geoTiffFile.isFile()) {
			System.out.println("GTiffDataReader : Exist no file - " + filePath.toString());
			return;
		}

		// --------------------------------------------------------------------------------------------//
		if (!isOnlyGeotiff) {
			try {
				crs = getCRSInfo(filePath);

				// tfw file path
				int lastIdx = filePath.lastIndexOf(".");
				String tfwFilePath = filePath.substring(0, lastIdx) + ".tfw";
				File tfwFile = new File(tfwFilePath);

				if (!tfwFile.exists() || !tfwFile.isFile()) {
					isTfwFile = false;
				}
			} catch (Exception ex) {
				System.out.println("GTiffDataReader : (Exception) - getCRSInfo - " + ex.toString());
				ex.printStackTrace();
			}
		}
		// --------------------------------------------------------------------------------------------//

		try {
			ParameterValue<OverviewPolicy> policy = AbstractGridFormat.OVERVIEW_POLICY.createValue();
			policy.setValue(OverviewPolicy.IGNORE);

			// this will basically read 4 tiles worth of data at once from the disk
			ParameterValue<String> gridSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();

			// Setting read type: use JAI ImageRead(true) or ImageReaders read
			// methods(false)
			ParameterValue<Boolean> useJaiRead = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
			useJaiRead.setValue(true);

			// --------------------------------------------------------------------------------------------//
			if (isOnlyGeotiff) {
				Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
				// ---------------------------------------------------------------
				// GridCoverage2DReader reader = new GeoTiffReader(geoTiffFile);
				// ---------------------------------------------------------------
				GeoTiffReader reader = new GeoTiffReader(geoTiffFile, hints);
				// ---------------------------------------------------------------

				this._grid2d = reader.read(new GeneralParameterValue[] { policy, gridSize, useJaiRead });

				crs = _grid2d.getCoordinateReferenceSystem();
				if (CRS.equalsIgnoreMetadata(crs, AbstractGridFormat.getDefaultCRS())) {
					this._IsAbsoluteCoordinateSystem = false;
				} else {
					this._IsAbsoluteCoordinateSystem = true;
				}
			} else {
				if (crs != null || isTfwFile) {
					GridCoverage2DReader reader = new GeoTiffReader(geoTiffFile);
					this._grid2d = reader.read(new GeneralParameterValue[] { policy, gridSize, useJaiRead });

					this._IsAbsoluteCoordinateSystem = true;
				} else {
					// [DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]GTiffDataReader : None CRS");
					}
					// [DEBUG]

					// Default Coordinate System
					CoordinateReferenceSystem tempCrs = AbstractGridFormat.getDefaultCRS();
					this._grid2d = openTiffFile(filePath, tempCrs);
				}
			}
			// --------------------------------------------------------------------------------------------//

			//
			if (isThreeBandLimit) {
				if (this._grid2d != null) {
					// ---------------------------------------------------------//
					// ������ RGB 3��� ���Ϸ� ���� ����
					if (this._grid2d.getNumSampleDimensions() > 3) {
						this._grid2d.dispose(true);
						this._grid2d = null;
						this._IsAbsoluteCoordinateSystem = false;

						System.out.println("GTiffDataReader : No support the 4 band file - " + filePath.toString());
						return;
					}
					// ---------------------------------------------------------//
				}
			}

		} catch (NullPointerException npe) {
			System.out.println("GTiffDataReader : (NullPointerException) " + npe.toString());
			npe.printStackTrace();

			this._grid2d = null;
			this._IsAbsoluteCoordinateSystem = false;
		} catch (ArithmeticException ae) {
			System.out.println("GTiffDataReader : (ArithmeticException) " + ae.toString());
			ae.printStackTrace();

			this._grid2d = null;
			this._IsAbsoluteCoordinateSystem = false;
		} catch (DataSourceException dse) {
			System.out.println("GTiffDataReader : (DataSourceException) " + dse.toString());
			dse.printStackTrace();

			this._grid2d = null;
			this._IsAbsoluteCoordinateSystem = false;
		} catch (Exception ex) {
			System.out.println("GTiffDataReader : (Exception) " + ex.toString());
			ex.printStackTrace();

			this._grid2d = null;
			this._IsAbsoluteCoordinateSystem = false;
		}
	}

	// Ŀ������ ������ �ʱ�ȭ�Ѵ�.
	public void destory() {
		if (_grid2d != null)
			_grid2d.dispose(true);
		_grid2d = null;
		_iPixels = null;
		_IsAbsoluteCoordinateSystem = false;
	}

	// ���� ���� ������ ��ȯ�Ѵ�.
	// @
	// @ return : boolean ���� ���� ����
	public boolean IsOpened() {
		if (_grid2d != null)
			return true;
		else
			return false;
	}

	// ������ǥ�� ������ ��ȯ�Ѵ�.
	// @
	// @ return : boolean ������ǥ�� ����
	public boolean IsAbsoluteCoordinateSystem() {
		return _IsAbsoluteCoordinateSystem;
	}

	// ������ ��ǥ�� ������ ��ȯ�Ѵ�.
	// @ filePath : ���� ���
	// @
	// @ return : CoordinateReferenceSystem ��ǥ�� ����
	public CoordinateReferenceSystem getCRSInfo(String filePath) throws Exception, IOException {
		File geoTiffFile = new File(filePath);
		CoordinateReferenceSystem foundCrs = null;
		TIFFImageReaderSpi readerSPI = null;
		ImageReader reader = null;
		ImageInputStream inStream = null;
		IIOMetadata iioMetadata = null;
		GeoTiffIIOMetadataDecoder metadata = null;

		if (!geoTiffFile.exists() || !geoTiffFile.isFile()) {
			System.out.println("GTiffDataReader.getCRSInfo : Exist no file - " + filePath.toString());
			return foundCrs;
		}

		readerSPI = new TIFFImageReaderSpi();
		reader = readerSPI.createReaderInstance();
		inStream = ImageIO.createImageInputStream(geoTiffFile);
		reader.setInput(inStream);
		iioMetadata = reader.getImageMetadata(0);

//[DEBUG]
//		if(_IS_DEBUG) {
//			String[] names = iioMetadata.getMetadataFormatNames();
//			int length = names.length;
//			for(int i=0; i<length; i++) {
//				displayMetaData(iioMetadata.getAsTree(names[i]));
//			}
//		}
//[DEBUG]		

		try {
			if (iioMetadata != null)
				metadata = new GeoTiffIIOMetadataDecoder(iioMetadata);

			if (metadata != null) {
				Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
				GeoTiffMetadata2CRSAdapter gtcs = new GeoTiffMetadata2CRSAdapter(hints);

				if (gtcs != null)
					foundCrs = gtcs.createCoordinateSystem(metadata);
			}
		} catch (IllegalArgumentException iae) {
			// System.out.println("GTiffDataReader.getCRSInfo : (IllegalArgumentException) "
			// + iae.toString());
			// iae.printStackTrace();

			foundCrs = null;
		} catch (UnsupportedOperationException uoe) {
			// System.out.println("GTiffDataReader.getCRSInfo :
			// (UnsupportedOperationException) " + uoe.toString());
			// uoe.printStackTrace();

			foundCrs = null;
		} catch (NullPointerException npe) {
			// System.out.println("GTiffDataReader.getCRSInfo : (NullPointerException) " +
			// npe.toString());
			// npe.printStackTrace();

			foundCrs = null;
		} catch (ArithmeticException ae) {
			// System.out.println("GTiffDataReader.getCRSInfo : (ArithmeticException) " +
			// ae.toString());
			// ae.printStackTrace();

			foundCrs = null;
		} catch (DataSourceException dse) {
			// System.out.println("GTiffDataReader.getCRSInfo : (DataSourceException) " +
			// dse.toString());
			// dse.printStackTrace();

			foundCrs = null;
		} catch (Exception ex) {
			// System.out.println("GTiffDataReader.getCRSInfo : (Exception) " +
			// ex.toString());
			// ex.printStackTrace();

			foundCrs = null;
		}

		reader.dispose();
		geoTiffFile = null;

		return foundCrs;
	}

	// Tiff ������ �����Ѵ�.
	// @ filePath : ���� ���
	// @ inCrs : ���� ��ǥ��
	// @
	// @ return : GridCoverage2D �׸��� Ŀ������ ����
	public GridCoverage2D openTiffFile(String filePath, CoordinateReferenceSystem inCrs) throws IOException {
		GridCoverage2D gc = null;
		File tiffFile = new File(filePath);

		if (!tiffFile.exists() || !tiffFile.isFile()) {
			System.out.println("GTiffDataReader.openGeoTiffWithNoCRS : Exist no file - " + filePath.toString());
			return gc;
		}

		try {
			TIFFImageReaderSpi readerSPI = new TIFFImageReaderSpi();
			ImageReader reader = readerSPI.createReaderInstance();
			ImageInputStream inStream = ImageIO.createImageInputStream(tiffFile);
			double LBX = 0, LBY = 0, RTX = 0, RTY = 0;
			int w = 0, h = 0;
			double resX = 1, resY = 1;
			double orgX = 0, orgY = 0;

			reader.setInput(inStream);
			w = reader.getWidth(0);
			h = reader.getHeight(0);

			LBX = orgX;
			LBY = orgY - (double) h * resY;
			RTX = orgX + (double) w * resX;
			RTY = orgY;

//[DEBUG]
			if (_IS_DEBUG) {
				int gridOffsetX = reader.getTileGridXOffset(0);
				int gridOffsetY = reader.getTileGridYOffset(0);

				System.out.println("[DEBUG]GTiffDataReader.openTiffFile : ");
				System.out.println("[DEBUG]\t w = " + w + ", h = " + h);
				System.out.println("[DEBUG]\t gridOffsetX = " + gridOffsetX + ", gridOffsetY = " + gridOffsetY);
				System.out.println("[DEBUG]\t resX = " + resX + ", resY = " + resY);
				System.out.println("[DEBUG]\t orgX = " + orgX + ", orgY = " + orgY);
				System.out.println("[DEBUG]\t LBX = " + LBX + ", LBY = " + LBY);
				System.out.println("[DEBUG]\t RTX = " + RTX + ", RTY = " + RTY);
			}
//[DEBUG]

			GridCoverageFactory gcf = new GridCoverageFactory();
			BufferedImage image = ImageIO.read(tiffFile);
			ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(LBX, RTX, LBY, RTY, inCrs);

			gc = gcf.create("name", image, referencedEnvelope);
		} catch (IOException ex) {
			System.out.println("GTiffDataReader.openTiffFile : " + ex.toString());
			ex.printStackTrace();

			gc = null;
		}

		return gc;
	}

	// ���� ������ ��ǥ�� ������ ��ȯ�Ѵ�.
	// @
	// @ return : CoordinateReferenceSystem ��ǥ�� ����
	public CoordinateReferenceSystem getCrs() {
		if (_grid2d == null)
			return null;
		else
			return _grid2d.getCoordinateReferenceSystem();
	}

	// ���� ������ ���� ������ ��ȯ�Ѵ�.
	// @
	// @ return : GridGeometry2D ���� ����
	public GridGeometry2D getGridGeom() {
		if (_grid2d == null)
			return null;
		else
			return _grid2d.getGridGeometry();
	}

	// ���� ������ ���� ����(������ǥ)�� ��ȯ�Ѵ�.
	// @
	// @ return : Envelope2D ���� ����(������ǥ)
	public Envelope2D getEnvelope() {
		if (_grid2d == null)
			return null;
		else
			return _grid2d.getGridGeometry().getEnvelope2D();
	}

	// ���� ������ ���� ����(������ǥ)�� ��ȯ�Ѵ�.
	// @
	// @ return : GridEnvelope2D ���� ����(������ǥ)
	public GridEnvelope2D getGridEnvelope() {
		if (_grid2d == null)
			return null;
		else
			return _grid2d.getGridGeometry().getGridRange2D();
	}

	// ���� ������ ������ ��ȯ�Ѵ�.
	// @
	// @ return : int ������ ����
	public int getBandCount() {
		if (_grid2d == null)
			return 0;
		else
			return _grid2d.getNumSampleDimensions();
	}

	// ���� ������ ���� ������ ������ ��ȯ�Ѵ�.
	// @
	// @ return : int ������ ���� ������ ����
	public int getDataType() {
		if (_grid2d == null)
			return 0;
		else {
			SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
			int dataBufferType = TypeMap.getDataBufferType(sdType);

			return dataBufferType;
		}
	}

	// ���� ������ ���ô� ��Ʈ���� ��ȯ�Ѵ�.
	// @
	// @ return : int ������ ���ô� ��Ʈ��
	public int getBitPerSample() {
		int sbpp = 8;

		if (_grid2d != null) {
			SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
			int dataBufferType = TypeMap.getDataBufferType(sdType);

			switch (dataBufferType) {
			case DataBuffer.TYPE_BYTE:
				sbpp = 8;
				break;
			case DataBuffer.TYPE_SHORT:
			case DataBuffer.TYPE_USHORT:
				sbpp = 16;
				break;
			case DataBuffer.TYPE_INT:
			case DataBuffer.TYPE_FLOAT:
				sbpp = 32;
				break;
			case DataBuffer.TYPE_DOUBLE:
				sbpp = 64;
				break;
			case DataBuffer.TYPE_UNDEFINED:
			default:
				sbpp = 8;
				break;
			}
		}

		return sbpp;
	}

	// ���� ������ ���ô� ����Ʈ���� ��ȯ�Ѵ�.
	// @
	// @ return : int ������ ���ô� ����Ʈ��
	public int getBytePerSample() {
		if (_grid2d == null)
			return 0;
		else
			return getBitPerSample() / 8;
	}

	// ���� ������ �ȼ��� ����Ʈ���� ��ȯ�Ѵ�.
	// @
	// @ return : int ������ �ȼ��� ����Ʈ��
	public int getBytePerPixel() {
		if (_grid2d == null)
			return 0;
		else
			return getBytePerSample() * getBandCount();
	}

	// ���� ������ ���� ũ�⸦ ��ȯ�Ѵ�.
	// @
	// @ return : int[2] ���� ũ��
	public int[] getGridSize() {
		int[] gridSize = { 0, 0 };

		if (_grid2d != null) {
			gridSize[0] = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
			gridSize[1] = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		}
		return gridSize;
	}

	public void loadPixelsInt() {
		initPixelsInt();
		_iPixels = getAllPixelsInt();
	}

	public void initPixelsInt() {
		if (_iPixels != null) {
			_iPixels = null;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Covert the coordinate

	// ������ǥ�� ������ǥ�� ��ȯ�Ѵ�.
	// @ inCoord : ������ǥ
	// @
	// @ return : DirectPosition ��ȯ�� ������ǥ
	public DirectPosition getCRSToGrid(Coordinate inCoord) throws TransformException {
		MathTransform2D transCRSToGrid2d = getGridGeom().getCRSToGrid2D();
		CoordinateReferenceSystem crs = _grid2d.getCoordinateReferenceSystem();
		DirectPosition gdPixel = new DirectPosition2D(0, 0);
		DirectPosition gdPos = new DirectPosition2D(crs, inCoord.x, inCoord.y);

		try {
			transCRSToGrid2d.transform(gdPos, gdPixel);
		} catch (TransformException te) {
			System.out.println("GTiffDataReader.getCRSToGrid : (TransformException) " + te.toString());
			te.printStackTrace();
		}

		return gdPixel;
	}

	// ������ǥ�� ������ǥ�� ��ȯ�Ѵ�.
	// @ inCoord : ������ǥ
	// @
	// @ return : DirectPosition ��ȯ�� ������ǥ
	public DirectPosition getGridToCRS(Coordinate inCoord) throws TransformException {
		MathTransform2D transGridToCRS2D = getGridGeom().getGridToCRS2D();
		DirectPosition gdPixel = new DirectPosition2D(inCoord.x, inCoord.y);
		DirectPosition gdPos = new DirectPosition2D(0, 0);

		try {
			transGridToCRS2D.transform(gdPixel, gdPos);
		} catch (TransformException te) {
			System.out.println("GTiffDataReader.getGridToCRS : (TransformException) " + te.toString());
			te.printStackTrace();
		}

		return gdPos;
	}

	// ���� ���� ���� ���뿵���� ȭ�ҿ������� ��ȯ�Ѵ�.
	// @ coordLB : ������ ���ϴ� ������ǥ
	// @ coordRT : ������ ���� ������ǥ
	// @
	// @ return : Rectangle ȭ�ҿ���
	public Rectangle convertEnvelopeToGridEnvelope(Coordinate coordLB, Coordinate coordRT) {
		Rectangle gridRec = null;

		try {
			DirectPosition gridLB = getCRSToGrid(coordLB);
			DirectPosition gridRT = getCRSToGrid(coordRT);
			double x = 0, y = 0, w = 0, h = 0;

			x = gridLB.getCoordinate()[0];
			y = gridRT.getCoordinate()[1];
			w = Math.abs(gridRT.getCoordinate()[0] - gridLB.getCoordinate()[0]);
			h = Math.abs(gridLB.getCoordinate()[1] - gridRT.getCoordinate()[1]);

			// ���� �ݿø�
			gridRec = new Rectangle((int) (x + 0.5), (int) (y + 0.5), (int) (w + 0.5), (int) (h + 0.5));
		} catch (TransformException te) {
			System.out.println("GTiffDataReader.convertEnvelopeToGridEnvelope : (TransformException) " + te.toString());
			te.printStackTrace();

			gridRec = null;
		}

		return gridRec;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get the pixel value at the GIS position about this band

	// 16��Ʈ ȭ�Ұ�(2Byte)�� 8��Ʈ ȭ�Ұ�(1Byte)���� ��ȯ�Ѵ�.
	// @ bit16Data : 16��Ʈ ȭ�Ұ�(2Byte)
	// @
	// @ return : int 8��Ʈ ȭ�Ұ�(1Byte)
	public int convertData16bitTo8bit(int bit16Data) {
		int bit8Data = 0;
		double Temp = (double) bit16Data / (double) _div16BitTo8Bit;

		if (Temp <= 0)
			bit8Data = 0;
		else if (Temp >= 255)
			bit8Data = 255;
		else {
			bit8Data = (int) Math.ceil(Temp); // �ø�
			if (bit8Data >= 255)
				bit8Data = 255;
		}

		return bit8Data;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ����� Byte�� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coord : ������ǥ
	// @ band : ���
	// @
	// @ return : byte ȭ�Ұ�
	public byte getValueByCoordByte(Coordinate coord, RGBBand band) {
		byte retVal = 0; // no data
		Envelope2D envelope = _grid2d.getGridGeometry().getEnvelope2D();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);
		int bc = _grid2d.getNumSampleDimensions();
		int[] gridVals = new int[bc];
		int i = 0;
		CoordinateReferenceSystem crs = _grid2d.getCoordinateReferenceSystem();
		DirectPosition gdPos = new DirectPosition2D(crs, coord.x, coord.y);
		int nBand = Integer.parseInt(band.toString());

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByCoordByte : ");
			System.out
					.println("[DEBUG]\t gdPos : x = " + gdPos.getCoordinate()[0] + ", y = " + gdPos.getCoordinate()[1]);
		}
//[DEBUG]

		try {
			if (envelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);

				// ------------------------------------------------//
				// 16 bit (unsigned short) -> 8 bit(byte) ��ȯ
				// ------------------------------------------------//
				switch (dataBufferType) {
				case DataBuffer.TYPE_SHORT:
				case DataBuffer.TYPE_USHORT: {
					for (i = 0; i < bc; i++) {
						gridVals[i] = convertData16bitTo8bit(gridVals[i]);
					}
				}
					break;
				}
				// ------------------------------------------------//

				if (0 <= nBand && nBand <= _grid2d.getNumSampleDimensions()) {
					retVal = (byte) gridVals[nBand];
				}
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByCoordByte : " + e.toString());
			e.printStackTrace();

			retVal = 0;
		}

		return retVal;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ����� ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coord : ������ǥ
	// @ band : ���
	// @
	// @ return : int ȭ�Ұ�
	public int getValueByCoordInt(Coordinate coord, RGBBand band) {
		int retVal = 0; // no data
		Envelope2D envelope = _grid2d.getGridGeometry().getEnvelope2D();
		int[] gridVals = new int[_grid2d.getNumSampleDimensions()];
		CoordinateReferenceSystem crs = _grid2d.getCoordinateReferenceSystem();
		DirectPosition gdPos = new DirectPosition2D(crs, coord.x, coord.y);
		int nBand = Integer.parseInt(band.toString());

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByCoordInt : ");
			System.out
					.println("[DEBUG]\t gdPos : x = " + gdPos.getCoordinate()[0] + ", y = " + gdPos.getCoordinate()[1]);
		}
//[DEBUG]

		try {
			if (envelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);

				if (0 <= nBand && nBand <= _grid2d.getNumSampleDimensions()) {
					retVal = gridVals[nBand];
				}
			} else {
				retVal = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByCoordInt : " + e.toString());
			e.printStackTrace();

			retVal = 0;
		}

		return retVal;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ����� �Ǽ��� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coord : ������ǥ
	// @ band : ���
	// @
	// @ return : double ȭ�Ұ�
	public double getValueByCoordDouble(Coordinate coord, RGBBand band) {
		double retVal = 0; // no data
		Envelope2D envelope = _grid2d.getGridGeometry().getEnvelope2D();
		double[] gridVals = new double[_grid2d.getNumSampleDimensions()];
		CoordinateReferenceSystem crs = _grid2d.getCoordinateReferenceSystem();
		DirectPosition gdPos = new DirectPosition2D(crs, coord.x, coord.y);
		int nBand = Integer.parseInt(band.toString());

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByCoordDouble : ");
			System.out
					.println("[DEBUG]\t gdPos : x = " + gdPos.getCoordinate()[0] + ", y = " + gdPos.getCoordinate()[1]);
		}
//[DEBUG]

		try {
			if (envelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);

				if (0 <= nBand && nBand <= _grid2d.getNumSampleDimensions()) {
					retVal = gridVals[nBand];
				}
			} else {
				retVal = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByCoordDouble : " + e.toString());
			e.printStackTrace();

			retVal = 0;
		}

		return retVal;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get the pixel value at the grid position per this band

	// �ش� ��ġ(������ǥ) �� �ش� ����� Byte�� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ col : ������ǥ X
	// @ row : ������ǥ Y
	// @ band : ���
	// @
	// @ return : byte ȭ�Ұ�
	public byte getValueByPixelByte(int col, int row, RGBBand band) {
		byte retVal = 0; // no data
		GridEnvelope2D gridEnvelope = _grid2d.getGridGeometry().getGridRange2D();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);
		int bc = _grid2d.getNumSampleDimensions();
		int[] gridVals = new int[bc];
		int i = 0;
		GridCoordinates2D gdPos = new GridCoordinates2D(col, row);
		int nBand = Integer.parseInt(band.toString());

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByPixelByte : ");
			System.out.println("[DEBUG]\t gdPos : col = " + gdPos.getX() + ", row = " + gdPos.getY());
		}
//[DEBUG]

		try {
			if (gridEnvelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);

				// ------------------------------------------------//
				// 16 bit (unsigned short) -> 8 bit(byte) ��ȯ
				// ------------------------------------------------//
				switch (dataBufferType) {
				case DataBuffer.TYPE_SHORT:
				case DataBuffer.TYPE_USHORT: {
					for (i = 0; i < bc; i++) {
						gridVals[i] = convertData16bitTo8bit(gridVals[i]);
					}
				}
					break;
				}
				// ------------------------------------------------//

				if (0 <= nBand && nBand <= _grid2d.getNumSampleDimensions()) {
					retVal = (byte) gridVals[nBand];
				}
			} else {
				retVal = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByPixelByte : " + e.toString());
			e.printStackTrace();

			retVal = 0;
		}

		return retVal;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ����� ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ col : ������ǥ X
	// @ row : ������ǥ Y
	// @ band : ���
	// @
	// @ return : int ȭ�Ұ�
	public int getValueByPixelInt(int col, int row, RGBBand band) {
		int retVal = 0; // no data
		GridEnvelope2D gridEnvelope = _grid2d.getGridGeometry().getGridRange2D();
		int[] gridVals = new int[_grid2d.getNumSampleDimensions()];
		GridCoordinates2D gdPos = new GridCoordinates2D(col, row);
		int nBand = Integer.parseInt(band.toString());

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByPixelInt : ");
			System.out.println("[DEBUG]\t gdPos : col = " + gdPos.getX() + ", row = " + gdPos.getY());
		}
//[DEBUG]

		try {
			if (gridEnvelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);

				if (0 <= nBand && nBand <= _grid2d.getNumSampleDimensions()) {
					retVal = gridVals[nBand];
				}
			} else {
				retVal = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByPixelInt : " + e.toString());
			e.printStackTrace();

			retVal = 0;
		}

		return retVal;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ����� �Ǽ��� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ col : ������ǥ X
	// @ row : ������ǥ Y
	// @ band : ���
	// @
	// @ return : double ȭ�Ұ�
	public double getValueByPixelDouble(int col, int row, RGBBand band) {
		double retVal = 0; // no data
		GridEnvelope2D gridEnvelope = _grid2d.getGridGeometry().getGridRange2D();
		double[] gridVals = new double[_grid2d.getNumSampleDimensions()];
		GridCoordinates2D gdPos = new GridCoordinates2D(col, row);
		int nBand = Integer.parseInt(band.toString());

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByPixelDouble : ");
			System.out.println("[DEBUG]\t gdPos : col = " + gdPos.getX() + ", row = " + gdPos.getY());
		}
//[DEBUG]

		try {
			if (gridEnvelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);

				if (0 <= nBand && nBand <= _grid2d.getNumSampleDimensions()) {
					retVal = gridVals[nBand];
				}
			} else {
				retVal = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByPixelDouble : " + e.toString());
			e.printStackTrace();

			retVal = 0;
		}

		return retVal;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get the pixel value at the grid position

	// �ش� ��ġ(������ǥ)�� ��庰 Byte�� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ col : ������ǥ X
	// @ row : ������ǥ Y
	// @
	// @ return : byte[] ȭ�Ұ� ( [1] or [3] )
	public byte[] getValueByPixelByte(int col, int row) {
		GridEnvelope2D gridEnvelope = _grid2d.getGridGeometry().getGridRange2D();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);
		int bc = _grid2d.getNumSampleDimensions();
		byte[] gridVals = new byte[bc];
		int i = 0;
		GridCoordinates2D gdPos = new GridCoordinates2D(col, row);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByPixelByte : ");
			System.out.println("[DEBUG]\t gdPos : col = " + gdPos.getX() + ", row = " + gdPos.getY());
		}
//[DEBUG]

		try {
			if (gridEnvelope.contains(gdPos)) {
				int[] tmpGridVals = new int[bc];
				_grid2d.evaluate(gdPos, tmpGridVals);

				// ------------------------------------------------//
				// 16 bit (unsigned short) -> 8 bit(byte) ��ȯ
				// ------------------------------------------------//
				switch (dataBufferType) {
				case DataBuffer.TYPE_SHORT:
				case DataBuffer.TYPE_USHORT: {
					for (i = 0; i < bc; i++) {
						tmpGridVals[i] = convertData16bitTo8bit(tmpGridVals[i]);
					}
				}
					break;
				}
				// ------------------------------------------------//

				for (i = 0; i < bc; i++)
					gridVals[i] = (byte) tmpGridVals[i];
			} else {
				for (i = 0; i < bc; i++)
					gridVals[i] = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByPixelByte : " + e.toString());
			e.printStackTrace();

			for (i = 0; i < bc; i++)
				gridVals[i] = 0;
		}

		return gridVals;
	}

	// �ش� ��ġ(������ǥ)�� ��庰 ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ col : ������ǥ X
	// @ row : ������ǥ Y
	// @
	// @ return : int[] ȭ�Ұ� ( [1] or [3] )
	public int[] getValueByPixelInt(int col, int row) {
		GridEnvelope2D gridEnvelope = _grid2d.getGridGeometry().getGridRange2D();
		int bc = _grid2d.getNumSampleDimensions();
		int i = 0;
		int[] gridVals = new int[bc];
		GridCoordinates2D gdPos = new GridCoordinates2D(col, row);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByPixelInt : ");
			System.out.println("[DEBUG]\t gdPos : col = " + gdPos.getX() + ", row = " + gdPos.getY());
		}
//[DEBUG]

		try {
			if (gridEnvelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);
			} else {
				for (i = 0; i < bc; i++)
					gridVals[i] = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByPixelInt : " + e.toString());
			e.printStackTrace();

			for (i = 0; i < bc; i++)
				gridVals[i] = 0;
		}

		return gridVals;
	}

	// �ش� ��ġ(������ǥ)�� ��庰 �Ǽ��� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ col : ������ǥ X
	// @ row : ������ǥ Y
	// @
	// @ return : double[] ȭ�Ұ� ( [1] or [3] )
	public double[] getValueByPixelDouble(int col, int row) {
		GridEnvelope2D gridEnvelope = _grid2d.getGridGeometry().getGridRange2D();
		int bc = _grid2d.getNumSampleDimensions();
		int i = 0;
		double[] gridVals = new double[bc];
		GridCoordinates2D gdPos = new GridCoordinates2D(col, row);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getValueByPixelDouble : ");
			System.out.println("[DEBUG]\t gdPos : col = " + gdPos.getX() + ", row = " + gdPos.getY());
		}
//[DEBUG]

		try {
			if (gridEnvelope.contains(gdPos)) {
				_grid2d.evaluate(gdPos, gridVals);
			} else {
				for (i = 0; i < bc; i++)
					gridVals[i] = 0;
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByPixelDouble : " + e.toString());
			e.printStackTrace();

			for (i = 0; i < bc; i++)
				gridVals[i] = 0;
		}

		return gridVals;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get all pixel value

	// ���� ������ ��ü Byte�� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @
	// @ return : byte[] ��ü ȭ�Ұ�
	public byte[] getAllPixelsByte() {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);
		int bc = _grid2d.getNumSampleDimensions();
		byte[] gridPixels = new byte[width * height * bc];
		int i = 0, j = 0, k = 0;

		// ---------------------------------------------------------------------//
		// �ּҿ� �ִ� ������ 11��Ʈ�� ����
		// ---------------------------------------------------------------------//
		/*
		 * for(j=0; j<height; j++) { for(i=0; i<width; i++) { int[] gridVals =
		 * getValueByPixelInt(i, j);
		 * 
		 * //------------------------------------------------// //16 bit (unsigned
		 * short) -> 8 bit(byte) ��ȯ
		 * //------------------------------------------------// switch(dataBufferType){
		 * case DataBuffer.TYPE_SHORT: case DataBuffer.TYPE_USHORT: { for(k=0; k<bc;
		 * k++) { gridVals[k] = convertData16bitTo8bit(gridVals[k]); } } break; }
		 * //------------------------------------------------//
		 * 
		 * for(k=0; k<bc; k++) { gridPixels[(i + j*width)*bc + k] = (byte) gridVals[k];
		 * } } }
		 */
		// ---------------------------------------------------------------------//

		// ---------------------------------------------------------------------//
		// �ּҿ� �ִ� ������ ��ü ȭ�� �������� �˻�
		// ---------------------------------------------------------------------//
		int[] gridPixelsInt = getAllPixelsInt();

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				for (k = 0; k < bc; k++) {
					int tempValue = 0;

					// ------------------------------------------------//
					// 16 bit (unsigned short) -> 8 bit(byte) ��ȯ
					// ------------------------------------------------//
					switch (dataBufferType) {
					case DataBuffer.TYPE_SHORT:
					case DataBuffer.TYPE_USHORT:
						tempValue = convertData16bitTo8bit(gridPixelsInt[(i + j * width) * bc + k]);
						break;
					default:
						tempValue = gridPixelsInt[(i + j * width) * bc + k];
						break;
					}
					// ------------------------------------------------//

					gridPixels[(i + j * width) * bc + k] = (byte) tempValue;
				}
			}
		}
		// ---------------------------------------------------------------------//

		return gridPixels;
	}

	// ���� ������ ��ü ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @
	// @ return : int[] ��ü ȭ�Ұ�
	public int[] getAllPixelsInt() {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		int bc = _grid2d.getNumSampleDimensions();
		int[] gridPixels = new int[width * height * bc];
		int i = 0, j = 0, k = 0;

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				int[] gridVals = getValueByPixelInt(i, j);

				for (k = 0; k < bc; k++) {
					gridPixels[(i + j * width) * bc + k] = gridVals[k];
				}
			}
		}

		return gridPixels;
	}

	// ���� ������ ��ü �Ǽ��� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @
	// @ return : double[] ��ü ȭ�Ұ�
	public double[] getAllPixelsDouble() {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		int bc = _grid2d.getNumSampleDimensions();
		double[] gridPixels = new double[width * height * bc];
		int i = 0, j = 0, k = 0;

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				double[] gridVals = getValueByPixelDouble(i, j);

				for (k = 0; k < bc; k++) {
					gridPixels[(i + j * width) * bc + k] = gridVals[k];
				}
			}
		}

		return gridPixels;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get all pixel value in the sub grid envelope

	// ���� ������ �Ϻ� ȭ�ҿ����� ���� ��ü Byte�� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ gridRec : ȭ�ҿ��� ����
	// @
	// @ return : byte[] ��ü ȭ�Ұ�
	public byte[] getAllPixelsByteOfGridEnvelope(Rectangle gridRec) {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);
		int bc = _grid2d.getNumSampleDimensions();
		byte[] gridPixels = null;
		int subOrgX = 0, subOrgY = 0;
		int subW = 0, subH = 0;
		int sx = 0, ex = 0;
		int sy = 0, ey = 0;
		int[] pixel = null;
		int i = 0, j = 0, k = 0;

		if (gridRec == null)
			return gridPixels;

		subOrgX = (int) gridRec.getX();
		subOrgY = (int) gridRec.getY();
		subW = (int) gridRec.getWidth();
		subH = (int) gridRec.getHeight();

		if (subOrgX > width)
			return gridPixels;
		if (subOrgY > height)
			return gridPixels;
		if (subW == 0)
			return gridPixels;
		if (subH == 0)
			return gridPixels;

		sx = subOrgX;
		ex = subOrgX + subW;
		sy = subOrgY;
		ey = subOrgY + subH;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t sx = " + sx + ", ex = " + ex);
			System.out.println("[DEBUG]\t sy = " + sy + ", ey = " + ey);
		}
//[DEBUG]

		gridPixels = new byte[subW * subH * bc];

		// ---------------------------------------------------------------------//
		// �ּҿ� �ִ� ������ 11��Ʈ�� ����
		// ---------------------------------------------------------------------//
		/*
		 * for(i=0; i<subW; i++) { for(j=0; j<subH; j++) { pixel = getValueByPixelInt(sx
		 * + i, sy + j);
		 * 
		 * //------------------------------------------------// //16 bit (unsigned
		 * short) -> 8 bit(byte) ��ȯ
		 * //------------------------------------------------// switch(dataBufferType){
		 * case DataBuffer.TYPE_SHORT: case DataBuffer.TYPE_USHORT: { for(k=0; k<bc;
		 * k++) { pixel[k] = convertData16bitTo8bit(pixel[k]); } } break; }
		 * //------------------------------------------------//
		 * 
		 * for(k=0; k<bc; k++) { gridPixels[(i + j*subW)*bc + k] = (byte)pixel[k]; } } }
		 */
		// ---------------------------------------------------------------------//

		// ---------------------------------------------------------------------//
		// �ּҿ� �ִ� ������ ��ü ȭ�� �������� �˻�
		// ---------------------------------------------------------------------//
		int[] gridPixelsInt = getAllPixelsIntOfGridEnvelope(gridRec);

		for (i = 0; i < subW; i++) {
			for (j = 0; j < subH; j++) {
				for (k = 0; k < bc; k++) {
					int tempValue = 0;

					// ------------------------------------------------//
					// 16 bit (unsigned short) -> 8 bit(byte) ��ȯ
					// ------------------------------------------------//
					switch (dataBufferType) {
					case DataBuffer.TYPE_SHORT:
					case DataBuffer.TYPE_USHORT:
						tempValue = convertData16bitTo8bit(gridPixelsInt[(i + j * subW) * bc + k]);
						break;
					default:
						tempValue = gridPixelsInt[(i + j * subW) * bc + k];
						break;
					}
					// ------------------------------------------------//

					gridPixels[(i + j * subW) * bc + k] = (byte) tempValue;
				}
			}
		}
		// ---------------------------------------------------------------------//

		return gridPixels;
	}

	// ���� ������ �Ϻ� ȭ�ҿ����� ���� ��ü ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ gridRec : ȭ�ҿ��� ����
	// @
	// @ return : int[] ��ü ȭ�Ұ�
	public int[] getAllPixelsIntOfGridEnvelope(Rectangle gridRec) {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		int bc = _grid2d.getNumSampleDimensions();
		int[] gridPixels = null;
		int subOrgX = 0, subOrgY = 0;
		int subW = 0, subH = 0;
		int sx = 0, ex = 0;
		int sy = 0, ey = 0;
		int[] pixel = null;
		int i = 0, j = 0, k = 0;

		if (gridRec == null)
			return gridPixels;

		subOrgX = (int) gridRec.getX();
		subOrgY = (int) gridRec.getY();
		subW = (int) gridRec.getWidth();
		subH = (int) gridRec.getHeight();

		if (subOrgX > width)
			return gridPixels;
		if (subOrgY > height)
			return gridPixels;
		if (subW == 0)
			return gridPixels;
		if (subH == 0)
			return gridPixels;

		sx = subOrgX;
		ex = subOrgX + subW;
		sy = subOrgY;
		ey = subOrgY + subH;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t sx = " + sx + ", ex = " + ex);
			System.out.println("[DEBUG]\t sy = " + sy + ", ey = " + ey);
		}
//[DEBUG]     

		gridPixels = new int[subW * subH * bc];
		for (i = 0; i < subW; i++) {
			for (j = 0; j < subH; j++) {
				pixel = getValueByPixelInt(sx + i, sy + j);

				for (k = 0; k < bc; k++) {
					gridPixels[(i + j * subW) * bc + k] = pixel[k];
				}
			}
		}

		return gridPixels;
	}

	// ���� ������ �Ϻ� ȭ�ҿ����� ���� ��ü �Ǽ��� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ gridRec : ȭ�ҿ��� ����
	// @
	// @ return : double[] ��ü ȭ�Ұ�
	public double[] getAllPixelsDoubleOfGridEnvelope(Rectangle gridRec) {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		int bc = _grid2d.getNumSampleDimensions();
		double[] gridPixels = null;
		int subOrgX = 0, subOrgY = 0;
		int subW = 0, subH = 0;
		int sx = 0, ex = 0;
		int sy = 0, ey = 0;
		double[] pixel = null;
		int i = 0, j = 0, k = 0;

		if (gridRec == null)
			return gridPixels;

		subOrgX = (int) gridRec.getX();
		subOrgY = (int) gridRec.getY();
		subW = (int) gridRec.getWidth();
		subH = (int) gridRec.getHeight();

		if (subOrgX > width)
			return gridPixels;
		if (subOrgY > height)
			return gridPixels;
		if (subW == 0)
			return gridPixels;
		if (subH == 0)
			return gridPixels;

		sx = subOrgX;
		ex = subOrgX + subW;
		sy = subOrgY;
		ey = subOrgY + subH;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t sx = " + sx + ", ex = " + ex);
			System.out.println("[DEBUG]\t sy = " + sy + ", ey = " + ey);
		}
//[DEBUG]        	

		gridPixels = new double[subW * subH * bc];
		for (i = 0; i < subW; i++) {
			for (j = 0; j < subH; j++) {
				pixel = getValueByPixelDouble(sx + i, sy + j);

				for (k = 0; k < bc; k++) {
					gridPixels[(i + j * subW) * bc + k] = pixel[k];
				}
			}
		}

		return gridPixels;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get all pixel value in the sub envelope

	// ���� ������ �Ϻ� ���뿵���� ���� ��ü Byte�� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coordLB : ������ ���ϴ� ������ǥ
	// @ coordRT : ������ ���� ������ǥ
	// @
	// @ return : byte[] ��ü ȭ�Ұ�
	public byte[] getAllPixelsByteOfEnvelope(Coordinate coordLB, Coordinate coordRT) {
		Rectangle gridRec = convertEnvelopeToGridEnvelope(coordLB, coordRT);
		byte[] gridPixels = getAllPixelsByteOfGridEnvelope(gridRec);
		return gridPixels;
	}

	// ���� ������ �Ϻ� ���뿵���� ���� ��ü ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coordLB : ������ ���ϴ� ������ǥ
	// @ coordRT : ������ ���� ������ǥ
	// @
	// @ return : int[] ��ü ȭ�Ұ�
	public int[] getAllPixelsIntOfEnvelope(Coordinate coordLB, Coordinate coordRT) {
		Rectangle gridRec = convertEnvelopeToGridEnvelope(coordLB, coordRT);
		int[] gridPixels = getAllPixelsIntOfGridEnvelope(gridRec);
		return gridPixels;
	}

	// ���� ������ �Ϻ� ���뿵���� ���� ��ü �Ǽ��� ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coordLB : ������ ���ϴ� ������ǥ
	// @ coordRT : ������ ���� ������ǥ
	// @
	// @ return : double[] ��ü ȭ�Ұ�
	public double[] getAllPixelsDoubleOfEnvelope(Coordinate coordLB, Coordinate coordRT) {
		Rectangle gridRec = convertEnvelopeToGridEnvelope(coordLB, coordRT);
		double[] gridPixels = getAllPixelsDoubleOfGridEnvelope(gridRec);
		return gridPixels;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get the histogram information per this band

	// ���� ������ ��庰 ������׷� ������ ��ȯ�Ѵ�.
	// @ histgR[256] : ��ȯ�� Red ��� ������׷� ����
	// @ histgG[256] : ��ȯ�� Green ��� ������׷� ����
	// @ histgB[256] : ��ȯ�� Blue ��� ������׷� ����
	// @
	// @ return : boolean ���� ���� ����
	public boolean getHistogram(int[] histgR, int[] histgG, int[] histgB) {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		int bc = _grid2d.getNumSampleDimensions();
		byte[] pixel = new byte[3];
		int i = 0, j = 0;

		if (histgR.length != 256 || histgG.length != 256 || histgB.length != 256)
			return false;

		// Initialize
		for (i = 0; i < 256; i++) {
			histgR[i] = histgG[i] = histgB[i] = 0;
		}

		// Histogram Information
		byte[] gridPixels = getAllPixelsByte();

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				switch (bc) {
				case 1: {
					pixel[0] = gridPixels[(i + j * width) * bc + 0];

					histgR[0xff & pixel[0]] += 1;
					histgG[0xff & pixel[0]] += 1;
					histgB[0xff & pixel[0]] += 1;
				}
					break;
				case 3:
				case 4: {
					pixel[0] = gridPixels[(i + j * width) * bc + 0];
					pixel[1] = gridPixels[(i + j * width) * bc + 1];
					pixel[2] = gridPixels[(i + j * width) * bc + 2];

					histgR[0xff & pixel[0]] += 1;
					histgG[0xff & pixel[1]] += 1;
					histgB[0xff & pixel[2]] += 1;
				}
					break;
				}
			}
		}

		return true;
	}

	// [16bit]���� ������ ��庰 ������׷� ������ ��ȯ�Ѵ�.
	// @ histgR[65536] : ��ȯ�� Red ��� ������׷� ����
	// @ histgG[65536] : ��ȯ�� Green ��� ������׷� ����
	// @ histgB[65536] : ��ȯ�� Blue ��� ������׷� ����
	// @
	// @ return : boolean ���� ���� ����
	public boolean getHistogram16(int[] histgR, int[] histgG, int[] histgB) {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		int bc = _grid2d.getNumSampleDimensions();
		int[] pixel = new int[3];
		int i = 0, j = 0;

		if (histgR.length != 65536 || histgG.length != 65536 || histgB.length != 65536)
			return false;

		// Initialize
		for (i = 0; i < 65536; i++) {
			histgR[i] = histgG[i] = histgB[i] = 0;
		}

		// Histogram Information
		int[] gridPixels = getAllPixelsInt();

		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++) {
				switch (bc) {
				case 1: {
					pixel[0] = gridPixels[(i + j * width) * bc + 0];

					histgR[pixel[0]] += 1;
					histgG[pixel[0]] += 1;
					histgB[pixel[0]] += 1;
				}
					break;
				case 3:
				case 4: {
					pixel[0] = gridPixels[(i + j * width) * bc + 0];
					pixel[1] = gridPixels[(i + j * width) * bc + 1];
					pixel[2] = gridPixels[(i + j * width) * bc + 2];

					histgR[pixel[0]] += 1;
					histgG[pixel[1]] += 1;
					histgB[pixel[2]] += 1;
				}
					break;
				}
			}
		}

		return true;
	}

	// ���� ������ �ش� ���뿵���� ��庰 ������׷� ������ ��ȯ�Ѵ�.
	// @ histgR[256] : ��ȯ�� Red ��� ������׷� ����
	// @ histgG[256] : ��ȯ�� Green ��� ������׷� ����
	// @ histgB[256] : ��ȯ�� Blue ��� ������׷� ����
	// @ coordLB : ������ ���ϴ� ������ǥ
	// @ coordRT : ������ ���� ������ǥ
	// @
	// @ return : boolean ���� ���� ����
	public boolean getHistogramOfEnvelope(int[] histgR, int[] histgG, int[] histgB, Coordinate coordLB,
			Coordinate coordRT) {
		boolean ret = true;
		Rectangle gridRec = convertEnvelopeToGridEnvelope(coordLB, coordRT);

		ret = getHistogramOfGridEnvelope(histgR, histgG, histgB, gridRec);

		if (!ret) {
			// Initialize
			for (int i = 0; i < 256; i++) {
				histgR[i] = histgG[i] = histgB[i] = 0;
			}
		}

		return ret;
	}

	// ���� ������ �ش� ȭ�ҿ����� ��庰 ������׷� ������ ��ȯ�Ѵ�.
	// @ histgR[256] : ��ȯ�� Red ��� ������׷� ����
	// @ histgG[256] : ��ȯ�� Green ��� ������׷� ����
	// @ histgB[256] : ��ȯ�� Blue ��� ������׷� ����
	// @ gridRec : ȭ�ҿ��� ����
	// @
	// @ return : boolean ���� ���� ����
	public boolean getHistogramOfGridEnvelope(int[] histgR, int[] histgG, int[] histgB, Rectangle gridRec) {
		int width = (int) _grid2d.getGridGeometry().getGridRange2D().getWidth();
		int height = (int) _grid2d.getGridGeometry().getGridRange2D().getHeight();
		int bc = _grid2d.getNumSampleDimensions();
		byte[] pixel = new byte[3];
		int i = 0, j = 0;
		int subOrgX = 0, subOrgY = 0;
		int subW = 0, subH = 0;

		if (histgR.length != 256 || histgG.length != 256 || histgB.length != 256)
			return false;

		// Initialize
		for (i = 0; i < 256; i++) {
			histgR[i] = histgG[i] = histgB[i] = 0;
		}

		if (gridRec == null)
			return false;

		subOrgX = (int) gridRec.getX();
		subOrgY = (int) gridRec.getY();
		subW = (int) gridRec.getWidth();
		subH = (int) gridRec.getHeight();

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getHistogramOfGridEnvelope : ");
			System.out.println("[DEBUG]\t gridRec : orgX = " + gridRec.getX() + ", orgY = " + gridRec.getY());
			System.out.println("[DEBUG]\t gridRec : w = " + gridRec.getWidth() + ", h = " + gridRec.getHeight());
		}
//[DEBUG]    	

		if (subOrgX > width)
			return false;
		if (subOrgY > height)
			return false;
		if (subW == 0)
			return false;
		if (subH == 0)
			return false;

		// Histogram Information
		byte[] gridPixels = getAllPixelsByteOfGridEnvelope(gridRec);

		if (gridPixels != null) {
			for (i = 0; i < subW; i++) {
				for (j = 0; j < subH; j++) {
					switch (bc) {
					case 1: {
						pixel[0] = gridPixels[(i + j * subW) * bc + 0];

						histgR[0xff & pixel[0]] += 1;
						histgG[0xff & pixel[0]] += 1;
						histgB[0xff & pixel[0]] += 1;
					}
						break;
					case 3:
					case 4: {
						pixel[0] = gridPixels[(i + j * subW) * bc + 0];
						pixel[1] = gridPixels[(i + j * subW) * bc + 1];
						pixel[2] = gridPixels[(i + j * subW) * bc + 2];

						histgR[0xff & pixel[0]] += 1;
						histgG[0xff & pixel[1]] += 1;
						histgB[0xff & pixel[2]] += 1;
					}
						break;
					}
				}
			}
		}

		return true;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Get the resampling pixel value at the GIS position about this band

	// �ش� ���� �� ��ǥ ���������� ���� ������ ���� ��ü Byte�� ȭ�Ұ����� ��ȯ�Ѵ�.
	// @ range : ���� ��������
	// @ envelope : ��ǥ ��������
	// @ resampleMethod : Resampling ���
	// @
	// @ return : byte[] ���� ��ü ȭ�Ұ�
	public byte[] getResamplePixelsByte(GridEnvelope2D range, Envelope2D envelope, ResamplingMethod resampleMethod) {
		byte[] gridPixels = null;
		int bc = getBandCount();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);
		int width = (int) range.getWidth();
		int height = (int) range.getHeight();
		double resX = 0;
		double resY = 0;
		Coordinate coord = new Coordinate();
		int pixelValue = 0;

		if (range.getWidth() == 0 || range.getHeight() == 0 || envelope.getWidth() == 0 || envelope.getHeight() == 0)
			return gridPixels;

		resX = envelope.getWidth() / range.getWidth();
		resY = envelope.getHeight() / range.getHeight();
		gridPixels = new byte[width * height * bc];

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getResamplePixelsByte : ");
			System.out.println("[DEBUG]\t range size : " + range.getWidth() + ", " + range.getHeight());
			System.out.println("[DEBUG]\t envelope size : " + envelope.getWidth() + ", " + envelope.getHeight());
			System.out.println("[DEBUG]\t envelope X : " + envelope.getMinX() + ", " + envelope.getMaxX());
			System.out.println("[DEBUG]\t envelope Y : " + envelope.getMinY() + ", " + envelope.getMaxY());
			System.out.println("[DEBUG]\t Res : resX = " + resX + ", h = " + resY);
		}
//[DEBUG]

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				coord.x = envelope.getMinX() + (double) i * resX;
				coord.y = envelope.getMaxY() - (double) j * resY;

				for (int k = 0; k < bc; k++) {
					switch (k) {
					case 0:
						pixelValue = getResamplePixel(coord, RGBBand.RED_BAND, resampleMethod);
						break;
					case 1:
						pixelValue = getResamplePixel(coord, RGBBand.GREEN_BAND, resampleMethod);
						break;
					case 2:
						pixelValue = getResamplePixel(coord, RGBBand.BLUE_BAND, resampleMethod);
						break;
					case 3:
						pixelValue = getResamplePixel(coord, RGBBand.ALPHA_BAND, resampleMethod);
						break;
					}

					switch (dataBufferType) {
					case DataBuffer.TYPE_SHORT:
					case DataBuffer.TYPE_USHORT:
						gridPixels[(i + j * width) * bc + k] = (byte) (0xff & convertData16bitTo8bit(pixelValue));
						break;
					default:
						gridPixels[(i + j * width) * bc + k] = (byte) (0xff & pixelValue);
						break;
					}
				}

			}
		}

		return gridPixels;
	}

	// �ش� ���� �� ��ǥ ���������� ���� ������ ���� ��ü ������ ȭ�Ұ����� ��ȯ�Ѵ�.
	// @ range : ���� ��������
	// @ envelope : ��ǥ ��������
	// @ resampleMethod : Resampling ���
	// @
	// @ return : int[] ���� ��ü ȭ�Ұ�
	public int[] getResamplePixelsInt(GridEnvelope2D range, Envelope2D envelope, ResamplingMethod resampleMethod,
			boolean isConvertTo8Bit) {
		int[] gridPixels = null;
		int bc = getBandCount();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);
		int width = (int) range.getWidth();
		int height = (int) range.getHeight();
		double resX = 0;
		double resY = 0;
		Coordinate coord = new Coordinate();
		int pixelValue = 0;

		if (range.getWidth() == 0 || range.getHeight() == 0 || envelope.getWidth() == 0 || envelope.getHeight() == 0)
			return gridPixels;

		resX = envelope.getWidth() / range.getWidth();
		resY = envelope.getHeight() / range.getHeight();
		gridPixels = new int[width * height * bc];

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GTiffDataReader.getResamplePixelsInt : ");
			System.out.println("[DEBUG]\t range size : " + range.getWidth() + ", " + range.getHeight());
			System.out.println("[DEBUG]\t envelope size : " + envelope.getWidth() + ", " + envelope.getHeight());
			System.out.println("[DEBUG]\t envelope X : " + envelope.getMinX() + ", " + envelope.getMaxX());
			System.out.println("[DEBUG]\t envelope Y : " + envelope.getMinY() + ", " + envelope.getMaxY());
			System.out.println("[DEBUG]\t Res : resX = " + resX + ", h = " + resY);
		}
//[DEBUG]

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				coord.x = envelope.getMinX() + (double) i * resX;
				coord.y = envelope.getMaxY() - (double) j * resY;

				for (int k = 0; k < bc; k++) {
					switch (k) {
					case 0:
						pixelValue = getResamplePixel(coord, RGBBand.RED_BAND, resampleMethod);
						break;
					case 1:
						pixelValue = getResamplePixel(coord, RGBBand.GREEN_BAND, resampleMethod);
						break;
					case 2:
						pixelValue = getResamplePixel(coord, RGBBand.BLUE_BAND, resampleMethod);
						break;
					}

					switch (dataBufferType) {
					case DataBuffer.TYPE_SHORT:
					case DataBuffer.TYPE_USHORT: {
						if (isConvertTo8Bit)
							gridPixels[(i + j * width) * bc + k] = convertData16bitTo8bit(pixelValue);
						else
							gridPixels[(i + j * width) * bc + k] = pixelValue;
					}
						break;
					case DataBuffer.TYPE_INT: {
						if (isConvertTo8Bit)
							gridPixels[(i + j * width) * bc + k] = convertData16bitTo8bit(pixelValue);
						else
							gridPixels[(i + j * width) * bc + k] = pixelValue;
					}
						break;
					default:
						gridPixels[(i + j * width) * bc + k] = pixelValue;
						break;
					}
				}

			}
		}

		return gridPixels;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ��忡 ���� ������ ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coord : ������ǥ
	// @ band : ���
	// @ resampleMethod : Resampling ���
	// @
	// @ return : int ȭ�Ұ�
	public int getResamplePixel(Coordinate coord, RGBBand band, ResamplingMethod resampleMethod) {
		int retVal = 0; // no data

		if (resampleMethod == ResamplingMethod.NEAREST_NEIGHBOR)
			retVal = getResamplePixelByNearset(coord, band);
		else if (resampleMethod == ResamplingMethod.BILINEAR)
			retVal = getResamplePixelByBilinear(coord, band);
		else if (resampleMethod == ResamplingMethod.CUBIC_CONVOLUTION)
			retVal = getResamplePixelByCubic(coord, band);

		return retVal;
	}

	// �ε��� ȭ�������κ��� �ش� ��ġ(������ǥ) �� �ش� ����� ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ col : ������ǥ X
	// @ row : ������ǥ Y
	// @ band : ���
	// @
	// @ return : int ȭ�Ұ�
	private int getValueByPixelIntFromMemory(int col, int row, RGBBand band) {
		int retVal = 0; // no data
		int[] gridSize = getGridSize();
		int bc = getBandCount();
		int nBand = Integer.parseInt(band.toString());
		int index = (col + row * gridSize[0]) * bc + nBand;

		if (_iPixels == null) {
			retVal = getValueByPixelInt(col, row, band);
			return retVal;
		}

		try {
			if (col < 0 || row < 0 || col >= gridSize[0] || row >= gridSize[1]) {
				retVal = 0;
			} else {
				if (index < _iPixels.length)
					retVal = _iPixels[index];
			}
		} catch (Exception e) {
			System.out.println("GTiffDataReader.getValueByPixelIntFromMemory : " + e.toString());
			e.printStackTrace();

			retVal = 0;
		}

		return retVal;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ��忡 ���� �ֱٸ� �ٻ������ ������ ������ ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coord : ������ǥ
	// @ band : ���
	// @
	// @ return : int ȭ�Ұ�
	private int getResamplePixelByNearset(Coordinate coord, RGBBand band) {
		int retVal = 0; // no data

		try {
			DirectPosition gdPos = getCRSToGrid(coord); // ������ǥ
			int col = (int) (gdPos.getCoordinate()[0] + 0.5);
			int row = (int) (gdPos.getCoordinate()[1] + 0.5);

			retVal = getValueByPixelIntFromMemory(col, row, band);
		} catch (TransformException te) {
			System.out.println("GTiffDataReader.getResamplePixelInt : (TransformException) " + te.toString());
			te.printStackTrace();
		}

		return retVal;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ��忡 ���� Bilinear ������� ������ ������ ȭ�Ұ���
	// ��ȯ�Ѵ�.
	// @ coord : ������ǥ
	// @ band : ���
	// @
	// @ return : int ȭ�Ұ�
	private int getResamplePixelByBilinear(Coordinate coord, RGBBand band) {
		int retVal = 0; // no data
		int[] gridSize = getGridSize();

		try {
			DirectPosition gdPos = getCRSToGrid(coord); // ������ǥ
			int nX = (int) (gdPos.getCoordinate()[0]);
			int nY = (int) (gdPos.getCoordinate()[1]);
			double tx = gdPos.getCoordinate()[0] - (double) nX;
			double ty = gdPos.getCoordinate()[1] - (double) nY;
			double txty = tx * ty;
			int[][] srcValue = null;

			if (nX < 0 || nY < 0 || nX >= gridSize[0] || nY >= gridSize[1]) {
				retVal = 0; // no data
			} else {
				srcValue = new int[2][];
				srcValue[0] = new int[2];
				srcValue[1] = new int[2];

				srcValue[0][0] = getValueByPixelIntFromMemory(nX, nY, band);
				srcValue[0][1] = getValueByPixelIntFromMemory(nX + 1, nY, band);
				srcValue[1][1] = getValueByPixelIntFromMemory(nX + 1, nY + 1, band);
				srcValue[1][0] = getValueByPixelIntFromMemory(nX, nY + 1, band);

				retVal = (int) ((double) srcValue[0][0] * (1.f - ty - tx + txty) + (double) srcValue[0][1] * (tx - txty)
						+ (double) srcValue[1][1] * txty + (double) srcValue[1][0] * (ty - txty));
			}
		} catch (TransformException te) {
			System.out.println("GTiffDataReader.getResamplePixelInt : (TransformException) " + te.toString());
			te.printStackTrace();
		}

		return retVal;
	}

	// �ش� ��ġ(������ǥ) �� �ش� ��忡 ���� Cubic Convolution ������� ������ ������
	// ȭ�Ұ��� ��ȯ�Ѵ�.
	// @ coord : ������ǥ
	// @ band : ���
	// @
	// @ return : int ȭ�Ұ�
	private int getResamplePixelByCubic(Coordinate coord, RGBBand band) {
		int retVal = 0; // no data
		int[] gridSize = getGridSize();
		SampleDimensionType sdType = _grid2d.getSampleDimension(0).getSampleDimensionType();
		int dataBufferType = TypeMap.getDataBufferType(sdType);

		try {
			DirectPosition gdPos = getCRSToGrid(coord); // ������ǥ
			int nX = (int) (gdPos.getCoordinate()[0]);
			int nY = (int) (gdPos.getCoordinate()[1]);
			double[] kernelX = new double[4];
			double[] kernelY = new double[4];
			double srcValue = 0;
			double dstValue = 0;
			int col = 0, row = 0;

			if (nX < 0 || nY < 0 || nX >= gridSize[0] || nY >= gridSize[1]) {
				retVal = 0; // no data
			} else {
				// calculate multiplication factors for all pixels
				if (nX <= 0 || nX >= gridSize[0] - 2 || nY <= 0 || nY >= gridSize[1] - 2) {
					dstValue = getResamplePixelByBilinear(coord, band);
				} else {
					// Generalized Bicubic kernel (for a=1 it is the same as BicubicKernel)
					double a = 1.0; // 0.5; // Catmull-Rom interpolation
					double p = 0;
					int i = 0, j = 0;

					for (i = 0; i < 4; i++) {
						col = nX - 1 + i;
						kernelX[i] = KernelGeneralizedCubic(gdPos.getCoordinate()[0] - (double) col, a);

						row = nY - 1 + i;
						kernelY[i] = KernelGeneralizedCubic(gdPos.getCoordinate()[1] - (double) row, a);
					}

					for (j = 0; j < 4; j++) {
						row = nY - 1 + j;

						p = 0;
						for (i = 0; i < 4; i++) {
							col = nX - 1 + i;
							srcValue = (double) getValueByPixelIntFromMemory(col, row, band);
							p += srcValue * kernelX[i];
						}

						dstValue += p * kernelY[j];
					}
				}

				switch (dataBufferType) {
				case DataBuffer.TYPE_SHORT:
				case DataBuffer.TYPE_USHORT: {
					if (dstValue > _maxBitFor16bit - 1)
						dstValue = _maxBitFor16bit - 1;
					if (dstValue < 0.f)
						dstValue = 0; // ���� �� �߻� ����ó��
				}
					break;
				case DataBuffer.TYPE_BYTE: {
					if (dstValue > 255.f)
						dstValue = 255;
					if (dstValue < 0.f)
						dstValue = 0; // ���� �� �߻� ����ó��
				}
					break;
				}

				retVal = (int) dstValue;
			}
		} catch (TransformException te) {
			System.out.println("GTiffDataReader.getResamplePixelInt : (TransformException) " + te.toString());
			te.printStackTrace();
		}

		return retVal;
	}

	// Generalized Bicubic kernel ���� ��ȯ�Ѵ�.
	// @ x : kernel position
	// @ a : weight value
	// @
	// @ return : double kernel value
	/**
	 * Generalized Bicubic kernel (for a=1 it is the same as BicubicKernel): |
	 * (-a+2)|t|**3 + (a-3)|t|**2 + 1 , |t| < 1 h(t) = | -a|t|**3 + 5a|t|**2 - 8a|t|
	 * + 4a , 1 <= |t| < 2 | 0 , otherwise Often used values for a are 1 and 1/2.
	 */
	private double KernelGeneralizedCubic(double x, double a) {
		if (x < 0.0)
			x = -x; // ���밪
		double z = 0.0;
		if (x < 1.0)
			z = x * x * (x * (-a + 2.0) + (a - 3.0)) + 1.0;
		else if (x < 2.0)
			z = -a * x * x * x + 5.0 * a * x * x - 8.0 * a * x + 4.0 * a;
		return z;
	}

	// �ش� Node�� ���� ��Ÿ�����͸� ����Ѵ�.
	// @ root : Node
	private void displayMetaData(Node root) {
		displayMetaData(root, 0);
	}

	// Ư�� ������ ���� ������ ����Ѵ�.
	// @ level : ����
	private void indent(int level) {
		for (int i = 0; i < level; i++)
			System.out.print("	");
	}

	// �ش� Node�� Ư�� ������ ���� ��Ÿ�����͸� ����Ѵ�.
	// @ node : Node
	// @ level : ����
	private void displayMetaData(Node node, int level) {
		// print open tag of element
		indent(level);
		System.out.print("<" + node.getNodeName());

		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			// print attribute values
			int length = map.getLength();
			for (int i = 0; i < length; i++) {
				Node attr = map.item(i);
				System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
			}
		}

		Node child = node.getFirstChild();
		if (child == null) {
			// no children, so close element and return
			System.out.println("/>");
		}

		// children, so close current tag
		System.out.println(">");
		while (child != null) {
			// print children recursively
			displayMetaData(child, level + 1);
			child = child.getNextSibling();
		}

		// print close tag of element
		indent(level);
		System.out.println("</" + node.getNodeName() + ">");
	}

}
