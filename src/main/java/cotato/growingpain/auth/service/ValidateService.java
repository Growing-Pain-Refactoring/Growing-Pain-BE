package cotato.growingpain.auth.service;

import cotato.growingpain.common.exception.AppException;
import cotato.growingpain.common.exception.ErrorCode;
import cotato.growingpain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateService {

    private final MemberRepository memberRepository;

    public void checkDuplicateNickName(String name) {
        if (isDuplicateNickname(name)) {
            log.error("[회원 가입 실패]: 존재하는 닉네임 " + name);
            throw new AppException(ErrorCode.NICKNAME_DUPLICATED);
        }
    }

    public boolean isDuplicateNickname(String nickName) {
        return memberRepository.findByName(nickName).isPresent();
    }
}
