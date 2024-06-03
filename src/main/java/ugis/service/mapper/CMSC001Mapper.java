package ugis.service.mapper;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CMSC001VO;

/**
 * @Class Name : CMSC001Mapper.java
 * @Description : 로그인
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
@Mapper("cmsc001Mapper")
public interface CMSC001Mapper {

	public Long signUpUser(CMSC001VO user);

	public CMSC001VO getUserById(String userId);

	public void updateUserPwd(CMSC001VO updateUser);

}
