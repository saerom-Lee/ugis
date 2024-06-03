package ugis.service;

import ugis.service.vo.CTPageVO;
import ugis.service.vo.CTSC001VO;
import ugis.service.vo.CTSC001VOInsertVO;

import java.util.List;

public interface CTSC001Service<T> {

	T get(T t) throws Exception;
	List<T> search(CTPageVO<T> vo) throws Exception;
	void add(T vo) throws Exception;
	void upt(CTSC001VO vo) throws Exception;
	void del(T vo) throws Exception;
	long insert(T vo) throws Exception;
	List<T> select(CTSC001VO vo) throws Exception;
	List<T> file_select(CTSC001VO vo) throws Exception;
	long vidoinsert(CTSC001VO vo) throws Exception;
	long vidoinsertMeta(CTSC001VO vo) throws Exception;
	long insertUsgsMetaAll(CTSC001VOInsertVO vo) throws Exception;
	long insertUsgsMetaEssential(CTSC001VOInsertVO vo) throws Exception;
	long insertUsgs(CTSC001VOInsertVO vo) throws Exception;
	long insertTnUsgsEssential(CTSC001VOInsertVO vo) throws Exception;
	int selectMaxVidoId() throws Exception;
}
