package ugis.service.mapper;

import java.util.List;

import ugis.service.vo.CTPageVO;

public interface CTSCBaseMapper<T> {

	T get(T vo) throws Exception;
	
	List<T> getList(CTPageVO<T> vo);
	
	long add(T vo) throws Exception;
	void upt(T vo) throws Exception;
	void del(T vo) throws Exception;
}
