package ugis.cmmn.imgproc.data;

//Landsat ������ �����纸���� ���� �Ķ���� Ŭ����
public class AbsoluteRadiatingCorrectionLandsatInputImpl implements AbsoluteRadiatingCorrectionLandsatInput {

	// TOA Reflectance(Atsensor reflectance) ������ ���
	private double ReflectanceMultiple = 1.0;

	// TOA Reflectance(Atsensor reflectance) ������ ���
	private double ReflectanceAddtion = 0.0;

	// ���翡����(Radiance) ������ ���
	private double RadianceMultiple = 1.0;

	// ���翡����(Radiance) ������ ���
	private double RadianceAddtion = 0.0;

	@Override
	public void setReflectanceMultiple(double mult) {
		// TODO Auto-generated method stub
		ReflectanceMultiple = mult;
	}

	@Override
	public double getReflectanceMultiple() {
		// TODO Auto-generated method stub
		return ReflectanceMultiple;
	}

	@Override
	public void setReflectanceAddtion(double add) {
		// TODO Auto-generated method stub
		ReflectanceAddtion = add;
	}

	@Override
	public double getReflectanceAddtion() {
		// TODO Auto-generated method stub
		return ReflectanceAddtion;
	}

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
