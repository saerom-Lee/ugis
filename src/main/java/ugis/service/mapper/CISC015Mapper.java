package ugis.service.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.vo.CISC015VO.ObjChangeListRes;
import ugis.service.vo.CISC015VO.ObjChangeRsltListRes;
import ugis.service.vo.CISC015VO.ObjChangeSearch;
import ugis.service.vo.CISC015VO.TnAiModelAnals;
import ugis.service.vo.CISC015VO.TnAiModelAnalsVidoMapngResult;

@Mapper("cisc015Mapper")
public interface CISC015Mapper {

	List<ObjChangeListRes> selectSateList(ObjChangeSearch dto);

	Long saveAiModelAnals(TnAiModelAnals dto);

	int updateAiModelAnalsStreYn(TnAiModelAnals dto);

	void saveAiModelAnalsResult(TnAiModelAnalsVidoMapngResult dto);

	List<ObjChangeRsltListRes> selectAiModelAnalsList();

	List<ObjChangeRsltListRes> selectAiModelAnalsRsltList();

	List<ObjChangeRsltListRes> selectAiModelAnalsVectorList();

	int deleteVector(TnAiModelAnalsVidoMapngResult dto);

	List<EgovMap> selectAirList(ObjChangeSearch dto);

	List<ObjChangeListRes> selectUsgsWorkList(ObjChangeSearch dto);

	List<ObjChangeRsltListRes> selectAiModelAnalsRsltWorkList();

}
