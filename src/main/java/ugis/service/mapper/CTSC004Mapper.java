package ugis.service.mapper;

import ugis.service.vo.CTSC004VO;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("ctsc004Mapper")
public interface CTSC004Mapper {

	//무료 영상 이력검색
	List<EgovMap> selectTnHistList(CTSC004VO vo);
	//무료 영상 로그 조회
	List<EgovMap> selectTnLogFree(CTSC004VO vo);
	//긴급 영상 로그 조회
	List<EgovMap> selectTnLogEmer(CTSC004VO vo);
	//무료영상 메타조회
	List<EgovMap> selectTnMetaFree(CTSC004VO vo);
	//긴급영상 메타조회
	List<EgovMap> selectTnMetaEmer(CTSC004VO vo);
	
	
}
