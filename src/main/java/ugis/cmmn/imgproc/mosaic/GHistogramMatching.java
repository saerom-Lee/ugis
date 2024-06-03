package ugis.cmmn.imgproc.mosaic;

import org.geotools.geometry.Envelope2D;

import ugis.cmmn.imgproc.GImageEnhancement;
import ugis.cmmn.imgproc.data.GHistogramMatchingData;
import ugis.cmmn.imgproc.data.GMosaicHistogramData;
import ugis.cmmn.imgproc.data.GMosaicProcData;

//������׷� ��Ī ���� Ŭ����
public class GHistogramMatching {

	///////////////////////////////////////////////////////////////////////////
	// Output

	// ��� ������׷� ����
	private GMosaicHistogramData _outHistoData = null;

	///////////////////////////////////////////////////////////////////////////
	// Input

	// Master ���� (��󿵻�)
	private GMosaicProcData _Master = null;

	// Slave ���� (���ؿ���)
	private GMosaicProcData _Slave = null;

	// �޸� ����
	private int _nDevice = 0; // 0: memory, 1: file(raw file)

	// Gray ����
	private int _nGrayLevel = 256;

	// Master �̹��� ���뿵�� ����
	private Envelope2D _mbr2dMaster = null;

	// Slave �̹��� ���뿵�� ����
	private Envelope2D _mbr2dSlave = null;

	// ������׷� ��Ī ����
	public GImageEnhancement.HistorgramMatchingMethod _nHistogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT;

	// ������׷� ��Ī�� ���� Feather Width ���� ����
	public boolean _bFeatherWidth = true;

	// ������׷� ��Ī�� ���� Feather Width ũ��
	public int _lFeatherWidth = 10;

	// �˻� ������ ũ��
	private int _lSearchWidth = 100; // 200;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ������׷� ��Ī�� �����Ѵ�.
	// @ nHistogramMatchingMethod : ������׷� ��Ī ����
	// @ bFeatherWidth : ������׷� ��Ī�� ���� Feather Width ���� ����
	// @ lFeatherWidth : ������׷� ��Ī�� ���� Feather Width ũ��
	// @ lSearchWidth : �˻� ������ ũ��
	// @ master : Master ���� (��󿵻�)
	// @ slave : Slave ���� (���ؿ���)
	// @ mbr2dMaster : Master �̹��� ���뿵�� ���� (��󿵻�)
	// @ mbr2dSlave : Slave �̹��� ���뿵�� ���� (���ؿ���)
	// @ lSeamp : Seam Point ���
	// @
	// @ return : GMosaicHistogramData ������׷� ��Ī ���
	public GMosaicHistogramData procHistogramMatching(
			GImageEnhancement.HistorgramMatchingMethod nHistogramMatchingMethod, boolean bFeatherWidth,
			int lFeatherWidth, int lSearchWidth, GMosaicProcData master, GMosaicProcData slave, Envelope2D mbr2dMaster,
			Envelope2D mbr2dSlave, int[] lSeamp) {

		// Input
		_nHistogramMatchingMethod = nHistogramMatchingMethod;
		_bFeatherWidth = bFeatherWidth;
		_lFeatherWidth = lFeatherWidth;
		_lSearchWidth = lSearchWidth;
		_Master = master;
		_Slave = slave;
		_mbr2dMaster = mbr2dMaster;
		_mbr2dSlave = mbr2dSlave;

		// Output
		_outHistoData = new GMosaicHistogramData();
		_outHistoData._lHeight = 0;
		_outHistoData._lWidth = 0;
		_outHistoData._histMatchInfo._dblMeanMaster = 0.0; // Match Mean & Std. Dev.
		_outHistoData._histMatchInfo._dblMeanSlave = 0.0;
		_outHistoData._histMatchInfo._dblVariance = 0.0;
		_outHistoData._histMatchInfo._dblCovariance = 0.0;
		_outHistoData._histMatchInfo._nMinMaster = 0; // Hue Adjustment & Match Cumulative Frequency
		_outHistoData._histMatchInfo._nMinSlave = 0;
		_outHistoData._histMatchInfo._nMaxMaster = 0;
		_outHistoData._histMatchInfo._nMaxSlave = 0;
		_outHistoData._histMatchInfo._nLUTModified = null; // Match Cumulative Frequency

		// {Get Data
		_outHistoData._lHeight = (int) _Master._imgData._imgBox2d.getHeight();
		_outHistoData._lWidth = (int) _Master._imgData._imgBox2d.getWidth();
		_nDevice = 0; // nDevice;

		// Overlap Path, Row ����!!(GetOverlapShape�� ���� ���� ���¸�!!)
		int nShape = getOverlapShape(_outHistoData._lHeight, _outHistoData._lWidth);

		_outHistoData._pImage = calcHistogramMatchingData(nShape, lSeamp);

		// Feathering
		if (_bFeatherWidth) {
			int fw = _lFeatherWidth;
			if (fw > 0) {
				if (_nDevice == 0)
					calcFeatheringMem(fw, nShape, lSeamp);
			}
		}

		// Set Histogram Data
		if (_nDevice == 0)
			_outHistoData._nDeviceIndex = 0;
		if (_nDevice == 1)
			_outHistoData._nDeviceIndex = 1;

		return _outHistoData;
	}

	// Overlap Path, Row ���� ���¸� ��ȯ�Ѵ�.
	// @ lHieght : ���� ���� ũ��
	// @ lWidth : ���� ���� ũ��
	// @
	// @ return : int Overlap Path, Row ���� ���� (0 : wide, 1 : narrow)
	public int getOverlapShape(int lHeight, int lWidth) {
		int nShape = -1;
		if (lHeight <= lWidth)
			nShape = 0; // wide
		else if (lHeight > lWidth)
			nShape = 1; // narrow
		return nShape;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ������׷� ��Ī ����� ����Ѵ�.
	// @ nShape : Overlap Path, Row ���� ����
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcHistogramMatchingData(int nShape, int[] lSeamp) {
		byte[] outPixles = null;

		/* ############ Histogram Matching Algorithm ############## */
		// 0 : None
		// 1 : Match Mean & Std. Dev.
		// 2 : Match Cumulative Frequency
		// 3 : Hue Adjustment Through Moving the Histogram

		if (_nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.NONE) // None
		{
			if (nShape == 0) // Horizon
			{
				// if(_nDevice == 0)
				outPixles = calcNoneAlgorithmH_Mem(lSeamp);
			}
			if (nShape == 1) // Vertical
			{
				// if(_nDevice == 0)
				outPixles = calcNoneAlgorithmV_Mem(lSeamp);
			}
		} else if (_nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATCH_MEAN_STD_DEV) // Match
																												// Mean
																												// &
																												// Std.
																												// Dev.
		{
			if (nShape == 0) // Horizon
			{
				// if(_nDevice == 0)
				outPixles = calcMatchMeanStdDevH_Mem(lSeamp);
			}
			if (nShape == 1) // Vertical
			{
				// if(_nDevice == 0)
				outPixles = calcMatchMeanStdDevV_Mem(lSeamp);
			}
		} else if (_nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.MATH_CUMULATIVE_FREQUENCY) // Match
																														// Cumulative
																														// Frequency
		{
			if (nShape == 0) // Horizon
			{
				// if(_nDevice == 0)
				outPixles = calcMatchCumulativeFrequencyH_Mem(lSeamp);
			}
			if (nShape == 1) // Vertical
			{
				// if(_nDevice == 0)
				outPixles = calcMatchCumulativeFrequencyV_Mem(lSeamp);
			}
		} else if (_nHistogramMatchingMethod == GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT) // Hue
																											// Adjustment
		{
			if (nShape == 0) // Horizon
			{
				// if(_nDevice == 0)
				outPixles = calcHueAdjustmentH_Mem(lSeamp);
			}
			if (nShape == 1) // Vertical
			{
				// if(_nDevice == 0)
				outPixles = calcHueAdjustmentV_Mem(lSeamp);
			}
		}

		return outPixles;
	}

	// �˰��� ������� ������� ������׷� ��Ī ����� ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcNoneAlgorithmH_Mem(int[] lSeamPoint) {
		// None Histogram Matching Algorithm
		// The shape of horizontal direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		int nIndex = 0;

		try {

			// Creat Mosaic Image of Overlap Area
			if (_mbr2dMaster.getMaxY() > _mbr2dSlave.getMaxY()) {
				for (int i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (int j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i <= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];

						} else if (i > lSeamPoint[j]) // slave
						{
							OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];
							if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
						}
					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMaxY() < _mbr2dSlave.getMaxY()) {
				for (int i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (int j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i >= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];
						} else if (i < lSeamPoint[j]) // slave
						{
							OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];
							if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GHistogramMatching.calcNoneAlgorithmH_Mem : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return OV_IMG;
	}

	// �˰��� ������� �������� ������׷� ��Ī ����� ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcNoneAlgorithmV_Mem(int[] lSeamPoint) {
		// None Histogram Matching Algorithm
		// The shape of vertical direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		int nIndex = 0;

		try {

			// Creat Mosaic Image of Overlap Area
			if (_mbr2dMaster.getMinX() > _mbr2dSlave.getMinX()) {
				for (int i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (int j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j >= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];
						} else if (j < lSeamPoint[i]) // slave
						{
							OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];
							if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
						}
					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMinX() < _mbr2dSlave.getMinX()) {
				for (int i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (int j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j <= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];
						} else if (j > lSeamPoint[i]) // slave
						{
							OV_IMG[nIndex] = _Slave._imgData._pImage[nIndex];
							if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GHistogramMatching.calcNoneAlgorithmV_Mem : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return OV_IMG;
	}

	// Match Mean & Std. Dev. �˰����� �����Ͽ� ������� ������׷� ��Ī ����� ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcMatchMeanStdDevH_Mem(int[] lSeamPoint) {
		// Match Mean Standard Deviation
		// The shape of horizontal direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		GHistogramMatchingData histMatchInfo = _outHistoData._histMatchInfo;

		int i = 0, j = 0;
		int lStartRow = 0, lEndRow = 0;
		int lCalH = (int) ((double) _lSearchWidth / 2.0);

		// Calculate Mean of each image within searching buffer
		double dwSumMaster = 0.0, dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;
		double dblMatched_DN = 0;// ȭ�Ұ� ���� ó�� (10.19)
		int Matched_DN = 0;

		try {

			for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
			{
				// {get extent of row
				lStartRow = lSeamPoint[j] - lCalH;
				lEndRow = lSeamPoint[j] + lCalH;
				if (lStartRow < 0)
					lStartRow = 0;
				if (lEndRow > _outHistoData._lHeight)
					lEndRow = _outHistoData._lHeight;
				// }

				for (i = lStartRow; i < lEndRow; i++) // row(i)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						dwSumMaster += (double) (0xff & _Master._imgData._pImage[nIndex]);
						dwSumSlave += (double) (0xff & _Slave._imgData._pImage[nIndex]);
						lCount++;
					}
				} // end row(j)
			} // end col(i)

			histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
			histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

			// Calculate m_outHistoData.dblVariance and m_outHistoData.dblCovariance of the
			// two images within searching buffer
			histMatchInfo._dblVariance = 0.0;
			histMatchInfo._dblCovariance = 0.0;

			for (j = 0; j < _outHistoData._lWidth; j++) // column(j)
			{
				// {get extent of row
				lStartRow = lSeamPoint[j] - lCalH;
				lEndRow = lSeamPoint[j] + lCalH;
				if (lStartRow < 0)
					lStartRow = 0;
				if (lEndRow > _outHistoData._lHeight)
					lEndRow = _outHistoData._lHeight;
				// }

				for (i = lStartRow; i < lEndRow; i++) // row(i)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						histMatchInfo._dblVariance += ((double) (0xff & _Slave._imgData._pImage[nIndex])
								- histMatchInfo._dblMeanSlave)
								* ((double) (0xff & _Slave._imgData._pImage[nIndex]) - histMatchInfo._dblMeanSlave);
						histMatchInfo._dblCovariance += ((double) (0xff & _Master._imgData._pImage[nIndex])
								- histMatchInfo._dblMeanMaster)
								* ((double) (0xff & _Slave._imgData._pImage[nIndex]) - histMatchInfo._dblMeanSlave);
					}
				} // end row(j)
			} // end col(i)

			// Create Mosaicked Image within the Overlap Area
			// Horizontal direction

			if (_mbr2dMaster.getMaxY() > _mbr2dSlave.getMaxY()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i <= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
							{
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (i > lSeamPoint[j]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// exception
								OV_IMG[nIndex] = (byte) 0;
							}
						}

					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMaxY() < _mbr2dSlave.getMaxY()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i >= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
							{
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (i < lSeamPoint[j]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// exception
								OV_IMG[nIndex] = (byte) 0;
							}
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GHistogramMatching.calcMatchMeanStdDevH_Mem : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return OV_IMG;
	}

	// Match Mean & Std. Dev. �˰����� �����Ͽ� �������� ������׷� ��Ī ����� ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcMatchMeanStdDevV_Mem(int[] lSeamPoint) {
		// Match Mean Standard Deviation
		// The shape of vertical direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		GHistogramMatchingData histMatchInfo = _outHistoData._histMatchInfo;

		int i = 0, j = 0;
		int lStartCol = 0, lEndCol = 0; // Initialize...
		int lCalW = (int) ((double) _lSearchWidth / 2.0);

		// Calculate Mean of each image within searching buffer
		double dwSumMaster = 0.0;
		double dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;
		double dblMatched_DN = 0;// ȭ�Ұ� ���� ó�� (10.19)
		int Matched_DN = 0;

		try {

			for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
			{
				// {get extent of row
				lStartCol = lSeamPoint[i] - lCalW;
				lEndCol = lSeamPoint[i] + lCalW;
				if (lStartCol < 0)
					lStartCol = 0;
				if (lEndCol > _outHistoData._lWidth)
					lEndCol = _outHistoData._lWidth;
				// }

				for (j = lStartCol; j < lEndCol; j++) // col(j)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						dwSumMaster += (double) (0xff & _Master._imgData._pImage[nIndex]);
						dwSumSlave += (double) (0xff & _Slave._imgData._pImage[nIndex]);
						lCount++;
					}
				} // end row(j)
			} // end col(i)

			histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
			histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

			// Calculate m_outHistoData.dblVariance and m_outHistoData.dblCovariance of the
			// two images within searching buffer
			histMatchInfo._dblVariance = 0.0;
			histMatchInfo._dblCovariance = 0.0;

			for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
			{
				// {get extent of row
				lStartCol = lSeamPoint[i] - lCalW;
				lEndCol = lSeamPoint[i] + lCalW;
				if (lStartCol < 0)
					lStartCol = 0;
				if (lEndCol > _outHistoData._lWidth)
					lEndCol = _outHistoData._lWidth;
				// }

				for (j = lStartCol; j < lEndCol; j++) // col(j)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						histMatchInfo._dblVariance += ((double) (0xff & _Slave._imgData._pImage[nIndex])
								- histMatchInfo._dblMeanSlave)
								* ((double) (0xff & _Slave._imgData._pImage[nIndex]) - histMatchInfo._dblMeanSlave);
						histMatchInfo._dblCovariance += ((double) (0xff & _Master._imgData._pImage[nIndex])
								- histMatchInfo._dblMeanMaster)
								* ((double) (0xff & _Slave._imgData._pImage[nIndex]) - histMatchInfo._dblMeanSlave);
					}
				} // end row(j)
			} // end col(i)

			// Creat Mosaic Image of Overlap Area
			// Vertical direction

			if (_mbr2dMaster.getMinX() > _mbr2dSlave.getMinX()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j >= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {// exception
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (j < lSeamPoint[i]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// exception
								OV_IMG[nIndex] = (byte) 0;
							}
						}
					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMinX() < _mbr2dSlave.getMinX()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j <= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {// exception
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (j > lSeamPoint[i]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = (histMatchInfo._dblCovariance / histMatchInfo._dblVariance
										* ((double) (0xff & _Slave._imgData._pImage[nIndex])
												- histMatchInfo._dblMeanSlave)
										+ histMatchInfo._dblMeanMaster);
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// exception
								OV_IMG[nIndex] = (byte) 0;
							}
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GHistogramMatching.calcMatchMeanStdDevV_Mem : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return OV_IMG;
	}

	// Match Cumulative Frequency �˰����� �����Ͽ� ������� ������׷� ��Ī ����� ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcMatchCumulativeFrequencyH_Mem(int[] lSeamPoint) {
		// Match Cumulative Frequency
		// The shape of horizontal direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		GHistogramMatchingData histMatchInfo = _outHistoData._histMatchInfo;

		int i = 0, j = 0; // Initialize...
		int Master_DN = 0, Slave_DN = 0; // Initialize...
		histMatchInfo._nMinMaster = 255;
		histMatchInfo._nMinSlave = 255;
		histMatchInfo._nMaxMaster = 0;
		histMatchInfo._nMaxSlave = 0;
		histMatchInfo._nLUTModified = new int[_nGrayLevel];

		int[] nMasterHistogram = new int[_nGrayLevel];
		int[] nSlaveHistogram = new int[_nGrayLevel];

		for (i = 0; i < _nGrayLevel; i++) {
			nMasterHistogram[i] = 0;
			nSlaveHistogram[i] = 0;

			// Look Up Table �ʱ�ȭ (10.19)
			histMatchInfo._nLUTModified[i] = i;
		}

		// Get Max, Min, Mean
		int lCalH = (int) ((double) _lSearchWidth / 2.0);
		int lStartRow = 0, lEndRow = 0;
		double dwSumMaster = 0.0;
		double dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;
		int nDiff1 = 0, nDiff2 = 0;
		int nRoopCount = 0;

		try {

			for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
			{
				// {get extent of row
				lStartRow = lSeamPoint[j] - lCalH;
				lEndRow = lSeamPoint[j] + lCalH;
				if (lStartRow < 0)
					lStartRow = 0;
				if (lEndRow > _outHistoData._lHeight)
					lEndRow = _outHistoData._lHeight;

				for (i = lStartRow; i < lEndRow; i++) // row(i)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						lCount++;
						Master_DN = (0xff & _Master._imgData._pImage[nIndex]);
						Slave_DN = (0xff & _Slave._imgData._pImage[nIndex]);

						dwSumMaster += (double) (0xff & _Master._imgData._pImage[nIndex]);
						dwSumSlave += (double) (0xff & _Slave._imgData._pImage[nIndex]);

						nMasterHistogram[Master_DN]++;
						nSlaveHistogram[Slave_DN]++;

						if ((0xff & _Master._imgData._pImage[nIndex]) < histMatchInfo._nMinMaster)
							histMatchInfo._nMinMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Master._imgData._pImage[nIndex]) > histMatchInfo._nMaxMaster)
							histMatchInfo._nMaxMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) < histMatchInfo._nMinSlave)
							histMatchInfo._nMinSlave = (0xff & _Slave._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) > histMatchInfo._nMaxSlave)
							histMatchInfo._nMaxSlave = (0xff & _Slave._imgData._pImage[nIndex]);
					}
				} // end row(j)
			} // end col(i)

			// get mean
			histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
			histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

			// get cumulative histogram
			for (i = 1; i < _nGrayLevel; i++) {
				nMasterHistogram[i] = nMasterHistogram[i] + nMasterHistogram[i - 1];
				nSlaveHistogram[i] = nSlaveHistogram[i] + nSlaveHistogram[i - 1];
			}

			// Create modified LUT of slave image within searching buffer
			nDiff1 = 0;
			nDiff2 = 0;
			nRoopCount = 0;

			for (i = histMatchInfo._nMinSlave; i < histMatchInfo._nMaxSlave + 1; i++) {
				// ����ó�� (10.19)
				// for(j=nRoopCount; j<histMatchInfo._nMaxMaster+1; j++)
				for (j = nRoopCount; j < histMatchInfo._nMaxMaster; j++) {
					if ((i == histMatchInfo._nMaxSlave) && (histMatchInfo._nMaxSlave == 255)
							&& (j == histMatchInfo._nMaxMaster) && (histMatchInfo._nMaxMaster == 255))
						break;
					else if ((nSlaveHistogram[i] >= nMasterHistogram[j])
							&& (nSlaveHistogram[i] < nMasterHistogram[j + 1]))
						break;
				}

				// ����ó�� (10.19)
				if (j >= 255)
					break;

				nDiff1 = Math.abs(nSlaveHistogram[i] - nMasterHistogram[j]);
				nDiff2 = Math.abs(nSlaveHistogram[i] - nMasterHistogram[j + 1]);

				if ((i == histMatchInfo._nMaxSlave) && (histMatchInfo._nMaxSlave == 255)
						&& (j == histMatchInfo._nMaxMaster) && (histMatchInfo._nMaxMaster == 255)) {
					nDiff1 = 0;
					nDiff2 = 1;
				} else if (nDiff1 < nDiff2)
					histMatchInfo._nLUTModified[i] = j;
				else
					histMatchInfo._nLUTModified[i] = j + 1;

				nRoopCount = j;
			}

			for (i = 0; i < histMatchInfo._nMinSlave; i++) {
				if (i == 0)
					histMatchInfo._nLUTModified[i] = 0;
				if ((i != 0) && (i < histMatchInfo._nLUTModified[histMatchInfo._nMinSlave]))
					histMatchInfo._nLUTModified[i] = i;
				if ((i != 0) && (i >= histMatchInfo._nLUTModified[histMatchInfo._nMinSlave]))
					histMatchInfo._nLUTModified[i] = histMatchInfo._nLUTModified[histMatchInfo._nMinSlave];
			}

			for (i = histMatchInfo._nMaxSlave + 1; i < _nGrayLevel; i++) {
				if (i < histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave])
					histMatchInfo._nLUTModified[i] = histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave];
				if (i >= histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave])
					histMatchInfo._nLUTModified[i] = i;
			}

			// Create Mosaicked Image within the Overlap Area
			// horizontal direction
			int Matched_DN;

			if (_mbr2dMaster.getMaxY() > _mbr2dSlave.getMaxY()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i <= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
							{
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							}
						} else if (i > lSeamPoint[j]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0))// exception
								OV_IMG[nIndex] = (byte) 0;
						}
					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMaxY() < _mbr2dSlave.getMaxY()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i >= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
							{
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							}
						} else if (i < lSeamPoint[j]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0))// exception
								OV_IMG[nIndex] = (byte) 0;
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GHistogramMatching.calcMatchCumulativeFrequencyH_Mem : (IndexOutOfBoundsException) "
					+ iobe.toString());
			iobe.printStackTrace();
		}

		// Delete dynamic memory
		nMasterHistogram = null;
		nSlaveHistogram = null;

		return OV_IMG;
	}

	// Match Cumulative Frequency �˰����� �����Ͽ� �������� ������׷� ��Ī �����
	// ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcMatchCumulativeFrequencyV_Mem(int[] lSeamPoint) {
		// Match Cumulative Frequency
		// The shape of vertical direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		GHistogramMatchingData histMatchInfo = _outHistoData._histMatchInfo;

		int i = 0, j = 0; // Initialize...
		int Master_DN = 0, Slave_DN = 0; // Initialize...
		histMatchInfo._nMinMaster = 255;
		histMatchInfo._nMinSlave = 255;
		histMatchInfo._nMaxMaster = 0;
		histMatchInfo._nMaxSlave = 0;
		histMatchInfo._nLUTModified = new int[_nGrayLevel];

		int[] nMasterHistogram = new int[_nGrayLevel];
		int[] nSlaveHistogram = new int[_nGrayLevel];

		for (i = 0; i < _nGrayLevel; i++) {
			nMasterHistogram[i] = 0;
			nSlaveHistogram[i] = 0;

			// Look Up Table �ʱ�ȭ (10.19)
			histMatchInfo._nLUTModified[i] = i;
		}

		// Get Max, Min, Mean
		int lCalW = (int) ((double) _lSearchWidth / 2.0);
		int lStartCol, lEndCol;
		double dwSumMaster = 0.0;
		double dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;

		try {

			for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
			{
				// {get extent of row
				lStartCol = lSeamPoint[i] - lCalW;
				lEndCol = lSeamPoint[i] + lCalW;
				if (lStartCol < 0)
					lStartCol = 0;
				if (lEndCol > _outHistoData._lWidth)
					lEndCol = _outHistoData._lWidth;

				for (j = lStartCol; j < lEndCol; j++) // col(j)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						lCount++;
						Master_DN = (0xff & _Master._imgData._pImage[nIndex]);
						Slave_DN = (0xff & _Slave._imgData._pImage[nIndex]);

						dwSumMaster += (double) (0xff & _Master._imgData._pImage[nIndex]);
						dwSumSlave += (double) (0xff & _Slave._imgData._pImage[nIndex]);

						nMasterHistogram[Master_DN]++;
						nSlaveHistogram[Slave_DN]++;

						if ((0xff & _Master._imgData._pImage[nIndex]) < histMatchInfo._nMinMaster)
							histMatchInfo._nMinMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Master._imgData._pImage[nIndex]) > histMatchInfo._nMaxMaster)
							histMatchInfo._nMaxMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) < histMatchInfo._nMinSlave)
							histMatchInfo._nMinSlave = (0xff & _Slave._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) > histMatchInfo._nMaxSlave)
							histMatchInfo._nMaxSlave = (0xff & _Slave._imgData._pImage[nIndex]);
					}
				} // end col(j)
			} // end row(i)

			// get mean
			histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
			histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

			// get cumulative histogram
			for (i = 1; i < _nGrayLevel; i++) {
				nMasterHistogram[i] = nMasterHistogram[i] + nMasterHistogram[i - 1];
				nSlaveHistogram[i] = nSlaveHistogram[i] + nSlaveHistogram[i - 1];
			}

			// Create modified LUT of slave image within searching buffer
			int nDiff1, nDiff2;
			int nRoopCount = 0;

			for (i = histMatchInfo._nMinSlave; i < histMatchInfo._nMaxSlave + 1; i++) {
				// ����ó�� (10.19)
				// for(j=nRoopCount; j<histMatchInfo._nMaxMaster+1; j++)
				for (j = nRoopCount; j < histMatchInfo._nMaxMaster; j++) {
					if ((i == histMatchInfo._nMaxSlave) && (histMatchInfo._nMaxSlave == 255)
							&& (j == histMatchInfo._nMaxMaster) && (histMatchInfo._nMaxMaster == 255))
						break;
					else if ((nSlaveHistogram[i] >= nMasterHistogram[j])
							&& (nSlaveHistogram[i] < nMasterHistogram[j + 1]))
						break;
				}

				// ����ó�� (10.19)
				if (j >= 255)
					break;

				nDiff1 = Math.abs(nSlaveHistogram[i] - nMasterHistogram[j]);
				nDiff2 = Math.abs(nSlaveHistogram[i] - nMasterHistogram[j + 1]);

				if ((i == histMatchInfo._nMaxSlave) && (histMatchInfo._nMaxSlave == 255)
						&& (j == histMatchInfo._nMaxMaster) && (histMatchInfo._nMaxMaster == 255)) {
					nDiff1 = 0;
					nDiff2 = 1;
				} else if (nDiff1 < nDiff2)
					histMatchInfo._nLUTModified[i] = j;
				else
					histMatchInfo._nLUTModified[i] = j + 1;

				nRoopCount = j;
			}

			for (i = 0; i < histMatchInfo._nMinSlave; i++) {
				if (i == 0)
					histMatchInfo._nLUTModified[i] = 0;
				if ((i != 0) && (i < histMatchInfo._nLUTModified[histMatchInfo._nMinSlave]))
					histMatchInfo._nLUTModified[i] = i;
				if ((i != 0) && (i >= histMatchInfo._nLUTModified[histMatchInfo._nMinSlave]))
					histMatchInfo._nLUTModified[i] = histMatchInfo._nLUTModified[histMatchInfo._nMinSlave];
			}

			for (i = histMatchInfo._nMaxSlave + 1; i < _nGrayLevel; i++) {
				if (i < histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave])
					histMatchInfo._nLUTModified[i] = histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave];
				if (i >= histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave])
					histMatchInfo._nLUTModified[i] = i;
			}

			// Creat Mosaic Image of Overlap Area
			// Vertical direction
			int Matched_DN;

			if (_mbr2dMaster.getMinX() > _mbr2dSlave.getMinX()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j >= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
							{
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							}
						} else if (j < lSeamPoint[i]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0))// exception
								OV_IMG[nIndex] = (byte) 0;
						}
					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMinX() < _mbr2dSlave.getMinX()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j <= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0))// exception
							{
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							}
						} else if (j > lSeamPoint[i]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								Matched_DN = (0xff & _Slave._imgData._pImage[nIndex]);
								OV_IMG[nIndex] = (byte) histMatchInfo._nLUTModified[Matched_DN];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0))// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0))// exception
								OV_IMG[nIndex] = (byte) 0;
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GHistogramMatching.calcMatchCumulativeFrequencyV_Mem : (IndexOutOfBoundsException) "
					+ iobe.toString());
			iobe.printStackTrace();
		}

		// Delete dynamic memory
		nMasterHistogram = null;
		nSlaveHistogram = null;

		return OV_IMG;
	}

	// Hue Adjustment Through Moving the Histogram �˰����� �����Ͽ� ������� ������׷�
	// ��Ī ����� ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcHueAdjustmentH_Mem(int[] lSeamPoint) {
		// Hue Adjustment
		// The shape of horizontal direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		GHistogramMatchingData histMatchInfo = _outHistoData._histMatchInfo;

		// Calculate basic statistics of each image within searching buffer
		histMatchInfo._nMinMaster = 255;
		histMatchInfo._nMinSlave = 255;
		histMatchInfo._nMaxMaster = 0;
		histMatchInfo._nMaxSlave = 0;

		int i = 0, j = 0; // Initialize...
		int lCalH = (int) ((double) _lSearchWidth / 2.0);
		int lStartRow = 0, lEndRow = 0;
		double dwSumMaster = 0.0;
		double dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;
		double dblMatched_DN = 0;// ȭ�Ұ� ���� ó�� (10.19)
		int Matched_DN = 0;

		try {

			for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
			{
				// {get extent of row
				lStartRow = lSeamPoint[j] - lCalH;
				lEndRow = lSeamPoint[j] + lCalH;
				if (lStartRow < 0)
					lStartRow = 0;
				if (lEndRow > _outHistoData._lHeight)
					lEndRow = _outHistoData._lHeight;
				// }

				for (i = lStartRow; i < lEndRow; i++) // row(i)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						lCount++;
						dwSumMaster += (double) (0xff & _Master._imgData._pImage[nIndex]);
						dwSumSlave += (double) (0xff & _Slave._imgData._pImage[nIndex]);

						if ((0xff & _Master._imgData._pImage[nIndex]) < histMatchInfo._nMinMaster)
							histMatchInfo._nMinMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Master._imgData._pImage[nIndex]) > histMatchInfo._nMaxMaster)
							histMatchInfo._nMaxMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) < histMatchInfo._nMinSlave)
							histMatchInfo._nMinSlave = (0xff & _Slave._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) > histMatchInfo._nMaxSlave)
							histMatchInfo._nMaxSlave = (0xff & _Slave._imgData._pImage[nIndex]);
					}

				} // end row(j)
			} // end col(i)

			// get mean
			histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
			histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

			// Create Mosaicked Image within the Overlap Area
			// Horizontal direction

			if (_mbr2dMaster.getMaxY() > _mbr2dSlave.getMaxY()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i <= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {// exception
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (i > lSeamPoint[j]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// exception
								OV_IMG[nIndex] = (byte) 0;
							}
						}
					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMaxY() < _mbr2dSlave.getMaxY()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (i >= lSeamPoint[j]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {// exception
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (i < lSeamPoint[j]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// exception
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// exception
								OV_IMG[nIndex] = (byte) 0;
							}
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GHistogramMatching.calcHueAdjustmentH_Mem : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return OV_IMG;
	}

	// Hue Adjustment Through Moving the Histogram �˰����� �����Ͽ� �������� ������׷�
	// ��Ī ����� ����Ѵ�.
	// @ lSeamp : Seam Point ���
	// @
	// @ return : byte[] ������׷� ��Ī ���
	private byte[] calcHueAdjustmentV_Mem(int[] lSeamPoint) {
		// Hue Adjustment
		// The shape of vertical direction
		byte[] OV_IMG = new byte[_outHistoData._lHeight * _outHistoData._lWidth];
		GHistogramMatchingData histMatchInfo = _outHistoData._histMatchInfo;

		// Calculate basic statistics of each image within searching buffer
		histMatchInfo._nMinMaster = 255;
		histMatchInfo._nMinSlave = 255;
		histMatchInfo._nMaxMaster = 0;
		histMatchInfo._nMaxSlave = 0;

		int i = 0, j = 0;
		int lCalW = (int) ((double) _lSearchWidth / 2.0);
		int lStartCol = 0, lEndCol = 0;
		double dwSumMaster = 0.0;
		double dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;
		double dblMatched_DN = 0;// ȭ�Ұ� ���� ó�� (10.19)
		int Matched_DN = 0;

		try {

			for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
			{
				// {get extent of row
				lStartCol = lSeamPoint[i] - lCalW;
				lEndCol = lSeamPoint[i] + lCalW;
				if (lStartCol < 0)
					lStartCol = 0;
				if (lEndCol > _outHistoData._lWidth)
					lEndCol = _outHistoData._lWidth;
				// }

				for (j = lStartCol; j < lEndCol; j++) // col(j)
				{
					nIndex = j + _outHistoData._lWidth * i;

					if (((0xff & _Master._imgData._pImage[nIndex]) != 0)
							&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {
						lCount++;
						dwSumMaster += (double) (0xff & _Master._imgData._pImage[nIndex]);
						dwSumSlave += (double) (0xff & _Slave._imgData._pImage[nIndex]);

						if ((0xff & _Master._imgData._pImage[nIndex]) < histMatchInfo._nMinMaster)
							histMatchInfo._nMinMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Master._imgData._pImage[nIndex]) > histMatchInfo._nMaxMaster)
							histMatchInfo._nMaxMaster = (0xff & _Master._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) < histMatchInfo._nMinSlave)
							histMatchInfo._nMinSlave = (0xff & _Slave._imgData._pImage[nIndex]);

						if ((0xff & _Slave._imgData._pImage[nIndex]) > histMatchInfo._nMaxSlave)
							histMatchInfo._nMaxSlave = (0xff & _Slave._imgData._pImage[nIndex]);
					}
				} // end row(j)
			} // end col(i)

			// get mean
			histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
			histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

			// Creat Mosaic Image of Overlap Area
			// Vertical direction

			if (_mbr2dMaster.getMinX() > _mbr2dSlave.getMinX()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j >= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {// exception
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (j < lSeamPoint[i]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// except
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// except
								OV_IMG[nIndex] = 0;
							}
						}
					} // end col(j)
				} // end row(i)
			} else if (_mbr2dMaster.getMinX() < _mbr2dSlave.getMinX()) {
				for (i = 0; i < _outHistoData._lHeight; i++) // row(i)
				{
					for (j = 0; j < _outHistoData._lWidth; j++) // col(j)
					{
						nIndex = j + _outHistoData._lWidth * i;

						if (j <= lSeamPoint[i]) // master
						{
							OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							if (((0xff & _Master._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Slave._imgData._pImage[nIndex]) != 0)) {// exception
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							}
						} else if (j > lSeamPoint[i]) // slave
						{
							if ((0xff & _Slave._imgData._pImage[nIndex]) != 0) {
								// -------------------------------------------------------------------//
								// ȭ�Ұ� ���� ó�� (10.19)
								// -------------------------------------------------------------------//
								dblMatched_DN = ((double) (histMatchInfo._nMaxMaster - histMatchInfo._nMinMaster)
										/ (double) (histMatchInfo._nMaxSlave - histMatchInfo._nMinSlave)
										* (double) (0xff & _Slave._imgData._pImage[nIndex])
										+ (histMatchInfo._dblMeanMaster - histMatchInfo._dblMeanSlave));
								if (dblMatched_DN < 0)
									Matched_DN = 0;
								else if (dblMatched_DN > 255)
									Matched_DN = 255;
								else
									Matched_DN = (int) Math.round(dblMatched_DN);
								// -------------------------------------------------------------------//
								OV_IMG[nIndex] = (byte) Matched_DN;
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) != 0)) {// except
								OV_IMG[nIndex] = _Master._imgData._pImage[nIndex];
							} else if (((0xff & _Slave._imgData._pImage[nIndex]) == 0)
									&& ((0xff & _Master._imgData._pImage[nIndex]) == 0)) {// except
								OV_IMG[nIndex] = 0;
							}
						}
					} // end col(j)
				} // end row(i)
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println(
					"GHistogramMatching.calcHueAdjustmentV_Mem : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}

		return OV_IMG;
	}

	// ��� ���� ȭ�� ������ Feathering�� �����Ͽ� ����Ѵ�.
	// @ nFw : Feather ������ ũ��
	// @ nShape : Overlap Path, Row ���� ����
	// @ lSeamPoint : Seam Point ���
	private void calcFeatheringMem(int nFw, int nShape, int[] lSeamPoint) {
		int i = 0, j = 0; // Initialize...
		int nKernel = 9;
		int nHalfKernel = (int) ((double) nKernel / 2.0);
		int nCount = 0, nSum = 0;
		double dblMean = 0;
		int nIndex = 0;

		try {

			if (nShape == 0) {
				for (j = 0; j < _outHistoData._lWidth; j++) {
					for (i = lSeamPoint[j] - nFw; i < lSeamPoint[j] + nFw; i++) {
						nIndex = j + _outHistoData._lWidth * i;

						nCount = 0;
						nSum = 0;
						dblMean = 0;
						for (int n = (j - nHalfKernel); n <= (j + nHalfKernel); n++) {
							for (int m = (i - nHalfKernel); m <= (i + nHalfKernel); m++) {
								if (((n >= 0) && (m >= 0))
										&& ((n < _outHistoData._lWidth) && (m < _outHistoData._lHeight))) {
									nSum += (int) (0xff & _outHistoData._pImage[n + _outHistoData._lWidth * m]);
									nCount++;
								}
							} // end 'm'
						} // end 'n'
						dblMean = (double) nSum / (double) nCount;
						nIndex = j + _outHistoData._lWidth * i;
						_outHistoData._pImage[nIndex] = (byte) (int) dblMean;
					} // end 'i'

				} // end 'j'
			}

			if (nShape == 1) {
				for (i = 0; i < _outHistoData._lHeight; i++) {
					for (j = lSeamPoint[i] - nFw; j < lSeamPoint[i] + nFw; j++) {
						nIndex = j + _outHistoData._lWidth * i;

						nCount = 0;
						nSum = 0;
						dblMean = 0;
						for (int m = (i - nHalfKernel); m <= (i + nHalfKernel); m++) {
							for (int n = (j - nHalfKernel); n <= (j + nHalfKernel); n++) {
								if (((m >= 0) && (n >= 0))
										&& ((m < _outHistoData._lHeight) && (n < _outHistoData._lWidth))) {
									nSum += (int) (0xff & _outHistoData._pImage[n + _outHistoData._lWidth * m]);
									nCount++;
								}
							} // end 'n'
						} // end 'm'
						dblMean = (double) nSum / (double) nCount;
						nIndex = j + _outHistoData._lWidth * i;
						_outHistoData._pImage[nIndex] = (byte) (int) dblMean;
					} // end 'j'
				} // end 'i'
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("GHistogramMatching.calcFeatheringMem : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}

}
