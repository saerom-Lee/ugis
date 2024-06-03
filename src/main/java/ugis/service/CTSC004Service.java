package ugis.service;

import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.vo.CTSC004VO;

public interface CTSC004Service<T> {
	
	//이력조회
	List<EgovMap> selectTnHistList(CTSC004VO vo);
	//로그조회
	List<EgovMap> selectTnLogList(CTSC004VO vo);
	
	//메타조회
	List<EgovMap> selectTnMetaList(CTSC004VO vo);
}
