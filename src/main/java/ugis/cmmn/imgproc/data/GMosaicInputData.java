package ugis.cmmn.imgproc.data;

//������ũ �Է�  ���� Ŭ����
public class GMosaicInputData {
	
	//������ũ �̹��� ���
	public String[] _strMosaicImageArray = null;
	
	//Gray & Color ��� ����
	public int _nGrayBand = 0;						// R : 0, G : 1, B : 2, Color : -1
	
	//�̹��� ��Ī ��� ���� ���
	public short[] _nBandOderingArray = null;		// is null or length == 0 -> Gray
	
	//������ũ ��� ���� ���� ���
	public String _strOutFileName = "";

	//������ũ �Է�  ������ �����Ѵ�.
	//	@ data : ������ũ �Է�  ����
	public void Copy(GMosaicInputData data) {
		int size = 0;
		int i = 0;
		
		if(data._strMosaicImageArray != null) {
			size = data._strMosaicImageArray.length;
			
			this._strMosaicImageArray = new String[size];
			for(i=0; i<size; i++) {
				this._strMosaicImageArray[i] = data._strMosaicImageArray[i];
			}
		}

		this._nGrayBand = data._nGrayBand;
		
		if(data._nBandOderingArray != null) {
			size = data._nBandOderingArray.length;
			
			this._nBandOderingArray = new short[size];
			for(i=0; i<size; i++) {
				this._nBandOderingArray[i] = data._nBandOderingArray[i];
			}
		}
		
		this._strOutFileName = data._strOutFileName;
	}
	
	//������ ������ũ �Է�  ������ ��ȯ�Ѵ�.
	//	@ return : GMosaicInputData ������ũ �Է�  ����
	public GMosaicInputData clone() {
		GMosaicInputData ret = new GMosaicInputData();
		ret.Copy(this);
		return ret;
	}
}
