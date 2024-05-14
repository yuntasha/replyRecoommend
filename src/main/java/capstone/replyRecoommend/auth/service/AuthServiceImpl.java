package capstone.replyRecoommend.auth.service;

import capstone.replyRecoommend.auth.domain.User;
import capstone.replyRecoommend.auth.dto.AuthRequestDTO;
import capstone.replyRecoommend.auth.dto.TokenMapper;
import capstone.replyRecoommend.auth.repository.UserRepository;
import capstone.replyRecoommend.security.JwtFactory;
import capstone.replyRecoommend.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtFactory jwtFactory;

    @Transactional
    public User findUser(Long id){
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.orElse(null);
    }

    @Transactional
    public TokenMapper loginUser(AuthRequestDTO.LoginDTO loginDTO){
        User user = loginOrRegisterUser(loginDTO);
        return jwtFactory.generateBothToken(user.getId());
    }

    private User loginOrRegisterUser(AuthRequestDTO.LoginDTO loginDTO){
        Optional<User> findUser = userRepository.findByEmail(loginDTO.getEmail());
        return findUser.orElseGet(() -> userRepository.save(User.builder()
                .name(loginDTO.getName())
                .email(loginDTO.getEmail())
                .build()));
    }

    public TokenMapper reissue(AuthRequestDTO.RefreshTokenDTO refreshTokenDTO){
        return jwtFactory.reissueToken(refreshTokenDTO.getRefreshToken());
    }
}