package ugis.cmmn.imgproc.data;

import org.locationtech.jts.geom.Coordinate;

import ugis.cmmn.imgproc.mosaic.GAutoMosaic;

//Sub ���� ���� Ŭ����
public class GSubArea {

	// Image type
	public GAutoMosaic.AreaImageType _nAreaIndex = GAutoMosaic.AreaImageType.AREA_NONE; // Area Index-> 0 : None 1 :
																						// Master Image 2 : Slave Image

	// ���� �߽� ��ǥ
	public Coordinate _posCenterUtm = new Coordinate();

	// ���� ���� ��ǥ
	public Coordinate _posULUtm = new Coordinate();

	// Total number of row
	public int _lNumRow = 0;

	// Total number of column
	public int _lNumCol = 0;

	// �޸� ����
	public int _nDeviceIndex = 0; // 0: memory, 1: file

	// ���� ȭ�� ����
	public byte[] _pImage = null;

	// ���� ���� ���
	public String _strFileName = "";

}
