package ugis.cmmn.imgproc.mosaic;

import ugis.cmmn.imgproc.GImageEnhancement;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.data.GMosaicResultData;
import ugis.cmmn.imgproc.data.GMosaicStatisticData;

//�̹��� ȭ�� ��� ���� Ŭ����
public class GImageStatistics {

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// �̹��� ȭ�� ��� ������ ��ȯ�Ѵ�.
	// @ gdReader : //GeoTiff ����(���� �� �б�) ���� Ŭ����
	// @ nBand : ��� (R : 2, G : 1, B : 0)
	// @
	// @ return : GMosaicResultData registData ������ũ ��� ����
	public void getImgStatisticsData(GTiffDataReader gdReader, int nBand, GMosaicResultData registData) {
		int[] histgR = new int[256];
		int[] histgG = new int[256];
		int[] histgB = new int[256];
		GImageEnhancement enhance = new GImageEnhancement();
		int[] gridSize = null;

		if (gdReader == null)
			return;

		gridSize = gdReader.getGridSize();
		gdReader.getHistogram(histgR, histgG, histgB);

		enhance.calcAutoHistogramMinMax(histgR, histgG, histgB, registData._pMn, registData._pMx);
		registData._imgData._imgBox2d.setBounds(0, 0, gridSize[0], gridSize[1]);

		registData._nCC = gdReader.getBandCount();
		registData._nBpp = gdReader.getBandCount();

		calcImgStatisticsData(histgR, histgG, histgB, nBand, registData);
	}

	// �̹��� ȭ�� ��� ������ ����Ѵ�.
	// @ histgR[256] : Red ��� ������׷� ����
	// @ histgG[256] : Green ��� ������׷� ����
	// @ histgB[256] : Blue ��� ������׷� ����
	// @ nBand : ��� (R : 2, G : 1, B : 0)
	// @
	// @ return : GMosaicResultData registData ������ũ ��� ����
	public void calcImgStatisticsData(int[] histgR, int[] histgG, int[] histgB, int nBand,
			GMosaicResultData registData) {
		// Get Image Statistics
		int nModeFreq = 0;
		int nMax = 0, nMin = 255, nMode = 0;
		double dblMean = 0.0, dblV = 0.0;
		int nSum = 0, nCount = 0, nValidCount = 0;
		int i = 0, j = 0, k = 0;
		int[] pHisto = null;
		GMosaicStatisticData statTmpData = new GMosaicStatisticData();

		try {

			switch (registData._nCC) {
			case 1: {
				nMin = 255;
				nMax = 0;
				nMode = 0;
				dblMean = 0.0;
				dblV = 0.0;
				nModeFreq = 0;

				// @todo : Max Pixel Value
				// for(i=1; i<=200; i++)
				for (i = 1; i <= 255; i++) {
					if (histgR[i] > 0) {
						nMin = Math.min(i, nMin);
						nMax = Math.max(i, nMax);

						// Compare with UINT and int
						if ((int) histgR[i] > nModeFreq) {
							nMode = i;
							nModeFreq = histgR[i];
						}
						nSum += i * histgR[i];
						nCount += histgR[i];
					}
				}
				dblMean = (double) nSum / (double) nCount;

				for (j = nMin; j < nMax; j++) {
					if (histgR[j] != 0) {
						dblV += Math.pow(Math.abs((double) j - dblMean), 2) * (double) histgR[j];
						nValidCount += histgR[j];
					}
				}

				registData._statData._nMin = nMin;
				registData._statData._nMax = nMax;
				registData._statData._nMode = nMode;
				registData._statData._nRange = nMax - nMin;
				registData._statData._dblMean = dblMean;
				registData._statData._dblMeanMode = Math.abs(dblMean - nMode);
				registData._statData._dblVariance = (double) (dblV / (double) (nValidCount));
			}
				break;
			case 3: {
				for (i = 0; i < 3; i++) {
					nMin = 255;
					nMax = 0;
					nMode = 0;
					dblMean = 0.0;
					dblV = 0.0;
					nModeFreq = 0;
					nSum = 0;
					nCount = 0;
					nValidCount = 0;

					if (i == 0)
						pHisto = histgR;
					else if (i == 1)
						pHisto = histgG;
					else if (i == 2)
						pHisto = histgB;

					// @todo : Max Pixel Value
					// for(j=1; j<=200; j++)
					for (j = 1; j <= 255; j++) {
						if (pHisto[j] > 0) {
							if (j < nMin)
								nMin = j;
							if (j > nMax)
								nMax = j;
							if ((int) pHisto[j] > nModeFreq) {
								nMode = j;
								nModeFreq = pHisto[j];
							}
							nSum += j * pHisto[j];
							nCount += pHisto[j];
						}
					}
					dblMean = (double) nSum / (double) nCount;

					for (k = nMin; k < nMax; k++) {
						if (pHisto[k] != 0) {
							dblV += Math.pow(Math.abs((double) k - dblMean), 2) * (double) pHisto[k];
							nValidCount += pHisto[k];
						}
					}

					statTmpData._nMin = nMin;
					statTmpData._nMax = nMax;
					statTmpData._nMode = nMode;
					statTmpData._nRange = nMax - nMin;
					statTmpData._dblMean = dblMean;
					statTmpData._dblMeanMode = Math.abs(dblMean - nMode);
					statTmpData._dblVariance = (double) (dblV / (double) (nValidCount));

					if (i == 0)
						registData._statRData.Copy(statTmpData);
					else if (i == 1)
						registData._statGData.Copy(statTmpData);
					else if (i == 2)
						registData._statBData.Copy(statTmpData);
				}

				if (nBand == 2)
					registData._statData.Copy(registData._statRData);
				else if (nBand == 1)
					registData._statData.Copy(registData._statGData);
				else if (nBand == 0)
					registData._statData.Copy(registData._statBData);

				////////////////////////////////////////////////////////////////
			}
				break;
			}

			for (i = 0; i < 256; i++) {
				registData._lHistogramR[i] = histgR[i];
				registData._lHistogramG[i] = histgG[i];
				registData._lHistogramB[i] = histgB[i];
			}

		} catch (IndexOutOfBoundsException iobe) {
			System.out
					.println("GImageStatistics.calcImgStatisticsData : (IndexOutOfBoundsException) " + iobe.toString());
			iobe.printStackTrace();
		}
	}
}
