package ugis.service.vo;

import java.util.Date;

/**
 * @Class Name : CTSC002VO.java
 * @Description : CTSC002VO Class
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2021.10.14  			 클래스 생성 
 * @ 2021.10.19  			 CTSC004VO로 부터 상속 추가
 *
 * @author
 * @since 2021. 10.14
 * @version 1.0
 * @see
 *
 *  Copyright (C) by XXXXX All right reserved.
 */
public class CTSC002VO extends CTSC004VO {

	/**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = 292690629944358560L;

	private double ltcr_x;  //minx
	private double ltcr_y;	//miny
	private double rtcr_x; //maxx
	private double rtcr_y; //maxy


	public double getLtcr_x() {
		return ltcr_x;
	}

	public void setLtcr_x(double ltcr_x) {
		this.ltcr_x = ltcr_x;
	}

	public double getLtcr_y() {
		return ltcr_y;
	}

	public void setLtcr_y(double ltcr_y) {
		this.ltcr_y = ltcr_y;
	}

	public double getRtcr_x() {
		return rtcr_x;
	}

	public void setRtcr_x(double rtcr_x) {
		this.rtcr_x = rtcr_x;
	}

	public double getRtcr_y() {
		return rtcr_y;
	}

	public void setRtcr_y(double rtcr_y) {
		this.rtcr_y = rtcr_y;
	}
}
