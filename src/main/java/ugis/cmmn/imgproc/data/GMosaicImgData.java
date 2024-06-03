package ugis.cmmn.imgproc.data;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

//������ũ �̹��� ���� Ŭ����
public class GMosaicImgData {

	// ���� ���� ���� ���� : ���� ��� �� 16��Ʈ �ʴ� ��Ʈ ����
	public GFileData _oFileData = new GFileData(); // String _strFileName ���� ���� ���� ���

	// ������ũ �̹��� ���뿵�� ����
	public Envelope2D _mbr2d = new Envelope2D();

	// ������ũ �̹��� ���󿵿� ����
	public GridEnvelope2D _imgBox2d = new GridEnvelope2D();

	// ������ũ �̹��� ȭ�Ұ�
	public byte[] _pImage = null;

	// ������ũ �̹��� ����
	public int _lBandNum = 0; // Number of Band

	// ������ũ �̹��� �ػ�
	public double[] _dblPixelScales = { 0, 0 };

	// ������ũ �̹��� ���� �̹��� ��ǥ
	public Coordinate _lImgUL = new Coordinate();

	// ��ǥ�� ����
	public CoordinateReferenceSystem _Crs = null;

	// ������ũ �̹��� ������ �����Ѵ�.
	// @ data : ������ũ �̹��� ����
	public void Copy(GMosaicImgData data) {
		int size = 0;
		int i = 0;

		this._oFileData.Copy(data._oFileData);

		// @todo : Envelope2D.SetRect - Rectangle2D�� x, y�� LB
		this._mbr2d.setCoordinateReferenceSystem(data._mbr2d.getCoordinateReferenceSystem());
		this._mbr2d.setRect(data._mbr2d.getBounds2D());
		this._imgBox2d.setRect(data._imgBox2d.getBounds2D());

		if (data._pImage != null) {
			size = data._pImage.length;

			this._pImage = new byte[size];
			for (i = 0; i < size; i++) {
				this._pImage[i] = data._pImage[i];
			}
		}

		this._lBandNum = data._lBandNum;
		this._dblPixelScales[0] = data._dblPixelScales[0];
		this._dblPixelScales[1] = data._dblPixelScales[1];
		this._lImgUL.x = data._lImgUL.x;
		this._lImgUL.y = data._lImgUL.y;

		if (data._Crs != null) {
			if (CRS.equalsIgnoreMetadata(data._Crs, AbstractGridFormat.getDefaultCRS())) {
				this._Crs = AbstractGridFormat.getDefaultCRS();
			} else {
				String strWkt = data._Crs.toWKT();
				try {
					this._Crs = CRS.parseWKT(strWkt);
				} catch (FactoryException fe) {
					System.out.println("GMosaicImgData.Copy : " + fe.toString());
					fe.printStackTrace();
				}
			}
		}
	}

	// ������ ������ũ �̹��� ������ ��ȯ�Ѵ�.
	// @ return : GMosaicImgData ������ũ �̹��� ����
	public GMosaicImgData clone() {
		GMosaicImgData ret = new GMosaicImgData();
		ret.Copy(this);
		return ret;
	}
}
