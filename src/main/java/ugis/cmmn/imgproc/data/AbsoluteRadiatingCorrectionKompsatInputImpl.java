package ugis.cmmn.imgproc.data;

//Kompsat ������ �����纸���� ���� �Ķ���� Ŭ����
public class AbsoluteRadiatingCorrectionKompsatInputImpl implements AbsoluteRadiatingCorrectionKompsatInput {

	// ���翡����(Radiance) ������ ���
	private double RadianceMultiple = 1.0;

	// ���翡����(Radiance) ������ ���
	private double RadianceAddtion = 0.0;

	@Override
	public void setRadianceMultiple(double mult) {
		// TODO Auto-generated method stub
		RadianceMultiple = mult;
	}

	@Override
	public double getRadianceMultiple() {
		// TODO Auto-generated method stub
		return RadianceMultiple;
	}

	@Override
	public void setRadianceAddtion(double add) {
		// TODO Auto-generated method stub
		RadianceAddtion = add;
	}

	@Override
	public double getRadianceAddtion() {
		// TODO Auto-generated method stub
		return RadianceAddtion;
	}

}
