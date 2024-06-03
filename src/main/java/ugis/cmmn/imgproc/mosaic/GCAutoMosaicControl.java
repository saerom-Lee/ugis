package ugis.cmmn.imgproc.mosaic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ugis.cmmn.imgproc.GImageEnhancement;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.GTiffDataWriter;
import ugis.cmmn.imgproc.data.GMosaicAlgorithmData;
import ugis.cmmn.imgproc.data.GMosaicData;
import ugis.cmmn.imgproc.data.GMosaicResultData;
import ugis.cmmn.imgproc.data.GOpenFileData;

//�ڵ� ������ũ ���� Ŭ����
public class GCAutoMosaicControl {

	///////////////////////////////////////////////////////////////////////////
	// Output

	// ������ũ ��� ����
	private GMosaicResultData _pMosaicResultData = null; // Mosaic Result Data(REGIST_DATA Type)

	///////////////////////////////////////////////////////////////////////////
	// Input

	// ������ũ ����
	private GMosaicData _pMosaicData = null;

	// Path, Row�� ������ũ ��� ���� ���
	private GMosaicResultData[][] _pMosaicRegistData = null; // Mosaic Regist Data for processing logic

	// Master ������ũ ��� ���� ���
	GMosaicResultData[] _pMasterData = null; // Mosaic Master Data(REGIST_DATA Type)

	// ������ũ �˰��� ����
	private GMosaicAlgorithmData _pMosaicAlgorithmData = null;

	// ������ũ ������ ���� ���
	ArrayList<GOpenFileData> _pAddMosaicDataArray = null; // selected data for mosaicking

	// ������ũ ������ ����
	GOpenFileData _pAddMosaicData = null;

	// ������ũ ��� ���� �̸�
	private String _strResultFileName = "";

	// ������ũ �ӽ� ��� ���� �̸� (R, G, B)
	private String _strResultTmpFileName_R = "";
	private String _strResultTmpFileName_G = "";
	private String _strResultTmpFileName_B = "";

	// ��ǥ�� ����
	public CoordinateReferenceSystem _Crs = null;

	///////////////////////////////////////////////////////////////////////////
	// Processing

	// Master ��� Ŭ����
	private GMasterRegistration _masterReg = new GMasterRegistration();

	// �ڵ� ������ũ Ŭ����
	private GAutoMosaic _autoMosaic = null;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// �ʱ�ȭ
	public void init() {
		_pMosaicData = null;
		_pMosaicRegistData = null;
		_pMosaicAlgorithmData = null;
		_pMosaicResultData = null;
	}

	// ���� ���� ����� �����Ѵ�.
	// @ pAddFileDataArray : ���� ���� ���
	public void setAddFileData(ArrayList<GOpenFileData> pAddFileDataArray) {
		_pAddMosaicDataArray = pAddFileDataArray;

		if (_pAddMosaicDataArray.size() > 0) {
			if (_pAddMosaicDataArray.get(0)._Crs != null) {
				if (CRS.equalsIgnoreMetadata(_pAddMosaicDataArray.get(0)._Crs, AbstractGridFormat.getDefaultCRS())) {
					_Crs = AbstractGridFormat.getDefaultCRS();
				} else {
					String strWkt = _pAddMosaicDataArray.get(0)._Crs.toWKT();
					try {
						_Crs = CRS.parseWKT(strWkt);
					} catch (FactoryException fe) {
						System.out.println("GCAutoMosaicControl.setAddFileData : " + fe.toString());
						fe.printStackTrace();
					}
				}
			}
		}
	}

	// �ش� ������ũ ������ ���� ��Ͽ� ���� Path, Row�� ������ũ ��� ���� ����� �����Ѵ�.
	// @ return : Coordinate nPathRow ��ȯ�� Path, Row ����
	// @ return : GMosaicResultData[][] ������ Path, Row�� ������ũ ��� ���� ���
	public GMosaicResultData[][] procPreprocessing(Coordinate nPathRow) {
		return _masterReg.procPreprocessing(_pAddMosaicDataArray, nPathRow);
	}

	// ������ũ ��� ���� ����� �����Ѵ�.
	// @ pMosaicRegistData : Path, Row�� ������ũ ��� ���� ���
	public void setMosaicRegistData(GMosaicResultData[][] pMosaicRegistData) {
		_masterReg.setMosaicRegistData(pMosaicRegistData);
	}

	// �̹��� ȭ�� ��� ������ ����Ѵ�.
	// @ pMosaicData : ������ũ ����
	// @ nPathNum : Path �ε���
	// @ nRowNum : Row �ε���
	// @
	// @ return : GMosaicResultData[][] Path, Row�� ������ũ ��� ���� ���
	public GMosaicResultData[][] calcImgStatisticsData(GMosaicData pMosaicData, int nPath, int nRow) {
		return _masterReg.calcImgStatisticsData(pMosaicData, nPath, nRow, _pAddMosaicDataArray);
	}

	// ������ũ ������ �����Ѵ�.
	// @ pMosaicData : ������ũ ����
	// @ pMosaicAlgorithmData : ������ũ �˰��� ����
	// @ pMosaicRegistData : Path, Row�� ������ũ ��� ���� ���
	// @ strResultFileName : ������ũ ��� ���� �̸�
	public void setMosaicData(GMosaicData pMosaicData, GMosaicAlgorithmData pMosaicAlgorithmData,
			GMosaicResultData[][] pMosaicRegistData, String strResultFileName) {
		init();

		_pMosaicData = pMosaicData;
		_pMosaicAlgorithmData = pMosaicAlgorithmData;
		_pMosaicRegistData = pMosaicRegistData;

		// ----------------------------------------------------------------------//
		_strResultFileName = strResultFileName;

		int nIndex = _strResultFileName.lastIndexOf('.');
		String strFilePath = _strResultFileName.substring(0, nIndex);
		_strResultTmpFileName_R = strFilePath + "_R.raw";
		_strResultTmpFileName_G = strFilePath + "_G.raw";
		_strResultTmpFileName_B = strFilePath + "_B.raw";
		// ----------------------------------------------------------------------//

		GOpenFileData pOpenFileData = null;
		GTiffDataReader gdReader = null;

		pOpenFileData = _pAddMosaicDataArray.get(0);

		// Read the input file
		try {
			gdReader = new GTiffDataReader(pOpenFileData._oFileData._strFilePath, pOpenFileData._oFileData._maxBit16);

			if (gdReader.IsOpened()) {
				GridEnvelope2D gridEnvelope = gdReader.getGridEnvelope();
				Envelope2D envelope = gdReader.getEnvelope();

				if (envelope.getWidth() != 0 && gridEnvelope.getWidth() != 0)
					_pMosaicData._dblPixelScales[0] = envelope.getWidth() / gridEnvelope.getWidth();
				if (envelope.getHeight() != 0 && gridEnvelope.getHeight() != 0)
					_pMosaicData._dblPixelScales[1] = envelope.getHeight() / gridEnvelope.getHeight();
			}

		} catch (Exception ex) {
			System.out.println("GCAutoMosaicControl.calcImgStatisticsData : " + ex.toString());
			ex.printStackTrace();

			System.out.println("\t Layer Name : " + pOpenFileData._oFileData._strFilePath);
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;
	}

	// ������ũ�� �����Ѵ�.
	public void procMosaic() {
		freeMosaicResultData(); // _pMosaicResultData �ʱ�ȭ!!

		// Check Saving Seam point file
		if (_pMosaicAlgorithmData._bSaveSeamPoint)
			writeSeamlineFile();

		// Check Open Seam Point file
		if (_pMosaicAlgorithmData._nSeamLineMethod == GSeamline.SeamlineMethod.USE_EXIST_SEAMLINE) // 2
			readSeamlineFile();

		int nIndex = 0, nImgBandNum = 0;
		String strTmp = "";
		_pMosaicResultData = new GMosaicResultData();

		nIndex = _strResultFileName.length() - 4;
		strTmp = _strResultFileName.substring(0, nIndex);

		try {

			for (int nPath = 0; nPath < _pMosaicData._nNumOfPath; nPath++) {
				for (int nRow = 0; nRow < _pMosaicData._nNumOfRow; nRow++) {
					if (_pMosaicRegistData[nPath][nRow]._bCheckMaster)
						nImgBandNum = _pMosaicRegistData[nPath][nRow]._imgData._lBandNum;
				}
			}

			for (int i = 0; i < nImgBandNum; i++) {
				if (i == 0 && nImgBandNum != 1 && !_pMosaicAlgorithmData._bSaveSeamPoint) {
					_pMosaicAlgorithmData._bSaveSeamPoint = true;
					strTmp = _strResultFileName.substring(0, _strResultFileName.length() - 4);
					_pMosaicAlgorithmData._strSPointDataFileName = strTmp + "_SeamPointTmp.dat";
				}

				// Initialize...
				_autoMosaic = null;
				_autoMosaic = new GAutoMosaic();
				_autoMosaic.setBandIndex(i);

				///////////////////////////
				// Create Mosaic Image
				procAutoMosaicking();
				// End Create Mosaic Image
				////////////////////////////

				// Save Seam Point
				if (_pMosaicAlgorithmData._bSaveSeamPoint && i == 0)
					saveSeamPoint(_pMosaicAlgorithmData._strSPointDataFileName);

				if (_pMosaicData._nBandOderingArray.size() < 1) {
					_pMosaicResultData = _autoMosaic.getMosaicResultData();
					break;
				}

				createTmpMosaicFile(i);

				for (int nPath = 0; nPath < _pMosaicData._nNumOfPath; nPath++) {
					for (int nRow = 0; nRow < _pMosaicData._nNumOfRow; nRow++) {
						_pMosaicRegistData[nPath][nRow]._bUseChecking = false;
					}
				}

				if (i == 2) {
					createColorResultImage();

					File file = new File(_pMosaicAlgorithmData._strSPointDataFileName);
					if (file.exists())
						file.delete();
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GCAutoMosaicControl.procMosaic : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// ������ũ ��� ������ �����Ѵ�.
	public void saveImageFile() {
		if (_strResultFileName.isEmpty())
			return;

		if (saveTiffFile() < 0)
			System.out.println("GCAutoMosaicControl.saveImageFile : ERROR - Mosaic(GeoTiff) Error!");
		else
			System.out.println("GCAutoMosaicControl.saveImageFile : Mosaic(GeoTiff) OK!");

		freeMosaicResultData();
	}

	// Ŭ������ �ʱ�ȭ��Ų��.
	public void destroy() {
		int i = 0, j = 0;
		if (_pMasterData != null) {
			for (i = 0; i < _pMosaicData._nNumOfMaster; i++) {
				_pMasterData[i]._imgData._pImage = null;

				for (j = 0; j < 256; j++) {
					_pMasterData[i]._lHistogramR[j] = 0;
					_pMasterData[i]._lHistogramG[j] = 0;
					_pMasterData[i]._lHistogramB[j] = 0;
				}

				for (j = 0; j < 3; j++) {
					_pMasterData[i]._pMn[j] = 0;
					_pMasterData[i]._pMx[j] = 255;
				}
			}
		}

		_pMasterData = null;
		////////////////////////////////////////////////////////
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// �ڵ� ������ũ�� �����Ѵ�.
	private void procAutoMosaicking() {
		// Get Data for mosaic processing
		double[] dblPixelScales = { 0, 0 };
		int i = 0, j = 0;

		dblPixelScales[0] = _pMosaicData._dblPixelScales[0];
		dblPixelScales[1] = _pMosaicData._dblPixelScales[1];

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GCAutoMosaicControl.procAutoMosaicking :");
		}
//[DEBUG]			

		// Initialize...
		for (i = 0; i < _pMosaicData._nNumOfPath; i++) {
			for (j = 0; j < _pMosaicData._nNumOfRow; j++) {
				_pMosaicRegistData[i][j]._imgData._pImage = null;
			}
		}

		/////////////////////////////////////////////////////////////////////////
		// Auto Mosaic Image Processing(logic)
		/////////////////////////////////////////////////////////////////////////
		GMosaicResultData[] pMasterData = null;

		// _pMasterData ����!!
		/////////////////////////////////////////////////////////////////////////
		_pMasterData = new GMosaicResultData[_pMosaicData._nNumOfMaster];
		for (i = 0; i < _pMosaicData._nNumOfMaster; i++) {
			_pMasterData[i] = new GMosaicResultData();
		}
		pMasterData = _pMasterData;
		/////////////////////////////////////////////////////////////////////////

		for (i = 0; i < _pMosaicData._nNumOfMaster; i++) {
			pMasterData[i]._bCheckData = false;
			pMasterData[i]._bCheckMaster = true;
			pMasterData[i]._bUseChecking = false;
		}

		// Get Logic Count
		int nLogicCount = _masterReg.getLogicCount();

		_autoMosaic.setLogicCount(nLogicCount - 1);
		_autoMosaic.setMosaicData(_pMosaicData, _pMosaicRegistData, _pMosaicAlgorithmData);

		// -------------------------------------------------------------------------------------------//
		// {_nBandOderingArray} R : 2, G : 1, B : 0, length == 0 -> Gray
		// -------------------------------------------------------------------------------------------//
		if (_pMosaicData._nBandOderingArray.size() > 0)
			_autoMosaic.recalcStatData();

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Mosaic The Master Area");
		}
//[DEBUG]
		// Master Area Mosaic
		_autoMosaic.procMosaicMasterArea(pMasterData);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Mosaic The Sub Area");
		}
//[DEBUG]		
		// Sub Mosaic
		_autoMosaic.procMosaicSubArea(pMasterData);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Create The Master Mosiac Image");
		}
//[DEBUG]				
		// Create Master Mosaic image(nRowesult image)
		_autoMosaic.procCreateMasterMosiacImage(pMasterData);
		/////////////////////////////////////////////////////////////////////////
	}

	// ������ũ ��� ������ GeoTiff ���Ϸ� �����Ѵ�.
	// @ return : int ���� ���
	private int saveTiffFile() {
		int result = -1;
		int bitspersample = 0;
		int bc = 1;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("GCAutoMosaicControl.saveTiffFile");
		}
//[DEBUG]

		if (_pMosaicData._nBandOderingArray.size() == 3)
			bc = 3;
		else
			bc = 1;

		bitspersample = bc * 8;

		//////////////////////////////////////////////////////////////////////////
		// Begin Save GeoTiff
		//////////////////////////////////////////////////////////////////////////
		String strOutputFile = _strResultFileName;

		// if(bitspersample == 24 || bitspersample == 8)
		if (bc == 3 || bc == 1) {
			GTiffDataWriter gdWriter = new GTiffDataWriter();

			byte[] lpByte = _pMosaicResultData._imgData._pImage; // ȭ������
			int dwWidth = (int) _pMosaicResultData._imgData._imgBox2d.getWidth(); // ������ �ʺ�
			int dwHeight = (int) _pMosaicResultData._imgData._imgBox2d.getHeight(); // ������ ����
			Coordinate coordLB = new Coordinate(_pMosaicResultData._imgData._mbr2d.getMinX(),
					_pMosaicResultData._imgData._mbr2d.getMinY());
			Coordinate coordRT = new Coordinate(_pMosaicResultData._imgData._mbr2d.getMaxX(),
					_pMosaicResultData._imgData._mbr2d.getMaxY());

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("\t lpByte : size = " + lpByte.length);
				System.out.println("\t bc = " + bc);
				System.out.println("\t dwWidth = " + dwWidth);
				System.out.println("\t dwHeight = " + dwHeight);
				System.out.println("\t coordLB : x = " + coordLB.x + ", y = " + coordLB.y);
				System.out.println("\t coordRT : x = " + coordRT.x + ", y = " + coordRT.y);
			}
//[DEBUG]

			try {
				if (gdWriter.geoTiffWriter(strOutputFile, _Crs, coordLB, coordRT, dwWidth, dwHeight, bc, lpByte))
					result = 1;
				else
					result = -1;
			} catch (Exception ex) {
				System.out.println("GCAutoMosaicControl.saveTiffFile : " + ex.toString());
				ex.printStackTrace();
			}

		}
		//////////////////////////////////////////////////////////////////////////
		// End Save GeoTiff
		//////////////////////////////////////////////////////////////////////////

		if (result < 0) {
			File file = new File(strOutputFile);
			if (file.exists())
				file.delete();
		}

		return result;
	}

	// Seam Line ������ �����Ѵ�.
	private void writeSeamlineFile() {
		String strSeamData = "", strTmp = "";
		int nIndex = 0;

		if (_pMosaicAlgorithmData._strSPointFileName.isEmpty())
			return;

		nIndex = _pMosaicAlgorithmData._strSPointFileName.length() - 4;
		_pMosaicAlgorithmData._strSPointDataFileName = _pMosaicAlgorithmData._strSPointFileName.substring(0, nIndex)
				+ "_seampoint.dat";

		try {
			// ���� ��ü ����
			File file = new File(_pMosaicAlgorithmData._strSPointFileName);
			// ��� ��Ʈ�� ����
			FileWriter filewriter = new FileWriter(file);
			// ��� ���� ����
			BufferedWriter bufWriter = new BufferedWriter(filewriter);

			writeString(bufWriter, false, "##### Auto Image Mosaicking Program ######", "");
			writeString(bufWriter, false, "* Existing Seamline Method", "");
			if (_pMosaicAlgorithmData._nMosaicProcDirection == GAutoMosaic.MosaicProcMethod.VERTICAL) {
				writeString(bufWriter, "MosaicLogicMethod", 0);
			} else if (_pMosaicAlgorithmData._nMosaicProcDirection == GAutoMosaic.MosaicProcMethod.Horizontal) {
				writeString(bufWriter, "MosaicLogicMethod", 1);
			}

			if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.NONE) {
				writeString(bufWriter, "HistogramMatchingMethod", 0);
			} else if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATCH_MEAN_STD_DEV) {
				writeString(bufWriter, "HistogramMatchingMethod", 1);
			} else if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATH_CUMULATIVE_FREQUENCY) {
				writeString(bufWriter, "HistogramMatchingMethod", 2);
			} else if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT) {
				writeString(bufWriter, "HistogramMatchingMethod", 3);
			}

			writeString(bufWriter, "NumberOfImage", _pAddMosaicDataArray.size());
			writeString(bufWriter, "NumberOfPath", _pMosaicData._nNumOfPath);
			writeString(bufWriter, "NumberOfRow", _pMosaicData._nNumOfRow);
			writeString(bufWriter, "NumberOfMaster", _pMosaicData._nNumOfMaster);
			writeString(bufWriter, false, "", "");

			for (int p = 0; p < _pMosaicData._nNumOfPath; p++) {
				for (int r = 0; r < _pMosaicData._nNumOfRow; r++) {
					if (_pMosaicRegistData[p][r]._bCheckData) {
						writeString(bufWriter, "Path", p);
						writeString(bufWriter, "Row", r);
						writeString(bufWriter, true, "layername", _pMosaicRegistData[p][r]._oFileData._strFilePath);

						if (_pMosaicRegistData[p][r]._oFileData._maxBit16 == GTiffDataReader.BIT16ToBIT8.MAX_BIT11) {
							writeString(bufWriter, "MaxBit16", 0);
						} else if (_pMosaicRegistData[p][r]._oFileData._maxBit16 == GTiffDataReader.BIT16ToBIT8.MAX_BIT16) {
							writeString(bufWriter, "MaxBit16", 1);
						}

						writeString(bufWriter, "CheckMaster", (_pMosaicRegistData[p][r]._bCheckMaster) ? 1 : 0);
						writeString(bufWriter, "LogicOrder", _pMosaicRegistData[p][r]._nLogicOrder);
						writeString(bufWriter, "ReferenceWeight", _pMosaicRegistData[p][r]._nRefWeight);
						strTmp = Double.toString(_pMosaicRegistData[p][r]._dblSimilarity);
						writeString(bufWriter, true, "Similarity", strTmp);
						strTmp = Double.toString(_pMosaicRegistData[p][r]._statData._dblVariance) + " \r\n";
						writeString(bufWriter, true, "Variance", strTmp);
					}
				}
			}

			writeString(bufWriter, false, "** Seam Point **", "");
			writeString(bufWriter, true, "SeamFileName", _pMosaicAlgorithmData._strSPointDataFileName);

			bufWriter.close();
			filewriter.close();
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.writeSeamlineFile : " + ioe.toString());
			ioe.printStackTrace();
		}
	}

	// Seam Line ������ �о�´�.
	private void readSeamlineFile() {
		String strSeamData = "", strTmp = "";
		int nImageDataNum = 0, nPathNum = 0, nRowNum = 0;

		if (_pMosaicAlgorithmData._strSPointFileName.isEmpty())
			return;

		try {
			// ���� ��ü ����
			File file = new File(_pMosaicAlgorithmData._strSPointFileName);
			// �Է� ��Ʈ�� ����
			FileReader filereader = new FileReader(file);
			// �Է� ���� ����
			BufferedReader bufReader = new BufferedReader(filereader);

			readString(bufReader, false, "");
			readString(bufReader, false, "");
			readString(bufReader, false, "");
			readString(bufReader, false, "");

			strTmp = readString(bufReader, true, "MosaicLogicMethod");
			switch (Integer.parseInt(strTmp)) {
			case 0:
				_pMosaicAlgorithmData._nMosaicProcDirection = GAutoMosaic.MosaicProcMethod.VERTICAL;
				break;
			case 1:
				_pMosaicAlgorithmData._nMosaicProcDirection = GAutoMosaic.MosaicProcMethod.Horizontal;
				break;
			}

			strTmp = readString(bufReader, true, "HistogramMatchingMethod");
			switch (Integer.parseInt(strTmp)) {
			case 0:
				_pMosaicAlgorithmData._nHistogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.NONE;
				break;
			case 1:
				_pMosaicAlgorithmData._nHistogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.MATCH_MEAN_STD_DEV;
				break;
			case 2:
				_pMosaicAlgorithmData._nHistogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.MATH_CUMULATIVE_FREQUENCY;
				break;
			case 3:
				_pMosaicAlgorithmData._nHistogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT;
				break;
			}

			strTmp = readString(bufReader, true, "NumberOfImage");
			nImageDataNum = Integer.parseInt(strTmp);
			// Check Number of Data
			if (nImageDataNum != _pAddMosaicDataArray.size()) {
				System.out.println("GCAutoMosaicControl.readSeamlineFile : WARNING - Number Of Data");
				return;
			}

			strTmp = readString(bufReader, true, "NumberOfPath");
			nPathNum = Integer.parseInt(strTmp);
			// Check Number of Path
			if (nPathNum != _pMosaicData._nNumOfPath) {
				System.out.println("GCAutoMosaicControl.readSeamlineFile : WARNING - Number Of Path");
				return;
			}

			strTmp = readString(bufReader, true, "NumberOfRow");
			nRowNum = Integer.parseInt(strTmp);
			// Check Number of Row
			if (nRowNum != _pMosaicData._nNumOfRow) {
				System.out.println("GCAutoMosaicControl.readSeamlineFile : WARNING - Number Of Row");
				return;
			}

			strTmp = readString(bufReader, true, "NumberOfMaster");
			_pMosaicData._nNumOfMaster = Integer.parseInt(strTmp);
			readString(bufReader, false, "");

			// Read REGIST DATA
			for (int i = 0; i < nImageDataNum; i++) {
				readString(bufReader, false, "");
				strTmp = readString(bufReader, true, "Path");
				nPathNum = Integer.parseInt(strTmp);
				strTmp = readString(bufReader, true, "Row");
				nRowNum = Integer.parseInt(strTmp);

				strTmp = readString(bufReader, true, "LayerName");
				strTmp = readString(bufReader, true, "MaxBit16");

				if (_pMosaicRegistData[nPathNum][nRowNum]._bCheckData) {
					strTmp = readString(bufReader, true, "CheckMaster");
					_pMosaicRegistData[nPathNum][nRowNum]._bCheckMaster = (Integer.parseInt(strTmp) == 1) ? true
							: false;
					strTmp = readString(bufReader, true, "LogicOrder");
					_pMosaicRegistData[nPathNum][nRowNum]._nLogicOrder = Integer.parseInt(strTmp);
					strTmp = readString(bufReader, true, "ReferenceWeight");
					_pMosaicRegistData[nPathNum][nRowNum]._nRefWeight = Integer.parseInt(strTmp);
					strTmp = readString(bufReader, true, "Similarity");
					_pMosaicRegistData[nPathNum][nRowNum]._dblSimilarity = Double.parseDouble(strTmp);
					strTmp = readString(bufReader, true, "Variance");
					_pMosaicRegistData[nPathNum][nRowNum]._statData._dblVariance = Double.parseDouble(strTmp);
				} else {
					System.out.println("GCAutoMosaicControl.readSeamlineFile : Error - Image Information");
					return;
				}
			}

			readString(bufReader, false, "");
			readString(bufReader, false, "");
			readString(bufReader, false, "");

			_pMosaicAlgorithmData._strSPointDataFileName = readString(bufReader, true, "SeamFileName");

			// Read Seam Point file data
			_pMosaicAlgorithmData._strSeamPoint = copyFileToData(_pMosaicAlgorithmData._strSPointDataFileName);

			bufReader.close();
			filereader.close();
		} catch (FileNotFoundException fex) {
			System.out.println("GCAutoMosaicControl.readSeamlineFile : " + fex.toString());
			fex.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.readSeamlineFile : " + ioe.toString());
			ioe.printStackTrace();
		}
	}

	// ���ڿ��� �ش� ���Ͽ� �����Ѵ�.
	// @ bufWriter : ��� ����
	// @ bTab : Tab ��� ����
	// @ strFormat : ���� ���ڿ�
	// @ strData : ���ڿ� ���� ����
	private void writeString(BufferedWriter bufWriter, boolean bTab, String strFormat, String strData) {
		String strText = "";

		if (bTab)
			strText = strFormat + " : " + strData + "\r\n";
		else
			strText = strFormat + "\r\n\r\n";

		try {
			bufWriter.write(strText);
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.writeString : " + ioe.toString());
			ioe.printStackTrace();
		}
	}

	// ���ڿ��� �ش� ���Ͽ� �����Ѵ�.
	// @ bufWriter : ��� ����
	// @ strFormat : ���� ���ڿ�
	// @ nData : ������ ���� ����
	private void writeString(BufferedWriter bufWriter, String strFormat, int nData) {
		String strText = "";

		strText = strFormat + " : " + Integer.toString(nData) + "\r\n";

		try {
			bufWriter.write(strText);
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.writeString : " + ioe.toString());
			ioe.printStackTrace();
		}
	}

	// �ش� ���Ͽ��� ���ڿ��� �о�´�.
	// @ bufReader : �Է� ����
	// @ bTab : Tab ��� ����
	// @ strData : ���ڿ� ���� ����
	// @
	// @ return : String �о�� ���ڿ�
	private String readString(BufferedReader bufReader, boolean bTab, String strData) {
		String strText = "", strLine = "";

		try {
			strLine = bufReader.readLine();

			if (bTab) {
				int nIndex = strLine.indexOf(":") + 2;
				strText = strLine.substring(nIndex, strLine.length() - nIndex - 1);
			}

			strText = strText.trim();
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.readString : " + ioe.toString());
			ioe.printStackTrace();

			strText = "";
		}

		return strText;
	}

	// Seam Point ������ �����Ѵ�.
	// @ strFilePath : ���� ���
	private void saveSeamPoint(String strFilePath) {
		String strSeamPoint = "";
		int nSize = 0;

		strSeamPoint = _pMosaicAlgorithmData._strSeamPoint;

		try {
			// ���� ��ü ����
			File file = new File(strFilePath);
			// ��� ��Ʈ�� ����
			FileWriter filewriter = new FileWriter(file);
			// ��� ���� ����
			BufferedWriter bufWriter = new BufferedWriter(filewriter);

			nSize = strSeamPoint.length();
			bufWriter.write(strSeamPoint, 0, nSize);

			bufWriter.close();
			filewriter.close();
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.saveSeamPoint : " + ioe.toString());
			ioe.printStackTrace();
		}
	}

	// ���� ������ ���ڿ��� ��ȯ�Ѵ�.
	// @ strFilePath : ���� ���
	private String copyFileToData(String strFilePath) {
		String strTemp = "";
		int dwSize = 0;
		char[] temp = null;

		try {
			// ���� ��ü ����
			File file = new File(_pMosaicAlgorithmData._strSPointFileName);
			// �Է� ��Ʈ�� ����
			FileReader filereader = new FileReader(file);
			// �Է� ���� ����
			BufferedReader bufReader = new BufferedReader(filereader);

			dwSize = (int) file.length();
			temp = new char[dwSize + 1];
			temp[dwSize] = '\0';

			bufReader.read(temp, 0, dwSize);
			strTemp = String.valueOf(temp);
			temp = null;

			bufReader.close();
			filereader.close();
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.copyFileToData : " + ioe.toString());
			ioe.printStackTrace();
		}

		return strTemp;
	}

	// ������ũ ��� ������ �ʱ�ȭ�Ѵ�.
	private void freeMosaicResultData() {
		int i = 0;

		if (_pMosaicResultData != null) {
			_pMosaicResultData._imgData._pImage = null;

			for (i = 0; i < 256; i++) {
				_pMosaicResultData._lHistogramR[i] = 0;
				_pMosaicResultData._lHistogramG[i] = 0;
				_pMosaicResultData._lHistogramB[i] = 0;
			}

			for (i = 0; i < 3; i++) {
				_pMosaicResultData._pMn[i] = 0;
				_pMosaicResultData._pMx[i] = 255;
			}

		}

		_pMosaicResultData = null;
	}

	// ������ũ �ӽ� ������ �����Ѵ�.
	// @ nBandOder : ��� ���� (2 : R, 1 : G, 0 : B)
	private boolean createTmpMosaicFile(int nBandOder) {
		boolean bRes = false;

		if (_pMosaicData._nBandOderingArray.size() > 0) {
			GMosaicResultData pMosaicResultData = new GMosaicResultData();
			String strResultRaw = "";

			pMosaicResultData = _autoMosaic.getMosaicResultData();

			switch (_pMosaicData._nBandOderingArray.get(nBandOder).intValue()) {
			case 0:
				strResultRaw = _strResultTmpFileName_B;
				break;
			case 1:
				strResultRaw = _strResultTmpFileName_G;
				break;
			case 2:
				strResultRaw = _strResultTmpFileName_R;
				break;
			}

			try {
				int nSize = (int) pMosaicResultData._imgData._imgBox2d.getWidth()
						* (int) pMosaicResultData._imgData._imgBox2d.getHeight();

				// ���� ��ü ����
				File file = new File(strResultRaw);
				// ��� ��Ʈ�� ����
				FileOutputStream fileOutStream = new FileOutputStream(file);

				fileOutStream.write(pMosaicResultData._imgData._pImage, 0, nSize);
				fileOutStream.close();

			} catch (IOException ioe) {
				System.out.println("GCAutoMosaicControl.createTmpMosaicFile : " + ioe.toString());
				ioe.printStackTrace();
			}
		}

		return bRes;
	}

	private boolean createColorResultImage() {
		File RBandImgFile, GBandImgFile, BBandImgFile;
		int nSize = 0, nResultIndex = 0;
		int width = 0, height = 0;
		byte[] pRBufferper = null;
		byte[] pGBufferper = null;
		byte[] pBBufferper = null;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("GCAutoMosaicControl.createColorResultImage");
		}
//[DEBUG]

		_pMosaicResultData = _autoMosaic.getMosaicResultData();

		try {
			// ���� ��ü ����
			RBandImgFile = new File(_strResultTmpFileName_R);
			GBandImgFile = new File(_strResultTmpFileName_G);
			BBandImgFile = new File(_strResultTmpFileName_B);

			if (!RBandImgFile.exists() || !RBandImgFile.isFile()) {
				return false;
			}
			if (!GBandImgFile.exists() || !GBandImgFile.isFile()) {
				return false;
			}
			if (!BBandImgFile.exists() || !BBandImgFile.isFile()) {
				return false;
			}

			// ��� ��Ʈ�� ����
			FileInputStream RBandFileInStream = new FileInputStream(RBandImgFile);
			FileInputStream GBandFileInStream = new FileInputStream(GBandImgFile);
			FileInputStream BBandFileInStream = new FileInputStream(BBandImgFile);

			width = (int) _pMosaicResultData._imgData._imgBox2d.getWidth();
			height = (int) _pMosaicResultData._imgData._imgBox2d.getHeight();

			pRBufferper = new byte[width];
			pGBufferper = new byte[width];
			pBBufferper = new byte[width];

			nSize = width * height * 3;
			_pMosaicResultData._imgData._pImage = new byte[nSize];

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("\t Mosaic Result Image : w = " + width + ", h = " + height + ", size = " + nSize);
			}
//[DEBUG]

			for (int i = 0; i < height; i++) {
				RBandFileInStream.read(pRBufferper, 0, width);
				GBandFileInStream.read(pGBufferper, 0, width);
				BBandFileInStream.read(pBBufferper, 0, width);

				for (int j = 0; j < width; j++) {
					nResultIndex = (j + width * i) * 3;
					_pMosaicResultData._imgData._pImage[nResultIndex + 0] = pRBufferper[j]; // Red
					_pMosaicResultData._imgData._pImage[nResultIndex + 1] = pGBufferper[j]; // Green
					_pMosaicResultData._imgData._pImage[nResultIndex + 2] = pBBufferper[j]; // Blue
				}
			}

			RBandFileInStream.close();
			GBandFileInStream.close();
			BBandFileInStream.close();

			RBandImgFile.delete();
			GBandImgFile.delete();
			BBandImgFile.delete();
		} catch (IOException ioe) {
			System.out.println("GCAutoMosaicControl.createColorResultImage : " + ioe.toString());
			ioe.printStackTrace();

			return false;
		}

		return true;
	}
}
