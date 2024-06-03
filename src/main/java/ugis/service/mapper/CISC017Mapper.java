package ugis.service.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CISC017VO.ChangeDetctRsltListRes;
import ugis.service.vo.CISC017VO.ThChngeDetct;
import ugis.service.vo.CISC017VO.ThChngeDetctVidoResult;

@Mapper("cisc017Mapper")
public interface CISC017Mapper {

	Long saveThChngeDetct(ThChngeDetct dto);

	int updateThChngeDetctStreYn(ThChngeDetct dto);

	void saveThChngeDetctResult(ThChngeDetctVidoResult dto);

	List<ChangeDetctRsltListRes> selectThChngeDetctList();

	List<ChangeDetctRsltListRes> selectThChngeDetctRsltList();

	List<ChangeDetctRsltListRes> selectThChngeDetctVectorList();

	int deleteVector(ThChngeDetctVidoResult dto);

	// ChangeDetctRsltListRes selectThChngeDetctRsltWorkList(Long vidoId);

	List<ChangeDetctRsltListRes> selectThChngeDetctRsltWorkList();

}
