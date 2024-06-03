package ugis.service.vo;

import lombok.Data;

/**
 * @Class Name : DataCodeVO.java
 * @Description : 긴급공간정보 생성
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
@Data
public class DataCodeVO {

	//데이터 코드
	private String dataCd;
	//1뎁스
	private String depthOne;
	//2뎁스
	private String depthTwo;
	
	
	public String getdataCd() {
		return dataCd;
	}

	public void setdataCd(String dataCd) {
		this.dataCd = dataCd;
	}

	
	public String getdepthOne() {
		return depthOne;
	}

	public void setdepthOne(String depthOne) {
		this.depthOne = depthOne;
	}

	
	public String getdepthTwo() {
		return depthTwo;
	}

	public void setdepthTwo(String depthTwo) {
		this.depthTwo = depthTwo;
	}


}
