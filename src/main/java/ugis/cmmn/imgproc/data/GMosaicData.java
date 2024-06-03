package ugis.cmmn.imgproc.data;

import java.util.ArrayList;

//������ũ ���� Ŭ����
public class GMosaicData {

	// �̹��� ���� ����
	public int _nNumOfFile = 0;

	// Path ����
	public int _nNumOfPath = 0;

	// Row ����
	public int _nNumOfRow = 0;

	// Main Master Path
	public int _nMainMasterPath = 0;

	// Main Master Rows
	public int _nMainMasterRow = 0;

	// Master ����
	public int _nNumOfMaster = 0;

	// ������ũ �̹��� �ػ�
	public double[] _dblPixelScales = { 0, 0 };

	// �̹��� ��Ī ��� ����
	public int _nGrayBand = 2; // R : 2, G : 1, B : 0, BandOdering : -1

	// �̹��� ��Ī ��� ���� ���
	public ArrayList<Integer> _nBandOderingArray = new ArrayList<Integer>(); // R : 2, G : 1, B : 0, length == 0 -> Gray

	// ������ũ ������ �����Ѵ�.
	// @ data : ������ũ ����
	public void Copy(GMosaicData data) {
		int size = 0;
		int i = 0;

		this._nNumOfFile = data._nNumOfFile;
		this._nNumOfPath = data._nNumOfPath;
		this._nNumOfRow = data._nNumOfRow;
		this._nMainMasterPath = data._nMainMasterPath;
		this._nMainMasterRow = data._nMainMasterRow;
		this._nNumOfMaster = data._nNumOfMaster;
		this._dblPixelScales[0] = data._dblPixelScales[0];
		this._dblPixelScales[1] = data._dblPixelScales[1];
		this._nGrayBand = data._nGrayBand;

		this._nBandOderingArray.clear();
		if (data._nBandOderingArray.size() > 0) {
			size = data._nBandOderingArray.size();

			for (i = 0; i < size; i++) {
				Integer oInt = new Integer(data._nBandOderingArray.get(i));
				this._nBandOderingArray.add(oInt);
			}
		}
	}

	// ������ ������ũ ������ ��ȯ�Ѵ�.
	// @ return : GMosaicData ������ũ ����
	public GMosaicData clone() {
		GMosaicData ret = new GMosaicData();
		ret.Copy(this);
		return ret;
	}

}
