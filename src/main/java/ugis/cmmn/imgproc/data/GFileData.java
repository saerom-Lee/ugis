package ugis.cmmn.imgproc.data;

import ugis.cmmn.imgproc.GTiffDataReader;

//���� ���� Ŭ����
public class GFileData {

	// ���� ���
	public String _strFilePath = "";

	// 16��Ʈ �ִ� ��Ʈ ����
	public GTiffDataReader.BIT16ToBIT8 _maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

	// ���� ���� ���� (First Master)
	public boolean _isReferenced = false;

	// ���� ������ �����Ѵ�.
	// @ data : ���� ����
	public void Copy(GFileData data) {
		this._strFilePath = data._strFilePath;
		this._maxBit16 = data._maxBit16;
		this._isReferenced = data._isReferenced;
	}

	// ������ ���� ������ ��ȯ�Ѵ�.
	// @ return : GFileData ���� ����
	public GFileData clone() {
		GFileData ret = new GFileData();
		ret.Copy(this);
		return ret;
	}
}
