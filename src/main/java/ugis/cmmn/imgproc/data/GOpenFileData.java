package ugis.cmmn.imgproc.data;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

//Open ���� ���� Ŭ����
public class GOpenFileData {

	// ���� ���� : ���� ��� �� 16��Ʈ �ʴ� ��Ʈ ����
	public GFileData _oFileData = new GFileData();

	// ���� �̸�
	public String _strFileName = "";

	// ���� Ȯ����
	public String _strFileExt = "";

	// ���� ũ��
	public String _strFileSize = "";

	// �ػ�
	public double[] _dblPixelScales = { 0, 0 };

	// ��� ��
	public int _lBandNum = 0;

	// �̹��� ���뿵�� ����
	public Envelope2D _mbr2d = new Envelope2D();

	// �߽� ���� ��ǥ
	public Coordinate _dblUtmCenter = new Coordinate();

	// Path �ε���
	public int _nScenePath = 0; // Scene path

	// Row �ε���
	public int _nSceneRow = 0; // Scene row

	// ��ǥ�� ����
	public CoordinateReferenceSystem _Crs = null;

	// Open ���� ������ �����Ѵ�.
	// @ data : Open ���� ����
	public void Copy(GOpenFileData data) {
		this._oFileData.Copy(data._oFileData);

		this._strFileName = data._strFileName;
		this._strFileExt = data._strFileExt;
		this._strFileSize = data._strFileSize;

		this._dblPixelScales[0] = data._dblPixelScales[0];
		this._dblPixelScales[1] = data._dblPixelScales[1];

		this._lBandNum = data._lBandNum;

		// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
		this._mbr2d.setCoordinateReferenceSystem(data._mbr2d.getCoordinateReferenceSystem());
		this._mbr2d.setRect(data._mbr2d.getBounds2D());
		this._dblUtmCenter.setCoordinate(data._dblUtmCenter);

		this._nScenePath = data._nScenePath;
		this._nSceneRow = data._nSceneRow;

		if (data._Crs != null) {
			if (CRS.equalsIgnoreMetadata(data._Crs, AbstractGridFormat.getDefaultCRS())) {
				this._Crs = AbstractGridFormat.getDefaultCRS();
			} else {
				String strWkt = data._Crs.toWKT();
				try {
					this._Crs = CRS.parseWKT(strWkt);
				} catch (FactoryException fe) {
					System.out.println("GOpenFileData.Copy : " + fe.toString());
					fe.printStackTrace();
				}
			}
		}
	}

	// ������ Open ���� ������ ��ȯ�Ѵ�.
	// @ return : GOpenFileData Open ���� ����
	public GOpenFileData clone() {
		GOpenFileData ret = new GOpenFileData();
		ret.Copy(this);
		return ret;
	}
}
