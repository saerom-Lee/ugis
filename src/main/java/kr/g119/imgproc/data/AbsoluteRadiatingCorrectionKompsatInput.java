package kr.g119.imgproc.data;

//Kompsat ������ �����纸���� ���� �Ķ���� �������̽�
public interface AbsoluteRadiatingCorrectionKompsatInput {

    //���翡����(Radiance) ������ ���
    void setRadianceMultiple(double mult);
    double getRadianceMultiple();
    
    //���翡����(Radiance) ������ ���
    void setRadianceAddtion(double add);
    double getRadianceAddtion();
}
