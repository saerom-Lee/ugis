package ugis.cmmn.imgproc.mosaic;



import org.locationtech.jts.geom.Coordinate;
//Sub ���� ���� Ŭ����
public class GSubArea {

	//Image type
	public int _nAreaIndex = 0;  

	//���� �߽� ��ǥ
	public Coordinate _posCenterUtm = new Coordinate();

	//���� ���� ��ǥ
	public Coordinate _posULUtm = new Coordinate(); 
	
	//Total number of row
	public int _lNumRow = 0;  
	
	//Total number of column
	public int _lNumCol = 0;  

	//�޸� ����
	public int _nDeviceIndex = 0;	// 0: memory, 1: file
	
	//���� ȭ�� ����
	public byte[] _pImage = null;
	
	//���� ���� ���
	public String _strFileName;
	
}
