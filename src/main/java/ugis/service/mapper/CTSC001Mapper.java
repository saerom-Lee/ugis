package ugis.service.mapper;

import ugis.service.vo.CISC010VO;
import ugis.service.vo.CTSC001VO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CTSC001VOInsertVO;

import java.util.HashMap;
import java.util.List;

@Mapper("ctsc001Mapper")
public interface CTSC001Mapper extends CTSCBaseMapper<CTSC001VO> {
	String transform(CTSC001VO vo);
	// 등록된 입력 자료를 가져온다.
	Long usgsInsert(CTSC001VO vo);
	//등록된 자동다운로드 목록을 가져온다.
	List<CTSC001VO> select(CTSC001VO vo);
	List<CTSC001VO> file_select(CTSC001VO vo);
	Long insert(CTSC001VO vo);
    long vidoinsertMeta(CTSC001VO vo);
	Long insertUsgsMetaAll(CTSC001VOInsertVO vo);
	Long insertUsgsMetaEssential(CTSC001VOInsertVO vo);

	Long insertTnUsgs(CTSC001VOInsertVO vo);
	Long insertTnUsgsEssential(CTSC001VOInsertVO vo);
	int selectMaxVidoId();
}
