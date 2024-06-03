package ugis.service.mapper;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.vo.CTSC005VO;

@Mapper("ctsc005Mapper")
public interface CTSC005Mapper {

	List<EgovMap> selectYearStaticFree (HashMap<String, Object> params);
	
	List<EgovMap> selectYearStaticEmer (HashMap<String, Object> params);
	
	List<EgovMap> selectKindsStatic (HashMap<String, Object> params);
}
