package ugis.cmmn.imgproc.data;

//������ũ ��� ���� Ŭ����
public class GMosaicResultData {

	// �޸� ����
	public int _nDeviceIndex = 0; // 0: memory, 1: file(raw file)

	// ������ũ �̹��� ���� ���� : ���� ��� �� 16��Ʈ �ʴ� ��Ʈ ����
	public GFileData _oFileData = new GFileData(); // String _strLayerName ������ũ �̹��� ���� ���

	// Scene path
	public int _nScenePath = 0;

	// Scene row
	public int _nSceneRow = 0;

	// ������ũ �̹��� ���� Ŭ����
	public GMosaicImgData _imgData = new GMosaicImgData();

	// R��� ������׷� ����
	public int[] _lHistogramR = new int[256];

	// G��� ������׷� ����
	public int[] _lHistogramG = new int[256];

	// B��� ������׷� ����
	public int[] _lHistogramB = new int[256];

	// ������ũ ��� ���� Ŭ���� (Gray)
	public GMosaicStatisticData _statData = new GMosaicStatisticData();

	// ������ũ ��� ���� Ŭ���� (Red)
	public GMosaicStatisticData _statRData = new GMosaicStatisticData();

	// ������ũ ��� ���� Ŭ���� (Green)
	public GMosaicStatisticData _statGData = new GMosaicStatisticData();

	// ������ũ ��� ���� Ŭ���� (Blue)
	public GMosaicStatisticData _statBData = new GMosaicStatisticData();

	// Similarity
	public double _dblSimilarity = 0;

	// ���� ����ġ
	public int _nRefWeight = 0;

	// ���� ����
	public int _nLogicOrder = 0; // (0: slave, 1: order of master)

	// ������ ����
	public boolean _bCheckMaster = true; // (TRUE: master, FALSE: slave)

	// ������ ����
	public boolean _bCheckData = true; // (TRUE: exist, FALSE: don't exist)

	// ��� ����
	public boolean _bUseChecking = true; // (TRUE: use, FALSE: don't use)

	// ������ Band ��
	public int _nCC = 0;

	// Pixel�� Byte �� (1 : 8bit, 2 : 16bit)
	public int _nBpp = 0;

	// ��庰 �ּ� ȭ�Ұ� (R : 2, G : 1, B : 0)
	public int[] _pMn = new int[3];

	// ��庰 �ִ� ȭ�Ұ� (R : 2, G : 1, B : 0)
	public int[] _pMx = new int[3];

	// ������ũ ��� ������ �����ϴ�.
	// @ data : ������ũ ��� ����
	public void Copy(GMosaicResultData data) {
		int i = 0;

		this._nDeviceIndex = data._nDeviceIndex;

		this._oFileData.Copy(data._oFileData);

		this._nScenePath = data._nScenePath;
		this._nSceneRow = data._nSceneRow;

		this._imgData.Copy(data._imgData);

		for (i = 0; i < 256; i++) {
			this._lHistogramR[i] = data._lHistogramR[i];
			this._lHistogramG[i] = data._lHistogramG[i];
			this._lHistogramB[i] = data._lHistogramB[i];
		}

		this._statData.Copy(data._statData);
		this._statRData.Copy(data._statRData);
		this._statGData.Copy(data._statGData);
		this._statBData.Copy(data._statBData);

		this._dblSimilarity = data._dblSimilarity;
		this._nRefWeight = data._nRefWeight;
		this._nLogicOrder = data._nLogicOrder;
		this._bCheckMaster = data._bCheckMaster;
		this._bCheckData = data._bCheckData;
		this._bUseChecking = data._bUseChecking;
		this._nCC = data._nCC;
		this._nBpp = data._nBpp;

		for (i = 0; i < 3; i++) {
			this._pMn[i] = data._pMn[i];
			this._pMx[i] = data._pMx[i];
		}
	}

	// ������ ������ũ ��� ������ ��ȯ�Ѵ�.
	// @ return : GMosaicResultData ������ũ ��� ����
	public GMosaicResultData clone() {
		GMosaicResultData ret = new GMosaicResultData();
		ret.Copy(this);
		return ret;
	}
}
