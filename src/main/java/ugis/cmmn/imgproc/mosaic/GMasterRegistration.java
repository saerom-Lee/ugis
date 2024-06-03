package ugis.cmmn.imgproc.mosaic;

import java.util.ArrayList;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.Envelope2D;
import org.locationtech.jts.geom.Coordinate;

import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.data.GMosaicData;
import ugis.cmmn.imgproc.data.GMosaicResultData;
import ugis.cmmn.imgproc.data.GOpenFileData;

//Master ��� Ŭ����
public class GMasterRegistration {

	///////////////////////////////////////////////////////////////////////////
	// Input

	// Path, Row�� ������ũ ��� ���� ���
	private GMosaicResultData[][] _pRegistData = null;

	///////////////////////////////////////////////////////////////////////////

	// ������ũ ������ ���� ���
	private ArrayList<GOpenFileData> _pAddFileDataArray; // selected data for mosaicking

	// Path ����
	private int _nNumOfPath = 0;

	// Row ����
	private int _nNumOfRow = 0;

	// Master ����
	private int _nNumOfMaster = 0;

	// Main Master Path �ε���
	private int _nMainMasterPath = 0;

	// Main Master Row �ε���
	private int _nMainMasterRow = 0;

	// �ּ� X �ε���
	private int _nIndexMinX = 0;

	// �ִ� X �ε���
	private int _nIndexMaxX = 0;

	// ���� ��� ����(����ġ)
	private int _nWeight = 0;

	// ���� ����
	boolean _bProcess = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ������ũ ��� ���� ����� �����Ѵ�.
	// @ pMosaicRegistData : Path, Row�� ������ũ ��� ���� ���
	public void setMosaicRegistData(GMosaicResultData[][] pMosaicRegistData) {
		_pRegistData = pMosaicRegistData;
	}

	// Master ���� ����(����ġ)�� ��ȯ�Ѵ�.
	// @ return : int Master ���� ����(����ġ)
	public int getLogicCount() {
		return _nWeight;
	}

	// �ش� ������ũ ������ ���� ��Ͽ� ���� Path, Row�� ������ũ ��� ���� ����� �����Ѵ�.
	// @ pAddFileDataArray : ������ũ ������ ���� ���
	// @
	// @ return : Coordinate nPathRow ��ȯ�� Path, Row ����
	// @ return : GMosaicResultData[][] ������ Path, Row�� ������ũ ��� ���� ���
	public GMosaicResultData[][] procPreprocessing(ArrayList<GOpenFileData> pAddFileDataArray, Coordinate nPathRow) {
		_pAddFileDataArray = pAddFileDataArray;

		// Get Registration Table
		getImageRegistration();

		// Check Registration of Selected Images
		if (!checkMosaicDataRegistration()) {
			System.out.println("GMasterRegistration.procPreprocessing : ERROR - Image registration");
			return null;
		}

		nPathRow.x = _nNumOfPath;
		nPathRow.y = _nNumOfRow;

		return _pRegistData;
	}

	// �̹��� ȭ�� ��� ������ ����Ѵ�.
	// @ pMosaicData : ������ũ ����
	// @ nPathNum : Path �ε���
	// @ nRowNum : Row �ε���
	// @ pAddFileDataArray : ������ũ ������ ���� ���
	// @
	// @ return : GMosaicResultData[][] Path, Row�� ������ũ ��� ���� ���
	public GMosaicResultData[][] calcImgStatisticsData(GMosaicData pMosaicData, int nPathNum, int nRowNum,
			ArrayList<GOpenFileData> pAddFileDataArray) {
		// Check using overview

		_nNumOfPath = nPathNum;
		_nNumOfRow = nRowNum;
		_pAddFileDataArray = pAddFileDataArray;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.calcImgStatisticsData : Start - Get Statistics Analysis");
		}
//[DEBUG]		
		GTiffDataReader gdReader = null;
		GImageStatistics imgStatistics = new GImageStatistics();
		int nBand = 0;
		nBand = pMosaicData._nGrayBand;
		if (nBand == -1)
			nBand = 2;

		_nWeight = 0;

		try {

			// Get Statistics Analysis for finding Master image
			for (int nPath = 0; nPath < _nNumOfPath; nPath++) {
				for (int nRow = 0; nRow < _nNumOfRow; nRow++) {
					if (_pRegistData[nPath][nRow]._bCheckData) {
						// Read the input file
						try {
							gdReader = new GTiffDataReader(_pRegistData[nPath][nRow]._oFileData._strFilePath,
									_pRegistData[nPath][nRow]._oFileData._maxBit16);
						} catch (Exception ex) {
							System.out.println("GMasterRegistration.calcImgStatisticsData : " + ex.toString());
							System.out.println("\t Layer Name : " + _pRegistData[nPath][nRow]._oFileData._strFilePath);

							if (gdReader != null)
								gdReader.destory();
							gdReader = null;
							continue;
						}

						if (!gdReader.IsOpened()) {
							if (gdReader != null)
								gdReader.destory();
							gdReader = null;
							continue;
						}

						imgStatistics.getImgStatisticsData(gdReader, nBand, _pRegistData[nPath][nRow]); // ��ҿ���

						///////////////////////////////////////////////////////////////////////////////////////////////
						int[] gridSize = gdReader.getGridSize();
						GridEnvelope2D gridEnvelope = gdReader.getGridEnvelope();
						Envelope2D envelope = gdReader.getEnvelope();

						_pRegistData[nPath][nRow]._imgData._pImage = null;
						_pRegistData[nPath][nRow]._imgData._imgBox2d.setBounds(0, 0, gridSize[0], gridSize[1]);

						if (envelope.getWidth() != 0 && gridEnvelope.getWidth() != 0)
							_pRegistData[nPath][nRow]._imgData._dblPixelScales[0] = envelope.getWidth()
									/ gridEnvelope.getWidth();
						if (envelope.getHeight() != 0 && gridEnvelope.getHeight() != 0)
							_pRegistData[nPath][nRow]._imgData._dblPixelScales[1] = envelope.getHeight()
									/ gridEnvelope.getHeight();
						///////////////////////////////////////////////////////////////////////////////////////////////

						_nWeight++;

						if (gdReader != null)
							gdReader.destory();
						gdReader = null;
					}

//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t End GetPixelData(): nPath = " + nPath + ", nRow = " + nRow);
					}
//[DEBUG]
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GMasterRegistration.calcImgStatisticsData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.calcImgStatisticsData : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

		if (gdReader != null)
			gdReader.destory();
		gdReader = null;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.calcImgStatisticsData : End - Get Statistics Analysis");
		}
//[DEBUG]		

		// Get Logic Weight and Init Logic Order
		getAutoLogicWeight(_nWeight);

		// Set Onpreprocessing data
		pMosaicData._nMainMasterPath = _nMainMasterPath;
		pMosaicData._nMainMasterRow = _nMainMasterRow;
		pMosaicData._nNumOfMaster = _nNumOfMaster;
		pMosaicData._nNumOfPath = _nNumOfPath;
		pMosaicData._nNumOfRow = _nNumOfRow;

		return _pRegistData;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ����� ��ġ ������ �̿��Ͽ� ������ Path, Row�� ����Ѵ�.
	private void getImageRegistration() {
		ArrayList<ArrayList<GOpenFileData>> RegistDataArray = new ArrayList<ArrayList<GOpenFileData>>();

		// Sorting!!(dblUtmCenterY Ascending)
		setDataSort(_pAddFileDataArray);

		// Sorting!!(dblUtmCenterX Descending)
		setStripDataArray(_pAddFileDataArray, RegistDataArray);

		_nNumOfPath = calcNumOfPath(RegistDataArray);
		_nNumOfRow = RegistDataArray.size();

		setLogicTable(RegistDataArray);

		// Free Memery(CArray of CArray)!!
		////////////////////////////////////////////////////////////////////
		ArrayList<GOpenFileData> pStripDataArray = null;
		for (int i = 0; i < RegistDataArray.size(); i++) {
			pStripDataArray = RegistDataArray.get(i);

			if (pStripDataArray != null) {
				pStripDataArray.clear();
				pStripDataArray = null;
			}
		}
		RegistDataArray.clear();
		////////////////////////////////////////////////////////////////////
	}

	// ������ũ ���� ��� ���θ� Ȯ���Ѵ�.
	// @ return : boolean ������ũ ���� ��� ����
	private boolean checkMosaicDataRegistration() {
		int nPath = 0, nRow = 0;

		if (_nNumOfRow > 1 && _nNumOfPath > 1) {
			for (nRow = 0; nRow < _nNumOfRow; nRow++) {
				if (nRow == 0) // first line
				{
					if (!checkMultiMosaicData(nRow, true, false))
						return false;
				} else if (nRow == _nNumOfRow - 1) // last line
				{
					if (!checkMultiMosaicData(nRow, false, false))
						return false;
				} else // etc line
				{
					if (!checkMultiMosaicData(nRow, true, true))
						return false;
				}
			}
		} else if (_nNumOfRow == 1) {
			for (nPath = 0; nPath < _nNumOfPath; nPath++) {
				if (!checkOneMosaicData(nPath, 0))
					return false;
			}
		} else if (_nNumOfPath == 1) {
			for (nRow = 0; nRow < _nNumOfRow; nRow++) {
				if (!checkOneMosaicData(0, nRow))
					return false;
			}
		}
		return true;
	}

	// �ڵ� ���� ��� ����(����ġ)�� ����Ѵ�.
	// @ nMaxWeight : �ִ� ���� ��� ����(����ġ)
	private void getAutoLogicWeight(int nMaxWeight) {
		int nPath, nRow;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getAutoLogicWeight");
		}
//[DEBUG]			

		// int **nRange;
		int[][] nRange = create2DimInt(_nNumOfPath, _nNumOfRow);

		// double** dblMeanMode;
		double[][] dblMeanMode = create2DimDouble(_nNumOfPath, _nNumOfRow);

		// dblVariance = new double*[_nNumOfPath];
		double[][] dblVariance = create2DimDouble(_nNumOfPath, _nNumOfRow);

		// int** nRefWeight;
		int[][] nRefWeight = create2DimInt(_nNumOfPath, _nNumOfRow);

		for (nPath = 0; nPath < _nNumOfPath; nPath++) {
			for (nRow = 0; nRow < _nNumOfRow; nRow++) {
				nRange[nPath][nRow] = _pRegistData[nPath][nRow]._statData._nRange;
				dblMeanMode[nPath][nRow] = _pRegistData[nPath][nRow]._statData._dblMeanMode;
				dblVariance[nPath][nRow] = _pRegistData[nPath][nRow]._statData._dblVariance;
				nRefWeight[nPath][nRow] = 0;
			}
		}

		getStatisticsWeight(nMaxWeight, nRange, dblMeanMode, dblVariance, nRefWeight);

		// Get Number of Master
		_nNumOfMaster = _nWeight / 9;
		if (_nNumOfMaster <= 0)
			_nNumOfMaster = 1;

		// Get Auto Logic Master
		double[] dblMainVariance = { 0 };
		int[] nMainPath = { 0 };
		int[] nMainRow = { 0 };

		getFirstMaster(nRefWeight, nMainPath, nMainRow, dblMainVariance);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out
					.println("[DEBUG]\t First Master Check : Main : Path = " + nMainPath[0] + ", Row = " + nMainRow[0]);
			for (nPath = 0; nPath < _nNumOfPath; nPath++) {
				for (nRow = 0; nRow < _nNumOfRow; nRow++) {
					System.out.println("[DEBUG]\t\t File Name = " + _pRegistData[nPath][nRow]._oFileData._strFilePath);
					System.out.println("[DEBUG]\t\t LogicOrder[" + nPath + "][" + nRow + "] = "
							+ _pRegistData[nPath][nRow]._nLogicOrder);
				}
			}
		}
//[DEBUG]			

		// get similarity
		for (nPath = 0; nPath < _nNumOfPath; nPath++) {
			for (nRow = 0; nRow < _nNumOfRow; nRow++) {
				if (_pRegistData[nPath][nRow]._bCheckData) {
					_pRegistData[nPath][nRow]._dblSimilarity = calcSimilarity(dblMainVariance[0],
							_pRegistData[nPath][nRow]._statData._dblVariance);
				}
			}
		}

		getSecondMaster(nMainPath[0], nMainRow[0]);

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Second Master Check : ");
			for (nPath = 0; nPath < _nNumOfPath; nPath++) {
				for (nRow = 0; nRow < _nNumOfRow; nRow++) {
					System.out.println("[DEBUG]\t\t File Name = " + _pRegistData[nPath][nRow]._oFileData._strFilePath);
					System.out.println("[DEBUG]\t\t LogicOrder[" + nPath + "][" + nRow + "] = "
							+ _pRegistData[nPath][nRow]._nLogicOrder);
				}
			}
		}
//[DEBUG]			

		getAutoLogicSlave();

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Auto Logic Slave Check : ");
			for (nPath = 0; nPath < _nNumOfPath; nPath++) {
				for (nRow = 0; nRow < _nNumOfRow; nRow++) {
					System.out.println("[DEBUG]\t\t File Name = " + _pRegistData[nPath][nRow]._oFileData._strFilePath);
					System.out.println("[DEBUG]\t\t LogicOrder[" + nPath + "][" + nRow + "] = "
							+ _pRegistData[nPath][nRow]._nLogicOrder);
				}
			}
		}
//[DEBUG]			

		setLogicRegistTable(nRefWeight);

		delete2DimInt(nRange, _nNumOfPath);
		delete2DimDouble(dblMeanMode, _nNumOfPath);
		delete2DimDouble(dblVariance, _nNumOfPath);
		delete2DimInt(nRefWeight, _nNumOfPath);
	}

	// Similarity�� ����Ѵ�.
	// @ masterVariance : Master �л�
	// @ slaveVariance : Slave �л�
	// @
	// @ return : double ���� Similarity
	private double calcSimilarity(double masterVariance, double slaveVariance) {
		return (Math.sqrt(masterVariance) / Math.sqrt(slaveVariance));
	}

	// ������ 2���� �迭�� �����Ѵ�.
	// @ Row : �迭�� row ��
	// @ Col : �迭�� col ��
	// @
	// @ return : int[][] ������ 2���� �迭
	private int[][] create2DimInt(int Row, int Col) {
		assert (Row > 0 && Col > 0);

		int[][] pData = new int[Row][];

		for (int i = 0; i < Row; i++) {
			pData[i] = new int[Col];
		}

		return pData;
	}

	// �Ǽ��� 2���� �迭�� �����Ѵ�.
	// @ Row : �迭�� row ��
	// @ Col : �迭�� col ��
	// @
	// @ return : double[][] ������ 2���� �迭
	private double[][] create2DimDouble(int Row, int Col) {
		assert (Row > 0 && Col > 0);

		double[][] pData = new double[Row][];

		for (int i = 0; i < Row; i++) {
			pData[i] = new double[Col];
		}

		return pData;
	}

	// ������ 2���� �迭�� �����Ѵ�.
	// @ pData : ������ 2���� �迭
	// @ Row : �迭�� row ��
	private void delete2DimInt(int[][] pData, int Row) {
		assert (pData != null && Row > 0);

		for (int row = 0; row < Row; row++) {
			pData[row] = null;
		}

		pData = null;
	}

	// �Ǽ��� 2���� �迭�� �����Ѵ�.
	// @ pData : �Ǽ��� 2���� �迭
	// @ Row : �迭�� row ��
	private void delete2DimDouble(double[][] pData, int Row) {
		assert (pData != null && Row > 0);

		for (int row = 0; row < Row; row++) {
			pData[row] = null;
		}

		pData = null;
	}

	// ��� ���̺��� ������ũ ��� ������ �����Ѵ�.
	// @ pIndata : ���� ���� Ŭ����
	// @ nPath : Path �ε���
	// @ nRow : Row �ε���
	// @
	// @ return : GMosaicResultData outdata ������ ������ũ ��� ����
	private void setRegistTable(GOpenFileData pIndata, int nPath, int nRow, GMosaicResultData outdata) {
		outdata._nScenePath = nPath;
		outdata._nSceneRow = nRow;
		outdata._oFileData.Copy(pIndata._oFileData);
//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.setRegistTable :");
			System.out.println("[DEBUG]\t nPath :" + nPath + ", Row : " + nRow);
			System.out.println("[DEBUG]\t [pIndata] File Name :" + pIndata._oFileData._strFilePath);
			System.out.println("[DEBUG]\t [pIndata] Is Referenced :" + pIndata._oFileData._isReferenced);
			System.out.println("[DEBUG]\t [outdata] File Name :" + outdata._oFileData._strFilePath);
			System.out.println("[DEBUG]\t [outdata] Is Referenced :" + outdata._oFileData._isReferenced);
		}
//[DEBUG]				

		outdata._nDeviceIndex = 0;
		outdata._imgData._dblPixelScales[0] = pIndata._dblPixelScales[0];
		outdata._imgData._dblPixelScales[1] = pIndata._dblPixelScales[1];
		outdata._imgData._lBandNum = pIndata._lBandNum;

		// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
		outdata._imgData._mbr2d.setCoordinateReferenceSystem(pIndata._mbr2d.getCoordinateReferenceSystem());
		outdata._imgData._mbr2d.setRect(pIndata._mbr2d.getBounds2D());

		outdata._nRefWeight = 0;
		outdata._nLogicOrder = -1;

		outdata._bCheckData = true;
		outdata._bUseChecking = false;
		outdata._bCheckMaster = false;
	}

	// �ش� Row�� ���� ���� ������ũ ���� ���θ� ��ȯ�Ѵ�.
	// @ nRow : Row �ε���
	// @ bDifference : Difference ����
	// @ bEtc : Etc ����
	// @
	// @ return : boolean ���� ������ũ ���� ����
	private boolean checkMultiMosaicData(int nRow, boolean bDifference, boolean bEtc) {
		int nPath = 0; // Initialize...
		int nDifference = 0;

		if (bDifference)
			nDifference = 1;
		else
			nDifference = -1;

		try {

			if (!bEtc) {
				for (nPath = 0; nPath < _nNumOfPath; nPath++) {
					if (_pRegistData[nPath][nRow]._bCheckData) {
						if (nPath == 0) // first column
						{
							if (!_pRegistData[nPath + 1][nRow]._bCheckData
									&& !_pRegistData[nPath][nRow + nDifference]._bCheckData) {
								_bProcess = false;
								return false;
							}
						} else if (nPath == _nNumOfPath - 1) // last column
						{
							if (!_pRegistData[nPath - 1][nRow]._bCheckData
									&& !_pRegistData[nPath][nRow + nDifference]._bCheckData) {
								_bProcess = false;
								return false;
							}
						} else { // etc column
							if (!_pRegistData[nPath - 1][nRow]._bCheckData && !_pRegistData[nPath + 1][nRow]._bCheckData
									&& !_pRegistData[nPath][nRow + nDifference]._bCheckData) {
								_bProcess = false;
								return false;
							}
						}
					}
				}
			} else {
				for (nPath = 0; nPath < _nNumOfPath; nPath++) {
					if (_pRegistData[nPath][nRow]._bCheckData) {
						if (nPath == 0) // first column
						{
							if (!_pRegistData[nPath + 1][nRow]._bCheckData && !_pRegistData[nPath][nRow - 1]._bCheckData
									&& !_pRegistData[nPath][nRow + 1]._bCheckData) {
								_bProcess = false;
								return false;
							}
						} else if (nPath == _nNumOfPath - 1) // last column
						{
							if (!_pRegistData[nPath - 1][nRow]._bCheckData
									&& !_pRegistData[nPath][nRow + nDifference]._bCheckData) {
								_bProcess = false;
								return false;
							}
						} else { // etc column
							if (!_pRegistData[nPath - 1][nRow]._bCheckData && !_pRegistData[nPath + 1][nRow]._bCheckData
									&& !_pRegistData[nPath][nRow + nDifference]._bCheckData) {
								_bProcess = false;
								return false;
							}
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GMasterRegistration.checkMultiMosaicData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.checkMultiMosaicData : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

		return true;
	}

	// �ش� Path, Row�� ���� ������ũ ���� ������ ��ȯ�Ѵ�.
	// @ nPath : Path �ε���
	// @ nRow : Row �ε���
	// @
	// @ return : boolean ������ũ ���� ����
	private boolean checkOneMosaicData(int nPath, int nRow) {
		int nPathDifference = 0, nRowDifference = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.checkOneMosaicData");
			System.out.println("[DEBUG]\t ##################################");
			System.out
					.println("[DEBUG]\t nPath = " + (nPath + nPathDifference) + ", nRow = " + (nRow + nRowDifference));
			System.out.println("[DEBUG]\t ##################################");
		}
//[DEBUG]	

		try {

			if (_pRegistData[nPath][nRow]._bCheckData) {
				if (nPath == 0) // first column
				{
					if (!_pRegistData[nPath + nPathDifference][nRow + nRowDifference]._bCheckData) {
						_bProcess = false;
						return false;
					}
				} else if (nPath == _nNumOfPath - 1) // last column
				{
					if (!_pRegistData[nPath - nPathDifference][nRow - nRowDifference]._bCheckData) {
						_bProcess = false;
						return false;
					}
				} else { // etc column
					if (!_pRegistData[nPath - nPathDifference][nRow - nRowDifference]._bCheckData
							&& !_pRegistData[nPath + nPathDifference][nRow + nRowDifference]._bCheckData) {
						_bProcess = false;
						return false;
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out
					.println("GMasterRegistration.checkOneMosaicData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.checkOneMosaicData : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

		return true;
	}

	// ȭ�� ��������� �̿��Ͽ� ���� ��� ����(����ġ)�� ����Ѵ�.
	// @ nMaxWeight : �ִ� ���� ��� ����(����ġ)
	// @
	// @ return : int[][] nRange Path, Row �迭 ����
	// @ return : double[][] dwMeanMode ��� �迭 ����
	// @ return : double[][] dwVariance �л� �迭 ����
	// @ return : int[][] nRefWeight ���� ����ġ �迭 ����
	private void getStatisticsWeight(int nMaxWeight, int[][] nRange, double[][] dwMeanMode, double[][] dwVariance,
			int[][] nRefWeight) {
		// Get Statistics Weight
		int nWeight = nMaxWeight;
		int nPath = 0, nRow = 0;
		int nMaxR = 0, nRPath = -1, nRRow = -1, nMMPath = -1, nMMRow = -1;
		int nVPath = -1, nVRow = -1;
		double nMinMM = 256, nMaxV = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getStatisticsWeight : Start ");
		}
//[DEBUG]	

		try {

			while (nWeight > 0) {
				nMaxR = 0;
				nRPath = -1;
				nRRow = -1;
				nMinMM = 256;
				nMMPath = -1;
				nMMRow = -1;
				nMaxV = 0;
				nVPath = -1;
				nVRow = -1;

				for (nPath = 0; nPath < _nNumOfPath; nPath++) {
					for (nRow = 0; nRow < _nNumOfRow; nRow++) {
						if (_pRegistData[nPath][nRow]._bCheckData && nRange[nPath][nRow] > nMaxR) {
							nMaxR = nRange[nPath][nRow];
							nRPath = nPath;
							nRRow = nRow;
						}

						if (_pRegistData[nPath][nRow]._bCheckData && dwMeanMode[nPath][nRow] < nMinMM) {
							nMinMM = dwMeanMode[nPath][nRow];
							nMMPath = nPath;
							nMMRow = nRow;
						}

						if (_pRegistData[nPath][nRow]._bCheckData && dwVariance[nPath][nRow] > nMaxV) {
							nMaxV = dwVariance[nPath][nRow];
							nVPath = nPath;
							nVRow = nRow;
						}
					} // end row
				} // end path

				if (nRPath >= 0 && nRRow >= 0)
					nRefWeight[nRPath][nRRow] += nWeight; // get range weight
				if (nMMPath >= 0 && nMMRow >= 0)
					nRefWeight[nMMPath][nMMRow] += nWeight; // get mean-mode weight
				if (nVPath >= 0 && nVRow >= 0)
					nRefWeight[nVPath][nVRow] += nWeight; // get variance weight

				if (nRPath >= 0 && nRRow >= 0)
					nRange[nRPath][nRRow] = 0;
				if (nMMPath >= 0 && nMMRow >= 0)
					dwMeanMode[nMMPath][nMMRow] = 256;
				if (nVPath >= 0 && nVRow >= 0)
					dwVariance[nVPath][nVRow] = 0;

				nWeight--;
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GMasterRegistration.getStatisticsWeight : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.getStatisticsWeight : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getStatisticsWeight : End ");
		}
//[DEBUG]
	}

	// �ش� Path, Row�� ���� ù��° Master�� ����Ͽ� �л��� ��ȯ�Ѵ�.
	// @ nRefWeight : ���� ����ġ �迭 ����
	// @
	// @ return : int[1] nMainPath Path �ε���
	// @ return : int[1] nMainRow Row �ε���
	// @ return : double[1] dblMainVariance �л�
	private void getFirstMaster(int[][] nRefWeight, int[] nMainPath, int[] nMainRow, double[] dblMainVariance) {
		int nPath = 0, nRow = 0;
		int nMax = 0, nMaxPath = -1, nMaxRow = -1;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getFirstMaster : Start ");
		}
//[DEBUG]

		try {
			nMaxPath = -1;
			nMaxRow = -1;

			// get first master
			if (_nNumOfMaster >= 1) {
				if (_nNumOfPath <= 3 && _nNumOfRow <= 3) {
					for (nPath = 0; nPath < _nNumOfPath; nPath++) {
						for (nRow = 0; nRow < _nNumOfRow; nRow++) {
							if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
									&& nRefWeight[nPath][nRow] > nMax) {
								nMax = nRefWeight[nPath][nRow];
								nMaxPath = nPath;
								nMaxRow = nRow;
							}
						} // end FOR(row)
					} // end FOR(path)
				} else if (_nNumOfPath > 3 && _nNumOfRow <= 3) {
					for (nPath = 1; nPath < _nNumOfPath - 1; nPath++) {
						for (nRow = 0; nRow < _nNumOfRow; nRow++) {
							if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
									&& nRefWeight[nPath][nRow] > nMax) {
								nMax = nRefWeight[nPath][nRow];
								nMaxPath = nPath;
								nMaxRow = nRow;
							}
						} // end FOR(row)
					} // end FOR(path)
				} else if (_nNumOfPath <= 3 && _nNumOfRow > 3) {
					for (nPath = 0; nPath < _nNumOfPath; nPath++) {
						for (nRow = 1; nRow < _nNumOfRow - 1; nRow++) {
							if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
									&& nRefWeight[nPath][nRow] > nMax) {
								nMax = nRefWeight[nPath][nRow];
								nMaxPath = nPath;
								nMaxRow = nRow;
							}
						} // end FOR(row)
					} // end FOR(path)
				} else {
					for (nPath = 1; nPath < _nNumOfPath - 1; nPath++) {
						for (nRow = 1; nRow < _nNumOfRow - 1; nRow++) {
							if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
									&& nRefWeight[nPath][nRow] > nMax) {
								nMax = nRefWeight[nPath][nRow];
								nMaxPath = nPath;
								nMaxRow = nRow;
							}
						} // end FOR(row)
					} // end FOR(path)
				}

				// ---------------------------------------------------------------------//
				// Exist the Reference Image
				// ---------------------------------------------------------------------//
				for (nPath = 0; nPath < _nNumOfPath; nPath++) {
					for (nRow = 0; nRow < _nNumOfRow; nRow++) {
						if (!_pRegistData[nPath][nRow]._oFileData._isReferenced)
							continue;

						if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster) {
							nMaxPath = nPath;
							nMaxRow = nRow;
							break;
						}
					} // end FOR(row)
				} // end FOR(path)
					// ---------------------------------------------------------------------//

				// set master logic order
				if (nMaxPath >= 0 && nMaxRow >= 0) {
					_pRegistData[nMaxPath][nMaxRow]._nLogicOrder = 1;
					_pRegistData[nMaxPath][nMaxRow]._bCheckMaster = true;
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t ##################################");
						System.out.println("[DEBUG]\t nMaxPath = " + nMaxPath + ", nMaxRow = " + nMaxRow);
						System.out.println("[DEBUG]\t _pRegistData[" + nMaxPath + "][" + nMaxRow + "]._nLogicOrder = "
								+ _pRegistData[nMaxPath][nMaxRow]._nLogicOrder);
						System.out.println("[DEBUG]\t ##################################");
					}
//[DEBUG]

					dblMainVariance[0] = _pRegistData[nMaxPath][nMaxRow]._statData._dblVariance;
					nMainPath[0] = nMaxPath;
					nMainRow[0] = nMaxRow;
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GMasterRegistration.getFirstMaster : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.getFirstMaster : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getFirstMaster : End ");
		}
//[DEBUG]
	}

	// �ش� Path, Row�� ���� �ι�° Master�� ����Ѵ�.
	// @ nMainPath : Path �ε���
	// @ nMainRow : Row �ε���
	private void getSecondMaster(int nMainPath, int nMainRow) {
		int nPath = 0, nRow = 0;

		// get second master
		int nMasterNum = 2;
		double nMin = 10000;
		int nMinPath = -1, nMinRow = -1;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getSecondMaster : Start ");
		}
//[DEBUG]

		try {

			while (nMasterNum <= _nNumOfMaster) {
				nMinPath = -1;
				nMinRow = -1;

				if (_nNumOfPath >= 6 && _nNumOfRow <= 3) {
					for (nPath = 1; nPath < _nNumOfPath - 1; nPath++) {
						for (nRow = 0; nRow < _nNumOfRow; nRow++) {
							if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
									&& Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0) <= nMin) {
								if (Math.abs(nMainPath - nPath) >= 3) {
									nMin = Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0);
									nMinPath = nPath;
									nMinRow = nRow;
								}
							}
						} // end FOR(row)
					} // end FOR(path)

					// set master logic order
					if (nMinPath >= 0 && nMinRow >= 0) {
						_pRegistData[nMinPath][nMinRow]._nLogicOrder = nMasterNum;
						_pRegistData[nMinPath][nMinRow]._bCheckMaster = true;
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t ##################################");
							System.out.println("[DEBUG]\t nMinPath = " + nMinPath + ", nMinRow = " + nMinRow);
							System.out.println("[DEBUG]\t _pRegistData[" + nMinPath + "][" + nMinRow
									+ "]._nLogicOrder = " + _pRegistData[nMinPath][nMinRow]._nLogicOrder);
							System.out.println("[DEBUG]\t ##################################");
						}
//[DEBUG]
					}
				} else if (_nNumOfPath <= 3 && _nNumOfRow >= 6) {
					for (nPath = 0; nPath < _nNumOfPath; nPath++) {
						for (nRow = 1; nRow < _nNumOfRow - 1; nRow++) {
							if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
									&& Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0) <= nMin) {
								if (Math.abs(nMainRow - nRow) >= 3) {
									nMin = Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0);
									nMinPath = nPath;
									nMinRow = nRow;
								}
							}
						} // end FOR(row)
					} // end FOR(path)

					// set master logic order
					if (nMinPath >= 0 && nMinRow >= 0) {
						_pRegistData[nMinPath][nMinRow]._nLogicOrder = nMasterNum;
						_pRegistData[nMinPath][nMinRow]._bCheckMaster = true;
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t ##################################");
							System.out.println("[DEBUG]\t nMinPath = " + nMinPath + ", nMinRow = " + nMinRow);
							System.out.println("[DEBUG]\t _pRegistData[" + nMinPath + "][" + nMinRow
									+ "]._nLogicOrder = " + _pRegistData[nMinPath][nMinRow]._nLogicOrder);
							System.out.println("[DEBUG]\t ##################################");
						}
//[DEBUG]
					}
				} else {
					for (nPath = 1; nPath < _nNumOfPath - 1; nPath++) {
						for (nRow = 1; nRow < _nNumOfRow - 1; nRow++) {
							if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
									&& Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0) <= nMin) {
								if (Math.sqrt(Math.pow(Math.abs(nMainPath - nPath), 2)
										+ Math.pow(Math.abs(nMainRow - nRow), 2)) >= 3) {
									nMin = Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0);
									nMinPath = nPath;
									nMinRow = nRow;
								}
							}
						} // end FOR(row)
					} // end FOR(path)

					// set master logic order
					if (nMinPath >= 0 && nMinRow >= 0) {
						_pRegistData[nMinPath][nMinRow]._nLogicOrder = nMasterNum;
						_pRegistData[nMinPath][nMinRow]._bCheckMaster = true;
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t ##################################");
							System.out.println("[DEBUG]\t nMinPath = " + nMinPath + ", nMinRow = " + nMinRow);
							System.out.println("[DEBUG]\t _pRegistData[" + nMinPath + "][" + nMinRow
									+ "]._nLogicOrder = " + _pRegistData[nMinPath][nMinRow]._nLogicOrder);
							System.out.println("[DEBUG]\t ##################################");
						}
//[DEBUG]
					}
				}

				nMasterNum++; // increase master_num
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GMasterRegistration.getSecondMaster : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.getSecondMaster : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getSecondMaster : End ");
		}
//[DEBUG]
	}

	// �ڵ� Logic Slave�� ����Ѵ�.
	private void getAutoLogicSlave() {
		int nPath, nRow;

		// Get Auto Logic Slave
		int slave_num = _nNumOfMaster + 1;
		double dwMin = 10000;
		int nMinPath = -1, nMinRow = -1;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getAutoLogicSlave : Start ");
		}
//[DEBUG]

		try {

			while (slave_num <= _nWeight) {
				dwMin = 10000;
				nMinPath = -1;
				nMinRow = -1;

				for (nPath = 0; nPath < _nNumOfPath; nPath++) {
					for (nRow = 0; nRow < _nNumOfRow; nRow++) {
						if (_pRegistData[nPath][nRow]._bCheckData && !_pRegistData[nPath][nRow]._bCheckMaster
								&& !_pRegistData[nPath][nRow]._bUseChecking
								&& Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0) <= dwMin) {
							dwMin = Math.abs(_pRegistData[nPath][nRow]._dblSimilarity - 1.0);
							nMinPath = nPath;
							nMinRow = nRow;
						}
					} // end FOR(row)
				} // end FOR(path)

				// set slave logic order
				if (nMinPath >= 0 && nMinRow >= 0) {
					_pRegistData[nMinPath][nMinRow]._nLogicOrder = slave_num;
					_pRegistData[nMinPath][nMinRow]._bUseChecking = true;
					slave_num++;
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t ##################################");
						System.out.println("[DEBUG]\t nMinPath = " + nMinPath + ", nMinRow = " + nMinRow);
						System.out.println("[DEBUG]\t _pRegistData[" + nMinPath + "][" + nMinRow + "]._nLogicOrder = "
								+ _pRegistData[nMinPath][nMinRow]._nLogicOrder);
						System.out.println("[DEBUG]\t ##################################");
					}
//[DEBUG]
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out
					.println("GMasterRegistration.getAutoLogicSlave : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.getAutoLogicSlave : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GMasterRegistration.getAutoLogicSlave : End ");
		}
//[DEBUG]

	}

	// Logic Regist Table�� �����Ѵ�.
	// @ nRefWeight : ���� ����ġ �迭 ����
	private void setLogicRegistTable(int[][] nRefWeight) {
		int nPath = 0, nRow = 0;

		try {

			// Set Regist Table
			for (nPath = 0; nPath < _nNumOfPath; nPath++) {
				for (nRow = 0; nRow < _nNumOfRow; nRow++) {
					if (_pRegistData[nPath][nRow]._bCheckData)
						_pRegistData[nPath][nRow]._nRefWeight = nRefWeight[nPath][nRow];
					else
						_pRegistData[nPath][nRow]._nRefWeight = 0;

					_pRegistData[nPath][nRow]._bUseChecking = false;

					if (_pRegistData[nPath][nRow]._nLogicOrder == 1) {
						_nMainMasterPath = nPath;
						_nMainMasterRow = nRow;
					}

				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GMasterRegistration.setLogicRegistTable : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.setLogicRegistTable : (Exception) " + ex.toString());
			ex.printStackTrace();
		}
	}

	// ���� ���� ����� �����Ѵ�.
	// @ return : ArrayList<GOpenFileData> pSortDataArray ��ȯ ���ĵ� ���� ���� ���
	private void setDataSort(ArrayList<GOpenFileData> pSortDataArray) {

		try {

			////////////////////////////////////////////////////////////////////
			// Sorting!!(dblUtmCenterY Ascending)
			////////////////////////////////////////////////////////////////////
			for (int i = 0; i < pSortDataArray.size() - 1; i++) {
				GOpenFileData pAddFileData = null;
				GOpenFileData pPreAddFileData = null;
				GOpenFileData pTmpFileData = null;

				pAddFileData = pSortDataArray.get(i);
				for (int j = i + 1; j < pSortDataArray.size(); j++) {
					pPreAddFileData = pSortDataArray.get(j);
					if (pAddFileData._dblUtmCenter.y < pPreAddFileData._dblUtmCenter.y) {
						pTmpFileData = pPreAddFileData;
						pPreAddFileData = pAddFileData;
						pAddFileData = pTmpFileData;
						pSortDataArray.set(i, pAddFileData);
						pSortDataArray.set(j, pPreAddFileData);
					}
				}
			}
			////////////////////////////////////////////////////////////////////

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GMasterRegistration.calcImgStatisticsData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.calcImgStatisticsData : (Exception) " + ex.toString());
			ex.printStackTrace();
		}
	}

	// Strip ���� �迭 ����� �����Ѵ�.
	// @ pSortDataArray : ���� ���� ���
	// @
	// @ return : ArrayList<ArrayList<GOpenFileData>> pRegistDataArray Strip ���� �迭
	// ���
	private void setStripDataArray(ArrayList<GOpenFileData> pSortDataArray,
			ArrayList<ArrayList<GOpenFileData>> pRegistDataArray) {
		ArrayList<GOpenFileData> pStripDataArray = null;
		GOpenFileData pAddFileData = null;
		GOpenFileData pPreAddFileData = null;
		GOpenFileData pTmpFileData = null;

		pStripDataArray = new ArrayList<GOpenFileData>();

		try {

			// Base Master!!(for loop ���� ������ �̸� �־��ش�.)
			////////////////////////////////////////////////////////
			if (pSortDataArray.size() > 0) {
				pAddFileData = pSortDataArray.get(0);
				pStripDataArray.add(pAddFileData);
			}
			////////////////////////////////////////////////////////

			for (int i = 0; i < pSortDataArray.size() - 1; i++) {
				pAddFileData = pSortDataArray.get(i);
				pPreAddFileData = pSortDataArray.get(i + 1);

				if (pAddFileData._mbr2d.getMaxY()
						- Math.abs(pAddFileData._mbr2d.getMaxY() - pAddFileData._mbr2d.getMinY())
								* 0.3 > pPreAddFileData._dblUtmCenter.y
						&& pAddFileData._mbr2d.getMinY()
								+ Math.abs(pAddFileData._mbr2d.getMaxY() - pAddFileData._mbr2d.getMinY())
										* 0.3 < pPreAddFileData._dblUtmCenter.y) {
					pStripDataArray.add(pPreAddFileData);
				} else {
					////////////////////////////////////////////////////////////////////
					// Sorting!!(dblUtmCenterX Descending)
					////////////////////////////////////////////////////////////////////
					for (int j = 0; j < pStripDataArray.size() - 1; j++) {
						pAddFileData = pStripDataArray.get(j);
						for (int k = j + 1; k < pStripDataArray.size(); k++) {
							pPreAddFileData = pStripDataArray.get(k);
							if (pAddFileData._dblUtmCenter.x > pPreAddFileData._dblUtmCenter.x) {
								pTmpFileData = pPreAddFileData;
								pPreAddFileData = pAddFileData;
								pAddFileData = pTmpFileData;
								pStripDataArray.set(j, pAddFileData);
								pStripDataArray.set(k, pPreAddFileData);
							}
						}
					}
					////////////////////////////////////////////////////////////////////
					pRegistDataArray.add(pStripDataArray);

					pStripDataArray = new ArrayList<GOpenFileData>();
					pPreAddFileData = pSortDataArray.get(i + 1);
					pStripDataArray.add(pPreAddFileData);
				}
			}

			////////////////////////////////////////////////////////////////////
			// Sorting!!(dblUtmCenterX Descending)
			////////////////////////////////////////////////////////////////////
			for (int j = 0; j < pStripDataArray.size() - 1; j++) {
				pAddFileData = pStripDataArray.get(j);
				for (int k = j + 1; k < pStripDataArray.size(); k++) {
					pPreAddFileData = pStripDataArray.get(k);
					if (pAddFileData._dblUtmCenter.x > pPreAddFileData._dblUtmCenter.x) {
						pTmpFileData = pPreAddFileData;
						pPreAddFileData = pAddFileData;
						pAddFileData = pTmpFileData;
						pStripDataArray.set(j, pAddFileData);
						pStripDataArray.set(k, pPreAddFileData);
					}
				}
			}
			////////////////////////////////////////////////////////////////////
			pRegistDataArray.add(pStripDataArray);
		} catch (IndexOutOfBoundsException iobe) {
			System.out
					.println("GMasterRegistration.setStripDataArray : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.setStripDataArray : (Exception) " + ex.toString());
			ex.printStackTrace();
		}
	}

	// Path ������ ����Ѵ�.
	// @ pRegistDataArray : Strip ���� �迭 ���
	// @
	// @ return : int Path ����
	private int calcNumOfPath(ArrayList<ArrayList<GOpenFileData>> pRegistDataArray) {
		int nNumOfPath = 0, nStripSize = 0, nLengthMin = 0;
		double dwUtmMinX = 0, dwUtmMaxX = 0, dwUtmCenterLength = 0, dwUtmCenterLengthMin = 0;
		ArrayList<GOpenFileData> pStripDataArray = null;
		ArrayList<GOpenFileData> pPreStripDataArray = null;
		GOpenFileData pAddFileData = null;
		GOpenFileData pTmpStartFileData = null;
		GOpenFileData pTmpEndFileData = null;
		int i = 0;

		try {

			for (i = 0; i < pRegistDataArray.size(); i++) {
				pStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(i);
				pAddFileData = pStripDataArray.get(0);
				if (i == 0) {
					dwUtmMinX = pAddFileData._mbr2d.getMinX();
					dwUtmMaxX = pAddFileData._mbr2d.getMinX();
				}
				if (pAddFileData._mbr2d.getMinX() < dwUtmMinX) {
					dwUtmMinX = pAddFileData._mbr2d.getMinX();
					_nIndexMinX = i;
				}
				if (pAddFileData._mbr2d.getMinX() > dwUtmMaxX) {
					dwUtmMaxX = pAddFileData._mbr2d.getMinX();
					_nIndexMaxX = i;
				}
			}

			pPreStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(_nIndexMaxX);
			pTmpStartFileData = pPreStripDataArray.get(0);
			nStripSize = pPreStripDataArray.size();
			pTmpEndFileData = pPreStripDataArray.get(nStripSize - 1);
			pStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(_nIndexMinX);
			nStripSize = pStripDataArray.size();
			pAddFileData = pStripDataArray.get(nStripSize - 1);

			if (pAddFileData._mbr2d.getMaxX() > pTmpEndFileData._mbr2d.getMaxX()) {
				nNumOfPath = pStripDataArray.size();
			} else if (pAddFileData._mbr2d.getMaxX() > pTmpStartFileData._mbr2d.getMinX()) {
				for (i = 0; i < nStripSize; i++) {
					pAddFileData = pStripDataArray.get(i);

					dwUtmCenterLength = Math
							.sqrt(Math.pow((pTmpStartFileData._dblUtmCenter.x - pAddFileData._dblUtmCenter.x), 2)
									+ Math.pow((pTmpStartFileData._dblUtmCenter.y - pAddFileData._dblUtmCenter.y), 2));

					if (i == 0)
						dwUtmCenterLengthMin = dwUtmCenterLength;

					if (dwUtmCenterLengthMin > dwUtmCenterLength) {
						dwUtmCenterLengthMin = dwUtmCenterLength;
						nLengthMin = i;
					}
				}
				if (nLengthMin == 0)
					nNumOfPath = Math.max(pStripDataArray.size(), pPreStripDataArray.size());
				else
					nNumOfPath = nLengthMin + pPreStripDataArray.size();
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GMasterRegistration.calcNumOfPath : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.calcNumOfPath : (Exception) " + ex.toString());
			ex.printStackTrace();
		}

		return nNumOfPath;
	}

	// Logic Table�� �����Ѵ�.
	// @ pRegistDataArray : Strip ���� �迭 ���
	private void setLogicTable(ArrayList<ArrayList<GOpenFileData>> pRegistDataArray) {
		// Set Logic Table
		int nPath = 0, nRow = 0, i = 0, j = 0, nLengthMin = 0;
		double dwUtmCenterLengthMin = 0, dwUtmCenterLength = 0;
		ArrayList<GOpenFileData> pStripDataArray = null;
		ArrayList<GOpenFileData> pPreStripDataArray = null;
		GOpenFileData pAddFileData = null;
		GOpenFileData pTmpFileData = null;

		try {

			_pRegistData = new GMosaicResultData[_nNumOfPath][];
			for (nPath = 0; nPath < _nNumOfPath; nPath++) {
				_pRegistData[nPath] = new GMosaicResultData[_nNumOfRow];
				for (nRow = 0; nRow < _nNumOfRow; nRow++) {
					_pRegistData[nPath][nRow] = new GMosaicResultData();
				}

			}

			for (nPath = 0; nPath < _nNumOfPath; nPath++) {
				for (nRow = 0; nRow < _nNumOfRow; nRow++) {
					_pRegistData[nPath][nRow]._nScenePath = -1;
					_pRegistData[nPath][nRow]._nSceneRow = -1;
					_pRegistData[nPath][nRow]._bCheckData = false;
					_pRegistData[nPath][nRow]._bUseChecking = false;
					_pRegistData[nPath][nRow]._bCheckMaster = false;
				}
			}

			pStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(_nIndexMinX);

			for (nPath = 0; nPath < pStripDataArray.size(); nPath++) {
				pAddFileData = pStripDataArray.get(nPath);
				setRegistTable(pAddFileData, nPath, _nIndexMinX, _pRegistData[nPath][_nIndexMinX]);
			}

			for (i = _nIndexMinX - 1; i >= 0; i--) {
				pStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(i);
				pAddFileData = pStripDataArray.get(0);
				pPreStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(_nIndexMinX);
				pTmpFileData = pStripDataArray.get(pStripDataArray.size() - 1);
				if (pTmpFileData._mbr2d.getMaxX() > pAddFileData._mbr2d.getMinX()) {
					for (j = 0; j < pPreStripDataArray.size(); j++) {
						pTmpFileData = pPreStripDataArray.get(j);

						dwUtmCenterLength = Math
								.sqrt(Math.pow((pTmpFileData._dblUtmCenter.x - pAddFileData._dblUtmCenter.x), 2)
										+ Math.pow((pTmpFileData._dblUtmCenter.y - pAddFileData._dblUtmCenter.y), 2));

						if (j == 0)
							dwUtmCenterLengthMin = dwUtmCenterLength;

						if (dwUtmCenterLengthMin > dwUtmCenterLength) {
							dwUtmCenterLengthMin = dwUtmCenterLength;
							nLengthMin = j;
						}
					}
				}

				for (nPath = 0; nPath < pStripDataArray.size(); nPath++) {
					pAddFileData = pStripDataArray.get(nPath);
					setRegistTable(pAddFileData, nLengthMin + nPath, i, _pRegistData[nLengthMin + nPath][i]);
				}
			}

			for (i = _nIndexMinX + 1; i < pRegistDataArray.size(); i++) {
				pStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(i);
				pAddFileData = pStripDataArray.get(0);
				pPreStripDataArray = (ArrayList<GOpenFileData>) pRegistDataArray.get(_nIndexMinX);
				pTmpFileData = pStripDataArray.get(pStripDataArray.size() - 1);
				if (pTmpFileData._mbr2d.getMaxX() > pAddFileData._mbr2d.getMinX()) {
					for (j = 0; j < pPreStripDataArray.size(); j++) {
						pTmpFileData = pPreStripDataArray.get(j);

						dwUtmCenterLength = Math
								.sqrt(Math.pow((pTmpFileData._dblUtmCenter.x - pAddFileData._dblUtmCenter.x), 2)
										+ Math.pow((pTmpFileData._dblUtmCenter.y - pAddFileData._dblUtmCenter.y), 2));

						if (j == 0)
							dwUtmCenterLengthMin = dwUtmCenterLength;

						if (dwUtmCenterLengthMin > dwUtmCenterLength) {
							dwUtmCenterLengthMin = dwUtmCenterLength;
							nLengthMin = j;
						}
					}
				}

				for (nPath = 0; nPath < pStripDataArray.size(); nPath++) {
					pAddFileData = pStripDataArray.get(nPath);
					setRegistTable(pAddFileData, nLengthMin + nPath, i, _pRegistData[nLengthMin + nPath][i]);
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GMasterRegistration.setLogicTable : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		} catch (Exception ex) {
			System.out.println("GMasterRegistration.setLogicTable : (Exception) " + ex.toString());
			ex.printStackTrace();
		}
	}

}
