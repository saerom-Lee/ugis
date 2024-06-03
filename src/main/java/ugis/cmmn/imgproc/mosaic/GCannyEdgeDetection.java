package ugis.cmmn.imgproc.mosaic;

import ugis.cmmn.imgproc.data.GCannyEdgeData;

//Canny Edge ���� Ŭ����
public class GCannyEdgeDetection {

	// ���� ���ǵǾ� �ִ� ����
	public static final float _BOOSTBLURFACTOR = 90.f;
	public static final int _EDGE = 0;
	public static final int _NO_EDGE = 255;
	public static final int _POSSIBLE_EDGE = 128;

	// Input
	private byte[] _pOpenImage = null;
	private int _lImageW = 0;
	private int _lImageH = 0;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final boolean _IS_DEBUG = false;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Canny Edge�� �����Ѵ�.
	// @ pImage : ������ byte�� ȭ�Ұ�
	// @ width : ������ ���� ũ��
	// @ height: ������ ���� ũ��
	// @ Sigma : ǥ������
	// @ LowThresh : �ּ� �Ӱ谪
	// @ HighThresh: �ִ� �Ӱ谪
	// @
	// @ return : GCannyEdgeInfo ����� Canny Edge ����
	public GCannyEdgeData procCannyOperator(byte[] pImage, int width, int height, float Sigma, float LowThresh,
			float HighThresh) {
		GCannyEdgeData edgeInfo = new GCannyEdgeData();

		_pOpenImage = pImage;
		_lImageW = width;
		_lImageH = height;

		/***********************************
		 * perform canny edge detection *
		 ***********************************/

		int[] nSmoothImage = null; // The image after Gaussian Smoothing
		int[] Grad_x = null; // The first derivative image, x and y direction
		int[] Grad_y = null;
		short[] Local_Maxima = null; // Points that are local maximal magnitude

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GCannyEdgeDetection.CannySeamlineOperator : ");
			System.out.println("[DEBUG]\t Start : CannySeamlineOperator");
			System.out.println("[DEBUG]\t Image size : -> (" + _lImageW + ", " + _lImageH + ")");
		}
//[DEBUG]

		try {
			// -----------------------------------------------------------------------------------//
			nSmoothImage = new int[_lImageW * _lImageH];
			Grad_x = new int[_lImageW * _lImageH];
			Grad_y = new int[_lImageW * _lImageH];

			// Perform Gaussian Smoothing on the image using the input standard
			// deviation(simga)
			GaussianFilter(Sigma, nSmoothImage);

			// Compute the first derivative in the x and y directions
			FirstDerivative_x_y(nSmoothImage, Grad_x, Grad_y);

			nSmoothImage = null; // Memory free of the smooth image
			// -----------------------------------------------------------------------------------//

			// -----------------------------------------------------------------------------------//
			// Compute the magnitude of the gradient
			edgeInfo._GradMag = new int[_lImageW * _lImageH];
			GradientMagnitude(Grad_x, Grad_y, edgeInfo._GradMag);

			// Perform non-maximal suppression
			Local_Maxima = new short[_lImageH * _lImageW]; // Alloacate local maxima image
			NonMaximalSuppression(edgeInfo._GradMag, Grad_x, Grad_y, Local_Maxima);

			Grad_x = null; // Memory free of the x-gradient
			Grad_y = null; // Memory free of the y-gradient
			// -----------------------------------------------------------------------------------//

			// -----------------------------------------------------------------------------------//
			// Use hysteresis to mark the edge pixels
			edgeInfo._EdgeImage = new short[_lImageW * _lImageH];
			HysteresisThresh(edgeInfo._GradMag, Local_Maxima, LowThresh, HighThresh, edgeInfo._EdgeImage);

			edgeInfo._GradMag = null; // Memory free of the gradient magnitude
			Local_Maxima = null; // Memory free of the local maxima
			// -----------------------------------------------------------------------------------//
		} catch (Exception ex) {
			System.out.println("GCannyEdgeDetection.CannySeamlineOperator : " + ex.toString());
			ex.printStackTrace();
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t End : CannySeamlineOperator");
		}
//[DEBUG] 

		return edgeInfo;
	}

	// Seamline Canny Edge�� �����Ѵ�.
	// @ pImage : ������ byte�� ȭ�Ұ�
	// @ width : ������ ���� ũ��
	// @ height: ������ ���� ũ��
	// @ Sigma : ǥ������
	// @ LowThresh : �ּ� �Ӱ谪
	// @ HighThresh: �ִ� �Ӱ谪
	// @
	// @ return : GCannyEdgeInfo ����� Canny Edge ����
	public GCannyEdgeData procCannySeamlineOperator(byte[] pImage, int width, int height, float Sigma, float LowThresh,
			float HighThresh) {
		GCannyEdgeData edgeInfo = new GCannyEdgeData();

		_pOpenImage = pImage;
		_lImageW = width;
		_lImageH = height;

		/***********************************
		 * perform canny edge detection *
		 ***********************************/

		int[] nSmoothImage = null; // The image after Gaussian Smoothing
		int[] Grad_x = null; // The first derivative image, x and y direction
		int[] Grad_y = null;
		short[] Local_Maxima = null; // Points that are local maximal magnitude

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GCannyEdgeDetection.CannySeamlineOperator : ");
			System.out.println("[DEBUG]\t Start : CannySeamlineOperator");
			System.out.println("[DEBUG]\t Image size : -> (" + _lImageW + ", " + _lImageH + ")");
		}
//[DEBUG]

		try {
			// -----------------------------------------------------------------------------------//
			nSmoothImage = new int[_lImageW * _lImageH];
			Grad_x = new int[_lImageW * _lImageH];
			Grad_y = new int[_lImageW * _lImageH];

			// Perform Gaussian Smoothing on the image using the input standard
			// deviation(simga)
			GaussianFilter(Sigma, nSmoothImage);

			// Compute the first derivative in the x and y directions
			FirstDerivative_x_y(nSmoothImage, Grad_x, Grad_y);

			nSmoothImage = null; // Memory free of the smooth image
			// -----------------------------------------------------------------------------------//

			// -----------------------------------------------------------------------------------//
			// Compute the magnitude of the gradient
			edgeInfo._GradMag = new int[_lImageW * _lImageH];
			GradientMagnitude(Grad_x, Grad_y, edgeInfo._GradMag);

			// Perform non-maximal suppression
			Local_Maxima = new short[_lImageH * _lImageW]; // Alloacate local maxima image
			NonMaximalSuppression(edgeInfo._GradMag, Grad_x, Grad_y, Local_Maxima);

			Grad_x = null; // Memory free of the x-gradient
			Grad_y = null; // Memory free of the y-gradient
			// -----------------------------------------------------------------------------------//

			// -----------------------------------------------------------------------------------//
			// Use hysteresis to mark the edge pixels
			edgeInfo._EdgeImage = new short[_lImageW * _lImageH];
			HysteresisThresh(edgeInfo._GradMag, Local_Maxima, LowThresh, HighThresh, edgeInfo._EdgeImage);

			Local_Maxima = null; // Memory free of the local maxima
			// -----------------------------------------------------------------------------------//
		} catch (Exception ex) {
			System.out.println("GCannyEdgeDetection.CannySeamlineOperator : " + ex.toString());
			ex.printStackTrace();
		}

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]\t End : CannySeamlineOperator");
		}
//[DEBUG]

		return edgeInfo;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ����þ� ���͸� �����Ͽ� ������ ��źȭ�Ѵ�.
	// @ Sigma : ǥ������
	// @
	// @ return : SmoothImage ��źȭ�� ������ short�� ȭ�Ұ�
	private void GaussianFilter(float Sigma, int[] SmoothImage) {
		/****************************************
		 * Blur an image with a Gaussian Filter *
		 ****************************************/

		int r = 0, c = 0;
		int rr = 0, cc = 0;
		int[] kernel_size = new int[1]; // Dimension of the gaussian kernel
		float[] kernel = null; // A 1-D Gaussian Kernel
		int kernel_center = 0; // Half of the Window(Kernel) Size
		float[] tmpImage = new float[_lImageH * _lImageW]; // Buffer for seperable filter gaussian smoothing
															// Allocate a temporary buffer and the smoothed image
		float dot = 0.f; // Dot product summing variable
		float sum = 0.f; // Sum of the kernel weights variable

//[DEBUG]
		if (_IS_DEBUG) {
			System.out.println("[DEBUG]GCannyEdgeDetection.GaussianFilter : ");
			System.out.println("[DEBUG]\t Image size : -> (" + _lImageW + ", " + _lImageH + ")");
		}
//[DEBUG]

		// Create a 1-D gaussian smoothing kernel
		kernel = Make_GaussianKernel(Sigma, kernel_size);
		kernel_center = (int) ((float) kernel_size[0] / 2.f);

		try {
			// Blur in the x-direction
			for (r = 0; r < _lImageH; r++) {
				for (c = 0; c < _lImageW; c++) {

					dot = 0.f;
					sum = 0.f;

					for (cc = (-kernel_center); cc <= kernel_center; cc++) {

						if ((c + cc >= 0) && (c + cc < _lImageW)) {
							dot += (float) (0xff & _pOpenImage[(c + cc) + _lImageW * r]) * kernel[kernel_center + cc];
							sum += kernel[kernel_center + cc];
						}
					}

					tmpImage[c + _lImageW * r] = dot / sum;
				}
			}

			// Blur in the y-direction
			for (c = 0; c < _lImageW; c++) {
				for (r = 0; r < _lImageH; r++) {

					dot = 0.f;
					sum = 0.f;

					for (rr = (-kernel_center); rr <= kernel_center; rr++) {

						if ((r + rr >= 0) && (r + rr < _lImageH)) {
							dot += tmpImage[c + _lImageW * (r + rr)] * kernel[kernel_center + rr];
							sum += kernel[kernel_center + rr];
						}
					}

					SmoothImage[c + _lImageW * r] = (short) (dot * _BOOSTBLURFACTOR / sum + 0.5);
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GCannyEdgeDetection.GaussianFilter : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();
		}

		tmpImage = null;
		kernel = null;
	}

	// ����þ� Ŀ���� �����Ѵ�.
	// @ Sigma : ǥ������
	// @
	// @ return : WindowSize ������ ũ��
	// @ return : float[] ����þ� Ŀ��
	private float[] Make_GaussianKernel(float Sigma, int[] WindowSize) {
		int winSize = (int) (1 + 2 * Math.ceil(2.5 * Sigma));
		float[] kernel = new float[winSize]; // Allocate a kernel
		int i = 0;
		int center = (int) (winSize / 2.0);
		float x = 0.f, fx = 0.f;
		float sum = 0.f;

		/********************************
		 * Create a 1-D Gaussian Kernel *
		 ********************************/

		try {
			for (i = 0; i < winSize; i++) {
				x = (float) (i - center);
				fx = (float) (Math.pow(2.71828, -0.5 * x * x / (Sigma * Sigma)) / (Sigma * Math.sqrt(6.2831853)));
				kernel[i] = fx;
				sum += fx;
			}

			for (i = 0; i < winSize; i++)
				kernel[i] /= sum;
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out
					.println("GCannyEdgeDetection.Make_GaussianKernel : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();
		}

		WindowSize[0] = winSize;
		return kernel;
	}

	// ���� ���� ���� ���⿡ ���� 1�� ��̺��� ����Ѵ�.
	// @ SmoothImage : ��źȭ�� ������ short�� ȭ�Ұ�
	// @
	// @ return : grad_x ���ι��� 1�� ��̺� ����
	// @ return : grad_y ���ι��� 1�� ��̺� ����
	private void FirstDerivative_x_y(int[] SmoothImage, int[] grad_x, int[] grad_y) {
		/****************************************************************************
		 * Compute the first derivative of the image in both the x and y directions * *
		 * The differential filters that are used are * -1 * gradient_x = -1 0 +1 and
		 * gradient_y = 0 * 1 *
		 ****************************************************************************/

		int r, c, pos;

		// Allocate images to store the derivatives
		//

		try {
			// Compute the x-derivative
			for (r = 0; r < _lImageH; r++) {
				pos = r * _lImageW;
				grad_x[pos] = (SmoothImage[pos + 1] - SmoothImage[pos]);
				pos++;

				for (c = 1; c < (_lImageW - 1); c++, pos++) {
					grad_x[pos] = (SmoothImage[pos + 1] - SmoothImage[pos - 1]);
				}

				grad_x[pos] = (SmoothImage[pos] - SmoothImage[pos - 1]);
			}

			// Compute the y-derivative
			for (c = 0; c < _lImageW; c++) {
				pos = c;
				grad_y[pos] = (SmoothImage[pos + _lImageW] - SmoothImage[pos]);
				pos += _lImageW;

				for (r = 1; r < (_lImageH - 1); r++, pos += _lImageW) {
					grad_y[pos] = (SmoothImage[pos + _lImageW] - SmoothImage[pos - _lImageW]);
				}

				grad_y[pos] = (SmoothImage[pos] - SmoothImage[pos - _lImageW]);
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out
					.println("GCannyEdgeDetection.FirstDerivative_x_y : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();
		}
	}

	// ���� ���� ���� ���⿡ ���� 1�� ��̺��� ���⸦ ����Ѵ�.
	// @ grad_x : ���ι��� 1�� ��̺� ����
	// @ grad_y : ���ι��� 1�� ��̺� ����
	// @
	// @ return : Grad_Magnitude 1�� ��̺� �̹���
	private void GradientMagnitude(int[] grad_x, int[] grad_y, int[] Grad_Magnitude) {
		/*****************************************
		 * Compute the magnitude of the gradient *
		 *****************************************/

		int r, c, pos;

		// Allocate an image to store the magnitude of the gradient
		//

		try {
			for (r = 0, pos = 0; r < _lImageH; r++) {
				for (c = 0; c < _lImageW; c++, pos++) {

					int sq1 = (int) Math.pow(grad_x[pos], 2);
					int sq2 = (int) Math.pow(grad_y[pos], 2);
					Grad_Magnitude[pos] = (short) (0.5 + Math.sqrt((float) sq1 + (float) sq2));
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GCannyEdgeDetection.GradientMagnitude : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();
		}
	}

	// ������ 1�� ��̺� �̹����� ���� NMS(Non-maximal suppression)�� ����Ѵ�.
	// @ Magnitude : 1�� ��̺� �̹���
	// @ grad_x : ���ι��� 1�� ��̺� ����
	// @ grad_y : ���ι��� 1�� ��̺� ����
	// @
	// @ return : pResult NMS(Non-maximal suppression) ����
	private void NonMaximalSuppression(int[] Magnitude, int[] grad_x, int[] grad_y, short[] pResult) {
		/******************************************************************
		 * Non-Maximal suppression to the magnitude of the gradient image *
		 ******************************************************************/

		int r = 0, c = 0, count = 0;
		int z1 = 0, z2 = 0;
		int m00 = 0, gx = 0, gy = 0;
		float mag1 = 0, mag2 = 0, x_perp = 0, y_perp = 0;

		// short[] MagRow_Ptr = null;
		// short[] Mag_Ptr = null;
		// short[] GxRow_Ptr = null;
		// short[] Gx_Ptr = null;
		// short[] GyRow_Ptr = null;
		// short[] Gy_Ptr = null;
		// Byte[] ResultRow_Ptr = null;
		// Byte[] Result_Ptr = null;
		// short[] MagRow_Ptr = null;
		int nMagRow = 0;
		int nMag = 0;
		int nGxRow = 0;
		int nGx = 0;
		int nGyRow = 0;
		int nGy = 0;
		int nResultRow = 0;
		int nResult = 0;

		try {
			// Zero the edges of the result image
			nResultRow = 0; // ResultRow_Ptr = pResult;
			nResult = _lImageW * (_lImageH - 1); // Result_Ptr = pResult+_lImageW*(_lImageH-1);
			for (count = 0; count < _lImageW; nResultRow++, nResult++, count++) { // ResultRow_Ptr++, Result_Ptr++
				pResult[nResultRow] = pResult[nResult] = 0;
			}

			nResultRow = 0; // Result_Ptr=pResult;
			nResult = _lImageW - 1; // ResultRow_Ptr=pResult+_lImageW-1
			for (count = 0; count < _lImageH; count++, nResult += _lImageW, nResultRow += _lImageW) { // Result_Ptr+=_lImageW,
																										// ResultRow_Ptr+=_lImageW
				pResult[nResult] = pResult[nResultRow] = 0;
			}

			// Supperssion non-maximum points
			nMagRow = _lImageW + 1; // MagRow_Ptr=Magnitude+_lImageW+1;
			nGxRow = _lImageW + 1; // GxRow_Ptr=grad_x+_lImageW+1;
			nGyRow = _lImageW + 1; // GyRow_Ptr=grad_y+_lImageW+1;
			nResultRow = _lImageW + 1; // ResultRow_Ptr=pResult+_lImageW+1;
			for (r = 1; r < _lImageH
					- 2; r++, nMagRow += _lImageW, nGxRow += _lImageW, nGyRow += _lImageW, nResultRow += _lImageW) { // MagRow_Ptr+=m_lImageW,
																														// GxRow_Ptr+=m_lImageW,
																														// GyRow_Ptr+=m_lImageW,
																														// ResultRow_Ptr+=m_lImageW
				nMag = nMagRow; // Mag_Ptr=MagRow_Ptr;
				nGx = nGxRow; // Gx_Ptr=GxRow_Ptr;
				nGy = nGyRow; // Gy_Ptr=GyRow_Ptr;
				nResult = nResultRow; // Result_Ptr=ResultRow_Ptr;
				for (c = 1; c < _lImageW - 2; c++, nMag++, nGx++, nGy++, nResult++) { // Mag_Ptr++, Gx_Ptr++, Gy_Ptr++,
																						// Result_Ptr++
					m00 = Magnitude[nMag];

					if (m00 == 0) {
						pResult[nResult] = _NO_EDGE;
					} else {
						x_perp = -(gx = grad_x[nGx]) / ((float) m00);
						y_perp = (gy = grad_x[nGx]) / ((float) m00);
					}

					if (gx >= 0) {
						if (gy >= 0) {
							if (gx >= gy) { // [1 1 1]
								// left point
								z1 = Magnitude[nMag - 1]; // *(Mag_Ptr - 1);
								z2 = Magnitude[nMag - _lImageW - 1]; // *(Mag_Ptr - _lImageW - 1);
								mag1 = (m00 - z1) * x_perp + (z2 - z1) * y_perp;

								// right point
								z1 = Magnitude[nMag + 1]; // *(Mag_Ptr + 1);
								z2 = Magnitude[nMag + _lImageW + 1]; // *(Mag_Ptr + _lImageW + 1);
								mag2 = (m00 - z1) * x_perp + (z2 - z1) * y_perp;
							} else { // [1 1 0]
								// left point
								z1 = Magnitude[nMag - _lImageW]; // *(Mag_Ptr - _lImageW);
								z2 = Magnitude[nMag - _lImageW - 1]; // *(Mag_Ptr - _lImageW - 1);
								mag1 = (z1 - z2) * x_perp + (z1 - m00) * y_perp;

								// right point
								z1 = Magnitude[nMag + _lImageW]; // *(Mag_Ptr + _lImageW);
								z2 = Magnitude[nMag + _lImageW + 1]; // *(Mag_Ptr + _lImageW + 1);
								mag2 = (z1 - z2) * x_perp + (z1 - m00) * y_perp;
							}
						} else { // gy<0
							if (gx >= -gy) { // [1 0 1]
								// left point
								z1 = Magnitude[nMag - 1]; // *(Mag_Ptr - 1);
								z2 = Magnitude[nMag + _lImageW - 1]; // *(Mag_Ptr + _lImageW - 1);
								mag1 = (m00 - z1) * x_perp + (z1 - z2) * y_perp;

								// right point
								z1 = Magnitude[nMag + 1]; // *(Mag_Ptr + 1);
								z2 = Magnitude[nMag - _lImageW + 1]; // *(Mag_Ptr - _lImageW + 1);
								mag2 = (m00 - z1) * x_perp + (z1 - z2) * y_perp;
							} else { // [1 0 0]
								// left point
								z1 = Magnitude[nMag + _lImageW]; // *(Mag_Ptr + _lImageW);
								z2 = Magnitude[nMag + _lImageW - 1]; // *(Mag_Ptr + _lImageW - 1);
								mag1 = (z1 - z2) * x_perp + (m00 - z1) * y_perp;

								// right point
								z1 = Magnitude[nMag - _lImageW]; // *(Mag_Ptr - _lImageW);
								z2 = Magnitude[nMag - _lImageW + 1]; // *(Mag_Ptr - _lImageW + 1);
								mag2 = (z1 - z2) * x_perp + (m00 - z1) * y_perp;
							}
						}

					} else { // gx<0
						if ((gy = grad_y[nGy]) >= 0) { // if( (gy=(*Gy_Ptr)) >= 0 ) {
							if (-gx >= gy) { // [0 1 1]
								// left point
								z1 = Magnitude[nMag + 1]; // *(Mag_Ptr + 1);
								z2 = Magnitude[nMag - _lImageW + 1]; // *(Mag_Ptr - _lImageW + 1);
								mag1 = (z1 - m00) * x_perp + (z2 - z1) * y_perp;

								// right point
								z1 = Magnitude[nMag - 1]; // *(Mag_Ptr - 1);
								z2 = Magnitude[nMag + _lImageW - 1]; // *(Mag_Ptr + _lImageW - 1);
								mag2 = (z1 - m00) * x_perp + (z2 - z1) * y_perp;
							} else { // [0 1 0]
								// left point
								z1 = Magnitude[nMag - _lImageW]; // *(Mag_Ptr - _lImageW);
								z2 = Magnitude[nMag - _lImageW + 1]; // *(Mag_Ptr - _lImageW + 1);
								mag1 = (z2 - z1) * x_perp + (z1 - m00) * y_perp;

								// right point
								z1 = Magnitude[nMag + _lImageW]; // *(Mag_Ptr + _lImageW);
								z2 = Magnitude[nMag + _lImageW - 1]; // *(Mag_Ptr + _lImageW - 1);
								mag2 = (z2 - z1) * x_perp + (z1 - m00) * y_perp;
							}
						} else { // (gy=(*Gy_Ptr)) < 0
							if (-gx > -gy) { // [0 0 1]
								// left point
								z1 = Magnitude[nMag + 1]; // *(Mag_Ptr + 1);
								z2 = Magnitude[nMag + _lImageW + 1]; // *(Mag_Ptr + _lImageW + 1);
								mag1 = (z1 - m00) * x_perp + (z1 - z2) * y_perp;

								// right point
								z1 = Magnitude[nMag - 1]; // *(Mag_Ptr - 1);
								z2 = Magnitude[nMag - _lImageW - 1]; // *(Mag_Ptr - _lImageW - 1);
								mag2 = (z1 - m00) * x_perp + (z1 - z2) * y_perp;
							} else { // [0 0 0]
								// left point
								z1 = Magnitude[nMag + _lImageW]; // *(Mag_Ptr + _lImageW);
								z2 = Magnitude[nMag + _lImageW + 1]; // *(Mag_Ptr + _lImageW + 1);
								mag1 = (z2 - z1) * x_perp + (m00 - z1) * y_perp;

								// right point
								z1 = Magnitude[nMag - _lImageW]; // *(Mag_Ptr - _lImageW);
								z2 = Magnitude[nMag - _lImageW - 1]; // *(Mag_Ptr - _lImageW - 1);
								mag2 = (z2 - z1) * x_perp + (m00 - z1) * y_perp;
							}
						}
					}

					// Now determine if the current point is a maximum point
					if ((mag1 > 0.0) || (mag2 > 0.0)) {
						pResult[nResult] = _NO_EDGE;
					} else {
						if (mag2 == 0.0)
							pResult[nResult] = _NO_EDGE;
						else
							pResult[nResult] = _POSSIBLE_EDGE;
					}

				} // (c)
			} // (r)
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println(
					"GCannyEdgeDetection.NonMaximalSuppression : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();
		}
	}

	// ������ 1�� ��̺� �̹����� NMS(Non-maximal suppression) ������ ���� Edge �̹�����
	// �����Ѵ�.
	// @ Grad_Mag : 1�� ��̺� �̹���
	// @ LocalMaxima : NMS(Non-maximal suppression) ����
	// @ tlow : �ּ� �Ӱ谪
	// @ thigh : �ִ� �Ӱ�
	// @
	// @ return : EdgeImage Edge �̹���
	private void HysteresisThresh(int[] Grad_Mag, short[] LocalMaxima, float tlow, float thigh, short[] EdgeImage) {
		/******************************************************************
		 * This routine finds edges that are above some high threshold or are connected
		 * to a high pixel by a path of pixels greater than a low threshold.
		 ******************************************************************/

		int r = 0, c = 0, pos = 0;
		int num_edges = 0, high_count = 0;
		int low_threshold = 0, high_threshold = 0;
		int[] hist = new int[32768];
		int maximum_mag = 0;
		int[] count = new int[1];

		try {
			for (r = 0, pos = 0; r < _lImageH; r++) {
				for (c = 0; c < _lImageW; c++, pos++) {
					if (LocalMaxima[pos] == _POSSIBLE_EDGE)
						EdgeImage[pos] = _POSSIBLE_EDGE;
					else
						EdgeImage[pos] = _NO_EDGE;
				}
			}

			for (r = 0, pos = 0; r < _lImageH; r++, pos += _lImageW) {
				EdgeImage[pos] = _NO_EDGE;
				EdgeImage[pos + _lImageW - 1] = _NO_EDGE;
			}

			pos = (_lImageH - 1) * _lImageW;
			for (c = 0; c < _lImageW; c++, pos++) {
				EdgeImage[c] = _NO_EDGE;
				EdgeImage[pos] = _NO_EDGE;
			}

			// Compute the histogram of the magnitude image
			// Then use the histogram to compute hysteresis threshold
			for (r = 0; r < 32768; r++)
				hist[r] = 0;

			for (r = 0, pos = 0; r < _lImageH; r++) {
				for (c = 0; c < _lImageW; c++, pos++) {
					if (EdgeImage[pos] == _POSSIBLE_EDGE)
						hist[Grad_Mag[pos]]++;
				}
			}

			// Compute the number of pixels that passed the non-maximal suppression
			for (r = 1, num_edges = 0; r < 32768; r++) {
				if (hist[r] != 0)
					maximum_mag = r;
				num_edges += hist[r];
			}

			high_count = (int) (num_edges * thigh + 0.5);

			// Compute high and low threshold values
			r = 1;
			num_edges = hist[1];
			while ((r < (maximum_mag - 1)) && (num_edges < high_count)) {
				r++;
				num_edges += hist[r];
			}

			high_threshold = r;
			low_threshold = (int) (high_threshold * tlow + 0.5);

			// This loop looks for pixels above the highthreshold to locate edges and
			// then calls follow edge to continue the edge
			for (r = 0, pos = 0; r < _lImageH; r++) {
				for (c = 0; c < _lImageW; c++, pos++) {

					count[0] = 0;
					if ((EdgeImage[pos] == _POSSIBLE_EDGE) && (Grad_Mag[pos] >= high_threshold)) {
						EdgeImage[pos] = _EDGE;
						FollowEdge(EdgeImage, pos, Grad_Mag, pos, low_threshold, count);
					}
				}
			}

			// Set all the remaining possible edges to non-edges
			for (r = 0, pos = 0; r < _lImageH; r++) {
				for (c = 0; c < _lImageW; c++, pos++) {
					if (EdgeImage[pos] != _EDGE)
						EdgeImage[pos] = _NO_EDGE;
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GCannyEdgeDetection.HysteresisThresh : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();
		}
	}

	// �ش� ��ġ������ ���� Edge�� �����Ѵ�.
	// @ EdgeMap : Edge �̹���
	// @ nEdgeMap : Edge ��ġ
	// @ EdgeMag : 1�� ��̺� �̹���
	// @ nEdgeMag : 1�� ��̺� ��ġ
	// @ low_value : �ּ� �Ӱ谪
	// @
	// @ return : count ����� Edge ����
	private void FollowEdge(short[] EdgeMap, int nEdgeMap, int[] EdgeMag, int nEdgeMag, int low_value, int[] count) {
		int[] x = { 1, 1, 0, -1, -1, -1, 0, 1 };
		int[] y = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int stop_value = (int) ((double) (_lImageH + _lImageW) / 4.0);
		int i = 0;
		boolean b_end = false;
		boolean b_reset = true;

		// byte[] tmpMap_Ptry = null;
		// short[] tmpMag_Ptr = null;
		// byte[] curMap_Ptr = EdgeMap;
		// short[] curMag_Ptr = EdgeMag;
		int nTmpMap = 0;
		int nTmpMag = 0;
		int nCurMap = nEdgeMap;
		int nCurMag = nEdgeMag;

		try {
			while (!b_end) {
				count[0]++;
				if (count[0] > stop_value) {
					b_reset = false;

					for (i = 0; i < 8; i++) {
						nTmpMap = nCurMap - y[i] * _lImageW + x[i]; // tmpMap_Ptr = curMap_Ptr - y[i]*_lImageW + x[i];
						nTmpMag = nCurMag - y[i] * _lImageW + x[i]; // tmpMag_Ptr = curMag_Ptr - y[i]*_lImageW + x[i];

						if ((EdgeMap[nTmpMap] == _POSSIBLE_EDGE) && (EdgeMag[nTmpMag] > low_value)) { // if(
																										// ((*tmpMap_Ptr)==POSSIBLE_EDGE)
																										// &&
																										// ((*tmpMag_Ptr)>low_value)
																										// ) {
							EdgeMap[nTmpMap] = _EDGE;

							nCurMap = nTmpMap; // curMap_Ptr = tmpMap_Ptr;
							nCurMag = nTmpMag; // curMag_Ptr = tmpMag_Ptr;

							b_reset = true;
						}

						if (b_reset)
							break;
					}
					if (b_reset)
						continue;
				}
				b_end = true;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("GCannyEdgeDetection.FollowEdge : (IndexOutOfBoundsException) " + ex.toString());
			ex.printStackTrace();
		}
	}

	//
}
