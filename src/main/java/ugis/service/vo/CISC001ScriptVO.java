package ugis.service.vo;

/**
 * @Class Name : CISC001ProjectLogVO.java
 * @Description : 프로젝트 로그 관리
 * @Modification Information
 * @
 * @  수정일 / 수정자 / 수정내용
 * @ -------------------------------------------------
 * @ 2021.11.xx / psh / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
public class CISC001ScriptVO {
	private String scriptId;
	private String scriptNm;
	private String workKind;
	private String satKind;	
	private String metaDataNm;    //메타데이터
	private String algorithmNm;   //알고리즘
	private String inputFileNm;   //입력, 기준영상
	private String inputFileNm2;  //입력2(모자이크용)
	private String inputFileNm3;  //입력3(모자이크용)
	private String inputFileNm4;  //입력4(모자이크용)
	private String outputFileNm;  //결과영상
	private String targetFileNm;  //대상 영상, (모자이크 기준영상)
	private String toaOutputFileNm;  //TOA 결과영상
	private String gain;           //radinace gain
	private String offset;         //radinace offset
	private String reflectGain;    //TOA gain
	private String reflectOffset;  //TOA offset
	private String redBand;       //RED BAND	
	private String greenBand;     //GREEN BAND
	private String blueBand;      //BLUE BAND
	private String controlType;      //조정방식
	private String autoAreaControl;  //자동영역 설정방법
	private String extrlFileCoursNm; //외부(실제)경로명
	private String histogramArea;
	private String regDt;	
	private String useYn;
	public String getScriptId() {
		return scriptId;
	}
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}	
	public String getScriptNm() {
		return scriptNm;
	}
	public void setScriptNm(String scriptNm) {
		this.scriptNm = scriptNm;
	}
	public String getWorkKind() {
		return workKind;
	}
	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}
	public String getSatKind() {
		return satKind;
	}
	public void setSatKind(String satKind) {
		this.satKind = satKind;
	}
	public String getMetaDataNm() {
		return metaDataNm;
	}
	public void setMetaDataNm(String metaDataNm) {
		this.metaDataNm = metaDataNm;
	}
	public String getAlgorithmNm() {
		return algorithmNm;
	}
	public void setAlgorithmNm(String algorithmNm) {
		this.algorithmNm = algorithmNm;
	}
	public String getInputFileNm() {
		return inputFileNm;
	}
	public void setInputFileNm(String inputFileNm) {
		this.inputFileNm = inputFileNm;
	}
	public String getOutputFileNm() {
		return outputFileNm;
	}
	public void setOutputFileNm(String outputFileNm) {
		this.outputFileNm = outputFileNm;
	}
	public String getTargetFileNm() {
		return targetFileNm;
	}
	public void setTargetFileNm(String targetFileNm) {
		this.targetFileNm = targetFileNm;
	}
	public String getToaOutputFileNm() {
		return toaOutputFileNm;
	}
	public void setToaOutputFileNm(String toaOutputFileNm) {
		this.toaOutputFileNm = toaOutputFileNm;
	}
	public String getGain() {
		return gain;
	}
	public void setGain(String gain) {
		this.gain = gain;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getReflectGain() {
		return reflectGain;
	}
	public void setReflectGain(String reflectGain) {
		this.reflectGain = reflectGain;
	}
	public String getReflectOffset() {
		return reflectOffset;
	}
	public void setReflectOffset(String reflectOffset) {
		this.reflectOffset = reflectOffset;
	}
	public String getRedBand() {
		return redBand;
	}
	public void setRedBand(String redBand) {
		this.redBand = redBand;
	}
	public String getGreenBand() {
		return greenBand;
	}
	public void setGreenBand(String greenBand) {
		this.greenBand = greenBand;
	}
	public String getBlueBand() {
		return blueBand;
	}
	public void setBlueBand(String blueBand) {
		this.blueBand = blueBand;
	}
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public String getAutoAreaControl() {
		return autoAreaControl;
	}
	public void setAutoAreaControl(String autoAreaControl) {
		this.autoAreaControl = autoAreaControl;
	}
	public String getExtrlFileCoursNm() {
		return extrlFileCoursNm;
	}
	public void setExtrlFileCoursNm(String extrlFileCoursNm) {
		this.extrlFileCoursNm = extrlFileCoursNm;
	}	
	public String getHistogramArea() {
		return histogramArea;
	}
	public void setHistogramArea(String histogramArea) {
		this.histogramArea = histogramArea;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getInputFileNm2() {
		return inputFileNm2;
	}
	public void setInputFileNm2(String inputFileNm2) {
		this.inputFileNm2 = inputFileNm2;
	}
	public String getInputFileNm3() {
		return inputFileNm3;
	}
	public void setInputFileNm3(String inputFileNm3) {
		this.inputFileNm3 = inputFileNm3;
	}
	public String getInputFileNm4() {
		return inputFileNm4;
	}
	public void setInputFileNm4(String inputFileNm4) {
		this.inputFileNm4 = inputFileNm4;
	}
	
	
	
}
