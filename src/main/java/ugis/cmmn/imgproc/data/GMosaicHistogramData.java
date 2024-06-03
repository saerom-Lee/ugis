package ugis.cmmn.imgproc.data;

//������ũ ������׷� ���� Ŭ����
public class GMosaicHistogramData {
	
	//�޸� ����
	public int _nDeviceIndex = 0;	// 0: memory, 1: file(raw file)

	//���� ȭ�� ����
	public byte[] _pImage = null;

	//���� ���� ���� : ���� ��� �� 16��Ʈ �ʴ� ��Ʈ ����
	public GFileData _oFileData = new GFileData();				//String _strFileName	���� ���� ���

	//���� ���� ũ��
	public int _lWidth = 0;
	
	//���� ���� ũ�� = 0
	public int _lHeight = 0;

	//������׷� ��Ī ����
	public GHistogramMatchingData _histMatchInfo = new GHistogramMatchingData();
	
	//������ũ ������׷� ������ �����Ѵ�.
	//	@ data : ������ũ ������׷� ����
	public void Copy(GMosaicHistogramData data) {
		int size = 0;
		int i = 0;
		
		this._nDeviceIndex = data._nDeviceIndex;

		if(data._pImage != null) {
			size = data._pImage.length;
			
			this._pImage = new byte[size];
			for(i=0; i<size; i++) {
				this._pImage[i] = data._pImage[i];
			}
		}

		this._oFileData.Copy(data._oFileData); 
		
		this._lWidth = data._lWidth;
		this._lHeight = data._lHeight;

		this._histMatchInfo.Copy(data._histMatchInfo);
	}
	
	//������ ������ũ ������׷� ������ ��ȯ�Ѵ�.
	//	@ return : GMosaicHistogramData ������ũ ������׷� ����
	public GMosaicHistogramData clone() {
		GMosaicHistogramData ret = new GMosaicHistogramData();
		ret.Copy(this);
		return ret;
	}
	
}
