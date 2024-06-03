package ugis.service;

import java.util.List;

import ugis.service.vo.CTPageVO;

public interface CTSCBaseService<T> {

	T get(T t) throws Exception;
	List<T> search(CTPageVO<T> vo) throws Exception;
	void add(T vo) throws Exception;
	void upt(T vo) throws Exception;
	void del(T vo) throws Exception;
	long insert(T vo) throws Exception;



}
