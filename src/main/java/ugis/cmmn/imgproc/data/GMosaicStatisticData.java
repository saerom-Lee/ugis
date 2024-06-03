package ugis.cmmn.imgproc.data;

//������ũ ��� ���� Ŭ����
public class GMosaicStatisticData {

	// Maximum gray value
	public int _nMax = 0;

	// Minimum gray value
	public int _nMin = 0;

	// Maximum frequency value
	public int _nMode = 0;

	// Median gray value
	public int _nMedian = 0;

	// Mean of gray value
	public double _dblMean = 0;

	// Variance of gray value
	public double _dblVariance = 0;

	// Interval from Minimum gray value to Maximum gray value
	public int _nRange = 0;

	// Interval from Maximum frequency value to Mean of gray value
	public double _dblMeanMode = 0;

	// ������ũ ��� ������ �����Ѵ�.
	// @ data : ������ũ ��� ����
	public void Copy(GMosaicStatisticData data) {
		this._nMax = data._nMax;
		this._nMin = data._nMin;
		this._nMode = data._nMode;
		this._nMedian = data._nMedian;
		this._dblMean = data._dblMean;
		this._dblVariance = data._dblVariance;
		this._nRange = data._nRange;
		this._dblMeanMode = data._dblMeanMode;
	}

	// ������ ������ũ ��� ������ ��ȯ�Ѵ�.
	// @ return : GMosaicStatisticData ������ũ ��� ����
	public GMosaicStatisticData clone() {
		GMosaicStatisticData ret = new GMosaicStatisticData();
		ret.Copy(this);
		return ret;
	}
}
