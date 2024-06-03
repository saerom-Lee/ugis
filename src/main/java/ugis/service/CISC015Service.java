package ugis.service;

import java.util.List;

import ugis.service.vo.CISC015VO.ObjChangeListRes;
import ugis.service.vo.CISC015VO.ObjChangeRsltListRes;
import ugis.service.vo.CISC015VO.ObjChangeSearch;
import ugis.service.vo.CISC015VO.TnAiModelAnals;
import ugis.service.vo.CISC015VO.TnAiModelAnalsVidoMapngResult;

public interface CISC015Service {

	List<ObjChangeListRes> selectSateList(ObjChangeSearch dto) throws Exception;

	Long saveAiModelAnals(TnAiModelAnals dto) throws Exception;

	int updateAiModelAnalsStreYn(TnAiModelAnals dto) throws Exception;

	int updateVector(TnAiModelAnalsVidoMapngResult dto) throws Exception;

	ObjChangeRsltListRes selectAiModelAnalsRsltList() throws Exception;

	ObjChangeRsltListRes selectAiModelAnalsRsltWorkList() throws Exception;

	List<ObjChangeRsltListRes> selectAiModelAnalsVectorList() throws Exception;

	int deleteVector(TnAiModelAnalsVidoMapngResult dto) throws Exception;

	List<ObjChangeListRes> selectUsgsWorkList(ObjChangeSearch dto);

}
