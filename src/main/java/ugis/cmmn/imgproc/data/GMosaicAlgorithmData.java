package ugis.cmmn.imgproc.data;

import ugis.cmmn.imgproc.GImageEnhancement;
import ugis.cmmn.imgproc.mosaic.GAutoMosaic;
import ugis.cmmn.imgproc.mosaic.GSeamline;

//������ũ �˰��� ���� Ŭ����
public class GMosaicAlgorithmData {

	// Seamline ����
	public GSeamline.SeamlineMethod _nSeamLineMethod = GSeamline.SeamlineMethod.FEATURE_SELECTION_USING_EDGE;

	// Seamline ������ ���� ������ ũ��
	public int _lSearchWidth = 100;

	// Seam Point ���ϸ�
	public String _strSPointFileName = "";

	// Seame Point ���ϸ�
	public String _strSPointDataFileName = "";

	// Seam Point ����
	public String _strSeamPoint = "";

	// Seam Point ���� ����
	public boolean _bSaveSeamPoint = false;

	// ������׷� ��Ī ����
	public GImageEnhancement.HistorgramMatchingMethod _nHistogramMatchingMethod = GImageEnhancement.HistorgramMatchingMethod.HUE_ADJUSTMENT;

	// ������׷� ��Ī�� ���� Feather Width ���� ����
	public boolean _bFeatherWidth = false;

	// ������׷� ��Ī�� ���� Feather Width ũ��
	public int _lFeatherWidth = 10;

	// ������ũ ���μ��� ����
	public GAutoMosaic.MosaicProcMethod _nMosaicProcDirection = GAutoMosaic.MosaicProcMethod.VERTICAL;

	// ������ũ �˰��� ������ �����Ѵ�.
	// @ data : ������ũ �˰��� ����
	public void Copy(GMosaicAlgorithmData data) {
		this._nSeamLineMethod = data._nSeamLineMethod;
		this._lSearchWidth = data._lSearchWidth;
		this._strSPointFileName = data._strSPointFileName;
		this._strSPointDataFileName = data._strSPointDataFileName;
		this._strSeamPoint = data._strSeamPoint;
		this._bSaveSeamPoint = data._bSaveSeamPoint;
		this._nHistogramMatchingMethod = data._nHistogramMatchingMethod;
		this._bFeatherWidth = data._bFeatherWidth;
		this._lFeatherWidth = data._lFeatherWidth;
		this._nMosaicProcDirection = data._nMosaicProcDirection;
	}

	// ������ ������ũ �˰��� ������ ��ȯ�Ѵ�.
	// @ return : GMosaicAlgorithmData ������ũ �˰��� ����
	public GMosaicAlgorithmData clone() {
		GMosaicAlgorithmData ret = new GMosaicAlgorithmData();
		ret.Copy(this);
		return ret;
	}

}
