package ugis.cmmn.imgproc.mosaic;

import java.awt.geom.Rectangle2D;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.Envelope2D;
import org.locationtech.jts.geom.Coordinate;

import ugis.cmmn.imgproc.GImageEnhancement;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GImageProcessor.ProcessCode;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.data.GMosaicAlgorithmData;
import ugis.cmmn.imgproc.data.GMosaicData;
import ugis.cmmn.imgproc.data.GMosaicHistogramData;
import ugis.cmmn.imgproc.data.GMosaicProcData;
import ugis.cmmn.imgproc.data.GMosaicResultData;

//�ڵ� ������ũ Ŭ����
public class GAutoMosaic {

	// ������ũ ���μ��� ����
	public enum MosaicProcMethod {
		VERTICAL("Vertical"), // 0 : Vertical(Row) - Default
		Horizontal("Horizontal"); // 1 : Horizontal(Path)

		private String name;

		MosaicProcMethod(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// ���μ��� ���⼺
	public enum ProcessDirection {
		PROCESS_CENTER("Process Center"), // 100 : Center - Default
		PROCESS_LEFTRIGHT("Process Left & Right"), // 101 : Left & Right
		PROCESS_TOPBOTTOM("Process Top & Bottom"), // 102 : Top & Bottom
		PROCESS_TAB("Process Tab"); // 103 : Tap

		private String name;

		ProcessDirection(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// Path ���⼺ (Center Type)
	public enum PathDirection {
		PATH_CENTER("Path Center"), // 0 : Center - Default
		PATH_UP("Path Up"), // 1 : Up
		PATH_DOWN("Path Down"), // 2 : Down
		PATH_NONE("Path None"); // 3 : NOne

		private String name;

		PathDirection(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public enum AreaImageType {
		AREA_NONE("Area None", 0), // 0 : None - Default
		AREA_MASTER("Area Master", 1), // 1 : Master Image
		AREA_SLAVE("Area Slave", 2); // 2 : Slave Image

		private String name;
		private int type;

		AreaImageType(String name, int type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public int getType() {
			return type;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setType(int type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Output

	// ������ũ ��� ����
	private GMosaicResultData _mosaicResultData = null;

	///////////////////////////////////////////////////////////////////////////
	// Input

	// ������ũ ����
	private GMosaicData _pMosaicData = null;

	// Path, Row�� ������ũ ��� ���� ���
	private GMosaicResultData[][] _pMosaicRegistData = null;

	// ������ũ �˰��� ����
	private GMosaicAlgorithmData _pMosaicAlgorithmData = null;

	// Master ���� ����(����ġ)
	private int _nLogicCount = 0;

	// ������ũ ��� �ε���
	private int _nBandIndex = 0;

	/////////////////////////////////////////////////////////////////////////
	// ���� ����

	// ������ũ �ػ�
	private double[] _dblPixelScales = { 0, 0 };

	// Sub ���� ���� ���
	private GSubArea[] _subArea = new GSubArea[9]; // consist of 9 zone

	// ���� ��ǥ ���� ���
	private Coordinate[] _point = new Coordinate[16];

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ������
	public GAutoMosaic() {
		int i = 0;

		// Sub ���� ���� ���
		for (i = 0; i < 9; i++) {
			_subArea[i] = new GSubArea();
		}

		// ���� ��ǥ ���� ���
		_point = new Coordinate[16];
		for (i = 0; i < 16; i++) {
			_point[i] = new Coordinate();
		}

		_mosaicResultData = new GMosaicResultData();
	}

	// ������ũ ��� ������ ��ȯ�ϴ�.
	// @ return : GMosaicResultData ������ũ ��� ����
	public GMosaicResultData getMosaicResultData() {
		return _mosaicResultData;
	}

	// Master ���� ����(����ġ)�� �����Ѵ�.
	// @ nCount : Master ���� ����(����ġ)
	public void setLogicCount(int nCount) {
		_nLogicCount = nCount;
	}

	// ������ũ ������ �����Ѵ�.
	// @ pMosaicData : ������ũ ����
	// @ pMosaicRegistData : Path, Row�� ������ũ ��� ���� ���
	// @ pMosaicAlgorithmData : ������ũ �˰��� ����
	public void setMosaicData(GMosaicData pMosaicData, GMosaicResultData[][] pMosaicRegistData,
			GMosaicAlgorithmData pMosaicAlgorithmData) {
		_pMosaicData = pMosaicData;
		_pMosaicRegistData = pMosaicRegistData;
		_pMosaicAlgorithmData = pMosaicAlgorithmData;
	}

	// ������ũ ��� �ε����� �����Ѵ�.
	// @ nBandIndex : ������ũ ��� �ε���
	public void setBandIndex(int nBandIndex) {
		_nBandIndex = nBandIndex;
	}

	// ������ũ ��� ������ �����Ѵ�.
	// @ return : GImageProcessor.ProcessCode ���� ��� ����
	public GImageProcessor.ProcessCode recalcStatData() {
		GImageProcessor.ProcessCode ret = GImageProcessor.ProcessCode.SUCCESS;
		int[] histg = null;
		int[] histgR = new int[256];
		int[] histgG = new int[256];
		int[] histgB = new int[256];
		int nMin = 255, nMax = 0, nSum = 0, nCount = 0;
		int nModeFreq = 0, nMode = 0;
		double dblMean = 0.0, dblV = 0.0;
		int nValidCount = 0, nBandTab = 0, nBand = 0;

		if (_pMosaicData == null) {
			System.out.println("GAutoMosaic.ReCalcStatData : Exist no mosaic data.");
			ret = GImageProcessor.ProcessCode.ERROR;

			return ret;
		}

		// -------------------------------------------------------------------------------------------//
		// {_nBandOderingArray} R : 2, G : 1, B : 0, length == 0 -> Gray
		// -------------------------------------------------------------------------------------------//
		if (_nBandIndex < _pMosaicData._nBandOderingArray.size())
			nBand = _pMosaicData._nBandOderingArray.get(_nBandIndex).intValue();

		if (nBand == 2)
			nBandTab = 0; // Red
		else if (nBand == 1)
			nBandTab = 1; // Green
		else if (nBand == 0)
			nBandTab = 2; // Blue
		// -------------------------------------------------------------------------------------------//

		try {

			for (int nPath = 0; nPath < _pMosaicData._nNumOfPath; nPath++) {
				for (int nRow = 0; nRow < _pMosaicData._nNumOfRow; nRow++) {
					if (_pMosaicRegistData[nPath][nRow]._oFileData._strFilePath.isEmpty())
						continue;

					GTiffDataReader gdReader = null;

					// Read the input file
					try {
						gdReader = new GTiffDataReader(_pMosaicRegistData[nPath][nRow]._oFileData._strFilePath,
								_pMosaicRegistData[nPath][nRow]._oFileData._maxBit16);
					} catch (Exception ex) {
						System.out.println("GAutoMosaic.recalcStatData : " + ex.toString());
						System.out
								.println("\t Layer Name : " + _pMosaicRegistData[nPath][nRow]._oFileData._strFilePath);
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

					gdReader.getHistogram(histgR, histgG, histgB);

					nMin = 255;
					nMax = 0;
					nMode = 0;
					dblMean = 0.0;
					dblV = 0.0;
					nModeFreq = 0;
					nSum = 0;
					nCount = 0;
					nValidCount = 0;

					switch (nBandTab) {
					case 0:
						histg = histgR;
					case 1:
						histg = histgG;
					case 2:
						histg = histgB;
					default:
						histg = histgR;
					}

					// @todo : Max Pixel Value
					// for(int i=1; i<=200; i++)
					for (int i = 1; i <= 255; i++) {
						if (histg[i] > 0) {
							if (i < nMin)
								nMin = i;
							if (i > nMax)
								nMax = i;
							if ((int) histg[i] > nModeFreq) {
								nMode = i;
								nModeFreq = histg[i];
							}
							nSum += i * histg[i];
							nCount += histg[i];
						}
					}
					dblMean = (double) nSum / (double) nCount;

					for (int j = nMin; j < nMax; j++) {
						if (histg[j] != 0) {
							dblV += Math.pow(Math.abs((double) j - dblMean), 2) * histg[j];
							nValidCount += histg[j];
						}
					}

					_pMosaicRegistData[nPath][nRow]._statData._nMin = nMin;
					_pMosaicRegistData[nPath][nRow]._statData._nMax = nMax;
					_pMosaicRegistData[nPath][nRow]._statData._nMode = nMode;
					_pMosaicRegistData[nPath][nRow]._statData._nRange = nMax - nMin;
					_pMosaicRegistData[nPath][nRow]._statData._dblMean = dblMean;
					_pMosaicRegistData[nPath][nRow]._statData._dblMeanMode = Math.abs(dblMean - nMode);
					_pMosaicRegistData[nPath][nRow]._statData._dblVariance = (double) (dblV / (double) (nValidCount));

					if (gdReader != null)
						gdReader.destory();
					gdReader = null;
				}
			}

		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GAutoMosaic.recalcStatData : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();

			ret = GImageProcessor.ProcessCode.ERROR_FAIL_PROCESS;

			return ret;
		} catch (Exception ex) {
			System.out.println("GAutoMosaic.recalcStatData : " + ex.toString());
			ex.printStackTrace();

			ret = GImageProcessor.ProcessCode.ERROR_FAIL_PROCESS;

			return ret;
		}

		return ret;
	}

	// Master ������ ������ũ ��� ���� ����� �����Ѵ�.
	// @ pMasterData : ������ũ ��� ���� ���
	// @
	// @ return : GMosaicResultData[] Master ������ ������ũ ��� ���� ���
	// @ return : GImageProcessor.ProcessCode ���� ��� ����
	public GImageProcessor.ProcessCode procMosaicMasterArea(GMosaicResultData[] pMasterData) {
		GImageProcessor.ProcessCode ret = GImageProcessor.ProcessCode.SUCCESS;

		int nPath = 0, nRow = 0, nPathDiffer = 0, nRowDiffer = 0;
		int nMasterCount = 1;
		MosaicProcMethod nPrcNumber = MosaicProcMethod.VERTICAL; // nPrcNumber = 0;
		boolean bPrcOut = false;

		GMosaicProcData Master = new GMosaicProcData();
		GMosaicProcData Slave = new GMosaicProcData();
		GMosaicResultData resultCenter = new GMosaicResultData();
		GMosaicResultData resultLeft = new GMosaicResultData();
		GMosaicResultData resultRight = new GMosaicResultData();
		GMosaicResultData resultTop = new GMosaicResultData();
		GMosaicResultData resultBottom = new GMosaicResultData();

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.procMosaicMasterArea :");
			System.out.println("[DEBUG]\t procMosaicMasterArea() [1] : nNumOfMaster = " + _pMosaicData._nNumOfMaster);
		}
//[DEBUG]

		try {

			while (nMasterCount <= _pMosaicData._nNumOfMaster) {
				for (nPath = 0; nPath < _pMosaicData._nNumOfPath; nPath++) {
					for (nRow = 0; nRow < _pMosaicData._nNumOfRow; nRow++) {
						if (_pMosaicRegistData[nPath][nRow]._bCheckMaster
								&& _pMosaicRegistData[nPath][nRow]._nLogicOrder == nMasterCount
								&& _pMosaicRegistData[nPath][nRow]._bCheckData
								&& !_pMosaicRegistData[nPath][nRow]._bUseChecking) {
							nPrcNumber = checkPrcNum(nPath, nRow, _pMosaicAlgorithmData._nMosaicProcDirection);

							// Result Data Initialize!!
							initResultData(_mosaicResultData, true);
							initResultData(resultCenter, true);

//[DEBUG]
							if (_IS_DEBUG) {
								System.out.println("[DEBUG]\t  nPrcNumber = " + nPrcNumber);
							}
//[DEBUG]		
							if ((nPrcNumber == MosaicProcMethod.VERTICAL
									&& _pMosaicAlgorithmData._nMosaicProcDirection == MosaicProcMethod.VERTICAL)
									|| (nPrcNumber == MosaicProcMethod.Horizontal
											&& _pMosaicAlgorithmData._nMosaicProcDirection == MosaicProcMethod.Horizontal)) {
								initResultData(resultLeft, false);
								initResultData(resultRight, false);
								getCenterData(nPath, nRow, MosaicProcMethod.Horizontal, resultCenter);
								getMasterLeftRightData(nPath, nRow, MosaicProcMethod.Horizontal, resultLeft);
								getMasterLeftRightData(nPath, nRow, MosaicProcMethod.VERTICAL, resultRight);

								if (resultLeft._bCheckData) {
									if (_mosaicResultData._bCheckData) {
										getMosaicprcData(_mosaicResultData, Master); // get master image
									} else if (!_mosaicResultData._bCheckData) {
										getMosaicprcData(resultCenter, Master); // get master image
										resultCenter._bUseChecking = true;
									}
									getMosaicprcData(resultLeft, Slave); // get slave image
									resultLeft._bUseChecking = true;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println(
												"[DEBUG]\t  resultLeft._bUseChecking = " + resultLeft._bUseChecking);
										System.out.println(
												"[DEBUG]\t  calcMosaicProcessing() [2] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}

								if (resultRight._bCheckData) {
									if (_mosaicResultData._bCheckData) {
										getMosaicprcData(_mosaicResultData, Master); // get master image
									} else if (!_mosaicResultData._bCheckData) {
										getMosaicprcData(resultCenter, Master); // get master image
										resultCenter._bUseChecking = true;
									}
									getMosaicprcData(resultRight, Slave);
									resultRight._bUseChecking = true;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println(
												"[DEBUG]\t  resultRight.bCheckData = " + resultRight._bCheckData);
										System.out.println(
												"[DEBUG]\t  Master : (w, h) = (" + Master._imgData._imgBox2d.getWidth()
														+ ", " + Master._imgData._imgBox2d.getHeight() + ")");
										System.out.println(
												"[DEBUG]\t  Slave : (w, h) = (" + Slave._imgData._imgBox2d.getWidth()
														+ ", " + Slave._imgData._imgBox2d.getHeight() + ")");
										System.out.println(
												"[DEBUG]\t  calcMosaicProcessing() [3] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}
							} // end nPrcNumber=0
							else if ((nPrcNumber == MosaicProcMethod.Horizontal
									&& _pMosaicAlgorithmData._nMosaicProcDirection == MosaicProcMethod.VERTICAL)
									|| (nPrcNumber == MosaicProcMethod.VERTICAL
											&& _pMosaicAlgorithmData._nMosaicProcDirection == MosaicProcMethod.Horizontal)) {
								initResultData(resultTop, false);
								initResultData(resultBottom, false);
								getCenterData(nPath, nRow, MosaicProcMethod.VERTICAL, resultCenter);
								getMasterTopBottomData(nPath, nRow, MosaicProcMethod.Horizontal, resultTop);
								getMasterTopBottomData(nPath, nRow, MosaicProcMethod.VERTICAL, resultBottom);

								if (resultTop._bCheckData) {
									if (_mosaicResultData._bCheckData)
										getMosaicprcData(_mosaicResultData, Master); // get master image
									else if (!_mosaicResultData._bCheckData) {
										getMosaicprcData(resultCenter, Master);
										resultCenter._bUseChecking = true;
									}
									getMosaicprcData(resultTop, Slave); // get slave image
									resultTop._bUseChecking = true;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println(
												"[DEBUG]\t  resultTop._bUseChecking = " + resultTop._bUseChecking);
										System.out.println(
												"[DEBUG]\t  calcMosaicProcessing() [4] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}

								if (resultBottom._bCheckData) {
									if (_mosaicResultData._bCheckData)
										getMosaicprcData(_mosaicResultData, Master);
									else if (!_mosaicResultData._bCheckData) {
										getMosaicprcData(resultCenter, Master);
										resultCenter._bUseChecking = true;
									}
									getMosaicprcData(resultBottom, Slave); // get slave image
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  resultBottom._bUseChecking = "
												+ resultBottom._bUseChecking);
										System.out.println(
												"[DEBUG]\t  calcMosaicProcessing() [5] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]						
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}

							} // end nPrcNumber=1

							// @todo : Copy�� �´��� ? Equal�� �´��� ?
							pMasterData[nMasterCount - 1].Copy(_mosaicResultData);
							pMasterData[nMasterCount - 1]._bCheckData = true;
							bPrcOut = true;

							break;
						} // end if ( main master )
					} // end 'r'

					if (bPrcOut) {
						bPrcOut = false;
						break;
					}
				} // end 'p'
				nMasterCount++;
			} // end while

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.procMosaicMasterArea : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return ret;
	}

	// Sub ������ ������ũ ��� ���� ����� �����Ѵ�.
	// @ pMasterData : ������ũ ��� ���� ���
	// @
	// @ return : GMosaicResultData[] Sub ������ ������ũ ��� ���� ���
	// @ return : GImageProcessor.ProcessCode ���� ��� ����
	public GImageProcessor.ProcessCode procMosaicSubArea(GMosaicResultData[] pMasterData) {
		GImageProcessor.ProcessCode ret = GImageProcessor.ProcessCode.SUCCESS;

		int nPath = 0, nRow = 0, nPathDiffer = 0, nRowDiffer = 0;
		int nMasterCount = 1;
		int nPrcNumber = 0;
		boolean bPrcOut = false;
		int nTab = 2; // �� �ʱⰪ�� 2�ΰ�?

		GMosaicProcData Master = new GMosaicProcData();
		GMosaicProcData Slave = new GMosaicProcData();
		GMosaicResultData resultCenter = new GMosaicResultData();
		GMosaicResultData resultLeft = new GMosaicResultData();
		GMosaicResultData resultRight = new GMosaicResultData();
		GMosaicResultData resultTop = new GMosaicResultData();
		GMosaicResultData resultBottom = new GMosaicResultData();

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.procMosaicSubArea :");
			System.out.println("[DEBUG]\t procMosaicSubArea() [1] : nNumOfMaster = " + _pMosaicData._nNumOfMaster
					+ "[_nLogicCount=" + _nLogicCount + "]");
		}
//[DEBUG]

		try {

			while (_nLogicCount > _pMosaicData._nNumOfMaster - 1) {
				nMasterCount = 1;
				while (nMasterCount <= _pMosaicData._nNumOfMaster) {
					for (nPath = 0; nPath < _pMosaicData._nNumOfPath; nPath++) {
						for (nRow = 0; nRow < _pMosaicData._nNumOfRow; nRow++) {
							if (_pMosaicRegistData[nPath][nRow]._bCheckMaster
									&& _pMosaicRegistData[nPath][nRow]._nLogicOrder == nMasterCount
									&& _pMosaicRegistData[nPath][nRow]._bCheckData
									&& _pMosaicRegistData[nPath][nRow]._bUseChecking) {
//[DEBUG]
								if (_IS_DEBUG) {
									System.out.println(
											"[DEBUG]\t procMosaicSubArea() [1-0] : InitResultData() [(nPath, nRow) = ("
													+ nPath + ", " + nRow + ")], nMasterCount = " + nMasterCount);
								}
//[DEBUG]

								initResultData(resultLeft, false);
								initResultData(resultRight, false);
								initResultData(resultTop, false);
								initResultData(resultBottom, false);

								if (nPath - nTab >= 0) {
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t procMosaicSubArea() [1-1] : resultLeft");
									}
//[DEBUG]
									getSlaveLeftRightData(nPath, nRow, nTab, MosaicProcMethod.Horizontal, resultLeft);
								}
								if (nPath + nTab < _pMosaicData._nNumOfPath) {
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t procMosaicSubArea() [1-2] : resultRight");
									}
//[DEBUG]
									getSlaveLeftRightData(nPath, nRow, nTab, MosaicProcMethod.VERTICAL, resultRight);
								}

								if (nRow - nTab >= 0) {
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t procMosaicSubArea() [1-3] : resultTop");
									}
//[DEBUG]
									getSlaveTopBottomData(nPath, nRow, nTab, MosaicProcMethod.Horizontal, resultTop);
								}
								if (nRow + nTab < _pMosaicData._nNumOfRow) {
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t procMosaicSubArea() [1-4] : resultBottom");
									}
//[DEBUG]
									getSlaveTopBottomData(nPath, nRow, nTab, MosaicProcMethod.VERTICAL, resultBottom);
								}

								// Master Area Mosaicking
								_mosaicResultData._bCheckData = false;
								_mosaicResultData._bUseChecking = false;

								if (resultLeft._bCheckData) // left mosaic
								{
									getMosaicprcData(pMasterData[nMasterCount - 1], Master); // get master image
									getMosaicprcData(resultLeft, Slave); // get slave image
									resultLeft._bUseChecking = true;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println(
												"[DEBUG]\t  procMosaicSubArea() [2] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]						
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}
								if (resultRight._bCheckData) // right mosaic
								{
									if (_mosaicResultData._bCheckData) {
										getMosaicprcData(_mosaicResultData, Master); // get master image
									} else {
										getMosaicprcData(pMasterData[nMasterCount - 1], Master); // get master image
									}

									getMosaicprcData(resultRight, Slave); // get slave image
									resultRight._bUseChecking = true;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println(
												"[DEBUG]\t  procMosaicSubArea() [3] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}
								if (resultTop._bCheckData) // top mosaic
								{
									if (_mosaicResultData._bCheckData) {
										getMosaicprcData(_mosaicResultData, Master); // get master image
									} else {
										getMosaicprcData(pMasterData[nMasterCount - 1], Master); // get master image
									}

									getMosaicprcData(resultTop, Slave); // get slave image
									resultTop._bUseChecking = true;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println(
												"[DEBUG]\t  procMosaicSubArea() [4] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}
								if (resultBottom._bCheckData) // bottom mosaic
								{
									if (_mosaicResultData._bCheckData) {
										getMosaicprcData(_mosaicResultData, Master); // get master image
									} else {
										getMosaicprcData(pMasterData[nMasterCount - 1], Master); // get master image
									}

									getMosaicprcData(resultBottom, Slave); // get slave image
									resultBottom._bUseChecking = true;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println(
												"[DEBUG]\t  procMosaicSubArea() [5] : Call calcMosaicProcessing [(nPath, nRow)=("
														+ nPath + ", " + nRow + ")]");
									}
//[DEBUG]
									calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
									_mosaicResultData._bCheckData = true;
									_nLogicCount--;
//[DEBUG]
									if (_IS_DEBUG) {
										System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
									}
//[DEBUG]
								}

								if (_mosaicResultData._bCheckData) {
									// @todo : Copy�� �´��� ? Equal�� �´��� ?
									pMasterData[nMasterCount - 1].Copy(_mosaicResultData);
									pMasterData[nMasterCount - 1]._bCheckData = true;
									pMasterData[nMasterCount - 1]._bUseChecking = false;
								}
								bPrcOut = true;
								break;
							} // end if(master)
						} // end for(nRow)

						if (bPrcOut) {
							bPrcOut = false;
							break;
						}
					} // end for(nPath)
					nMasterCount++;
				} // end while(nMasterCount)

				if (_nLogicCount > _pMosaicData._nNumOfMaster - 1)
					nTab++;
			} // end while(m_nLogicCount)

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.procMosaicSubArea : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return ret;
	}

	// ������ũ ��� ���� ��Ͽ� ���� ������ũ ������ �����Ѵ�.
	// @ pMasterData : ������ũ ��� ���� ���
	// @
	// @ return : GMosaicResultData[] ������ũ ��� ���� ���
	// @ return : GImageProcessor.ProcessCode ���� ��� ����
	public GImageProcessor.ProcessCode procCreateMasterMosiacImage(GMosaicResultData[] pMasterData) {
		GImageProcessor.ProcessCode ret = GImageProcessor.ProcessCode.SUCCESS;

		GMosaicProcData Master = new GMosaicProcData();
		GMosaicProcData Slave = new GMosaicProcData();

		int nCount = 0, nMinDist = 1000, nMinId = 0;
		double[] dblMasterDist = null;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.procCreateMasterMosiacImage :");
			System.out.println("[DEBUG]\t procCreateMasterMosiacImage() [1] : nNumOfMaster = "
					+ _pMosaicData._nNumOfMaster + "[_nLogicCount=" + _nLogicCount + "]");
		}
//[DEBUG]

		try {

			if (_pMosaicData._nNumOfMaster >= 2) {
				if (_pMosaicData._nNumOfMaster == 2) {
					getMosaicprcData(pMasterData[0], Master); // get master image
					getMosaicprcData(pMasterData[1], Slave); // get slave image
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println(
								"[DEBUG]\t  procCreateMasterMosiacImage() [2] : Call calcMosaicProcessing [_nLogicCount = "
										+ _nLogicCount + "]");
					}
//[DEBUG]

					calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
					_mosaicResultData._bCheckData = true;
					_nLogicCount--;
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
					}
//[DEBUG]
				} else {
					// calculate distance between masters
					dblMasterDist = new double[_pMosaicData._nNumOfMaster - 1];

					for (int i = 1; i < _pMosaicData._nNumOfMaster; i++) {
						dblMasterDist[i - 1] = Math
								.sqrt(Math.pow(Math.abs(pMasterData[0]._nScenePath - pMasterData[i]._nScenePath), 2)
										- Math.pow(Math.abs(pMasterData[0]._nSceneRow - pMasterData[i]._nSceneRow), 2));
					}

					// Start Main Mosaicking
					nCount = 0;
					while (nCount < _pMosaicData._nNumOfMaster) {
						nMinDist = 1000;
						nMinId = 0;
						for (int j = 1; j < _pMosaicData._nNumOfMaster; j++) {

							if ((dblMasterDist[j - 1] < nMinDist) && (!pMasterData[j]._bUseChecking)) {
								nMinId = j;
							}
						}

						// get master
						if (nCount == 0) {
							getMosaicprcData(pMasterData[0], Master);
							pMasterData[0]._bUseChecking = true;
						} else {
							getMosaicprcData(_mosaicResultData, Master);
							_mosaicResultData._bUseChecking = true;
						}

						// get slave
						getMosaicprcData(pMasterData[nMinId], Slave); // get slave image
						pMasterData[nMinId]._bUseChecking = true;
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println(
									"[DEBUG]\t  procCreateMasterMosiacImage() [3] : Call calcMosaicProcessing [nCount = "
											+ nCount + "][_nLogicCount = " + _nLogicCount + "]");
						}
//[DEBUG]
						// Mosaicking!
						calcMosaicProcessing(Master, Slave, _mosaicResultData); // Create Mosaic Image
						_mosaicResultData._bCheckData = true;
						_nLogicCount--;
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t  _nLogicCount = " + _nLogicCount);
						}
//[DEBUG]
						// increase count!!
						nCount++;
					} // end while
						////////////////////////////////////////////////////////////////////////////
						// End Main Mosaicking
						////////////////////////////////////////////////////////////////////////////

					dblMasterDist = null;
					////////////////////////////////////////////////////////////////////////////
				}
			} // end "Create Master Mosaic image(nRowesult image)"

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GAutoMosaic.procCreateMasterMosiacImage : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return ret;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ������ũ ���μ������࿡ ���� ���� Path, Row������ ���� ���� ������ Ȯ���Ͽ� ��ȯ�Ѵ�.
	// @ nPath : ���� Path
	// @ nRow : ���� Row
	// @ nMosaicProcDirection : ������ũ ���μ��� ����
	// @
	// @ return : MosaicProcMethod ���� ���� ����
	private MosaicProcMethod checkPrcNum(int nPath, int nRow, MosaicProcMethod nMosaicProcDirection) {

		try {

			if (nMosaicProcDirection == MosaicProcMethod.VERTICAL) {
				// check nPrcNumber
				if (nPath - 1 >= 0 && nRow - 1 >= 0 && nRow + 1 < _pMosaicData._nNumOfRow) {
					if (!_pMosaicRegistData[nPath - 1][nRow]._bCheckData
							&& _pMosaicRegistData[nPath - 1][nRow - 1]._bCheckData
							&& _pMosaicRegistData[nPath - 1][nRow + 1]._bCheckData) {
						return MosaicProcMethod.Horizontal;
					}
				}
				if (nPath + 1 < _pMosaicData._nNumOfPath && nRow - 1 >= 0 && nRow + 1 < _pMosaicData._nNumOfRow) {
					if (!_pMosaicRegistData[nPath + 1][nRow]._bCheckData
							&& _pMosaicRegistData[nPath + 1][nRow - 1]._bCheckData
							&& _pMosaicRegistData[nPath + 1][nRow + 1]._bCheckData) {
						return MosaicProcMethod.Horizontal;
					}
				}
			} else {
				// check nPrcNumber
				if (nPath - 1 >= 0 && nRow - 1 >= 0 && nPath + 1 < _pMosaicData._nNumOfPath) {
					if (!_pMosaicRegistData[nPath][nRow - 1]._bCheckData
							&& _pMosaicRegistData[nPath - 1][nRow - 1]._bCheckData
							&& _pMosaicRegistData[nPath + 1][nRow - 1]._bCheckData) {
						return MosaicProcMethod.Horizontal;
					}
				}
				if (nRow + 1 < _pMosaicData._nNumOfRow && nPath - 1 >= 0 && nPath + 1 < _pMosaicData._nNumOfPath) {
					if (!_pMosaicRegistData[nPath][nRow + 1]._bCheckData
							&& _pMosaicRegistData[nPath - 1][nRow + 1]._bCheckData
							&& _pMosaicRegistData[nPath + 1][nRow + 1]._bCheckData) {
						return MosaicProcMethod.Horizontal;
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.checkPrcNum : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return MosaicProcMethod.VERTICAL;
	}

	// ������ũ ���μ������࿡ ���� ���� Path, Row������ Center ������ũ ��� ������
	// ��ȯ�Ѵ�.
	// @ nPath : ���� Path
	// @ nRow : ���� Row
	// @ nMosaicProcDirection : ������ũ ���μ��� ����
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void getCenterData(int nPath, int nRow, MosaicProcMethod nMosaicProcDirection,
			GMosaicResultData resultData) {
		int nPathDiffer = 0, nRowDiffer = 0, nData = 0, nNumData = 0;
		ProcessDirection nDirection = ProcessDirection.PROCESS_TAB;

		if (nMosaicProcDirection == MosaicProcMethod.Horizontal) {
			nPathDiffer = 0;
			nRowDiffer = -1;
			nData = nRow;
			nNumData = _pMosaicData._nNumOfRow;
			nDirection = ProcessDirection.PROCESS_LEFTRIGHT;
		} else {
			nPathDiffer = -1;
			nRowDiffer = 0;
			nData = nPath;
			nNumData = _pMosaicData._nNumOfPath;
			nDirection = ProcessDirection.PROCESS_TOPBOTTOM;
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.getCenterData :");
		}
//[DEBUG]

		try {

			// {get center
			// Case 1 : Center Up
			////////////////////////////////////////////////////////////////////////////////////////////////
			if (nData - 1 >= 0)// center up
			{
				if (_pMosaicRegistData[nPath + nPathDiffer][nRow + nRowDiffer]._bCheckData
						&& !_pMosaicRegistData[nPath + nPathDiffer][nRow + nRowDiffer]._bCheckMaster
						&& !_pMosaicRegistData[nPath + nPathDiffer][nRow + nRowDiffer]._bUseChecking) {
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t getCenterData() [1] : Call calcProcessing(" + nPath + ", " + nRow
								+ ")  [(nPath, nRow)=(" + nPath + ", " + nRow + ")]");
					}
//[DEBUG]
					calcProcessing(nPath, nRow, true, nDirection, _mosaicResultData);
				}
			}
			////////////////////////////////////////////////////////////////////////////////////////////////

			// Case 2 : Center Down
			////////////////////////////////////////////////////////////////////////////////////////////////
			if (nMosaicProcDirection == MosaicProcMethod.Horizontal)
				nRowDiffer += 2;
			else
				nPathDiffer += 2;
			if (nData + 1 < nNumData)// center down
			{
				if (_pMosaicRegistData[nPath + nPathDiffer][nRow + nRowDiffer]._bCheckData
						&& !_pMosaicRegistData[nPath + nPathDiffer][nRow + nRowDiffer]._bCheckMaster
						&& !_pMosaicRegistData[nPath + nPathDiffer][nRow + nRowDiffer]._bUseChecking) {
//[DEBUG]
					if (_IS_DEBUG) {
						System.out.println("[DEBUG]\t getCenterData() [2] : Call calcProcessing(" + nPath + ", " + nRow
								+ ")  [(nPath, nRow)=(" + nPath + ", " + nRow + ")]");
					}
//[DEBUG]
					calcProcessing(nPath, nRow, false, nDirection, _mosaicResultData);
				}
			}
			////////////////////////////////////////////////////////////////////////////////////////////////

			// Case 3 : Only Master
			////////////////////////////////////////////////////////////////////////////////////////////////
			if (!_pMosaicRegistData[nPath][nRow]._bUseChecking && !_mosaicResultData._bCheckData)// only master
			{
//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t getCenterData() [3] : Call calcProcessing(" + nPath + ", " + nRow
							+ ")  [(nPath, nRow)=(" + nPath + ", " + nRow + ")]");
				}
//[DEBUG]
				// @todo : Copy�� �´��� ? Equal�� �´��� ?
				resultData.Copy(_pMosaicRegistData[nPath][nRow]);
				_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
				resultData._bCheckData = true;
			}
			////////////////////////////////////////////////////////////////////////////////////////////////

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.getCenterData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		// }end center
	}

	// ������ũ ���μ������࿡ ���� ���� Path, Row������ Master Left & Right ������ũ ���
	// ������ ��ȯ�Ѵ�.
	// @ nPath : ���� Path
	// @ nRow : ���� Row
	// @ nMosaicProcDirection : ������ũ ���μ��� ����
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void getMasterLeftRightData(int nPath, int nRow, MosaicProcMethod nMosaicProcDirection,
			GMosaicResultData resultData) {
		int nPathDiffer = 0;
		boolean bRes = false;

		if (nMosaicProcDirection == MosaicProcMethod.Horizontal) {
			nPathDiffer = -1;
			if (nPath - 1 >= 0)
				bRes = true;
			else
				bRes = false;
		} else {
			nPathDiffer = 1;
			if (nPath + 1 < _pMosaicData._nNumOfPath)
				bRes = true;
			else
				bRes = false;
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.getMasterLeftRightData :");
		}
//[DEBUG]

		try {

			// {get left, Right
			if (bRes) {
				double dblGainCenter = 0, dblGainUp = 0, dblGainDown = 0;
				boolean bCheckCenter = false, bCheckUp = false, bCheckDown = false;

				if (_pMosaicRegistData[nPath + nPathDiffer][nRow]._bCheckData
						&& !_pMosaicRegistData[nPath + nPathDiffer][nRow]._bCheckMaster
						&& !_pMosaicRegistData[nPath + nPathDiffer][nRow]._bUseChecking)// check 'r'
				{
					dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow]._statData._dblVariance);
					bCheckCenter = true;
				}
				if (nRow - 1 >= 0) {
					if (_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._bCheckData
							&& !_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._bCheckMaster
							&& !_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._bUseChecking) {
						dblGainUp = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._statData._dblVariance);
						bCheckUp = true;
					}
				}
				if (nRow + 1 < _pMosaicData._nNumOfRow) {
					if (_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]._bCheckData
							&& !_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]._bCheckMaster
							&& !_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]._bUseChecking) {
						dblGainDown = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]._statData._dblVariance);
						bCheckDown = true;
					}
				}
				if (getSubMasterPath(dblGainCenter, dblGainUp, dblGainDown) == PathDirection.PATH_CENTER) {
					if (bCheckCenter && bCheckUp) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t getMasterLeftRightData() [1] : Call calcProcessing("
									+ (nPath + nPathDiffer) + ", " + nRow + ")  [(nPath, nRow)=(" + nPath + ", " + nRow
									+ ")]");
						}
//[DEBUG]
						calcProcessing(nPath + nPathDiffer, nRow, true, ProcessDirection.PROCESS_LEFTRIGHT, resultData);
					}
					if (bCheckCenter && bCheckDown) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t getMasterLeftRightData() [2] : Call calcProcessing("
									+ (nPath + nPathDiffer) + ", " + nRow + ")  [(nPath, nRow)=(" + nPath + ", " + nRow
									+ ")]");
						}
//[DEBUG]
						calcProcessing(nPath + nPathDiffer, nRow, false, ProcessDirection.PROCESS_LEFTRIGHT,
								resultData);
					}
					if (!bCheckUp && !bCheckDown) {
						// @todo : Copy�� �´��� ? Equal�� �´��� ?
						resultData.Copy(_pMosaicRegistData[nPath + nPathDiffer][nRow]);
						_pMosaicRegistData[nPath + nPathDiffer][nRow]._bUseChecking = true;
						resultData._bCheckData = true;
					}
				} else if (getSubMasterPath(dblGainCenter, dblGainUp, dblGainDown) == PathDirection.PATH_UP) {
					if (bCheckUp && bCheckCenter) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t getMasterLeftRightData() [3] : Call calcProcessing("
									+ (nPath + nPathDiffer) + ", " + (nRow - 1) + ")  [(nPath, nRow)=(" + nPath + ", "
									+ nRow + ")]");
						}
//[DEBUG]
						calcProcessing(nPath + nPathDiffer, nRow - 1, false, ProcessDirection.PROCESS_LEFTRIGHT,
								resultData);
						if (bCheckDown) {
//[DEBUG]
							if (_IS_DEBUG) {
								System.out.println("[DEBUG]\t getMasterLeftRightData() [4] : Call calcProcessing("
										+ (nPath + nPathDiffer) + ", " + nRow + ")  [(nPath, nRow)=(" + nPath + ", "
										+ nRow + ")]");
							}
//[DEBUG]
							_pMosaicRegistData[nPath + nPathDiffer][nRow]._bUseChecking = true;
							calcProcessing(nPath + nPathDiffer, nRow, false, ProcessDirection.PROCESS_LEFTRIGHT,
									resultData);
						}
					}
					if (!bCheckCenter && !bCheckDown) {
						// @todo : Copy�� �´��� ? Equal�� �´��� ?
						resultData.Copy(_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]);
						_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._bUseChecking = true;
						resultData._bCheckData = true;
					}
				} else if (getSubMasterPath(dblGainCenter, dblGainUp, dblGainDown) == PathDirection.PATH_DOWN) {
					if (bCheckDown && bCheckCenter) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t getMasterLeftRightData() [5] : Call calcProcessing("
									+ (nPath + nPathDiffer) + ", " + (nRow + 1) + ")  [(nPath, nRow)=(" + nPath + ", "
									+ nRow + ")]");
						}
//[DEBUG]
						calcProcessing(nPath + nPathDiffer, nRow + 1, true, ProcessDirection.PROCESS_LEFTRIGHT,
								resultData);
						if (bCheckUp) {
							// @todo : nRow-2 ?
							_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._bUseChecking = true;

//[DEBUG]
							if (_IS_DEBUG) {
								System.out.println("[DEBUG]\t getMasterLeftRightData() [6] : Call calcProcessing("
										+ (nPath + nPathDiffer) + ", " + (nRow - 2) + ")  [(nPath, nRow)=(" + nPath
										+ ", " + nRow + ")]");
							}
//[DEBUG]
							// @todo : nRow-2 ?
							calcProcessing(nPath + nPathDiffer, nRow - 1, true, ProcessDirection.PROCESS_LEFTRIGHT,
									resultData);
						}
					}
					if (!bCheckCenter && !bCheckUp) {
						// @todo : Copy�� �´��� ? Equal�� �´��� ?
						resultData.Copy(_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]);
						_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]._bUseChecking = true;
						resultData._bCheckData = true;
					}
				}
			} // }end left, Right

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.getMasterLeftRightData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// ������ũ ���μ������࿡ ���� ���� Path, Row������ Master Top & Bottom ������ũ ���
	// ������ ��ȯ�Ѵ�.
	// @ nPath : ���� Path
	// @ nRow : ���� Row
	// @ nMosaicProcDirection : ������ũ ���μ��� ����
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void getMasterTopBottomData(int nPath, int nRow, MosaicProcMethod nMosaicProcDirection,
			GMosaicResultData resultData) {
		int nRowDiffer = 0;
		boolean bRes = false;

		if (nMosaicProcDirection == MosaicProcMethod.Horizontal) {
			nRowDiffer = -1;
			if (nRow - 1 >= 0)
				bRes = true;
			else
				bRes = false;
		} else {
			nRowDiffer = 1;
			if (nRow + 1 < _pMosaicData._nNumOfRow)
				bRes = true;
			else
				bRes = false;
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.getMasterTopBottomData :");
		}
//[DEBUG]

		try {

			// {get Top, bottom
			if (bRes) {
				double dblGainCenter = 0, dblGainLeft = 0, dblGainRight = 0;
				boolean bCheckCenter = false, bCheckLeft = false, bCheckRight = false;

				if (_pMosaicRegistData[nPath][nRow + nRowDiffer]._bCheckData
						&& !_pMosaicRegistData[nPath][nRow + nRowDiffer]._bCheckMaster
						&& !_pMosaicRegistData[nPath][nRow + nRowDiffer]._bUseChecking) {
					dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath][nRow + nRowDiffer]._statData._dblVariance);
					bCheckCenter = true;
				}
				if (nPath - 1 >= 0) {
					if (_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]._bCheckData
							&& !_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]._bCheckMaster
							&& !_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]._bUseChecking) {
						dblGainLeft = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]._statData._dblVariance);
						bCheckLeft = true;
					}
				}
				if (nPath + 1 < _pMosaicData._nNumOfPath) {
					if (_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]._bCheckData
							&& !_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]._bCheckMaster
							&& !_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]._bUseChecking) {
						dblGainRight = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]._statData._dblVariance);
						bCheckRight = true;
					}
				}

				if (getSubMasterPath(dblGainCenter, dblGainLeft, dblGainRight) == PathDirection.PATH_CENTER)// center
				{
					if (bCheckCenter && bCheckLeft) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t getMasterTopBottomData() [1] : Call calcProcessing(" + nPath
									+ ", " + (nRow + nRowDiffer) + ")  [(nPath, nRow)=(" + nPath + ", " + nRow + ")]");
						}
//[DEBUG]
						calcProcessing(nPath, nRow + nRowDiffer, true, ProcessDirection.PROCESS_TOPBOTTOM, resultData);
					}
					if (bCheckCenter && bCheckRight) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println("[DEBUG]\t getMasterTopBottomData() [2] : Call calcProcessing(" + nPath
									+ ", " + (nRow + nRowDiffer) + ")  [(nPath, nRow)=(" + nPath + ", " + nRow + ")]");
						}
//[DEBUG]
						calcProcessing(nPath, nRow + nRowDiffer, false, ProcessDirection.PROCESS_TOPBOTTOM, resultData);
					}
					if (!bCheckLeft && !bCheckRight) {
						// @todo : Copy�� �´��� ? Equal�� �´��� ?
						resultData.Copy(_pMosaicRegistData[nPath][nRow + nRowDiffer]);
						_pMosaicRegistData[nPath][nRow + nRowDiffer]._bUseChecking = true;
						resultData._bCheckData = true;
					}
				} else if (getSubMasterPath(dblGainCenter, dblGainLeft, dblGainRight) == PathDirection.PATH_UP) {
					if (bCheckLeft && bCheckCenter) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println(
									"[DEBUG]\t getMasterTopBottomData() [3] : Call calcProcessing(" + (nPath - 1) + ", "
											+ (nRow + nRowDiffer) + ")  [(nPath, nRow)=(" + nPath + ", " + nRow + ")]");
						}
//[DEBUG]
						calcProcessing(nPath - 1, nRow + nRowDiffer, false, ProcessDirection.PROCESS_TOPBOTTOM,
								resultData);
						if (bCheckRight) {
//[DEBUG]
							if (_IS_DEBUG) {
								System.out.println("[DEBUG]\t getMasterTopBottomData() [4] : Call calcProcessing("
										+ nPath + ", " + (nRow + nRowDiffer) + ")  [(nPath, nRow)=(" + nPath + ", "
										+ nRow + ")]");
							}
//[DEBUG]
							_pMosaicRegistData[nPath][nRow + nRowDiffer]._bUseChecking = true;
							calcProcessing(nPath, nRow + nRowDiffer, false, ProcessDirection.PROCESS_TOPBOTTOM,
									resultData);
						}
					}
					if (!bCheckCenter && !bCheckRight) {
						// @todo : Copy�� �´��� ? Equal�� �´��� ?
						resultData.Copy(_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]);
						_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]._bUseChecking = true;
						resultData._bCheckData = true;
					}
				} else if (getSubMasterPath(dblGainCenter, dblGainLeft, dblGainRight) == PathDirection.PATH_DOWN) {
					if (bCheckRight && bCheckCenter) {
//[DEBUG]
						if (_IS_DEBUG) {
							System.out.println(
									"[DEBUG]\t getMasterTopBottomData() [5] : Call calcProcessing(" + (nPath + 1) + ", "
											+ (nRow + nRowDiffer) + ")  [(nPath, nRow)=(" + nPath + ", " + nRow + ")]");
						}
//[DEBUG]
						calcProcessing(nPath + 1, nRow + nRowDiffer, true, ProcessDirection.PROCESS_TOPBOTTOM,
								resultData);
						if (bCheckLeft) {
//[DEBUG]
							if (_IS_DEBUG) {
								System.out.println("[DEBUG]\t getMasterTopBottomData() [6] : Call calcProcessing("
										+ (nPath - 2) + ", " + (nRow + nRowDiffer) + ")  [(nPath, nRow)=(" + nPath
										+ ", " + nRow + ")]");
							}
//[DEBUG]
							// @todo : nPath-2 ?
							_pMosaicRegistData[nPath][nRow + nRowDiffer]._bUseChecking = true;
							calcProcessing(nPath, nRow + nRowDiffer, true, ProcessDirection.PROCESS_TOPBOTTOM,
									resultData);
						}
					}
					if (!bCheckCenter && !bCheckLeft) {
						// @todo : Copy�� �´��� ? Equal�� �´��� ?
						resultData.Copy(_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]);
						_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]._bUseChecking = true;
						resultData._bCheckData = true;
					}
				}
			} // end Top, bottom

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.getMasterTopBottomData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// ������ũ ���μ������࿡ ���� ���� Path, Row������ Slave Left & Right ������ũ ���
	// ������ ��ȯ�Ѵ�.
	// @ nPath : ���� Path
	// @ nRow : ���� Row
	// @ nMosaicProcDirection : ������ũ ���μ��� ����
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void getSlaveLeftRightData(int nPath, int nRow, int nTab, MosaicProcMethod nMosaicProcDirection,
			GMosaicResultData resultData) {
		int nPathDiffer = 0;
		boolean bRes = false, bOK = true;
		double dblGainCenter = 0, dblGainUp = 0, dblGainDown = 0;
		boolean bCheckCenter = false, bCheckUp = false, bCheckDown = false;

		if (nMosaicProcDirection == MosaicProcMethod.Horizontal) {
			nPathDiffer = -nTab;
			if (nPath - nTab >= 0)
				bRes = true;
			else
				bRes = false;
		} else {
			nPathDiffer = nTab;
			if (nPath + nTab < _pMosaicData._nNumOfPath)
				bRes = true;
			else
				bRes = false;
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.getSlaveLeftRightData :");
		}
//[DEBUG]

		try {

			// get left image
			if (bRes) {
				if (nRow - 1 >= 0 && nRow + 1 < _pMosaicData._nNumOfRow)// use center, up, down
				{
					bOK = isCheckData(nPath + nPathDiffer, nRow, nTab, PathDirection.PATH_CENTER,
							MosaicProcMethod.Horizontal);

					if (bOK) {
						dblGainUp = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._statData._dblVariance);
						bCheckUp = true;
						dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow]._statData._dblVariance);
						bCheckCenter = true;
						dblGainDown = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]._statData._dblVariance);
						bCheckDown = true;

						if (getSubMasterPath(dblGainCenter, dblGainUp, dblGainDown) == PathDirection.PATH_CENTER) // center
							checkSlaveLRDataCenter(nPath + nPathDiffer, nRow, nTab, PathDirection.PATH_CENTER,
									resultData);
						else if (getSubMasterPath(dblGainCenter, dblGainUp, dblGainDown) == PathDirection.PATH_UP) // up
							checkSlaveLRDataCenter(nPath + nPathDiffer, nRow, nTab, PathDirection.PATH_UP, resultData);
						else if (getSubMasterPath(dblGainCenter, dblGainUp, dblGainDown) == PathDirection.PATH_DOWN) // down
							checkSlaveLRDataCenter(nPath + nPathDiffer, nRow, nTab, PathDirection.PATH_DOWN,
									resultData);
					}
				} else if (nRow - 1 >= 0 && nRow + 1 >= _pMosaicData._nNumOfRow)// use center, up
				{
					bOK = isCheckData(nPath + nPathDiffer, nRow, nTab, PathDirection.PATH_UP,
							MosaicProcMethod.Horizontal);

					if (bOK) {
						dblGainUp = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow - 1]._statData._dblVariance);
						bCheckUp = true;
						dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow]._statData._dblVariance);
						bCheckCenter = true;

						if (getSubMasterPath(dblGainCenter, dblGainUp, 0) == PathDirection.PATH_CENTER)
							checkSlaveLRData(nPath + nPathDiffer, nRow, nTab, true, PathDirection.PATH_CENTER,
									resultData);
						else if (getSubMasterPath(dblGainCenter, dblGainUp, 0) == PathDirection.PATH_UP)
							checkSlaveLRData(nPath + nPathDiffer, nRow, nTab, true, PathDirection.PATH_UP, resultData);
					}
				} else if (nRow - 1 < 0 && nRow + 1 < _pMosaicData._nNumOfRow)// use center, down
				{
					bOK = isCheckData(nPath + nPathDiffer, nRow, nTab, PathDirection.PATH_DOWN,
							MosaicProcMethod.Horizontal);

					if (bOK) {
						dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow]._statData._dblVariance);
						bCheckCenter = true;
						dblGainDown = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
								/ Math.sqrt(_pMosaicRegistData[nPath + nPathDiffer][nRow + 1]._statData._dblVariance);
						bCheckDown = true;

						if (getSubMasterPath(dblGainCenter, 0, dblGainDown) == PathDirection.PATH_CENTER)
							checkSlaveLRData(nPath + nPathDiffer, nRow, nTab, false, PathDirection.PATH_CENTER,
									resultData);
						else if (getSubMasterPath(dblGainCenter, 0, dblGainDown) == PathDirection.PATH_DOWN)
							checkSlaveLRData(nPath + nPathDiffer, nRow, nTab, false, PathDirection.PATH_DOWN,
									resultData);
					}
				} else if (nRow - 1 < 0 && nRow + 1 >= _pMosaicData._nNumOfRow) // use center
				{
					bOK = isCheckData(nPath + nPathDiffer, nRow, nTab, PathDirection.PATH_NONE,
							MosaicProcMethod.Horizontal);

					if (bOK) {
						if (_pMosaicRegistData[nPath + nPathDiffer][nRow]._bCheckData
								&& !_pMosaicRegistData[nPath + nPathDiffer][nRow]._bCheckMaster
								&& !_pMosaicRegistData[nPath + nPathDiffer][nRow]._bUseChecking)// center check
						{
							// @todo : Copy�� �´��� ? Equal�� �´��� ?
							resultData.Copy(_pMosaicRegistData[nPath + nPathDiffer][nRow]);
							_pMosaicRegistData[nPath + nPathDiffer][nRow]._bUseChecking = true;
							resultData._bCheckData = true;
						}
					}
				}
				////////////////////////////////////////////////////////////////////////////////////////
			} // end get image

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.getSlaveLeftRightData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// ������ũ ���μ������࿡ ���� ���� Path, Row������ Slave Top & Bottom ������ũ ���
	// ������ ��ȯ�Ѵ�.
	// @ nPath : ���� Path
	// @ nRow : ���� Row
	// @ nMosaicProcDirection : ������ũ ���μ��� ����
	// @
	// @ reutrn : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void getSlaveTopBottomData(int nPath, int nRow, int nTab, MosaicProcMethod nMosaicProcDirection,
			GMosaicResultData resultData) {
		int nRowDiffer = 0;
		boolean bOK = true;
		double dblGainCenter = 0, dblGainLeft = 0, dblGainRight = 0;
		boolean bCheckCenter = false, bCheckLeft = false, bCheckRight = false;

		if (nMosaicProcDirection == MosaicProcMethod.Horizontal)
			nRowDiffer = -nTab;
		else
			nRowDiffer = nTab;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.getSlaveTopBottomData :");
		}
//[DEBUG]

		try {

			// get image
			if (nPath - 1 >= 0 && nPath + 1 < _pMosaicData._nNumOfPath)// use center, left, right
			{
				bOK = isCheckData(nPath, nRow + nRowDiffer, nTab, PathDirection.PATH_CENTER, MosaicProcMethod.VERTICAL);

				if (bOK) {
					dblGainLeft = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]._statData._dblVariance);
					bCheckLeft = true;
					dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath][nRow + nRowDiffer]._statData._dblVariance);
					bCheckCenter = true;
					dblGainRight = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]._statData._dblVariance);
					bCheckRight = true;

					if (getSubMasterPath(dblGainCenter, dblGainLeft, dblGainRight) == PathDirection.PATH_CENTER)
						checkSlaveTBDataCenter(nPath, nRow + nRowDiffer, nTab, PathDirection.PATH_CENTER,
								nMosaicProcDirection, resultData);
					else if (getSubMasterPath(dblGainCenter, dblGainLeft, dblGainRight) == PathDirection.PATH_UP)
						checkSlaveTBDataCenter(nPath, nRow + nRowDiffer, nTab, PathDirection.PATH_UP,
								nMosaicProcDirection, resultData);
					else if (getSubMasterPath(dblGainCenter, dblGainLeft, dblGainRight) == PathDirection.PATH_DOWN)
						checkSlaveTBDataCenter(nPath, nRow + nRowDiffer, nTab, PathDirection.PATH_DOWN,
								nMosaicProcDirection, resultData);
				}
			} else if (nPath - 1 >= 0 && nPath + 1 >= _pMosaicData._nNumOfPath)// use center, left
			{
				bOK = isCheckData(nPath, nRow + nRowDiffer, nTab, PathDirection.PATH_UP, MosaicProcMethod.VERTICAL);

				if (bOK) {
					dblGainLeft = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath - 1][nRow + nRowDiffer]._statData._dblVariance);
					bCheckLeft = true;
					dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath][nRow + nRowDiffer]._statData._dblVariance);
					bCheckCenter = true;

					if (getSubMasterPath(dblGainCenter, dblGainLeft, 0) == PathDirection.PATH_CENTER)
						checkSlaveTBData(nPath, nRow + nRowDiffer, nTab, true, PathDirection.PATH_CENTER, resultData);
					else if (getSubMasterPath(dblGainCenter, dblGainLeft, 0) == PathDirection.PATH_UP)
						checkSlaveTBData(nPath, nRow + nRowDiffer, nTab, true, PathDirection.PATH_UP, resultData);
				}
			} else if (nPath - 1 < 0 && nPath + 1 < _pMosaicData._nNumOfPath)// use center, right
			{
				bOK = isCheckData(nPath, nRow + nRowDiffer, nTab, PathDirection.PATH_UP, MosaicProcMethod.VERTICAL);

				if (bOK) {
					dblGainCenter = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath][nRow + nRowDiffer]._statData._dblVariance);
					bCheckCenter = true;
					dblGainRight = Math.sqrt(_pMosaicRegistData[nPath][nRow]._statData._dblVariance)
							/ Math.sqrt(_pMosaicRegistData[nPath + 1][nRow + nRowDiffer]._statData._dblVariance);
					bCheckRight = true;

					if (getSubMasterPath(dblGainCenter, 0, dblGainRight) == PathDirection.PATH_CENTER)
						checkSlaveTBData(nPath, nRow + nRowDiffer, nTab, false, PathDirection.PATH_CENTER, resultData);
					else if (getSubMasterPath(dblGainCenter, 0, dblGainRight) == PathDirection.PATH_DOWN)
						checkSlaveTBData(nPath, nRow + nRowDiffer, nTab, false, PathDirection.PATH_DOWN, resultData);
				}
			} else if (nPath - 1 < 0 && nPath + 1 >= _pMosaicData._nNumOfPath) // only center
			{
				bOK = isCheckData(nPath, nRow + nRowDiffer, nTab, PathDirection.PATH_NONE, MosaicProcMethod.VERTICAL);

				if (bOK) {
					if (_pMosaicRegistData[nPath][nRow + nRowDiffer]._bCheckData
							&& !_pMosaicRegistData[nPath][nRow + nRowDiffer]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow + nRowDiffer]._bUseChecking)// center check
					{
						// @todo : Copy�� �´��� ? Equal�� �´��� ?
						resultData.Copy(_pMosaicRegistData[nPath][nRow + nRowDiffer]);
						_pMosaicRegistData[nPath][nRow + nRowDiffer]._bUseChecking = true;
						resultData._bCheckData = true;
					}
				}
			} // end get image

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.getSlaveTopBottomData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// ������ũ ���μ������࿡ ���� ���� Path, Row������ ������ũ�� �����Ͽ� ������ũ ���
	// ������ ��ȯ�Ѵ�.
	// @ nPath : ���� Path
	// @ nRow : ���� Row
	// @ bCenterUpDown : ������ũ ���μ��� ����
	// @ nDirection : ���μ��� ���⼺
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void calcProcessing(int nPath, int nRow, boolean bCenterUpDown, ProcessDirection nDirection,
			GMosaicResultData resultData) {
		GMosaicProcData Master = new GMosaicProcData();
		GMosaicProcData Slave = new GMosaicProcData();
		int nNewPath = nPath, nNewRow = nRow; // Initialize...

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.calcProcessing :");
		}
//[DEBUG]

		try {

			if (!bCenterUpDown) {
				if (_pMosaicRegistData[nPath][nRow]._bUseChecking)
					getMosaicprcData(resultData, Master); // get master image
				else {
					getMosaicprcData(_pMosaicRegistData[nPath][nRow], Master); // get master image
					_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
				}
				if (nDirection == ProcessDirection.PROCESS_LEFTRIGHT)
					nNewRow++;
				else if (nDirection == ProcessDirection.PROCESS_TOPBOTTOM)
					nNewPath++;
			} else {
				if (_pMosaicRegistData[nPath][nRow]._bUseChecking)
					getMosaicprcData(resultData, Master); // get master image
				else {
					getMosaicprcData(_pMosaicRegistData[nPath][nRow], Master); // get master image
					_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
				}
				if (nDirection == ProcessDirection.PROCESS_LEFTRIGHT)
					nNewRow--;
				else if (nDirection == ProcessDirection.PROCESS_TOPBOTTOM)
					nNewPath--;
			}

			if (nDirection == ProcessDirection.PROCESS_LEFTRIGHT) {
//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t calcProcessing() [1] : PROCESS_LEFTRIGHT  [" + nPath + ", " + nNewRow
							+ "] _nLogicCount = " + _nLogicCount);
				}
//[DEBUG]
				getMosaicprcData(_pMosaicRegistData[nPath][nNewRow], Slave); // get slave image
				_pMosaicRegistData[nPath][nNewRow]._bUseChecking = true;
			} else if (nDirection == ProcessDirection.PROCESS_TOPBOTTOM) {
//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t calcProcessing() [2] : PROCESS_TOPBOTTOM  [" + nNewPath + ", " + nRow
							+ "] _nLogicCount = " + _nLogicCount);
				}
//[DEBUG]
				getMosaicprcData(_pMosaicRegistData[nNewPath][nRow], Slave); // get slave image
				_pMosaicRegistData[nNewPath][nRow]._bUseChecking = true;
			} else if (nDirection == ProcessDirection.PROCESS_TAB) {
//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t calcProcessing() [3] : PROCESS_TAB  [" + nPath + ", " + nRow
							+ "] _nLogicCount = " + _nLogicCount);
				}
//[DEBUG]
				getMosaicprcData(_pMosaicRegistData[nPath][nRow], Slave); // get slave image
				_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
			}

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t calcProcessing() [4] : calcMosaicProcessing  [(nPath, nRow)=(" + nPath
						+ ", " + nRow + ")][(nNewPath, nNewRow)=(" + nNewPath + ", " + nNewRow + ")] _nLogicCount = "
						+ _nLogicCount);
			}
//[DEBUG]
			calcMosaicProcessing(Master, Slave, resultData); // Create Mosaic Image

			resultData._bCheckData = true;
			_nLogicCount--;
//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t _nLogicCount = " + _nLogicCount);
			}
//[DEBUG]

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.calcProcessing : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// �ش� Master�� Slave�� ���� ������ũ�� �����Ͽ� ������ũ ��� ������ ��ȯ�Ѵ�.
	// @ Master : Master ������ũ ���� ����
	// @ Slave : Slave ������ũ ���� ����
	// @
	// @ return : GMosaicResultData outData ��ȯ�� ������ũ ��� ����
	private void calcMosaicProcessing(GMosaicProcData Master, GMosaicProcData Slave, GMosaicResultData outData) {

		calcMosaicProcessing_MemBase(Master, Slave, outData);

//		//Select Processing Method
//		if(selectProcessingMethod(Master._nDeviceIndex, Slave._nDeviceIndex) == 0)
//		{
//			//memory base processing
//			calcMosaicProcessing_MemBase(Master, Slave, outData);
//		}
//		else
//		{
//			//file base processing
//			calcMosaicProcessing_FileBase(Master, Slave, outData);
//		}

	}

	// �ش� ������ũ ��� ������ ���� ������ũ ���� ������ ��ȯ�Ѵ�.
	// @ registData : ������ũ ��� ����
	// @
	// @ return : GMosaicProcData outData ��ȯ�� ������ũ ���� ����
	// @ return : GImageProcessor.ProcessCode ���� ��� ����
	private ProcessCode getMosaicprcData(GMosaicResultData registData, GMosaicProcData outData) {
		GImageProcessor.ProcessCode ret = GImageProcessor.ProcessCode.SUCCESS;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.getMosaicprcData : _nBandIndex= " + _nBandIndex);
		}
//[DEBUG]	

		try {
			// Get Image
			////////////////////////////////////////////////////////////////
			if (registData._imgData._pImage == null) {
				GTiffDataReader gdReader = null;
				Envelope2D envelope = null;
				GridEnvelope2D gridEnvelope = null;
				byte[] pixels = null;
				int bc = 0;
				int[] gridSize = null;

				// Read the input file
				try {
					gdReader = new GTiffDataReader(registData._oFileData._strFilePath, registData._oFileData._maxBit16);
				} catch (Exception ex) {
					System.out.println("GAutoMosaic.getMosaicprcData : " + ex.toString());
					System.out.println("\t Layer Name : " + registData._oFileData._strFilePath);
					System.out.println("\t Error : " + GImageProcessor.ProcessCode.ERROR_FAIL_READ.toString());
					ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

					if (gdReader != null)
						gdReader.destory();
					gdReader = null;
					return ret;
				}

				if (!gdReader.IsOpened()) {
					System.out.println("\t Error : " + GImageProcessor.ProcessCode.ERROR_FAIL_READ.toString());
					ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;

					if (gdReader != null)
						gdReader.destory();
					gdReader = null;
					return ret;
				}

				pixels = gdReader.getAllPixelsByte();
				bc = gdReader.getBandCount();
				envelope = gdReader.getEnvelope();
				gridEnvelope = gdReader.getGridEnvelope();
				gridSize = gdReader.getGridSize();

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t File : " + registData._oFileData._strFilePath);
					System.out
							.println("[DEBUG]\t\t Min(LB) : x = " + envelope.getMinX() + ", y = " + envelope.getMinY());
					System.out
							.println("[DEBUG]\t\t Max(RT) : x = " + envelope.getMaxX() + ", y = " + envelope.getMaxY());
				}
//[DEBUG]

				// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
				registData._imgData._mbr2d.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
				registData._imgData._mbr2d.setRect(envelope.getBounds2D());
				registData._imgData._imgBox2d.setRect(gridEnvelope.getBounds2D());
				registData._imgData._dblPixelScales[0] = envelope.getWidth() / gridEnvelope.getWidth();
				registData._imgData._dblPixelScales[1] = envelope.getHeight() / gridEnvelope.getHeight();
				registData._imgData._lBandNum = bc;

				switch (bc) {
				case 1: {
					registData._imgData._pImage = pixels.clone();
				}
					break;
				case 3: {
					int nRegistDataIndex = 0, nImageInfoIndex = 0, nBand = 0;

					// R : 2, G : 1, B : 0, BandOdering : -1
					nBand = _pMosaicData._nGrayBand;
					if (nBand == -1) {
						if (_pMosaicData._nBandOderingArray != null) {
							if (_nBandIndex < _pMosaicData._nBandOderingArray.size()) {
								nBand = _pMosaicData._nBandOderingArray.get(_nBandIndex).intValue();
//[DEBUG]
								if (_IS_DEBUG) {
									System.out.println("[DEBUG]\t Layer Name : " + registData._oFileData._strFilePath);
									System.out.println("[DEBUG]\t _nBandIndex= " + _nBandIndex);
									System.out.println("[DEBUG]\t nBand= " + nBand);
								}
//[DEBUG]
							}
						}
					}
					if (nBand < 0 || nBand > 2)
						nBand = 2;

					registData._imgData._pImage = new byte[gridSize[0] * gridSize[1]];

					for (int j = 0; j < gridSize[1]; j++) {
						for (int i = 0; i < gridSize[0]; i++) {
							nRegistDataIndex = i + j * gridSize[0];
							nImageInfoIndex = (i + j * gridSize[0]) * bc + (2 - nBand); // R : 2, G : 1, B : 0
							registData._imgData._pImage[nRegistDataIndex] = pixels[nImageInfoIndex];
						}
					}
				}
					break;
				}

				if (gdReader != null)
					gdReader.destory();
				gdReader = null;
			}
			////////////////////////////////////////////////////////////////

			outData._imgData._pImage = registData._imgData._pImage;
			outData._imgData._oFileData._strFilePath = "";
			outData._imgData._oFileData._maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			outData._nDeviceIndex = 0;

			// Get Image Size
			outData._imgData._imgBox2d.setRect(registData._imgData._imgBox2d.getBounds2D());
			outData._imgData._dblPixelScales[0] = registData._imgData._dblPixelScales[0];
			outData._imgData._dblPixelScales[1] = registData._imgData._dblPixelScales[1];
			outData._imgData._lBandNum = registData._imgData._lBandNum;

			// Get Scene path and row
			outData._nScenePath = registData._nScenePath;
			outData._nSceneRow = registData._nSceneRow;

			// Get UTM Coord
			// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
			outData._imgData._mbr2d
					.setCoordinateReferenceSystem(registData._imgData._mbr2d.getCoordinateReferenceSystem());
			outData._imgData._mbr2d.setRect(registData._imgData._mbr2d.getBounds2D());

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t registData : ");
				System.out.println("[DEBUG]\t\t Min(LB) : x = " + registData._imgData._mbr2d.getMinX() + ", y = "
						+ registData._imgData._mbr2d.getMinY());
				System.out.println("[DEBUG]\t\t Max(RT) : x = " + registData._imgData._mbr2d.getMaxX() + ", y = "
						+ registData._imgData._mbr2d.getMaxY());
				System.out.println("[DEBUG]\t outData : ");
				System.out.println("[DEBUG]\t\t Min(LB) : x = " + outData._imgData._mbr2d.getMinX() + ", y = "
						+ outData._imgData._mbr2d.getMinY());
				System.out.println("[DEBUG]\t\t Max(RT) : x = " + outData._imgData._mbr2d.getMaxX() + ", y = "
						+ outData._imgData._mbr2d.getMaxY());
			}
//[DEBUG]

		} catch (Exception ex) {
			System.out.println("GAutoMosaic.getMosaicprcData : " + ex.toString());
			ex.printStackTrace();

			ret = GImageProcessor.ProcessCode.ERROR_FAIL_PROCESS;
		}

		return ret;
	}

	// Center, Up, Down�� Gain(ȭ�� �л갪)�� �������� Sub Master�� Path ���⼺�� ��ȯ�Ѵ�.
	// @ dblGainC
	// @ dblGainU
	// @ dblGainD
	// @
	// @ return : PathDirection Path ���⼺
	private PathDirection getSubMasterPath(double dblGainC, double dblGainU, double dblGainD) {
		// 0 : Center, 1 : Up, 2 : Down
		PathDirection ret = PathDirection.PATH_CENTER;

		if (dblGainC == 0 && dblGainU != 0 && dblGainD != 0) {
			if (Math.abs(1.0 - dblGainU) < Math.abs(1.0 - dblGainD))
				ret = PathDirection.PATH_UP; // ret = 1;
			else if (Math.abs(1.0 - dblGainD) < Math.abs(1.0 - dblGainU))
				ret = PathDirection.PATH_DOWN; // ret = 2;
		} else if (dblGainC != 0 && dblGainU == 0 && dblGainD != 0) {
			if (Math.abs(1.0 - dblGainC) < Math.abs(1.0 - dblGainD))
				ret = PathDirection.PATH_CENTER; // ret = 0;
			else if (Math.abs(1.0 - dblGainD) < Math.abs(1.0 - dblGainC))
				ret = PathDirection.PATH_DOWN; // ret = 2;
		} else if (dblGainC != 0 && dblGainU != 0 && dblGainD == 0) {
			if (Math.abs(1.0 - dblGainC) < Math.abs(1.0 - dblGainU))
				ret = PathDirection.PATH_CENTER; // ret = 0;
			else if (Math.abs(1.0 - dblGainU) < Math.abs(1.0 - dblGainC))
				ret = PathDirection.PATH_UP; // ret = 1;
		} else if (dblGainC != 0 && dblGainU == 0 && dblGainD == 0) {
			ret = PathDirection.PATH_CENTER; // ret = 0;
		} else if (dblGainC == 0 && dblGainU != 0 && dblGainD == 0) {
			ret = PathDirection.PATH_UP; // ret = 1;
		} else if (dblGainC == 0 && dblGainU == 0 && dblGainD != 0) {
			ret = PathDirection.PATH_DOWN; // ret = 2;
		} else {
			if ((Math.abs(1.0 - dblGainC) < Math.abs(1.0 - dblGainU))
					&& (Math.abs(1.0 - dblGainC) < Math.abs(1.0 - dblGainD)))
				ret = PathDirection.PATH_CENTER; // ret = 0;
			else if ((Math.abs(1.0 - dblGainU) < Math.abs(1.0 - dblGainC))
					&& (Math.abs(1.0 - dblGainU) < Math.abs(1.0 - dblGainD)))
				ret = PathDirection.PATH_UP; // ret = 1;
			else if ((Math.abs(1.0 - dblGainD) < Math.abs(1.0 - dblGainC))
					&& (Math.abs(1.0 - dblGainD) < Math.abs(1.0 - dblGainU)))
				ret = PathDirection.PATH_DOWN; // ret = 2;
		}

		return ret;
	}

	// ������ũ ��� ������ �ʱ�ȭ�Ѵ�.
	// @ mosaicResultData : �ʱ�ȭ�� ������ũ ��� ����
	// @ bCheckMaster : Master Ȯ�� ����
	private void initResultData(GMosaicResultData mosaicResultData, boolean bCheckMaster) {
		mosaicResultData._bCheckMaster = bCheckMaster;
		mosaicResultData._bCheckData = false;
		mosaicResultData._bUseChecking = false;
	}

	// ���μ��� �޸� ������ ��ȯ�Ѵ�.
	// @ nMasterDevice : Master �޸� ����
	// @ nSlaveDevice : Slave �޸� ����
	// @
	// @ return : int ���μ��� �޸� ����
	private int selectProcessingMethod(int nMasterDevice, int nSlaveDevice) {
		// This function select processing method for mosaicking
		// memory base processing : return number==0
		// file base processing : return number==1
		if (nMasterDevice == 0 && nSlaveDevice == 0)
			return 0;
		else
			return 1;

	}

	// �ش� Master, Slave�� ���� ������ũ�� �����Ͽ� ������ũ ���� ��� ������ ��ȯ�Ѵ�.
	// @ Master : Master ������ũ ���μ��� ����
	// @ Slave : Slave ������ũ ���μ��� ����
	// @
	// @ return : GMosaicResultData outData ��ȯ�� ������ũ ���� ��� ����
	private void calcMosaicProcessing_MemBase(GMosaicProcData Master, GMosaicProcData Slave,
			GMosaicResultData outData) {
		_dblPixelScales[0] = _pMosaicData._dblPixelScales[0];
		_dblPixelScales[1] = _pMosaicData._dblPixelScales[1];
//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.calcMosaicProcessing_MemBase :");
		}
//[DEBUG]	

		// Get Overlap Area
		GMosaicProcData ovMaster = getOverlapArea(true, Master, Slave); // master
		GMosaicProcData ovSlave = getOverlapArea(false, Master, Slave); // slave

		// Get Overlap Image
		getOverlapImage(Master, true, ovMaster); // master
		getOverlapImage(Slave, false, ovSlave); // slave

		// Area Determination
		areaDetermination(Master, Slave, ovMaster, ovSlave);
//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Before OnSeamline : Master = Mbr2d LB(" + Master._imgData._mbr2d.getMinX()
					+ ", " + Master._imgData._mbr2d.getMinY() + ")" + " ~ RT(" + Master._imgData._mbr2d.getMaxX() + ", "
					+ Master._imgData._mbr2d.getMaxY() + ")");
			System.out.println("[DEBUG]\t Before OnSeamline : Slave = Mbr2d LB(" + Slave._imgData._mbr2d.getMinX()
					+ ", " + Slave._imgData._mbr2d.getMinY() + ")" + " ~ RT(" + Slave._imgData._mbr2d.getMaxX() + ", "
					+ Slave._imgData._mbr2d.getMaxY() + ")");
			System.out.println("[DEBUG]\t Before OnSeamline : Overlap(Master) = Mbr2d LB("
					+ ovMaster._imgData._mbr2d.getMinX() + ", " + ovMaster._imgData._mbr2d.getMinY() + ")" + " ~ RT("
					+ ovMaster._imgData._mbr2d.getMaxX() + ", " + ovMaster._imgData._mbr2d.getMaxY() + ")");
			System.out.println("[DEBUG]\t Before OnSeamline : Overlap(Slave) = Mbr2d LB("
					+ ovSlave._imgData._mbr2d.getMinX() + ", " + ovSlave._imgData._mbr2d.getMinY() + ")" + " ~ RT("
					+ ovSlave._imgData._mbr2d.getMaxX() + ", " + ovSlave._imgData._mbr2d.getMaxY() + ")");
			System.out.println("[DEBUG]\t Before OnSeamline : Master = ImgBox2d (w, h) : ("
					+ Master._imgData._imgBox2d.getWidth() + ", " + Master._imgData._imgBox2d.getHeight() + ")");
			System.out.println("[DEBUG]\t Before OnSeamline : Slave = ImgBox2d (w, h) : "
					+ Slave._imgData._imgBox2d.getWidth() + ", " + Slave._imgData._imgBox2d.getHeight() + ")");
			System.out.println("[DEBUG]\t Before OnSeamline : Overlap(Master) = ImgBox2d (w, h) : ("
					+ ovMaster._imgData._imgBox2d.getWidth() + ", " + ovMaster._imgData._imgBox2d.getHeight() + ")");
			System.out.println("[DEBUG]\t Before OnSeamline : Overlap(Master) = ImgBox2d (w, h) : ("
					+ ovSlave._imgData._imgBox2d.getWidth() + ", " + ovSlave._imgData._imgBox2d.getHeight() + ")");
		}
//[DEBUG]
		////////////////////////////////////////////////////////////////////////////////////////////

		// Seamline
		GSeamline seam = new GSeamline();
		int[] lSeamPoint = null;
		GSeamline.SeamlineMethod nTmpSeamLineMethod = GSeamline.SeamlineMethod.MINIMUM_DIFFERENCE_SUM;
		if (_nBandIndex != 0 && !_pMosaicAlgorithmData._strSeamPoint.isEmpty()) {
			nTmpSeamLineMethod = _pMosaicAlgorithmData._nSeamLineMethod;
			_pMosaicAlgorithmData._nSeamLineMethod = GSeamline.SeamlineMethod.USE_EXIST_SEAMLINE;
		}

		lSeamPoint = seam.procSeamline(_pMosaicAlgorithmData, ovMaster, ovSlave);
		if (nTmpSeamLineMethod != GSeamline.SeamlineMethod.MINIMUM_DIFFERENCE_SUM)
			_pMosaicAlgorithmData._nSeamLineMethod = nTmpSeamLineMethod;

		//////////////////////////////////////////////////////////////////////

		// Histogram Equalization
		GHistogramMatching histMatching = new GHistogramMatching();
		GMosaicHistogramData historamData;

		historamData = histMatching.procHistogramMatching(_pMosaicAlgorithmData._nHistogramMatchingMethod,
				_pMosaicAlgorithmData._bFeatherWidth, _pMosaicAlgorithmData._lFeatherWidth,
				_pMosaicAlgorithmData._lSearchWidth, ovMaster, ovSlave, Master._imgData._mbr2d, Slave._imgData._mbr2d,
				lSeamPoint);
//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println(
					"[DEBUG]\t historamData - (w, h) = (" + historamData._lWidth + ", " + historamData._lHeight + ")");
		}
//[DEBUG]

		// {delete dymamic memory
		ovMaster._imgData._pImage = null;
		ovSlave._imgData._pImage = null;

		lSeamPoint = null;
		// }

		// Check Result Image Size
		// int result_H = (int)(Master._imgData._imgBox2d.getHeight() +
		// Slave._imgData._imgBox2d.getHeight()) - historamData._lHeight;
		// int result_W = (int)(Master._imgData._imgBox2d.getWidth() +
		// Slave._imgData._imgBox2d.getWidth()) - historamData._lWidth;
		// int result_size = (int)Math.round((double)result_H * (double)result_W /
		// 1024000.0); //Mega-Bytes

		// Create Mosaic image
		createMemoryMosaic_MemBase(Master, Slave, historamData, outData);

		if (historamData._pImage != null)
			historamData._pImage = null;
		if (historamData._histMatchInfo._nLUTModified != null)
			historamData._histMatchInfo._nLUTModified = null;

		Master._imgData._pImage = null;
		Slave._imgData._pImage = null;
		/////////////////////////////////////////////////////////////
	}

	// Master�� Slave�� ��ø ������ ��ȯ�Ѵ�.
	// @ bMaster : Master ����
	// @ Master : Master ������ũ ���μ��� ����
	// @ Slave : Slave ������ũ ���μ��� ����
	// @
	// @ return : GMosaicProcData ��ø ���� ������ũ ���μ��� ����
	GMosaicProcData getOverlapArea(boolean bMaster, GMosaicProcData Master, GMosaicProcData Slave) {
		GMosaicProcData overlap = new GMosaicProcData();
		Rectangle2D overlapRect = Master._imgData._mbr2d.createIntersection(Slave._imgData._mbr2d.getBounds2D());

		// Get UTM Coordinates of Overlap Area
		overlap._imgData._dblPixelScales[0] = Math.min(Master._imgData._dblPixelScales[0],
				Slave._imgData._dblPixelScales[0]);
		overlap._imgData._dblPixelScales[1] = Math.min(Master._imgData._dblPixelScales[1],
				Slave._imgData._dblPixelScales[1]);
		overlap._imgData._lBandNum = Math.min(Master._imgData._lBandNum, Slave._imgData._lBandNum);

		// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
		overlap._imgData._mbr2d.setCoordinateReferenceSystem(Master._imgData._mbr2d.getCoordinateReferenceSystem());
		overlap._imgData._mbr2d.setRect(overlapRect);

		int nOverlapLTX, nOverlapLTY, nOverlapRBX, nOverlapRBY;
		nOverlapLTX = (int) (overlap._imgData._mbr2d.getMinX() / (double) _dblPixelScales[0]);
		nOverlapLTY = (int) (overlap._imgData._mbr2d.getMaxY() / (double) _dblPixelScales[1]);
		nOverlapRBX = (int) (overlap._imgData._mbr2d.getMaxX() / (double) _dblPixelScales[0]);
		nOverlapRBY = (int) (overlap._imgData._mbr2d.getMinY() / (double) _dblPixelScales[1]);

		int nMasterLTX, nMasterLTY, nSlaveLTX, nSlaveLTY;
		nMasterLTX = (int) (Master._imgData._mbr2d.getMinX() / (double) _dblPixelScales[0]);
		nMasterLTY = (int) (Master._imgData._mbr2d.getMaxY() / (double) _dblPixelScales[1]);
		nSlaveLTX = (int) (Slave._imgData._mbr2d.getMinX() / (double) _dblPixelScales[0]);
		nSlaveLTY = (int) (Slave._imgData._mbr2d.getMaxY() / (double) _dblPixelScales[1]);

		// Get Height & Width of Overlap Area
		overlap._imgData._imgBox2d.setBounds(0, 0, nOverlapRBX - nOverlapLTX, nOverlapLTY - nOverlapRBY);

		if (bMaster) // Get Overlap Area of Master(pixel coord)
		{
			overlap._imgData._lImgUL.x = nOverlapLTX - nMasterLTX;
			overlap._imgData._lImgUL.y = nMasterLTY - nOverlapLTY;

			if ((Master._imgData._mbr2d.getMinX() >= overlap._imgData._mbr2d.getMinX())
					|| (Master._imgData._mbr2d.getMaxX() <= overlap._imgData._mbr2d.getMaxX())) {
				if (overlap._imgData._lImgUL.x != 0 && overlap._imgData._lImgUL.x != (int) Math
						.abs(Master._imgData._imgBox2d.getWidth() - overlap._imgData._imgBox2d.getWidth()))
					overlap._imgData._lImgUL.x = (int) Math
							.abs(Master._imgData._imgBox2d.getWidth() - overlap._imgData._imgBox2d.getWidth());
			}

			if ((Master._imgData._mbr2d.getMaxY() <= overlap._imgData._mbr2d.getMaxY())
					|| (Master._imgData._mbr2d.getMinY() >= overlap._imgData._mbr2d.getMinY())) {
				if (overlap._imgData._lImgUL.y != 0 && Master._imgData._imgBox2d
						.getHeight() != overlap._imgData._imgBox2d.getHeight() + overlap._imgData._lImgUL.y)
					overlap._imgData._lImgUL.y = Master._imgData._imgBox2d.getHeight()
							- overlap._imgData._imgBox2d.getHeight();
			}

			overlap._nScenePath = Master._nScenePath;
			overlap._nSceneRow = Master._nSceneRow;
		} else // Get Overlap Area of Slave(pixel coord)
		{
			overlap._imgData._lImgUL.x = nOverlapLTX - nSlaveLTX;
			overlap._imgData._lImgUL.y = nSlaveLTY - nOverlapLTY;

			if ((Slave._imgData._mbr2d.getMinX() >= overlap._imgData._mbr2d.getMinX())
					|| (Slave._imgData._mbr2d.getMaxX() <= overlap._imgData._mbr2d.getMaxX())) {
				if (overlap._imgData._lImgUL.x != 0 && overlap._imgData._lImgUL.x != (int) Math
						.abs(Slave._imgData._imgBox2d.getWidth() - overlap._imgData._imgBox2d.getWidth()))
					overlap._imgData._lImgUL.x = (int) Math
							.abs(Slave._imgData._imgBox2d.getWidth() - overlap._imgData._imgBox2d.getWidth());
			}

			if ((Slave._imgData._mbr2d.getMaxY() <= overlap._imgData._mbr2d.getMaxY())
					|| (Slave._imgData._mbr2d.getMinY() >= overlap._imgData._mbr2d.getMinY())) {
				if (overlap._imgData._lImgUL.y != 0 && Slave._imgData._imgBox2d
						.getHeight() != overlap._imgData._imgBox2d.getHeight() + overlap._imgData._lImgUL.y)
					overlap._imgData._lImgUL.y = Slave._imgData._imgBox2d.getHeight()
							- overlap._imgData._imgBox2d.getHeight();
			}

			overlap._nScenePath = Slave._nScenePath;
			overlap._nSceneRow = Slave._nSceneRow;
		}

		return overlap;
	}

	// �ش� ������ũ ���μ��� ������ ���� ��ø���� ������ũ ���μ��� ������ ȭ�� ������
	// ��ȯ�Ѵ�.
	// @ Data : ������ũ ���μ��� ����
	// @ bMaster : Master ����
	// @
	// @ return : GMosaicProcData ovData ��ȯ�� ��ø���� ������ũ ���μ��� ����
	private void getOverlapImage(GMosaicProcData Data, boolean bMaster, GMosaicProcData ovData) {
		ovData._imgData._dblPixelScales[0] = Data._imgData._dblPixelScales[0];
		ovData._imgData._dblPixelScales[1] = Data._imgData._dblPixelScales[1];
		ovData._imgData._lBandNum = Data._imgData._lBandNum;

		try {

			// if(Data._nDeviceIndex==0) //memory
			{
				int width = (int) ovData._imgData._imgBox2d.getWidth();
				int height = (int) ovData._imgData._imgBox2d.getHeight();
				int ovIdx = 0, dataIdx = 0;

				ovData._nDeviceIndex = 0;
				ovData._imgData._pImage = new byte[width * height];

				for (int j = 0; j < height; j++) {
					for (int i = 0; i < width; i++) {
						ovIdx = i + j * width;
						dataIdx = (i + (int) ovData._imgData._lImgUL.x)
								+ (int) Data._imgData._imgBox2d.getWidth() * (j + (int) ovData._imgData._lImgUL.y);

						ovData._imgData._pImage[ovIdx] = Data._imgData._pImage[dataIdx];
					}
				}
			} // end memory

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.getOverlapImage : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// �ش� Master, Slave ������ũ ���μ��� ������ ��ø ������ũ ���μ��� ������ ���� ��ø
	// ���� ������ �����Ѵ�.
	// @ Master : Master ������ũ ���μ��� ����
	// @ Slave : Slave ������ũ ���μ��� ����
	// @ ovMaster : ��ø Master ������ũ ���μ��� ����
	// @ ovSlave : ��ø Slave ������ũ ���μ��� ����
	private void areaDetermination(GMosaicProcData Master, GMosaicProcData Slave, GMosaicProcData ovMaster,
			GMosaicProcData ovSlave) {
		int i = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.areaDetermination");
		}
//[DEBUG]

		try {

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t Master : " + Master._imgData._oFileData._strFilePath);
				System.out.println("[DEBUG]\t\t Min(LB) : x = " + Master._imgData._mbr2d.getMinX() + ", y = "
						+ Master._imgData._mbr2d.getMinY());
				System.out.println("[DEBUG]\t\t Max(RT) : x = " + Master._imgData._mbr2d.getMaxX() + ", y = "
						+ Master._imgData._mbr2d.getMaxY());
				System.out.println("[DEBUG]\t Slave : " + Slave._imgData._oFileData._strFilePath);
				System.out.println("[DEBUG]\t\t Min(LB) : x = " + Slave._imgData._mbr2d.getMinX() + ", y = "
						+ Slave._imgData._mbr2d.getMinY());
				System.out.println("[DEBUG]\t\t Max(RT) : x = " + Slave._imgData._mbr2d.getMaxX() + ", y = "
						+ Slave._imgData._mbr2d.getMaxY());
				System.out.println("[DEBUG]\t ovMaster : ");
				System.out.println("[DEBUG]\t\t Min(LB) : x = " + ovMaster._imgData._mbr2d.getMinX() + ", y = "
						+ ovMaster._imgData._mbr2d.getMinY());
				System.out.println("[DEBUG]\t\t Max(RT) : x = " + ovMaster._imgData._mbr2d.getMaxX() + ", y = "
						+ ovMaster._imgData._mbr2d.getMaxY());
			}
//[DEBUG]	

			/* ######## Determine Corner Coordinates ######## */
			_point[0].x = Math.min(Master._imgData._mbr2d.getMinX(), Slave._imgData._mbr2d.getMinX()); // LT
			_point[0].y = Math.max(Master._imgData._mbr2d.getMaxY(), Slave._imgData._mbr2d.getMaxY());

			_point[5].x = ovMaster._imgData._mbr2d.getMinX();
			_point[5].y = ovMaster._imgData._mbr2d.getMaxY();

			_point[10].x = ovMaster._imgData._mbr2d.getMaxX();
			_point[10].y = ovMaster._imgData._mbr2d.getMinY();

			_point[15].x = Math.max(Master._imgData._mbr2d.getMaxX(), Slave._imgData._mbr2d.getMaxX()); // RB
			_point[15].y = Math.min(Master._imgData._mbr2d.getMinY(), Slave._imgData._mbr2d.getMinY());

//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]\t\t _point[0] : x = " + _point[0].x + ", y = " + _point[0].y);
				System.out.println("[DEBUG]\t\t _point[5] : x = " + _point[5].x + ", y = " + _point[5].y);
				System.out.println("[DEBUG]\t\t _point[10] : x = " + _point[10].x + ", y = " + _point[10].y);
				System.out.println("[DEBUG]\t\t _point[15] : x = " + _point[15].x + ", y = " + _point[15].y);
			}
//[DEBUG]	

			for (i = 0; i < 4; i++) {
				_point[4 * i + 0].x = _point[0].x;
				_point[4 * i + 1].x = _point[5].x;
				_point[4 * i + 2].x = _point[10].x;
				_point[4 * i + 3].x = _point[15].x;

				_point[0 + i].y = _point[0].y;
				_point[4 + i].y = _point[5].y;
				_point[8 + i].y = _point[10].y;
				_point[12 + i].y = _point[15].y;
			}

			/*
			 * ###### Determine center coordinate of each area and size of sub-image ######
			 */
			for (i = 0; i < 3; i++) {
				_subArea[i]._posCenterUtm.x = _point[i].x + ((_point[i + 1].x - _point[i].x) / 2.0);
				_subArea[i]._posCenterUtm.y = _point[i].y - ((_point[i].y - _point[i + 4].y) / 2.0);
				_subArea[i]._posULUtm.x = _point[i].x;
				_subArea[i]._posULUtm.y = _point[i].y;

				_subArea[i]._lNumCol = (int) (_point[i + 1].x / (double) _dblPixelScales[0])
						- (int) (_point[i].x / (double) _dblPixelScales[0]);
				_subArea[i]._lNumRow = (int) (_point[i].y / (double) _dblPixelScales[1])
						- (int) (_point[i + 4].y / (double) _dblPixelScales[1]);
			}

			for (i = 3; i < 6; i++) {
				_subArea[i]._posCenterUtm.x = _point[i + 1].x + ((_point[i + 2].x - _point[i + 1].x) / 2.0);
				_subArea[i]._posCenterUtm.y = _point[i + 1].y - ((_point[i + 1].y - _point[i + 5].y) / 2.0);
				_subArea[i]._posULUtm.x = _point[i + 1].x;
				_subArea[i]._posULUtm.y = _point[i + 1].y;

				_subArea[i]._lNumCol = (int) (_point[i + 2].x / (double) _dblPixelScales[0])
						- (int) (_point[i + 1].x / (double) _dblPixelScales[0]);
				_subArea[i]._lNumRow = (int) (_point[i + 1].y / (double) _dblPixelScales[1])
						- (int) (_point[i + 5].y / (double) _dblPixelScales[1]);
			}

			for (i = 6; i < 9; i++) {
				_subArea[i]._posCenterUtm.x = _point[i + 2].x + ((_point[i + 3].x - _point[i + 2].x) / 2.0);
				_subArea[i]._posCenterUtm.y = _point[i + 2].y - ((_point[i + 2].y - _point[i + 6].y) / 2.0);
				_subArea[i]._posULUtm.x = _point[i + 2].x;
				_subArea[i]._posULUtm.y = _point[i + 2].y;

				_subArea[i]._lNumCol = (int) (_point[i + 3].x / (double) _dblPixelScales[0])
						- (int) (_point[i + 2].x / (double) _dblPixelScales[0]);
				_subArea[i]._lNumRow = (int) (_point[i + 2].y / (double) _dblPixelScales[1])
						- (int) (_point[i + 6].y / (double) _dblPixelScales[1]);
			}

			int calc_W, calc_H, result_W, result_H;
			calc_W = _subArea[0]._lNumCol + _subArea[1]._lNumCol + _subArea[2]._lNumCol;
			calc_H = _subArea[0]._lNumRow + _subArea[3]._lNumRow + _subArea[6]._lNumRow;
			result_W = (int) (Master._imgData._imgBox2d.getWidth() + Slave._imgData._imgBox2d.getWidth()
					- ovMaster._imgData._imgBox2d.getWidth());
			result_H = (int) (Master._imgData._imgBox2d.getHeight() + Slave._imgData._imgBox2d.getHeight()
					- ovMaster._imgData._imgBox2d.getHeight());

			if (calc_W != result_W) {
				for (i = 0; i < 3; i++) {
					_subArea[3 * i + 2]._lNumCol = result_W - (_subArea[0]._lNumCol + _subArea[1]._lNumCol);
				}
			}

			if (calc_H != result_H) {
				for (i = 0; i < 3; i++) {
					_subArea[i + 6]._lNumRow = result_H - (_subArea[0]._lNumRow + _subArea[3]._lNumRow);
				}
			}

			/* ##### Determine image type covering sub-area ##### */
			// Area Index-> 0 : None 1 : Master Image 2 : Slave Image
			int nIndex = 0;
			for (i = 0; i < 9; i++) {
				if (((_subArea[i]._posCenterUtm.x > Master._imgData._mbr2d.getMinX())
						&& (_subArea[i]._posCenterUtm.x < Master._imgData._mbr2d.getMaxX()))
						&& ((_subArea[i]._posCenterUtm.y < Master._imgData._mbr2d.getMaxY())
								&& (_subArea[i]._posCenterUtm.y > Master._imgData._mbr2d.getMinY()))) {
					nIndex += 1;
				}

				if (((_subArea[i]._posCenterUtm.x > Slave._imgData._mbr2d.getMinX())
						&& (_subArea[i]._posCenterUtm.x < Slave._imgData._mbr2d.getMaxX()))
						&& ((_subArea[i]._posCenterUtm.y < Slave._imgData._mbr2d.getMaxY())
								&& (_subArea[i]._posCenterUtm.y > Slave._imgData._mbr2d.getMinY()))) {
					nIndex -= 1;
				}

				if (nIndex > 0)
					_subArea[i]._nAreaIndex = GAutoMosaic.AreaImageType.AREA_MASTER.getType();
				else if (nIndex == 0)
					_subArea[i]._nAreaIndex = GAutoMosaic.AreaImageType.AREA_NONE.getType();
				else if (nIndex < 0)
					_subArea[i]._nAreaIndex = GAutoMosaic.AreaImageType.AREA_SLAVE.getType();

				nIndex = 0;
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.areaDetermination : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return;
	}

	// ?????????????????????????????????????????????????????????????????????????????

	// ������ũ �޸𸮸� �����Ѵ�.
	// @ Master : Master ������ũ ���μ��� ����
	// @ Slave : Slave ������ũ ���μ��� ����
	// @ histData : ������ũ ������׷� ����
	// @
	// @ result : GMosaicResultData result ������ũ ��� ����
	private void createMemoryMosaic_MemBase(GMosaicProcData Master, GMosaicProcData Slave,
			GMosaicHistogramData histData, GMosaicResultData result) {
		byte Matched_DN;
		int lStartCol, lStartRow, lStartColSub, lStartRowSub;
		int OverlapHeight, OverlapWidth;
		int nAreaID;
		int i = 0, j = 0;
		int width = 0, height = 0;

		// Set device of result data
		result._nDeviceIndex = 0; // memory base

		// Get Overlap Image Data
		OverlapHeight = histData._lHeight;
		OverlapWidth = histData._lWidth;

		// Get Result Image Size
		width = (int) (Master._imgData._imgBox2d.getWidth() + Slave._imgData._imgBox2d.getWidth() - OverlapWidth);
		height = (int) (Master._imgData._imgBox2d.getHeight() + Slave._imgData._imgBox2d.getHeight() - OverlapHeight);
		result._imgData._imgBox2d.setBounds(0, 0, width, height);

		// Create Image Memory
		result._imgData._pImage = new byte[width * height];

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GAutoMosaic.createMemoryMosaic_MemBase : Create pImage!! ||");
		}
//[DEBUG]		

		//////////////////////////////////////////////////////////
		int totRow = 0, cntRow = 0;
		int m = 0, n = 0;
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				nAreaID = 3 * i + j;
				totRow += _subArea[nAreaID]._lNumRow;
			}
		}
		if (totRow < 1)
			totRow = 1;
		//////////////////////////////////////////////////////////

		try {

			//////////////////////////////
			// Result Image Creation
			lStartRow = 0;
			for (i = 0; i < 3; i++) {
				if (i != 0)
					lStartRow += _subArea[3 * (i - 1)]._lNumRow;

				lStartCol = 0; // Result Image�� ������ǥ ����
				for (j = 0; j < 3; j++) {
					if (j != 0)
						lStartCol += _subArea[(j - 1)]._lNumCol;

					nAreaID = 3 * i + j;

					if ((_subArea[nAreaID]._nAreaIndex == AreaImageType.AREA_NONE.getType()) && (nAreaID != 4)) // Master
																												// or
																												// Slave
					// Image�� ����
					// ��ġ�� �ʴ�
					// ����
					{
						for (m = 0; m < _subArea[nAreaID]._lNumRow; m++) {
							cntRow++;

							// --> ������ memset�� 0x00���� ó��
							for (n = 0; n < _subArea[nAreaID]._lNumCol; n++) {
								result._imgData._pImage[(lStartCol + n) + width * (lStartRow + m)] = (byte) 0;
							}
						}
					} else if (nAreaID == 4) // Overlap Area
					{
						for (m = 0; m < _subArea[nAreaID]._lNumRow; m++) {
							cntRow++;

							// for(n=0; n<m_subArea[nAreaID].lNumCol; n++)
							// {
							// //if( (m < OverlapHeight) && (n < OverlapWidth) )
							// // result.imgData.pImage[(lStartCol+n)+result.imgData.lWidth*(lStartRow+m)] =
							// histData.pImage[n+OverlapWidth*m];
							// }
							// --> SetData(m_hMem, lStartCol+result.imgData.lWidth*(lStartRow+m),
							// &histData.pImage[OverlapWidth*m], m_subArea[nAreaID].lNumCol);
							for (n = 0; n < _subArea[nAreaID]._lNumCol; n++) {
								// @todo : ����ó�� (IndexOutOfBoundsException)
								if ((m < OverlapHeight) && (n < OverlapWidth))
									result._imgData._pImage[(lStartCol + width * (lStartRow + m))
											+ n] = histData._pImage[(OverlapWidth * m) + n];
							}
						}
					} else if (_subArea[nAreaID]._nAreaIndex == AreaImageType.AREA_MASTER.getType()) // Master Image��
																										// ��ġ�� ����
					{
						lStartColSub = (int) ((double) (_subArea[nAreaID]._posULUtm.x) / (double) _dblPixelScales[0])
								- (int) ((double) (Master._imgData._mbr2d.getMinX()) / (double) _dblPixelScales[0]);

						if (lStartColSub < 0)
							lStartColSub = 0;

						lStartRowSub = (int) ((double) (Master._imgData._mbr2d.getMaxY()) / (double) _dblPixelScales[1])
								- (int) ((double) (_subArea[nAreaID]._posULUtm.y) / (double) _dblPixelScales[1]);

						if (lStartRowSub < 0)
							lStartRowSub = 0;

						for (m = 0; m < _subArea[nAreaID]._lNumRow; m++) {
							cntRow++;

							// for(n=0; n<m_subArea[nAreaID].lNumCol; n++)
							// {
							// if( (m+lStartRowSub < Master.imgData.lHeight) && (n+lStartColSub <
							// Master.imgData.lWidth) )
							// result.imgData.pImage[(lStartCol+n)+result.imgData.lWidth*(lStartRow+m)] =
							// Master.imgData.pImage[((lStartColSub+n)*Master.imgData.lBandNum+nBand)+Master.imgData.lWidth*(lStartRowSub+m)];
							// }
							// --> SetData(m_hMem, lStartCol+result.imgData.lWidth*(lStartRow+m),
							// &Master.imgData.pImage[lStartColSub+Master.imgData.lWidth*(lStartRowSub+m)],
							// m_subArea[nAreaID].lNumCol);
							for (n = 0; n < _subArea[nAreaID]._lNumCol; n++) {
								// @todo : ����ó�� (IndexOutOfBoundsException
								if ((m + lStartRowSub < (int) Master._imgData._imgBox2d.getHeight())
										&& (n + lStartColSub < (int) Master._imgData._imgBox2d.getWidth()))
									result._imgData._pImage[(lStartCol + width * (lStartRow + m))
											+ n] = Master._imgData._pImage[(lStartColSub
													+ (int) Master._imgData._imgBox2d.getWidth() * (lStartRowSub + m))
													+ n];
							}
						}
					}

					else if (_subArea[nAreaID]._nAreaIndex == AreaImageType.AREA_SLAVE.getType()) // Slave Image�� ��ġ��
																									// �κ�
					{
						lStartColSub = (int) ((double) (_subArea[nAreaID]._posULUtm.x) / (double) _dblPixelScales[0])
								- (int) ((double) (Slave._imgData._mbr2d.getMinX()) / (double) _dblPixelScales[0]);

						if (lStartColSub < 0)
							lStartColSub = 0;

						lStartRowSub = (int) ((double) (Slave._imgData._mbr2d.getMaxY()) / (double) _dblPixelScales[1])
								- (int) ((double) (_subArea[nAreaID]._posULUtm.y) / (double) _dblPixelScales[1]);

						if (lStartRowSub < 0)
							lStartRowSub = 0;

						byte[] pBuffer = new byte[_subArea[nAreaID]._lNumCol];
						for (m = 0; m < _subArea[nAreaID]._lNumRow; m++) {
							cntRow++;

							for (n = 0; n < _subArea[nAreaID]._lNumCol; n++) {
								if ((m + lStartRowSub < (int) Slave._imgData._imgBox2d.getHeight())
										&& (n + lStartColSub < (int) Slave._imgData._imgBox2d.getWidth())) {
									if (Slave._imgData._pImage[(lStartColSub + n)
											+ (int) Slave._imgData._imgBox2d.getWidth() * (lStartRowSub + m)] == 0)
										pBuffer[n] = (byte) 0;
									else if (Slave._imgData._pImage[(lStartColSub + n)
											+ (int) Slave._imgData._imgBox2d.getWidth() * (lStartRowSub + m)] != 0) {
										Matched_DN = getMatchedDN(Slave._imgData._pImage[lStartColSub + n
												+ (int) Slave._imgData._imgBox2d.getWidth() * (lStartRowSub + m)],
												histData);
										pBuffer[n] = Matched_DN;
									}
								}
							}

							// --> SetData(m_hMem, lStartCol+result.imgData.lWidth*(lStartRow+m), pBuffer,
							// _subArea[nAreaID]._lNumCol);
							for (n = 0; n < _subArea[nAreaID]._lNumCol; n++) {
								result._imgData._pImage[(lStartCol + width * (lStartRow + m)) + n] = pBuffer[n];
							}
						}
						pBuffer = null;
					}
				} // End of For(j)
			} // End of For(i)

		} catch (IndexOutOfBoundsException iobe) {
			System.out
					.println("GAutoMosaic.createMemoryMosaic_MemBase : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		cntRow++;

		// { Coordinate Informations for GeoTiff Image Creation
		// LT : _point[0], RB : _point[15]
		// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
		result._imgData._mbr2d.setCoordinateReferenceSystem(Master._imgData._mbr2d.getCoordinateReferenceSystem());
		result._imgData._mbr2d.setRect(_point[0].x, _point[15].y, Math.abs(_point[15].x - _point[0].x),
				Math.abs(_point[0].y - _point[15].y));

		result._nScenePath = Master._nScenePath;
		result._nSceneRow = Master._nSceneRow;
		result._imgData._dblPixelScales[0] = _dblPixelScales[0];
		result._imgData._dblPixelScales[1] = _dblPixelScales[1];
		// }

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t Master : " + Master._imgData._oFileData._strFilePath);
			System.out.println("[DEBUG]\t\t Min(LB) : x = " + Master._imgData._mbr2d.getMinX() + ", y = "
					+ Master._imgData._mbr2d.getMinY());
			System.out.println("[DEBUG]\t\t Max(RT) : x = " + Master._imgData._mbr2d.getMaxX() + ", y = "
					+ Master._imgData._mbr2d.getMaxY());
			System.out.println("[DEBUG]\t Slave : " + Slave._imgData._oFileData._strFilePath);
			System.out.println("[DEBUG]\t\t Min(LB) : x = " + Slave._imgData._mbr2d.getMinX() + ", y = "
					+ Slave._imgData._mbr2d.getMinY());
			System.out.println("[DEBUG]\t\t Max(RT) : x = " + Slave._imgData._mbr2d.getMaxX() + ", y = "
					+ Slave._imgData._mbr2d.getMaxY());
			System.out.println("[DEBUG]\t result : " + result._imgData._oFileData._strFilePath);
			System.out.println("[DEBUG]\t\t Min(LB) : x = " + result._imgData._mbr2d.getMinX() + ", y = "
					+ result._imgData._mbr2d.getMinY());
			System.out.println("[DEBUG]\t\t Max(RT) : x = " + result._imgData._mbr2d.getMaxX() + ", y = "
					+ result._imgData._mbr2d.getMaxY());
			System.out.println("[DEBUG]\t _point[] : ");
			System.out.println("[DEBUG]\t\t _point[0] : x = " + _point[0].x + ", y = " + _point[0].y);
			System.out.println("[DEBUG]\t\t _point[5] : x = " + _point[5].x + ", y = " + _point[5].y);
			System.out.println("[DEBUG]\t\t _point[10] : x = " + _point[10].x + ", y = " + _point[10].y);
			System.out.println("[DEBUG]\t\t _point[15] : x = " + _point[15].x + ", y = " + _point[15].y);
		}
//[DEBUG]				

	}

	// �ش� ȭ�Ұ��� ���� ������׷� ��Ī ����� ��ȯ�ϴ�.
	// @ Value : ȭ�Ұ�
	// @ histData : ������ũ ������׷� ����
	// @
	// @ return : byte ������׷� ��Ī ���
	private byte getMatchedDN(byte Value, GMosaicHistogramData histData) {
		byte Matched_DN = Value;
		double dblMatched_DN = 0;
		int nMatched_DN = 0;

		try {

			if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.NONE) // None
			{
				Matched_DN = Value;
			} else if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATCH_MEAN_STD_DEV) // Match
																																			// Mean
																																			// &
																																			// Std.
																																			// Dev.
			{
				// Matched_DN = (BYTE)( histData.dblCovariance / histData.dblVariance *
				// ((double)Value - histData.dblMeanSlave) + histData.dblMeanMaster );
				if ((0xff & Value) != 0) {
					// -------------------------------------------------------------------//
					// ȭ�Ұ� ���� ó�� (10.19)
					// -------------------------------------------------------------------//
					dblMatched_DN = (histData._histMatchInfo._dblCovariance / histData._histMatchInfo._dblVariance
							* ((double) (0xff & Value) - histData._histMatchInfo._dblMeanSlave)
							+ histData._histMatchInfo._dblMeanMaster);
					if (dblMatched_DN < 0)
						nMatched_DN = 0;
					else if (dblMatched_DN > 255)
						nMatched_DN = 255;
					else
						nMatched_DN = (int) Math.round(dblMatched_DN);
					// -------------------------------------------------------------------//
					Matched_DN = (byte) nMatched_DN;
				}

			} else if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATH_CUMULATIVE_FREQUENCY) // Match
																																				// Cumulative
																																				// Frequency
			{
				// Matched_DN = (BYTE)histData.nLUTModified[Value];
				if ((0xff & Value) != 0) {
					nMatched_DN = (0xff & Value);
					Matched_DN = (byte) histData._histMatchInfo._nLUTModified[nMatched_DN];
				}
			} else if (_pMosaicAlgorithmData._nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT) // Hue
																																		// Adjustment
																																		// Through
																																		// Moving
																																		// the
																																		// Histogram
			{
				// Matched_DN = (BYTE)( (double)(histData.nMaxMaster-histData.nMinMaster) /
				// (double)(histData.nMaxSlave-histData.nMinSlave) * Value +
				// (histData.dblMeanMaster - histData.dblMeanSlave) );
				if ((0xff & Value) != 0) {
					// -------------------------------------------------------------------//
					// ȭ�Ұ� ���� ó�� (10.19)
					// -------------------------------------------------------------------//
					dblMatched_DN = ((double) (histData._histMatchInfo._nMaxMaster
							- histData._histMatchInfo._nMinMaster)
							/ (double) (histData._histMatchInfo._nMaxSlave - histData._histMatchInfo._nMinSlave)
							* (double) (0xff & Value)
							+ (histData._histMatchInfo._dblMeanMaster - histData._histMatchInfo._dblMeanSlave));
					if (dblMatched_DN < 0)
						nMatched_DN = 0;
					else if (dblMatched_DN > 255)
						nMatched_DN = 255;
					else
						nMatched_DN = (int) Math.round(dblMatched_DN);
					// -------------------------------------------------------------------//
					Matched_DN = (byte) nMatched_DN;
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.getMatchedDN : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return Matched_DN;
	}

	// �ش� Path, Row, Tab�� ���� ���μ��� ���⼺�� Path ���⼺ ������ �������� ������ũ
	// ��� ������ ��� ������ ��ȯ�Ѵ�.
	// @ nPath : Path ����
	// @ nRow : Row ����
	// @ nTab : Tab ����
	// @ nType : Path ���⼺
	// @ nDirection : ���μ��� ���⼺
	// @
	// @ return : boolean ������ũ ��� ���� ��� ����
	private boolean isCheckData(int nPath, int nRow, int nTab, PathDirection nType, MosaicProcMethod nDirection) {
		boolean bOK = true, bCheck = false;
		int nNewPath1, nNewPath2, nNewRow1, nNewRow2;

		try {

			if (_pMosaicRegistData[nPath][nRow]._bCheckData && _pMosaicRegistData[nPath][nRow]._bUseChecking) {
				bOK = false;
			}
			if (nType != PathDirection.PATH_NONE) // 3
			{
				if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bUseChecking) {
					for (int i = 1; i <= nTab - 1; i++) {
						if (nDirection == MosaicProcMethod.Horizontal) {
							nNewRow1 = nRow - i;
							nNewRow2 = nRow + i;
							nNewPath1 = nNewPath2 = nPath;
						} else {
							nNewPath1 = nPath - i;
							nNewPath2 = nPath + i;
							nNewRow1 = nNewRow2 = nRow;
						}

						if (nType == PathDirection.PATH_CENTER) // 0
						{
							bCheck = (_pMosaicRegistData[nNewPath1][nNewRow1]._bCheckData
									&& _pMosaicRegistData[nNewPath1][nNewRow1]._bUseChecking)
									|| (_pMosaicRegistData[nNewPath2][nNewRow2]._bCheckData
											&& _pMosaicRegistData[nNewPath2][nNewRow2]._bUseChecking);
						} else if (nType == PathDirection.PATH_UP) // 1
						{
							bCheck = (_pMosaicRegistData[nNewPath1][nNewRow1]._bCheckData
									&& _pMosaicRegistData[nNewPath1][nNewRow1]._bUseChecking);
						} else if (nType == PathDirection.PATH_DOWN) // 2
						{
							bCheck = (_pMosaicRegistData[nNewPath2][nNewRow2]._bCheckData
									&& _pMosaicRegistData[nNewPath2][nNewRow2]._bUseChecking);
						}

						if (bCheck) {
							bOK = false;
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.isCheckData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return bOK;
	}

	// �ش� Path, Row, Tab�� ���� Path ���⼺(Center Type) ������ �������� Slave Center
	// Left & RIght ������ũ ��� ������ ��ȯ�Ѵ�.
	// @ nPath : Path ����
	// @ nRow : Row ����
	// @ nTab : Tab ����
	// @ nCenterType : Path ���⼺(Center Type)
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void checkSlaveLRDataCenter(int nPath, int nRow, int nTab, PathDirection nCenterType,
			GMosaicResultData resultData) {
		// @todo : ??????
		// nPath,nRow�� ������ �Ѿ���� üũ�κ� �߰��Ұ�

		int nTmpRow = 0;

		try {

			if (nCenterType == PathDirection.PATH_CENTER) // 0
			{
				if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
						&& !_pMosaicRegistData[nPath][nRow]._bUseChecking)// center check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nPath][nRow]);
					_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
					resultData._bCheckData = true;

					if (_pMosaicRegistData[nPath][nRow - 1]._bCheckData
							&& !_pMosaicRegistData[nPath][nRow - 1]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow - 1]._bUseChecking)// up check
					{
						_pMosaicRegistData[nPath][nRow - 1]._bUseChecking = true;
						calcProcessing(nPath, nRow - 1, false, ProcessDirection.PROCESS_TAB, resultData);

						if (nTab - 1 >= 2) {
							for (int i = 2; i <= nTab - 1; i++) {
								if (nRow - i >= 0) {
									if (_pMosaicRegistData[nPath][nRow - i]._bCheckData
											&& !_pMosaicRegistData[nPath][nRow - i]._bCheckMaster
											&& !_pMosaicRegistData[nPath][nRow - i]._bUseChecking)// up check
									{
										_pMosaicRegistData[nPath][nRow - i]._bUseChecking = true;
										calcProcessing(nPath, nRow - i, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							}
						}
					}
					if (_pMosaicRegistData[nPath][nRow + 1]._bCheckData
							&& !_pMosaicRegistData[nPath][nRow + 1]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow + 1]._bUseChecking)// down check
					{
						_pMosaicRegistData[nPath][nRow + 1]._bUseChecking = true;
						calcProcessing(nPath, nRow + 1, false, ProcessDirection.PROCESS_TAB, resultData);

						if (nTab - 1 >= 2) {
							for (int j = 2; j <= nTab - 1; j++) {
								if (nRow + j < _pMosaicData._nNumOfRow) {
									if (_pMosaicRegistData[nPath][nRow + j]._bCheckData
											&& !_pMosaicRegistData[nPath][nRow + j]._bCheckMaster
											&& !_pMosaicRegistData[nPath][nRow + j]._bUseChecking)// down check
									{
										_pMosaicRegistData[nPath][nRow + j]._bUseChecking = true;
										calcProcessing(nPath, nRow + j, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							}
						}
					}
				}
			} else {
				if (nCenterType == PathDirection.PATH_UP) // 1
				{
					nTmpRow = nRow - 1;
				} else if (nCenterType == PathDirection.PATH_DOWN) // 2
				{
					nTmpRow = nRow + 1;
				}

				if (_pMosaicRegistData[nPath][nTmpRow]._bCheckData && !_pMosaicRegistData[nPath][nTmpRow]._bCheckMaster
						&& !_pMosaicRegistData[nPath][nTmpRow]._bUseChecking)// down check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nPath][nTmpRow]);
					_pMosaicRegistData[nPath][nTmpRow]._bUseChecking = true;
					resultData._bCheckData = true;

					if (nTab - 1 >= 2) {
						for (int i = 2; i <= nTab - 1; i++) {
							if (nCenterType == PathDirection.PATH_UP) // 1
							{
								if (nRow - i >= 0) {
									if (_pMosaicRegistData[nPath][nRow - i]._bCheckData
											&& !_pMosaicRegistData[nPath][nRow - i]._bCheckMaster
											&& !_pMosaicRegistData[nPath][nRow - i]._bUseChecking)// up up check
									{
										_pMosaicRegistData[nPath][nRow - i]._bUseChecking = true;
										calcProcessing(nPath, nRow - i, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							} else if (nCenterType == PathDirection.PATH_DOWN) // 2
							{
								if (nRow + i < _pMosaicData._nNumOfRow) {
									if (_pMosaicRegistData[nPath][nRow + i]._bCheckData
											&& !_pMosaicRegistData[nPath][nRow + i]._bCheckMaster
											&& !_pMosaicRegistData[nPath][nRow + i]._bUseChecking)// up check
									{
										_pMosaicRegistData[nPath][nRow + i]._bUseChecking = true;
										calcProcessing(nPath, nRow + i, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							}
						}
					}

					if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow]._bUseChecking)// center check
					{
						_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
						calcProcessing(nPath, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
					}

					if (nCenterType == PathDirection.PATH_UP) // 1
					{
						nTmpRow += 2;
					} else if (nCenterType == PathDirection.PATH_DOWN) // 2
					{
						nTmpRow -= 2;
					}

					if (_pMosaicRegistData[nPath][nTmpRow]._bCheckData
							&& !_pMosaicRegistData[nPath][nTmpRow]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nTmpRow]._bUseChecking)// up check
					{
						_pMosaicRegistData[nPath][nTmpRow]._bUseChecking = true;
						calcProcessing(nPath, nTmpRow, false, ProcessDirection.PROCESS_TAB, resultData);

						if (nTab - 1 >= 2) {
							for (int j = 2; j <= nTab - 1; j++) {
								if (nCenterType == PathDirection.PATH_UP) // 1
								{
									if (nRow + j < _pMosaicData._nNumOfRow) {
										if (_pMosaicRegistData[nPath][nRow + j]._bCheckData
												&& !_pMosaicRegistData[nPath][nRow + j]._bCheckMaster
												&& !_pMosaicRegistData[nPath][nRow + j]._bUseChecking)// down check
										{
											_pMosaicRegistData[nPath][nRow + j]._bUseChecking = true;
											calcProcessing(nPath, nRow + j, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								} else if (nCenterType == PathDirection.PATH_DOWN) // 2
								{
									if (nRow - j >= 0) {
										if (_pMosaicRegistData[nPath][nRow - j]._bCheckData
												&& !_pMosaicRegistData[nPath][nRow - j]._bCheckMaster
												&& !_pMosaicRegistData[nPath][nRow - j]._bUseChecking)// up check
										{
											_pMosaicRegistData[nPath][nRow - j]._bUseChecking = true;
											calcProcessing(nPath, nRow - j, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.checkSlaveLRDataCenter : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// �ش� Path, Row, Tab�� ���� Path ���⼺(Center Type) ������ �������� Slave Left &
	// RIght ������ũ ��� ������ ��ȯ�Ѵ�.
	// @ nPath : Path ����
	// @ nRow : Row ����
	// @ nTab : Tab ����
	// @ bCenterUp : Center Up ����
	// @ nCenterType : Path ���⼺(Center Type)
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void checkSlaveLRData(int nPath, int nRow, int nTab, boolean bCenterUp, PathDirection nCenterType,
			GMosaicResultData resultData) {
		// @todo : ??????
		// nPath,nRow�� ������ �Ѿ���� üũ�κ� �߰��Ұ�

		int nTmpRow = 0;
		boolean bCheck = false;

		if (bCenterUp) {
			nTmpRow = nRow - 1;
		} else {
			nTmpRow = nRow + 1;
		}

		try {

			if (nCenterType == PathDirection.PATH_CENTER) // 0
			{
				if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
						&& !_pMosaicRegistData[nPath][nRow]._bUseChecking)// center check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nPath][nRow]);
					_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
					resultData._bCheckData = true;

					if (_pMosaicRegistData[nPath][nTmpRow]._bCheckData
							&& !_pMosaicRegistData[nPath][nTmpRow]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nTmpRow]._bUseChecking)// up check
					{
						_pMosaicRegistData[nPath][nTmpRow]._bUseChecking = true;
						calcProcessing(nPath, nTmpRow, false, ProcessDirection.PROCESS_TAB, resultData);

						if (nTab - 1 >= 2) {
							for (int i = 2; i <= nTab - 1; i++) {
								if (bCenterUp) {
									if (nRow - i >= 0) {
										if (_pMosaicRegistData[nPath][nRow - i]._bCheckData
												&& !_pMosaicRegistData[nPath][nRow - i]._bCheckMaster
												&& !_pMosaicRegistData[nPath][nRow - i]._bUseChecking)// up check
										{
											_pMosaicRegistData[nPath][nRow - i]._bUseChecking = true;
											calcProcessing(nPath, nRow - i, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								} else {
									if (nRow + i < _pMosaicData._nNumOfRow) {
										if (_pMosaicRegistData[nPath][nRow + i]._bCheckData
												&& !_pMosaicRegistData[nPath][nRow + i]._bCheckMaster
												&& !_pMosaicRegistData[nPath][nRow + i]._bUseChecking)// up check
										{
											_pMosaicRegistData[nPath][nRow + i]._bUseChecking = true;
											calcProcessing(nPath, nRow + i, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								}
							}
						}
					}
				}
			} else if (nCenterType == PathDirection.PATH_UP || nCenterType == PathDirection.PATH_DOWN) // 1, 2
			{
				if (_pMosaicRegistData[nPath][nTmpRow]._bCheckData && !_pMosaicRegistData[nPath][nTmpRow]._bCheckMaster
						&& !_pMosaicRegistData[nPath][nTmpRow]._bUseChecking)// center check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nPath][nTmpRow]);
					_pMosaicRegistData[nPath][nTmpRow]._bUseChecking = true;
					resultData._bCheckData = true;

					if (nTab - 1 >= 2) {
						for (int j = 2; j <= nTab - 1; j++) {
							if (bCenterUp) {
								if (nRow - j >= 0) {
									if (_pMosaicRegistData[nPath][nRow - j]._bCheckData
											&& !_pMosaicRegistData[nPath][nRow - j]._bCheckMaster
											&& !_pMosaicRegistData[nPath][nRow - j]._bUseChecking)// up check
									{
										_pMosaicRegistData[nPath][nRow - j]._bUseChecking = true;
										calcProcessing(nPath, nRow - j, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							} else {
								if (nRow + j < _pMosaicData._nNumOfRow) {
									if (_pMosaicRegistData[nPath][nRow + j]._bCheckData
											&& !_pMosaicRegistData[nPath][nRow + j]._bCheckMaster
											&& !_pMosaicRegistData[nPath][nRow + j]._bUseChecking)// up check
									{
										_pMosaicRegistData[nPath][nRow + j]._bUseChecking = true;
										calcProcessing(nPath, nRow + j, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							}
						}
					}

					if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow]._bUseChecking)// center check
					{
						_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
						calcProcessing(nPath, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.checkSlaveLRData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// �ش� Path, Row, Tab�� ���� Path ���⼺(Center Type) ������ �������� Slave Center
	// Tab ������ũ ��� ������ ��ȯ�Ѵ�.
	// @ nPath : Path ����
	// @ nRow : Row ����
	// @ nTab : Tab ����
	// @ nCenterType : Path ���⼺(Center Type)
	// @ bDirection : ������ũ ���μ��� ����
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void checkSlaveTBDataCenter(int nPath, int nRow, int nTab, PathDirection nCenterType,
			MosaicProcMethod bDirection, GMosaicResultData resultData) {
		// @todo : ??????
		// nPath,nRow�� ������ �Ѿ���� üũ�κ� �߰��Ұ�

		int nTmpPath = 0, nTmp = 0;
		boolean bRes = false;

		try {

			if (nCenterType == PathDirection.PATH_CENTER) // 0
			{
				if (bDirection == MosaicProcMethod.Horizontal)
					bRes = _pMosaicRegistData[nPath][nRow]._bCheckData
							&& !_pMosaicRegistData[nPath][nRow]._bUseChecking;
				else
					bRes = _pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow]._bUseChecking;
				if (bRes)// center check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nPath][nRow]);
					_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
					resultData._bCheckData = true;

					if (nPath - 1 >= 0) {
						if (_pMosaicRegistData[nPath - 1][nRow]._bCheckData
								&& !_pMosaicRegistData[nPath - 1][nRow]._bCheckMaster
								&& !_pMosaicRegistData[nPath - 1][nRow]._bUseChecking)// left check
						{
							_pMosaicRegistData[nPath - 1][nRow]._bUseChecking = true;
							calcProcessing(nPath - 1, nRow, false, ProcessDirection.PROCESS_TAB, resultData);

							for (int j = 2; j <= nTab; j++) {
								if (nPath - j >= 0) {
									if (_pMosaicRegistData[nPath - j][nRow]._bCheckData
											&& !_pMosaicRegistData[nPath - j][nRow]._bCheckMaster
											&& !_pMosaicRegistData[nPath - j][nRow]._bUseChecking)// left check
									{
										_pMosaicRegistData[nPath - j][nRow]._bUseChecking = true;
										calcProcessing(nPath - j, nRow, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							}
						}
					}

					if (nPath + 1 < _pMosaicData._nNumOfPath) {
						if (_pMosaicRegistData[nPath + 1][nRow]._bCheckData
								&& !_pMosaicRegistData[nPath + 1][nRow]._bCheckMaster
								&& !_pMosaicRegistData[nPath + 1][nRow]._bUseChecking)// right check
						{
							_pMosaicRegistData[nPath + 1][nRow]._bUseChecking = true;
							calcProcessing(nPath + 1, nRow, false, ProcessDirection.PROCESS_TAB, resultData);

							for (int j = 2; j <= nTab; j++) {
								if (nPath + j < _pMosaicData._nNumOfPath) {
									if (_pMosaicRegistData[nPath + j][nRow]._bCheckData
											&& !_pMosaicRegistData[nPath + j][nRow]._bCheckMaster
											&& !_pMosaicRegistData[nPath + j][nRow]._bUseChecking)// right check
									{
										_pMosaicRegistData[nPath + j][nRow]._bUseChecking = true;
										calcProcessing(nPath + j, nRow, false, ProcessDirection.PROCESS_TAB,
												resultData);
									}
								}
							}
						}
					}
				}
			} else {
				if (nCenterType == PathDirection.PATH_UP) // 1
				{
					nTmpPath = nPath - 1;
				} else if (nCenterType == PathDirection.PATH_DOWN) // 2
				{
					nTmpPath = nPath + 1;
				}

				if (_pMosaicRegistData[nTmpPath][nRow]._bCheckData && !_pMosaicRegistData[nTmpPath][nRow]._bCheckMaster
						&& !_pMosaicRegistData[nTmpPath][nRow]._bUseChecking)// down check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nTmpPath][nRow]);
					_pMosaicRegistData[nTmpPath][nRow]._bUseChecking = true;
					resultData._bCheckData = true;

					for (int i = 2; i <= nTab; i++) {
						if (nCenterType == PathDirection.PATH_UP) // 1
						{
							if (nPath - i >= 0) {
								if (_pMosaicRegistData[nPath - i][nRow]._bCheckData
										&& !_pMosaicRegistData[nPath - i][nRow]._bCheckMaster
										&& !_pMosaicRegistData[nPath - i][nRow]._bUseChecking)// up check
								{
									_pMosaicRegistData[nPath - i][nRow]._bUseChecking = true;
									calcProcessing(nPath - i, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
								}
							}
						} else if (nCenterType == PathDirection.PATH_DOWN) // 2
						{
							if (nPath + i < _pMosaicData._nNumOfPath) {
								if (_pMosaicRegistData[nPath + i][nRow]._bCheckData
										&& !_pMosaicRegistData[nPath + i][nRow]._bCheckMaster
										&& !_pMosaicRegistData[nPath + i][nRow]._bUseChecking)// up check
								{
									_pMosaicRegistData[nPath + i][nRow]._bUseChecking = true;
									calcProcessing(nPath + i, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
								}
							}
						}
					}

					if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow]._bUseChecking)// center check
					{
						_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
						calcProcessing(nPath, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
					}

					if (nCenterType == PathDirection.PATH_UP) // 1
					{
						nTmpPath += 2;
						if (nPath + 1 < _pMosaicData._nNumOfPath)
							bRes = true;
						else
							bRes = false;
					} else if (nCenterType == PathDirection.PATH_DOWN) // 2
					{
						nTmpPath -= 2;
						if (nPath - 1 >= 0)
							bRes = true;
						else
							bRes = false;
					}

					if (bRes)// right
					{
						if (_pMosaicRegistData[nTmpPath][nRow]._bCheckData
								&& !_pMosaicRegistData[nTmpPath][nRow]._bCheckMaster
								&& !_pMosaicRegistData[nTmpPath][nRow]._bUseChecking)// up check
						{
							_pMosaicRegistData[nTmpPath][nRow]._bUseChecking = true;
							calcProcessing(nTmpPath, nRow, false, ProcessDirection.PROCESS_TAB, resultData);

							for (int j = 2; j <= nTab; j++) {
								if (nCenterType == PathDirection.PATH_UP) // 1
								{
									if (nPath + j < _pMosaicData._nNumOfPath) {
										if (_pMosaicRegistData[nPath + j][nRow]._bCheckData
												&& !_pMosaicRegistData[nPath + j][nRow]._bCheckMaster
												&& !_pMosaicRegistData[nPath + j][nRow]._bUseChecking)// up check
										{
											_pMosaicRegistData[nPath + j][nRow]._bUseChecking = true;
											calcProcessing(nPath + j, nRow, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								} else if (nCenterType == PathDirection.PATH_DOWN) // 2
								{
									if (nPath - j >= 0) {
										if (_pMosaicRegistData[nPath - j][nRow]._bCheckData
												&& !_pMosaicRegistData[nPath - j][nRow]._bCheckMaster
												&& !_pMosaicRegistData[nPath - j][nRow]._bUseChecking)// up check
										{
											_pMosaicRegistData[nPath - j][nRow]._bUseChecking = true;
											calcProcessing(nPath - j, nRow, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.checkSlaveTBDataCenter : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

	// �ش� Path, Row, Tab�� ���� Path ���⼺(Center Type) ������ �������� Slave Tab
	// ������ũ ��� ������ ��ȯ�Ѵ�.
	// @ nPath : Path ����
	// @ nRow : Row ����
	// @ nTab : Tab ����
	// @ bCenterLeft : Center Left ����
	// @ nCenterType : Path ���⼺(Center Type)
	// @
	// @ return : GMosaicResultData resultData ��ȯ�� ������ũ ��� ����
	private void checkSlaveTBData(int nPath, int nRow, int nTab, boolean bCenterLeft, PathDirection nCenterType,
			GMosaicResultData resultData) {
		// @todo : ??????
		// nPath,nRow�� ������ �Ѿ���� üũ�κ� �߰��Ұ�

		int nTmpPath = 0;
		boolean bCheck = false, bRes = false;

		if (bCenterLeft) {
			nTmpPath = nPath - 1;
			if (nPath - 1 >= 0)
				bRes = true;
			else
				bRes = false;
		} else {
			nTmpPath = nPath + 1;
			if (nPath + 1 < _pMosaicData._nNumOfPath)
				bRes = true;
			else
				bRes = false;
		}

		try {

			if (nCenterType == PathDirection.PATH_CENTER) // 0
			{
				if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
						&& !_pMosaicRegistData[nPath][nRow]._bUseChecking)// center check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nPath][nRow]);
					_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
					resultData._bCheckData = true;

					if (bRes) {
						if (_pMosaicRegistData[nTmpPath][nRow]._bCheckData
								&& !_pMosaicRegistData[nTmpPath][nRow]._bCheckMaster
								&& !_pMosaicRegistData[nTmpPath][nRow]._bUseChecking)// up check
						{
							_pMosaicRegistData[nTmpPath][nRow]._bUseChecking = true;
							calcProcessing(nTmpPath, nRow, false, ProcessDirection.PROCESS_TAB, resultData);

							for (int i = 2; i <= nTab; i++) {
								if (bCenterLeft) {
									if (nPath - i >= 0) {
										if (_pMosaicRegistData[nPath - i][nRow]._bCheckData
												&& !_pMosaicRegistData[nPath - i][nRow]._bCheckMaster
												&& !_pMosaicRegistData[nPath - i][nRow]._bUseChecking)// up check
										{
											_pMosaicRegistData[nPath - i][nRow]._bUseChecking = true;
											calcProcessing(nPath - i, nRow, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								} else {
									if (nPath + i < _pMosaicData._nNumOfPath) {
										if (_pMosaicRegistData[nPath + i][nRow]._bCheckData
												&& !_pMosaicRegistData[nPath + i][nRow]._bCheckMaster
												&& !_pMosaicRegistData[nPath + i][nRow]._bUseChecking)// up check
										{
											_pMosaicRegistData[nPath + i][nRow]._bUseChecking = true;
											calcProcessing(nPath + i, nRow, false, ProcessDirection.PROCESS_TAB,
													resultData);
										}
									}
								}
							}
						}
					}
				}
			} else if (nCenterType == PathDirection.PATH_UP || nCenterType == PathDirection.PATH_DOWN) // 1, 2
			{
				if (_pMosaicRegistData[nTmpPath][nRow]._bCheckData && !_pMosaicRegistData[nTmpPath][nRow]._bCheckMaster
						&& !_pMosaicRegistData[nTmpPath][nRow]._bUseChecking)// center check
				{
					// @todo : Copy�� �´��� ? Equal�� �´��� ?
					resultData.Copy(_pMosaicRegistData[nTmpPath][nRow]);
					_pMosaicRegistData[nTmpPath][nRow]._bUseChecking = true;
					resultData._bCheckData = true;

					for (int j = 2; j <= nTab; j++) {
						if (bCenterLeft) {
							if (nPath - j >= 0) {
								if (_pMosaicRegistData[nPath - j][nRow]._bCheckData
										&& !_pMosaicRegistData[nPath - j][nRow]._bCheckMaster
										&& !_pMosaicRegistData[nPath - j][nRow]._bUseChecking)// up check
								{
									_pMosaicRegistData[nPath - j][nRow]._bUseChecking = true;
									calcProcessing(nPath - j, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
								}
							}
						} else {
							if (nPath + j < _pMosaicData._nNumOfPath) {
								if (_pMosaicRegistData[nPath + j][nRow]._bCheckData
										&& !_pMosaicRegistData[nPath + j][nRow]._bCheckMaster
										&& !_pMosaicRegistData[nPath + j][nRow]._bUseChecking)// up check
								{
									_pMosaicRegistData[nPath + j][nRow]._bUseChecking = true;
									calcProcessing(nPath + j, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
								}
							}
						}
					}

					if (_pMosaicRegistData[nPath][nRow]._bCheckData && !_pMosaicRegistData[nPath][nRow]._bCheckMaster
							&& !_pMosaicRegistData[nPath][nRow]._bUseChecking)// center check
					{
						_pMosaicRegistData[nPath][nRow]._bUseChecking = true;
						calcProcessing(nPath, nRow, false, ProcessDirection.PROCESS_TAB, resultData);
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GAutoMosaic.checkSlaveTBData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

}
