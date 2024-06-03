package ugis.service.impl;

import java.util.List;

import ugis.service.CTSCBaseService;
import ugis.service.mapper.CTSCBaseMapper;
import ugis.service.vo.CTPageVO;
import ugis.service.vo.CTSC001VO;

public abstract class CTSCBaseServiceImpl<T> implements CTSCBaseService<T>{

	public CTSCBaseServiceImpl() {}
	
	public abstract CTSCBaseMapper<T> getMapper();
	
	@Override
	public T get(T vo) throws Exception {
		return getMapper().get(vo);
	}

	@Override
	public List<T> search(CTPageVO<T> vo) throws Exception {
		return getMapper().getList(vo);
	}

	@Override
	public void add(T vo) throws Exception {
		getMapper().add(vo);

	}

	@Override
	public void upt(T vo) throws Exception {
		getMapper().upt(vo);
	}

	@Override
	public void del(T vo) throws Exception {
		getMapper().del(vo);
	}
}
