package ugis.cmmn.imgproc.data;

//������׷� ��Ī ���� Ŭ����
public class GHistogramMatchingData {

	// Match Mean & Std. Dev. (Master Mean)
	public double _dblMeanMaster = 0;

	// Match Mean & Std. Dev. (Slave Mean)
	public double _dblMeanSlave = 0;

	// Match Mean & Std. Dev. (Slave vs. Slave Variance)
	public double _dblVariance = 0;

	// Match Mean & Std. Dev. (Slave vs. Master Covariance)
	public double _dblCovariance = 0;

	// Match Cumulative Frequency (������ Look Up Table)
	public int[] _nLUTModified = null;

	// Hue Adjustment & Match Cumulative Frequency (Master Min)
	public int _nMinMaster = 0;

	// Hue Adjustment & Match Cumulative Frequency (Slave Min)
	public int _nMinSlave = 0;

	// Hue Adjustment & Match Cumulative Frequency (Master Max)
	public int _nMaxMaster = 0;

	// Hue Adjustment & Match Cumulative Frequency (Slave Max)
	public int _nMaxSlave = 0;

	// ������׷� ��Ī ������ �����Ѵ�.
	// @ data : ������׷� ��Ī ����
	public void Copy(GHistogramMatchingData data) {
		int size = 0;
		int i = 0;

		this._dblMeanMaster = data._dblMeanMaster;
		this._dblMeanSlave = data._dblMeanSlave;
		this._dblVariance = data._dblVariance;
		this._dblCovariance = data._dblCovariance;

		if (data._nLUTModified != null) {
			size = data._nLUTModified.length;

			this._nLUTModified = new int[size];
			for (i = 0; i < size; i++) {
				this._nLUTModified[i] = data._nLUTModified[i];
			}
		}

		this._nMinMaster = data._nMinMaster;
		this._nMinSlave = data._nMinSlave;
		this._nMaxMaster = data._nMaxMaster;
		this._nMaxSlave = data._nMaxSlave;
	}

	// ������ ������׷� ��Ī ������ ��ȯ�Ѵ�.
	// @ return : GHistogramMatchingInfo ������׷� ��Ī ����
	public GHistogramMatchingData clone() {
		GHistogramMatchingData ret = new GHistogramMatchingData();
		ret.Copy(this);
		return ret;
	}
}
