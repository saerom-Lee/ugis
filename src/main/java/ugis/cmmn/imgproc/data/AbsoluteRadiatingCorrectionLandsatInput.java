package ugis.cmmn.imgproc.data;

//Landsat ������ �����纸���� ���� �Ķ���� �������̽�
public interface AbsoluteRadiatingCorrectionLandsatInput {

	// TOA Reflectance(Atsensor reflectance) ������ ���
	void setReflectanceMultiple(double mult);

	double getReflectanceMultiple();

	// TOA Reflectance(Atsensor reflectance) ������ ���
	void setReflectanceAddtion(double add);

	double getReflectanceAddtion();

	// ���翡����(Radiance) ������ ���
	void setRadianceMultiple(double mult);

	double getRadianceMultiple();

	// ���翡����(Radiance) ������ ���
	void setRadianceAddtion(double add);

	double getRadianceAddtion();
}
