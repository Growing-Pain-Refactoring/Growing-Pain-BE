package cotato.growingpain.infrastructure.converter;

import cotato.growingpain.domain.entity.Member;
import cotato.growingpain.dto.MemberInfoDto;

public class MemberConverter {

    public static MemberInfoDto toMemberInfoDto(Member member) {
        return new MemberInfoDto(member.getEmail(), member.getName());
    }
}
