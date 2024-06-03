package ugis.cmmn.imgproc.data;

//Canny Edge ���� Ŭ����
public class GCannyEdgeData {

	// 1�� ��̺� �̹���
	public int[] _GradMag = null;

	// Edge Image
	public short[] _EdgeImage = null;

	// Canny Edge ������ �����Ѵ�.
	// @ data : Canny Edge ����
	public void Copy(GCannyEdgeData data) {
		int size = 0;
		int i = 0;

		if (data._GradMag != null) {
			size = data._GradMag.length;

			this._GradMag = new int[size];
			for (i = 0; i < size; i++) {
				this._GradMag[i] = data._GradMag[i];
			}
		}

		if (data._EdgeImage != null) {
			size = data._EdgeImage.length;

			this._EdgeImage = new short[size];
			for (i = 0; i < size; i++) {
				this._EdgeImage[i] = data._EdgeImage[i];
			}
		}
	}

	// ������ Canny Edge ������ ��ȯ�Ѵ�.
	// @ return : GCannyEdgeInfo Canny Edge ����
	public GCannyEdgeData clone() {
		GCannyEdgeData ret = new GCannyEdgeData();
		ret.Copy(this);
		return ret;
	}
}
