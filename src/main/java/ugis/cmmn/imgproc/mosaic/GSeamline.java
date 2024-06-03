package ugis.cmmn.imgproc.mosaic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.locationtech.jts.geom.Coordinate;

import ugis.cmmn.imgproc.data.GCannyEdgeData;
import ugis.cmmn.imgproc.data.GMosaicAlgorithmData;
import ugis.cmmn.imgproc.data.GMosaicProcData;

//Seamline ���� ���� Ŭ����
public class GSeamline {

	// Seamline ����
	public enum SeamlineMethod {
		MINIMUM_DIFFERENCE_SUM("Minimum Gray Difference Sum"), MIDDLE_LINE("Middle Line Fitting"),
		USE_EXIST_SEAMLINE("Use Existing Seamline"),
		FEATURE_SELECTION_USING_EDGE("Feature Selection Using Canny Edge Operator"); // Default

		private String name;

		SeamlineMethod(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// Edge�� ���⼺
	private final int _RIGHT_DIR = 1;
	private final int _LEFT_DIR = 3;
	private final int _UP_DIR = 2;
	private final int _DOWN_DIR = 4;
	private final int _RIGHTUP_DIR = 5;
	private final int _LEFTUP_DIR = 6;
	private final int _LEFTDOWN_DIR = 7;
	private final int _RIGHTDOWN_DIR = 8;

	///////////////////////////////////////////////////////////////////////////
	// Output

	// @todo : Seam Point ���
	// String strSeamPoint
	// Boundary LeftTop
	// Number of Band
	// Resolution X
	// Resolution Y
	// Shape (0 : horizontal width, 1 : vertical height)
	// Width(Height) Size (Seam Point Count)
	// **Data**
	// Index

	///////////////////////////////////////////////////////////////////////////
	// Input

	// ������ũ �˰��� ����
	GMosaicAlgorithmData _pMosaicAlgorithmData = null;

	// Master ���� (��󿵻�)
	private GMosaicProcData _Master = null;

	// Slave ���� (���ؿ���)
	private GMosaicProcData _Slave = null;

	// �޸� ����
	private int _nDevice = 0; // 0: memory, 1: file(raw file)

	// Master Overlap Area (���� ũ��)
	private int _lHeight = 0;

	// Master Overlap Area (���� ũ��)
	private int _lWidth = 0;

	// Center Point of Overlap Area (������ǥ X)
	private int _lCneterPointX = 0;

	// Center Point of Overlap Area (������ǥ Y)
	private int _lCneterPointY = 0;

	// ����þ� ǥ������
	private float _fGaussianSigma = 2.0f;

	// ���� �Ӱ谪
	private float _fLowThresh = 0.8f;

	// ���� �Ӱ谪
	private float _fHighThresh = 0.4f;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Seamline ������ �����Ѵ�.
	// @ pMosaicAlgorithmData : ������ũ �˰��� ����
	// @ master : Master ���� (��󿵻�)
	// @ slave : Slave ���� (���ؿ���)
	// @
	// @ return : int[] Seam Point ���
	public int[] procSeamline(GMosaicAlgorithmData pMosaicAlgorithmData, GMosaicProcData master,
			GMosaicProcData slave) {
		int[] lSeamPoint = null;
		int nShape = 0;
		GHistogramMatching histomatching = new GHistogramMatching();

		// Input
		_pMosaicAlgorithmData = pMosaicAlgorithmData;
		_Master = master;
		_Slave = slave;

		// Output

		// {Get Data
		_lHeight = (int) _Master._imgData._imgBox2d.getHeight();
		_lWidth = (int) _Master._imgData._imgBox2d.getWidth();
		_nDevice = 0; // nDevice;

		// Get Center Point of Overlap Area
		_lCneterPointX = (int) ((double) _lWidth / 2.0) - 1;
		_lCneterPointY = (int) ((double) _lHeight / 2.0) - 1;

		// Get Overlap Shape (0 : wide, 1 : narrow)
		nShape = histomatching.getOverlapShape(_lHeight, _lWidth);

		lSeamPoint = calcSeamPoint(nShape);

		if (_pMosaicAlgorithmData._bSaveSeamPoint) {
			printSeamPoint(lSeamPoint, nShape);
		}

		return lSeamPoint;
	}

	// Seam Line ������ �о Seam Point ����� ��ȯ�Ѵ�.
	// @ strSPointDataFileName : Seam Line ���� ���
	// @
	// @ return : int[] Seam Point ���
	public int[] readExistingSeamline(String strSPointDataFileName) {
		int[] lSeamPoint = null;
		int nLines = 0;
		String strTmp = "";

		if (strSPointDataFileName.isEmpty())
			return lSeamPoint;

		try {
			// ���� ��ü ����
			File file = new File(strSPointDataFileName);
			// �Է� ��Ʈ�� ����
			FileReader filereader = new FileReader(file);
			// �Է� ���� ����
			BufferedReader bufReader = new BufferedReader(filereader);

			// ------------------------------------------------------------------
			strTmp = bufReader.readLine(); // [line 1] Boundary LeftTop
			strTmp = bufReader.readLine(); // [line 2] Number of Band
			strTmp = bufReader.readLine(); // [line 3] Resolution X
			strTmp = bufReader.readLine(); // [line 4] Resolution Y
			strTmp = bufReader.readLine(); // [line 5] Shape (0 : horizontal width, 1 : vertical height)

			if (strTmp.equals("Shape : 0")) {
				strTmp = bufReader.readLine(); // [line 6] Width Size (Seam Point Count)
				nLines = Integer.parseInt(strTmp.replaceFirst("Width Size : ", "").toString());
			} else if (strTmp.equals("Shape : 1")) {
				strTmp = bufReader.readLine(); // [line 6] Height Size (Seam Point Count)
				nLines = Integer.parseInt(strTmp.replaceFirst("Height Size : ", "").toString());
			}
			// ------------------------------------------------------------------

			if (nLines != 0) {
				lSeamPoint = new int[nLines];
				strTmp = bufReader.readLine(); // [line 7] (null)
				strTmp = bufReader.readLine(); // [line 8] **Data**

				for (int i = 0; i < nLines; i++) {
					strTmp = bufReader.readLine();
					lSeamPoint[i] = Integer.parseInt(strTmp);
				}
			}

			bufReader.close();
			filereader.close();
		} catch (FileNotFoundException fex) {
			System.out.println("GSeamline.readExistingSeamline : " + fex.toString());
			fex.printStackTrace();

			lSeamPoint = null;
		} catch (IOException ioe) {
			System.out.println("GSeamline.readExistingSeamline : " + ioe.toString());
			ioe.printStackTrace();

			lSeamPoint = null;
		}

		return lSeamPoint;
	}

	// Seam Point ����� ���ڿ��� �����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @ nShape : Overlap ���� (0 : wide, 1 : narrow)
	public void printSeamPoint(int[] lSeamp, int nShape) {
		String strTmp = "";

		_pMosaicAlgorithmData._strSeamPoint = "";

		// [line 1] Boundary LeftTop
		strTmp = "Boundary LeftTop : " + Double.toString(_Master._imgData._mbr2d.getMinX()) + ", "
				+ Double.toString(_Master._imgData._mbr2d.getMaxY()) + "\r\n";
		_pMosaicAlgorithmData._strSeamPoint += strTmp;

		// [line 2] Number of Band
		strTmp = "Number of Band : " + Integer.toString(_Master._imgData._lBandNum) + "\r\n";
		_pMosaicAlgorithmData._strSeamPoint += strTmp;

		// [line 3] Resolution X
		strTmp = "Resolution X : " + Double.toString(_Master._imgData._dblPixelScales[0]) + "\r\n";
		_pMosaicAlgorithmData._strSeamPoint += strTmp;

		// [line 4] Resolution Y
		strTmp = "Resolution Y : " + Double.toString(_Master._imgData._dblPixelScales[1]) + "\r\n";
		_pMosaicAlgorithmData._strSeamPoint += strTmp;

		// [line 5] Shape (0 : horizontal width, 1 : vertical height)
		strTmp = "Shape : " + Integer.toString(nShape) + "\r\n";
		_pMosaicAlgorithmData._strSeamPoint += strTmp;

		if (nShape == 0) // 0 : wide
		{
			// [line 6] Width Size
			strTmp = "Width Size : " + Integer.toString(_lWidth) + "\r\n";
			_pMosaicAlgorithmData._strSeamPoint += strTmp;

			// [line 7] (null)
			// [line 8] **Data**
			strTmp = "\r\n**Data**\r\n";
			_pMosaicAlgorithmData._strSeamPoint += strTmp;

			for (int i = 0; i < _lWidth; i++) {
				// Index
				strTmp = Integer.toString(lSeamp[i]) + "\r\n";
				_pMosaicAlgorithmData._strSeamPoint += strTmp;
			}
		} else if (nShape == 1) // 1 : narrow
		{
			// [line 6] Height Size
			strTmp = "Height Size : " + Integer.toString(_lHeight) + "\r\n";
			_pMosaicAlgorithmData._strSeamPoint += strTmp;

			// [line 7] (null)
			// [line 8] **Data**
			strTmp = "\r\n**Data**\r\n";
			_pMosaicAlgorithmData._strSeamPoint += strTmp;

			for (int i = 0; i < _lHeight; i++) {
				// Index
				strTmp = Integer.toString(lSeamp[i]) + "\r\n";
				_pMosaicAlgorithmData._strSeamPoint += strTmp;
			}
		}

		_pMosaicAlgorithmData._strSeamPoint += "EOF\r\n";
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Seam Point�� ����Ѵ�.
	// @ nShape : Overlap Path, Row ���� ����
	// @
	// @ return : int[] Seam Point ���
	private int[] calcSeamPoint(int nShape) {
		int[] lSeamPoint = null;

		/* ########## Seamline Algorithm ############## */
		// Algorithm = 0 : Minimun Difference Sum
		// Algorithm = 1 : Middle Line
		// Algorithm = 2 : Use existing Seamline
		// Algorithm = 3 : Feature Selection Using Canny Edge Operator

		if (_pMosaicAlgorithmData._nSeamLineMethod == SeamlineMethod.MINIMUM_DIFFERENCE_SUM) // Minimun Difference Sum
		{
			if (nShape == 0) // Horizon
			{
				// if(_nDevice == 0)
				lSeamPoint = calcMinDiffSum_H_MemBase();
			} else if (nShape == 1) // Vertical
			{
				// if(_nDevice == 0)
				lSeamPoint = calcMinDiffSum_V_MemBase();
			}
		} else if (_pMosaicAlgorithmData._nSeamLineMethod == SeamlineMethod.MIDDLE_LINE) // Middle Line
		{
			if (nShape == 0) // Horizon
			{
				// if(_nDevice == 0)
				lSeamPoint = calcMiddleLine_H_MemBase();
			} else if (nShape == 1) // Vertical
			{
				// if(_nDevice == 0)
				lSeamPoint = calcMiddleLine_V_MemBase();
			}
		} else if (_pMosaicAlgorithmData._nSeamLineMethod == SeamlineMethod.USE_EXIST_SEAMLINE) // Use existing Seamline
		{
			lSeamPoint = readExistingSeamline(_pMosaicAlgorithmData._strSPointDataFileName);
		} else if (_pMosaicAlgorithmData._nSeamLineMethod == SeamlineMethod.FEATURE_SELECTION_USING_EDGE) // Feature
																											// Selection
		{
			if (nShape == 0) // Horizon
			{
				// if(_nDevice == 0)
				lSeamPoint = calcFeatureDetection_MinDiffSum_H_MemBase();
			} else if (nShape == 1) // Vertical
			{
				// if(_nDevice == 0)
				lSeamPoint = calcFeatureDetection_MinDiffSum_V_MemBase();
			}
		}

		return lSeamPoint;
	}

	// Minimum Gray Difference Sum �˰����� �����Ͽ� ������� Seam Point�� ����Ѵ�.
	// @ return : int[] Seam Point ���
	private int[] calcMinDiffSum_H_MemBase() {
		int[] lSeamPoint = new int[_lWidth];

		double dwSumMean;
		int lStart, lEnd;
		int lCheckY, lTmp;
		int mPosX;
		int nSum = 0;
		int nSumCount = 0;
		double dwSumMeanTmp = 0.0;
		int lExtend = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
		int masterValue = 0;
		int slaveValue = 0;
		int i = 0, j = 0, k = 0;

		mPosX = _lCneterPointX;

		try {

			// Right Direction
			for (i = mPosX; i < _lWidth; i++) {
				if (i == _lCneterPointX) {
					lStart = _lCneterPointY - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);
					lEnd = _lCneterPointY + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);
					lCheckY = _lCneterPointY;
				} else {
					lStart = lSeamPoint[i - 1] - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
					lEnd = lSeamPoint[i - 1] + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
					lCheckY = lSeamPoint[i - 1];
				}
				if (lStart < 50)
					lStart = 50;
				if (lEnd > _lHeight - 50)
					lEnd = _lHeight - 50;

				dwSumMean = 255; // Init SumMean each column

				for (j = lStart; j < lEnd; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						if (k >= 0 && k < _lHeight) {
							masterValue = (0xff & _Master._imgData._pImage[i + _lWidth * k]); // get master image value
							slaveValue = (0xff & _Slave._imgData._pImage[i + _lWidth * k]); // get slave image value
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dwSumMeanTmp = (double) nSum / (double) nSumCount;
						if ((dwSumMeanTmp < dwSumMean) || ((dwSumMeanTmp == dwSumMean)
								&& (Math.abs(j - lCheckY) < Math.abs(lSeamPoint[i] - lCheckY)))) {
							dwSumMean = dwSumMeanTmp;
							lSeamPoint[i] = j;
						}
					}
				} // end height(j)
					// Case: Seam Point is not selected -> Select a former seam point
				if (dwSumMean == 255)
					lSeamPoint[i] = lSeamPoint[i - 1];
			} // end width(i)

			// Left Direction
			for (i = (_lCneterPointX - 1); i >= 0; i--) {
				lCheckY = lSeamPoint[i + 1];
				if (i == _lCneterPointX - 1)
					lTmp = _lCneterPointX;
				else
					lTmp = i + 1;

				lStart = lSeamPoint[lTmp] - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
				lEnd = lSeamPoint[lTmp] + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
				if (lStart < 50)
					lStart = 50;
				if (lEnd > _lHeight - 50)
					lEnd = _lHeight - 50;

				dwSumMean = 255; // Init SumMean each column

				for (j = lStart; j < lEnd; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						if (k >= 0 && k < _lHeight) {
							masterValue = (0xff & _Master._imgData._pImage[i + _lWidth * k]);
							slaveValue = (0xff & _Slave._imgData._pImage[i + _lWidth * k]);
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dwSumMeanTmp = (double) nSum / (double) nSumCount;
						if ((dwSumMeanTmp < dwSumMean) || ((dwSumMeanTmp == dwSumMean)
								&& (Math.abs(j - lCheckY) < Math.abs(lSeamPoint[i] - lCheckY)))) {
							dwSumMean = dwSumMeanTmp;
							lSeamPoint[i] = j;
						}
					}
				} // end height(j)

				// Case: Seam Point is not selected -> Select a former seam point
				if (dwSumMean == 255)
					lSeamPoint[i] = lSeamPoint[i + 1];
			} // end width(i)

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GSeamline.calcMinDiffSum_H_MemBase : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return lSeamPoint; // return seam point
	}

	// Minimum Gray Difference Sum �˰����� �����Ͽ� �������� Seam Point�� ����Ѵ�.
	// @ return : int[] Seam Point ���
	private int[] calcMinDiffSum_V_MemBase() {
		int[] lSeamPoint = new int[_lHeight];
		double dwSumMean = 0;
		int lStart = 0, lEnd = 0;
		int lCheckX = 0, lTmp = 0;
		int mPosY = 0;
		int nSum = 0;
		int nSumCount = 0;
		double dwSumMeanTmp = 0.0;
		int lExtend = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
		int masterValue = 0;
		int slaveValue = 0;
		int i = 0, j = 0, k = 0;

		mPosY = _lCneterPointY;

		try {

			// Down Direction
			for (i = mPosY; i < _lHeight; i++) {
				if (i == _lCneterPointY) {
					lStart = _lCneterPointX - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);
					lEnd = _lCneterPointX + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);
					lCheckX = _lCneterPointX;
				} else {
					lStart = lSeamPoint[i - 1] - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
					lEnd = lSeamPoint[i - 1] + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
					lCheckX = lSeamPoint[i - 1];
				}

				if (lStart < 50)
					lStart = 50;
				if (lEnd > _lWidth - 50)
					lEnd = _lWidth - 50;

				dwSumMean = 255; // Init SumMean each column

				for (j = lStart; j < lEnd; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						if (k >= 0 && k < _lWidth) {
							masterValue = (0xff & _Master._imgData._pImage[k + _lWidth * i]);
							slaveValue = (0xff & _Slave._imgData._pImage[k + _lWidth * i]);
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dwSumMeanTmp = (double) nSum / (double) nSumCount;
						if ((dwSumMeanTmp < dwSumMean) || ((dwSumMeanTmp == dwSumMean)
								&& (Math.abs(j - lCheckX) < Math.abs(lSeamPoint[i] - lCheckX)))) {
							dwSumMean = dwSumMeanTmp;
							lSeamPoint[i] = j;
						}
					}
				} // end width(j)
					// Case: Seam Point is not selected -> Select a former seam point
				if (dwSumMean == 255)
					lSeamPoint[i] = lSeamPoint[i - 1];
			} // end height(i)

			// *****
			// Up Direction
			for (i = (_lCneterPointY - 1); i >= 0; i--) {
				if (i == _lCneterPointY - 1)
					lTmp = _lCneterPointY;
				else
					lTmp = i + 1;

				lStart = lSeamPoint[lTmp] - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
				lEnd = lSeamPoint[lTmp] + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
				if (lStart < 50)
					lStart = 50;
				if (lEnd > _lWidth - 50)
					lEnd = _lWidth - 50;
				lCheckX = lSeamPoint[lTmp];

				dwSumMean = 255; // Init SumMean each column

				for (j = lStart; j < lEnd; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						if (k >= 0 && k < _lWidth) {
							masterValue = (0xff & _Master._imgData._pImage[k + _lWidth * i]);
							slaveValue = (0xff & _Slave._imgData._pImage[k + _lWidth * i]);
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dwSumMeanTmp = (double) nSum / (double) nSumCount;
						if ((dwSumMeanTmp < dwSumMean) || ((dwSumMeanTmp == dwSumMean)
								&& (Math.abs(j - lCheckX) < Math.abs(lSeamPoint[i] - lCheckX)))) {
							dwSumMean = dwSumMeanTmp;
							lSeamPoint[i] = j;
						}
					}
				} // end width(j)

				// Case: Seam Point is not selected -> Select a former seam point
				if (dwSumMean == 255)
					lSeamPoint[i] = lSeamPoint[i + 1];
			} // end height(i)

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GSeamline.calcMinDiffSum_V_MemBase : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return lSeamPoint;
	}

	// Middle Line Fitting �˰����� �����Ͽ� ������� Seam Point�� ����Ѵ�.
	// @ return : int[] Seam Point ���
	private int[] calcMiddleLine_H_MemBase() {
		int[] lSeamPoint = new int[_lWidth];

		int i = 0, j = 0;
		int lCol1 = (int) ((double) _lWidth / 4.0);
		int lCol2 = _lWidth - lCol1;
		int lStartRow = 0, lEndRow = 0;

		try {

			for (i = 0; i < _lHeight; i++) {
				if (((0xff & _Master._imgData._pImage[lCol1 + _lWidth * i]) != 0)
						&& ((0xff & _Slave._imgData._pImage[lCol1 + _lWidth * i]) != 0)) {
					lStartRow = i;
					break;
				}
			}
			for (i = _lHeight - 1; i >= 0; i--) {
				if (((0xff & _Master._imgData._pImage[lCol1 + _lWidth * i]) != 0)
						&& ((0xff & _Slave._imgData._pImage[lCol1 + _lWidth * i]) != 0)) {
					lEndRow = i;
					break;
				}
			}
			int lRow1 = lStartRow + (int) ((lEndRow - lStartRow) / 2.0);

			for (i = 0; i < _lHeight; i++) {
				if (((0xff & _Master._imgData._pImage[lCol2 + _lWidth * i]) != 0)
						&& ((0xff & _Slave._imgData._pImage[lCol2 + _lWidth * i]) != 0)) {
					lStartRow = i;
					break;
				}
			}
			for (i = _lHeight - 1; i >= 0; i--) {
				if (((0xff & _Master._imgData._pImage[lCol2 + _lWidth * i]) != 0)
						&& ((0xff & _Slave._imgData._pImage[lCol2 + _lWidth * i]) != 0)) {
					lEndRow = i;
					break;
				}
			}
			int lRow2 = lStartRow + (int) ((lEndRow - lStartRow) / 2.0);

			// Get dblGain & offset
			double dwdblGain = (double) (lRow1 - lRow2) / (double) (lCol1 - lCol2);
			double dwOffset = (double) lRow1 - (double) (lRow1 - lRow2) / (double) (lCol1 - lCol2) * (double) lCol1;

			// Get lSeamPoints
			for (j = 0; j < _lWidth; j++) {
				lSeamPoint[j] = (int) (dwdblGain * j + dwOffset);

				if (lSeamPoint[j] > _lHeight - 1)
					lSeamPoint[j] = _lHeight - 1;
				if (lSeamPoint[j] < 0)
					lSeamPoint[j] = 0;
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GSeamline.calcMiddleLine_H_MemBase : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return lSeamPoint;
	}

	// Middle Line Fitting �˰����� �����Ͽ� �������� Seam Point�� ����Ѵ�.
	// @ return : int[] Seam Point ���
	private int[] calcMiddleLine_V_MemBase() {
		int[] lSeamPoint = new int[_lHeight];

		int i = 0, j = 0;
		int lRow1 = (int) ((double) _lHeight / 4.0);
		int lRow2 = _lHeight - lRow1;
		int lStartCol = 0, lEndCol = 0;

		try {

			for (j = 0; j < _lWidth; j++) {
				if (((0xff & _Master._imgData._pImage[j + _lWidth * lRow1]) != 0)
						&& ((0xff & _Slave._imgData._pImage[j + _lWidth * lRow1]) != 0)) {
					lStartCol = j;
					break;
				}

			}
			for (j = _lWidth - 1; j >= 0; j--) {
				if (((0xff & _Master._imgData._pImage[j + _lWidth * lRow1]) != 0)
						&& ((0xff & _Slave._imgData._pImage[j + _lWidth * lRow1]) != 0)) {
					lEndCol = j;
					break;
				}
			}
			int col1 = lStartCol + (int) ((lEndCol - lStartCol) / 2.0);

			for (j = 0; j < _lWidth; j++) {
				if (((0xff & _Master._imgData._pImage[j + _lWidth * lRow2]) != 0)
						&& ((0xff & _Slave._imgData._pImage[j + _lWidth * lRow2]) != 0)) {
					lStartCol = j;
					break;
				}

			}
			for (j = _lWidth - 1; j >= 0; j--) {
				if (((0xff & _Master._imgData._pImage[j + _lWidth * lRow2]) != 0)
						&& ((0xff & _Slave._imgData._pImage[j + _lWidth * lRow2]) != 0)) {
					lEndCol = j;
					break;
				}
			}

			int lCol2 = lStartCol + (int) ((lEndCol - lStartCol) / 2.0);

			double dwdblGain = (double) (col1 - lCol2) / (double) (lRow1 - lRow2);
			double dwOffset = (double) col1 - (double) (col1 - lCol2) / (double) (lRow1 - lRow2) * (double) lRow1;

			for (i = 0; i < _lHeight; i++) {
				lSeamPoint[i] = (int) (dwdblGain * i + dwOffset);
				if (lSeamPoint[i] > _lWidth - 1) {
					lSeamPoint[i] = _lWidth - 1;
				}
				if (lSeamPoint[i] < 0) {
					lSeamPoint[i] = 0;
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GSeamline.calcMiddleLine_V_MemBase : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return lSeamPoint;
	}

	// Feature Selection Using Canny Edge Operator �˰����� �����Ͽ� ������� Seam
	// Point�� ����Ѵ�.
	// @ return : int[] Seam Point ���
	private int[] calcFeatureDetection_MinDiffSum_H_MemBase() {
		int[] lSeamPoint = new int[_lWidth];

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GSeamline.calcFeatureDetection_MinDiffSum_H_MemBase : ");
			System.out.println("[DEBUG]\t Start : ");
		}
//[DEBUG]

//		[STEP 1]
		//////////////////////////////////////
		// Edge Detection

//[DEBUG]	
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t GET MASTER EDGE IMAGE ");
		}
//[DEBUG]		

		// GET MASTER EDGE IMAGE
		GCannyEdgeDetection edge1 = new GCannyEdgeDetection();
		GCannyEdgeData masterEdgeInfo = edge1.procCannySeamlineOperator(_Master._imgData._pImage, _lHeight, _lWidth,
				_fGaussianSigma, _fLowThresh, _fHighThresh);

		////////////////////////////////////////////////////////////////////////////////////////////

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("");
			System.out.println("[DEBUG]\t GET SLAVE EDGE IMAGE");
		}
//[DEBUG]	

		// GET SLAVE EDGE IMAGE
		GCannyEdgeDetection edge2 = new GCannyEdgeDetection();
		GCannyEdgeData slaveEdgeInfo = edge2.procCannySeamlineOperator(_Slave._imgData._pImage, _lHeight, _lWidth,
				_fGaussianSigma, _fLowThresh, _fHighThresh);

		////////////////////////////////////////////////////////////////////////////////////////////

//		[STEP 2]
		///////////////////////////////////////
		// Seam line Extraction

		int i, j, k, lExtent, lStartCp, lEndCp;
		int lSwHalf = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
		int iOld, iNew, jOld, jNew;
		int lStartPos, lEndPos;
		boolean bInitPoint = false;
		double dblSumMean = 255.0, dblGain = 0, dblOffset = 0, dblSumMeanValue = 0;
		int nSum = 0, nSumCount = 0;
		int nCheckingValue = 0;
		int masterValue = 0, slaveValue = 0;
		Coordinate pos = null;

		lExtent = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
		for (j = 0; j < _lWidth; j++)
			lSeamPoint[j] = -1;

//		[STEP 2-1]
		// GET INITIAL POINT(lCneterPointX)
		lStartCp = _lCneterPointY - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);
		lEndCp = _lCneterPointY + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);

		if (lStartCp < 50)
			lStartCp = 50;
		if (lEndCp > _lWidth - 50)
			lEndCp = _lWidth - 50;

		try {

			while (!bInitPoint) {
				for (i = lStartCp; i < lEndCp; i++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (i - lExtent); k < (i + lExtent); k++) {
						if (k >= 0 && k < _lHeight) {
							masterValue = (0xff & _Master._imgData._pImage[_lCneterPointX + _lWidth * k]);
							slaveValue = (0xff & _Slave._imgData._pImage[_lCneterPointX + _lWidth * k]);
						}

						if (k >= 0 && masterValue != 0 && slaveValue != 0) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // 'k'

					if (nSum != 0 && nSumCount != 0) {
						dblSumMeanValue = (double) nSum / (double) nSumCount;

						if (dblSumMeanValue < dblSumMean) {
							if (masterEdgeInfo._EdgeImage[_lCneterPointX + _lWidth * i] == 0
									&& slaveEdgeInfo._EdgeImage[_lCneterPointX + _lWidth * i] == 0)// edge check
							{
								dblSumMean = dblSumMeanValue;
								lSeamPoint[_lCneterPointX] = i;
							}
						} else if (dblSumMeanValue == dblSumMean && Math.abs(j - _lCneterPointX) < Math
								.abs(lSeamPoint[_lCneterPointY] - _lCneterPointX)) {
							if (masterEdgeInfo._EdgeImage[_lCneterPointX + _lWidth * i] == 0
									&& slaveEdgeInfo._EdgeImage[_lCneterPointX + _lWidth * i] == 0)// edge check
							{
								dblSumMean = dblSumMeanValue;
								lSeamPoint[_lCneterPointX] = i;
							}
						}
					}

				} // 'i'

				if (dblSumMean == 255.0) {
					_lCneterPointX++;

					// @todo : ����ó�� (IndexOutOfBoundsException
					// if(_lCneterPointX >= _lWidth)
					if (_lCneterPointX >= (_lWidth - 1))
						break;
				} else
					bInitPoint = true;
			}

//			STEP 2-2
//			//RIGHT DIRECTION
			j = _lCneterPointX; // initial value
			while (j > 0 && j < _lWidth - 1) {
				if (lSeamPoint[j] == -1) {
					while (lSeamPoint[j] == -1)
						j--;

					if (!getImageChecking(j, lSeamPoint[j])) {
						lSeamPoint[j] = lSeamPoint[j - 1];
						j++;
					}
				}

				nCheckingValue = getEdgeChecking_OR(lSeamPoint[j], j, masterEdgeInfo._GradMag, slaveEdgeInfo._GradMag,
						masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _RIGHT_DIR);

				if (nCheckingValue == _RIGHT_DIR)// right edge
				{
					lSeamPoint[j + 1] = lSeamPoint[j];
					j++;
				} else if (nCheckingValue == _RIGHTUP_DIR)// right Up edge
				{
					lSeamPoint[j + 1] = lSeamPoint[j] - 1;
					j++;
				} else if (nCheckingValue == _RIGHTDOWN_DIR)// right down edge
				{
					lSeamPoint[j + 1] = lSeamPoint[j] + 1;
					j++;
				} else if (nCheckingValue == 0)// end edge
				{
					jOld = j;
					iOld = lSeamPoint[j];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _RIGHT_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(jNew - jOld) - 1 > 5) {
						if (Math.abs(iNew - iOld) - 1 < 3)
							lSeamPoint[j] = lSeamPoint[j - 1];
						else {
							dblGain = (double) (iNew - iOld) / (double) (jNew - jOld);
							dblOffset = (double) iNew - dblGain * (double) jNew;
							for (j = (jOld + 1); j < (jNew); j++)
								lSeamPoint[j] = (int) Math.round(dblGain * (double) j + dblOffset);

							for (j = jOld + 1; j < jNew; j++) {
								lStartPos = lSeamPoint[j] - 2;
								lEndPos = lSeamPoint[j] + 2;
								findEdgePoint_MinDiffSum_MemBase(j, lStartPos, lEndPos, lSeamPoint, _RIGHT_DIR);
							}
						}
					} else {
						for (j = (jOld + 1); j < (jNew); j++)
							lSeamPoint[j] = lSeamPoint[j - 1];
					}

					lSeamPoint[jNew] = iNew;
					j = jNew;

				} else if (nCheckingValue == 255)// no edge
				{
					jOld = j;
					iOld = lSeamPoint[j];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _RIGHT_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(jNew - jOld) - 1 > 5) {
						if (Math.abs(iNew - iOld) - 1 < 3)
							lSeamPoint[j] = lSeamPoint[j - 1];
						else {
							dblGain = (double) (iNew - iOld) / (double) (jNew - jOld);
							dblOffset = (double) iNew - dblGain * (double) jNew;
							for (j = (jOld + 1); j < (jNew); j++)
								lSeamPoint[j] = (int) Math.round(dblGain * (double) j + dblOffset);

							for (j = jOld + 1; j < jNew; j++) {

								lStartPos = lSeamPoint[j] - 2;
								lEndPos = lSeamPoint[j] + 2;
								findEdgePoint_MinDiffSum_MemBase(j, lStartPos, lEndPos, lSeamPoint, _RIGHT_DIR);
							}
						}
					} else {
						for (j = (jOld + 1); j < (jNew); j++)
							lSeamPoint[j] = lSeamPoint[j - 1];
					}

					lSeamPoint[jNew] = iNew;
					j = jNew;
				}

			} // end while

//			STEP 2-3
//			//LEFT DIRECTION
			j = _lCneterPointX; // initial value
			lSwHalf = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
			while (j > 0 && j < _lWidth - 1) {
				if (lSeamPoint[j] == -1) {
					while (lSeamPoint[j] == -1)
						j++;

					if (!getImageChecking(j, lSeamPoint[j])) {
						lSeamPoint[j] = lSeamPoint[j + 1];
						j--;
					}
				}

				nCheckingValue = getEdgeChecking_OR(lSeamPoint[j], j, masterEdgeInfo._GradMag, slaveEdgeInfo._GradMag,
						masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _LEFT_DIR);

				if (nCheckingValue == _LEFT_DIR)// left edge
				{
					lSeamPoint[j - 1] = lSeamPoint[j];
					j--;
				} else if (nCheckingValue == _LEFTUP_DIR)// left up edge
				{
					lSeamPoint[j - 1] = lSeamPoint[j] - 1;
					j--;
				} else if (nCheckingValue == _LEFTDOWN_DIR)// left down edge
				{
					lSeamPoint[j - 1] = lSeamPoint[j] + 1;
					j--;
				} else if (nCheckingValue == 0)// end edge
				{
					jOld = j;
					iOld = lSeamPoint[j];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _LEFT_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(jOld - jNew) - 1 > 5) {
						if (Math.abs(iNew - iOld) - 1 < 3)
							lSeamPoint[j] = lSeamPoint[j + 1];
						else {
							dblGain = (double) (iOld - iNew) / (double) (jOld - jNew);
							dblOffset = (double) iOld - dblGain * (double) jOld;
							for (j = (jOld - 1); j > (jNew); j--)
								lSeamPoint[j] = (int) Math.round(dblGain * (double) j + dblOffset);

							for (j = jOld + 1; j > jNew; j--) {
								lStartPos = lSeamPoint[j] - 2;
								lEndPos = lSeamPoint[j] + 2;
								findEdgePoint_MinDiffSum_MemBase(j, lStartPos, lEndPos, lSeamPoint, _LEFT_DIR);
							}
						}
					} else {
						for (j = (jOld - 1); j > (jNew); j--)
							lSeamPoint[j] = lSeamPoint[j + 1];
					}

					lSeamPoint[jNew] = iNew;
					j = jNew;
				} else if (nCheckingValue == 255)// no edge
				{
					jOld = j;
					iOld = lSeamPoint[j];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _LEFT_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(jOld - jNew) - 1 > 5) {
						if (Math.abs(iNew - iOld) - 1 < 3)
							lSeamPoint[j] = lSeamPoint[j + 1];
						else {
							dblGain = (double) (iOld - iNew) / (double) (jOld - jNew);
							dblOffset = (double) iOld - dblGain * (double) jOld;
							for (j = (jOld - 1); j > (jNew); j--)
								lSeamPoint[j] = (int) Math.round(dblGain * (double) j + dblOffset);

							for (j = jOld + 1; j > jNew; j--) {
								lStartPos = lSeamPoint[j] - 2;
								lEndPos = lSeamPoint[j] + 2;
								findEdgePoint_MinDiffSum_MemBase(j, lStartPos, lEndPos, lSeamPoint, _LEFT_DIR);
							}
						}
					} else {
						for (j = (jOld - 1); j > (jNew); j--)
							lSeamPoint[j] = lSeamPoint[j + 1];
					}

					lSeamPoint[jNew] = iNew;
					j = jNew;
				}

			} // end while

			for (j = _lCneterPointX + 1; j < _lWidth; j++) {
				if (lSeamPoint[j] == -1)
					lSeamPoint[j] = lSeamPoint[j - 1];
			}

			for (j = _lCneterPointX - 1; j >= 0; j--) {
				if (lSeamPoint[j] == -1)
					lSeamPoint[j] = lSeamPoint[j + 1];
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GSeamline.calcFeatureDetection_MinDiffSum_H_MemBase : (IndexOutOfBoundsException) "
					+ iobe.toString());
			iobe.printStackTrace();
		}

		// Free Memory
		////////////////////////////////////////////////
		if (masterEdgeInfo._EdgeImage != null)
			masterEdgeInfo._EdgeImage = null;
		if (masterEdgeInfo._GradMag != null)
			masterEdgeInfo._GradMag = null;

		if (slaveEdgeInfo._EdgeImage != null)
			slaveEdgeInfo._EdgeImage = null;
		if (slaveEdgeInfo._GradMag != null)
			slaveEdgeInfo._GradMag = null;
		////////////////////////////////////////////////

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("");
			System.out.println("[DEBUG]\t _lCneterPointX = " + _lCneterPointX + ", _lCneterPointY = " + _lCneterPointX);
			System.out.println("[DEBUG]\t End");
			System.out.println("");
		}
//[DEBUG]		

		return lSeamPoint;
	}

	// Feature Selection Using Canny Edge Operator �˰����� �����Ͽ� �������� Seam
	// Point�� ����Ѵ�.
	// @ return : int[] Seam Point ���
	private int[] calcFeatureDetection_MinDiffSum_V_MemBase() {
		int[] lSeamPoint = new int[_lHeight];

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GSeamline.calcFeatureDetection_MinDiffSum_V_MemBase : ");
			System.out.println("[DEBUG]\t Start : ");
		}
//[DEBUG]

//		STEP 1
		//////////////////////////////////////
		// Edge Detection

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t GET MASTER EDGE IMAGE ");
		}
//[DEBUG]

		// GET MASTER EDGE IMAGE
		GCannyEdgeDetection edge1 = new GCannyEdgeDetection();
		GCannyEdgeData masterEdgeInfo = edge1.procCannySeamlineOperator(_Master._imgData._pImage, _lHeight, _lWidth,
				_fGaussianSigma, _fLowThresh, _fHighThresh);

		////////////////////////////////////////////////////////////////////////////////////////////

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t GET SLAVE EDGE IMAGE ");
		}
//[DEBUG]

		// GET SLAVE EDGE IMAGE
		GCannyEdgeDetection edge2 = new GCannyEdgeDetection();
		GCannyEdgeData slaveEdgeInfo = edge2.procCannySeamlineOperator(_Slave._imgData._pImage, _lHeight, _lWidth,
				_fGaussianSigma, _fLowThresh, _fHighThresh);

		////////////////////////////////////////////////////////////////////////////////////////////

//		STEP 2
		///////////////////////////////////////
		// Seam line Extraction

		int i, j, k, lExtent, lStartCp, lEndCp;
		int lSwHalf = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
		int iOld, iNew, jOld, jNew;
		int lStartPos, lEndPos;
		boolean bInitPoint = false;
		double dblSumMean = 255.0, dblGain, dblOffset, dblSumMeanValue;
		int nSum = 0, nSumCount = 0;
		int nCheckingValue = 0;
		int masterValue = 0, slaveValue = 0;
		Coordinate pos = null;

		lExtent = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
		for (i = 0; i < _lHeight; i++)
			lSeamPoint[i] = -1;

//		STEP 2-1
		// GET INITIAL POINT(lCneterPointY)
		lStartCp = _lCneterPointX - (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);
		lEndCp = _lCneterPointX + (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 2.0);

		if (lStartCp < 50)
			lStartCp = 50;
		if (lEndCp > _lWidth - 50)
			lEndCp = _lWidth - 50;

		try {

			while (!bInitPoint) {
				for (j = lStartCp; j < lEndCp; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtent); k < (j + lExtent); k++) {
						if (k >= 0 && k < _lWidth) {
							masterValue = (0xff & _Master._imgData._pImage[k + _lWidth * _lCneterPointY]);
							slaveValue = (0xff & _Slave._imgData._pImage[k + _lWidth * _lCneterPointY]);
						}

						if (k >= 0 && masterValue != 0 && slaveValue != 0) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // 'k'

					if (nSum != 0 && nSumCount != 0) {
						dblSumMeanValue = (double) nSum / (double) nSumCount;

						if (dblSumMeanValue < dblSumMean) {
							if (masterEdgeInfo._EdgeImage[j + _lWidth * _lCneterPointY] == 0
									&& slaveEdgeInfo._EdgeImage[j + _lWidth * _lCneterPointY] == 0)// edge check
							{
								dblSumMean = dblSumMeanValue;
								lSeamPoint[_lCneterPointY] = j;
							}
						} else if (dblSumMeanValue == dblSumMean && Math.abs(j - _lCneterPointX) < Math
								.abs(lSeamPoint[_lCneterPointY] - _lCneterPointX)) {
							if (masterEdgeInfo._EdgeImage[j + _lWidth * _lCneterPointY] == 0
									&& slaveEdgeInfo._EdgeImage[j + _lWidth * _lCneterPointY] == 0)// edge check
							{
								dblSumMean = dblSumMeanValue;
								lSeamPoint[_lCneterPointY] = j;
							}
						}
					}

				} // 'j'

				if (dblSumMean == 255.0) {
					_lCneterPointY++;

					// @todo : ����ó�� (IndexOutOfBoundsException
					// if(_lCneterPointY >= _lHeight)
					if (_lCneterPointY >= (_lHeight - 1))
						break;
				} else
					bInitPoint = true;
			}

//			STEP 2-2
//			//DOWN DIRECTION
			i = _lCneterPointY; // initial value
			while (i > 0 && i < _lHeight - 1) {
				if (lSeamPoint[i] == -1) {
					while (lSeamPoint[i] == -1)
						i--;

					if (!getImageChecking(i, lSeamPoint[i])) {
						lSeamPoint[i] = lSeamPoint[i - 1];
						i++;
					}
				}

				nCheckingValue = getEdgeChecking_OR(i, lSeamPoint[i], masterEdgeInfo._GradMag, slaveEdgeInfo._GradMag,
						masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _DOWN_DIR);

				if (nCheckingValue == _DOWN_DIR)// down edge
				{
					lSeamPoint[i + 1] = lSeamPoint[i];
					i++;
				} else if (nCheckingValue == _LEFTDOWN_DIR)// left down edge
				{
					lSeamPoint[i + 1] = lSeamPoint[i] - 1;
					i++;
				} else if (nCheckingValue == _RIGHTDOWN_DIR)// right down edge
				{
					lSeamPoint[i + 1] = lSeamPoint[i] + 1;
					i++;
				} else if (nCheckingValue == 0)// end edge
				{
					iOld = i;
					jOld = lSeamPoint[i];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _DOWN_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(iNew - iOld) - 1 > 5) {
						if (Math.abs(jNew - jOld) - 1 < 3)
							lSeamPoint[i] = lSeamPoint[i - 1];
						else {
							dblGain = (double) (jNew - jOld) / (double) (iNew - iOld);
							dblOffset = (double) jNew - dblGain * (double) iNew;
							for (int kk = (iOld + 1); kk < (iNew); kk++)
								lSeamPoint[kk] = (int) Math.round(dblGain * (double) kk + dblOffset);

							for (k = iOld + 1; k < iNew; k++) {
								lStartPos = lSeamPoint[k] - 2;
								lEndPos = lSeamPoint[k] + 2;
								findEdgePoint_MinDiffSum_MemBase(k, lStartPos, lEndPos, lSeamPoint, _DOWN_DIR);
							}
						}
					} else {
						for (i = (iOld + 1); i < (iNew); i++)
							lSeamPoint[i] = lSeamPoint[i - 1];
					}

					lSeamPoint[iNew] = jNew;
					i = iNew;

				} else if (nCheckingValue == 255)// no edge
				{
					iOld = i;
					jOld = lSeamPoint[i];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _DOWN_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(iNew - iOld) - 1 > 5) {
						if (Math.abs(jNew - jOld) - 1 < 3)
							lSeamPoint[i] = lSeamPoint[i - 1];
						else {
							dblGain = (double) (jNew - jOld) / (double) (iNew - iOld);
							dblOffset = (double) jNew - dblGain * (double) iNew;
							for (int kk = (iOld + 1); kk < (iNew); kk++)
								lSeamPoint[kk] = (int) Math.round(dblGain * (double) kk + dblOffset);

							for (k = iOld + 1; k < iNew; k++) {

								lStartPos = lSeamPoint[k] - 2;
								lEndPos = lSeamPoint[k] + 2;
								findEdgePoint_MinDiffSum_MemBase(k, lStartPos, lEndPos, lSeamPoint, _DOWN_DIR);
							}
						}
					} else {
						for (k = (iOld + 1); k < (iNew); k++)
							lSeamPoint[k] = lSeamPoint[k - 1];
					}

					lSeamPoint[iNew] = jNew;
					i = iNew;
				}

			} // end while

//			STEP 2-3
//			//UP DIRECTION
			i = _lCneterPointY; // initial value
			lSwHalf = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
			while (i > 0 && i < _lHeight - 1) {
				if (lSeamPoint[i] == -1) {
					while (lSeamPoint[i] == -1)
						i++;

					if (!getImageChecking(i, lSeamPoint[i])) {
						lSeamPoint[i] = lSeamPoint[i + 1];
						i--;
					}
				}

				nCheckingValue = getEdgeChecking_OR(i, lSeamPoint[i], masterEdgeInfo._GradMag, slaveEdgeInfo._GradMag,
						masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _UP_DIR);

				if (nCheckingValue == _UP_DIR)// up edge
				{
					lSeamPoint[i - 1] = lSeamPoint[i];
					i--;
				} else if (nCheckingValue == _RIGHTUP_DIR)// right up edge
				{
					lSeamPoint[i - 1] = lSeamPoint[i] + 1;
					i--;
				} else if (nCheckingValue == _LEFTUP_DIR)// left up edge
				{
					lSeamPoint[i - 1] = lSeamPoint[i] - 1;
					i--;
				} else if (nCheckingValue == 0)// end edge
				{
					iOld = i;
					jOld = lSeamPoint[i];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _UP_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(iOld - iNew) - 1 > 5) {
						if (Math.abs(jNew - jOld) - 1 < 3)
							lSeamPoint[i] = lSeamPoint[i + 1];
						else {
							dblGain = (double) (jOld - jNew) / (double) (iOld - iNew);
							dblOffset = (double) jOld - dblGain * (double) iOld;
							for (int kk = (iOld - 1); kk > (iNew); kk--)
								lSeamPoint[kk] = (int) Math.round(dblGain * (double) kk + dblOffset);

							for (k = iOld - 1; k > iNew; k--) {

								lStartPos = lSeamPoint[k] - 2;
								lEndPos = lSeamPoint[k] + 2;
								findEdgePoint_MinDiffSum_MemBase(k, lStartPos, lEndPos, lSeamPoint, _UP_DIR);
							}
						}
					} else {
						for (k = (iOld - 1); k > (iNew); k--)
							lSeamPoint[k] = lSeamPoint[k + 1];
					}

					lSeamPoint[iNew] = jNew;
					i = iNew;
				} else if (nCheckingValue == 255)// no edge
				{
					iOld = i;
					jOld = lSeamPoint[i];
					pos = findEdgePoint_Distance_MemBase_OR(iOld, jOld, lSwHalf, masterEdgeInfo._GradMag,
							slaveEdgeInfo._GradMag, masterEdgeInfo._EdgeImage, slaveEdgeInfo._EdgeImage, _UP_DIR);
					iNew = (int) pos.y;
					jNew = (int) pos.x;

					if (Math.abs(iOld - iNew) - 1 > 5) {
						if (Math.abs(jNew - jOld) - 1 < 3)
							lSeamPoint[i] = lSeamPoint[i + 1];
						else {
							dblGain = (double) (jOld - jNew) / (double) (iOld - iNew);
							dblOffset = (double) jOld - dblGain * (double) iOld;
							for (int kk = (iOld - 1); kk > (iNew); kk--)
								lSeamPoint[kk] = (int) Math.round(dblGain * (double) kk + dblOffset);

							for (k = iOld - 1; k > iNew; k--) {

								lStartPos = lSeamPoint[k] - 2;
								lEndPos = lSeamPoint[k] + 2;
								findEdgePoint_MinDiffSum_MemBase(k, lStartPos, lEndPos, lSeamPoint, _UP_DIR);
							}
						}
					} else {
						for (k = (iOld - 1); k > (iNew); k--)
							lSeamPoint[k] = lSeamPoint[k + 1];
					}

					lSeamPoint[iNew] = jNew;
					i = iNew;
				}

			} // end while

			for (i = _lCneterPointY + 1; i < _lHeight; i++) {
				if (lSeamPoint[i] == -1)
					lSeamPoint[i] = lSeamPoint[i - 1];
			}

			for (i = _lCneterPointY - 1; i >= 0; i--) {
				if (lSeamPoint[i] == -1)
					lSeamPoint[i] = lSeamPoint[i + 1];
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GSeamline.calcFeatureDetection_MinDiffSum_V_MemBase : (IndexOutOfBoundsException) "
					+ iobe.toString());
			iobe.printStackTrace();
		}

		// Free Memory
		////////////////////////////////////////////////
		if (masterEdgeInfo._EdgeImage != null)
			masterEdgeInfo._EdgeImage = null;
		if (masterEdgeInfo._GradMag != null)
			masterEdgeInfo._GradMag = null;

		if (slaveEdgeInfo._EdgeImage != null)
			slaveEdgeInfo._EdgeImage = null;
		if (slaveEdgeInfo._GradMag != null)
			slaveEdgeInfo._GradMag = null;
		////////////////////////////////////////////////

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("");
			System.out.println("[DEBUG]\t _lCneterPointX = " + _lCneterPointX + ", _lCneterPointY = " + _lCneterPointX);
			System.out.println("[DEBUG]\t End");
			System.out.println("");
		}
//[DEBUG]

		return lSeamPoint;
	}

	// �ȼ��� ��ȿ���� ��ȯ�Ѵ�.
	// @ row : ���� ��ġ
	// @ col : ���� ��ġ
	// @
	// @ return : boolean ��ȿ��
	private boolean getImageChecking(int row, int col) {
		if ((0xff & _Master._imgData._pImage[col + _lWidth * row]) != 0
				&& (0xff & _Slave._imgData._pImage[col + _lWidth * row]) != 0)
			return true;
		else
			return false;
	}

	// �ش� ȭ�� ��ġ������ Edge�� ���⼺�� �˻��Ѵ�.
	// @ lRow : ������ǥ Y
	// @ lCol : ������ǥ X
	// @ grad1 : Master ������ 1�� ��̺� �̹���
	// @ grad2 : Slave ������ 1�� ��̺� �̹���
	// @ edge1 : Master ������ Edge Image
	// @ edge2 : Slave ������ Edge Image
	// @ nSearchDir : �˻� ����
	// @
	// @ return : int Edge�� ���⼺
	private int getEdgeChecking_OR(int lRow, int lCol, int[] grad1, int[] grad2, short[] edge1, short[] edge2,
			int nSearchDir) {

		/************************************
		 * <Search Direction Index> Up | 6 2 5 Left<- 3 P 1 -> Right 7 4 8 | Down
		 * 
		 * END Edge: return 0 NO Edge: return 255
		 *************************************/

		int nMinGrad = 0;

		try {

			if (nSearchDir == _RIGHT_DIR)// RIGHT DIRECTION
			{
				if ((edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
						|| edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE) && getImageChecking(lRow, lCol))// current
																														// edge
				{
					if (lCol + 1 < _lWidth)// right
					{
						nMinGrad = 300000;
						if ((edge1[(lCol + 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE
								|| edge2[(lCol + 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow, lCol + 1))// next edge
						{
							if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge1[(lCol + 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad1[lCol + _lWidth * lRow] - grad1[(lCol + 1) + _lWidth * lRow]) < nMinGrad)
									return _RIGHT_DIR;// right next edge
							}
							if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge2[(lCol + 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad2[lCol + _lWidth * lRow] - grad2[(lCol + 1) + _lWidth * lRow]) < nMinGrad)
									return _RIGHT_DIR;// right next edge
							}
						} else if (lRow + 1 < _lHeight)// right down
						{
							if ((edge1[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol + 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol + 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _RIGHTDOWN_DIR;// rigth down next edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol + 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _RIGHTDOWN_DIR;// right down next edge
								}
							}
						} else if (lRow - 1 >= 0)// right up
						{
							if ((edge1[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol + 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol + 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _RIGHTUP_DIR;// right up next edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol + 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _RIGHTUP_DIR;// right up next edge
								}
							}
						}
					}
				} else// no current edge
				{
					if (lCol + 1 < _lWidth) {
						if ((edge1[(lCol + 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE
								|| edge2[(lCol + 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow, lCol + 1))// next edge
						{
							return _RIGHT_DIR;// right next edge
						} else if (lRow + 1 < _lHeight) {
							if ((edge1[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol + 1))// next edge
								return _RIGHTDOWN_DIR;// rigth down next edge
						} else if (lRow - 1 >= 0) {
							if ((edge1[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol + 1))// next edge
								return _RIGHTUP_DIR;// right up next edge
						}
					}
				}
			} else if (nSearchDir == _UP_DIR)// UP DIRECTION
			{
				if ((edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
						|| edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE) && getImageChecking(lRow, lCol))// current
																														// edge
				{
					if (lRow - 1 >= 0) {
						nMinGrad = 300000;
						if ((edge1[lCol + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
								|| edge2[lCol + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow - 1, lCol))// next edge
						{
							if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge1[lCol + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad1[lCol + _lWidth * lRow] - grad1[lCol + _lWidth * (lRow - 1)]) < nMinGrad)
									return _UP_DIR;// up next edge
							}
							if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge2[lCol + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad2[lCol + _lWidth * lRow] - grad2[lCol + _lWidth * (lRow - 1)]) < nMinGrad)
									return _UP_DIR;// up next edge
							}
						} else if (lCol + 1 < _lWidth) {
							if ((edge1[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol + 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol + 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _RIGHTUP_DIR;// right up next edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol + 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _RIGHTUP_DIR;// right up next edge
								}
							}
						} else if (lCol - 1 >= 0) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol - 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol + 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _LEFTUP_DIR;// left up next edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol - 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _LEFTUP_DIR;// left up next edge
								}
							}
						}
					} else {
						return 0;// end current edge -> no next edge
					}
				} else// no current edge
				{
					if (lRow - 1 >= 0) {
						if ((edge1[lCol + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
								|| edge2[lCol + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow - 1, lCol)) {
							return _UP_DIR;// up next edge
						} else if (lCol + 1 < _lWidth) {
							if ((edge1[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol + 1))
								return _RIGHTUP_DIR;// right up next edge
						} else if (lCol - 1 >= 0) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol - 1))
								return _LEFTUP_DIR;// left up next edge
						}
					}
				}
			} else if (nSearchDir == _LEFT_DIR)// LEFT DIRECTION
			{
				if ((edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
						|| edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE) && getImageChecking(lRow, lCol))// current
																														// edge
				{
					if (lCol - 1 >= 0) {
						nMinGrad = 300000;
						if ((edge1[(lCol - 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE
								|| edge2[(lCol - 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow, lCol - 1))// next edge
						{
							if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge1[(lCol - 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad1[lCol + _lWidth * lRow] - grad1[(lCol - 1) + _lWidth * lRow]) < nMinGrad)
									return _LEFT_DIR;// left next edge
							}
							if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge2[(lCol - 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad2[lCol + _lWidth * lRow] - grad2[(lCol - 1) + _lWidth * lRow]) < nMinGrad)
									return _LEFT_DIR;// left next edge
							}
						} else if (lRow - 1 >= 0) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol - 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol - 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _LEFTUP_DIR;// left up next edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol - 1) + _lWidth * (lRow - 1)]) < nMinGrad)
										return _LEFTUP_DIR;// left up next edge
								}
							}
						} else if (lRow + 1 < _lHeight) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol - 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol - 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _LEFTDOWN_DIR;// left down next edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol - 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _LEFTDOWN_DIR;// left down next edge
								}
							}
						}
					}
				} else// no current edge
				{
					if (lCol - 1 >= 0) {
						if ((edge1[(lCol - 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE
								|| edge2[(lCol - 1) + _lWidth * lRow] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow, lCol - 1))// next edge
						{
							return _LEFT_DIR;// left next edge
						} else if (lRow - 1 >= 0) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow - 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow - 1, lCol - 1))// next edge
								return _LEFTUP_DIR;// left up next edge
						} else if (lRow + 1 < _lHeight) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol - 1))// next edge
								return _LEFTDOWN_DIR;// left down next edge
						}
					}
				}
			} else if (nSearchDir == _DOWN_DIR)// DOWN DIRECTION
			{
				if ((edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
						|| edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE) && getImageChecking(lRow, lCol))// current
																														// edge
				{
					if (lRow + 1 < _lHeight) {
						nMinGrad = 30000;

						if ((edge1[lCol + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
								|| edge2[lCol + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow + 1, lCol))// next edge
						{
							if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge1[lCol + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad1[lCol + _lWidth * lRow] - grad1[lCol + _lWidth * (lRow + 1)]) < nMinGrad)
									return _DOWN_DIR;// down next edge
							}
							if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
									&& edge2[lCol + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
								if (Math.abs(
										grad2[lCol + _lWidth * lRow] - grad2[lCol + _lWidth * (lRow + 1)]) < nMinGrad)
									return _DOWN_DIR;// down next edge
							}
						} else if (lCol - 1 >= 0) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol - 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol - 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _LEFTDOWN_DIR;// left down edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol - 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _LEFTDOWN_DIR;// left down edge
								}
							}
						} else if (lCol + 1 < _lWidth) {
							if ((edge1[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol + 1))// next edge
							{
								if (edge1[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge1[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad1[lCol + _lWidth * lRow]
											- grad1[(lCol + 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _RIGHTDOWN_DIR;// right down edge
								}
								if (edge2[lCol + _lWidth * lRow] == GCannyEdgeDetection._EDGE
										&& edge2[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE) {
									if (Math.abs(grad2[lCol + _lWidth * lRow]
											- grad2[(lCol + 1) + _lWidth * (lRow + 1)]) < nMinGrad)
										return _RIGHTDOWN_DIR;// right down edge
								}
							}
						}
					} else {
						return 0;// end current edge -> no next edge
					}
				} else {
					if (lRow + 1 < _lHeight)// no current edge
					{
						if ((edge1[lCol + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
								|| edge2[lCol + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
								&& getImageChecking(lRow + 1, lCol))// next edge
						{
							return _DOWN_DIR;// down next edge
						} else if (lCol - 1 >= 0) {
							if ((edge1[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol - 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol - 1))// next edge
								return _LEFTDOWN_DIR;// left down edge
						} else if (lCol + 1 < _lWidth) {
							if ((edge1[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE
									|| edge2[(lCol + 1) + _lWidth * (lRow + 1)] == GCannyEdgeDetection._EDGE)
									&& getImageChecking(lRow + 1, lCol + 1))// next edge
								return _RIGHTDOWN_DIR;// right down edge
						}
					}
				}
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GSeamline.getEdgeChecking_OR : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return 255; // no current edge!!
	}

	// �Ÿ� ���� Edge Point�� �˻��Ѵ�.
	// @ lOldRow : ������ǥ Y
	// @ lOldCol : ������ǥ X
	// @ lSwHalf : �˻� ������ �߰���
	// @ grad1 : Master ������ 1�� ��̺� �̹���
	// @ grad2 : Slave ������ 1�� ��̺� �̹���
	// @ edge1 : Master ������ Edge Image
	// @ edge2 : Slave ������ Edge Image
	// @ nSearchDir : �˻� ����
	// @
	// @ return : Coordinate Edge Point
	private Coordinate findEdgePoint_Distance_MemBase_OR(int lOldRow, int lOldCol, int lSwHalf, int[] grad1,
			int[] grad2, short[] edge1, short[] edge2, int nSearchDir) {
		Coordinate edge_point = new Coordinate(-1, -1);
		int lCenterPos = 0, lStartPos = 0, lEndPos = 0;
		int nMaxGrad = 0, nCheckingValue = 0;
		int i = 0, j = 0;

		try {

			if (nSearchDir == _DOWN_DIR || nSearchDir == _UP_DIR) {
				lCenterPos = lOldCol;
				lStartPos = lOldCol - lSwHalf;
				lEndPos = lOldCol + lSwHalf;

				if (lStartPos < 50)
					lStartPos = 50;
				if (lEndPos > _lWidth - 50)
					lEndPos = _lWidth - 50;
			} else {
				lCenterPos = lOldRow;
				lStartPos = lOldRow - lSwHalf;
				lEndPos = lOldRow + lSwHalf;

				if (lStartPos < 50)
					lStartPos = 50;
				if (lEndPos > _lHeight - 50)
					lEndPos = _lHeight - 50;
			}

			if (nSearchDir == _DOWN_DIR)// down
			{
				for (i = lOldRow + 1; i < _lHeight; i++) {
					if (i == _lHeight - 1) {
						edge_point.y = i;
						edge_point.x = lOldCol;
					} else {
						nMaxGrad = 0;

						for (j = lStartPos; j < lCenterPos; j++) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _DOWN_DIR);

								if (nCheckingValue == _DOWN_DIR || nCheckingValue == _LEFTDOWN_DIR
										|| nCheckingValue == _RIGHTDOWN_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'j'

						for (j = lEndPos; j >= lCenterPos; j--) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _DOWN_DIR);

								if (nCheckingValue == _DOWN_DIR || nCheckingValue == _LEFTDOWN_DIR
										|| nCheckingValue == _RIGHTDOWN_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'j'

					}

					if (edge_point.y != -1 && edge_point.x != -1)
						break;

				} // 'i'
			} else if (nSearchDir == _UP_DIR)// Up
			{
				for (i = lOldRow - 1; i >= 0; i--) {
					if (i == 0) {
						edge_point.y = i;
						edge_point.x = lOldCol;
					} else {
						nMaxGrad = 0;

						for (j = lEndPos; j >= lCenterPos; j--) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _UP_DIR);

								if (nCheckingValue == _UP_DIR || nCheckingValue == _RIGHTUP_DIR
										|| nCheckingValue == _LEFTUP_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'j'

						for (j = lStartPos; j < lCenterPos; j++) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _UP_DIR);

								if (nCheckingValue == _UP_DIR || nCheckingValue == _RIGHTUP_DIR
										|| nCheckingValue == _LEFTUP_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'j'
					}

					if (edge_point.y != -1 && edge_point.x != -1)
						break;

				} // 'i'
			} else if (nSearchDir == _RIGHT_DIR)// right
			{
				for (j = lOldCol + 1; j < _lWidth; j++) {
					if (j == _lWidth - 1) {
						edge_point.y = lOldRow;
						edge_point.x = j;
					} else {
						nMaxGrad = 0;

						for (i = lEndPos; i >= lCenterPos; i--) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _RIGHT_DIR);

								if (nCheckingValue == _RIGHT_DIR || nCheckingValue == _RIGHTDOWN_DIR
										|| nCheckingValue == _RIGHTUP_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'i'

						for (i = lStartPos; i < lCenterPos; i++) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _RIGHT_DIR);

								if (nCheckingValue == _RIGHT_DIR || nCheckingValue == _RIGHTDOWN_DIR
										|| nCheckingValue == _RIGHTUP_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'i'
					}

					if (edge_point.y != -1 && edge_point.x != -1)
						break;

				} // 'i'
			} else if (nSearchDir == _LEFT_DIR)// Left
			{
				for (j = lOldCol - 1; j >= 0; j--) {
					if (j == 0) {
						edge_point.y = lOldRow;
						edge_point.x = j;
					} else {
						nMaxGrad = 0;

						for (i = lStartPos; i < lCenterPos; i++) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _LEFT_DIR);

								if (nCheckingValue == _LEFT_DIR || nCheckingValue == _LEFTUP_DIR
										|| nCheckingValue == _LEFTDOWN_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'i'

						for (i = lEndPos; i >= lCenterPos; i--) {
							if ((edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
									|| edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE) && getImageChecking(i, j)) {
								nCheckingValue = getEdgeChecking_OR(i, j, grad1, grad2, edge1, edge2, _LEFT_DIR);

								if (nCheckingValue == _LEFT_DIR || nCheckingValue == _LEFTUP_DIR
										|| nCheckingValue == _LEFTDOWN_DIR) {
									if (edge1[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad1[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad1[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}

									if (edge2[j + _lWidth * i] == GCannyEdgeDetection._EDGE
											&& grad2[j + _lWidth * i] > nMaxGrad) {
										nMaxGrad = grad2[j + _lWidth * i];
										edge_point.y = i;
										edge_point.x = j;
									}
								}
							}
						} // 'i'

					}

					if (edge_point.y != -1 && edge_point.x != -1)
						break;

				} // 'i'
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GSeamline.findEdgePoint_Distance_MemBase_OR : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return edge_point;
	}

	// Minimum Gray Difference Sum ���� Edge Point�� �˻��Ѵ�.
	// @ pos : �˻� ��ġ(�ε���)
	// @ search1 : �˻� ���� ���� ��ġ(�ε���)
	// @ search2 : �˻� ���� ���� ��ġ(�ε���)
	// @ lSeamp : Seam Point ���
	// @ nSearchDir : �˻� ����
	private void findEdgePoint_MinDiffSum_MemBase(int pos, int search1, int search2, int[] lSeamp, int nSearchDir) {
		double dblSumMean = 255; // Init SumMean each column
		int nSum = 0, nSumCount = 0;
		double dblSumMeanTmp = 0.0;
		int lCheckX = 0, lCheckY = 0;
		int lExtend = (int) ((double) _pMosaicAlgorithmData._lSearchWidth / 10.0);
		int masterValue = 0, slaveValue = 0;
		int j = 0, k = 0;

		try {

			if (nSearchDir == _DOWN_DIR) {
				lCheckX = lSeamp[pos - 1];
				for (j = search1; j <= search2; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						masterValue = 0;
						slaveValue = 0;

						if (k >= 0 && k < _lWidth) {
							masterValue = (0xff & _Master._imgData._pImage[k + _lWidth * pos]);
							slaveValue = (0xff & _Slave._imgData._pImage[k + _lWidth * pos]);
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dblSumMeanTmp = (double) nSum / (double) nSumCount;
						if (dblSumMeanTmp < dblSumMean) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						} else if ((dblSumMeanTmp == dblSumMean)
								&& (Math.abs(j - lCheckX) < Math.abs(lSeamp[pos] - lCheckX))) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						}
					}

				} // end width(j)

				// Case: Seam Point is not selected -> Select a former seam point
				if (dblSumMean == 255)
					lSeamp[pos] = lSeamp[pos - 1];

			} else if (nSearchDir == _UP_DIR) {
				// Up Direction
				lCheckX = lSeamp[pos + 1];
				for (j = search1; j <= search2; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						masterValue = 0;
						slaveValue = 0;

						if (k >= 0 && k < _lWidth) {
							masterValue = (0xff & _Master._imgData._pImage[k + _lWidth * pos]);
							slaveValue = (0xff & _Slave._imgData._pImage[k + _lWidth * pos]);
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dblSumMeanTmp = (double) nSum / (double) nSumCount;
						if (dblSumMeanTmp < dblSumMean) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						} else if ((dblSumMeanTmp == dblSumMean)
								&& (Math.abs(j - lCheckX) < Math.abs(lSeamp[pos] - lCheckX))) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						}
					}

				} // end m_lWidth(j)

				// Case: Seam Point is not selected -> Select a former seam point
				if (dblSumMean == 255)
					lSeamp[pos] = lSeamp[pos + 1];
			} else if (nSearchDir == _RIGHT_DIR) {
				lCheckY = lSeamp[pos - 1];

				for (j = search1; j <= search2; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						masterValue = 0;
						slaveValue = 0;

						if (k >= 0 && k < _lHeight) {
							masterValue = (0xff & _Master._imgData._pImage[pos + _lWidth * k]); // get master image
																								// value
							slaveValue = (0xff & _Slave._imgData._pImage[pos + _lWidth * k]); // get slave image value
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dblSumMeanTmp = (double) nSum / (double) nSumCount;
						if (dblSumMeanTmp < dblSumMean) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						} else if ((dblSumMeanTmp == dblSumMean)
								&& (Math.abs(j - lCheckY) < Math.abs(lSeamp[pos] - lCheckY))) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						}
					}

				} // end height(j)

				// Case: Seam Point is not selected -> Select a former seam point
				if (dblSumMean == 255)
					lSeamp[pos] = lSeamp[pos - 1];

			} else if (nSearchDir == _LEFT_DIR) {
				lCheckY = lSeamp[pos + 1];

				for (j = search1; j <= search2; j++) {
					nSum = 0;
					nSumCount = 0;

					for (k = (j - lExtend); k < (j + lExtend); k++) {
						masterValue = 0;
						slaveValue = 0;
						if (k >= 0 && k < _lHeight) {
							masterValue = (0xff & _Master._imgData._pImage[pos + _lWidth * k]);
							slaveValue = (0xff & _Slave._imgData._pImage[pos + _lWidth * k]);
						}

						if ((k >= 0) && (masterValue != 0) && (slaveValue != 0)) {
							nSum += Math.abs(masterValue - slaveValue);
							nSumCount++;
						} else {
							nSum = 0;
							nSumCount = 0;
							break;
						}
					} // end searching area(k)

					if (nSum != 0 && nSumCount != 0) {
						dblSumMeanTmp = (double) nSum / (double) nSumCount;
						if (dblSumMeanTmp < dblSumMean) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						} else if ((dblSumMeanTmp == dblSumMean)
								&& (Math.abs(j - lCheckY) < Math.abs(lSeamp[pos] - lCheckY))) {
							dblSumMean = dblSumMeanTmp;
							lSeamp[pos] = j;
						}
					}

				} // end m_lHeight(j)

				// Case: Seam Point is not selected -> Select a former seam point
				if (dblSumMean == 255)
					lSeamp[pos] = lSeamp[pos + 1];
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GSeamline.findEdgePoint_MinDiffSum_MemBase : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

}
