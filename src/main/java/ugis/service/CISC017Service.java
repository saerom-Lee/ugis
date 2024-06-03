package ugis.service;

import java.util.List;

import ugis.service.vo.CISC017VO.ChangeDetctRsltListRes;
import ugis.service.vo.CISC017VO.ThChngeDetct;
import ugis.service.vo.CISC017VO.ThChngeDetctVidoResult;

public interface CISC017Service {

	Long saveThChngeDetct(ThChngeDetct dto) throws Exception;

	int updateThChngeDetctStreYn(ThChngeDetct dto) throws Exception;

	ChangeDetctRsltListRes selectThChngeDetctRsltList() throws Exception;

	List<ChangeDetctRsltListRes> selectThChngeDetctVectorList() throws Exception;

	int deleteVector(ThChngeDetctVidoResult dto) throws Exception;

	int updateVector(ThChngeDetctVidoResult dto) throws Exception;

//	ChangeDetctRsltListRes selectThChngeDetctRsltWorkList(Long vidoId);

	ChangeDetctRsltListRes selectThChngeDetctRsltWorkList();

}
