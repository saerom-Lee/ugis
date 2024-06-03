package ugis.service;

import java.util.HashMap;
import java.util.List;

import ugis.service.vo.CISC005VO;

public interface CISC005Service {


	//항공영상
	List<CISC005VO> selectAeroSatList(HashMap<String, Object> params);
	
	//위성영상 검색
	List<CISC005VO> selectSoilSatList(HashMap<String, Object> params);
	
	//기타위성
	List<CISC005VO> selectEtcSatList(HashMap<String, Object> params);
	

}
