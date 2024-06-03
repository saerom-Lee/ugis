package ugis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ugis.service.CTSC001Service;
import ugis.service.mapper.CTSC001Mapper;
import ugis.service.mapper.CTSCBaseMapper;
import ugis.service.vo.CTSC001VO;
import ugis.service.vo.CTSC001VOInsertVO;

import java.util.List;

@Service("ctsc001Service")
public class CTSC001ServiceImpl extends CTSCBaseServiceImpl<CTSC001VO> implements CTSC001Service<CTSC001VO> {

	@Resource(name = "ctsc001Mapper")
	CTSC001Mapper mapper;

	@Override
	public CTSCBaseMapper<CTSC001VO> getMapper() {
		return mapper;
	}

	public void add(CTSC001VO vo) throws Exception {
		CTSC001VO result = transform(vo);
//		super.add(vo);
	}

	public long insert(CTSC001VO vo) throws Exception {
		long intcd = mapper.usgsInsert(vo);

		return intcd;
	}

	@Override
	public List<CTSC001VO> select(CTSC001VO vo) throws Exception {
		List<CTSC001VO> select = mapper.select(vo);
		return select;
	}

	@Override
	public List<CTSC001VO> file_select(CTSC001VO vo) throws Exception {
		List<CTSC001VO> select = mapper.file_select(vo);
		return select;
	}

	public CTSC001VO transform(CTSC001VO vo) {
		// CTSC001VO result = new CTSC001VO();
		String temp = mapper.transform(vo);
		temp = temp.replace("LINESTRING(", "").replace(")", "");
		// LINESTRING(127.02108667361966 37.29477832678067,127.02835230681802
		// 37.298487175894735)
		String[] points = temp.split(",");
		String[] ul = points[0].split(" ");
		String[] lr = points[1].split(" ");
		String ulx = ul[0].trim();
		String uly = ul[1].trim();
		String lrx = lr[0].trim();
		String lry = lr[1].trim();
		vo.setUlx(Double.parseDouble(ulx));
		vo.setUly(Double.parseDouble(uly));
		vo.setLrx(Double.parseDouble(lrx));
		vo.setLry(Double.parseDouble(lry));
		return vo;
	}

	// usgs 수집 영상 insert
	public long vidoinsert(CTSC001VO vo) throws Exception {
		long intcd = mapper.insert(vo);

		return intcd;
	}

	@Override
	public long vidoinsertMeta(CTSC001VO vo) throws Exception {

		long intcd = mapper.vidoinsertMeta(vo);
		return intcd;
	}

	@Override
	public long insertUsgsMetaAll(CTSC001VOInsertVO vo) throws  Exception {
		long intcd = mapper.insertUsgsMetaAll(vo);
		return intcd;
	}

	@Override
	public long insertUsgsMetaEssential(CTSC001VOInsertVO vo) throws Exception {
		long intcd = mapper.insertUsgsMetaEssential(vo);
		return intcd;
	}
	@Override
	public long insertUsgs(CTSC001VOInsertVO vo) throws Exception {
		long intcd = mapper.insertTnUsgs(vo);
		return intcd;
	}

	@Override
	public long insertTnUsgsEssential(CTSC001VOInsertVO vo) throws Exception {
		long intcd = mapper.insertTnUsgsEssential(vo);
		return intcd;
	}
	@Override
	public int selectMaxVidoId() throws Exception {
		int maxId = mapper.selectMaxVidoId();
		return maxId;
	}
}
