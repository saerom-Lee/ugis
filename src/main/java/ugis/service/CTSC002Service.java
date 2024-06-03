package ugis.service;

import ugis.service.vo.CTPageVO;
import ugis.service.vo.CTSC001VO;
import ugis.service.vo.CTSC002VO;

import java.util.List;

public interface CTSC002Service<T> {

	T get(T t) throws Exception;
	List<T> search(CTPageVO<T> vo) throws Exception;
	void add(T vo) throws Exception;
	void upt(T vo) throws Exception;
	void del(T vo) throws Exception;
	long insert(T vo) throws Exception;

	List<CTSC002VO> ctsc002_search(CTSC002VO vo);

}
