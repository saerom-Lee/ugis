package ugis.cmmn.imgproc.data;

//������ũ ���� ���� Ŭ����
public class GMosaicProcData {

	// ������ũ �̹��� ���� Ŭ����
	public GMosaicImgData _imgData = new GMosaicImgData();

	// Scene path
	public int _nScenePath = 0;

	// Scene row
	public int _nSceneRow = 0;

	// �޸� ����
	public int _nDeviceIndex = 0; // 0: memory, 1: file(raw file)

	// ������ũ ���� ������ �����Ѵ�.
	// @ data : ������ũ ���� ����
	public void Copy(GMosaicProcData data) {
		this._imgData.Copy(data._imgData);
		this._nScenePath = data._nScenePath;
		this._nSceneRow = data._nSceneRow;
		this._nDeviceIndex = data._nDeviceIndex;
	}

	// ������ ������ũ ���� ������ ��ȯ�Ѵ�.
	// @ return : GMosaicProcData ������ũ ���� ����
	public GMosaicProcData clone() {
		GMosaicProcData ret = new GMosaicProcData();
		ret.Copy(this);
		return ret;
	}

}
