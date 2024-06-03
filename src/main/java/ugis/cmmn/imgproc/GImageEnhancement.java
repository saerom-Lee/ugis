package ugis.cmmn.imgproc;

import org.geotools.coverage.grid.GridEnvelope2D;

import ugis.cmmn.imgproc.data.GHistogramMatchingData;

//���� Enhancement Ŭ����
public class GImageEnhancement {

	// ���� Sigma ����
	public enum LinearSigma {
		LINEAR_1SIGMA("Linear 1 Sigma"), LINEAR_2SIGMA("Linear 2 Sigma"), LINEAR_3SIGMA("Linear 3 Sigma");

		private String name;

		LinearSigma(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// ������׷� ����
	public enum Enhancement {
		LINEAR_RSTRECHING("Linear Stretching"), HISTOGRAM_EQUALIZATION("Histogram Equalization");

		private String name;

		Enhancement(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	// ������׷� ��Ī ����
	public enum HistorgramMatchingMethod {
		NONE("None"), MATCH_MEAN_STD_DEV("Match Mean & Std. Dev."),
		MATH_CUMULATIVE_FREQUENCY("Match Cumulative Frequency"),
		HUE_ADJUSTMENT("Hue Adjustment Through Moving the Histogram"); // Default

		private String name;

		HistorgramMatchingMethod(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// �ش�������׷� ������ ���Ͽ� ȭ���� �ִ��ּҰ��� ��ȯ�Ѵ�.
	// @ histgR[256] : Red ��� ������׷� ����
	// @ histgG[256] : Green ��� ������׷� ����
	// @ histgB[256] : Blue ��� ������׷� ����
	// @ stretchingMin : ��ȯ�� ��庰 �ּ� ȭ�Ұ� [3]
	// @ stretchingMax : ��ȯ�� ��庰 �ִ� ȭ�Ұ� [3]
	public void calcAutoHistogramMinMax(int[] histgR, int[] histgG, int[] histgB, int[] stretchingMin,
			int[] stretchingMax) {
		int bitMax = 255;
		int CurMinR = 0, CurMinG = 0, CurMinB = 0, CurMinValue = 255;
		int CurMaxR = 255, CurMaxG = 255, CurMaxB = 255, CurMaxValue = 0;
		int i = 0;
		boolean isFirst = true;

		//////////////////////////////////////////////////////////////////////////
		// R Band
		CurMinValue = 0;
		CurMaxValue = 255;
		isFirst = true;

		// 0�� 255�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgR[i] > 0) {
				if (isFirst) {
					CurMinValue = i;
					CurMaxValue = i;
					isFirst = false;
				} else {
					CurMinValue = Math.min(CurMinValue, i);
					CurMaxValue = Math.max(CurMaxValue, i);
				}
			}
		}

		CurMinR = CurMinValue;
		CurMaxR = CurMaxValue;

		//////////////////////////////////////////////////////////////////////////
		// G Band
		CurMinValue = 0;
		CurMaxValue = 255;
		isFirst = true;

		// 0�� 255�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgG[i] > 0) {
				if (isFirst) {
					CurMinValue = i;
					CurMaxValue = i;
					isFirst = false;
				} else {
					CurMinValue = Math.min(CurMinValue, i);
					CurMaxValue = Math.max(CurMaxValue, i);
				}
			}
		}

		CurMinG = CurMinValue;
		CurMaxG = CurMaxValue;

		//////////////////////////////////////////////////////////////////////////
		// B Band
		CurMinValue = 0;
		CurMaxValue = 255;
		isFirst = true;

		// 0�� 255�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgB[i] > 0) {
				if (isFirst) {
					CurMinValue = i;
					CurMaxValue = i;
					isFirst = false;
				} else {
					CurMinValue = Math.min(CurMinValue, i);
					CurMaxValue = Math.max(CurMaxValue, i);
				}
			}
		}

		CurMinB = CurMinValue;
		CurMaxB = CurMaxValue;

		//////////////////////////////////////////////////////////////////////////

		stretchingMax[0] = CurMaxR;
		stretchingMax[1] = CurMaxG;
		stretchingMax[2] = CurMaxB;

		stretchingMin[0] = CurMinR;
		stretchingMin[1] = CurMinG;
		stretchingMin[2] = CurMinB;
	}

	// �ش�������׷� ������ ���Ͽ� �Էµ� ���� Sigma ������ ���� ȭ���� �ִ��ּҰ��� ��ȯ�Ѵ�.
	// @ sigmaBase : ���� Sigma ����
	// @ histgR[256] : Red ��� ������׷� ����
	// @ histgG[256] : Green ��� ������׷� ����
	// @ histgB[256] : Blue ��� ������׷� ����
	// @ stretchingMin : ��ȯ�� ��庰 �ּ� ȭ�Ұ� [3]
	// @ stretchingMax : ��ȯ�� ��庰 �ִ� ȭ�Ұ� [3]
	public void calcAutoHistogramMinMaxByLinearSigma(LinearSigma sigmaBase, int[] histgR, int[] histgG, int[] histgB,
			int[] stretchingMin, int[] stretchingMax) {
		double SigmaBase = 1;
		int bitMax = 255;
		int CurMinR = 0, CurMinG = 0, CurMinB = 0;
		int CurMaxR = 255, CurMaxG = 255, CurMaxB = 255;
		double MeanValue = 0;
		double SigmaValue = 0;
		double histValue = 0;
		double pixelValue = 0;
		double invValue = 0;
		int TotalPixels = 0;
		int i = 0;

		if (sigmaBase == LinearSigma.LINEAR_1SIGMA)
			SigmaBase = 1;
		else if (sigmaBase == LinearSigma.LINEAR_2SIGMA)
			SigmaBase = 2;
		else if (sigmaBase == LinearSigma.LINEAR_3SIGMA)
			SigmaBase = 3;

		//////////////////////////////////////////////////////////////////////////
		// R Band
		histValue = 0;
		pixelValue = 0;
		TotalPixels = 0;
		MeanValue = 0;
		SigmaValue = 0;

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgR[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgR[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgR[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (MeanValue < SigmaBase * SigmaValue)
			CurMinR = 0;
		else
			CurMinR = (int) (MeanValue - SigmaBase * SigmaValue);

		CurMaxR = (int) (MeanValue + SigmaBase * SigmaValue);
		if (CurMaxR > bitMax)
			CurMaxR = bitMax;

		//////////////////////////////////////////////////////////////////////////
		// G Band
		histValue = 0;
		pixelValue = 0;
		TotalPixels = 0;
		MeanValue = 0;
		SigmaValue = 0;

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgG[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgG[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgG[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (MeanValue < SigmaBase * SigmaValue)
			CurMinG = 0;
		else
			CurMinG = (int) (MeanValue - SigmaBase * SigmaValue);

		CurMaxG = (int) (MeanValue + SigmaBase * SigmaValue);
		if (CurMaxG > bitMax)
			CurMaxG = bitMax;

		//////////////////////////////////////////////////////////////////////////
		// B Band
		histValue = 0.;
		pixelValue = 0.;
		TotalPixels = 0;
		MeanValue = 0.;
		SigmaValue = 0.;

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgB[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgB[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgB[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (MeanValue < SigmaBase * SigmaValue)
			CurMinB = 0;
		else
			CurMinB = (int) (MeanValue - SigmaBase * SigmaValue);

		CurMaxB = (int) (MeanValue + SigmaBase * SigmaValue);
		if (CurMaxB > bitMax)
			CurMaxB = bitMax;

		//////////////////////////////////////////////////////////////////////////

		stretchingMax[0] = CurMaxR;
		stretchingMax[1] = CurMaxG;
		stretchingMax[2] = CurMaxB;

		stretchingMin[0] = CurMinR;
		stretchingMin[1] = CurMinG;
		stretchingMin[2] = CurMinB;
	}

	// �ش� ȭ�ҿ� ���� ������׷��� Linear Stretching ������� �����Ѵ�.
	// @ pixels : �Է� ȭ�Ұ�
	// @ bc : ����
	// @ width : ���� ���� ũ��
	// @ height : ���� ���� ũ��
	// @ stretchingMin : ��庰 �ּ� ȭ�Ұ� [1] or [3]
	// @ stretchingMax : ��庰 �ִ� ȭ�Ұ� [1] or [3]
	// @
	// @ return : void
	public void calcLinearStretching(byte[] pixels, int bc, int width, int height, int[] stretchingMin,
			int[] stretchingMax) {
		int[][] LUT = new int[3][256];
		double[] vd = new double[3];
		int i = 0, j = 0;

		try {
			// Range per band
			for (i = 0; i < 3; i++) {
				if (stretchingMax.length != 3 || stretchingMin.length != 3) {
					vd[i] = (double) (stretchingMax[0] - stretchingMin[0]);
				} else {
					vd[i] = (double) (stretchingMax[i] - stretchingMin[i]);
				}
			}

			// Look Up Table
			for (i = 0; i < 3; i++) {
				for (j = 0; j < 256; j++) {
					if (stretchingMax.length != 3 || stretchingMin.length != 3) {
						if (j < stretchingMin[0])
							LUT[i][j] = 0;
						else if (j > stretchingMax[0])
							LUT[i][j] = 255;
						else
							LUT[i][j] = (int) ((double) ((j - stretchingMin[0]) * 256.0f) / vd[i]);
					} else {
						if (j < stretchingMin[i])
							LUT[i][j] = 0;
						else if (j > stretchingMax[i])
							LUT[i][j] = 255;
						else
							LUT[i][j] = (int) ((double) ((j - stretchingMin[i]) * 256.0f) / vd[i]);
					}
				}
			}

			applyLUT(pixels, bc, width, height, LUT);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.calcLinearStretching : " + ex.toString());
			ex.printStackTrace();
		}
	}

	// �ش� ȭ�ҿ� ���� ������׷��� Histogram Equalization ������� �ڵ� �����Ѵ�.
	// @ pixels : �Է� ȭ�Ұ�
	// @ bc : ����
	// @ width : ���� ���� ũ��
	// @ height : ���� ���� ũ��
	// @ histgR[256] : Red ��� ������׷� ����
	// @ histgG[256] : Green ��� ������׷� ����
	// @ histgB[256] : Blue ��� ������׷� ����
	// @ isApplyBlueBand : Blue ��� LUT �ϰ� ���� ���� (true : Blue ��� LUT �ϰ� ����,
	// false : ��庰 LUT ����)
	// @
	// @ return : void
	public void calcHistogramAutoEqualization(byte[] pixels, int bc, int width, int height, int[] histgR, int[] histgG,
			int[] histgB, boolean isApplyBlueBand) {
		int[][] LUT = new int[3][256];
		int[][] eqLUT = new int[3][256];
		long TotalPixels = 0;
		double scaleFactor = 1.;
		int pv = 0, i = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageEnhancement.calcHistogramAutoEqualization : ");
		}
//[DEBUG] 		

		if (histgR.length != 256 || histgB.length != 256 || histgB.length != 256) {
			return;
		}

		try {

			///////////////////////////////////////////////////////////////////
			// ��Ȱȭ(Equalization)
			///////////////////////////////////////////////////////////////////
			// h(i) = (Gmax / Nt) * H(i)
			// h(i) : ����ȭ�� ������׷�
			// Gmax : ������ �ִ� ��� 256
			// Nt : �Է¿��� ���� ���� �ȼ� ����
			// H(i) : ������ ���� ������׷� ��ġ
			///////////////////////////////////////////////////////////////////

			for (i = 0; i < 256; i++) {
				eqLUT[0][i] = eqLUT[1][i] = eqLUT[2][i] = i;
			}

			switch (bc) {
			case 1: {
				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgR[i];
				}
				scaleFactor = 255.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgR[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[0][i] = eqLUT[1][i] = eqLUT[2][i] = pv;
				}
			}
				break;
			case 3: {
				//////////////////////////////////////////////////////////////////////////
				// R Band
				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgR[i];
				}
				scaleFactor = 255.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgR[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[0][i] = pv;
				}

				//////////////////////////////////////////////////////////////////////////
				// G Band
				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgG[i];
				}
				scaleFactor = 255.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgG[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[1][i] = pv;
				}

				//////////////////////////////////////////////////////////////////////////
				// B Band
				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgB[i];
				}
				scaleFactor = 255.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 256; i++) {
					TotalPixels += histgB[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[2][i] = pv;
				}
			}
				break;
			}

			// @todo : Blue Band ���� ��Ȱȭ ���� �ʿ�
			for (i = 0; i < 256; i++) {
				if (isApplyBlueBand) {
					// PhotoShop Equalization
					LUT[0][i] = eqLUT[2][i];
					LUT[1][i] = eqLUT[2][i];
					LUT[2][i] = eqLUT[2][i];
				} else {
					// Stereo Matching
					LUT[0][i] = eqLUT[0][i];
					LUT[1][i] = eqLUT[1][i];
					LUT[2][i] = eqLUT[2][i];
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t LUT[0][" + i + "] : " + LUT[0][i] + ", LUT[0][\" + i + \"] : "
							+ LUT[1][i] + ", LUT[0][\" + i + \"] : " + LUT[2][i]);
				}
//[DEBUG]  			
			}

			applyLUT(pixels, bc, width, height, LUT);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.calcHistogramAutoEqualization : " + ex.toString());
			ex.printStackTrace();
		}
	}

	// �ش� ȭ�ҿ� Look Up Table�� �����Ѵ�.
	// @ pixels : �Է� ȭ�Ұ�
	// @ bc : ����
	// @ width : ���� ���� ũ��
	// @ height : ���� ���� ũ��
	// @ LUT : ȭ�Һ�ȯ Look Up Table ����
	// @
	// @ return : void
	private void applyLUT(byte[] pixels, int bc, int width, int height, int[][] LUT) {
		int[] pixel = new int[3];

		try {
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {

					switch (bc) {
					case 1: {
						// !!! Java�� Byte�� ��ȣ�� ������ !!! (-128 ~ 127)
						pixel[0] = 0xff & pixels[(i + j * width) * bc + 0];

						// !!! Java�� Byte�� ��ȣ�� ������ !!! (-128 ~ 127)
						pixels[(i + j * width) * bc + 0] = (byte) LUT[0][pixel[0]];
					}
						break;
					case 3: {
						// !!! Java�� Byte�� ��ȣ�� ������ !!! (-128 ~ 127)
						pixel[0] = 0xff & pixels[(i + j * width) * bc + 0];
						pixel[1] = 0xff & pixels[(i + j * width) * bc + 1];
						pixel[2] = 0xff & pixels[(i + j * width) * bc + 2];

						// !!! Java�� Byte�� ��ȣ�� ������ !!! (-128 ~ 127)
						pixels[(i + j * width) * bc + 0] = (byte) LUT[0][pixel[0]];
						pixels[(i + j * width) * bc + 1] = (byte) LUT[1][pixel[1]];
						pixels[(i + j * width) * bc + 2] = (byte) LUT[2][pixel[2]];
					}
						break;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.applyLUT : " + ex.toString());
			ex.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// [16bit]�ش� ������׷� ������ ���Ͽ� ȭ���� �ִ��ּҰ��� ��ȯ�Ѵ�.
	// @ histgR[65536] : Red ��� ������׷� ����
	// @ histgG[65536] : Green ��� ������׷� ����
	// @ histgB[65536] : Blue ��� ������׷� ����
	// @ stretchingMin : ��ȯ�� ��庰 �ּ� ȭ�Ұ� [3]
	// @ stretchingMax : ��ȯ�� ��庰 �ִ� ȭ�Ұ� [3]
	public void calcAutoHistogramMinMax16(int[] histgR, int[] histgG, int[] histgB, int[] stretchingMin,
			int[] stretchingMax) {
		int bitMax = 65535;
		int CurMinR = 0, CurMinG = 0, CurMinB = 0, CurMinValue = 65535;
		int CurMaxR = 65535, CurMaxG = 65535, CurMaxB = 65535, CurMaxValue = 0;
		int i = 0;
		boolean isFirst = true;

		//////////////////////////////////////////////////////////////////////////
		// R Band
		CurMinValue = 0;
		CurMaxValue = 65535;
		isFirst = true;

		// 0�� 65535�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgR[i] > 0) {
				if (isFirst) {
					CurMinValue = i;
					CurMaxValue = i;
					isFirst = false;
				} else {
					CurMinValue = Math.min(CurMinValue, i);
					CurMaxValue = Math.max(CurMaxValue, i);
				}
			}
		}

		CurMinR = CurMinValue;
		CurMaxR = CurMaxValue;

		//////////////////////////////////////////////////////////////////////////
		// G Band
		CurMinValue = 0;
		CurMaxValue = 65535;
		isFirst = true;

		// 0�� 65535�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgG[i] > 0) {
				if (isFirst) {
					CurMinValue = i;
					CurMaxValue = i;
					isFirst = false;
				} else {
					CurMinValue = Math.min(CurMinValue, i);
					CurMaxValue = Math.max(CurMaxValue, i);
				}
			}
		}

		CurMinG = CurMinValue;
		CurMaxG = CurMaxValue;

		//////////////////////////////////////////////////////////////////////////
		// B Band
		CurMinValue = 0;
		CurMaxValue = 65535;
		isFirst = true;

		// 0�� 65535�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgB[i] > 0) {
				if (isFirst) {
					CurMinValue = i;
					CurMaxValue = i;
					isFirst = false;
				} else {
					CurMinValue = Math.min(CurMinValue, i);
					CurMaxValue = Math.max(CurMaxValue, i);
				}
			}
		}

		CurMinB = CurMinValue;
		CurMaxB = CurMaxValue;

		//////////////////////////////////////////////////////////////////////////

		stretchingMax[0] = CurMaxR;
		stretchingMax[1] = CurMaxG;
		stretchingMax[2] = CurMaxB;

		stretchingMin[0] = CurMinR;
		stretchingMin[1] = CurMinG;
		stretchingMin[2] = CurMinB;
	}

	// [16bit]�ش� ������׷� ������ ���Ͽ� �Էµ� ���� Sigma ������ ���� ȭ���� �ִ��ּҰ���
	// ��ȯ�Ѵ�.
	// @ sigmaBase : ���� Sigma ����
	// @ histgR[65536] : Red ��� ������׷� ����
	// @ histgG[65536] : Green ��� ������׷� ����
	// @ histgB[65536] : Blue ��� ������׷� ����
	// @ stretchingMin : ��ȯ�� ��庰 �ּ� ȭ�Ұ� [3]
	// @ stretchingMax : ��ȯ�� ��庰 �ִ� ȭ�Ұ� [3]
	// @ stretchinSigma : ��ȯ�� ��庰 Sigma�� [3]
	public void calcAutoHistogramMinMaxByLinearSigma16(LinearSigma sigmaBase, boolean _isApplyedMinMax, int[] histgR,
			int[] histgG, int[] histgB, int[] stretchingMin, int[] stretchingMax, double[] stretchinSigma) {
		double SigmaBase = 1;
		int bitMax = 65535;
		int histMin = 0, histMax = 0;
		int CurMinR = 0, CurMinG = 0, CurMinB = 0;
		int CurMaxR = 65535, CurMaxG = 65535, CurMaxB = 65535;
		double MeanValue = 0;
		double SigmaValue = 0;
		double histValue = 0;
		double pixelValue = 0;
		double invValue = 0;
		int TotalPixels = 0;
		int i = 0;

		if (sigmaBase == LinearSigma.LINEAR_1SIGMA)
			SigmaBase = 1;
		else if (sigmaBase == LinearSigma.LINEAR_2SIGMA)
			SigmaBase = 2;
		else if (sigmaBase == LinearSigma.LINEAR_3SIGMA)
			SigmaBase = 3;

		//////////////////////////////////////////////////////////////////////////
		// R Band
		histValue = 0.;
		pixelValue = 0.;
		TotalPixels = 0;
		MeanValue = 0.;
		SigmaValue = 0.;
		histMin = 0;
		histMax = 65535;

		// 0�� 65535�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgR[i] > 0) {
				histMin = i;
				break;
			}
		}

		// 0�� 65535�� ����
		// for (i=bitMax;i>=0;i--) {
		for (i = bitMax - 1; i > 0; i--) {
			if (histgR[i] > 0) {
				histMax = i;
				break;
			}
		}

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgR[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgR[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgR[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (_isApplyedMinMax) {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinR = histMin;
			else
				CurMinR = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxR = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxR > histMax)
				CurMaxR = histMax;
		} else {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinR = 0;
			else
				CurMinR = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxR = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxR > bitMax)
				CurMaxR = bitMax;
		}

		stretchinSigma[0] = SigmaValue;

		//////////////////////////////////////////////////////////////////////////
		// G Band
		histValue = 0.;
		pixelValue = 0.;
		TotalPixels = 0;
		MeanValue = 0.;
		SigmaValue = 0.;
		histMin = 0;
		histMax = 65535;

		// 0�� 65535�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgG[i] > 0) {
				histMin = i;
				break;
			}
		}

		// 0�� 65535�� ����
		// for (i=bitMax;i>=0;i--) {
		for (i = bitMax - 1; i > 0; i--) {
			if (histgG[i] > 0) {
				histMax = i;
				break;
			}
		}

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgG[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgG[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgG[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (_isApplyedMinMax) {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinG = histMin;
			else
				CurMinG = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxG = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxG > histMax)
				CurMaxG = histMax;
		} else {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinG = 0;
			else
				CurMinG = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxG = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxG > bitMax)
				CurMaxG = bitMax;
		}

		stretchinSigma[1] = SigmaValue;

		//////////////////////////////////////////////////////////////////////////
		// B Band
		histValue = 0.;
		pixelValue = 0.;
		TotalPixels = 0;
		MeanValue = 0.;
		SigmaValue = 0.;
		histMin = 0;
		histMax = 65535;

		// 0�� 65535�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgB[i] > 0) {
				histMin = i;
				break;
			}
		}

		// 0�� 65535�� ����
		// for (i=bitMax;i>=0;i--) {
		for (i = bitMax - 1; i > 0; i--) {
			if (histgB[i] > 0) {
				histMax = i;
				break;
			}
		}

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgB[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgB[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgB[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (_isApplyedMinMax) {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinB = histMin;
			else
				CurMinB = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxB = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxB > histMax)
				CurMaxB = histMax;
		} else {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinB = 0;
			else
				CurMinB = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxB = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxB > bitMax)
				CurMaxB = bitMax;
		}

		stretchinSigma[2] = SigmaValue;

		//////////////////////////////////////////////////////////////////////////

		stretchingMax[0] = CurMaxR;
		stretchingMax[1] = CurMaxG;
		stretchingMax[2] = CurMaxB;

		stretchingMin[0] = CurMinR;
		stretchingMin[1] = CurMinG;
		stretchingMin[2] = CurMinB;
	}

	// [Xbit]�ش� ������׷� ������ ���Ͽ� �Էµ� ���� Sigma ������ ���� ȭ���� �ִ��ּҰ���
	// ��ȯ�Ѵ�.
	// @ maxPixel : �ִ� ȭ�Ұ�
	// @ sigmaBase : ���� Sigma ����
	// @ histgR[maxPixel+1] : Red ��� ������׷� ����
	// @ histgG[maxPixel+1] : Green ��� ������׷� ����
	// @ histgB[maxPixel+1] : Blue ��� ������׷� ����
	// @ stretchingMin : ��ȯ�� ��庰 �ּ� ȭ�Ұ� [3]
	// @ stretchingMax : ��ȯ�� ��庰 �ִ� ȭ�Ұ� [3]
	// @ stretchinSigma : ��ȯ�� ��庰 Sigma�� [3]
	public void calcAutoHistogramMinMaxByLinearSigmaX(int maxPixel, LinearSigma sigmaBase, boolean _isApplyedMinMax,
			int[] histgR, int[] histgG, int[] histgB, int[] stretchingMin, int[] stretchingMax,
			double[] stretchinSigma) {
		double SigmaBase = 1;
		int bitMax = maxPixel;
		int histMin = 0, histMax = 0;
		int CurMinR = 0, CurMinG = 0, CurMinB = 0;
		int CurMaxR = maxPixel, CurMaxG = maxPixel, CurMaxB = maxPixel;
		double MeanValue = 0;
		double SigmaValue = 0;
		double histValue = 0;
		double pixelValue = 0;
		double invValue = 0;
		int TotalPixels = 0;
		int i = 0;

		if (sigmaBase == LinearSigma.LINEAR_1SIGMA)
			SigmaBase = 1;
		else if (sigmaBase == LinearSigma.LINEAR_2SIGMA)
			SigmaBase = 2;
		else if (sigmaBase == LinearSigma.LINEAR_3SIGMA)
			SigmaBase = 3;

		//////////////////////////////////////////////////////////////////////////
		// R Band
		histValue = 0.;
		pixelValue = 0.;
		TotalPixels = 0;
		MeanValue = 0.;
		SigmaValue = 0.;
		histMin = 0;
		histMax = bitMax;

		// 0�� bitMax�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgR[i] > 0) {
				histMin = i;
				break;
			}
		}

		// 0�� bitMax�� ����
		// for (i=bitMax;i>=0;i--) {
		for (i = bitMax - 1; i > 0; i--) {
			if (histgR[i] > 0) {
				histMax = i;
				break;
			}
		}

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgR[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgR[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgR[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (_isApplyedMinMax) {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinR = histMin;
			else
				CurMinR = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxR = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxR > histMax)
				CurMaxR = histMax;
		} else {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinR = 0;
			else
				CurMinR = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxR = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxR > bitMax)
				CurMaxR = bitMax;
		}

		stretchinSigma[0] = SigmaValue;

		//////////////////////////////////////////////////////////////////////////
		// G Band
		histValue = 0.;
		pixelValue = 0.;
		TotalPixels = 0;
		MeanValue = 0.;
		SigmaValue = 0.;
		histMin = 0;
		histMax = bitMax;

		// 0�� bitMax�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgG[i] > 0) {
				histMin = i;
				break;
			}
		}

		// 0�� bitMax�� ����
		// for (i=bitMax;i>=0;i--) {
		for (i = bitMax - 1; i > 0; i--) {
			if (histgG[i] > 0) {
				histMax = i;
				break;
			}
		}

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgG[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgG[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgG[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (_isApplyedMinMax) {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinG = histMin;
			else
				CurMinG = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxG = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxG > histMax)
				CurMaxG = histMax;
		} else {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinG = 0;
			else
				CurMinG = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxG = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxG > bitMax)
				CurMaxG = bitMax;
		}

		stretchinSigma[1] = SigmaValue;

		//////////////////////////////////////////////////////////////////////////
		// B Band
		histValue = 0.;
		pixelValue = 0.;
		TotalPixels = 0;
		MeanValue = 0.;
		SigmaValue = 0.;
		histMin = 0;
		histMax = bitMax;

		// 0�� bitMax�� ����
		// for (i=0;i<bitMax+1;i++) {
		for (i = 1; i < bitMax; i++) {
			if (histgB[i] > 0) {
				histMin = i;
				break;
			}
		}

		// 0�� bitMax�� ����
		// for (i=bitMax;i>=0;i--) {
		for (i = bitMax - 1; i > 0; i--) {
			if (histgB[i] > 0) {
				histMax = i;
				break;
			}
		}

		for (i = 0; i < bitMax + 1; i++)
			TotalPixels += histgB[i];

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgB[i];
			pixelValue = (double) i;
			MeanValue += histValue * pixelValue / (double) TotalPixels;
		}

		for (i = 0; i < bitMax + 1; i++) {
			histValue = (double) histgB[i];
			pixelValue = (double) i;
			invValue = (MeanValue - pixelValue);
			SigmaValue += invValue * invValue * histValue / (double) TotalPixels;
		}
		SigmaValue = Math.sqrt(SigmaValue);

		if (_isApplyedMinMax) {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinB = histMin;
			else
				CurMinB = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxB = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxB > histMax)
				CurMaxB = histMax;
		} else {
			if (MeanValue < SigmaBase * SigmaValue)
				CurMinB = 0;
			else
				CurMinB = (int) (MeanValue - SigmaBase * SigmaValue);

			CurMaxB = (int) (MeanValue + SigmaBase * SigmaValue);
			if (CurMaxB > bitMax)
				CurMaxB = bitMax;
		}

		stretchinSigma[2] = SigmaValue;

		//////////////////////////////////////////////////////////////////////////

		stretchingMax[0] = CurMaxR;
		stretchingMax[1] = CurMaxG;
		stretchingMax[2] = CurMaxB;

		stretchingMin[0] = CurMinR;
		stretchingMin[1] = CurMinG;
		stretchingMin[2] = CurMinB;
	}

	// [16bit]�ش� ȭ�ҿ� ���� ������׷��� Linear Stretching ������� �����Ѵ�.
	// @ pixels : �Է� ȭ�Ұ�
	// @ bc : ����
	// @ width : ���� ���� ũ��
	// @ height : ���� ���� ũ��
	// @ stretchingMin : ��庰 �ּ� ȭ�Ұ� [1] or [3]
	// @ stretchingMax : ��庰 �ִ� ȭ�Ұ� [1] or [3]
	// @
	// @ return : void
	public void calcLinearStretching16(int[] pixels, int bc, int width, int height, int[] stretchingMin,
			int[] stretchingMax) {
		int[][] LUT = new int[3][65536];
		double[] vd = new double[3];
		int i = 0, j = 0;

		try {
			// Range per band
			for (i = 0; i < 3; i++) {
				if (stretchingMax.length != 3 || stretchingMin.length != 3) {
					vd[i] = (double) (stretchingMax[0] - stretchingMin[0]);
				} else {
					vd[i] = (double) (stretchingMax[i] - stretchingMin[i]);
				}
			}

			// Look Up Table
			for (i = 0; i < 3; i++) {
				for (j = 0; j < 65536; j++) {
					if (stretchingMax.length != 3 || stretchingMin.length != 3) {
						if (j < stretchingMin[0])
							LUT[i][j] = 0;
						else if (j > stretchingMax[0])
							LUT[i][j] = 65535;
						else
							LUT[i][j] = (int) ((double) ((j - stretchingMin[0]) * 65536.0f) / vd[i]);
					} else {
						if (j < stretchingMin[i])
							LUT[i][j] = 0;
						else if (j > stretchingMax[i])
							LUT[i][j] = 65535;
						else
							LUT[i][j] = (int) ((double) ((j - stretchingMin[i]) * 65536.0f) / vd[i]);
					}
				}
			}

			applyLUT16(pixels, bc, width, height, LUT);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.calcLinearStretching16 : " + ex.toString());
			ex.printStackTrace();
		}
	}

	// [16bit]�ش� ȭ�ҿ� ���� ������׷��� Histogram Equalization ������� �ڵ� �����Ѵ�.
	// @ pixels : �Է� ȭ�Ұ�
	// @ bc : ����
	// @ width : ���� ���� ũ��
	// @ height : ���� ���� ũ��
	// @ histgR[65536] : Red ��� ������׷� ����
	// @ histgG[65536] : Green ��� ������׷� ����
	// @ histgB[65536] : Blue ��� ������׷� ����
	// @ isApplyBlueBand : Blue ��� LUT �ϰ� ���� ���� (true : Blue ��� LUT �ϰ� ����,
	// false : ��庰 LUT ����)
	// @
	// @ return : void
	public void calcHistogramAutoEqualization16(int[] pixels, int bc, int width, int height, int[] histgR, int[] histgG,
			int[] histgB, boolean isApplyBlueBand) {
		int[][] LUT = new int[3][65536];
		int[][] eqLUT = new int[3][65536];
		long TotalPixels = 0;
		double scaleFactor = 1.;
		int pv = 0, i = 0;

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GImageEnhancement.calcHistogramAutoEqualization16 : ");
		}
//[DEBUG] 		

		if (histgR.length != 65536 || histgB.length != 65536 || histgB.length != 65536) {
			return;
		}

		try {

			///////////////////////////////////////////////////////////////////
			// ��Ȱȭ(Equalization)
			///////////////////////////////////////////////////////////////////
			// h(i) = (Gmax / Nt) * H(i)
			// h(i) : ����ȭ�� ������׷�
			// Gmax : ������ �ִ� ��� 256
			// Nt : �Է¿��� ���� ���� �ȼ� ����
			// H(i) : ������ ���� ������׷� ��ġ
			///////////////////////////////////////////////////////////////////

			for (i = 0; i < 65536; i++) {
				eqLUT[0][i] = eqLUT[1][i] = eqLUT[2][i] = i;
			}

			switch (bc) {
			case 1: {
				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgR[i];
				}
				scaleFactor = 65535.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgR[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[0][i] = eqLUT[1][i] = eqLUT[2][i] = pv;
				}
			}
				break;
			case 3: {
				//////////////////////////////////////////////////////////////////////////
				// R Band
				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgR[i];
				}
				scaleFactor = 65535.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgR[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[0][i] = pv;
				}

				//////////////////////////////////////////////////////////////////////////
				// G Band
				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgG[i];
				}
				scaleFactor = 65535.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgG[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[1][i] = pv;
				}

				//////////////////////////////////////////////////////////////////////////
				// B Band
				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgB[i];
				}
				scaleFactor = 65535.0f / (double) (TotalPixels);

				TotalPixels = 0;
				for (i = 0; i < 65536; i++) {
					TotalPixels += histgB[i];
					pv = (int) ((((double) TotalPixels) * scaleFactor) + 0.5);

					eqLUT[2][i] = pv;
				}
			}
				break;
			}

			// @todo : Blue Band ���� ��Ȱȭ ���� �ʿ�
			for (i = 0; i < 65536; i++) {
				if (isApplyBlueBand) {
					// PhotoShop Equalization
					LUT[0][i] = eqLUT[2][i];
					LUT[1][i] = eqLUT[2][i];
					LUT[2][i] = eqLUT[2][i];
				} else {
					// Stereo Matching
					LUT[0][i] = eqLUT[0][i];
					LUT[1][i] = eqLUT[1][i];
					LUT[2][i] = eqLUT[2][i];
				}

//[DEBUG]
				if (_IS_DEBUG) {
					System.out.println("[DEBUG]\t LUT[0][" + i + "] : " + LUT[0][i] + ", LUT[0][\" + i + \"] : "
							+ LUT[1][i] + ", LUT[0][\" + i + \"] : " + LUT[2][i]);
				}
//[DEBUG]  			
			}

			applyLUT16(pixels, bc, width, height, LUT);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.calcHistogramAutoEqualization16 : " + ex.toString());
			ex.printStackTrace();
		}
	}

	// [16bit]�ش� ȭ�ҿ� Look Up Table�� �����Ѵ�.
	// @ pixels : �Է� ȭ�Ұ�
	// @ bc : ����
	// @ width : ���� ���� ũ��
	// @ height : ���� ���� ũ��
	// @ LUT : ȭ�Һ�ȯ Look Up Table ����
	// @
	// @ return : void
	public void applyLUT16(int[] pixels, int bc, int width, int height, int[][] LUT) {
		int[] pixel = new int[3];

		try {
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {

					switch (bc) {
					case 1: {
						pixel[0] = pixels[(i + j * width) * bc + 0];

						pixels[(i + j * width) * bc + 0] = LUT[0][pixel[0]];
					}
						break;
					case 3: {
						pixel[0] = pixels[(i + j * width) * bc + 0];
						pixel[1] = pixels[(i + j * width) * bc + 1];
						pixel[2] = pixels[(i + j * width) * bc + 2];

						pixels[(i + j * width) * bc + 0] = LUT[0][pixel[0]];
						pixels[(i + j * width) * bc + 1] = LUT[1][pixel[1]];
						pixels[(i + j * width) * bc + 2] = LUT[2][pixel[2]];
					}
						break;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.applyLUT16 : " + ex.toString());
			ex.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ����(Master) ���� ���� ���(Slave) ������ ������׷� ��Ī ������ ����Ѵ�.
	// @ matchingMethod : ������׷� ��Ī ����
	// @ masterPixels : ����(Master) ���� ȭ������
	// @ masterGridEnvelope : ����(Master) ���� ũ��
	// @ slavePixels : ���(Slave) ���� ȭ������
	// @ slaveGridEnvelope : ���(Slave) ���� ũ��
	// @ isOverlap : ��ø ���� ���� (true : ��ø ����, false : ��ü ����)
	// @
	// @ return : GHistogramMatchingInfo ������׷� ��Ī ����
	public GHistogramMatchingData calcHistogramMatchingInfo(HistorgramMatchingMethod matchingMethod,
			byte[] masterPixels, GridEnvelope2D masterGridEnvelope, byte[] slavePixels,
			GridEnvelope2D slaveGridEnvelope, boolean isOverlap) {
		GHistogramMatchingData histMatchInfo = null;

		if (matchingMethod == HistorgramMatchingMethod.MATCH_MEAN_STD_DEV) {
			histMatchInfo = calcHistogramMatchingInfoByMatchMeanStdDev(masterPixels, masterGridEnvelope, slavePixels,
					slaveGridEnvelope, isOverlap);
		} else if (matchingMethod == HistorgramMatchingMethod.MATH_CUMULATIVE_FREQUENCY) {
			histMatchInfo = calcHistogramMatchingInfoByMatchCumulativeFrequency(masterPixels, masterGridEnvelope,
					slavePixels, slaveGridEnvelope, isOverlap);
		} else if (matchingMethod == HistorgramMatchingMethod.HUE_ADJUSTMENT) {
			histMatchInfo = calcHistogramMatchingInfoByHueAdjustment(masterPixels, masterGridEnvelope, slavePixels,
					slaveGridEnvelope, isOverlap);
		} else {
			histMatchInfo = new GHistogramMatchingData();

			histMatchInfo._dblMeanMaster = 0.0; // Match Mean & Std. Dev.
			histMatchInfo._dblMeanSlave = 0.0;
			histMatchInfo._dblVariance = 0.0;
			histMatchInfo._dblCovariance = 0.0;
			histMatchInfo._nMinMaster = 0; // Hue Adjustment & Match Cumulative Frequency
			histMatchInfo._nMinSlave = 0;
			histMatchInfo._nMaxMaster = 0;
			histMatchInfo._nMaxSlave = 0;
			histMatchInfo._nLUTModified = null; // Match Cumulative Frequency
		}

		return histMatchInfo;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Match Mean & Std. Dev. ������׷� ��Ī �˰������� ����(Master) ���� ����
	// ���(Slave) ������ ������׷� ��Ī ������ ����Ѵ�.
	// @ masterPixels : ����(Master) ���� ȭ������
	// @ masterGridEnvelope : ����(Master) ���� ũ��
	// @ slavePixels : ���(Slave) ���� ȭ������
	// @ slaveGridEnvelope : ���(Slave) ���� ũ��
	// @ isOverlap : ��ø ���� ���� (true : ��ø ����, false : ��ü ����)
	// @
	// @ return : GHistogramMatchingInfo ������׷� ��Ī ����
	private GHistogramMatchingData calcHistogramMatchingInfoByMatchMeanStdDev(byte[] masterPixels,
			GridEnvelope2D masterGridEnvelope, byte[] slavePixels, GridEnvelope2D slaveGridEnvelope,
			boolean isOverlap) {
		// Match Mean Standard Deviation
		GHistogramMatchingData histMatchInfo = new GHistogramMatchingData();
		int i = 0, j = 0;
		int width = 0, height = 0;
		double dwSumMaster = 0.0, dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;

		histMatchInfo._dblMeanMaster = 0.0; // Match Mean & Std. Dev.
		histMatchInfo._dblMeanSlave = 0.0;
		histMatchInfo._dblVariance = 0.0;
		histMatchInfo._dblCovariance = 0.0;

		try {
			// ��ø ������ �ִ� ��츸 �ش��
			if (isOverlap) {
				width = (int) masterGridEnvelope.getWidth();
				height = (int) masterGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & masterPixels[nIndex]) != 0) && ((0xff & slavePixels[nIndex]) != 0)) {
							dwSumMaster += (double) (0xff & masterPixels[nIndex]);
							dwSumSlave += (double) (0xff & slavePixels[nIndex]);
							lCount++;
						}
					} // end row(j)
				} // end col(i)

				histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
				histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

				histMatchInfo._dblVariance = 0.0;
				histMatchInfo._dblCovariance = 0.0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & masterPixels[nIndex]) != 0) && ((0xff & slavePixels[nIndex]) != 0)) {
							dwSumMaster += (double) (0xff & masterPixels[nIndex]);
							dwSumSlave += (double) (0xff & slavePixels[nIndex]);
							lCount++;

							histMatchInfo._dblVariance += ((double) (0xff & slavePixels[nIndex])
									- histMatchInfo._dblMeanSlave)
									* ((double) (0xff & slavePixels[nIndex]) - histMatchInfo._dblMeanSlave);
							histMatchInfo._dblCovariance += ((double) (0xff & masterPixels[nIndex])
									- histMatchInfo._dblMeanMaster)
									* ((double) (0xff & slavePixels[nIndex]) - histMatchInfo._dblMeanSlave);
						}
					} // end row(j)
				} // end col(i)
			} else {
				// Master
				width = (int) masterGridEnvelope.getWidth();
				height = (int) masterGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & masterPixels[nIndex]) != 0)) {
							dwSumMaster += (double) (0xff & masterPixels[nIndex]);
							lCount++;
						}
					} // end row(j)
				} // end col(i)
				histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;

				// Slave
				width = (int) slaveGridEnvelope.getWidth();
				height = (int) slaveGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & slavePixels[nIndex]) != 0)) {
							dwSumSlave += (double) (0xff & slavePixels[nIndex]);
							lCount++;
						}
					} // end row(j)
				} // end col(i)
				histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

				// @NOTICE : �ߺ������� �����Ƿ� ��ȣ �л갪 ��� �ȵ�
				histMatchInfo._dblVariance = 1.0;
				histMatchInfo._dblCovariance = 1.0;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.calcHistogramMatchingInfoByMatchMeanStdDev : " + ex.toString());
			ex.printStackTrace();
		}

		return histMatchInfo;
	}

	// Match Cumulative Frequency ������׷� ��Ī �˰������� ����(Master) ���� ����
	// ���(Slave) ������ ������׷� ��Ī ������ ����Ѵ�.
	// @ masterPixels : ����(Master) ���� ȭ������
	// @ masterGridEnvelope : ����(Master) ���� ũ��
	// @ slavePixels : ���(Slave) ���� ȭ������
	// @ slaveGridEnvelope : ���(Slave) ���� ũ��
	// @ isOverlap : ��ø ���� ���� (true : ��ø ����, false : ��ü ����)
	// @
	// @ return : GHistogramMatchingInfo ������׷� ��Ī ����
	private GHistogramMatchingData calcHistogramMatchingInfoByMatchCumulativeFrequency(byte[] masterPixels,
			GridEnvelope2D masterGridEnvelope, byte[] slavePixels, GridEnvelope2D slaveGridEnvelope,
			boolean isOverlap) {
		// Match Cumulative Frequency
		GHistogramMatchingData histMatchInfo = new GHistogramMatchingData();
		int nGrayLevel = 256;
		int i = 0, j = 0; // Initialize...
		int width = 0, height = 0;
		int Master_DN = 0, Slave_DN = 0; // Initialize...
		double dwSumMaster = 0.0;
		double dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;
		int[] nMasterHistogram = new int[nGrayLevel];
		int[] nSlaveHistogram = new int[nGrayLevel];
		int nDiff1 = 0, nDiff2 = 0;
		int nRoopCount = 0;

		histMatchInfo._nMinMaster = 255;
		histMatchInfo._nMinSlave = 255;
		histMatchInfo._nMaxMaster = 0;
		histMatchInfo._nMaxSlave = 0;
		histMatchInfo._nLUTModified = new int[nGrayLevel]; // Match Cumulative Frequency

		for (i = 0; i < nGrayLevel; i++) {
			nMasterHistogram[i] = 0;
			nSlaveHistogram[i] = 0;

			// Look Up Table �ʱ�ȭ (10.19)
			histMatchInfo._nLUTModified[i] = i;
		}

		try {
			if (isOverlap) {
				width = (int) masterGridEnvelope.getWidth();
				height = (int) masterGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & masterPixels[nIndex]) != 0) && ((0xff & slavePixels[nIndex]) != 0)) {
							lCount++;
							Master_DN = (0xff & masterPixels[nIndex]);
							Slave_DN = (0xff & slavePixels[nIndex]);

							dwSumMaster += (double) (0xff & masterPixels[nIndex]);
							dwSumSlave += (double) (0xff & slavePixels[nIndex]);

							nMasterHistogram[Master_DN]++;
							nSlaveHistogram[Slave_DN]++;

							if ((0xff & masterPixels[nIndex]) < histMatchInfo._nMinMaster)
								histMatchInfo._nMinMaster = (0xff & masterPixels[nIndex]);

							if ((0xff & masterPixels[nIndex]) > histMatchInfo._nMaxMaster)
								histMatchInfo._nMaxMaster = (0xff & masterPixels[nIndex]);

							if ((0xff & slavePixels[nIndex]) < histMatchInfo._nMinSlave)
								histMatchInfo._nMinSlave = (0xff & slavePixels[nIndex]);

							if ((0xff & slavePixels[nIndex]) > histMatchInfo._nMaxSlave)
								histMatchInfo._nMaxSlave = (0xff & slavePixels[nIndex]);
						}
					} // end row(j)
				} // end col(i)

				// get mean
				histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
				histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

				// get cumulative histogram
				for (i = 1; i < nGrayLevel; i++) {
					nMasterHistogram[i] = nMasterHistogram[i] + nMasterHistogram[i - 1];
					nSlaveHistogram[i] = nSlaveHistogram[i] + nSlaveHistogram[i - 1];
				}
			} else {
				// --------------------------------------------------------------------------------//
				// Master
				// --------------------------------------------------------------------------------//
				width = (int) masterGridEnvelope.getWidth();
				height = (int) masterGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & masterPixels[nIndex]) != 0)) {
							lCount++;
							Master_DN = (0xff & masterPixels[nIndex]);
							dwSumMaster += (double) (0xff & masterPixels[nIndex]);
							nMasterHistogram[Master_DN]++;

							if ((0xff & masterPixels[nIndex]) < histMatchInfo._nMinMaster)
								histMatchInfo._nMinMaster = (0xff & masterPixels[nIndex]);

							if ((0xff & masterPixels[nIndex]) > histMatchInfo._nMaxMaster)
								histMatchInfo._nMaxMaster = (0xff & masterPixels[nIndex]);
						}
					} // end row(j)
				} // end col(i)

				// get mean
				histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;

				// get cumulative histogram
				for (i = 1; i < nGrayLevel; i++) {
					nMasterHistogram[i] = nMasterHistogram[i] + nMasterHistogram[i - 1];
				}
				// --------------------------------------------------------------------------------//

				// --------------------------------------------------------------------------------//
				// Slave
				// --------------------------------------------------------------------------------//
				width = (int) slaveGridEnvelope.getWidth();
				height = (int) slaveGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & slavePixels[nIndex]) != 0)) {
							lCount++;
							Slave_DN = (0xff & slavePixels[nIndex]);
							dwSumSlave += (double) (0xff & slavePixels[nIndex]);
							nSlaveHistogram[Slave_DN]++;

							if ((0xff & slavePixels[nIndex]) < histMatchInfo._nMinSlave)
								histMatchInfo._nMinSlave = (0xff & slavePixels[nIndex]);

							if ((0xff & slavePixels[nIndex]) > histMatchInfo._nMaxSlave)
								histMatchInfo._nMaxSlave = (0xff & slavePixels[nIndex]);
						}
					} // end row(j)
				} // end col(i)

				// get mean
				histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;

				// get cumulative histogram
				for (i = 1; i < nGrayLevel; i++) {
					nSlaveHistogram[i] = nSlaveHistogram[i] + nSlaveHistogram[i - 1];
				}
				// --------------------------------------------------------------------------------//
			}

			// Create modified LUT of slave image
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

			for (i = histMatchInfo._nMaxSlave + 1; i < nGrayLevel; i++) {
				if (i < histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave])
					histMatchInfo._nLUTModified[i] = histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave];
				if (i >= histMatchInfo._nLUTModified[histMatchInfo._nMaxSlave])
					histMatchInfo._nLUTModified[i] = i;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println(
					"GImageEnhancement.calcHistogramMatchingInfoByMatchCumulativeFrequency : " + ex.toString());
			ex.printStackTrace();
//[DEBUG]
			if (_IS_DEBUG) {
				System.out.println("[DEBUG]GImageEnhancement.calcHistogramMatchingInfoByMatchCumulativeFrequency : i = "
						+ i + ", j = " + j + ", nRoopCount = " + nRoopCount);
			}
//[DEBUG]			

		}

		return histMatchInfo;
	}

	// Hue Adjustment Through Moving the Histogram ������׷� ��Ī �˰�������
	// ����(Master) ���� ���� ���(Slave) ������ ������׷� ��Ī ������ ����Ѵ�.
	// @ masterPixels : ����(Master) ���� ȭ������
	// @ masterGridEnvelope : ����(Master) ���� ũ��
	// @ slavePixels : ���(Slave) ���� ȭ������
	// @ slaveGridEnvelope : ���(Slave) ���� ũ��
	// @ isOverlap : ��ø ���� ���� (true : ��ø ����, false : ��ü ����)
	// @
	// @ return : GHistogramMatchingInfo ������׷� ��Ī ����
	private GHistogramMatchingData calcHistogramMatchingInfoByHueAdjustment(byte[] masterPixels,
			GridEnvelope2D masterGridEnvelope, byte[] slavePixels, GridEnvelope2D slaveGridEnvelope,
			boolean isOverlap) {
		// Hue Adjustment
		GHistogramMatchingData histMatchInfo = new GHistogramMatchingData();
		int i = 0, j = 0; // Initialize...
		int width = 0, height = 0;
		double dwSumMaster = 0.0;
		double dwSumSlave = 0.0;
		int lCount = 0;
		int nIndex = 0;

		// Calculate basic statistics of each image
		histMatchInfo._nMinMaster = 255; // Hue Adjustment & Match Cumulative Frequency
		histMatchInfo._nMinSlave = 255;
		histMatchInfo._nMaxMaster = 0;
		histMatchInfo._nMaxSlave = 0;

		try {
			if (isOverlap) {
				width = (int) masterGridEnvelope.getWidth();
				height = (int) masterGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & masterPixels[nIndex]) != 0) && ((0xff & slavePixels[nIndex]) != 0)) {
							lCount++;
							dwSumMaster += (double) (0xff & masterPixels[nIndex]);
							dwSumSlave += (double) (0xff & slavePixels[nIndex]);

							if ((0xff & masterPixels[nIndex]) < histMatchInfo._nMinMaster)
								histMatchInfo._nMinMaster = (0xff & masterPixels[nIndex]);

							if ((0xff & masterPixels[nIndex]) > histMatchInfo._nMaxMaster)
								histMatchInfo._nMaxMaster = (0xff & masterPixels[nIndex]);

							if ((0xff & slavePixels[nIndex]) < histMatchInfo._nMinSlave)
								histMatchInfo._nMinSlave = (0xff & slavePixels[nIndex]);

							if ((0xff & slavePixels[nIndex]) > histMatchInfo._nMaxSlave)
								histMatchInfo._nMaxSlave = (0xff & slavePixels[nIndex]);
						}

					} // end row(j)
				} // end col(i)

				// get mean
				histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
				histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;
			} else {
				// --------------------------------------------------------------------------------//
				// Master
				// --------------------------------------------------------------------------------//
				width = (int) masterGridEnvelope.getWidth();
				height = (int) masterGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & masterPixels[nIndex]) != 0)) {
							lCount++;
							dwSumMaster += (double) (0xff & masterPixels[nIndex]);

							if ((0xff & masterPixels[nIndex]) < histMatchInfo._nMinMaster)
								histMatchInfo._nMinMaster = (0xff & masterPixels[nIndex]);

							if ((0xff & masterPixels[nIndex]) > histMatchInfo._nMaxMaster)
								histMatchInfo._nMaxMaster = (0xff & masterPixels[nIndex]);
						}

					} // end row(j)
				} // end col(i)

				// get mean
				histMatchInfo._dblMeanMaster = dwSumMaster / (double) lCount;
				// --------------------------------------------------------------------------------//

				// --------------------------------------------------------------------------------//
				// Slave
				// --------------------------------------------------------------------------------//
				width = (int) slaveGridEnvelope.getWidth();
				height = (int) slaveGridEnvelope.getHeight();
				lCount = 0;
				for (j = 0; j < width; j++) // col(j)
				{
					for (i = 0; i < height; i++) // row(i)
					{
						nIndex = j + width * i;

						if (((0xff & slavePixels[nIndex]) != 0)) {
							lCount++;
							dwSumSlave += (double) (0xff & slavePixels[nIndex]);

							if ((0xff & slavePixels[nIndex]) < histMatchInfo._nMinSlave)
								histMatchInfo._nMinSlave = (0xff & slavePixels[nIndex]);

							if ((0xff & slavePixels[nIndex]) > histMatchInfo._nMaxSlave)
								histMatchInfo._nMaxSlave = (0xff & slavePixels[nIndex]);
						}

					} // end row(j)
				} // end col(i)

				// get mean
				histMatchInfo._dblMeanSlave = dwSumSlave / (double) lCount;
				// --------------------------------------------------------------------------------//
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GImageEnhancement.calcHistogramMatchingInfoByHueAdjustment : " + ex.toString());
			ex.printStackTrace();
		}

		return histMatchInfo;
	}
}
