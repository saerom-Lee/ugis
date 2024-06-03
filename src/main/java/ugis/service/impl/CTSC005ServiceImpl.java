package ugis.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import lombok.RequiredArgsConstructor;
import ugis.service.CTSC005Service;
import ugis.service.CTSCBaseService;
import ugis.service.mapper.CTSC005Mapper;
import ugis.service.mapper.CTSCBaseMapper;
import ugis.service.vo.CTSC005VO;

@RequiredArgsConstructor
@Service("ctsc005Service")
public class CTSC005ServiceImpl implements CTSC005Service<CTSC005VO>{

		private final CTSC005Mapper ctsc005Mapper;

		/**
		 *  연도별 통계 조회 무료영상
		 */
		@Override
		public List<EgovMap> selectYearStaticFree(HashMap<String, Object> params) {
			// TODO Auto-generated method stub
			return ctsc005Mapper.selectYearStaticFree(params);
		}

		@Override
		public List<EgovMap> selectYearStaticEmer(HashMap<String, Object> params) {
			// TODO Auto-generated method stub
			return ctsc005Mapper.selectYearStaticEmer(params);
		}

		@Override
		public List<EgovMap> selectKindsStatic(HashMap<String, Object> params) {
			// TODO Auto-generated method stub
			return ctsc005Mapper.selectKindsStatic(params);
		}
}
