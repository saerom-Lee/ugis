package ugis.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.mapper.CMSC001Mapper;
import ugis.service.vo.CMSC001VO;

/**
 * @Class Name : CMSC001ServiceImpl.java
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
@Service
public class CMSC001Service extends EgovAbstractServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CMSC001Service.class);

	@Resource(name = "cmsc001Mapper")
	private CMSC001Mapper cmsc001Mapper;

	public CMSC001VO getUserById(String userId, String pwd) {

		try {
			CMSC001VO user = cmsc001Mapper.getUserById(userId);

			if (user != null) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				if (encoder.matches(pwd, user.getPassword())) {
					return user;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public Long signUpUser(CMSC001VO user) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));

		return cmsc001Mapper.signUpUser(user);
	}

	public void updateUserPwd(String userId, String pwd, String afterPwd) {
		try {
			CMSC001VO user = cmsc001Mapper.getUserById(userId);
			if (user != null) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				if (encoder.matches(pwd, user.getPassword())) {
					CMSC001VO updateUser = new CMSC001VO();
					updateUser.setId(userId);
					updateUser.setPassword(encoder.encode(afterPwd));
					cmsc001Mapper.updateUserPwd(updateUser);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CMSC001VO user = cmsc001Mapper.getUserById(username);
		if (user != null) {
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new User(user.getId(), user.getPassword(), authorities);
		} else {
			return null;
		}
	}

}
